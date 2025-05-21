package br.com.projetoestoque.dao;

import br.com.projetoestoque.model.ConexaoBancoDeDados;
import br.com.projetoestoque.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "INSERT INTO produtos (codigo, marca, modelo, categoria, quantidade, preco) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, produto.getCodigo());
            statement.setString(2, produto.getMarca());
            statement.setString(3, produto.getModelo());
            statement.setString(4, produto.getCategoria());
            statement.setInt(5, (int) produto.getQuantidade());
            statement.setDouble(6, produto.getPreco());

            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getString("id"));
                produto.setCodigo(resultSet.getString("codigo"));
                produto.setMarca(resultSet.getString("marca"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setQuantidade(resultSet.getDouble("quantidade"));
                produto.setPreco(resultSet.getDouble("preco"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void editar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET codigo = ?, marca = ?, modelo = ?, categoria = ?, quantidade = ?, preco = ? WHERE id = ?";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produto.getCodigo());
            statement.setString(2, produto.getMarca());
            statement.setString(3, produto.getModelo());
            statement.setString(4, produto.getCategoria());
            statement.setDouble(5, produto.getQuantidade());
            statement.setDouble(6, produto.getPreco());
            statement.setString(7, produto.getId());
            statement.executeUpdate();
        }
    }

    public Produto buscarPorId(int id) throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "SELECT * FROM produtos WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getString("id"));
                produto.setCodigo(resultSet.getString("codigo"));
                produto.setMarca(resultSet.getString("marca"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setQuantidade(resultSet.getDouble("quantidade"));
                produto.setPreco(resultSet.getDouble("preco"));
                return produto;
            }
            return null;
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             // Prepare the SQL statement
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setMarca(rs.getString("marca"));
                p.setModelo(rs.getString("modelo"));
                p.setCategoria(rs.getString("categoria"));
                p.setPreco(rs.getDouble("preco"));
                p.setQuantidade(rs.getInt("quantidade"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // ==============================
    // 📉 Produtos com estoque baixo
    // ==============================
    public List<Produto> listarProdutosComEstoqueBaixo(double limite) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE quantidade < ?";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, limite);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getString("id"));
                produto.setCodigo(resultSet.getString("codigo"));
                produto.setMarca(resultSet.getString("marca"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setQuantidade(resultSet.getDouble("quantidade"));
                produto.setPreco(resultSet.getDouble("preco"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    // ===========================
    // 🔕 Produtos inativos (0 un)
    // ===========================
    public List<Produto> listarProdutosInativos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE quantidade = 0";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getString("id"));
                produto.setCodigo(resultSet.getString("codigo"));
                produto.setMarca(resultSet.getString("marca"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setQuantidade(resultSet.getDouble("quantidade"));
                produto.setPreco(resultSet.getDouble("preco"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public List<Produto> listarProdutosInativos(int dias) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos p WHERE NOT EXISTS (" +
                     "SELECT 1 FROM movimentacoes m WHERE m.produto_id = p.id AND m.data >= NOW() - INTERVAL '" + dias + " days')";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getString("id"));
                produto.setCodigo(resultSet.getString("codigo"));
                produto.setMarca(resultSet.getString("marca"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setQuantidade(resultSet.getDouble("quantidade"));
                produto.setPreco(resultSet.getDouble("preco"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    // ============================
    // 📊 Resumo geral por categoria
    // ============================
    public Map<String, Integer> obterResumoPorCategoria() {
        Map<String, Integer> resumo = new HashMap<>();
        String sql = "SELECT categoria, COUNT(*) AS total FROM produtos GROUP BY categoria";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String categoria = resultSet.getString("categoria");
                int total = resultSet.getInt("total");
                resumo.put(categoria, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resumo;
    }

    public void processarOpcao(String opcao, StringBuilder sb) {
        switch (opcao) {
            case "Produtos Inativos":
                List<Produto> inativos = listarProdutosInativos(30); // 30 dias sem movimentação
                sb.append("=== Produtos Inativos (sem movimentação nos últimos 30 dias) ===\n");
                for (Produto p : inativos) {
                    sb.append(String.format("%s %s | Qtde: %.2f\n", p.getMarca(), p.getModelo(), p.getQuantidade()));
                }
                break;
            // ...other cases...
        }
    }

    public List<String> listarMovimentacoesRecentes(int dias) {
        List<String> movimentacoes = new ArrayList<>();
        String sql = "SELECT m.*, p.marca, p.modelo FROM movimentacoes m " +
                     "JOIN produtos p ON m.produto_id = p.id " +
                     "WHERE m.data >= NOW() - INTERVAL '" + dias + " days' ORDER BY m.data DESC";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String linha = String.format(
                    "[%s] %s %s | %s | Qtde: %d",
                    rs.getTimestamp("data").toLocalDateTime().toString(),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("tipo"),
                    rs.getInt("quantidade")
                );
                movimentacoes.add(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimentacoes;
    }
}
