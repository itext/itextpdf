/*
 * $Id: PdfPageLabels.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.factories.RomanAlphabetFactory;
import com.itextpdf.text.factories.RomanNumberFactory;

/** Page labels are used to identify each
 * page visually on the screen or in print.
 * @author  Paulo Soares
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
    private HashMap<Integer, PdfDictionary> map;

    /** Creates a new PdfPageLabel with a default logical page 1
     */
    public PdfPageLabels() {
        map = new HashMap<Integer, PdfDictionary>();
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
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("in.a.page.label.the.page.numbers.must.be.greater.or.equal.to.1"));
        PdfDictionary dic = new PdfDictionary();
        if (numberStyle >= 0 && numberStyle < numberingStyle.length)
            dic.put(PdfName.S, numberingStyle[numberStyle]);
        if (text != null)
            dic.put(PdfName.P, new PdfString(text, PdfObject.TEXT_UNICODE));
        if (firstPage != 1)
            dic.put(PdfName.ST, new PdfNumber(firstPage));
        map.put(Integer.valueOf(page - 1), dic);
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

    /** Adds or replaces a page label.
     */
    public void addPageLabel(PdfPageLabelFormat format) {
        addPageLabel(format.physicalPage, format.numberStyle, format.prefix, format.logicalPage);
    }

    /** Removes a page label. The first page label can not be removed, only changed.
     * @param page the real page to remove
     */
    public void removePageLabel(int page) {
        if (page <= 1)
            return;
        map.remove(Integer.valueOf(page - 1));
    }

    /** Gets the page label dictionary to insert into the document.
     * @return the page label dictionary
     */
    public PdfDictionary getDictionary(PdfWriter writer) {
        try {
            return PdfNumberTree.writeTree(map, writer);
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Retrieves the page labels from a PDF as an array of String objects.
     * @param reader a PdfReader object that has the page labels you want to retrieve
     * @return	a String array or <code>null</code> if no page labels are present
     */
    public static String[] getPageLabels(PdfReader reader) {

		int n = reader.getNumberOfPages();

    	PdfDictionary dict = reader.getCatalog();
		PdfDictionary labels = (PdfDictionary)PdfReader.getPdfObjectRelease(dict.get(PdfName.PAGELABELS));
        if (labels == null)
            return null;

		String[] labelstrings = new String[n];

		HashMap<Integer, PdfObject> numberTree = PdfNumberTree.readTree(labels);

		int pagecount = 1;
		Integer current;
		String prefix = "";
		char type = 'D';
		for (int i = 0; i < n; i++) {
			current = Integer.valueOf(i);
			if (numberTree.containsKey(current)) {
				PdfDictionary d = (PdfDictionary)PdfReader.getPdfObjectRelease(numberTree.get(current));
				if (d.contains(PdfName.ST)) {
					pagecount = ((PdfNumber)d.get(PdfName.ST)).intValue();
				}
				else {
					pagecount = 1;
				}
				if (d.contains(PdfName.P)) {
					prefix = ((PdfString)d.get(PdfName.P)).toUnicodeString();
				}
				if (d.contains(PdfName.S)) {
					type = ((PdfName)d.get(PdfName.S)).toString().charAt(1);
				}
				else {
					type = 'e';
				}
			}
			switch(type) {
			default:
				labelstrings[i] = prefix + pagecount;
				break;
			case 'R':
				labelstrings[i] = prefix + RomanNumberFactory.getUpperCaseString(pagecount);
				break;
			case 'r':
				labelstrings[i] = prefix + RomanNumberFactory.getLowerCaseString(pagecount);
				break;
			case 'A':
				labelstrings[i] = prefix + RomanAlphabetFactory.getUpperCaseString(pagecount);
				break;
			case 'a':
				labelstrings[i] = prefix + RomanAlphabetFactory.getLowerCaseString(pagecount);
				break;
			case 'e':
				labelstrings[i] = prefix;
				break;
			}
			pagecount++;
		}
		return labelstrings;
    }

    /**
     * Retrieves the page labels from a PDF as an array of {@link PdfPageLabelFormat} objects.
     * @param reader a PdfReader object that has the page labels you want to retrieve
     * @return	a PdfPageLabelEntry array, containing an entry for each format change
     * or <code>null</code> if no page labels are present
     */
    public static PdfPageLabelFormat[] getPageLabelFormats(PdfReader reader) {
        PdfDictionary dict = reader.getCatalog();
        PdfDictionary labels = (PdfDictionary)PdfReader.getPdfObjectRelease(dict.get(PdfName.PAGELABELS));
        if (labels == null)
            return null;
        HashMap<Integer, PdfObject> numberTree = PdfNumberTree.readTree(labels);
        Integer numbers[] = new Integer[numberTree.size()];
        numbers = numberTree.keySet().toArray(numbers);
        Arrays.sort(numbers);
        PdfPageLabelFormat[] formats = new PdfPageLabelFormat[numberTree.size()];
        String prefix;
        int numberStyle;
        int pagecount;
        for (int k = 0; k < numbers.length; ++k) {
            Integer key = numbers[k];
            PdfDictionary d = (PdfDictionary)PdfReader.getPdfObjectRelease(numberTree.get(key));
            if (d.contains(PdfName.ST)) {
                pagecount = ((PdfNumber)d.get(PdfName.ST)).intValue();
            } else {
                pagecount = 1;
            }
            if (d.contains(PdfName.P)) {
                prefix = ((PdfString)d.get(PdfName.P)).toUnicodeString();
            } else {
                prefix = "";
            }
            if (d.contains(PdfName.S)) {
                char type = ((PdfName)d.get(PdfName.S)).toString().charAt(1);
                switch(type) {
                    case 'R': numberStyle = UPPERCASE_ROMAN_NUMERALS; break;
                    case 'r': numberStyle = LOWERCASE_ROMAN_NUMERALS; break;
                    case 'A': numberStyle = UPPERCASE_LETTERS; break;
                    case 'a': numberStyle = LOWERCASE_LETTERS; break;
                    default: numberStyle = DECIMAL_ARABIC_NUMERALS; break;
                }
            } else {
                numberStyle = EMPTY;
            }
            formats[k] = new PdfPageLabelFormat(key.intValue()+1, numberStyle, prefix, pagecount);
        }
        return formats;
    }

    public static class PdfPageLabelFormat {

        public int physicalPage;
        public int numberStyle;
        public String prefix;
        public int logicalPage;

        /** Creates a page label format.
         * @param physicalPage the real page to start the numbering. First page is 1
         * @param numberStyle the numbering style such as LOWERCASE_ROMAN_NUMERALS
         * @param prefix the text to prefix the number. Can be <CODE>null</CODE> or empty
         * @param logicalPage the first logical page number
         */
        public PdfPageLabelFormat(int physicalPage, int numberStyle, String prefix, int logicalPage) {
            this.physicalPage = physicalPage;
            this.numberStyle = numberStyle;
            this.prefix = prefix;
            this.logicalPage = logicalPage;
        }
    }
}
