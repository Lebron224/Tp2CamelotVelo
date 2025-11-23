package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Maison extends GameObject{
    private int adresse;
    private boolean abonnement;

    private BoitesAuLettres boite;
    private ArrayList<Fenetre> fenetres;
    private Text adresseTexte;

    private final Random rnd = new Random();


    private static final double POSITION_X_BOITE = 200;
    private static final double POSiTION_Y_FEN = 50;
    private static final double POSITION_X_FEN01 = 300;
    private static final double POSITION_X_FEN02 = 600;

    public Maison(int adresse, double positionX) {
        this.velocite = Point2D.ZERO;
        this.acceleration = Point2D.ZERO;


        this.adresse = adresse;
        this.abonnement = rnd.nextBoolean();

        this.imgView = new ImageView(new Image("porte.png"));
        imgView.setFitWidth(143);
        imgView.setFitHeight(195);

        adresseTexte = new Text("" + adresse);
        adresseTexte.setFill(Color.YELLOW);
        adresseTexte.setFont(Font.font(35));

        this.position = new Point2D(positionX, MainJavaFX.HEIGHT - imgView.getFitHeight());

        this.imgView.setX(position.getX());
        this.imgView.setY(position.getY());

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
    public void draw(double deltaTemps, Camera camera) {

    }

    public void drawMaison(GraphicsContext gc, Camera camera, double deltaTemps) {

        var coordoEcran = camera.coordoEcran(position);

        // Position de l'image
        this.imgView.setX(coordoEcran.getX());
        this.imgView.setY(coordoEcran.getY());

        adresseTexte.setX(imgView.getX() + 45);
        adresseTexte.setY(imgView.getY() + 50);


        // Dessiner la boîte aux lettres et les fenêtres
        boite.draw(deltaTemps, camera);

        for (var f : fenetres) {
            f.draw(deltaTemps, camera);
        }
    }


    @Override
    public void update(double deltaTemps) {

    }

    public boolean aUnAbonnement() {
        return abonnement;
    }

    public int getAdresse() {
        return adresse;
    }

    public ArrayList<Fenetre> getFenetres() {
        return fenetres;
    }

    public BoitesAuLettres getBoite() {
        return boite;
    }

    public Text getAdresseTexte() {
        return adresseTexte;
    }
}
