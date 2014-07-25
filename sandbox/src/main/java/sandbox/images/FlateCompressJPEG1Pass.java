/**
 * This sample is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/21958449/can-itextsharp-generate-pdf-with-jpeg-images-that-are-multi-stage-filtered-both
 * 
 * The question was about adding compression to an image that already used /DCTDecode
 * 
 * IMPORTANT:
 * This sample uses core iText functionality that was written in answer to the question.
 * This example will only work starting with iText 5.5.1 
 */
package sandbox.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class FlateCompressJPEG1Pass {
    public static final String IMAGE = "resources/images/berlin2013.jpg";
    public static final String DEST = "results/images/flatecompress_image1.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FlateCompressJPEG1Pass().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        Image img = Image.getInstance(IMAGE);
        img.setCompressionLevel(PdfStream.BEST_COMPRESSION);
        img.scaleAbsolute(PageSize.A4.rotate());
        img.setAbsolutePosition(0, 0);
        document.add(img);
        document.close();
    }
}
