/* in_action/chapter14/RomeoJuliet.java */

package in_action.chapter14;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ElementTags;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.html.Markup;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.SAXmyHandler;
import com.lowagie.text.xml.TagMap;
import com.lowagie.text.xml.XmlPeer;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class RomeoJuliet {

	/**
	 * This object contains a speaker and a number of occurrances in the play
	 */

	class Speaker implements Comparable {

		// name of the speaker
		private String name;

		// number of occurrances
		private int occurrance = 1;

		/**
		 * One of the speakers in the play.
		 * 
		 * @param name
		 */
		public Speaker(String name) {
			this.name = name;
		}

		/**
		 * Gets the name of the speaker.
		 * 
		 * @return a name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the number of occurances of the speaker.
		 * 
		 * @return a number of textblocks
		 */
		public int getOccurrance() {
			return occurrance;
		}

		/**
		 * There is something odd going on in this compareTo. Do you see it?
		 * 
		 * @param o
		 *            an other speaker object
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			Speaker otherSpeaker = (Speaker) o;
			if (otherSpeaker.getName().equals(name)) {
				this.occurrance += otherSpeaker.getOccurrance();
				otherSpeaker.occurrance = this.occurrance;
				return 0;
			}
			return name.compareTo(otherSpeaker.getName());
		}
	}

	/**
	 * This is an example of a PageEvents class you should write. This is an
	 * inner class to keep all the code of the example in one file. If you want
	 * to use a PageEvent, you may want to put the code in a separate class.
	 */

	class MyPageEvents extends PdfPageEventHelper {

		/** we will keep a list of speakers */
		TreeSet speakers = new TreeSet();

		/** This is the contentbyte object of the writer */
		PdfContentByte cb;

		/** we will put the final number of pages in a template */
		PdfTemplate template;

		/** this is the BaseFont we are going to use for the header / footer */
		BaseFont bf = null;

		/** this is the current act of the play */
		String act = "";

		/**
		 * Every speaker will be tagged, so that he can be added to the list of
		 * speakers.
		 * 
		 * @see com.lowagie.text.pdf.PdfPageEventHelper#onGenericTag(com.lowagie.text.pdf.PdfWriter,
		 *      com.lowagie.text.Document, com.lowagie.text.Rectangle,
		 *      java.lang.String)
		 */
		public void onGenericTag(PdfWriter writer, Document document,
				Rectangle rect, String text) {
			speakers.add(new Speaker(text));
		}

		/**
		 * The first thing to do when the document is opened, is to define the
		 * BaseFont, get the Direct Content object and create the template that
		 * will hold the final number of pages.
		 * 
		 * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
		 *      com.lowagie.text.Document)
		 */
		public void onOpenDocument(PdfWriter writer, Document document) {
			try {
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
						BaseFont.NOT_EMBEDDED);
				cb = writer.getDirectContent();
				template = cb.createTemplate(50, 50);
				writer.setLinearPageMode();
			} catch (DocumentException de) {
			} catch (IOException ioe) {
			}
		}

		/**
		 * Every ACT is seen as a Chapter. We get the title of the act, so that
		 * we can display it in the header.
		 * 
		 * @see com.lowagie.text.pdf.PdfPageEventHelper#onChapter(com.lowagie.text.pdf.PdfWriter,
		 *      com.lowagie.text.Document, float, com.lowagie.text.Paragraph)
		 */
		public void onChapter(PdfWriter writer, Document document,
				float paragraphPosition, Paragraph title) {
			act = title.getContent();
		}

		/**
		 * After the content of the page is written, we put page X of Y at the
		 * bottom of the page and we add either "Romeo and Juliet" of the title
		 * of the current act as a header.
		 * 
		 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter,
		 *      com.lowagie.text.Document)
		 */
		public void onEndPage(PdfWriter writer, Document document) {
			int pageN = writer.getPageNumber();
			String text = "Page " + pageN + " of ";
			float len = bf.getWidthPoint(text, 8);
			cb.beginText();
			cb.setFontAndSize(bf, 8);
			cb.setTextMatrix(280, 30);
			cb.showText(text);
			cb.endText();
			cb.addTemplate(template, 280 + len, 30);
			cb.beginText();
			cb.setFontAndSize(bf, 8);
			cb.setTextMatrix(280, 820);
			if (pageN % 2 == 1) {
				cb.showText("Romeo and Juliet");
			} else {
				cb.showText(act);
			}
			cb.endText();
		}
	}

	/**
	 * Normally you either choose to use a HashMap with XmlPeer objects, or a
	 * TagMap object that reads a TagMap in XML. Here we used a hybrid solution
	 * (for educational purposes only!) with on one side the tags in the XML
	 * tagmap, on the other side an XmlPeer object that overrides the properties
	 * of one of the tags.
	 */

	class RomeoJulietMap extends TagMap {

		private static final long serialVersionUID = -581399109376697694L;

		/**
		 * Constructs a TagMap based on an XML file and/or on XmlPeer objects
		 * that are added.
		 * 
		 * @param tagmap
		 *            the path to an xml file
		 * @throws IOException
		 */
		public RomeoJulietMap(String tagmap) throws IOException {
			super(new FileInputStream(tagmap));
			XmlPeer peer = new XmlPeer(ElementTags.CHUNK, "SPEAKER");
			peer.addValue(Markup.CSS_KEY_FONTSIZE, "10");
			peer.addValue(Markup.CSS_KEY_FONTWEIGHT,
					Markup.CSS_VALUE_BOLD);
			peer.addValue(ElementTags.GENERICTAG, "");
			put(peer.getAlias(), peer);
		}
	}

	/**
	 * Special implementation of het XML handler. It adds a paragraph after each
	 * SPEAKER block and avoids closing the document after the final closing
	 * tag.
	 */
	class MyHandler extends SAXmyHandler {

		/**
		 * We have to override the constructor
		 * 
		 * @param document
		 *            the Document object
		 * @param tagmap
		 *            the tagmap
		 * @throws IOException
		 * @throws DocumentException
		 */
		public MyHandler(Document document, HashMap tagmap)
				throws DocumentException, IOException {
			super(document, tagmap);
		}

		/**
		 * We only alter the handling of some endtags.
		 * 
		 * @param uri
		 *            the uri of the namespace
		 * @param lname
		 *            the local name of the tag
		 * @param name
		 *            the name of the tag
		 */
		public void endElement(String uri, String lname, String name) {
			if (myTags.containsKey(name)) {
				XmlPeer peer = (XmlPeer) myTags.get(name);
				// we don't want the document to be close
				// because we are going to add a page after the xml is parsed
				if (isDocumentRoot(peer.getTag())) {
					return;
				}
				handleEndingTags(peer.getTag());
				// we want to add a paragraph after the speaker chunk
				if ("SPEAKER".equals(name)) {
					try {
						TextElementArray previous = (TextElementArray) stack
								.pop();
						previous.add(new Paragraph(16));
						stack.push(previous);
					} catch (EmptyStackException ese) {
					}
				}
			} else {
				handleEndingTags(name);
			}
		}
	}

	/**
	 * Example that takes an XML file, converts it to PDF and adds all kinds of
	 * extra's, such as an alternating header, a footer with page x of y, a page
	 * with metadata,...
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) {

		System.out.println("Chapter 14: Romeo and Juliet");
		System.out.println("-> Creates a PDF file that converts a play in XML");
		System.out.println("   to PDF and adds extra stuff using page events.");
		System.out.println("-> jars needed: iText.jar");
		System.out
				.println("-> resources needed: romeo_juliet.xml and tagmap.xml");
		System.out.println("-> resulting PDF: romeo_juliet.pdf");

		RomeoJuliet rj = new RomeoJuliet();
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4, 80, 50, 30, 65);

		try {
			// step 2:
			// we create a writer that listens to the document
			// and directs a XML-stream to a file
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/romeo_juliet.pdf"));

			// create add the event handler
			MyPageEvents events = rj.new MyPageEvents();
			writer.setPageEvent(events);

			// step 3: we create a parser and set the document handler
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			// step 4: we parse the document
			RomeoJulietMap tagmap = rj.new RomeoJulietMap(
					"resources/in_action/chapter14/tagmap.xml");
			parser.parse("resources/in_action/chapter14/romeo_juliet.xml", rj.new MyHandler(
					document, tagmap));

			int end_play = writer.getPageNumber();
			events.template.beginText();
			events.template.setFontAndSize(events.bf, 8);
			events.template.showText(String.valueOf(end_play));
			events.template.endText();

			document.newPage();
			writer.setPageEvent(null);

			Speaker speaker;
			for (Iterator i = events.speakers.iterator(); i.hasNext();) {
				speaker = (Speaker) i.next();
				document.add(new Paragraph(speaker.getName() + ": "
						+ speaker.getOccurrance() + " speech blocks"));
			}
			int end_doc = writer.getPageNumber();
			int[] reorder = new int[end_doc];
			for (int i = 0; i < reorder.length; i++) {
				reorder[i] = i + end_play + 1;
				if (reorder[i] > end_doc)
					reorder[i] -= end_doc;
			}
			document.newPage();
			writer.reorderPages(reorder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
	}
}