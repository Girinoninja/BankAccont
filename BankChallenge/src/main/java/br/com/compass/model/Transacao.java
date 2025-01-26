package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Transacao {
	public Transacao(int int1, int int2, String string, BigDecimal bigDecimal, LocalDateTime localDateTime) {
		// TODO Auto-generated constructor stub
	}
	private int idConta;
	private Long Tipo_transacao; //1 - SAQUE, 2 - DEPOSITO, 3 - TRANSFERENCIA 
	private BigDecimal valor;
	private Long conta_transacao;

}
