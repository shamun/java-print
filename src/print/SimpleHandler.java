package print;

import java.awt.print.PrinterException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.net.*;

import java.io.File;

public class SimpleHandler implements Runnable {

  private Socket sock = null;
  private DataInputStream sockInput = null;
  private DataOutputStream sockOutput = null;
  private Thread myThread = null;

  public SimpleHandler(Socket sock) throws IOException {
    this.sock = sock;
    DataInputStream sockInput = new DataInputStream(sock.getInputStream());
    DataOutputStream sockOutput = new DataOutputStream(sock.getOutputStream());
    this.myThread = new Thread(this);
  }

  public void start() {
    myThread.start();
  }

  public void run() {
    while (true) {

      try {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
        String get = in.readLine();
        System.out.println("DATA>>> : " + get);
        if (get.length() > 3) {
          if (get.equals("exit")) {
            System.exit(0);
          } else {
            this.print(get);
          }
        }
        //sockOutput.write(get); 
        sockOutput.flush();
      } catch (Exception e) {
        e.printStackTrace(System.err);
        break;
      }
    }

    try {
      System.err.println("server: close");
      sock.close();
    } catch (Exception e) {
      System.err.println("server: Exception, e=" + e);
      e.printStackTrace(System.err);
    }

  }

  public void print(String infile) throws Exception {
    String status = null;
    PDDocument doc = null;
    File file = null;
    try {
      file = new File("c:\\" + infile);
      doc = PDDocument.load(file);
      doc.silentPrint();

      status = ">>> Silent print";
    } finally {
      if (doc != null) {
        doc.close();
        if (file.exists()) {
          file.delete();
          status += " and deleted";
        } else {
          status += " and not deleted because not exists";
        }
      } else {
        status += " and doc != null";
      }
    }


    System.out.println(infile + " : " + status);
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter("c:\\printlog.log",true));
//            out.write(infile + " : " + status );
//            out.close();
//        } catch (IOException e) {
//        }        

  }
}
