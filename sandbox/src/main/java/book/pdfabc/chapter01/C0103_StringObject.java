/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfString;

public class C0103_StringObject {

    public static void main(String[] args) {    
        PdfString s1 = new PdfString("Test");
        PdfString s2 = new PdfString("\u6d4b\u8bd5", PdfString.TEXT_UNICODE);
        showObject(s1);
        showObject(s2);
        s1.setHexWriting(true);
        showObject(s1);
        showObject(new PdfDate());
    }
    
    public static void showObject(PdfString obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> string? " + obj.isString());
        System.out.println("-> type: " + obj.type());
        System.out.println("-> bytes: " + new String(obj.getBytes()));
        System.out.println("-> toString: " + obj.toString());
        System.out.println("-> hexWriting: " + obj.isHexWriting());
        System.out.println("-> encoding: " + obj.getEncoding());
        System.out.println("-> bytes: " + new String(obj.getOriginalBytes()));
        System.out.println("-> unicode string: " + obj.toUnicodeString());
    }
}
