package ca.qc.bdeb.sim.tp2camelotvelo;

import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Maison;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Particule;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Représente un niveau du jeu, contenant des maisons et des particules.
 * Gère la logique liée au niveau, comme sa largeur, les maisons abonnées,
 * le champ électrique et la création de particules.
 */
public class Niveau {

    /** Générateur aléatoire pour créer les maisons et particules. */
    private final Random rnd = new Random();

    /** Liste des maisons présentes dans le niveau. */
    private final ArrayList<Maison> maisons = new ArrayList<>();

    /** Liste des particules électriques présentes dans le niveau. */
    private final ArrayList<Particule> particules = new ArrayList<>();

    /** Numéro du niveau. */
    public int niveau;

    /**
     * Constructeur du niveau.
     *
     * @param niveau numéro du niveau à créer
     */
    public Niveau(int niveau) {
        this.niveau = niveau;
        initialiserNiveau(); // Initialise maisons et particules
    }

    /**
     * Initialise le niveau en créant les maisons et les particules.
     */
    private void initialiserNiveau() {
        maisons.clear(); // Vide la liste des maisons

        int addresseDepart = 100 + rnd.nextInt(851); // Adresse de départ aléatoire

        // Création de 12 maisons espacées
        for (int i = 0; i < 12; i++) {
            maisons.add(new Maison(addresseDepart + (i * 2), 1300 * (i + 1)));
        }

        creerParticules(); // Crée les particules du niveau
    }

    /**
     * Crée les particules électriques en fonction du niveau.
     */
    public void creerParticules() {
        particules.clear(); // Vide la liste des particules

        if (niveau >= 2) {
            int nbrParticules = Math.min((niveau - 1) * 30, 400); // Limite max 400
            for (int i = 0; i < nbrParticules; i++) {
                particules.add(new Particule(new Point2D(
                        rnd.nextDouble(0, getLargeurNiveau()),
                        rnd.nextDouble(0, MainJavaFX.HEIGHT))
                ));
            }
        }
    }

    /**
     * Vérifie si le niveau est terminé selon la position de Camelot.
     *
     * @param positionCamelot position actuelle du joueur
     * @return true si Camelot a dépassé la limite du niveau
     */
    public boolean estTermine(Point2D positionCamelot) {
        if (maisons.isEmpty())
            return false; // sécurité si aucune maison

        Maison derniereMaison = maisons.get(maisons.size() - 1);

        double limite = derniereMaison.getPosition().getX() + (1.5 * MainJavaFX.WIDTH);

        return positionCamelot.getX() >= limite;
    }

    /**
     * Calcule le champ électrique total en un point donné,
     * en sommant les contributions de toutes les particules.
     *
     * @param position position où calculer le champ
     * @return vecteur champ électrique total
     */
    public Point2D champElectriqueTousParticule(Point2D position) {
        var champElectrique = Point2D.ZERO;

        for (var p : particules) {
            champElectrique = champElectrique.add(p.champEn(position));
        }

        return champElectrique;
    }

    /**
     * Crée des particules de test disposées en deux lignes,
     * une en haut et une en bas du niveau.
     */
    public void creerParticulesTest() {
        particules.clear();


        if (niveau >= 2) {

            // ✅ Première ligne en haut
            for (double x = 50; x < getLargeurNiveau(); x += 50) {
                particules.add(new Particule(new Point2D(x, 10)));
            }

            // ✅ Deuxième ligne en bas
            for (double x = 50; x < getLargeurNiveau(); x += 50) {
                particules.add(new Particule(new Point2D(x, MainJavaFX.HEIGHT - 10)));
            }
        }
    }

    /**
     * Récupère toutes les maisons ayant un abonnement.
     *
     * @return liste des maisons abonnées
     */
    public ArrayList<Maison> getMaisonsAbonnees() {
        var maisonsAbonnees = new ArrayList<Maison>();
        for (var m : maisons) {
            if (m.aUnAbonnement()) {
                maisonsAbonnees.add(m);
            }
        }

        return maisonsAbonnees;
    }

    /**
     * Retourne la liste de toutes les maisons du niveau.
     *
     * @return liste des maisons
     */
    public ArrayList<Maison> getMaisons() {
        return maisons;
    }

    /**
     * Retourne la liste de toutes les particules du niveau.
     *
     * @return liste des particules
     */
    public ArrayList<Particule> getParticules() {
        return particules;
    }

    /**
     * Retourne la largeur totale du niveau, basée sur la dernière maison.
     *
     * @return largeur du niveau en pixels
     */
    public double getLargeurNiveau() {

        // Si aucune maison → largeur minimale
        if (maisons.isEmpty())
            return MainJavaFX.WIDTH;

        // x de la dernière maison
        var derniere = maisons.get(maisons.size() - 1);

        return derniere.getPosition().getX() + MainJavaFX.WIDTH * 1.5;
    }
}

