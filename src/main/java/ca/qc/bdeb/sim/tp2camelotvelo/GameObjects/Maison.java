package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class Maison extends GameObject{
    private int adresse;
    private boolean abonnement;

    private BoitesAuLettres boite;
    private ArrayList<Fenetre> fenetres;

    private final Random rnd = new Random();


    private static final double POSITION_X_BOITE = 200;
    private static final double POSiTION_Y_FEN = 50;
    private static final double POSITION_X_FEN01 = 300;
    private static final double POSITION_X_FEN02 = 600;

    public Maison(int adresse, double positionX) {
        this.adresse = adresse;
        this.abonnement = rnd.nextBoolean();

        this.imgView = new ImageView(new Image("resources/ca/qc/bdeb/sim/tp2camelotvelo/porte.png"));
        imgView.setX(positionX);
        imgView.setY(MainJavaFX.HEIGHT - imgView.getFitHeight());

        double posBoiteX = positionX + POSITION_X_BOITE;
        double minY = MainJavaFX.HEIGHT * 0.2;
        double maxY = MainJavaFX.HEIGHT * 0.7;
        double posBoiteY = minY + rnd.nextDouble() * (maxY - minY);

        this.boite = new BoitesAuLettres(new Point2D(posBoiteX, posBoiteY), this.abonnement);

        this.fenetres = new ArrayList<>();
        int nbrFenetres = rnd.nextInt(3);
        if (nbrFenetres >= 1) fenetres.add(new Fenetre(new Point2D(positionX + POSITION_X_FEN01, POSiTION_Y_FEN), this.abonnement));
        if (nbrFenetres == 2) fenetres.add(new Fenetre(new Point2D(positionX + POSITION_X_FEN02, POSiTION_Y_FEN), this.abonnement));
    }

    @Override
    protected void draw(double deltaTemps, Camera camera) {

        var coordoEcran = camera.coordoEcran(position);

        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());

        boite.draw(deltaTemps, camera);

        for (var f :
                fenetres) {
            f.draw(deltaTemps, camera);
        }
    }

    @Override
    protected void update(double deltaTemps) {

    }

    public ArrayList<Fenetre> getFenetres() {
        return fenetres;
    }

    public BoitesAuLettres getBoite() {
        return boite;
    }
}
