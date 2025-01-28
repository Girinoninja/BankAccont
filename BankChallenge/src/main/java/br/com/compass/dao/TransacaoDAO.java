package br.com.compass.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

import br.com.compass.model.Transacao;
import br.com.compass.util.DatabaseConfig;

public class TransacaoDAO {

	public void criarTransacao(Long idContaOrigem, Long idContaDestino, BigDecimal valor, LocalDateTime dataHora, Transacao.TipoTransacao tipo) throws SQLException {
	    String sql = "INSERT INTO Transacao (id_conta_origem, id_conta_destino, valor, data_hora, tipo_transacao) VALUES (?, ?, ?, ?, ?)";
	    
	    try (Connection conexao = DatabaseConfig.getConnection();
	         PreparedStatement stmt = conexao.prepareStatement(sql)) {

	        // Preenchendo os parâmetros da consulta
	        stmt.setLong(1, idContaOrigem);
	        stmt.setLong(2, idContaDestino);
	        stmt.setBigDecimal(3, valor);
	        stmt.setTimestamp(4, Timestamp.valueOf(dataHora));
	        stmt.setString(5, tipo.name());

	        // Executando a inserção
	        stmt.executeUpdate();
	        System.out.println("Transação registrada com sucesso!");
	    }
	}


    public List<Transacao> listarTransacoesPorConta(Long idConta) throws SQLException {
        String sql = "SELECT * FROM Transacao WHERE id_conta_origem = ? OR id_conta_destino = ?";
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection conexao = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, idConta);
            stmt.setLong(2, idConta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacao transacao = new Transacao(
                            rs.getLong("id_conta_origem"),
                            rs.getLong("id_conta_destino"),
                            Transacao.TipoTransacao.valueOf(rs.getString("tipo_transacao")),
                            rs.getBigDecimal("valor"),
                            rs.getTimestamp("data_hora").toLocalDateTime()
                    );
                    transacoes.add(transacao);
                }
            }
        }
        return transacoes;
    }
    
}
