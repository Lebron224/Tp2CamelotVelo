package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;


import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Input;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Camelot extends GameObject {
    private boolean toucheLeSol;
    private double tempsTotal = 0;
    private double cooldown= 0;
    private final double masseJournaux;

    private final Image img1 = new Image("camelot1.png");
    private final Image img2 = new Image("camelot2.png");
    private int nbrJournaux;

    public Camelot(){
        this.velocite = new Point2D(400, 0);
        this.acceleration = new Point2D(0, ACCELERATION_GRAVITE);

        this.masseJournaux = 1 + Math.random();

        this.imgView = new ImageView(img1);
        imgView.setFitWidth(172); imgView.setFitHeight(144);

        this.position = new Point2D(0.20 * MainJavaFX.WIDTH, MainJavaFX.HEIGHT - imgView.getFitHeight());
        this.toucheLeSol = true;
    }



    @Override
    public void draw(double deltaTemps, Camera camera) {
        tempsTotal += deltaTemps;

        var coordoEcran = camera.coordoEcran(position);


        int index = (int) Math.floor(tempsTotal * 4) % 2;
        if (index == 0) imgView.setImage(img1);
        else imgView.setImage(img2);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());

    }

    @Override
    public void update(double deltaTemps) {

        if (cooldown > 0) cooldown -= deltaTemps;

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

    public Journal lancerHaut(){
        var shiftEnfonce = Input.isKeyPressed(KeyCode.SHIFT);
        if (peutLancerJournal()) return null;

        var q = new Point2D(150, -1100);
        if (shiftEnfonce) q = q.multiply(1.5);

        return creerJournal(q);
    }

    public Journal lancerAvant(){
        var shiftEnfonce = Input.isKeyPressed(KeyCode.SHIFT);
        if (peutLancerJournal()) return null;

        var q = new Point2D(900, -900);
        if (shiftEnfonce) q = q.multiply(1.5);

        return creerJournal(q);
    }

    private Journal creerJournal(Point2D q){
        if (peutLancerJournal()) return null;

        var posDepart = position.add(
                imgView.getFitWidth() / 2 - 52,
                imgView.getFitHeight() / 2 - 31
        );

        var velociteInitiale = velocite.add(q.multiply(1/masseJournaux));

        return new Journal(posDepart,velociteInitiale, masseJournaux);
    }


    public boolean peutLancerJournal(){
        return nbrJournaux == 0 || cooldown < 0;
    }

    public void ajouterJournaux(int quantite){
        this.nbrJournaux += quantite;
    }

    public void retirerJournaux(int quantite){
        this.nbrJournaux -= quantite;
    }

    public int getNbrJournaux() {
        return nbrJournaux;
    }

    public void setNbrJournaux(int nbrJournaux){
        this.nbrJournaux = nbrJournaux;
    }

    public void  resetJournal(){
        this.nbrJournaux = 12;
    }

}
