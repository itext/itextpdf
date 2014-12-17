/*
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26586093/how-to-add-a-shading-pattern-to-a-custom-shape-in-itext
 */
package sandbox.objects;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShadedFill {
    
    public static final String DEST = "results/objects/shaded_fill.pdf";
    
        
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ShadedFill().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        float x = 36;
        float y = 740;
        float side = 70;
        PdfShading axial = PdfShading.simpleAxial(writer, x, y,
                x + side, y, BaseColor.PINK, BaseColor.BLUE);
        PdfShadingPattern shading = new PdfShadingPattern(axial);
        canvas.setShadingFill(shading);
        canvas.moveTo(x,y);        
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + (side / 2), (float)(y + (side * Math.sin(Math.PI / 3))));
        canvas.closePathFillStroke();
        // step 5
        document.close();
    }
}
