/**
 * Created by Rafael on 15/09/2016.
 */

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SistemaArquivos implements Runnable {
    private OutputStream resposta;
    private InputStream mensagem;
    private Database server;

    public SistemaArquivos(InputStream msg, OutputStream resp, Database db) {
        mensagem = msg;
        resposta = resp;
        server = db;
    }

    public void run() {
        try {

            BufferedWriter respostaCliente = new BufferedWriter(new OutputStreamWriter(resposta));
            BufferedReader in = new BufferedReader(new InputStreamReader(mensagem));
            String[] linhaDeRequisicao;
            String linha = "";
            String mensagemDeCorpo = " ";
            int codigoResposta = 400;
            String mensagemDeResposta = " ";
            String cabecalho = "";
            int length = 0;

            linha = in.readLine();
            linhaDeRequisicao = linha.split(" ");

            if ("POST".equals(linhaDeRequisicao[0]) || "PUT".equals(linhaDeRequisicao[0])) {
                while ((linha = in.readLine()) != null) {

                    if (linha.isEmpty()) {
                        break;
                    }
                    if (linha.startsWith("Content-Length: ")) {
                        int index = linha.indexOf(':') + 1;
                        String len = linha.substring(index).trim();
                        length = Integer.parseInt(len);
                    }
                }
                int read;
                while ((read = in.read()) != -1) {

                    mensagemDeCorpo = mensagemDeCorpo + ((char) read);
                    if (mensagemDeCorpo.length() == length + 1) {
                        break;
                    }
                }
            }

            Resposta saida = new Resposta();

            switch (linhaDeRequisicao[0]) {
                case "GET":
                    mensagemDeCorpo = server.GetRequest(linhaDeRequisicao[1]) + "\r\n";
                    codigoResposta = server.responseCode;
                    cabecalho = server.metadataCreation + "\r\n" + server.metadataModification + "\r\n" + server.metadataVersion + "\r\n" + server.lengthStatement + "\r\n";
                    mensagemDeResposta = saida.resp(codigoResposta);
                    break;
                case "POST":
                    Thread.sleep(10*1000);
                    mensagemDeCorpo = server.PostRequest(linhaDeRequisicao[1], mensagemDeCorpo) + "\r\n";
                    codigoResposta = server.responseCode;
                    cabecalho = server.metadataCreation + "\r\n" + server.metadataModification + "\r\n" + server.metadataVersion + "\r\n" + server.lengthStatement + "\r\n";
                    mensagemDeResposta = saida.resp(codigoResposta);
//                    System.out.print(codigoResposta);

                    break;
                case "DELETE":
                    mensagemDeCorpo = server.DeleteRequest(linhaDeRequisicao[1]) + "\r\n";
                    codigoResposta = server.responseCode;
                    cabecalho = "No Response\r\n";
                    mensagemDeResposta = saida.resp(codigoResposta);

                    break;
                case "PUT":
                    mensagemDeCorpo = server.PutRequest(linhaDeRequisicao[1], mensagemDeCorpo) + "\r\n";
                    codigoResposta = server.responseCode;
                    cabecalho = server.metadataCreation + "\r\n" + server.metadataModification + "\r\n" + server.metadataVersion + "\r\n" + server.lengthStatement + "\r\n";
                    mensagemDeResposta = saida.resp(codigoResposta);

                    break;
                case "HEAD":
                    mensagemDeCorpo = server.HeadRequest(linhaDeRequisicao[1]) + "\r\n";
                    codigoResposta = server.responseCode;
                    cabecalho = server.metadataCreation + "\r\n" + server.metadataModification + "\r\n" + server.metadataVersion + "\r\n" + server.lengthStatement + "\r\n";
                    mensagemDeResposta = saida.resp(codigoResposta);

                    break;
                default:
                    mensagemDeResposta = saida.resp(codigoResposta);
            }

            String linhaRequisicaoResp = linhaDeRequisicao[2] + " " + codigoResposta + " " + mensagemDeResposta + "\r\n";
            String mensagemResposta = linhaRequisicaoResp + cabecalho + "\n" + mensagemDeCorpo;

            respostaCliente.write(mensagemResposta);
            System.out.println(mensagemResposta);
            respostaCliente.flush();

            mensagem.close();
            in.close();
            resposta.close();

        } catch (IOException e) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, e);
       }
 catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
