package br.com.compass.service;

import br.com.compass.model.Conta;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaService {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/meubanco";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public void criarConta(Conta conta) throws SQLException {
        String sql = "INSERT INTO Conta (id_usuario, tipo_conta, saldo) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, conta.getIdUsuario());
            stmt.setLong(2, conta.getTipo_conta());
            stmt.setBigDecimal(3, conta.getSaldo());
            stmt.executeUpdate();
        }
    }

    public List<Conta> listarContas() throws SQLException {
        String sql = "SELECT * FROM Conta";
        List<Conta> contas = new ArrayList<>();
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Conta conta = new Conta(
                        rs.getInt("id_conta"),
                        rs.getInt("id_usuario"),
                        rs.getString("tipo_conta"),
                        rs.getBigDecimal("saldo")
                );
                contas.add(conta);
            }
        }
        return contas;
    }

    public void atualizarSaldo(int idConta, double novoSaldo) throws SQLException {
        String sql = "UPDATE Conta SET saldo = ? WHERE id_conta = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(novoSaldo));
            stmt.setInt(2, idConta);
            stmt.executeUpdate();
        }
    }

    public void deletarConta(int idConta) throws SQLException {
        String sql = "DELETE FROM Conta WHERE id_conta = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            stmt.executeUpdate();
        }
    }
}
