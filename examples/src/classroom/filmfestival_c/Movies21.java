/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.filmfestival_c;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

public class Movies21 {

	public static final String RESULT = "results/classroom/filmfestival/movies21.pdf";
	public final static Logger LOGGER = Logger.getLogger(Movies21.class.getName());

	@SuppressWarnings("unchecked")
	public static void main(String args[]) {
		Movies18.main(args);
		Movies20.main(args);
		try {
			PdfReader reader1 = new PdfReader(Movies18.RESULT);
			List<Map> list1 = SimpleBookmark.getBookmark(reader1);
			int[] offsets1 = new int[list1.size() + 1];
			int count = 0;
			for (Map<String, String> mark : list1) {
				offsets1[count++] = getPageNumber(mark.get("Page"));
			}
			offsets1[count] = reader1.getNumberOfPages() + 1;
			PdfReader reader2 = new PdfReader(Movies20.RESULT);
			List<Map> list2 = SimpleBookmark.getBookmark(reader2);
			if (list2.size() != list1.size()) {
				throw new DocumentException("The documents don't have the same number of bookmark entries.");
			}
			int[] offsets2 = new int[list2.size() + 1];
			count = 0;
			for (Map<String, String> mark : list2) {
				offsets2[count++] = getPageNumber(mark.get("Page"));
			}
			offsets2[count] = reader2.getNumberOfPages() + 1;
			Document document = new Document();
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(RESULT));
			document.open();
			for (int i = 0; i < list1.size(); i++) {
				for (int j = offsets1[i]; j < offsets1[i + 1]; j++) {
					copy.addPage(copy.getImportedPage(reader1, j));
				}
				for (int j = offsets2[i]; j < offsets2[i + 1]; j++) {
					copy.addPage(copy.getImportedPage(reader2, j));
				}
			}
			document.close();
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e);
		} catch (DocumentException e) {
			LOGGER.warn("IOException: " + e);
		}
	}

	private static int getPageNumber(String string) {
		string = string.substring(0, string.indexOf(" "));
		return Integer.parseInt(string);
	}
}
