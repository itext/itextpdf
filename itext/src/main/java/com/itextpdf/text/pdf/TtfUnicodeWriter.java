package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TtfUnicodeWriter {

    protected PdfWriter writer = null;

    public TtfUnicodeWriter(PdfWriter writer) {
        this.writer = writer;
    }

    public void writeFont(TrueTypeFontUnicode font, PdfIndirectReference ref, Object params[], byte[] rotbits) throws DocumentException, IOException {
        HashMap<Integer, int[]> longTag = (HashMap<Integer, int[]>)params[0];
        font.addRangeUni(longTag, true, font.subset);
        int metrics[][] = longTag.values().toArray(new int[0][]);
        Arrays.sort(metrics, font);
        PdfIndirectReference ind_font = null;
        PdfObject pobj = null;
        PdfIndirectObject obj = null;
        PdfIndirectReference cidset = null;
        // sivan: cff
        if (font.cff) {
            byte b[] = font.readCffFont();
            if (font.subset || font.subsetRanges != null) {
                CFFFontSubset cff = new CFFFontSubset(new RandomAccessFileOrArray(b),longTag);
                b = cff.Process(cff.getNames()[0]);
            }
            pobj = new BaseFont.StreamFont(b, "CIDFontType0C", font.compressionLevel);
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        } else {
            byte[] b;
            if (font.subset || font.directoryOffset != 0) {
                TrueTypeFontSubSet sb = new TrueTypeFontSubSet(font.fileName, new RandomAccessFileOrArray(font.rf), new HashSet<Integer>(longTag.keySet()), font.directoryOffset, false, false);
                b = sb.process();
            }
            else {
                b = font.getFullFont();
            }
            int lengths[] = new int[]{b.length};
            pobj = new BaseFont.StreamFont(b, lengths, font.compressionLevel);
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        String subsetPrefix = "";
        if (font.subset)
            subsetPrefix = font.createSubsetPrefix();
        PdfDictionary dic = font.getFontDescriptor(ind_font, subsetPrefix, cidset);
        obj = writer.addToBody(dic);
        ind_font = obj.getIndirectReference();

        pobj = font.getCIDFontType2(ind_font, subsetPrefix, metrics);
        obj = writer.addToBody(pobj);
        ind_font = obj.getIndirectReference();

        pobj = font.getToUnicode(metrics);
        PdfIndirectReference toUnicodeRef = null;

        if (pobj != null) {
            obj = writer.addToBody(pobj);
            toUnicodeRef = obj.getIndirectReference();
        }

        pobj = font.getFontBaseType(ind_font, subsetPrefix, toUnicodeRef);
        writer.addToBody(pobj, ref);
    }


}
