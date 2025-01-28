package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class Transacao {
    private Long idContaOrigem;
    private Long idContaDestino;
    private TipoTransacao tipoTransacao;
    private BigDecimal valor;
    private LocalDateTime dataHora;

    public enum TipoTransacao {
        SAQUE, DEPOSITO, TRANSFERENCIA
    }

    @Override
    public String toString() {
        return "Transacao{" +
               "idContaOrigem=" + idContaOrigem +
               ", idContaDestino=" + idContaDestino +
               ", tipoTransacao=" + tipoTransacao +
               ", valor=" + valor +
               ", dataHora=" + dataHora +
               '}';
    }
    public Transacao(Long idContaOrigem, Long idContaDestino, TipoTransacao tipoTransacao, BigDecimal valor, LocalDateTime dataHora) {
        if (idContaOrigem == null || tipoTransacao == null || valor == null || dataHora == null) {
            throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos");
        }
        this.idContaOrigem = idContaOrigem;
        this.idContaDestino = idContaDestino;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    private static final List<Transacao> transacoes = new ArrayList<>();

    public static void criarTransacao(Transacao transacao) {
        transacoes.add(transacao);
        System.out.println("Transaction logged: " + transacao);
    }

    public static List<Transacao> listarTransacoesPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Transacao> transacoesPeriodo = new ArrayList<>();
        for (Transacao transacao : transacoes) {
            if (!transacao.getDataHora().isBefore(inicio) && !transacao.getDataHora().isAfter(fim)) {
                transacoesPeriodo.add(transacao);
            }
        }
        return transacoesPeriodo;
    }
	public Transacao(int int1, int int2, int int3, String string, BigDecimal bigDecimal, LocalDateTime localDateTime) {
		// TODO Auto-generated constructor stub
	}

}

