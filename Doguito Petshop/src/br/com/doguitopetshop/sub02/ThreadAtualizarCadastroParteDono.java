
package br.com.doguitopetshop.sub02;

import br.com.doguitopetshop.Dono;
import br.com.doguitopetshop.protocolo.Protocolo;
import com.google.gson.Gson;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ThreadAtualizarCadastroParteDono implements Runnable {
    private SSLSocket socket;
    private ServidorAtualizarCadastroParteDono servidor;
    private ExecutorService threadPool;
    private int contadorIdMensagem;

    public ThreadAtualizarCadastroParteDono(ExecutorService threadPool, SSLSocket socket,
                                            ServidorAtualizarCadastroParteDono servidor, int contadorIdMensagem) {
        this.threadPool = threadPool;
        this.socket = socket;
        this.servidor = servidor;
        this.contadorIdMensagem = contadorIdMensagem;
    }

    @Override
    public void run() {
        try {

            System.out.println("Distribuindo as tarefas para o cliente " + socket);

            OutputStream saidaCliente = socket.getOutputStream();
            PrintWriter printSaida = new PrintWriter(saidaCliente, true);

            String orienta = "Digite (atualizar dono) para confirmação: ";
            printSaida.println(orienta);

            // Lê a resposta em formato JSON enviada pelo cliente
            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String json = entradaCliente.readLine();
            System.out.println("Requisição em JSON recebida do cliente: " + json);

            // Converte o JSON Protocolo para objeto
            Gson gson = new Gson();
            Protocolo resposta = gson.fromJson(json, Protocolo.class);

            // Obter a representação JSON do objeto Dono
            String parametrosJson = resposta.getParametros();

            // Converter JSON em objeto Dono
            Dono dono = gson.fromJson(parametrosJson, Dono.class);


            // Extrai os atributos do objeto resposta

            String nome = dono.getNome();
            String cpf = dono.getCpf();
            String telefone = dono.getTelefone();
            String email = dono.getEmail();
            String cep = dono.getCep();
            String numero = dono.getNumero();
            String login = dono.getLogin();
            String senha = dono.getSenha();
            String nomePet = dono.getNomeDoPet();
            String tipoPet = dono.getTipoDoPet();

            String mensagem = "Requisição recebida do cliente:\nNome: " + nome + "\nCPF: " + cpf +
                    "\nTelefone: " + telefone + "\nEmail: " + email + "\nCEP: " + cep + "\nNúmero: " + numero +
                    "\nLogin: " + login + "\nSenha: " + senha + "\nNome do Pet: " + nomePet +
                    "\nTipo do Pet: " + tipoPet;

            printSaida.println(resposta.atualizarDadosParteDono(Protocolo.OP_ATUALIZAR_PARTE_DONO, dono, socket,
                    contadorIdMensagem));

        } catch (Exception e) {
            System.out.println("Conexão com o cliente fechada com sucesso!");

        }
    }
}