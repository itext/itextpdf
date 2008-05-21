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
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class Movies14 {


	public static final String RESULT = "results/classroom/filmfestival/movies14.pdf";
	public static final Logger LOGGER = Logger.getLogger(Movies14.class.getName());
	
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
			cell = new PdfPCell();
			Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font italic = new Font(Font.HELVETICA, 12, Font.ITALIC);
			p = new Paragraph("FILMFESTIVAL", bold);
			p.setAlignment(Element.ALIGN_CENTER);
			cell.addElement(p);
			cell.setColspan(2);
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell();
			cell.setFixedHeight(20);
			cell.setColspan(2);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setCellEvent(new Movies14().new PageCell());
			table.addCell(cell);
			table.setHeaderRows(2);
			table.setFooterRows(1);
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
	
	class PageCell implements PdfPCellEvent {

		private BaseFont bf;
		
		public PageCell() throws DocumentException, IOException {
			bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		}
		
		@Override
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte text = canvas[PdfPTable.TEXTCANVAS];
			PdfWriter writer = text.getPdfWriter();
			text.beginText();
			text.setFontAndSize(bf, 12);
			text.showTextAligned(Element.ALIGN_CENTER, "Page " + writer.getPageNumber(),
					(rect.getRight() + rect.getLeft()) / 2, rect.getBottom() + 5, 0);
			text.endText();
		}
		
	}
}
