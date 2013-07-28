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
package com.itextpdf.tool.xml.svg.graphic;

import java.util.List;
import java.util.Map;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.tool.xml.svg.tags.TagUtils;


public class CssSvgAppliers {
	private static CssSvgAppliers myself = new CssSvgAppliers();
	
	/**
	 * @return singleton instance
	 */
	public static CssSvgAppliers getInstance() {
		return myself;
	}	
	
	public void applyForText(PdfContentByte cb, final Map<String, String> css, Chunk chunk) {
		setStrokeAndFillColor(cb, css);
		setStrokeAndFill(cb, css);	
		
		try{
			Font font = new Font(FontFamily.COURIER, 6, Font.NORMAL, BaseColor.BLACK);
			
			Font font2 = chunk.getFont();
			
			BaseFont bf2 = font2.getBaseFont();
			//BaseFont bf = ;
			
			if(bf2 == null){
				cb.setFontAndSize(font.getCalculatedBaseFont(false), font2.getSize());
			}else{
				cb.setFontAndSize(bf2, font2.getSize());
			}
		}catch(Exception exp){}	
		
	}	
	
	
	void setStrokeAndFill(PdfContentByte cb, Map<String, String> css){
		
		//default is true for both
		String fillValue = css.get("fill");
		String strokeValue = css.get("stroke");
			
		boolean fill = (fillValue == null || !fillValue.equals("none")); 
		boolean stroke = (strokeValue != null && !strokeValue.equals("none"));
			
		if(fill && stroke){
			cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
		}else if (fill){
			cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
		}else if (stroke){
			cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE);
		}else{
			cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
		}
	}	
	
	public void apply(PdfContentByte cb, final Map<String, String> css) {
		setStrokeAndFillColor(cb, css);
	
		//line width
		// what is the value in svg, in pdf it is units
		setLineWidth(cb, css);
		setLineCap(cb, css);
		setLineJoin(cb, css);
		setLineDash(cb, css);
	}
	
	void setStrokeAndFillColor(PdfContentByte cb, Map<String, String> css){
		BaseColor fillColor = getColor(css.get(SVGAttributes.FILL));
		if(fillColor == null){
			//first check the attributes
			fillColor = BaseColor.BLACK;
		}
		cb.setColorFill(fillColor);

		BaseColor strokeColor = getColor(css.get(SVGAttributes.STROKE));
		if(strokeColor == null){
			strokeColor = fillColor;
		}
		cb.setColorStroke(strokeColor);
	}
	
	
	private BaseColor getColor(String name){
		if(name == null || name.equals("none")) return null;
		BaseColor color = HtmlUtilities.decodeColor(name);
		if(color == null){
			color = SVGAttributes.cleanColorString(name);			
		}
		return color;
	}
	
	void setLineWidth(PdfContentByte cb, Map<String, String> css){
		String width = css.get(SVGAttributes.STROKE_WIDTH);
		if(width != null){
			try{
				cb.setLineWidth(Float.parseFloat(width));				
			}catch(Exception exp){
				//DO nothing
			}
		}
	}	
	
	void setLineDash(PdfContentByte cb, Map<String, String> css){
		String lineDash = css.get(SVGAttributes.STROKE_DASHARRAY);
		if(lineDash == null || lineDash.equals("none")){
			return;
		}
		
		List<String> list = TagUtils.splitValueList(lineDash);
		if(list == null) return;
		float phase[] = new float[list.size()*2];
		//the lineDash was validated before adding to CSS, so this is a valid pattern
		
		boolean allZero = true;
		int i = 0;
		for (String str : list) {
			try{
				phase[i] = Integer.parseInt(str);
				phase[i+list.size()] = phase[i];
				if(phase[i] != 0){
					allZero = false;
				}
				i++;
			}catch(Exception exp){
			}
		}
		if(!allZero){
			cb.setLineDash(phase, 0);
		}
	}	
	
	void setLineCap(PdfContentByte cb, Map<String, String> css){
		String lineCap = css.get(SVGAttributes.LINE_CAP);
		if(lineCap != null){
			if(lineCap.equals("round")) {
				cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
			}else if(lineCap.equals("square")) {
				cb.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
			}else {
				cb.setLineCap(PdfContentByte.LINE_CAP_BUTT);
			}
		}else {
			cb.setLineCap(PdfContentByte.LINE_CAP_BUTT);
		}
	}
	
	void setLineJoin(PdfContentByte cb, Map<String, String> css){
		String lineJoin = css.get(SVGAttributes.LINE_JOIN);
		if(lineJoin != null){
			if(lineJoin.equals("round")) {
				cb.setLineJoin(PdfContentByte.LINE_JOIN_ROUND);
			}else if(lineJoin.equals("bevel")) {
				cb.setLineJoin(PdfContentByte.LINE_JOIN_BEVEL);
			}else {
				cb.setLineJoin(PdfContentByte.LINE_JOIN_MITER);
			}
		}else{
			cb.setLineJoin(PdfContentByte.LINE_JOIN_MITER);
		}
	}	
	
	public void close(PdfContentByte cb, Map<String, String> css) {
		//default is true for both
		String fillValue = css.get("fill");
		String strokeValue = css.get("stroke");
		
		boolean fill = (fillValue == null || !fillValue.equals("none")); 
		boolean stroke = (strokeValue == null || !strokeValue.equals("none"));
		
		if(fill && stroke){
			cb.fillStroke();
		}else if (fill){
			cb.fill();
		}else if (stroke){
			cb.stroke();
		}
	}	
}
