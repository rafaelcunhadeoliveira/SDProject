/**
 * Created by Rafael on 15/09/2016.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class SistemaArquivos {

    public static int porta = 5000;
    public static void main (String[] args) throws IOException {

//        for (String arg : args){
//            if(arg.contains("Porta:")){
//                String[] argPorta = arg.split(":");
//                porta = Integer.parseInt(argPorta[1]);
//            }
//        }
        while(true) {
            SistemaArquivos x = new SistemaArquivos();
            x.run(porta);
        }
    }
    public void run(int porta) throws IOException {

        ServerSocket socket = new ServerSocket(porta);
        Servidor server = new Servidor("1", "Primeiro no");
        int tamanhoMsg = 0;


        while(true) {

            Socket cliente = socket.accept();
            BufferedWriter respostaCliente = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
            InputStream is = cliente.getInputStream();
            DataInputStream recebeRequisicao = new DataInputStream(is);

            String[] linhaDeRequisicao = new String[3];
            String linha = "";
            String mensagemDeCorpo = " ";
            int codigoResposta = 400;
            String mensagemDeResposta = " ";
            String cabecalho = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            int length = 0;
            linha = in.readLine();
            linhaDeRequisicao = linha.split(" ");

            if("POST".equals(linhaDeRequisicao[0]) || "PUT".equals(linhaDeRequisicao[0])) {
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

                    mensagemDeCorpo = mensagemDeCorpo+((char) read);
                    if (mensagemDeCorpo.length() == length + 1){
                        break;
                    }
                }
            }

            Resposta saida = new Resposta();

                switch (linhaDeRequisicao[0]) {
                    case "GET":
                        mensagemDeCorpo = server.GetRequest(linhaDeRequisicao[1])+ "\r\n";
                        codigoResposta = server.responseCode;
                        cabecalho = server.metadataCreation+"\r\n"+server.metadataModification+"\r\n"+server.metadataVersion+"\r\n"+server.lengthStatement+"\r\n";
                        mensagemDeResposta = saida.resp(codigoResposta);
                        break;
                    case "POST":
                        mensagemDeCorpo = server.PostRequest(linhaDeRequisicao[1], mensagemDeCorpo)+ "\r\n";
                        codigoResposta = server.responseCode;
                        cabecalho = server.metadataCreation+"\r\n"+server.metadataModification+"\r\n"+server.metadataVersion+"\r\n"+server.lengthStatement+"\r\n";
                        mensagemDeResposta = saida.resp(codigoResposta);
                        break;
                    case "DELETE":
                        mensagemDeCorpo = server.DeleteRequest(linhaDeRequisicao[1])+ "\r\n";
                        codigoResposta = server.responseCode;
                        cabecalho = "No Response\r\n";
                        mensagemDeResposta = saida.resp(codigoResposta);
                        break;
                    case "PUT":
                        mensagemDeCorpo = server.PutRequest(linhaDeRequisicao[1], mensagemDeCorpo)+ "\r\n";
                        codigoResposta = server.responseCode;
                        cabecalho = server.metadataCreation+"\r\n"+server.metadataModification+"\r\n"+server.metadataVersion+"\r\n"+server.lengthStatement+"\r\n";
                        mensagemDeResposta = saida.resp(codigoResposta);
                        break;
                    case "HEAD":
                        mensagemDeCorpo = server.HeadRequest(linhaDeRequisicao[1])+ "\r\n";
                        codigoResposta = server.responseCode;
                        cabecalho = server.metadataCreation+"\r\n"+server.metadataModification+"\r\n"+server.metadataVersion+"\r\n"+server.lengthStatement+"\r\n";
                        mensagemDeResposta = saida.resp(codigoResposta);
                        break;
                    default:
                        mensagemDeResposta = saida.resp(codigoResposta);
                }

            String linhaRequisicaoResp = linhaDeRequisicao[2]+" "+codigoResposta +" "+ mensagemDeResposta + "\r\n";

            String mensagemResposta = linhaRequisicaoResp + cabecalho+ mensagemDeCorpo;
            respostaCliente.write(mensagemResposta);
            respostaCliente.flush();
            cliente.close();

        }


    }
}
