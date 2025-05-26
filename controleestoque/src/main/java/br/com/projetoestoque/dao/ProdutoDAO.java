package br.com.projetoestoque.dao;

import br.com.projetoestoque.model.ConexaoBancoDeDados;
import br.com.projetoestoque.model.Produto;
import br.com.projetoestoque.util.Sessao; // Importe a classe Sessao

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
        double quantidadeAnterior = 0;
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT quantidade FROM produtos WHERE id = ?")) {
            stmt.setString(1, produto.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                quantidadeAnterior = rs.getDouble("quantidade");
            }
        }

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

        double diferenca = produto.getQuantidade() - quantidadeAnterior;
        if (diferenca != 0) {
            String tipo = diferenca > 0 ? "entrada" : "saida";
            int quantidadeMov = (int) Math.abs(diferenca);
            registrarMovimentacao(produto.getId(), tipo, quantidadeMov);
        }
    }

    private void registrarMovimentacao(String produtoId, String tipo, int quantidade) throws SQLException {
        String sql = "INSERT INTO movimentacoes (produto_id, tipo, quantidade, usuario_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(produtoId));
            statement.setString(2, tipo);
            statement.setInt(3, quantidade);
            statement.setInt(4, Sessao.getUsuarioId()); // Adiciona o ID do usuário logado
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
                     "SELECT 1 FROM movimentacoes m WHERE m.produto_id = p.id AND m.data >= DATEADD('DAY', -" + dias + ", NOW()))";
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

    public List<MovimentacaoDetalhada> listarMovimentacoesRecentesDetalhado(int dias) {
        List<MovimentacaoDetalhada> movimentacoes = new ArrayList<>();
        String sql = "SELECT m.data, m.tipo, m.quantidade, p.marca, p.modelo, u.nome AS nome_usuario " +
                     "FROM movimentacoes m " +
                     "JOIN produtos p ON m.produto_id = p.id " +
                     "JOIN usuarios u ON m.usuario_id = u.id " +
                     "WHERE m.data >= DATEADD('DAY', -" + dias + ", NOW()) ORDER BY m.data DESC";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                MovimentacaoDetalhada mov = new MovimentacaoDetalhada();
                mov.setData(rs.getTimestamp("data").toLocalDateTime());
                mov.setTipo(rs.getString("tipo"));
                mov.setQuantidade(rs.getInt("quantidade"));
                mov.setMarca(rs.getString("marca"));
                mov.setModelo(rs.getString("modelo"));
                mov.setNomeUsuario(rs.getString("nome_usuario"));
                movimentacoes.add(mov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimentacoes;
    }

    public static class MovimentacaoDetalhada {
        private LocalDateTime data;
        private String tipo;
        private int quantidade;
        private String marca;
        private String modelo;
        private String nomeUsuario;

        public LocalDateTime getData() {
            return data;
        }

        public void setData(LocalDateTime data) {
            this.data = data;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public String getMarca() {
            return marca;
        }

        public void setMarca(String marca) {
            this.marca = marca;
        }

        public String getModelo() {
            return modelo;
        }

        public void setModelo(String modelo) {
            this.modelo = modelo;
        }

        public String getNomeUsuario() {
            return nomeUsuario;
        }

        public void setNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
        }
    }
}