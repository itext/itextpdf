package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.security.XpathConstructor;
import com.itextpdf.text.pdf.security.XmlLocator;

import java.io.IOException;
import java.security.cert.Certificate;

public class XmlSignatureAppearance {

    /**
     * Constructs XmlSignatureAppearance object.
     * @param writer the writer to which the signature will be written.
     */
    XmlSignatureAppearance(PdfStamperImp writer) {
        this.writer = writer;
    }

    private PdfStamperImp writer;
    private PdfStamper stamper;
    private Certificate signCertificate;
    private XmlLocator xmlLocator;
    private XpathConstructor xpathConstructor;

    public PdfStamperImp getWriter() {
        return writer;
    }

    public PdfStamper getStamper() {
        return stamper;
    }

    public void setStamper(PdfStamper stamper) {
        this.stamper = stamper;
    }

    /**
     * Sets the certificate used to provide the text in the appearance.
     * This certificate doesn't take part in the actual signing process.
     * @param signCertificate the certificate
     */
    public void setCertificate(Certificate signCertificate) {
        this.signCertificate = signCertificate;
    }

    public Certificate getCertificate() {
        return signCertificate;
    }

    /**
     * Helps to locate xml stream
     * @return XmlLocator, cannot be null.
     */
    public XmlLocator getXmlLocator() {
        return xmlLocator;
    }


    public void setXmlLocator(XmlLocator xmlLocator) {
        this.xmlLocator = xmlLocator;
    }

    /**
     * Constructor for xpath expression in case signing only part of XML document.
     * @return XpathConstructor, can be null
     */
    public XpathConstructor getXpathConstructor() {
        return xpathConstructor;
    }

    public void setXpathConstructor(XpathConstructor xpathConstructor) {
        this.xpathConstructor = xpathConstructor;
    }

    /**
     * Close PdfStamper
     * @throws IOException
     * @throws DocumentException
     */
    public void close() throws IOException, DocumentException {
        writer.close(stamper.getMoreInfo());
    }
}
