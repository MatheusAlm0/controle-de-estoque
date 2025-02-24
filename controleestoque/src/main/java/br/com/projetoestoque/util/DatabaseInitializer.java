package br.com.projetoestoque.util;

import br.com.projetoestoque.model.ConexaoBancoDeDados;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     DatabaseInitializer.class.getResourceAsStream("/schema.sql")));
             Statement statement = connection.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            statement.execute(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}