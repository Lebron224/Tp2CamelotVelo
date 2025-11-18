package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoitesAuLettres extends GameObject{
    private final boolean abonnee;

    public BoitesAuLettres(Point2D position, boolean abonnee) {
        this.position = position;
        this.abonnee = abonnee;

        this.imgView = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/boite-aux-lettres.png"));
        imgView.setFitHeight(76);
        imgView.setFitWidth(81);
        imgView.setX(this.position.getX());
        imgView.setY(this.position.getY());
    }

    public void toucher() {
        if (abonnee) imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/boite-aux-lettres-vert.png"));
        else imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/boite-aux-lettres-rouge.png"));
    }

    @Override
    protected void draw(double deltaTemps) {
        imgView.setX(position.getX());
        imgView.setY(position.getY());
    }

    @Override
    protected void update(double deltaTemps) {

    }
}
