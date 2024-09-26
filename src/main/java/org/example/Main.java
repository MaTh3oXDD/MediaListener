package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Controller.LoginController;
import org.example.Hibernate.users;
import org.example.Manager.SceneManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main extends Application {
    private static SessionFactory factory;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.getInstance(primaryStage);

        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(users.class)
                .buildSessionFactory();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();


        LoginController loginController = loader.getController();
        loginController.setSessionFactory(factory);


        Scene scene = new Scene(root);
        primaryStage.setTitle("Login App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
