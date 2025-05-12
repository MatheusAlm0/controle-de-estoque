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
import javafx.scene.control.cell.PropertyValueFactory; // Importe esta classe

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

    // Adicione as injeções das colunas da TableView
    @FXML private TableColumn<Produto, String> colId;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colMarca;
    @FXML private TableColumn<Produto, String> colModelo;
    @FXML private TableColumn<Produto, String> colCategoria;
    // Tipos da coluna na TableView devem ser compatíveis com o tipo da propriedade no Model (Double para double)
    @FXML private TableColumn<Produto, Double> colquantidade;
    @FXML private TableColumn<Produto, Double> colPreco;

    // Instancie o DAO uma vez na classe
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    // Método initialize() - Chamado automaticamente após o FXML ser carregado
    @FXML
    public void initialize() {
        // Configurar as CellValueFactories para cada coluna da tabela
        // Garantindo que o nome da propriedade (em minúsculas) corresponda aos getters na classe Produto
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colquantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade")); // Já estava correto
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco")); // Já estava correto

        // Chame o método para carregar os dados quando a tela for inicializada
        carregarProdutosNaTabela();
    }

    @FXML
    private void salvarProduto() {
        // String id = idField.getText().trim(); // ID não é necessário para inserir um NOVO produto se ele for auto-incremento

        String codigo = codigoField.getText().trim();
        String marca = marcaField.getText().trim();
        String modelo = modeloField.getText().trim();
        String categoria = categoriaField.getText().trim();
        String quantidadeStr = quantidadeField.getText().trim();
        String precoStr = precoField.getText().trim();

        // Verificar se os campos obrigatórios estão preenchidos (removida a verificação do ID)
        if (codigo.isEmpty() || marca.isEmpty() || modelo.isEmpty() || categoria.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty()) {
            showAlert("Erro de Validação", "Todos os campos são obrigatórios!"); // Mensagem mais específica
            return;
        }

        double quantidade; // Mantido como double conforme seu Model e UI
        double preco;
        try {
            quantidade = Double.parseDouble(quantidadeStr);
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            showAlert("Erro de Validação", "Quantidade e Preço devem ser números válidos!");
            return;
        }

        // Crie o objeto Produto. Não passe o 'id' se for um novo produto e o banco gerar o ID.
        Produto novoProduto = new Produto(); // Use o construtor vazio
        // Não configure o ID para inserção, o banco cuidará disso
        novoProduto.setCodigo(codigo);
        novoProduto.setMarca(marca);
        novoProduto.setModelo(modelo);
        novoProduto.setCategoria(categoria);
        novoProduto.setQuantidade(quantidade); // Use o valor convertido
        novoProduto.setPreco(preco); // Use o valor convertido

        try {
            // ProdutoDAO produtoDAO = new ProdutoDAO(); // Não instancie o DAO aqui, use a instância da classe
            produtoDAO.inserir(novoProduto);
            showAlert("Sucesso", "Produto cadastrado com sucesso!");
            limparCampos(); // Limpar os campos após o cadastro bem-sucedido
            carregarProdutosNaTabela(); // Atualiza a tabela após o cadastro
        } catch (SQLException e) {
            // Usando showErrorAlert para erros de banco de dados
            showErrorAlert("Erro no Banco de Dados", "Erro ao cadastrar produto: " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para depuração
        } catch (Exception e) {
            // Capturar outras exceções inesperadas
             showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao salvar o produto.");
             e.printStackTrace();
        }
    }

    @FXML
    private void editarProduto() {
        // Implementação do método editarProduto
        // Você precisaria obter o produto selecionado da tabela,
        // preencher os campos do formulário com os dados desse produto
        // e, ao salvar, chamar produtoDAO.editar(produto) com o ID do produto.
        showAlert("Funcionalidade não implementada", "Função de edição de produto ainda não implementada.");
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
            // ProdutoDAO produtoDAO = new ProdutoDAO(); // Não instancie o DAO aqui
            // Converte o id (String do Model) para int para o método excluir do DAO
            int idParaExcluir = Integer.parseInt(produtoSelecionado.getId());
            produtoDAO.excluir(idParaExcluir);
            showAlert("Sucesso", "Produto excluído com sucesso!");
            limparCampos(); // Limpar campos após exclusão
            carregarProdutosNaTabela(); // Atualiza a tabela após a exclusão
        } catch (SQLException e) {
            showErrorAlert("Erro no Banco de Dados", "Erro ao excluir produto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Ocorre se o ID do produto selecionado não for um número válido ao converter para int
            showErrorAlert("Erro de ID", "ID do produto selecionado inválido para exclusão: " + produtoSelecionado.getId());
            e.printStackTrace();
        } catch (Exception e) {
             // Captura outras exceções inesperadas durante a exclusão
             showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao excluir o produto.");
             e.printStackTrace();
        }
    }

    // Método para carregar os produtos do banco e exibir na tabela
    // Modificado para não lançar SQLException e tratar a exceção internamente
    private void carregarProdutosNaTabela() {
        // ProdutoDAO produtoDAO = new ProdutoDAO(); // Não instancie o DAO aqui
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            ObservableList<Produto> produtosList = FXCollections.observableArrayList(produtos);
            produtosTableView.setItems(produtosList);
        } catch (Exception e) {
             // Captura outras exceções inesperadas durante o carregamento
             showErrorAlert("Erro Inesperado", "Ocorreu um erro inesperado ao carregar os produtos.");
             e.printStackTrace();
        }
    }

    // Método auxiliar para limpar os campos do formulário
    private void limparCampos() {
         idField.clear(); // Limpar o campo ID se for o comportamento desejado após cadastro
         codigoField.clear();
         marcaField.clear();
         modeloField.clear();
         categoriaField.clear();
         quantidadeField.clear();
         precoField.clear();
         // Opcional: Remover a seleção da tabela após limpar ou salvar/excluir
         produtosTableView.getSelectionModel().clearSelection();
    }


    // Método auxiliar para exibir alertas de informação/sucesso
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Tentar encontrar a janela dona do alerta de forma mais segura
        Window window = getWindow();
        if (window != null) {
             alert.initOwner(window);
        }
        alert.showAndWait();
    }

    // Método auxiliar para exibir alertas de erro
     private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Tipo ERROR
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Tentar encontrar a janela dona do alerta
        Window window = getWindow();
        if (window != null) {
             alert.initOwner(window);
        }
        alert.showAndWait();
    }

    // Método auxiliar para obter a Window do palco atual
    private Window getWindow() {
        Window window = null;
        if (produtosTableView != null && produtosTableView.getScene() != null) {
             window = produtosTableView.getScene().getWindow();
        } else if (idField != null && idField.getScene() != null) {
             window = idField.getScene().getWindow();
        }
        // Adicione outros campos UI se forem mais confiáveis para obter a cena
        return window;
    }
}