package org.unifimes.gestaoescolar.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;

import java.util.List;

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
    private ComboBox<Disciplina> comboTurmas;

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
        getDisciplinas();
    }

    private void getDisciplinas(){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinas = disciplinaDAO.findDisciplina(false);

        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinas);
        comboTurmas.setItems(observableDisciplina);
    }

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }
}
