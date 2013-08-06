package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;

public class PdfProperties {
    /** Keywords. */
    public static final String KEYWORDS = "Keywords";
    /** The PDF file version (for example: 1.0, 1.3, and so on). */
    public static final String VERSION = "PDFVersion";
    /** The Producer. */
    public static final String PRODUCER = "Producer";

    /**
     * Adds keywords.
     *
     * @param xmpMeta
     * @param keywords
     */
    static public void setKeywords(XMPMeta xmpMeta, String keywords) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_PDF, KEYWORDS, keywords);
    }

    /**
     * Adds the producer.
     *
     * @param xmpMeta
     * @param producer
     */
    static public void setProducer(XMPMeta xmpMeta, String producer) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_PDF, PRODUCER, producer);
    }

    /**
     * Adds the version.
     *
     * @param xmpMeta
     * @param version
     */
    static public void setVersion(XMPMeta xmpMeta, String version) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_PDF, VERSION, version);
    }
}
