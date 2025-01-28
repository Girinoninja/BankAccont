package br.com.compass.service;

import br.com.compass.model.Transacao;
import br.com.compass.util.DatabaseConfig;


import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoService {


	   
    // Registrar uma transação no banco de dados
    public static void criarTransacao(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO Transacao (id_conta_origem, id_conta_destino, tipo_transacao, valor, data_hora) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, transacao.getIdContaOrigem());
            stmt.setLong(2, transacao.getIdContaDestino());
            stmt.setString(3, transacao.getTipoTransacao().name());  // Armazenando como nome do enum
            stmt.setBigDecimal(4, transacao.getValor());
            stmt.setTimestamp(5, Timestamp.valueOf(transacao.getDataHora()));
            stmt.executeUpdate();
        }
    }

    // Listar transações por conta
    public List<Transacao> listarTransacoesPorConta(int idConta) throws SQLException {
        String sql = "SELECT * FROM Transacao WHERE id_conta_origem = ? OR id_conta_destino = ?";
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection conexao = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            stmt.setInt(2, idConta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacao transacao = new Transacao(
                            rs.getInt("id_transacao"),
                            rs.getInt("id_conta_origem"),
                            rs.getInt("id_conta_destino"),
                            rs.getString("tipo_transacao"),
                            rs.getBigDecimal("valor"),
                            rs.getTimestamp("data_hora").toLocalDateTime()
                    );
                    transacoes.add(transacao);
                }
            }
        }
        return transacoes;
    }

    // Listar transações por período
    public static List<Transacao> listarTransacoesPorPeriodo(int accountId, LocalDateTime inicio, LocalDateTime fim)  throws SQLException {
        String sql = "SELECT * FROM Transacao WHERE (id_conta_origem = ? OR id_conta_destino = ?) " +
                     "AND data_hora BETWEEN ? AND ?";
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection conexao = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setTimestamp(3, Timestamp.valueOf(inicio));
            stmt.setTimestamp(4, Timestamp.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacao transacao = new Transacao(
                            rs.getInt("id_transacao"),
                            rs.getInt("id_conta_origem"),
                            rs.getInt("id_conta_destino"),
                            rs.getString("tipo_transacao"),
                            rs.getBigDecimal("valor"),
                            rs.getTimestamp("data_hora").toLocalDateTime()
                    );
                    transacoes.add(transacao);
                }
            }
        }
        return transacoes;
    }
    public void transferir(int contaOrigem, int contaDestino, BigDecimal valor) throws SQLException {
        String sqlDebitar = "UPDATE Conta SET saldo = saldo - ? WHERE id_conta = ?";
        String sqlCreditar = "UPDATE Conta SET saldo = saldo + ? WHERE id_conta = ?";
        String sqlVerificarSaldo = "SELECT saldo FROM Conta WHERE id_conta = ?";
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }
        if (contaOrigem == contaDestino) {
            throw new IllegalArgumentException("As contas de origem e destino devem ser diferentes.");
        }

        
        try (Connection conexao = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD)) {
            conexao.setAutoCommit(false);

            try (PreparedStatement verificarSaldo = conexao.prepareStatement(sqlVerificarSaldo)) {
                verificarSaldo.setInt(1, contaOrigem);
                try (ResultSet rs = verificarSaldo.executeQuery()) {
                    if (rs.next()) {
                        BigDecimal saldo = rs.getBigDecimal("saldo");
                        if (saldo.compareTo(valor) < 0) {
                            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
                        }
                    } else {
                        throw new SQLException("Conta de origem não encontrada.");
                    }
                }
            }
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
            }
            if (contaOrigem == contaDestino) {
                throw new IllegalArgumentException("As contas de origem e destino devem ser diferentes.");
            }

            try (PreparedStatement debitar = conexao.prepareStatement(sqlDebitar);
                 PreparedStatement creditar = conexao.prepareStatement(sqlCreditar)) {

                debitar.setBigDecimal(1, valor);
                debitar.setInt(2, contaOrigem);
                debitar.executeUpdate();

                creditar.setBigDecimal(1, valor);
                creditar.setInt(2, contaDestino);
                creditar.executeUpdate();

                conexao.commit();
                System.out.println("Transferência concluída!");
 
            } catch (SQLException e) {
                conexao.rollback();

                throw e;
            }
        }
    }
    private boolean verificarSaldo(Connection conexao, int contaOrigem, BigDecimal valor) throws SQLException {
        String sql = "SELECT saldo FROM Conta WHERE id_conta = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, contaOrigem);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal saldo = rs.getBigDecimal("saldo");
                    return saldo.compareTo(valor) >= 0;
                } else {
                    throw new SQLException("Conta de origem não encontrada.");
                }
            }
        }
    }

    



}
