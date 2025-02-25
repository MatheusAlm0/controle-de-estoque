package br.com.projetoestoque.dao;

import br.com.projetoestoque.model.ConexaoBancoDeDados;
import br.com.projetoestoque.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void inserir(Usuario usuario) throws SQLException {
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "INSERT INTO usuarios (nome, nivel_acesso, senha) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getNivelAcesso());
            statement.setString(3, usuario.getSenha());
            statement.executeUpdate();
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection connection = ConexaoBancoDeDados.getConnection()) {
            String sql = "SELECT * FROM usuarios";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setNome(resultSet.getString("nome"));
                usuario.setNivelAcesso(resultSet.getString("nivel_acesso"));
                usuario.setSenha(resultSet.getString("senha"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void editar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, nivel_acesso = ?, senha = ? WHERE id = ?";
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getNivelAcesso());
            statement.setString(3, usuario.getSenha());
            statement.setInt(4, usuario.getId());
            statement.executeUpdate();
        }
    }
}