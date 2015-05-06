/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.html.table;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;

/**
 * @author Emiel Ackermann
 *
 */
public class TableBorderEvent implements PdfPTableEvent{
	/**
	 *
	 */
	private final TableStyleValues styleValues;

	/**
	 * Construct a new TableBorderEvent with the given TableStyleValues.
	 *
	 * @param styleValues the TableStyleValues
	 * @see com.itextpdf.text.pdf.PdfPTableEvent#tableLayout(com.itextpdf.text.pdf.PdfPTable,
	 *      float[][], float[], int, int,
	 *      com.itextpdf.text.pdf.PdfContentByte[])
	 */
    public TableBorderEvent(final TableStyleValues styleValues) {
    	this.styleValues = styleValues;
    }
    public void tableLayout(final PdfPTable table, final float[][] width, final float[] height,
            final int headerRows, final int rowStart, final PdfContentByte[] canvas) {
    	float left = styleValues.getBorderWidthLeft();
    	float right = styleValues.getBorderWidthRight();
    	float top = styleValues.getBorderWidthTop();
    	float bottom = styleValues.getBorderWidthBottom();
        float widths[] = width[0];
		float effectivePadding = left/2;
        float x1 = widths[0]-effectivePadding;
		effectivePadding = right/2;
        float x2 = widths[widths.length - 1]+effectivePadding;
		effectivePadding = top/2;
        float y1 = height[0]+effectivePadding;
		effectivePadding = bottom/2+styleValues.getVerBorderSpacing();
        float y2 = height[height.length - 1]-effectivePadding;
        PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
        BaseColor color = styleValues.getBackground();
        if(color != null) {
        	cb.setColorFill(color);
        	cb.rectangle(x1, y1, x2-x1, y2-y1);
        	cb.fill();
        }
        cb = canvas[PdfPTable.LINECANVAS];
        if(left != 0) {
        	color = styleValues.getBorderColorLeft();
        	if(color == null) {
        		color = BaseColor.BLACK;
        	}
        	cb.setLineWidth(left);
        	cb.setColorStroke(color);
        	cb.moveTo(x1, y1); // start leftUpperCorner
        	cb.lineTo(x1, y2); // left
        	cb.stroke();
        }
        if(bottom != 0) {
        	color = styleValues.getBorderColorBottom();
        	if(color == null) {
        		color = BaseColor.BLACK;
        	}
        	cb.setLineWidth(bottom);
 	        cb.setColorStroke(color);
 	        cb.moveTo(x1, y2); // left
 	        cb.lineTo(x2, y2); // bottom
 	        cb.stroke();
        }
        if(right != 0) {
        	color = styleValues.getBorderColorRight();
        	if(color == null) {
        		color = BaseColor.BLACK;
        	}
        	cb.setLineWidth(right);
	        cb.setColorStroke(color);
	        cb.moveTo(x2, y2); // bottom
	        cb.lineTo(x2, y1); // right
	        cb.stroke();
        }
        if(top != 0) {
        	color = styleValues.getBorderColorTop();
        	if(color == null) {
        		color = BaseColor.BLACK;
        	}
        	cb.setLineWidth(top);
	        cb.setColorStroke(color);
	        cb.moveTo(x2, y1); // right
	        cb.lineTo(x1, y1); // top
	        cb.stroke();
        }
        cb.resetRGBColorStroke();
    }

    /**
     * Returns the used TableStyleValues
     * @return TableStyleValues
     */
	public TableStyleValues getTableStyleValues(){
		return this.styleValues;
	}

}
