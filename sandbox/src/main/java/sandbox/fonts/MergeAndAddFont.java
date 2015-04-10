package sandbox.fonts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

public class MergeAndAddFont {

    public static final String FONT = "resources/fonts/GravitasOne.ttf";
    public static final String[] FILE_A = {
        "results/fonts/testA1.pdf", "results/fonts/testA2.pdf", "results/fonts/testA3.pdf"
    };
    public static final String[] FILE_B = {
        "results/fonts/testB1.pdf", "results/fonts/testB2.pdf", "results/fonts/testB3.pdf"
    };
    public static final String[] FILE_C = {
        "results/fonts/testC1.pdf", "results/fonts/testC2.pdf", "results/fonts/testC3.pdf"
    };
    public static final String[] CONTENT = {
        "abcdefgh", "ijklmnopq", "rstuvwxyz"
    };
    public static final String MERGED_A1 = "results/fonts/testA_merged1.pdf";
    public static final String MERGED_A2 = "results/fonts/testA_merged2.pdf";
    public static final String MERGED_B1 = "results/fonts/testB_merged1.pdf";
    public static final String MERGED_B2 = "results/fonts/testB_merged2.pdf";
    public static final String MERGED_C1 = "results/fonts/testC_merged1.pdf";
    public static final String MERGED_C2 = "results/fonts/testC_merged2.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        MergeAndAddFont app = new MergeAndAddFont();
        for (int i = 0; i < FILE_A.length; i++) {
            app.createPdf(FILE_A[i], CONTENT[i], true, true);
        }
        app.mergeFiles(FILE_A, MERGED_A1,false);
        app.mergeFiles(FILE_A, MERGED_A2, true);
        for (int i = 0; i < FILE_B.length; i++) {
            app.createPdf(FILE_B[i], CONTENT[i], true, false);
        }
        app.mergeFiles(FILE_B, MERGED_B1,false);
        app.mergeFiles(FILE_B, MERGED_B2, true);
        for (int i = 0; i < FILE_C.length; i++) {
            app.createPdf(FILE_C[i], CONTENT[i], false, false);
        }
        app.mergeFiles(FILE_C, MERGED_C1, true);
        app.embedFont(MERGED_C1, FONT, MERGED_C2);
    }

    public void createPdf(String filename, String text, boolean embedded, boolean subset) throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.WINANSI, embedded);
        bf.setSubset(subset);
        Font font = new Font(bf, 12);
        document.add(new Paragraph(text, font));
        // step 5
        document.close();
    }
    
    public void mergeFiles(String[] files, String result, boolean smart) throws IOException, DocumentException {
        Document document = new Document();
        PdfCopy copy;
        if (smart)
            copy = new PdfSmartCopy(document, new FileOutputStream(result));
        else
            copy = new PdfCopy(document, new FileOutputStream(result));
        document.open();
        PdfReader[] reader = new PdfReader[3];
        for (int i = 0; i < files.length; i++) {
            reader[i] = new PdfReader(files[i]);
            copy.addDocument(reader[i]);
        }
        document.close();
        for (int i = 0; i < reader.length; i++) {
            reader[i].close();
        }
    }
    
    private void embedFont(String merged, String fontfile, String result) throws IOException, DocumentException {
        // the font file
        RandomAccessFile raf = new RandomAccessFile(fontfile, "r");
        byte fontbytes[] = new byte[(int)raf.length()];
        raf.readFully(fontbytes);
        raf.close();
        // create a new stream for the font file
        PdfStream stream = new PdfStream(fontbytes);
        stream.flateCompress();
        stream.put(PdfName.LENGTH1, new PdfNumber(fontbytes.length));
        // create a reader object
        PdfReader reader = new PdfReader(merged);
        int n = reader.getXrefSize();
        PdfObject object;
        PdfDictionary font;
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(result));
        PdfName fontname = new PdfName(BaseFont.createFont(fontfile, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED).getPostscriptFontName());
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isDictionary())
                continue;
            font = (PdfDictionary)object;
            if (PdfName.FONTDESCRIPTOR.equals(font.get(PdfName.TYPE))
                && fontname.equals(font.get(PdfName.FONTNAME))) {
                PdfIndirectObject objref = stamper.getWriter().addToBody(stream);
                font.put(PdfName.FONTFILE2, objref.getIndirectReference());
            }
        }
        stamper.close();
        reader.close();
    }
}
