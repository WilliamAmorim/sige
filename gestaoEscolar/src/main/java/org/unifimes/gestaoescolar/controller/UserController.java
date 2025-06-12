package org.unifimes.gestaoescolar.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unifimes.gestaoescolar.HomeApplication;
import org.unifimes.gestaoescolar.dao.DisciplinaDAO;
import org.unifimes.gestaoescolar.dao.UserDAO;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.model.User.TipoUsuario;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    @FXML private Button confirmarCadastro_button;
    @FXML private TableView<User> user_tableView;
    @FXML private TableColumn<User, String> nome_column;
    @FXML private TableColumn<User, String> cpf_column;
    @FXML private TableColumn<User, String> tipo_column;
    @FXML private TextField nomeTextField;
    @FXML private TextField cpfTextField;
    @FXML private PasswordField senhaField;
    @FXML private ComboBox<String> tipoComboBox;
    @FXML private ComboBox<Disciplina> disciplinaComboBox;
    @FXML private ListView<Disciplina> disciplinasListView;
    @FXML private Button pesquisar_button;
    @FXML private Button voltar_button;

    private final ObservableList<User> usuarios = FXCollections.observableArrayList();
    private final ObservableList<Disciplina> disciplinas = FXCollections.observableArrayList();

    private User usuarioSelecionado = null;

    @FXML
    public void initialize() {
        showTableUser();
        showDisciplinas();

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
            if (event.getClickCount() == 2) {
                User selectedUser = user_tableView.getSelectionModel().getSelectedItem();

                if (selectedUser != null) {
                    if (usuarioSelecionado != null && usuarioSelecionado.equals(selectedUser)) {
                        limparCampos();
                        showAlert("Modo edição cancelado", "Você saiu do modo de edição.");
                    } else {
                        usuarioSelecionado = selectedUser;
                        preencherFormulario(usuarioSelecionado);
                        confirmarCadastro_button.setText("Salvar Edição");
                    }
                }
            }
        });

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
        String tipoStr = tipoComboBox.getValue();
        String senha = senhaField.getText();

        if (nome.isEmpty() || cpf.isEmpty() || tipoStr == null || senha.isEmpty()) {
            showAlert("Erro de Validação", "Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        TipoUsuario tipo;
        try {
            tipo = tipoFromString(tipoStr);
        } catch (IllegalArgumentException e) {
            showAlert("Erro de Tipo", "Tipo de usuário inválido.");
            return;
        }

        UserDAO userDao = new UserDAO();
        boolean result;

        if (usuarioSelecionado == null) {
            User usuario = new User(null, nome, cpf, tipo, senha, new ArrayList<>(disciplinas));
            result = userDao.addUser(usuario);
            if (result) {
                usuarios.add(usuario);
                showAlert("Sucesso", "Usuário cadastrado com sucesso!");
            } else {
                showAlert("Erro ao Cadastrar", "Não foi possível cadastrar o usuário.");
                return;
            }
        } else {
            usuarioSelecionado.setNome(nome);
            usuarioSelecionado.setCpf(cpf);
            usuarioSelecionado.setSenha(senha);
            usuarioSelecionado.setTipo(tipo);
            usuarioSelecionado.setDisciplinas(new ArrayList<>(disciplinas));

            result = userDao.updateUser(usuarioSelecionado);
            if (result) {
                showAlert("Sucesso", "Usuário atualizado com sucesso!");
                showTableUser();
            } else {
                showAlert("Erro ao Atualizar", "Não foi possível atualizar os dados do usuário.");
                return;
            }
        }

        limparCampos();
    }

    private void pesquisarUsuarios() {
        showAlert("Aviso", "A funcionalidade de pesquisa ainda não está implementada.");
    }

    private void voltar() {
        HomeApplication scene = new HomeApplication();
        scene.abrirScene("home");
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void showTableUser() {
        cpf_column.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        nome_column.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tipo_column.setCellValueFactory(cellData -> new SimpleStringProperty(
                switch (cellData.getValue().getTipo()) {
                    case ADMIN -> "Administrador";
                    case PROFESSOR -> "Professor";
                }
        ));

        UserDAO search = new UserDAO();
        List<User> users = search.getUser();
        usuarios.setAll(users);
        user_tableView.setItems(usuarios);
    }

    private void showDisciplinas() {
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<Disciplina> disciplinasList = disciplinaDAO.getDisciplinas();
        ObservableList<Disciplina> observableDisciplina = FXCollections.observableArrayList(disciplinasList);
        disciplinaComboBox.setItems(observableDisciplina);
    }

    private void preencherFormulario(User user) {
        nomeTextField.setText(user.getNome());
        cpfTextField.setText(user.getCpf());
        senhaField.setText(user.getSenha());

        if (user.getTipo() != null) {
            String tipoStr = switch (user.getTipo()) {
                case ADMIN -> "Administrador";
                case PROFESSOR -> "Professor";
            };
            tipoComboBox.setValue(tipoStr);
        }

        if (user.getDisciplinas() != null) {
            disciplinas.setAll(user.getDisciplinas());
        } else {
            disciplinas.clear();
        }
    }

    private void limparCampos() {
        nomeTextField.clear();
        cpfTextField.clear();
        senhaField.clear();
        tipoComboBox.setValue(null);
        disciplinas.clear();
        usuarioSelecionado = null;
        confirmarCadastro_button.setText("Cadastrar");
    }

    private TipoUsuario tipoFromString(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "administrador" -> TipoUsuario.ADMIN;
            case "professor" -> TipoUsuario.PROFESSOR;
            default -> throw new IllegalArgumentException("Tipo inválido: " + tipo);
        };
    }
}
