package org.unifimes.gestaoescolar.modal;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DisciplinaModal {

    public interface DisciplinaCallback {
        void onDisciplinaCadastrada(String nome, int cargaHoraria);
    }

    public static void show(DisciplinaCallback callback) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Cadastro de Disciplina");

        Label nomeLabel = new Label("Nome:");
        TextField nomeField = new TextField();

        Label cargaHorariaLabel = new Label("Carga Horária:");
        TextField cargaHorariaField = new TextField();

        Button cadastrarBtn = new Button("Cadastrar");
        cadastrarBtn.setOnAction(e -> {
            String nome = nomeField.getText();
            String cargaHorariaStr = cargaHorariaField.getText();

            if (nome.isEmpty() || cargaHorariaStr.isEmpty()) {
                showAlert("Preencha todos os campos.");
                return;
            }

            try {
                int cargaHoraria = Integer.parseInt(cargaHorariaStr);
                callback.onDisciplinaCadastrada(nome, cargaHoraria);
                window.close();
            } catch (NumberFormatException ex) {
                showAlert("Carga horária deve ser um número inteiro.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(nomeLabel, nomeField, cargaHorariaLabel, cargaHorariaField, cadastrarBtn);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

