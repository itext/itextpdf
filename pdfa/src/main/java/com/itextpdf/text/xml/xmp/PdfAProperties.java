package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;

public class PdfAProperties {
    /**
     * Part, always 1.
     */
    public static final String PART = "part";
    /**
     * Conformance, A, B, or U.
     */
    public static final String CONFORMANCE = "conformance";

    /**
     * Adds part.
     *
     * @param xmpMeta
     * @param part
     */
    static public void setPart(XMPMeta xmpMeta, String part) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, part);
    }

    /**
     * Adds the conformance.
     *
     * @param xmpMeta
     * @param conformance
     */
    static public void setConformance(XMPMeta xmpMeta, String conformance) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, conformance);
    }
}
