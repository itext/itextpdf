/*
 * $Id$
 * $Name$
 * 
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *  
 */

package com.lowagie.text.pdf;

import com.lowagie.text.Anchor;
import com.lowagie.text.Annotation;
import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Element;
import com.lowagie.text.Chunk;
import com.lowagie.text.Graphic;
import com.lowagie.text.Header;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Meta;
import com.lowagie.text.Phrase;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.Watermark;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <CODE>PdfDocument</CODE> is the class that is used by <CODE>PdfWriter</CODE>
 * to translate a <CODE>Document</CODE> into a PDF with different pages.
 * <P>
 * A <CODE>PdfDocument</CODE> always listens to a <CODE>Document</CODE>
 * and adds the Pdf representation of every <CODE>Element</CODE> that is
 * added to the <CODE>Document</CODE>.
 *
 * @see		com.lowagie.text.Document
 * @see		com.lowagie.text.DocListener
 * @see		PdfWriter
 * 
 * @author  bruno@lowagie.com
 */

class PdfDocument extends Document implements DocListener {

	/**
	 * <CODE>PdfInfo</CODE> is the PDF InfoDictionary.
	 * <P>
	 * A document's trailer may contain a reference to an Info dictionary that provides information
	 * about the document. This optional dictionary may contain one or more keys, whose values
	 * should be strings.<BR>
	 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 6.10 (page 120-121)
	 *
	 * @author  bruno@lowagie.com
	 */

	public class PdfInfo extends PdfDictionary {

	// constructors

		/**
		 * Construct a <CODE>PdfInfo</CODE>-object.
		 */

		PdfInfo() {
			super();
			addProducer();
			addCreationDate();
		}

		/**
		 * Constructs a <CODE>PdfInfo</CODE>-object.
		 *
		 * @param		author		name of the author of the document
		 * @param		title		title of the document
		 * @param		subject		subject of the document
		 */

		PdfInfo(String author, String title, String subject) {
			this();
			addTitle(title);
			addSubject(subject);
			addAuthor(author);
		}

		/**
		 * Adds the title of the document.
		 *
		 * @param	title		the title of the document
		 */

		void addTitle(String title) {
			put(PdfName.TITLE, new PdfString(title));
		}				 

		/**
		 * Adds the subject to the document.
		 *
		 * @param	subject		the subject of the document
		 */

		void addSubject(String subject) {
			put(PdfName.SUBJECT, new PdfString(subject));
		}				 

		/**
		 * Adds some keywords to the document.
		 *
		 * @param	keywords		the keywords of the document
		 */

		void addKeywords(String keywords) {
			put(PdfName.KEYWORDS, new PdfString(keywords));
		}

		/**
		 * Adds the name of the author to the document.
		 *
		 * @param	author		the name of the author
		 */

		void addAuthor(String author) {
			put(PdfName.AUTHOR, new PdfString(author));
		}

		/**
		 * Adds the name of the producer to the document.
		 */

		void addProducer() {
			put(PdfName.PRODUCER, new PdfString("iText0.30 by Bruno Lowagie"));
		}

		/**
		 * Adds the date of creation to the document.
		 */

		void addCreationDate() {
			put(PdfName.CREATIONDATE, new PdfDate());
		}
	}

	/**
	 * <CODE>PdfCatalog</CODE> is the PDF Catalog-object.
	 * <P>
	 * The Catalog is a dictionary that is the root node of the document. It contains a reference
	 * to the tree of pages contained in the document, a reference to the tree of objects representing
	 * the document's outline, a reference to the document's article threads, and the list of named
	 * destinations. In addition, the Catalog indicates whether the document's outline or thumbnail
	 * page images should be displayed automatically when the document is viewed and wether some location
	 * other than the first page should be shown when the document is opened.<BR>
	 * In this class however, only the reference to the tree of pages is implemented.<BR>
	 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 6.2 (page 67-71)
	 *
	 * @author  bruno@lowagie.com
	 */

	class PdfCatalog extends PdfDictionary {

	// constructors

		/**
		 * Constructs a <CODE>PdfCatalog</CODE>.
		 *
		 * @param		pages		an indirect reference to the root of the document's Pages tree.
		 */

		PdfCatalog(PdfIndirectReference pages) {
			super(CATALOG);
			put(PdfName.PAGES, pages);
		}

		/**
		 * Constructs a <CODE>PdfCatalog</CODE>.
		 *
		 * @param		pages		an indirect reference to the root of the document's Pages tree.
		 * @param		outlines	an indirect reference to the outline tree.
		 */

		PdfCatalog(PdfIndirectReference pages, PdfIndirectReference outlines) {
			super(CATALOG);
			put(PdfName.PAGES, pages);
			put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
			put(PdfName.OUTLINES, outlines);
		}
	}

// membervariables

