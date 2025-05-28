package br.com.projetoestoque.controller;

import java.sql.SQLException;
import java.util.List;
import br.com.projetoestoque.dao.ProdutoDAO;
import br.com.projetoestoque.model.Produto;
import br.com.projetoestoque.service.NotificacaoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; 
import javafx.collections.transformation.SortedList; 
import javafx.stage.Window;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TextField campoBusca;

    @FXML
    private TableView<Produto> produtosTableView;

    @FXML private TableColumn<Produto, String> colId;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colMarca;
    @FXML private TableColumn<Produto, String> colModelo;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, Double> colquantidade;
    @FXML private TableColumn<Produto, Double> colPreco;

    private ProdutoDAO produtoDAO = new ProdutoDAO();

    private ObservableList<Produto> masterData = FXCollections.observableArrayList();

    private FilteredList<Produto> filteredData;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colquantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        carregarProdutosNaTabela();

        filteredData = new FilteredList<>(masterData, p -> true);

        campoBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(produto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (produto.getMarca().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                } else if (produto.getModelo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false; 
            });
        });

        SortedList<Produto> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(produtosTableView.comparatorProperty());

        produtosTableView.setItems(sortedData);
    }

    @FXML
    private void salvarProduto() {
        String codigo = codigoField.getText().trim();
        String marca = marcaField.getText().trim();
        String modelo = modeloField.getText().trim();
        String categoria = categoriaField.getText().trim();
        String quantidadeStr = quantidadeField.getText().trim();
        String precoStr = precoField.getText().trim();
        String idStr = idField.getText().trim(); 

        if (codigo.isEmpty() || marca.isEmpty() || modelo.isEmpty() || categoria.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty()) {
            showAlert("Erro de Validação", "Todos os campos são obrigatórios!");
            return;
        }

        double quantidade;
        double preco;
        try {
            quantidade = Double.parseDouble(quantidadeStr);
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            showAlert("Erro de Validação", "Quantidade e Preço devem ser números válidos!");
            return;
        }

        Produto produtoParaSalvar;
        if (idStr.isEmpty()) {
            produtoParaSalvar = new Produto();
        } else {
            produtoParaSalvar = new Produto();
            produtoParaSalvar.setId(idStr); 
        }

        produtoParaSalvar.setCodigo(codigo);
        produtoParaSalvar.setMarca(marca);
        produtoParaSalvar.setModelo(modelo);
        produtoParaSalvar.setCategoria(categoria);
        produtoParaSalvar.setQuantidade(quantidade);
        produtoParaSalvar.setPreco(preco);

        try {
            if (idStr.isEmpty()) {
                produtoDAO.inserir(produtoParaSalvar);
                showAlert("Sucesso", "Produto cadastrado com sucesso!");
            } else {
                produtoDAO.editar(produtoParaSalvar);
                showAlert("Sucesso", "Produto atualizado com sucesso!");
            }

            if (produtoParaSalvar.getQuantidade() < 5) {
                NotificacaoService.getInstance().adicionar(
                    "Produto: " + produtoParaSalvar.getMarca() + " " + produtoParaSalvar.getModelo() +
                    " | Código: " + produtoParaSalvar.getCodigo() +
                    " | Quantidade: " + produtoParaSalvar.getQuantidade() +
                    " - Estoque baixo!"
                );
            }

            limparCampos(); 
            carregarProdutosNaTabela(); 
        } catch (SQLException e) {
            showErrorAlert("Erro no Banco de Dados", "Erro ao salvar produto: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao salvar o produto.");
            e.printStackTrace();
        }
    }


    @FXML
    private void editarProduto() {
        Produto produtoSelecionado = produtosTableView.getSelectionModel().getSelectedItem();

        if (produtoSelecionado == null) {
            showAlert("Nenhum produto selecionado", "Por favor, selecione um produto na tabela para editar.");
            return;
        }

        idField.setText(produtoSelecionado.getId()); 
        codigoField.setText(produtoSelecionado.getCodigo());
        marcaField.setText(produtoSelecionado.getMarca());
        modeloField.setText(produtoSelecionado.getModelo());
        categoriaField.setText(produtoSelecionado.getCategoria());
        quantidadeField.setText(String.valueOf(produtoSelecionado.getQuantidade())); 
        precoField.setText(String.valueOf(produtoSelecionado.getPreco())); 
    }

    @FXML
    private void excluirProduto() {
        Produto produtoSelecionado = produtosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            showAlert("Erro de Seleção", "Nenhum produto selecionado para exclusão!"); 
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação de Exclusão");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Tem certeza que deseja excluir o produto selecionado: " + produtoSelecionado.getCodigo() + "?"); // Mostra o código do produto
        confirmAlert.initOwner(produtosTableView.getScene().getWindow()); 
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return; 
        }

        try {
            
            int idParaExcluir = Integer.parseInt(produtoSelecionado.getId());
            produtoDAO.excluir(idParaExcluir);
            showAlert("Sucesso", "Produto excluído com sucesso!");
            limparCampos(); 
            carregarProdutosNaTabela(); 
        } catch (SQLException e) {
            showErrorAlert("Erro no Banco de Dados", "Erro ao excluir produto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showErrorAlert("Erro de ID", "ID do produto selecionado inválido para exclusão: " + produtoSelecionado.getId());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao excluir o produto.");
            e.printStackTrace();
        }
    }

    private void carregarProdutosNaTabela() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            masterData.clear();
            masterData.addAll(produtos);
        } catch (Exception e) {
            showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao carregar os produtos.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        idField.clear(); 
        codigoField.clear();
        marcaField.clear();
        modeloField.clear();
        categoriaField.clear();
        quantidadeField.clear();
        precoField.clear();
        produtosTableView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Window window = getWindow();
        if (window != null) {
            alert.initOwner(window);
        }
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Window window = getWindow();
        if (window != null) {
            alert.initOwner(window);
        }
        alert.showAndWait();
    }

    private Window getWindow() {
        Window window = null;
        if (produtosTableView != null && produtosTableView.getScene() != null) {
            window = produtosTableView.getScene().getWindow();
        } else if (idField != null && idField.getScene() != null) {
            window = idField.getScene().getWindow();
        }
        return window;
    }
}