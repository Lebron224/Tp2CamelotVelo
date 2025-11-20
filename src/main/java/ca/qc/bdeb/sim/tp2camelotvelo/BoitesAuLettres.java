package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoitesAuLettres extends GameObject{
    private final boolean abonnee;

    private boolean dejaTouchee = false;

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
        if (dejaTouchee) return;

        dejaTouchee = true;

        if (abonnee) imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/boite-aux-lettres-vert.png"));
        else imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/boite-aux-lettres-rouge.png"));
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
