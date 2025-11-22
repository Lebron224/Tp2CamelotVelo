package ca.qc.bdeb.sim.tp2camelotvelo.Decor;

import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.GameObject;
import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brique extends GameObject {
    private static final double LARGEUR_BRIQUE = 192;
    private static final double HAUTEUR_BRIQUE = 96;

    public Brique(double positionX, double positionY) {
        this.position = new Point2D(positionX, positionY);

        this.imgView = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/brique.png"));
        this.imgView.setFitWidth(LARGEUR_BRIQUE);
        this.imgView.setFitHeight(HAUTEUR_BRIQUE);

        this.imgView.setX(positionX);
        this.imgView.setY(positionY);
    }

    @Override
    public void draw(double deltaTemps, Camera camera) {

        var coordoEcran = camera.coordoEcran(position);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    @Override
    public void update(double deltaTemps) {
        // Les briques sont statiques, pas de mise à jour nécessaire
    }

    /**
     * Nettoie les ressources de la brique
     */
    public void nettoyer() {
        if (this.imgView != null) {
            // Supprimer les références pour aider le garbage collector
            this.imgView.setImage(null);
            this.imgView = null;
        }
    }
}