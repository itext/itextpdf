package book.pdftemplates;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class D02_FillForm {

    public static final String SRC = "resources/pdfs/state.pdf";
    public static final String DEST = "results/pdftemplates/california.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new D02_FillForm().manipulatePdf(SRC, DEST);
    }
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream(dest));
        AcroFields fields = stamper.getAcroFields();
        fields.setField("name", "CALIFORNIA");
        fields.setField("abbr", "CA");
        fields.setField("capital", "Sacramento");
        fields.setField("city", "Los Angeles");
        fields.setField("population", "36,961,664");
        fields.setField("surface", "163,707");
        fields.setField("timezone1", "PT (UTC-8)");
        fields.setField("timezone2", "-");
        fields.setField("dst", "YES");
        stamper.close();
        reader.close();
    }
}
