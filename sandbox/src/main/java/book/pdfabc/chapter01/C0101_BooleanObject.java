/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfBoolean;

public class C0101_BooleanObject {

    public static void main(String[] args) {
        showObject(PdfBoolean.PDFTRUE);
        showObject(PdfBoolean.PDFFALSE);
    }

    public static void showObject(PdfBoolean obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> boolean? " + obj.isBoolean());
        System.out.println("-> type: " + obj.type());
        System.out.println("-> toString: " + obj.toString());
        System.out.println("-> booleanvalue: " + obj.booleanValue());
    }
}
