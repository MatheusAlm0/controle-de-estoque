package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.UsuarioDAO;
import br.com.projetoestoque.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

import java.sql.SQLException;
import java.util.List;

public class GerenciarUsuariosController {

    @FXML private TextField nomeField;
    @FXML private ComboBox<String> nivelAcessoComboBox;
    @FXML private PasswordField senhaField;
    @FXML private TableView<Usuario> usuariosTableView;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colNivelAcesso;
    @FXML private TableColumn<Usuario, String> colSenha;
    @FXML private TextField buscarField;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ObservableList<Usuario> usuariosList;
    private FilteredList<Usuario> filteredList;

    @FXML
    private void initialize() {
        nivelAcessoComboBox.setItems(FXCollections.observableArrayList("Visualizador", "Operador", "Gerente"));

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNivelAcesso.setCellValueFactory(new PropertyValueFactory<>("nivelAcesso"));
        colSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        colNome.setStyle("-fx-alignment: CENTER;");
        colNivelAcesso.setStyle("-fx-alignment: CENTER;");
        colSenha.setStyle("-fx-alignment: CENTER;");

        colNome.prefWidthProperty().bind(usuariosTableView.widthProperty().multiply(0.4));
        colNivelAcesso.prefWidthProperty().bind(usuariosTableView.widthProperty().multiply(0.4));
        colSenha.prefWidthProperty().bind(usuariosTableView.widthProperty().multiply(0.2));

        try {
            carregarUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cadastrarUsuario() {
        String nome = nomeField.getText().trim();
        String nivelAcesso = nivelAcessoComboBox.getValue();
        String senha = senhaField.getText().trim();

        if (nome.isEmpty() || nivelAcesso == null || senha.isEmpty()) {
            showAlert("Erro", "Todos os campos são obrigatórios!");
            return;
        }

        Usuario usuario = new Usuario(nome, nivelAcesso, senha);

        try {
            usuarioDAO.inserir(usuario);
            showAlert("Sucesso", "Usuário cadastrado com sucesso!");
            carregarUsuarios();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirUsuario() {
        Usuario usuarioSelecionado = usuariosTableView.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado == null) {
            showAlert("Erro", "Nenhum usuário selecionado!");
            return;
        }

        try {
            usuarioDAO.excluir(usuarioSelecionado.getId());
            showAlert("Sucesso", "Usuário excluído com sucesso!");
            carregarUsuarios();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao excluir usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void editarUsuario() {
        Usuario usuarioSelecionado = usuariosTableView.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado == null) {
            showAlert("Erro", "Nenhum usuário selecionado!");
            return;
        }

        String nome = nomeField.getText().trim();
        String nivelAcesso = nivelAcessoComboBox.getValue();
        String senha = senhaField.getText().trim();

        if (nome.isEmpty() || nivelAcesso == null || senha.isEmpty()) {
            showAlert("Erro", "Todos os campos são obrigatórios!");
            return;
        }

        usuarioSelecionado.setNome(nome);
        usuarioSelecionado.setNivelAcesso(nivelAcesso);
        usuarioSelecionado.setSenha(senha);

        try {
            usuarioDAO.editar(usuarioSelecionado);
            showAlert("Sucesso", "Usuário editado com sucesso!");
            carregarUsuarios();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao editar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void filtrarUsuarios() {
        String termoBusca = buscarField.getText().toLowerCase().trim();

        if (filteredList == null) return;

        filteredList.setPredicate(usuario -> {
            if (termoBusca.isEmpty()) return true;
            return usuario.getNome().toLowerCase().contains(termoBusca);
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Window window = usuariosTableView.getScene().getWindow();
        alert.initOwner(window);
        alert.showAndWait();
    }

    private void carregarUsuarios() throws SQLException {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        usuariosList = FXCollections.observableArrayList(usuarios);
        filteredList = new FilteredList<>(usuariosList, p -> true);
        usuariosTableView.setItems(filteredList);
    }
}
