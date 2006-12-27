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

package com.lowagie.text.pdf.interfaces;

import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;

/**
 * Viewer preferences are described in section 3.6.1 and 8.1 of the
 * PDF Reference 1.7 (Table 3.25 on p139-142 and Table 8.1 on p579-581).
 * They are explained in section 13.1 of the book 'iText in Action'.
 * The values of the different  * preferences were originally stored
 * in class PdfWriter, but they have been moved to this separate interface
 * for reasons of convenience.
 */

public interface PdfViewerPreferences {

	// page layout (section 13.1.1 of "iText in Action")
	
    /** A viewer preference */
	public static final int PageLayoutSinglePage = 1;
	/** A viewer preference */
	public static final int PageLayoutOneColumn = 2;
	/** A viewer preference */
	public static final int PageLayoutTwoColumnLeft = 4;
	/** A viewer preference */
	public static final int PageLayoutTwoColumnRight = 8;
	/** A viewer preference */
	public static final int PageLayoutTwoPageLeft = 16;
	/** A viewer preference */
	public static final int PageLayoutTwoPageRight = 32;

	// page mode (section 13.1.2 of "iText in Action")
	
	/** A viewer preference */
	public static final int PageModeUseNone = 64;
	/** A viewer preference */
	public static final int PageModeUseOutlines = 128;
	/** A viewer preference */
	public static final int PageModeUseThumbs = 256;
	/** A viewer preference */
	public static final int PageModeFullScreen = 512;
	/** A viewer preference */
	public static final int PageModeUseOC = 1024;
	/** A viewer preference */
	public static final int PageModeUseAttachments = 2048;

	// viewer preferences PDF Reference table 8.1

	final PdfName[] VIEWER_PREFERENCES = {
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
		PdfName.SIMPLEX, PdfName.DUPLEXFLIPSHORTEDGE, PdfName.DUPLEXFLIPSHORTEDGE
	};	
	
	// values for setting viewer preferences in iText versions older than 1.5.x
	
	/** A viewer preference */
	public static final int HideToolbar = 1 << 12;
	/** A viewer preference */
	public static final int HideMenubar = 1 << 13;
	/** A viewer preference */
	public static final int HideWindowUI = 1 << 14;
	/** A viewer preference */
	public static final int FitWindow = 1 << 15;
	/** A viewer preference */
	public static final int CenterWindow = 1 << 16;
	/** A viewer preference */
	public static final int DisplayDocTitle = 1 << 17;

	/** A viewer preference */
	public static final int NonFullScreenPageModeUseNone = 1 << 18;
	/** A viewer preference */
	public static final int NonFullScreenPageModeUseOutlines = 1 << 19;
	/** A viewer preference */
	public static final int NonFullScreenPageModeUseThumbs = 1 << 20;
	/** A viewer preference */
	public static final int NonFullScreenPageModeUseOC = 1 << 21;

	/** A viewer preference */
	public static final int DirectionL2R = 1 << 22;
	/** A viewer preference */
	public static final int DirectionR2L = 1 << 23;

	/** A viewer preference */
	public static final int PrintScalingNone = 1 << 24;
		
    /**
     * Sets the page layout and page mode preferences by ORing one or two of these constants.
     * <p>
     * <ul>
     * <li>The page layout to be used when the document is opened (choose one).
     *   <ul>
     *   <li><b>PageLayoutSinglePage</b> - Display one page at a time. (default)
     *   <li><b>PageLayoutOneColumn</b> - Display the pages in one column.
     *   <li><b>PageLayoutTwoColumnLeft</b> - Display the pages in two columns, with
     *       oddnumbered pages on the left.
     *   <li><b>PageLayoutTwoColumnRight</b> - Display the pages in two columns, with
     *       oddnumbered pages on the right.
     *   <li><b>PageLayoutTwoPageLeft</b> - Display the pages two at a time, with
     *       oddnumbered pages on the left.
     *   <li><b>PageLayoutTwoPageRight</b> - Display the pages two at a time, with
     *       oddnumbered pages on the right.
     *   </ul>
     * <li>The page mode how the document should be displayed
     *     when opened (choose one).
     *   <ul>
     *   <li><b>PageModeUseNone</b> - Neither document outline nor thumbnail images visible. (default)
     *   <li><b>PageModeUseOutlines</b> - Document outline visible.
     *   <li><b>PageModeUseThumbs</b> - Thumbnail images visible.
     *   <li><b>PageModeFullScreen</b> - Full-screen mode, with no menu bar, window
     *       controls, or any other window visible.
     *   <li><b>PageModeUseOC</b> - Optional content group panel visible
     *   <li><b>PageModeUseAttachments</b> - Attachments panel visible
     *   </ul>
     * </ul>
     * For backward compatibility these values are also supported,
     * but it's better to use method <code>addViewerPreference(key, value)</code>
     * if you want to change the following preferences:
     * <ul>
     * <li><b>HideToolbar</b> - A flag specifying whether to hide the viewer application's tool
     *     bars when the document is active.
     * <li><b>HideMenubar</b> - A flag specifying whether to hide the viewer application's
     *     menu bar when the document is active.
     * <li><b>HideWindowUI</b> - A flag specifying whether to hide user interface elements in
     *     the document's window (such as scroll bars and navigation controls),
     *     leaving only the document's contents displayed.
     * <li><b>FitWindow</b> - A flag specifying whether to resize the document's window to
     *     fit the size of the first displayed page.
     * <li><b>CenterWindow</b> - A flag specifying whether to position the document's window
     *     in the center of the screen.
     * <li><b>DisplayDocTitle</b> - A flag specifying whether to display the document's title
     *     in the top bar.
     * <li>The predominant reading order for text. This entry has no direct effect on the
     *     document's contents or page numbering, but can be used to determine the relative
     *     positioning of pages when displayed side by side or printed <i>n-up</i> (choose one).
     *   <ul>
     *   <li><b>DirectionL2R</b> - Left to right
     *   <li><b>DirectionR2L</b> - Right to left (including vertical writing systems such as
     *       Chinese, Japanese, and Korean)
     *   </ul>
     * <li>The document's page mode, specifying how to display the
     *     document on exiting full-screen mode. It is meaningful only
     *     if the page mode is <b>PageModeFullScreen</b> (choose one).
     *   <ul>
     *   <li><b>NonFullScreenPageModeUseNone</b> - Neither document outline nor thumbnail images
     *       visible
     *   <li><b>NonFullScreenPageModeUseOutlines</b> - Document outline visible
     *   <li><b>NonFullScreenPageModeUseThumbs</b> - Thumbnail images visible
     *   <li><b>NonFullScreenPageModeUseOC</b> - Optional content group panel visible
     *   </ul>
     * <li><b>PrintScalingNone</b> - Indicates that the print dialog should reflect no page scaling.
     * </ul>
     * @param preferences the viewer preferences
	 * @see PdfViewerPreferences#addViewerPreference
     */
    public void setViewerPreferences(int preferences);
    
