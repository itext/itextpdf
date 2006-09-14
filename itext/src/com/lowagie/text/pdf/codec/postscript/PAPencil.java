/*
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * This class is based on a sample class by SUN.
 * The original copyright notice is as follows:
 * 
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 * 
 * The license agreement can be found at this URL:
 * http://java.sun.com/products/java-media/2D/samples/samples-license.html
 * See also the file sun.txt in directory com.lowagie.text.pdf
 */

package com.lowagie.text.pdf.codec.postscript;

import java.util.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;


public class PAPencil {

    static protected class State implements Cloneable {
        public Stroke stroke;
        public Paint paint;
        public AffineTransform at;
        public Shape clipShape;
        public Font font;
        public Composite composite;
        public GeneralPath path;

        public State(){
            this(null);
        }

        public State(Graphics2D g){
            if(g == null){
                this.stroke = new BasicStroke();
                this.paint = Color.black;
                this.at = new AffineTransform();
                this.font = new Font("SansSerif", Font.PLAIN, 12);
                this.composite = AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1.0f);
                this.clipShape = null;
            } else {
                this.recordState(g);
            }
            this.path = new GeneralPath();
        }

        public void recordState(Graphics2D g){
            this.stroke = g.getStroke();
            this.paint = g.getPaint();
            this.at = g.getTransform();
            this.font = g.getFont();
            this.composite = g.getComposite();
            this.clipShape = g.getClip();
        }

        public void stampState(Graphics2D g, Dimension size){
            g.setTransform(new AffineTransform());
            g.setClip(new Rectangle(0, 0, size.width, size.height));
            g.setStroke(this.stroke);
            g.setPaint(this.paint);
            g.setTransform(this.at);
            g.setFont(this.font);
            g.setComposite(this.composite);
            if(this.clipShape != null){
                g.clip(this.clipShape);
            }
        }

