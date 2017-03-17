/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Dmitry Trusevich, Bruno Lowagie, et al.
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
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * PdfASmartCopy has the same functionality as PdfACopy,
 * but when resources (such as fonts, images,...) are
 * encountered, a reference to these resources is saved
 * in a cache, so that they can be reused.
 * This requires more memory, but reduces the file size
 * of the resulting PDF document.
 */
public class PdfASmartCopy extends PdfACopy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfSmartCopy.class);

    /** the cache with the streams and references. */
    private HashMap<PdfSmartCopy.ByteStore, PdfIndirectReference> streamMap = null;
    private final HashMap<RefKey, Integer> serialized = new HashMap<RefKey, Integer>();

    /**
     * Constructor
     *
     * @param document         document
     * @param os               outputstream
     * @param conformanceLevel
     */
    public PdfASmartCopy(Document document, OutputStream os, PdfAConformanceLevel conformanceLevel) throws DocumentException {
        super(document, os, conformanceLevel);
        this.streamMap = new HashMap<PdfSmartCopy.ByteStore, PdfIndirectReference>();
    }

    @Override
    protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
        PdfObject srcObj = PdfReader.getPdfObjectRelease(in);
        PdfSmartCopy.ByteStore streamKey = null;
        boolean validStream = false;
        if (srcObj.isStream()) {
            streamKey = new PdfSmartCopy.ByteStore((PRStream)srcObj, serialized);
            validStream = true;
            PdfIndirectReference streamRef = streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        }
        else if (srcObj.isDictionary()) {
            streamKey = new PdfSmartCopy.ByteStore((PdfDictionary)srcObj, serialized);
            validStream = true;
            PdfIndirectReference streamRef = streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        }

        PdfIndirectReference theRef;
        RefKey key = new RefKey(in);
        IndirectReferences iRef = indirects.get(key);
        if (iRef != null) {
            theRef = iRef.getRef();
            if (iRef.getCopied()) {
                return theRef;
            }
        } else {
            theRef = body.getPdfIndirectReference();
            iRef = new IndirectReferences(theRef);
            indirects.put(key, iRef);
        }
        if (srcObj.isDictionary()) {
            PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary)srcObj).get(PdfName.TYPE));
            if (type != null) {
                if ((PdfName.PAGE.equals(type))) {
                    return theRef;
                }
                if ((PdfName.CATALOG.equals(type))) {
                    LOGGER.warn(MessageLocalization.getComposedMessage("make.copy.of.catalog.dictionary.is.forbidden"));
                    return null;
                }
            }
        }
        iRef.setCopied();

        if (validStream) {
            streamMap.put(streamKey, theRef);
        }

        PdfObject obj = copyObject(srcObj);
        addToBody(obj, theRef);
        return theRef;
    }

    @Override
    public void freeReader(PdfReader reader) throws IOException {
        serialized.clear();
        super.freeReader(reader);
    }

    @Override
    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        if (currentPdfReaderInstance.getReader() != reader)
            serialized.clear();
        super.addPage(iPage);
    }
}
