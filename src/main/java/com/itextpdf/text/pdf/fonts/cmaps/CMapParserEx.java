/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author psoares
 */
public class CMapParserEx {
    
    private static final PdfName CMAPNAME = new PdfName("CMapName");
    private static final String DEF = "def";
    private static final String ENDCIDRANGE = "endcidrange";
    private static final String ENDCIDCHAR = "endcidchar";
    private static final String ENDBFRANGE = "endbfrange";
    private static final String ENDBFCHAR = "endbfchar";
    private static final String USECMAP = "usecmap";
    private static final int MAXLEVEL = 10;
    
    public static void parseCid(String cmapName, AbstractCMap cmap, CidLocation location) throws IOException {
        parseCid(cmapName, cmap, location, 0);
    }
    
    private static void parseCid(String cmapName, AbstractCMap cmap, CidLocation location, int level) throws IOException {
        if (level >= MAXLEVEL)
            return;
        PRTokeniser inp = location.getLocation(cmapName);
        try {
            ArrayList<PdfObject> list = new ArrayList<PdfObject>();
            PdfContentParser cp = new PdfContentParser(inp);
            while (true) {
                cp.parse(list);
                if (list.isEmpty())
                    break;
                String last = list.get(list.size() - 1).toString();
                if (level == 0 && list.size() == 3 && last.equals(DEF)) {
                    PdfObject key = list.get(0);
                    if (PdfName.REGISTRY.equals(key))
                        cmap.setRegistry(list.get(1).toString());
                    else if (PdfName.ORDERING.equals(key))
                        cmap.setOrdering(list.get(1).toString());
                    else if (CMAPNAME.equals(key))
                        cmap.setName(list.get(1).toString());
                    else if (PdfName.SUPPLEMENT.equals(key)) {
                        try {
                            cmap.setSupplement(((PdfNumber)list.get(1)).intValue());
                        }
                        catch (Exception ex) {}
                    }
                }
                else if ((last.equals(ENDCIDCHAR) || last.equals(ENDBFCHAR)) && list.size() >= 3) {
                    int lmax = list.size() - 2;
                    for (int k = 0; k < lmax; k += 2) {
                        if (list.get(k) instanceof PdfString) {
                            cmap.addChar((PdfString)list.get(k), list.get(k + 1));
                        }
                    }
                }
                else if ((last.equals(ENDCIDRANGE) || last.equals(ENDBFRANGE)) && list.size() >= 4) {
                    int lmax = list.size() - 3;
                    for (int k = 0; k < lmax; k += 3) {
                        if (list.get(k) instanceof PdfString && list.get(k + 1) instanceof PdfString) {
                            cmap.addRange((PdfString)list.get(k), (PdfString)list.get(k + 1), list.get(k + 2));
                        }
                    }
                }
                else if (last.equals(USECMAP) && list.size() == 2 && list.get(0) instanceof PdfName) {
                    parseCid(cmapName, cmap, location, level + 1);
                }
            }
        }
        finally {
            inp.close();
        }
    }
    
    private static void encodeSequence(int size, byte seqs[], char cid, ArrayList<char[]> planes) {
        --size;
        int nextPlane = 0;
        for (int idx = 0; idx < size; ++idx) {
            char plane[] = planes.get(nextPlane);
            int one = seqs[idx] & 0xff;
            char c = plane[one];
            if (c != 0 && (c & 0x8000) == 0)
                throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping"));
            if (c == 0) {
                planes.add(new char[256]);
                c = (char)(planes.size() - 1 | 0x8000);
                plane[one] = c;
            }
            nextPlane = c & 0x7fff;
        }
        char plane[] = planes.get(nextPlane);
        int one = seqs[size] & 0xff;
        char c = plane[one];
        if ((c & 0x8000) != 0)
            throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping"));
        plane[one] = cid;
    }
    
}
