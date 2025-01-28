package br.com.compass.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Usuario {
    private String nome;
    private String cpf;
    private String cnpj;
    private String telefone;
    private Long id;
    private String senha;
    private LocalDate dataNascimento;
    
    

    // Método para validar CPF
    public boolean isCpfValid() {
        return cpf != null && cpf.matches("\\d{11}");
    }

    // Método para validar CNPJ
    public boolean isCnpjValid() {
        return cnpj != null && cnpj.matches("\\d{14}");
    }

    // Método para verificar se o usuário é maior de idade
    public boolean isMaiorDeIdade() {
        if (dataNascimento == null) {
            throw new IllegalStateException("Data de nascimento não informada.");
        }
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }

    // Método para atualizar telefone
    public void atualizarTelefone(String novoTelefone) {
        if (novoTelefone != null && Pattern.matches("\\d{10,11}", novoTelefone)) {
            this.telefone = novoTelefone;
        } else {
            throw new IllegalArgumentException("Telefone inválido.");
        }
    }

    // Método para atualizar senha
    public void atualizarSenha(String novaSenha) {
        if (novaSenha != null && novaSenha.length() >= 6) {
            this.senha = novaSenha;
        } else {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres.");
        }
    }

    // Método para exibir informações formatadas do usuário
    public String exibirInformacoes() {
        return String.format(
                "Nome: %s%nCPF: %s%nCNPJ: %s%nTelefone: %s%nID: %d%nData de Nascimento: %s%n",
                nome,
                cpf != null ? cpf : "Não informado",
                cnpj != null ? cnpj : "Não informado",
                telefone != null ? telefone : "Não informado",
                id,
                dataNascimento != null ? dataNascimento.toString() : "Não informado"
        );
    }

	public Usuario(int int1, String string, String string2) {
		// TODO Auto-generated constructor stub
	}

	public String getAccountId() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIdUsuario() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCpfCnpj() {
		// TODO Auto-generated method stub
		return null;
	}

	public Usuario(int int1, String string, LocalDate localDate, String string2, String string3, String string4) {
		// TODO Auto-generated constructor stub
	}
}
