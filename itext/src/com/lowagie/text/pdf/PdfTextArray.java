/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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
 */

package com.lowagie.text.pdf;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <CODE>PdfTextArray</CODE> defines an array with displacements and <CODE>PdfString</CODE>-objects.
 * <P>
 * A <CODE>TextArray</CODE> is used with the operator <VAR>TJ</VAR> in <CODE>PdfText</CODE>.
 * The first object in this array has to be a <CODE>PdfString</CODE>;
 * see reference manual version 1.3 section 8.7.5, pages 346-347.
 */

public final class PdfTextArray{
    ArrayList arrayList;
    // constructors
    
    
/**
 * Constructs an <CODE>PdfArray</CODE>-object, containing 1 <CODE>PdfObject</CODE>.
 */
    
    public PdfTextArray(PdfString str) {
        arrayList = new ArrayList();
        arrayList.add(str);
    }
    
/**
 * Adds a <CODE>PdfNumber</CODE> to the <CODE>PdfArray</CODE>.
 *
 * @param		number			displacement of the string
 *
 * @return		<CODE>true</CODE>
 */
    
    public void add(PdfString str)
    {
        arrayList.add(str);
    }
    
    public void add(PdfNumber number)
    {
        arrayList.add(number);
    }
    
    ArrayList getArrayList() {
        return arrayList;
    }
}