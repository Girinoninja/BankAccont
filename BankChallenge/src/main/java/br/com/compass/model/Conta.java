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
	private Long tipo_conta; //1 - corrente, 2 - salario, 3 - poupanca
	private Long id;
	public int getIdUsuario() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Conta(int int1, int int2, String string, BigDecimal bigDecimal) {
		// TODO Auto-generated constructor stub
	}

}
