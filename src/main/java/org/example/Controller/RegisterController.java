package org.example.Controller;

import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.Hibernate.users;
import org.example.Manager.SceneManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class RegisterController {
    @FXML
    private TextField LoginField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField PasswordRepeatField;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button BackButton;

    private SessionFactory factory;
    public void setSessionFactory(SessionFactory sessionFactory){
        this.factory = sessionFactory;
    }
    @FXML
    public void initialize()
    {
        RegisterButton.setOnAction(event -> handleRegisterButton());
        BackButton.setOnAction(event -> handleBackButton());
    }
    public void handleBackButton()
    {
        SceneManager.getInstance().changeScene("/view/login.fxml");
    }
    public void handleRegisterButton()
    {
        String login = LoginField.getText();
        String password = PasswordField.getText();
        String passwordRepeat = PasswordRepeatField.getText();

        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            users newUser = new users(login, password);

            session.save(newUser);

            transaction.commit();

            System.out.println("User successfully added to the database.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


}
