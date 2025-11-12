package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe utilitaire qui fournit une méthode pour dessiner une flèche sur
 * un GraphicsContext2D dans un Canvas JavaFX
 */
public class UtilitairesDessins {
    // Classe de méthodes static seulement, pas d'instances autorisées
    private UtilitairesDessins() {}

    /**
     * Dessine le vecteur fourni à partir d'un certain point sur l'écran.
     * <p>
     * Si le vecteur a un module de plus de 80, une longueur maximale de 80 est utilisée
     * (question d'éviter d'afficher des vecteurs inutilement trop gros sur toute la largeur de l'écran)
     *
     * @param origineEcran Point de départ du vecteur (en pixels, sur l'écran)
     * @param vecteur      Vecteur à dessiner avec une flèche
     * @param context      Contexte graphique du Canvas
     */
    public static void dessinerVecteurForce(Point2D origineEcran, Point2D vecteur, GraphicsContext context) {
        if (vecteur.getX() != vecteur.getX()) {
            throw new IllegalArgumentException("Erreur dans les maths, probablement due à une division par zéro");
        }

        double longueurMax = 80;
        double forceMax = 80;

        // Calcule la longueur du vecteur comme un pourcentage de la force maximale affichée
        double pourcentage = Math.clamp(vecteur.magnitude(), 0, forceMax) / forceMax;

        dessinerVecteur(
                origineEcran,
                vecteur.normalize().multiply(pourcentage * longueurMax),
                context);
    }

    private static void dessinerVecteur(Point2D origineEcran, Point2D vecteur, GraphicsContext context) {
        final double rayon = 1.5;
        var finEcran = origineEcran.add(vecteur);

        context.save();
        context.setStroke(Color.CORAL);
        context.setFill(Color.CORAL);
        context.setLineWidth(2);

        // Base du vecteur
        context.fillOval(
                origineEcran.getX() - rayon, origineEcran.getY() - rayon,
                rayon * 2, rayon * 2);

        // Magnitude proche de zéro : pas de tête de flèche
        if (vecteur.dotProduct(vecteur) < rayon * rayon)
            return;

        // Corps de la flèche
        context.strokeLine(
                origineEcran.getX(), origineEcran.getY(),
                finEcran.getX(), finEcran.getY()
        );

        // Tête de la flèche
        context.translate(finEcran.getX(), finEcran.getY());
        context.rotate(Math.toDegrees(Math.atan2(vecteur.getY(), vecteur.getX())));
        context.fillPolygon(
                new double[]{
                        0, 10, 0
                },
                new double[]{
                        -10, 0, 10
                },
                3);

        context.restore();
    }
}
