package ca.qc.bdeb.sim.tp2camelotvelo;


import javafx.geometry.Point2D;

public class Camelot extends GameObject {
    private static final Point2D POSITION_FINALE = new Point2D(200, 500); // PlaceHolder
    private static final Point2D VELOCITE_INITIALE = new Point2D(200, 500); // PlaceHolder


    public Camelot(){
        this.velocite = VELOCITE_INITIALE;
        this.position = POSITION_FINALE;
    }

    @Override
    protected void draw() {

    }

    @Override
    protected void update(double deltaTemps) {

    }
}
