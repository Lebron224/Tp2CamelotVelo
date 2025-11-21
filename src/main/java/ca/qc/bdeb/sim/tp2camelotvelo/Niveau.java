package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

public class Niveau {
    private Canvas canvas;
    private GraphicsContext gc;
    private final Random rnd = new Random();
    private ArrayList<Maison> maisons = new ArrayList<>();
    private ArrayList<Particule> particules = new ArrayList<>();
    private ArrayList<Journaux> journaux = new ArrayList<>();
    private final Camelot camelot;
    public int niveau;
    
    public Niveau(int niveau,  Camelot camelot) {
        this.niveau = niveau;
        this.camelot = camelot;
        this.canvas = new Canvas(maisons.getLast().position.getX() + (1.5 * MainJavaFX.WIDTH), MainJavaFX.HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        initialiserNiveau();
    }

    private void initialiserNiveau() {
        maisons.clear();

        int addresseDepart = 100 + rnd.nextInt(851);
        for (int i = 0; i < 12; i++) {
            maisons.add(new Maison(addresseDepart  + (i*2), 1300 * (i+1)));
        }

        for (int i = 0; i < 12; i++) {
            journaux.add(new Journaux(camelot));
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

    private void verificationCollision() {
        for (int i = 0; i < journaux.size(); i++) {
            Journaux j = journaux.get(i);
            for (Maison m : maisons) {
                var fen = m.getFenetres();
                var boite = m.getBoite();
                if (j.collision(boite)) {
                    journaux.remove(j);
                }
                for (var f : fen) {
                    if (j.collision(f)) {
                        journaux.remove(j);
                    }
                }
            }
        }
    }

    public boolean estTermine(Point2D positionCamelot) {
        Maison derniereMaison = maisons.get(maisons.size() - 1);
        double limite = derniereMaison.getPosition().getX() + (1.5 * MainJavaFX.WIDTH);
        return positionCamelot.getX() >= limite;
    }



    public int getNiveau() {
        return niveau;
    }

    public ArrayList<Maison> getMaisons() {
        return maisons;
    }

    public ArrayList<Particule> getParticules() {
        return particules;
    }

    public ArrayList<Journaux> getJournaux() {
        return journaux;
    }
}
