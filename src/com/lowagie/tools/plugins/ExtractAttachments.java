/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Paulo Soares and Anonymous.
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
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JInternalFrame;

import com.lowagie.text.pdf.*;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.LabelAccessory;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you extract the attachements of a PDF.
 */
public class ExtractAttachments extends AbstractTool {

	static {
		addVersion("$Id$");
	}

	/**
	 * Constructs a ExtractAttachements object.
	 */
	public ExtractAttachments() {
		FileArgument f = new FileArgument(this, "srcfile",
				"The file you want to operate on", false, new PdfFilter());
		f.setLabel(new LabelAccessory());
		arguments.add(f);
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("ExtractAttachments", true, false,
				true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== ExtractAttachments OPENED ===");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("srcfile") == null)
				throw new InstantiationException(
						"You need to choose a sourcefile");
			File src = (File) getValue("srcfile");

			// we create a reader for a certain document
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			String outPath = src.getParentFile().getAbsolutePath();
			PdfDictionary catalog = reader.getCatalog();
			PdfDictionary names = (PdfDictionary) PdfReader
					.getPdfObject(catalog.get(PdfName.NAMES));
			if (names != null) {
				PdfDictionary embFiles = (PdfDictionary) PdfReader
						.getPdfObject(names.get(new PdfName("EmbeddedFiles")));
				if (embFiles != null) {
					HashMap embMap = PdfNameTree.readTree(embFiles);
					for (Iterator i = embMap.values().iterator(); i.hasNext();) {
						PdfDictionary filespec = (PdfDictionary) PdfReader
								.getPdfObject((PdfObject) i.next());
						unpackFile(reader, filespec, outPath);
					}
				}
			}
			for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
				PdfArray annots = (PdfArray) PdfReader.getPdfObject(reader
						.getPageN(k).get(PdfName.ANNOTS));
				if (annots == null)
					continue;
				for (Iterator i = annots.listIterator(); i.hasNext();) {
					PdfDictionary annot = (PdfDictionary) PdfReader
							.getPdfObject((PdfObject) i.next());
					PdfName subType = (PdfName) PdfReader.getPdfObject(annot
							.get(PdfName.SUBTYPE));
					if (!PdfName.FILEATTACHMENT.equals(subType))
						continue;
					PdfDictionary filespec = (PdfDictionary) PdfReader
							.getPdfObject(annot.get(PdfName.FS));
					unpackFile(reader, filespec, outPath);
				}
			}

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
	 * Extract the attachements of a PDF.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ExtractAttachments tool = new ExtractAttachments();
		if (args.length < 1) {
			System.err.println(tool.getUsage());
		}
		tool.setArguments(args);
		tool.execute();
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
	 */
	protected File getDestPathPDF() throws InstantiationException {
		throw new InstantiationException("There is more than one destfile.");
	}

	/**
	 * Unpacks a file attachment.
	 * @param reader The object that reads the PDF document
	 * @param filespec The dictonary containing the file specifications
	 * @param outPath The path where the attachment has to be written
	 */
	public static void unpackFile(PdfReader reader, PdfDictionary filespec,
			String outPath) {
		if (filespec == null)
			return;
		try {
			PdfName type = (PdfName) PdfReader.getPdfObject(filespec
					.get(PdfName.TYPE));
			if (!PdfName.F.equals(type) && !PdfName.FILESPEC.equals(type))
				return;
			PdfDictionary ef = (PdfDictionary) PdfReader.getPdfObject(filespec
					.get(PdfName.EF));
			if (ef == null)
				return;
			PdfString fn = (PdfString) PdfReader.getPdfObject(filespec
					.get(PdfName.F));
			System.out.println("Unpacking file '" + fn + "' to " + outPath);
			if (fn == null)
				return;
			File fLast = new File(fn.toUnicodeString());
			File fullPath = new File(outPath, fLast.getName());
			if (fullPath.exists())
				return;
			PRStream prs = (PRStream) PdfReader.getPdfObject(ef.get(PdfName.F));
			if (prs == null)
				return;
			byte b[] = PdfReader.getStreamBytes(prs);
			FileOutputStream fout = new FileOutputStream(fullPath);
			fout.write(b);
			fout.close();
		} catch (Exception e) {
		}
	}

}