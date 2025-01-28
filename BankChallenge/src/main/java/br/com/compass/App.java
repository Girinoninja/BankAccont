package br.com.compass;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.com.compass.model.Conta;
import br.com.compass.model.Transacao;
import br.com.compass.model.Usuario;
import br.com.compass.service.BancoService;
import br.com.compass.service.ContaService;
import br.com.compass.service.TransacaoService;
import br.com.compass.util.DatabaseConfig;
import br.com.compass.util.Validador;

public class App {
    private static final Map<String, Usuario> usuarios = new HashMap<>();
    private static final Map<Long, BigDecimal> saldos = new HashMap<>();
 

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        mainMenu(scanner);
        scanner.close();
        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner) throws SQLException {
        boolean running = true;
        while (running) {
            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    if (login(scanner)) {
                        System.out.println("Login successful!");
                        bankMenu(scanner);
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                    break;
                case 2:
                    createAccount(scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    	public static boolean login(Scanner scanner) {
    	    System.out.println("========= Login =========");
    	    System.out.print("Enter CPF or CNPJ: ");
    	    String cpfCnpj = scanner.nextLine();
    	    if (!Validador.validarCPF(cpfCnpj) && !Validador.validarCNPJ(cpfCnpj)) {
    	        System.out.println("Invalid CPF or CNPJ format.");
    	        return false;
    	    }
    	    System.out.print("Enter Password: ");
    	    String senha = scanner.nextLine();
    	    Usuario usuario = usuarios.get(cpfCnpj);
    	    if (usuario != null && usuario.getSenha().equals(senha)) {
    	        System.out.println("Welcome, " + usuario.getNome() + "!");
    	        System.out.println("Your account ID is: " + usuario.getAccountId()); // Exibe o ID da conta
    	        return true;
    	    }
    	    return false;
    	}

    public static void createAccount(Scanner scanner) {
        System.out.println("========= Account Opening =========");
        System.out.print("Enter CPF or CNPJ: ");
        String cpfCnpj = scanner.nextLine();
        if (!Validador.validarCPF(cpfCnpj) && !Validador.validarCNPJ(cpfCnpj)) {
            System.out.println("Invalid CPF or CNPJ format.");
            return;
        }
        System.out.print("Enter Name: ");
        String nome = scanner.nextLine();
        System.out.print("Enter Password: ");
        String senha = scanner.nextLine();
        if (usuarios.containsKey(cpfCnpj)) {
            System.out.println("User with this CPF or CNPJ already exists.");
        } else {
            long accountId = saldos.size() + 1; // Generates a new account ID
            usuarios.put(cpfCnpj, new Usuario(nome, cpfCnpj, null, null, accountId, senha, null));
            saldos.put(accountId, BigDecimal.ZERO); // Initialize balance to zero
            System.out.println("Account successfully created for " + nome + "!");
        }
    }



    public static void bankMenu(Scanner scanner) throws SQLException {
        boolean running = true;

        while (running) {
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            try {
				switch (option) {
				    case 1:
				        deposit(scanner);
				        break;
				    case 2:
				        withdraw(scanner);
				        break;
				    case 3:
				        checkBalance(scanner);
				        System.out.print("Enter your account ID: ");
				        int accountId = scanner.nextInt();
				        scanner.nextLine(); // Consumir quebra de linha restante

				        System.out.print("Start date (YYYY-MM-DDTHH:MM): ");
				        LocalDateTime inicio = LocalDateTime.parse(scanner.nextLine());

				        System.out.print("End date (YYYY-MM-DDTHH:MM): ");
				        LocalDateTime fim = LocalDateTime.parse(scanner.nextLine());

				        List<Transacao> transacoesPeriodo = TransacaoService.listarTransacoesPorPeriodo(accountId, inicio, fim);
				        System.out.println("Transactions in the selected period: " + transacoesPeriodo);
				        
				        if (transacoesPeriodo.isEmpty()) {
				            System.out.println("No transactions found in the selected period.");
				        } else {
				            System.out.println("Transactions in the selected period: " + transacoesPeriodo);
				        }
				        break;
				    case 4:
				        transfer(scanner);

				        break;
				    case 5:
				        bankStatement(scanner);
				        break;
				    case 0:
				        System.out.println("Exiting Bank Menu...");
				        running = false;
				        break;
				    default:
				        System.out.println("Invalid option! Please try again.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

 // No método deposit, use BigDecimal em vez de Double
    public static void deposit(Scanner scanner) throws SQLException {
        System.out.println("========= Deposit =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();
        scanner.nextLine(); // Consumir quebra de linha

        System.out.print("Enter the amount to deposit: ");
        String amountInput = scanner.nextLine().replace(",", "."); // Normaliza entrada

        try {
            BigDecimal amount = new BigDecimal(amountInput);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("The deposit amount must be positive!");
                return;
            }

            if (saldos.containsKey(accountId)) {
                BigDecimal currentBalance = saldos.get(accountId);
                saldos.put(accountId, currentBalance.add(amount));
                System.out.printf("Deposit successful! New balance: %.2f%n", saldos.get(accountId));

                // Log transaction (create Transacao object)
                Transacao transacao = new Transacao(accountId, null, Transacao.TipoTransacao.DEPOSITO, amount, LocalDateTime.now());
                TransacaoService.criarTransacao(transacao);
            } else {
                System.out.println("Account not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please try again.");
        }
    }

    public static void withdraw(Scanner scanner) {
        System.out.println("========= Withdraw =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Enter the amount to withdraw: ");
        String amountInput = scanner.nextLine().replace(",", "."); // Normaliza entrada

        try {
            BigDecimal amount = new BigDecimal(amountInput);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("The withdrawal amount must be positive!");
                return;
            }

            if (saldos.containsKey(accountId)) {
                BigDecimal currentBalance = saldos.get(accountId);

                if (currentBalance.compareTo(amount) >= 0) {
                    saldos.put(accountId, currentBalance.subtract(amount));
                    System.out.printf("Withdrawal successful! New balance: %.2f%n", saldos.get(accountId));
                } else {
                    System.out.println("Insufficient funds!");
                }
            } else {
                System.out.println("Account not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please try again.");
        }
    }
    public static void checkBalance(Scanner scanner) throws SQLException {
        System.out.println("========= Check Balance =========");
        System.out.print("Enter your account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha
        verificarSaldoEExtrato(accountId);
    }

    public static void verificarSaldoEExtrato(int idConta) throws SQLException {
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

    public static void transfer(Scanner scanner) {
        System.out.println("========= Transfer =========");
        System.out.print("Enter your account ID: ");
        Long sourceAccountId = scanner.nextLong();

        System.out.print("Enter the destination account ID: ");
        Long destinationAccountId = scanner.nextLong();

        System.out.print("Enter the amount to transfer: ");
        BigDecimal amount = scanner.nextBigDecimal(); // Usando BigDecimal em vez de double

        // Verifica se ambas as contas existem
        if (saldos.containsKey(sourceAccountId) && saldos.containsKey(destinationAccountId)) {
            BigDecimal sourceSaldo = saldos.get(sourceAccountId);
            
            // Verifica se há saldo suficiente para a transferência
            if (sourceSaldo.compareTo(amount) >= 0) { 
                // Subtrai o valor da conta de origem e adiciona à conta de destino
                saldos.put(sourceAccountId, sourceSaldo.subtract(amount));
                saldos.put(destinationAccountId, saldos.get(destinationAccountId).add(amount));
                System.out.println("Transfer successful!");
            } else {
                System.out.println("Insufficient funds!");
            }
        } else {
            System.out.println("One or both accounts not found!");
        }
    }

    public static void bankStatement(Scanner scanner) {
        System.out.println("========= Bank Statement =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();

        if (saldos.containsKey(accountId)) {
            System.out.printf("Your account balance is: %.2f%n", saldos.get(accountId));
        } else {
            System.out.println("Account not found!");
        }
    }



}
