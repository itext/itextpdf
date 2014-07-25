/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20119709/itext-hyphen-in-table-cell
 * 
 * We create a table with and we add a word that needs to be hyphenated.
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.HyphenationAuto;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.hyphenation.Hyphenation;
import com.itextpdf.text.pdf.hyphenation.Hyphenator;

@WrapToTest
public class HyphenationExample {

    public static final String DEST = "results/tables/hyphenation_table.pdf";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HyphenationExample().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Hyphenator h = new Hyphenator("de", "DE", 2, 2);
        Hyphenation s = h.hyphenate("Leistungsscheinziffer"); 
        System.out.println(s);

        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(10);
        Chunk chunk = new Chunk("Leistungsscheinziffer");
        chunk.setHyphenation(new HyphenationAuto("de", "DE", 2,2));
        table.addCell(new Phrase(chunk));
        Phrase phrase = new Phrase();
        phrase.setHyphenation(new HyphenationAuto("de", "DE", 2,2));
        phrase.add(new Chunk("Leistungsscheinziffer"));
        table.addCell(phrase);
        document.add(table);
        document.close();
    }
}
