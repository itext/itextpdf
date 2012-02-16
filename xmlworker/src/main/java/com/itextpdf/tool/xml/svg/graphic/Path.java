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
package com.itextpdf.tool.xml.svg.graphic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.tool.xml.svg.PathBean;
import com.itextpdf.tool.xml.svg.PathItem;
import com.itextpdf.tool.xml.svg.tags.Graphic;
import com.itextpdf.tool.xml.svg.utils.EllipseArc;


public class Path extends Graphic {
	PathBean path;
	
	public Path(PathBean path, Map<String, String> css) {
		super(css);
		this.path = path;
	}
	
	
	
	@Override
	public void draw(PdfContentByte cb) {
		drawElement(cb);
	}
		
	//just a method that cleans up the path before drawing it
	List<PathItem> translate(List<PathItem> items){
		List<PathItem> result = new ArrayList<PathItem>();
		try{
			for (PathItem item : items) {
				if(item.isMoveTo() || item.isLineTo()){
					translateForMoveAndLineTo(item, result);
				}
				if(item.isHorizontalLineTo() || item.isVerticalLineTo()){
					translateForHorizontalOrVerticalLine(item, result);
				}
				
				if(item.isCubicBezier() || item.isQuadraticBezier() || item.isCubicBezierShorthand() || item.isQuadraticBezierShorthand()){
					translateCurves(item, result);
				}
				
				if(item.isArcTo()){
					translateArc(item, result);
				}
				
				if(item.isClosePath()){
					result.add(item);
				}
			}
		}catch(Exception exp){
			//when an exception occurs in one of the translate methods, stop looking at the rest of the path and return the list
		}
		return result;
	}
	
	
	private float[] getCurrentPoint(List<PathItem> result){
		float[] currentPoint = new float[2];
		if(result != null && result.size() != 0){
			List<Float> coordinates = result.get(result.size() - 1).getCoordinates();
			currentPoint[0] = coordinates.get(coordinates.size() - 2);
			currentPoint[1] = coordinates.get(coordinates.size() - 1);
		}
		return currentPoint;
	}	
	
	private void translateArc(PathItem item, List<PathItem> result) throws Exception{
		//add the current point at the end
		
		int size = 7;
		//needs groups of 7 coordinates
		List<Float> numbers = item.getCoordinates();
		for (int i = 0; i < (numbers.size() / size); i++) {
			//first add the current coordinates, copy all and if relative, change the last two coordinates
			List<Float> coordinates = new ArrayList<Float>();
			float[] currentPoint = getCurrentPoint(result);
			
			for (int j = 0; j < size; j++) { 
				if(j == size-2 && item.isRelative()){ //x
					coordinates.add(currentPoint[0] + (numbers.get((i*size)+j)));
				}else if(j == size-1 && item.isRelative()){ //y
					coordinates.add(currentPoint[1] + (numbers.get((i*size)+j)));
				}else{
					coordinates.add(numbers.get((i*size)+j));
				}	
			}
			//this is a bit strange but easiest way to transfer the coordinates
			coordinates.add(currentPoint[0]);
			coordinates.add(currentPoint[1]);
			result.add(new PathItem(coordinates, PathItem.ARC));
		}
		if(numbers.size() % size != 0){
			throw new Exception("Something wrong with the number of coordinates in the path");
		}		
	}
		
	private void translateCurves(PathItem item, List<PathItem> result) throws Exception{
		int size;
		if(item.isCubicBezier()){
			size = 6;
		}else if (item.isCubicBezierShorthand() || item.isQuadraticBezier()){
			size = 4;
		}else{
			size = 2;
		}
		
		List<Float> numbers = item.getCoordinates();
		
		for (int i = 0; i < (numbers.size() / size); i++) {
			List<Float> coordinates = new ArrayList<Float>();
		//shorthand notations - get the coordinates of the first control point
			if(item.isCubicBezierShorthand() || item.isQuadraticBezierShorthand()){
				//add the last control point of the previous pathItem
				if(result != null && result.size() != 0){
					PathItem previous = result.get(result.size() - 1);
					if((previous.isCubicBezier() && item.isCubicBezierShorthand()) 
							|| (previous.isQuadraticBezier() && item.isQuadraticBezierShorthand())){
						List<Float> previousCoordinates = result.get(result.size() - 1).getCoordinates();
						
						float xPreviousControlPoint = previousCoordinates.get(previousCoordinates.size() - 4);
						float yPreviousControlPoint = previousCoordinates.get(previousCoordinates.size() - 3);
						 
						//reflection if this point to the currentPoint
						float current[] = getCurrentPoint(result);
						coordinates.add(2*current[0] - xPreviousControlPoint);
						coordinates.add(2*current[1] - yPreviousControlPoint);
					}	
				}
				if (coordinates.size() == 0){
					//add the currentPoint
					float current[] = getCurrentPoint(result);
					coordinates.add(current[0]);
					coordinates.add(current[1]);						
				}				
			}
				
			for (int j = 0; j < size; j++) { //copy the rest of the coordinates
				if(item.isRelative()){
					float[] currentPoint = getCurrentPoint(result);
					if(j % 2 == 0){
						coordinates.add(currentPoint[0] + (numbers.get((i*size)+j)));
					}else{
						coordinates.add(currentPoint[1] + (numbers.get((i*size)+j)));
					}
				}else {
					coordinates.add(numbers.get((i*size)+j));
				}				
			}
			if(item.isCubicBezier() || item.isCubicBezierShorthand()){
				result.add(new PathItem(coordinates, PathItem.CUBIC_BEZIER));
			}else{
				result.add(new PathItem(coordinates, PathItem.QUADRATIC_BEZIER));
			}			
		}
		if(numbers.size() % size != 0){
			throw new Exception("Something wrong with the number of coordinates in the path");
		}
	}
	
