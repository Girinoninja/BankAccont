package br.com.compass.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import br.com.compass.dao.ContaDAO;

public class BancoService {
    private static final BancoService instance = new BancoService();
    private final ContaDAO contaDAO = new ContaDAO();

    private BancoService() {}

    public static BancoService getInstance() {
        return instance;
    }

    public void deposit(Long accountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        BigDecimal currentBalance = contaDAO.buscarSaldo(accountId.intValue());
        BigDecimal newBalance = currentBalance.add(amount);

        contaDAO.atualizarSaldo(accountId.intValue(), newBalance);

        System.out.printf("Deposit successful! New balance: %.2f%n", newBalance);
    }

    public void withdraw(Long accountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero.");
        }

        BigDecimal currentBalance = contaDAO.buscarSaldo(accountId.intValue());
        if (amount.compareTo(currentBalance) > 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        BigDecimal newBalance = currentBalance.subtract(amount);
        contaDAO.atualizarSaldo(accountId.intValue(), newBalance);

        System.out.printf("Withdrawal successful! New balance: %.2f%n", newBalance);
    }

    public void transfer(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }

        BigDecimal sourceBalance = contaDAO.buscarSaldo(sourceAccountId.intValue());
        if (amount.compareTo(sourceBalance) > 0) {
            throw new IllegalArgumentException("Insufficient funds in source account.");
        }

        BigDecimal destinationBalance = contaDAO.buscarSaldo(destinationAccountId.intValue());

        contaDAO.atualizarSaldo(sourceAccountId.intValue(), sourceBalance.subtract(amount));
        contaDAO.atualizarSaldo(destinationAccountId.intValue(), destinationBalance.add(amount));

        System.out.println("Transfer completed successfully!");
    }
}
