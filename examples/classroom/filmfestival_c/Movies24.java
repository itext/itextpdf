package classroom.filmfestival_c;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.database.MySessionFactory;
import classroom.festivaldatabase.DirectorName;
import classroom.festivaldatabase.FestivalEntry;
import classroom.festivaldatabase.FestivalScreening;
import classroom.festivaldatabase.FilmTitle;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSmartCopy;
import com.lowagie.text.pdf.PdfStamper;

public class Movies24 {

	public static final String DATASHEET = "resources/classroom/filmfestival/datasheet.pdf";
	public static final String RESULT = "results/classroom/filmfestival/movies24.pdf";
	public final static Logger LOGGER = Logger.getLogger(Movies24.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Session session = null;
		try {
			session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("from FilmTitle order by title");
			List<FilmTitle> results = q.list();
			Document document = new Document();
			PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(RESULT));
			document.open();
			PdfReader reader;
			PdfStamper stamper;
			AcroFields form;
			ByteArrayOutputStream baos;
			for (FilmTitle movie : results) {
				baos = new ByteArrayOutputStream();
				reader = new PdfReader(DATASHEET);
				stamper = new PdfStamper(reader, baos);
				form = stamper.getAcroFields();
				form.setField("title", movie.getTitle());
				form.setField("director", getDirectors(movie));
				form.setField("year", String.valueOf(movie.getYear()));
				form.setField("duration", String.valueOf(movie.getDuration()));
				form.setField("category", "c" + getCategory(movie));
				Set<FestivalScreening> screenings = (Set<FestivalScreening>) movie.getFestivalScreenings();
				for (FestivalScreening screening : screenings) {
					form.setField(screening.getId().getPlace(), "Yes");
				}
				stamper.setFormFlattening(true);
				stamper.close();
				reader = new PdfReader(baos.toByteArray());
				copy.addPage(copy.getImportedPage(reader, 1));
			}
			document.close();
		} catch (HibernateException e) {
			LOGGER.warn("HibernateException: " + e);
		} catch (IOException e) {
			LOGGER.warn("IOException: " + e);
		} catch (DocumentException e) {
			LOGGER.warn("DocumentException: " + e);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (HibernateException e) {
				LOGGER.warn("HibernateTest - Closing session: " + e);
			}
		}		
	}

	@SuppressWarnings("unchecked")
	private static String getDirectors(FilmTitle movie) {
		Set<DirectorName> directors = movie.getDirectorNames();
		StringBuffer buf = new StringBuffer();
		for (DirectorName director : directors) {
			buf.append(director.getName());
			buf.append(',');
			buf.append(' ');
		}
		int i = buf.length();
		if (i > 0)
			buf.delete(i - 2, i);
		return buf.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected static int getCategory(FilmTitle movie) {
		Set<FestivalEntry> entries = movie.getFestivalEntries();
		for (FestivalEntry entry : entries) {
			if (entry.getId().getYear() == 2007) {
				return entry.getFilmCategory().getCategoryId();
			}
		}
		return 0;
	}
}