	/** The <CODE>PdfWriter</CODE>. */
	private PdfWriter writer;

	/** some meta information about the Document. */
	private PdfInfo info = new PdfInfo();

	// Horizontal line

	/** The line that is currently being written. */
	private PdfLine line = null;

	/** This represents the current indentation of the PDF Elements on the left side. */
	private int indentLeft = 0;

	/** This represents the current indentation of the PDF Elements on the right side. */
	private int indentRight = 0;

	/** This represents the current indentation of the PDF Elements on the left side. */
	private int listIndentLeft = 0;

	/** This represents the current alignment of the PDF Elements. */
	private int alignment = Element.ALIGN_LEFT;

	// Vertical lines

	/** This is the PdfText object, containing the text. */
	private PdfText text;

	/** This is the PdfContent object, containing the borders and other Graphics. */
	private PdfContent graphics;

	/** The lines that iare written untill now. */
	private ArrayList lines = new ArrayList();

	/** This represents the leading of the lines. */
	private int leading = 0;

	/** This is the current height of the document. */
	private int currentHeight = 0;

	/** This represents the current indentation of the PDF Elements on the top side. */
	private int indentTop = 0;

	/** This represents the current indentation of the PDF Elements on the bottom side. */
	private int indentBottom = 0;

	/** This checks if the page is empty. */
	private boolean pageEmpty = true;

	// resources

	/** This is the size of the current Page. */
	protected Rectangle thisPageSize = null;

	/** This is the FontDictionary of the current Page. */
	protected PdfFontDictionary fontDictionary;

	/** This is the XObjectDictionary of the current Page. */
	protected PdfXObjectDictionary xObjectDictionary;

	// images

	/** This is the list with all the images in the document. */
	private HashMap images = new HashMap();

	/** This is the image that could not be shown on a previous page. */
	private Image imageWait = null;

	/** This is the position where the image ends. */
	private int imageEnd = -1;

	/** This is the indentation caused by an image on the left. */
	private int imageIndentLeft = 0;

	/** This is the indentation caused by an image on the right. */
	private int imageIndentRight = 0;

	// annotations and outlines

	/** This is the array containing the references to the annotations. */
	private PdfArray annotations;

	/** This is the <CODE>ArrayList</CODE> with the outlines of the document. */
	private ArrayList outlines;

	/** This is the current <CODE>PdfOutline</CODE> in the hierarchy of outlines. */
	private PdfOutline currentOutline;

// constructors

	/**
	 * Constructs a new PDF document.
	 */

	public PdfDocument() throws DocumentException {
		super();
		addProducer();
		addCreationDate();
	}

// listener methods

	/**
	 * Adds a <CODE>PdfWriter</CODE> to the <CODE>PdfDocument</CODE>.
	 *
	 * @param	writer		the <CODE>PdfWriter</CODE> that writes everything
	 *                      what is added to this document to an outputstream.
	 */

	public final void addWriter(PdfWriter writer) throws DocumentException {
		if (this.writer == null) {
			this.writer = writer;
			return;
		}
		throw new DocumentException("You can only add a writer to a PdfDocument once.");
	}		  

	/**
	 * Sets the pagesize.
	 *
	 * @param	pageSize	the new pagesize
	 */

	public boolean setPageSize(Rectangle pageSize) {
		if (writer.isPaused()) {
			return false;
		}
		this.pageSize = pageSize;
		return true;
	}

    /**
     * Sets the <CODE>Watermark</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
     */

    public boolean add(Watermark watermark) {
		if (writer.isPaused()) {
			return false;
		}
		this.watermark = watermark;
		return true;
	}

	/**
	 * Removes the <CODE>Watermark</CODE>.
	 */

	public void removeWatermark() {
		if (writer.isPaused()) {
			return;
		}
		this.watermark = null;
	}

	/**
	 * Sets the margins.
	 *			   							
	 * @param	marginLeft		the margin on the left
	 * @param	marginRight		the margin on the right
	 * @param	marginTop		the margin on the top
	 * @param	marginBottom	the margin on the bottom
	 * @return	a <CODE>boolean</CODE>
	 */

	public boolean setMargins(int marginLeft, int marginRight, int marginTop, int marginBottom) {
		if (writer.isPaused()) {
			return false;
		}
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
		return true;
	}

	/** 
	 * Makes a new page and sends it to the <CODE>PdfWriter</CODE>.
	 *
	 * @return	a <CODE>boolean</CODE>
	 */

