/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This class by Bruno Lowagie. Copyright (c) 2002 Bruno Lowagie.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext@lowagie.com
 */

/**
 * This class demonstrates how to splot a PDF file using iText.
 * @author Bruno Lowagie
 */
package com.lowagie.tools;

import java.io.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class split_pdf extends java.lang.Object {
    
    /**
     * This class can be used to concatenate existing PDF files.
     * (This was an example known as PdfCopy.java)
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
					throw new DocumentException("You can't split this document at page " + pagenumber + "; there is no such page.");
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
						cb1.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
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
						cb2.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
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

