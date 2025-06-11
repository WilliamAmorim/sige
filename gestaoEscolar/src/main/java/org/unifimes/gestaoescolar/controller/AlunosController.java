package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.AlunoDAO;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.Aluno;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.FrequenciaTable;
import org.unifimes.gestaoescolar.model.Turma;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlunosController {

    @FXML
    private Button confirmarCadastro_button;


    @FXML
    private TableView<Aluno> user_tableView;

    @FXML
    private TableColumn<Aluno, String> nome_col;

    @FXML
    private TableColumn<Aluno, String> matricula_col;

    @FXML
    private TableColumn<Aluno, String> nascimento_col;

    @FXML
    private TableColumn<Aluno, String> status_col;

    @FXML
    private TextField matriculaTextField;

    @FXML
    private DatePicker nascimento_textField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private Button pesquisar_button;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<Turma> turma_comboBox;

    @FXML
    private Button voltar_button;

    private int turmaSelecionada = 0;

    @FXML
    private void initialize() {
        HomeApplication scene = new HomeApplication();
        voltar_button.setOnMouseClicked(event -> scene.abrirScene("home"));
        getTurmas();

        statusComboBox.getItems().addAll("Ativo", "Inativo");

        turma_comboBox.setOnAction(event -> {
            Turma turm = turma_comboBox.getSelectionModel().getSelectedItem();
            if (turm != null) {
                turmaSelecionada = turm.getId();
                getAlunosByTurma();
            }
        });
    }

    private void getTurmas(){
        TurmaDAO turmaDAO = new TurmaDAO();
        List<Turma> turmas = turmaDAO.getTurmas();

        ObservableList<Turma> observableTurmas = FXCollections.observableArrayList(turmas);
        turma_comboBox.setItems(observableTurmas);
    }

    private void getAlunosByTurma() {
        System.out.println("Buscando Alunos");
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAlunosByTurma(turmaSelecionada);

        ObservableList<Aluno> dados = FXCollections.observableArrayList();

        dados.addAll(alunos);

        nome_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        matricula_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricula()));
        nascimento_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataNascimento()));
        status_col.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isAtivo()).asString());

        user_tableView.setItems(dados);
    }

    @FXML
    void confirmarCadastro(ActionEvent event) {
        if(turmaSelecionada != 0) {
            String aluno = nomeTextField.getText();
            boolean status = statusComboBox.getSelectionModel().getSelectedItem() == "ativo";
            LocalDate dataSelecionada = nascimento_textField.getValue();

            if (dataSelecionada != null) {
                String dataEmTexto = dataSelecionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (!aluno.isEmpty() || !dataEmTexto.isEmpty()) {
                    Aluno newAluno = new Aluno(0,aluno,null,dataEmTexto,turmaSelecionada+"",status);
                    AlunoDAO alunoDAO = new AlunoDAO();
                    boolean resultado =  alunoDAO.addAluno(newAluno);
                    if (resultado){
                        showAlert("Aviso", "Cadastro OK");
                        getAlunosByTurma();
                    }
                } else {
                    showAlert("Aviso", "Preencha todos os cammpos");
                }
            } else {
                showAlert("Aviso", "Nenhuma data selecionada");
            }
        }else{
            showAlert("Aviso", "Selecione uma turma");
        }

    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
