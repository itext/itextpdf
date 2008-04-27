package classroom.filmfestival_c;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.database.MySessionFactory;
import classroom.festivaldatabase.DirectorName;
import classroom.festivaldatabase.FestivalEntry;
import classroom.festivaldatabase.FestivalScreening;
import classroom.festivaldatabase.FestivalScreeningId;
import classroom.festivaldatabase.FilmTitle;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class Movies17 extends AbstractMovies {

	public static final String RESULT = "results/classroom/filmfestival/movies17.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies17.class.getName());
	
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
			
			Query query = session.createQuery("from FestivalScreening where id.day=? order by id.time, id.place");
			for (Date day : days) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(day);
				if (gc.get(GregorianCalendar.YEAR) != YEAR) continue;
				document.add(getTable(query, day));
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
	
	@SuppressWarnings("unchecked")
	protected static Element getTable(Query q, Date date) throws DocumentException {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		
	    PdfPCell cell = new PdfPCell(new Phrase(date.toString(), NORMALWHITE));
	    cell.setBackgroundColor(BLACK);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(cell);
	    
		q.setDate(0, date);
		java.util.List<FestivalScreening> screenings = q.list();
		for (FestivalScreening screening : screenings) {
			addScreening(table, screening);
		}
		return table;
	}

	protected static void addScreening(PdfPTable outer,
			FestivalScreening screening) throws DocumentException {
		FilmTitle movie = screening.getFilmTitle();
		// we construct a 3 column table
		PdfPTable table = new PdfPTable(3);
		table.setWidths(WIDTHS);
		// the first cell with the full title spans all the columns
		PdfPCell cell = new PdfPCell();
		cell.addElement(fullTitle(screening));
		cell.setColspan(3);
		cell.setBorder(PdfPCell.NO_BORDER);
		setColor(movie, cell);
		table.addCell(cell);
		
		cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		table.addCell(cell);
		
		cell = new PdfPCell();
		cell.addElement(directors(movie));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		table.addCell(cell);
		
		cell = new PdfPCell();
		cell.addElement(screenings(screening));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		table.addCell(cell);
		
		outer.addCell(table);
		
	}

	protected static Element fullTitle(FestivalScreening screening) throws DocumentException {
		FilmTitle movie = screening.getFilmTitle();
		// a table with 3 cells
		PdfPTable table = new PdfPTable(3);
		table.setWidths(INNER);
		table.setWidthPercentage(100);
		
		// the title(s)
		Paragraph p = new Paragraph();
		p.add(new Phrase(movie.getTitle(), BOLD));
		p.setLeading(16);
		// maybe an alternative title
		if (movie.getATitle().trim().length() > 0) {
			p.add(new Phrase(" (" + movie.getATitle() + ")"));
		}
		PdfPCell cell = new PdfPCell();
		cell.addElement(p);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		table.addCell(cell);
		
		// eXploreZone?
		cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		if (isExploreZone(movie)) {
			cell.setBackgroundColor(WHITE);
			cell.setUseAscender(true);
			cell.setUseDescender(true);
			cell.addElement(new Paragraph("eXplore"));
		}
		table.addCell(cell);
		
		// Duration / shortfilm
		cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setBackgroundColor(WHITE);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		StringBuffer buf = new StringBuffer();
		buf.append(movie.getDuration());
		buf.append('\'');
		if (getExtra(screening) > 0) {
			buf.append(" + KF");
			buf.append(getExtra(screening));
			buf.append('\'');
		}
		p = new Paragraph(buf.toString());
		p.setAlignment(Element.ALIGN_CENTER);
		cell.addElement(p);
		table.addCell(cell);
		return table;
	}

	@SuppressWarnings("unchecked")
	protected static boolean isExploreZone(FilmTitle movie) {
		Set<FestivalEntry> entries = movie.getFestivalEntries();
		for (FestivalEntry entry : entries) {
			if (entry.getId().getYear() == YEAR
					&& entry.getExplorezone() == 1)
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	protected static void setColor(FilmTitle movie, PdfPCell cell) {
		Set<FestivalEntry> entries = movie.getFestivalEntries();
		for (FestivalEntry entry : entries) {
			if (entry.getId().getYear() == YEAR) {
				cell.setBackgroundColor(COLOR[entry.getFilmCategory().getCategoryId()]);
				return;
			}
		}
	}

	protected static int getExtra(FestivalScreening screening) {
		return screening.getShortfilm();
	}

	@SuppressWarnings("unchecked")
	protected static Element directors(FilmTitle movie) {
		Set<DirectorName> directors = movie.getDirectorNames();
		if (directors.size() == 0) {
			Paragraph p = new Paragraph("various directors");
			p.setLeading(16);
			return p;
		}
		if (directors.size() == 1) {
			DirectorName name = directors.iterator().next();
			Paragraph p = new Paragraph(name.getName());
			p.setLeading(16);
			return p;
		}
		List list = new List(List.UNORDERED, 10);
		ListItem li;
		for (DirectorName director : directors) {
			li = new ListItem(director.getName());
			li.setLeading(16);
			list.add(li);
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected static Element screenings(FestivalScreening screening) {
		// the specific screening
		Paragraph p = getScreening(screening, BOLDSMALL);
		p.setLeading(16);
		
		FilmTitle movie = screening.getFilmTitle();
		Set<FestivalScreening> screenings = movie.getFestivalScreenings();
		if (screenings.size() == 1) {
			return p;
		}
		// the alternative screenings
		List list = new List(List.UNORDERED, 10);
		list.add(new ListItem(p));
		ListItem li;
		for (FestivalScreening ascreening : screenings) {
			if (ascreening.getId().equals(screening.getId())) continue;
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(ascreening.getId().getDay());
			if (gc.get(GregorianCalendar.YEAR) != YEAR) continue;
			li = new ListItem(getScreening(ascreening, SMALL));
			li.setLeading(12);
			list.add(li);
		}
		return list;
	}
	
	protected static Paragraph getScreening(FestivalScreening screening,
			Font font) {
		FestivalScreeningId id = screening.getId();
		Paragraph p = new Paragraph(id.getPlace(), font);
		p.add(new Chunk(": "));
		p.add(new Chunk(id.getDay().toString()));
		p.add(new Chunk(" "));
		p.add(new Chunk(id.getTime().toString()));
		if (screening.getPress() == 1)
			p.add(new Chunk(" (press only)"));
		return p;
	}
}
