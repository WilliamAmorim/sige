package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.AlunoDAO;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.FrequenciaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.*;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboarController {
    @FXML
    private Button limpar_action;

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
    private BarChart<String, Number> frequencia_chart;

    @FXML
    private TableColumn<Aluno, String> status_col;

    @FXML
    private ComboBox<Turma> turmas_comboBox;

    @FXML
    private ComboBox<String> semestre_comboBox;

    @FXML
    private Button voltar_button;

    @FXML
    private Button atualizar_action;

    private int turmaSelecionada;
    private int disciplinaSeleciona;
    private int alunoSelecionado;
    private int semestreSelecionado = 0;

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        atualizar_action.setOnMouseClicked(event -> atualizar());

        voltar_button.setOnMouseClicked(event -> openScreen("home"));

        limpar_action.setOnMouseClicked(event -> limpar());

        semestre_comboBox.getItems().addAll("1", "2","3","4","5","6","7");
        semestre_comboBox.setOnAction(event -> {
            String semes = semestre_comboBox.getSelectionModel().getSelectedItem();
            if(!semes.isEmpty()){
                    semestreSelecionado = Integer.parseInt(semes);
                    preencherCabecalho();
                    getFrequencia();
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
                    getFrequencia();
            }
        });

        disciplinas_comboBox.setOnAction(event -> {
            Disciplina disciplinaSelect = disciplinas_comboBox.getSelectionModel().getSelectedItem();
                    disciplinaSeleciona = disciplinaSelect.getId();
                    getBoletim();
                    preencherCabecalho();
                    getFrequencia();
        });

        alunos_table.setOnMouseClicked(event -> {
            Aluno selectedUser = alunos_table.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                //preencherFormulario(selectedUser);
                alunoSelecionado = selectedUser.getId();
                getBoletim();
            }
        });

        nota01_col.setCellValueFactory(new PropertyValueFactory<>("nota01"));
        nota02_col.setCellValueFactory(new PropertyValueFactory<>("nota02"));
        nota03_col.setCellValueFactory(new PropertyValueFactory<>("nota03"));
        nota04_col.setCellValueFactory(new PropertyValueFactory<>("nota04"));
        media_col.setCellValueFactory(new PropertyValueFactory<>("media"));

        TableCellFactoryComCor(nota01_col);
        TableCellFactoryComCor(nota02_col);
        TableCellFactoryComCor(nota03_col);
        TableCellFactoryComCor(nota04_col);
        TableCellFactoryComCor(media_col);

    }

    private void limpar(){
        turmas_comboBox.getSelectionModel().clearSelection();
        disciplinas_comboBox.getSelectionModel().clearSelection();
        semestre_comboBox.getSelectionModel().clearSelection();

        turmas_comboBox.setItems(FXCollections.observableArrayList());
        disciplinas_comboBox.setItems(FXCollections.observableArrayList());
        semestre_comboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7"));

        alunos_table.getItems().clear();
        boletim_table.getItems().clear();
        frequencia_chart.getData().clear();
        pieChartAprovacao.getData().clear();

        lblTotalAlunos.setText("0");
        lblAprovados.setText("0");
        lblReprovados.setText("0");

        turmaSelecionada = 0;
        disciplinaSeleciona = 0;
        alunoSelecionado = 0;
        semestreSelecionado = 0;

        getTurmas();
    }


    private void atualizar(){
        semestre_comboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7"));

        getTurmas(); // Primeiro recupera as turmas

        // Só atualiza os dados se os valores estiverem selecionados
        if (turmaSelecionada > 0) {
            getAlunosByTurma();
            getDisciplinasPorTurma(turmaSelecionada);
        }

        if (semestreSelecionado > 0 && disciplinaSeleciona > 0) {
            getFrequencia();
        }

        if (turmaSelecionada > 0 && semestreSelecionado > 0 && disciplinaSeleciona > 0) {
            preencherCabecalho();
        }
    }


    private void TableCellFactoryComCor(TableColumn<NotaTable, Double> column) {
        column.setCellFactory(col -> new TableCell<NotaTable, Double>() {
            @Override
            protected void updateItem(Double nota, boolean empty) {
                super.updateItem(nota, empty);
                if (empty || nota == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.1f", nota));
                    if (nota >= 6.0) {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-background-color: #f8d7da; -fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void preencherCabecalho(){
        AlunoDAO alunoDAO = new AlunoDAO();
        String[] dados;
        if(disciplinaSeleciona == 0){
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
            dados  = alunoDAO.getTotalAlunos(turmaSelecionada, semestreSelecionado, disciplinaDAO.getDisciplinasByTurmaId(turmaSelecionada),alunoDAO.getAlunosByTurma(turmaSelecionada));
        }else{
            dados = alunoDAO.getTotalAlunos(turmaSelecionada, semestreSelecionado, disciplinaSeleciona);
        }


        lblTotalAlunos.setText(dados[0]);
        lblAprovados.setText(dados[1]);
        lblReprovados.setText(dados[2]);

        pieChartData.clear(); // Limpa os dados anteriores
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

    private void getFrequencia(){
        FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
        List<FrequenciaChart> frequenciaChart = frequenciaDAO.getGraficoFrequencia(turmaSelecionada,disciplinaSeleciona,semestreSelecionado);
        preencherFrequenciaChart(frequenciaChart);
    }

    public void preencherFrequenciaChart(List<FrequenciaChart> dados) {
        // Limpa o gráfico
        frequencia_chart.getData().clear();

        // Mapa para agrupar as séries por bimestre
        Map<Integer, XYChart.Series<String, Number>> seriesPorBimestre = new HashMap<>();

        for (FrequenciaChart fc : dados) {
            // Cria a série se não existir
            seriesPorBimestre.putIfAbsent(fc.getBimestre(), new XYChart.Series<>());
            XYChart.Series<String, Number> serie = seriesPorBimestre.get(fc.getBimestre());

            serie.setName("Bimestre " + fc.getBimestre());
            serie.getData().add(new XYChart.Data<>(fc.getNome_aluno(), fc.getTotal_faltas()));
        }

        // Adiciona todas as séries no gráfico
        frequencia_chart.getData().addAll(seriesPorBimestre.values());
    }

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }
}
