package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.GameManager;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Représente une boîte aux lettres dans le monde du jeu.
 * Elle peut être abonnée (doit recevoir un journal) ou non.
 * Lorsqu'un journal entre en collision avec elle, son état change.
 */
public class BoitesAuLettres extends GameObject {

    /** Indique si la boîte appartient à un abonné. */
    private final boolean abonnee;

    /** Indique si la boîte a déjà été touchée par un journal. */
    private boolean dejaTouchee = false;

    /**
     * Constructeur d'une boîte aux lettres.
     *
     * @param position position de la boîte dans le monde
     * @param abonnee  true si la boîte appartient à un abonné
     */
    public BoitesAuLettres(Point2D position, boolean abonnee) {

        this.position = position;
        this.abonnee = abonnee;

        // Image de base
        this.imgView = new ImageView(new Image("boite-aux-lettres.png"));
        imgView.setFitHeight(76);
        imgView.setFitWidth(81);

        // Positionnement de l'imageView
        imgView.setX(this.position.getX());
        imgView.setY(this.position.getY());
    }

    /**
     * Applique l'effet d'un journal touchant la boîte aux lettres.
     * Change l'image selon l'état (abonné ou non) et ajoute de l'argent si applicable.
     *
     * @param gm référence au GameManager pour ajouter l'argent
     */
    public void toucher(GameManager gm) {
        if (dejaTouchee) return; // On ne peut toucher qu'une fois

        dejaTouchee = true;

        if (abonnee) {
            imgView.setImage(new Image("boite-aux-lettres-vert.png"));
            gm.ajouterArgent(1); // Gain pour une livraison réussie
        } else {
            imgView.setImage(new Image("boite-aux-lettres-rouge.png"));
        }
    }

    /**
     * Dessine la boîte aux lettres selon sa position relative à la caméra.
     *
     * @param camera     caméra utilisée pour convertir les coordonnées
     */
    @Override
    public void draw(Camera camera) {

        var coordoEcran = camera.coordoEcran(position); // Calcule les coordonnées relatives à la caméra

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    /**
     * Mise à jour de la boîte aux lettres.
     * Les boîtes sont statiques → aucune logique nécessaire.
     *
     * @param deltaTemps temps écoulé
     */
    @Override
    public void update(double deltaTemps) {
        // Aucun comportement dynamique
    }

    /**
     * Dessine le hitbox de la boîte aux lettres (mode debug).
     *
     * @param gc     contexte graphique
     * @param camera caméra utilisée pour ajuster la position
     */
    public void dessinerCollision(GraphicsContext gc, Camera camera) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        var coordoEcran = camera.coordoEcran(position);

        gc.strokeRect(
                coordoEcran.getX(),
                coordoEcran.getY(),
                imgView.getFitWidth(),
                imgView.getFitHeight()
        );
    }

    /**
     * @return true si la boîte a déjà été touchée par un journal
     */
    public boolean isDejaTouchee() {
        return dejaTouchee;
    }
}
