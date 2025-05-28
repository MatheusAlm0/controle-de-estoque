package br.com.projetoestoque.dao;

import br.com.projetoestoque.model.Notificacao;
import br.com.projetoestoque.model.ConexaoBancoDeDados;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {

    public void salvar(Notificacao notificacao) {
        String sql = "INSERT INTO notificacoes (mensagem, lida) VALUES (?, ?)";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notificacao.getMensagem());
            stmt.setBoolean(2, notificacao.isLida());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notificacao> listarTodas() {
        List<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacoes";
        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Notificacao n = new Notificacao(rs.getString("mensagem"));
                if (rs.getBoolean("lida")) n.marcarComoLida();
                lista.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
