package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.util.Arrays;
import java.util.HashSet;

public class PdfA2Checker extends PdfA1Checker {

    static private HashSet<PdfName> allowedBlendModes = new HashSet<PdfName>(Arrays.asList(new PdfName[] {PdfGState.BM_NORMAL, PdfGState.BM_COMPATIBLE,
            PdfGState.BM_MULTIPLY, PdfGState.BM_SCREEN, PdfGState.BM_OVERLAY, PdfGState.BM_DARKEN, PdfGState.BM_LIGHTEN, PdfGState.BM_COLORDODGE,
            PdfGState.BM_COLORBURN, PdfGState.BM_HARDLIGHT, PdfGState.BM_SOFTLIGHT, PdfGState.BM_DIFFERENCE, PdfGState.BM_EXCLUSION}));

    @Override
    protected void checkGState(PdfWriter writer, int key, Object obj1) {
        PdfDictionary gs = (PdfDictionary) obj1;
        PdfObject obj = gs.get(PdfName.BM);
        if (obj != null && !allowedBlendModes.contains(obj)) {
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
        }
    }

}
