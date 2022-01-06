/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

/**
 * A generic Document class.
 * <P>
 * All kinds of Text-elements can be added to a <CODE>HTMLDocument</CODE>.
 * The <CODE>Document</CODE> signals all the listeners when an element has
 * been added.
 * <P>
 * Remark:
 * <OL>
 *     <LI>Once a document is created you can add some meta information.
 *     <LI>You can also set the headers/footers.
 *     <LI>You have to open the document before you can write content.
 * <LI>You can only write content (no more meta-formation!) once a document is
 * opened.
 * <LI>When you change the header/footer on a certain page, this will be
 * effective starting on the next page.
 * <LI>After closing the document, every listener (as well as its <CODE>
 * OutputStream</CODE>) is closed too.
 * </OL>
 * Example: <BLOCKQUOTE>
 *
 * <PRE>// creation of the document with a certain size and certain margins
 * <STRONG>Document document = new Document(PageSize.A4, 50, 50, 50, 50);
 * </STRONG> try {
 *   // creation of the different writers
 *   HtmlWriter.getInstance(<STRONG>document </STRONG>, System.out);
 *   PdfWriter.getInstance(<STRONG>document </STRONG>, new FileOutputStream("text.pdf"));
 *   // we add some meta information to the document
 *   <STRONG>document.addAuthor("Bruno Lowagie"); </STRONG>
 *   <STRONG>document.addSubject("This is the result of a Test."); </STRONG>
 *   // we open the document for writing
 *   <STRONG>document.open(); </STRONG>
 *   <STRONG>document.add(new Paragraph("Hello world"));</STRONG>
 *  } catch(DocumentException de) {
 *   System.err.println(de.getMessage());
 *  }
 *  <STRONG>document.close();</STRONG>
 * </PRE>
 *
 * </BLOCKQUOTE>
 */

public class Document implements DocListener, IAccessibleElement {

    /**
	 * Allows the pdf documents to be produced without compression for debugging
	 * purposes.
	 */
    public static boolean compress = true;

	/**
	 * When true the file access is not done through a memory mapped file. Use it if the file
     * is too big to be mapped in your address space.
	 */
    public static boolean plainRandomAccess = false;

    /** Scales the WMF font size. The default value is 0.86. */
    public static float wmfFontCorrection = 0.86f;

	/**
	 * The DocListener.
	 * @since iText 5.1.0 changed from private to protected
	 */
    protected ArrayList<DocListener> listeners = new ArrayList<DocListener>();

	/** Is the document open or not? */
    protected boolean open;

	/** Has the document already been closed? */
    protected boolean close;

    // membervariables concerning the layout

	/** The size of the page. */
    protected Rectangle pageSize;

	/** margin in x direction starting from the left */
    protected float marginLeft = 0;

	/** margin in x direction starting from the right */
    protected float marginRight = 0;

	/** margin in y direction starting from the top */
    protected float marginTop = 0;

	/** margin in y direction starting from the bottom */
    protected float marginBottom = 0;

    /** mirroring of the left/right margins */
    protected boolean marginMirroring = false;

    /**
     * mirroring of the top/bottom margins
     * @since	2.1.6
     */
    protected boolean marginMirroringTopBottom = false;

	/** Content of JavaScript onLoad function */
    protected String javaScript_onLoad = null;

	/** Content of JavaScript onUnLoad function */
    protected String javaScript_onUnLoad = null;

	/** Style class in HTML body tag */
    protected String htmlStyleClass = null;

    // headers, footers

	/** Current pagenumber */
    protected int pageN = 0;

    /** This is a chapter number in case ChapterAutoNumber is used. */
    protected int chapternumber = 0;

    protected PdfName role = PdfName.DOCUMENT;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected AccessibleElementId id = new AccessibleElementId();

    // constructor

	/**
	 * Constructs a new <CODE>Document</CODE> -object.
 */

    public Document() {
        this(PageSize.A4);
    }

	/**
	 * Constructs a new <CODE>Document</CODE> -object.
 *
	 * @param pageSize
	 *            the pageSize
 */

