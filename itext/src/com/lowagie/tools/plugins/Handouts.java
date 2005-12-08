/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.tools.plugins;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.OptionArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * Generates a PDF file that is useable as Handout.
 */
public class Handouts extends AbstractTool {

	static {
		addVersion("$Id$");
	}

	/**
	 * Constructs a Handouts object.
	 */
	public Handouts() {
		arguments.add(new FileArgument(this, "srcfile", "The file you want to convert", false, new PdfFilter()));
		arguments.add(new FileArgument(this, "destfile", "The file to which the Handout has to be written", true, new PdfFilter()));
		OptionArgument oa = new OptionArgument(this, "pages", "The number of pages you want on one handout page");
		oa.addOption("2 pages on 1", "2");
		oa.addOption("3 pages on 1", "3");
		oa.addOption("4 pages on 1", "4");
		oa.addOption("5 pages on 1", "5");
		oa.addOption("6 pages on 1", "6");
		oa.addOption("7 pages on 1", "7");
		oa.addOption("8 pages on 1", "8");
		arguments.add(oa);
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Handouts", true, false, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== Handouts OPENED ===");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
			File src = (File)getValue("srcfile");
			if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
			File dest = (File)getValue("destfile");
			int pages;
			try {
				pages = Integer.parseInt((String) getValue("pages"));
			}
			catch(Exception e) {
				pages = 4;
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
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			// we retrieve the total number of pages
			int n = reader.getNumberOfPages();
			System.out.println("There are " + n + " pages in the original file.");

			// step 1: creation of a document-object
			Document document = new Document(PageSize.A4);
			// step 2: we create a writer that listens to the document
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
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
				float factorx = (x2 - x1) / rect.width();
				float factory = (y1[p] - y2[p]) / rect.height();
				float factor = (factorx < factory ? factorx : factory);
				float dx = (factorx == factor ? 0f : ((x2 - x1) - rect.width() * factor) / 2f);
				float dy = (factory == factor ? 0f : ((y1[p] - y2[p]) - rect.height() * factor) / 2f);
				page = writer.getImportedPage(reader, i);
				rotation = reader.getPageRotation(i);
				if (rotation == 90 || rotation == 270) {
					cb.addTemplate(page, 0, -factor, factor, 0, x1 + dx, y2[p] + dy + rect.height() * factor);
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
				cb.rectangle(x1 + dx, y2[p] + dy, rect.width() * factor, rect.height() * factor);
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
        	JOptionPane.showMessageDialog(internalFrame,
        		    e.getMessage(),
        		    e.getClass().getName(),
        		    JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
		}
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
	 */
	public void valueHasChanged(ToolArgument arg) {
		if (internalFrame == null) {
			// if the internal frame is null, the tool was called from the commandline
			return;
		}
		// represent the changes of the argument in the internal frame
	}


    /**
     * Converts a PDF file to a PDF file useable as Handout.
     * @param args
     */
	public static void main(String[] args) {
    	Handouts tool = new Handouts();
    	if (args.length < 2) {
    		System.err.println(tool.getUsage());
    	}
    	tool.setArguments(args);
        tool.execute();
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
	 */
	protected File getDestPathPDF() throws InstantiationException {
		return (File)getValue("destfile");
	}
}
