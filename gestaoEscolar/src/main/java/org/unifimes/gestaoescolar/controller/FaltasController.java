package org.unifimes.gestaoescolar.controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.AlunoDAO;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.FrequenciaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.*;

import java.util.ArrayList;
import java.util.List;

public class FaltasController {

    @FXML
    private ComboBox<Disciplina> disciplina_comboBox;

    @FXML
    private Button finalizar_button;

    @FXML
    private TableView<FrequenciaTable> tabela_alunos;

    @FXML
    private TableColumn<FrequenciaTable, String> nomeCol;

    @FXML
    private TableColumn<FrequenciaTable, String> matriculaCol;

    @FXML
    private TableColumn<FrequenciaTable, Boolean> falta_col;

    @FXML
    private ComboBox<Turma> turma_comboBox;

    @FXML
    private Button voltar_button;

    private int turmaSelecionada;
    private int disciplinaSeleciona;

    @FXML
    private void initialize() {
        voltar_button.setOnMouseClicked(event -> openScreen("home"));
        getTurmas();
        turma_comboBox.setOnAction(event -> {
            Turma turmaSelecionada = turma_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelecionada != null) {
                getDisciplinasPorTurma(turmaSelecionada.getId());
            }
        });

        disciplina_comboBox.setOnAction(event -> {
            Disciplina disciplinaSelect = disciplina_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelecionada > 0) {
                //Buscar Alunos
                disciplinaSeleciona = disciplinaSelect.getId();
                getAlunosByTurma();
            }
        });

        finalizar_button.setOnAction(event -> {
            ObservableList<FrequenciaTable> lista = tabela_alunos.getItems();
            List<Frequencia> frequencias = new ArrayList<>();

            for (FrequenciaTable aluno : lista) {
                Frequencia frequencia = new Frequencia(0,aluno.getID(),disciplinaSeleciona,aluno.isFalta(),null);
                frequencias.add(frequencia);
            }

            FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
            boolean isFrequencia = frequenciaDAO.isFrequencia(turmaSelecionada,disciplinaSeleciona);
            if(isFrequencia) {
                boolean resultado = frequenciaDAO.addMultipleFrequencia(frequencias);
                if (resultado) {
                    showAlert("Tudo ok", "Cadastro OK");
                } else {
                    showAlert("Ocorreu um error", "Não foi possivel finalizar a frequencia");
                }
            }else{
                showAlert("Alerta", "Frequencia já foi realizada");
            }

        });

    }

    private void getTurmas(){
        TurmaDAO turmaDAO = new TurmaDAO();
        List<Turma> turmas = turmaDAO.getTurmas();

        ObservableList<Turma> observableTurmas = FXCollections.observableArrayList(turmas);
        turma_comboBox.setItems(observableTurmas);
    }

    private void getDisciplinasPorTurma(int id){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinas = disciplinaDAO.getDisciplinasByTurmaId(id);
        turmaSelecionada = id;
        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinas);
        disciplina_comboBox.setItems(observableDisciplina);
    }

    private void getAlunosByTurma() {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAlunosByTurma(turmaSelecionada);

        ObservableList<FrequenciaTable> dados = FXCollections.observableArrayList();

        for (Aluno aluno : alunos) {
            dados.add(new FrequenciaTable(aluno.getId(),aluno.getNome(), aluno.getMatricula()));
        }

        nomeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        matriculaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricula()));
        falta_col.setCellValueFactory(cellData -> cellData.getValue().faltaProperty());

        falta_col.setCellFactory(tc -> new javafx.scene.control.cell.CheckBoxTableCell<>());

        tabela_alunos.setItems(dados);
    }

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}

