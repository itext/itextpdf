package sandbox.acroforms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class MergeForms {

    public static final String SRC1 = "resources/pdfs/subscribe.pdf";
    public static final String SRC2 = "resources/pdfs/state.pdf";
    public static final String DEST = "results/acroforms/merged_form.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeForms().createPdf(DEST);
    }
    
    public void createPdf(String filename, PdfReader[] readers) throws IOException, DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(filename));
        copy.setMergeFields();
        document.open();
        for (PdfReader reader : readers) {
            copy.addDocument(reader);
        }
        document.close();
        for (PdfReader reader : readers) {
            reader.close();
        }
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        PdfReader[] readers = {
            new PdfReader(getFile1()),
            new PdfReader(getFile2())
        };
        createPdf(filename, readers);
    }

    public String getFile1() { return SRC1; }
    public String getFile2() { return SRC2; }
}
