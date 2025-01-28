package br.com.compass.util;

public class Validador {

    // Validação de CPF
    public static boolean validarCPF(String cpf) {
        // Remove todos os caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se o CPF tem 11 dígitos e não é uma sequência de números repetidos
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma1 = 0, soma2 = 0;

            // Calcula a soma dos primeiros 9 dígitos multiplicados por pesos
            for (int i = 0; i < 9; i++) {
                int num = Character.getNumericValue(cpf.charAt(i));
                soma1 += num * (10 - i);
                soma2 += num * (11 - i);
            }

            // Calcula os dois dígitos verificadores
            int digito1 = (soma1 * 10) % 11;
            digito1 = digito1 == 10 ? 0 : digito1;
            soma2 += digito1 * 2;

            int digito2 = (soma2 * 10) % 11;
            digito2 = digito2 == 10 ? 0 : digito2;

            // Verifica se os dois dígitos verificadores são válidos
            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                   digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false; // Em caso de erro, retorna falso
        }
    }

    // Validação de CNPJ
    public static boolean validarCNPJ(String cnpj) {
        // Remove todos os caracteres não numéricos
        cnpj = cnpj.replaceAll("\\D", "");

        // Verifica se o CNPJ tem 14 dígitos e não é uma sequência de números repetidos
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Pesos para os cálculos de CNPJ
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma1 = 0, soma2 = 0;

            // Calcula a soma dos primeiros 12 dígitos multiplicados pelos pesos
            for (int i = 0; i < 12; i++) {
                int num = Character.getNumericValue(cnpj.charAt(i));
                soma1 += num * pesos1[i];
                soma2 += num * pesos2[i];
            }

            // Calcula os dois dígitos verificadores
            int digito1 = soma1 % 11 < 2 ? 0 : 11 - (soma1 % 11);
            soma2 += digito1 * pesos2[12];

            int digito2 = soma2 % 11 < 2 ? 0 : 11 - (soma2 % 11);

            // Verifica se os dois dígitos verificadores são válidos
            return digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
                   digito2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (Exception e) {
            return false; // Em caso de erro, retorna falso
        }
    }
}
