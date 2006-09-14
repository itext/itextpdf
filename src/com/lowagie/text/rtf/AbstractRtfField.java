/**
 * $Id$
 *
 * Copyright 2002 by 
 * <a href="http://www.smb-tec.com">SMB</a> 
 * Dirk Weigenand (Dirk.Weigenand@smb-tec.com)
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
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
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

package com.lowagie.text.rtf;

import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;


/**
 * This class implements an abstract RtfField.
 *
 * This class is based on the RtfWriter-package from Mark Hall.
 * 
 * ONLY FOR USE WITH THE RtfWriter NOT with the RtfWriter2.
 *
 * @author Dirk Weigenand (Dirk.Weigenand@smb-tec.com)
 * @version $Id$
 * @since Mon Aug 19 14:50:39 2002
 * @deprecated Please move to the RtfWriter2 and associated classes.
 */
abstract class AbstractRtfField extends Chunk implements RtfField {
    private static final byte[] fldDirty = "\\flddirty".getBytes();
    private static final byte[] fldPriv = "\\fldpriv".getBytes();
    private static final byte[] fldLock = "\\fldlock".getBytes();
    private static final byte[] fldEdit = "\\fldedit".getBytes();
    private static final byte[] fldAlt = "\\fldalt".getBytes();

    /**
     * public constructor
     * @param content the content of the field
     * @param font the font of the field
     */
    public AbstractRtfField(String content, Font font) {
        super(content, font);
    }

    /**
     * Determines whether this RtfField is locked, i.e. it cannot be
     * updated. Defaults to <tt>false</tt>.
     */
    private boolean rtfFieldIsLocked = false;

    /**
     * Determines whether a formatting change has been made since the
     * field was last updated. Defaults to <tt>false</tt>.
     */
    private boolean rtfFieldIsDirty = false;

    /**
     * Determines whether text has been added, removed from thre field
     * result since the field was last updated. Defaults to
     * <tt>false</tt>.
     */
    private boolean rtfFieldWasEdited = false;

    /**
     * Determines whether the field is in suitable form for
     * display. Defaults to <tt>false</tt>.
     */
    private boolean rtfFieldIsPrivate = false;

    /**
     * Determines whether this RtfField shall refer to an end note.
     */
    private boolean rtfFieldIsAlt = false;

    /**
     * Determines whtether the field is locked, i.e. it cannot be
     * updated.
     * 
     * @return <tt>true</tt> iff the field cannot be updated,
     * <tt>false</tt> otherwise.
     */
    public final boolean isLocked() {
        return this.rtfFieldIsLocked;
    }

    /**
     * Set whether the field can be updated.
     *
     * @param rtfFieldIsLocked <tt>true</tt> if the field cannot be
     * updated, <tt>false</tt> otherwise.
     */
    public final void setLocked(final boolean rtfFieldIsLocked) {
        this.rtfFieldIsLocked = rtfFieldIsLocked;
    }

    /**
     * Set whether a formatting change has been made since the field
     * was last updated
     * @param rtfFieldIsDirty <tt>true</tt> if the field was
     * changed since the field was last updated, <tt>false</tt>
     * otherwise.
     */
    public final void setDirty(final boolean rtfFieldIsDirty) {
        this.rtfFieldIsDirty = rtfFieldIsDirty;
    }

    /**
     * Determines whether the field was changed since the field was
     * last updated
     * @return <tt>true</tt> if the field was changed since the field
     * was last updated, <tt>false</tt> otherwise.
     */
    public final boolean isDirty() {
        return this.rtfFieldIsDirty;
    }

    /**
     * Set whether text has been added, removed from thre field result
     * since the field was last updated.
     * @param rtfFieldWasEdited Determines whether text has been
     * added, removed from the field result since the field was last
     * updated (<tt>true</tt>, <tt>false</tt> otherwise..
     */
    public final void setEdited(final boolean rtfFieldWasEdited) {
        this.rtfFieldWasEdited = rtfFieldWasEdited;
    }

    /**
     * Determines whether text has been added, removed from the field
     * result since the field was last updated.
     * @return rtfFieldWasEdited <tt>true</tt> if text has been added,
     * removed from the field result since the field was last updated,
     * <tt>false</tt> otherwise.
     */
    public final boolean wasEdited() {
        return this.rtfFieldWasEdited;
    }

