/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStream;

public class C0107_StreamObject {

    public static void main(String[] args) {
        PdfStream stream = new PdfStream(
         "Long stream of data stored in a FlateDecode compressed stream object"
         .getBytes());
        stream.flateCompress();
        showObject(stream);
    }
    
    public static void showObject(PdfStream obj) {
        System.out.println(obj.getClass().getName() + ":");
        System.out.println("-> stream? " + obj.isStream());
        System.out.println("-> type: " + obj.type());
        System.out.println("-> toString: " + obj.toString());
        System.out.println("-> raw length: " + obj.getRawLength());
        System.out.println("-> size: " + obj.size());
        for (PdfName key : obj.getKeys()) {
            System.out.print(" " + key + ": ");
            System.out.println(obj.get(key));
        }
    }
}
