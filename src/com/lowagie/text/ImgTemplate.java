/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001 by Bruno Lowagie and Paulo Soares.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 * This class was entirely written by Paulo Soares.
 */

package com.lowagie.text;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * PdfTemplate that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 *
 * @author  Paulo Soares
 */

public class ImgTemplate extends Image implements Element {
    
    /** Creats an Image from a PdfTemplate.
     *
     * @param template the PdfTemplate
     * @throws BadElementException on error
     */
    public ImgTemplate(PdfTemplate template) throws BadElementException{
        super((URL)null);
        type = IMGTEMPLATE;
        scaledHeight = template.getHeight();
        setTop(scaledHeight);
        scaledWidth = template.getWidth();
        setRight(scaledWidth);
        this.template = template;
        plainWidth = width();
        plainHeight = height();
    }
}