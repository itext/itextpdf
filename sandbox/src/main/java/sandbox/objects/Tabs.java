/**
 * Example written by Michaël Demey to demonstrate the use of the new Tab functionality.
 *
 * The code adds three paragraphs:
 * 1. Without a tab
 * 2. With a leading tab
 * 3. With an inline tab
 */
package sandbox.objects;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import sandbox.WrapToTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@WrapToTest
public class Tabs {

    public static final String DEST = "results/objects/tabs.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Tabs().createPdf(DEST);
    }

    public void createPdf(String dest) throws FileNotFoundException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();

        Paragraph p = new Paragraph("Hello World.");

        document.add(p);

        p = new Paragraph();
        p.setTabSettings(new TabSettings(56f));
        p.add(Chunk.TABBING);
        p.add(new Chunk("Hello World with tab."));

        document.add(p);

        p = new Paragraph();
        p.add(new Chunk("Hello World with"));
        p.setTabSettings(new TabSettings(56f));
        p.add(Chunk.TABBING);
        p.add(new Chunk("an inline tab."));

        document.add(p);

        document.close();
    }

}