    /**
     * Set whether the field is in suitable form for
     * display. I.e. it's not a field with a picture as field result
     * @param rtfFieldIsPrivate Determines whether the field is in
     * suitable form for display: <tt>true</tt> it can be displayed,
     * <tt>false</tt> it cannot be displayed.
     */
    public final void setPrivate(final boolean rtfFieldIsPrivate) {
        this.rtfFieldIsPrivate = rtfFieldIsPrivate;
    }

    /**
     * Determines whether the field is in suitable form for display.
     * @return whether the field is in suitable form for display:
     * <tt>true</tt> yes, <tt>false</tt> no it cannot be displayed.
     */
    public final boolean isPrivate() {
        return this.rtfFieldIsPrivate;
    }

    /**
     * Abstract method for writing custom stuff to the Field
     * Initialization Stuff part of an RtfField.
     * @param out
     * @throws IOException
     */
    public abstract void writeRtfFieldInitializationStuff(OutputStream out) throws IOException;

    /**
     * Abstract method for writing custom stuff to the Field Result
     * part of an RtfField.
     * @param out
     * @throws IOException
     */
    public abstract void writeRtfFieldResultStuff(OutputStream out) throws IOException;

    /**
     * Determines whether this RtfField shall refer to an end note.
     * @param rtfFieldIsAlt <tt>true</tt> if this RtfField shall refer
     * to an end note, <tt>false</tt> otherwise
     */
    public final void setAlt(final boolean rtfFieldIsAlt) {
        this.rtfFieldIsAlt = rtfFieldIsAlt;
    }

    /**
     * Determines whether this RtfField shall refer to an end
     * note.
     * @return <tt>true</tt> if this RtfField shall refer to an end
     * note, <tt>false</tt> otherwise.
     */
    public final boolean isAlt() {
        return this.rtfFieldIsAlt;
    }

    /**
     * empty implementation for Chunk.
     * @return an empty string
     */
    public final String content() {
        return "";
    }

    /**
     * For Interface RtfField.
     * @param writer
     * @param out
     * @throws IOException
     */
    public void write( RtfWriter writer, OutputStream out ) throws IOException {
        writeRtfFieldBegin(out);
        writeRtfFieldModifiers(out);
        writeRtfFieldInstBegin(out);
        writer.writeInitialFontSignature( out, this );
        writeRtfFieldInitializationStuff(out);
        writeRtfFieldInstEnd(out);
        writeRtfFieldResultBegin(out);
        writer.writeInitialFontSignature( out, this );
        writeRtfFieldResultStuff(out);
        writeRtfFieldResultEnd(out);
        writeRtfFieldEnd(out);
    }

    /**
     * Write the beginning of an RtfField to the OutputStream.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldBegin(OutputStream out)  throws IOException {
        out.write(RtfWriter.openGroup);
        out.write(RtfWriter.escape);
        out.write(RtfWriter.field);
    }

    /**
     * Write the modifiers defined for a RtfField to the OutputStream.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldModifiers(OutputStream out) throws IOException {
        if (isDirty()) {
            out.write(fldDirty);
        }

        if (wasEdited()) {
            out.write(fldEdit);
        }

        if (isLocked()) {
            out.write(fldLock);
        }

        if (isPrivate()) {
            out.write(fldPriv);
        }
    }

    /**
     * Write RtfField Initialization Stuff to OutputStream.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldInstBegin(OutputStream out) throws IOException {
        out.write( RtfWriter.openGroup );        
        out.write( RtfWriter.escape );
        out.write( RtfWriter.fieldContent );
        out.write( RtfWriter.delimiter );
    }

    /**
     * Write end of RtfField Initialization Stuff to OutputStream.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldInstEnd(OutputStream out) throws IOException {
        if (isAlt()) {
            out.write( fldAlt );
            out.write( RtfWriter.delimiter );
        }

        out.write( RtfWriter.closeGroup );
    }

    /**
     * Write beginning of RtfField Result to OutputStream.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldResultBegin(OutputStream out) throws IOException {
        out.write( RtfWriter.openGroup );        
        out.write( RtfWriter.escape );
        out.write( RtfWriter.fieldDisplay );
        out.write( RtfWriter.delimiter );
    }

    /**
     * Write end of RtfField Result to OutputStream.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldResultEnd(OutputStream out) throws IOException {
        out.write( RtfWriter.delimiter );
        out.write( RtfWriter.closeGroup );
    }

    /**
     * Close the RtfField.
     * @param out
     * @throws IOException
     */
    protected final void writeRtfFieldEnd(OutputStream out) throws IOException {
        out.write( RtfWriter.closeGroup );
    }
}
