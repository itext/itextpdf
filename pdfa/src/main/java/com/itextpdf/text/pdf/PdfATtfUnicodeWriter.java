/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Alexander Chingarev, Bruno Lowagie, et al.
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

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @see TtfUnicodeWriter
 */
public class PdfATtfUnicodeWriter extends TtfUnicodeWriter {
    final protected PdfAConformanceLevel pdfAConformanceLevel;

    /**
     * @see TtfUnicodeWriter#TtfUnicodeWriter(PdfWriter)
     */
    public PdfATtfUnicodeWriter(PdfWriter writer, PdfAConformanceLevel pdfAConformanceLevel) {
        super(writer);
        this.pdfAConformanceLevel = pdfAConformanceLevel;
    }

    /**
     * @see TtfUnicodeWriter#writeFont(TrueTypeFontUnicode, PdfIndirectReference, Object[], byte[])
     */
    public void writeFont(TrueTypeFontUnicode font, PdfIndirectReference ref, Object params[], byte[] rotbits) throws DocumentException, IOException {
        @SuppressWarnings("unchecked")
		HashMap<Integer, int[]> longTag = (HashMap<Integer, int[]>)params[0];
        font.addRangeUni(longTag, true, font.subset);
        int metrics[][] = longTag.values().toArray(new int[0][]);
        Arrays.sort(metrics, font);
        PdfIndirectReference ind_font = null;
        PdfObject pobj = null;
        PdfIndirectObject obj = null;
        PdfIndirectReference cidset = null;
        if (pdfAConformanceLevel == PdfAConformanceLevel.PDF_A_1A || pdfAConformanceLevel == PdfAConformanceLevel.PDF_A_1B) {
            PdfStream stream;
            if (metrics.length == 0) {
                stream = new PdfStream(new byte[]{(byte)0x80});
            } else {
                int top = metrics[metrics.length - 1][0];
                byte[] bt = new byte[top / 8 + 1];
                for (int k = 0; k < metrics.length; ++k) {
                    int v = metrics[k][0];
                    bt[v / 8] |= rotbits[v % 8];
                }
                stream = new PdfStream(bt);
                stream.flateCompress(font.compressionLevel);
            }
            cidset = writer.addToBody(stream).getIndirectReference();
        }
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
            subsetPrefix = BaseFont.createSubsetPrefix();
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