        public Object clone(){
            try {
                State n = (State)super.clone();

                n.at = (AffineTransform) this.at.clone();
                n.path = new GeneralPath();
                n.path.append(this.path, false);
                return n;
            } catch(CloneNotSupportedException e){
                throw new InternalError();
            }
        }

    }

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    /**
     * The canvas size.
     */
    protected Dimension size;

    /**
     * The current graphics state.
     */
    protected State state;

    /**
     * The stack of graphic states.
     */
    protected Stack gStack;

    /**
     * The font hashtable with postscript names as keys
     */
    protected HashMap fonts;

    /**
     * The current graphics device
     */
    public Graphics2D graphics;

    //
    // Constructors
    //

    public PAPencil(Component component){
	this.graphics = (Graphics2D) component.getGraphics();
	this.size = component.getSize();
	this.initgraphics();
    }

    public PAPencil(Graphics graphics, Dimension size){
	this.graphics = (Graphics2D) graphics;
	this.size = size;
	this.initgraphics();
    }

    //
    // Graphics state management
    //

    public void gsave(){
        this.state.recordState(this.graphics);
	State next = (State) this.state.clone();

	this.gStack.push(this.state);
	this.state = next;
    }

    public void grestore(){
       	if(this.gStack.empty()){
	    this.initgraphics();
	} else {
	    this.state = (State) this.gStack.pop();
            this.state.stampState(this.graphics, this.size);
	}
    }

    public void grestoreall(){
	this.initgraphics();
    }

    public void initgraphics(){
      AffineTransform at = new AffineTransform();
      // turn anti-aliasing and high-quality rendering on
      this.graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
      this.graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

      // initialize to a postscript coordinate system
      at.translate(0, this.size.getHeight());
      at.scale(1, -1);
      this.graphics.setTransform(at);
      //        this.graphics.translate(0, this.size.getHeight());
      //        this.graphics.scale(1, -1);

      // state, stack and page
      this.state = new State(this.graphics);
      this.gStack = new Stack();
      this.erasepage();
    }

    //
    // Path definition
    //

    public void newpath(){
	this.state.path.reset();
    }

    public void moveto(double x, double y){
	this.state.path.moveTo((float) x, (float) y);
    }

    public void moveto(Point2D p) {
	this.moveto(p.getX(), p.getY());
    }

    public void rmoveto(double dx, double dy) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
	this.state.path.moveTo((float) (currentPoint.getX() + dx) , (float) (currentPoint.getY() + dy));
    }

    public void lineto(double x, double y) throws PainterException {
	Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
	this.state.path.lineTo((float) x, (float) y);
    }

    public void lineto(Point2D p) throws PainterException {
	this.lineto(p.getX(), p.getY());
    }

    public void rlineto(double dx, double dy) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
        this.state.path.lineTo((float) (currentPoint.getX() + dx) , (float) (currentPoint.getY() + dy));
    }

    /**
     *
     * @param cx double Centerpoint x
     * @param cy double Centerpoint y
     * @param r double  Radius r
     * @param ang1 double first angle
     * @param ang2 double second angle
     */
    public void arc(double cx, double cy, double r, double ang1, double ang2){
        Arc2D.Float arc = new Arc2D.Float((float) ( cx - r ), (float) ( cy
                - r ), (float) r * 2f, (float) r * 2f,- (float) ang1,- (float) ( ang2 -
                ang1 ), Arc2D.OPEN );
        Point2D currentPoint = this.state.path.getCurrentPoint();
        if(currentPoint == null){
            this.state.path.append(arc, false);
        } else {
            this.state.path.append(arc, true);
        }
    }
    
    public void arcn(double cx, double cy, double r, double ang1, double ang2) {
        Arc2D.Float arc = new Arc2D.Float((float) ( cx - r ), (float)
        ( cy - r ), (float) r * 2f, (float) r * 2f,- (float) ang1, -(float) (
                ang2 - ang1 ), Arc2D.OPEN );
        Point2D currentPoint = this.state.path.getCurrentPoint();
        if(currentPoint == null){
            this.state.path.append(arc, false);
        } else {
            this.state.path.append(arc, true);
        }
    }

    public void curveto(double x1, double y1, double x2, double y2,
		         double x3, double y3) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
        this.state.path.curveTo((float) x1, (float) y1, (float) x2, (float) y2,
                                  (float) x3, (float) y3);
    }

    public void rcurveto(double dx1, double dy1, double dx2, double dy2,
			  double dx3, double dy3) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
	double x0 = currentPoint.getX();
	double y0 = currentPoint.getY();
	this.curveto(x0 + dx1, y0 + dy1, x0 + dx2, y0 + dy2, x0 + dx3,y0 + dy3);
    }

    public void closepath(){
        this.state.path.closePath();
    }

    // PENDING(uweh): just a placeholder for now
    public void clippath(){
        this.rectpath(0.0d, 0.0d, size.width, size.height);
    }
    public void clip(){
       PdfGraphics2D pdfg2d = (PdfGraphics2D) this.graphics;
       pdfg2d.clip(this.state.path);
        this.newpath();
//      Area currentclip=new Area(this.state.clipShape);
//      Area addclip=new Area(this.state.path.createTransformedShape(AffineTransform.getTranslateInstance(0,0)));
//      currentclip.intersect(addclip);
//      this.graphics.clip(currentclip );
    }
    public void erasepage(){
	this.graphics.clearRect(0, 0, size.width, size.height);
    }

    public void charpath(String aString, boolean adjustForStroking) {
      FontRenderContext frc=this.graphics.getFontRenderContext();
      Font fn=this.state.font;
//      System.out.println("Fonthoehe:"+fn.getSize2D());
	GlyphVector glyphVector = fn.createGlyphVector(frc, aString);
        Point2D currentPoint = this.state.path.getCurrentPoint();
        Shape glyphShape = glyphVector.getOutline();
        AffineTransform currentTransform = AffineTransform.getScaleInstance(1,-1);
        glyphShape=currentTransform.createTransformedShape(glyphShape);
        AffineTransform currentTransform2 = AffineTransform.getTranslateInstance((float)currentPoint.getX(),(float)currentPoint.getY());
        glyphShape=currentTransform2.createTransformedShape(glyphShape);
        this.state.path.append(glyphShape, false);
    }

    public void showpage(){
      PdfGraphics2D pdfg2d = (PdfGraphics2D) this.graphics;
        PdfContentByte cb = pdfg2d.getContent();
      try {
        cb.getPdfWriter().newPage();
      }
      catch (com.lowagie.text.DocumentException ex) {
        ex.printStackTrace();
      }
    }

    public void show(String string) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();
        AffineTransform currentTransform = this.graphics.getTransform();
        Point2D tranformedPoint = currentTransform.transform(currentPoint, null);

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
        this.graphics.setTransform(new AffineTransform());
        this.graphics.drawString(string, (float) tranformedPoint.getX(), (float) tranformedPoint.getY());
        this.graphics.setTransform(currentTransform);
    }

    public void fill(){
        this.graphics.fill(this.state.path);
        this.newpath();
    }

    public void eofill(){
        this.state.path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
        this.graphics.fill(this.state.path);
        this.state.path.setWindingRule(GeneralPath.WIND_NON_ZERO);
        this.newpath();
    }

    public void stroke(){
       this.graphics.draw(this.state.path);
        this.newpath();

    }

    public void rectfill(double x, double y, double width, double height){
	this.gsave();
	this.rectpath(x, y, width, height);
	this.fill();
	this.grestore();
    }

    public void rectfill(Rectangle2D rect){
        this.rectfill(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void rectstroke(double x, double y, double width, double height){
	this.gsave();
	this.rectpath(x, y, width, height);
	this.stroke();
	this.grestore();
    }

    public void rectstroke(Rectangle2D rect){
	this.rectstroke(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void rectpath(double x, double y, double width, double height){
	this.newpath();
	this.moveto(x, y);
	try {
	    this.rlineto(width, 0);
	    this.rlineto(0, height);
	    this.rlineto(-width, 0);
	} catch(PainterException e){
	}
	this.closepath();
    }

    // convenience

    // this guy tries to find an appropiate font
    // if he fails returns whatever font he wants
    public Font findFont(String fontname){
        Font result;
        StringBuffer buffer = new StringBuffer(fontname);
        int i, n;
        n = buffer.length();

        for(i = 0; i < n; i++){
            if(buffer.charAt(i) == '-'){
                buffer.setCharAt(i,' ');
            }
        }

        fontname = buffer.toString();

        if(this.fonts == null){
            // construct the fonts dictionary
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] fontArray = genv.getAllFonts();
            this.fonts = new HashMap();
            for(i = 0; i < fontArray.length; i++){
                String postscriptName = fontArray[i].getPSName();
                this.fonts.put(postscriptName, fontArray[i]);
            }
        }
        result = (Font) this.fonts.get(fontname);
        if(result == null){
//            result = new Font("SansSerif", Font.PLAIN, 12);
                result = new Font("Arial", Font.PLAIN, 12);
        }
        return result;
    }
}