    public Document(Rectangle pageSize) {
        this(pageSize, 36, 36, 36, 36);
    }

	/**
	 * Constructs a new <CODE>Document</CODE> -object.
 *
	 * @param pageSize
	 *            the pageSize
	 * @param marginLeft
	 *            the margin on the left
	 * @param marginRight
	 *            the margin on the right
	 * @param marginTop
	 *            the margin on the top
	 * @param marginBottom
	 *            the margin on the bottom
 */

	public Document(Rectangle pageSize, float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
        this.pageSize = pageSize;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    // listener methods

	/**
 * Adds a <CODE>DocListener</CODE> to the <CODE>Document</CODE>.
 *
	 * @param listener
	 *            the new DocListener.
 */

    public void addDocListener(DocListener listener) {
        listeners.add(listener);
        if (listener instanceof IAccessibleElement) {
            IAccessibleElement ae = (IAccessibleElement)listener;
            ae.setRole(this.role);
            ae.setId(this.id);
            if (this.accessibleAttributes != null) {
                for (PdfName key : this.accessibleAttributes.keySet())
                    ae.setAccessibleAttribute(key, this.accessibleAttributes.get(key));
            }
        }
    }

	/**
 * Removes a <CODE>DocListener</CODE> from the <CODE>Document</CODE>.
 *
	 * @param listener
	 *            the DocListener that has to be removed.
 */

    public void removeDocListener(DocListener listener) {
        listeners.remove(listener);
    }

    // methods implementing the DocListener interface

	/**
	 * Adds an <CODE>Element</CODE> to the <CODE>Document</CODE>.
 *
	 * @param element
	 *            the <CODE>Element</CODE> to add
	 * @return <CODE>true</CODE> if the element was added, <CODE>false
	 *         </CODE> if not
	 * @throws DocumentException
	 *             when a document isn't open yet, or has been closed
 */

    public boolean add(Element element) throws DocumentException {
        if (close) {
			throw new DocumentException(MessageLocalization.getComposedMessage("the.document.has.been.closed.you.can.t.add.any.elements"));
        }
		if (!open && element.isContent()) {
			throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information"));
        }
        boolean success = false;
        if (element instanceof ChapterAutoNumber) {
        	chapternumber = ((ChapterAutoNumber)element).setAutomaticNumber(chapternumber);
        }
        for (DocListener listener : listeners) {
            success |= listener.add(element);
        }
		if (element instanceof LargeElement) {
			LargeElement e = (LargeElement)element;
			if (!e.isComplete())
				e.flushContent();
		}
        return success;
    }

	/**
 * Opens the document.
 * <P>
	 * Once the document is opened, you can't write any Header- or
	 * Meta-information anymore. You have to open the document before you can
	 * begin to add content to the body of the document.
 */

    public void open() {
		if (!close) {
            open = true;
        }
		for (DocListener listener : listeners) {
            listener.setPageSize(pageSize);
			listener.setMargins(marginLeft, marginRight, marginTop,
                    marginBottom);
            listener.open();
        }
    }

	/**
 * Sets the pagesize.
 *
	 * @param pageSize
	 *            the new pagesize
 * @return	a <CODE>boolean</CODE>
 */

    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
		for (DocListener listener : listeners) {
            listener.setPageSize(pageSize);
        }
        return true;
    }

	/**
 * Sets the margins.
 *
	 * @param marginLeft
	 *            the margin on the left
	 * @param marginRight
	 *            the margin on the right
	 * @param marginTop
	 *            the margin on the top
	 * @param marginBottom
	 *            the margin on the bottom
 * @return	a <CODE>boolean</CODE>
 */

	public boolean setMargins(float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
		for (DocListener listener : listeners) {
			listener.setMargins(marginLeft, marginRight, marginTop,
					marginBottom);
        }
        return true;
    }

	/**
 * Signals that an new page has to be started.
 *
	 * @return <CODE>true</CODE> if the page was added, <CODE>false</CODE>
	 *         if not.
 */

