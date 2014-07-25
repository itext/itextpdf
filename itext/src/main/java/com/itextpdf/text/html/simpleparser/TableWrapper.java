/*
 * $Id: TableWrapper.java 6134 2013-12-23 13:15:14Z blowagie $
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
package com.itextpdf.text.html.simpleparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * We use a TableWrapper because PdfPTable is rather complex
 * to put on the HTMLWorker stack.
 * @author  psoares
 * @since 5.0.6 (renamed)
 */
public class TableWrapper implements Element {
	/**
	 * The styles that need to be applied to the table
	 * @since 5.0.6 renamed from props
	 */
    private final Map<String, String> styles = new HashMap<String, String>();
    /**
     * Nested list containing the PdfPCell elements that are part of this table.
     */
    private final List<List<PdfPCell>> rows = new ArrayList<List<PdfPCell>>();

    /**
     * Array containing the widths of the columns.
     * @since iText 5.0.6
     */
    private float[] colWidths;

    /**
     * Creates a new instance of IncTable.
     * @param	attrs	a Map containing attributes
     */
    public TableWrapper(final Map<String, String> attrs) {
        this.styles.putAll(attrs);
    }

    /**
     * Adds a new row to the table.
     * @param row a list of PdfPCell elements
     */
    public void addRow(List<PdfPCell> row) {
        if (row != null) {
            Collections.reverse(row);
            rows.add(row);
            row = null;
        }
    }

    /**
     * Setter for the column widths
     * @since iText 5.0.6
     */
    public void setColWidths(final float[] colWidths) {
        this.colWidths = colWidths;
    }

    /**
     * Creates a new PdfPTable based on the info assembled
     * in the table stub.
     * @return	a PdfPTable
     */
    public PdfPTable createTable() {
    	// no rows = simplest table possible
        if (rows.isEmpty())
            return new PdfPTable(1);
        // how many columns?
        int ncol = 0;
        for (PdfPCell pc : rows.get(0)) {
            ncol += pc.getColspan();
        }
        PdfPTable table = new PdfPTable(ncol);
        // table width
        String width = styles.get(HtmlTags.WIDTH);
        if (width == null)
            table.setWidthPercentage(100);
        else {
            if (width.endsWith("%"))
                table.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
            else {
                table.setTotalWidth(Float.parseFloat(width));
                table.setLockedWidth(true);
            }
        }
        // horizontal alignment
        String alignment = styles.get(HtmlTags.ALIGN);
        int align = Element.ALIGN_LEFT;
        if (alignment != null) {
        	align = HtmlUtilities.alignmentValue(alignment);
        }
        table.setHorizontalAlignment(align);
        // column widths
		try {
			if (colWidths != null)
				table.setWidths(colWidths);
		} catch (Exception e) {
			// fail silently
		}
		// add the cells
        for (List<PdfPCell> col : rows) {
            for (PdfPCell pc : col) {
                table.addCell(pc);
            }
        }
        return table;
    }

    // these Element methods are irrelevant for a table stub.

    /**
     * @since 5.0.1
     */
    public List<Chunk> getChunks() {
        return null;
    }

    /**
     * @since 5.0.1
     */
    public boolean isContent() {
        return false;
    }

    /**
     * @since 5.0.1
     */
    public boolean isNestable() {
        return false;
    }

    /**
     * @since 5.0.1
     */
    public boolean process(final ElementListener listener) {
        return false;
    }

    /**
     * @since 5.0.1
     */
    public int type() {
        return 0;
    }

}
