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

/**
 * Représente une maison dans le jeu. Chaque maison possède :
 * - une adresse
 * - un abonnement (booléen)
 * - une boîte aux lettres
 * - 0 à 2 fenêtres
 * - une porte affichée à l'écran
 *
 * L’apparence des fenêtres et la position verticale de la boîte
 * aux lettres sont générées aléatoirement.
 */
public class Maison extends GameObject {

    /** Adresse de la maison. */
    private final int adresse;

    /** Indique si la maison est abonnée. */
    private final boolean abonnement;

    /** Boîte aux lettres associée à la maison. */
    private final BoitesAuLettres boite;

    /** Liste des fenêtres générées pour cette maison. */
    private final ArrayList<Fenetre> fenetres;

    /** Texte affichant l'adresse au-dessus de la porte. */
    private final Text adresseTexte;

    /** Générateur aléatoire pour décider du nombre de fenêtres et de l'abonnement. */
    private final Random rnd = new Random();

    // Positions relatives des éléments de la maison
    private static final double POSITION_X_BOITE = 200;
    private static final double POSiTION_Y_FEN = 50;
    private static final double POSITION_X_FEN01 = 300;
    private static final double POSITION_X_FEN02 = 600;

    /**
     * Constructeur d'une maison.
     *
     * @param adresse Numéro civique.
     * @param positionX Position horizontale de la maison dans le monde.
     */
    public Maison(int adresse, double positionX) {
        this.velocite = Point2D.ZERO;      // La maison ne bouge jamais
        this.acceleration = Point2D.ZERO;  // Aucun mouvement physique

        this.adresse = adresse;
        this.abonnement = rnd.nextBoolean(); // Abonnement assigné aléatoirement

        // Création et dimensionnement de la porte
        this.imgView = new ImageView(new Image("porte.png"));
        imgView.setFitWidth(143);
        imgView.setFitHeight(195);

        // Texte de l’adresse affiché au-dessus de la porte
        adresseTexte = new Text("" + adresse);
        adresseTexte.setFill(Color.YELLOW);
        adresseTexte.setFont(Font.font(35));

        // Position de la maison au sol, basée sur la hauteur totale
        this.position = new Point2D(positionX, MainJavaFX.HEIGHT - imgView.getFitHeight());

        // Position initiale de la porte
        this.imgView.setX(position.getX());
        this.imgView.setY(position.getY());

        // Génération de la position de la boîte aux lettres
        double posBoiteX = positionX + POSITION_X_BOITE;

        // Limites verticales pour un placement aléatoire crédible
        double minY = MainJavaFX.HEIGHT * 0.2;
        double maxY = MainJavaFX.HEIGHT * 0.7;

        // Position aléatoire entre 20% et 70% de la hauteur de l'écran
        double posBoiteY = minY + rnd.nextDouble() * (maxY - minY);

        // Création de la boîte aux lettres
        this.boite = new BoitesAuLettres(new Point2D(posBoiteX, posBoiteY), this.abonnement);

        // --- Génération aléatoire des fenêtres ---
        this.fenetres = new ArrayList<>();
        int nbrFenetres = rnd.nextInt(3); // 0, 1 ou 2 fenêtres

        // Ajout de la première fenêtre
        if (nbrFenetres >= 1)
            fenetres.add(new Fenetre(
                    new Point2D(positionX + POSITION_X_FEN01, POSiTION_Y_FEN),
                    this.abonnement));

        // Ajout de la deuxième fenêtre
        if (nbrFenetres == 2)
            fenetres.add(new Fenetre(
                    new Point2D(positionX + POSITION_X_FEN02, POSiTION_Y_FEN),
                    this.abonnement));
    }

    /**
     * Dessine la maison, la porte, l’adresse, la boîte aux lettres et toutes les fenêtres.
     *
     * @param camera Caméra utilisée pour convertir les coordonnées monde → écran.
     */
    @Override
    public void draw(Camera camera) {

        // Conversion de la position en coordonnées écran
        var coordoEcran = camera.coordoEcran(position);

        // Positionnement de la porte selon la caméra
        this.imgView.setX(coordoEcran.getX());
        this.imgView.setY(coordoEcran.getY());

        // Position du texte de l’adresse (au-dessus de la porte)
        adresseTexte.setX(imgView.getX() + 45);
        adresseTexte.setY(imgView.getY() + 50);

        // Dessiner la boîte aux lettres
        boite.draw(camera);

        // Dessiner chaque fenêtre
        for (var f : fenetres) {
            f.draw(camera);
        }
    }

    /**
     * Mise à jour logique de la maison (Rien, car la maison est statique).
     * @param deltaTemps Temps écoulé.
     */
    @Override
    public void update(double deltaTemps) {}


    /** @return true si le propriétaire est abonné. */
    public boolean aUnAbonnement() {
        return abonnement;
    }

    /** @return Adresse de la maison. */
    public int getAdresse() {
        return adresse;
    }

    /** @return Liste des fenêtres générées. */
    public ArrayList<Fenetre> getFenetres() {
        return fenetres;
    }

    /** @return La boîte aux lettres de la maison. */
    public BoitesAuLettres getBoite() {
        return boite;
    }

    /** @return Le texte affichant l'adresse. */
    public Text getAdresseTexte() {
        return adresseTexte;
    }
}
