/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
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
 */

package com.lowagie.text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import com.lowagie.text.pdf.PdfContentByte;

/**
 * A <CODE>Graphic</CODE> element can contain several geometric figures (curves, lines,...).
 * <P>
 * If you want to use this <CODE>Element</CODE>, please read the Sections 8.4 and 8.5 of
 * the PDF Reference Manual version 1.3 first.
 *
 * @see		Element
 */

public class Graphic extends PdfContentByte implements Element {
    
/** This is a type of Graphic. */
    public static final String HORIZONTAL_LINE = "HORIZONTAL";
    
/** This is a type of Graphic. */
    public static final String BORDER = "BORDER";
    
/** Contains some of the attributes for this Graphic. */
    private HashMap attributes;
    
    // constructor
    
/**
 * Constructs a <CODE>Graphic</CODE>-object.
 */
    
    public Graphic() {
        super(null);
    }
    
    // implementation of the Element interface
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param	listener	an <CODE>ElementListener</CODE>
 * <CODE>true</CODE> if the element was processed successfully
 */
    
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public int type() {
        return Element.GRAPHIC;
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
/**
 * Orders this graphic to draw a horizontal line.
 */
    
    public void setHorizontalLine(float linewidth, float percentage) {
        if (attributes == null) attributes = new HashMap();
        attributes.put(HORIZONTAL_LINE, new Object[]{new Float(linewidth), new Float(percentage), new Color(0, 0, 0)});
    }
    
/**
 * Orders this graphic to draw a horizontal line.
 */
    
    public void setHorizontalLine(float linewidth, float percentage, Color color) {
        if (attributes == null) attributes = new HashMap();
        attributes.put(HORIZONTAL_LINE, new Object[]{new Float(linewidth), new Float(percentage), color});
    }
    
/**
 * draws a horizontal line.
 */
    
    public void drawHorizontalLine(float lineWidth, Color color, float x1, float x2, float y) {
        setLineWidth(lineWidth);
        setColorStroke(color);
        moveTo(x1, y);
        lineTo(x2, y);
        stroke();
        resetRGBColorStroke();
    }
    
/**
 * Orders this graphic to draw a horizontal line.
 */
    
    public void setBorder(float linewidth, float extraSpace) {
        if (attributes == null) attributes = new HashMap();
        attributes.put(BORDER, new Object[]{new Float(linewidth), new Float(extraSpace), new Color(0, 0, 0)});
    }
    
/**
 * Orders this graphic to draw a horizontal line.
 */
    
    public void setBorder(float linewidth, float extraSpace, Color color) {
        if (attributes == null) attributes = new HashMap();
        attributes.put(BORDER, new Object[]{new Float(linewidth), new Float(extraSpace), color});
    }
    
/**
 * Draws a border
 */
    public void drawBorder(float lineWidth, Color color, float llx, float lly, float urx, float ury) {
        setLineWidth(lineWidth);
        setColorStroke(color);
        rectangle(llx, lly, urx - llx, ury - lly);
        stroke();
        resetRGBColorStroke();
    }
    
/**
 * Processes the attributes of this object.
 */
    
    public void processAttributes(float llx, float lly, float urx, float ury, float y) {
        if (attributes == null) return;
        String attribute;
        Object[] o;
        for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
            attribute = (String) i.next();
            o = (Object[]) attributes.get(attribute);
            if (HORIZONTAL_LINE.equals(attribute)) {
                float p = ((Float)o[1]).floatValue();
                float w = (urx - llx) * (100.0f - p) / 200.0f;
                drawHorizontalLine(((Float)o[0]).floatValue(), (Color)o[2], llx + w, urx - w, y);
            }
            if (BORDER.equals(attribute)) {
                float extra = ((Float)o[1]).floatValue();
                drawBorder(((Float)o[0]).floatValue(), (Color)o[2], llx - extra, lly - extra, urx + extra, ury + extra);
            }
        }
    }
}
