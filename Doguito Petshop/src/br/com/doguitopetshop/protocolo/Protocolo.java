
package br.com.doguitopetshop.protocolo;

import br.com.doguitopetshop.Colaborador;
import br.com.doguitopetshop.Dono;
import br.com.doguitopetshop.TabelaAgenda;
import br.com.doguitopetshop.json.JsonAgenda;
import br.com.doguitopetshop.json.JsonColaboradores;
import br.com.doguitopetshop.json.JsonPets;
import com.google.gson.Gson;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class Protocolo implements Serializable {

    public final static String TIPO_REQUISICAO = "REQUISICAO";
    public final static String TIPO_RESPOSTA = "RESPOSTA";

    public final static int OP_CADASTRAR_PET = 1;
    public final static int OP_ATUALIZAR_PARTE_DONO = 2;
    public final static int OP_AGENDAR_DONO = 3;
    public final static int OP_EXCLUIR_AGENDAMENTO_DONO = 4;
    public final static int OP_INSERIR_HORARIO = 5;
    public final static int OP_APAGAR_HORARIO = 6;
    public final static int OP_CADASTRAR_COLABORADOR = 7;
    public final static int OP_ATUALIZAR_COLABORADOR = 8;

    String tipo;
    int codOperacao;
    int idMensagem;
    String idCliente;
    String parametros;
    String mensagemResposta;
    String reqCod;
    String colaborador;

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }


    public Protocolo(String tipoRequisicao, int opLoginPet, int idMensagem, String idCliente, TabelaAgenda requisicao,
                     String colaborador) {
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.mensagemResposta = mensagemResposta;
        this.reqCod = reqCod;
        this.colaborador = colaborador;
    }


    public Protocolo(String tipoRequisicao, int opLoginPet, int idMensagem, String idCliente, Dono requisicao,
                     String reqcod) {
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.mensagemResposta = mensagemResposta;
        this.reqCod = reqCod;
    }

    public Protocolo(String tipoRequisicao, int opLoginPet, int idMensagem, String idCliente, Dono requisicao,
                     TabelaAgenda reqcod) {
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.mensagemResposta = mensagemResposta;
        this.reqCod = String.valueOf(reqCod);
    }


    public Protocolo(String tipo, int codOperacao, int idMensagem, String idCliente, String mensagemResposta) {
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.mensagemResposta = mensagemResposta;
    }

    public Protocolo(String tipo, int codOperacao, int idMensagem, String idCliente, Dono parametros) {
        super();
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.parametros = String.valueOf(parametros);
    }

    public Protocolo(String tipo, int codOperacao, int idMensagem, String idCliente, Colaborador parametros) {
        super();
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.parametros = String.valueOf(parametros);
    }

    public Protocolo(String tipo, int codOperacao, int idMensagem, String idCliente, TabelaAgenda parametros) {
        super();
        this.tipo = tipo;
        this.codOperacao = codOperacao;
        this.idMensagem = idMensagem;
        this.idCliente = idCliente;
        this.parametros = String.valueOf(parametros);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCodOperacao() {
        return codOperacao;
    }

    public void setCodOperacao(int codOperacao) {
        this.codOperacao = codOperacao;
    }

    public int getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getMensagemResposta() {
        return mensagemResposta;
    }

    public String getReqCod() {
        return reqCod;
    }

    public void setReqCod(String reqCod) {
        this.reqCod = reqCod;
    }

    public void setMensagemResposta(String mensagemResposta) {
        this.mensagemResposta = mensagemResposta;
    }

    public String cadastraPet(int codOperacao, Dono parametros, SSLSocket socket, int contadorIdMensagem) throws IOException {

        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 1) {
            JsonPets jsonPets = new JsonPets();
            String idSessao = "Inválido";
            if (parametros.cadastrar(parametros)) {

                idSessao = jsonPets.adicionaPetAoJsonThread(parametros, "pets.json");
                resposta = new Protocolo(TIPO_RESPOSTA, OP_CADASTRAR_PET, contadorIdMensagem, idSessao,
                        "Pet cadastrado com sucesso no sistema!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            } else {
                resposta = new Protocolo(TIPO_RESPOSTA, OP_CADASTRAR_PET, contadorIdMensagem, idSessao,
                        "Erro ao cadastrar pet no sistema!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);

            }
        }
        return resposta.getMensagemResposta();
    }

    public String atualizarDadosParteDono(int codOperacao, Dono parametros, SSLSocket socket, int contadorIdMensagem)
            throws IOException {

        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 2) {

            JsonPets jsonPets = new JsonPets();

            String idSessao = jsonPets.verificarLoginSenha(parametros.getLogin(), parametros.getSenha(),
                    "pets.json", socket);

            if(idSessao != null) {
                jsonPets.obterDadosdoDonoLogado(parametros, socket);
                resposta = new Protocolo(TIPO_RESPOSTA, OP_ATUALIZAR_PARTE_DONO, contadorIdMensagem, idSessao,
                        "Atualização de cadastro do pet feita com sucesso!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            }else{
                resposta = new Protocolo(TIPO_RESPOSTA, OP_ATUALIZAR_PARTE_DONO, contadorIdMensagem, idSessao,
                        "Erro ao atualizar cadastro. Verifique se ele exite no sistema!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            }
        }
        return resposta.getMensagemResposta();
    }
    public String agendarConsultaDono(int codOperacao, Dono parametros, SSLSocket socket, TabelaAgenda resp,
                                      int contadorIdMensagem) throws IOException {
        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 3) {
            JsonAgenda ag = new JsonAgenda();
            ag.imprimirHorariosDoJsonAgenda("agenda.json", 1, socket);

            JsonPets jsonPets = new JsonPets();
            String idSessao = jsonPets.verificarLoginSenha(parametros.getLogin(), parametros.getSenha(),
                    "pets.json", socket);
            if (idSessao != null) {
                JsonPets pet = new JsonPets();
                TabelaAgenda agDadosDono = pet.obterDadosdoDonoLogado("pets.json", parametros, socket);
                if (agDadosDono != null) {
                    boolean ok = ag.solicitarConsultaThread("agenda.json", agDadosDono,
                            resp.getIdDaConsulta(), socket);
                    if (ok) {
                        resposta = new Protocolo(TIPO_RESPOSTA, OP_AGENDAR_DONO, contadorIdMensagem, idSessao,
                                "Agendamento feito com sucesso!");
                        Gson gson = new Gson();
                        String respostaJson = gson.toJson(resposta);
                        saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                    } else {
                        resposta = new Protocolo(TIPO_RESPOSTA, OP_AGENDAR_DONO, contadorIdMensagem, idSessao,
                                "Erro em agendar consulta! " +
                                                "Horário foi marcado ou não existe na agenda!");
                        Gson gson = new Gson();
                        String respostaJson = gson.toJson(resposta);
                        saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                    }
                } else {
                    resposta = new Protocolo(TIPO_RESPOSTA, OP_AGENDAR_DONO, contadorIdMensagem, idSessao,
                            "Dados de cadastros não foram puxados!");
                    Gson gson = new Gson();
                    String respostaJson = gson.toJson(resposta);
                    saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                }
            } else {
                resposta = new Protocolo(TIPO_RESPOSTA, OP_AGENDAR_DONO, contadorIdMensagem, idSessao,
                        "O cadastro não existe na base de dados!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            }
        }
        return resposta.getMensagemResposta();
    }

    public String excluirConsultaDono(int codOperacao, Dono parametros, SSLSocket socket, String resp,
                                      int contadorIdMensagem) throws IOException {
        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 4) {

            JsonPets pet = new JsonPets();
            TabelaAgenda agDadosDonoEx = pet.obterDadosdoDonoLogado("pets.json", parametros, socket);

            JsonAgenda agex = new JsonAgenda();
            agex.imprimirAgendamentosParticular(agDadosDonoEx, socket);

            JsonPets jsonPets = new JsonPets();
            String idSessao = jsonPets.verificarLoginSenha(parametros.getLogin(), parametros.getSenha(),
                    "pets.json", socket);

            if(idSessao != null){
                boolean ok = agex.removerConsultaThread("agenda.json", agDadosDonoEx, resp, socket);
                    if(ok){
                    resposta = new Protocolo(TIPO_RESPOSTA, OP_EXCLUIR_AGENDAMENTO_DONO, contadorIdMensagem, idSessao,
                        "Consulta excluída com sucesso!");
                    Gson gson = new Gson();
                    String respostaJson = gson.toJson(resposta);
                    saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            }else{
                    resposta = new Protocolo(TIPO_RESPOSTA, OP_EXCLUIR_AGENDAMENTO_DONO, contadorIdMensagem, idSessao,
                            "Erro ao apagar consulta! Código não encontrado ou ela já foi excluída.");
                    Gson gson = new Gson();
                    String respostaJson = gson.toJson(resposta);
                    saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                }
            }else{
                resposta = new Protocolo(TIPO_RESPOSTA, OP_EXCLUIR_AGENDAMENTO_DONO, contadorIdMensagem, idSessao,
                        "Cadastro não encontrado!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            }

            }
            return resposta.getMensagemResposta();
        }


    public String inserirHorario(int codOperacao, TabelaAgenda parametros, SSLSocket socket,
                                int contadorIdMensagem, Colaborador colaborador) throws IOException {
        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 5) {

            JsonColaboradores jsonCol = new JsonColaboradores();
            String idSessao = jsonCol.verificarLoginSenhaThread(colaborador.getLogin(), colaborador.getSenha(),
                    "colaboradores.json");
            if(idSessao != null){
               boolean ok = parametros.cadastrarHorarioThread(parametros, socket);
               if(ok){
                    resposta = new Protocolo(TIPO_RESPOSTA, OP_INSERIR_HORARIO, contadorIdMensagem, idSessao,
                            "Horário inserido com sucesso!");
                    Gson gson = new Gson();
                    String respostaJson = gson.toJson(resposta);
                    saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                }else {
                   resposta = new Protocolo(TIPO_RESPOSTA, OP_INSERIR_HORARIO, contadorIdMensagem, idSessao,
                           "Erro ao inserir horário! Ele já existe no sistema.");
                   Gson gson = new Gson();
                   String respostaJson = gson.toJson(resposta);
                   saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
               }
            }
        }
        return resposta.getMensagemResposta();
    }

    public String apagarHorario(int codOperacao, TabelaAgenda parametros, SSLSocket socket, int contadorIdMensagem,
                                Colaborador colaborador) throws IOException {

        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 6) {

            JsonColaboradores jsonCol = new JsonColaboradores();
            String idSessao = jsonCol.verificarLoginSenhaThread(colaborador.getLogin(), colaborador.getSenha(),
                    "colaboradores.json");
            if(idSessao != null){
                boolean ok = parametros.apagaHorario(parametros, socket);
                if(ok){
                    resposta = new Protocolo(TIPO_RESPOSTA, OP_APAGAR_HORARIO, contadorIdMensagem, idSessao,
                            "Horário excluído com sucesso!");
                    Gson gson = new Gson();
                    String respostaJson = gson.toJson(resposta);
                    saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
            }else {
                    resposta = new Protocolo(TIPO_RESPOSTA, OP_APAGAR_HORARIO, contadorIdMensagem, idSessao,
                            "Erro ao apagar o horário inserido!");
                    Gson gson = new Gson();
                    String respostaJson = gson.toJson(resposta);
                    saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                }
            }
        }
        return resposta.getMensagemResposta();
    }
    public String cadastrarColaborador(int codOperacao, Colaborador parametros, SSLSocket socket, int contadorIdMensagem)
            throws IOException {

        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 7) {
            JsonColaboradores jsonColaboradores = new JsonColaboradores();
            String idSessao = "Inválido";
            if (parametros.cadastrar(parametros, socket)) {
                idSessao = jsonColaboradores.adicionaColaboradorAoJson(parametros, "colaboradores.json");
                resposta = new Protocolo(TIPO_RESPOSTA, OP_CADASTRAR_COLABORADOR, contadorIdMensagem, idSessao,
                        "Colaborador cadastrado no sistema!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);
                resposta.getMensagemResposta();
            } else {
                resposta = new Protocolo(TIPO_RESPOSTA, OP_CADASTRAR_COLABORADOR, contadorIdMensagem, idSessao,
                        "O login já pertence a outro usuário. Tente novamente.");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);
                saidaCliente.println("Resposta enviada pelo servidor: " + respostaJson);

            }
        }
        return resposta.getMensagemResposta();

    }

    public String atualizarColaborador(int codOperacao, Colaborador parametros, SSLSocket socket, int contadorIdMensagem)
            throws IOException {

        PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
        Protocolo resposta = null;

        if (codOperacao == 8) {
            JsonColaboradores col = new JsonColaboradores();
            String idSessao = col.atualizarDadosDoColaborador(parametros, socket);

            if (idSessao != null) {
                resposta = new Protocolo(TIPO_RESPOSTA, OP_ATUALIZAR_COLABORADOR, contadorIdMensagem, idSessao,
                        "Os dados do colaborador foram atualizados!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);

                saidaCliente.println(respostaJson);
            } else {
                resposta = new Protocolo(TIPO_RESPOSTA, OP_ATUALIZAR_COLABORADOR, contadorIdMensagem, idSessao,
                        "Cadastro não existe na base de dados!");
                Gson gson = new Gson();
                String respostaJson = gson.toJson(resposta);

                saidaCliente.println(respostaJson);
            }

        }
        return resposta.getMensagemResposta();
    }
}



