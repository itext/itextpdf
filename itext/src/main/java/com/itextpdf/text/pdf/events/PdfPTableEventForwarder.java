/*
 * $Id: PdfPTableEventForwarder.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text.pdf.events;

import java.util.ArrayList;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfPTableEventAfterSplit;
import com.itextpdf.text.pdf.PdfPTableEventSplit;

/**
 * If you want to add more than one page event to a PdfPTable,
 * you have to construct a PdfPTableEventForwarder, add the
 * different events to this object and add the forwarder to
 * the PdfWriter.
 */

public class PdfPTableEventForwarder implements PdfPTableEventAfterSplit {

	/** ArrayList containing all the PageEvents that have to be executed. */
	protected ArrayList<PdfPTableEvent> events = new ArrayList<PdfPTableEvent>();

	/**
	 * Add a page event to the forwarder.
	 * @param event an event that has to be added to the forwarder.
	 */
	public void addTableEvent(PdfPTableEvent event) {
		events.add(event);
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfPTableEvent#tableLayout(com.itextpdf.text.pdf.PdfPTable, float[][], float[], int, int, com.itextpdf.text.pdf.PdfContentByte[])
	 */
	public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
		for (PdfPTableEvent event: events) {
			event.tableLayout(table, widths, heights, headerRows, rowStart, canvases);
		}
	}

    /**
     * @see com.itextpdf.text.pdf.PdfPTableEventSplit#splitTable(com.itextpdf.text.pdf.PdfPTable)
	 * @since iText 5.0.6
     */
    public void splitTable(PdfPTable table) {
		for (PdfPTableEvent event: events) {
			if (event instanceof PdfPTableEventSplit)
                ((PdfPTableEventSplit)event).splitTable(table);
		}
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfPTableEventAfterSplit#afterSplitTable(com.itextpdf.text.pdf.PdfPTable, com.itextpdf.text.pdf.PdfPRow, int)
     * @since iText 5.4.3
     */
    public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) {
        for (PdfPTableEvent event : events) {
            if (event instanceof PdfPTableEventAfterSplit)
                ((PdfPTableEventAfterSplit) event).afterSplitTable(table, startRow, startIdx);
        }
    }
}