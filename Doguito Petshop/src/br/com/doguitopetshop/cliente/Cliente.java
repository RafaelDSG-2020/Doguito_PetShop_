
package br.com.doguitopetshop.cliente;

import br.com.doguitopetshop.Colaborador;
import br.com.doguitopetshop.Dono;
import br.com.doguitopetshop.TabelaAgenda;
import br.com.doguitopetshop.protocolo.Protocolo;
import com.google.gson.Gson;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;
import java.util.Scanner;

public class Cliente {

    // Contém o certificado de confiança para estabelecer conexões
    // seguras em um ambiente de comunicação criptografada, como SSL/TLS.
    private static final String TRUSTSTORE_PATH = "/home/rafael/IdeaProjects/Doguito Petshop Final/src/br/com/doguitopetshop/" +
            "Seguranca/truststore.jks";
    private static final String TRUSTSTORE_PASSWORD = "890023";

    public static void main(String[] args) throws Exception {

        // O JKS é usado para armazenar o certificado de chave pública e privada
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(TRUSTSTORE_PATH), TRUSTSTORE_PASSWORD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Criar o socket do cliente SSL e aplica AES
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("localhost", 7001);
        String[] cifra = {"TLS_AES_256_GCM_SHA384"};
        socket.setEnabledCipherSuites(cifra);

        Thread threadEnviaComando = new Thread(() -> {

            try {
                OutputStream saida = socket.getOutputStream();
                Scanner teclado = new Scanner(System.in);
                while (teclado.hasNextLine()) {
                    String linha = teclado.nextLine();

                    if (linha.equals("cadastrar pet")) {

                        // Preenche o objeto que será passado como parâmetro
                        Dono requisicao = new Dono("ricardo", "0000", "8888888",
                                "ricardo@hotmail.com", "63900-211", "420", "ricardosilva",
                                "12345", "toto", "cachorro");


                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_CADASTRAR_PET,
                                0, "Default", requisicao);

                        Gson gson = new Gson();

                        // Converter o objeto dono em JSON separado
                        String donoJson = gson.toJson(requisicao);

                        // Atualizar o objeto protocolo com o JSON do dono
                        envia.setParametros(donoJson);

                        // Converter o objeto Protocolo completo em JSON
                        String json = gson.toJson(envia);


                        saida.write(json.getBytes());
                        saida.write("\n".getBytes());
                        saida.flush();
                        System.out.println("Requisição enviada para o servidor: " + json);

                    } else if (linha.equals("atualizar dono")) {

                        Dono requisicao = new Dono("ricardo", "0000", "8888888",
                                "ricardosilva@hotmail.com", "63900-211", "420", "ricardosilva",
                                "12345", "toto", "cachorro");;

                        /*Dono requisicao = new Dono("naty", "105678", "8889878",
                                "natal@gmail.com", "63900-211", "59465", "natygui",
                                "12345", "jose", "gatinho");*/


                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_CADASTRAR_PET,
                                0, "Default", requisicao);

                        Gson gson = new Gson();

                        // Converter o objeto dono em JSON separado
                        String donoJson = gson.toJson(requisicao);

                        // Atualizar o objeto protocolo com o JSON do dono
                        envia.setParametros(donoJson);

                        // Converter o objeto Protocolo completo em JSON
                        String json = gson.toJson(envia);

                        saida.write(json.getBytes());
                        saida.write("\n".getBytes());
                        saida.flush();
                        System.out.println("Requisição enviada para o servidor: " + json);

                    } else if (linha.equals("agendar dono")) {

                        Dono requisicao = new Dono("909010", "agenda", "12345");

                        TabelaAgenda reqcod = new TabelaAgenda("1175");

                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_AGENDAR_DONO,
                                0, "Default", requisicao, reqcod);

                        Gson gson = new Gson();

                        // Converter o objeto dono em JSON separado
                        String donoJson = gson.toJson(requisicao);

                        // Atualizar o objeto protocolo com o JSON do dono
                        envia.setParametros(donoJson);

                        // Converter o objeto tabelaagenda em JSON separado
                        String tabelaJson = gson.toJson(reqcod);

                        // Atualizar o objeto protocolo com o JSON do dono
                        envia.setReqCod(tabelaJson);

                        // Converter o objeto Protocolo completo em JSON
                        String fim = gson.toJson(envia);

                        saida.write(fim.getBytes());
                        saida.write("\n".getBytes());
                        saida.flush();
                        System.out.println("Requisição enviada para o servidor: " + fim);

                    } else if (linha.equals("excluir agendamento dono")) {
                        Dono requisicao = new Dono("909010", "agenda", "12345");

                        TabelaAgenda reqcod = new TabelaAgenda("1175");

                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO,
                                Protocolo.OP_EXCLUIR_AGENDAMENTO_DONO, 0, "Default",
                                requisicao, reqcod);

                        Gson gson = new Gson();

                        // Converter o objeto dono em JSON separado
                        String donoJson = gson.toJson(requisicao);

                        // Atualizar o objeto protocolo com o JSON do dono
                        envia.setParametros(donoJson);

                        // Converter o objeto tabelaagenda em JSON separado
                        String tabelaJson = gson.toJson(reqcod);

                        // Atualizar o objeto protocolo com o JSON do dono
                        envia.setReqCod(tabelaJson);

                        // Converter o objeto Protocolo completo em JSON
                        String fim = gson.toJson(envia);

                        saida.write(fim.getBytes());
                        saida.write("\n".getBytes());
                        saida.flush();
                        System.out.println("Requisição enviada para o servidor: " + fim);

                    } else if (linha.equals("inserir horario")) {
                        TabelaAgenda requisicao = new TabelaAgenda("12/07/2023", "13:00",
                                "roberta3", "1564");

                        Colaborador colaborador = new Colaborador("admin", "123", "123");

                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_INSERIR_HORARIO,
                                0, "Default", requisicao, String.valueOf(colaborador));

                        Gson gson = new Gson();

                        // Encapsula objeto TabelaAgenda e envia como parâmetro do Protocolo
                        String tabelaJson = gson.toJson(requisicao);
                        envia.setParametros(tabelaJson);

                        // Encapsula objeto Colaborador e envia como parâmetro do Protocolo
                        String colJson = gson.toJson(colaborador);
                        envia.setColaborador(colJson);

                        // Transforma o protocolo em JSON
                        String json = gson.toJson(envia);

                        saida.write(json.getBytes());
                        saida.write("\n".getBytes());
                        saida.flush();
                        System.out.println("Requisição enviada para o servidor: " + json);

                    } else if (linha.equals("apagar horario")) {
                        TabelaAgenda requisicao = new TabelaAgenda("12/07/2023", "13:00",
                                "roberta3", "1564");

                        Colaborador colaborador = new Colaborador("admin", "123", "123");

                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_APAGAR_HORARIO,
                                0, "Default", requisicao, String.valueOf(colaborador));

