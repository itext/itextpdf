/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter03;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class C0306_PageAnnotations {
    public static final String SRC = "resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader(SRC);
        PdfDictionary page = reader.getPageN(7);
        PdfArray annots = page.getAsArray(PdfName.ANNOTS);
        for (int i = 0; i < annots.size(); i++) {
            System.out.println("Annotation " + (i + 1));
            showEntries(annots.getAsDict(i));
        }
        PdfDictionary link = annots.getAsDict(1);
        System.out.println("Action:");
        showEntries(link.getAsDict(PdfName.A));
    }
    
    public static void showEntries(PdfDictionary dict) {
        for (PdfName key : dict.getKeys()) {
            System.out.print(key + ": ");
            System.out.println(dict.get(key));
        }
    }

}
