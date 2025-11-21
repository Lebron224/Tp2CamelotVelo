package ca.qc.bdeb.sim.tp2camelotvelo;

import ca.qc.bdeb.sim.tp2camelotvelo.Decor.ArrierePlan;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Camelot;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Maison;
import ca.qc.bdeb.sim.tp2camelotvelo.GameObjects.Particule;
import ca.qc.bdeb.sim.tp2camelotvelo.Utilities.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameManager {
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext gc;
    private Group root;

    private Camelot camelot;
    private Niveau niveauActuel;
    private Camera camera;
    private ArrierePlan arrierePlan;

    private int argent = 0;
    private int numNiveau = 1;
    private boolean modeDebug = false;

    private static final double TEMPS_CHARGEMENT_NIVEAU = 3.0;

    public GameManager(Scene scene, Group root) {
        this.scene = scene;
        this.root = root;
        this.canvas = new Canvas(MainJavaFX.WIDTH, MainJavaFX.HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.root.getChildren().add(canvas);

        // Initialiser le camelot
        this.camelot = new Camelot();

        // Initialiser la caméra
        this.camera = new Camera(camelot);

        // Initialiser l'arrière-plan
        this.arrierePlan = new ArrierePlan();

        this.niveauActuel = new Niveau(numNiveau, camelot);

        root.getChildren().add(camelot.getImgView());

        for (Maison maison : niveauActuel.getMaisons()) {
            root.getChildren().add(maison.getImgView());
            root.getChildren().add(maison.getBoite().getImgView());
            for (var fenetre : maison.getFenetres()) {
                root.getChildren().add(fenetre.getImgView());
            }
        }

        // Ajouter les particules
        if (!niveauActuel.getParticules().isEmpty()) {
            for (Particule particule : niveauActuel.getParticules()) {
                root.getChildren().add(particule.getImgView());
            }
        }

    }

    private void demarrerAnimation(){

    }
}