	public boolean newPage() throws DocumentException {
		if (pageEmpty || writer.isPaused()) {
			return false;
		}
		// we flush the arraylist with recently written lines
		flushLines();
		// we assemble the resources of this pages
		PdfResources resources = new PdfResources();
		int procset = PdfProcSet.PDF;
		if (fontDictionary.containsFont()) {
			resources.add(fontDictionary);
			procset |= PdfProcSet.TEXT;
		}
		if (xObjectDictionary.containsXObject()) {
			resources.add(xObjectDictionary);
			procset |= PdfProcSet.IMAGEC;
		}
		resources.add(new PdfProcSet(procset));
		// we make a new page and add it to the document
		PdfPage page = new PdfPage(new PdfRectangle(thisPageSize), resources);
		// we add the annotations
		if (annotations.size() > 0) {
			page.put(PdfName.ANNOTS, annotations);
		}
		if (!open || close) {
			throw new PdfException("The document isn't open.");
		}	 
		PdfIndirectReference pageReference = writer.add(page, new PdfContents(graphics, text));
		// we update the outlines
		for (Iterator i = outlines.iterator(); i.hasNext(); ) {
			PdfOutline outline = (PdfOutline) i.next();
			outline.setDestinationPage(pageReference);
		}					 
		// we initialize the new page
		initPage();
		return true;
	}

// methods to open and close a document

	/**
	 * Opens the document.
	 * <B>
	 * You have to open the document before you can begin to add content
	 * to the body of the document.
	 */

	public void open() {
		if (!open) {
			super.open();
			writer.open();
			outlines = new ArrayList();
			PdfOutline outline = new PdfOutline();
			outlines.add(outline);
			currentOutline = outline;
		}
		try {
			initPage();
		}
		catch(DocumentException de) {
		}
	}

	/**
	 * Closes the document.
	 * <B>
	 * Once all the content has been written in the body, you have to close
	 * the body. After that nothing can be written to the body anymore.
	 *
	 * @return	<CODE>void</CODE>
	 */

	public void close() {
		if (close) {
			return;
		}
		try {
			newPage();
			newPage();
		}
		catch(DocumentException pe) {
		}
		super.close();
		
		if (outlines.size() > 1) {
			int objectNumber = writer.size();
			int level = 0;
			// telling each outline what its indirect reference is
			for (Iterator i = outlines.iterator(); i.hasNext(); ) {
				PdfOutline o = (PdfOutline) i.next();
				if (o.level() > level) { 
					level = o.level();
				}
				PdfIndirectReference reference = new PdfIndirectReference(PdfObject.DICTIONARY, objectNumber);
				o.setIndirectReference(reference);
				objectNumber++;
			}
			// setting al the navigation keys (First, Last, Next, Previous)
			for (int l = 0; l <= level; l++) {
				PdfOutline levelOutline = null;
				for (Iterator i = outlines.iterator(); i.hasNext(); ) {
					PdfOutline o = (PdfOutline) i.next();
					if (o.level() == l) {
						if (levelOutline == null) {
							levelOutline = o;
						}
						else {
							if (o.parent().indirectReference().getNumber() == levelOutline.parent().indirectReference().getNumber()) {
								levelOutline.put(PdfName.NEXT, o.indirectReference());
								o.put(PdfName.PREV, levelOutline.indirectReference());
							}
							levelOutline = o;
						}
					}
					else if (o.level() == (l + 1)) {
						if (o.parent().get(PdfName.FIRST) == null) {
							o.parent().put(PdfName.FIRST, o.indirectReference());
						}
						o.parent().put(PdfName.LAST, o.indirectReference());
					}
				}
			}
			// write everything to the PdfWriter
			try {
				for (Iterator i = outlines.iterator(); i.hasNext(); ) {
					writer.add((PdfOutline) i.next());
				}
			}
			catch(PdfException pe) {
				System.err.println(pe.getMessage());
			}
		}
		writer.close();
	} 

    /**
     * Signals that a <CODE>PdfFont</CODE> was added to the <CODE>PdfDocument</CODE>. 
     *
	 * @param	font		the <CODE>PdfFont</CODE> to add
	 * @throws	PdfException	when a document isn't open yet, or has been closed
     */

    private void add(PdfFont font) throws PdfException {
		if (!open || close) {
			throw new PdfException("The document isn't open.");
		}
		fontDictionary.put(font.getName(), writer.add(font));
	}

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
     */

