package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.UsuarioDAO;
import br.com.projetoestoque.main.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("Tentativa de login com usuário: " + username);

        if (usuarioDAO.autenticar(username, password)) {
            System.out.println("Login bem-sucedido para o usuário: " + username);
            App.setRoot("view/admin"); 
        } else {
            System.out.println("Login falhou para o usuário: " + username);
            errorLabel.setText("Usuário ou senha incorretos.");
        }
    }
}