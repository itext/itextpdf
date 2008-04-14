/* in_action/chapter14/SimpleLetter.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;

import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.SAXiTextHandler;
import com.lowagie.text.xml.XmlPeer;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SimpleLetters extends SAXiTextHandler {

	/**
	 * @param a
	 *            document listener
	 * @param tagmap
	 * @throws DocumentException
	 * @throws IOException
	 */
	public SimpleLetters(DocListener document) throws DocumentException,
			IOException {
		super(document);
	}

	/**
	 * @param tagmap
	 *            The tagmap to set.
	 */
	public void setTagMap(HashMap tagmap) {
		myTags = tagmap;
	}

	/**
	 * This method gets called when a start tag is encountered.
	 * 
	 * @param uri
	 *            the Uniform Resource Identifier
	 * @param lname
	 *            the local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed.
	 * @param name
	 *            the name of the tag that is encountered
	 * @param attrs
	 *            the list of attributes
	 */

	public void startElement(String uri, String lname, String name,
			Attributes attrs) {
		if (myTags.containsKey(name)) {
			XmlPeer peer = (XmlPeer) myTags.get(name);
			if (isDocumentRoot(peer.getTag())) {
				return;
			}
			handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
		} else {
			Properties attributes = new Properties();
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					String attribute = attrs.getQName(i);
					attributes.setProperty(attribute, attrs.getValue(i));
				}
			}
			handleStartingTags(name, attributes);
		}
	}

	/**
	 * This method gets called when an end tag is encountered.
	 * 
	 * @param uri
	 *            the Uniform Resource Identifier
	 * @param lname
	 *            the local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed.
	 * @param name
	 *            the name of the tag that ends
	 */

	public void endElement(String uri, String lname, String name) {
		if (myTags.containsKey(name)) {
			XmlPeer peer = (XmlPeer) myTags.get(name);
			if (isDocumentRoot(peer.getTag())) {
				while (!stack.empty()) {
					Element element = (Element) stack.pop();
					if (!stack.empty()) {
						TextElementArray previous = (TextElementArray) stack
								.pop();
						previous.add(element);
						stack.push(previous);
					} else {
						try {
							document.add(element);
						} catch (DocumentException e) {
							e.printStackTrace();
						}
					}
				}
				if (currentChunk != null) {
					try {
						document.add(currentChunk);
					} catch (DocumentException e) {
						e.printStackTrace();
					}
				}
				return;
			}
			handleEndingTags(peer.getTag());
		} else {
			handleEndingTags(name);
		}
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
		System.out.println("   simple_letter.pdf and simple_letters.pdf");
		// step 1: creation of a document-object
		SimpleLetter.createMailPaper();
		try {
			Document document = new Document(PageSize.A4, 36, 36, 144, 36);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/simple_letters.pdf"));
			writer.setPageEvent(new SimpleLetter());
			document.open();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			SimpleLetters handler = new SimpleLetters(document);
			handler.setTagMap(SimpleLetter.getTagMap("Bruno", "Lowagie",
					"bruno@lowagie.com", "http://www.lowagie.com/"));
			parser.parse("resources/in_action/chapter14/simple_letter.xml", handler);
			document.newPage();
			handler.setTagMap(SimpleLetter.getTagMap("Paulo", "Soares",
					"psoares@consiste.pt", "http://itextpdf.sourceforge.net/"));
			parser.parse("resources/in_action/chapter14/simple_letter.xml", handler);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}