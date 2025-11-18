package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Journaux extends GameObject{
    private final static double MASSE_JOURNAUX = 1 + Math.random();
    private final Camelot camelot;

    private double cooldown = 0;
    private boolean impulsionApplique = false;
    private boolean aSuppression = false;



    public Journaux(Camelot camelot) {
        this.camelot = camelot;


        this.imgView = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/journal.png"));
        imgView.setFitWidth(52);
        imgView.setFitHeight(31);

        this.position = camelot.position.add(
                camelot.imgView.getFitWidth() / 2 - imgView.getFitWidth() / 2,
                camelot.imgView.getFitHeight() / 2 - imgView.getFitHeight() / 2
        );
        this.velocite = camelot.velocite;
        this.acceleration = new Point2D(0,ACCELERATION_GRAVITE);
    }

    @Override
    protected void draw(double deltaTemps) {
        imgView.setX(position.getX());
        imgView.setY(position.getY());
    }

    @Override
    protected void update(double deltaTemps) {

        inputReads(deltaTemps);

        updatePhysique(deltaTemps);

        restrictionsJournaux();
    }

    private void restrictionsJournaux() {
        double max  = 1500;
        if (velocite.magnitude()  >= max){
            velocite  = velocite.multiply(max / velocite.magnitude());
        }

        if (position.getX() + imgView.getFitWidth() < 0 ||
        position.getX() >= MainJavaFX.WIDTH ||
        position.getY() <= 0 ||
        position.getY() + imgView.getFitHeight() >= MainJavaFX.HEIGHT){
            this.aSuppression = true;
        }
    }

    private void inputReads(double deltaTemps) {
        if (cooldown > 0){
            cooldown -= deltaTemps;
        }

        boolean enAvant = Input.isKeyPressed(KeyCode.X);
        boolean enHaut = Input.isKeyPressed(KeyCode.Z);
        boolean plusFort = Input.isKeyPressed(KeyCode.SHIFT);
        Point2D quantiteMouvement;

        if (!impulsionApplique && cooldown <= 0 && (enHaut || enAvant)){
            if (enAvant) quantiteMouvement = new Point2D(900, -900);
            else if (enHaut) quantiteMouvement = new Point2D(150, -1100);
            else quantiteMouvement = Point2D.ZERO;

            if (plusFort) quantiteMouvement = quantiteMouvement.multiply(1.5);

            this.velocite = camelot.velocite.add(quantiteMouvement.multiply(1/MASSE_JOURNAUX));

            impulsionApplique = true;
            cooldown = 0.5;
        }
    }

    private void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }
}
