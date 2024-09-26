package org.example.Manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static SceneManager getInstance(Stage primaryStage) {
        if (instance == null) {
            instance = new SceneManager(primaryStage);
        }
        return instance;
    }

    public static SceneManager getInstance() {
        return instance;
    }

    public void changeScene(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
