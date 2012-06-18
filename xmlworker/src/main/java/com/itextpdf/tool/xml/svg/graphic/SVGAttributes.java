/*
 * $Id$
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
package com.itextpdf.tool.xml.svg.graphic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.tool.xml.svg.tags.TagUtils;


/*fill-opacity 1
fill-rule nonzero
fill-opacity 1
stroke-opacity 1
stroke-miterlimit 4
stroke-dasharray 0,11,8,1
stroke-opacity 1
stroke-miterlimit 10*/

public class SVGAttributes {
	public final static int TYPE_OTHER = 0;
	public final static int TYPE_COLOR = 1;
	public final static int TYPE_LINE = 2;	
	public final static int TYPE_LINE_CAP = 3;	
	public final static int TYPE_LINE_JOIN = 4;
	public final static int TYPE_LINE_DASH = 5; //a list of integer values
	
	final static String STROKE = "stroke";
	final static String STROKE_WIDTH = "stroke-width";
	final static String FILL = "fill";
	final static String LINE_CAP = "stroke-linecap";
	final static String LINE_JOIN = "stroke-linejoin";
	final static String FILL_OPACITY = "fill-opacity";
	final static String STROKE_OPACITY = "stroke-opacity";
	final static String FILL_RULE = "fill-rule";
	final static String STROKE_MASTERLIMIT = "stroke-miterlimit";
	final static String STROKE_DASHARRAY = "stroke-dasharray";
	final static String FONT_SIZE = "font-size";
	final static String FONT_FAMILY = "font-family";
	
	
	final static String[] LINE_CAP_VALUES = {"butt", "round", "square"};
	final static String[] LINE_JOIN_VALUES = {"miter", "round", "bevel"};
	//TODo make this an instance
	
	public static Map<String, Integer> getSVGAttributesList(){
		Map<String, Integer> attributes = new HashMap<String, Integer>();
		attributes.put(STROKE, TYPE_COLOR);
		attributes.put(STROKE_WIDTH, TYPE_LINE);
		attributes.put(FILL, TYPE_COLOR);
		attributes.put(LINE_CAP, TYPE_LINE_CAP);
		attributes.put(LINE_JOIN, TYPE_LINE_JOIN);
		attributes.put(STROKE_DASHARRAY, TYPE_LINE_DASH);
		attributes.put(FONT_SIZE, TYPE_LINE);
		attributes.put(FONT_FAMILY, TYPE_OTHER);
		return attributes;
	}
	
	public static boolean isValidAttribute(String key, String value, Map<String, Integer> attributes){
		if( attributes.get(key) != null){
			switch ((int) attributes.get(key)) {
			case SVGAttributes.TYPE_LINE:
				return isValidValueForLine(value);
			case SVGAttributes.TYPE_COLOR:
				return isValidColor(value);	
			case SVGAttributes.TYPE_LINE_CAP:
				return isValidString(value, LINE_CAP_VALUES);
			case SVGAttributes.TYPE_LINE_JOIN:
				return isValidString(value, LINE_JOIN_VALUES);
			case SVGAttributes.TYPE_LINE_DASH:
				return isValidDashArray(value);	
			case SVGAttributes.TYPE_OTHER:
				return true;
			default:
				//System.out.println(key);
				break;
			}
		}
		return false;
	}
	
	public static boolean isValidDashArray(String value){
		//comma or space separated list of integers or NONE?
		if(value.equalsIgnoreCase("none")) return true;
		
		List<String> list = TagUtils.splitValueList(value);
		for (String str : list) {
			try{
				int result = Integer.parseInt(str);
				if(result < 0){
					return false;
				}
			}catch(Exception exp){
				return false;
			}			
		}
		
		return true;
	}
	
	public static boolean isValidString(String value, String[] possibleValues){
		if(value == null) return false;		
		
		for (int i = 0; i < possibleValues.length; i++) {
			if(possibleValues[i].equals(value)){
				return true;
			}
		}
		return false;
	}
		
	public static boolean isValidColor(String value){
		if(value == null) return false;
		if(value.equals("none")){
			return true;
		}
		if(HtmlUtilities.decodeColor(value) != null){
			return true;
		}
		if(cleanColorString(value) != null){
			return true;
		}		
		return false;
	}
	
	//TODO: dit is echt zwaar foefelen
	//gecopieerd van WebColors and aangepast omdat in de SVG van openstreetmap, kleuren staan met decimale waarden ipv integers
	public static BaseColor cleanColorString(String name){
		int[] c = { 0, 0, 0, 255 };
		name = name.toLowerCase();
		if (name.startsWith("rgb(")) {
            StringTokenizer tok = new StringTokenizer(name, "rgb(), \t\r\n\f");
            for (int k = 0; k < 3; ++k) {
                String v = tok.nextToken();
                if (v.endsWith("%"))
                    c[k] = (int)Double.parseDouble(v.substring(0, v.length() - 1)) * 255 / 100;
                else
                    c[k] = (int)Double.parseDouble(v);
                if (c[k] < 0)
                    c[k] = 0;
                else if (c[k] > 255)
                    c[k] = 255;
            }
            return new BaseColor(c[0], c[1], c[2], c[3]);
        }
		return null; 

	}
	
	public static boolean isValidValueForLine(String value){
		if(value == null) return false;
		try{
			double result = Double.parseDouble(value);
			if(result >= 0){
				return true;
			}
		}catch(Exception exp){
			return false;
		}
		return false;
	}	
}
