package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.UsuarioDAO;
import br.com.projetoestoque.main.App;
import br.com.projetoestoque.util.Sessao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class LoginController {

    @FXML
    private TextField emailField; // era usernameField

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        messageLabel.setText(""); // Limpa mensagem anterior

        Integer usuarioId = usuarioDAO.autenticarPorEmail(email, password);
        String nivel = usuarioDAO.obterNivelAcessoPorEmail(email);

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
            messageLabel.setText("Email ou senha incorretos.");
        }
    }

    @FXML
    private void handleEsqueciSenha() {
        //String emailUsuario = usernameField.getText();
        String assunto = "Esqueci minha senha :(";
        String corpo = "Preciso trocar minha senha!";
        String gmailUrl = String.format(
            "https://mail.google.com/mail/?view=cm&fs=1&to=matheusalmeida8742@gmail.com&su=%s&body=%s",
            encode(assunto), encode(corpo)
        );
        try {
            Desktop.getDesktop().browse(new URI(gmailUrl));
        } catch (Exception e) {
            messageLabel.setText("Não foi possível abrir o Gmail no navegador.");
        }
    }

    private String encode(String texto) {
        try {
            return java.net.URLEncoder.encode(texto, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            return texto;
        }
    }
}