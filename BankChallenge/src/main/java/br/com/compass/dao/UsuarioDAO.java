package br.com.compass.dao;

import br.com.compass.model.Usuario;
import java.sql.*;

public class UsuarioDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/seu_banco";
    private static final String USER = "seu_usuario";
    private static final String PASSWORD = "sua_senha";

    public void criarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (id_usuario, nome, cpf_cnpj) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getCpfCnpj());

            stmt.executeUpdate();
        }
    }

    public Usuario buscarUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getString("cpf_cnpj")
                    );
                } else {
                    throw new IllegalArgumentException("Usuário não encontrado.");
                }
            }
        }
    }
}
