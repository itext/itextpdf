package javaone.edition14.part2;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContentStreams {

    public static final String RESULT_HIGH = "results/javaone/edition2014/part2/hello-highlevel.pdf";
    public static final String RESULT_LOW = "results/javaone/edition2014/part2/hello-lowlevel.pdf";
    public static final String RESULT_UNCOMPRESSED = "results/javaone/edition2014/part2/hello-uncompressed.pdf";
    public static final String RESULT_CHUNKS = "results/javaone/edition2014/part2/hello-chunks.pdf";
    public static final String RESULT_ABSOLUTE = "results/javaone/edition2014/part2/hello-absolute.pdf";
    public static final String RESULT_REFLOW = "results/javaone/edition2014/part2/hello-reflow.pdf";
    public static final String RESULT_REFLOW_LOW = "results/javaone/edition2014/part2/hello-reflow-low.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(RESULT_HIGH);
        file.getParentFile().mkdirs();
    	ContentStreams cs = new ContentStreams();
    	cs.createPdfHigh();
    	cs.createPdfLow();
    	cs.createPdfUncompressed();
    	cs.createPdfChunks();
    	cs.createPdfAbsolute();
    	cs.createPdfReflow();
    	cs.createPdfReflowLow();
    }

    public void createPdfHigh()	throws DocumentException, IOException {
        // step 1
        Document document = new Document(PageSize.LETTER);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT_HIGH));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }
    
    public void createPdfLow() throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_LOW));
        writer.setCompressionLevel(PdfStream.NO_COMPRESSION);
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.saveState();                               // q
        canvas.beginText();                               // BT
        canvas.moveText(36, 788);                         // 36 788 Td
        canvas.setFontAndSize(BaseFont.createFont(), 12); // /F1 12 Tf
        canvas.showText("Hello World!");                  // (Hello World!)Tj
        canvas.endText();                                 // ET
        canvas.restoreState();                            // Q
        // step 5
        document.close();
    }
    
    public void createPdfUncompressed() throws FileNotFoundException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_UNCOMPRESSED));
        writer.setCompressionLevel(PdfStream.NO_COMPRESSION);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }

    public void createPdfChunks() throws FileNotFoundException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_CHUNKS));
        writer.setCompressionLevel(PdfStream.NO_COMPRESSION);
        // step 3
        document.open();
        // step 4
        Paragraph p = new Paragraph();
        p.setLeading(18);
        document.add(p);
        document.add(new Chunk("H"));
        document.add(new Chunk("e"));
        document.add(new Chunk("l"));
        document.add(new Chunk("l"));
        document.add(new Chunk("o"));
        document.add(new Chunk(" "));
        document.add(new Chunk("W"));
        document.add(new Chunk("o"));
        document.add(new Chunk("r"));
        document.add(new Chunk("l"));
        document.add(new Chunk("d"));
        document.add(new Chunk("!"));
        // step 5
        document.close();
    }
    
    public void createPdfAbsolute() throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        Document.compress = false;
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_ABSOLUTE));
        //writer.setCompressionLevel(PdfStream.NO_COMPRESSION);
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContentUnder();
        writer.setCompressionLevel(0);
        canvas.saveState();                               // q
        canvas.beginText();                               // BT
        canvas.moveText(36, 788);                         // 36 788 Td
        canvas.setFontAndSize(BaseFont.createFont(), 12); // /F1 12 Tf
        canvas.showText("Hel");                           // (Hel)Tj
        canvas.moveText(30.65f, 0);
        canvas.showText("World!");                        // (World!)Tj
        canvas.moveText(-12.7f, 0);
        canvas.showText("lo");                            // (lo)Tj
        canvas.endText();                                 // ET
        canvas.restoreState();                            // Q
        // step 5
        document.close();
        Document.compress = true;
    }
    
	public void createPdfReflow() throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		Document.compress = false;
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(RESULT_REFLOW));
		// step 3
		document.open();
		// step 4
		document.add(new Paragraph("0 Hello World 1 Hello World 2 Hello World 3 Hello World 4 Hello World 5 Hello World 6 Hello World 7 Hello World 8 Hello World 9 Hello World A Hello World B Hello World"));
		// step 5
		document.close();
		Document.compress = true;
	}
	
    public void createPdfReflowLow() throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_REFLOW_LOW));
        writer.setCompressionLevel(PdfStream.NO_COMPRESSION);
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.saveState();                               // q
        canvas.beginText();                               // BT
        canvas.moveText(36, 788);                         // 36 788 Td
        canvas.setFontAndSize(BaseFont.createFont(), 12); // /F1 12 Tf
        canvas.showText("0 Hello World 1 Hello World 2 Hello World 3 Hello World 4 Hello World 5 Hello World 6 Hello World 7 Hello World 8 Hello World 9 Hello World A Hello World B Hello World");
        canvas.endText();                                 // ET
        canvas.restoreState();                            // Q
        // step 5
        document.close();
    }

}