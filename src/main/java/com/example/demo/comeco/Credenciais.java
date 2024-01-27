    package com.example.demo.comeco;

    import com.example.demo.Conexao.PostgreSQLConnector;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;

    public class Credenciais {
        public static String USUARIO;
        public static String SENHA_USUARIO;

//        public static final String ADMIN;


        public static String getUSUARIO() {
            return USUARIO;
        }

        public static void setUSUARIO(String USUARIO) {
            Credenciais.USUARIO = USUARIO;
        }

        public static String getSenhaUsuario() {
            return SENHA_USUARIO;
        }

        public static void setSenhaUsuario(String senhaUsuario) {
            SENHA_USUARIO = senhaUsuario;
        }

        public Credenciais(String usuario, String senhaUsuario) {
            this.USUARIO = usuario;
            this.SENHA_USUARIO = senhaUsuario;
//            this.ADMIN = admin;

        }  public Credenciais() {
        }
        public boolean verificarCredenciais(String username, String password) {
            String query = "SELECT * FROM usuario WHERE username = ? AND password = ?";

            try (Connection connection = PostgreSQLConnector.conectar();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // Definindo os parâmetros da consulta
                statement.setString(1, username);
                statement.setString(2, password);

                // Executando a consulta
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Verificando se há algum resultado retornado
                    return resultSet.next(); // Se houver algum resultado, a autenticação é bem-sucedida
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Em caso de exceção, consideramos a autenticação como falha
            }
        }
    }

