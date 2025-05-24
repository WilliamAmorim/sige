package org.unifimes.gestaoescolar.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.unifimes.gestaoescolar.HomeApplication;

public class DashboarController {
    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private TableColumn<?, ?> colFaltas;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableColumn<?, ?> colSituacao;

    @FXML
    private ComboBox<String> comboTurmas;

    @FXML
    private Label lblAprovados;

    @FXML
    private Label lblMaisFaltas;

    @FXML
    private Label lblReprovados;

    @FXML
    private Label lblTotalAlunos;

    @FXML
    private TableView<?> tableAlunos;

    @FXML
    private Button voltar_button;

    @FXML
    private void initialize() {
        voltar_button.setOnMouseClicked(event -> openScreen("home"));

        comboTurmas.getItems().addAll("1ºA", "2ºB", "3ºC");
        comboTurmas.getSelectionModel().selectFirst();
    }

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }
}
