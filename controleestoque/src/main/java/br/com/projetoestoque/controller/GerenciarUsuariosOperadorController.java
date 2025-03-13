package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.UsuarioDAO;
import br.com.projetoestoque.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class GerenciarUsuariosOperadorController {

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

    

    private void carregarUsuarios() throws SQLException {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        usuariosList = FXCollections.observableArrayList(usuarios);
        usuariosTableView.setItems(usuariosList);
    }
}
