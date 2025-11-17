package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public abstract class GameObject {
    public static final double ACCELERATION_GRAVITE = 1500;
    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected ImageView imgView;


    protected abstract void draw(double deltaTemps);
    protected abstract void update(double deltaTemps);
}
