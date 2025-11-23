package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.GameManager;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BoitesAuLettres extends GameObject{
    private final boolean abonnee;

    private boolean dejaTouchee = false;

    public BoitesAuLettres(Point2D position, boolean abonnee) {
        this.position = position;
        this.abonnee = abonnee;

        this.imgView = new ImageView(new Image("boite-aux-lettres.png"));
        imgView.setFitHeight(76);
        imgView.setFitWidth(81);
        imgView.setX(this.position.getX());
        imgView.setY(this.position.getY());
    }

    public void toucher(GameManager gm) {
        if (dejaTouchee) return;

        dejaTouchee = true;

        if (abonnee){
            imgView.setImage(new Image("boite-aux-lettres-vert.png"));
            gm.ajouterArgent(1);
        } else imgView.setImage(new Image("boite-aux-lettres-rouge.png"));
    }

    @Override
    public void draw(double deltaTemps, Camera camera) {

        var coordoEcran = camera.coordoEcran(position);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    @Override
    public void update(double deltaTemps) {

    }

    public void dessinerCollision(GraphicsContext gc) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        gc.strokeRect(position.getX(), position.getY(),
                imgView.getFitWidth(), imgView.getFitHeight());
    }

    public boolean isDejaTouchee() {
        return dejaTouchee;
    }
}
