package br.com.compass.service;

import java.util.HashMap;
import java.util.Map;

public class BancoService {
    private static BancoService instance = null;
    private final Map<Long, Double> saldos = new HashMap<>();

    // Construtor privado para Singleton
    private BancoService() {}

    // Método para obter a instância única
    public static BancoService getInstance() {
        if (instance == null) {
            instance = new BancoService();
        }
        return instance;
    }

    public void depositar(Long accountId, double amount) {
        if (amount <= 0) {
            System.out.println("Amount should be positive.");
            return;
        }

        saldos.put(accountId, saldos.getOrDefault(accountId, 0.0) + amount);
        System.out.printf("Deposit successful! New balance: %.2f%n", saldos.get(accountId));
    }

    public void sacar(Long accountId, double amount) {
        if (amount <= 0) {
            System.out.println("Amount should be positive.");
            return;
        }

        double currentBalance = saldos.getOrDefault(accountId, 0.0);
        if (amount > currentBalance) {
            System.out.println("Insufficient funds.");
            return;
        }

        saldos.put(accountId, currentBalance - amount);
        System.out.printf("Withdrawal successful! New balance: %.2f%n", saldos.get(accountId));
    }

    public double consultarSaldo(Long accountId) {
        System.out.printf("Current balance: %.2f%n", saldos.getOrDefault(accountId, 0.0));
		return accountId;
    }

    public void transferir(Long sourceAccountId, Long destinationAccountId, double amount) {
        if (amount <= 0) {
            System.out.println("Amount should be positive.");
            return;
        }

        double sourceBalance = saldos.getOrDefault(sourceAccountId, 0.0);
        if (amount > sourceBalance) {
            System.out.println("Insufficient funds.");
            return;
        }

        saldos.put(sourceAccountId, sourceBalance - amount);
        saldos.put(destinationAccountId, saldos.getOrDefault(destinationAccountId, 0.0) + amount);
        System.out.printf("Transfer successful! Source account balance: %.2f, Destination account balance: %.2f%n", 
                          saldos.get(sourceAccountId), saldos.get(destinationAccountId));
    }
}
