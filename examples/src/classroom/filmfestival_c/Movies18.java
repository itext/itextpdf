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
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.database.MySessionFactory;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public class Movies18 extends Movies17 {

	public static final String RESULT = "results/classroom/filmfestival/movies18.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies18.class.getName());
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void main(String[] args) {
	
		// step 1
		Document document = new Document();
		try {
			// step 2
			OutputStream os = new FileOutputStream(RESULT);
			PdfWriter.getInstance(document, os);
			// step 3
			document.open();
			// step 4
			
			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("select distinct festival.id.day from FestivalScreening as festival order by festival.id.day");
			java.util.List<Date> days = q.list();
			
			Chapter chapter;
			Query query = session.createQuery("from FestivalScreening where id.day=? order by id.time, id.place");
			for (Date day : days) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(day);
				if (gc.get(GregorianCalendar.YEAR) != YEAR) continue;
				chapter = new Chapter("", 0);
				chapter.setNumberDepth(0);
				chapter.setBookmarkTitle(day.toString());
				chapter.add(getTable(query, day));
				document.add(chapter);
			}
			
			// step 5
			document.close();
		} catch (IOException e) {
			LOGGER.error("IOException: ", e);
		} catch (DocumentException e) {
			LOGGER.error("DocumentException: ", e);
		}
	}
}