                        Gson gson = new Gson();

                        // Encapsula objeto TabelaAgenda e envia como parâmetro do protocolo
                        String tabelaJson = gson.toJson(requisicao);
                        envia.setParametros(tabelaJson);

                        // Encapsula objeto Colaborador e envia como parâmetro do protocolo
                        String colJson = gson.toJson(colaborador);
                        envia.setColaborador(colJson);

                        // Transforma o protocolo em JSON
                        String json = gson.toJson(envia);

                        saida.write(json.getBytes());
                        saida.write("\n".getBytes());
                        saida.flush();
                        System.out.println("Requisição enviada para o servidor: " + json);

                    } else if (linha.equals("cadastrar colaborador")) {
                        Colaborador requisicao = new Colaborador("ravena", "86767", "6777",
                                "ravena@gmail.com", "59621-010", "45555", "raveninha",
                                "3210", "veterinaria", "6789");

                        Gson gson = new Gson();
                        String json = gson.toJson(requisicao);

                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_CADASTRAR_COLABORADOR,
                                0, "Default", requisicao);

                        envia.setParametros(json);

                        String fim = gson.toJson(envia);

                        saida.write(fim.getBytes());
                        saida.write("\n".getBytes()); // Adiciona uma quebra de linha após o JSON
                        saida.flush();
                        System.out.println("Resposta enviada para o servidor: " + json);

                        linha += "\n"; // Adiciona uma quebra de linha ao comando
                        saida.write(json.getBytes());
                    } else if (linha.equals("atualizar colaborador")) {
                        Colaborador requisicao = new Colaborador("ravenasilveira", "86767", "6777",
                                "ravenabombom@gmail.com", "59621-010", "45555", "raveninha",
                                "3210", "veterinaria", "6789");


                        Protocolo envia = new Protocolo(Protocolo.TIPO_REQUISICAO, Protocolo.OP_ATUALIZAR_COLABORADOR,
                                0, "Default", requisicao);

                        Gson gson = new Gson();

                        // Converter o objeto Colaborador em JSON separado
                        String colaboradorJson = gson.toJson(requisicao);

                        // Atualizar o objeto Protocolo com o JSON do Colaborador
                        envia.setParametros(colaboradorJson);

                        // Converter o objeto Protocolo completo em JSON
                        String json = gson.toJson(envia);

                        saida.write(json.getBytes());
                        saida.write("\n".getBytes()); // Adiciona uma quebra de linha após o JSON
                        saida.flush();
                        System.out.println("Resposta enviada para o servidor: " + json);

                        linha += "\n"; // Adiciona uma quebra de linha ao comando
                        saida.write(json.getBytes());

                    } else {
                        if(linha.equals("")) {
                            System.out.println("Saindo...!");
                            break;
                        }
                    }
                }
                teclado.close();
                saida.close();
            } catch (IOException e) {
                System.out.println("Conexão do cliente fechada com sucesso!");
            }

        });

        Thread threadRecebeResposta = new Thread(() -> {

                    try {
                        System.out.println("APTO A RECEBER DADOS DO SERVIDOR \n");
                        InputStream respostaServidor = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = respostaServidor.read(buffer)) != -1) {
                            String linha = new String(buffer, 0, bytesRead);
                            System.out.println(linha);
                        }
                        respostaServidor.close();


                    } catch (IOException e) {
                       // throw new RuntimeException(e);
            }
        });
        threadRecebeResposta.start();
        threadEnviaComando.start();

        threadEnviaComando.join();

        System.out.println("Fechando socket do cliente!");
        socket.close();
    }
}
