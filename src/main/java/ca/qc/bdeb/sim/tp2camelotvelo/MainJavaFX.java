package ca.qc.bdeb.sim.tp2camelotvelo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;



public class MainJavaFX extends Application {
    private int niveau;
    public static double WIDTH = 900 , HEIGHT = 580;
    private Stage stage;

    @Override
    public void start(Stage primaryStage){
        this.stage = primaryStage;
        var scene = sceneNiveau();


        scene.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        }));

        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Camelot à vélo!");
        primaryStage.show();
    }

    private Scene sceneNiveau(){
        var root = new StackPane();
        var scene = new Scene(root, WIDTH, HEIGHT);
        root.setAlignment(Pos.CENTER);
        root.setBackground(Background.fill(Color.BLACK));

        var textNiveau = new Text("Niveau "+ niveau);
        textNiveau.setFont(Font.font(70));
        textNiveau.setFill(Color.GREEN);
        textNiveau.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().add(textNiveau);


        return scene;
    }

    private Scene sceneJeu(){
        var root = new BorderPane();
        var scene = new Scene(root, WIDTH, HEIGHT);

        return scene;
    }



    public static void main(String[] args) {
        launch();
    }
}