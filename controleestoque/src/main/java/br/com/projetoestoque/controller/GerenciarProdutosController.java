package br.com.projetoestoque.controller;

import java.sql.SQLException;
import java.util.List;
import br.com.projetoestoque.dao.ProdutoDAO;
import br.com.projetoestoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // Importar FilteredList
import javafx.collections.transformation.SortedList; // Importar SortedList (opcional, mas recomendado para a tabela)
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

    // Novo campo para a busca
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

    // Lista original de todos os produtos
    private ObservableList<Produto> masterData = FXCollections.observableArrayList();

    // Lista filtrada para a tabela
    private FilteredList<Produto> filteredData;

    @FXML
    public void initialize() {
        // Configurar as CellValueFactories para cada coluna da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colquantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Carregar os dados iniciais na masterData
        carregarProdutosNaTabela();

        // 1. Wrap the ObservableList in a FilteredList (initially displays all data).
        filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        campoBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(produto -> {
                // If filter text is empty, display all products.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare marca and modelo with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (produto.getMarca().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches marca.
                } else if (produto.getModelo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches modelo.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        // (Optional: Add a Comparator if you want custom sorting beyond table column clicks)
        SortedList<Produto> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // Otherwise, sorting the table columns would have no effect.
        sortedData.comparatorProperty().bind(produtosTableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
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
        String idStr = idField.getText().trim(); // Capturar o ID

        // Verificar se os campos obrigatórios estão preenchidos
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
            // Se o ID está vazio, é um novo produto
            produtoParaSalvar = new Produto();
        } else {
            // Caso contrário, é um produto existente que será atualizado
            produtoParaSalvar = new Produto();
            produtoParaSalvar.setId(idStr); // Definir o ID para identificação
        }

        // Preencher os dados
        produtoParaSalvar.setCodigo(codigo);
        produtoParaSalvar.setMarca(marca);
        produtoParaSalvar.setModelo(modelo);
        produtoParaSalvar.setCategoria(categoria);
        produtoParaSalvar.setQuantidade(quantidade);
        produtoParaSalvar.setPreco(preco);

        try {
            if (idStr.isEmpty()) {
                // Se o ID estiver vazio, é um novo produto, então chama o método para inserir
                produtoDAO.inserir(produtoParaSalvar);
                showAlert("Sucesso", "Produto cadastrado com sucesso!");
            } else {
                // Caso contrário, faz a atualização
                produtoDAO.editar(produtoParaSalvar);
                showAlert("Sucesso", "Produto atualizado com sucesso!");
            }

            limparCampos(); // Limpar os campos após o salvar
            carregarProdutosNaTabela(); // Atualiza a masterData e o filteredData
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

        // Preencher os campos com os dados do produto selecionado
        idField.setText(produtoSelecionado.getId()); // Preencher o campo ID
        codigoField.setText(produtoSelecionado.getCodigo());
        marcaField.setText(produtoSelecionado.getMarca());
        modeloField.setText(produtoSelecionado.getModelo());
        categoriaField.setText(produtoSelecionado.getCategoria());
        quantidadeField.setText(String.valueOf(produtoSelecionado.getQuantidade())); // Converter para String
        precoField.setText(String.valueOf(produtoSelecionado.getPreco())); // Converter para String
    }

    @FXML
    private void excluirProduto() {
        Produto produtoSelecionado = produtosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            showAlert("Erro de Seleção", "Nenhum produto selecionado para exclusão!"); // Mensagem mais específica
            return;
        }

        // Confirmação antes de excluir (recomendado)
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação de Exclusão");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Tem certeza que deseja excluir o produto selecionado: " + produtoSelecionado.getCodigo() + "?"); // Mostra o código do produto
        confirmAlert.initOwner(produtosTableView.getScene().getWindow()); // Define o dono do alerta
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return; // Usuário cancelou a exclusão
        }

        try {
            // Converte o id (String do Model) para int para o método excluir do DAO
            int idParaExcluir = Integer.parseInt(produtoSelecionado.getId());
            produtoDAO.excluir(idParaExcluir);
            showAlert("Sucesso", "Produto excluído com sucesso!");
            limparCampos(); // Limpar campos após exclusão
            carregarProdutosNaTabela(); // Atualiza a masterData e o filteredData
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
            // Limpa a masterData antes de adicionar os novos dados
            masterData.clear();
            masterData.addAll(produtos);
            // Não atualizamos diretamente o TableView aqui.
            // O FilteredList e SortedList que estão bindados ao TableView serão atualizados
            // automaticamente quando a masterData for modificada.
        } catch (Exception e) {
            showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao carregar os produtos.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        idField.clear(); // Limpar o campo ID se for o comportamento desejado após cadastro
        codigoField.clear();
        marcaField.clear();
        modeloField.clear();
        categoriaField.clear();
        quantidadeField.clear();
        precoField.clear();
        produtosTableView.getSelectionModel().clearSelection(); // Opcional: Remover a seleção da tabela após limpar ou salvar/excluir
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
        Alert alert = new Alert(Alert.AlertType.ERROR); // Tipo ERROR
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