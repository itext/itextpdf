package com.lowagie.text.pdf.internal;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfBoolean;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.interfaces.PdfViewerPreferences;

/**
 * Stores the information concerning viewer preferences,
 * and contains the business logic that allows you to set viewer preferences.
 */

public class PdfViewerPreferencesImp implements PdfViewerPreferences {
	
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
			if ((preferences & HideToolbar) != 0)
				viewerPreferences.put(PdfName.HIDETOOLBAR, PdfBoolean.PDFTRUE);
			if ((preferences & HideMenubar) != 0)
				viewerPreferences.put(PdfName.HIDEMENUBAR, PdfBoolean.PDFTRUE);
			if ((preferences & HideWindowUI) != 0)
				viewerPreferences.put(PdfName.HIDEWINDOWUI, PdfBoolean.PDFTRUE);
			if ((preferences & FitWindow) != 0)
				viewerPreferences.put(PdfName.FITWINDOW, PdfBoolean.PDFTRUE);
			if ((preferences & CenterWindow) != 0)
				viewerPreferences.put(PdfName.CENTERWINDOW, PdfBoolean.PDFTRUE);
			if ((preferences & DisplayDocTitle) != 0)
				viewerPreferences.put(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);
			
			if ((preferences & NonFullScreenPageModeUseNone) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USENONE);
			else if ((preferences & NonFullScreenPageModeUseOutlines) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOUTLINES);
			else if ((preferences & NonFullScreenPageModeUseThumbs) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USETHUMBS);
			else if ((preferences & NonFullScreenPageModeUseOC) != 0)
				viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOC);

			if ((preferences & DirectionL2R) != 0)
				viewerPreferences.put(PdfName.DIRECTION, PdfName.L2R);
			else if ((preferences & DirectionR2L) != 0)
				viewerPreferences.put(PdfName.DIRECTION, PdfName.R2L);

			if ((preferences & PrintScalingNone) != 0)
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
	 * @param viewerPreferences
	 * @param catalog
	 */
	public static void setViewerPreferences(
			PdfViewerPreferencesImp viewerPreferences, PdfDictionary catalog) {

		int preferences = viewerPreferences.pageLayoutAndMode;
		
		// Page Layout
		catalog.remove(PdfName.PAGELAYOUT);
		if ((preferences & PageLayoutSinglePage) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.SINGLEPAGE);
		else if ((preferences & PageLayoutOneColumn) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.ONECOLUMN);
		else if ((preferences & PageLayoutTwoColumnLeft) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOCOLUMNLEFT);
		else if ((preferences & PageLayoutTwoColumnRight) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOCOLUMNRIGHT);
		else if ((preferences & PageLayoutTwoPageLeft) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOPAGELEFT);
		else if ((preferences & PageLayoutTwoPageRight) != 0)
			catalog.put(PdfName.PAGELAYOUT, PdfName.TWOPAGERIGHT);

		// Page Mode
		catalog.remove(PdfName.PAGEMODE);
		if ((preferences & PageModeUseNone) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USENONE);
		else if ((preferences & PageModeUseOutlines) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
		else if ((preferences & PageModeUseThumbs) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USETHUMBS);
		else if ((preferences & PageModeFullScreen) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.FULLSCREEN);
		else if ((preferences & PageModeUseOC) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USEOC);
		else if ((preferences & PageModeUseAttachments) != 0)
			catalog.put(PdfName.PAGEMODE, PdfName.USEATTACHMENTS);

		// viewer preferences (Table 8.1 of the PDF Reference)
		catalog.remove(PdfName.VIEWERPREFERENCES);
		if (viewerPreferences.viewerPreferences.size() > 0) {
			catalog.put(PdfName.VIEWERPREFERENCES, viewerPreferences.viewerPreferences);
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
				prefs |= PdfViewerPreferences.PageLayoutSinglePage;
			else if (name.equals(PdfName.ONECOLUMN))
				prefs |= PdfViewerPreferences.PageLayoutOneColumn;
			else if (name.equals(PdfName.TWOCOLUMNLEFT))
				prefs |= PdfViewerPreferences.PageLayoutTwoColumnLeft;
			else if (name.equals(PdfName.TWOCOLUMNRIGHT))
				prefs |= PdfViewerPreferences.PageLayoutTwoColumnRight;
			else if (name.equals(PdfName.TWOPAGELEFT))
				prefs |= PdfViewerPreferences.PageLayoutTwoPageLeft;
			else if (name.equals(PdfName.TWOPAGERIGHT))
				prefs |= PdfViewerPreferences.PageLayoutTwoPageRight;
		}
		// page mode
		obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGEMODE));
		if (obj != null && obj.isName()) {
			name = (PdfName) obj;
			if (name.equals(PdfName.USENONE))
				prefs |= PdfViewerPreferences.PageModeUseNone;
			else if (name.equals(PdfName.USEOUTLINES))
				prefs |= PdfViewerPreferences.PageModeUseOutlines;
			else if (name.equals(PdfName.USETHUMBS))
				prefs |= PdfViewerPreferences.PageModeUseThumbs;
			else if (name.equals(PdfName.USEOC))
				prefs |= PdfViewerPreferences.PageModeUseOC;
			else if (name.equals(PdfName.USEATTACHMENTS))
				prefs |= PdfViewerPreferences.PageModeUseAttachments;
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