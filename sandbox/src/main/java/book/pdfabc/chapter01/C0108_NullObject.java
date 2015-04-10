/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfNull;

public class C0108_NullObject {

    public static void main(String[] args) {
        showObject(PdfNull.PDFNULL);
    }
    
    public static void showObject(PdfNull obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> type: " + obj.type());
        System.out.println("-> bytes: " + new String(obj.getBytes()));
        System.out.println("-> toString: " + obj.toString());
    }
}
