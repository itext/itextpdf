/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTransparencyGroup;
import com.itextpdf.text.pdf.PdfWriter;

public class C0420_TransparentOverlay {


    public static final String DEST = "results/pdfabc/chapter04/transparent_overlay.pdf";;
    public static final String RESOURCE = "resources/images/bruno_ingeborg.jpg";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0420_TransparentOverlay().createPdf(DEST);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(850, 600));
        PdfWriter writer
          = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        
        // add the clipped image
        Image img = Image.getInstance(RESOURCE);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        canvas.ellipse(1, 1, 848, 598);
        canvas.clip();
        canvas.newPath();
        canvas.addImage(img, w, 0, 0, h, 0, -600);

        // Create a transparent PdfTemplate
        PdfTemplate t2 = writer.getDirectContent().createTemplate(850, 600);
        PdfTransparencyGroup transGroup = new PdfTransparencyGroup();
        transGroup.put(PdfName.CS, PdfName.DEVICEGRAY);
        transGroup.setIsolated(true);
        transGroup.setKnockout(false);
        t2.setGroup(transGroup);

        // Add transparent ellipses to the template
        int gradationStep = 30;
        float[] gradationRatioList = new float[gradationStep];
        for(int i = 0; i < gradationStep; i++) {
            gradationRatioList[i] = 1 - (float)Math.sin(Math.toRadians(90.0f / gradationStep * (i + 1)));
        }
        for(int i = 1; i < gradationStep + 1; i++) {
            t2.setLineWidth(5 * (gradationStep + 1 - i));
            t2.setGrayStroke(gradationRatioList[gradationStep - i]);
            t2.ellipse(0, 0, 850, 600);
            t2.stroke();
        }
        
        // Create an image mask for the direct content
        PdfDictionary maskDict = new PdfDictionary();
        maskDict.put(PdfName.TYPE, PdfName.MASK );
        maskDict.put(PdfName.S, new PdfName("Luminosity"));
        maskDict.put(new PdfName("G"), t2.getIndirectReference());
        PdfGState gState = new PdfGState();
        gState.put(PdfName.SMASK, maskDict);
        canvas.setGState(gState);
        
        canvas.addTemplate(t2, 0, 0);

        document.close();
    }
}
