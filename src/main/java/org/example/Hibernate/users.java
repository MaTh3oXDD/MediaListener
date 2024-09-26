package org.example.Hibernate;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login", length = 255, nullable = false)
    private String login;
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    public users() {
    }

    public users(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public users(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
