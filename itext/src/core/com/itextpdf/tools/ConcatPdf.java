/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.tools;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;

/**
 * Tool that can be used to concatenate existing PDF files.
 * @since 2.1.1 (renamed to follow Java naming conventions)
 */
public class ConcatPdf {
    
    /**
     * This class can be used to concatenate existing PDF files.
     * (This was an example known as PdfCopy.java)
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("arguments: file1 [file2 ...] destfile");
        }
        else {
            try {
                int pageOffset = 0;
                ArrayList master = new ArrayList();
                int f = 0;
                String outFile = args[args.length-1];
                Document document = null;
                PdfCopy  writer = null;
                while (f < args.length-1) {
                    // we create a reader for a certain document
                    PdfReader reader = new PdfReader(args[f]);
                    reader.consolidateNamedDestinations();
                    // we retrieve the total number of pages
                    int n = reader.getNumberOfPages();
                    List bookmarks = SimpleBookmark.getBookmark(reader);
                    if (bookmarks != null) {
                        if (pageOffset != 0)
                            SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                        master.addAll(bookmarks);
                    }
                    pageOffset += n;
                    System.out.println("There are " + n + " pages in " + args[f]);
                    
                    if (f == 0) {
                        // step 1: creation of a document-object
                        document = new Document(reader.getPageSizeWithRotation(1));
                        // step 2: we create a writer that listens to the document
                        writer = new PdfCopy(document, new FileOutputStream(outFile));
                        // step 3: we open the document
                        document.open();
                    }
                    // step 4: we add content
                    PdfImportedPage page;
                    for (int i = 0; i < n; ) {
                        ++i;
                        page = writer.getImportedPage(reader, i);
                        writer.addPage(page);
                        System.out.println("Processed page " + i);
                    }
                    writer.freeReader(reader);
                    f++;
                }
                if (!master.isEmpty())
                    writer.setOutlines(master);
                // step 5: we close the document
                document.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
