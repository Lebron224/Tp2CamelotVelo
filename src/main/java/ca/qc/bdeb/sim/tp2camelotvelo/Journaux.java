package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Journaux extends GameObject{
    private final static double MASSE_JOURNAUX = 1 + Math.random();
    private final Camelot camelot;

    private double cooldown = 0;
    private boolean impulsionAppliquee = false;
    private boolean aSupprimmer = false;



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
    protected void draw(double deltaTemps, Camera camera) {

        var coordoEcran = camera.coordoEcran(position);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
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
        if (// Mettre la condition de la camera
            position.getY() > MainJavaFX.HEIGHT){
            aSupprimmer = true;
        }
    }

    private void inputReads(double deltaTemps) {
        if (cooldown > 0){
            cooldown -= deltaTemps;
        }

        boolean enAvant = Input.isKeyPressed(KeyCode.X);
        boolean enHaut = Input.isKeyPressed(KeyCode.Z);
        boolean plusFort = Input.isKeyPressed(KeyCode.SHIFT);

        if (!impulsionAppliquee) {

            // Le journal reste attaché au camelot tant qu’il n’est pas lancé
            position = camelot.position.add(
                    camelot.imgView.getFitWidth() / 2 - imgView.getFitWidth() / 2,
                    camelot.imgView.getFitHeight() / 2 - imgView.getFitHeight() / 2
            );

            if ((enAvant || enHaut) && cooldown <= 0) {

                Point2D q; // impulsion

                if (enHaut) {
                    q = new Point2D(900, -900);
                } else {
                    q = new Point2D(150, -1100);
                }

                if (plusFort)
                    q = q.multiply(1.5);

                this.velocite = camelot.velocite.add(q.multiply(1.0 / MASSE_JOURNAUX));

                impulsionAppliquee = true;
                cooldown = 0.5;
            }
        }
    }

    private void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }
}
