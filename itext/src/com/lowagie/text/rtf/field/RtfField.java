/*
 * $Id$
 * $Name$
 *
 * Copyright 2004 by Mark Hall
 * Uses code Copyright 2002
 *   <a href="http://www.smb-tec.com">SMB</a> 
 *   Dirk Weigenand (Dirk.Weigenand@smb-tec.com)
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfFont;


/**
 * The RtfField class is an abstract base class for all rtf field functionality.
 * Subclasses only need to implement the two abstract methods writeFieldInstContent
 * and writeFieldResultContent. All other field functionality is handled by the
 * RtfField class.
 * 
 * @version $Id$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Dirk Weigenand (Dirk.Weigenand@smb-tec.com)
 * @author Thomas Bickel (tmb99@inode.at)
 */
public abstract class RtfField extends Chunk implements RtfBasicElement {

    /**
     * Constant for a rtf field
     */
    private static final byte[] FIELD = "\\field".getBytes();
    /**
     * Constant for a dirty field
     */
    private static final byte[] FIELD_DIRTY = "\\flddirty".getBytes();
    /**
     * Constant for a private field
     */
    private static final byte[] FIELD_PRIVATE = "\\fldpriv".getBytes();
    /**
     * Constant for a locked field
     */
    private static final byte[] FIELD_LOCKED = "\\fldlock".getBytes();
    /**
     * Constant for a edited field
     */
    private static final byte[] FIELD_EDIT = "\\fldedit".getBytes();
    /**
     * Constant for an alt field
     */
    private static final byte[] FIELD_ALT = "\\fldalt".getBytes();
    /**
     * Constant for the field instructions
     */
    private static final byte[] FIELD_INSTRUCTIONS = "\\*\\fldinst".getBytes();
    /**
     * Constant for the field result
     */
    private static final byte[] FIELD_RESULT = "\\fldrslt".getBytes();

    /**
     * Is the field dirty
     */
    private boolean fieldDirty = false;
    /**
     * Is the field edited
     */
    private boolean fieldEdit = false;
    /**
     * Is the field locked
     */
    private boolean fieldLocked = false;
    /**
     * Is the field private
     */
    private boolean fieldPrivate = false;
    /**
     * Is it an alt field
     */
    private boolean fieldAlt = false;
    /**
     * Whether this RtfField is in a table
     */
    private boolean inTable = false;
    /**
     * Whether this RtfElement is in a header
     */
    private boolean inHeader = false;
    /**
     * The RtfDocument this RtfField belongs to 
     */
    protected RtfDocument document = null;
    /**
     * The RtfFont of this RtfField
     */
    private RtfFont font = null;

    /**
     * Constructs a RtfField for a RtfDocument. This is not very useful,
     * since the RtfField by itself does not do anything. Use one of the
     * subclasses instead.
     * 
     * @param doc The RtfDocument this RtfField belongs to.
     */
    protected RtfField(RtfDocument doc) {
        this(doc, new Font());
    }
    
    /**
     * Constructs a RtfField for a RtfDocument. This is not very useful,
     * since the RtfField by itself does not do anything. Use one of the
     * subclasses instead.
     * 
     * @param doc The RtfDocument this RtfField belongs to.
     * @param font The Font this RtfField should use
     */
    protected RtfField(RtfDocument doc, Font font) {
        super("", font);
        this.document = doc;
        this.font = new RtfFont(this.document, font);
    }
    
    /**
     * Sets the RtfDocument this RtfElement belongs to
     * 
     * @param doc The RtfDocument to use
     */
    public void setRtfDocument(RtfDocument doc) {
        this.document = doc;
        this.font.setRtfDocument(this.document);
    }
    
