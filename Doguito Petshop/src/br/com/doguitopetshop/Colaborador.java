
package br.com.doguitopetshop;

public class Colaborador extends Pessoa {
    private String cargo;
    private String matricula;
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public String getCargo() {
        return cargo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Colaborador(String nome, String cpf, String telefone, String email,
                       String cep, String numero, String login, String senha,
                       String cargo, String matricula) {
        super(nome, cpf, telefone, email, cep, numero, login, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
        this.numero = numero;
        this.login = login;
        this.senha = senha;
        this.cargo = cargo;
        this.matricula = matricula;
    }

    public Colaborador(String login, String senha, String cpf){
        this.login = login;
        this.senha = senha;
        this.cpf = cpf;
    }
}


