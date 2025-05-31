package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.UserDAO;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    @FXML private Button confirmarCadastro_button;


    // Tabela de usuários
    @FXML private TableView<User> user_tableView;
    @FXML private TableColumn<User, String> nome_column;
    @FXML private TableColumn<User, String> cpf_column;
    @FXML private TableColumn<User, String> tipo_column;

    // Campos do formulário
    @FXML private TextField nomeTextField;
    @FXML private TextField cpfTextField;
    @FXML private PasswordField senhaField;
    @FXML private ComboBox<String> tipoComboBox;

    // Disciplinas
    @FXML private ComboBox<Disciplina> disciplinaComboBox;
    @FXML private ListView<Disciplina> disciplinasListView;

    // Botões
    @FXML private Button pesquisar_button;
    @FXML private Button voltar_button;

    private final ObservableList<User> usuarios = FXCollections.observableArrayList();
    private final ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        showTableUser();
        showDisciplinas();

        // Inicializa tipos
        tipoComboBox.setItems(FXCollections.observableArrayList("Administrador", "Professor"));

        disciplinasListView.setItems(disciplinas);

        disciplinasListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Disciplina disciplinaSelecionada = disciplinasListView.getSelectionModel().getSelectedItem();
                if (disciplinaSelecionada != null) {
                    disciplinas.remove(disciplinaSelecionada);
                }
            }
        });

        user_tableView.setOnMouseClicked(event -> {
            User selectedUser = user_tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                preencherFormulario(selectedUser);
            }
        });



        // Lógica de botões
        pesquisar_button.setOnAction(e -> pesquisarUsuarios());
        voltar_button.setOnAction(e -> voltar());
    }

    @FXML
    private void adicionarDisciplina() {
        Disciplina disciplina = disciplinaComboBox.getValue();
        if (disciplina != null && !disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    @FXML
    private void confirmarCadastro() {
        String nome = nomeTextField.getText();
        String cpf = cpfTextField.getText();
        String tipo = tipoComboBox.getValue();

        if (nome.isEmpty() || cpf.isEmpty() || tipo == null) {
            showAlert("Erro", "Todos os campos devem ser preenchidos!");
            return;
        }

        User usuario = new User(null, nome, cpf, tipo, null, new ArrayList<>(disciplinas));

        usuarios.add(usuario);

        // Limpa campos
        nomeTextField.clear();
        cpfTextField.clear();
        senhaField.clear();
        tipoComboBox.setValue(null);
        disciplinas.clear();
    }

    private void pesquisarUsuarios() {
        // Aqui você pode implementar lógica de busca
        showAlert("Pesquisar", "Função de pesquisa ainda não implementada.");
    }

    private void voltar() {
        // Navegação para a tela anterior
        showAlert("Voltar", "Função de voltar ainda não implementada.");
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void showTableUser(){
        // Configura as colunas com base nos atributos da classe User
        cpf_column.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        nome_column.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tipo_column.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        // Busca os usuários e popula a TableView
        UserDAO search = new UserDAO();
        List<User> users = search.getUser();
        //ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        usuarios.setAll(users);
        user_tableView.setItems(usuarios);
    }

    private void showDisciplinas(){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinas = disciplinaDAO.getDisciplinas();

        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinas);
        disciplinaComboBox.setItems(observableDisciplina);
        //disciplinasListView.setItems(observableDisciplina);
    }

    private void preencherFormulario(User user) {
        nomeTextField.setText(user.getNome());
        cpfTextField.setText(user.getCpf());
        tipoComboBox.setValue(user.getTipo());

        // Atualiza botão para "Editar"
        confirmarCadastro_button.setText("Editar");

        // Se quiser também popular as disciplinas associadas ao usuário
        // Aqui estou assumindo que o User tem um campo List<Disciplina> disciplinas (caso tenha)
        if (user.getDisciplinas() != null) {
            System.out.println("Tem disciplinas"+user.getDisciplinas());
            disciplinas.setAll(user.getDisciplinas());
        } else {
            System.out.println("Não tem disciplinas"+user.getDisciplinas());
            disciplinas.clear();
        }
    }



}
