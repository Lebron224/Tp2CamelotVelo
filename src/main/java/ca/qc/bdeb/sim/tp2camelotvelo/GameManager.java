package ca.qc.bdeb.sim.tp2camelotvelo;

import ca.qc.bdeb.sim.tp2camelotvelo.Decor.ArrierePlan;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Journal;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Maison;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Particule;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Input;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.UtilitairesDessins;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class GameManager {
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext gc;
    private Group root;

    private Camelot camelot;
    private Niveau niveauActuel;
    private Camera camera;
    private ArrierePlan arrierePlan;
    private ArrayList<Journal> journauxActifs = new ArrayList<>();
    private ImageView iconeJournal = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/icone-journal.png"));
    private ImageView iconeDollar = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/icone-dollar.png"));
    private ImageView iconeMaison = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/icone-maison.png"));


    private double tempsChargement = 0;
    private int argent = 0;
    private int numNiveau = 1;
    private boolean modeDebug = false;
    private boolean afficherChampElec = false;
    private boolean enChargement = false;
    private boolean niveauTermine = false;
    private boolean partieTermine = false;

    private static final double TEMPS_CHARGEMENT_NIVEAU = 3.0;

    public GameManager(Scene scene, Group root) {
        this.scene = scene;
        this.root = root;
        this.canvas = new Canvas(MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.root.getChildren().add(canvas);

        initialiserJeu();
        demarrerAnimation();
    }

    private void initialiserJeu(){
        this.camelot = new Camelot();

        // Initialiser la caméra
        this.camera = new Camera();

        // Initialiser l'arrière-plan
        this.arrierePlan = new ArrierePlan();

        commencerNiveau(numNiveau);

        configurerControles();
    }

    private void commencerNiveau(int numNiveau){
        this.niveauActuel = new Niveau(numNiveau);

        camelot.setPosition(new Point2D(0, MainJavaFX.HEIGHT - camelot.getImgView().getFitHeight()));

        ajouterObjetsRoot();
    }

    private void ajouterObjetsRoot(){

        root.getChildren().removeIf(node -> !(node instanceof Canvas));

        arrierePlan.ajouterArrierePlan(root);

        root.getChildren().add(camelot.getImgView());

        for (Maison maison : niveauActuel.getMaisons()) {
            root.getChildren().add(maison.getImgView());
            root.getChildren().add(maison.getBoite().getImgView());
            for (var fenetre : maison.getFenetres()) {
                root.getChildren().add(fenetre.getImgView());
            }
        }

        // Ajouter les particules
        if (!niveauActuel.getParticules().isEmpty()) {
            for (Particule particule : niveauActuel.getParticules()) {
                root.getChildren().add(particule.getImgView());
            }
        }
    }

    private void configurerControles(){

        scene.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        }));

        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        // Controles Debug
        if (Input.isKeyPressed(KeyCode.Q)) camelot.ajouterJournaux(10);
        if (Input.isKeyPressed(KeyCode.K)) camelot.setNbrJournaux(0);
        if (Input.isKeyPressed(KeyCode.L)) terminerNiveau();
        if (Input.isKeyPressed(KeyCode.D)) modeDebug = !modeDebug;
        if (Input.isKeyPressed(KeyCode.F)) afficherChampElec = !afficherChampElec;
        if (Input.isKeyPressed(KeyCode.I)) niveauActuel.creerParticulesTest();

    }

    private void gererLancements(){
        if (Input.isKeyPressed(KeyCode.Z)) lancerJournalHaut();
        if (Input.isKeyPressed(KeyCode.X)) lancerJournalAvant();
    }

    private void lancerJournalHaut(){
        var journal = camelot.lancerHaut();

        if (journal != null){
            journauxActifs.add(journal);
            root.getChildren().add(journal.getImgView());
        }
    }

    private void lancerJournalAvant(){
        var journal = camelot.lancerAvant();

        if (journal != null){
            journauxActifs.add(journal);
            root.getChildren().add(journal.getImgView());
        }
    }

    private void demarrerAnimation(){
        var timer = new AnimationTimer(){
            private long dernierTemps = System.nanoTime();
            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;

                update(deltaTemps);
                draw(deltaTemps);

                dernierTemps = temps;

            }
        };
        timer.start();
    }

    private void update(double detltaTemps){

        //Gerer Fin de partie
        if (partieTermine){
            gererFinPartie(detltaTemps);
            return;
        }

        // Gerer chargement Niveau
        if (enChargement){
            gererChargementNiveau(detltaTemps);
            return;
        }

        gererLancements();

        camelot.update(detltaTemps);

        camera.update(camelot.getPosition());

        arrierePlan.updateAvecCamera(camera.getPositionCamera().getX());

        mettreAJourJournaux(detltaTemps);

        verificationCollisions();


        if (niveauActuel.estTermine(camelot.getPosition())){
            terminerNiveau();
        }

        // Vérifier si la partie est terminée
        if (camelot.getNbrJournaux() <= 0) {
            terminerPartie();
        }
    }

    private void mettreAJourJournaux(double detltaTemps){
        var champElectrique = Point2D.ZERO;
        for (var j : journauxActifs) {
            if (numNiveau >= 2) {
                champElectrique = niveauActuel.champElectriqueTousParticule(j.getPosition());

                j.updateAvecChampElectrique(detltaTemps, camera, champElectrique);

            } else {
                j.update(detltaTemps,  camera);
            }
            j.draw(detltaTemps, camera);

            if (j.estASupprimer()){
                supprimerJournal(j);
            }
        }
    }

    private void verificationCollisions(){
        for (var j : journauxActifs) {
            for (var m : niveauActuel.getMaisons()){
                var boite = m.getBoite();
                var fen = m.getFenetres();

                if (j.collision(boite)){
                    supprimerJournal(j);
                    boite.toucher();
                }

                for (var f : fen){
                    if (j.collision(f)){
                        supprimerJournal(j);
                        f.toucher();
                    }
                }
            }
        }
    }

    private void gererChargementNiveau(double detltaTemps){
        tempsChargement += detltaTemps;
        if (tempsChargement >= TEMPS_CHARGEMENT_NIVEAU) {
            recommencerNiveau(numNiveau);
        }
    }

    private void gererFinPartie(double detltaTemps){
        tempsChargement += detltaTemps;
        if (tempsChargement >= TEMPS_CHARGEMENT_NIVEAU) {
            recommencerPartie();
        }
    }

    private void terminerNiveau(){
        enChargement = true;
        tempsChargement = 0;
        numNiveau++;
    }

    private void terminerPartie(){
        partieTermine = true;
        tempsChargement = 0;
    }

    private void recommencerPartie(){
        numNiveau = 1;
        argent = 0;
        camelot.resetJournal();
        partieTermine = false;
        journauxActifs.clear();
        commencerNiveau(numNiveau);
    }

    private void recommencerNiveau(int numNiveau){
        enChargement = false;
        commencerNiveau(numNiveau);
    }

    private void supprimerJournal(Journal journal){
        root.getChildren().remove(journal.getImgView());
        journauxActifs.remove(journal);
        camelot.retirerJournaux(1);
    }

    private void draw(double deltaTemps){
        gc.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        if (partieTermine){
            dessinerEcranFin();
            return;
        }

        if (enChargement){
            dessinerEcranChargement();
            return;
        }

        dessinerUI();

        drawObjects(deltaTemps);

        if (modeDebug) dessinerModeDebug();

        if (afficherChampElec) dessinerChampElec();
    }

    private void drawObjects(double deltaTemps) {
        for (var briques : arrierePlan.getGrilleBriques())
            for (var brique : briques)
                brique.draw(deltaTemps, camera);

        for (var m : niveauActuel.getMaisons())
            m.draw(deltaTemps, camera);

        camelot.draw(deltaTemps,  camera);

        for (var j : journauxActifs) j.draw(deltaTemps, camera);

        if (numNiveau >= 2){
            for (var p : niveauActuel.getParticules())
                p.draw(deltaTemps, camera);
        }
    }

    private void dessinerEcranChargement(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        gc.setFill(Color.GREEN);
        gc.setFont(Font.font(48));
        gc.fillText(
                "Niveau "+ numNiveau,
                MainJavaFX.WIDTH / 2 - 100,
                MainJavaFX.HEIGHT / 2
        );
    }

    private void dessinerEcranFin(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        gc.setFill(Color.RED);
        gc.setFont(Font.font(40));
        gc.fillText(
                "Rupture de stocks",
                MainJavaFX.WIDTH / 2 - 100,
                MainJavaFX.HEIGHT / 2 - 100
        );
        gc.setFill(Color.GREEN);
        gc.setFont(Font.font(48));
        gc.fillText(
                "Argent collecté : " + argent + "$",
                MainJavaFX.WIDTH /  2 - 150,
                MainJavaFX.HEIGHT / 2
        );
    }

    private void dessinerUI(){
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, MainJavaFX.WIDTH, 50);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(16));

        // Journaux restants
        gc.fillText(" " + camelot.getNbrJournaux(), 50, 20);

        // Argent
        gc.fillText(" " + argent + "$", 150, 20);

        StringBuilder adresse = new StringBuilder();
        for (var m : niveauActuel.getMaisonsAbonnees()){
            adresse.append(m.getAdresse()).append(" ");
        }
        gc.fillText(adresse.toString(), 225, 20);

        setIcones();

    }

    private void setIcones(){
        iconeJournal.setFitWidth(36);
        iconeJournal.setFitHeight(32);
        iconeJournal.setX(10); iconeJournal.setY(10);

        iconeDollar.setFitWidth(45);
        iconeDollar.setFitHeight(26);
        iconeDollar.setX(100);  iconeDollar.setY(10);

        iconeMaison.setFitWidth(33);
        iconeMaison.setFitHeight(33);
        iconeMaison.setX(185);  iconeMaison.setY(10);

    }

    private void dessinerModeDebug(){
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        for (var j : journauxActifs){
            j.dessinerCollision(gc);
        }

        for (var m : niveauActuel.getMaisons()){
            m.getBoite().dessinerCollision(gc);

            for(var f : m.getFenetres()){
                f.dessinerCollision(gc);
            }
        }
    }

    private void dessinerChampElec(){
        for (double x = 0; x < niveauActuel.getLargeurNiveau(); x += 50) {
            for (double y = 0; y < MainJavaFX.HEIGHT; y += 50) {
                var positionMonde = new Point2D(x, y);
                var positionEcran = camera.coordoEcran(positionMonde);

                if (camera.estVisible(positionEcran)) {
                    var force = niveauActuel.champElectriqueTousParticule(positionMonde);
                    UtilitairesDessins.dessinerVecteurForce(
                            positionEcran,
                            force,
                            gc
                    );
                }
            }
        }
    }
}
