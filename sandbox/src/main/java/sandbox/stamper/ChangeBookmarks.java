/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24217657/set-inherit-zoomaction-property-to-bookmark-in-the-pdf-file
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.SimpleBookmark;

public class ChangeBookmarks {

    public static final String SRC = "resources/pdfs/bookmarks.pdf";
    public static final String DEST = "results/stamper/changed_bookmarks.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeBookmarks().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);
        changeList(list);
        stamper.setOutlines(list);
        stamper.close();
        reader.close();
    }
    
    public void changeList(List<HashMap<String, Object>> list) {
    	for (HashMap<String, Object> entry : list) {
    		for (String key : entry.keySet()) {
    			if ("Kids".equals(key)) {
    				Object o = entry.get(key);
    				changeList((List<HashMap<String, Object>>)o);
    			}
    			else if ("Page".equals(key)) {
    				String dest = (String)entry.get(key);
    				entry.put("Page", dest.replaceAll("Fit", "FitV 60"));
    			}
    		}
    	}
    }
}
