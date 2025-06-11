package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.AlunoDAO;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.FrequenciaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class NotasController {

    @FXML
    private Label aluno_label;

    @FXML
    private TableView<NotaTable> alunos_table;

    @FXML
    private Button confirmarNota;

    @FXML
    private ComboBox<Disciplina> disciplina_comboBox;

    @FXML
    private TableColumn<NotaTable, Double> media_col;

    @FXML
    private TableColumn<NotaTable, String> nome_col;

    @FXML
    private TableColumn<NotaTable, Double> nota01_col;

    @FXML
    private TableColumn<NotaTable, Double> nota02_col;

    @FXML
    private TableColumn<NotaTable, Double> nota03_col;

    @FXML
    private TableColumn<NotaTable, Double> nota04_col;

    @FXML
    private TextField nota_textField;

    @FXML
    private ComboBox<Turma> turma_comboBox;

    @FXML
    private Button voltar_button;

    private int turmaSelecionada = 0;
    private int disciplinaSeleciona = 0;
    private NotaTable alunoSelecionadoTable;
    private Turma turmaObj;

    @FXML
    private void initialize() {
        voltar_button.setOnMouseClicked(event -> openScreen("home"));
        getTurmas();
        turma_comboBox.setOnAction(event -> {
            Turma turmaSelecionada = turma_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelecionada != null) {
                turmaObj = turmaSelecionada;
                getDisciplinasPorTurma(turmaSelecionada.getId());
            }
        });

        disciplina_comboBox.setOnAction(event -> {
            Disciplina disciplinaSelect = disciplina_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelecionada > 0) {

                disciplinaSeleciona = disciplinaSelect.getId();

                getAlunosByTurma();
            }
        });

        alunos_table.setOnMouseClicked(event -> {
            NotaTable selectedUser = alunos_table.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                //preencherFormulario(selectedUser);
                aluno_label.setText("Aluno: "+selectedUser.getNome());
                alunoSelecionadoTable = selectedUser;
            }
        });

        confirmarNota.setOnAction(actionEvent -> confirmarNota());
    }

    private void confirmarNota(){
        if(turmaSelecionada != 0 && disciplinaSeleciona != 0) {
            if (alunoSelecionadoTable.getNota01() > 0 && alunoSelecionadoTable.getNota02() > 0 && alunoSelecionadoTable.getNota03() > 0 && alunoSelecionadoTable.getNota04() > 0) {
                showAlert("Aluno Invalido", "Todas as notas já foram lançadas");
            } else {
                double notaValor = parseDouble(nota_textField.getText());
                if (notaValor > 0 && notaValor < 10) {
                    Nota nota = new Nota(0, alunoSelecionadoTable.getId(), disciplinaSeleciona, getBimestreAtualDesde(turmaObj.getAno()), notaValor);
                    AlunoDAO alunosDAO = new AlunoDAO();
                    boolean resultado = alunosDAO.addAlunoNota(nota);
                    if (resultado) {
                        showAlert("Cadastro Realizado com sucesso", "Nova nota atribuida");
                        getAlunosByTurma();
                        nota_textField.clear();
                        aluno_label.setText("Aluno:");
                    }
                } else {
                    showAlert("Nota Invalida", "A nota deve ser no maximo 10");
                }
            }
        }else {showAlert("Error", "Selecione alguma turma e disciplina");}
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

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }

    private void getAlunosByTurma() {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<NotaTable> listaNotas = alunoDAO.getAlunosNotaByTurma(turmaSelecionada,disciplinaSeleciona);

        ObservableList<NotaTable> dados = FXCollections.observableArrayList();

        dados.addAll(listaNotas);

        nome_col.setCellValueFactory(new PropertyValueFactory<>("nome"));
        media_col.setCellValueFactory(new PropertyValueFactory<>("media"));
        nota01_col.setCellValueFactory(new PropertyValueFactory<>("nota01"));
        nota02_col.setCellValueFactory(new PropertyValueFactory<>("nota02"));
        nota03_col.setCellValueFactory(new PropertyValueFactory<>("nota03"));
        nota04_col.setCellValueFactory(new PropertyValueFactory<>("nota04"));

        alunos_table.setItems(dados);
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static int getBimestreAtualDesde(int anoInicioCurso) {
        LocalDate hoje = LocalDate.now();
        int anoAtual = hoje.getYear();
        int mesAtual = hoje.getMonthValue();

        // Diferença de anos * 12 meses + meses atuais = total de meses desde o início
        int mesesDesdeInicio = (anoAtual - anoInicioCurso) * 12 + mesAtual;

        // Cada bimestre tem 3 meses, então:
        int bimestreAtual = (int) Math.ceil(mesesDesdeInicio / 3.0);

        return bimestreAtual;
    }
}

