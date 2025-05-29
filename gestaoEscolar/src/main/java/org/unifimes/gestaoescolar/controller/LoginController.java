package org.unifimes.gestaoescolar.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.Session;

public class LoginController {

    @FXML
    private TextField cpfField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button loginButton;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String cpf = cpfField.getText().trim();
        String senha = senhaField.getText().trim();

        if (cpf.isEmpty() || senha.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos obrigatórios", "Por favor, preencha CPF e senha.");
            return;
        }

        if (Session.loginDAO(cpf, senha,rememberMe.isSelected())) {
            HomeApplication scene = new HomeApplication();
            scene.abrirScene("home");
        } else {
            showAlert(AlertType.ERROR, "Falha no login", "CPF ou senha inválidos.");
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
