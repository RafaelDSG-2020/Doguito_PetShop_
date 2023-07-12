
package br.com.doguitopetshop.sub02;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.*;

public class ServidorExcluirConsultaParteDono {

    private static ExecutorService threadPool;
    private SSLServerSocket servidor;
    private boolean estaRodando;
    private KeyStore keyStore;
    private KeyManagerFactory kmf;
    private SSLContext sslContext;
    private String[] cifra;
    private SSLParameters sslParameters;
    private static final String KEYSTORE_PATH = "/home/rafael/IdeaProjects/Doguito Petshop Final/src/br/com/doguitopetshop/" +
            "Seguranca/keystore.jks";
    private static final String KEYSTORE_PASSWORD = "890023";

    public ServidorExcluirConsultaParteDono() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        System.out.println("---- Iniciando Servidor - [EXCLUIR PARTE DONO - 7004] -----");
        this.keyStore = KeyStore.getInstance("JKS");
        this.keyStore.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());

        this.kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        this.kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        this.sslContext = SSLContext.getInstance("TLS");
        //this.sslContext.init(kmf.getKeyManagers(), null, null);

        this.cifra = new String[]{"TLS_AES_256_GCM_SHA384"};
        this.sslParameters = new SSLParameters(cifra, null);
        this.sslContext.init(kmf.getKeyManagers(), null, null);


        this.servidor = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(7004);
        // Configurar as cifras suportadas pelo servidor
        servidor.setEnabledCipherSuites(cifra);
        this.threadPool = Executors.newCachedThreadPool();
        this.estaRodando = true;
    }

    public void rodar() throws IOException {
        int contadorIdMensagem = 0;
        while (this.estaRodando) {
            try {
                SSLSocket socket = (SSLSocket) servidor.accept();
                contadorIdMensagem++;
                System.out.println("Aceitando novo cliente na porta " + socket.getPort());
                ThreadExcluirConsultaParteDono threadExcluirConsultaParteDono = new
                        ThreadExcluirConsultaParteDono(threadPool, socket, this, contadorIdMensagem);
                threadPool.execute(threadExcluirConsultaParteDono);
            } catch (SocketException e) {
                System.out.println("SocketException, Está rodando? " + this.estaRodando);

            }
        }
    }

    public void parar() throws IOException {
        estaRodando = false;
        servidor.close();
        threadPool.shutdown(); // Espera todos os clientes terminar
    }

    public static void main (String[]args) throws Exception {
        ServidorExcluirConsultaParteDono servidor = new ServidorExcluirConsultaParteDono();
        servidor.rodar();
        servidor.parar();
        threadPool.shutdown();

    }
}
