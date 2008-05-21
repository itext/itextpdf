/* in_action/chapter14/SimpleLetter.java */

package in_action.chapter14;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.XmlParser;
import com.lowagie.text.xml.XmlPeer;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SimpleLetter extends PdfPageEventHelper {

	/** The template for the letter. */
	protected PdfImportedPage paper;

	/** The layer to which the template will be added. */
	protected PdfLayer not_printed;

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter14/simple_letter.pdf");
			paper = writer.getImportedPage(reader, 1);
			not_printed = new PdfLayer("template", writer);
			not_printed.setOnPanel(false);
			not_printed.setPrint("Print", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onStartPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.beginLayer(not_printed);
		cb.addTemplate(paper, 0, 0);
		cb.endLayer();
	}

	/**
	 * Returns a HashMap that can be used as tagmap.
	 */
	public static HashMap getTagMap(String givenname, String name, String mail,
			String site) {
		HashMap tagmap = new HashMap();

		XmlPeer peer = new XmlPeer(ElementTags.ITEXT, "letter");
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "givenname");
		peer.setContent(givenname);
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "name");
		peer.setContent(name);
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.CHUNK, "mail");
		peer.setContent(mail);
		tagmap.put(peer.getAlias(), peer);

		peer = new XmlPeer(ElementTags.ANCHOR, "website");
		peer.setContent(site);
		peer.addValue(ElementTags.REFERENCE, site);
		peer.addValue(ElementTags.COLOR, "#0000FF");
		tagmap.put(peer.getAlias(), peer);

		return tagmap;
	}

	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: Simple Letter");
		System.out.println("-> Creates a PDF file with a header and a footer.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: simple_letter.xml and iTextLogo.gif");
		System.out.println("-> file generated in /results subdirectory:");
		System.out.println("   simple_letter.pdf, simple_letter1.pdf, simple_letter2.pdf");
		// step 1: creation of a document-object
		createMailPaper();
		try {
			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/simple_letter1.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			writer.setViewerPreferences(PdfWriter.PrintScalingNone);
			writer.setPageEvent(new SimpleLetter());
			XmlParser.parse(document, "resources/in_action/chapter14/simple_letter.xml",
					getTagMap("Bruno", "Lowagie", "bruno@lowagie.com",
							"http://www.lowagie.com/"));
			document = new Document(PageSize.A4);
			writer = PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter14/simple_letter2.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			writer.setViewerPreferences(PdfWriter.PrintScalingNone);
			writer.setPageEvent(new SimpleLetter());
			XmlParser.parse(document, "resources/in_action/chapter14/simple_letter.xml",
					getTagMap("Paulo", "Soares", "psoares@consiste.pt",
							"http://itextpdf.sourceforge.net/"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	public static void createMailPaper() {
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/simple_letter.pdf"));
			document.open();
			Image img = Image
					.getInstance("resources/in_action/chapter10/iTextLogo.gif");
			PdfContentByte cb = writer.getDirectContent();
			cb.setColorStroke(Color.orange);
			cb.setLineWidth(2);
			cb.rectangle(20, 20, document.getPageSize().getWidth() - 40, document
					.getPageSize().getHeight() - 40);
			cb.stroke();
			PdfPTable table = new PdfPTable(2);
			Phrase p = new Phrase();
			Chunk ck = new Chunk("lowagie.com\n", new Font(Font.TIMES_ROMAN,
					16, Font.BOLDITALIC, Color.blue));
			p.add(ck);
			ck = new Chunk("Ghent\nBelgium", new Font(Font.HELVETICA, 12,
					Font.NORMAL, Color.darkGray));
			p.add(ck);
			table.getDefaultCell().setBackgroundColor(Color.yellow);
			table.getDefaultCell().setBorderWidth(0);
			table.addCell(p);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(new Phrase(new Chunk(img, 0, 0)));
			table.setTotalWidth(document.right() - document.left());
			table.writeSelectedRows(0, -1, document.left(), document
					.getPageSize().getHeight() - 50, cb);
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}