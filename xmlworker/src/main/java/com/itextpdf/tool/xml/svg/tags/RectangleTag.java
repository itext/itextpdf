/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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


import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.svg.AbstractGraphicProcessor;
import com.itextpdf.tool.xml.svg.graphic.Rectangle;


public class RectangleTag extends AbstractGraphicProcessor {
//<rect x="60" y="60" width="" height="" rx="5" cy="5"/>

	@Override
	public List<Element> end(WorkerContext ctx, Tag tag,
				List<Element> currentContent) {
		
		Map<String, String> attributes = tag.getAttributes();
		if(attributes != null){
			
			//width and height wrong or not existing -> draw nothing
			float width, height;
			try{
				width = Integer.parseInt(attributes.get("width"));
			}catch (Exception e) {
				return new ArrayList<Element>(0);
			}
			try{
				height = Integer.parseInt(attributes.get("height"));
			}catch (Exception e) {
				return new ArrayList<Element>(0);
			}
			
			if(width <= 0 || height <= 0){
				return new ArrayList<Element>(0);
			}
			
			//x,y missing or wrong -> just zero, negative value is acceptable			
			float x = 0,  y = 0, rx = 0, ry = 0;
			try{
				x = Integer.parseInt(attributes.get("x"));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			try{
				y = Integer.parseInt(attributes.get("y"));
			}catch (Exception e) {
				// TODO: handle exception
			}

			//if one of these is wrong or missing, take the other ones value
			//if the value is specified but less or equal than 0 -> no rounding
			try{				
				rx = Integer.parseInt(attributes.get("rx"));				
			}catch (Exception e) {
				rx = -1;
			}
			
			try{
				ry = Integer.parseInt(attributes.get("ry"));
			}catch (Exception e) {
				ry = -1;
			}
			
			//one out of the two is zero (but was specified)
			if((attributes.get("rx") != null && rx == 0) || (attributes.get("ry") != null && ry == 0)){
				rx = 0;
				ry = 0;
			}else{ //copy the wrong values
				if(rx <= 0){
					rx = ry;
				}
				if(ry <= 0){
					ry = rx;
				}
				
				if(rx <= 0 && ry <= 0){
					rx = 0;
					ry = 0;
				}
			}
			
	    	List<Element> l = new ArrayList<Element>(1);
	    	
	    	l.add(new Rectangle(x, y, width, height, rx, ry, tag.getCSS()));
	    	return l;
		}else{
			return new ArrayList<Element>(0);
		}        
	}
	
	@Override
	public boolean isElementWithId() {
		return true;
	}	
}
