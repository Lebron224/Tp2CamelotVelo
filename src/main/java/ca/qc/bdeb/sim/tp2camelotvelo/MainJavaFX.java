package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class MainJavaFX extends Application {
    public static double WIDTH = 900 , HEIGHT = 580;

    @Override
    public void start(Stage primaryStage) {
        var root = new Pane();
        var scene = new Scene(root, WIDTH, HEIGHT);

        root.setBackground(Background.fill(Color.BLACK));

        new GameManager(scene, root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Camelot à vélo!");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}