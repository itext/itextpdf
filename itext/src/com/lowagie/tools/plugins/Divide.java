/*
 * $Id$
 * $Name$
 *
 * Copyright 2006 by Carsten Hammer.
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

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you generate a PDF that shows N pages on 1.
 */
public class Divide extends AbstractTool {

	static {
		addVersion("$Id$");
	}

	/**
	 * Constructs an Divide object.
	 */
	public Divide() {
		menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
		arguments.add(new FileArgument(this, "srcfile",
				"The file you want to divide", false, new PdfFilter()));
		arguments.add(new FileArgument(this, "destfile", "The resulting PDF",
				true, new PdfFilter()));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Divide", true, false, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== Divide OPENED ===");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("srcfile") == null) {
				throw new InstantiationException(
						"You need to choose a sourcefile");
			}
			File src = (File) getValue("srcfile");
			if (getValue("destfile") == null) {
				throw new InstantiationException(
						"You need to choose a destination file");
			}
			File dest = (File) getValue("destfile");

			// we create a reader for a certain document
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			// we retrieve the total number of pages and the page size
			int total = reader.getNumberOfPages();
			System.out.println("There are " + total
					+ " pages in the original file.");

			Rectangle pageSize = reader.getPageSize(1);
			Rectangle newSize = new Rectangle(pageSize.width() / 2, pageSize
					.height());
			// step 1: creation of a document-object
			Document document = new Document(newSize, 0, 0, 0, 0);
			// step 2: we create a writer that listens to the document
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(dest));
			// step 3: we open the document
			document.open();
			// step 4: adding the content
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page;
			float offsetX, offsetY;
			int p;
			for (int i = 0; i < total; i++) {
				p = i + 1;
				pageSize = reader.getPageSize(p);
				newSize = new Rectangle(pageSize.width() / 2, pageSize.height());

				document.newPage();
				offsetX = 0;
				offsetY = 0;
				page = writer.getImportedPage(reader, p);
				cb.addTemplate(page, 1, 0, 0, 1, offsetX, offsetY);
				document.newPage();
				offsetX = -newSize.width();
				offsetY = 0;
				page = writer.getImportedPage(reader, p);
				cb.addTemplate(page, 1, 0, 0, 1, offsetX, offsetY);

			}
			// step 5: we close the document
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
	 */
	public void valueHasChanged(ToolArgument arg) {
		if (internalFrame == null) {
			// if the internal frame is null, the tool was called from the
			// commandline
			return;
		}
		// represent the changes of the argument in the internal frame
	}

	/**
	 * Generates a divided version of an NUp version of an existing PDF file.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Divide tool = new Divide();
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
		return (File) getValue("destfile");
	}
}