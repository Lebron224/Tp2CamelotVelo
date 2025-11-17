package ca.qc.bdeb.sim.tp2camelotvelo;


import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Camelot extends GameObject {
    private static final Point2D POSITION_INITIALE = new Point2D(0.20 * MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
    private static final Point2D VELOCITE_INITIALE = new Point2D(400, 0);

    private boolean toucheLeSol;
    private double tempsTotal = 0;


    public Camelot(){
        this.velocite = VELOCITE_INITIALE;
        this.position = POSITION_INITIALE;
        this.acceleration = new Point2D(0, ACCELERATION_GRAVITE);
        this.imgView = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/camelot1.png"));
        imgView.setFitWidth(172); imgView.setFitHeight(144);
        this.toucheLeSol = true;
    }

    @Override
    protected void draw(double deltaTemps) {
        tempsTotal += deltaTemps;


        int index = (int) Math.floor(tempsTotal * 4) % 2;
        if (index == 0) imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/camelot1.png"));
        else imgView.setImage(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/camelot2.png"));

        imgView.setX(position.getX());
        imgView.setY(position.getY());


    }

    @Override
    protected void update(double deltaTemps) {
        inputReads();

        updatePhysique(deltaTemps);

        restrictionsPosition();
    }

    private void restrictionsPosition() {
        if (position.getY() + imgView.getFitHeight() >= MainJavaFX.HEIGHT) {
            position = new Point2D(position.getX(), MainJavaFX.HEIGHT - imgView.getFitHeight());
            toucheLeSol = true;
            velocite = new Point2D(velocite.getX(), 0);
        }

        velocite = new Point2D(
                Math.clamp(velocite.getX(), 200, 600),
                velocite.getY()
        );
    }

    private void inputReads() {
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean jump = Input.isKeyPressed(KeyCode.UP) || Input.isKeyPressed(KeyCode.SPACE);

        double accelX;

        if (gauche) {
            accelX = -300;
        } else if (droite) {
            accelX = 300;
        } else {
            if (velocite.getX() < 400) accelX = 300;
            else if (velocite.getX() > 400) accelX = -300;
            else accelX = 0;
        }

        acceleration = new Point2D(accelX, ACCELERATION_GRAVITE);

        if (jump && toucheLeSol){
            velocite = new Point2D(velocite.getX(), -500);
            toucheLeSol = false;
        }
    }

    private void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }
}
