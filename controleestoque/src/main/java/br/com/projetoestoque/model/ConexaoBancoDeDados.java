package br.com.projetoestoque.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBancoDeDados {
    private static final String URL = "jdbc:h2:file:C:/Users/lscal/Desktop/controle-de-estoque/banco_de_dados"; // jdbc:h2:./banco_de_dados significa que o DB esta armazenando os dados em umarquivo
    private static final String USUARIO = "admin";
    private static final String SENHA = "123";

    @SuppressWarnings("exports")
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}