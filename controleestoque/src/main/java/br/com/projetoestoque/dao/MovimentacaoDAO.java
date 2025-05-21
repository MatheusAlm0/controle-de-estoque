package br.com.projetoestoque.dao;

import br.com.projetoestoque.model.Movimentacao;
import br.com.projetoestoque.model.ConexaoBancoDeDados;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MovimentacaoDAO {

    public List<Movimentacao> listarRecentes(int limite) {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes ORDER BY data DESC LIMIT ?";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movimentacao m = new Movimentacao(
                        rs.getInt("id"),
                        rs.getInt("produto_id"),
                        rs.getInt("quantidade"),
                        rs.getString("tipo"),
                        rs.getTimestamp("data").toLocalDateTime()
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
} 
