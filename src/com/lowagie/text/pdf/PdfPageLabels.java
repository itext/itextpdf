/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import com.lowagie.text.List;
import com.lowagie.text.RomanList;

/** Page labels are used to identify each
 * page visually on the screen or in print.
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public class PdfPageLabels {

    /** Logical pages will have the form 1,2,3,...
     */    
    public static final int DECIMAL_ARABIC_NUMERALS = 0;
    /** Logical pages will have the form I,II,III,IV,...
     */    
    public static final int UPPERCASE_ROMAN_NUMERALS = 1;
    /** Logical pages will have the form i,ii,iii,iv,...
     */    
    public static final int LOWERCASE_ROMAN_NUMERALS = 2;
    /** Logical pages will have the form of uppercase letters
     * (A to Z for the first 26 pages, AA to ZZ for the next 26, and so on)
     */    
    public static final int UPPERCASE_LETTERS = 3;
    /** Logical pages will have the form of uppercase letters
     * (a to z for the first 26 pages, aa to zz for the next 26, and so on)
     */    
    public static final int LOWERCASE_LETTERS = 4;
    /** No logical page numbers are generated but fixed text may
     * still exist
     */    
    public static final int EMPTY = 5;
    /** Dictionary values to set the logical page styles
     */    
    static PdfName numberingStyle[] = new PdfName[]{PdfName.D, PdfName.R,
                new PdfName("r"), PdfName.A, new PdfName("a")};
    /** The sequence of logical pages. Will contain at least a value for page 1
     */    
    TreeMap map;
    
    /** Creates a new PdfPageLabel with a default logical page 1
     */
    public PdfPageLabels() {
        map = new TreeMap();
        addPageLabel(1, DECIMAL_ARABIC_NUMERALS, null, 1);
    }

    /** Adds or replaces a page label.
     * @param page the real page to start the numbering. First page is 1
     * @param numberStyle the numbering style such as LOWERCASE_ROMAN_NUMERALS
     * @param text the text to prefix the number. Can be <CODE>null</CODE> or empty
     * @param firstPage the first logical page number
     */    
    public void addPageLabel(int page, int numberStyle, String text, int firstPage) {
        if (page < 1 || firstPage < 1)
            throw new IllegalArgumentException("In a page label the page numbers must be greater or equal to 1.");
        PdfName pdfName = null;
        if (numberStyle >= 0 && numberStyle < numberingStyle.length)
            pdfName = numberingStyle[numberStyle];
        Integer iPage = new Integer(page);
        Object obj = new Object[]{iPage, pdfName, text, new Integer(firstPage)};
        map.put(iPage, obj);
    }

    /** Adds or replaces a page label. The first logical page has the default
     * of 1.
     * @param page the real page to start the numbering. First page is 1
     * @param numberStyle the numbering style such as LOWERCASE_ROMAN_NUMERALS
     * @param text the text to prefix the number. Can be <CODE>null</CODE> or empty
     */    
    public void addPageLabel(int page, int numberStyle, String text) {
        addPageLabel(page, numberStyle, text, 1);
    }
    
    /** Adds or replaces a page label. There is no text prefix and the first
     * logical page has the default of 1.
     * @param page the real page to start the numbering. First page is 1
     * @param numberStyle the numbering style such as LOWERCASE_ROMAN_NUMERALS
     */    
    public void addPageLabel(int page, int numberStyle) {
        addPageLabel(page, numberStyle, null, 1);
    }
    
    /** Removes a page label. The first page label can not be removed, only changed.
     * @param page the real page to remove
     */    
    public void removePageLabel(int page) {
        if (page <= 1)
            return;
        map.remove(new Integer(page));
    }

    /** Gets the page label dictionary to insert into the document.
     * @return the page label dictionary
     */    
    PdfDictionary getDictionary() {
        PdfDictionary dic = new PdfDictionary();
        PdfArray array = new PdfArray();
        for (Iterator it = map.values().iterator(); it.hasNext();) {
            Object obj[] = (Object[])it.next();
            PdfDictionary subDic = new PdfDictionary();
            PdfName pName = (PdfName)obj[1];
            if (pName != null)
                subDic.put(PdfName.S, pName);
            String text = (String)obj[2];
            if (text != null)
                subDic.put(PdfName.P, new PdfString(text, PdfObject.TEXT_UNICODE));
            int st = ((Integer)obj[3]).intValue();
            if (st != 1)
                subDic.put(PdfName.ST, new PdfNumber(st));
            array.add(new PdfNumber(((Integer)obj[0]).intValue() - 1));
            array.add(subDic);
        }
        dic.put(PdfName.NUMS, array);
        return dic;
    }
    
    /**
     * Retrieves the page labels from a PDF as an array of String objects.
     * @param reader a PdfReader object that has the page labels you want to retrieve
     * @return	a String array
     */
    public static String[] getPageLabels(PdfReader reader) {
    	
		int n = reader.getNumberOfPages();
		String[] labelstrings = new String[n];
		
    	PdfDictionary dict = reader.getCatalog();
		PdfDictionary labels = (PdfDictionary)PdfReader.getPdfObject((PdfObject)dict.get(PdfName.PAGELABELS));
		PdfArray numbers = (PdfArray)PdfReader.getPdfObject((PdfObject)labels.get(PdfName.NUMS));
		
		PdfNumber pageIndex;
		PdfDictionary pageLabel;
		HashMap numberTree = new HashMap();
		for (Iterator i = numbers.listIterator(); i.hasNext(); ) {
			pageIndex = (PdfNumber)i.next();
			pageLabel = (PdfDictionary) PdfReader.getPdfObject((PdfObject)i.next());
			numberTree.put(new Integer(pageIndex.intValue()), pageLabel);
		}
		
		int pagecount = 1;
		Integer current;
		String prefix = "";
		char type = 'D';
		for (int i = 0; i < n; i++) {
			current = new Integer(i);
			if (numberTree.containsKey(current)) {
				PdfDictionary d = (PdfDictionary)numberTree.get(current);
				if (d.contains(PdfName.ST)) {
					pagecount = ((PdfNumber)d.get(PdfName.ST)).intValue();
				}
				else {
					pagecount = 1;
				}
				if (d.contains(PdfName.P)) {
					prefix = ((PdfString)d.get(PdfName.P)).toString();
				}
				if (d.contains(PdfName.S)) {
					type = ((PdfName)d.get(PdfName.S)).toString().charAt(1);
				}
			}
			switch(type) {
			default:
				labelstrings[i] = prefix + pagecount;
				break;
			case 'R':
				labelstrings[i] = prefix + RomanList.toRomanUpperCase(pagecount);
				break;
			case 'r':
				labelstrings[i] = prefix + RomanList.toRomanLowerCase(pagecount);
				break;
			case 'A':
				labelstrings[i] = prefix + List.getUpperCaseLetter(pagecount);
				break;
			case 'a':
				labelstrings[i] = prefix + List.getLowerCaseLetter(pagecount);
				break;
			}
			pagecount++;
		}
		return labelstrings;
    }
}