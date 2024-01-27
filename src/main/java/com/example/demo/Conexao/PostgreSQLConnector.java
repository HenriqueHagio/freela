package com.example.demo.Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQLConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/demo";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "senha123";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void criarTabelaUsuario() {
        try (Connection connection = conectar();
             Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "role VARCHAR(50) NOT NULL," +
                    "status BOOLEAN NOT NULL" +
                    ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
