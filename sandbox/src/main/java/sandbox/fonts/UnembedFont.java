/**
 * Example written by Bruno Lowagie in answer to a question by a customer.
 */
package sandbox.fonts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class UnembedFont {

    public static final String SRC = "results/fonts/withSerifFont.pdf";
    public static final String DEST = "results/fonts/withoutSerifFont.pdf";

    public static void main(String[] args) throws DocumentException, IOException {
        UnembedFont app = new UnembedFont();
        app.manipulatePdf(SRC, DEST);
    }
    
    /**
     * Creates a PDF with an embedded font.
     */
    public void createPdf(String file) throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        OutputStream os = new FileOutputStream(file);
        PdfWriter.getInstance(document, os);
        // step 3
        document.open();
        // step 4
        BaseFont bf = BaseFont.createFont("resources/fonts/PT_Serif-Web-Regular.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font f = new Font(bf, 12);
        document.add(new Paragraph("This is a test with Times New Roman.", f));
        // step 5
        document.close();
    }
    
    /**
     * Removes the embedded font
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
	    createPdf(src);
        // we create a reader instance
        PdfReader reader = new PdfReader(src);
        // we loop over all objects
        PdfObject obj;
        for (int i = 1; i < reader.getXrefSize(); i++) {
            obj = reader.getPdfObject(i);
            // we skip all objects that aren't a dictionary
            if (obj == null || !obj.isDictionary())
                continue;
            // we process all dictionaries
            unembedTTF((PdfDictionary)obj);
        }
        // removing unused objects will remove unused font file streams
        reader.removeUnusedObjects();
        // we persist the altered document
        OutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = new PdfStamper(reader, os);
        stamper.close();
    }
    
    /**
     * Processes a dictionary.
     * In case of font dictionaries, the dictionary is processed.
     */
    public void unembedTTF(PdfDictionary dict) {
        // we ignore all dictionaries that aren't font dictionaries
        if (!dict.isFont())
            return;
        // we only remove TTF fonts
        if (dict.getAsDict(PdfName.FONTFILE2) != null) {
            return;
        }
        // check if a subset was used (in which case we remove the prefix)
        PdfName baseFont = dict.getAsName(PdfName.BASEFONT);
        if (baseFont.getBytes()[7] == '+') {
            baseFont = new PdfName(baseFont.toString().substring(8));
            dict.put(PdfName.BASEFONT, baseFont);
        }
        // we check if there's a font descriptor
        PdfDictionary fontDescriptor = dict.getAsDict(PdfName.FONTDESCRIPTOR);
        if (fontDescriptor == null)
            return;
        // is there is, we replace the fontname and remove the font file
        fontDescriptor.put(PdfName.FONTNAME, baseFont);
        fontDescriptor.remove(PdfName.FONTFILE2);
    }
}