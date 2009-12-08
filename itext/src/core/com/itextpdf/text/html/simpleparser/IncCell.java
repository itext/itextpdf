/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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

import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.html.Markup;
import com.itextpdf.text.pdf.PdfPCell;
/**
 *
 * @author  psoares
 */
public class IncCell implements TextElementArray {
    
    private ArrayList chunks = new ArrayList();
    private PdfPCell cell;
    
    /** Creates a new instance of IncCell */
    public IncCell(String tag, ChainedProperties props) {
        cell = new PdfPCell((Phrase)null);
        String value = props.getProperty("colspan");
        if (value != null)
            cell.setColspan(Integer.parseInt(value));
        value = props.getProperty("align");
        if (tag.equals("th"))
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (value != null) {
            if ("center".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            else if ("right".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            else if ("left".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            else if ("justify".equalsIgnoreCase(value))
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        }
        value = props.getProperty("valign");
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (value != null) {
            if ("top".equalsIgnoreCase(value))
                cell.setVerticalAlignment(Element.ALIGN_TOP);
            else if ("bottom".equalsIgnoreCase(value))
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        }
        value = props.getProperty("border");
        float border = 0;
        if (value != null)
            border = Float.parseFloat(value);
        cell.setBorderWidth(border);
        value = props.getProperty("cellpadding");
        if (value != null)
            cell.setPadding(Float.parseFloat(value));
        cell.setUseDescender(true);
        value = props.getProperty("bgcolor");
        cell.setBackgroundColor(Markup.decodeColor(value));
    }
    
    public boolean add(Object o) {
        if (!(o instanceof Element))
            return false;
        cell.addElement((Element)o);
        return true;
    }
    
    public ArrayList getChunks() {
        return chunks;
    }
    
    public boolean process(ElementListener listener) {
        return true;
    }
    
    public int type() {
        return Element.RECTANGLE;
    }
    
    public PdfPCell getCell() {
        return cell;
    }  
    
	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}  
}