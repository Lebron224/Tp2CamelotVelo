package ca.qc.bdeb.sim.tp2camelotvelo.Utilities;

import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Représente une caméra qui suit Camelot et ajuste l'affichage du monde.
 * Fournit des méthodes pour convertir des positions monde → écran,
 * dessiner des informations de debug et vérifier la visibilité.
 */
public class Camera {

    /** Position de la caméra dans le monde. */
    private Point2D positionCamera;

    /**
     * Constructeur de la caméra. Initialise la position à (0,0).
     */
    public Camera() {
        positionCamera = Point2D.ZERO;
    }

    /**
     * Convertit une position dans le monde en position à l'écran,
     * en prenant en compte le décalage de la caméra.
     *
     * @param positionMonde position dans le monde
     * @return position correspondante sur l'écran
     */
    public Point2D coordoEcran(Point2D positionMonde) {
        return positionMonde.subtract(positionCamera); // Décale selon la caméra
    }

    /**
     * Retourne la position actuelle de la caméra.
     *
     * @return position de la caméra
     */
    public Point2D getPositionCamera() {
        return positionCamera;
    }

    /**
     * Met à jour la position de la caméra pour suivre Camelot.
     * La caméra se déplace uniquement horizontalement et ne peut pas dépasser 0.
     *
     * @param positionCamelot position actuelle de Camelot
     */
    public void update(Point2D positionCamelot) {
        var posX = positionCamelot.getX() - (MainJavaFX.WIDTH * 0.2); // Décalage vers la gauche
        posX = Math.max(0, posX); // Ne pas passer à gauche de 0

        this.positionCamera = new Point2D(posX, 0);
    }

    /**
     * Dessine des informations de debug sur la caméra et la position de Camelot.
     *
     * @param gc GraphicsContext pour dessiner
     * @param camelot objet Camelot à suivre
     */
    public void dessinerDebug(GraphicsContext gc, Camelot camelot) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        var coordoEcran = coordoEcran(camelot.getPosition()); // Conversion monde → écran

        // Ligne verticale à la position de Camelot
        gc.strokeLine(coordoEcran.getX(), 0, coordoEcran.getX(), MainJavaFX.HEIGHT);
    }

    /**
     * Vérifie si une position écran est visible dans la fenêtre de jeu
     * avec une marge de 100 pixels.
     *
     * @param positionEcran position sur l'écran
     * @return true si visible, false sinon
     */
    public boolean estVisible(Point2D positionEcran) {
        return positionEcran.getX() >= -100 &&
                positionEcran.getX() <= MainJavaFX.WIDTH + 100 &&
                positionEcran.getY() >= -100 &&
                positionEcran.getY() <= MainJavaFX.HEIGHT + 100;
    }
}
