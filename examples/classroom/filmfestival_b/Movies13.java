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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class Movies13 {


	public static final String RESULT = "results/classroom/filmfestival/movies13.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies13.class.getName());
	
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
			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("from FilmTitle order by title");
			java.util.List<FilmTitle> results = q.list();
			
			PdfPTable table = new PdfPTable(2);
			table.setWidths(new float[]{ 1, 5 });
			File f;
			Paragraph p;
			Chunk c;
			PdfPCell cell;
			Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
			for (FilmTitle movie : results) {
				f = new File("resources/classroom/filmposters/" + movie.getFilmId() + ".jpg");
				if (f.exists()) {
					cell = new PdfPCell(Image.getInstance(f.getPath()), true);
					cell.setPadding(2);
				}
				else {
					cell = new PdfPCell();
				}
				table.addCell(cell);
				p = new Paragraph(20);
				c = new Chunk(movie.getTitle(), bold);
				c.setAnchor("http://cinema.lowagie.com/titel.php?id=" + movie.getFilmId());
				p.add(c);
				c = new Chunk(" (" + movie.getYear() + ") ", italic);
				p.add(c);
				c = new Chunk("IMDB");
				c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
				p.add(c);
				cell = new PdfPCell();
				cell.setUseAscender(true);
				cell.setUseDescender(true);
				cell.addElement(p);
				Set<DirectorName> directors = movie.getDirectorNames();
				List list = new List();
				for (DirectorName director : directors) {
					list.add(director.getName());
				}
				cell.addElement(list);
				table.addCell(cell);
			}
			document.add(table);
			// step 5
			document.close();
		} catch (IOException e) {
			LOGGER.error("IOException: ", e);
		} catch (DocumentException e) {
			LOGGER.error("DocumentException: ", e);
		}
	}
}
