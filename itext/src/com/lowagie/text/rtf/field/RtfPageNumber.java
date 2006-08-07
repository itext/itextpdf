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

package com.lowagie.text.rtf.field;

import java.io.IOException;

import com.lowagie.text.Font;
import com.lowagie.text.rtf.document.RtfDocument;


/**
 * The RtfPageNumber provides the page number field in rtf documents.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author <a href="mailto:Steffen.Stundzig@smb-tec.com">Steffen.Stundzig@smb-tec.com</a>
 */
public class RtfPageNumber extends RtfField {

    /**
     * Constant for the page number
     */
    private static final byte[] PAGE_NUMBER = "PAGE".getBytes();
    
    /**
     * Constructs a RtfPageNumber. This can be added anywhere to add a page number field.
     */
    public RtfPageNumber() {
        super(null);
    }
    
    /**
     * Constructs a RtfPageNumber with a specified Font. This can be added anywhere to
     * add a page number field.
     * @param font
     */
    public RtfPageNumber(Font font) {
        super(null, font);
    }
    
    /**
     * Constructs a RtfPageNumber object.
     * 
     * @param doc The RtfDocument this RtfPageNumber belongs to
     */
    public RtfPageNumber(RtfDocument doc) {
        super(doc);
    }
    
    /**
     * Constructs a RtfPageNumber object with a specific font.
     * 
     * @param doc The RtfDocument this RtfPageNumber belongs to
     * @param font The Font to use
     */
    public RtfPageNumber(RtfDocument doc, Font font) {
        super(doc, font);
    }
    
    /**
     * Writes the field instruction content
     * 
     * @return A byte array containing "PAGE"
     * @throws IOException
     */
    protected byte[] writeFieldInstContent() throws IOException {
        return PAGE_NUMBER;
    }

    /**
     * Writes the field result content
     * 
     * @return An empty byte array
     * @throws IOException
     */
    protected byte[] writeFieldResultContent() throws IOException {
        return new byte[0];
    }
}
