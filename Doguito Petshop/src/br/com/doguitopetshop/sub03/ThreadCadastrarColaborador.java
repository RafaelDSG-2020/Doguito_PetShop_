
package br.com.doguitopetshop.sub03;

import br.com.doguitopetshop.Colaborador;
import br.com.doguitopetshop.protocolo.Protocolo;
import com.google.gson.Gson;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ThreadCadastrarColaborador implements Runnable {
    private SSLSocket socket;
    private ServidorCadastrarColaborador servidor;
    private ExecutorService threadPool;
    private int contadorIdMensagem;

    public ThreadCadastrarColaborador(ExecutorService threadPool, SSLSocket socket, ServidorCadastrarColaborador servidor,
                                      int contadorIdMensagem) {
        this.threadPool = threadPool;
        this.socket = socket;
        this.servidor = servidor;
        this.contadorIdMensagem = contadorIdMensagem;
    }

    @Override
    public void run() {
        try {

            System.out.println("Distribuindo as tarefas para o cliente " + socket);

            // Mecanismo de orientação para o usuário
            OutputStream saidaCliente = socket.getOutputStream();
            PrintWriter printSaida = new PrintWriter(saidaCliente, true);

            String orienta = "Digite (cadastrar colaborador) para confirmação: ";
            printSaida.println(orienta);

            // Lê a resposta em formato JSON enviada pelo cliente
            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String json = entradaCliente.readLine();
            System.out.println("Resposta em JSON recebida do cliente: " + json);


            // Converte o JSON Protocolo para objeto
            Gson gson = new Gson();
            Protocolo resposta = gson.fromJson(json, Protocolo.class);

            // Obter a representação JSON do objeto Dono
            String parametrosJson = resposta.getParametros();

            // Converter JSON em objeto Dono
            Colaborador colaborador = gson.fromJson(parametrosJson, Colaborador.class);

            // Extrai os atributos do objeto resposta
            String nome = colaborador.getNome();
            String cpf = colaborador.getCpf();
            String telefone = colaborador.getTelefone();
            String email = colaborador.getEmail();
            String cep = colaborador.getCep();
            String numero = colaborador.getNumero();
            String login = colaborador.getLogin();
            String senha = colaborador.getSenha();
            String cargo = colaborador.getCargo();
            String matricula = colaborador.getMatricula();

            String mensagem = "Resposta recebida do cliente:\nNome: " + nome + "\nCPF: " + cpf
                    + "\nTelefone: " + telefone + "\nEmail: " + email + "\nCEP: " + cep
                    + "\nNúmero: " + numero + "\nLogin: " + login + "\nSenha: " + senha
                    + "\nCargo: " + cargo + "\nMatrícula: " + matricula;
            System.out.println(mensagem);

            printSaida.println(resposta.cadastrarColaborador(Protocolo.OP_CADASTRAR_COLABORADOR, colaborador, socket,
                    contadorIdMensagem));

        } catch (Exception e) {
            System.out.println("Conexão com o cliente fechada com sucesso!");

        }
    }
}