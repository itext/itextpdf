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

import com.itextpdf.text.DocListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.TempFileCache;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.interfaces.PdfAConformance;
import com.itextpdf.text.pdf.interfaces.PdfIsoConformance;
import com.itextpdf.text.pdf.internal.PdfAChecker;
import com.itextpdf.text.pdf.internal.PdfAConformanceImp;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.text.xml.xmp.XmpWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Extension of PdfWriter that will attempt to create a PDF/A file
 * instead of an ordinary PDF file.
 * @see PdfWriter
 */
public class PdfAWriter extends PdfWriter {

    public static String MimeTypePdf         = "application/pdf";
    public static String MimeTypeOctetStream = "application/octet-stream";
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

    /**
     * Always throws an exception since PDF/X conformance level cannot be set for PDF/A conformant documents.
     * @param pdfx
     */
    public void setPDFXConformance(final int pdfx) {
        throw new PdfXConformanceException(MessageLocalization.getComposedMessage("pdfx.conformance.cannot.be.set.for.PdfAWriter.instance"));
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
    @Override
    protected TtfUnicodeWriter getTtfUnicodeWriter() {
        if (ttfUnicodeWriter == null)
            ttfUnicodeWriter = new PdfATtfUnicodeWriter(this, ((PdfAConformance)pdfIsoConformance).getConformanceLevel());
        return ttfUnicodeWriter;
    }

    /**
     * @see PdfWriter#createXmpWriter(java.io.ByteArrayOutputStream, com.itextpdf.text.pdf.PdfDictionary)
     */
    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, PdfDictionary info) throws IOException {
        return xmpWriter = new PdfAXmpWriter(baos, info, ((PdfAConformance)pdfIsoConformance).getConformanceLevel(), this);
    }

    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, HashMap<String, String> info) throws IOException {
        return xmpWriter = new PdfAXmpWriter(baos, info, ((PdfAConformance)pdfIsoConformance).getConformanceLevel(), this);
    }

    /**
     * @see com.itextpdf.text.pdf.PdfWriter#initPdfIsoConformance()
     */
    protected PdfIsoConformance initPdfIsoConformance() {
        return new PdfAConformanceImp(this);
    }

	protected Counter COUNTER = CounterFactory.getCounter(PdfAWriter.class);
	protected Counter getCounter() {
		return COUNTER;
	}

    @Override
    protected void cacheObject(PdfIndirectObject iobj) {
        getPdfAChecker().cacheObject(iobj.getIndirectReference(), iobj.object);
    }

    private PdfAChecker getPdfAChecker() {
        return ((PdfAConformanceImp)pdfIsoConformance).getPdfAChecker();
    }

    /**
     * Use this method to add a file attachment at the document level.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @param mimeType mime type of the file
     * @param afRelationshipValue AFRelationship key value, @see AFRelationshipValue. If <CODE>null</CODE>, @see AFRelationshipValue.Unspecified will be added.
     * @param fileParameter the optional extra file parameters such as the creation or modification date 
     * @return the file specification
     * @throws IOException on error
     */
    public PdfFileSpecification addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay,
                                  String mimeType, PdfName afRelationshipValue, PdfDictionary fileParameter) throws IOException {
        PdfFileSpecification pdfFileSpecification = PdfFileSpecification.fileEmbedded(this, file, fileDisplay,
                fileStore, mimeType, fileParameter, PdfStream.BEST_COMPRESSION);

        if (afRelationshipValue != null)
            pdfFileSpecification.put(PdfName.AFRELATIONSHIP, afRelationshipValue);
        else
            pdfFileSpecification.put(PdfName.AFRELATIONSHIP, AFRelationshipValue.Unspecified);

        addFileAttachment(description, pdfFileSpecification);
        return pdfFileSpecification;
    }

    /**
     * Use this method to add a file attachment at the document level.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @param mimeType mime type of the file
     * @param afRelationshipValue AFRelationship key value, @see AFRelationshipValue. If <CODE>null</CODE>, @see AFRelationshipValue.Unspecified will be added.
     * @return the file specification
     * @throws IOException on error
     */
    public PdfFileSpecification addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay,
                                  String mimeType, PdfName afRelationshipValue) throws IOException {
        return addFileAttachment(description, fileStore, file, fileDisplay, mimeType, afRelationshipValue, null);
    }

    /**
     * Use this method to add a file attachment at the document level. Adds @see MimeTypeOctetStream as mime type.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @param afRelationshipValue AFRelationship key value, @see AFRelationshipValue. If <CODE>null</CODE>, @see AFRelationshipValue.Unspecified will be added.
     *
     * @throws IOException on error
     */
    public void addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay,
                                  PdfName afRelationshipValue) throws IOException {
        addFileAttachment(description, fileStore, file, fileDisplay, MimeTypeOctetStream, afRelationshipValue);
    }

    /**
     * Use this method to add a file attachment at the document level. Adds @see MimeTypeOctetStream as mime type and @see AFRelationshipValue.Unspecified as AFRelationship.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @throws IOException on error
     */
    @Override
    public void addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay)
            throws IOException {
        addFileAttachment(description, fileStore, file, fileDisplay, AFRelationshipValue.Unspecified);
    }

    /**
     * Use this method to add a file attachment at the document level.  Adds @see MimeTypePdf as mime type and @see AFRelationshipValue.Unspecified as AFRelationship.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @throws IOException on error
     */
    public void addPdfAttachment(String description, byte[] fileStore, String file, String fileDisplay)
            throws IOException {
        addPdfAttachment(description, fileStore, file, fileDisplay, AFRelationshipValue.Unspecified);
    }

    /**
     * Use this method to add a file attachment at the document level. Adds @see MimeTypePdf as mime type.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @param afRelationshipValue AFRelationship key value, <see>AFRelationshipValue</see>. If <CODE>null</CODE>, @see AFRelationshipValue.Unspecified will be added.
     *
     * @throws IOException on error
     */
    public void addPdfAttachment(String description, byte[] fileStore, String file, String fileDisplay, PdfName afRelationshipValue) throws IOException {
        addFileAttachment(description, fileStore, file, fileDisplay, MimeTypePdf, afRelationshipValue);
    }

    @Override
    public void close() {
        super.close();
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

    public void useExternalCacheForPdfA(TempFileCache fileCache) {
        if (pdfIsoConformance instanceof PdfAConformanceImp) {
            ((PdfAConformanceImp) pdfIsoConformance).getPdfAChecker().useExternalCache(fileCache);
        }
    }
}
