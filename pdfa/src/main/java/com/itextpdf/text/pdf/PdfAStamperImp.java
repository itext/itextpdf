/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
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
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.interfaces.PdfAConformance;
import com.itextpdf.text.pdf.interfaces.PdfIsoConformance;
import com.itextpdf.text.pdf.internal.PdfAChecker;
import com.itextpdf.text.pdf.internal.PdfAConformanceImp;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.impl.XMPMetaParser;
import com.itextpdf.xmp.properties.XMPProperty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Extension to PdfStamperImp that will attempt to keep a file
 * in conformance with the PDF/A standard.
 */
public class PdfAStamperImp extends PdfStamperImp {

    protected Counter COUNTER = CounterFactory.getCounter(PdfAStamper.class);
    XMPMeta xmpMeta = null;
    /**
     * Creates new PdfStamperImp.
     *
     * @param reader           reads the PDF
     * @param os               the output destination
     * @param pdfVersion       the new pdf version or '\0' to keep the same version as the original document
     * @param append
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException
     */
    PdfAStamperImp(PdfReader reader, OutputStream os, char pdfVersion, boolean append, PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        super(reader, os, pdfVersion, append);
        ((PdfAConformance) pdfIsoConformance).setConformanceLevel(conformanceLevel);
        PdfAWriter.setPdfVersion(this, conformanceLevel);
        readPdfAInfo();
    }

    protected void readColorProfile() {
        PdfObject outputIntents = reader.getCatalog().getAsArray(PdfName.OUTPUTINTENTS);
        if (outputIntents != null && ((PdfArray) outputIntents).size() > 0) {
            PdfStream iccProfileStream = null;
            for (int i = 0; i < ((PdfArray) outputIntents).size(); i++) {
                PdfDictionary outputIntentDictionary = ((PdfArray) outputIntents).getAsDict(i);
                if (outputIntentDictionary != null) {
                    PdfName gts = outputIntentDictionary.getAsName(PdfName.S);
                    if (iccProfileStream == null || PdfName.GTS_PDFA1.equals(gts)) {
                        iccProfileStream = outputIntentDictionary.getAsStream(PdfName.DESTOUTPUTPROFILE);
                        if (iccProfileStream != null && PdfName.GTS_PDFA1.equals(gts))
                            break;
                    }
                }
            }
            if (iccProfileStream instanceof PRStream) {
                try {
                    colorProfile = ICC_Profile.getInstance(PdfReader.getStreamBytes((PRStream)iccProfileStream));
                } catch(IOException exc) {
                    throw new ExceptionConverter(exc);
                }
            }
        }
    }

    /**
     * @see PdfStamperImp#setOutputIntents(String, String, String, String, ICC_Profile)
     */
    public void setOutputIntents(final String outputConditionIdentifier, final String outputCondition, final String registryName, final String info, final ICC_Profile colorProfile) throws IOException {
        super.setOutputIntents(outputConditionIdentifier, outputCondition, registryName, info, colorProfile);
        PdfArray a = extraCatalog.getAsArray(PdfName.OUTPUTINTENTS);
        if (a != null) {
            PdfDictionary d = a.getAsDict(0);
            if (d != null) {
                d.put(PdfName.S, PdfName.GTS_PDFA1);
            }
        }
    }

    /**
     * Always throws an exception since PDF/X conformance level cannot be set for PDF/A conformant documents.
     *
     * @param pdfx
     */
    public void setPDFXConformance(final int pdfx) {
        throw new PdfAConformanceException(MessageLocalization.getComposedMessage("pdfx.conformance.cannot.be.set.for.PdfAStamperImp.instance"));
    }

    /**
     * @see com.itextpdf.text.pdf.PdfStamperImp#getTtfUnicodeWriter()
     */
    @Override
    protected TtfUnicodeWriter getTtfUnicodeWriter() {
        if (ttfUnicodeWriter == null)
            ttfUnicodeWriter = new PdfATtfUnicodeWriter(this, ((PdfAConformance) pdfIsoConformance).getConformanceLevel());
        return ttfUnicodeWriter;
    }

