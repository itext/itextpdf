package sandbox.collections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.collection.PdfCollection;

@WrapToTest
public class PortableCollection {

    public static final String DEST = "results/collections/portable_collection.pdf";
    public static final String DATA = "resources/data/united_states.csv";
    public static final String HELLO = "resources/pdfs/hello.pdf";
    public static final String IMG = "resources/images/berlin2013.jpg";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PortableCollection().createPdf(DEST);
    }
    

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.add(new Paragraph("Portable collection"));
        PdfCollection collection = new PdfCollection(PdfCollection.TILE);
        writer.setCollection(collection);
        PdfFileSpecification fileSpec = PdfFileSpecification.fileEmbedded(
                writer, DATA, "united_states.csv", null);
        writer.addFileAttachment("united_states.csv", fileSpec);
        fileSpec = PdfFileSpecification.fileEmbedded(
                writer, HELLO, "hello.pdf", null);
        writer.addFileAttachment("hello.pdf", fileSpec);
        fileSpec = PdfFileSpecification.fileEmbedded(
                writer, IMG, "berlin2013.jpg", null);
        writer.addFileAttachment("berlin2013.jpg", fileSpec);
        document.close();
    }
}
