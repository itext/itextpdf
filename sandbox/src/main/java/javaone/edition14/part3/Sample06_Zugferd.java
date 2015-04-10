/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.AFRelationshipValue;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.xmp.XMPException;
import java.io.File;

/**
 * Creates a PDF that conforms with the ZUGFeRD standard.
 */
public class Sample06_Zugferd {

    /** The resulting PDF. */
    public static final String DEST = "results/javaone/edition2014/part3/quickbrownfox6.pdf";
    /** An image resource. */
    public static final String FOX = "resources/images/fox.bmp";
    /** An image resource. */
    public static final String DOG = "resources/images/dog.bmp";
    /** A path to a color profile. */
    public static final String ICC = "resources/data/sRGB_CS_profile.icm";
    /** A font that will be embedded. */
    public static final String FONT = "resources/fonts/FreeSans.ttf";
    /** A path to an XML file that will be attached to the PDF. */
    public static final String XML = "resources/xml/invoice.xml";
    
    /**
     * Creates a PDF that conforms with the ZUGFeRD standard.
     * @param args  no arguments needed
     * @throws IOException
     * @throws DocumentException
     * @throws XMPException 
     */
    static public void main(String args[]) throws IOException, DocumentException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Sample06_Zugferd().createPdf(DEST);
    }
    
    /**
     * Creates a PDF that conforms with the ZUGFeRD standard.
     * @param dest  the path to the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws XMPException 
     */
    public void createPdf(String dest) throws IOException, DocumentException, XMPException {
        Document document = new Document(PageSize.A4.rotate());
        //PDF/A-3b
        //Create PdfAWriter with the required conformance level
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(dest), PdfAConformanceLevel.ZUGFeRD);
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        //Create XMP metadata
        writer.createXmpMetadata();
        writer.getXmpWriter().setProperty(PdfAXmpWriter.zugferdSchemaNS, PdfAXmpWriter.zugferdDocumentFileName, "invoice.xml");
        //====================
        document.open();
        //PDF/A-3b
        //Set output intents
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(ICC));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        //===================

        Paragraph p = new Paragraph();
        //PDF/A-3b
        //Embed font
        p.setFont(FontFactory.getFont(FONT, BaseFont.WINANSI, BaseFont.EMBEDDED, 20));
        //=============
        Chunk c = new Chunk("The quick brown ");
        p.add(c);
        Image i = Image.getInstance(FOX);
        c = new Chunk(i, 0, -24);
        p.add(c);
        c = new Chunk(" jumps over the lazy ");
        p.add(c);
        i = Image.getInstance(DOG);
        c = new Chunk(i, 0, -24);
        p.add(c);
        document.add(p);

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fileSpec = writer.addFileAttachment(
                "ZUGFeRD invoice", null, XML,
                "invoice.xml", "application/xml",
                AFRelationshipValue.Alternative, parameters);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getReference());
        writer.getExtraCatalog().put(PdfName.AF, array);

        document.close();
    }

}
