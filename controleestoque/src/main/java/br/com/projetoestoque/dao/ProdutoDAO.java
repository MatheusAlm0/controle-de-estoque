package br.com.projetoestoque.dao;

import br.com.projetoestoque.model.ConexaoBancoDeDados;
import br.com.projetoestoque.model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "INSERT INTO produtos (nome, categoria, preco) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getCategoria());
            statement.setDouble(3, produto.getPreco());
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
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setPreco(resultSet.getDouble("preco"));
                return produto;
            }
            return null;
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "SELECT * FROM produtos";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setCategoria(resultSet.getString("categoria"));
                produto.setPreco(resultSet.getDouble("preco"));
                produtos.add(produto);
            }
            return produtos;
        }
    }

    public void atualizar(Produto produto) throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "UPDATE produtos SET nome = ?, categoria = ?, preco = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getCategoria());
            statement.setDouble(3, produto.getPreco());
            statement.setInt(4, produto.getId());
            statement.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "DELETE FROM produtos WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}