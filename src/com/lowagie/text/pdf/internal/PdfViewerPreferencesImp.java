package com.lowagie.text.pdf.internal;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfBoolean;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

public class PdfViewerPreferencesImp implements PdfViewerPreferences {

	/** This value will hold the viewer preferences as a sequence of bits. */
	private int simpleViewerPreferences = 0;
	
	/** This dictionary holds viewer preferences for printing. */
	private PdfDictionary viewerPreferences = new PdfDictionary();
	
	/** The mask to decide if a ViewerPreferences dictionary is needed */
	private static final int simpleViewerPreferencesMask = 0xfff000;

	/** A series of viewer preferences. */
	private static final PdfName PAGE_VIEWER_PREFERENCES[] = {
		PdfName.VIEWAREA, PdfName.VIEWCLIP, PdfName.PRINTAREA, PdfName.PRINTCLIP
	};
	/** A series of viewer preferences. */
	private static final PdfName PAGE_BOUNDARIES[] = {
		PdfName.MEDIABOX, PdfName.CROPBOX, PdfName.BLEEDBOX, PdfName.TRIMBOX, PdfName.ARTBOX
	};
	/** A series of viewer preferences. */
	private static final PdfName DUPLEX_VIEWER_PREFERENCES[] = {
		PdfName.SIMPLEX, PdfName.DUPLEXFLIPSHORTEDGE, PdfName.DUPLEXFLIPSHORTEDGE
	};
	/** A series of viewer preferences. */
	private static final int VIEWER_PREFERENCES_INTS[] = {
		HideToolbar, HideMenubar, HideWindowUI, FitWindow, CenterWindow, DisplayDocTitle
	};
	/** A series of viewer preferences.  */
    private static final PdfName VIEWER_PREFERENCES_NAMES[] = {
    	PdfName.HIDETOOLBAR, PdfName.HIDEMENUBAR, PdfName.HIDEWINDOWUI, PdfName.FITWINDOW, PdfName.CENTERWINDOW, PdfName.DISPLAYDOCTITLE
    };
 
	/**
	 * Sets the viewer preferences as the sum of several constants.
	 * 
	 * @param preferences
	 *            the viewer preferences
	 * @see PdfViewerPreferences#setViewerPreferences
	 */
	public void setViewerPreferences(int preferences) {
		this.simpleViewerPreferences |= preferences;
	}
	
	/**
	 * Sets the viewer preferences for printing.
	 */
	public void addViewerPreference(PdfName key, PdfObject value) {
		boolean keyOk = false;
		boolean valueOk = false;
		for (int i = 0; i < PAGE_VIEWER_PREFERENCES.length && !keyOk; i++) {
			if (PAGE_VIEWER_PREFERENCES[i].equals(key))
				keyOk = true;
		}
		for (int i = 0; i < PAGE_BOUNDARIES.length && !valueOk; i++) {
			if (PAGE_BOUNDARIES[i].equals(value))
				valueOk = true;
		}
		if (valueOk && keyOk) {
			viewerPreferences.put(key, value);
			return;
		}
		if (PdfName.PRINTSCALING.equals(key)) {
			if(PdfName.NONE.equals(value) || PdfName.APPDEFAULT.equals(value))
				viewerPreferences.put(key, value);
		}
		else if(PdfName.DUPLEX.equals(key)) {
			for (int i = 0; i < DUPLEX_VIEWER_PREFERENCES.length; i++) {
				if (DUPLEX_VIEWER_PREFERENCES[i].equals(value))
					viewerPreferences.put(key, value);
			}
		}
		else if (PdfName.PICKTRAYBYPDFSIZE.equals(key)) {
			if (value instanceof PdfBoolean)
				viewerPreferences.put(key, value);
		}
		else if (PdfName.PRINTPAGERANGE.equals(key)) {
			if (value instanceof PdfArray)
				viewerPreferences.put(key, value);
		}
		else if (PdfName.NUMCOPIES.equals(key)) {
			if (value instanceof PdfNumber)
				viewerPreferences.put(key, value);
		}
	}

