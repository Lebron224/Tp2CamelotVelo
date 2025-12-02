package ca.qc.bdeb.sim.tp2camelotvelo;

import ca.qc.bdeb.sim.tp2camelotvelo.Decor.ArrierePlan;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Journal;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Maison;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Input;
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


/**
 * Classe principale qui gère la logique du jeu.

 * - Gestion du Camelot, journaux, maisons et particules
 * - Gestion des collisions et de la physique
 * - Gestion de l'affichage via un Canvas
 * - Gestion des niveaux, du score et des écrans de transition
 * - Synchronisé avec la boucle d'animation (AnimationTimer)
 */
public class GameManager {

    /** Référence à la scène JavaFX affichant le jeu. */
    private final Scene scene;

    /** Contexte graphique utilisé pour dessiner sur le Canvas principal. */
    private final GraphicsContext gc;

    /** Conteneur racine de la fenêtre JavaFX (inclut le Canvas et les éléments visuels). */
    private final Pane root;


    /** Objet principal contrôlé par le joueur (le personnage Camelot). */
    private Camelot camelot;

    /** Niveau actuellement en cours d’exécution. */
    private Niveau niveauActuel;

    /** Caméra utilisée pour ajuster l’affichage en fonction de la position du joueur. */
    private Camera camera;

    /** Arrière-plan graphique du niveau. */
    private ArrierePlan arrierePlan;

    /** Canvas principal sur lequel tous les éléments du jeu sont dessinés. */
    private final Canvas canvas;

    /** Liste des journaux actifs actuellement présents dans le monde du jeu. */
    private final ArrayList<Journal> journauxActifs = new ArrayList<>();


    /** Icône affichée dans l’interface pour représenter un journal. */
    private final ImageView iconeJournal = new ImageView(new Image("icone-journal.png"));

    /** Icône affichée dans l’interface pour représenter l’argent. */
    private final ImageView iconeDollar = new ImageView(new Image("icone-dollar.png"));

    /** Icône affichée dans l’interface pour représenter une maison (objectif). */
    private final ImageView iconeMaison = new ImageView(new Image("icone-maison.png"));


    /** Temps écoulé lors du chargement d’un niveau ou de la partie. */
    private double tempsChargement = 0;

    /** Argent total accumulé par le joueur. */
    private int argent = 0;

    /** Numéro du niveau actuellement joué. */
    private int numNiveau = 1;

    /** Active ou désactive le mode debug (affichage d'informations supplémentaires). */
    private boolean modeDebug = false;

    /** Indique si le champ électrique doit être affiché en mode debug. */
    private boolean afficherChampElec = false;

    /** Indique s'il faut creer des particules test ou non (Mode debug) */
    private boolean creerParticulesTest = false;

    /** Indique si un niveau est en cours de chargement. */
    private boolean enChargement = false;

    /** Indique si la partie est terminée. */
    private boolean partieTermine = false;


    /** Temps d’attente avant le début d’un niveau (en secondes). */
    private final double TEMPS_CHARGEMENT_NIVEAU = 3.0;

    /**
     * Constructeur du GameManager.
     * Initialise la scène, le canvas, les contrôles, puis lance la boucle d'animation.
     *
     * @param scene La scène principale du jeu
     * @param root  Le conteneur principal dans lequel sont ajoutés les éléments graphiques
     */
    public GameManager(Scene scene, Pane root) {

        // Initialization de la scene principale
        this.scene = scene;

        // Initialization du root principale
        this.root = root;

        // Création du canvas avec dimensions définies
        canvas = new Canvas(MainJavaFX.WIDTH, MainJavaFX.HEIGHT);

        // Contexte graphique associé au canvas
        this.gc = canvas.getGraphicsContext2D();

        // Ajout du canvas dans la racine
        this.root.getChildren().add(canvas);

        // Configure les entrées clavier
        configurerControles();

        // Initialise les objets du jeu
        initialiserJeu();

        // Démarre la boucle d'animation
        demarrerAnimation();
    }

