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


import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.tool.xml.svg.PathItem;
import com.itextpdf.tool.xml.svg.tags.Graphic;
import com.itextpdf.tool.xml.svg.utils.EllipseArc;


public class TextPathGroup extends Graphic {
	final List<Element> list;
	final float width, height, x, y;
	Path path;
	final static int fontsize = 40; 
	
	public TextPathGroup(List<Element> list, float x, float y, float width, float height, Map<String, String> css, Path path) {
		super(css);
		this.list = list;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.path = path;
	}
	
	void drawGroup(PdfContentByte cb) throws Exception{
		draw(cb, pathToLineSegment(), list);
	}
	
    public void draw(PdfContentByte cb, List<float[]> lines, List<Element> list) throws Exception{
    	if(lines.size() < 2){
    		return; //Do nothing
    	}  
    	
    	//TODO check all the style elements
    	cb.setLineWidth(3);
    	//first set a letter type
    	BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);    	
    	cb.setFontAndSize(bf, fontsize);

    	float xPrevious = lines.get(0)[0], yPrevious = lines.get(0)[1];  
    	int indexOfTextElement = 0, indexOfCharacter = 0;
    	
       	String currentCharacter = getCharacter(indexOfTextElement, indexOfCharacter);    	
    	while(currentCharacter == null || currentCharacter.length() == 0){
			if(indexOfCharacter >= getLengthOfText(indexOfTextElement) - 1){ //this was the last character of the text element
   				if(indexOfTextElement == list.size() - 1){ //this was the last text element; exit while loop
   					return; //stop
   				}else{ //goto first character of the next element
   					//TODO set the css
   					indexOfTextElement++;
   					indexOfCharacter = 0;
   				}
   			}  
			currentCharacter = getCharacter(indexOfTextElement, indexOfCharacter); 
    	}
    	
    	double halfWidthOfCharacter = cb.getEffectiveStringWidth(currentCharacter, true) / 2.0; 
    	double totalLength = 0;
    	
    	boolean lookForStart = true;
    	
