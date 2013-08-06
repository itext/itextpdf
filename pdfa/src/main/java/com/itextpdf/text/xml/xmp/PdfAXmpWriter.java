/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.pdf.*;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Subclass of XmpWriter that adds info about the PDF/A level.
 * @see XmpWriter
 */
public class PdfAXmpWriter extends XmpWriter {

    /**
     * Creates and XMP writer that adds info about the PDF/A conformance level.
     * @param os
     * @param conformanceLevel
     * @throws IOException
     */
    public PdfAXmpWriter(OutputStream os, PdfAConformanceLevel conformanceLevel) throws IOException {
        super(os);
        try {
            addRdfDescription(conformanceLevel);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc);
        }
    }

    /**
     * Creates and XMP writer that adds info about the PDF/A conformance level.
     * @param os
     * @param info
     * @param conformanceLevel
     * @throws IOException
     */
    public PdfAXmpWriter(OutputStream os, PdfDictionary info, PdfAConformanceLevel conformanceLevel) throws IOException {
        super(os, info);
        try {
            addRdfDescription(conformanceLevel);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc);
        }
    }

    /**
     * Creates and XMP writer that adds info about the PDF/A conformance level.
     * @param os
     * @param info
     * @param conformanceLevel
     * @throws IOException
     */
    public PdfAXmpWriter(OutputStream os, Map<String, String> info, PdfAConformanceLevel conformanceLevel) throws IOException {
        super(os, info);
        try {
            addRdfDescription(conformanceLevel);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc);
        }
    }

    /**
     * Adds information about the PDF/A conformance level to the XMP metadata.
     * @param conformanceLevel
     * @throws IOException
     */
    private void addRdfDescription(PdfAConformanceLevel conformanceLevel) throws XMPException {
        switch (conformanceLevel) {
            case PDF_A_1A:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "1");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "A");
                break;
            case PDF_A_1B:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "1");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "B");
                break;
            case PDF_A_2A:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "2");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "A");
                break;
            case PDF_A_2B:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "2");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "B");
                break;
            case PDF_A_2U:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "2");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "U");
                break;
            case PDF_A_3A:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "3");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "A");
                break;
            case PDF_A_3B:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "3");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "B");
                break;
            case PDF_A_3U:
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.PART, "3");
                xmpMeta.setProperty(XMPConst.NS_PDFA_ID, PdfAProperties.CONFORMANCE, "U");
                break;
            default:
                break;
        }
    }
}
