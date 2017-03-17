/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Pavel Alay, et al.
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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

/**
 * Extension of PdfCopy that will attempt to keep a file
 * in conformance with the PDF/A standard.
 * @see PdfCopy
 */
public class PdfACopy extends PdfCopy {
    /**
     * Constructor
     *
     * @param document document
     * @param os       outputstream
     */
    public PdfACopy(Document document, OutputStream os, PdfAConformanceLevel conformanceLevel) throws DocumentException {
        super(document, os);
        ((PdfAConformance)pdfIsoConformance).setConformanceLevel(conformanceLevel);
        PdfAWriter.setPdfVersion(this, conformanceLevel);
    }

    protected Counter COUNTER = CounterFactory.getCounter(PdfACopy.class);
    protected Counter getCounter() {
        return COUNTER;
    }

    @Override
    protected PdfIsoConformance initPdfIsoConformance() {
        return new PdfAConformanceImp(this);
    }

    @Override
    protected void cacheObject(PdfIndirectObject iobj) {
        super.cacheObject(iobj);
        getPdfAChecker().cacheObject(iobj.getIndirectReference(), iobj.object);
    }

    private PdfAChecker getPdfAChecker() {
        return ((PdfAConformanceImp)pdfIsoConformance).getPdfAChecker();
    }

    @Override
    public void addDocument(PdfReader reader) throws DocumentException, IOException {
        checkPdfAInfo(reader);
        super.addDocument(reader);
    }

    @Override
    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        checkPdfAInfo(iPage.readerInstance.getReader());
        super.addPage(iPage);
    }

    @Override
    public PageStamp createPageStamp(PdfImportedPage iPage) {
        checkPdfAInfo(iPage.readerInstance.getReader());
        return super.createPageStamp(iPage);
    }

    @Override
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
     * Copies the output intent dictionary from other document to this one.
     * @param reader the other document
     * @param checkExistence <CODE>true</CODE> to just check for the existence of a valid output intent
     * dictionary, <CODE>false</CODE> to insert the dictionary if it exists
     * @throws IOException on error
     * @return <CODE>true</CODE> if the output intent dictionary exists, <CODE>false</CODE>
     * otherwise
     */
    @Override
    public boolean setOutputIntents(PdfReader reader, boolean checkExistence) throws IOException {
        PdfDictionary catalog = reader.catalog;
        PdfArray outs = catalog.getAsArray(PdfName.OUTPUTINTENTS);
        if (outs == null)
            return false;
        if (outs.size() == 0)
            return false;
        PdfDictionary outa = outs.getAsDict(0);
        PdfObject obj = PdfReader.getPdfObject(outa.get(PdfName.S));
        if (obj == null || !PdfName.GTS_PDFA1.equals(obj))
            return false;
        if (checkExistence)
            return true;
        PRStream stream = (PRStream) PdfReader.getPdfObject(outa.get(PdfName.DESTOUTPUTPROFILE));
        byte[] destProfile = null;
        if (stream != null) {
            destProfile = PdfReader.getStreamBytes(stream);
        }
        setOutputIntents(getNameString(outa, PdfName.OUTPUTCONDITIONIDENTIFIER), getNameString(outa, PdfName.OUTPUTCONDITION),
                getNameString(outa, PdfName.REGISTRYNAME), getNameString(outa, PdfName.INFO), destProfile);
        return true;
    }

    @Override
    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, PdfDictionary info) throws IOException {
        return new PdfAXmpWriter(baos, info, ((PdfAConformance) pdfIsoConformance).getConformanceLevel(), this);
    }

    @Override
    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, HashMap<String, String> info) throws IOException {
        return new PdfAXmpWriter(baos, info, ((PdfAConformance) pdfIsoConformance).getConformanceLevel(), this);
    }

    /**
     * @see com.itextpdf.text.pdf.PdfWriter#getTtfUnicodeWriter()
     */
    @Override
    protected TtfUnicodeWriter getTtfUnicodeWriter() {
        if (ttfUnicodeWriter == null)
            ttfUnicodeWriter = new PdfATtfUnicodeWriter(this, ((PdfAConformance)pdfIsoConformance).getConformanceLevel());
        return ttfUnicodeWriter;
    }

    @Override
    public void close() {
        super.close();
        getPdfAChecker().close(this);
    }

    private void checkPdfAInfo(PdfReader reader) {
        byte[] metadata;
        XMPMeta xmpMeta;
        XMPProperty pdfaidConformance;
        XMPProperty pdfaidPart;
        try {
            metadata = reader.getMetadata();
            xmpMeta = XMPMetaParser.parse(metadata, null);
            pdfaidConformance = xmpMeta.getProperty(XMPConst.NS_PDFA_ID, "pdfaid:conformance");
            pdfaidPart = xmpMeta.getProperty(XMPConst.NS_PDFA_ID, "pdfaid:part");
        } catch (Throwable e) {
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.documents.can.be.added.in.PdfACopy"));
        }
        if (pdfaidConformance == null || pdfaidPart == null) {
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.pdfa.documents.can.be.added.in.PdfACopy"));
        }

        switch (((PdfAConformance) pdfIsoConformance).getConformanceLevel()) {
            case PDF_A_1A:
            case PDF_A_1B:
                if (!"1".equals(pdfaidPart.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("different.pdf.a.version", "1"));
                }
                break;
            case PDF_A_2A:
            case PDF_A_2B:
            case PDF_A_2U:
                if (!"2".equals(pdfaidPart.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("different.pdf.a.version", "2"));
                }
                break;
            case PDF_A_3A:
            case PDF_A_3B:
            case PDF_A_3U:
            case ZUGFeRD:
                if (!"3".equals(pdfaidPart.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("different.pdf.a.version", "3"));
                }
                break;
        }

        switch (((PdfAConformance) pdfIsoConformance).getConformanceLevel()) {
            case PDF_A_1A:
            case PDF_A_2A:
            case PDF_A_3A:
                if (!"A".equals(pdfaidConformance.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("incompatible.pdf.a.conformance.level", "a"));
                }
                break;
            case PDF_A_2U:
            case PDF_A_3U:
                if ("B".equals(pdfaidConformance.getValue())) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("incompatible.pdf.a.conformance.level", "u"));
                }
                break;

        }
    }
}
