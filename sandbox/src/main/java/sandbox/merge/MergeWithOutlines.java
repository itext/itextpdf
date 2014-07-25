/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow
 * 
 * When concatenating documents, we add a named destination every time
 * a new document is started. After we've finished merging, we add an extra
 * page with the table of contents and links to the named destinations.
 */
package sandbox.merge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class MergeWithOutlines {

    public static final String SRC1 = "resources/pdfs/hello.pdf";
    public static final String SRC2 = "resources/pdfs/links1.pdf";
    public static final String SRC3 = "resources/pdfs/links2.pdf";
    public static final String DEST = "results/merge/merge_with_outlines.pdf";
    
    public Map<String, PdfReader> filesToMerge;
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeWithOutlines().createPdf(DEST);
    }
    
    
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(filename));
        document.open();
        int page = 1;
        ArrayList<HashMap<String, Object>> outlines = new ArrayList<HashMap<String, Object>>();
        // add the first document
        PdfReader reader1 = new PdfReader(SRC1);
        copy.addDocument(reader1);
        // add the first outline element
        HashMap<String, Object> helloworld = new HashMap<String, Object>();
        helloworld.put("Title", "Hello World");
        helloworld.put("Action", "GoTo");
        helloworld.put("Page", String.format("%d Fit", page));
        outlines.add(helloworld);
        // update page count
        page += reader1.getNumberOfPages();
        // add the second document
        PdfReader reader2 = new PdfReader(SRC2);
        copy.addDocument(reader2);
        // add the second outline element as a kid of the first one
        ArrayList<HashMap<String, Object>> kids = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> link1 = new HashMap<String, Object>();
        link1.put("Title", "link1");
        link1.put("Action", "GoTo");
        link1.put("Page", String.format("%d Fit", page));
        kids.add(link1);
        helloworld.put("Kids", kids);
        // update page count
        page += reader2.getNumberOfPages();
        // add the third document
        PdfReader reader3 = new PdfReader(SRC3);
        copy.addDocument(reader3);
        // add the third outline element to the root
        HashMap<String, Object> link2 = new HashMap<String, Object>();
        link2.put("Title", "Link 2");
        link2.put("Action", "GoTo");
        link2.put("Page", String.format("%d Fit", page));
        outlines.add(link2);
        // add the outlines
        copy.setOutlines(outlines);
        // close the document
        document.close();
        reader1.close();
        reader2.close();
        reader3.close();
    }
}