	/**
	 * Returns the viewer preferences.
	 */
	public int getSimpleViewerPreferences() {
		return simpleViewerPreferences;
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

		int preferences = viewerPreferences.simpleViewerPreferences;
		
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
		PdfDictionary vp = new PdfDictionary();
		vp.putAll(viewerPreferences.viewerPreferences);
		if (vp.size() == 0 && (preferences & PdfViewerPreferencesImp.simpleViewerPreferencesMask) == 0) {
			return;
		}

		if ((preferences & HideToolbar) != 0)
			vp.put(PdfName.HIDETOOLBAR, PdfBoolean.PDFTRUE);
		if ((preferences & HideMenubar) != 0)
			vp.put(PdfName.HIDEMENUBAR, PdfBoolean.PDFTRUE);
		if ((preferences & HideWindowUI) != 0)
			vp.put(PdfName.HIDEWINDOWUI, PdfBoolean.PDFTRUE);
		if ((preferences & FitWindow) != 0)
			vp.put(PdfName.FITWINDOW, PdfBoolean.PDFTRUE);
		if ((preferences & CenterWindow) != 0)
			vp.put(PdfName.CENTERWINDOW, PdfBoolean.PDFTRUE);
		if ((preferences & DisplayDocTitle) != 0)
			vp.put(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);

		if ((preferences & NonFullScreenPageModeUseNone) != 0)
			vp.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USENONE);
		else if ((preferences & NonFullScreenPageModeUseOutlines) != 0)
			vp.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOUTLINES);
		else if ((preferences & NonFullScreenPageModeUseThumbs) != 0)
			vp.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USETHUMBS);
		else if ((preferences & NonFullScreenPageModeUseOC) != 0)
			vp.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOC);

		if ((preferences & DirectionL2R) != 0)
			vp.put(PdfName.DIRECTION, PdfName.L2R);
		else if ((preferences & DirectionR2L) != 0)
			vp.put(PdfName.DIRECTION, PdfName.R2L);

		if ((preferences & PrintScalingNone) != 0)
			vp.put(PdfName.PRINTSCALING, PdfName.NONE);

		catalog.put(PdfName.VIEWERPREFERENCES, vp);
	}

	public static PdfViewerPreferencesImp getViewerPreferences(PdfDictionary catalog) {
		PdfViewerPreferencesImp preferences = new PdfViewerPreferencesImp();
		int prefs = 0;
		PdfName name = null;
		PdfObject obj = PdfReader.getPdfObjectRelease(catalog
				.get(PdfName.PAGELAYOUT));
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
		obj = PdfReader.getPdfObjectRelease(catalog
				.get(PdfName.VIEWERPREFERENCES));
		if (obj != null && obj.isDictionary()) {
			PdfDictionary vp = (PdfDictionary) obj;
			for (int k = 0; k < VIEWER_PREFERENCES_NAMES.length; ++k) {
				obj = PdfReader.getPdfObject(vp.get(VIEWER_PREFERENCES_NAMES[k]));
				if (obj != null && "true".equals(obj.toString()))
					prefs |= VIEWER_PREFERENCES_INTS[k];
			}
			obj = PdfReader.getPdfObjectRelease(vp.get(PdfName.PRINTSCALING));
			if (PdfName.NONE.equals(obj))
				prefs |= PdfViewerPreferences.PrintScalingNone;
			obj = PdfReader.getPdfObjectRelease(vp.get(PdfName.NONFULLSCREENPAGEMODE));
			if (obj != null && obj.isName()) {
				name = (PdfName) obj;
				if (name.equals(PdfName.USENONE))
					prefs |= PdfViewerPreferences.NonFullScreenPageModeUseNone;
				else if (name.equals(PdfName.USEOUTLINES))
					prefs |= PdfViewerPreferences.NonFullScreenPageModeUseOutlines;
				else if (name.equals(PdfName.USETHUMBS))
					prefs |= PdfViewerPreferences.NonFullScreenPageModeUseThumbs;
				else if (name.equals(PdfName.USEOC))
					prefs |= PdfViewerPreferences.NonFullScreenPageModeUseOC;
			}
			obj = PdfReader.getPdfObjectRelease(vp.get(PdfName.DIRECTION));
			if (obj != null && obj.isName()) {
				name = (PdfName) obj;
				if (name.equals(PdfName.L2R))
					prefs |= PdfViewerPreferences.DirectionL2R;
				else if (name.equals(PdfName.R2L))
					prefs |= PdfViewerPreferences.DirectionR2L;
			}
		}
		preferences.setViewerPreferences(prefs);
		return preferences;
	}
}
