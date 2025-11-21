package ca.qc.bdeb.sim.tp2camelotvelo.Decor;

import ca.qc.bdeb.sim.tp2camelotvelo.MainJavaFX;

import java.util.ArrayList;

public class ArrierePlan {
    private ArrayList<ArrayList<Brique>> grilleBriques = new ArrayList<>();
    private static final double LARGEUR_BRIQUE = 192;
    private static final double HAUTEUR_BRIQUE = 96;
    private static final double LARGEUR_ECRAN = 900;
    private static final double HAUTEUR_ECRAN = 580;

    private double dernierePositionCameraX = 0;
    private int colonnesVisibles;
    private int lignesVisibles;

    public ArrierePlan() {
        this.colonnesVisibles = (int) Math.ceil(LARGEUR_ECRAN / LARGEUR_BRIQUE) + 2; // +2 pour dépassement
        this.lignesVisibles = (int) Math.ceil(HAUTEUR_ECRAN / HAUTEUR_BRIQUE) + 1;
        creerGrilleBriques(0); // Commencer à la position 0
    }

    /**
     * Met à jour l'arrière-plan en fonction de la position de la caméra
     */
    public void updateAvecCamera(double positionCameraX) {
        // ✅ Vérifier si la caméra s'est suffisamment déplacée pour justifier une mise à jour
        double deltaX = positionCameraX - dernierePositionCameraX;
        if (Math.abs(deltaX) < LARGEUR_BRIQUE / 2) {
            return; // Pas besoin de mise à jour
        }

        // ✅ Calculer la colonne de départ visible
        int colonneDebut = (int) (positionCameraX / LARGEUR_BRIQUE);

        // ✅ Recréer la grille seulement si nécessaire
        if (colonneDebut != (int)(dernierePositionCameraX / LARGEUR_BRIQUE)) {
            recreerGrilleBriques(colonneDebut);
            dernierePositionCameraX = positionCameraX;
        }
    }

    /**
     * Crée une nouvelle grille de briques centrée sur la colonne de départ
     */
    private void recreerGrilleBriques(int colonneDebut) {
        // ✅ Supprimer les anciennes briques
        supprimerBriques();

        // ✅ Créer les nouvelles briques visibles
        grilleBriques.clear();

        for (int ligne = 0; ligne < lignesVisibles; ligne++) {
            ArrayList<Brique> ligneBriques = new ArrayList<>();

            for (int colonne = 0; colonne < colonnesVisibles; colonne++) {
                double x = (colonneDebut + colonne) * LARGEUR_BRIQUE;
                double y = ligne * HAUTEUR_BRIQUE;
                ligneBriques.add(new Brique(x, y));
            }
            grilleBriques.add(ligneBriques);
        }

        System.out.println("Grille recréée à partir de la colonne " + colonneDebut);
    }

    /**
     * Version initiale pour créer la grille
     */
    private void creerGrilleBriques(double positionCameraX) {
        int colonneDebut = (int) (positionCameraX / LARGEUR_BRIQUE);
        recreerGrilleBriques(colonneDebut);
    }

    /**
     * Supprime toutes les briques (pour libérer la mémoire)
     */
    private void supprimerBriques() {
        for (ArrayList<Brique> ligne : grilleBriques) {
            for (Brique brique : ligne) {
                brique.nettoyer(); // Méthode à implémenter dans Brique
            }
            ligne.clear();
        }
    }

    public ArrayList<ArrayList<Brique>> getGrilleBriques() {
        return grilleBriques;
    }
}