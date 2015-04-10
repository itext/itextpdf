/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter03;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.SimpleNamedDestination;

public class C0307_DestinationsOutlines {
    public static final String SRC = "resources/pdfs/primes.pdf";

    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader(SRC);
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary names = catalog.getAsDict(PdfName.NAMES);
        PdfDictionary dests = names.getAsDict(PdfName.DESTS);
        PdfArray array = dests.getAsArray(PdfName.NAMES);
        System.out.println(array.getAsString(0));
        System.out.println(array.getAsArray(1));
        
        Map<String, String> map = SimpleNamedDestination.getNamedDestination(reader, false);
        System.out.println(map.get("Prime101"));
        
        PdfDictionary outlines = catalog.getAsDict(PdfName.OUTLINES);
        System.out.println("Root:");
        showEntries(outlines);
        System.out.println("First:");
        showEntries(outlines.getAsDict(PdfName.FIRST));
        System.out.println("Last:");
        showEntries(outlines.getAsDict(PdfName.LAST));
        
        List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
        for (HashMap<String, Object> item : bookmarks) {
            System.out.println(item);
        }        
        reader.close();
    }
    
    public static void showEntries(PdfDictionary dict) {
        for (PdfName key : dict.getKeys()) {
            System.out.print(key + ": ");
            System.out.println(dict.get(key));
        }
    }
}
