/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Alexander Chingarev, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

public class PdfA1Checker extends PdfAChecker {

    public final double maxRealValue = 32767;
    public final int maxStringLength = 65535;
    public final int maxArrayLength = 8191;
    public final int maxDictionaryLength = 4095;
    public final int maxGsStackDepth = 28;
    protected int gsStackDepth = 0;

    @Override
    protected void checkFont(PdfWriter writer, int key, Object obj1) {
        BaseFont bf = (BaseFont) obj1;
        if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
            PdfStream prs = null;
            PdfDictionary fontDictionary = ((DocumentFont) bf).getFontDictionary();
            PdfDictionary fontDescriptor = fontDictionary.getAsDict(PdfName.FONTDESCRIPTOR);
            if (fontDescriptor != null) {
                prs = fontDescriptor.getAsStream(PdfName.FONTFILE);
                if (prs == null) {
                    prs = fontDescriptor.getAsStream(PdfName.FONTFILE2);
                }
                if (prs == null) {
                    prs = fontDescriptor.getAsStream(PdfName.FONTFILE3);
                }
            }
            if (prs == null) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
            }
        } else {
            if (!bf.isEmbedded())
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
        }
    }

    @Override
    protected void checkImage(PdfWriter writer, int key, Object obj1) {
        PdfImage image = (PdfImage) obj1;
        if (image.get(PdfName.SMASK) != null)
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images"));
    }

    @Override
    protected void checkGState(PdfWriter writer, int key, Object obj1) {
        PdfDictionary gs = (PdfDictionary) obj1;
        PdfObject obj = gs.get(PdfName.BM);
        if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
        obj = gs.get(PdfName.CA);
        double v = 0.0;
        if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0)
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
        obj = gs.get(PdfName.ca);
        v = 0.0;
        if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0)
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
    }

    @Override
    protected void checkLayer(PdfWriter writer, int key, Object obj1) {
        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("layers.are.not.allowed"));
    }

    @Override
    protected void checkTrailer(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfWriter.PdfTrailer) {
            PdfWriter.PdfTrailer trailer = (PdfWriter.PdfTrailer) obj1;
            if (trailer.get(PdfName.ENCRYPT) != null) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("keyword.encrypt.shall.not.be.used.in.the.trailer.dictionary"));
            }
        }
    }

    @Override
    protected void checkStream(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfStream) {
            PdfStream stream = (PdfStream) obj1;
            if (stream.contains(PdfName.F) || stream.contains(PdfName.FFILTER) || stream.contains(PdfName.FDECODEPARMS)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("stream.object.dictionary.shall.not.contain.the.f.ffilter.or.fdecodeparams.keys"));
            }
            if (stream.contains(PdfName.LZWDECODE)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
            }
        }
    }

    @Override
    protected void checkFileSpec(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfFileSpecification) {
            PdfFileSpecification fileSpec = (PdfFileSpecification) obj1;
            if (fileSpec.contains(PdfName.EF)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("embedded.files.are.not.permitted"));
            }
        }
    }

    @Override
    protected void checkPdfObject(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfNumber) {
            PdfNumber number = (PdfNumber) obj1;
            if (Math.abs(number.doubleValue()) > maxRealValue && number.toString().contains(".")) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("real.number.is.out.of.range"));
            }
        } else if (obj1 instanceof PdfString) {
            PdfString string = (PdfString) obj1;
            if (string.getBytes().length > maxStringLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.string.is.too.long"));
            }
        } else if (obj1 instanceof PdfArray) {
            PdfArray array = (PdfArray) obj1;
            if (array.size() > maxArrayLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.array.is.out.of.bounds"));
            }
        } else if (obj1 instanceof PdfDictionary) {
            PdfDictionary dictionary = (PdfDictionary) obj1;
            if (dictionary.size() > maxDictionaryLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.dictionary.is.out.of.bounds"));
            }
        }
    }

    @Override
    protected void checkCanvas(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof String) {
            if ("q".equals(obj1)) {
                if (++gsStackDepth > maxGsStackDepth)
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("graphics.state.stack.depth.is.greater.than.28"));
            } else if ("Q".equals(obj1)) {
                gsStackDepth--;
            }
        }
    }


}
