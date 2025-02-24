package br.com.projetoestoque.controller;

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

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Aqui você fará a lógica de autenticação
        if (autenticar(username, password)) {
            App.setRoot("view/admin"); // Carrega a tela do admin
        } else {
            errorLabel.setText("Usuário ou senha incorretos.");
        }
    }

    private boolean autenticar(String username, String password) {
        // ***SUBSTITUA ESTE CÓDIGO DE EXEMPLO PELA SUA LÓGICA DE AUTENTICAÇÃO***
        // Aqui você deve consultar o banco de dados ou outra fonte de dados
        // para verificar se o usuário e a senha são válidos.

        // Exemplo (substitua por sua lógica real):
        if (username.equals("admin") && password.equals("123")) {
            return true;
        }
        return false;
    }
}