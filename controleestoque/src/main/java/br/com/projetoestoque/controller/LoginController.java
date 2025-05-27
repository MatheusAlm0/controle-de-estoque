package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.UsuarioDAO;
import br.com.projetoestoque.main.App;
import br.com.projetoestoque.util.Sessao;
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
    private Label messageLabel; // Alinhe o fx:id no FXML para "messageLabel"

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        messageLabel.setText(""); // Limpa mensagem anterior

        Integer usuarioId = usuarioDAO.autenticar(username, password);
        String nivel = usuarioDAO.obterNivelAcesso(username);

        if (usuarioId != null && nivel != null) {
            Sessao.setUsuarioId(usuarioId);

            switch (nivel.trim().toLowerCase()) {
                case "admin":
                    App.setRoot("view/admin");
                    break;
                case "operador":
                    App.setRoot("view/operador");
                    break;
                case "gerente":
                    App.setRoot("view/gerente");
                    break;
                case "visualizador":
                    App.setRoot("view/visualizador");
                    break;
                default:
                    messageLabel.setText("Erro ao identificar o nível de acesso.");
            }
        } else {
            messageLabel.setText("Usuário ou senha incorretos.");
        }
    }
}