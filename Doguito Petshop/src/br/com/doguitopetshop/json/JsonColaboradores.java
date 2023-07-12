
package br.com.doguitopetshop.json;

import br.com.doguitopetshop.Colaborador;
import br.com.doguitopetshop.api.ConsultaCep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

public class JsonColaboradores {
    private String nomeDoArquivoJson;

    public void setNomeDoArquivoJson(String nomeDoArquivo) {
        this.nomeDoArquivoJson = nomeDoArquivoJson;
    }

    public String getNomeDoArquivoJson() {
        String nomeTempdoJson = nomeDoArquivoJson;
        return nomeTempdoJson;
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String adicionaColaboradorAoJson(Colaborador colaborador, String nomeDoJson) {
        setNomeDoArquivoJson(nomeDoJson);
        Type tipoListaColaboradores = new TypeToken<List<Colaborador>>() {
        }.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<Colaborador> colaboradores = gson.fromJson(reader, tipoListaColaboradores);

            if (colaboradores == null) {
                colaboradores = new ArrayList<>();
            }

            colaboradores.add(colaborador);

            try (FileWriter writer = new FileWriter(nomeDoJson)) {
                gson.toJson(colaboradores, writer);
            }

            return colaborador.getId();
        } catch (FileNotFoundException e) {
            List<Colaborador> colaboradores = new ArrayList<>();
            colaboradores.add(colaborador);

            try (FileWriter writer = new FileWriter(nomeDoJson)) {
                gson.toJson(colaboradores, writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String verificarLoginSenhaThread(String login, String senha, String caminho) {

        try (FileReader reader = new FileReader(caminho)) {
            List<Colaborador> colaboradores = gson.fromJson(reader, new TypeToken<List<Colaborador>>() {
            }.getType());

            if (colaboradores != null) {
                for (Colaborador colaborador : colaboradores) {
                    if (colaborador.getLogin().equals(login) && colaborador.getSenha().equals(senha)) {
                        System.out.println("Logado com sucesso!");
                        System.out.println("Olá, " + colaborador.getNome());
                        return colaborador.getId();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
        System.out.println("Colaborador(a) não encontrado!");
        return null;
    }

    public boolean procurarColaborador(String nome, String matricula, String caminho, SSLSocket socket)
            throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        try (FileReader reader = new FileReader(caminho)) {
            List<Colaborador> colaboradores = gson.fromJson(reader, new TypeToken<List<Colaborador>>() {
            }.getType());

            if (colaboradores != null) {
                for (Colaborador colaborador : colaboradores) {
                    if (colaborador.getNome().equals(nome) && colaborador.getMatricula().equals(matricula)) {
                        printSaida.println("Colaborador encontrado!");
                        printSaida.println("Nome: " + colaborador.getNome());
                        printSaida.println("Matrícula: " + colaborador.getMatricula());
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            printSaida.println("Arquivo não encontrado.");
        } catch (IOException e) {
            printSaida.println("Erro ao ler arquivo.");
        }
        printSaida.println("Colaborador não encontrado!");
        return false;
    }
    public void buscaColaboradorNoJson(String cpf, String nomeDoJson, SSLSocket socket) throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        setNomeDoArquivoJson(nomeDoJson);
        Type tipoListaColaboradores = new TypeToken<List<Colaborador>>() {}.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<Colaborador> colaboradores = gson.fromJson(reader, tipoListaColaboradores);

            if (colaboradores != null) {
                for (Colaborador colab : colaboradores) {
                    if (colab.getCpf().equals(cpf)) {
                        printSaida.println("Dados do colaborador encontrado:");
                        printSaida.println("ID: " + colab.getId());
                        printSaida.println("Nome: " + colab.getNome());
                        printSaida.println("CPF: " + colab.getCpf());
                        printSaida.println("Endereço: " + colab.getEndereco());
                        printSaida.println("Telefone: " + colab.getTelefone());
                        printSaida.println("E-mail: " + colab.getEmail());
                        printSaida.println("Matricula:" + colab.getMatricula());
                        printSaida.println("Cargo: " + colab.getCargo());
                        return;
                    }
                }
            }

            printSaida.println("Colaborador não encontrado.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String atualizarDadosDoColaborador(Colaborador c, SSLSocket socket) throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        String cpf = c.getCpf();

        buscaColaboradorNoJson(cpf, "colaboradores.json", socket);


        String id = null;
        try (FileReader reader = new FileReader("colaboradores.json")) {
            List<Colaborador> colaboradores = gson.fromJson(reader, new TypeToken<List<Colaborador>>() {
            }.getType());

            boolean atualizado = false;
            if (colaboradores != null) {
                atualizado = false;
                for (Colaborador colaborador : colaboradores) {
                    if (colaborador.getCpf().equals(cpf)) {

                        String novoCargo = c.getCargo();
                        colaborador.setCargo(novoCargo);

                        //String novaMatricula = c.getMatricula();
                        //colaborador.setMatricula(novaMatricula);

                        String novoTelefone = c.getTelefone();
                        colaborador.setTelefone(novoTelefone);

                        String novoEmail = c.getEmail();
                        colaborador.setEmail(novoEmail);

                        String novoCep = c.getCep();
                        colaborador.setCep(novoCep);
                        ConsultaCep consultaCep = new ConsultaCep();
                        colaborador.setEndereco(consultaCep.buscaEndereco(novoCep));

                        String novoNumero = c.getNumero();
                        colaborador.setNumero(novoNumero);

                        colaborador.perguntaLoginThread(colaborador);
                        atualizado = true;

                        colaborador.setSenha(c.getSenha());

                        id = colaborador.getId();
                    }
                }
            }
            if (atualizado) {
                try (FileWriter writer = new FileWriter("colaboradores.json")) {
                    gson.toJson(colaboradores, writer);
                    printSaida.println("Campo atualizado com sucesso!");
                } catch (IOException e) {
                    printSaida.println("Erro ao escrever no arquivo.");
                }
            } else {
                printSaida.println("Dado não encontrado. A atualização falhou.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
        return id;
    }
    public boolean verificaSeLoginExisteNoJsonColaboradores(String login){

        try (FileReader reader = new FileReader("colaboradores.json")) {
            List<Colaborador> colaboradores = gson.fromJson(reader, new TypeToken<List<Colaborador>>() {
            }.getType());

            if (colaboradores != null) {
                for (Colaborador colaborador : colaboradores) {
                    if (colaborador.getLogin().equals(login)) {
                        return true;

                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
        return false;
    }
}

