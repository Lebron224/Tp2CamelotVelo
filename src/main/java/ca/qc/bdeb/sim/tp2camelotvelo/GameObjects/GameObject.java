package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

/**
 * Classe abstraite de base pour tous les objets du jeu.
 * Gère la position, la vélocité, l'accélération, l'affichage, le hitbox
 * et fournit des outils pour la détection des collisions.
 */
public abstract class GameObject {

    /** Accélération gravitationnelle appliquée aux objets (en px/s²). */
    protected static final double ACCELERATION_GRAVITE = 1500;

    /** Position actuelle de l'objet dans le monde. */
    protected Point2D position;

    /** Vitesse actuelle de l'objet. */
    protected Point2D velocite;

    /** Accélération actuelle de l'objet. */
    protected Point2D acceleration;

    /** ImageView représentant l'objet à l'écran. */
    protected ImageView imgView;

    /** Rectangle utilisé pour la détection des collisions. */
    protected Rectangle2D hitBox;

    /**
     * Constructeur protégé : utilisé par les classes enfants.
     */
    protected GameObject() {
    }

    /**
     * @return la position actuelle de l'objet.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Modifie la position de l'objet.
     *
     * @param position nouvelle position
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * @return l'ImageView affichant l'objet.
     */
    public ImageView getImgView() {
        return imgView;
    }

    /**
     * Calcule et retourne la hitbox actuelle de l'objet.
     * La hitbox est basées directement sur la position et les dimensions de l'image.
     *
     * @return Rectangle2D représentant la hitbox
     */
    public Rectangle2D getHitBox() {

        // Met à jour la hitbox selon la position actuelle et les dimensions de l'image
        this.hitBox = new Rectangle2D(
                position.getX(),
                position.getY(),
                imgView.getFitWidth(),
                imgView.getFitHeight()
        );
        return hitBox;
    }

    /**
     * Vérifie la collision avec un autre GameObject.
     *
     * @param a l'objet à comparer
     * @return true s'il y a intersection entre les hitbox
     */
    public boolean collision(GameObject a) {
        return this.getHitBox().intersects(a.getHitBox());
    }

    /**
     * Dessine l'objet selon la caméra et son delta temps.
     *
     * @param camera caméra à utiliser pour convertir les coordonnées
     */
    public abstract void draw(Camera camera);

    /**
     * Met à jour la logique de l'objet (physique, mouvement, etc.)
     *
     * @param deltaTemps temps écoulé depuis la dernière frame
     */
    public abstract void update(double deltaTemps);
}
