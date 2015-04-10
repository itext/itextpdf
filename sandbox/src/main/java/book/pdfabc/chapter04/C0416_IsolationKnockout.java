/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTransparencyGroup;
import com.itextpdf.text.pdf.PdfWriter;

public class C0416_IsolationKnockout {

    public static final String DEST = "results/pdfabc/chapter04/isolation_knockout.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0416_IsolationKnockout().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate backdrop = cb.createTemplate(200, 200);
        PdfShading axial = PdfShading.simpleAxial(writer, 0, 0, 200, 0,
                BaseColor.YELLOW, BaseColor.RED);
        PdfShadingPattern axialPattern = new PdfShadingPattern(axial);
        backdrop.setShadingFill(axialPattern);
        backdrop.setColorStroke(BaseColor.BLACK);
        backdrop.setLineWidth(2);
        backdrop.rectangle(0, 0, 200, 200);
        backdrop.fillStroke();
        
        float gap = (document.getPageSize().getWidth() - 400) / 10;
        cb.addTemplate(backdrop, 50 + gap, 500);
        cb.addTemplate(backdrop, 250 + 2 * gap, 500);
        cb.addTemplate(backdrop, 50 + gap, 500 - 200 - gap);
        cb.addTemplate(backdrop, 250 + 2 * gap, 500 - 200 - gap);
        
        PdfTemplate tp;
        PdfTransparencyGroup group;
        
        tp = cb.createTemplate(200, 200);
        pictureCircles(0, 0, tp);
        group = new PdfTransparencyGroup();
        group.setIsolated(true);
        group.setKnockout(true);
        tp.setGroup(group);
        cb.addTemplate(tp, 50 + gap, 500);

        tp = cb.createTemplate(200, 200);
        pictureCircles(0, 0, tp);
        group = new PdfTransparencyGroup();
        group.setIsolated(true);
        group.setKnockout(false);
        tp.setGroup(group);
        cb.addTemplate(tp, 250 + 2 * gap, 500);

        tp = cb.createTemplate(200, 200);
        pictureCircles(0, 0, tp);
        group = new PdfTransparencyGroup();
        group.setIsolated(false);
        group.setKnockout(true);
        tp.setGroup(group);
        cb.addTemplate(tp, 50 + gap, 500 - 200 - gap);

        tp = cb.createTemplate(200, 200);
        pictureCircles(0, 0, tp);
        group = new PdfTransparencyGroup();
        group.setIsolated(false);
        group.setKnockout(false);
        tp.setGroup(group);
        cb.addTemplate(tp, 250 + 2 * gap, 500 - 200 - gap);
        
        document.close();
    }

    /**
     * Prints 3 circles in different colors that intersect with eachother.
     * 
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    public static void pictureCircles(float x, float y, PdfContentByte cb) {
        PdfGState gs = new PdfGState();
        gs.setBlendMode(PdfGState.BM_MULTIPLY);
        gs.setFillOpacity(1f);
        cb.setGState(gs);
        cb.setColorFill(new CMYKColor(0f, 0f, 0f, 0.15f));
        cb.circle(x + 75, y + 75, 70);
        cb.fill();
        cb.circle(x + 75, y + 125, 70);
        cb.fill();
        cb.circle(x + 125, y + 75, 70);
        cb.fill();
        cb.circle(x + 125, y + 125, 70);
        cb.fill();
    }

}
