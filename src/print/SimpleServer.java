package print;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class SimpleServer 
{
    private int serverPort = 0;
    private ServerSocket serverSock = null;
    private Socket sock = null;

    public SimpleServer(int serverPort) throws IOException 
    {
        this.serverPort = serverPort;
        serverSock = new ServerSocket(serverPort);
    }


    
    public void waitForConnections() {
        while (true) {
            try {
                sock = serverSock.accept();
                System.err.println("server: open");
                SimpleHandler handler = new SimpleHandler(sock);
                handler.start();
            }
            catch (IOException e){
                e.printStackTrace(System.err);
            }
        }
    }

    public static void main(String argv[]) {
        int port = 8081;

        SimpleServer server = null;
        try {
            server = new SimpleServer(port);
        }
        catch (IOException e){
            e.printStackTrace(System.err);
        }
        server.waitForConnections();
    }
}
