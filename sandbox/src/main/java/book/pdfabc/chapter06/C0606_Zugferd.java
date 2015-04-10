package book.pdfabc.chapter06;

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

public class C0606_Zugferd {
    public static final String DEST = "results/pdfabc/chapter06/quickbrownfox6.pdf";
    public static final String FOX = "resources/images/fox.bmp";
    public static final String DOG = "resources/images/dog.bmp";
    public static final String ICC = "resources/data/sRGB_CS_profile.icm";
    public static final String FONT = "resources/fonts/FreeSans.ttf";
    public static final String XML = "resources/xml/invoice.xml";
    
    static public void main(String args[]) throws IOException, DocumentException, XMPException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0606_Zugferd().createPdf(DEST);
    }
    
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
