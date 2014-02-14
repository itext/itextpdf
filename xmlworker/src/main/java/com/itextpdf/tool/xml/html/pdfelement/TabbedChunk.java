/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.tool.xml.html.pdfelement;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

/**
 *
 * A TabbedChunk is a Chunk that contains tabcount and alignment
 * data to allow the HTML to PDF conversion to accept tabstop css.
 *
 */
public class TabbedChunk extends Chunk {
	private int tabCount;
	private String alignment;

	/**
	 * Constructor
	 * @param content the content of the Chunk
	 */
	public TabbedChunk(final String content) {
		super(content);
	}

	/**
	 *
	 * @param verticalPositionMark the drawInterface used to draw the tab. tabPosition an X
	 * @param parseToPt that will be used as start position for the next Chunk.
	 * @param b if true, a newline will be added if the tabPosition has already
	 * been reached.
	 * @param alignment the alignment
	 */
	public TabbedChunk(final VerticalPositionMark verticalPositionMark,
			final float parseToPt, final boolean b, final String alignment) {
		super(verticalPositionMark, parseToPt, b);
		this.alignment = alignment;
	}

	/**
	 * @param verticalPositionMark the drawInterface to use to draw the tab. tabPosition an X
	 * @param parseToPt that will be used as start position for the next Chunk.
	 * @param b if true, a newline will be added if the tabPosition has already
	 * been reached.
	 */
	public TabbedChunk(final VerticalPositionMark verticalPositionMark,
			final float parseToPt, final boolean b) {
		super(verticalPositionMark, parseToPt, b);
	}

	/**
	 * Set the tabCount for this Chunk.
	 * @param tabCount  the tabcount
	 */
	public void setTabCount(final int tabCount) {
		this.tabCount = tabCount;
	}

	/**
	 * Returns the tabCount for this Chunk.
	 * @return tabCount
	 */
	public int getTabCount() {
		return tabCount;
	}

	/**
	 * Returns the alignment for this Chunk.
	 * @param alignment the alignment
	 */
	public void setAlignment(final String alignment) {
		this.alignment = alignment;
	}

	/**
	 * Return the Alignment for this Chunk
	 * @return alignment
	 */
	public String getAlignment() {
		return alignment;
	}

}
