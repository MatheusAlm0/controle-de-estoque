package br.com.projetoestoque.controller;

import br.com.projetoestoque.dao.ProdutoDAO;
import br.com.projetoestoque.model.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Map;

public class VisualizarRelatoriosController {

    @FXML
    private ComboBox<String> relatorioComboBox;

    @FXML
    private TextArea relatorioTextArea;

    private final ProdutoDAO dao = new ProdutoDAO();

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
    }

    @FXML
    private void gerarRelatorio() {
        String tipoRelatorio = relatorioComboBox.getValue();
        StringBuilder sb = new StringBuilder();

        switch (tipoRelatorio) {
            case "Resumo Geral":
                List<Produto> todos = dao.listarTodos();
                sb.append("=== Resumo Geral do Estoque ===\n");
                for (Produto p : todos) {
                    sb.append(String.format("%s %s | Cat: %s | Qtde: %.2f | Preço: %.2f\n",
                            p.getMarca(), p.getModelo(), p.getCategoria(), p.getQuantidade(), p.getPreco()));
                }
                break;

            case "Estoque Baixo":
                List<Produto> baixos = dao.listarProdutosComEstoqueBaixo(5); // Limite pode ser ajustado
                sb.append("=== Produtos com Estoque Baixo (menos de 5) ===\n");
                for (Produto p : baixos) {
                    sb.append(String.format("%s %s | Qtde: %.2f\n", p.getMarca(), p.getModelo(), p.getQuantidade()));
                }
                break;

            case "Produtos por Categoria":
                Map<String, Integer> resumo = dao.obterResumoPorCategoria();
                sb.append("=== Produtos por Categoria ===\n");
                for (Map.Entry<String, Integer> entry : resumo.entrySet()) {
                    sb.append(String.format("%s: %d produtos\n", entry.getKey(), entry.getValue()));
                }
                break;

            case "Produtos Inativos":
                List<Produto> inativos = dao.listarProdutosInativos(30); // 30 dias sem movimentação
                sb.append("=== Produtos Inativos (sem movimentação nos últimos 30 dias) ===\n");
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
                sb.append("Selecione um tipo de relatório.");
        }

        relatorioTextArea.setText(sb.toString());
    }
}