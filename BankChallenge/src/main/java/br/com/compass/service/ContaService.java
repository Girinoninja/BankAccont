package br.com.compass.service;

import br.com.compass.dao.ContaDAO;
import br.com.compass.model.Conta;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ContaService {
    private final ContaDAO contaDAO = new ContaDAO();

    public Conta abrirConta(Long idUsuario, Long tipoConta) throws SQLException {
        if (tipoConta < 1 || tipoConta > 3) {
            throw new IllegalArgumentException("Tipo de conta inválido. Use 1 (Corrente), 2 (Salário), ou 3 (Poupança).");
        }

        Conta novaConta = new Conta(tipoConta, idUsuario);
        contaDAO.criarConta(novaConta);
        return novaConta;
    }

    public List<Conta> listarContas() throws SQLException {
        return contaDAO.listarContas();
    }

    public Conta buscarContaPorId(long idConta) throws SQLException {
        Conta conta = contaDAO.buscarContaPorId(idConta);
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }
        return conta;
    }

    public void atualizarSaldo(long idConta, BigDecimal novoSaldo) throws SQLException {
        contaDAO.atualizarSaldo(idConta, novoSaldo);
    }

    public void deletarConta(long idConta) throws SQLException {
        contaDAO.deletarConta(idConta);
    }
}
