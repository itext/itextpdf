/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.filmfestival_c;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import classroom.database.MySessionFactory;
import classroom.festivaldatabase.FestivalEntry;
import classroom.festivaldatabase.FestivalScreening;
import classroom.festivaldatabase.FilmTitle;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class Movies19 extends AbstractMovies {

	public static final String RESULT = "results/classroom/filmfestival/movies19.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies19.class.getName());
	
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
			// step 3
			document.open();
			// step 4

			Session session = (Session) MySessionFactory.currentSession();
			Query q = session.createQuery("select distinct festival.id.day from FestivalScreening as festival order by festival.id.day");
			java.util.List<Date> days = q.list();
			
			for (Date day : days) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(day);
				if (gc.get(GregorianCalendar.YEAR) != YEAR) continue;
				createSheet(session, day, writer);
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
	protected static void createSheet(Session session, Date date,
			PdfWriter writer) {
		Query q;
		q = session.createQuery("select distinct screening.id.place from FestivalScreening as screening where id.day=? order by screening.id.place");
		q.setDate(0, date);
		List<String> places = q.list();
		
		Rectangle art = writer.getBoxSize("art");
		// initialize the dimensions for this day
	    float top = art.getTop();
	    float row_height = art.getHeight() / places.size();
	    if (row_height > 70) {
	      row_height = 70;
	    }
	    float column_width = art.getWidth() / COLUMNS;
	    float left = art.getLeft() + column_width;
	    float minute = (2f * column_width) / 60f;
		
	    // content
	    drawGrid(writer, places, date.toString(), left, column_width, top, row_height);
	    
		q = session.createQuery("from FestivalScreening where id.day=? order by id.time, id.place");
		q.setDate(0, date);
		List<FestivalScreening> screenings = q.list();
	    for (FestivalScreening screening : screenings) {
	      try {
	    	  drawMovie(writer, screening, places, minute, left, top, row_height);
	      } catch (DocumentException e) {
	    	  LOGGER.error("DocumentException: ", e);
	      }
	    }
	}

	protected static void drawGrid(PdfWriter writer, List<String> places,
			String day, float left, float column_width, float top, float row_height) {

		// CANVAS
		PdfContentByte directcontent = writer.getDirectContent();
		Rectangle art = writer.getBoxSize("art");
		
		// LINES
		
		directcontent.setLineWidth(1);
		float bottom = art.getTop();
		
		// rows
		int rows = places.size();
		for (int i = 0; i <= rows; i++) {
			directcontent.moveTo(art.getLeft(), art.getTop() - (i * row_height));
			directcontent.lineTo(art.getRight(), art.getTop() - (i * row_height));
			bottom = art.getTop() - (i * row_height);
		}
		
		// Rectangle
		directcontent.moveTo(art.getLeft(), art.getTop());
		directcontent.lineTo(art.getLeft(), bottom);
		directcontent.moveTo(left, art.getTop());
		directcontent.lineTo(left, bottom);
		directcontent.moveTo(art.getLeft() + (COLUMNS * column_width), art.getTop());
		directcontent.lineTo(art.getLeft() + (COLUMNS * column_width), bottom);
		directcontent.stroke();
		
		// columns
		directcontent.saveState();
		directcontent.setLineWidth(0.3f);
		directcontent.setColorStroke(SILVER);
		directcontent.setLineDash(3, 1);
		for (int i = 2; i < COLUMNS; i++) {
			directcontent.moveTo(art.getLeft() + (i * column_width), art.getTop());
			directcontent.lineTo(art.getLeft() + (i * column_width), bottom);
		}
		directcontent.stroke();
		directcontent.restoreState();
		
		// TEXT
		
		// date
		directcontent.beginText();
		directcontent.setFontAndSize(FONT, 12);
		directcontent.showTextAligned(Element.ALIGN_RIGHT, day, art.getLeft() - 5, art.getTop(), 90);
		directcontent.endText();
		
		// time
		for (int i = 1; i < COLUMNS; i++) {
			directcontent.beginText();
			directcontent.setFontAndSize(FONT, 8);
			directcontent.showTextAligned(Element.ALIGN_LEFT, TIME[i - 1], art.getLeft() + (i * column_width) + 5, top + 5, 90);
			directcontent.endText();
		}
		
		// places
		for (int i = 0; i < rows; i++) {
			directcontent.beginText();
			directcontent.setFontAndSize(FONT, 12);
			directcontent.showTextAligned(Element.ALIGN_CENTER, places.get(i), art.getLeft() + 16, art.getTop() - ((i + 0.5f) * row_height), 90);
			directcontent.endText();
		}
	}

	protected static void drawMovie(PdfWriter writer, FestivalScreening screening,
			List<String> places, float minute, float left, float top, float row_height) throws DocumentException {
		FilmTitle movie = screening.getFilmTitle();
		// defining the rectangle
		int place = places.indexOf(screening.getId().getPlace());
		float llx, urx, lly, ury;
		llx = left + minute * getMinutesAfter930(screening);
		urx = llx + minute * (screening.getShortfilm() + movie.getDuration());
		ury = top - place * row_height;
		lly = top - ((place + 1) * row_height);
		
		// FOREGROUND
		PdfContentByte foreground = writer.getDirectContent();
		
		// rectangle
		foreground.setColorStroke(BLACK);
		foreground.setLineWidth(1);
		foreground.moveTo(urx, lly);
		foreground.lineTo(llx, lly);
		foreground.lineTo(llx, ury);
		foreground.lineTo(urx, ury);
		foreground.closePathStroke();
		// title
		ColumnText ct = new ColumnText(foreground);
		ct.setSimpleColumn(new Phrase(movie.getTitle(), SMALLEST), llx, lly, urx, ury, 14, Element.ALIGN_CENTER);
		ct.go();
		
		// BACKGROUND
		PdfContentByte background = writer.getDirectContentUnder();
		
		// draw background
		background.setColorFill(getColor(movie));
		background.moveTo(urx, lly);
		background.lineTo(llx, lly);
		background.lineTo(llx, ury);
		background.lineTo(urx, ury);
		background.fill();
		
		// draw a white P for press screenings
		if (screening.getPress() == 1) {
			background.saveState();
			background.beginText();
			background.setFontAndSize(FONT, 24);
			background.setColorFill(WHITE);
			background.showTextAligned(Element.ALIGN_CENTER, "P", (llx + urx) / 2f, (lly + ury) / 2f - 12, 0);
			background.endText();
			background.restoreState();
		}
		
		// draw shortfilm
		background.setColorStroke(WHITE);
		if (screening.getShortfilm() > 0) {
			background.moveTo(llx + (minute * screening.getShortfilm()), lly);
			background.lineTo(llx + (minute * screening.getShortfilm()), ury);
			background.stroke();
		}
	}

	@SuppressWarnings("unchecked")
	protected static Color getColor(FilmTitle movie) {
		Set<FestivalEntry> entries = movie.getFestivalEntries();
		for (FestivalEntry entry : entries) {
			if (entry.getId().getYear() == YEAR) {
				return COLOR[entry.getFilmCategory().getCategoryId()];
			}
		}
		return WHITE;
	}

	protected static float getMinutesAfter930(FestivalScreening screening) {
		long l = screening.getId().getTime().getTime() - TIME930;
		return (int) (l / 60000l);
	}
}