    public boolean add(Element element) throws DocumentException {
		if (writer.isPaused()) {
			return false;
		}
		try {
			switch(element.type()) {

			// Information (headers)

			case Element.HEADER:
				// other headers than those below are not supported
				return false;
			case Element.TITLE:
				info.addTitle(((Meta)element).content());
				break;
			case Element.SUBJECT:
				info.addSubject(((Meta)element).content());
				break;
			case Element.KEYWORDS:
				info.addKeywords(((Meta)element).content());
				break;
			case Element.AUTHOR:
				info.addAuthor(((Meta)element).content());
				break;
			case Element.PRODUCER:
				// you can not change the name of the producer
				info.addProducer();
				break;
			case Element.CREATIONDATE:
				// you can not set the creation date, only reset it
				info.addCreationDate();
				break;

			// content (text)

			case Element.CHUNK:
				// if there isn't a current line available, we make one
				if (line == null) {
					carriageReturn(); 
				}
				// we cast the element to a chunk
				PdfChunk chunk = new PdfChunk((Chunk) element);
				// we try to add the chunk to the line, until we succeed
				{
					int beginX = indentRight() - line.widthLeft();
					int lineY = indentTop() - currentHeight - line.height() - (chunk.font().size() / 3);
					PdfChunk overflow;
					while ((overflow = line.add(chunk)) != null) {
						carriageReturn();
						if (chunk.isUnderlined() || chunk.isStrikethru()) {
							if (chunk.isStrikethru()) {
								lineY += (chunk.font().size() / 1.5);
							}
							graphics.moveTo(beginX, lineY);
							graphics.lineTo(indentRight(), lineY);
							graphics.stroke();
							beginX = indentLeft();
							lineY = indentTop() - currentHeight - line.height() - (chunk.font().size() / 3);
						}
						chunk = overflow;
					}		
					if (chunk.isUnderlined() || chunk.isStrikethru()) {
						if (chunk.isStrikethru()) { 
							lineY += (chunk.font().size() / 1.5);		
						}
						graphics.moveTo(beginX, lineY);
						graphics.lineTo(indentRight() - line.widthLeft(), lineY);
						graphics.stroke();
					}
				}
				pageEmpty = false;
				break;

			case Element.ANCHOR:
				Anchor anchor = (Anchor) element;
				URL url = anchor.url();
				leading = anchor.leading();
				PdfAction action = null;
				if (url != null) {
					action = new PdfAction(url);
				}
												   
				if (line == null) {
					carriageReturn();
				}
				int beginx = indentRight() - line.widthLeft();
				for (Iterator i = anchor.getChunks().iterator(); i.hasNext(); ) {
					PdfChunk aChunk = new PdfChunk((Chunk) i.next());
					PdfChunk overflowChunk;						
					int beginX = indentRight() - line.widthLeft();
					int lineY = indentTop() - currentHeight - line.height() - (aChunk.font().size() / 3);
					while ((overflowChunk = line.add(aChunk)) != null) {
						if (action != null) { 
							// this is a hack, it doesn't work in all cases
							int endx = indentRight();
							if (alignment == Element.ALIGN_RIGHT || alignment == Element.ALIGN_CENTER) {
								beginx += line.indentLeft();
								endx += line.indentLeft();
							}
							annotations.add(new PdfAnnotation(beginx, indentTop() - currentHeight - line.height() - (aChunk.font().size() / 3), endx, indentTop() - currentHeight - line.height() - (aChunk.font().size() / 3) + aChunk.font().size(), action));
						}
						beginx = indentLeft();
						carriageReturn();
						if (aChunk.isUnderlined() || aChunk.isStrikethru()) {
							if (aChunk.isStrikethru()) {
								lineY += (aChunk.font().size() / 1.5);
							}
							graphics.moveTo(beginX, lineY);
							graphics.lineTo(indentRight(), lineY);
							graphics.stroke();
							beginX = indentLeft();
							lineY = indentTop() - currentHeight - line.height() - (aChunk.font().size() / 3);
						}
						aChunk = overflowChunk;
					}				 		
					if (aChunk.isUnderlined() || aChunk.isStrikethru()) {
						if (aChunk.isStrikethru()) {
							lineY += (aChunk.font().size() / 1.5);			
						}
						graphics.moveTo(beginX, lineY);
						graphics.lineTo(indentRight() - line.widthLeft(), lineY);
						graphics.stroke();
					} 
					if (action != null) {
						int endx = indentRight();
						if (alignment == Element.ALIGN_RIGHT || alignment == Element.ALIGN_CENTER) {
							beginx += line.indentLeft();
							endx += line.indentLeft();
						}
						annotations.add(new PdfAnnotation(beginx, indentTop() - currentHeight - line.height() - (aChunk.font().size() / 3), endx - line.widthLeft(), indentTop() - currentHeight - line.height() - (aChunk.font().size() / 3) + aChunk.font().size(), action));
					}
				}
				pageEmpty = false;
				break;

			case Element.ANNOTATION:												   
				if (line == null) {
					carriageReturn();
				}
				Annotation annot = (Annotation) element;
				annotations.add(new PdfAnnotation(indentRight() - line.widthLeft(), indentTop() - currentHeight, indentRight() - line.widthLeft() + 20, indentTop() - currentHeight - 20, new PdfString(annot.title()), new PdfString(annot.content())));
				pageEmpty = false;
				break;

			case Element.PHRASE:
				// we cast the element to a phrase and set the leading of the document
				leading = ((Phrase) element).leading();
				// we process the element
				element.process(this);
				break;

			case Element.PARAGRAPH:
				// we cast the element to a paragraph
				Paragraph paragraph = (Paragraph) element;
				// we adjust the parameters of the document
				alignment = paragraph.alignment();
				indentLeft += paragraph.indentationLeft();
				indentRight += paragraph.indentationRight();
				leading = paragraph.leading();
				carriageReturn();
				// we don't want to make orphans/widows
				if (currentHeight + line.height() + leading > indentTop() - indentBottom()) {
					newPage();
				}
				// we process the paragraph
				element.process(this);
				// if the last line is justified, it should be aligned to the left
				if (line.hasToBeJustified()) {
					line.resetAlignment();
				}
				// some parameters are set back to normal again
				carriageReturn();
				alignment = Element.ALIGN_LEFT;
				indentLeft -= paragraph.indentationLeft();
				indentRight -= paragraph.indentationRight();
				break;

			case Element.SECTION:
			case Element.CHAPTER:
				// Chapters and Sections only differ in their constructor
				// so we cast both to a Section
				Section section = (Section) element;

				PdfDestination destination = new PdfDestination(PdfDestination.FITH, indentTop() - currentHeight);
				while (currentOutline.level() >= section.depth()) {
					currentOutline = currentOutline.parent();
				}
				PdfOutline outline = new PdfOutline(currentOutline, destination, section.title());
				outlines.add(outline);
				currentOutline = outline;

				// some values are set
				carriageReturn();
				indentLeft += section.indentationLeft();
				indentRight += section.indentationRight();				
				// the title of the section (if any has to be printed)
				if (section.title() != null) {
					add(section.title());
				}
				indentLeft += section.indentation();
				// we process the section
				element.process(this);
				// some parameters are set back to normal again
				indentLeft -= section.indentationLeft() + section.indentation();
				indentRight -= section.indentationRight();
				// if the section is a chapter, we begin a new page
				if (section.isChapter()) {
					newPage();
				}
				// otherwise, we begin a new line
				else {
					newLine();
				}
				break;

			case Element.LIST:
				// we cast the element to a List
				List list = (List) element;
				// we adjust the document
				listIndentLeft += list.indentationLeft();
				indentRight += list.indentationRight();
				// we process the items in the list
				element.process(this);
				// some parameters are set back to normal again
				listIndentLeft -= list.indentationLeft();
				indentRight -= list.indentationRight();
				break;

			case Element.LISTITEM:
				// we cast the element to a ListItem
				ListItem listItem = (ListItem) element;
				// we adjust the document
				alignment = listItem.alignment();
				listIndentLeft += listItem.indentationLeft();
				indentRight += listItem.indentationRight();
				leading = listItem.leading();
				carriageReturn();
				// we prepare the current line to be able to show us the listsymbol
				line.setListItem(listItem);
				// we process the item
				element.process(this);
				// if the last line is justified, it should be aligned to the left
				if (line.hasToBeJustified()) {
					line.resetAlignment();
				}
				// some parameters are set back to normal again
				carriageReturn();
				listIndentLeft -= listItem.indentationLeft();
				indentRight -= listItem.indentationRight();
				break;

			case Element.RECTANGLE:
				Rectangle rectangle = (Rectangle) element;
				graphics.rectangle(rectangle);
				pageEmpty = false;
				break;

			case Element.TABLE:
				// before every table, we add a new line and flush all lines
				newLine();
				flushLines();

				// initialisation of parameters
				boolean newPage = false;
				int pagetop = indentTop();
				int	oldHeight = currentHeight;
				int cellDisplacement;
				PdfCell cell;
				Color color;

				// constructing the PdfTable
				PdfTable table = new PdfTable((Table) element,
					indentLeft(), indentRight(),
					currentHeight > 0 ? pagetop - currentHeight : pagetop);

				// drawing the table
				ArrayList cells = table.getCells();
				ArrayList headercells = null;
				while (! cells.isEmpty()) {

					// does the table fit on one page?
					newPage = false;
					if (table.bottom() < indentBottom()) {
						newPage = true;
					}

					// we paint the graphics of the table
					if (table.hasHeader()) {
						graphics.rectangle(table.rectangle(top(), indentBottom()));
					}
					else {
						graphics.rectangle(table.rectangle(indentTop(), indentBottom()));
					}

					// loop over the cells
					for (Iterator iterator = cells.iterator(); iterator.hasNext(); ) {
						cell = (PdfCell) iterator.next();

						lines = cell.getLines(pagetop, indentBottom());
						// if there are lines to add, add them
						if (lines != null && lines.size() > 0) {

							// we paint the borders of the cells
							if (pagetop == indentTop()) {
								graphics.rectangle(cell.rectangle(pagetop - leading / 2, indentBottom()));
							}
							else {
								graphics.rectangle(cell.rectangle(pagetop, indentBottom()));
							}

							// we write the text
							int cellTop = cell.top(pagetop - oldHeight);
							text.move(0, cellTop);
							cellDisplacement = flushLines() - cellTop;
							text.move(0, cellDisplacement);
							if (oldHeight + cellDisplacement > currentHeight) {
								currentHeight = oldHeight + cellDisplacement;
							}
						}

						// if a cell is allready added completely, remove it
						if (cell.mayBeRemoved()) {
							iterator.remove();
						} 
					}

					// if the table continues on the next page
					if (newPage && ! cells.isEmpty()) {
													   
						int difference = indentBottom() + leading;
						newPage();
						flushLines();

						int newTop;
						int newBottom;
							   	
						// this part repeats the table headers (if any)
						headercells = table.getHeaderCells();
						int size = headercells.size();
						if (size > 0) {
							// this is the top of the headersection
							cell = (PdfCell) headercells.get(0);
							int oldTop = cell.top(-table.cellpadding());
							// loop over all the cells of the table header
							for (int i = 0; i < size; i++) {
								cell = (PdfCell) headercells.get(i);
						
								// calculation of the new cellpositions
								newTop = indentTop() + cell.top(-table.cellpadding()) - oldTop;
								cell.setTop(newTop);
								newBottom = indentTop() + cell.bottom() - oldTop;
								cell.setBottom(newBottom);
								pagetop = cell.bottom();

								// we paint the borders of the cell
								graphics.rectangle(cell.rectangle(indentTop() - leading / 2, indentBottom()));
							
								// we write the text of the cell
								lines = cell.getLines(indentTop(), indentBottom());
								int cellTop = cell.top(indentTop());
								text.move(0, cellTop);
								cellDisplacement = flushLines() - cellTop;
								text.move(0, cellDisplacement);
							}
						
							currentHeight =  indentTop() - pagetop - table.cellpadding();
							text.move(0, pagetop - indentTop() + table.cellpadding() - currentHeight);
						}
						oldHeight = currentHeight;

						// calculating the new positions of the table and the cells
						table.setTop(indentTop());
						table.setBottom(pagetop - difference + table.bottom(table.cellpadding()));
						size = cells.size();
						for (int i = 0; i < size; i++) {
							cell = (PdfCell) cells.get(i);
							newTop = pagetop - difference + cell.top(-table.cellpadding());
							newBottom = pagetop - difference + cell.bottom();
							cell.setTop(newTop);
							cell.setBottom(newBottom);
						}
					}
				}

				text.move(0, oldHeight - currentHeight);
				lines.add(line);
				currentHeight += line.height() - pagetop + indentTop();
				line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
				break;

			case Element.GIF:
			case Element.JPEG:
			case Element.PNG:
			case Element.IMGRAW:
				carriageReturn(); 
				add((Image) element);
				pageEmpty = false;
				break;

			case Element.GRAPHIC:
				graphics.add((Graphic) element);
				pageEmpty = false;
				break;

			default:
				return false;
			}
			return true;
		}
		catch(Exception e) {
			throw new DocumentException(e.getMessage());
		}
	}

// methods to add Content
	
