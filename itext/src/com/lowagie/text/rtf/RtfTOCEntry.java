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
import com.lowagie.text.Font;

/**
 * This class can be used to insert entries for a table of contents into 
 * the RTF document.
 *
 * This class is based on the RtfWriter-package from Mark Hall.
 * @author <a href="mailto:Steffen.Stundzig@smb-tec.com">Steffen.Stundzig@smb-tec.com</a> 
 * @version $Revision$Date: 2003/05/02 09:01:36 $
 */
public class RtfTOCEntry extends Chunk implements RtfField {


    private boolean         hideText = false;

    private boolean         hidePageNumber = false;    

    private String    entryName;

    private Font      entryFont;    

    private Font      contentFont;    


    public RtfTOCEntry( String content, Font contentFont ) {
        this( content, contentFont, content, contentFont );
//        super( content, font );
//        this.entryName = content;
//        printEntryNameAsText = true;
    }


    public RtfTOCEntry( String content, Font contentFont, String entryName, Font entryFont ) {
        super( content, contentFont );
        // hide the text of the entry, because it is printed  
        this.entryName = entryName;
        this.entryFont = entryFont;
        this.contentFont = contentFont;
    }


    public void write( RtfWriter writer, OutputStream out ) throws IOException {

        if (!hideText) {
            writer.writeInitialFontSignature( out, new Chunk("", contentFont) );
            out.write( RtfWriter.filterSpecialChar( content() ).getBytes() );
            writer.writeFinishingFontSignature( out, new Chunk("", contentFont) );
        }

        if (!entryFont.equals( contentFont )) {
            writer.writeInitialFontSignature(out, new Chunk("", entryFont) );
            writeField( out );
            writer.writeFinishingFontSignature(out, new Chunk("", entryFont) );
        } else {
            writer.writeInitialFontSignature(out, new Chunk("", contentFont) );
            writeField( out );
            writer.writeFinishingFontSignature(out, new Chunk("", contentFont) );
        }
    }

    
    private void writeField( OutputStream out ) throws IOException {
        
        // always hide the toc entry
        out.write( RtfWriter.openGroup );
        out.write( RtfWriter.escape );
        out.write( "v".getBytes() );

        // tc field entry
        out.write( RtfWriter.openGroup );
        out.write( RtfWriter.escape );
        if (!hidePageNumber) {
            out.write( "tc".getBytes() );
        } else {
            out.write( "tcn".getBytes() );
        }    
        out.write( RtfWriter.delimiter );
        out.write( RtfWriter.filterSpecialChar( entryName ).getBytes() );
        out.write( RtfWriter.delimiter );
        out.write( RtfWriter.closeGroup );        

        out.write( RtfWriter.closeGroup );        
    }


    public void hideText() {
        hideText = true;
    }


    public void hidePageNumber() {
        hidePageNumber = true;
    }
}


