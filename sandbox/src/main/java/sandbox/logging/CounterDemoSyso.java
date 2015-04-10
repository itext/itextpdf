package sandbox.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class CounterDemoSyso {

    public static final String HELLO = "results/logging/hello.pdf";
    public static final String HELLO2 = "results/logging/hello2.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        CounterDemoSyso app = new CounterDemoSyso();
        app.initCounter();
        app.createPdf(HELLO);
        app.manipulatePdf(HELLO, HELLO2);
    }

    public void initCounter() throws IOException {
        File file = new File(HELLO);
        file.getParentFile().mkdirs();
        CounterFactory.getInstance().setCounter(new SysoCounter());
    }

    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        document.add(new Paragraph("Hello World!"));
        document.close();
    }
    
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte pagecontent = stamper.getOverContent(1);
        ColumnText.showTextAligned(pagecontent, Element.ALIGN_RIGHT,
                    new Phrase("Stamped text"), 559, 806, 0);
        stamper.close();
        reader.close();
    }
}
