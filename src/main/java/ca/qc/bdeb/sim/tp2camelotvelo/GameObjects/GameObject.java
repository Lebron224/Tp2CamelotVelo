package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public abstract class GameObject {
    public static final double ACCELERATION_GRAVITE = 1500;
    protected  Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected ImageView imgView;
    protected Rectangle2D hitBox;

    public GameObject(Point2D position, ImageView imageView) {
        this.position = position;
        this.imgView = imageView;

        this.hitBox = new Rectangle2D(
                position.getX(),
                position.getY(),
                imgView.getFitWidth(),
                imgView.getFitHeight()
        );
    }

    protected GameObject() {
    }

    public  Point2D getPosition() {
        return position;
    }

    public Point2D getVelocite() {
        return velocite;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public Rectangle2D getHitBox() {
        this.hitBox = new Rectangle2D(
                position.getX(),
                position.getY(),
                imgView.getFitWidth(),
                imgView.getFitHeight()
        );
        return hitBox;
    }

    public boolean collision(GameObject a){
        return this.getHitBox().intersects(a.getHitBox());
    }

    protected abstract void draw(double deltaTemps, Camera camera);
    protected abstract void update(double deltaTemps);
}
