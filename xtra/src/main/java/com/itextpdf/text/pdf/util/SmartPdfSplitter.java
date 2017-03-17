/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Splits a PDF based on a given file size.
 */
public class SmartPdfSplitter {

    private final Logger LOGGER = LoggerFactory.getLogger(SmartPdfSplitter.class);
    
    protected PdfReader reader;
    protected int numberOfPages = 0;
    protected int currentPage = 1;
    protected boolean overSized = false;
    
    public SmartPdfSplitter(PdfReader reader) throws IOException {
        this.reader = reader;
        reader.setAppendable(true);
        numberOfPages = reader.getNumberOfPages();
        LOGGER.info(String.format("Creating a splitter for a document with %s pages", numberOfPages));
    }
    
    public boolean hasMorePages() {
        return currentPage <= numberOfPages;
    }
    
    public boolean isOverSized() {
        return overSized;
    }
    
    public boolean split(OutputStream os, long sizeInBytes) throws IOException, DocumentException {
        if (!hasMorePages()) {
            os.close();
            return false;
        }
        overSized = false;
        Document document = new Document();
        PdfCopy copy = new PdfSmartCopy(document, os);
        document.open();
        boolean hasPage = false;
        PdfResourceCounter counter = new PdfResourceCounter(reader.getTrailer());
        long trailer = counter.getLength(null);
        Map<Integer, PdfObject> resources = counter.getResources();
        long length = 0;
        long page;
        while (hasMorePages()) {
            counter = new PdfResourceCounter(reader.getPageN(currentPage));
            page = counter.getLength(resources);
            resources = counter.getResources();
            length += page + trailer + xrefLength(resources.size());
            LOGGER.info(String.format("Page %s: Comparing %s with %s", currentPage, length, sizeInBytes));
            LOGGER.info(String.format("   page %s trailer %s xref %s", page, trailer, xrefLength(resources.size())));
            if (!hasPage || length < sizeInBytes) {
                hasPage = true;
                copy.addPage(copy.getImportedPage(reader, currentPage));
                length = copy.getOs().getCounter();
                LOGGER.info(String.format("Size after adding page: %s", length));
                if (length > sizeInBytes) overSized = true;
                currentPage++;
            }
            else {
                LOGGER.info("Page doesn't fit");
                break;
            }
        }
        document.close();
        return true;
    }
    
    private long xrefLength(int size) {
        return 20l * (size + 1);
    }
}
