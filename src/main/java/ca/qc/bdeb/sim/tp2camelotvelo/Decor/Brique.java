package ca.qc.bdeb.sim.tp2camelotvelo.Decor;

import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.GameObject;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Représente une brique décorative dans le jeu.
 * C'est un objet statique qui ne bouge pas et qui est uniquement dessiné.
 */
public class Brique extends GameObject {

    /** Largeur de la brique en pixels. */
    public static final double LARGEUR_BRIQUE = 192;

    /** Hauteur de la brique en pixels. */
    public static final double HAUTEUR_BRIQUE = 96;

    /**
     * Constructeur de la brique.
     * Initialise sa position et configure l'image.
     *
     * @param position position initiale de la brique dans le monde
     */
    public Brique(Point2D position) {
        this.position = position;

        // Création de l'ImageView pour la brique
        this.imgView = new ImageView(new Image("brique.png"));
        this.imgView.setFitWidth(LARGEUR_BRIQUE);
        this.imgView.setFitHeight(HAUTEUR_BRIQUE);
        this.imgView.setX(position.getX()); // Position initiale X
        this.imgView.setY(position.getY()); // Position initiale Y
    }

    /**
     * Dessine la brique à l'écran en prenant en compte la position de la caméra.
     *
     * @param deltaTemps temps écoulé depuis la dernière frame
     * @param camera caméra utilisée pour convertir les coordonnées monde → écran
     */
    @Override
    public void draw(double deltaTemps, Camera camera) {

        var coordoEcran = camera.coordoEcran(position); // Conversion monde → écran

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    /**
     * Met à jour l'état de la brique.
     * Comme la brique est statique, cette méthode ne fait rien.
     *
     * @param deltaTemps temps écoulé depuis la dernière frame
     */
    @Override
    public void update(double deltaTemps) {
        // Statique, aucune mise à jour nécessaire
    }
}