/*
 * Copyright 2008 by Kevin Day.
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
 * the Initial Developer are Copyright (C) 1999-2008 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2008 by Paulo Soares. All Rights Reserved.
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
package com.lowagie.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.lowagie.text.pdf.fonts.cmaps.CMap;
import com.lowagie.text.pdf.fonts.cmaps.CMapParser;

/**
 * Implementation of DocumentFont used while parsing PDF streams.
 * @since 2.1.4
 */
public class CMapAwareDocumentFont extends DocumentFont {

	/** The font dictionary. */
    private PdfDictionary fontDic;
    /** CMap instance. */
    private CMap cmap;
    
    /**
     * Creates an instance of a CMapAwareFont based on an indirect reference to a font.
     * @param refFont	the indirect reference to a font
     */
    public CMapAwareDocumentFont(PRIndirectReference refFont) {
        super(refFont);
        fontDic = (PdfDictionary)PdfReader.getPdfObjectRelease(refFont);
        processToUni();
    }

    /**
     * Does some processing if the font dictionary indicates that the font is in unicode.
     */
    private void processToUni(){
        
        PdfObject toUni = fontDic.get(PdfName.TOUNICODE);

        if (toUni == null)
            return;
        
        try {
            byte[] cmapBytes = PdfReader.getStreamBytes((PRStream)PdfReader.getPdfObjectRelease(toUni));
            CMapParser cmapParser = new CMapParser();
            cmap = cmapParser.parse(new ByteArrayInputStream(cmapBytes));
        } catch (IOException e) {
            throw new Error("Unable to obtain cmap - " + e.getMessage(), e);
        }

    }
    
    /**
     * Encodes bytes to a String.
     * @param bytes		the bytes from a stream
     * @param offset	an offset
     * @param len		a length
     * @return	a String encoded taking into account if the bytes are in unicode or not.
     */
    public String encode(byte[] bytes, int offset, int len){
            if (cmap != null){
                if (len > bytes.length)
                    System.out.println("Length problem...");
                return cmap.lookup(bytes, offset, len);
            }
        
            if (len == 1)
                return new String(bytes, offset, 1);
            
            throw new Error("Multi-byte glyphs not implemented yet");
    }

}