	private void translateForHorizontalOrVerticalLine(PathItem item, List<PathItem> result){
		//TODO check if this is correct
		List<Float> numbers = item.getCoordinates();
		if(numbers.size() == 0){
			numbers.add(0f);
		}
		float[] currentPoint = getCurrentPoint(result);

		if(!item.isRelative()){ //take the last command
			List<Float> coordinates = new ArrayList<Float>();
			if(item.isHorizontalLineTo()){
				coordinates.add(numbers.get(numbers.size() - 1));
				coordinates.add(currentPoint[1]);
			}else{
				coordinates.add(currentPoint[0]);
				coordinates.add(numbers.get(numbers.size() - 1));
			}
			
			result.add(new PathItem(coordinates, PathItem.LINE));
		}else{
			float coordinate = 0f;
			for (int i = 0; i < numbers.size(); i++) {
				coordinate = coordinate + numbers.get(i);				
			}
			List<Float> coordinates = new ArrayList<Float>();
			if(item.isHorizontalLineTo()){
				coordinates.add(coordinate + currentPoint[0]);
				coordinates.add(currentPoint[1]);
			}else{
				coordinates.add(currentPoint[0]);
				coordinates.add(coordinate + currentPoint[1]);
			}
			result.add(new PathItem(coordinates, PathItem.LINE));
		}
	}		
	
	private void translateForMoveAndLineTo(PathItem item, List<PathItem> result){
		List<Float> numbers = item.getCoordinates();
		if(numbers.size() % 2 == 1){
			numbers.add(0f);
		}
		
		//for each pair
		for (int i = 0; i < (numbers.size() / 2); i++) {
			float x = numbers.get(i*2);			
			float y = numbers.get((i*2)+1);
			
			if(item.isRelative()){
				float[] currentPoint = getCurrentPoint(result);
				x = x + currentPoint[0];
				y = y + currentPoint[1];
			}
			
			List<Float> coordinates = new ArrayList<Float>();
			coordinates.add(x);
			coordinates.add(y);
			
			if(item.isMoveTo() && i == 0){
				result.add(new PathItem(coordinates, PathItem.MOVE));
			}else{
				result.add(new PathItem(coordinates, PathItem.LINE));				
			}			
		}
	}
	
	private void drawArc(PdfContentByte cb, List<Float> numbers){
		EllipseArc ellipse = EllipseArc.createEllipseArc(numbers.get(7), numbers.get(8), numbers.get(5), numbers.get(6), numbers.get(0), numbers.get(1), numbers.get(4), numbers.get(3));
		
		cb.setColorFill(BaseColor.ORANGE);
		cb.rectangle(numbers.get(7), numbers.get(8), 2, 2); //p1
		cb.fill();
		cb.setColorFill(BaseColor.GREEN);
		cb.rectangle(numbers.get(5), numbers.get(6), 2, 2); //p2
		cb.fill();
		
		cb.arc(ellipse.getCx() - numbers.get(0), ellipse.getCy() - numbers.get(1), ellipse.getCx() + numbers.get(0), ellipse.getCy() + numbers.get(1),
				ellipse.getStartAng(), ellipse.getExtend());
	}
	
	public List<PathItem> getTranslatedPathItems(){
		return translate(path.getPathItems());
	}
			
	void drawElement(PdfContentByte cb){
		try{
			List<PathItem> translatedItems = translate(path.getPathItems());
			
			//loop over the items in the path
			for (PathItem item : translatedItems) {
				List<Float> numbers = item.getCoordinates();
				
				if(item.isMoveTo()){
					cb.moveTo(numbers.get(0), numbers.get(1));
				}else if(item.isLineTo()){
					cb.lineTo(numbers.get(0), numbers.get(1));
				}else if(item.isCubicBezier()){
					cb.curveTo(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3), numbers.get(4), numbers.get(5));
				}else if(item.isQuadraticBezier()){
					cb.curveTo(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3));
				}else if(item.isArcTo()){
					drawArc(cb, numbers);
				}else if(item.isClosePath()){
					cb.closePath();
				}else{
					//System.out.println(item);
				}
			}
		}catch(Exception exp){
			//TODO
		}
	}
}
