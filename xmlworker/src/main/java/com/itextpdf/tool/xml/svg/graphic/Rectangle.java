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
import java.util.Map;


import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.tool.xml.svg.tags.Graphic;


public class Rectangle extends Graphic {
	float x, y, width, height, rx, ry;
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getRx() {
		return rx;
	}

	public float getRy() {
		return ry;
	}

	public Rectangle(float x, float y, float width, float height, float rx, float ry, Map<String, String> css){
		super(css);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rx = rx;
		this.ry = ry;
	}	

	@Override
	public void draw(PdfContentByte cb) {
		//TODO check the line width with this rectangles SVG takes 5 pixel out and 5 pixels in when asking line-width=10
		//TODO check the values for rx and ry what if they get to big
		
		if(rx == 0 || ry == 0){
			cb.rectangle(x, y, width, height);
		}else{ //corners
			/*
			
			if(rx > x / 2){
				rx = x/2;
			}
			if(ry > y / 2){
				ry = y/2;
			}*/			
			
			cb.moveTo(x + rx, y);
			cb.lineTo(x + width - rx, y);
			arc(x + width - 2 * rx, y, x + width, y + 2 * ry, -90, 90, cb);
			cb.lineTo(x + width, y + height - ry);
			arc(x + width, y + height - 2 * ry, x + width - 2 * rx, y + height, 0, 90, cb);
			cb.lineTo(x + rx, y + height);			
			arc(x + 2 * rx, y + height, x, y + height - 2 * ry, 90, 90, cb);
			cb.lineTo(x, y + ry);
			arc(x, y + 2 * ry, x + 2 * rx, y, 180, 90, cb);
			cb.closePath();
		}
		
	}
		
	//copied this because of the moveTo
    public void arc(final float x1, final float y1, final float x2, final float y2, final float startAng, final float extent, PdfContentByte cb) {
        ArrayList<float[]> ar = PdfContentByte.bezierArc(x1, y1, x2, y2, startAng, extent);
        if (ar.isEmpty())
            return;
        float pt[] = ar.get(0);
        //moveTo(pt[0], pt[1]);
        for (int k = 0; k < ar.size(); ++k) {
            pt = ar.get(k);
            cb.curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
        }
    }	
}
