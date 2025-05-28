package br.com.projetoestoque.controller;

import br.com.projetoestoque.main.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnProdutos;

    @FXML
    private Button btnRelatorios;

    @FXML
    private Button btnFechar;

    @FXML
    private Button btnLogout;

    @FXML
    private void handleLogout() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoestoque/view/login.fxml"));
        javafx.scene.Parent loginPane = loader.load(); 
    
        contentArea.getScene().setRoot(loginPane);
    }

    @FXML
    private void handleUsuarios() throws IOException {
        carregarTela("view/gerenciar_usuarios");
    }

    @FXML
    private void handleProdutos() throws IOException {
        carregarTela("view/gerenciar_produtos");
    }

    @FXML
    private void handleRelatorios() throws IOException {
        carregarTela("view/visualizar_relatorios");
    }

    @FXML
    private void handleFechar() {
        Platform.exit();
    }

    private void carregarTela(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoestoque/" + fxml + ".fxml"));
        contentArea.getChildren().setAll((javafx.scene.Node) loader.load());
    }

    @FXML
    private void abrirNotificacoes() {
        try {
            Parent notificacoesView = FXMLLoader.load(getClass().getResource("/br/com/projetoestoque/view/notificacoes.fxml"));
            contentArea.getChildren().setAll(notificacoesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}