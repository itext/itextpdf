package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import java.util.HashMap;

public class CMapCidByte extends AbstractCMap {
    private HashMap<Integer,byte[]> map = new HashMap<Integer,byte[]>();
    private final byte[] EMPTY = {};
    
    @Override
    void addChar(PdfString mark, PdfObject code) {
        if (!(code instanceof PdfNumber))
            return;
        byte[] ser = decodeStringToByte(mark);
        map.put(Integer.valueOf(((PdfNumber)code).intValue()), ser);
    }
    
    public byte[] lookup(int cid) {
        byte[] ser = map.get(Integer.valueOf(cid));
        if (ser == null)
            return EMPTY;
        else
            return ser;
    }
}
