
package print;
// import java.awt.print.PrinterException;
import org.apache.pdfbox.pdmodel.PDDocument;
// import org.apache.pdfbox.pdmodel.PDPage;
// import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
// import org.apache.pdfbox.pdmodel.font.PDType1Font;
// import org.apache.pdfbox.pdmodel.font.PDFont;
import java.io.File;

public class Print {

  public static void main(String[] args) throws Exception {
    
    PDDocument doc = null;
    File file = null;
    
    try {
      file = new File("c:\\" + args[0]);
      doc = PDDocument.load(file);
      doc.silentPrint();
      
    } finally {
      if (doc != null) {
        doc.close();
        //if (file.exists()) {
          //file.delete();
        //}
      }
    }
  }
  
}
