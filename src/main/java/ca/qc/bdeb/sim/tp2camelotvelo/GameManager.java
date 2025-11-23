package ca.qc.bdeb.sim.tp2camelotvelo;

import ca.qc.bdeb.sim.tp2camelotvelo.Decor.ArrierePlan;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Journal;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Maison;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Particule;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Input;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.InputCooldown;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.UtilitairesDessins;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Iterator;

public class GameManager {
    private final Scene scene;
    private final GraphicsContext gc;
    private final Pane root;

    private Camelot camelot;
    private Niveau niveauActuel;
    private Camera camera;
    private ArrierePlan arrierePlan;
    private Canvas canvas;
    private final ArrayList<Journal> journauxActifs = new ArrayList<>();
    private final ImageView iconeJournal = new ImageView(new Image("icone-journal.png"));
    private final ImageView iconeDollar = new ImageView(new Image("icone-dollar.png"));
    private final ImageView iconeMaison = new ImageView(new Image("icone-maison.png"));


    InputCooldown inputCooldown = new InputCooldown();
    private double tempsChargement = 0;
    private int argent = 0;
    private int numNiveau = 1;
    private boolean modeDebug = false;
    private boolean afficherChampElec = false;
    private boolean enChargement = false;
    private boolean partieTermine = false;

    private static final double TEMPS_CHARGEMENT_NIVEAU = 3.0;
    private static final double TEMPS_CHARGEMENT_KEYs = 0.5;

