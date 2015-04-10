/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfFormXObject;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfObject;

public class C0109_LiteralObject {

    public static void main(String[] args) {
        showObject(PdfFormXObject.MATRIX);
        showObject(new PdfLiteral(
            PdfObject.DICTIONARY, "<</Type/Custom/Contents [1 2 3]>>"));
    }
    public static void showObject(PdfObject obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> type: " + obj.type());
        System.out.println("-> bytes: " + new String(obj.getBytes()));
        System.out.println("-> toString: " + obj.toString());
    }
}
