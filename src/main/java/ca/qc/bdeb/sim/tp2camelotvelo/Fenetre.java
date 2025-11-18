package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Fenetre extends GameObject{
    private final boolean abonnee;

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
        if (abonnee) imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/fenetre-brisee-vert.png"));
        else imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/fenetre-brisee-rouge.png"));

    }

    @Override
    protected void draw(double deltaTemps) {

    }

    @Override
    protected void update(double deltaTemps) {

    }
}
