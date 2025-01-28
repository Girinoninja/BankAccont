package br.com.compass.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter


public class Conta {
    private int ativa; // 1 - ativa, 2 - inativa
    private BigDecimal saldo;
    private Long tipoConta; // 1 - corrente, 2 - salario, 3 - poupanca
    private Long id;
    private Long idUsuario; // Relacionamento com o usuário
    
    public Conta(Long tipoConta, Long idUsuario) {
        this.ativa = 1; // Por padrão a conta é ativa
        this.saldo = BigDecimal.ZERO; // Inicializa com zero
        this.tipoConta = tipoConta;
        this.idUsuario = idUsuario;
    }

    // Método para verificar se a conta está ativa
    public boolean isAtiva() {
        return ativa == 1;
    }

    // Método para ativar a conta
    public void ativarConta() {
        this.ativa = 1;
    }

    // Método para inativar a conta
    public void inativarConta() {
        this.ativa = 2;
    }

    // Método para adicionar saldo
    public void adicionarSaldo(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) > 0) {
            this.saldo = this.saldo.add(valor);
        } else {
            throw new IllegalArgumentException("O valor para adicionar deve ser maior que zero.");
        }
    }

    // Método para remover saldo
    public void removerSaldo(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) > 0 && this.saldo.compareTo(valor) >= 0) {
            this.saldo = this.saldo.subtract(valor);
        } else {
            throw new IllegalArgumentException("Saldo insuficiente ou valor inválido.");
        }
    }

    // Método para verificar o tipo de conta
    public String getTipoConta() {
        switch (tipoConta.intValue()) {
            case 1:
                return "Corrente";
            case 2:
                return "Salário";
            case 3:
                return "Poupança";
            default:
                return "Desconhecido";
        }
    }

	public Conta(int int1, int int2, String string, BigDecimal bigDecimal) {
		// TODO Auto-generated constructor stub
	}
}