	/**
	 * Adds an image to the document.
	 */

	private void add(Image image) throws PdfException, DocumentException {
		PdfName name;
		// if the images is already added, just retrieve the name
		if (images.containsKey(image)) {
			name = (PdfName) images.get(image);
		}
		// if it's a new image, add it to the document
		else {
			PdfImage i = new PdfImage(image, "img" + images.size());
			writer.add(i);
			name = i.name();
			images.put(image, name);
		}

		if (image.hasAbsolutePosition()) {
			graphics.doImage(name, image.matrix(), image.absoluteX(), image.absoluteY());
			xObjectDictionary.put(name, writer.getImageReference(name));
			return;
		}					

		// if there isn't enough room for the image on this page, save it for the next page
		if (currentHeight != 0 && indentTop() - currentHeight - image.scaledHeight() < indentBottom()) {
			if (imageWait == null) {
				imageWait = image;
				return;
			}
			newPage();
			if (currentHeight != 0 && indentTop() - currentHeight - image.scaledHeight() < indentBottom()) {
				imageWait = image;
				return;
			}
		}
		xObjectDictionary.put(name, writer.getImageReference(name));
		boolean textwrap = (image.alignment() & Image.TEXTWRAP) == Image.TEXTWRAP
						&& !((image.alignment() & Image.MIDDLE) == Image.MIDDLE);
		boolean underlying = (image.alignment() & Image.UNDERLYING) == Image.UNDERLYING;
		int lowerleft = indentTop() - currentHeight - image.scaledHeight();
		switch(image.alignment() & Image.MIDDLE) {
		case Image.RIGHT:
			graphics.doImage(name, image.matrix(), indentRight() - image.scaledWidth(), lowerleft);
			break;
		case Image.MIDDLE:
			int middle = indentRight() - indentLeft() - image.scaledWidth();
			graphics.doImage(name, image.matrix(), indentLeft() + (middle / 2), lowerleft);
			break;
		case Image.LEFT:
		default:
			graphics.doImage(name, image.matrix(), indentLeft(), lowerleft);			
		}
		if (textwrap) {
			if (imageEnd < 0 || imageEnd < currentHeight + image.scaledHeight()) {
				imageEnd = currentHeight + image.scaledHeight();
			}
			if ((image.alignment() & Image.RIGHT) == Image.RIGHT) {
				imageIndentRight += image.scaledWidth();
			}
			else {
				imageIndentLeft += image.scaledWidth();
			}
		}
		if (!(textwrap || underlying)) {
			currentHeight += image.scaledHeight();
			flushLines();
			text.move(0, -image.scaledHeight());
		}
	}

