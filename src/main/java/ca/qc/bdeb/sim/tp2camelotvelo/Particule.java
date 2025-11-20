package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particule extends GameObject{
    private final double rayon = 10;
    private GraphicsContext gc;
    private static final double CONSTANTE_COULOMB = 90;
    private static final double CHARGE = 900;
    private Point2D champElectrique;
    private Color couleur;

    public Particule(Point2D position, GraphicsContext gc){
        this.gc = gc;
        this.position = position;

        double teinte = Math.random() * 360;
        this.couleur = Color.hsb(teinte, 1, 1);

    }

    public Point2D champEn(Point2D point) {

        // vecteur de la particule vers le point
        Point2D direction = point.subtract(this.position);

        double distance = direction.magnitude();
        if (distance < 1) distance = 1;  // éviter explosion numérique

        // module du champ électrique
        double Ei = (CONSTANTE_COULOMB * Math.abs(CHARGE)) / (distance * distance);

        // direction unitaire
        Point2D d = direction.normalize();

        // vecteur du champ
        return d.multiply(Ei);
    }

    @Override
    protected void draw(double deltaTemps, Camera camera) {
        gc.setFill(couleur);
        var coordoEcran = camera.coordoEcran(position);

        gc.fillOval(
                coordoEcran.getX(), coordoEcran.getY(),
                rayon, rayon
        );

    }

    @Override
    protected void update(double deltaTemps) {

    }
}
