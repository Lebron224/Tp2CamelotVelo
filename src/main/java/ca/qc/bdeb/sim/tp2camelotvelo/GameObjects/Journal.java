package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Journal extends GameObject {
    private final double masse;
    private boolean aSupprimer = false;
    private boolean estLance = false;

    // ✅ Charge électrique pour les niveaux ≥ 2
    private final double chargeElectrique = 900;

    public Journal(Point2D position, Point2D velocite, double masse) {
        this.masse = masse;
        this.position = position;
        this.velocite = velocite;
        this.acceleration = new Point2D(0, ACCELERATION_GRAVITE);

        this.imgView = new ImageView(new Image("journal.png"));
        imgView.setFitWidth(52);
        imgView.setFitHeight(31);
        imgView.setX(position.getX());
        imgView.setY(position.getY());

        this.estLance = true; // ✅ Un journal créé est immédiatement lancé
    }

    @Override
    public void draw(double deltaTemps, Camera camera) {
        Point2D coordoEcran = camera.coordoEcran(position);
        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }
    @Override
    public void update(double deltaTemps) {

    }
    public void update(double deltaTemps, Camera camera) {
        updatePhysique(deltaTemps);
        restrictionsJournal(camera);
    }

    /**
     * Met à jour la physique avec le champ électrique
     */
    public void updateAvecChampElectrique(double deltaTemps, Camera camera, Point2D champElectrique) {
        // ✅ Calculer l'accélération due au champ électrique
        Point2D forceElectrique = champElectrique.multiply(chargeElectrique);
        Point2D accelerationElectrique = forceElectrique.multiply(1.0 / masse);

        // ✅ Accélération totale = gravité + champ électrique
        this.acceleration = new Point2D(accelerationElectrique.getX(),
                ACCELERATION_GRAVITE + accelerationElectrique.getY());

        updatePhysique(deltaTemps);
        restrictionsJournal(camera);
    }

    private void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }

    private void restrictionsJournal(Camera camera) {
        // ✅ Limitation de vitesse à 1500px/s
        double max = 1500;
        if (velocite.magnitude() >= max) {
            velocite = velocite.multiply(max / velocite.magnitude());
        }

        // ✅ Supprimer si sort par gauche, droite ou bas
        double cameraX = camera.getPositionCamera().getX();
        if (position.getX() + imgView.getFitWidth() < cameraX ||
                position.getY() > MainJavaFX.HEIGHT) {
            aSupprimer = true;
        }
    }

    // ✅ Getters
    public boolean estASupprimer() {
        return aSupprimer;
    }

    // ✅ Pour le mode debug
    public void dessinerCollision(GraphicsContext gc) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        gc.strokeRect(position.getX(), position.getY(),
                imgView.getFitWidth(), imgView.getFitHeight());
    }
}