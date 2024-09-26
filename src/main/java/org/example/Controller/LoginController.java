package org.example.Controller;

import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Hibernate.users;
import org.example.Manager.SceneManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.io.IOException;


public class LoginController {
    @FXML
    private Button LoginButton;
    @FXML
    private Button RegisterButton;
    @FXML
    private TextField LoginField;
    @FXML
    private TextField PasswordField;
    private SessionFactory factory;
    @FXML
    private void initialize() {

        RegisterButton.setOnAction(event -> handleRegister());
        LoginButton.setOnAction(event -> handleLogin());
    }
    public void setSessionFactory(SessionFactory sessionFactory){
        this.factory = sessionFactory;
    }
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            Parent root = loader.load();

            RegisterController registerController = loader.getController();
            registerController.setSessionFactory(factory);

            Scene scene = new Scene(root);
            Stage stage = SceneManager.getInstance().getPrimaryStage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleLogin() {
        String username = LoginField.getText();
        String password = PasswordField.getText();
        if (factory == null) {
            System.out.println("SessionFactory is not initialized.");
            return;
        }
        if (checkCredentials(username, password)) {
            SceneManager.getInstance().changeScene("/view/main.fxml");
        } else {
            System.out.println("Invalid username or password");
        }
    }

    public boolean checkCredentials(String login, String password) {
        Session session = factory.openSession();
        boolean isValid = false;

        try {
            session.beginTransaction();

            TypedQuery<users> query = session.createQuery(
                    "FROM users WHERE login = :login AND password = :password", users.class);
            query.setParameter("login", login);
            query.setParameter("password", password);

            users user = null;
            try {
                user = query.getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                System.out.println("Invalid username or password");
                return false;
            }

            if (user != null) {
                isValid = true;
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }

        return isValid;

    }
}
