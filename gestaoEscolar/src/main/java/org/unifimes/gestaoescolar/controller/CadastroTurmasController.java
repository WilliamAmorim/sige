package org.unifimes.gestaoescolar.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.unifimes.gestaoescolar.modal.DisciplinaModal;

public class CadastroTurmasController {

    @FXML
    private Button btnAdicionarDisciplina;

    @FXML
    private ListView<String> listDisciplinas; // para exibir as disciplinas cadastradas (opcional)

    @FXML
    public void initialize() {
        btnAdicionarDisciplina.setOnAction(e -> {
            DisciplinaModal.show((nome, cargaHoraria) -> {
                // Aqui você pode adicionar a disciplina à lista da turma, banco de dados, etc.
                String disciplina = nome + " (" + cargaHoraria + "h)";
                listDisciplinas.getItems().add(disciplina);

                // ou se quiser salvar em um objeto Disciplina:
                // Disciplina nova = new Disciplina(nome, cargaHoraria);
                // turma.adicionarDisciplina(nova);
            });
        });
    }
}

