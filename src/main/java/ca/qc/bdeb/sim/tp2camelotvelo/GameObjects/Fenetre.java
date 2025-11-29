package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.GameManager;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Représente une fenêtre dans le monde du jeu.
 * Elle peut être abonnée ou non, ce qui influence les points gagnés/perdus
 * lorsqu’un journal la touche.
 */
public class Fenetre extends GameObject {

    /** Indique si la fenêtre appartient à un abonné. */
    private final boolean abonnee;

    /** Indique si la fenêtre a déjà été touchée par un journal. */
    private boolean dejaTouchee = false;

    /**
     * Constructeur d’une fenêtre.
     *
     * @param position Position dans le monde du jeu
     * @param abonnee  true si la fenêtre appartient à un abonné
     */
    public Fenetre(Point2D position, boolean abonnee) {

        this.position = position;
        this.abonnee = abonnee;

        // Image de base de la fenêtre
        this.imgView = new ImageView(new Image("fenetre.png"));
        this.imgView.setFitHeight(130);
        this.imgView.setFitWidth(159);

        // Positionnement de l'imageView
        imgView.setX(this.position.getX());
        imgView.setY(this.position.getY());
    }

    /**
     * Applique l'effet d'un journal touchant la fenêtre.
     * Peut faire perdre ou gagner de l'argent.
     *
     * @param gm gestionnaire principal du jeu
     */
    public void toucher(GameManager gm) {
        if (dejaTouchee) return; // Une fenêtre ne peut être touchée qu'une seule fois

        dejaTouchee = true;

        // Fenêtre d’un abonné → pénalité
        if (abonnee) {
            imgView.setImage(new Image("fenetre-brisee-rouge.png"));
            gm.retirerArgent(2);

            // Fenêtre non abonnée → récompense
        } else {
            imgView.setImage(new Image("fenetre-brisee-vert.png"));
            gm.ajouterArgent(2);
        }
    }

    /**
     * Dessine la fenêtre selon la position relative à la caméra.
     *
     * @param camera     caméra du jeu
     */
    @Override
    public void draw(Camera camera) {

        // Conversion des coordonnées monde à écran
        var coordoEcran = camera.coordoEcran(position);

        // Mise à jour de l'affichage
        imgView.setX(coordoEcran.getX());
        imgView.setY(coordoEcran.getY());
    }

    /**
     * Met à jour la fenêtre (statique, donc rien à faire).
     *
     * @param deltaTemps temps écoulé
     */
    @Override
    public void update(double deltaTemps) {
        // Aucun comportement dynamique
    }

    /**
     * Dessine la hitbox de la fenêtre (mode debug).
     *
     * @param gc     contexte graphique
     * @param camera caméra utilisée pour ajuster la position
     */
    public void dessinerCollision(GraphicsContext gc, Camera camera) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        // Position de la hitbox selon la caméra
        var coordoEcran = camera.coordoEcran(position);

        // Affichage du rectangle de collision
        gc.strokeRect(
                coordoEcran.getX(),
                coordoEcran.getY(),
                imgView.getFitWidth(),
                imgView.getFitHeight()
        );
    }

    /**
     * @return true si la fenêtre a déjà été touchée
     */
    public boolean isDejaTouchee() {
        return dejaTouchee;
    }
}
