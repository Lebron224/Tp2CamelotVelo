package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

public class Camera {
    public static Point2D positionCamera;

    public static Point2D coordoEcran(Point2D positionMonde){
        return positionMonde.subtract(positionCamera);
    }

    public Point2D getPositionCamera() {
        return positionCamera;
    }

    public void setPositionCamera(Point2D positionCamera) {
        Camera.positionCamera = positionCamera;
    }
}
