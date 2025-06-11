package org.unifimes.gestaoescolar.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;

import java.util.List;

public class TurmasController {


    @FXML
    private ComboBox<?> anoComboBox;

    @FXML
    private TableColumn<?, ?> ano_col;

    @FXML
    private TableColumn<?, ?> codigo_col;

    @FXML
    private Button confirmarCadastro_button;

    @FXML
    private ComboBox<Disciplina> disciplinaComboBox;

    @FXML
    private ListView<Disciplina> disciplinasListView;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TableColumn<?, ?> nome_col;

    @FXML
    private Button pesquisar_button;

    @FXML
    private ComboBox<?> turnoComboBox;

    @FXML
    private TableColumn<?, ?> turno_col;

    @FXML
    private TableView<?> user_tableView;

    @FXML
    private Button voltar_button;

    private final ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        showDisciplinas();

        disciplinasListView.setItems(disciplinas);
        disciplinasListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Disciplina disciplinaSelecionada = disciplinasListView.getSelectionModel().getSelectedItem();
                if (disciplinaSelecionada != null) {
                    disciplinas.remove(disciplinaSelecionada);
                }
            }
        });
    }

    @FXML
    void confirmarCadastro(ActionEvent event) {

    }


    @FXML
    private void adicionarDisciplina() {
        Disciplina disciplina = disciplinaComboBox.getValue();
        if (disciplina != null && !disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    private void showDisciplinas() {
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinas = disciplinaDAO.getDisciplinas();

        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinas);
        disciplinaComboBox.setItems(observableDisciplina);
    }


}
