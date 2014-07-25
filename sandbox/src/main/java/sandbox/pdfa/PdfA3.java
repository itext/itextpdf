package sandbox.pdfa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPTable;

@WrapToTest
public class PdfA3 {
    public static final String DEST = "results/pdfa/pdf_a.pdf";
    public static final String DATA = "resources/data/united_states.csv";
    public static final String FONT = "resources/fonts/OpenSans-Regular.ttf";
    public static final String BOLD = "resources/fonts/OpenSans-Bold.ttf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfA3().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Font font = new Font(BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
        Font bold = new Font(BaseFont.createFont(BOLD, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
        Document document = new Document(PageSize.A4.rotate());
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(dest), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();
        File file = new File("resources/data/sRGB_CS_profile.icm");
        ICC_Profile icc = ICC_Profile
                .getInstance(new FileInputStream(file));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fileSpec = PdfFileSpecification.fileEmbedded(
                writer, DATA,
                "united_states.csv", null, "text/csv", parameters, 0);
        fileSpec.put(new PdfName("AFRelationship"), new PdfName("Data"));
        writer.addFileAttachment("united_states.csv", fileSpec);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getReference());
        writer.getExtraCatalog().put(new PdfName("AF"), array);
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        process(table, line, bold);
        table.setHeaderRows(1);
        while ((line = br.readLine()) != null) {
            process(table, line, font);
        }
        br.close();
        document.add(table);
        document.close();
    }
    
    public void process(PdfPTable table, String line, Font font) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            table.addCell(new Phrase(tokenizer.nextToken(), font));
        }
    }
}