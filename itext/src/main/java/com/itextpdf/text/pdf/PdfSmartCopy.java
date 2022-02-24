/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

/**
 * PdfSmartCopy has the same functionality as PdfCopy,
 * but when resources (such as fonts, images,...) are
 * encountered, a reference to these resources is saved
 * in a cache, so that they can be reused.
 * This requires more memory, but reduces the file size
 * of the resulting PDF document.
 */

public class PdfSmartCopy extends PdfCopy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfSmartCopy.class);

	/** the cache with the streams and references. */
    private HashMap<ByteStore, PdfIndirectReference> streamMap = null;
    private final HashMap<RefKey, Integer> serialized = new HashMap<RefKey, Integer>();

    protected Counter COUNTER = CounterFactory.getCounter(PdfSmartCopy.class);
    protected Counter getCounter() {
    	return COUNTER;
    }

    /** Creates a PdfSmartCopy instance. */
    public PdfSmartCopy(Document document, OutputStream os) throws DocumentException {
        super(document, os);
        this.streamMap = new HashMap<ByteStore, PdfIndirectReference>();
    }
    /**
     * Translate a PRIndirectReference to a PdfIndirectReference
     * In addition, translates the object numbers, and copies the
     * referenced object to the output file if it wasn't available
     * in the cache yet. If it's in the cache, the reference to
     * the already used stream is returned.
     *
     * NB: PRIndirectReferences (and PRIndirectObjects) really need to know what
     * file they came from, because each file has its own namespace. The translation
     * we do from their namespace to ours is *at best* heuristic, and guaranteed to
     * fail under some circumstances.
     */
    @Override
    protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
        PdfObject srcObj = PdfReader.getPdfObjectRelease(in);
        ByteStore streamKey = null;
        boolean validStream = false;
        if (srcObj.isStream()) {
            streamKey = new ByteStore((PRStream)srcObj, serialized);
            validStream = true;
            PdfIndirectReference streamRef = streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        }
        else if (srcObj.isDictionary()) {
            streamKey = new ByteStore((PdfDictionary)srcObj, serialized);
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

    static class ByteStore {
        private final byte[] b;
        private final int hash;
        private MessageDigest md5;

        private void serObject(PdfObject obj, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
            if (level <= 0)
                return;
            if (obj == null) {
                bb.append("$Lnull");
                return;
            }
            PdfIndirectReference ref = null;
            ByteBuffer savedBb = null;

            if (obj.isIndirect()) {
                ref = (PdfIndirectReference)obj;
                RefKey key = new RefKey(ref);
                if (serialized.containsKey(key)) {
                    bb.append(serialized.get(key));
                    return;
                }
                else {
                    savedBb = bb;
                    bb = new ByteBuffer();
                }
            }
            obj = PdfReader.getPdfObject(obj);
            if (obj.isStream()) {
                bb.append("$B");
                serDic((PdfDictionary) obj, level - 1, bb, serialized);
                if (level > 0) {
                    md5.reset();
                    bb.append(md5.digest(PdfReader.getStreamBytesRaw((PRStream)obj)));
                }
            }
            else if (obj.isDictionary()) {
                serDic((PdfDictionary)obj, level - 1, bb, serialized);
            }
            else if (obj.isArray()) {
                serArray((PdfArray)obj, level - 1, bb, serialized);
            }
            else if (obj.isString()) {
                bb.append("$S").append(obj.toString());
            }
            else if (obj.isName()) {
                bb.append("$N").append(obj.toString());
            }
            else
                bb.append("$L").append(obj.toString());

            if (savedBb != null) {
                RefKey key = new RefKey(ref);
                if (!serialized.containsKey(key))
                    serialized.put(key, calculateHash(bb.getBuffer()));
                savedBb.append(bb);
            }
        }

        private void serDic(PdfDictionary dic, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
            bb.append("$D");
            if (level <= 0)
                return;
            Object[] keys = dic.getKeys().toArray();
            Arrays.sort(keys);
            for (int k = 0; k < keys.length; ++k) {
                if(keys[k].equals(PdfName.P) &&(dic.get((PdfName)keys[k]).isIndirect() || dic.get((PdfName)keys[k]).isDictionary())) // ignore recursive call
                    continue;
                    serObject((PdfObject) keys[k], level, bb, serialized);
                    serObject(dic.get((PdfName) keys[k]), level, bb, serialized);

            }
        }

        private void serArray(PdfArray array, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
            bb.append("$A");
            if (level <= 0)
                return;
            for (int k = 0; k < array.size(); ++k) {
                serObject(array.getPdfObject(k), level, bb, serialized);
            }
        }

        ByteStore(PRStream str, HashMap<RefKey, Integer> serialized) throws IOException {
            try {
                md5 = MessageDigest.getInstance("MD5");
            }
            catch (Exception e) {
                throw new ExceptionConverter(e);
            }
            ByteBuffer bb = new ByteBuffer();
            int level = 100;
            serObject(str, level, bb, serialized);
            this.b = bb.toByteArray();
            hash = calculateHash(this.b);
            md5 = null;
        }

        ByteStore(PdfDictionary dict, HashMap<RefKey, Integer> serialized) throws IOException {
            try {
                md5 = MessageDigest.getInstance("MD5");
            }
            catch (Exception e) {
                throw new ExceptionConverter(e);
            }
            ByteBuffer bb = new ByteBuffer();
            int level = 100;
            serObject(dict, level, bb, serialized);
            this.b = bb.toByteArray();
            hash = calculateHash(this.b);
            md5 = null;
        }

        private static int calculateHash(byte[] b) {
             int hash = 0;
                int len = b.length;
                for (int k = 0; k < len; ++k)
                    hash = hash * 31 + (b[k] & 0xff);
             return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ByteStore))
                return false;
            if (hashCode() != obj.hashCode())
                return false;
            return Arrays.equals(b, ((ByteStore)obj).b);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
