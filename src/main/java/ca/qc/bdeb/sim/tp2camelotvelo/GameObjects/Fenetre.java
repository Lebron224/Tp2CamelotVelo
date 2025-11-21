package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Fenetre extends GameObject{
    private final boolean abonnee;
    private boolean dejaTouchee = false;

    public Fenetre(Point2D position, boolean abonnee) {
        this.position = position;
        this.abonnee = abonnee;

        this.imgView = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/fenetre.png"));
        this.imgView.setFitHeight(130);
        this.imgView.setFitWidth(159);
        imgView.setX(this.position.getX());
        imgView.setY(this.position.getY());
    }

    public void toucher(){
        if (dejaTouchee) return;

        dejaTouchee = true;

        if (abonnee) imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/fenetre-brisee-rouge.png"));
        else imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/fenetre-brisee-vert.png"));

    }

    @Override
    protected void draw(double deltaTemps, Camera camera) {

        var coordoEcran = camera.coordoEcran(position);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    @Override
    protected void update(double deltaTemps) {

    }
}
