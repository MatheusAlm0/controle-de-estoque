package br.com.projetoestoque.controller;

import java.sql.SQLException;
import java.util.List;
import br.com.projetoestoque.dao.ProdutoDAO;
import br.com.projetoestoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Window;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GerenciarProdutosController {

    @FXML
    private TextField idField;

    @FXML
    private TextField codigoField;

    @FXML
    private TextField marcaField;

    @FXML
    private TextField modeloField;

    @FXML
    private TextField categoriaField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TextField precoField;

    @FXML
    private TableView<Produto> produtosTableView; // Parametrize the TableView with Produto

    @FXML
    private void salvarProduto() {
        String id = idField.getText().trim();
        String codigo = codigoField.getText().trim();
        String marca = marcaField.getText().trim();
        String modelo = modeloField.getText().trim();
        String categoria = categoriaField.getText().trim();
        String quantidadeStr = quantidadeField.getText().trim();
        String precoStr = precoField.getText().trim();

        // Verificar se os campos obrigatórios estão preenchidos
        if (id.isEmpty() || codigo.isEmpty() || marca.isEmpty() || modelo.isEmpty() || categoria.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty()) {
            showAlert("Erro", "Todos os campos são obrigatórios!");
            return;
        }

        double quantidade;
        double preco;
        try {
            quantidade = Double.parseDouble(quantidadeStr);
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            showAlert("Erro", "Quantidade e Preço devem ser números válidos!");
            return;
        }

        Produto produto = new Produto(id, codigo, marca, modelo, categoria, quantidade, preco);

        try {
            ProdutoDAO produtoDAO = new ProdutoDAO();
            produtoDAO.inserir(produto);
            showAlert("Sucesso", "Produto cadastrado com sucesso!");
            carregarProduto(); // Atualiza a tabela após o cadastro
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao cadastrar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void editarProduto() {
        // Implementação do método editarProduto
        showAlert("Editar Produto", "Função de edição de produto ainda não implementada.");
    }

    @FXML
    private void excluirProduto() {
        Produto produtoSelecionado = produtosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            showAlert("Erro", "Nenhum produto selecionado!");
            return;
        }

        try {
            ProdutoDAO produtoDAO = new ProdutoDAO();
            produtoDAO.excluir(Integer.parseInt(produtoSelecionado.getId())); // Converte o id para int
            showAlert("Sucesso", "Produto excluído com sucesso!");
            carregarProduto(); // Atualiza a tabela após a exclusão
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao excluir produto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert("Erro", "ID do produto inválido!");
            e.printStackTrace();
        }
    }

    private void carregarProduto() throws SQLException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listarTodos();
        ObservableList<Produto> produtosList = FXCollections.observableArrayList(produtos);
        produtosTableView.setItems(produtosList);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Window window = produtosTableView.getScene().getWindow();
    alert.initOwner(window);
        alert.showAndWait();
    }
}