    /**
     * Writes the field beginning. Also writes field properties.
     * 
     * @return A byte array with the field beginning.
     * @deprecated replaced by {@link #writeFieldBegin(OutputStream)}
     * @throws IOException
     */
    private byte[] writeFieldBegin() throws IOException 
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        writeFieldBegin(result);
        return result.toByteArray();
    }
    /**
     * Writes the field beginning. Also writes field properties.
     * 
     * @return A byte array with the field beginning.
     * @throws IOException
     */
    private void writeFieldBegin(OutputStream result) throws IOException 
    {
        result.write(OPEN_GROUP);
        result.write(FIELD);
        if(fieldDirty) result.write(FIELD_DIRTY);
        if(fieldEdit) result.write(FIELD_EDIT);
        if(fieldLocked) result.write(FIELD_LOCKED);
        if(fieldPrivate) result.write(FIELD_PRIVATE);
    }
    
    /**
     * Writes the beginning of the field instruction area.
     * 
     * @return The beginning of the field instruction area
     * @deprecated replaced by {@link #writeFieldInstBegin(OutputStream)}
     * @throws IOException
     */
    private byte[] writeFieldInstBegin() throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        writeFieldInstBegin(result);
        return result.toByteArray();
    }
    /**
     * Writes the beginning of the field instruction area.
     * 
     * @return The beginning of the field instruction area
     * @throws IOException
     */
    private void writeFieldInstBegin(OutputStream result) throws IOException 
    {
        result.write(OPEN_GROUP);        
        result.write(FIELD_INSTRUCTIONS);
        result.write(DELIMITER);
    }
    
    /**
     * Writes the content of the field instruction area. Override this
     * method in your subclasses.
     * 
     * @return The content of the field instruction area
     * @deprecated replaced by {@link #writeFieldInstContent(OutputStream)}
     * @throws IOException If an error occurs.
     */
    protected abstract byte[] writeFieldInstContent() throws IOException;

    /**
     * Writes the content of the field instruction area. Override this
     * method in your subclasses.
     */
    protected void writeFieldInstContent(OutputStream out) throws IOException
    {
    	byte[] b = writeFieldInstContent();
    	if(b != null) out.write(b);
    }
    
    /**
     * Writes the end of the field instruction area.
     * 
     * @return A byte array containing the end of the field instruction area
     * @deprecated replaced by {@link #writeFieldInstEnd(OutputStream)}
     * @throws IOException
     */
    private byte[] writeFieldInstEnd() throws IOException 
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        writeFieldInstEnd(result);
        return result.toByteArray();
    }
    /**
     * Writes the end of the field instruction area.
     */
    private void writeFieldInstEnd(OutputStream result) throws IOException 
    {
        if(fieldAlt) {
            result.write(DELIMITER);
            result.write(FIELD_ALT);
        }
        result.write(CLOSE_GROUP);
    }
    
    /**
     * Writes the beginning of the field result area
     * 
     * @return A byte array containing the beginning of the field result area
     * @deprecated replaced by {@link #writeFieldResultBegin(OutputStream)} 
     * @throws IOException
     */
    private byte[] writeFieldResultBegin() throws IOException 
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        writeFieldResultBegin(result);
        return result.toByteArray();
    }
    /**
     * Writes the beginning of the field result area
     */
    private void writeFieldResultBegin(final OutputStream result) throws IOException 
    {
        result.write(OPEN_GROUP);
        result.write(FIELD_RESULT);
        result.write(DELIMITER);
    }
    
    /**
     * Writes the content of the pre-calculated field result. Override this
     * method in your subclasses.
     * 
     * @return A byte array containing the field result
     * @deprecated replaced by {@link #writeFieldResultContent(OutputStream)}
     * @throws IOException If an error occurs
     */
    protected abstract byte[] writeFieldResultContent() throws IOException;    
    /**
     * Writes the content of the pre-calculated field result. Override this
     * method in your subclasses.
     */ 
    protected void writeFieldResultContent(OutputStream out) throws IOException
    {
    	byte[] b = writeFieldResultContent();
    	if(b != null) out.write(b);
    }
    
    /**
     * Writes the end of the field result area
     * 
     * @return A byte array containing the end of the field result area
     * @deprecated replaced by {@link #writeFieldResultEnd(OutputStream)}
     * @throws IOException
     */
    private byte[] writeFieldResultEnd() throws IOException 
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        writeFieldResultEnd(result);
        return result.toByteArray();
    }
    /**
     * Writes the end of the field result area
     */ 
    private void writeFieldResultEnd(final OutputStream result) throws IOException 
    {
        result.write(DELIMITER);
        result.write(CLOSE_GROUP);
    }
    
    /**
     * Writes the end of the field
     * 
     * @return A byte array containing the end of the field
     * @deprecated replaced by {@link #writeFieldEnd(OutputStream)}
     * @throws IOException
     */
    private byte[] writeFieldEnd() throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        writeFieldEnd(result);        
        return result.toByteArray();
    }
    /**
     * Writes the end of the field
     */
    private void writeFieldEnd(OutputStream result) throws IOException
    {
        result.write(CLOSE_GROUP);
    }
    
    
    /**
     * Write the content of this RtfField.
     * 
     * @return A byte array containing the content of this RtfField
     * @deprecated replaced by {@link #writeContent(OutputStream)}
     */
    public byte[] write()
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
        	writeContent(result);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    /**
     * Writes the element content to the given output stream.
     */    
    public void writeContent(final OutputStream result) throws IOException
    {
        result.write(this.font.writeBegin());
        
//        result.write(writeFieldBegin());
//        result.write(writeFieldInstBegin());
//        result.write(writeFieldInstContent());
//        result.write(writeFieldInstEnd());
//        result.write(writeFieldResultBegin());
//        result.write(writeFieldResultContent());
//        result.write(writeFieldResultEnd());
//        result.write(writeFieldEnd());        
        writeFieldBegin(result);
        writeFieldInstBegin(result);
        writeFieldInstContent(result);
        writeFieldInstEnd(result);
        writeFieldResultBegin(result);
        writeFieldResultContent(result);
        writeFieldResultEnd(result);
        writeFieldEnd(result);
        
        result.write(this.font.writeEnd());
    }        
        
    /**
     * Get whether this field is an alt field
     * 
     * @return Returns whether this field is an alt field
     */
    public boolean isFieldAlt() {
        return fieldAlt;
    }
    
    /**
     * Set whether this field is an alt field
     * 
     * @param fieldAlt The value to use
     */
    public void setFieldAlt(boolean fieldAlt) {
        this.fieldAlt = fieldAlt;
    }
    
    /**
     * Get whether this field is dirty
     * 
     * @return Returns whether this field is dirty
     */
    public boolean isFieldDirty() {
        return fieldDirty;
    }
    
    /**
     * Set whether this field is dirty
     * 
     * @param fieldDirty The value to use
     */
    public void setFieldDirty(boolean fieldDirty) {
        this.fieldDirty = fieldDirty;
    }
    
    /**
     * Get whether this field is edited
     * 
     * @return Returns whether this field is edited
     */
    public boolean isFieldEdit() {
        return fieldEdit;
    }
    
    /**
     * Set whether this field is edited.
     * 
     * @param fieldEdit The value to use
     */
    public void setFieldEdit(boolean fieldEdit) {
        this.fieldEdit = fieldEdit;
    }
    
    /**
     * Get whether this field is locked
     * 
     * @return Returns the fieldLocked.
     */
    public boolean isFieldLocked() {
        return fieldLocked;
    }
    
    /**
     * Set whether this field is locked
     * @param fieldLocked The value to use
     */
    public void setFieldLocked(boolean fieldLocked) {
        this.fieldLocked = fieldLocked;
    }
    
    /**
     * Get whether this field is private
     * 
     * @return Returns the fieldPrivate.
     */
    public boolean isFieldPrivate() {
        return fieldPrivate;
    }
    
    /**
     * Set whether this field is private
     * 
     * @param fieldPrivate The value to use
     */
    public void setFieldPrivate(boolean fieldPrivate) {
        this.fieldPrivate = fieldPrivate;
    }

    /**
     * Sets whether this RtfField is in a table
     * 
     * @param inTable <code>True</code> if this RtfField is in a table, <code>false</code> otherwise
     */
    public void setInTable(boolean inTable) {
        this.inTable = inTable;
    }
    
    /**
     * Sets whether this RtfField is in a header
     * 
     * @param inHeader <code>True</code> if this RtfField is in a header, <code>false</code> otherwise
     */
    public void setInHeader(boolean inHeader) {
        this.inHeader = inHeader;
    }
    
    /**
     * An RtfField is never empty.
     */
    public boolean isEmpty() {
        return false;
    }
    
    /**
     * Override setFont to perform the correct font handling.
     */
    public void setFont(Font font) {
        super.setFont(font);
        this.font = new RtfFont(this.document, font);
    }
}
