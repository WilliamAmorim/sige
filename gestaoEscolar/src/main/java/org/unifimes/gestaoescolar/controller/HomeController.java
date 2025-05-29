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
    private VBox sair_vbox;

    @FXML
    private void initialize() {
        HomeApplication scene = new HomeApplication();
        dashboad_vbox.setOnMouseClicked(event -> openScreen("dashboard"));

        usuarios_vbox.setOnMouseClicked(Mouse -> scene.abrirScene("usuarios"));

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
