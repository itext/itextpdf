package com.lowagie.text.pdf;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.Iterator;

/** Page labels are used to identify each
 * page visually on the screen or in print.
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public class PdfPageLabels implements Comparator {

    /** Logical pages will have the form 1,2,3,...
     */    
    public static int DECIMAL_ARABIC_NUMERALS = 0;
    /** Logical pages will have the form I,II,III,IV,...
     */    
    public static int UPPERCASE_ROMAN_NUMERALS = 1;
    /** Logical pages will have the form i,ii,iii,iv,...
     */    
    public static int LOWERCASE_ROMAN_NUMERALS = 2;
    /** Logical pages will have the form of uppercase letters
     * (A to Z for the first 26 pages, AA to ZZ for the next 26, and so on)
     */    
    public static int UPPERCASE_LETTERS = 3;
    /** Logical pages will have the form of uppercase letters
     * (a to z for the first 26 pages, aa to zz for the next 26, and so on)
     */    
    public static int LOWERCASE_LETTERS = 4;
    /** No logical page numbers are generated but fixed text may
     * still exist
     */    
    public static int EMPTY = 5;
    /** Dictionary values to set the logical page styles
     */    
    static PdfName numberingStyle[];
    /** The sequence of logical pages. Will contain at least a value for page 1
     */    
    TreeMap map;
    
    static {
        try {
            numberingStyle = new PdfName[]{new PdfName("D"), new PdfName("R"),
                new PdfName("r"), new PdfName("A"), new PdfName("a")};
        }
        catch (Exception e) {
        }
    }
    /** Creates a new PdfPageLabel with a default logical page 1
     */
    public PdfPageLabels() {
        map = new TreeMap(this);
        addPageLabel(1, DECIMAL_ARABIC_NUMERALS, null, 1);
    }

    /** Compares two <CODE>Integer</CODE>.
     * @param obj the first <CODE>Integer</CODE>
     * @param obj1 the second <CODE>Integer</CODE>
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
     */    
    public int compare(Object obj, Object obj1) {
        int v1 = ((Integer)obj).intValue();
        int v2 = ((Integer)obj1).intValue();
        if (v1 < v2)
            return -1;
        if (v1 == v2)
            return 0;
        return 1;
    }
    
    /** Not used
     * @param obj not used
     * @return always <CODE>true</CODE>
     */    
    public boolean equals(Object obj) {
        return true;
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
    
    /** Removes a page label. The first page lagel can not be removed, only changed.
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
        try {
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
                    subDic.put(new PdfName("St"), new PdfNumber(st));
                array.add(new PdfNumber(((Integer)obj[0]).intValue() - 1));
                array.add(subDic);
            }
            dic.put(new PdfName("Nums"), array);
        }
        catch (BadPdfFormatException e) {
        }
        return dic;
    }
}
