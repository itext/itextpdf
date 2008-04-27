/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.filmfestival_b;

import java.io.File;
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
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;

public class Movies09 {


	public static final String RESULT = "results/classroom/filmfestival/movies09.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies09.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// step 1
		Document document = new Document();
		try {
			// step 2
			OutputStream os = new FileOutputStream(RESULT);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			// step 3
			document.open();
			// step 4
			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("from FilmTitle order by title");
			java.util.List<FilmTitle> results = q.list();
			
			ColumnText column = new ColumnText(writer.getDirectContent());
			column.setSimpleColumn(document.left(), document.bottom(),
					document.right(), document.top());
			
			float pos;
			int status;
			File f;
			Image img;
			Paragraph p;
			Chunk c;
			Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
			for (FilmTitle movie : results) {
				f = new File("resources/classroom/filmposters/" + movie.getFilmId() + ".jpg");
				if (f.exists()) {
					img = Image.getInstance(f.getPath());
					img.setWidthPercentage(0);
					img.scaleToFit(72, 144);
				}
				else {
					img = null;
				}
				p = new Paragraph(20);
				c = new Chunk(movie.getTitle(), bold);
				c.setAnchor("http://cinema.lowagie.com/titel.php?id=" + movie.getFilmId());
				p.add(c);
				c = new Chunk(" (" + movie.getYear() + ") ", italic);
				p.add(c);
				c = new Chunk("IMDB");
				c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
				p.add(c);
				Set<DirectorName> directors = movie.getDirectorNames();
				List list = new List();
				for (DirectorName director : directors) {
					list.add(director.getName());
				}
				
				if (img != null) column.addElement(img);
				column.addElement(p);
				column.addElement(list);
				pos = column.getYLine();
				status = column.go(true);
				if (ColumnText.hasMoreText(status)) {
					document.newPage();
					column.setText(null);
					column.setYLine(document.top());
				}
				else {
					column.setYLine(pos);
				}
				if (img != null) column.addElement(img);
				column.addElement(p);
				column.addElement(list);
				column.addElement(Chunk.NEWLINE);
				column.go();
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
