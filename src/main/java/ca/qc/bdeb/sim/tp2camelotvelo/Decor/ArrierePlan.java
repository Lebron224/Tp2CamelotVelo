package ca.qc.bdeb.sim.tp2camelotvelo.Decor;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

// import static de la classe brique pour les dimensions d'une seule brique
import static ca.qc.bdeb.sim.tp2camelotvelo.Decor.Brique.LARGEUR_BRIQUE;
import static ca.qc.bdeb.sim.tp2camelotvelo.Decor.Brique.HAUTEUR_BRIQUE;


/**
 * Représente l'arrière-plan du jeu.
 * Gère dynamiquement une grille de briques qui suit la caméra,
 * ajoute des briques à droite et supprime celles qui sortent de l'écran à gauche.
 */
public class ArrierePlan {

    /** Liste des briques de l'arrière-plan. */
    private final ArrayList<Brique> briques = new ArrayList<>();

    /** Conteneur racine où sont ajoutées les briques. */
    private final Pane root;

    /**
     * Constructeur de l'arrière-plan.
     *
     * @param root Pane racine du jeu
     */
    public ArrierePlan(Pane root) {
        this.root = root;
        creerGrilleInitiale(); // Crée la grille initiale de briques
    }

    /**
     * Met à jour l'arrière-plan en fonction de la position de la caméra.
     * Ajoute des briques à droite si nécessaire et supprime celles trop à gauche.
     *
     * @param camera caméra actuelle
     */
    public void updateAvecCamera(Camera camera) {
        double positionCameraX = camera.getPositionCamera().getX();

        // Vérifier si on doit ajouter de nouvelles briques à droite
        if (doitAjouterBriquesDroite(positionCameraX)) {
            ajouterBriquesDroite();
        }

        // Supprimer les briques trop à gauche
        supprimerBriquesGauche(positionCameraX);
    }

    /**
     * Vérifie si des briques doivent être ajoutées à droite de l'écran.
     *
     * @param positionCameraX position X de la caméra
     * @return true si de nouvelles briques doivent être ajoutées
     */
    private boolean doitAjouterBriquesDroite(double positionCameraX) {
        if (briques.isEmpty()) return true;

        // Trouver la brique la plus à droite
        var derniereBrique = briques.get(briques.size() - 1);
        double finGrille = derniereBrique.getPosition().getX() + LARGEUR_BRIQUE;
        double cameraFin = positionCameraX + MainJavaFX.WIDTH;

        // Ajouter si la caméra approche de la fin de la grille
        return cameraFin > finGrille - LARGEUR_BRIQUE;
    }

    /**
     * Ajoute des briques à droite de la grille existante.
     */
    private void ajouterBriquesDroite() {
        if (briques.isEmpty()) return;

        // Trouver la position de la dernière brique
        var derniereBrique = briques.get(briques.size() - 1);
        double nouvellePositionX = derniereBrique.getPosition().getX() + LARGEUR_BRIQUE;

        // Ajouter 2 colonnes de briques (marge de sécurité)
        for (int i = 0; i < 2; i++) {
            ajouterColonneBriques(nouvellePositionX);
            nouvellePositionX += LARGEUR_BRIQUE;
        }
    }

    /**
     * Supprime les briques qui sont complètement sorties de l'écran à gauche.
     *
     * @param positionCameraX position X de la caméra
     */
    private void supprimerBriquesGauche(double positionCameraX) {
        var briquesASupprimer = new ArrayList<Brique>();

        for (var b : briques) {
            double finBrique = b.getPosition().getX() + LARGEUR_BRIQUE;
            // Supprimer si la brique est complètement sortie de l'écran
            if (finBrique < positionCameraX) {
                briquesASupprimer.add(b);
            }
        }

        // Supprimer du root et de la liste
        for (var b : briquesASupprimer) {
            root.getChildren().remove(b.getImgView());
            briques.remove(b);
        }
    }

    /**
     * Ajoute une colonne complète de briques verticalement.
     *
     * @param positionX position X de la colonne
     */
    private void ajouterColonneBriques(double positionX) {
        int lignesNecessaires = (int) Math.ceil(MainJavaFX.HEIGHT / HAUTEUR_BRIQUE) + 1;

        for (int ligne = 0; ligne < lignesNecessaires; ligne++) {
            double y = ligne * HAUTEUR_BRIQUE;
            var nouvelleBrique = new Brique(new Point2D(positionX, y));
            briques.add(nouvelleBrique);
            root.getChildren().add(nouvelleBrique.getImgView());
        }
    }

    /**
     * Crée la grille initiale de briques sur 2 écrans de largeur.
     */
    private void creerGrilleInitiale() {
        briques.clear();

        int colonnesInitiales = (int) Math.ceil(MainJavaFX.HEIGHT * 2 / LARGEUR_BRIQUE);

        for (int colonne = 0; colonne < colonnesInitiales; colonne++) {
            double x = colonne * LARGEUR_BRIQUE;
            ajouterColonneBriques(x);
        }
    }

    /**
     * Ajoute toutes les briques au root. Utile pour l'initialisation.
     */
    public void ajouterArrierePlan() {
        for (var b : briques) {
            root.getChildren().add(b.getImgView());
        }
    }

    /**
     * Dessine toutes les briques de l'arrière-plan en prenant en compte la caméra.
     *
     * @param camera caméra actuelle
     */
    public void draw(Camera camera) {
        for (var b : briques) {
            b.draw(camera); // draw statique, deltaTemps = 0
            b.getImgView().toBack(); // Met derrière les autres objets
        }
    }
}
