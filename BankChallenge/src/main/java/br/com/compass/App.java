package br.com.compass;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import br.com.compass.model.Usuario;
import br.com.compass.service.BancoService;

public class App {
	
	
	 private static final Map<String, Usuario> usuarios = new HashMap<>();
	 private static final Map<Long, Double> saldos = new HashMap<>(); 
	 private static final BancoService bancoService = BancoService.getInstance();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        mainMenu(scanner);

        scanner.close();
        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner) {
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
                case 2:
                	abrirConta(scanner);
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

        // Verifica usuário no "banco de dados"
        Usuario usuario = usuarios.get(cpfCnpj);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("Welcome, " + usuario.getNome() + "!");
            return true;
        }
        return false;
    }

    public static void abrirConta(Scanner scanner) {
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
            usuarios.put(cpfCnpj, new Usuario(nome, cpfCnpj, null, null, null, senha));
            System.out.println("Account successfully created for " + nome + "!");
        }
    }


    public static void bankMenu(Scanner scanner) {
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

            switch (option) {
                case 1:
                    deposit(scanner);
                    break;
                case 2:
                    withdraw(scanner);
                    break;
                case 3:
                    checkBalance(scanner);
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
        }
    }

    public static void deposit(Scanner scanner) {
        System.out.println("========= Deposit =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();

        System.out.print("Enter the amount to deposit: ");
        double amount = scanner.nextDouble();

        try {
            bancoService.depositar(accountId, amount);
            System.out.printf("Deposit successful! New balance: %.2f%n", bancoService.consultarSaldo(accountId));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void withdraw(Scanner scanner) {
        System.out.println("========= Withdraw =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();

        System.out.print("Enter the amount to withdraw: ");
        double amount = scanner.nextDouble();

        try {
            bancoService.sacar(accountId, amount);
            System.out.printf("Withdrawal successful! New balance: %.2f%n", bancoService.consultarSaldo(accountId));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkBalance(Scanner scanner) {
        System.out.println("========= Check Balance =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();

        try {
            double saldo = bancoService.consultarSaldo(accountId);
            System.out.printf("Your current balance is: %.2f%n", saldo);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void transfer(Scanner scanner) {
        System.out.println("========= Transfer =========");
        System.out.print("Enter your account ID: ");
        Long sourceAccountId = scanner.nextLong();

        System.out.print("Enter the destination account ID: ");
        Long destinationAccountId = scanner.nextLong();

        System.out.print("Enter the amount to transfer: ");
        double amount = scanner.nextDouble();

        try {
            bancoService.transferir(sourceAccountId, destinationAccountId, amount);
            System.out.printf("Transfer successful! New balance of account %d: %.2f%n",
                    sourceAccountId, bancoService.consultarSaldo(sourceAccountId));
            System.out.printf("New balance of account %d: %.2f%n",
                    destinationAccountId, bancoService.consultarSaldo(destinationAccountId));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void bankStatement(Scanner scanner) {
        System.out.println("========= Bank Statement =========");
        System.out.print("Enter your account ID: ");
        Long accountId = scanner.nextLong();
        bancoService.consultarSaldo(accountId);
        
        if (!saldos.containsKey(accountId)) {
            System.out.println("Account not found!");
            return;
        }

        System.out.printf("Your account balance is: %.2f%n", saldos.get(accountId));
        System.out.println("Additional transaction details can be added later.");
    }

/*
    private static void seedUsers() {
        // Criando usuários
        Usuario usuario1 = new Usuario("Alice", "12345678901", null, 999999999L, 1L, "senha123");
        Usuario usuario2 = new Usuario("Company X", null, "12345678000199", 888888888L, 2L, "password456");
        usuarios.put("12345678901", usuario1);
        usuarios.put("12345678000199", usuario2);

        // Criando saldos iniciais para as contas
        saldos.put(1L, 5000.00); // Saldo da conta 1
        saldos.put(2L, 10000.00); // Saldo da conta 2
    }*/
}
