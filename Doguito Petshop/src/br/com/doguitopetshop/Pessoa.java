
package br.com.doguitopetshop;

import br.com.doguitopetshop.api.ConsultaCep;
import br.com.doguitopetshop.api.Endereco;
import br.com.doguitopetshop.json.JsonColaboradores;
import br.com.doguitopetshop.json.JsonPets;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Pessoa {

    public String id;

    public String nome;

    public String cpf;

    public String telefone;

    public String email;

    public String cep;

    public Endereco endereco;

    public String numero;

    public String login;

    public String senha;

    public Pessoa() {
    }

    public Pessoa(String nome, String cpf, String telefone, String email, String cep, String numero, String login,
                  String senha) {
    }

    public Pessoa(String login, String senha) {
    }

    public Pessoa(String cpf, String login, String senha) {
    }

    public String getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setId(String id) {
        this.id = UUID.randomUUID().toString();
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getCep() {
        return cep;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getNumero() {
        return numero;
    }

    public String getLogin() {
        return login;
    }
    public String getSenha() {
        return senha;
    }

    public boolean cadastrar(Dono p) {

        boolean deubom = false;
        boolean cepValido = false;
        while (!cepValido) {
            try {
                cep = p.getCep();
                ConsultaCep consultaCep = new ConsultaCep();
                endereco = consultaCep.buscaEndereco(cep);
                setEndereco(endereco);
                p.setCep(cep);
                cepValido = true; // Define cepValido como true para sair do loop
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        perguntaLoginThread(p);

        if (perguntaLoginThread(p)){
            criarId(p);
            deubom = true;
        }else{
            System.out.println("O login já pertence a outro usuário. Tente novamente.");
        }
        return deubom;
    }
    public boolean cadastrar(Colaborador p, Socket socket) throws IOException {
        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        boolean deubom = false;
        boolean cepValido = false;
        while (!cepValido) {
            try {
                cep = p.getCep();
                ConsultaCep consultaCep = new ConsultaCep();
                endereco = consultaCep.buscaEndereco(cep);
                setEndereco(endereco);
                p.setCep(cep);
                // Define cepValido como true para sair do loop
                cepValido = true;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        perguntaLoginThread(p);

        if (perguntaLoginThread(p)){
            criarId(p);
            deubom = true;
        }else{
            printSaida.println("O login já pertence a outro usuário. Tente novamente.");
        }
        return deubom;
    }

    public void criarId(Dono p) {
      p.setId(UUID.randomUUID().toString());
   }
    public void criarId(Colaborador p) {
        p.setId(UUID.randomUUID().toString());
    }

    public boolean verificaSeExistePessoaComLogin(String dado) {
        JsonPets pj = new JsonPets();
        JsonColaboradores cl = new JsonColaboradores();

        boolean temNoPets = pj.verificaSeLoginExisteNoJsonPets(dado);
        boolean temNoCol = cl.verificaSeLoginExisteNoJsonColaboradores(dado);

        if (temNoPets && !temNoCol) {
            System.out.println("Existe um cliente com esse login!");
            return false;
        } else if (!temNoPets && temNoCol) {
            System.out.println("Existe colaborador com esse login!");
            return false;
        } else if (!temNoPets && !temNoCol) {
            return true;
        }
        return false;
    }
    public boolean perguntaLoginThread(Pessoa p) {
        String login = p.getLogin();
        if(verificaSeExistePessoaComLogin(login)){
            p.setLogin(login);
            return true;
        }else{
            return false;
        }
    }
}
