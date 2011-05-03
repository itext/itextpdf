/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.html.table;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * @author Emiel Ackermann
 *
 */
public class CellSpacingEvent implements PdfPCellEvent {
	private final TableStyleValues styleValues;

	public CellSpacingEvent(final TableStyleValues styleValues) {
		this.styleValues = styleValues;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
	 *      com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(final PdfPCell cell, final Rectangle position,
			final PdfContentByte[] canvases) {
		float effectivePadding = cell.getBorderWidthLeft() + styleValues.getHorBorderSpacing();
		float x1 = position.getLeft() + effectivePadding;
		effectivePadding = cell.getBorderWidthRight() + styleValues.getHorBorderSpacing();
		float x2 = position.getRight() - effectivePadding;
		effectivePadding = cell.getBorderWidthTop() + styleValues.getVerBorderSpacing();
		float y1 = position.getTop() - effectivePadding;
		effectivePadding = cell.getBorderWidthBottom() + styleValues.getVerBorderSpacing();
		float y2 = position.getBottom() + effectivePadding;
		PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
		BaseColor borderColor = cell.getBorderColorLeft();
		// Checking one border side is enough
		if(borderColor != null) {
			cb.setLineWidth(cell.getBorderWidthLeft());
			cb.setColorStroke(borderColor);
	        cb.moveTo(x1, y1); // start leftUpperCorner
	        cb.lineTo(x1, y2); // left
	        cb.stroke();
	        cb.setLineWidth(cell.getBorderWidthBottom());
	        cb.setColorStroke(cell.getBorderColorBottom());
	        cb.moveTo(x1, y2); // left
	        cb.lineTo(x2, y2); // bottom
	        cb.stroke();
	        cb.setLineWidth(cell.getBorderWidthRight());
	        cb.setColorStroke(cell.getBorderColorRight());
	        cb.moveTo(x2, y2); // bottom
	        cb.lineTo(x2, y1); // right
	        cb.stroke();
	        cb.setLineWidth(cell.getBorderWidthTop());
	        cb.setColorStroke(cell.getBorderColorTop());
	        cb.moveTo(x2, y1); // right
	        cb.lineTo(x1, y1); // top
	        cb.stroke();
	        cb.resetRGBColorStroke();
		}
	}
}
