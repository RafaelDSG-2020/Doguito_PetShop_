
package br.com.doguitopetshop.json;

import br.com.doguitopetshop.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonAgenda {
    private String nomeDoArquivoJson;

    public void setNomeDoArquivoJson(String nomeDoArquivo) {
        this.nomeDoArquivoJson = nomeDoArquivo;
    }

    public String getNomeDoArquivoJson() {
        return nomeDoArquivoJson;
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean adicionaHorarioAoJson(TabelaAgenda a, String nomeDoJson) {
        setNomeDoArquivoJson(nomeDoJson);
        Type tipoListaTabelas = new TypeToken<List<TabelaAgenda>>() {}.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaTabelas);

            if (tabelas == null) {
                tabelas = new ArrayList<>();
            }

            tabelas.add(a);

            try (FileWriter writer = new FileWriter(nomeDoJson)) {
                gson.toJson(tabelas, writer);
                // Retorna verdadeiro se o JSON foi modificado com sucesso
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
                // Retorna falso se ocorrer um erro ao escrever no arquivo JSON
                return false;
            }
        } catch (FileNotFoundException e) {
            List<TabelaAgenda> tabelas = new ArrayList<>();
            tabelas.add(a);

            try (FileWriter writer = new FileWriter(nomeDoJson)) {
                gson.toJson(tabelas, writer);
                // Retorna verdadeiro se o JSON foi criado e modificado com sucesso
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
                // Retorna falso se ocorrer um erro ao escrever no arquivo JSON
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Retorna falso se ocorrer um erro ao ler o arquivo JSON
            return false;
        }
    }


    public void imprimirHorariosDoJsonAgenda(String nomeDoJson, int cenario, SSLSocket socket) throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);


        setNomeDoArquivoJson(nomeDoJson);
        Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {
        }.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<TabelaAgenda> tabelaAgendaList = gson.fromJson(reader, tipoListaElementos);

            if (tabelaAgendaList != null) {
                boolean encontrouHorario = false;

                for (TabelaAgenda tabelaAgenda : tabelaAgendaList) {
                    if (cenario == 0 && tabelaAgenda.getNomeDono() != null && tabelaAgenda.getNomePet() != null &&
                            tabelaAgenda.getCpfDono() != null) {
                        printSaida.println("Data: " + tabelaAgenda.getData());
                        printSaida.println("Hora: " + tabelaAgenda.getHora());
                        printSaida.println("Código : " + tabelaAgenda.getIdDaConsulta());
                        printSaida.println("Nome do Veterinário: " + tabelaAgenda.getNomeDoVeterinario());
                        printSaida.println("Matricula: " + tabelaAgenda.getMatricula());
                        printSaida.println("Nome do Dono: " + tabelaAgenda.getNomeDono());
                        printSaida.println("Nome do Pet: " + tabelaAgenda.getNomePet());
                        printSaida.println("CPF do Dono: " + tabelaAgenda.getCpfDono());
                        printSaida.println("ID do Dono: " + tabelaAgenda.getIdDono());
                        encontrouHorario = true;
                    }
                    if (cenario == 1 && tabelaAgenda.getNomeDono() == null && tabelaAgenda.getNomePet() == null &&
                            tabelaAgenda.getCpfDono() == null) {
                        printSaida.println("Data: " + tabelaAgenda.getData());
                        printSaida.println("Hora: " + tabelaAgenda.getHora());
                        printSaida.println("Código : " + tabelaAgenda.getIdDaConsulta());
                        printSaida.println("Nome do Veterinário: " + tabelaAgenda.getNomeDoVeterinario());
                        printSaida.println("Matricula: " + tabelaAgenda.getMatricula());
                        encontrouHorario = true;
                    }
                }

                if (!encontrouHorario) {
                    if (cenario == 0) {
                        printSaida.println("Ninguém reservou consulta!");
                    } else if (cenario == 1) {
                        printSaida.println("Nenhum horário disponível com os campos nulos!");
                    } else {
                        printSaida.println("Opção inválida!");
                    }
                }
            } else {
                printSaida.println("Nenhum horário disponível!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean verificarIdRepetido(String idDaConsulta, String nomeDoJson) {
        Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {
        }.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);
            if (tabelas != null) {
                for (TabelaAgenda tabela : tabelas) {
                    String id = tabela.getIdDaConsulta();
                    if (id != null && id.equals(idDaConsulta)) {
                        // ID já existe, é repetido
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ID não está repetido
        return false;
    }

    public boolean solicitarConsultaThread(String nomeDoJson, TabelaAgenda ag, String resp, SSLSocket socket)
            throws IOException {
        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {}.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);
            if (tabelas != null) {
                boolean codigoEncontrado = false;
                boolean codigoExistente = false;
                for (TabelaAgenda tabela : tabelas) {
                    if (resp.equals(tabela.getIdDaConsulta())) {
                        codigoEncontrado = true;
                        if (ag != null) {
                            // Verificar se os campos já estão preenchidos
                            if (tabela.getNomeDono() != null && tabela.getCpfDono() != null &&
                                    tabela.getNomePet() != null && tabela.getIdDono() != null) {
                                // Retornar falso se os campos já estiverem preenchidos
                                return false;
                            }

                            // Preencher os campos
                            tabela.setNomeDono(ag.getNomeDono());
                            tabela.setCpfDono(ag.getCpfDono());
                            tabela.setNomePet(ag.getNomePet());
                            tabela.setIdDono(ag.getIdDono());
                        }
                    }
                    codigoExistente = true;
                }
                if (!codigoEncontrado && codigoExistente) {
                    printSaida.println("Código não encontrado!");
                    // Retorno indicando que o código não foi encontrado
                    return false;
                }

                // Escrever de volta no arquivo JSON
                try (FileWriter writer = new FileWriter(nomeDoJson)) {
                    gson.toJson(tabelas, writer);
                } catch (IOException e) {
                    printSaida.println("Erro ao escrever no arquivo.");
                    e.printStackTrace();
                    // Retorno indicando falha na escrita no arquivo
                    return false;
                }
                // Retorno indicando que a consulta foi encontrada e atualizada com sucesso
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Retorno indicando que a consulta não foi encontrada
        return false;
    }
    public boolean removerConsultaThread(String nomeDoJson, TabelaAgenda ag, String resp, SSLSocket socket)
            throws IOException {
        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {}.getType();

        try (FileReader reader = new FileReader(nomeDoJson)) {
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);

            if (tabelas != null) {
                boolean encontrouConsulta = false;
                for (TabelaAgenda tabela : tabelas) {
                    if (tabela.getIdDaConsulta() != null && tabela.getIdDaConsulta().equals(resp)) {
                        encontrouConsulta = true;
                        if (tabela.getNomeDono() == null && tabela.getCpfDono() == null &&
                                tabela.getNomePet() == null && tabela.getIdDono() == null) {
                            // Retornar falso se os campos já estiverem apagados
                            return false;
                        }
                        tabela.setNomeDono(null);
                        tabela.setCpfDono(null);
                        tabela.setNomePet(null);
                        tabela.setIdDono(null);
                    }
                }
                if (!encontrouConsulta) {
                    printSaida.println("Consulta não encontrada!");
                    // Retornar falso se a consulta não for encontrada
                    return false;
                }

                // Escrever de volta no arquivo JSON
                try (FileWriter writer = new FileWriter(nomeDoJson)) {
                    gson.toJson(tabelas, writer);
                } catch (IOException e) {
                    printSaida.println("Erro ao escrever no arquivo.");
                    e.printStackTrace();
                    // Retornar falso em caso de erro na escrita no arquivo
                    return false;
                }
            }
        } catch (IOException e) {
            printSaida.println("Erro ao ler o arquivo.");
            e.printStackTrace();
            // Retornar falso em caso de erro na leitura do arquivo
            return false;
        }
        // Retornar verdadeiro se os campos foram apagados com sucesso
        return true;
    }
    public void imprimirAgendamentosParticular(TabelaAgenda ag, SSLSocket socket) throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        try (FileReader reader = new FileReader("agenda.json")) {
            List<TabelaAgenda> tabelas = gson.fromJson(reader, new TypeToken<List<TabelaAgenda>>() {
            }.getType());

            if (tabelas != null) {
                for (TabelaAgenda tabela : tabelas) {
                    if ((ag.getNomeDono() != null && ag.getNomeDono().equals(tabela.getNomeDono())) ||
                            (ag.getNomePet() != null && ag.getNomePet().equals(tabela.getNomePet())) ||
                            (ag.getCpfDono() != null && ag.getCpfDono().equals(tabela.getCpfDono()))) {

                        printSaida.println("======================" + "\n");
                        printSaida.println("Data: " + tabela.getData());
                        printSaida.println("Hora: " + tabela.getHora());
                        printSaida.println("Código : " + tabela.getIdDaConsulta());
                        printSaida.println("Nome do Veterinário: " + tabela.getNomeDoVeterinario());
                        printSaida.println("Matricula: " + tabela.getMatricula());
                        printSaida.println("Nome do Dono: " + tabela.getNomeDono());
                        printSaida.println("Nome do Pet: " + tabela.getNomePet());
                        printSaida.println("CPF do Dono: " + tabela.getCpfDono());

                    }
                }
            }

        } catch (FileNotFoundException e) {
            printSaida.println("Arquivo não encontrado.");
        } catch (IOException e) {
            printSaida.println("Erro ao ler arquivo.");
        }
    }

    public void atualizaSolicitacoesFeitasPeloCliente(String infoAtualizada, String nomeAntigo, int escolha,
                                                      Dono dono) {
        try (FileReader reader = new FileReader("agenda.json")) {
            Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {
            }.getType();
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);

            if (tabelas != null) {
                boolean atualizado = false;
                for (TabelaAgenda tabela : tabelas) {
                    if (dono.getId().equals(tabela.getIdDono())) {
                        if (tabela.getNomePet() != null && tabela.getNomePet().equals(nomeAntigo) &&
                                escolha == 1) {
                            tabela.setNomePet(infoAtualizada);
                            atualizado = true;
                        } else {
                            System.out.println("Nome antigo não encontrado");
                        }
                    }
                }

                if (atualizado) {
                    try (FileWriter writer = new FileWriter("agenda.json")) {
                        gson.toJson(tabelas, writer);
                    }
                    System.out.println("Campo atualizado com sucesso!");
                } else {
                    System.out.println("Opção não permitida ou nome antigo não encontrado na agenda!");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler ou escrever no arquivo.");
        }
    }

    public boolean verSeJaExisteHorario(String data, String hora, String nomeVeterinario, String matricula,
                                        SSLSocket socket) throws IOException {
        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        try (FileReader reader = new FileReader("agenda.json")) {
            Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {
            }.getType();
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);

            if (tabelas != null) {
                for (TabelaAgenda tabela : tabelas) {
                    if (tabela.getData().equals(data) && tabela.getHora().equals(hora) &&
                            tabela.getNomeDoVeterinario().equals(nomeVeterinario) &&
                            tabela.getMatricula().equals(matricula)) {
                            printSaida.println("Esse horário de consulta para o veterinário escolhido " +
                                "já existe no sistema!");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo.");
        }
        System.out.println("Novo horário cadastrado com sucesso!");
        return false;
    }
    public TabelaAgenda verSeJaExisteHorarioRetorna(String data, String hora, String nomeVeterinario, String matricula,
                                                    SSLSocket socket) throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        try (FileReader reader = new FileReader("agenda.json")) {
            Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {
            }.getType();
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);

            if (tabelas != null) {
                for (TabelaAgenda tabela : tabelas) {
                    if (tabela.getData().equals(data) && tabela.getHora().equals(hora) &&
                            tabela.getNomeDoVeterinario().equals(nomeVeterinario) &&
                            tabela.getMatricula().equals(matricula)) {
                            printSaida.println("Esse horário de consulta para o veterinário escolhido " +
                               "já existe no sistema!");
                            return tabela;
                    }
                }
            }
        } catch (IOException e) {
            printSaida.println("Erro ao ler o arquivo.");
        }
        return null;
    }
    public void apagaHorarioMarcado(String data, String hora, String veterinario, String matricula, SSLSocket socket)
            throws IOException {

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        try (FileReader reader = new FileReader("agenda.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type tipoListaElementos = new TypeToken<List<TabelaAgenda>>() {}.getType();
            List<TabelaAgenda> tabelas = gson.fromJson(reader, tipoListaElementos);

            if (tabelas != null) {
                Iterator<TabelaAgenda> iterator = tabelas.iterator();
                while (iterator.hasNext()) {
                    TabelaAgenda tabela = iterator.next();
                    // Verificar se a tabela corresponde aos critérios desejados para exclusão
                    if (tabela.getData().equals(data) &&
                            tabela.getHora().equals(hora) &&
                            tabela.getNomeDoVeterinario().equals(veterinario) &&
                            tabela.getMatricula().equals(matricula)) {
                        // Remover a tabela da lista
                        iterator.remove();
                        // Parar a iteração após encontrar a tabela a ser removida
                        break;
                    }
                }

                // Salvar a lista atualizada de volta no arquivo JSON com formatação
                try (FileWriter writer = new FileWriter("agenda.json")) {
                    gson.toJson(tabelas, writer);
                    printSaida.println("Horário marcado removido com sucesso.");
                } catch (IOException e) {
                    printSaida.println("Erro ao escrever no arquivo.");
                }
            }
        } catch (IOException e) {
           printSaida.println("Erro ao ler o arquivo.");
        }
    }


}
