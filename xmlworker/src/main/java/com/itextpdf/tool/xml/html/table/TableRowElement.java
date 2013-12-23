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
package com.itextpdf.tool.xml.html.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.tool.xml.exceptions.NotImplementedException;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;

/**
 * @author redlab_b
 *
 */
public class TableRowElement implements Element {

	/**
	 * Enumeration used for determining the correct order of TableRowElements when adding the table to a document.
	 * <br />
	 * Possible values:
	 * <ul>
	 * <li>CAPTION_TOP</li>
	 * <li>HEADER</li>
	 * <li>BODY</li>
	 * <li>FOOTER</li>
	 * <li>CAPTION_BOTTOM</li>
	 * </ul>
	 *
	 * @author Emiel Ackermann
	 *
	 */
	public enum Place{
		/**
		 * The caption element on top
		 */
		CAPTION_TOP(-2, -2),/**
		 *
		 * A header row
		 */
		HEADER(-1, -1),/**
		 *
		 * Body rows
		 */
		BODY(0, 1),/**
		 *  Footer rows
		 */
		FOOTER(1, 0), /**
		 * The caption element in the bottom
		 */
		CAPTION_BOTTOM(2, 2);

		private Integer normal;
		private Integer repeated;

		private Place(final Integer normal, final Integer repeated) {
			this.normal = normal;
			this.repeated = repeated;
		}
		/**
		 * The position when header/footers should not be repeated on each page.
		 * @return an integer position
		 */
		public Integer getNormal() {
			return normal;
		}
		/**
		 * The position when headers/footers should be repeated on each page.
		 * @return an integer position
		 */
		public Integer getRepeated() {
			return repeated;
		}
	};
	private final Place place;
	private final List<HtmlCell> content;

    /**
     * Constructor based on the currentContent and a {@link Place}. All none {@link TableData} elements are filtered out of the current content list.
     * @param currentContent List<Element> containing all elements found between <tr> and </tr>.
     * @param place a {@link Place} in the table (caption, header, body or footer).
     */
    public TableRowElement(final List<Element> currentContent, final Place place) {
        // filter out none TD elements, discard others
    	content = new ArrayList<HtmlCell>(currentContent.size());
    	for (Element e : currentContent) {
    		if (e instanceof HtmlCell) {
    			content.add((HtmlCell) e);
    		}
    	}
        this.place = place;
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.Element#process(com.itextpdf.text.ElementListener)
     */
    public boolean process(final ElementListener listener) {
    	throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.Element#type()
     */
    public int type() {
    	throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.Element#isContent()
     */
    public boolean isContent() {
    	throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.Element#isNestable()
     */
    public boolean isNestable() {
    	throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.Element#getChunks()
     */
    public List<Chunk> getChunks() {
        throw new NotImplementedException();
    }

    /**
     * @return the content.
     */
    public List<HtmlCell> getContent() {
        return content;
    }

	/**
	 * @return the {@link Place} of the row.
	 */
	public Place getPlace() {
		return place;
	}
}
