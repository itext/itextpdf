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
package com.itextpdf.tool.xml.svg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.tool.xml.svg.exceptions.SvgParseException;



public class TransformationMatrix {
	
	public static AffineTransform getTransformationMatrix(String transform){
		//matrix, translate, scale, rotate, skewX, skewY
		AffineTransform matrix = null;
		
		List<String> listWithTransformations = splitString(transform);
		for (String str : listWithTransformations) {
			AffineTransform newMatrix = strToMatrix(str);
			if(newMatrix != null){
				if(matrix == null){
					matrix = newMatrix;
				}else{
					matrix.concatenate(newMatrix);	
				}
			}
		}
		return matrix;
	}
	
	static List<String> splitString(String transform){
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(transform,")", false);
		while (tokenizer.hasMoreTokens()){
			list.add(tokenizer.nextToken().trim()+")");
		}
		return list;
	}
	
	static AffineTransform strToMatrix(String str){
		try{
			
			//matrix, translate, scale, rotate, skewX, skewY
			if(str.startsWith("matrix")){
				return createWithMatrix(getValuesFromStr(str));
			}else if(str.startsWith("translate")){
				return createForTranslate(getValuesFromStr(str));
			}else if(str.startsWith("scale")){
				return createForScale(getValuesFromStr(str));
			}else if(str.startsWith("rotate")){
				return createForRotate(getValuesFromStr(str));
			}else if(str.startsWith("skewX")){
				return createForSkewX(getValuesFromStr(str));
			}else if(str.startsWith("skewY")){
				return createForSkewY(getValuesFromStr(str)); 
			}else{
				//no transformation at all
				return null;
			}
		}catch(SvgParseException exp){
			//when a mistake happens, do nothing
			return null;
		}	
	}
	
	//if only one coordinate the second one is zero
	static AffineTransform createForSkewY(List<Float> values) throws SvgParseException{
		if(values.size() != 1){
			throw new SvgParseException("Could not parse the transform");
		}
		return new AffineTransform(1, Math.tan(Math.toRadians(values.get(0))), 0, 1, 0, 0);
	}		
	
	//if only one coordinate the second one is zero
	static AffineTransform createForSkewX(List<Float> values) throws SvgParseException{
		if(values.size() != 1){
			throw new SvgParseException("Could not parse the transform");
		}
		return new AffineTransform(1, 0, Math.tan(Math.toRadians(values.get(0))), 1, 0, 0);
	}	
	
	//if only one coordinate the second one is zero
	static AffineTransform createForRotate(List<Float> values) throws SvgParseException{
		if(values.size() != 1){
			throw new SvgParseException("Could not parse the transform");
		}
		return AffineTransform.getRotateInstance(Math.toRadians(values.get(0)));
	}	
	
	//if only one coordinate the second one is zero
	static AffineTransform createForScale(List<Float> values) throws SvgParseException{
		if(values.size() == 1){
			return AffineTransform.getScaleInstance(values.get(0), values.get(0));
		}		
		if(values.size() != 2){
			throw new SvgParseException("Could not parse the transform");
		}
		return AffineTransform.getScaleInstance(values.get(0), values.get(1));
	}		
	
	//if only one coordinate the second one is zero
	static AffineTransform createForTranslate(List<Float> values) throws SvgParseException{
		if(values.size() == 1){
			return AffineTransform.getTranslateInstance(values.get(0), 0);
		}		
		if(values.size() != 2){
			throw new SvgParseException("Could not parse the transform");
		}
		return AffineTransform.getTranslateInstance(values.get(0), values.get(1));
	}	
	
	static AffineTransform createWithMatrix(List<Float> values) throws SvgParseException{
		if(values.size() != 6){
			throw new SvgParseException("Could not parse the transform");
		}
		return new AffineTransform(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5));
	}
	
	static List<Float> getValuesFromStr(String str) throws SvgParseException{
		try{
			String numbers = str.substring(str.indexOf('(')+1, str.indexOf(')'));
			List<Float> result = new ArrayList<Float>();
			StringTokenizer tokenizer = new StringTokenizer(numbers, ",");
			while (tokenizer.hasMoreTokens()){
				result.add(Float.parseFloat(tokenizer.nextToken()));
			}
			return result;
		}catch(Exception exp){
			throw new SvgParseException("Could not parse the transform");
		}	
	}
	
}
