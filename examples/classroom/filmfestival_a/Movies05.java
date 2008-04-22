package classroom.filmfestival_a;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.database.MySessionFactory;
import classroom.festivaldatabase.DirectorName;
import classroom.festivaldatabase.FilmTitle;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class Movies05 {

	public static final String RESULT = "results/classroom/filmfestival/movies05.pdf";
	public final static Logger LOGGER = Logger.getLogger(Movies05.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// step 1
		Document document = new Document();
		try {
			// step 2
			OutputStream os = new FileOutputStream(RESULT);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			writer.setPageEvent(new Movies05().new Ellipse());
			// step 3
			document.open();
			// step 4
			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("from FilmTitle order by title");
			java.util.List<FilmTitle> results = q.list();
			Paragraph p;
			Chunk c;
			Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
			Font white = new Font(Font.HELVETICA, 12, Font.BOLD | Font.ITALIC, Color.WHITE);
			for (FilmTitle movie : results) {
				p = new Paragraph(20);
				c = new Chunk(movie.getTitle(), bold);
				c.setAnchor("http://cinema.lowagie.com/titel.php?id=" + movie.getFilmId());
				p.add(c);
				c = new Chunk(" (" + movie.getYear() + ") ", italic);
				p.add(c);
				c = new Chunk("IMDB", white);
				c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
				c.setGenericTag("ellipse");
				p.add(c);
				document.add(p);
				Set<DirectorName> directors = movie.getDirectorNames();
				List list = new List();
				for (DirectorName director : directors) {
					list.add(director.getName());
				}
				document.add(list);
			}
			// step 5
			document.close();
		} catch (IOException e) {
			LOGGER.error("IOException: ", e);
		} catch (DocumentException e) {
			LOGGER.error("DocumentException: ", e);
		}
	}

	class Ellipse extends PdfPageEventHelper {

		public void onGenericTag(PdfWriter writer, Document pdfDocument,
				Rectangle rect, String text) {
			PdfContentByte content = writer.getDirectContentUnder();
			content.saveState();
			content.setRGBColorFill(0, 0, 255);
			content.ellipse(rect.getLeft() - 3f, rect.getBottom() - 5f,
					rect.getRight() + 3f, rect.getTop() + 3f);
			content.fill();
			content.restoreState();
		}

	}
}
