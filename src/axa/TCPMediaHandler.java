package axa;

import java.awt.print.PrinterException;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

public class TCPMediaHandler implements Runnable {
  
  private Socket sock = null;
  private InputStream sockInput = null;
  private OutputStream sockOutput = null;
  private Thread myThread = null;
  private String newline = "\r\n";
  public static String data = "";
  
  public TCPMediaHandler(Socket sock) throws IOException {
    sock.setReceiveBufferSize(10 * 1024 + 1024);
    sock.setSendBufferSize(10 * 1024 + 1024);
    this.sock = sock;
    sockInput = sock.getInputStream();
    sockOutput = sock.getOutputStream();
    this.myThread = new Thread(this);
  }

  public void start() {
    myThread.start();
  }

  public void quickclose() throws IOException {
    sockOutput.flush();
    sock.setSoTimeout(100);
  }

  public void slowclose1() throws IOException {
    System.out.println("[TCPMediaHandler]: socket flush, timeout 900");
    sockOutput.flush();
    sock.setSoTimeout(10);
  }

  public void slowclose() throws IOException {
    System.out.println("[TCPMediaHandler]: socket flush, timeout 10");
    sockOutput.flush();
    sock.setSoTimeout(10);
  }

  public void run() {
    while (true) {
      byte[] buf = new byte[300];
      int bytes_read = 0;
      try {
        bytes_read = sockInput.read(buf, 0, buf.length);        
        data += new String(buf, 0, bytes_read);
        System.out.println("[TCPMediaHandler]: incomeing data: " +  bytes_read + " bytes, data=" +data);        
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
    
    /* ISSUES: do not catch correctly the packets
     echo -n "vlc;en.flv;end" | nc localhost 58891 */
    if (data.contains("\n")) {
      System.out.println("Newline character found, replacing. " + data);
      data = data.replace("\n", "");
      System.out.println("Newline removed, replacing. " + data);
      //System.exit(0);
    } else {
      System.out.println("continue ...");
    }
    
    try {
      if (data.endsWith("end")) {
        if (data.startsWith("print")) {
          printNow("");
          sockOutput.write("[s]: print;end".getBytes());
          slowclose();   
        } else if (data.startsWith("rtf")) {
          printRTFNow("");
          sockOutput.write("[s]: rtf;end".getBytes());
          slowclose();             
        } else if (data.startsWith("many")) {                      
          String packet[] = data.split(";"); 
          System.out.println("[debug]: lenght " +  packet[1].length() );            
          sockOutput.write(packet[1].getBytes());          
          slowclose();
        } else if (data.startsWith("exit")) {          
          System.exit(0);
          sockOutput.write("stop".getBytes());
          slowclose();
        } else {
          System.out.println("[TCPMediaHandler]: !!!!!!! no match!!! !!!!!! ");
        }
      } else {
        System.out.println("[TCPMediaHandler]: !!!!!!! no end found at the end !!!!!! ");
      }
    } catch (Exception a) {
      a.printStackTrace();
    }        
    System.out.println("[TCPMediaHandler]:>>>>>>>>>>>" + data);
    data = "";
    

    try {
      System.out.println("[TCPMediaHandler]: Closing socket.");
      sock.close();
    } catch (Exception e) {
      System.err.println("Exception while closing socket, e=" + e);
      e.printStackTrace();
    }
  }
  
  /* TCP Bytes */
  public static String sendTCPBytes(String bytes, String[] ip) throws IOException {
    String downloaded = null;
    Socket socket = new Socket(ip[2], 58888);
    socket.setReceiveBufferSize(10 * 1024 + 1024);
    socket.setSendBufferSize(10 * 1024 + 1024);
    DataOutputStream upload = new DataOutputStream(socket.getOutputStream());
    BufferedReader download = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String caps = bytes;
    System.out.println(caps);
    upload.writeBytes(caps);
    upload.flush();
    String get;
    downloaded = download.readLine();
    System.out.println("[TCPMediaHandler]: >>>>>> " + downloaded);
    socket.close();
    return downloaded;
  }

  public static void printRTFNow(String userInput) throws IOException, PrinterException {
    PDDocument doc = null;
    File file = null;
    
    try {
      if (userInput.isEmpty()) {
        file = new File("c:\\doc.rtf");
        System.out.println("c:\\doc.rtf");
      } else {
        file = new File("c:\\doc.rtf");
        System.out.println("c:\\doc.rtf");        
      }
      doc = PDDocument.load(file);
      doc.silentPrint();
      
    } finally {
      if (doc != null) {
        doc.close();
      }
    }    
  }
  
  public static void printNow(String userInput) throws IOException, PrinterException {
    PDDocument doc = null;
    File file = null;
    
    try {
      if (userInput.isEmpty()) {
        file = new File("c:\\emptyfile.pdf");
        System.out.println("c:\\emptyfile.pdf");
      } else {
        file = new File(userInput);
        System.out.println(userInput);
      }
      doc = PDDocument.load(file);
      doc.silentPrint();
      
    } finally {
      if (doc != null) {
        doc.close();
      }
    }    
  }
  
}
