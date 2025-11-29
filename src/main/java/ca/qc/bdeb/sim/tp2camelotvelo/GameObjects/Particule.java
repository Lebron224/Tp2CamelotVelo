package ca.qc.bdeb.sim.tp2camelotvelo.GameObjects;

import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Représente une particule chargée utilisée pour générer un champ électrique.
 * La particule :
 *  - possède une position fixe
 *  - calcule le champ électrique en un point donné selon la loi de Coulomb
 */
public class Particule extends GameObject {

    /** Rayon de la particule. */
    private final double rayon = 15;

    /** Constante de Coulomb (ajustée pour le jeu). */
    private static final double CONSTANTE_COULOMB = 90;

    /** Charge électrique de la particule. */
    private static final double CHARGE = 900;

    /** Couleur unique de la particule, générée aléatoirement. */
    private final Color couleur;

    /**
     * Constructeur.
     *
     * @param position Position de la particule dans le monde.
     */
    public Particule(Point2D position){
        this.position = position;

        // Couleur HSB aléatoire (teinte 0-360)
        double teinte = Math.random() * 360;
        this.couleur = Color.hsb(teinte, 1, 1);
    }

    /**
     * Calcule le champ électrique produit par cette particule en un point donné,
     * selon une version simplifiée de la loi de Coulomb.
     *
     * @param point Point où évaluer le champ électrique.
     * @return Vecteur du champ électrique au point.
     */
    public Point2D champEn(Point2D point) {

        // Vecteur de la particule vers le point ciblé
        var direction = point.subtract(this.position);

        // Distance entre le point et la particule
        var distance = direction.magnitude();

        // Évite une division par zéro ou des forces infinies
        if (distance < 1) distance = 1;

        // Calcul du module du champ électrique : k * q / r²
        var Ei = (CONSTANTE_COULOMB * Math.abs(CHARGE)) / (distance * distance);

        // Direction unitaire du champ
        var d = direction.normalize();

        // Vecteur complet du champ électrique
        return d.multiply(Ei);
    }

    /**
     * Méthode draw normalement imposée par GameObject,
     * mais ici inutilisée (la particule est dessinée via la surcharge).
     *
     * @param camera Caméra du jeu.
     */
    @Override
    public void draw(Camera camera) {
        // Rien à dessiner ici (on utilise la méthode draw avec GraphicsContext).
    }

    /**
     * Dessine la particule comme un cercle coloré.
     *
     * @param camera Caméra du jeu.
     * @param gc Contexte graphique utilisé pour afficher la particule.
     */
    public void draw(Camera camera, GraphicsContext gc) {

        // Appliquer la couleur de la particule
        gc.setFill(couleur);

        // Position convertie en coordonnées écran
        var coordoEcran = camera.coordoEcran(position);

        // Cercle représentant la particule
        gc.fillOval(
                coordoEcran.getX(), coordoEcran.getY(),
                rayon, rayon
        );
    }

    /**
     * Mise à jour logique de la particule.
     * Les particules sont statiques : aucune mise à jour requise.
     */
    @Override
    public void update(double deltaTemps) {
        // Rien, car les particules sont statiques
    }
}
