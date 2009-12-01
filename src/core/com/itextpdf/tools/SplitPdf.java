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
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class demonstrates how to split a PDF file using iText.
 * @author Bruno Lowagie
 * @since 2.1.1 (renamed to follow Java naming conventions)
 */
public class SplitPdf extends java.lang.Object {
    
    /**
     * This class can be used to split an existing PDF file.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if (args.length != 4) {
            System.err.println("arguments: srcfile destfile1 destfile2 pagenumber");
        }
        else {
            try {
				int pagenumber = Integer.parseInt(args[3]);
                
				// we create a reader for a certain document
				PdfReader reader = new PdfReader(args[0]);
				// we retrieve the total number of pages
				int n = reader.getNumberOfPages();
				System.out.println("There are " + n + " pages in the original file.");
                
				if (pagenumber < 2 || pagenumber > n) {
					throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.split.this.document.at.page.1.there.is.no.such.page", pagenumber));
				}
                
				// step 1: creation of a document-object
				Document document1 = new Document(reader.getPageSizeWithRotation(1));
				Document document2 = new Document(reader.getPageSizeWithRotation(pagenumber));
				// step 2: we create a writer that listens to the document
				PdfWriter writer1 = PdfWriter.getInstance(document1, new FileOutputStream(args[1]));
				PdfWriter writer2 = PdfWriter.getInstance(document2, new FileOutputStream(args[2]));
				// step 3: we open the document
				document1.open();
				PdfContentByte cb1 = writer1.getDirectContent();
				document2.open();
				PdfContentByte cb2 = writer2.getDirectContent();
				PdfImportedPage page;
				int rotation;
				int i = 0;
				// step 4: we add content
				while (i < pagenumber - 1) {
					i++;
					document1.setPageSize(reader.getPageSizeWithRotation(i));
					document1.newPage();
					page = writer1.getImportedPage(reader, i);
					rotation = reader.getPageRotation(i);
					if (rotation == 90 || rotation == 270) {
						cb1.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
					}
					else {
						cb1.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
					}
				}
				while (i < n) {
					i++;
					document2.setPageSize(reader.getPageSizeWithRotation(i));
					document2.newPage();
					page = writer2.getImportedPage(reader, i);
					rotation = reader.getPageRotation(i);
					if (rotation == 90 || rotation == 270) {
						cb2.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
					}
					else {
						cb2.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
					}
					System.out.println("Processed page " + i);
				}
				// step 5: we close the document
				document1.close();
				document2.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

