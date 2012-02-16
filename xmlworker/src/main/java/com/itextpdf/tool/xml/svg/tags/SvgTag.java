/*
 * $Id: $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: VVB, Bruno Lowagie, et al.
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
package com.itextpdf.tool.xml.svg.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.html.AbstractTagProcessor;
import com.itextpdf.tool.xml.svg.graphic.Svg;


public class SvgTag extends AbstractTagProcessor{
	@Override
	public List<Element> start(WorkerContext ctx, Tag tag) {
		float height = 0; //TODO check viewbox 
		float width = 0; //TODO check viewbox 
		Rectangle r = null;
		String viewbox = "";
		Map<String, String> attributes = tag.getAttributes();
		if(attributes != null){
			try{				
				height = Float.parseFloat(attributes.get("height"));
			}catch(Exception exp){
				//TODO
			}	
			try{				
				width = Float.parseFloat(attributes.get("width"));
			}catch(Exception exp){
				//TODO
			}		
			try{				
				viewbox = attributes.get("viewBox");
				r = new Rectangle(0, 0);
				StringTokenizer st = new StringTokenizer(viewbox);
				if (st.hasMoreTokens())
					r.setRight(Float.parseFloat(st.nextToken()));
				if (st.hasMoreTokens())
					r.setBottom(- Float.parseFloat(st.nextToken()));
				if (st.hasMoreTokens())
					r.setLeft(r.getRight() + Float.parseFloat(st.nextToken()));
				if (st.hasMoreTokens())
					r.setTop(r.getBottom() + Float.parseFloat(st.nextToken()));
				r.normalize();
			}catch(Exception exp){
				//TODO
			}
		}
		if (r == null) {
			r = new Rectangle(width, height);
		}
		else if (width == 0 && height == 0) {
			width = r.getWidth();
			height = r.getHeight();
		}
		List<Element> elems = new ArrayList<Element>();
		elems.add(new Svg(height, width, r, tag.getCSS()));
		return elems;
	}	
}
