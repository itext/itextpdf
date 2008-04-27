package classroom.filmfestival_c;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.database.MySessionFactory;
import classroom.festivaldatabase.FestivalScreening;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class Movies16 extends AbstractMovies {


	public static final String RESULT = "results/classroom/filmfestival/movies16.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies16.class.getName());
	
	@SuppressWarnings("unchecked")
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
			PdfPTable table;
			PdfPCell cell;
			Chunk imdb;
			
			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("select distinct festival.id.day from FestivalScreening as festival order by festival.id.day");
			java.util.List<Date> days = q.list();
			java.util.List<FestivalScreening> screenings;
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date;
			for (Date day : days) {
				date = df.format(day);
				if (!date.startsWith("2007")) continue;
				
				table = new PdfPTable(new float[]{ 7, 1, 2, 1 });
			    cell = new PdfPCell(new Phrase(date, NORMALWHITE));
			    cell.setBackgroundColor(BLACK);
			    cell.setColspan(4);
			    table.addCell(cell);
				
				q = session.createQuery(String.format("from FestivalScreening where id.day='%s' order by id.time, id.place", date));
				screenings = q.list();
				for (FestivalScreening screening : screenings) {
					table.addCell(screening.getFilmTitle().getTitle());
				    table.addCell(screening.getId().getPlace().toString());
				    cell = new PdfPCell(new Phrase(screening.getId().getTime().toString()));
				    if (screening.getPress() == 1) {
				    	cell.setBackgroundColor(SILVER);
				    }
				    table.addCell(cell);
				    if (screening.getFilmTitle().getImdb().startsWith("?")) {
				    	table.addCell("");
				    }
				    else {
				        imdb = new Chunk("imdb");
				        imdb.setAnchor("http://imdb.com/title/tt" + screening.getFilmTitle().getImdb());
				        table.addCell(new Phrase(imdb));
				    }
				}
				
				document.add(table);
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
