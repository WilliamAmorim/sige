package org.unifimes.gestaoescolar.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.UserDAO;
import org.unifimes.gestaoescolar.model.User;

import java.util.List;

public class UsuariosController {

    @FXML
    private TableColumn<User, String> cpf_column;

    @FXML
    private ListView<?> disciplina_listview;

    @FXML
    private TableColumn<User, String> nome_column;

    @FXML
    private TableColumn<User, String> tipo_column;

    @FXML
    private TableView<User> user_tableView;

    @FXML
    private Button voltar_button;

    @FXML
    private void initialize() {
        HomeApplication scene = new HomeApplication();
        voltar_button.setOnMouseClicked(Mouse -> scene.abrirScene("home"));
        showTableUser();
    }


    private void showTableUser(){
        // Configura as colunas com base nos atributos da classe User
        cpf_column.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        nome_column.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tipo_column.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // Busca os usuários e popula a TableView
        UserDAO search = new UserDAO();
        List<User> users = search.getUser();
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        System.out.println("observableUsers:"+observableUsers);
        user_tableView.setItems(observableUsers);
    }

}
