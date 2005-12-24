/*
 * $Id$
 * $Name$
 *
 * Copyright 2003, 2004, 2005 by Mark Hall
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

package com.lowagie.text.rtf.document;

import com.lowagie.text.DocumentException;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.RtfMapper;
import com.lowagie.text.rtf.document.output.RtfDataCache;
import com.lowagie.text.rtf.document.output.RtfDiskCache;
import com.lowagie.text.rtf.document.output.RtfMemoryCache;
import com.lowagie.text.rtf.graphic.RtfImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The RtfDocument stores all document related data and also the main data stream.
 * INTERNAL CLASS - NOT TO BE USED DIRECTLY
 *
 * Version: $Id$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Todd Bush (Todd.Bush@canopysystems.com) [Tab support]
 */
public class RtfDocument extends RtfElement {
    /**
     * Stores the actual document data
     */
    private RtfDataCache data = null;
    /**
     * The RtfMapper to use in this RtfDocument
     */
    private RtfMapper mapper = null;
    /**
     * The RtfDocumentHeader that handles all document header methods
     */
    private RtfDocumentHeader documentHeader = null;
    /**
     * Stores integers that have been generated as unique random numbers
     */
    private ArrayList previousRandomInts = null;
    /**
     * Whether to automatically generate TOC entries for Chapters and Sections. Defaults to false
     */
    private boolean autogenerateTOCEntries = false;
    /**
     * Whether data has been written to the RtfDataCache.
     */
    private boolean dataWritten = false;
    /**
     * The RtfDocumentSettings for this RtfDocument.
     */
    private RtfDocumentSettings documentSettings = null;
    /**
     * The last RtfBasicElement that was added directly to the RtfDocument.
     */
    private RtfBasicElement lastElementWritten = null;
    
    /**
     * Constant for the Rtf document start
     */
    private static final byte[] RTF_DOCUMENT = "\\rtf1".getBytes();

    /**
     * The default constructor for a RtfDocument
     */
    public RtfDocument() {
        super(null);
        data = new RtfMemoryCache();
        mapper = new RtfMapper(this);
        documentHeader = new RtfDocumentHeader(this);
        documentHeader.init();
        previousRandomInts = new ArrayList();
        this.documentSettings = new RtfDocumentSettings(this);
    }

    /**
     * Writes the document
     *
     * @return A byte array containing the complete rtf document
     */
    public byte[] writeDocument() {
        ByteArrayOutputStream docStream = new ByteArrayOutputStream();
        try {
            docStream.write(OPEN_GROUP);
            docStream.write(RtfDocument.RTF_DOCUMENT);
            docStream.write(documentHeader.write());
            data.writeTo(docStream);
            docStream.write(CLOSE_GROUP);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return docStream.toByteArray();
    }
    
    /**
     * Adds an element to the rtf document
     * 
     * @param element The element to add
     */
    public void add(RtfBasicElement element) {
        try {
            if(element instanceof RtfInfoElement) {
                this.documentHeader.addInfoElement((RtfInfoElement) element);
            } else {
                this.dataWritten = true;
                if(element instanceof RtfImage) {
                    ((RtfImage) element).setTopLevelElement(true);
                }
                data.getOutputStream().write(element.write());
                this.lastElementWritten = element;
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Gets the RtfMapper object of this RtfDocument
     * 
     * @return The RtfMapper
     */
    public RtfMapper getMapper() {
        return mapper;
    }
    
    /**
     * Generates a random integer that is unique with respect to the document.
     * 
     * @return A random int
     */
    public int getRandomInt() {
        Integer newInt = null;
        do {
            newInt = new Integer((int) (Math.random() * Integer.MAX_VALUE));
        } while(previousRandomInts.contains(newInt));
        previousRandomInts.add(newInt);
        return newInt.intValue();
    }
    
    /**
     * Gets the RtfDocumentHeader of this RtfDocument
     * 
     * @return The RtfDocumentHeader of this RtfDocument
     */
    public RtfDocumentHeader getDocumentHeader() {
        return this.documentHeader;
    }
    
    /**
     * Replaces special characters with their unicode values
     * @param str The original <code>String</code>
     * @param useHex indicated if the hexadecimal value has to be used
     * @param softLineBreaks whether to use soft line breaks instead of default hard ones.
     *
     * @return The converted String
     */
    public String filterSpecialChar(String str, boolean useHex, boolean softLineBreaks) {
        int length = str.length();
        int z = (int) 'z';
        StringBuffer ret = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);

            if (ch == '\\') {
                ret.append("\\\\");
            } else if (ch == '\n') {
                if(softLineBreaks) {
                    ret.append("\\line ");
                } else {
                    ret.append("\\par ");
                }
            } else if (ch == '\t') {
                ret.append("\\tab ");
            } else if (((int) ch) > z && this.documentSettings.isAlwaysUseUnicode()) {
                if(useHex) {
                    ret.append("\\\'").append(Long.toHexString((long) ch));
                } else {
                    ret.append("\\u").append((long) ch).append('?');
                }
            } else {
                ret.append(ch);
            }
        }
        String s = ret.toString();
        if(s.indexOf("$newpage$") >= 0) {
            String before = s.substring(0, s.indexOf("$newpage$"));
            String after = s.substring(s.indexOf("$newpage$") + 9);
            ret = new StringBuffer(before);
            ret.append("\\page\\par ");
            ret.append(after);
            return ret.toString();
        }
        return s;
    }
    
    /**
     * Whether to automagically generate table of contents entries when
     * adding Chapters or Sections.
     * 
     * @param autogenerate Whether to automatically generate TOC entries
     */
    public void setAutogenerateTOCEntries(boolean autogenerate) {
        this.autogenerateTOCEntries = autogenerate;
    }
    
    /**
     * Get whether to autmatically generate table of contents entries
     * 
     * @return Wheter to automatically generate TOC entries
     */
    public boolean getAutogenerateTOCEntries() {
        return this.autogenerateTOCEntries;
    }
    
    /**
     * Sets the rtf data cache style to use. Valid values are given in the 
     * RtfDataCache class.
     *  
     * @param dataCacheStyle The style to use.
     * @throws DocumentException If data has already been written into the data cache.
     * @throws IOException If the disk cache could not be initialised.
     */
    public void setDataCacheStyle(int dataCacheStyle) throws DocumentException, IOException {
        if(dataWritten) {
            throw new DocumentException("Data has already been written into the data cache. You can not change the cache style anymore.");
        }
        switch(dataCacheStyle) {
            case RtfDataCache.CACHE_MEMORY : this.data = new RtfMemoryCache(); break;
            case RtfDataCache.CACHE_DISK   : this.data = new RtfDiskCache(); break;
            default                        : this.data = new RtfMemoryCache(); break;
        }
    }
    
    /**
     * Gets the RtfDocumentSettings that specify how the rtf document is generated.
     * 
     * @return The current RtfDocumentSettings.
     */
    public RtfDocumentSettings getDocumentSettings() {
        return this.documentSettings;
    }
    
    /**
     * Gets the last RtfBasicElement that was directly added to the RtfDocument.
     *  
     * @return The last RtfBasicElement that was directly added to the RtfDocument.
     */
    public RtfBasicElement getLastElementWritten() {
        return this.lastElementWritten;
    }
}
