/*
 * $Id: ZapfDingbatsList.java 6134 2013-12-23 13:15:14Z blowagie $
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
 * A special-version of <CODE>LIST</CODE> which use zapfdingbats-letters.
 *
 * @see com.itextpdf.text.List
 * @author Michael Niedermair and Bruno Lowagie
 */

public class ZapfDingbatsList extends List {

	/**
	 * char-number in zapfdingbats
	 */
	protected int zn;

	/**
	 * Creates a ZapfDingbatsList
	 *
	 * @param zn a char-number
	 */
	public ZapfDingbatsList(int zn) {
		super(true);
		this.zn = zn;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

	/**
	 * Creates a ZapfDingbatsList
	 *
	 * @param zn a char-number
	 * @param symbolIndent	indent
	 */
	public ZapfDingbatsList(int zn, int symbolIndent) {
		super(true, symbolIndent);
		this.zn = zn;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL));
		postSymbol = " ";
	}

    /**
     * Creates a ZapfDingbatList with a colored symbol
     *
     * @param zn a char-number
     * @param symbolIndent indent
     * @param zapfDingbatColor color for the ZpafDingbat
     */
    public ZapfDingbatsList(int zn, int symbolIndent, BaseColor zapfDingbatColor) {
		super(true, symbolIndent);
		this.zn = zn;
		float fontsize = symbol.getFont().getSize();
		symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL, zapfDingbatColor));
		postSymbol = " ";
	}

    /**
     * Sets the dingbat's color.
     *
     * @param zapfDingbatColor color for the ZapfDingbat
     */
    public void setDingbatColor(BaseColor zapfDingbatColor) {
        float fontsize = symbol.getFont().getSize();
        symbol.setFont(FontFactory.getFont(FontFactory.ZAPFDINGBATS, fontsize, Font.NORMAL, zapfDingbatColor));
    }

	/**
	 * set the char-number
	 * @param zn a char-number
	 */
	public void setCharNumber(int zn) {
		this.zn = zn;
	}

	/**
	 * get the char-number
	 *
	 * @return	char-number
	 */
	public int getCharNumber() {
		return zn;
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
			chunk.append(String.valueOf((char)zn));
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
