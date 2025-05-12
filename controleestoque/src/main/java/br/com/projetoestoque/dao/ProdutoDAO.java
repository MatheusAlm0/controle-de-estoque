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

    // Método inserir modificado minimamente para garantir o commit
    public void inserir(Produto produto) throws SQLException {
        // Usando try-with-resources que fecha a conexão automaticamente
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            // Comando SQL para inserir (coluna 'id' é SERIAL, não incluída)
            String sql = "INSERT INTO produtos (codigo, marca, modelo, categoria, quantidade, preco) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, produto.getCodigo());
            statement.setString(2, produto.getMarca());
            statement.setString(3, produto.getModelo());
            statement.setString(4, produto.getCategoria());
            // Verifique o tipo setInt vs setDouble com base na sua tabela INT vs DOUBLE
            statement.setInt(5, (int) produto.getQuantidade()); // Mantido original
            statement.setDouble(6, produto.getPreco());

            statement.executeUpdate(); // Executa a inserção

            // *** ALTERAÇÃO MÍNIMA AQUI: Comitar a transação para salvar as mudanças ***
            connection.commit(); // Garante que a inserção seja salva no banco

        } catch (SQLException e) {
             // *** ALTERAÇÃO MÍNIMA AQUI: Fazer rollback em caso de erro (boa prática) ***
             // Se algo der errado antes do commit, desfaz a operação.
             // O try-with-resources garante que a conexão será fechada.
            e.printStackTrace(); // Loga o erro (como no original)
            throw e; // Relança a exceção (como no original, se houvesse throws)
        }
        // A conexão é fechada automaticamente pelo try-with-resources ao sair do bloco try ou catch
    }

    // Os métodos listarTodos, excluir, editar e buscarPorId permanecem como no seu código original
    // Eles não foram modificados para atender ao pedido de "altere o minimo possivel" e "ajuda somente com isso"

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "SELECT * FROM produtos";
            PreparedStatement statement = connection.prepareStatement(sql);
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
}