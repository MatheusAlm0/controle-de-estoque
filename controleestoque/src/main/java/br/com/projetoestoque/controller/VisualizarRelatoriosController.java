package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.ProdutoDAO;
import br.com.projetoestoque.model.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class VisualizarRelatoriosController {

    @FXML
    private ComboBox<String> relatorioComboBox;

    @FXML
    private TextArea relatorioTextArea;

    @FXML
    private void initialize() {
        relatorioComboBox.getItems().addAll(
            "Resumo Geral",
            "Estoque Baixo",
            "Produtos por Categoria",
            "Produtos Inativos",
            "Entradas/Saídas Recentes"
        );
    }

    @FXML
    private void gerarRelatorio() {
        String tipoRelatorio = relatorioComboBox.getValue();
        ProdutoDAO dao = new ProdutoDAO();
        StringBuilder sb = new StringBuilder();

        switch (tipoRelatorio) {
            case "Resumo Geral":
                sb.append("=== Resumo Geral do Estoque ===\n");
                double totalValor = 0;
                int totalItens = 0;
                List<Produto> produtos = dao.listarTodos(); // <-- troque aqui
                for (Produto p : produtos) {
                    sb.append(String.format("%s %s | Qtde: %.2f | Preço: R$%.2f\n", p.getMarca(), p.getModelo(), p.getQuantidade(), p.getPreco()));
                    totalValor += p.getPreco() * p.getQuantidade();
                    totalItens += p.getQuantidade();
                }
                sb.append("\nTotal de itens: ").append(totalItens);
                sb.append("\nValor total estimado: R$").append(String.format("%.2f", totalValor));
                break;

            case "Estoque Baixo":
                sb.append("=== Produtos com Estoque Baixo (<= 5) ===\n");
                List<Produto> baixos = dao.listarProdutosComEstoqueBaixo(5);
                for (Produto p : baixos) {
                    sb.append(String.format("%s %s | Qtde: %.2f\n", p.getMarca(), p.getModelo(), p.getQuantidade()));
                }
                break;

            case "Produtos por Categoria":
                sb.append("=== Produtos Agrupados por Categoria ===\n");
                produtos = dao.listarTodos(); // <-- troque aqui
                produtos.stream()
                        .map(Produto::getCategoria)
                        .distinct()
                        .forEach(categoria -> {
                            sb.append("\nCategoria: ").append(categoria).append("\n");
                            produtos.stream()
                                    .filter(p -> p.getCategoria().equals(categoria))
                                    .forEach(p -> sb.append(String.format("  - %s %s | Qtde: %.2f\n", p.getMarca(), p.getModelo(), p.getQuantidade())));
                        });
                break;

            case "Produtos Inativos":
                sb.append("=== Produtos Inativos ===\n");
                List<Produto> inativos = dao.listarProdutosInativos();
                for (Produto p : inativos) {
                    sb.append(String.format("%s %s | Qtde: %.2f\n", p.getMarca(), p.getModelo(), p.getQuantidade()));
                }
                break;

            case "Entradas/Saídas Recentes":
                List<String> movs = dao.listarMovimentacoesRecentes(7); // últimos 7 dias
                sb.append("=== Entradas/Saídas Recentes (últimos 7 dias) ===\n");
                for (String linha : movs) {
                    sb.append(linha).append("\n");
                }
                break;

            default:
                sb.append("Relatório não reconhecido.");
        }

        relatorioTextArea.setText(sb.toString());
    }
}
