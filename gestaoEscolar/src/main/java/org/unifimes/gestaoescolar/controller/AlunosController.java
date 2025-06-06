package org.unifimes.gestaoescolar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.unifimes.gestaoescolar.HomeApplication;

public class AlunosController {

    @FXML
    private Button confirmarCadastro_button;

    @FXML
    private TableColumn<?, ?> cpf_column;

    @FXML
    private TextField matriculaTextField;

    @FXML
    private TextField nascimentoTextField1;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TableColumn<?, ?> nome_column;

    @FXML
    private Button pesquisar_button;

    @FXML
    private ComboBox<?> statusComboBox;

    @FXML
    private TableColumn<?, ?> tipo_column;

    @FXML
    private TableColumn<?, ?> tipo_column1;

    @FXML
    private TableColumn<?, ?> tipo_column11;

    @FXML
    private TableView<?> user_tableView;

    @FXML
    private Button voltar_button;


    @FXML
    private void initialize() {
        HomeApplication scene = new HomeApplication();
        voltar_button.setOnMouseClicked(event -> scene.abrirScene("home"));
    }

    @FXML
    void confirmarCadastro(ActionEvent event) {

    }

}
