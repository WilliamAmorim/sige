package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.AlunoDAO;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.*;

import java.util.List;

public class DashboarController {
    @FXML
    private TableView<Aluno> alunos_table;

    @FXML
    private TableView<NotaTable> boletim_table;

    @FXML
    private TableColumn<NotaTable, String> disciplina_col;

    @FXML
    private ComboBox<Disciplina> disciplinas_comboBox;

    @FXML
    private TableColumn<Aluno, String> id_col;

    @FXML
    private Label lblAprovados;

    @FXML
    private Label lblMaisFaltas;

    @FXML
    private Label lblReprovados;

    @FXML
    private Label lblTotalAlunos;

    @FXML
    private TableColumn<Aluno, String> matricula_col;

    @FXML
    private TableColumn<NotaTable, Double> media_col;

    @FXML
    private TableColumn<Aluno, String> nascimento_col;

    @FXML
    private TableColumn<Aluno, String> nome_col;

    @FXML
    private TableColumn<NotaTable, Double> nota01_col;

    @FXML
    private TableColumn<NotaTable, Double> nota02_col;

    @FXML
    private TableColumn<NotaTable, Double> nota03_col;

    @FXML
    private TableColumn<NotaTable, Double> nota04_col;

    @FXML
    private PieChart pieChartAprovacao;

    @FXML
    private PieChart pieChartFrequencia;

    @FXML
    private TableColumn<Aluno, String> status_col;

    @FXML
    private ComboBox<Turma> turmas_comboBox;

    @FXML
    private ComboBox<String> semestre_comboBox;

    @FXML
    private Button voltar_button;

    private int turmaSelecionada;
    private int disciplinaSeleciona;
    private int alunoSelecionado;
    private int semestreSelecionado = 0;

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        voltar_button.setOnMouseClicked(event -> openScreen("home"));

        semestre_comboBox.getItems().addAll("1", "2","3","4","5","6","7");
        semestre_comboBox.setOnAction(event -> {
            String semes = semestre_comboBox.getSelectionModel().getSelectedItem();
            if(!semes.isEmpty()){
                semestreSelecionado = Integer.parseInt(semes);
                preencherCabecalho();
            }
        });

        getTurmas();
        turmas_comboBox.setOnAction(event -> {
            Turma turmaSelec = turmas_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelec != null) {
                turmaSelecionada = turmaSelec.getId();
                getDisciplinasPorTurma(turmaSelec.getId());
                getAlunosByTurma();
                preencherCabecalho();
            }
        });

        disciplinas_comboBox.setOnAction(event -> {
            Disciplina disciplinaSelect = disciplinas_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelecionada > 0) {
                //Buscar Alunos
                disciplinaSeleciona = disciplinaSelect.getId();
                getBoletim();
                preencherCabecalho();
            }
        });

        alunos_table.setOnMouseClicked(event -> {
            Aluno selectedUser = alunos_table.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                //preencherFormulario(selectedUser);
                alunoSelecionado = selectedUser.getId();
                getBoletim();
            }
        });

    }

    private void preencherCabecalho(){
        AlunoDAO alunoDAO = new AlunoDAO();
        String[] dados = alunoDAO.getTotalAlunos(turmaSelecionada,semestreSelecionado,disciplinaSeleciona);
        lblTotalAlunos.setText(dados[0]);
        lblAprovados.setText(dados[1]);
        lblReprovados.setText(dados[2]);

        pieChartAprovacao.getData().clear();
        pieChartData.add(new PieChart.Data("Aprovados", Double.parseDouble(dados[1])));
        pieChartData.add(new PieChart.Data("Reprovados", Double.parseDouble(dados[2])));
        pieChartAprovacao.setData(pieChartData);

    }

    private void getTurmas(){
        TurmaDAO turmaDAO = new TurmaDAO();
        List<Turma> turmas = turmaDAO.getTurmas();

        ObservableList<Turma> observableTurmas = FXCollections.observableArrayList(turmas);
        turmas_comboBox.setItems(observableTurmas);
    }

    private void getDisciplinasPorTurma(int id){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinas = disciplinaDAO.getDisciplinasByTurmaId(id);

        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinas);
        disciplinas_comboBox.setItems(observableDisciplina);
    }

    private void getAlunosByTurma() {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAlunosByTurma(turmaSelecionada);

        ObservableList<Aluno> dados = FXCollections.observableArrayList();

        dados.addAll(alunos);

        id_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asString());
        nome_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        matricula_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricula()));
        nascimento_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataNascimento()));
        status_col.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isAtivo()).asString());

        alunos_table.setItems(dados);

    }

    private void getBoletim(){
        AlunoDAO alunoDAO = new AlunoDAO();
        List<NotaTable> listaNotas = alunoDAO.getAlunoBoletimByTurma(turmaSelecionada,alunoSelecionado,semestreSelecionado);

        ObservableList<NotaTable> dados = FXCollections.observableArrayList();

        dados.addAll(listaNotas);

        disciplina_col.setCellValueFactory(new PropertyValueFactory<>("nome"));
        media_col.setCellValueFactory(new PropertyValueFactory<>("media"));
        nota01_col.setCellValueFactory(new PropertyValueFactory<>("nota01"));
        nota02_col.setCellValueFactory(new PropertyValueFactory<>("nota02"));
        nota03_col.setCellValueFactory(new PropertyValueFactory<>("nota03"));
        nota04_col.setCellValueFactory(new PropertyValueFactory<>("nota04"));

        boletim_table.setItems(dados);
    }



    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }
}
