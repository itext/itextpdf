/*
 * $Id: WritableDirectElement.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.api.WriterOperation;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

/**
 * An element that is not an element, it holds {@link Element#WRITABLE_DIRECT}
 * as Element type. It implements WriterOperation to do operations on the
 * {@link PdfWriter} and the {@link Document} that must be done at the time of
 * the writing. Much like a {@link VerticalPositionMark} but little different.
 *
 * @author itextpdf.com
 *
 */
public abstract class WritableDirectElement implements Element, WriterOperation {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.text.Element#process(com.itextpdf.text.ElementListener)
	 */
	public boolean process(final ElementListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return {@link Element#WRITABLE_DIRECT}
	 */
	public int type() {
		return Element.WRITABLE_DIRECT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.text.Element#isContent()
	 */
	public boolean isContent() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.text.Element#isNestable()
	 */
	public boolean isNestable() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.text.Element#getChunks()
	 */
	public List<Chunk> getChunks() {
		return new ArrayList<Chunk>(0);
	}

}
