
package axa;

import java.awt.print.PrinterException;
import print.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Print {
  
  private int serverPort = 0;
  private ServerSocket serverSock = null;
  private Socket sock = null; 
  
  public Print() throws Exception {
    serverPort = 58888;    
    serverSock = new ServerSocket();
    serverSock.setReuseAddress(true);
    serverSock.bind(new InetSocketAddress(serverPort));
    System.out.println("[TCPMediaHandler]: Server started");    
  }
  
  public void startup() {
    System.out.println("start");
    waitForConnections();
  }

  public void waitForConnections() {
    while (true) {
      try {
        sock = serverSock.accept();
        System.out.println("[TCPMediaHandler]: Accepted new socket");
        TCPMediaHandler handler = new TCPMediaHandler(sock);
        handler.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }  
//  
//  public static void printNow(String userInput) throws IOException, PrinterException {
//    PDDocument doc = null;
//    File file = null;
//    
//    try {
//      if (userInput.isEmpty()) {
//        file = new File("c:\\emptyfile.pdf");
//      } else {
//        file = new File(userInput);
//      }
//      doc = PDDocument.load(file);
//      doc.silentPrint();
//      
//    } finally {
//      if (doc != null) {
//        doc.close();
//      }
//    }    
//  }
  
  public static void main(String[] args) throws Exception {
    Print a = new Print();
    a.startup();    
  }
  
}
