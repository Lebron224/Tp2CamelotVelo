package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Représente un journal lancé par le joueur.
 * Le journal est soumis à la gravité, possède une masse,
 * peut être affecté par un champ électrique (niveaux ≥ 2)
 * et disparaît lorsqu’il sort de l’écran.
 */
public class Journal extends GameObject {

    /** Masse du journal. */
    private final double masse;

    /** Indique si l’objet doit être supprimé du jeu. */
    private boolean aSupprimer = false;

    /** Charge électrique appliquée au journal dans les niveaux ≥ 2. */
    private final double chargeElectrique = 900;

    /**
     * Constructeur du journal.
     *
     * @param position Position initiale du journal.
     * @param velocite Vitesse initiale.
     * @param masse Masse physique du journal.
     */
    public Journal(Point2D position, Point2D velocite, double masse) {

        this.masse = masse;
        this.position = position;
        this.velocite = velocite;

        // Accélération initiale = gravité
        this.acceleration = new Point2D(0, ACCELERATION_GRAVITE);

        // Création et dimension de l'image du journal
        this.imgView = new ImageView(new Image("journal.png"));
        imgView.setFitWidth(52);   // largeur selon consigne
        imgView.setFitHeight(31);  // hauteur selon consigne

        // Positionnement initiale de l'imageView
        imgView.setX(position.getX());
        imgView.setY(position.getY());

    }

    /**
     * Dessine le journal sur l'écran après transformation par la caméra.
     *
     * @param camera Caméra utilisée pour la conversion monde → écran.
     */
    @Override
    public void draw(Camera camera) {

        // Convertit position du monde → écran
        Point2D coordoEcran = camera.coordoEcran(position);

        // Déplace l'image à la bonne position
        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    /** Mise à jour simple (non utilisée ici). */
    @Override
    public void update(double deltaTemps) { }

    /**
     * Mise à jour physique + suppression hors écran.
     *
     * @param deltaTemps Temps écoulé
     * @param camera Caméra pour vérifier les sorties de l’écran
     */
    public void update(double deltaTemps, Camera camera) {
        updatePhysique(deltaTemps);
        restrictionsJournal(camera);
    }

    /**
     * Mise à jour physique intégrant le champ électrique.
     *
     * @param deltaTemps Temps écoulé
     * @param camera Caméra pour vérifier les limites
     * @param champElectrique Champ électrique appliqué au journal
     */
    public void updateAvecChampElectrique(double deltaTemps, Camera camera, Point2D champElectrique) {

        // Force électrique : F = qE
        var forceElectrique = champElectrique.multiply(chargeElectrique);

        // Accélération : a = F/m
        var accelerationElectrique = forceElectrique.multiply(1.0 / masse);

        // Accel totale = gravité + composante électrique
        this.acceleration = new Point2D(
                accelerationElectrique.getX(),
                ACCELERATION_GRAVITE + accelerationElectrique.getY()
        );

        // Mise à jour physique complète
        update(deltaTemps, camera);
    }

    /**
     * Met à jour la position et la vitesse en utilisant la physique classique :
     * v += a * dt
     * x += v * dt
     *
     * @param deltaTemps Temps écoulé
     */
    private void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps)); // mise à jour vitesse
        position = position.add(velocite.multiply(deltaTemps));     // mise à jour position
    }

    /**
     * Applique les restrictions :
     * - limite de vitesse
     * - suppression hors de la caméra
     *
     * @param camera Caméra pour déterminer si l’objet sort
     */
    private void restrictionsJournal(Camera camera) {
        // Limite la vitesse maximale à 1500 px/s
        double max = 1500;
        if (velocite.magnitude() >= max) {
            velocite = velocite.multiply(max / velocite.magnitude()); // normalisation
        }

        // Position X de la caméra
        double cameraX = camera.getPositionCamera().getX();

        // Supprimer si :
        // - sort par la gauche du décor visible
        // - sort par le bas de la fenêtre
        if (position.getX() + imgView.getFitWidth() < cameraX ||
                position.getY() > MainJavaFX.HEIGHT) {

            aSupprimer = true;
        }
    }

    /**
     * @return true si le journal doit être retiré du jeu.
     */
    public boolean estASupprimer() {
        return aSupprimer;
    }

    /**
     * Dessine la hitbox du journal (pour le mode debug).
     *
     * @param gc Contexte graphique
     * @param camera Caméra pour la conversion des coordonnées
     */
    public void dessinerCollision(GraphicsContext gc, Camera camera) {

        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        var coordoEcran = camera.coordoEcran(position);

        // Dessine le rectangle de collision
        gc.strokeRect(
                coordoEcran.getX(),
                coordoEcran.getY(),
                imgView.getFitWidth(),
                imgView.getFitHeight()
        );
    }
}
