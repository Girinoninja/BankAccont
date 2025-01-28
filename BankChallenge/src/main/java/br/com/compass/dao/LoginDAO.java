package br.com.compass.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.compass.model.LoginModel;

public class LoginDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/seu_banco";
    private static final String USER = "seu_usuario";
    private static final String PASSWORD = "sua_senha";

    public void criarLogin(LoginModel login) throws SQLException {
        String sql = "INSERT INTO login (id_login, username, senha) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, login.getIdLogin());
            stmt.setString(2, login.getUsername());
            stmt.setString(3, login.getSenha());

            stmt.executeUpdate();
        }
    }
}
