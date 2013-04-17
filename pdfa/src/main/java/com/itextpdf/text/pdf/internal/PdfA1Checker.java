package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

public class PdfA1Checker extends PdfAChecker {

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
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
            }
        } else {
            if (!bf.isEmbedded())
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
        }
    }

    @Override
    protected void checkImage(PdfWriter writer, int key, Object obj1) {
        PdfImage image = (PdfImage) obj1;
        if (image.get(PdfName.SMASK) != null)
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images"));
    }

    @Override
    protected void checkGState(PdfWriter writer, int key, Object obj1) {
        PdfDictionary gs = (PdfDictionary) obj1;
        PdfObject obj = gs.get(PdfName.BM);
        if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
        obj = gs.get(PdfName.CA);
        double v = 0.0;
        if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0)
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
        obj = gs.get(PdfName.ca);
        v = 0.0;
        if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0)
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
    }

    @Override
    protected void checkLayer(PdfWriter writer, int key, Object obj1) {
        throw new PdfAConformanceException(MessageLocalization.getComposedMessage("layers.are.not.allowed"));
    }

}
