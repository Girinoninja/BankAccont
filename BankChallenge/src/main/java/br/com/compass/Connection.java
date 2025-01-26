package br.com.compass;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
	 // Dados de conexão
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/meubanco";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        java.sql.Connection conexao = null;
        try {
            // Conectar ao banco
            conexao = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conexao != null) {
                System.out.println("Conexão bem-sucedida!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        } finally {
            // Fechar a conexão
            if (conexao != null) {
                try {
                    conexao.close();
                    System.out.println("Conexão fechada.");
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}