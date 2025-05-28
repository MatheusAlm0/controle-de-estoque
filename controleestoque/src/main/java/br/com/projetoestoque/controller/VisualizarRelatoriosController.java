package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.ProdutoDAO;
import br.com.projetoestoque.model.Produto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPane;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class VisualizarRelatoriosController {

    @FXML
    private ComboBox<String> relatorioComboBox;

    @FXML
    private ComboBox<String> periodoComboBox;

    @FXML
    private StackPane relatorioContainer;

    @FXML
    private TextArea relatorioTextArea;

    @FXML
    private TableView<MovimentacaoView> movimentacoesTableView;
    @FXML
    private TableColumn<MovimentacaoView, String> colDataMov;
    @FXML
    private TableColumn<MovimentacaoView, String> colProdutoMov;
    @FXML
    private TableColumn<MovimentacaoView, String> colTipoMov;
    @FXML
    private TableColumn<MovimentacaoView, String> colQuantidadeMov;
    @FXML
    private TableColumn<MovimentacaoView, String> colUsuarioMov;

    @FXML
    private TableView<ResumoGeralView> resumoGeralTableView;
    @FXML
    private TableColumn<ResumoGeralView, String> colProdutoResumo;
    @FXML
    private TableColumn<ResumoGeralView, String> colCategoriaResumo;
    @FXML
    private TableColumn<ResumoGeralView, Number> colQuantidadeResumo;
    @FXML
    private TableColumn<ResumoGeralView, Number> colPrecoResumo;

    @FXML
    private TableView<EstoqueBaixoView> estoqueBaixoTableView;
    @FXML
    private TableColumn<EstoqueBaixoView, String> colCodigoBaixo;
    @FXML
    private TableColumn<EstoqueBaixoView, String> colProdutoBaixo;
    @FXML
    private TableColumn<EstoqueBaixoView, String> colCategoriaBaixo;
    @FXML
    private TableColumn<EstoqueBaixoView, Number> colQuantidadeBaixo;

    @FXML
    private TableView<CategoriaView> categoriaTableView;
    @FXML
    private TableColumn<CategoriaView, String> colCategoria;
    @FXML
    private TableColumn<CategoriaView, Number> colTotalProdutos;

    @FXML
    private TableView<InativoView> inativosTableView;
    @FXML
    private TableColumn<InativoView, String> colCodigoInativo;
    @FXML
    private TableColumn<InativoView, String> colProdutoInativo;
    @FXML
    private TableColumn<InativoView, String> colCategoriaInativo;
    @FXML
    private TableColumn<InativoView, Number> colQuantidadeInativo;

    @FXML
    private Label lblValorTotalEstoque;

    @FXML
    private Label lblMensagemInativos;

    @FXML
    private Label lblMensagemEstoqueBaixo; // Adicionado para a mensagem de estoque baixo

    private final ProdutoDAO dao = new ProdutoDAO();
    private final ObservableList<MovimentacaoView> movimentacoesList = FXCollections.observableArrayList();
    private final ObservableList<ResumoGeralView> resumoGeralList = FXCollections.observableArrayList();
    private final ObservableList<EstoqueBaixoView> estoqueBaixoList = FXCollections.observableArrayList();
    private final ObservableList<CategoriaView> categoriaList = FXCollections.observableArrayList();
    private final ObservableList<InativoView> inativosList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        relatorioComboBox.getItems().clear();
        relatorioComboBox.getItems().addAll(
                "Resumo Geral",
                "Estoque Baixo",
                "Produtos por Categoria",
                "Produtos Inativos",
                "Entradas/Saídas Recentes"
        );
        relatorioComboBox.getSelectionModel().selectFirst();

        periodoComboBox.getItems().setAll(
            "Tudo", "1 semana", "1 mês", "3 meses", "6 meses", "1 ano"
        );
        periodoComboBox.getSelectionModel().selectFirst();
        periodoComboBox.setVisible(false);

        relatorioComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean mostrarPeriodo = "Entradas/Saídas Recentes".equals(newVal);
            periodoComboBox.setVisible(mostrarPeriodo);
        });

        // Inicializa as colunas da TableView de Movimentações
        colDataMov.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        colProdutoMov.setCellValueFactory(cellData -> cellData.getValue().produtoNomeProperty());
        colTipoMov.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colQuantidadeMov.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asString());
        colUsuarioMov.setCellValueFactory(cellData -> cellData.getValue().nomeUsuarioProperty());
        movimentacoesTableView.setItems(movimentacoesList);

        // Inicializa as colunas da TableView de Resumo Geral
        colProdutoResumo.setCellValueFactory(cellData -> cellData.getValue().produtoNomeProperty());
        colCategoriaResumo.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
        colQuantidadeResumo.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty());
        colPrecoResumo.setCellValueFactory(cellData -> cellData.getValue().precoProperty());
        resumoGeralTableView.setItems(resumoGeralList);

        // Inicializa as colunas da TableView de Estoque Baixo
        colCodigoBaixo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colProdutoBaixo.setCellValueFactory(cellData -> cellData.getValue().produtoNomeProperty());
        colCategoriaBaixo.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
        colQuantidadeBaixo.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty());
        estoqueBaixoTableView.setItems(estoqueBaixoList);

        // Inicializa as colunas da TableView de Produtos por Categoria
        colCategoria.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
        colTotalProdutos.setCellValueFactory(cellData -> cellData.getValue().totalProdutosProperty());
        categoriaTableView.setItems(categoriaList);

        // Inicializa as colunas da TableView de Produtos Inativos
        colCodigoInativo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colProdutoInativo.setCellValueFactory(cellData -> cellData.getValue().produtoNomeProperty());
        colCategoriaInativo.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());
        colQuantidadeInativo.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty());
        inativosTableView.setItems(inativosList);
    }

    @FXML
    private void gerarRelatorio() {
        String tipoRelatorio = relatorioComboBox.getValue();
        System.out.println("Botão 'Gerar Relatório' clicado. Tipo selecionado: " + tipoRelatorio);

        relatorioTextArea.setVisible(false);
        movimentacoesTableView.setVisible(false);
        resumoGeralTableView.setVisible(false);
        estoqueBaixoTableView.setVisible(false);
        categoriaTableView.setVisible(false);
        inativosTableView.setVisible(false);
        relatorioTextArea.setText("");
        movimentacoesList.clear();
        resumoGeralList.clear();
        estoqueBaixoList.clear();
        categoriaList.clear();
        inativosList.clear();

        switch (tipoRelatorio) {
            case "Resumo Geral":
                System.out.println("Entrou no filtro: Resumo Geral");
                resumoGeralTableView.setVisible(true);
                List<Produto> todos = dao.listarTodos();
                for (Produto p : todos) {
                    resumoGeralList.add(new ResumoGeralView(
                            p.getMarca() + " " + p.getModelo(),
                            p.getCategoria(),
                            p.getQuantidade(),
                            p.getPreco()
                    ));
                }
                // Calcule o valor total
                double valorTotal = resumoGeralList.stream()
                        .mapToDouble(item -> item.getQuantidade() * item.getPreco())
                        .sum();

                // Formate o valor com separador de milhar e duas casas decimais
                String valorFormatado = String.format("Valor total do estoque: R$ %,.2f", valorTotal);

                // Exiba o label
                lblValorTotalEstoque.setText(valorFormatado);
                lblValorTotalEstoque.setVisible(true);
                break;

            case "Estoque Baixo":
                System.out.println("Entrou no filtro: Estoque Baixo");
                estoqueBaixoTableView.setVisible(true);
                lblMensagemEstoqueBaixo.setVisible(true);
                List<Produto> baixos = dao.listarProdutosComEstoqueBaixo(5);
                for (Produto p : baixos) {
                    estoqueBaixoList.add(new EstoqueBaixoView(
                            p.getCodigo(),             // Código
                            p.getMarca() + " " + p.getModelo(), // Produto
                            p.getCategoria(),          // Categoria
                            p.getQuantidade()          // Quantidade
                    ));
                }
                break;

            case "Produtos por Categoria":
                System.out.println("Entrou no filtro: Produtos por Categoria");
                categoriaTableView.setVisible(true);
                Map<String, Integer> resumoCategorias = dao.obterResumoPorCategoria();
                for (Map.Entry<String, Integer> entry : resumoCategorias.entrySet()) {
                    categoriaList.add(new CategoriaView(
                            entry.getKey(),
                            entry.getValue()
                    ));
                }
                break;

            case "Produtos Inativos":
                System.out.println("Entrou no filtro: Produtos Inativos");
                inativosTableView.setVisible(true);
                List<Produto> inativos = dao.listarProdutosInativos(30);
                for (Produto p : inativos) {
                    inativosList.add(new InativoView(
                            p.getCodigo(),
                            p.getMarca() + " " + p.getModelo(),
                            p.getCategoria(),
                            p.getQuantidade()
                    ));
                }
                lblMensagemInativos.setText("Produtos sem movimentação por 30 dias ou mais");
                lblMensagemInativos.setVisible(true); // <-- AGORA VAI APARECER!
                break;

            case "Entradas/Saídas Recentes":
                System.out.println("Entrou no filtro: Entradas/Saídas Recentes");
                movimentacoesTableView.setVisible(true);
                String periodo = periodoComboBox.getValue();
                System.out.println("Período selecionado: " + periodo);
                int dias = -1; // -1 para "Tudo"
                if (periodo != null) {
                    switch (periodo) {
                        case "1 semana": dias = 7; break;
                        case "1 mês": dias = 30; break;
                        case "3 meses": dias = 90; break;
                        case "6 meses": dias = 180; break;
                        case "1 ano": dias = 365; break;
                        case "Tudo": dias = -1; break;
                    }
                }
                List<ProdutoDAO.MovimentacaoDetalhada> movimentacoesDetalhadas =
                    (dias == -1)
                        ? dao.listarTodasMovimentacoesDetalhado()
                        : dao.listarMovimentacoesRecentesDetalhado(dias);

                System.out.println("Movimentações encontradas: " + movimentacoesDetalhadas.size());

                for (ProdutoDAO.MovimentacaoDetalhada mov : movimentacoesDetalhadas) {
                    movimentacoesList.add(new MovimentacaoView(
                            mov.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                            mov.getMarca() + " " + mov.getModelo(),
                            mov.getTipo(),
                            mov.getQuantidade(),
                            mov.getNomeUsuario()
                    ));
                }
                movimentacoesTableView.setItems(movimentacoesList);
                break;

            default:
                System.out.println("Nenhum filtro selecionado.");
                relatorioTextArea.setVisible(true);
                relatorioTextArea.setText("Selecione um tipo de relatório.");
        }
        lblValorTotalEstoque.setVisible(relatorioComboBox.getValue().equals("Resumo Geral"));
        lblMensagemInativos.setVisible(relatorioComboBox.getValue().equals("Produtos Inativos"));
    }

    // Classes internas para representar os dados das TableViews
    public static class MovimentacaoView {
        private final SimpleStringProperty data;
        private final SimpleStringProperty produtoNome;
        private final SimpleStringProperty tipo;
        private final SimpleIntegerProperty quantidade;
        private final SimpleStringProperty nomeUsuario;

        public MovimentacaoView(String data, String produtoNome, String tipo, int quantidade, String nomeUsuario) {
            this.data = new SimpleStringProperty(data);
            this.produtoNome = new SimpleStringProperty(produtoNome);
            this.tipo = new SimpleStringProperty(tipo);
            this.quantidade = new SimpleIntegerProperty(quantidade);
            this.nomeUsuario = new SimpleStringProperty(nomeUsuario);
        }
        public String getData() { return data.get(); }
        public SimpleStringProperty dataProperty() { return data; }
        public String getProdutoNome() { return produtoNome.get(); }
        public SimpleStringProperty produtoNomeProperty() { return produtoNome; }
        public String getTipo() { return tipo.get(); }
        public SimpleStringProperty tipoProperty() { return tipo; }
        public int getQuantidade() { return quantidade.get(); }
        public SimpleIntegerProperty quantidadeProperty() { return quantidade; }
        public String getNomeUsuario() { return nomeUsuario.get(); }
        public SimpleStringProperty nomeUsuarioProperty() { return nomeUsuario; }
    }

    public static class ResumoGeralView {
        private final SimpleStringProperty produtoNome;
        private final SimpleStringProperty categoria;
        private final SimpleDoubleProperty quantidade;
        private final SimpleDoubleProperty preco;

        public ResumoGeralView(String produtoNome, String categoria, double quantidade, double preco) {
            this.produtoNome = new SimpleStringProperty(produtoNome);
            this.categoria = new SimpleStringProperty(categoria);
            this.quantidade = new SimpleDoubleProperty(quantidade);
            this.preco = new SimpleDoubleProperty(preco);
        }
        public String getProdutoNome() { return produtoNome.get(); }
        public SimpleStringProperty produtoNomeProperty() { return produtoNome; }
        public String getCategoria() { return categoria.get(); }
        public SimpleStringProperty categoriaProperty() { return categoria; }
        public double getQuantidade() { return quantidade.get(); }
        public SimpleDoubleProperty quantidadeProperty() { return quantidade; }
        public double getPreco() { return preco.get(); }
        public SimpleDoubleProperty precoProperty() { return preco; }
    }

    public static class EstoqueBaixoView {
        private final SimpleStringProperty codigo;
        private final SimpleStringProperty produtoNome;
        private final SimpleStringProperty categoria;
        private final SimpleDoubleProperty quantidade;

        public EstoqueBaixoView(String codigo, String produtoNome, String categoria, double quantidade) {
            this.codigo = new SimpleStringProperty(codigo);
            this.produtoNome = new SimpleStringProperty(produtoNome);
            this.categoria = new SimpleStringProperty(categoria);
            this.quantidade = new SimpleDoubleProperty(quantidade);
        }

        public String getCodigo() { return codigo.get(); }
        public SimpleStringProperty codigoProperty() { return codigo; }
        public String getProdutoNome() { return produtoNome.get(); }
        public SimpleStringProperty produtoNomeProperty() { return produtoNome; }
        public String getCategoria() { return categoria.get(); }
        public SimpleStringProperty categoriaProperty() { return categoria; }
        public double getQuantidade() { return quantidade.get(); }
        public SimpleDoubleProperty quantidadeProperty() { return quantidade; }
    }

    public static class CategoriaView {
        private final SimpleStringProperty categoria;
        private final SimpleIntegerProperty totalProdutos;

        public CategoriaView(String categoria, int totalProdutos) {
            this.categoria = new SimpleStringProperty(categoria);
            this.totalProdutos = new SimpleIntegerProperty(totalProdutos);
        }
        public String getCategoria() { return categoria.get(); }
        public SimpleStringProperty categoriaProperty() { return categoria; }
        public int getTotalProdutos() { return totalProdutos.get(); }
        public SimpleIntegerProperty totalProdutosProperty() { return totalProdutos; }
    }

    public static class InativoView {
        private final SimpleStringProperty codigo;
        private final SimpleStringProperty produtoNome;
        private final SimpleStringProperty categoria;
        private final SimpleDoubleProperty quantidade;

        public InativoView(String codigo, String produtoNome, String categoria, double quantidade) {
            this.codigo = new SimpleStringProperty(codigo);
            this.produtoNome = new SimpleStringProperty(produtoNome);
            this.categoria = new SimpleStringProperty(categoria);
            this.quantidade = new SimpleDoubleProperty(quantidade);
        }
        public String getCodigo() { return codigo.get(); }
        public SimpleStringProperty codigoProperty() { return codigo; }
        public String getProdutoNome() { return produtoNome.get(); }
        public SimpleStringProperty produtoNomeProperty() { return produtoNome; }
        public String getCategoria() { return categoria.get(); }
        public SimpleStringProperty categoriaProperty() { return categoria; }
        public double getQuantidade() { return quantidade.get(); }
        public SimpleDoubleProperty quantidadeProperty() { return quantidade; }
    }
}