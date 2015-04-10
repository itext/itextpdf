/*
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26325712/itext-add-image-to-existing-document-using-itext-pdfstamper
 * Copyright 2014, iText Group NV
 */
package sandbox.stamper;

import com.itextpdf.text.BaseColor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;

/**
 * Example that shows how to watermark a less simple PDF.
 */
public class StampBarcode {
    /** The original PDF file. */
    public static final String SRC
        = "resources/pdfs/superman.pdf";
    /** The resulting PDF file. */
    public static final String DEST
        = "results/stamper/add_barcode.pdf";

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
        new StampBarcode().manipulatePdf(SRC, DEST);
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
        int n = reader.getNumberOfPages();
        Rectangle pagesize;
        for (int i = 1; i <= n; i++) {
            PdfContentByte over = stamper.getOverContent(i);
            pagesize = reader.getPageSize(i);
            float x = pagesize.getLeft() + 10;
            float y = pagesize.getTop() - 50;       
            BarcodeEAN barcode = new BarcodeEAN();
            barcode.setCodeType(Barcode.EAN8);
            String s = String.valueOf(i);
            s = "00000000".substring(s.length()) + s; 
            barcode.setCode(s);
            PdfTemplate template =
                    barcode.createTemplateWithBarcode(over, BaseColor.BLACK, BaseColor.BLACK);
            over.addTemplate(template, x, y);
        }
        stamper.close();
        reader.close();
    }
}
