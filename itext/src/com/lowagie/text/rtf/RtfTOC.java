/**
 * $Id$
 *
 * Copyright 2002 by 
 * <a href="http://www.smb-tec.com">SMB</a> 
 * <a href="mailto:Steffen.Stundzig@smb-tec.com">Steffen.Stundzig@smb-tec.com</a>
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
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.ExceptionConverter;

/**
 * This class can be used to insert a table of contents into 
 * the RTF document.
 * Therefore the field TOC is used. It works great in Word 2000. 
 * StarOffice doesn't support such fields. Other word version
 * are not tested yet.
 * 
 * ONLY FOR USE WITH THE RtfWriter NOT with the RtfWriter2.
 *
 * This class is based on the RtfWriter-package from Mark Hall.
 * @author <a href="mailto:Steffen.Stundzig@smb-tec.com">Steffen.Stundzig@smb-tec.com</a> 
 * @version $Revision$Date: 2004/09/24 15:23:46 $
 */
public class RtfTOC extends Chunk implements RtfField {


    private String      defaultText = "Klicken Sie mit der rechten Maustaste auf diesen Text, um das Inhaltsverzeichnis zu aktualisieren!";

    private boolean     addTOCAsTOCEntry = false;

    private Font        entryFont = null;
    private String      entryName = null;


    /**
     * @param tocName the headline of the table of contents
     * @param tocFont the font for the headline
     */
    public RtfTOC( String tocName, Font tocFont ) {
        super( tocName, tocFont );
    }


    public void write( RtfWriter writer, OutputStream out ) throws IOException {

        writer.writeInitialFontSignature( out, this );
        out.write( RtfWriter.filterSpecialChar( content(), true ).getBytes() );
        writer.writeFinishingFontSignature( out, this );
        
        if (addTOCAsTOCEntry) {
            RtfTOCEntry entry = new RtfTOCEntry( entryName, entryFont );
            entry.hideText();
            try {
                writer.add( entry );
            } catch ( DocumentException de ) {
                throw new ExceptionConverter(de);
            }
        }

        // line break after headline
        out.write( RtfWriter.escape );
        out.write( RtfWriter.paragraph );
        out.write( RtfWriter.delimiter );

        // toc field entry
        out.write( RtfWriter.openGroup );
        out.write( RtfWriter.escape );
        out.write( RtfWriter.field );
            // field initialization stuff
            out.write( RtfWriter.openGroup );        
            out.write( RtfWriter.escape );
            out.write( RtfWriter.fieldContent );
            out.write( RtfWriter.delimiter );
            out.write( "TOC".getBytes() );
            // create the TOC based on the 'toc entries'
            out.write( RtfWriter.delimiter );
            out.write( RtfWriter.escape );        
            out.write( RtfWriter.escape );        
            out.write( "f".getBytes() );
            out.write( RtfWriter.delimiter );
            // create Hyperlink TOC Entrie 
            out.write( RtfWriter.escape );        
            out.write( RtfWriter.escape );        
            out.write( "h".getBytes() );
            out.write( RtfWriter.delimiter );
            // create the TOC based on the paragraph level
            out.write( RtfWriter.delimiter );
            out.write( RtfWriter.escape );        
            out.write( RtfWriter.escape );        
            out.write( "u".getBytes() );
            out.write( RtfWriter.delimiter );
            // create the TOC based on the paragraph headlines 1-5
            out.write( RtfWriter.delimiter );
            out.write( RtfWriter.escape );        
            out.write( RtfWriter.escape );        
            out.write( "o".getBytes() );
            out.write( RtfWriter.delimiter );
            out.write( "\"1-5\"".getBytes() );
            out.write( RtfWriter.delimiter );
            out.write( RtfWriter.closeGroup );

            // field default result stuff
            out.write( RtfWriter.openGroup );        
            out.write( RtfWriter.escape );
            out.write( RtfWriter.fieldDisplay );
            out.write( RtfWriter.delimiter );
            out.write( defaultText.getBytes() );
            out.write( RtfWriter.delimiter );
            out.write( RtfWriter.closeGroup );
        out.write( RtfWriter.closeGroup );
    }

    
    public void addTOCAsTOCEntry( String entryName, Font entryFont ) {
        this.addTOCAsTOCEntry = true;
        this.entryFont = entryFont;
        this.entryName = entryName;        
    }

    
    public void setDefaultText( String text ) {
        this.defaultText = text;
    }
}
