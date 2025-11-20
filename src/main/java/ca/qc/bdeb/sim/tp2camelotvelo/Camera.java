package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;

public class Camera {
    private final Camelot camelot;
    private Point2D positionCamera;

    public Camera(Camelot camelot) {
        this.camelot = camelot;
        positionCamera = new Point2D(
                camelot.position.getX() - MainJavaFX.WIDTH * 0.20,
                0
        );
    }

    public Point2D coordoEcran(Point2D positionMonde){
        return positionMonde.subtract(positionCamera);
    }

    public Point2D getPositionCamera() {
        return positionCamera;
    }

    public void setPositionCamera(Point2D positionCamera) {
        this.positionCamera = positionCamera;
    }

    public void update(double deltaTemps) {
        positionCamera = positionCamera.add(camelot.getVelocite().multiply(deltaTemps));
    }
}
