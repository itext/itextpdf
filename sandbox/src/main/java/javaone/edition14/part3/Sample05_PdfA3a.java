/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part3;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Creates a PDF that conforms with PDF/A-3 Level A.
 */
public class Sample05_PdfA3a {

    /** The resulting PDF. */
    public static final String DEST = "results/javaone/edition2014/part3/quickbrownfox4.pdf";
    /** An image resource. */
    public static final String FOX = "resources/images/fox.bmp";
    /** An image resource. */
    public static final String DOG = "resources/images/dog.bmp";
    /** A path to a color profile. */
    public static final String ICC = "resources/data/sRGB_CS_profile.icm";
    /** A font that will be embedded. */
    public static final String FONT = "resources/fonts/FreeSans.ttf";
    
    /**
     * Creates a PDF that conforms with PDF/A-3 Level A.
     * @param args  no arguments needed
     * @throws IOException
     * @throws DocumentException 
     */
    static public void main(String args[]) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Sample05_PdfA3a().createPdf(DEST);
    }
    
    /**
     * Creates a PDF that conforms with PDF/A-3 Level B.
     * @param dest  the path to the resulting PDF
     * @throws IOException
     * @throws DocumentException 
     */
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        //PDF/A-3a
        //Create PdfAWriter with the required conformance level
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(dest), PdfAConformanceLevel.PDF_A_3A);
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        //====================
        //TAGGED PDF
        //Make document tagged
        writer.setTagged();
        //===============
        //PDF/UA
        //Set document metadata
        writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
        document.addLanguage("en-US");
        document.addTitle("Some title");
        writer.createXmpMetadata();
        //=====================
        document.open();
        //PDF/A-3b
        //Set output intents
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(ICC));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        //===================

        Paragraph p = new Paragraph();
        //PDF/UA
        //Embed font
        p.setFont(FontFactory.getFont(FONT, BaseFont.WINANSI, BaseFont.EMBEDDED, 20));
        //==================
        Chunk c = new Chunk("The quick brown ");
        p.add(c);
        Image i = Image.getInstance(FOX);
        c = new Chunk(i, 0, -24);
        //PDF/UA
        //Set alt text
        c.setAccessibleAttribute(PdfName.ALT, new PdfString("Fox"));
        //==============
        p.add(c);
        p.add(new Chunk(" jumps over the lazy "));
        i = Image.getInstance(DOG);
        c = new Chunk(i, 0, -24);
        //PDF/UA
        //Set alt text
        c.setAccessibleAttribute(PdfName.ALT, new PdfString("Dog"));
        //==================
        p.add(c);
        document.add(p);

        document.close();
    }

}
