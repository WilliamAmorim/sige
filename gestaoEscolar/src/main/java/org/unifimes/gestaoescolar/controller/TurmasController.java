package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.modal.DisciplinaModal;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.Turma;

import java.util.ArrayList;
import java.util.List;

public class TurmasController {

    @FXML private ComboBox<String> anoComboBox;
    @FXML private TableColumn<Turma, String> ano_col;
    @FXML private TableColumn<Turma, String> codigo_col;
    @FXML private Button confirmarCadastro_button;
    @FXML private ComboBox<Disciplina> disciplinaComboBox;
    @FXML private ListView<Disciplina> disciplinasListView;
    @FXML private TextField nomeTextField;
    @FXML private TableColumn<Turma, String> nome_col;
    @FXML private Button pesquisar_button;
    @FXML private ComboBox<String> turnoComboBox;
    @FXML private TableColumn<Turma, String> turno_col;
    @FXML private TableView<Turma> user_tableView;
    @FXML private Button voltar_button;

    private final ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();
    private Turma turmaEmEdicao = null; // guarda a turma selecionada para edição

    @FXML
    public void initialize() {
        showDisciplinas();
        getTurmas();

        voltar_button.setOnAction(e -> voltar());

        anoComboBox.setItems(FXCollections.observableArrayList("2025"));
        turnoComboBox.setItems(FXCollections.observableArrayList("Manhã", "Tarde", "Noite"));

        disciplinasListView.setItems(disciplinas);
        disciplinasListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Disciplina disciplinaSelecionada = disciplinasListView.getSelectionModel().getSelectedItem();
                if (disciplinaSelecionada != null) {
                    disciplinas.remove(disciplinaSelecionada);
                }
            }
        });

        // Adiciona lógica de duplo clique para edição/desseleção
        user_tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Turma selectedTurma = user_tableView.getSelectionModel().getSelectedItem();
                if (selectedTurma != null) {
                    if (turmaEmEdicao != null && turmaEmEdicao.getId() == selectedTurma.getId()) {
                        // Se for o mesmo selecionado, desmarca e sai do modo edição
                        limparFormulario();
                        turmaEmEdicao = null;
                        user_tableView.getSelectionModel().clearSelection();
                    } else {
                        turmaEmEdicao = selectedTurma;
                        preencherFormulario(turmaEmEdicao);
                    }
                }
            }
        });
    }

    @FXML
    void addNovaDisciplina(ActionEvent event) {
        criarDisciplina();
    }

    private void voltar() {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene("home");
    }

    @FXML
    void confirmarCadastro(ActionEvent event) {
        String nome = nomeTextField.getText();
        String turno = turnoComboBox.getValue();
        String anoStr = anoComboBox.getValue();

        if (nome.isEmpty() || turno == null || anoStr == null) {
            showAlert("Erro de validação", "Por favor, preencha todos os campos obrigatórios: Nome, Ano e Turno.");
            return;
        }

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException e) {
            showAlert("Erro de validação", "Ano inválido. Por favor, selecione um ano válido.");
            return;
        }

        TurmaDAO turmaDAO = new TurmaDAO();
        boolean result;

        if (turmaEmEdicao == null) {
            // Cadastro novo
            Turma novaTurma = new Turma(0, nome, turno, ano, new ArrayList<>(disciplinas));
            result = turmaDAO.addTurma(novaTurma);
            if (result) {
                showAlert("Sucesso", "Turma cadastrada com sucesso!");
                getTurmas();
                limparFormulario();
            } else {
                showAlert("Erro ao cadastrar", "Não foi possível cadastrar a turma. Tente novamente.");
            }
        } else {
            // Atualizar turma existente
            turmaEmEdicao.setNome(nome);
            turmaEmEdicao.setTurno(turno);
            turmaEmEdicao.setAno(ano);
            turmaEmEdicao.setDisciplinas(new ArrayList<>(disciplinas));

            result = turmaDAO.updateTurma(turmaEmEdicao);
            if (result) {
                showAlert("Sucesso", "Turma atualizada com sucesso!");
                getTurmas();
                limparFormulario();
                turmaEmEdicao = null;
            } else {
                showAlert("Erro ao atualizar", "Não foi possível atualizar a turma. Tente novamente.");
            }
        }
    }

    private void getTurmas() {
        TurmaDAO turmaDAO = new TurmaDAO();
        List<Turma> turmas = turmaDAO.getTurmas();

        ObservableList<Turma> observableTurmas = FXCollections.observableArrayList(turmas);

        nome_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        codigo_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asString());
        turno_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTurno()));
        ano_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAno()).asString());

        user_tableView.setItems(observableTurmas);
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
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

    private void criarDisciplina() {
        DisciplinaModal.show((nome, cargaHoraria) -> {
            Disciplina disciplina = new Disciplina(0, cargaHoraria, nome);
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
            boolean result = disciplinaDAO.addDisciplina(disciplina);
            if (result) {
                showDisciplinas();
                showAlert("Disciplina cadastrada", "Disciplina cadastrada com sucesso.");
            } else {
                showAlert("Erro ao cadastrar disciplina", "Ocorreu um erro ao cadastrar a disciplina. Tente novamente.");
            }
        });
    }

    private void preencherFormulario(Turma turma) {
        nomeTextField.setText(turma.getNome());
        anoComboBox.setValue(String.valueOf(turma.getAno()));
        turnoComboBox.setValue(turma.getTurno());

        if (turma.getDisciplinas() != null) {
            disciplinas.setAll(turma.getDisciplinas());
        } else {
            disciplinas.clear();
        }
    }

    private void limparFormulario() {
        nomeTextField.clear();
        anoComboBox.setValue(null);
        turnoComboBox.setValue(null);
        disciplinas.clear();
    }
}
