
package br.com.doguitopetshop;

import br.com.doguitopetshop.json.JsonAgenda;
import br.com.doguitopetshop.json.JsonColaboradores;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class TabelaAgenda {
    private String data;
    private String hora;
    private String idDaConsulta;
    private String nomeDoVeterinario;
    private String matricula;

    // Advindos dos dados do Dono para agendamento
    private String nomeDono;
    private String nomePet;
    private String cpfDono;

    private String idDono;

    public TabelaAgenda(String data, String hora, String nomeDoVeterinario, String matricula) {
        this.data = data;
        this.hora = hora;
        this.nomeDoVeterinario = nomeDoVeterinario;
        this.matricula = matricula;
    }

    public TabelaAgenda() {
    }

    public TabelaAgenda(String idDaConsulta) {
        this.idDaConsulta = idDaConsulta;
    }

    public String getIdDono() {
        return idDono;
    }

    public void setIdDono(String idDono) {
        this.idDono = idDono;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getNomePet() {
        return nomePet;

    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public String getCpfDono() {
        return cpfDono;
    }

    public void setCpfDono(String cpfDono) {
        this.cpfDono = cpfDono;
    }

    public String getNomeDoVeterinario() {
        return nomeDoVeterinario;
    }

    public void setNomeDoVeterinario(String nomeDoVeterinario) {
        this.nomeDoVeterinario = nomeDoVeterinario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdDaConsulta() {
        return idDaConsulta;
    }

    public void setIdDaConsulta() {
        boolean idRepetido;
        do {
            int randomID = 1000 + new Random().nextInt(9001); // Gera um número aleatório entre 1000 e 10000
            String newId = String.valueOf(randomID);
            JsonAgenda agenda = new JsonAgenda();
            idRepetido = agenda.verificarIdRepetido(newId, "agenda.json");
            if (!idRepetido) {
                this.idDaConsulta = newId;
            }
        } while (idRepetido);
    }

    public boolean cadastrarHorarioThread(TabelaAgenda tab, SSLSocket socket) throws IOException {
        JsonColaboradores j = new JsonColaboradores();

        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        String data = tab.getData();
        String hora = tab.getHora();
        String nomeVeterinario = tab.getNomeDoVeterinario();
        String matricula = tab.getMatricula();
        tab.setIdDaConsulta();

        if (j.procurarColaborador(nomeVeterinario, matricula, "colaboradores.json", socket)) {
            JsonAgenda a = new JsonAgenda();
            String caminho = "agenda.json";

            JsonAgenda verSeTem = new JsonAgenda();
            if (!verSeTem.verSeJaExisteHorario(data, hora, nomeVeterinario, matricula, socket)) {
                printSaida.println("========== CONFERIR HORÁRIO ========== ");
                printSaida.println("Data: " + tab.getData());
                printSaida.println("Hora:" + tab.getHora());
                printSaida.println("Veterinário: " + tab.getNomeDoVeterinario());
                printSaida.println("Matrícula do veterinário: " + tab.getMatricula());

                boolean ok = a.adicionaHorarioAoJson(tab, caminho);
                return ok; // Retorna o valor de 'ok' (verdadeiro ou falso)
            }
        }
        return false; // Retorna falso caso alguma condição não seja atendida
    }

    public boolean apagaHorario(TabelaAgenda tab, SSLSocket socket) throws IOException {
        OutputStream saidaCliente = socket.getOutputStream();
        PrintWriter printSaida = new PrintWriter(saidaCliente, true);

        String data = tab.getData();
        String hora = tab.getHora();
        String nomeVeterinario = tab.getNomeDoVeterinario();
        String matricula = tab.getMatricula();

        JsonAgenda verSeTem = new JsonAgenda();
        if (verSeTem.verSeJaExisteHorario(data, hora, nomeVeterinario, matricula, socket)) {
            TabelaAgenda ret = verSeTem.verSeJaExisteHorarioRetorna(data, hora, nomeVeterinario, matricula, socket);
            printSaida.println("========== HORARIO ENCONTRADO ========== ");
            printSaida.println("Data: " + tab.getData());
            printSaida.println("Hora: " + tab.getHora());
            printSaida.println("Veterinário: " + tab.getNomeDoVeterinario());
            printSaida.println("Matrícula do veterinário: " + tab.getMatricula());
            printSaida.println("Nome do dono: " + ret.getNomeDono());
            printSaida.println("Nome do pet: " + ret.getNomePet());
            printSaida.println("CPF do dono: " + ret.getCpfDono());

            if (ret.getNomePet() == null && ret.getNomeDono() == null && ret.getCpfDono() == null) {
                JsonAgenda ag = new JsonAgenda();
                ag.apagaHorarioMarcado(data, hora, nomeVeterinario, matricula, socket);
                return true; // Retornar verdadeiro quando o horário for excluído com sucesso
            } else {
                printSaida.println("Um cliente marcou o horário. Entre em contato com ele!");
            }
        } else {
            printSaida.println("Não existe esse horário no sistema!");
        }

        return false; // Retornar falso nas situações em que não for possível excluir o horário
    }

}
