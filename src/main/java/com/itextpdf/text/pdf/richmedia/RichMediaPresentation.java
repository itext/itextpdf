/*
 * $Id: PdfAnnotationsImp.java 4113 2009-12-01 11:08:59Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text.pdf.richmedia;

import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;

/**
 * The RichMediaPresentation dictionary contains information about how the
 * annotation and user interface elements are to be visually laid out and
 * drawn.
 * See ExtensionLevel 3 p82
 * @since	5.0.0
 */
public class RichMediaPresentation extends PdfDictionary {
	
	/**
	 * Creates a RichMediaPresentation dictionary.
	 */
	public RichMediaPresentation() {
		super(PdfName.RICHMEDIAPRESENTATION);
	}
	
	/**
	 * Sets the style for the presentation;
	 * can be embedded or windowed.
	 * @param	style PdfName.EMBEDDED or PdfName.WINDOWED
	 */
	public void setStyle(PdfName style) {
		put(PdfName.STYLE, style);
	}
	
	/**
	 * A RichMediaWindow Dictionary that describes the size and
	 * position of the floating user interface window when the
	 * value for Style is set to Windowed.
	 * @param	window	a RichMediaWindow object
	 */
	public void setWindow(RichMediaWindow window) {
		put(PdfName.WINDOW, window);
	}
	
	/**
	 * Set a flag that indicates whether the page content is displayed
	 * through the transparent areas of the rich media content (where
	 * the alpha value is less than 1.0). If true, the rich media artwork
	 * is composited over the page content using an alpha channel. If false,
	 * the rich media artwork is drawn over an opaque background prior to
	 * composition over the page content.
	 * @param	transparent	a boolean
	 */
	public void setTransparent(PdfBoolean transparent) {
		put(PdfName.TRANSPARENT, transparent);
	}
	
	/**
	 * Sets a flag that indicates the default behavior of the navigation pane
	 * user interface element. If true, the navigation pane is visible when
	 * the content is initially activated. If false, the navigation pane is
	 * not displayed by default.
	 * @param	navigationPane	a boolean
	 */
	public void setNavigationPane(PdfBoolean navigationPane) {
		put(PdfName.NAVIGATIONPANE, navigationPane);
	}

	/**
	 * Sets a flag that indicates the default behavior of an interactive
	 * toolbar associated with this annotation. If true, a toolbar is
	 * displayed when the annotation is activated and given focus. If false,
	 * a toolbar is not displayed by default.
	 * @param	toolbar	a boolean
	 */
	public void setToolbar(PdfBoolean toolbar) {
		put(PdfName.TOOLBAR, toolbar);
	}

	/**
	 * Sets a flag that indicates whether a context click on the rich media
	 * annotation is passed to the media player run time or is handled by
	 * the conforming reader. If false, the conforming reader handles the
	 * context click. If true, the conforming reader's context menu is not
	 * visible, and the user sees the context menu and any custom items
	 * generated by the media player run time.
	 * @param	passContextClick	a boolean
	 */
	public void setPassContextClick(PdfBoolean passContextClick) {
		put(PdfName.PASSCONTEXTCLICK, passContextClick);
	}
}
