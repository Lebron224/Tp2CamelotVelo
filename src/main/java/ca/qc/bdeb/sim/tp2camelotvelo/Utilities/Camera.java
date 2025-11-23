package ca.qc.bdeb.sim.tp2camelotvelo.Utilities;

import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Camera {
    private Point2D positionCamera;

    public Camera() {
        positionCamera = Point2D.ZERO;
    }

    public Point2D coordoEcran(Point2D positionMonde){
        return positionMonde.subtract(positionCamera);
    }

    public Point2D getPositionCamera() {
        return positionCamera;
    }

    public void update(Point2D positionCamelot) {
        var posX = positionCamelot.getX() - (MainJavaFX.WIDTH * 0.2);
        posX = Math.max(0, posX);

        this.positionCamera = new Point2D(posX, 0);
    }

    public void dessinerDebug(GraphicsContext gc, Camelot camelot){
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        var coordoEcran = coordoEcran(camelot.getPosition());

        gc.strokeLine(coordoEcran.getX(),0, coordoEcran.getX(), MainJavaFX.HEIGHT);
    }

    public boolean estVisible(Point2D positionEcran){
            return positionEcran.getX() >= -100 &&
                    positionEcran.getX() <= MainJavaFX.WIDTH + 100 &&
                    positionEcran.getY() >= -100 &&
                    positionEcran.getY() <= MainJavaFX.HEIGHT + 100;

    }
}
