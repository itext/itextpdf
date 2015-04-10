/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfString;

public class C0105_ArrayObject {

    public static void main(String[] args) {
        PdfArray array = new PdfArray();
        array.add(PdfName.FIRST);
        array.add(new PdfString("Second"));
        array.add(new PdfNumber(3));
        array.add(PdfBoolean.PDFFALSE);
        showObject(array);
        showObject(new PdfRectangle(595, 842));
    }
    
    public static void showObject(PdfArray obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> array? " + obj.isArray());
        System.out.println("-> type: " + obj.type());
        System.out.println("-> toString: " + obj.toString());
        System.out.println("-> size: " + obj.size());
        System.out.print("-> Values:");
        for (int i = 0; i < obj.size(); i++) {
            System.out.print(" ");
            System.out.print(obj.getPdfObject(i));
        }
        System.out.println();
    }

}
