package br.com.compass.service;

import br.com.compass.model.Transacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoService {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/meubanco";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public void criarTransacao(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO Transacao (id_conta, tipo_transacao, valor) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, transacao.getIdConta());
            stmt.setLong(2, transacao.getTipo_transacao());
            stmt.setBigDecimal(3, transacao.getValor());
            stmt.executeUpdate();
        }
    }

    public List<Transacao> listarTransacoesPorConta(int idConta) throws SQLException {
        String sql = "SELECT * FROM Transacao WHERE id_conta = ?";
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transacao transacao = new Transacao(
                            rs.getInt("id_transacao"),
                            rs.getInt("id_conta"),
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
}
