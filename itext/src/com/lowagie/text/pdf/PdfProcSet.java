/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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

package com.lowagie.text.pdf;

/**
 * <CODE>PdfProcSet</CODE> is the PDF ProcSet object.
 * <P>
 * The types of instructions that may be used in a PDF marking context are grouped
 * into independent sets of related instructions. Each of these sets, called a ProcSet,
 * may or may not be used in a particular context. ProcSets contain implementations of
 * the PDF operators and are used only when a page or other context is printed.
 * The Resources Dictionary for each conext must contain a <B>ProcSet</B> key whose
 * value is an array consisting of the names of the ProcSets used in that context.
 * Each of the entries in the array must be one of the predefined names that are
 * contained in this class as static membervariables.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.6 (page 198).
 *
 * @see		PdfResource
 * @see		PdfResources
 */

class PdfProcSet implements PdfResource {
    
    // membervariables
    
/**	This is a possible type procset */
    public static final int PDF = 1;
    
/**	This is a possible type procset */
    public static final int TEXT = 2;
    
/**	This is a possible type procset */
    public static final int IMAGEB = 4;
    
/**	This is a possible type procset */
    public static final int IMAGEC = 8;
    
/**	This is a possible type procset */
    public static final int IMAGEI = 16;
    
/** This is the array containing all the possible procsettypes */
    private static int[] types;
    
/** This is the array containing all the possible procsetnames */
    private static PdfName[] names;
    
/** This is the number of possible procsettypes */
    private static final int N = 5;
    
    static {
        types = new int[N];
        names = new PdfName[N];
        types[0] = PDF;
        names[0] = PdfName.PDF;
        types[1] = TEXT;
        names[1] = PdfName.TEXT;
        types[2] = IMAGEB;
        names[2] = PdfName.IMAGEB;
        types[3] = IMAGEC;
        names[3] = PdfName.IMAGEC;
        types[4] = IMAGEI;
        names[4] = PdfName.IMAGEI;
    }
    
/** This is the value of this resource */
    private PdfObject value;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfResource</CODE> with the procset as a direct object.
 *
 * @param		procset		a number that represents the different procset-types in this resource
 */
    
    PdfProcSet(int procset) {
        value = getProcSet(procset);
    }
    
/**
 * Constructs a <CODE>PdfResource</CODE> with the procset as an indirect object.
 *
 * @param		procset		a <CODE>PdfIndirectReference</CODE> to a ProcSet
 */
    
    PdfProcSet(PdfIndirectReference procset) {
        value = procset;
    }
    
    // methods
    
/**
 * Returns the name of a resource.
 *
 * @return		a <CODE>PdfName</CODE>.
 */
    
    public PdfName key() {
        return PdfName.PROCSET;
    }
    
/**
 * Returns the object that represents the resource.
 *
 * @return		a <CODE>PdfObject</CODE>
 */
    
    public PdfObject value() {
        return value;
    }
    
/**
 * Constructs a <CODE>PdfArray</CODE> with a number of procsettypes.
 *
 * @param		procset		a number that represents the different procset-types in this resource
 * @return		a <CODE>PdfArray</CODE>
 */
    
    static final PdfArray getProcSet(int procset) {
        PdfArray tmp = new PdfArray();
        for (int i = 0; i < N; i++) {
            if ((procset & types[i]) > 0) {
                tmp.add(names[i]);
            }
        }
        return tmp;
    }
}