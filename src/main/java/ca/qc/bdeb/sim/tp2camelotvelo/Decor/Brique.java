package ca.qc.bdeb.sim.tp2camelotvelo.Decor;

import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.GameObject;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brique extends GameObject {
    private static final double LARGEUR_BRIQUE = 192;
    private static final double HAUTEUR_BRIQUE = 96;

    public Brique(Point2D position) {
        this.position = position;

        this.imgView = new ImageView(new Image("brique.png"));
        this.imgView.setFitWidth(LARGEUR_BRIQUE);
        this.imgView.setFitHeight(HAUTEUR_BRIQUE);
        this.imgView.setX(position.getX());
        this.imgView.setY(position.getY());
    }

    @Override
    public void draw(double deltaTemps, Camera camera) {
        Point2D coordoEcran = camera.coordoEcran(position);
        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    @Override
    public void update(double deltaTemps) {
        // Statique
    }
}