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

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An <CODE>Annotation</CODE> is a little note that can be added to a page on
 * a document.
 *
 * @see Element
 * @see Anchor
 */

public class Annotation implements Element {

	// membervariables

	/** This is a possible annotation type. */
	public static final int TEXT = 0;

	/** This is a possible annotation type. */
	public static final int URL_NET = 1;

	/** This is a possible annotation type. */
	public static final int URL_AS_STRING = 2;

	/** This is a possible annotation type. */
	public static final int FILE_DEST = 3;

	/** This is a possible annotation type. */
	public static final int FILE_PAGE = 4;

	/** This is a possible annotation type. */
	public static final int NAMED_DEST = 5;

	/** This is a possible annotation type. */
	public static final int LAUNCH = 6;

	/** This is a possible annotation type. */
	public static final int SCREEN = 7;

	/** This is a possible attribute. */
	public static final String TITLE = "title";

	/** This is a possible attribute. */
	public static final String CONTENT = "content";

	/** This is a possible attribute. */
	public static final String URL = "url";

	/** This is a possible attribute. */
	public static final String FILE = "file";

	/** This is a possible attribute. */
	public static final String DESTINATION = "destination";

	/** This is a possible attribute. */
	public static final String PAGE = "page";

	/** This is a possible attribute. */
	public static final String NAMED = "named";

	/** This is a possible attribute. */
	public static final String APPLICATION = "application";

	/** This is a possible attribute. */
	public static final String PARAMETERS = "parameters";

	/** This is a possible attribute. */
	public static final String OPERATION = "operation";

	/** This is a possible attribute. */
	public static final String DEFAULTDIR = "defaultdir";

	/** This is a possible attribute. */
	public static final String LLX = "llx";

	/** This is a possible attribute. */
	public static final String LLY = "lly";

	/** This is a possible attribute. */
	public static final String URX = "urx";

	/** This is a possible attribute. */
	public static final String URY = "ury";

	/** This is a possible attribute. */
	public static final String MIMETYPE = "mime";

	/** This is the type of annotation. */
	protected int annotationtype;

	/** This is the title of the <CODE>Annotation</CODE>. */
	protected HashMap<String, Object> annotationAttributes = new HashMap<String, Object>();

	/** This is the lower left x-value */
	protected float llx = Float.NaN;

	/** This is the lower left y-value */
	protected float lly = Float.NaN;

	/** This is the upper right x-value */
	protected float urx = Float.NaN;

	/** This is the upper right y-value */
	protected float ury = Float.NaN;