    /**
     * @see PdfStamperImp#createXmpWriter(java.io.ByteArrayOutputStream, com.itextpdf.text.pdf.PdfDictionary)
     */
    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, PdfDictionary info) throws IOException {
        return new PdfAXmpWriter(baos, info, ((PdfAConformance) pdfIsoConformance).getConformanceLevel(), this);
    }

    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, HashMap<String, String> info) throws IOException {
        return new PdfAXmpWriter(baos, info, ((PdfAConformance) pdfIsoConformance).getConformanceLevel(), this);
    }

    /**
     * @see com.itextpdf.text.pdf.PdfStamperImp#initPdfIsoConformance()
     */
    protected PdfIsoConformance initPdfIsoConformance() {
        return new PdfAConformanceImp(this);
    }

    protected Counter getCounter() {
        return COUNTER;
    }

    private void readPdfAInfo() {
        byte[] metadata = null;

        XMPProperty pdfaidConformance = null;
        XMPProperty pdfaidPart = null;
        try {
            metadata = reader.getMetadata();
            xmpMeta = XMPMetaParser.parse(metadata, null);
            pdfaidConformance = xmpMeta.getProperty(XMPConst.NS_PDFA_ID, "pdfaid:conformance");
            pdfaidPart = xmpMeta.getProperty(XMPConst.NS_PDFA_ID, "pdfaid:part");
        } catch (Throwable e) {
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.documents.can.be.opened.in.PdfAStamper"));
        }
        if (pdfaidConformance == null || pdfaidPart == null) {
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.documents.can.be.opened.in.PdfAStamper"));
        }
        switch (((PdfAConformance) pdfIsoConformance).getConformanceLevel()) {
            case PDF_A_1A:
            case PDF_A_1B:
                if (!"1".equals(pdfaidPart.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.1.documents.can.be.opened.in.PdfAStamper", "1"));
                }
                break;
            case PDF_A_2A:
            case PDF_A_2B:
            case PDF_A_2U:
                if (!"2".equals(pdfaidPart.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.1.documents.can.be.opened.in.PdfAStamper", "2"));
                }
                break;
            case PDF_A_3A:
            case PDF_A_3B:
            case PDF_A_3U:
                if (!"3".equals(pdfaidPart.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.1.documents.can.be.opened.in.PdfAStamper", "3"));
                }
                break;
        }
    }

    @Override
    protected void cacheObject(PdfIndirectObject iobj) {
        getPdfAChecker().cacheObject(iobj.getIndirectReference(), iobj.object);
    }

    private PdfAChecker getPdfAChecker() {
        return ((PdfAConformanceImp)pdfIsoConformance).getPdfAChecker();
    }

    @Override
    protected void close(Map<String, String> moreInfo) throws IOException {
        super.close(moreInfo);
        getPdfAChecker().close(this);
    }

    @Override
    public PdfAnnotation createAnnotation(Rectangle rect, PdfName subtype) {
        PdfAnnotation a = super.createAnnotation(rect, subtype);
        if (!PdfName.POPUP.equals(subtype))
            a.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        return a;
    }

    @Override
    public PdfAnnotation createAnnotation(float llx, float lly, float urx, float ury, PdfString title, PdfString content, PdfName subtype) {
        PdfAnnotation a = super.createAnnotation(llx, lly, urx, ury, title, content, subtype);
        if (!PdfName.POPUP.equals(subtype))
            a.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        return a;
    }

    @Override
    public PdfAnnotation createAnnotation(float llx, float lly, float urx, float ury, PdfAction action, PdfName subtype) {
        PdfAnnotation a = super.createAnnotation(llx, lly, urx, ury, action, subtype);
        if (!PdfName.POPUP.equals(subtype))
            a.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        return a;
    }

    public XMPMeta getXmpMeta() {
        return xmpMeta;
    }
}
