package ca.qc.bdeb.sim.tp2camelotvelo;

import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Maison;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Particule;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

public class Niveau {
    private final GraphicsContext gc;
    private final Random rnd = new Random();
    private final ArrayList<Maison> maisons = new ArrayList<>();
    private final ArrayList<Particule> particules = new ArrayList<>();
    public int niveau;
    
    public Niveau(int niveau) {
        this.niveau = niveau;
        Canvas canvas = new Canvas(getLargeurNiveau(), MainJavaFX.HEIGHT);
        this.gc = canvas.getGraphicsContext2D();

        initialiserNiveau();
    }

    private void initialiserNiveau() {
        maisons.clear();

        int addresseDepart = 100 + rnd.nextInt(851);
        for (int i = 0; i < 12; i++) {
            maisons.add(new Maison(addresseDepart  + (i*2), 1300 * (i+1)));
        }

        particules.clear();
        if (niveau >= 2) {
            int nbrParticules = Math.min((niveau - 1) * 30, 400);
            for (int i = 0; i < nbrParticules; i++) {
                particules.add(new Particule(new Point2D(
                        rnd.nextDouble(0, MainJavaFX.WIDTH),
                        rnd.nextDouble(0, MainJavaFX.HEIGHT)),
                        gc
                ));
            }
        }
    }

    public boolean estTermine(Point2D positionCamelot) {
        if (maisons.isEmpty())
            return false; // sécurité

        Maison derniereMaison = maisons.get(maisons.size() - 1);

        double limite = derniereMaison.getPosition().getX() + (1.5 * MainJavaFX.WIDTH);

        return positionCamelot.getX() >= limite;
    }


    public Point2D champElectriqueTousParticule(Point2D position){
        var champElectrique = Point2D.ZERO;

        for (var p : particules) {
            champElectrique = champElectrique.add(p.champEn(position));
        }

        return champElectrique;
    }

    public void creerParticulesTest(){
        particules.clear();

        // ✅ Première ligne en haut
        for (double x = 50; x < MainJavaFX.WIDTH; x += 50) {
            particules.add(new Particule(new Point2D(x, 10), gc));
        }

        // ✅ Deuxième ligne en bas
        for (double x = 50; x < MainJavaFX.WIDTH; x += 50) {
            particules.add(new Particule(new Point2D(x, MainJavaFX.HEIGHT - 10), gc));
        }
    }

    public ArrayList<Maison> getMaisonsAbonnees() {
        var maisonsAbonnees = new ArrayList<Maison>();
        for (var m : maisons) {
            if (m.aUnAbonnement())  {
                maisonsAbonnees.add(m);
            }
        }

        return maisonsAbonnees;
    }

    public ArrayList<Maison> getMaisons() {
        return maisons;
    }

    public ArrayList<Particule> getParticules() {
        return particules;
    }

    public double getLargeurNiveau(){

        // Si aucune maison → largeur minimale
        if (maisons.isEmpty())
            return MainJavaFX.WIDTH;

        // x de la dernière maison
        Maison derniere = maisons.get(maisons.size() - 1);

        return derniere.getPosition().getX() + MainJavaFX.WIDTH * 1.5;
    }
}
