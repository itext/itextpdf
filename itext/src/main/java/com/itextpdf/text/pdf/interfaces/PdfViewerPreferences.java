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
package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

/**
 * Viewer preferences are described in section 3.6.1 and 8.1 of the
 * PDF Reference 1.7 (Table 3.25 on p139-142 and Table 8.1 on p579-581).
 * They are explained in section 13.1 of the book 'iText in Action'.
 * The values of the different  preferences were originally stored
 * in class PdfWriter, but they have been moved to this separate interface
 * for reasons of convenience.
 */

public interface PdfViewerPreferences {	
		
    /**
     * Sets the page layout and page mode preferences by ORing one or two of these constants.
     * <p>
     * <ul>
     * <li>The page layout to be used when the document is opened (choose one).
     *   <ul>
     *   <li><b>PageLayoutSinglePage</b> - Display one page at a time. (default)
     *   <li><b>PageLayoutOneColumn</b> - Display the pages in one column.
     *   <li><b>PageLayoutTwoColumnLeft</b> - Display the pages in two columns, with
     *       odd-numbered pages on the left.
     *   <li><b>PageLayoutTwoColumnRight</b> - Display the pages in two columns, with
     *       odd-numbered pages on the right.
     *   <li><b>PageLayoutTwoPageLeft</b> - Display the pages two at a time, with
     *       odd-numbered pages on the left.
     *   <li><b>PageLayoutTwoPageRight</b> - Display the pages two at a time, with
     *       odd-numbered pages on the right.
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
     *   <li><b>NNonFullScreenPageModeUseThumbs</b> - Thumbnail images visible
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
     * 			<li>PdfName.<b>ARTBOX</b>
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
     * <li>In case the key is PdfName.<b>PICKTRAYBYPDFSIZE</b>, the value must be of type PdfBoolean.
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
