package sandbox.acroforms.reporting;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import sandbox.WrapToTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@WrapToTest(compareRenders = true)
public class FillFlattenMerge3 {

    public static final String SRC = "resources/pdfs/state.pdf";
    public static final String DEST = "results/acroforms/reporting/united_states_3.pdf";
    public static final String DATA = "resources/data/united_states.csv";
    public static final String[] FIELDS = {
        "name", "abbr", "capital", "city", "population", "surface", "timezone1", "timezone2", "dst"
    };
    public static final Font FONT = new Font(FontFamily.HELVETICA, 10);
    
    protected Map<String, Rectangle> positions;
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFlattenMerge3().manipulatePdf(SRC, DEST);
    }

    public class Background extends PdfPageEventHelper {

        PdfImportedPage background;
        
        public Background(PdfImportedPage background) {
            this.background = background;
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContentUnder();
            cb.addTemplate(background, 0, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase("page " + writer.getPageNumber()), 550, 800, 0);
        }
        
    }
    
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        AcroFields form = reader.getAcroFields();
        positions = new HashMap<String, Rectangle>();
        Rectangle rectangle;
        Map<String, AcroFields.Item> fields = form.getFields();
        for (String name : fields.keySet()) {
            rectangle = form.getFieldPositions(name).get(0).position;
            positions.put(name, rectangle);
        }
        
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        writer.setPageEvent(new Background(writer.getImportedPage(reader, 1)));
        document.open();

        PdfContentByte cb = writer.getDirectContent();
        StringTokenizer tokenizer;
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            int i = 0;
            tokenizer = new StringTokenizer(line, ";");
            while (tokenizer.hasMoreTokens()) {
                process(cb, FIELDS[i++], tokenizer.nextToken());
            }
            document.newPage();
        }
        br.close();
        document.close();
        
        reader.close();
    }
    
    protected void process(PdfContentByte cb, String name, String value) throws DocumentException {
        Rectangle rect = positions.get(name);
        Phrase p = new Phrase(value, FONT);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                p, rect.getLeft() + 2, rect.getBottom() + 2, 0);
    }
}