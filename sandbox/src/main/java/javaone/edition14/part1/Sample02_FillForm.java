/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part1;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Example that shows how to fill out a form.
 */
public class Sample02_FillForm {
    /** The original PDF file. */
    public static final String SRC
        = "resources/pdfs/form.pdf";
    /** The resulting PDF file. */
    public static final String DEST
        = "results/javaone/edition2014/part1/form_filled.pdf";
    
    /**
     * Fills out an interactive form.
     * @param args  no arguments needed
     * @throws DocumentException
     * @throws IOException 
     */
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Sample02_FillForm().manipulatePdf(SRC, DEST);
    }
    
    /**
     * Fills out a form with the name, company and country.
     * @param src   the path to the original form
     * @param dest  the path to the filled out form
     * @throws DocumentException
     * @throws IOException 
     */
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.setField("Name", "Raf Hens");
        form.setField("Company", "iText Software");
        form.setField("Country", "BELGIUM");
        stamper.close();
        reader.close();
    }
}
