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

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;

import java.io.*;

/**
 * A rtf page number field.
 *
 * This class is based on the RtfWriter-package from Mark Hall.
 * @author <a href="mailto:Steffen.Stundzig@smb-tec.com">Steffen.Stundzig@smb-tec.com</a>
 * @version $Revision$Date: 2002/04/30 11:38:58 $
 *
 * Modified by Mark Hall (mhall@austromail.at) 14.04.2002
 */
public class RtfPageNumber extends Chunk implements RtfField {

    protected static final String pageControl = "\\chpgn ";

    public RtfPageNumber( String content, Font contentFont ) {
        super( content, contentFont );
	/* This is a hack, because of the way multiple Chunks with the same Font are handled
	 * by iText Phrases and Paragraphs we have to add the Page Number Control to the content
	 * field. */
	this.content.append(pageControl);
    }


    public void write( RtfWriter writer, OutputStream out ) throws IOException {

        writer.writeInitialFontSignature( out, font() );
	out.write(content.toString().getBytes());
/*        out.write( RtfWriter.escape );
        out.write( pageControl );*/
        writer.writeFinishingFontSignature( out, font() );        
//        out.write( RtfWriter.openGroup );
//            out.write( RtfWriter.escape );
//            out.write( RtfWriter.field );
//            out.write( RtfWriter.openGroup );
//                out.write( RtfWriter.extendedEscape );
//                out.write( RtfWriter.fieldContent );
//                out.write( RtfWriter.openGroup );
//                    out.write( RtfWriter.delimiter );
//                    out.write( RtfWriter.fieldPage );
//                    out.write( RtfWriter.delimiter );
//                out.write( RtfWriter.closeGroup );
//            out.write( RtfWriter.closeGroup );
//            out.write( RtfWriter.openGroup );
//                out.write( RtfWriter.escape );
//                out.write( RtfWriter.fieldDisplay );
//                out.write( RtfWriter.openGroup );
//                out.write( RtfWriter.closeGroup );
//            out.write( RtfWriter.closeGroup );
//        out.write( RtfWriter.closeGroup );
    }
}


