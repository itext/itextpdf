package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Extension of PdfStamper that will attempt to keep a file
 * in conformance with the PDF/A standard.
 * @see PdfStamper
 */
public class PdfAStamper extends PdfStamper {

    /**
     * Starts the process of adding extra content to an existing PDF document keeping the document PDF/A conformant.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException or error
     */
    public PdfAStamper(final PdfReader reader, final OutputStream os, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        stamper = new PdfAStamperImp(reader, os, '\0', false, conformanceLevel);
    }

    /**
     * Starts the process of adding extra content to an existing PDF document keeping the document PDF/A conformant.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original document
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException or error
     */
    public PdfAStamper(final PdfReader reader, final OutputStream os, final char pdfVersion, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        stamper = new PdfAStamperImp(reader, os, pdfVersion, false, conformanceLevel);
    }

    /**
     * Starts the process of adding extra content to an existing PDF document keeping the document PDF/A conformant.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original document
     * @param append if <CODE>true</CODE> appends the document changes as a new revision. This is only useful for multiple signatures as nothing is gained in speed or memory
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException or error
     */
    public PdfAStamper(final PdfReader reader, final OutputStream os, final char pdfVersion, final boolean append, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        stamper = new PdfAStamperImp(reader, os, pdfVersion, append, conformanceLevel);
    }

}
