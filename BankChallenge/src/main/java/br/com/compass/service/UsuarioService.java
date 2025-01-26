package br.com.compass.service;

import br.com.compass.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/meubanco";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public void criarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (nome, data_nascimento, cpf, telefone, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getSenha()); // Criptografia sugerida
            stmt.executeUpdate();
        }
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        String sql = "SELECT * FROM Usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("senha")
                );
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public void atualizarUsuario(int id, Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET nome = ?, data_nascimento = ?, cpf = ?, telefone = ?, senha = ? WHERE id_usuario = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getSenha());
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public void deletarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