    public GameManager(Scene scene, Pane root) {
        this.scene = scene;
        this.root = root;
        canvas = new Canvas(MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.root.getChildren().add(canvas);


        configurerControles();
        initialiserJeu();
        demarrerAnimation();
    }

    private void initialiserJeu(){
        this.camelot = new Camelot();

        // Initialiser la caméra
        this.camera = new Camera();

        commencerPartie();

    }

    private void commencerNiveau(int niveau){

        if (enChargement || partieTermine) return;

        // Initialiser l'arrière-plan
        this.arrierePlan = new ArrierePlan(root);

        if (this.numNiveau != 1) camelot.ajouterJournaux(12);

        this.niveauActuel = new Niveau(niveau);

        camelot.setPosition(new Point2D(0, MainJavaFX.HEIGHT - camelot.getImgView().getFitHeight()));

        ajouterObjetsRoot();
    }

    private void ajouterObjetsRoot(){

        root.getChildren().removeIf(node ->
                node != canvas  &&
                        node != iconeJournal &&
                        node != iconeDollar &&
                        node != iconeMaison
        );

        arrierePlan.ajouterArrierePlan();

        root.getChildren().add(camelot.getImgView());

        for (Maison maison : niveauActuel.getMaisons()) {
            root.getChildren().add(maison.getImgView());
            root.getChildren().add(maison.getBoite().getImgView());
            for (var fenetre : maison.getFenetres()) {
                root.getChildren().add(fenetre.getImgView());
            }
        }

        root.getChildren().add(iconeJournal);
        root.getChildren().add(iconeDollar);
        root.getChildren().add(iconeMaison);

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


        inputCooldown.setCooldown(KeyCode.Q, 0.5);
        inputCooldown.setCooldown(KeyCode.K, 0.5);
        inputCooldown.setCooldown(KeyCode.L, 1);
        inputCooldown.setCooldown(KeyCode.D, 0.3);
        inputCooldown.setCooldown(KeyCode.F, 0.3);
        inputCooldown.setCooldown(KeyCode.I, 1);
        inputCooldown.setCooldown(KeyCode.Z, 0.5);
        inputCooldown.setCooldown(KeyCode.X, 0.5);
    }

    private void controles(InputCooldown inputCooldown) {
        // Controles Debug
        if (inputCooldown.tryPress(KeyCode.Q)) camelot.ajouterJournaux(10);
        if (inputCooldown.tryPress(KeyCode.K)) camelot.setNbrJournaux(0);
        if (inputCooldown.tryPress(KeyCode.L)) terminerNiveau();
        if (inputCooldown.tryPress(KeyCode.D)) modeDebug = !modeDebug;
        if (inputCooldown.tryPress(KeyCode.F)) afficherChampElec = !afficherChampElec;
        if (inputCooldown.tryPress(KeyCode.I)) niveauActuel.creerParticulesTest();
        if (camelot.peutLancerJournal()){
            if (inputCooldown.tryPress(KeyCode.Z)){
                lancerJournalHaut();
                camelot.retirerJournaux(1);
            } else if (inputCooldown.tryPress(KeyCode.X)){
                lancerJournalAvant();
                camelot.retirerJournaux(1);
            }
        }
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

        controles(inputCooldown);

        camelot.update(detltaTemps);

        camera.update(camelot.getPosition());

        arrierePlan.updateAvecCamera(camera);

        mettreAJourJournaux(detltaTemps);

        verificationCollisions();


        if (niveauActuel.estTermine(camelot.getPosition())){
            terminerNiveau();
        }

        // Vérifier si la partie est terminée
        if (camelot.getNbrJournaux() == 0 && niveauActuel.estTermine(camelot.getPosition())) {
            terminerPartie();
        }
    }

    private void mettreAJourJournaux(double deltaTemps) {

        var it = journauxActifs.iterator();

        while (it.hasNext()) {
            var j = it.next();

            // Update selon le niveau
            if (numNiveau >= 2) {
                var champElectrique = niveauActuel.champElectriqueTousParticule(j.getPosition());
                j.updateAvecChampElectrique(deltaTemps, camera, champElectrique);
            } else {
                j.update(deltaTemps, camera);
            }

            // Dessin
            j.draw(deltaTemps, camera);

            // Suppression via l'iterator (important!)
            if (j.estASupprimer()) {
                supprimerJournalViaIterator(it, j);  // 🔥 suppression sécurisée
            }
        }
    }


    private void verificationCollisions() {

        var it = journauxActifs.iterator();

        while (it.hasNext()) {
            var j = it.next();

            boolean touché = false;

            for (var m : niveauActuel.getMaisons()) {

                var boite = m.getBoite();

                // Collision avec la boîte aux lettres
                if (j.collision(boite) && !boite.isDejaTouchee()) {
                    boite.toucher(this);
                    supprimerJournalViaIterator(it, j);
                    touché = true;
                    break; // important : on arrête après suppression
                }

                // Collision avec les fenêtres
                for (var f : m.getFenetres()) {
                    if (j.collision(f) && !f.isDejaTouchee()) {
                        f.toucher(this);
                        supprimerJournalViaIterator(it, j);
                        touché = true;
                        break;
                    }
                }

                if (touché) break;
            }
        }
    }

    private void supprimerJournalViaIterator(Iterator<Journal> it, Journal j) {
        root.getChildren().remove(j.getImgView());
        it.remove();   // 🔥 suppression sécurisée pendant l'itération
    }



    private void gererChargementNiveau(double detltaTemps){
        tempsChargement += detltaTemps;
        if (tempsChargement >= TEMPS_CHARGEMENT_NIVEAU) {
            enChargement = false;
            commencerNiveau(numNiveau);
        }
    }

    private void gererFinPartie(double detltaTemps){
        tempsChargement += detltaTemps;
        if (tempsChargement >= TEMPS_CHARGEMENT_NIVEAU) {
            enChargement = false;
            commencerPartie();
        }
    }

    private void terminerNiveau(){

        enChargement = true;
        tempsChargement = 0;
        numNiveau++;
    }

    private void terminerPartie() {
        partieTermine = true;
        tempsChargement = 0;
    }

    private void commencerPartie(){
        enChargement = true;

        numNiveau = 1;
        argent = 0;
        camelot.resetJournal();
        partieTermine = false;
        journauxActifs.clear();
        commencerNiveau(numNiveau);
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


        for (var m : niveauActuel.getMaisons())
            m.draw(deltaTemps, camera);

        camelot.draw(deltaTemps,  camera);

        for (var j : journauxActifs) j.draw(deltaTemps, camera);

        if (numNiveau >= 2){
            for (var p : niveauActuel.getParticules())
                p.draw(deltaTemps, camera);
        }

        arrierePlan.draw(camera);
    }

    private void dessinerEcranChargement(){
        root.getChildren().removeIf(node -> !(node instanceof Canvas));

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
        root.getChildren().removeIf(node -> !(node instanceof Canvas));


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
        gc.fillRect(0, 0, MainJavaFX.WIDTH, 25);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(17));

        // Journaux restants
        gc.fillText(" " + camelot.getNbrJournaux(), 40, 20);

        // Argent
        gc.fillText(" " + argent + "$", 140, 20);

        StringBuilder adresse = new StringBuilder();
        for (var m : niveauActuel.getMaisonsAbonnees()){
            adresse.append(m.getAdresse()).append(" ");
        }
        gc.fillText(adresse.toString(), 210, 20);

        setIcones();

    }

    private void setIcones(){
        iconeJournal.setPreserveRatio(true);
        iconeJournal.setFitHeight(20);
        iconeJournal.setX(10); iconeJournal.setY(5);

        iconeDollar.setPreserveRatio(true);
        iconeDollar.setFitHeight(20);
        iconeDollar.setX(100);  iconeDollar.setY(5);

        iconeMaison.setPreserveRatio(true);
        iconeMaison.setFitHeight(20);
        iconeMaison.setX(185);  iconeMaison.setY(5);

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

    public void ajouterArgent(int quantite){
        this.argent += quantite;
    }

    public void retirerArgent(int quantite){
        this.argent -= quantite;
    }
}
