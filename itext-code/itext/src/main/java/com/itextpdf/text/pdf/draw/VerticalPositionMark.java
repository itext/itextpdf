/*
 * $Id: VerticalPositionMark.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.draw;

import java.util.List;
import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Helper class implementing the DrawInterface. Can be used to add
 * horizontal or vertical separators. Won't draw anything unless
 * you implement the draw method.
 * @since	2.1.2
 */

public class VerticalPositionMark implements DrawInterface, Element {

    /** Another implementation of the DrawInterface; its draw method will overrule LineSeparator.draw(). */
    protected DrawInterface drawInterface = null;

    /** The offset for the line. */
    protected float offset = 0;

	/**
	 * Creates a vertical position mark that won't draw anything unless
	 * you define a DrawInterface.
	 */
	public VerticalPositionMark() {
	}

	/**
	 * Creates a vertical position mark that won't draw anything unless
	 * you define a DrawInterface.
	 * @param	drawInterface	the drawInterface for this vertical position mark.
	 * @param	offset			the offset for this vertical position mark.
	 */
	public VerticalPositionMark(final DrawInterface drawInterface, final float offset) {
		this.drawInterface = drawInterface;
		this.offset = offset;
	}

	/**
	 * @see com.itextpdf.text.pdf.draw.DrawInterface#draw(com.itextpdf.text.pdf.PdfContentByte, float, float, float, float, float)
	 */
	public void draw(final PdfContentByte canvas, final float llx, final float lly, final float urx, final float ury, final float y) {
		if (drawInterface != null) {
			drawInterface.draw(canvas, llx, lly, urx, ury, y + offset);
		}
	}

    /**
     * @see com.itextpdf.text.Element#process(com.itextpdf.text.ElementListener)
     */
    public boolean process(final ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException e) {
			return false;
		}
    }

    /**
     * @see com.itextpdf.text.Element#type()
     */
    public int type() {
        return Element.YMARK;
    }

    /**
     * @see com.itextpdf.text.Element#isContent()
     */
    public boolean isContent() {
        return true;
    }

    /**
     * @see com.itextpdf.text.Element#isNestable()
     */
    public boolean isNestable() {
        return false;
    }

    /**
     * @see com.itextpdf.text.Element#getChunks()
     */
    public List<Chunk> getChunks() {
    	List<Chunk> list = new ArrayList<Chunk>();
    	list.add(new Chunk(this, true));
        return list;
    }

    /**
     * Getter for the interface with the overruling draw() method.
     * @return	a DrawInterface implementation
     */
    public DrawInterface getDrawInterface() {
        return drawInterface;
    }

    /**
     * Setter for the interface with the overruling draw() method.
     * @param drawInterface a DrawInterface implementation
     */
    public void setDrawInterface(final DrawInterface drawInterface) {
        this.drawInterface = drawInterface;
    }

    /**
     * Getter for the offset relative to the baseline of the current line.
     * @return	an offset
     */
    public float getOffset() {
        return offset;
    }

    /**
     * Setter for the offset. The offset is relative to the current
     * Y position. If you want to underline something, you have to
     * choose a negative offset.
     * @param offset	an offset
     */
    public void setOffset(final float offset) {
        this.offset = offset;
    }
}
