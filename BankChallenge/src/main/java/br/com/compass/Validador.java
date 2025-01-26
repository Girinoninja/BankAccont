package br.com.compass;

public class Validador {

    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", ""); // Remove caracteres não numéricos

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false; // Tamanho inválido ou sequência repetida
        }
        try {
            int soma1 = 0, soma2 = 0;
            for (int i = 0; i < 9; i++) {
                int num = Character.getNumericValue(cpf.charAt(i));
                soma1 += num * (10 - i);
                soma2 += num * (11 - i);
            }

            int digito1 = (soma1 * 10) % 11;
            digito1 = digito1 == 10 ? 0 : digito1;
            soma2 += digito1 * 2;

            int digito2 = (soma2 * 10) % 11;
            digito2 = digito2 == 10 ? 0 : digito2;

            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                   digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", ""); // Remove caracteres não numéricos

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false; // Tamanho inválido ou sequência repetida
        }

        try {
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma1 = 0, soma2 = 0;

            for (int i = 0; i < 12; i++) {
                int num = Character.getNumericValue(cnpj.charAt(i));
                soma1 += num * pesos1[i];
                soma2 += num * pesos2[i];
            }

            int digito1 = soma1 % 11 < 2 ? 0 : 11 - (soma1 % 11);
            soma2 += digito1 * pesos2[12];

            int digito2 = soma2 % 11 < 2 ? 0 : 11 - (soma2 % 11);

            return digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
                   digito2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }
}
