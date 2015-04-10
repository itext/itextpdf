/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter03;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;

public class C0301_TrailerInfo {
	
    public static final String SRC = "resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader(SRC);
        PdfDictionary trailer = reader.getTrailer();
        showEntries(trailer);
        PdfNumber size = (PdfNumber)trailer.get(PdfName.SIZE);
        showObject(size);
        size = trailer.getAsNumber(PdfName.SIZE);
        showObject(size);
        PdfArray ids = trailer.getAsArray(PdfName.ID);
        PdfString id1 = ids.getAsString(0);
        showObject(id1);
        PdfString id2 = ids.getAsString(1);
        showObject(id2);
        PdfObject object = trailer.get(PdfName.INFO);
        showObject(object);
        showObject(trailer.getAsDict(PdfName.INFO));
        PdfIndirectReference ref = trailer.getAsIndirectObject(PdfName.INFO);
        showObject(ref);
        object = reader.getPdfObject(ref.getNumber());
        showObject(object);
        object = PdfReader.getPdfObject(trailer.get(PdfName.INFO));
        showObject(object);
        reader.close();
    }
    
    public static void showEntries(PdfDictionary dict) {
        for (PdfName key : dict.getKeys()) {
            System.out.print(key + ": ");
            System.out.println(dict.get(key));
        }
    }
    
    public static void showObject(PdfObject obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> type: " + obj.type());
        System.out.println("-> toString: " + obj.toString());
    }
}
