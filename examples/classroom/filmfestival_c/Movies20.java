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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;

public class Movies20 extends Movies19 {

	public static final String RESULT = "results/classroom/filmfestival/movies20.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies20.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// step 1
		Rectangle rect = PageSize.A4.rotate();
		Document document = new Document(rect);
		try {
			// step 2
			OutputStream os = new FileOutputStream(RESULT);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			Rectangle art = new Rectangle(rect.getLeft(36), rect.getBottom(36), rect.getRight(36), rect.getTop(36));
			writer.setBoxSize("art", art);
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			// step 3
			document.open();
			// step 4

			PdfOutline root = writer.getDirectContent().getRootOutline();
			
			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("select distinct festival.id.day from FestivalScreening as festival order by festival.id.day");
			java.util.List<Date> days = q.list();
			
			for (Date day : days) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(day);
				if (gc.get(GregorianCalendar.YEAR) != YEAR) continue;
				createSheet(session, day, writer);
				new PdfOutline(root, new PdfDestination(PdfDestination.FIT), day.toString());
				document.newPage();
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
