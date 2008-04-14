/* in_action/chapter04/FoobarFlyer.java */

package in_action.chapter04;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarFlyer extends DefaultHandler {

	/** The Document to which the content is written. */
	protected Document document;

	/**
	 * This is a <CODE>Stack</CODE> of objects, waiting to be added to the
	 * document.
	 */
	protected Stack stack;

	/** This is the current chunk to which characters can be added. */
	protected Chunk currentChunk = null;

	/** Fontsizes */
	protected int[] FONTSIZES = { 24, 18, 16, 14, 12, 10 };

	/**
	 * Creates a new FoobarHtmlHandler.
	 * 
	 * @param html
	 * @param pdf
	 * @throws DocumentException
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SAXException
	 * @throws IOException
	 */
	public FoobarFlyer(String html, String pdf) throws DocumentException,
			ParserConfigurationException, FactoryConfigurationError,
			FileNotFoundException, SAXException, IOException {
		document = new Document();
		stack = new Stack();
		PdfWriter.getInstance(document, new FileOutputStream(pdf));
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(new InputSource(new FileInputStream(html)), this);
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String content = new String(ch, start, length);
		if (content.trim().length() == 0)
			return;
		if (currentChunk == null) {
			currentChunk = new Chunk(content.trim());
		} else {
			currentChunk.append(" ");
			currentChunk.append(content.trim());
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			if (document.isOpen()) {
				updateStack();
				for (int i = 0; i < 6; i++) {
					if (HtmlTags.H[i].equals(qName)) {
						flushStack();
						stack.push(new Paragraph(Float.NaN, "", new Font(
								Font.HELVETICA, FONTSIZES[i], Font.UNDEFINED,
								new CMYKColor(0.9f, 0.7f, 0.4f, 0.1f))));
						return;
					}
				}
				if ("blockquote".equals(qName)) {
					flushStack();
					Paragraph p = new Paragraph();
					p.setIndentationLeft(50);
					p.setIndentationRight(20);
					stack.push(p);
				} else if (HtmlTags.ANCHOR.equals(qName)) {
					Anchor anchor = new Anchor("", new Font(Font.HELVETICA,
							Font.UNDEFINED, Font.UNDEFINED, new CMYKColor(0.9f,
									0.7f, 0.4f, 0.1f)));
					anchor
							.setReference(attributes
									.getValue(HtmlTags.REFERENCE));
					stack.push(anchor);
				} else if (HtmlTags.ORDEREDLIST.equals(qName)) {
					stack.push(new List(List.ORDERED, 10));
				} else if (HtmlTags.UNORDEREDLIST.equals(qName)) {
					stack.push(new List(List.UNORDERED, 10));
				} else if (HtmlTags.LISTITEM.equals(qName)) {
					stack.push(new ListItem());
				} else if (HtmlTags.IMAGE.equals(qName)) {
					handleImage(attributes);
				}
			} else if (HtmlTags.BODY.equals(qName)) {
				document.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			if (document.isOpen()) {
				updateStack();
				for (int i = 0; i < 6; i++) {
					if (HtmlTags.H[i].equals(qName)) {
						flushStack();
						return;
					}
				}
				if ("blockquote".equals(qName)
						|| HtmlTags.ORDEREDLIST.equals(qName)
						|| HtmlTags.UNORDEREDLIST.equals(qName)) {
					flushStack();
				} else if (HtmlTags.NEWLINE.equals(qName)) {
					currentChunk = Chunk.NEWLINE;
					updateStack();
				} else if (HtmlTags.LISTITEM.equals(qName)) {
					ListItem listItem = (ListItem) stack.pop();
					List list = (List) stack.pop();
					list.add(listItem);
					stack.push(list);
				} else if (HtmlTags.ANCHOR.equals(qName)) {
					Anchor anchor = (Anchor) stack.pop();
					try {
						TextElementArray previous = (TextElementArray) stack
								.pop();
						previous.add(anchor);
						stack.push(previous);
					} catch (EmptyStackException es) {
						document.add(anchor);
					}
				} else if (HtmlTags.HTML.equals(qName)) {
					flushStack();
					document.close();
				}
			} else {
				if (HtmlTags.TITLE.equals(qName)) {
					document.addTitle(currentChunk.getContent().trim());
				}
				currentChunk = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * If the currentChunk is not null, it is forwarded to the stack and made
	 * null.
	 */
	private void updateStack() {
		if (currentChunk != null) {
			TextElementArray current;
			try {
				current = (TextElementArray) stack.pop();
				if (!(current instanceof Paragraph)
						|| !((Paragraph) current).isEmpty())
					current.add(new Chunk(" "));
			} catch (EmptyStackException ese) {
				current = new Paragraph();
			}
			current.add(currentChunk);
			stack.push(current);
			currentChunk = null;
		}
	}

	/**
	 * Flushes the stack, adding al objects in it to the document.
	 */
	private void flushStack() {
		try {
			while (stack.size() > 0) {
				Element element = (Element) stack.pop();
				try {
					TextElementArray previous = (TextElementArray) stack.pop();
					previous.add(element);
					stack.push(previous);
				} catch (EmptyStackException es) {
					document.add(element);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Handles the attributes of an IMG tag.
	 * 
	 * @param attributes
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws DocumentException
	 */
	private void handleImage(Attributes attributes)
			throws MalformedURLException, IOException, DocumentException {
		// do nothing for the moment
	}

	/**
	 * Generates a flyer in PDF for the new department of the Technological
	 * University of Foobar.
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(final String[] args) {
		System.out.println("Chapter 4: example Foobar Flyer");
		System.out.println("-> Creates a flyer for the T.U.F.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: foobar.html");
		System.out.println("-> resulting PDF: fancyflyer.pdf");
		try {
			new FoobarFlyer("resources/in_action/chapter04/foobar.html",
					"results/in_action/chapter04/fancyflyer.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}