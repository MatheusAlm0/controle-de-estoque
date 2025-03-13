package br.com.projetoestoque.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class VisualizadorProdutosController {

    @FXML
    private TextField nomeField;

    @SuppressWarnings("rawtypes")
    @FXML
    private ComboBox categoriaComboBox;

    @FXML
    private TextField precoField;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableView produtosTableView;

    @FXML
    private void salvarProduto() {
        // Implementar lógica para salvar produto no banco de dados
    }

    @FXML
    private void editarProduto() {
        // Implementar lógica para editar produto no banco de dados
    }

    @FXML
    private void excluirProduto() {
        // Implementar lógica para excluir produto do banco de dados
    }
}