/*
 * $Id$
 *
 * Copyright 2009 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999-2009 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2009 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf.richmedia;

import com.lowagie.text.pdf.PdfBoolean;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;

/**
 * The RichMediaPresentation dictionary contains information about how the
 * annotation and user interface elements are to be visually laid out and
 * drawn.
 * See ExtensionLevel 3 p82
 * @since	2.1.6
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