    /**
     * Initialise la partie.
     * <p>
     * - Création du Camelot
     * - Initialisation de la caméra
     * - Début de la partie (niveau 1)
     */
    private void initialiserJeu() {

        // Création du camelot
        this.camelot = new Camelot();

        // Initialisation de la caméra
        this.camera = new Camera();

        // Commencer la partie (niveau 1)
        commencerPartie();
    }

    /**
     * Démarre une nouvelle partie en réinitialisant les variables globales
     * et en lançant le premier niveau.
     */
    private void commencerPartie() {
        enChargement = true;
        tempsChargement = 0;

        numNiveau = 1;          // Réinitialise le niveau
        argent = 0;             // Remet l'argent à zéro
        camelot.resetJournal(); // Réinitialise le nombre de journaux
        partieTermine = false;  // La partie recommence
        journauxActifs.clear(); // Supprime tous les journaux existants

        commencerNiveau(numNiveau); // Lance le niveau 1
    }

    /**
     * Démarre un niveau spécifique, initialise l’arrière-plan et les objets du monde.
     *
     * @param niveau numéro du niveau à commencer
     */
    private void commencerNiveau(int niveau) {

        if (enChargement || partieTermine) return; // Empêche de commencer si déjà en chargement

        // Initialiser l'arrière-plan
        this.arrierePlan = new ArrierePlan(root);

        // Ajoute des journaux supplémentaires à partir du niveau 2
        if (this.numNiveau != 1) camelot.ajouterJournaux(12);

        // Charge le niveau
        this.niveauActuel = new Niveau(niveau);

        // Replace Camelot au point de départ
        camelot.setPosition(new Point2D(0, MainJavaFX.HEIGHT - camelot.getImgView().getFitHeight()));

        // Charge les objets du niveau dans le root
        ajouterObjetsRoot();
    }

    /**
     * Ajoute les objets du niveau dans le conteneur root,
     * tout en supprimant les anciens éléments sauf ceux essentiels (Canvas + icônes).
     */
    private void ajouterObjetsRoot() {

        // Nettoyage des éléments présents sauf le Canvas et les icônes UI
        root.getChildren().removeIf(node ->
                node != canvas &&
                        node != iconeJournal &&
                        node != iconeDollar &&
                        node != iconeMaison
        );

        // Ajout de l'arrière-plan
        arrierePlan.ajouterArrierePlan();

        // Ajoute Camelot
        root.getChildren().add(camelot.getImgView());

        // Ajoute les maisons et leurs sous-éléments
        for (Maison maison : niveauActuel.getMaisons()) {
            root.getChildren().add(maison.getImgView());
            root.getChildren().add(maison.getBoite().getImgView());
            root.getChildren().add(maison.getAdresseTexte());

            for (var fenetre : maison.getFenetres()) {
                root.getChildren().add(fenetre.getImgView());
            }
        }

        // Ajout des icônes HUD
        root.getChildren().add(iconeJournal);
        root.getChildren().add(iconeDollar);
        root.getChildren().add(iconeMaison);

    }

