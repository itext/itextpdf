/*
 * $Id: ZapfDingbatsNumberList.java 6134 2013-12-23 13:15:14Z blowagie $
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
package com.itextpdf.text;

/**
 *
 * A special-version of <CODE>LIST</CODE> which use zapfdingbats-numbers (1..10).
 *
 * @see com.itextpdf.text.List
 * @author Michael Niedermair and Bruno Lowagie
 */

public class ZapfDingbatsNumberList extends List {

	/**
	 * which type
	 */
	protected int type;

	/**
	 * Creates a ZapdDingbatsNumberList
	 * @param type the type of list
	 */
	public ZapfDingbatsNumberList(int type) {
		super(true);
		this.type = type;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * Creates a ZapdDingbatsNumberList
	 * @param type the type of list
	 * @param symbolIndent	indent
	 */
	public ZapfDingbatsNumberList(int type, int symbolIndent) {
		super(true, symbolIndent);
		this.type = type;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * set the type
	 *
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * get the type
	 *
	 * @return	char-number
	 */
	public int getType() {
		return type;
	}

	/**
	 * Adds an <CODE>Element</CODE> to the <CODE>List</CODE>.
	 *
	 * @param	o	the object to add.
	 * @return true if adding the object succeeded
	 */
	@Override
        public boolean add(Element o) {
		if (o instanceof ListItem) {
			ListItem item = (ListItem) o;
			Chunk chunk = new Chunk(preSymbol, symbol.getFont());
            chunk.setAttributes(symbol.getAttributes());
			switch (type ) {
				case 0:
					chunk.append(String.valueOf((char)(first + list.size() + 171)));
					break;
				case 1:
					chunk.append(String.valueOf((char)(first + list.size() + 181)));
					break;
				case 2:
					chunk.append(String.valueOf((char)(first + list.size() + 191)));
					break;
				default:
					chunk.append(String.valueOf((char)(first + list.size() + 201)));
			}
			chunk.append(postSymbol);
			item.setListSymbol(chunk);
			item.setIndentationLeft(symbolIndent, autoindent);
			item.setIndentationRight(0);
			list.add(item);
		} else if (o instanceof List) {
			List nested = (List) o;
			nested.setIndentationLeft(nested.getIndentationLeft() + symbolIndent);
			first--;
			return list.add(nested);
		}
		return false;
	}
}
