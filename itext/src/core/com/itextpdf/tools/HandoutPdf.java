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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Takes an existing PDF file and makes handouts.
 * @since 2.1.1 (renamed to follow Java naming conventions)
 */
public class HandoutPdf extends java.lang.Object {
    
	/**
	 * Makes handouts based on an existing PDF file.
	 * @param args the command line arguments
	 */
	public static void main (String args[]) {
		if (args.length != 3) {
			System.err.println("arguments: srcfile destfile pages");
		}
		else {
			try {
				int pages = Integer.parseInt(args[2]);
				if (pages < 2 || pages > 8) {
					throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.have.1.pages.on.one.page.minimum.2.maximum.8", pages));
				}
                
				float x1 = 30f;
				float x2 = 280f;
				float x3 = 320f;
				float x4 = 565f;
                
				float[] y1 = new float[pages];
				float[] y2 = new float[pages];
                
				float height = (778f - (20f * (pages - 1))) / pages;
				y1[0] = 812f;
				y2[0] = 812f - height;
                
				for (int i = 1; i < pages; i++) {
					y1[i] = y2[i - 1] - 20f;
					y2[i] = y1[i] - height;
				}
                
				// we create a reader for a certain document
				PdfReader reader = new PdfReader(args[0]);
				// we retrieve the total number of pages
				int n = reader.getNumberOfPages();
				System.out.println("There are " + n + " pages in the original file.");
                
				// step 1: creation of a document-object
				Document document = new Document(PageSize.A4);
				// step 2: we create a writer that listens to the document
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(args[1]));
				// step 3: we open the document
				document.open();
				PdfContentByte cb = writer.getDirectContent();
				PdfImportedPage page;
				int rotation;
				int i = 0;
				int p = 0;
				// step 4: we add content
				while (i < n) {
					i++;
					Rectangle rect = reader.getPageSizeWithRotation(i);
					float factorx = (x2 - x1) / rect.getWidth();
					float factory = (y1[p] - y2[p]) / rect.getHeight();
					float factor = (factorx < factory ? factorx : factory);
					float dx = (factorx == factor ? 0f : ((x2 - x1) - rect.getWidth() * factor) / 2f);
					float dy = (factory == factor ? 0f : ((y1[p] - y2[p]) - rect.getHeight() * factor) / 2f);
					page = writer.getImportedPage(reader, i);
					rotation = reader.getPageRotation(i);
					if (rotation == 90 || rotation == 270) {
						cb.addTemplate(page, 0, -factor, factor, 0, x1 + dx, y2[p] + dy + rect.getHeight() * factor);
					}
					else {
						cb.addTemplate(page, factor, 0, 0, factor, x1 + dx, y2[p] + dy);
					}
					cb.setRGBColorStroke(0xC0, 0xC0, 0xC0);
					cb.rectangle(x3 - 5f, y2[p] - 5f, x4 - x3 + 10f, y1[p] - y2[p] + 10f);
					for (float l = y1[p] - 19; l > y2[p]; l -= 16) {
						cb.moveTo(x3, l);
						cb.lineTo(x4, l);
					}
					cb.rectangle(x1 + dx, y2[p] + dy, rect.getWidth() * factor, rect.getHeight() * factor);
					cb.stroke();
					System.out.println("Processed page " + i);
					p++;
					if (p == pages) {
						p = 0;
						document.newPage();
					}
				}
				// step 5: we close the document
				document.close();
			}
			catch(Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
    }
}
