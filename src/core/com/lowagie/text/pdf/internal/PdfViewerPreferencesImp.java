/*
 * $Id$
 *
 * Copyright 2006 Bruno Lowagie
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
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
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

package com.lowagie.text.pdf.internal;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfBoolean;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.interfaces.PdfViewerPreferences;

/**
 * Stores the information concerning viewer preferences,
 * and contains the business logic that allows you to set viewer preferences.
 */

public class PdfViewerPreferencesImp implements PdfViewerPreferences {
	public static final PdfName[] VIEWER_PREFERENCES = {
			PdfName.HIDETOOLBAR,			// 0
			PdfName.HIDEMENUBAR,            // 1
			PdfName.HIDEWINDOWUI,           // 2
			PdfName.FITWINDOW,              // 3
			PdfName.CENTERWINDOW,			// 4
			PdfName.DISPLAYDOCTITLE,		// 5
			PdfName.NONFULLSCREENPAGEMODE,	// 6
			PdfName.DIRECTION,				// 7
			PdfName.VIEWAREA,				// 8
			PdfName.VIEWCLIP,				// 9
			PdfName.PRINTAREA,				// 10
			PdfName.PRINTCLIP,				// 11
			PdfName.PRINTSCALING,			// 12
			PdfName.DUPLEX,					// 13
			PdfName.PICKTRAYBYPDFSIZE,		// 14
			PdfName.PRINTPAGERANGE,			// 15
			PdfName.NUMCOPIES				// 16
		};


    /** A series of viewer preferences. */
    public static final PdfName NONFULLSCREENPAGEMODE_PREFERENCES[] = {
    	PdfName.USENONE, PdfName.USEOUTLINES, PdfName.USETHUMBS, PdfName.USEOC
    };
    /** A series of viewer preferences. */
    public static final PdfName DIRECTION_PREFERENCES[] = {
    	PdfName.L2R, PdfName.R2L
    };
	/** A series of viewer preferences. */
	public static final PdfName PAGE_BOUNDARIES[] = {
		PdfName.MEDIABOX, PdfName.CROPBOX, PdfName.BLEEDBOX, PdfName.TRIMBOX, PdfName.ARTBOX
	};
	/** A series of viewer preferences */
	public static final PdfName PRINTSCALING_PREFERENCES[] = {
		PdfName.APPDEFAULT, PdfName.NONE
	};
	/** A series of viewer preferences. */
	public static final PdfName DUPLEX_PREFERENCES[] = {
		PdfName.SIMPLEX, PdfName.DUPLEXFLIPSHORTEDGE, PdfName.DUPLEXFLIPLONGEDGE
	};
	
	/** This value will hold the viewer preferences for the page layout and page mode. */
	private int pageLayoutAndMode = 0;
	
	/** This dictionary holds the viewer preferences (other than page layout and page mode). */
	private PdfDictionary viewerPreferences = new PdfDictionary();
	
	/** The mask to decide if a ViewerPreferences dictionary is needed */
	private static final int viewerPreferencesMask = 0xfff000;

	/**
	 * Returns the page layout and page mode value.
	 */
	public int getPageLayoutAndMode() {
		return pageLayoutAndMode;
	}

	/**
	 * Returns the viewer preferences.
	 */
	public PdfDictionary getViewerPreferences() {
		return viewerPreferences;
	}
	