    /**
     * Adds a viewer preference.
     * <ul>
     * <li>In case the key is one of these values:
     * 		<ul>
     * 			<li>PdfName.<b>HIDETOOLBAR</b>
     * 			<li>PdfName.<b>HIDEMENUBAR</b>
     * 			<li>PdfName.<b>HIDEWINDOWUI</b>
     * 			<li>PdfName.<b>FITWINDOW</b>
     * 			<li>PdfName.<b>CENTERWINDOW</b>
     * 			<li>PdfName.<b>DISPLAYDOCTITLE</b>
     * 		</ul>
     * The value must be a of type PdfBoolean (true or false).
     * <li>In case the key is PdfName.<b>NONFULLSCREENPAGEMODE</b>,
     * the value must be one of these names:
     * 		<ul>
     * 			<li>PdfName.<b>USENONE</b>
     * 			<li>PdfName.<b>USEOUTLINES</b>
     * 			<li>PdfName.<b>USETHUMBS</b>
     * 			<li>PdfName.<b>USEOC</b>
     * 		</ul>
     * <li>In case the key is PdfName.DIRECTION,
     * the value must be one of these names:
     * 		<ul>
     * 			<li>PdfName.<b>L2R</b>
     * 			<li>PdfName.<b>R2L</b>
     * 		</ul>
     * <li>In case the key is one of these values:
     * 		<ul>
     * 			<li>PdfName.<b>VIEWAREA</b>
     * 			<li>PdfName.<b>VIEWCLIP</b>
     * 			<li>PdfName.<b>PRINTAREA</b>
     * 			<li>PdfName.<b>PRINTCLIP</b>
     * 		</ul>
     * 	The value must be one of these names:
     * 		<ul>
     * 			<li>PdfName.<b>MEDIABOX</b>
     * 			<li>PdfName.<b>CROPBOX</b>
     * 			<li>PdfName.<b>BLEEDBOX</b>
     * 			<li>PdfName.<b>TRIMBOX</b>
     * 			<li>PdfName.<b>AREABOX</b>
     * 		</ul>
     * <li>In case the key is PdfName.<b>PRINTSCALING</b>, the value can be
     * 		<ul>
     * 			<li>PdfName.<b>APPDEFAULT</b>
     * 			<li>PdfName.<b>NONE</b>
     * 		</ul>
     * <li>In case the key is PdfName.<b>DUPLEX</b>, the value can be:
     * 		<ul>
     * 			<li>PdfName.<b>SIMPLEX</b>
     * 			<li>PdfName.<b>DUPLEXFLIPSHORTEDGE</b>
     * 			<li>PdfName.<b>DUPLEXFLIPLONGEDGE</b>
     * 		</ul>
     * <li>In case the key is PdfName.<b>PickTrayBySize</b>, the value must be of type PdfBoolean.
     * <li>In case the key is PdfName.<b>PRINTPAGERANGE</b>, the value must be of type PdfArray.
     * <li>In case the key is PdfName.<b>NUMCOPIES</b>, the value must be of type PdfNumber.
     * <ul>
     * </ul>
     * @param key	the name of the viewer preference
     * @param value	the value of the viewer preference
	 * @see PdfViewerPreferences#setViewerPreferences
     */
    public void addViewerPreference(PdfName key, PdfObject value);    	
}