/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002, 2003, 2004 by Mark Hall
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
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
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

package com.lowagie.text.rtf.text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.graphic.RtfImage;


/**
 * The RtfParagraph is an extension of the RtfPhrase that adds alignment and
 * indentation properties. It wraps a Paragraph.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfParagraph extends RtfPhrase {

    /**
     * Constant for the end of a paragraph
     */
    public static final byte[] PARAGRAPH = "\\par".getBytes();
    /**
     * Constant for left alignment
     */
    public static final byte[] ALIGN_LEFT = "\\ql".getBytes();
    /**
     * Constant for right alignment
     */
    public static final byte[] ALIGN_RIGHT = "\\qr".getBytes();
    /**
     * Constant for center alignment
     */
    public static final byte[] ALIGN_CENTER = "\\qc".getBytes();
    /**
     * Constant for justified alignment
     */
    public static final byte[] ALIGN_JUSTIFY = "\\qj".getBytes();
    /**
     * Constant for left indentation
     */
    public static final byte[] INDENT_LEFT = "\\li".getBytes();
    /**
     * Constant for right indentation
     */
    public static final byte[] INDENT_RIGHT = "\\ri".getBytes();
    /**
     * Constant for keeping the paragraph together on one page
     */
    public static final byte[] KEEP_TOGETHER = "\\keep".getBytes();
    /**
     * Constant for keeping the paragraph toghether with the next one on one page
     */
    public static final byte[] KEEP_TOGETHER_WITH_NEXT = "\\keepn".getBytes();
    /**
     * Constant for the space before the paragraph.
     */
    private static final byte[] SPACING_BEFORE = "\\sb".getBytes();
    /**
     * Constant for the space after the paragraph.
     */
    private static final byte[] SPACING_AFTER = "\\sa".getBytes();
    
    /**
     * The alignment of this RtfParagraph
     */
    private int alignment = Element.ALIGN_UNDEFINED;
    /**
     * The left indentation of this RtfParagraph
     */
    private int indentLeft = 0;
    /**
     * The right indentation of this RtfParagraph
     */
    private int indentRight = 0;
    /**
     * Whether this RtfParagraph must stay on one page.
     */
    private boolean keepTogether = false;
    /**
     * Whether this RtfParagraph must stay on the same page as the next paragraph.
     */
    private boolean keepTogetherWithNext = false;
    /**
     * The space before this paragraph.
     */
    private int spacingBefore = 0;
    /**
     * The space after this paragraph.
     */
    private int spacingAfter = 0;
    
    /**
     * Constructs a RtfParagraph belonging to a RtfDocument based on a Paragraph.
     * 
     * @param doc The RtfDocument this RtfParagraph belongs to
     * @param paragraph The Paragraph that this RtfParagraph is based on
     */
    public RtfParagraph(RtfDocument doc, Paragraph paragraph) {
        super(doc, paragraph);
        
        this.alignment = paragraph.alignment();
        this.indentLeft = (int) (paragraph.indentationLeft() * RtfElement.TWIPS_FACTOR);
        this.indentRight = (int) (paragraph.indentationRight() * RtfElement.TWIPS_FACTOR);
        this.keepTogether = paragraph.getKeepTogether();
        this.spacingBefore = (int) (paragraph.spacingBefore() * RtfElement.TWIPS_FACTOR);
        this.spacingAfter = (int) (paragraph.spacingAfter() * RtfElement.TWIPS_FACTOR);
        
        for(int i = 0; i < this.chunks.size(); i++) {
            if(chunks.get(i) instanceof RtfImage) {
                ((RtfImage) chunks.get(i)).setAlignment(this.alignment);
            }
        }
    }
    
    /**
     * Set whether this RtfParagraph must stay on the same page as the next one.
     *  
     * @param keepTogetherWithNext Whether this RtfParagraph must keep together with the next.
     */
    public void setKeepTogetherWithNext(boolean keepTogetherWithNext) {
        this.keepTogetherWithNext = keepTogetherWithNext;
    }
    
    /**
     * Writes the content of this RtfParagraph. First paragraph specific data is written
     * and then the RtfChunks of this RtfParagraph are added.
     * 
     * @return The content of this RtfParagraph
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(PARAGRAPH_DEFAULTS);
            if(this.keepTogether) {
                result.write(KEEP_TOGETHER);
            }
            if(this.keepTogetherWithNext) {
                result.write(KEEP_TOGETHER_WITH_NEXT);
            }
            if(inTable) {
                result.write(IN_TABLE);
            }
            switch (alignment) {
            	case Element.ALIGN_LEFT:
            		result.write(ALIGN_LEFT);
            		break;
            	case Element.ALIGN_RIGHT:
            		result.write(ALIGN_RIGHT);
            		break;
            	case Element.ALIGN_CENTER:
            		result.write(ALIGN_CENTER);
            		break;
            	case Element.ALIGN_JUSTIFIED:
            	case Element.ALIGN_JUSTIFIED_ALL:
            		result.write(ALIGN_JUSTIFY);
            		break;
            }
    	    result.write(INDENT_LEFT);
    	    result.write(intToByteArray(indentLeft));
    	    result.write(INDENT_RIGHT);
    	    result.write(intToByteArray(indentRight));
    	    if(this.spacingBefore > 0) {
    	        result.write(SPACING_BEFORE);
    	        result.write(intToByteArray(this.spacingBefore));
    	    }
    	    if(this.spacingAfter > 0) {
    	        result.write(SPACING_AFTER);
    	        result.write(intToByteArray(this.spacingAfter));
    	    }
            if(this.lineLeading > 0) {
                result.write(LINE_SPACING);
                result.write(intToByteArray(this.lineLeading));
            }
            for(int i = 0; i < chunks.size(); i++) {
                result.write(((RtfBasicElement) chunks.get(i)).write());
            }
            if(!inTable) {
                result.write(PARAGRAPH);
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
}