	// constructors

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some
	 * text.
	 *
	 * @param llx
	 *            lower left x coordinate
	 * @param lly
	 *            lower left y coordinate
	 * @param urx
	 *            upper right x coordinate
	 * @param ury
	 *            upper right y coordinate
	 */
	private Annotation(final float llx, final float lly, final float urx, final float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	/**
	 * Copy constructor.
	 * @param an the annotation to create a new Annotation from
	 */
    public Annotation(final Annotation an) {
        annotationtype = an.annotationtype;
        annotationAttributes = an.annotationAttributes;
        llx = an.llx;
        lly = an.lly;
        urx = an.urx;
        ury = an.ury;
    }

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some
	 * text.
	 *
	 * @param title
	 *            the title of the annotation
	 * @param text
	 *            the content of the annotation
	 */
	public Annotation(final String title, final String text) {
		annotationtype = TEXT;
		annotationAttributes.put(TITLE, title);
		annotationAttributes.put(CONTENT, text);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some
	 * text.
	 *
	 * @param title
	 *            the title of the annotation
	 * @param text
	 *            the content of the annotation
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 */
	public Annotation(final String title, final String text, final float llx, final float lly,
			final float urx, final float ury) {
		this(llx, lly, urx, ury);
		annotationtype = TEXT;
		annotationAttributes.put(TITLE, title);
		annotationAttributes.put(CONTENT, text);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param url
	 *            the external reference
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury, final URL url) {
		this(llx, lly, urx, ury);
		annotationtype = URL_NET;
		annotationAttributes.put(URL, url);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param url
	 *            the external reference
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury, final String url) {
		this(llx, lly, urx, ury);
		annotationtype = URL_AS_STRING;
		annotationAttributes.put(FILE, url);
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param file
	 *            an external PDF file
	 * @param dest
	 *            the destination in this file
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury, final String file,
			final String dest) {
		this(llx, lly, urx, ury);
		annotationtype = FILE_DEST;
		annotationAttributes.put(FILE, file);
		annotationAttributes.put(DESTINATION, dest);
	}

	/**
	 * Creates a Screen annotation to embed media clips
	 *
	 * @param llx
	 * @param lly
	 * @param urx
	 * @param ury
	 * @param moviePath
	 *            path to the media clip file
	 * @param mimeType
	 *            mime type of the media
	 * @param showOnDisplay
	 *            if true play on display of the page
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury,
			final String moviePath, final String mimeType, final boolean showOnDisplay) {
		this(llx, lly, urx, ury);
		annotationtype = SCREEN;
		annotationAttributes.put(FILE, moviePath);
		annotationAttributes.put(MIMETYPE, mimeType);
		annotationAttributes.put(PARAMETERS, new boolean[] {
				false /* embedded */, showOnDisplay });
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param file
	 *            an external PDF file
	 * @param page
	 *            a page number in this file
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury, final String file,
			final int page) {
		this(llx, lly, urx, ury);
		annotationtype = FILE_PAGE;
		annotationAttributes.put(FILE, file);
		annotationAttributes.put(PAGE, Integer.valueOf(page));
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param named
	 *            a named destination in this file
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury, final int named) {
		this(llx, lly, urx, ury);
		annotationtype = NAMED_DEST;
		annotationAttributes.put(NAMED, Integer.valueOf(named));
	}

	/**
	 * Constructs an <CODE>Annotation</CODE>.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 * @param application
	 *            an external application
	 * @param parameters
	 *            parameters to pass to this application
	 * @param operation
	 *            the operation to pass to this application
	 * @param defaultdir
	 *            the default directory to run this application in
	 */
	public Annotation(final float llx, final float lly, final float urx, final float ury,
			final String application, final String parameters, final String operation,
			final String defaultdir) {
		this(llx, lly, urx, ury);
		annotationtype = LAUNCH;
		annotationAttributes.put(APPLICATION, application);
		annotationAttributes.put(PARAMETERS, parameters);
		annotationAttributes.put(OPERATION, operation);
		annotationAttributes.put(DEFAULTDIR, defaultdir);
	}

	// implementation of the Element-methods

	/**
	 * Gets the type of the text element.
	 *
	 * @return a type
	 */
	public int type() {
		return Element.ANNOTATION;
	}

	/**
	 * Processes the element by adding it (or the different parts) to an <CODE>
	 * ElementListener</CODE>.
	 *
	 * @param listener
	 *            an <CODE>ElementListener</CODE>
	 * @return <CODE>true</CODE> if the element was processed successfully
	 */
	public boolean process(final ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException de) {
			return false;
		}
	}

	/**
	 * Gets all the chunks in this element.
	 *
	 * @return an <CODE>ArrayList</CODE>
	 */

	public List<Chunk> getChunks() {
		return new ArrayList<Chunk>();
	}

	// methods

	/**
	 * Sets the dimensions of this annotation.
	 *
	 * @param llx
	 *            the lower left x-value
	 * @param lly
	 *            the lower left y-value
	 * @param urx
	 *            the upper right x-value
	 * @param ury
	 *            the upper right y-value
	 */
	public void setDimensions(final float llx, final float lly, final float urx, final float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	// methods to retrieve information

	/**
	 * Returns the lower left x-value.
	 *
	 * @return a value
	 */
	public float llx() {
		return llx;
	}

	/**
	 * Returns the lower left y-value.
	 *
	 * @return a value
	 */
	public float lly() {
		return lly;
	}

	/**
	 * Returns the upper right x-value.
	 *
	 * @return a value
	 */
	public float urx() {
		return urx;
	}

	/**
	 * Returns the upper right y-value.
	 *
	 * @return a value
	 */
	public float ury() {
		return ury;
	}

	/**
	 * Returns the lower left x-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float llx(final float def) {
		if (Float.isNaN(llx))
			return def;
		return llx;
	}

	/**
	 * Returns the lower left y-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float lly(final float def) {
		if (Float.isNaN(lly))
			return def;
		return lly;
	}

	/**
	 * Returns the upper right x-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float urx(final float def) {
		if (Float.isNaN(urx))
			return def;
		return urx;
	}

	/**
	 * Returns the upper right y-value.
	 *
	 * @param def
	 *            the default value
	 * @return a value
	 */
	public float ury(final float def) {
		if (Float.isNaN(ury))
			return def;
		return ury;
	}

	/**
	 * Returns the type of this <CODE>Annotation</CODE>.
	 *
	 * @return a type
	 */
	public int annotationType() {
		return annotationtype;
	}

	/**
	 * Returns the title of this <CODE>Annotation</CODE>.
	 *
	 * @return a name
	 */
	public String title() {
		String s = (String) annotationAttributes.get(TITLE);
		if (s == null)
			s = "";
		return s;
	}

	/**
	 * Gets the content of this <CODE>Annotation</CODE>.
	 *
	 * @return a reference
	 */
	public String content() {
		String s = (String) annotationAttributes.get(CONTENT);
		if (s == null)
			s = "";
		return s;
	}

	/**
	 * Gets the content of this <CODE>Annotation</CODE>.
	 *
	 * @return a reference
	 */
	public HashMap<String, Object> attributes() {
		return annotationAttributes;
	}

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}

}
