/*
 * $Id$
 * $Name$
 *
 * Copyright 2002 Bruno Lowagie
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

import java.util.Iterator;
import java.util.HashMap;

/**
 * Each PDF document can contain maximum 1 AcroForm.
 */

public class PdfAcroForm extends PdfDictionary {
    
    private PdfWriter writer;
    
    
    /** This is a map containing FieldTemplates. */
    private HashMap fieldTemplates = new HashMap();
    
    /** This is an array containing DocumentFields. */
    private PdfArray documentFields = new PdfArray();
    
    private PdfArray calculationOrder = new PdfArray();
    
    private int sigFlags = 0;
    
    /** Creates new PdfAcroForm */
    public PdfAcroForm(PdfWriter writer) {
        super();
        this.writer = writer;
    }
    
    /**
     * Adds fieldTemplates.
     */
    
    void addFieldTemplates(HashMap ft) {
        fieldTemplates.putAll(ft);
    }
    
    /**
     * Adds documentFields.
     */
    
    void addDocumentField(PdfIndirectReference ref) {
        documentFields.add(ref);
    }
    
    public void addCalculationOrder(PdfIndirectReference ref) {
        calculationOrder.add(ref);
    }
    
    public void setSigFlags(int f) {
        sigFlags |= f;
    }
    
    public void addFormField(PdfFormField formField) {
        writer.addAnnotation(formField);
    }
    
    /**
     * Closes the AcroForm.
     */
    
    public boolean isValid() {
        if (documentFields.size() == 0) return false;
        put(PdfName.FIELDS, documentFields);
        if (sigFlags != 0)
            put(PdfName.SIGFLAGS, new PdfNumber(sigFlags));
        if (calculationOrder.size() > 0)
            put(PdfName.CO, calculationOrder);
        if (fieldTemplates.size() == 0) return false;
        PdfDictionary dic = new PdfDictionary();
        for (Iterator it = fieldTemplates.keySet().iterator(); it.hasNext();) {
            PdfTemplate template = (PdfTemplate)it.next();
            PdfFormField.mergeResources(dic, (PdfDictionary)template.getResources());
        }
        put(PdfName.DR, dic);
        PdfDictionary fonts = (PdfDictionary)dic.get(PdfName.FONT);
        if (fonts != null) {
            put(PdfName.DA, new PdfString("/F1 0 Tf 0 g "));
            writer.eliminateFontSubset(fonts);
        }
        return true;
    }
}