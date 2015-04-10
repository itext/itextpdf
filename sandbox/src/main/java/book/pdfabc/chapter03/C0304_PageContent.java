/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter03;

import java.io.IOException;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class C0304_PageContent {
    public static final String SRC = "resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader(SRC);
        
        System.out.println("Page 1:");
        PdfDictionary page = reader.getPageN(1);
        PRStream contents = (PRStream)page.getAsStream(PdfName.CONTENTS);
        byte[] bytes = PdfReader.getStreamBytes(contents);
        System.out.println(new String(bytes));
        
        System.out.println("Page 2:");
        bytes = reader.getPageContent(2);
        System.out.println(new String(bytes));
        
        page = reader.getPageN(2);
        PdfDictionary resources = page.getAsDict(PdfName.RESOURCES); 
        for (PdfName key : resources.getKeys()) {
            System.out.print(key);
            System.out.print(": ");
            System.out.println(resources.getDirectObject(key));
        }
        
        System.out.println();
        System.out.println("XObjects:");
        PdfDictionary xObjects = resources.getAsDict(PdfName.XOBJECT);
        PRStream xObject = (PRStream)xObjects.getAsStream(new PdfName("Xf1"));
        for (PdfName key : xObject.getKeys()) {
            System.out.println(key + ": " + xObject.getDirectObject(key));
        }
        bytes = PdfReader.getStreamBytes(xObject);
        System.out.println(new String(bytes));

        System.out.println("Fonts:");
        PdfDictionary fonts = resources.getAsDict(PdfName.FONT);
        PdfDictionary font = fonts.getAsDict(new PdfName("F1"));
        for (PdfName key : font.getKeys()) {
            System.out.println(key + ": " + font.getDirectObject(key));
        }
        
        reader.close();
    }
}