    public boolean newPage() {
        if (!open || close) {
            return false;
        }
        for (DocListener listener : listeners) {
            listener.newPage();
        }
        return true;
    }

	/**
 * Sets the page number to 0.
 */

    public void resetPageCount() {
        pageN = 0;
        for (DocListener listener: listeners) {
            listener.resetPageCount();
        }
    }

	/**
 * Sets the page number.
 *
	 * @param pageN
	 *            the new page number
 */

    public void setPageCount(int pageN) {
        this.pageN = pageN;
        for (DocListener listener: listeners) {
            listener.setPageCount(pageN);
        }
    }

	/**
 * Returns the current page number.
 *
 * @return the current page number
 */

    public int getPageNumber() {
        return this.pageN;
    }

	/**
 * Closes the document.
 * <P>
	 * Once all the content has been written in the body, you have to close the
	 * body. After that nothing can be written to the body anymore.
 */

    public void close() {
		if (!close) {
            open = false;
            close = true;
        }
		for (DocListener listener : listeners) {
            listener.close();
        }
    }

    // methods concerning the header or some meta information

	/**
 * Adds a user defined header to the document.
 *
	 * @param name
	 *            the name of the header
	 * @param content
	 *            the content of the header
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addHeader(String name, String content) {
        try {
            return add(new Header(name, content));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the title to a Document.
 *
	 * @param title
	 *            the title
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addTitle(String title) {
        try {
            return add(new Meta(Element.TITLE, title));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the subject to a Document.
 *
	 * @param subject
	 *            the subject
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addSubject(String subject) {
        try {
            return add(new Meta(Element.SUBJECT, subject));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the keywords to a Document.
 *
	 * @param keywords
	 *            adds the keywords to the document
 * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addKeywords(String keywords) {
        try {
            return add(new Meta(Element.KEYWORDS, keywords));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the author to a Document.
 *
	 * @param author
	 *            the name of the author
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addAuthor(String author) {
        try {
            return add(new Meta(Element.AUTHOR, author));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the creator to a Document.
 *
	 * @param creator
	 *            the name of the creator
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addCreator(String creator) {
        try {
            return add(new Meta(Element.CREATOR, creator));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the producer to a Document.
 *
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addProducer() {
        try {
            return add(new Meta(Element.PRODUCER, Version.getInstance().getVersion()));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds a language to th document. Required for PDF/UA compatible documents.
     * @param language
     * @return <code>true</code> if successfull, <code>false</code> otherwise
     */
    public boolean addLanguage(String language) {
        try {
            return add(new Meta(Element.LANGUAGE, language));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

	/**
 * Adds the current date and time to a Document.
 *
 * @return	<CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
 */

    public boolean addCreationDate() {
        try {
			/* bugfix by 'taqua' (Thomas) */
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzz yyyy");
			return add(new Meta(Element.CREATIONDATE, sdf.format(new Date())));
		} catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    // methods to get the layout of the document.

	/**
 * Returns the left margin.
 *
 * @return	the left margin
 */

    public float leftMargin() {
        return marginLeft;
    }

	/**
 * Return the right margin.
 *
 * @return	the right margin
 */

    public float rightMargin() {
        return marginRight;
    }

	/**
 * Returns the top margin.
 *
 * @return	the top margin
 */

    public float topMargin() {
        return marginTop;
    }

	/**
 * Returns the bottom margin.
 *
 * @return	the bottom margin
 */

    public float bottomMargin() {
        return marginBottom;
    }

	/**
 * Returns the lower left x-coordinate.
 *
 * @return	the lower left x-coordinate
 */

    public float left() {
        return pageSize.getLeft(marginLeft);
    }

	/**
 * Returns the upper right x-coordinate.
 *
 * @return	the upper right x-coordinate
 */

    public float right() {
        return pageSize.getRight(marginRight);
    }

	/**
 * Returns the upper right y-coordinate.
 *
 * @return	the upper right y-coordinate
 */

    public float top() {
        return pageSize.getTop(marginTop);
    }

	/**
 * Returns the lower left y-coordinate.
 *
 * @return	the lower left y-coordinate
 */

    public float bottom() {
        return pageSize.getBottom(marginBottom);
    }

	/**
 * Returns the lower left x-coordinate considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the lower left x-coordinate
 */

    public float left(float margin) {
        return pageSize.getLeft(marginLeft + margin);
    }

	/**
 * Returns the upper right x-coordinate, considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the upper right x-coordinate
 */

    public float right(float margin) {
        return pageSize.getRight(marginRight + margin);
    }

	/**
 * Returns the upper right y-coordinate, considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the upper right y-coordinate
 */

    public float top(float margin) {
        return pageSize.getTop(marginTop + margin);
    }

	/**
 * Returns the lower left y-coordinate, considering a given margin.
 *
	 * @param margin
	 *            a margin
 * @return	the lower left y-coordinate
 */

    public float bottom(float margin) {
        return pageSize.getBottom(marginBottom + margin);
    }

	/**
 * Gets the pagesize.
	 *
 * @return the page size
 */

	public Rectangle getPageSize() {
        return this.pageSize;
    }

	/**
	 * Checks if the document is open.
	 *
     * @return <CODE>true</CODE> if the document is open
     */
    public boolean isOpen() {
        return open;
    }

	/**
 * Adds a JavaScript onLoad function to the HTML body tag
 *
	 * @param code
	 *            the JavaScript code to be executed on load of the HTML page
 */

    public void setJavaScript_onLoad(String code) {
        this.javaScript_onLoad = code;
    }

	/**
 * Gets the JavaScript onLoad command.
	 *
 * @return the JavaScript onLoad command
 */

    public String getJavaScript_onLoad() {
        return this.javaScript_onLoad;
    }

	/**
 * Adds a JavaScript onUnLoad function to the HTML body tag
 *
	 * @param code
	 *            the JavaScript code to be executed on unload of the HTML page
 */

    public void setJavaScript_onUnLoad(String code) {
        this.javaScript_onUnLoad = code;
    }

	/**
 * Gets the JavaScript onUnLoad command.
	 *
 * @return the JavaScript onUnLoad command
 */

    public String getJavaScript_onUnLoad() {
        return this.javaScript_onUnLoad;
    }

	/**
 * Adds a style class to the HTML body tag
 *
	 * @param htmlStyleClass
	 *            the style class for the HTML body tag
 */

    public void setHtmlStyleClass(String htmlStyleClass) {
        this.htmlStyleClass = htmlStyleClass;
    }

	/**
 * Gets the style class of the HTML body tag
 *
 * @return		the style class of the HTML body tag
 */

    public String getHtmlStyleClass() {
        return this.htmlStyleClass;
    }

    /**
     * Set the margin mirroring. It will mirror right/left margins for odd/even pages.
	 *
	 * @param marginMirroring
	 *            <CODE>true</CODE> to mirror the margins
     * @return always <CODE>true</CODE>
     */
    public boolean setMarginMirroring(boolean marginMirroring) {
        this.marginMirroring = marginMirroring;
        DocListener listener;
		for (Object element : listeners) {
            listener = (DocListener) element;
            listener.setMarginMirroring(marginMirroring);
        }
        return true;
    }

    /**
     * Set the margin mirroring. It will mirror top/bottom margins for odd/even pages.
	 *
	 * @param marginMirroringTopBottom
	 *            <CODE>true</CODE> to mirror the margins
     * @return always <CODE>true</CODE>
     * @since	2.1.6
     */
    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom) {
        this.marginMirroringTopBottom = marginMirroringTopBottom;
        DocListener listener;
		for (Object element : listeners) {
            listener = (DocListener) element;
            listener.setMarginMirroringTopBottom(marginMirroringTopBottom);
        }
        return true;
    }

    /**
     * Gets the margin mirroring flag.
	 *
     * @return the margin mirroring flag
     */
    public boolean isMarginMirroring() {
        return marginMirroring;
    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null)
            return accessibleAttributes.get(key);
        else
            return null;
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null)
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return accessibleAttributes;
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        return id;
    }

    public void setId(final AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }
}
