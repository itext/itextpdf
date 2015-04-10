/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfString;

public class C0106_DictionaryObject {

    public static void main(String[] args) {    
        PdfDictionary dict = new PdfDictionary(new PdfName("Custom"));
        dict.put(new PdfName("Entry1"), PdfName.FIRST);
        dict.put(new PdfName("Entry2"), new PdfString("Second"));
        dict.put(new PdfName("3rd"), new PdfNumber(3));
        dict.put(new PdfName("Fourth"), PdfBoolean.PDFFALSE);
        showObject(dict);
        showObject(PdfAction.gotoRemotePage("test.pdf", "dest", false, true));
    }

    public static void showObject(PdfDictionary obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> dictionary? " + obj.isDictionary());
        System.out.println("-> type: " + obj.type());
        System.out.println("-> toString: " + obj.toString());
        System.out.println("-> size: " + obj.size());
        for (PdfName key : obj.getKeys()) {
            System.out.print(" " + key + ": ");
            System.out.println(obj.get(key));
        }
    }    
    
}
