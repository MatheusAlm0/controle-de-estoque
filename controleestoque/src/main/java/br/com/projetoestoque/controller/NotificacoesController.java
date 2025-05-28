package br.com.projetoestoque.controller;

import br.com.projetoestoque.model.Notificacao;
import br.com.projetoestoque.service.NotificacaoService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class NotificacoesController {

    @FXML
    private VBox notificacoesContainer;

    @FXML
    private Label lblVazio;

    @FXML
    public void initialize() {
        atualizarNotificacoes();
    }

    private void atualizarNotificacoes() {
        notificacoesContainer.getChildren().clear();
        List<Notificacao> notificacoes = NotificacaoService.getInstance().getNotificacoes();

        if (notificacoes.isEmpty()) {
            lblVazio.setVisible(true);
            return;
        } else {
            lblVazio.setVisible(false);
        }

        for (Notificacao n : notificacoes) {
            HBox card = criarCardNotificacao(n);
            notificacoesContainer.getChildren().add(card);
        }
    }

    private HBox criarCardNotificacao(Notificacao notificacao) {
        HBox hbox = new HBox(12);
        hbox.setStyle("-fx-background-color: #f8d7da; -fx-padding: 16; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #f5c6cb; -fx-alignment: CENTER_LEFT;");
        Label lbl = new Label(notificacao.getMensagem());
        lbl.setStyle("-fx-font-size: 15; -fx-text-fill: #721c24; -fx-font-weight: bold;");
        Button btnRemover = new Button("X");
        btnRemover.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 13; -fx-background-radius: 50%; -fx-min-width: 28; -fx-min-height: 28;");
        btnRemover.setOnAction(e -> {
            NotificacaoService.getInstance().remover(notificacao);
            atualizarNotificacoes();
        });
        hbox.getChildren().addAll(lbl, btnRemover);
        return hbox;
    }
}
