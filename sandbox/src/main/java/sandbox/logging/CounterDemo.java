package sandbox.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class CounterDemo {

    public static final String HELLO = "results/logging/hello.pdf";
    public static final String HELLO2 = "results/logging/hello2.pdf";
    protected MyCounter counter;
    
    public class MyCounter implements Counter {

        public static final String LOG = "results/logging/counter.txt";
        protected String name;
        protected FileWriter writer;
        
        private MyCounter(Class<?> klass) throws IOException {
            this.name = klass.getName();
            writer = new FileWriter(LOG, true);
        }
        
        public Counter getCounter(Class<?> klass) {
            try {
                return new MyCounter(klass);
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }

        public void read(long l) {
            if (writer == null)
                throw new RuntimeException("No writer defined!");
            try {
                writer.write(String.format("[%s] %s: %s read\n", name, new Date().toString(), l));
                writer.flush();
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }

        public void written(long l) {
            if (writer == null)
                throw new RuntimeException("No writer defined!");
            try {
                writer.write(String.format("[%s] %s: %s written\n", name, new Date().toString(), l));
                writer.flush();
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
        
        public void close() throws IOException {
            writer.close();
        }
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(HELLO);
        file.getParentFile().mkdirs();
        CounterDemo app = new CounterDemo();
        app.initCounter();
        app.createPdf(HELLO);
        app.manipulatePdf(HELLO, HELLO2);
        app.closeCounter();
    }

    public void initCounter() throws IOException {
        counter = new MyCounter(getClass());
        CounterFactory.getInstance().setCounter(counter);
    }

    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
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

    public void closeCounter() throws IOException {
        counter.close();
    }
}
