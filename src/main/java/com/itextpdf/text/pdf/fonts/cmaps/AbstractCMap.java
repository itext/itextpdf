/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

/**
 *
 * @author psoares
 */
public abstract class AbstractCMap {

    private String cmapName;
    private String registry;
    private String ordering;
    private int supplement;
    
    public String getName() {
        return cmapName;
    }

    void setName(String cmapName) {
        this.cmapName = cmapName;
    }
    
    public String getOrdering() {
        return ordering;
    }

    void setOrdering(String ordering) {
        this.ordering = ordering;
    }
    
    public String getRegistry() {
        return registry;
    }

    void setRegistry(String registry) {
        this.registry = registry;
    }
    
    public int getSupplement() {
        return supplement;
    }
    
    void setSupplement(int supplement) {
        this.supplement = supplement;
    }

    abstract void addChar(PdfString mark, PdfObject code);
    
    void addRange(PdfString from, PdfString to, PdfObject code) {
        byte[] a1 = decodeStringToByte(from);
        byte[] a2 = decodeStringToByte(to);
        if (a1.length != a2.length || a1.length == 0)
            throw new IllegalArgumentException("Invalid map.");
        int start = a1[a1.length - 1] & 0xff;
        int end = a2[a2.length - 1] & 0xff;
        for (int k = start; k <= end; ++k) {
            a1[a1.length - 1] = (byte)k;
            PdfString s = new PdfString(a1);
            s.setHexWriting(true);
            if (code instanceof PdfArray) {
                addChar(s, ((PdfArray)code).getPdfObject(k - start));
            }
            else if (code instanceof PdfNumber) {
                int nn = ((PdfNumber)code).intValue() + k - start;
                addChar(s, new PdfNumber(nn));
            }
        }
    }
    
    public static byte[] decodeStringToByte(PdfString s) {
        byte[] b = s.getBytes();
        byte[] br = new byte[b.length];
        System.arraycopy(b, 0, br, 0, b.length);
        return br;
    }

    public String decodeStringToUnicode(PdfString ps) {
        if (ps.isHexWriting())
            return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
        else
            return ps.toUnicodeString();
    }
}