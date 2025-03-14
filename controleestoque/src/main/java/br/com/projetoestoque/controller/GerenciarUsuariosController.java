package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.UsuarioDAO;
import br.com.projetoestoque.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

import java.sql.SQLException;
import java.util.List;

public class GerenciarUsuariosController {

    @FXML
    private TextField nomeField;

    @FXML
    private ComboBox<String> nivelAcessoComboBox;

    @FXML
    private PasswordField senhaField;

    @FXML
    private TableView<Usuario> usuariosTableView;

    @FXML
    private TableColumn<Usuario, String> colNome;

    @FXML
    private TableColumn<Usuario, String> colNivelAcesso;

    @FXML
    private TableColumn<Usuario, String> colSenha;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ObservableList<Usuario> usuariosList;

    @FXML
    private void initialize() {
        // Preenche o ComboBox com as opções de nível de acesso
        nivelAcessoComboBox.setItems(FXCollections.observableArrayList("Visualizador", "Operador", "Gerente"));

        // Configura as colunas da tabela
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNivelAcesso.setCellValueFactory(new PropertyValueFactory<>("nivelAcesso"));
        colSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        // Centraliza o texto nas colunas
        colNome.setStyle("-fx-alignment: CENTER;");
        colNivelAcesso.setStyle("-fx-alignment: CENTER;");
        colSenha.setStyle("-fx-alignment: CENTER;");

        // Configura a largura das colunas da tabela
        colNome.prefWidthProperty().bind(usuariosTableView.widthProperty().multiply(0.4)); // 40% da largura total
        colNivelAcesso.prefWidthProperty().bind(usuariosTableView.widthProperty().multiply(0.4)); // 40% da largura total
        colSenha.prefWidthProperty().bind(usuariosTableView.widthProperty().multiply(0.2)); // 20% da largura total
    
        // Carrega os usuários da base de dados
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

        // Verificar se os campos obrigatórios estão preenchidos
        if (nome.isEmpty() || nivelAcesso == null || senha.isEmpty()) {
            showAlert("Erro", "Todos os campos são obrigatórios!");
            return;
        }

        Usuario usuario = new Usuario(nome, nivelAcesso, senha);

        try {
            usuarioDAO.inserir(usuario);
            showAlert("Sucesso", "Usuário cadastrado com sucesso!");
            carregarUsuarios(); // Atualiza a tabela após o cadastro
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
            carregarUsuarios(); // Atualiza a tabela após a exclusão
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

        // Verificar se os campos obrigatórios estão preenchidos
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
            carregarUsuarios(); // Atualiza a tabela após a edição
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao editar usuário: " + e.getMessage());
            e.printStackTrace();
        }
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
        usuariosTableView.setItems(usuariosList);
    }
}