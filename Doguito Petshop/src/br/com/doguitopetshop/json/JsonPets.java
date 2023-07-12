
package br.com.doguitopetshop.json;

import br.com.doguitopetshop.*;
import br.com.doguitopetshop.api.ConsultaCep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonPets {
    private String nomeDoArquivoJson;

    public void setNomeDoArquivoJson(String nomeDoArquivo) {
        this.nomeDoArquivoJson = nomeDoArquivoJson;
    }

    public String getNomeDoArquivoJson() {
        String nomeTempdoJson = nomeDoArquivoJson;
        return nomeTempdoJson;
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String adicionaPetAoJsonThread(Dono pet, String nomeDoJson) {
        setNomeDoArquivoJson(nomeDoJson);
        Type tipoListaDonos = new TypeToken<List<Dono>>() {
        }.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<Dono> donos = gson.fromJson(reader, tipoListaDonos);

            if (donos == null) {
                donos = new ArrayList<>();
            }

            donos.add(pet);

            try (FileWriter writer = new FileWriter(nomeDoJson)) {
                gson.toJson(donos, writer);
            }
        } catch (FileNotFoundException e) {
            List<Dono> donos = new ArrayList<>();
            donos.add(pet);

            try (FileWriter writer = new FileWriter(nomeDoJson)) {
                gson.toJson(donos, writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pet.getId();
    }


    public String verificarLoginSenha(String login, String senha, String caminho, SSLSocket socket)
            throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        try (FileReader reader = new FileReader(caminho)) {
            List<Dono> donos = gson.fromJson(reader, new TypeToken<List<Dono>>() {
            }.getType());

            if (donos != null) {
                for (Dono dono : donos) {
                    if (dono.getLogin().equals(login) && dono.getSenha().equals(senha)) {
                        System.out.println("Logado com sucesso!");
                        System.out.println("Olá, " + dono.getNome());
                        return dono.getId();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
        System.out.println("Cliente não encontrado!");
        return null;
    }
    public String[] armazenarLogineSenhaLogado(String loginRecebido, String senhaRecebida) {
        try (FileReader reader = new FileReader("pets.json")) {
            List<Dono> donos = gson.fromJson(reader, new TypeToken<List<Dono>>() {
            }.getType());

            if (donos != null) {
                for (Dono dono : donos) {
                    if (dono.getLogin().equals(loginRecebido) && dono.getSenha().equals(senhaRecebida)) {
                        return new String[]{loginRecebido, senhaRecebida};
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
        return null;
    }

    public TabelaAgenda obterDadosdoDonoLogado(String caminho, Dono parametros, SSLSocket socket) throws IOException {
        Scanner leitura = new Scanner(System.in);

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        String[] loginSenha = armazenarLogineSenhaLogado(parametros.getLogin(), parametros.getSenha());
        if (loginSenha != null) {
            String loginArmazenado = loginSenha[0];
            String senhaArmazenada = loginSenha[1];

            try (FileReader reader = new FileReader(caminho)) {
                List<Dono> donos = gson.fromJson(reader, new TypeToken<List<Dono>>() {
                }.getType());

                if (donos != null) {
                    for (Dono dono : donos) {
                        if (dono.getCpf().equals(parametros.getCpf()) && dono.getLogin().equals(loginArmazenado) &&
                                dono.getSenha().equals(senhaArmazenada)) {
                            TabelaAgenda ag = new TabelaAgenda();
                            ag.setNomeDono(dono.getNome());
                            ag.setCpfDono(dono.getCpf());
                            ag.setNomePet(dono.getNomeDoPet());
                            ag.setIdDono(dono.getId());
                            return ag;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                printSaida.println("Arquivo não encontrado.");
            } catch (IOException e) {
                printSaida.println("Erro ao ler arquivo.");
            }
        }
        return null;
    }

    public void obterDadosdoDonoLogado(Dono parametros, SSLSocket socket) throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        String[] loginSenha = armazenarLogineSenhaLogado(parametros.getLogin(), parametros.getSenha());
        if (loginSenha != null) {
            String loginArmazenado = loginSenha[0];
            String senhaArmazenada = loginSenha[1];

            try (FileReader reader = new FileReader("pets.json")) {
                List<Dono> donos = gson.fromJson(reader, new TypeToken<List<Dono>>() {
                }.getType());

                if (donos != null) {
                    for (Dono dono : donos) {
                        if (dono.getCpf().equals(parametros.getCpf()) && dono.getLogin().equals(loginArmazenado) &&
                                dono.getSenha().equals(senhaArmazenada)) {

                            System.out.println("Nome: " + dono.getNome());
                            System.out.println("CPF: " + dono.getCpf());
                            System.out.println("Endereço: " + dono.getEndereco());
                            System.out.println("Telefone: " + dono.getTelefone());
                            System.out.println("E-mail: " + dono.getEmail());
                            System.out.println("Nome do Pet: " + dono.getNomeDoPet());
                            System.out.println("Tipo do Pet: " + dono.getTipoDoPet());
                            System.out.println("Login: "+ dono.getLogin());
                            System.out.println("Senha: " + dono.getSenha());

                        }
                    }
                }

                JsonPets j = new JsonPets();
                j.atualizarDadosDoClientePeloClienteThread(parametros.getCpf(), parametros.getLogin(),
                        parametros.getSenha(), parametros);
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado.");
            } catch (IOException e) {
                System.out.println("Erro ao ler arquivo.");
            }
        }
    }

    public boolean verificaSeLoginExisteNoJsonPets(String login) {
        boolean loginExiste = false;

        try (FileReader reader = new FileReader("pets.json")) {
            List<Dono> donos = gson.fromJson(reader, new TypeToken<List<Dono>>() {}.getType());

            if (donos != null) {
                for (Dono dono : donos) {
                    if (dono.getLogin().equals(login)) {
                        loginExiste = true;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }

        if (loginExiste) {
            return true;
        } else {
            return false;
        }
    }
    public void atualizarDadosDoClientePeloClienteThread(String cpf, String login, String senha, Dono parametros) {

        try (FileReader reader = new FileReader("pets.json")) {
            List<Dono> donos = gson.fromJson(reader, new TypeToken<List<Dono>>() {
            }.getType());

            if (donos != null) {
                boolean atualizado = false;
                for (Dono dono : donos) {
                    if (dono.getCpf().equals(cpf) && dono.getLogin().equals(login) &&
                            dono.getSenha().equals(senha)) {

                        String nomeAntigo = dono.getNome();
                        String nomePetAntigo = dono.getNomeDoPet();
                        String nomeCpfAntigo = dono.getCpf();

                        System.out.println(nomeAntigo);
                        System.out.println(nomePetAntigo);
                        System.out.println(nomeCpfAntigo);

                        String novoNomePet = parametros.getNome();
                        dono.setNomeDoPet(novoNomePet);
                        JsonAgenda ag3 = new JsonAgenda();
                        ag3.atualizaSolicitacoesFeitasPeloCliente(novoNomePet, nomePetAntigo, 1, dono);

                        String novoTipoPet = parametros.getTipoDoPet();
                        dono.setTipoDoPet(novoTipoPet);

                        String novoTelefone = parametros.getTelefone();
                        dono.setTelefone(novoTelefone);

                        String novoEmail = parametros.getEmail();
                        dono.setEmail(novoEmail);

                        String novoCep = parametros.getCep();
                        dono.setCep(novoCep);
                        ConsultaCep consultaCep = new ConsultaCep();
                        dono.setEndereco(consultaCep.buscaEndereco(novoCep));

                        String novoNumero = parametros.getNumero();
                        dono.setNumero(novoNumero);

                        String novaSenha1 = parametros.getSenha();
                        String novaSenha2 = parametros.getSenha();

                        if (novaSenha1.equals(novaSenha2)) {
                            dono.setSenha(novaSenha1);
                                } else {
                                    System.out.println("As senhas não coincidem. A atualização falhou.");
                                }
                                atualizado = true;
                                break;
                    }

                }
                if (atualizado) {
                    try (FileWriter writer = new FileWriter("pets.json")) {
                        gson.toJson(donos, writer);
                        System.out.println("Operação concluída!");
                    } catch (IOException e) {
                        System.out.println("Erro ao escrever no arquivo.");
                    }
                } else {
                    System.out.println("ID não encontrado. A atualização falhou.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo.");
        }
    }


}









