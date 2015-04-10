/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfNumber;

public class C0102_NumberObject {

    public static void main(String[] args) {
        showObject(new PdfNumber("1.5"));
        showObject(new PdfNumber(100));
        showObject(new PdfNumber(100l));
        showObject(new PdfNumber(1.5));
        showObject(new PdfNumber(1.5f));
    }

    public static void showObject(PdfNumber obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> number? " + obj.isNumber());
        System.out.println("-> type: " + obj.type());
        System.out.println("-> bytes: " + new String(obj.getBytes()));
        System.out.println("-> toString: " + obj.toString());
        System.out.println("-> intValue: " + obj.intValue());
        System.out.println("-> longValue: " + obj.longValue());
        System.out.println("-> doubleValue: " + obj.doubleValue());
        System.out.println("-> floatValue: " + obj.floatValue());
    }
    
}
