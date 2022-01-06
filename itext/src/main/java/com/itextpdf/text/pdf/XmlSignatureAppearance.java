/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Pavel Alay, Bruno Lowagie, et al.
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
import com.itextpdf.text.pdf.security.XpathConstructor;
import com.itextpdf.text.pdf.security.XmlLocator;

import java.io.IOException;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

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

    /** Holds value of property xades:SigningTime. */
    private Calendar signDate;

    /** Holds value of property xades:Description. */
    private String description;

    /** Holds value of property xades:MimeType. */
    private String mimeType = "text/xml";

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Gets the signature date.
     * @return the signature date
     */
    public java.util.Calendar getSignDate() {
        if (signDate == null)
            signDate = Calendar.getInstance();
        return signDate;
    }

    /**
     * Sets the signature date.
     * @param signDate the signature date
     */
    public void setSignDate(java.util.Calendar signDate) {
        this.signDate = signDate;
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
