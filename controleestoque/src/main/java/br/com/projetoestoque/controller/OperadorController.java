package br.com.projetoestoque.controller;

import br.com.projetoestoque.main.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.application.Platform;

public class OperadorController {

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
    private void handleUsuarios() throws IOException {
        carregarTela("view/gerenciar_usuarios_operador");
    }

    @FXML
    private void handleProdutos() throws IOException {
        carregarTela("view/gerenciar_produtos");
    }

    @FXML
    private void handleFechar() {
        Platform.exit();
    }

    private void carregarTela(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoestoque/" + fxml + ".fxml"));
        contentArea.getChildren().setAll((javafx.scene.Node) loader.load());
    }
}