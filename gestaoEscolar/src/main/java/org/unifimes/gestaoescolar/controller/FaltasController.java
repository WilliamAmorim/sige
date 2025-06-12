package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Color;
import javafx.scene.control.TableRow;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.AlunoDAO;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.FrequenciaDAO;
import org.unifimes.gestaoescolar.dao.TurmaDAO;
import org.unifimes.gestaoescolar.model.*;

import java.time.LocalDate;
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
    private int turmaAnoSelecionada;
    private int disciplinaSeleciona;

    @FXML
    private void initialize() {
        voltar_button.setOnMouseClicked(event -> openScreen("home"));
        getTurmas();

        turma_comboBox.setOnAction(event -> {
            Turma turmaSelecionada = turma_comboBox.getSelectionModel().getSelectedItem();
            if (turmaSelecionada != null) {
                this.turmaSelecionada = turmaSelecionada.getId();
                this.turmaAnoSelecionada = turmaSelecionada.getAno();
                getDisciplinasPorTurma(turmaSelecionada.getId());
            }
        });

        disciplina_comboBox.setOnAction(event -> {
            Disciplina disciplinaSelect = disciplina_comboBox.getSelectionModel().getSelectedItem();
            if (disciplinaSelect != null && turmaSelecionada > 0) {
                disciplinaSeleciona = disciplinaSelect.getId();
                getAlunosByTurma();
            }
        });

        finalizar_button.setOnAction(event -> {
            if (turmaSelecionada == 0 || disciplinaSeleciona == 0) {
                showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma turma e uma disciplina antes de finalizar.");
                return;
            }

            ObservableList<FrequenciaTable> lista = tabela_alunos.getItems();
            List<Frequencia> frequencias = new ArrayList<>();

            for (FrequenciaTable aluno : lista) {
                Frequencia frequencia = new Frequencia(0, aluno.getID(), disciplinaSeleciona, aluno.isFalta(), null, getBimestreAtualDesde(turmaAnoSelecionada));
                frequencias.add(frequencia);
            }

            FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
            boolean jaExiste = frequenciaDAO.isFrequencia(turmaSelecionada, disciplinaSeleciona);
            if (jaExiste) {
                boolean resultado = frequenciaDAO.addMultipleFrequencia(frequencias);
                if (resultado) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Frequência registrada com sucesso!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível registrar a frequência. Tente novamente.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Atenção", "A frequência para esta turma e disciplina já foi registrada.");
            }
        });

        // Configurar colunas da tabela
        nomeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        matriculaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricula()));

        falta_col.setCellValueFactory(cellData -> cellData.getValue().faltaProperty());
        falta_col.setCellFactory(CheckBoxTableCell.forTableColumn(falta_col));

        // Destacar linha com falta marcada
        tabela_alunos.setRowFactory(tv -> {
            TableRow<FrequenciaTable> row = new TableRow<>();
            row.itemProperty().addListener((obs, previousAluno, currentAluno) -> {
                if (previousAluno != null) {
                    // Remover estilo do item anterior
                    row.getStyleClass().remove("falta-marcada");
                }
                if (currentAluno != null) {
                    if (currentAluno.isFalta()) {
                        if (!row.getStyleClass().contains("falta-marcada")) {
                            row.getStyleClass().add("falta-marcada");
                        }
                    } else {
                        row.getStyleClass().remove("falta-marcada");
                    }
                }
            });

            // Também escutar mudanças no checkbox para atualizar a cor da linha
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                FrequenciaTable item = row.getItem();
                if (item != null) {
                    if (item.isFalta()) {
                        if (!row.getStyleClass().contains("falta-marcada")) {
                            row.getStyleClass().add("falta-marcada");
                        }
                    } else {
                        row.getStyleClass().remove("falta-marcada");
                    }
                }
            });

            return row;
        });
    }

    private void getTurmas() {
        TurmaDAO turmaDAO = new TurmaDAO();
        List<Turma> turmas = turmaDAO.getTurmas();

        ObservableList<Turma> observableTurmas = FXCollections.observableArrayList(turmas);
        turma_comboBox.setItems(observableTurmas);
    }

    private void getDisciplinasPorTurma(int id) {
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinas = disciplinaDAO.getDisciplinasByTurmaId(id);
        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinas);
        disciplina_comboBox.setItems(observableDisciplina);
    }

    private void getAlunosByTurma() {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAlunosByTurma(turmaSelecionada);

        ObservableList<FrequenciaTable> dados = FXCollections.observableArrayList();

        for (Aluno aluno : alunos) {
            dados.add(new FrequenciaTable(aluno.getId(), aluno.getNome(), aluno.getMatricula()));
        }

        tabela_alunos.setItems(dados);

        tabela_alunos.setRowFactory(tv -> {
            TableRow<FrequenciaTable> row = new TableRow<>();
            row.itemProperty().addListener((obs, previousAluno, currentAluno) -> {
                if (previousAluno != null) {
                    previousAluno.faltaProperty().removeListener((obsFalta, oldVal, newVal) -> {
                        // Não precisa fazer nada aqui pois removemos listener anterior
                    });
                }
                if (currentAluno != null) {
                    // Listener para mudar a cor da linha quando o checkbox mudar
                    currentAluno.faltaProperty().addListener((obsFalta, oldVal, newVal) -> {
                        if (newVal) {
                            row.setStyle("-fx-background-color: lightcoral;"); // vermelho claro quando falta
                        } else {
                            row.setStyle(""); // volta ao padrão quando desmarcado
                        }
                    });
                    // Atualiza a cor da linha no momento que o item é setado na linha
                    if (currentAluno.isFalta()) {
                        row.setStyle("-fx-background-color: lightcoral;");
                    } else {
                        row.setStyle("");
                    }
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });

    }

    private void openScreen(String screenName) {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene(screenName);
    }

    private void showAlert(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static int getBimestreAtualDesde(int anoInicioCurso) {
        LocalDate hoje = LocalDate.now();
        int anoAtual = hoje.getYear();
        int mesAtual = hoje.getMonthValue();

        int mesesDesdeInicio = (anoAtual - anoInicioCurso) * 12 + mesAtual;

        int bimestreAtual = (int) Math.ceil(mesesDesdeInicio / 3.0);

        return bimestreAtual;
    }
}
