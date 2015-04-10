/*
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26670919/itextsharp-diacritic-chars
 */
package sandbox.parse;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Bruno Lowagie (iText Software)
 */
public class ParseCzech {
    
    public static final String SRC = "resources/pdfs/czech.pdf";
    public static final String DEST = "results/parse/czech.txt";
            
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ParseCzech().parse(SRC);
    }
    
    
    public void parse(String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        FileOutputStream fos = new FileOutputStream(DEST);
        for (int page = 1; page <= 1; page++) {
            fos.write(PdfTextExtractor.getTextFromPage(reader, page).getBytes("UTF-8"));
        }
        fos.flush();
        fos.close();
    }
}