    /**
     * Configure les contrôles clavier et initialise-les cooldowns des touches.
     */
    private void configurerControles() {

        // Appui sur une touche
        scene.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit(); // Quitte le jeu
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        }));

        // Relâchement d'une touche
        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        // Définition des cooldowns pour chaque touche
        Input.setCooldown(KeyCode.Q, 0.5);
        Input.setCooldown(KeyCode.K, 0.5);
        Input.setCooldown(KeyCode.L, 1);
        Input.setCooldown(KeyCode.D, 0.3);
        Input.setCooldown(KeyCode.F, 0.3);
        Input.setCooldown(KeyCode.I, 1);
    }

    /**
     * Gère toutes les actions liées aux contrôles clavier,
     * incluant les actions debug et le lancer de journaux.
     */
    private void controles() {

        // Contrôles Debug
        if (Input.tryPress(KeyCode.Q)) camelot.ajouterJournaux(10);
        if (Input.tryPress(KeyCode.K)) camelot.setNbrJournaux(0);
        if (Input.tryPress(KeyCode.L)) terminerNiveau();
        if (Input.tryPress(KeyCode.D)) modeDebug = !modeDebug;
        if (Input.tryPress(KeyCode.F)) afficherChampElec = !afficherChampElec;

        if (Input.tryPress(KeyCode.I)){
            creerParticulesTest = !creerParticulesTest;

            if (creerParticulesTest) niveauActuel.creerParticulesTest();
            else niveauActuel.creerParticules();
        }

        // Lancer les journaux si Camelot peut tirer
        if (camelot.peutLancerJournal()) {
            if (Input.isKeyPressed(KeyCode.Z)) {
                lancerJournalHaut();      // Tir vers le haut
                camelot.retirerJournaux(1);

                camelot.setCooldown(0);
            } else if (Input.isKeyPressed(KeyCode.X)) {
                lancerJournalAvant();    // Tir droit devant
                camelot.retirerJournaux(1);

                camelot.setCooldown(0);
            }
        }
    }

    /**
     * Fait lancer un journal verticalement par Camelot
     * et l’ajoute au monde s'il est valide.
     */
    private void lancerJournalHaut() {
        var journal = camelot.lancerHaut(); // Calcul du lancer

        if (journal != null) {
            journauxActifs.add(journal);     // Ajout à la liste
            root.getChildren().add(journal.getImgView()); // Ajout visuel
        }
    }

    /**
     * Fait lancer un journal horizontalement par Camelot
     * et l’ajoute dans la scène si le tir est valide.
     */
    private void lancerJournalAvant() {
        var journal = camelot.lancerAvant(); // Calcul du lancer

        if (journal != null) {
            journauxActifs.add(journal);
            root.getChildren().add(journal.getImgView());
        }
    }


    /**
     * Démarre l'animation principale du jeu en utilisant un AnimationTimer.
     * Chaque frame appelle update() et draw() avec le temps écoulé.
     */
    private void demarrerAnimation() {
        var timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9; // Convertit en secondes

                update(deltaTemps); // Met à jour la logique du jeu
                draw(deltaTemps);   // Dessine le jeu

                dernierTemps = temps; // Met à jour le temps précédent
            }
        };
        timer.start(); // Démarre l'animation
    }

    /**
     * Met à jour l'état du jeu pour chaque frame.
     *
     * @param detltaTemps temps écoulé depuis la dernière frame (en secondes)
     */
    private void update(double detltaTemps) {

        // Gérer la fin de partie
        if (partieTermine) {
            gererFinPartie(detltaTemps);
            return;
        }

        // Gérer le chargement du niveau
        if (enChargement) {
            gererChargementNiveau(detltaTemps);
            return;
        }

        controles();        // Gérer les entrées du joueur

        camelot.update(detltaTemps);     // Met à jour le personnage principal
        camera.update(camelot.getPosition()); // Met à jour la caméra
        arrierePlan.updateAvecCamera(camera); // Met à jour l'arrière-plan

        mettreAJourJournaux(detltaTemps); // Met à jour tous les journaux

        verificationCollisions(); // Vérifie les collisions avec les maisons

        // Vérifie si le niveau est terminé
        if (niveauActuel.estTermine(camelot.getPosition())) {
            terminerNiveau();
        }

        // Vérifie si la partie doit se terminer
        if (camelot.getNbrJournaux() == 0 && journauxActifs.isEmpty()) {
            terminerPartie();
        }
    }

    /**
     * Met à jour tous les journaux actifs, gère leur mouvement,
     * leurs interactions avec le champ électrique et supprime ceux qui doivent l'être.
     *
     * @param deltaTemps temps écoulé depuis la dernière frame (en secondes)
     */
    private void mettreAJourJournaux(double deltaTemps) {

        var it = journauxActifs.iterator();

        while (it.hasNext()) {
            var j = it.next();

            // Update selon le niveau et le champ électrique
            if (numNiveau >= 2) {
                var champElectrique = niveauActuel.champElectriqueTousParticule(j.getPosition());
                j.updateAvecChampElectrique(deltaTemps, camera, champElectrique);
            } else {
                j.update(deltaTemps, camera);
            }

            j.draw(camera); // Dessin du journal

            // Suppression sécurisée via l'iterator
            if (j.estASupprimer()) {
                supprimerJournalViaIterator(it, j);
            }
        }
    }

    /**
     * Vérifie les collisions des journaux avec les boîtes et fenêtres des maisons.
     */
    private void verificationCollisions() {

        var it = journauxActifs.iterator();

        while (it.hasNext()) {
            var j = it.next();

            boolean touche = false;

            for (var m : niveauActuel.getMaisons()) {

                var boite = m.getBoite();

                // Collision avec la boîte aux lettres
                if (j.collision(boite) && !boite.isDejaTouchee()) {
                    boite.toucher(this);
                    supprimerJournalViaIterator(it, j);
                    break; // Stop après suppression
                }

                // Collision avec les fenêtres
                for (var f : m.getFenetres()) {
                    if (j.collision(f) && !f.isDejaTouchee()) {
                        f.toucher(this);
                        supprimerJournalViaIterator(it, j);
                        touche = true;
                        break;
                    }
                }

                if (touche) break;
            }
        }
    }

    /**
     * Supprime un journal de la scène et de la liste via l'itérateur.
     *
     * @param it itérateur de la liste de journaux
     * @param j  journal à supprimer
     */
    private void supprimerJournalViaIterator(Iterator<Journal> it, Journal j) {
        root.getChildren().remove(j.getImgView()); // Retire de l'affichage
        it.remove(); // Supprime de la liste de manière sécurisée
    }

    /**
     * Gère le chargement d'un niveau avant son démarrage effectif.
     *
     * @param detltaTemps temps écoulé depuis la dernière frame
     */
    private void gererChargementNiveau(double detltaTemps) {
        tempsChargement += detltaTemps;
        if (tempsChargement >= TEMPS_CHARGEMENT_NIVEAU) {
            enChargement = false;
            commencerNiveau(numNiveau); // Commence le niveau
        }
    }

    /**
     * Gère la fin de partie avant de redémarrer le jeu.
     *
     * @param detltaTemps temps écoulé depuis la dernière frame
     */
    private void gererFinPartie(double detltaTemps) {
        tempsChargement += detltaTemps;
        if (tempsChargement >= TEMPS_CHARGEMENT_NIVEAU) {
            enChargement = false;
            commencerPartie(); // Redémarre la partie
        }
    }

    /**
     * Termine le niveau actuel et prépare le suivant.
     */
    private void terminerNiveau() {
        enChargement = true; // Bloque les entrées pendant le changement
        tempsChargement = 0;
        numNiveau++;          // Passe au niveau suivant
    }

    /**
     * Termine la partie en cours.
     */
    private void terminerPartie() {
        partieTermine = true; // Marque la partie comme terminée
        tempsChargement = 0;
    }


    /**
     * Dessine l'ensemble du jeu pour chaque frame.
     *
     * @param deltaTemps temps écoulé depuis la dernière frame
     */
    private void draw(double deltaTemps) {
        gc.clearRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT); // Nettoie l'écran

        if (partieTermine) {
            dessinerEcranFin(); // Affiche l'écran de fin
            return;
        }

        if (enChargement) {
            dessinerEcranChargement(); // Affiche l'écran de chargement
            return;
        }

        dessinerUI();          // Dessine l'interface utilisateur
        drawObjects(deltaTemps); // Dessine tous les objets du jeu

        if (modeDebug) dessinerModeDebug(); // Affiche les infos debug
        if (afficherChampElec) dessinerChampElec(); // Affiche le champ électrique
    }

    /**
     * Dessine tous les objets du niveau, y compris maisons, Camelot,
     * journaux, particules et arrière-plan.
     *
     * @param deltaTemps temps écoulé depuis la dernière frame
     */
    private void drawObjects(double deltaTemps) {

        for (var m : niveauActuel.getMaisons())
            m.draw(camera); // Dessin des maisons

        camelot.draw(deltaTemps, camera);        // Dessin de Camelot

        for (var j : journauxActifs) j.draw(camera); // Dessin des journaux

        if (numNiveau >= 2) {
            for (var p : niveauActuel.getParticules())
                p.draw(camera, gc); // Dessin des particules
        }

        arrierePlan.draw(camera); // Dessin de l'arrière-plan
    }

    /**
     * Dessine l'écran affiché pendant le chargement d'un niveau.
     */
    private void dessinerEcranChargement() {
        root.getChildren().removeIf(node -> !(node instanceof Canvas)); // Supprime tout sauf le Canvas

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT); // Fond noir

        gc.setFill(Color.GREEN);
        gc.setFont(Font.font(48));
        gc.fillText(
                "Niveau " + numNiveau,
                MainJavaFX.WIDTH / 2 - 100,
                MainJavaFX.HEIGHT / 2
        ); // Texte du niveau
    }

    /**
     * Dessine l'écran de fin de partie avec le message et l'argent collecté.
     */
    private void dessinerEcranFin() {
        root.getChildren().removeIf(node -> !(node instanceof Canvas)); // Nettoie l'écran

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, MainJavaFX.WIDTH, MainJavaFX.HEIGHT); // Fond noir

        gc.setFill(Color.RED);
        gc.setFont(Font.font(40));
        gc.fillText(
                "Rupture de stocks",
                MainJavaFX.WIDTH / 2 - 150,
                MainJavaFX.HEIGHT / 2 - 50
        ); // Message de fin

        gc.setFill(Color.GREEN);
        gc.setFont(Font.font(48));
        gc.fillText(
                "Argent collecté : " + argent + "$",
                MainJavaFX.WIDTH / 2 - 200,
                MainJavaFX.HEIGHT / 2 + 50
        ); // Affichage de l'argent
    }

    /**
     * Dessine l'interface utilisateur (HUD) comprenant journaux,
     * argent et adresses.
     */
    private void dessinerUI() {
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, MainJavaFX.WIDTH, 30); // Fond semi-transparent

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(17));

        // Journaux restants
        gc.fillText(" " + camelot.getNbrJournaux(), 40, 20);

        // Argent
        gc.fillText(" " + argent + "$", 140, 20);

        // Adresses des maisons abonnées
        StringBuilder adresse = new StringBuilder();
        for (var m : niveauActuel.getMaisonsAbonnees()) {
            adresse.append(m.getAdresse()).append(" ");
        }
        gc.fillText(adresse.toString(), 210, 20);

        setIcones(); // Positionne les icônes
    }

    /**
     * Configure les icônes de l'interface utilisateur.
     */
    private void setIcones() {
        iconeJournal.setPreserveRatio(true);
        iconeJournal.setFitHeight(20);
        iconeJournal.setX(10);
        iconeJournal.setY(5);

        iconeDollar.setPreserveRatio(true);
        iconeDollar.setFitHeight(20);
        iconeDollar.setX(100);
        iconeDollar.setY(5);

        iconeMaison.setPreserveRatio(true);
        iconeMaison.setFitHeight(20);
        iconeMaison.setX(185);
        iconeMaison.setY(5);
    }

    /**
     * Dessine les éléments de debug tels que collisions et informations
     * sur Camelot et la caméra.
     */
    private void dessinerModeDebug() {

        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        for (var j : journauxActifs) {
            j.dessinerCollision(gc, camera); // Collision des journaux
        }

        for (var m : niveauActuel.getMaisons()) {
            m.getBoite().dessinerCollision(gc, camera); // Collision boîte

            for (var f : m.getFenetres()) {
                f.dessinerCollision(gc, camera); // Collision fenêtres
            }
        }

        camera.dessinerDebug(gc, camelot); // Infos debug caméra
    }

    /**
     * Dessine le champ électrique du niveau sur la scène.
     */
    private void dessinerChampElec() {
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

    /**
     * Ajoute une quantité d'argent au joueur.
     *
     * @param quantite montant à ajouter
     */
    public void ajouterArgent(int quantite) {
        this.argent += quantite;
    }

    /**
     * Retire une quantité d'argent au joueur.
     *
     * @param quantite montant à retirer
     */
    public void retirerArgent(int quantite) {
        this.argent -= quantite;
    }
}
