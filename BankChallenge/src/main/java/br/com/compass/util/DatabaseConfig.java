package br.com.compass.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
    public static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/meubanco";
    public static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
    public static final String PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        Connection conexao = null;
        try {
            conexao = getConnection();
            if (conexao != null) {
                LOGGER.info("Conexão bem-sucedida!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados", e);
        } finally {
            if (conexao != null) {
                try {
                    conexao.close();
                    LOGGER.info("Conexão fechada.");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao fechar conexão", e);
                }
            }
        }
    }
}
