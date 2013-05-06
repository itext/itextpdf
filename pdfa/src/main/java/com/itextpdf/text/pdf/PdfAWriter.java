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
package com.itextpdf.text.pdf;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.interfaces.PdfAConformance;
import com.itextpdf.text.pdf.interfaces.PdfIsoConformance;
import com.itextpdf.text.pdf.internal.PdfAConformanceImp;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.text.xml.xmp.XmpWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Extension of PdfWriter that will attempt to create a PDF/A file
 * instead of an ordinary PDF file.
 * @see PdfWriter
 */
public class PdfAWriter extends PdfWriter {
	
    /**
     * Use this method to get an instance of the <CODE>PdfWriter</CODE>.
     * @param	document	The <CODE>Document</CODE> that has to be written
     * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @return	a new <CODE>PdfWriter</CODE>
     * @throws	DocumentException on error
     */
    public static PdfAWriter getInstance(final Document document, final OutputStream os, final PdfAConformanceLevel conformanceLevel)
    throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener(pdf);
        PdfAWriter writer = new PdfAWriter(pdf, os, conformanceLevel);
        pdf.addWriter(writer);
        return writer;
    }

    /**
     * Use this method to get an instance of the <CODE>PdfWriter</CODE>.
     * @param	document	The <CODE>Document</CODE> that has to be written
     * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
     * @param listener A <CODE>DocListener</CODE> to pass to the PdfDocument.
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @return	a new <CODE>PdfWriter</CODE>
     * @throws	DocumentException on error
     */
    public static PdfAWriter getInstance(final Document document, final OutputStream os, final DocListener listener, final PdfAConformanceLevel conformanceLevel)
    throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        pdf.addDocListener(listener);
        document.addDocListener(pdf);
        PdfAWriter writer = new PdfAWriter(pdf, os, conformanceLevel);
        pdf.addWriter(writer);
        return writer;
    }

    /**
     * Sets the Confomance level.
     * @param writer
     * @param conformanceLevel
     */
    static public void setPdfVersion(PdfWriter writer, PdfAConformanceLevel conformanceLevel) {
        switch (conformanceLevel) {
            case PDF_A_1A:
            case PDF_A_1B:
                writer.setPdfVersion(VERSION_1_4);
                break;
            case PDF_A_2A:
            case PDF_A_2B:
            case PDF_A_2U:
                writer.setPdfVersion(VERSION_1_7);
                break;
            case PDF_A_3A:
            case PDF_A_3B:
            case PDF_A_3U:
                writer.setPdfVersion(VERSION_1_7);
                break;
            default:
                writer.setPdfVersion(VERSION_1_4);
                break;
        }
    }

    /**
     * @see PdfWriter#setOutputIntents(String, String, String, String, ICC_Profile)
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
     * @param pdfx
     */
    public void setPDFXConformance(final int pdfx) {
        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("pdfx.conformance.cannot.be.set.for.PdfAWriter.instance"));
    }

    /**
     * @see com.itextpdf.text.pdf.PdfWriter#isPdfIso()
     */
    public boolean isPdfIso() {
        return pdfIsoConformance.isPdfIso();
    }

    /**
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     */
    protected PdfAWriter(PdfAConformanceLevel conformanceLevel) {
        super();
        ((PdfAConformance)pdfIsoConformance).setConformanceLevel(conformanceLevel);
        setPdfVersion(this, conformanceLevel);
    }

    /**
     * Constructs a <CODE>PdfAWriter</CODE>.
     * <P>
     * Remark: a PdfAWriter can only be constructed by calling the method <CODE>getInstance(Document document, OutputStream os, PdfAconformanceLevel conformanceLevel)</CODE>.
     * @param document the <CODE>PdfDocument</CODE> that has to be written
     * @param os the <CODE>OutputStream</CODE> the writer has to write to
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     */
    protected PdfAWriter(final PdfDocument document, final OutputStream os, final PdfAConformanceLevel conformanceLevel) {
        super(document, os);
        ((PdfAConformance)pdfIsoConformance).setConformanceLevel(conformanceLevel);
        setPdfVersion(this, conformanceLevel);
    }

    /**
     * @see com.itextpdf.text.pdf.PdfWriter#getTtfUnicodeWriter()
     */
    protected TtfUnicodeWriter getTtfUnicodeWriter() {
        if (ttfUnicodeWriter == null)
            ttfUnicodeWriter = new PdfATtfUnicodeWriter(this);
        return ttfUnicodeWriter;
    }

    /**
     * @see PdfWriter#getXmpWriter(java.io.ByteArrayOutputStream, com.itextpdf.text.pdf.PdfDictionary)
     */
    protected XmpWriter getXmpWriter(ByteArrayOutputStream baos, PdfDictionary info) throws IOException {
        if (xmpWriter == null)
            xmpWriter = new PdfAXmpWriter(baos, info, ((PdfAConformance)pdfIsoConformance).getConformanceLevel());
        return xmpWriter;
    }

    /**
     * @see PdfWriter#checkPdfIsoConformance(int, Object)
     */
    protected void checkPdfIsoConformance(int key, Object obj1) {
        ((PdfAConformanceImp)pdfIsoConformance).checkPdfAConformance(this, key, obj1);
    }

    /**
     * @see com.itextpdf.text.pdf.PdfWriter#getPdfIsoConformance()
     */
    protected PdfIsoConformance getPdfIsoConformance() {
        return new PdfAConformanceImp();
    }

	protected Counter COUNTER = CounterFactory.getCounter(PdfAWriter.class);
	protected Counter getCounter() {
		return COUNTER;
	}
}
