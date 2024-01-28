package com.example.demo.Conexao;

import com.example.demo.comeco.Usuario;

import java.sql.*;

public class PostgreSQLConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/demo";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "senha123";

//    Usuario usuario = new Usuario();

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

    public static void criarUsuario(Usuario usuario) {
        try (Connection connection = conectar();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO usuario(username, password, role) VALUES (?, ?, ?)")) {
            statement.setString(1, usuario.getUsername());
            statement.setString(2, usuario.getPassword());
            statement.setString(3, usuario.getRole());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public static void criarTabelaUsuario() {
        try (Connection connection = conectar();
             Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "role VARCHAR(50) NOT NULL" +
                    ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
