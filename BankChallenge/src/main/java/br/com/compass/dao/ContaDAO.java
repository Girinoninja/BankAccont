package br.com.compass.dao;

import br.com.compass.model.Conta;
import br.com.compass.util.DatabaseConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    public void criarConta(Conta conta) throws SQLException {
        String sql = "INSERT INTO Conta (id_usuario, tipo_conta, saldo, ativa) VALUES (?, ?, ?, ?)";
        try (Connection conexao = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, conta.getIdUsuario());
            stmt.setString(2, conta.getTipoConta());
            stmt.setBigDecimal(3, conta.getSaldo());
            stmt.setInt(4, conta.isAtiva() ? 1 : 0);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        conta.setId(rs.getLong(1)); // Recupera o ID gerado.
                    }
                }
            }
        }
    }

    public List<Conta> listarContas() throws SQLException {
        String sql = "SELECT * FROM Conta";
        List<Conta> contas = new ArrayList<>();
        try (Connection conexao = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Conta conta = new Conta(
                        rs.getInt("ativa"),
                        rs.getBigDecimal("saldo"),
                        rs.getLong("tipo_conta"),
                        rs.getLong("id_conta"),
                        rs.getLong("id_usuario")
                );
                contas.add(conta);
            }
        }
        return contas;
    }

    public Conta buscarContaPorId(long idConta) throws SQLException {
        String sql = "SELECT * FROM Conta WHERE id_conta = ?";
        try (Connection conexao = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Conta(
                            rs.getInt("ativa"),
                            rs.getBigDecimal("saldo"),
                            rs.getLong("tipo_conta"),
                            rs.getLong("id_conta"),
                            rs.getLong("id_usuario")
                    );
                }
            }
        }
        return null;
    }

    public void atualizarSaldo(long idConta, BigDecimal novoSaldo) throws SQLException {
        String sql = "UPDATE Conta SET saldo = ? WHERE id_conta = ?";
        try (Connection conexao = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setBigDecimal(1, novoSaldo);
            stmt.setLong(2, idConta);
            stmt.executeUpdate();
        }
    }

    public void deletarConta(long idConta) throws SQLException {
        String sql = "DELETE FROM Conta WHERE id_conta = ?";
        try (Connection conexao = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, idConta);
            stmt.executeUpdate();
        }
    }

    public BigDecimal buscarSaldo(int idConta) throws SQLException {
        String sql = "SELECT saldo FROM Conta WHERE id_conta = ?";
        try (Connection conexao = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("saldo");
                } else {
                    throw new IllegalArgumentException("Conta não encontrada para o ID fornecido: " + idConta);
                }
            }
        }
    }

    public void verificarSaldoEExtrato(int idConta) throws SQLException {
        String sqlSaldo = "SELECT saldo FROM Conta WHERE id_conta = ?";
        String sqlExtrato = "SELECT * FROM Transacao WHERE id_conta_origem = ? OR id_conta_destino = ? ORDER BY data_hora DESC";
        
        try (Connection conexao = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement stmtSaldo = conexao.prepareStatement(sqlSaldo);
             PreparedStatement stmtExtrato = conexao.prepareStatement(sqlExtrato)) {
            
            // Consultar saldo
            stmtSaldo.setInt(1, idConta);
            try (ResultSet rsSaldo = stmtSaldo.executeQuery()) {
                if (rsSaldo.next()) {
                    BigDecimal saldo = rsSaldo.getBigDecimal("saldo");
                    System.out.println("Seu saldo atual é: " + saldo);
                } else {
                    System.out.println("Conta não encontrada.");
                    return;
                }
            }
            
            // Consultar extrato
            stmtExtrato.setInt(1, idConta);
            stmtExtrato.setInt(2, idConta);
            try (ResultSet rsExtrato = stmtExtrato.executeQuery()) {
                System.out.println("Extrato recente:");
                while (rsExtrato.next()) {
                    System.out.println("Transação ID: " + rsExtrato.getInt("id_transacao"));
                    System.out.println("Tipo: " + rsExtrato.getString("tipo_transacao"));
                    System.out.println("Valor: " + rsExtrato.getBigDecimal("valor"));
                    System.out.println("Data e Hora: " + rsExtrato.getTimestamp("data_hora"));
                    System.out.println("----------------------------");
                }
            }
        }
    }
}


