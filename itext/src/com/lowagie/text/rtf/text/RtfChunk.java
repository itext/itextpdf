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

import com.lowagie.text.Chunk;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfFont;


/**
 * The RtfChunk contains one piece of text. The smallest text element available
 * in iText.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfChunk extends RtfElement {

    /**
     * Constant for the subscript flag
     */
    private static final byte[] FONT_SUBSCRIPT = "\\sub".getBytes();
    /**
     * Constant for the superscript flag
     */
    private static final byte[] FONT_SUPERSCRIPT = "\\super".getBytes();
    /**
     * Constant for the end of sub / superscript flag
     */
    private static final byte[] FONT_END_SUPER_SUBSCRIPT = "\\nosupersub".getBytes();

    /**
     * The font of this RtfChunk
     */
    private RtfFont font = null;
    /**
     * The actual content of this RtfChunk
     */
    private String content = "";
    /**
     * Whether to use soft line breaks instead of hard ones.
     */
    private boolean softLineBreaks = false;
    /**
     * The super / subscript of this RtfChunk
     */
    private float superSubScript = 0;

    /**
     * Constructs a RtfChunk based on the content of a Chunk
     * 
     * @param doc The RtfDocument that this Chunk belongs to
     * @param chunk The Chunk that this RtfChunk is based on
     */
    public RtfChunk(RtfDocument doc, Chunk chunk) {
        super(doc);
        
        if(chunk == null) {
            return;
        }
        
        if(chunk.getAttributes() != null && chunk.getAttributes().get(Chunk.SUBSUPSCRIPT) != null) {
            this.superSubScript = ((Float)chunk.getAttributes().get(Chunk.SUBSUPSCRIPT)).floatValue();
        }
        font = new RtfFont(doc, chunk.font());
        content = chunk.content();
    }
    
    /**
     * Writes the content of this RtfChunk. First the font information
     * is written, then the content, and then more font information
     * 
     * @return A byte array with the content of this RtfChunk
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(font.writeBegin());
            if(superSubScript < 0) {
                result.write(FONT_SUBSCRIPT);
            } else if(superSubScript > 0) {
                result.write(FONT_SUPERSCRIPT);
            }
            result.write(DELIMITER);
            
            result.write(document.filterSpecialChar(content, false, softLineBreaks).getBytes());
            
            if(superSubScript != 0) {
                result.write(FONT_END_SUPER_SUBSCRIPT);
            }
            result.write(font.writeEnd());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Sets the RtfDocument this RtfChunk belongs to.
     * 
     * @param doc The RtfDocument to use
     */
    public void setRtfDocument(RtfDocument doc) {
        super.setRtfDocument(doc);
        this.font.setRtfDocument(this.document);
    }
    
    /**
     * Sets whether to use soft line breaks instead of default hard ones.
     * 
     * @param softLineBreaks whether to use soft line breaks instead of default hard ones.
     */
    public void setSoftLineBreaks(boolean softLineBreaks) {
        this.softLineBreaks = softLineBreaks;
    }
    
    /**
     * Gets whether to use soft line breaks instead of default hard ones.
     * 
     * @return whether to use soft line breaks instead of default hard ones.
     */
    public boolean getSoftLineBreaks() {
        return this.softLineBreaks;
    }
}
