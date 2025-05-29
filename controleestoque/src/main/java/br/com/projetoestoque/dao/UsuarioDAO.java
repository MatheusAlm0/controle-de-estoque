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
        String sql = "INSERT INTO USUARIOS (NOME, NIVEL_ACESSO, SENHA, EMAIL) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getNivelAcesso());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEmail());
            stmt.executeUpdate();
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
                usuario.setEmail(resultSet.getString("email"));
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

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIOS SET NOME = ?, NIVEL_ACESSO = ?, SENHA = ?, EMAIL = ? WHERE ID = ?";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getNivelAcesso());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEmail());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Método que autentica o usuário e retorna seu nível de acesso.
     * @param username Nome do usuário
     * @param password Senha do usuário
     * @return Nível de acesso (admin, operador, gerente, visualizador) ou nulo se não for autenticado.
     */
    public Integer autenticar(String username, String password) {
    String sql = "SELECT id FROM usuarios WHERE nome = ? AND senha = ?";
    try (Connection connection = ConexaoBancoDeDados.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int idUsuario = resultSet.getInt("id");
            System.out.println("Usuário autenticado com sucesso: " + username + " | ID: " + idUsuario);
            return idUsuario;
        } else {
            System.out.println("Falha na autenticação para o usuário: " + username);
            return null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

public Integer autenticarPorEmail(String email, String senha) {
    String sql = "SELECT id FROM usuarios WHERE email = ? AND senha = ?";
    try (Connection connection = ConexaoBancoDeDados.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, email);
        statement.setString(2, senha);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int idUsuario = resultSet.getInt("id");
            System.out.println("Usuário autenticado com sucesso: " + email + " | ID: " + idUsuario);
            return idUsuario;
        } else {
            System.out.println("Falha na autenticação para o email: " + email);
            return null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

public String obterNivelAcesso(String username) {
    String sql = "SELECT nivel_acesso FROM usuarios WHERE nome = ?";
    try (Connection connection = ConexaoBancoDeDados.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("nivel_acesso");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public String obterNivelAcessoPorEmail(String email) {
    String sql = "SELECT nivel_acesso FROM usuarios WHERE email = ?";
    try (Connection connection = ConexaoBancoDeDados.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("nivel_acesso");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}
