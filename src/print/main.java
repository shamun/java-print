package print;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.io.IOException;

public class main extends Window {

  private JLabel label;
  private JButton button;
  private static String mytext = "Loading ";
  private static JLabel loading = new JLabel();

  public main() {
    super(new Frame());
    setLayout(new BorderLayout());
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //setBounds(0,0,1024, 768);
    setBounds(0, 0, 1024, 768);
    setBackground(Color.BLACK);

    button = new JButton("CALL");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });


    java.net.URL imageURL = main.class.getResource("logo.gif");
    ImageIcon logoimage = new ImageIcon(imageURL);
    label = new JLabel(logoimage);

    add(label, BorderLayout.NORTH);

    loading.setForeground(Color.green);
    add(loading, BorderLayout.CENTER);


    // Just for the -layout else needless
    button = new JButton("loading start");
    button.setBackground(Color.black);
    button.setForeground(Color.black);
    button.setBorder(null);
    add(button, BorderLayout.LINE_START);

    button = new JButton("loading end");
    button.setBackground(Color.black);
    button.setForeground(Color.black);
    button.setBorder(null);
    add(button, BorderLayout.PAGE_END);

    button = new JButton("a");
    button.setBackground(Color.black);
    button.setForeground(Color.black);
    button.setBorder(null);
    add(button, BorderLayout.LINE_END);

  }
  private static Timer timer = null;

  public static void main(String[] args) throws InterruptedException {
    new main().setVisible(true);

    //Thread.sleep(70000);    
    timer = new Timer(800, new ActionListener() {
      private int i = 1;
      private String text = ".";

      public void actionPerformed(ActionEvent e) {

        text += "...";
        loading.setText(mytext + text);
        System.out.println(i);
        i++;

        if (i == 70) {
          loading.setText(mytext + text + " [OK]");
          timer.stop();
          try {
            String command = "c:\\esko\\opera\\opera.exe /kioskmode -nocontextmenu -nokeys -nomenu -ScreenWidth 1024 -ScreenHeight 768";
            Process child = Runtime.getRuntime().exec(command);

          } catch (Exception ee) {
          }
          //System.exit(0);
        }
      }
    });
    timer.start();
    //System.exit(0);
    SimpleServer server = null;
    try {
      server = new SimpleServer(8081);
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
    server.waitForConnections();
    System.exit(0);
  }
}