	/**
	 * Initializes a page.
	 * <P>
	 * If the footer/header is set, it is printed.
	 */			

	private void initPage() throws DocumentException {

		// initialisation of some page objects
		annotations = new PdfArray();
		fontDictionary = new PdfFontDictionary();
		xObjectDictionary = new PdfXObjectDictionary();

		// the pagenumber is incremented
		pageN++;

		// graphics and text are initialized
		imageEnd = -1;
		imageIndentRight = 0;
		imageIndentLeft = 0;
		graphics = new PdfContent();
		text = new PdfText();
		leading = 16;
		indentBottom = 0;
		indentTop = 0;
		
		currentHeight = 0;
		int oldAlignment = alignment;

		// backgroundcolors, etc...
		thisPageSize = pageSize;
		if (pageSize.backgroundColor() != null
			|| pageSize.hasBorders()
			|| pageSize.borderColor() != null
			|| pageSize.grayFill() > 0) {
			add(pageSize);
		}

		// if there is a watermark, the watermark is added
		if (watermark != null) {
			
			PdfName name;
			// if the watermark is already added, just retrieve the name
			if (images.containsKey(watermark)) {
				name = (PdfName) images.get(watermark);
			}
			// if it's a new image, add it to the document
			else {
				PdfImage i = new PdfImage(watermark, "img" + images.size());
				writer.add(i);
				name = i.name();
				images.put(watermark, name);
			}
			xObjectDictionary.put(name, writer.getImageReference(name));
			graphics.doImage(name, watermark.matrix(), watermark.offsetX(), watermark.offsetY());
		}

		// if there is a footer, the footer is added
		if (footer != null) {
			footer.setPageNumber(pageN);
			leading = footer.paragraph().leading();
			add(footer.paragraph());
			// adding the footer limits the height
			indentBottom = currentHeight;
			text.move(left(), indentBottom());
			flushLines();
			text.move(-left(), -bottom());
			footer.setTop(bottom(currentHeight));
			footer.setBottom(bottom() - (int)(0.75 * leading));
			footer.setLeft(left());
			footer.setRight(right());
			graphics.rectangle(footer);
			indentBottom = currentHeight + leading * 3 / 2;
			currentHeight = 0;
		}

		// we move to the left/top position of the page
		text.move(left(), top());

		// if there is a header, the header = added
		if (header != null) {
			header.setPageNumber(pageN);
			leading = header.paragraph().leading();
			text.move(0, leading);
			add(header.paragraph());
			newLine();
			indentTop = currentHeight - leading;
			header.setTop(top() + leading);
			header.setBottom(indentTop() + leading * 2 / 3);
			header.setLeft(left());
			header.setRight(right());
			graphics.rectangle(header);
			flushLines();
			currentHeight = 0;
		}

		pageEmpty = true;

		// if there is an image waiting to be drawn, draw it
		try {
			if (imageWait != null) {
				add(imageWait);
				imageWait = null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		alignment = oldAlignment;
		carriageReturn();
	}

	/**
	 * If the current line is not empty or null, it is added to the arraylist
	 * of lines and a new empty line is added.
	 */

	private void carriageReturn() throws DocumentException {
		// the arraylist with lines may not be null
		if (lines == null) {
			lines = new ArrayList();
		}
		// If the current line is not null
		if (line != null) {
			// we check if the end of the page is reached
			if (currentHeight + line.height() < indentTop() - indentBottom()) {
				// if so nonempty lines are added and the heigt is augmented
				if (line.size() > 0) {
					currentHeight += line.height();
					lines.add(line);
				}
			}
			// if the end of the line is reached, we start a new page
			else {
				newPage();
			}
		}
		if (imageEnd > -1 && currentHeight > imageEnd) {
			imageEnd = -1;
			imageIndentRight = 0;
			imageIndentLeft = 0;
		}
		// a new current line is constructed
		line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
	}

	/**
	 * Adds the current line to the list of lines and also adds an empty line.
	 */

	private void newLine() throws DocumentException {
		carriageReturn();
		if (lines != null && lines.size() > 0) {
			lines.add(line);
			currentHeight += line.height();
		}
		line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
	}

	/**
	 * Writes all the lines to the <CODE>PdfText</CODE>-object.
	 *
	 * @return	the displacement that was caused
	 */

	private int flushLines() throws DocumentException {

		// checks if the ArrayList with the lines is not null
		if (lines == null) {
			return 0;
		}
			 
		// checks if a new Line has to be made.
		if (line != null && line.size() > 0) {
			lines.add(line);
			line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
		}

		// checks if the ArrayList with the lines is empty
		if (lines.size() == 0) {
			return 0;
		}

		// initialisation of some parameters
		PdfFont currentFont = null;
		int currentLeading = 0;
		int displacement = 0;
		PdfLine l;
		PdfChunk chunk;
		int numberOfSpaces;
		// looping over all the lines
		for (Iterator i = lines.iterator(); i.hasNext(); ) {

			// this is a line in the loop
			l = (PdfLine) i.next();
			// aligning the line
			text.move(l.indentLeft() - indentLeft() + listIndentLeft, -l.height());

			// is the line preceeded by a symbol?
			if (l.listSymbol() != null) {
				chunk = l.listSymbol();
				text.move(- l.listIndent(), 0);
				if (chunk.font().compareTo(currentFont) != 0) {
					currentFont = chunk.font();
					add(currentFont);
					text.setFont(currentFont);
				}
				if (chunk.color() != null) {
					Color color = chunk.color();
					text.setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
					text.show(chunk);
					text.resetRGBColorFill();
				}
				else {
					text.show(chunk);
				}
				text.move(l.listIndent(), 0);
			}

			// does the line need to be justified?
			numberOfSpaces = l.numberOfSpaces();
			if (l.hasToBeJustified() && numberOfSpaces != 0) {
				text.setWordSpacing(((double) (100 * l.widthLeft() / numberOfSpaces)) / 100.0);
			}

			// looping over all the chunks in 1 line
			for (Iterator j = l.iterator(); j.hasNext(); ) {
				chunk = (PdfChunk) j.next();

				if (chunk.font().compareTo(currentFont) != 0) {
					currentFont = chunk.font();
					add(currentFont);
					text.setFont(currentFont);
				}
				if (chunk.color() != null) {
					Color color = chunk.color();
					text.setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
					text.show(chunk);
					text.resetRGBColorFill();
				}
				else {
					text.show(chunk);
				}
			}
			text.newLine();
			displacement += l.height();
			if (indentLeft() - listIndentLeft != l.indentLeft()) {
				text.move(indentLeft() - l.indentLeft() - listIndentLeft, 0);
			}
			if (l.hasToBeJustified() && numberOfSpaces != 0) {
				text.setWordSpacing(0);
			}
		}
		lines = new ArrayList();
		return displacement;
	} 

// methods to retrieve information

	/**
	 * Gets the <CODE>PdfInfo</CODE>-object.
	 *
	 * @return	<CODE>PdfInfo</COPE>
	 */

	PdfInfo getInfo() {
		return info;
	}

	/**
	 * Gets the <CODE>PdfInfo</CODE>-object.
	 *
	 * @return	<CODE>PdfInfo</COPE>
	 */

	PdfCatalog getCatalog(PdfIndirectReference pages) {
		if (outlines.size() > 1) {
			PdfOutline outline = (PdfOutline) outlines.get(0);
			return new PdfCatalog(pages, outline.indirectReference());
		}
		return new PdfCatalog(pages);
	}

// methods concerning the layout

	/**
	 * Returns the bottomvalue of a <CODE>Table</CODE> if it were added to this document.
	 *
	 * @param	table	the table that may or may not be added to this document
	 * @return	a bottom value
	 */
	
	int bottom(Table table) {
		// constructing the PdfTable
		PdfTable tmp = new PdfTable(table, indentLeft(), indentRight(),
			currentHeight > 0 ? indentTop() - currentHeight : indentTop());
		return tmp.bottom();
	}

	/**
	 * Gets the indentation on the left side.
	 *
	 * @return	a margin
	 */

	private int indentLeft() {
		return left(indentLeft + listIndentLeft + imageIndentLeft);
	}

	/**
	 * Gets the indentation on the right side.
	 *
	 * @return	a margin
	 */

	private int indentRight() {
		return right(indentRight + imageIndentRight);
	}

	/**
	 * Gets the indentation on the top side.
	 *
	 * @return	a margin
	 */

	private int indentTop() {
		return top(indentTop);
	}

	/**
	 * Gets the indentation on the bottom side.
	 *
	 * @return	a margin
	 */

	int indentBottom() {
		return bottom(indentBottom);
	}
}
