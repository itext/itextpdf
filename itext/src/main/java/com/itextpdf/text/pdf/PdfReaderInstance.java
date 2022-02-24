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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.itextpdf.text.error_messages.MessageLocalization;
/**
 * Instance of PdfReader in each output document.
 *
 * @author Paulo Soares
 */
class PdfReaderInstance {
    static final PdfLiteral IDENTITYMATRIX = new PdfLiteral("[1 0 0 1 0 0]");
    static final PdfNumber ONE = new PdfNumber(1);
    int myXref[];
    PdfReader reader;
    RandomAccessFileOrArray file;
    HashMap<Integer, PdfImportedPage> importedPages = new HashMap<Integer, PdfImportedPage>();
    PdfWriter writer;
    HashSet<Integer> visited = new HashSet<Integer>();
    ArrayList<Integer> nextRound = new ArrayList<Integer>();

    PdfReaderInstance(PdfReader reader, PdfWriter writer) {
        this.reader = reader;
        this.writer = writer;
        file = reader.getSafeFile();
        myXref = new int[reader.getXrefSize()];
    }

    PdfReader getReader() {
        return reader;
    }

    PdfImportedPage getImportedPage(int pageNumber) {
        if (!reader.isOpenedWithFullPermissions())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password"));
        if (pageNumber < 1 || pageNumber > reader.getNumberOfPages())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", pageNumber));
        Integer i = Integer.valueOf(pageNumber);
        PdfImportedPage pageT = importedPages.get(i);
        if (pageT == null) {
            pageT = new PdfImportedPage(this, writer, pageNumber);
            importedPages.put(i, pageT);
        }
        return pageT;
    }

    int getNewObjectNumber(int number, int generation) {
        if (myXref[number] == 0) {
            myXref[number] = writer.getIndirectReferenceNumber();
            nextRound.add(Integer.valueOf(number));
        }
        return myXref[number];
    }

    RandomAccessFileOrArray getReaderFile() {
        return file;
    }

    PdfObject getResources(int pageNumber) {
        PdfObject obj = PdfReader.getPdfObjectRelease(reader.getPageNRelease(pageNumber).get(PdfName.RESOURCES));
        return obj;
    }

    /**
     * Gets the content stream of a page as a PdfStream object.
     * @param	pageNumber			the page of which you want the stream
     * @param	compressionLevel	the compression level you want to apply to the stream
     * @return	a PdfStream object
     * @since	2.1.3 (the method already existed without param compressionLevel)
     */
    PdfStream getFormXObject(int pageNumber, int compressionLevel) throws IOException {
        PdfDictionary page = reader.getPageNRelease(pageNumber);
        PdfObject contents = PdfReader.getPdfObjectRelease(page.get(PdfName.CONTENTS));
        PdfDictionary dic = new PdfDictionary();
        byte bout[] = null;
        if (contents != null) {
            if (contents.isStream())
                dic.putAll((PRStream)contents);
            else
                bout = reader.getPageContent(pageNumber, file);
        }
        else
            bout = new byte[0];
        dic.put(PdfName.RESOURCES, PdfReader.getPdfObjectRelease(page.get(PdfName.RESOURCES)));
        dic.put(PdfName.TYPE, PdfName.XOBJECT);
        dic.put(PdfName.SUBTYPE, PdfName.FORM);
        PdfImportedPage impPage = importedPages.get(Integer.valueOf(pageNumber));
        dic.put(PdfName.BBOX, new PdfRectangle(impPage.getBoundingBox()));
        PdfArray matrix = impPage.getMatrix();
        if (matrix == null)
            dic.put(PdfName.MATRIX, IDENTITYMATRIX);
        else
            dic.put(PdfName.MATRIX, matrix);
        dic.put(PdfName.FORMTYPE, ONE);
        PRStream stream;
        if (bout == null) {
            stream = new PRStream((PRStream)contents, dic);
        }
        else {
            stream = new PRStream(reader, bout, compressionLevel);
            stream.putAll(dic);
        }
        return stream;
    }

    void writeAllVisited() throws IOException {
        while (!nextRound.isEmpty()) {
            ArrayList<Integer> vec = nextRound;
            nextRound = new ArrayList<Integer>();
            for (int k = 0; k < vec.size(); ++k) {
                Integer i = vec.get(k);
                if (!visited.contains(i)) {
                    visited.add(i);
                    int n = i.intValue();
                    writer.addToBody(reader.getPdfObjectRelease(n), myXref[n]);
                }
            }
        }
    }

    public void writeAllPages() throws IOException {
        try {
            file.reOpen();
            for (Object element : importedPages.values()) {
                PdfImportedPage ip = (PdfImportedPage)element;
                if (ip.isToCopy()) {
                	writer.addToBody(ip.getFormXObject(writer.getCompressionLevel()), ip.getIndirectReference());
                	ip.setCopied();
                }
            }
            writeAllVisited();
        }
        finally {
            try {
// TODO: Removed - the user should be responsible for closing all PdfReaders.  But, this could cause a lot of memory leaks in code out there that hasn't been properly closing things - maybe add a finalizer to PdfReader that calls PdfReader#close() ??            	
//                reader.close();
                file.close();
            }
            catch (Exception e) {
                //Empty on purpose
            }
        }
    }
}
