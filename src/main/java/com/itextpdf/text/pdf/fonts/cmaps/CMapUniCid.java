package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.IntHashtable;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

/**
 *
 * @author psoares
 */
public class CMapUniCid extends AbstractCMap {
    private IntHashtable map = new IntHashtable(65537);
    
    @Override
    void addChar(PdfString mark, PdfObject code) {
        if (!(code instanceof PdfNumber))
            return;
        int codepoint;
        String s = decodeStringToUnicode(mark);
        if (Utilities.isSurrogatePair(s, 0))
            codepoint = Utilities.convertToUtf32(s, 0);
        else
            codepoint = (int)s.charAt(0);
        map.put(codepoint, ((PdfNumber)code).intValue());
    }
    
    public int lookup(int character) {
        return map.get(character);
    }    
}
