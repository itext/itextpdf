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

import com.lowagie.text.rtf.document.output.RtfDataCache;
import com.lowagie.text.rtf.style.RtfParagraphStyle;


/**
 * The RtfDocumentSettings contains output specific settings. These settings modify
 * how the actual document is then generated and some settings may mean that some
 * RTF readers can't read the document or render it wrongly.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfDocumentSettings {

    /**
     * The RtfDocument this RtfDocumentSettings belongs to.
     */
    private RtfDocument document = null;
    /**
     * Whether to also output the table row definition after the cell content.
     */
    private boolean outputTableRowDefinitionAfter = true;
    /**
     * Whether to output the line breaks that make the rtf document source more readable.
     */
    private boolean outputDebugLineBreaks = true;
    /**
     * Whether to always generate soft linebreaks for \n in Chunks.
     */
    private boolean alwaysGenerateSoftLinebreaks = false;
    /**
     * Whether to always translate characters past 'z' into unicode representations.
     */
    private boolean alwaysUseUnicode = true;
    /**
     * How to cache the document during generation. Defaults to RtfDataCache.CACHE_MEMORY;
     */
    private int dataCacheStyle = RtfDataCache.CACHE_MEMORY;

    
    /**
     * Constructs a new RtfDocumentSettings object.
     * 
     * @param document The RtfDocument this RtfDocumentSettings belong to.
     */
    public RtfDocumentSettings(RtfDocument document) {
        this.document = document;
    }
    
    /**
     * Gets whether to output the line breaks for increased rtf document readability.
     * 
     * @return Whether to output line breaks.
     */
    public boolean isOutputDebugLineBreaks() {
        return outputDebugLineBreaks;
    }
    
    /**
     * Sets whether to output the line breaks for increased rtf document readability.
     * Some line breaks may be added where the rtf specification demands it.
     * 
     * @param outputDebugLineBreaks The outputDebugLineBreaks to set.
     */
    public void setOutputDebugLineBreaks(boolean outputDebugLineBreaks) {
        this.outputDebugLineBreaks = outputDebugLineBreaks;
    }
    
    /**
     * Gets whether the table row definition should also be written after the cell content.
     * 
     * @return Returns the outputTableRowDefinitionAfter.
     */
    public boolean isOutputTableRowDefinitionAfter() {
        return outputTableRowDefinitionAfter;
    }
    
    /**
     * Sets whether the table row definition should also be written after the cell content.
     * This is recommended to be set to <code>true</code> if you need Word2000 compatiblity and
     * <code>false</code> if the document should be opened in OpenOffice.org Writer.
     * 
     * @param outputTableRowDefinitionAfter The outputTableRowDefinitionAfter to set.
     */
    public void setOutputTableRowDefinitionAfter(
            boolean outputTableRowDefinitionAfter) {
        this.outputTableRowDefinitionAfter = outputTableRowDefinitionAfter;
    }
    
    /**
     * Gets whether all linebreaks inside Chunks are generated as soft linebreaks.
     * 
     * @return <code>True</code> if soft linebreaks are generated, <code>false</code> for hard linebreaks.
     */
    public boolean isAlwaysGenerateSoftLinebreaks() {
        return this.alwaysGenerateSoftLinebreaks;
    }

    /**
     * Sets whether to always generate soft linebreaks.
     * 
     * @param alwaysGenerateSoftLinebreaks Whether to always generate soft linebreaks.
     */
    public void setAlwaysGenerateSoftLinebreaks(boolean alwaysGenerateSoftLinebreaks) {
        this.alwaysGenerateSoftLinebreaks = alwaysGenerateSoftLinebreaks;
    }
    
    /**
     * Gets whether all characters bigger than 'z' are represented as unicode.
     * 
     * @return <code>True</code> if unicode representation is used, <code>false</code> otherwise.
     */
    public boolean isAlwaysUseUnicode() {
        return this.alwaysUseUnicode;
    }
    
    /**
     * Sets whether to represent all characters bigger than 'z' as unicode.
     * 
     * @param alwaysUseUnicode <code>True</code> to use unicode representation, <code>false</code> otherwise.
     */
    public void setAlwaysUseUnicode(boolean alwaysUseUnicode) {
        this.alwaysUseUnicode = alwaysUseUnicode;
    }

    /**
     * Registers the RtfParagraphStyle for further use in the document. This does not need to be
     * done for the default styles in the RtfParagraphStyle object. Those are added automatically.
     * 
     * @param rtfParagraphStyle The RtfParagraphStyle to register.
     */
    public void registerParagraphStyle(RtfParagraphStyle rtfParagraphStyle) {
        this.document.getDocumentHeader().registerParagraphStyle(rtfParagraphStyle);
    }
    
    /**
     * Sets the data cache style. This controls where the document is cached during
     * generation. Two cache styles are supported:
     * <ul>
     *   <li>RtfDataCache.CACHE_MEMORY: The document is cached in memory. This is fast,
     *     but places a limit on how big the document can get before causing
     *     OutOfMemoryExceptions.</li>
     *   <li>RtfDataCache.CACHE_DISK: The document is cached on disk. This is slower
     *     than the CACHE_MEMORY setting, but the document size is now only constrained
     *     by the amount of free disk space.</li>
     * </ul>
     * 
     * @param dataCacheStyle The data cache style to set. Valid constants can be found
     *  in RtfDataCache.
     * @see com.lowagie.text.rtf.document.output.output.RtfDataCache.
     */
    public void setDataCacheStyle(int dataCacheStyle) {
        switch(dataCacheStyle) {
            case RtfDataCache.CACHE_MEMORY : this.dataCacheStyle = RtfDataCache.CACHE_MEMORY;break;
            case RtfDataCache.CACHE_DISK   : this.dataCacheStyle = RtfDataCache.CACHE_DISK;break;
            default                        : this.dataCacheStyle = RtfDataCache.CACHE_MEMORY;break;
        }
    }
    
    /**
     * Gets the current data cache style.
     * 
     * @return The current data cache style.
     */
    public int getDataCacheStyle() {
        return this.dataCacheStyle;
    }
}