	/**
	 * Sets the viewer preferences as the sum of several constants.
	 * 
	 * @param preferences
	 *            the viewer preferences
	 * @see PdfViewerPreferences#setViewerPreferences
	 */
	public void setViewerPreferences(int preferences) {
		this.pageLayoutAndMode |= preferences;
		// for backwards compatibility, it is also possible
		// to set the following viewer preferences with this method:
		if ((preferences & viewerPreferencesMask) != 0) {
			pageLayoutAndMode = ~viewerPreferencesMask & pageLayoutAndMode;
			if ((preferences & PdfWriter.HIDE_TOOLBAR) != 0)
				viewerPreferences.put(PdfName.HIDETOOLBAR, PdfBoolean.PDFTRUE);
			if ((preferences & PdfWriter.HIDE_MENUBAR) != 0)
				viewerPreferences.put(PdfName.HIDEMENUBAR, PdfBoolean.PDFTRUE);
			if ((preferences & PdfWriter.HIDE_WINDOW_UI) != 0)
				viewerPreferences.put(PdfName.HIDEWINDOWUI, PdfBoolean.PDFTRUE);
			if ((preferences & PdfWriter.FIT_WINDOW) != 0)
				viewerPreferences.put(PdfName.FITWINDOW, PdfBoolean.PDFTRUE);
			if ((preferences & PdfWriter.CENTER_WINDOW) != 0)
				viewerPreferences.put(PdfName.CENTERWINDOW, PdfBoolean.PDFTRUE);
			if ((preferences & PdfWriter.DISPLAY_DOC_TITLE) != 0)
				viewerPreferences.put(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);
			
			if ((preferences & PdfWriter.NON_FULL_SCREEN_PAGE_MODE_USE_NONE) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USENONE);
			else if ((preferences & PdfWriter.NON_FULL_SCREEN_PAGE_MODE_USE_OUTLINES) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOUTLINES);
			else if ((preferences & PdfWriter.NON_FULL_SCREEN_PAGE_MODE_USE_THUMBS) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USETHUMBS);
			else if ((preferences & PdfWriter.NON_FULL_SCREEN_PAGE_MODE_USE_OC) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOC);

			if ((preferences & PdfWriter.DIRECTION_L2R) != 0)
				viewerPreferences.put(PdfName.DIRECTION, PdfName.L2R);
			else if ((preferences & PdfWriter.DIRECTION_R2L) != 0)
				viewerPreferences.put(PdfName.DIRECTION, PdfName.R2L);

			if ((preferences & PdfWriter.PRINT_SCALING_NONE) != 0)
				viewerPreferences.put(PdfName.PRINTSCALING, PdfName.NONE);			
		}
	}
	
	/**
	 * Given a key for a viewer preference (a PdfName object),
	 * this method returns the index in the VIEWER_PREFERENCES array.
	 * @param key	a PdfName referring to a viewer preference
	 * @return	an index in the VIEWER_PREFERENCES array
	 */
	private int getIndex(PdfName key) {
		for (int i = 0; i < VIEWER_PREFERENCES.length; i++) {
			if (VIEWER_PREFERENCES[i].equals(key))
				return i;
		}
		return -1;
	}
	
