package ca.qc.bdeb.sim.tp2camelotvelo.Decor;

import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class ArrierePlan {
    private final ArrayList<Brique> briques = new ArrayList<>();
    private static final double LARGEUR_BRIQUE = 192;
    private static final double HAUTEUR_BRIQUE = 96;
    private static final double LARGEUR_ECRAN = 900;
    private static final double HAUTEUR_ECRAN = 580;

    private final Pane root;

    public ArrierePlan(Pane root) {
        this.root = root;
        creerGrilleInitiale();
    }

    /**
     * Met à jour l'arrière-plan en fonction de la caméra
     */
    public void updateAvecCamera(Camera camera) {
        double positionCameraX = camera.getPositionCamera().getX();

        // ✅ Vérifier si on doit ajouter de nouvelles briques à droite
        if (doitAjouterBriquesDroite(positionCameraX)) {
            ajouterBriquesDroite();
        }

        // ✅ Supprimer les briques trop à gauche
        supprimerBriquesGauche(positionCameraX);

    }

    /**
     * Vérifie si on doit ajouter des briques à droite
     */
    private boolean doitAjouterBriquesDroite(double positionCameraX) {
        if (briques.isEmpty()) return true;

        // ✅ Trouver la brique la plus à droite
        Brique derniereBrique = briques.get(briques.size() - 1);
        double finGrille = derniereBrique.getPosition().getX() + LARGEUR_BRIQUE;
        double cameraFin = positionCameraX + LARGEUR_ECRAN;

        // ✅ Ajouter si la caméra approche de la fin de la grille
        return cameraFin > finGrille - LARGEUR_BRIQUE;
    }

    /**
     * Ajoute des briques à droite
     */
    private void ajouterBriquesDroite() {
        if (briques.isEmpty()) return;

        // ✅ Trouver la position de la dernière brique
        var derniereBrique = briques.get(briques.size() - 1);
        double nouvellePositionX = derniereBrique.getPosition().getX() + LARGEUR_BRIQUE;

        // ✅ Ajouter 2 colonnes de briques (marge de sécurité)
        for (int i = 0; i < 2; i++) {
            ajouterColonneBriques(nouvellePositionX);
            nouvellePositionX += LARGEUR_BRIQUE;
        }
    }

    /**
     * Supprime les briques trop à gauche
     */
    private void supprimerBriquesGauche(double positionCameraX) {
        ArrayList<Brique> briquesASupprimer = new ArrayList<>();

        for (Brique brique : briques) {
            double finBrique = brique.getPosition().getX() + LARGEUR_BRIQUE;
            // ✅ Supprimer si la brique est complètement sortie de l'écran
            if (finBrique < positionCameraX) {
                briquesASupprimer.add(brique);
            }
        }

        // ✅ Supprimer du root et de la liste
        for (Brique brique : briquesASupprimer) {
            root.getChildren().remove(brique.getImgView());
            briques.remove(brique);
        }
    }

    /**
     * Ajoute une colonne complète de briques
     */
    private void ajouterColonneBriques(double positionX) {
        int lignesNecessaires = (int) Math.ceil(HAUTEUR_ECRAN / HAUTEUR_BRIQUE) + 1;

        for (int ligne = 0; ligne < lignesNecessaires; ligne++) {
            double y = ligne * HAUTEUR_BRIQUE;
            Brique nouvelleBrique = new Brique(new Point2D(positionX, y));
            briques.add(nouvelleBrique);
            root.getChildren().add(nouvelleBrique.getImgView());
        }
    }

    /**
     * Crée la grille initiale (2 écrans de large)
     */
    private void creerGrilleInitiale() {
        briques.clear();

        int colonnesInitiales = (int) Math.ceil(LARGEUR_ECRAN * 2 / LARGEUR_BRIQUE);

        for (int colonne = 0; colonne < colonnesInitiales; colonne++) {
            double x = colonne * LARGEUR_BRIQUE;
            ajouterColonneBriques(x);
        }
    }

    public void ajouterArrierePlan() {
        for (var b : briques) {
            root.getChildren().add(b.getImgView());
        }
    }

    public void draw(Camera camera) {
        for (var b : briques) {
            b.draw(0, camera);
            b.getImgView().toBack();
        }
    }
}