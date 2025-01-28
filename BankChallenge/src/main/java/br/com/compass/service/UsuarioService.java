package br.com.compass.service;


import br.com.compass.dao.UsuarioDAO;
import br.com.compass.model.Usuario;
import br.com.compass.util.Validador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsuarioService {
	private static final String URL = "jdbc:postgresql://127.0.0.1:5432/meubanco";
	private static final String USER = "postgres";
	private static final String PASSWORD = "root";
	private Map<String, Usuario> usuarios;
	private final UsuarioDAO usuarioDAO = new UsuarioDAO();

	
 

	public UsuarioService(Map<String, Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public boolean autenticar(String cpfCnpj, String senha) {
		if (!Validador.validarCPF(cpfCnpj) && !Validador.validarCNPJ(cpfCnpj)) {
			System.out.println("Invalid CPF or CNPJ format.");
			return false;
		}

		Usuario usuario = usuarios.get(cpfCnpj);
		return usuario != null && usuario.getSenha().equals(senha);
	}
	

	    public Usuario buscarUsuario(int idUsuario) {
	        try {
	            return usuarioDAO.buscarUsuario(idUsuario);
	        } catch (Exception e) {
	            System.err.println("Erro ao buscar usuário: " + e.getMessage());
	            return null;
	        }
	        }


	public void criarConta(String cpfCnpj, String nome, String senha) {
		if (usuarios.containsKey(cpfCnpj)) {
			System.out.println("User with this CPF or CNPJ already exists.");
			return;
		}

		Usuario usuario = new Usuario(nome, cpfCnpj, null, null, null, senha, null);
		usuarios.put(cpfCnpj, usuario);
		System.out.println("Account created successfully for " + nome);
	}

	public void criarUsuario(Usuario usuario) throws SQLException {
		String sql = "INSERT INTO usuario (nome, data_nascimento, cpf, telefone, senha) VALUES (?, ?, ?, ?, ?)";
		try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, usuario.getNome());
			stmt.setDate(2, Date.valueOf(usuario.getDataNascimento()));
			stmt.setString(3, usuario.getCpf());
			stmt.setString(4, usuario.getTelefone());
			stmt.setString(5, usuario.getSenha()); 
			stmt.executeUpdate();
			 usuarioDAO.criarUsuario(usuario);
	            System.out.println("Usuário criado com sucesso!");
		}  catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());

        }
	}

	public List<Usuario> listarUsuarios() throws SQLException {
		String sql = "SELECT * FROM Usuario";
		List<Usuario> usuarios = new ArrayList<>();
		try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conexao.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Usuario usuario = new Usuario(rs.getInt("id_usuario"), rs.getString("nome"),
						rs.getDate("data_nascimento").toLocalDate(), rs.getString("cpf"), rs.getString("telefone"),
						rs.getString("senha"));
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