	/**
	 * Checks if some value is valid for a certain key.
	 */
	private boolean isPossibleValue(PdfName value, PdfName[] accepted) {
		for (int i = 0; i < accepted.length; i++) {
			if (accepted[i].equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the viewer preferences for printing.
	 */
	public void addViewerPreference(PdfName key, PdfObject value) {
		switch(getIndex(key)) {
		case 0: // HIDETOOLBAR
		case 1: // HIDEMENUBAR
		case 2: // HIDEWINDOWUI
		case 3: // FITWINDOW
		case 4: // CENTERWINDOW
		case 5: // DISPLAYDOCTITLE
		case 14: // PICKTRAYBYPDFSIZE
			if (value instanceof PdfBoolean) {
				viewerPreferences.put(key, value);
			}
			break;
		case 6: // NONFULLSCREENPAGEMODE
			if (value instanceof PdfName
					&& isPossibleValue((PdfName)value, NONFULLSCREENPAGEMODE_PREFERENCES)) {
				viewerPreferences.put(key, value);
			}
			break;
		case 7: // DIRECTION
			if (value instanceof PdfName
					&& isPossibleValue((PdfName)value, DIRECTION_PREFERENCES)) {
				viewerPreferences.put(key, value);
			}
			break;
		case 8:  // VIEWAREA
		case 9:  // VIEWCLIP
		case 10: // PRINTAREA
		case 11: // PRINTCLIP
			if (value instanceof PdfName
					&& isPossibleValue((PdfName)value, PAGE_BOUNDARIES)) {
				viewerPreferences.put(key, value);
			}
			break;
		case 12: // PRINTSCALING
			if (value instanceof PdfName
					&& isPossibleValue((PdfName)value, PRINTSCALING_PREFERENCES)) {
				viewerPreferences.put(key, value);
			}
			break;
		case 13: // DUPLEX
			if (value instanceof PdfName
					&& isPossibleValue((PdfName)value, DUPLEX_PREFERENCES)) {
				viewerPreferences.put(key, value);
			}
			break;
		case 15: // PRINTPAGERANGE
			if (value instanceof PdfArray) {
				viewerPreferences.put(key, value);
			}
			break;
		case 16: // NUMCOPIES
			if (value instanceof PdfNumber)  {
				viewerPreferences.put(key, value);
			}
			break;
		}
	}

	/**
	 * Adds the viewer preferences defined in the preferences parameter to a
	 * PdfDictionary (more specifically the root or catalog of a PDF file).
	 * 
	 * @param catalog
	 */
	public void addToCatalog(PdfDictionary catalog) {
		// Page Layout
		catalog.remove(PdfName.PAGELAYOUT);
		if ((pageLayoutAndMode & PdfWriter.PAGE_LAYOUT_SINGLE_PAGE) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.SINGLEPAGE);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_LAYOUT_ONE_COLUMN) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.ONECOLUMN);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_LAYOUT_TWO_COLUMN_LEFT) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOCOLUMNLEFT);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_LAYOUT_TWO_COLUMN_RIGHT) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOCOLUMNRIGHT);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_LAYOUT_TWO_PAGE_LEFT) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOPAGELEFT);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_LAYOUT_TWO_PAGE_RIGHT) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOPAGERIGHT);

		// Page Mode
		catalog.remove(PdfName.PAGEMODE);
		if ((pageLayoutAndMode & PdfWriter.PAGE_MODE_USE_NONE) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USENONE);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_MODE_USE_OUTLINES) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_MODE_USE_THUMBS) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USETHUMBS);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_MODE_FULL_SCREEN) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.FULLSCREEN);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_MODE_USE_OC) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USEOC);
		else if ((pageLayoutAndMode & PdfWriter.PAGE_MODE_USE_ATTACHMENTS) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USEATTACHMENTS);

		// viewer preferences (Table 8.1 of the PDF Reference)
		catalog.remove(PdfName.VIEWERPREFERENCES);
		if (viewerPreferences.size() > 0) {
			catalog.put(PdfName.VIEWERPREFERENCES, viewerPreferences);
		}
	}

	public static PdfViewerPreferencesImp getViewerPreferences(PdfDictionary catalog) {
		PdfViewerPreferencesImp preferences = new PdfViewerPreferencesImp();
		int prefs = 0;
		PdfName name = null;
		// page layout
		PdfObject obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGELAYOUT));
		if (obj != null && obj.isName()) {
			name = (PdfName) obj;
			if (name.equals(PdfName.SINGLEPAGE))
				prefs |= PdfWriter.PAGE_LAYOUT_SINGLE_PAGE;
			else if (name.equals(PdfName.ONECOLUMN))
				prefs |= PdfWriter.PAGE_LAYOUT_ONE_COLUMN;
			else if (name.equals(PdfName.TWOCOLUMNLEFT))
				prefs |= PdfWriter.PAGE_LAYOUT_TWO_COLUMN_LEFT;
			else if (name.equals(PdfName.TWOCOLUMNRIGHT))
				prefs |= PdfWriter.PAGE_LAYOUT_TWO_COLUMN_RIGHT;
			else if (name.equals(PdfName.TWOPAGELEFT))
				prefs |= PdfWriter.PAGE_LAYOUT_TWO_PAGE_LEFT;
			else if (name.equals(PdfName.TWOPAGERIGHT))
				prefs |= PdfWriter.PAGE_LAYOUT_TWO_PAGE_RIGHT;
		}
		// page mode
		obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGEMODE));
		if (obj != null && obj.isName()) {
			name = (PdfName) obj;
			if (name.equals(PdfName.USENONE))
				prefs |= PdfWriter.PAGE_MODE_USE_NONE;
			else if (name.equals(PdfName.USEOUTLINES))
				prefs |= PdfWriter.PAGE_MODE_USE_OUTLINES;
			else if (name.equals(PdfName.USETHUMBS))
				prefs |= PdfWriter.PAGE_MODE_USE_THUMBS;
			else if (name.equals(PdfName.USEOC))
				prefs |= PdfWriter.PAGE_MODE_USE_OC;
			else if (name.equals(PdfName.USEATTACHMENTS))
				prefs |= PdfWriter.PAGE_MODE_USE_ATTACHMENTS;
		}
		// set page layout and page mode preferences
		preferences.setViewerPreferences(prefs);
		// other preferences
		obj = PdfReader.getPdfObjectRelease(catalog
				.get(PdfName.VIEWERPREFERENCES));
		if (obj != null && obj.isDictionary()) {
			PdfDictionary vp = (PdfDictionary) obj;
			for (int i = 0; i < VIEWER_PREFERENCES.length; i++) {
				obj = PdfReader.getPdfObjectRelease(vp.get(VIEWER_PREFERENCES[i]));
				preferences.addViewerPreference(VIEWER_PREFERENCES[i], obj);
			}
		}
		return preferences;
	}
}