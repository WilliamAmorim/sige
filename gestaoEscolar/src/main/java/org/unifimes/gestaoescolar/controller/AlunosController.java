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
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.Aluno;
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
    private Aluno alunoSelecionado = null;

    @FXML
    private void initialize() {
        HomeApplication scene = new HomeApplication();
        voltar_button.setOnMouseClicked(event -> scene.abrirScene("home"));

        getTurmas();
        statusComboBox.getItems().addAll("Ativo", "Inativo");

        turma_comboBox.setOnAction(event -> {
            Turma turma = turma_comboBox.getSelectionModel().getSelectedItem();
            if (turma != null) {
                turmaSelecionada = turma.getId();
                getAlunosByTurma();
            }
        });

        user_tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Aluno selecionado = user_tableView.getSelectionModel().getSelectedItem();

                if (selecionado == null)
                    return;

                if (alunoSelecionado != null && alunoSelecionado.equals(selecionado)) {
                    // Clicou duas vezes no mesmo aluno → sair do modo edição
                    limparCampos();
                    alunoSelecionado = null;
                    confirmarCadastro_button.setText("Cadastrar");
                } else {
                    // Clicou em um novo aluno → entrar no modo edição
                    alunoSelecionado = selecionado;
                    preencherFormularioComAluno(alunoSelecionado);
                    confirmarCadastro_button.setText("Salvar Edição");
                }
            }
        });

    }

    private void getTurmas() {
        TurmaDAO turmaDAO = new TurmaDAO();
        List<Turma> turmas = turmaDAO.getTurmas();
        ObservableList<Turma> observableTurmas = FXCollections.observableArrayList(turmas);
        turma_comboBox.setItems(observableTurmas);
    }

    private void getAlunosByTurma() {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAlunosByTurma(turmaSelecionada);
        ObservableList<Aluno> dados = FXCollections.observableArrayList(alunos);

        nome_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        matricula_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricula()));
        nascimento_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataNascimento()));
        status_col.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isAtivo()).asString());

        user_tableView.setItems(dados);
    }

    private void preencherFormularioComAluno(Aluno aluno) {
        nomeTextField.setText(aluno.getNome());
        statusComboBox.setValue(aluno.isAtivo() ? "Ativo" : "Inativo");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate nascimento = LocalDate.parse(aluno.getDataNascimento(), formatter);
            nascimento_textField.setValue(nascimento);
        } catch (Exception e) {
            nascimento_textField.setValue(null);
        }
    }

    @FXML
    void confirmarCadastro(ActionEvent event) {
        if (turmaSelecionada == 0) {
            showAlert("Atenção", "Por favor, selecione uma turma antes de prosseguir.");
            return;
        }

        String nome = nomeTextField.getText().trim();
        LocalDate dataNascimento = nascimento_textField.getValue();
        String statusSelecionado = statusComboBox.getSelectionModel().getSelectedItem();

        if (nome.isEmpty() || dataNascimento == null || statusSelecionado == null) {
            showAlert("Atenção", "Preencha todos os campos obrigatórios (nome, data de nascimento e status).");
            return;
        }

        boolean status = statusSelecionado.equalsIgnoreCase("ativo");
        String dataFormatada = dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            AlunoDAO alunoDAO = new AlunoDAO();

            if (alunoSelecionado == null) {
                // Cadastro de novo aluno
                Aluno novoAluno = new Aluno(0, nome, null, dataFormatada, String.valueOf(turmaSelecionada), status);
                boolean resultado = alunoDAO.addAluno(novoAluno);

                if (resultado) {
                    showAlert("Sucesso", "Aluno cadastrado com sucesso!");
                    getAlunosByTurma();
                    limparCampos();
                } else {
                    showAlert("Erro", "Erro ao cadastrar aluno.");
                }
            } else {
                // Edição de aluno existente
                alunoSelecionado.setNome(nome);
                alunoSelecionado.setDataNascimento(dataFormatada);
                alunoSelecionado.setAtivo(status);

                boolean resultado = alunoDAO.updateAluno(alunoSelecionado);

                if (resultado) {
                    showAlert("Sucesso", "Aluno atualizado com sucesso!");
                    getAlunosByTurma();
                    limparCampos();
                    confirmarCadastro_button.setText("Cadastrar");
                    alunoSelecionado = null;
                } else {
                    showAlert("Erro", "Erro ao atualizar aluno.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    private void limparCampos() {
        nomeTextField.clear();
        nascimento_textField.setValue(null);
        statusComboBox.getSelectionModel().clearSelection();
        alunoSelecionado = null;
        confirmarCadastro_button.setText("Cadastrar");
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