    	for (int j = 1; j < lines.size(); j++) {
			    		
    		float[] point = lines.get(j);
        	
        	double lengthLijnStuk = calculateDistance(xPrevious, yPrevious, point[0], point[1]);
        	//System.out.println(lengthLijnStuk);
        	totalLength = totalLength + lengthLijnStuk;
        	//System.out.println(totalLength);
        	        	
        	while(totalLength >= halfWidthOfCharacter){
       			double tussen = totalLength - halfWidthOfCharacter;
       			double xyAlongThis = lengthLijnStuk - tussen - halfWidthOfCharacter;
       			double xy[] = getPointOnLine(xPrevious, yPrevious, point[0], point[1], xyAlongThis);
       			
       			if(lookForStart){  
	       			showText(cb, point[0], point[1], xPrevious, yPrevious, xy[0], xy[1], currentCharacter);
	       			lookForStart = false;
	       			totalLength = tussen; //distance to the end of the line segment
	       			
       			}else{
       				//look for the end point
	       			lookForStart = true;
	       			totalLength = tussen; //distance to the end of the line segment 
	       			
	       			indexOfCharacter++;
	       			currentCharacter = getCharacter(indexOfTextElement, indexOfCharacter);
	       			
	       	    	while(currentCharacter == null || currentCharacter.length() == 0){
	       				if(indexOfCharacter >= getLengthOfText(indexOfTextElement) - 1){ //this was the last character of the text element
	       	   				if(indexOfTextElement == list.size() - 1){ //this was the last text element; exit while loop
	       	   					return;
	       	   				}else{ //goto first character of the next element
	       	   					indexOfTextElement++;
	       	   					indexOfCharacter = 0;
	       	   				}
	       	   			}  
	       				currentCharacter = getCharacter(indexOfTextElement, indexOfCharacter); 
	       	    	}  
	       	    	halfWidthOfCharacter = cb.getEffectiveStringWidth(currentCharacter, true) / 2.0; 
       			}
        	}
        	
        	xPrevious = point[0];
        	yPrevious = point[1];
		} 
    }	
	
	private String getCharacter(int indexOfTextElement, int indexOfCharacter){
		Text text = (Text)list.get(indexOfTextElement);
		if(text != null && text.getText().length() > indexOfCharacter) {
			return ""+text.getText().charAt(indexOfCharacter);
		}
		return null;
	}
	
	private int getLengthOfText(int indexOfTextElement){
		Text text = (Text)list.get(indexOfTextElement);
		return text.getText().length();
	}	
	
    public void drawLine(PdfContentByte cb, float x1, float y1, float x2, float y2, int step) throws Exception{
    	cb.moveTo(x1, y1);
    	cb.lineTo(x2, y2);
    	cb.stroke();
    	
    	for (int i = 0; i < 10; i++) {
        	double point[] = getPointOnLine(x1, y1, x2, y2, i*step);
        	cb.rectangle((float)point[0], (float)point[1], 2, 2);
        	cb.stroke();
		}
    }
    
    private void showText(PdfContentByte cb, float x, float y, float xPrevious, float yPrevious, double xmidden, double ymidden, String character) throws Exception{
    	double corner = calculateCorner(x, y, xPrevious, yPrevious);
    	cb.saveState();
    	PdfTemplate template2 = cb.createTemplate(1000, 1000);    	
       	
    	template2.beginText();
    	template2.setColorFill(BaseColor.BLACK);
      	BaseFont bf = BaseFont.createFont();
      	template2.setFontAndSize(bf, fontsize);
      	//template2.setTextRise(10);
      	//double halfWidthOfCharacter = cb.getEffectiveStringWidth(character+"", true) / 2.0;  
    	template2.setTextMatrix(0, 0);
    	template2.showText(character+"");
    	template2.endText();
    	
    	cb.concatCTM(AffineTransform.getTranslateInstance(xmidden, ymidden));
    	cb.concatCTM(AffineTransform.getRotateInstance(Math.toRadians(corner)));
    	cb.concatCTM(1, 0, 0, -1, 0, 0);
    	cb.addTemplate(template2, 0, 0);
    	cb.restoreState();  	
    }    
    
	private double calculateCorner(double x1, double y1, double x0, double y0){
		//to center
		double x = x1 - x0;
		double y = y1 - y0;
		if(x >= 0 && y >= 0){
			return(Math.toDegrees(Math.atan(y/x)));
		}
		if(x < 0 && y > 0){
			return(Math.toDegrees(Math.atan(y/x)) + 180);
		}
		if(x < 0 && y < 0){
			return(Math.toDegrees(Math.atan(y/x)) + 180);
		}
		
		if(x >= 0 && y < 0){
			return(Math.toDegrees(Math.atan(y/x)) + 360);
		}	
		return 0;
	}    
    
    private double[] getPointOnLine(float x1, float y1, float x2, float y2, double length){
    	double corner = calculateCorner(x2, y2, x1, y1);
    	//System.out.println(corner);
    	double rad = Math.toRadians(corner);
    	double result[] = new double[2];
    	result[0] = x1 + length * Math.cos(rad);
    	result[1] = y1 + length * Math.sin(rad);
    	return result;
    }
    
    private double calculateDistance(float x1, float y1, float x2, float y2){
    	return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }  
    
    private List<float[]> pathToLineSegment(){
    	return pathToLineSegment(path.getTranslatedPathItems());
    }
    
    private List<float[]> pathToLineSegment(List<PathItem> pathItems){
    	List<float[]> coordinates = new ArrayList<float[]>();
    	
		for (PathItem item : pathItems) {
			List<Float> numbers = item.getCoordinates();

			if(item.isMoveTo() || item.isLineTo()){
				float[] point = new float[2];
				point[0] = numbers.get(0);
				point[1] = numbers.get(1);
				coordinates.add(point);
			}else if(item.isCubicBezier() || item.isQuadraticBezier()){
				coordinates.addAll(bezierCurveToLines(coordinates, item));
			}else if(item.isArcTo()){
				coordinates.addAll(arcToLines(item));		
			}
		}
		
    	return coordinates;
    }
    
    private List<float[]> arcToLines(final PathItem item){
    	List<Float> numbers = item.getCoordinates();
		EllipseArc ellipse = EllipseArc.createEllipseArc(numbers.get(7), numbers.get(8), numbers.get(5), numbers.get(6), numbers.get(0), numbers.get(1), numbers.get(4), numbers.get(3));

		List<float[]> newCoordinates = PdfContentByte.bezierArc(ellipse.getCx() - numbers.get(0), ellipse.getCy() - numbers.get(1), ellipse.getCx() + numbers.get(0), ellipse.getCy() + numbers.get(1),
				ellipse.getStartAng(), ellipse.getExtend());
		
		List<float[]> result = new ArrayList<float[]>();
		
        if (newCoordinates.isEmpty()) return result;
           
        float pt[] = newCoordinates.get(0);
        float x0 = pt[0];
        float y0 = pt[1];
        
        for (int k = 0; k < newCoordinates.size(); ++k) {
            pt = newCoordinates.get(k);
            result.addAll(bezierCurveToLines(x0, y0, pt[2], pt[3], pt[4], pt[5], pt[6], pt[7], true));
            x0 = pt[6];
            y0 = pt[7];
        }
        return result;
    }
    
    private List<float[]> bezierCurveToLines(final List<float[]> coordinates, final PathItem item){
    	List<Float> numbers = item.getCoordinates();
    	
    	float start[] = coordinates.get(coordinates.size() - 1);
    	float x0 = start[0];
    	float y0 = start[1];
    	float x1 = numbers.get(0);
    	float y1 = numbers.get(1);
    	float x2 = numbers.get(2);
    	float y2 = numbers.get(3);
    	float x3 = 0;
    	float y3 = 0;
    	
    	if(item.isCubicBezier()){
        	x3 = numbers.get(4);
        	y3 = numbers.get(5);    		
    	}
    	
    	return bezierCurveToLines(x0, y0, x1, y1, x2, y2, x3, y3, item.isCubicBezier());
    }
    
    private List<float[]> bezierCurveToLines(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean cubic){
    	float A, B, C, D, E, F, G, H;
    	
    	if(cubic){
           	A = x3 - 3 * x2 + 3 * x1 - x0;
        	B = 3 * x2 - 6 * x1 + 3 * x0;
        	C = 3 * x1 - 3 * x0;
        	D = x0;

        	E = y3 - 3 * y2 + 3 * y1 - y0;
        	F = 3 * y2 - 6 * y1 + 3 * y0;
        	G = 3 * y1 - 3 * y0;
        	H = y0;    		
    	}else{        	
        	 A = 0;
        	 B = x2 - 2 * x1 + x0;
        	 C = 2 * x1 - 2 * x0;
        	 D = x0;
        	 E = 0;
        	 F = y2 - 2 * y1 + y0;
        	 G = 2 * y1 - 2 * y0;
        	 H = y0;        	 
    	}
    	
    	List<float[]> result = new ArrayList<float[]>();
    	float step = 0.005f;
    	for (float t = step; t <= 1; t += step) {
    		float[] point = new float[2];
    		point[0] = A * t * t * t + B * t * t + C * t + D;
    		point[1] = E * t * t * t + F * t * t + G * t + H;
    		result.add(point);
    	}
    	return result;
    }
	
	@Override
	public void draw(PdfContentByte cb) {
		try{
			drawGroup(cb);
		}catch(Exception exp){
			//System.out.println(exp.getMessage());
		}	
	}
}
