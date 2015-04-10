/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter01;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;

public class C0110_Objects {

    public static void main(String[] args) {
        
        PdfArray array = new PdfArray();
        array.add(PdfName.FIRST);
        array.add(new PdfString("Second"));
        array.add(new PdfNumber(3));
        array.add(PdfBoolean.PDFFALSE);
        
        PdfDictionary dict = new PdfDictionary();
        dict.put(new PdfName("Entry1"), PdfName.FIRST);
        dict.put(new PdfName("Entry2"), new PdfString("Second"));
        dict.put(new PdfName("3rd"), new PdfNumber(3));
        dict.put(new PdfName("Fourth"), PdfBoolean.PDFFALSE);
        dict.put(new PdfName("Fifth"), array);
        
        array.add(dict);
        
        System.out.println("ARRAY\n\n");
        for (int i = 0; i < array.size(); i++) {
            showObject(array.getPdfObject(i));
        }
        System.out.println("\n\nDICTIONARY\n\n");
        for (PdfName key : dict.getKeys()) {
            System.out.println(key + ": ");
            showObject(dict.get(key));
        }
    }
    

    
    public static void showObject(PdfObject obj) {
        if (obj == null)
            System.out.println("No such object");
        switch (obj.type()) {
        case PdfObject.BOOLEAN:
            C0101_BooleanObject.showObject((PdfBoolean)obj);
            break;
        case PdfObject.NUMBER:
            C0102_NumberObject.showObject((PdfNumber)obj);
            break;
        case PdfObject.STRING:
            C0103_StringObject.showObject((PdfString)obj);
            break;
        case PdfObject.NAME:
            C0104_NameObject.showObject((PdfName)obj);
            break;
        case PdfObject.ARRAY:
            C0105_ArrayObject.showObject((PdfArray)obj);
            break;
        case PdfObject.DICTIONARY:
            C0106_DictionaryObject.showObject((PdfDictionary)obj);
            break;
        case PdfObject.STREAM:
            C0107_StreamObject.showObject((PdfStream)obj);
            break;
        default:
            System.out.println("Unknown type: " + obj.getClass().getName());
        }
    }
}
