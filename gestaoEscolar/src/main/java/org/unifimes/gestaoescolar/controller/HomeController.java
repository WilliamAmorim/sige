package org.unifimes.gestaoescolar.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.Session;

public class HomeController {
    @FXML
    private VBox dashboad_vbox;

    @FXML
    private VBox usuarios_vbox;

    @FXML
    private VBox alunos_vbox;

    @FXML
    private VBox sair_vbox;

    @FXML
    private VBox vbox_faltas;


    @FXML
    private VBox notas_vbox;

    @FXML
    private void initialize() {
        HomeApplication scene = new HomeApplication();
        dashboad_vbox.setOnMouseClicked(event -> openScreen("dashboard"));

        usuarios_vbox.setOnMouseClicked(Mouse -> scene.abrirScene("usuarios"));

        alunos_vbox.setOnMouseClicked(Mouse -> scene.abrirScene("alunos"));

        vbox_faltas.setOnMouseClicked(Mouse -> scene.abrirScene("faltas"));

        notas_vbox.setOnMouseClicked(Mouse -> scene.abrirScene("notas"));

        sair_vbox.setOnMouseClicked(event -> closeSessionScreen());

    }

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }

    private void closeSessionScreen() {
        Session.logout();
        System.exit(0);

    }
}
