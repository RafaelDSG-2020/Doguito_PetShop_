
package br.com.doguitopetshop;

public class Dono extends Pessoa {
    private String nomeDoPet;
    private String tipoDoPet;

    public Dono(String cpf, String login, String senha) {
        super(cpf, login, senha);
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
    }
    public Dono() {
    }

    public Dono(String login, String senha){
        super(login, senha);
        this.login = login;
        this.senha = senha;
    }

    public Dono(String nome, String cpf, String telefone, String email, String cep,
                String numero, String login, String senha, String nomeDoPet, String tipoDoPet) {
        super(nome, cpf, telefone, email, cep, numero, login, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
        this.numero = numero;
        this.login = login;
        this.senha = senha;
        this.nomeDoPet = nomeDoPet;
        this.tipoDoPet = tipoDoPet;
    }

    public String getNomeDoPet() {
        return nomeDoPet;
    }

    public void setNomeDoPet(String nomeDoPet) {
        this.nomeDoPet = nomeDoPet;
    }

    public String getTipoDoPet() {
        return tipoDoPet;
    }

    public void setTipoDoPet(String tipoDoPet) {
        this.tipoDoPet = tipoDoPet;
    }

}