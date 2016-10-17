import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Rafael on 15/09/2016.
 */
public class Servidor {



    public static int porta = 5000;
    public static void main (String[] args) throws IOException {

//        for (String arg : args){
//            if(arg.contains("Porta:")){
//                String[] argPorta = arg.split(":");
//                porta = Integer.parseInt(argPorta[1]);
//            }
//        }
        Servidor x = new Servidor();
        x.StartDbServer(porta);
    }

    public void StartDbServer(int porta)throws IOException {

        ServerSocket socket = new ServerSocket(porta);

        while(true){
            Socket acceptance = socket.accept();
            Database database = new Database("1","Primeiro no");
            SistemaArquivos responder = new SistemaArquivos(acceptance.getInputStream(), acceptance.getOutputStream(), database);
            Thread x = new Thread(responder);
            x.start();
        }

    }


}

