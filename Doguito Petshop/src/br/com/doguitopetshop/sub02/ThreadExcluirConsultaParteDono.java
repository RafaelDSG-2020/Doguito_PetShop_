
package br.com.doguitopetshop.sub02;

import br.com.doguitopetshop.Dono;
import br.com.doguitopetshop.TabelaAgenda;
import br.com.doguitopetshop.protocolo.Protocolo;
import com.google.gson.Gson;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ThreadExcluirConsultaParteDono implements Runnable {
    private SSLSocket socket;
    private ServidorExcluirConsultaParteDono servidor;
    private ExecutorService threadPool;
    private int contadorIdMensagem;

    public ThreadExcluirConsultaParteDono(ExecutorService threadPool, SSLSocket socket,
                                          ServidorExcluirConsultaParteDono servidor, int contadorIdMensagem) {
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

            String orienta = "Digite (excluir agendamento dono) para confirmação: ";
            printSaida.println(orienta);

            // Lê a resposta em formato JSON enviada pelo cliente
            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String json = entradaCliente.readLine();
            System.out.println("Requisição em JSON recebida do cliente: " + json);

            // Converte o JSON protocolo para objeto
            Gson gson = new Gson();
            Protocolo resposta = gson.fromJson(json, Protocolo.class);

            // Obter a representação JSON do objeto dono
            String parametrosJson = resposta.getParametros();

            // Obter a representação JSON do objeto dono
            String parametrosCod = resposta.getReqCod();

            // Converter JSON em objeto dono
            Dono dono = gson.fromJson(parametrosJson, Dono.class);

            // Converter JSON em objeto tabelaagenda
            TabelaAgenda tabelaAgenda = gson.fromJson(parametrosCod, TabelaAgenda.class);


            // Extrai os atributos do objeto resposta e resp

            String cpf = dono.getCpf();
            String login = dono.getLogin();
            String senha = dono.getSenha();

            String cod = tabelaAgenda.getIdDaConsulta();

            String mensagem = "Requisição recebida do cliente:\nCPF: " + cpf + "\nLogin: " + login + "\nSenha: " + senha
                    + "\nCódigo: " + cod;

            System.out.println(mensagem);


            printSaida.println(resposta.excluirConsultaDono(Protocolo.OP_EXCLUIR_AGENDAMENTO_DONO, dono, socket, cod,
                    contadorIdMensagem));

        } catch (Exception e) {
            System.out.println("Conexão com o cliente fechada com sucesso!");

        }
    }
}