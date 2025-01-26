package br.com.compass.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Setter

public class Usuario {
	private String nome;
	private String cpf;
	private String cnpj;
	private String telefone;
	private Long id;
	private String senha;
	public String getDataNascimento() {

		return null;
	}
	public Usuario(int i, String string, LocalDate localDate, String string2, String string3, String string4) {

	}
	public Usuario(String string, String string2, Object localDate, long l, long m, String string4) {
		// TODO Auto-generated constructor stub
	}
	

}