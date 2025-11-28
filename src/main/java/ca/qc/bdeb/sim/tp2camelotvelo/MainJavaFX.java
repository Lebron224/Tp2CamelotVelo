package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



/**
 * Classe principale de l'application JavaFX.
 * Initialise la fenêtre, la scène et lance le jeu.
 */
public class MainJavaFX extends Application {

    /** Largeur de la fenêtre du jeu (en pixels). */
    public static double WIDTH = 900;

    /** Hauteur de la fenêtre du jeu (en pixels). */
    public static double HEIGHT = 580;

    /**
     * Point d'entrée de l'application JavaFX.
     * Initialise la fenêtre, la scène et le gestionnaire de jeu.
     *
     * @param primaryStage fenêtre principale de JavaFX
     */
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setResizable(false); // Empêche le redimensionnement de la fenêtre

        var root = new Pane(); // Conteneur principal
        var scene = new Scene(root, WIDTH, HEIGHT); // Scène principale avec dimensions

        root.setBackground(Background.fill(Color.BLACK)); // Fond noir

        new GameManager(scene, root); // Crée et initialise le jeu

        primaryStage.setScene(scene);      // Associe la scène à la fenêtre
        primaryStage.setTitle("Camelot à vélo!"); // Titre de la fenêtre
        primaryStage.show();              // Affiche la fenêtre
    }

    /**
     * Méthode principale qui lance l'application JavaFX.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch(); // Lance l'application JavaFX
    }
}