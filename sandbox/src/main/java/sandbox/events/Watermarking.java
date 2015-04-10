/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21575630/adding-watermark-directly-to-the-stream
 * 
 * Adding a watermark to the document immediately using a page event.
 */
package sandbox.events;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import sandbox.WrapToTest;

@WrapToTest
public class Watermarking {

    public static final String DEST = "results/events/united_states.pdf";
    public static final String DATA = "resources/data/united_states.csv";
    public static final Font FONT = new Font();
    public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Watermarking().createPdf(DEST);
    }
    
    public class Watermark extends PdfPageEventHelper {

        protected Phrase watermark = new Phrase("WATERMARK", new Font(FontFamily.HELVETICA, 60, Font.NORMAL, BaseColor.LIGHT_GRAY));
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, 421, 45);
        }
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        writer.setPageEvent(new Watermark());
        document.open();
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 1, 3});
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        process(table, line, BOLD);
        table.setHeaderRows(1);
        while ((line = br.readLine()) != null) {
            process(table, line, FONT);
        }
        br.close();
        document.add(table);
        document.close();
    }
    
    public void process(PdfPTable table, String line, Font font) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        int c = 0;
        while (tokenizer.hasMoreTokens() && c++ < 3) {
            table.addCell(new Phrase(tokenizer.nextToken(), font));
        }
    }
}