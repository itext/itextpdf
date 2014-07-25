package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPageLabels;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class PageLabels {

    public static final String DEST = "results/objects/pagelabels.pdf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PageLabels().createPdf(DEST);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setViewerPreferences(PdfWriter.PageLayoutTwoPageLeft | PdfWriter.PageModeUseThumbs);
        writer.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
        PdfPageLabels labels = new PdfPageLabels();
        labels.addPageLabel(1, PdfPageLabels.UPPERCASE_LETTERS);
        labels.addPageLabel(3, PdfPageLabels.DECIMAL_ARABIC_NUMERALS);
        labels.addPageLabel(4,
            PdfPageLabels.DECIMAL_ARABIC_NUMERALS, "Custom-", 2);
        writer.setPageLabels(labels);
        document.open();
        document.add(new Paragraph("Hello World"));
        document.add(new Paragraph("Hello People"));
        document.newPage();

        // we add the text to the direct content, but not in the right order
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf = BaseFont.createFont();
        cb.beginText();
        cb.setFontAndSize(bf, 12);
        cb.moveText(88.66f, 788); 
        cb.showText("ld");
        cb.moveText(-22f, 0); 
        cb.showText("Wor");
        cb.moveText(-15.33f, 0); 
        cb.showText("llo");
        cb.moveText(-15.33f, 0); 
        cb.showText("He");
        cb.endText();
        // we also add text in a form XObject
        PdfTemplate tmp = cb.createTemplate(250, 25);
        tmp.beginText();
        tmp.setFontAndSize(bf, 12);
        tmp.moveText(0, 7);
        tmp.showText("Hello People");
        tmp.endText();
        cb.addTemplate(tmp, 36, 763);
               
        document.setPageSize(PageSize.A4.rotate());
        document.newPage();
        document.add(new Paragraph("Hello World"));
        
        document.setPageSize(new Rectangle(842, 595));
        document.newPage();
        document.add(new Paragraph("Hello World"));
        document.setPageSize(PageSize.A4);
        
        writer.setCropBoxSize(new Rectangle(40, 40, 565, 795));
        document.newPage();
        document.add(new Paragraph("Hello World"));

        writer.setCropBoxSize(null);
        document.newPage();
        writer.addPageDictEntry(PdfName.USERUNIT, new PdfNumber(5));
        document.add(new Paragraph("Hello World"));

        writer.setBoxSize("art", new Rectangle(36, 36, 559, 806));
        document.newPage();
        Anchor anchor = new Anchor("World");
        anchor.setReference("http://maps.google.com");
        Paragraph p = new Paragraph("Hello ");
        p.add(anchor);
        document.add(p);
        Annotation a = new Annotation("Example", "This is a post-it annotation");
        document.add(a);
        
        document.close();
    }
}
