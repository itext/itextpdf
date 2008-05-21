package in_action.chapterX;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.XfdfReader;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class FoobarTranscriptMerge {
    /**
     * Merges an XFDF file with a PDF form.
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        try {
            // merging the FDF file
        	FoobarTranscriptOfRecords.main(args);
            XfdfReader fdfreader = new XfdfReader("resources/in_action/chapterX/Grades.xfdf");
            PdfReader pdfreader = new PdfReader("results/in_action/chapterX/FoobarTranscriptOfRecords.pdf");
            PdfStamper stamp = new PdfStamper(pdfreader, new FileOutputStream("results/in_action/chapterX/brunoToR_en.pdf"));
            AcroFields form = stamp.getAcroFields();
            form.setFields(fdfreader);
            stamp.close();
        	FoobarTranscriptOfRecordsFR.main(args);
            pdfreader = new PdfReader("results/in_action/chapterX/FoobarTranscriptOfRecordsFR.pdf");
            stamp = new PdfStamper(pdfreader, new FileOutputStream("results/in_action/chapterX/brunoToR_fr.pdf"));
            form = stamp.getAcroFields();
            form.setFields(fdfreader);
            stamp.setFormFlattening(true);
            stamp.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
