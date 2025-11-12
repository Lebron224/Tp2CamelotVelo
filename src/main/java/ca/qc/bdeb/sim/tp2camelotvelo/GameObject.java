package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public abstract class GameObject {
    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected ImageView imgView;

    protected abstract void draw();
    protected abstract void update(double deltatemps);
}
