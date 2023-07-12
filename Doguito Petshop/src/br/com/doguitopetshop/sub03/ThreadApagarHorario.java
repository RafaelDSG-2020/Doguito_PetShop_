
package br.com.doguitopetshop.sub03;

import br.com.doguitopetshop.Colaborador;
import br.com.doguitopetshop.TabelaAgenda;
import br.com.doguitopetshop.protocolo.Protocolo;
import com.google.gson.Gson;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ThreadApagarHorario implements Runnable {
    private SSLSocket socket;
    private ServidorApagarHorario servidor;
    private ExecutorService threadPool;
    private int contadorIdMensagem;

    public ThreadApagarHorario (ExecutorService threadPool, SSLSocket socket, ServidorApagarHorario servidor,
                                int contadorIdMensagem){
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

            String orienta = "Digite (apagar horario) para confirmação: ";
            printSaida.println(orienta);

            // Lê a resposta em formato JSON enviada pelo cliente
            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String json = entradaCliente.readLine();
            System.out.println("Requisição em JSON recebida do cliente: " + json);

            // Converte o JSON em um objeto Protocolo
            Gson gson = new Gson();
            Protocolo resposta = gson.fromJson(json, Protocolo.class);

            // Obter a representação JSON do objeto TabelaAgenda
            String parametrosJson = resposta.getParametros();

            String parametrosCol = resposta.getColaborador();

            // Converter JSON em objeto TabelaAgenda
            TabelaAgenda tabelaAgenda = gson.fromJson(parametrosJson, TabelaAgenda.class);
            Colaborador colaborador = gson.fromJson(parametrosCol, Colaborador.class);

            // Extrai os atributos do objeto resposta
            String data = tabelaAgenda.getData();
            String hora = tabelaAgenda.getHora();
            String nomeVeterinario = tabelaAgenda.getNomeDoVeterinario();
            String matricula = tabelaAgenda.getMatricula();

            String login = colaborador.getLogin();
            String senha = colaborador.getSenha();
            String cpf = colaborador.getCpf();

            String mensagem = "Resposta recebida do cliente:\n" +
                    "Data: " + data + "\n" +
                    "Hora: " + hora + "\n" +
                    "Nome do veterinário: " + nomeVeterinario + "\n" +
                    "Matrícula: " + matricula + "\n" +
                    "Login do colaborador: " + login + "\n" +
                    "Senha do colaborador: " + senha + "\n" +
                    "CPF do colaborador: " + cpf;

            System.out.println(mensagem);
            printSaida.println(resposta.apagarHorario(Protocolo.OP_APAGAR_HORARIO, tabelaAgenda, socket,
                    contadorIdMensagem, colaborador));

        } catch (Exception e) {
            System.out.println("Conexão com o cliente fechada com sucesso!");

        }
    }
}


