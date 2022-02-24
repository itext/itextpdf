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
package com.itextpdf.text.html.simpleparser;

import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * We use a CellWrapper because we need some extra info
 * that isn't available in PdfPCell.
 * @author  psoares
 * @since 5.0.6 (renamed)
 * @deprecated since 5.5.2
 */
@Deprecated
public class CellWrapper implements TextElementArray {

	/** The cell that is wrapped in this stub. */
    private final PdfPCell cell;

    /**
     * The width of the cell.
     * @since iText 5.0.6
     */
    private float width;

    /**
     * Indicates if the width is a percentage.
     * @since iText 5.0.6
     */
    private boolean percentage;

    /**
     * Creates a new instance of IncCell.
     * @param	tag		the cell that is wrapped in this object.
     * @param	chain	properties such as width
     * @since	5.0.6
     */
    public CellWrapper(final String tag, final ChainedProperties chain) {
        this.cell = createPdfPCell(tag, chain);
    	String value = chain.getProperty(HtmlTags.WIDTH);
        if (value != null) {
            value = value.trim();
        	if (value.endsWith("%")) {
        		percentage = true;
        		value = value.substring(0, value.length() - 1);
        	}
            width = Float.parseFloat(value);
        }
    }

    /**
     * Creates a PdfPCell element based on a tag and its properties.
     * @param	tag		a cell tag
     * @param	chain	the hierarchy chain
     * @return the created PdfPCell
     */
	public PdfPCell createPdfPCell(final String tag, final ChainedProperties chain) {
		PdfPCell cell = new PdfPCell((Phrase)null);
        // colspan
		String value = chain.getProperty(HtmlTags.COLSPAN);
        if (value != null)
            cell.setColspan(Integer.parseInt(value));
        // rowspan
        value = chain.getProperty(HtmlTags.ROWSPAN);
        if (value != null)
            cell.setRowspan(Integer.parseInt(value));
        // horizontal alignment
        if (tag.equals(HtmlTags.TH))
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        value = chain.getProperty(HtmlTags.ALIGN);
        if (value != null) {
            cell.setHorizontalAlignment(HtmlUtilities.alignmentValue(value));
        }
        // vertical alignment
        value = chain.getProperty(HtmlTags.VALIGN);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (value != null) {
            cell.setVerticalAlignment(HtmlUtilities.alignmentValue(value));
        }
        // border
        value = chain.getProperty(HtmlTags.BORDER);
        float border = 0;
        if (value != null)
            border = Float.parseFloat(value);
        cell.setBorderWidth(border);
        // cellpadding
        value = chain.getProperty(HtmlTags.CELLPADDING);
        if (value != null)
            cell.setPadding(Float.parseFloat(value));
        cell.setUseDescender(true);
        // background color
        value = chain.getProperty(HtmlTags.BGCOLOR);
        cell.setBackgroundColor(HtmlUtilities.decodeColor(value));
        return cell;
	}

    /**
     * Returns the PdfPCell.
     * @return the PdfPCell
     */
    public PdfPCell getCell() {
        return cell;
    }

    /**
     * Getter for the cell width
     * @return the width
     * @since iText 5.0.6
     */
    public float getWidth() {
        return width;
    }

    /**
     * Getter for percentage
     * @return true if the width is a percentage
     * @since iText 5.0.6
     */
    public boolean isPercentage() {
        return percentage;
    }

    /**
     * Implements the add method of the TextElementArray interface.
     * @param	o	an element that needs to be added to the cell.
     */
    public boolean add(final Element o) {
        cell.addElement(o);
        return true;
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
