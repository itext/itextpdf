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
import com.itextpdf.text.pdf.PdfReader;

public class C0302_PageTree {

    public static final String SRC = "resources/pdfs/primes.pdf";
	
    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader(SRC);
        PdfDictionary dict = reader.getCatalog();
        PdfDictionary pageroot = dict.getAsDict(PdfName.PAGES);
        new C0302_PageTree().expand(pageroot);
        reader.close();
    }

    private int page = 1;
    
    public void expand(PdfDictionary dict) {
        if (dict == null)
            return;
        PdfIndirectReference ref = dict.getAsIndirectObject(PdfName.PARENT);
        if (dict.isPage()) {
            System.out.println("Child of " + ref + ": PAGE " + (page++));
        }
        else if (dict.isPages()) {
            if (ref ==  null)
                System.out.println("PAGES ROOT");
            else
                System.out.println("Child of " + ref + ": PAGES");
            PdfArray kids = dict.getAsArray(PdfName.KIDS);
            System.out.println(kids);
            if (kids != null) {
                for (int i = 0; i < kids.size(); i++) {
                    expand(kids.getAsDict(i));
                }
            }
        }
    }
}
