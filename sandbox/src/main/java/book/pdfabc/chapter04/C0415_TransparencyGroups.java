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
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTransparencyGroup;
import com.itextpdf.text.pdf.PdfWriter;

public class C0415_TransparencyGroups {

    public static final String DEST = "results/pdfabc/chapter04/transparencygroups.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0415_TransparencyGroups().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate backdrop = cb.createTemplate(200, 200);
        backdrop.setColorStroke(GrayColor.GRAYBLACK);
        backdrop.setColorFill(new GrayColor(0.8f));
        backdrop.rectangle(0, 0, 100, 200);
        backdrop.fill();
        backdrop.setLineWidth(2);
        backdrop.rectangle(0, 0, 200, 200);
        backdrop.stroke();

        float gap = (document.getPageSize().getWidth() - 400) / 3;
        cb.addTemplate(backdrop, gap, 500);
        cb.addTemplate(backdrop, 200 + 2 * gap, 500);
        cb.addTemplate(backdrop, gap, 500 - 200 - gap);
        cb.addTemplate(backdrop, 200 + 2 * gap, 500 - 200 - gap);

        drawCircles(gap, 500, cb);
        cb.saveState();
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.5f);
        cb.setGState(gs1);
        drawCircles(200 + 2 * gap, 500, cb);
        cb.restoreState();

        cb.saveState();
        PdfTemplate tp = cb.createTemplate(200, 200);
        PdfTransparencyGroup group = new PdfTransparencyGroup();
        tp.setGroup(group);
        drawCircles(0, 0, tp);
        cb.setGState(gs1);
        cb.addTemplate(tp, gap, 500 - 200 - gap);
        cb.restoreState();

        cb.saveState();
        tp = cb.createTemplate(200, 200);
        tp.setGroup(group);
        PdfGState gs2 = new PdfGState();
        gs2.setFillOpacity(0.5f);
        gs2.setBlendMode(PdfGState.BM_HARDLIGHT);
        tp.setGState(gs2);
        drawCircles(0, 0, tp);
        cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
        cb.restoreState();

        cb.setColorFill(GrayColor.BLACK);
        ColumnText ct = new ColumnText(cb);
        Phrase ph = new Phrase("Ungrouped objects\nObject opacity = 1.0");
        ct.setSimpleColumn(ph, gap, 0, gap + 200, 500, 18, Element.ALIGN_CENTER);
        ct.go();

        ph = new Phrase("Ungrouped objects\nObject opacity = 0.5");
        ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500,
            18, Element.ALIGN_CENTER);
        ct.go();

        ph = new Phrase(
           "Transparency group\nObject opacity = 1.0\nGroup opacity = 0.5\nBlend mode = Normal");
        ct.setSimpleColumn(ph, gap, 0, gap + 200, 500 - 200 - gap, 18, Element.ALIGN_CENTER);
        ct.go();

        ph = new Phrase(
            "Transparency group\nObject opacity = 0.5\nGroup opacity = 1.0\nBlend mode = HardLight");
        ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500 - 200 - gap,
           18, Element.ALIGN_CENTER);
        ct.go();
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
    public static void drawCircles(float x, float y, PdfContentByte cb) {
        cb.setColorFill(BaseColor.RED);
        cb.circle(x + 70, y + 70, 50);
        cb.fill();
        cb.setColorFill(BaseColor.YELLOW);
        cb.circle(x + 100, y + 130, 50);
        cb.fill();
        cb.setColorFill(BaseColor.BLUE);
        cb.circle(x + 130, y + 70, 50);
        cb.fill();
    }
}
