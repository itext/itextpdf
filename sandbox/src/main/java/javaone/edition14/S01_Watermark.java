/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Example that shows how to add a watermark to a simple PDF.
 */
public class S01_Watermark {
    /** The original PDF file. */
    public static final String SRC
        = "resources/pdfs/hello.pdf";
    /** The resulting PDF file. */
    public static final String DEST
        = "results/javaone/edition2014/01_hello_watermark.pdf";

    /**
     * Fills out an interactive form.
     * @param args  no arguments needed
     * @throws DocumentException
     * @throws IOException 
     */
    public static void main(String[] args)
            throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new S01_Watermark().manipulatePdf(SRC, DEST);
    }
    
    /**
     * Fills out and flattens a form with the name, company and country.
     * @param src   the path to the original form
     * @param dest  the path to the filled out form
     * @throws DocumentException
     * @throws IOException 
     */
    public void manipulatePdf(String src, String dest)
            throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte under = stamper.getUnderContent(1);
        
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.3f);
        under.saveState();
        under.setGState(gs);
        under.setRGBColorFill(200, 200, 0);
        ColumnText.showTextAligned(under, Element.ALIGN_CENTER,
                new Phrase("Watermark", new Font(FontFamily.HELVETICA, 120)),
                297, 421, 45);
        under.restoreState();
        stamper.close();
        reader.close();
    }
}
