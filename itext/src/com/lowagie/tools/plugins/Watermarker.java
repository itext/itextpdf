/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Carsten Hammer and Bruno Lowagie
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

import java.io.*;

import javax.swing.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.tools.arguments.*;

/**
 * This tool lets you add a text watermark to all pages of a document.
 */
public class Watermarker extends AbstractTool {

	static {
		addVersion("$Id$");
	}

	/**
	 * This tool lets you add a text watermark to all pages of a document.
	 */
	public Watermarker() {
		super();
		arguments.add(new FileArgument(this, "srcfile",
				"The file you want to watermark", false, new PdfFilter()));
		arguments.add(new ToolArgument(this, "watermark", "The text that can be used as watermark", String.class.getName()));
		arguments.add(new ToolArgument(this, "fontsize", "The fontsize of the watermark text", String.class.getName()));
                arguments.add(new ToolArgument(this, "opacity", "The opacity of the watermark text", String.class.getName()));
		arguments.add(new FileArgument(this, "destfile",
				"The file to which the watermarked PDF has to be written",
				true, new PdfFilter()));

	}

	/**
	 * Creates the internal frame.
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Watermark", true, false, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== Watermark OPENED ===");
	}

	/**
	 * Executes the tool (in most cases this generates a PDF file).
	 */
	public void execute() {
		try {
			if (getValue("srcfile") == null) {
				throw new InstantiationException(
						"You need to choose a sourcefile");
			}
			if (getValue("destfile") == null) {
				throw new InstantiationException(
						"You need to choose a destination file");
			}
			if (getValue("watermark") == null) {
				throw new InstantiationException(
						"You need to add a text for the watermark");
			}
        	int fontsize = Integer.parseInt((String)getValue("fontsize"));
                float opacity = Float.parseFloat((String)getValue("opacity"));
			BaseFont bf = BaseFont.createFont("Helvetica", BaseFont.WINANSI,
					false);
			PdfReader reader = new PdfReader(((File) getValue("srcfile"))
					.getAbsolutePath());
			int pagecount = reader.getNumberOfPages();
			PdfGState gs1 = new PdfGState();
			gs1.setFillOpacity(opacity);
			String text = (String)getValue("watermark");
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					(File) getValue("destfile")));
			float txtwidth = bf.getWidthPoint(text, fontsize);
			for (int i = 1; i <= pagecount; i++) {
				PdfContentByte seitex = stamp.getOverContent(i);
				Rectangle recc = reader.getCropBox(i);
				float winkel = (float) Math.atan(recc.height() / recc.width());
				float m1 = (float) Math.cos(winkel);
				float m2 = (float) -Math.sin(winkel);
				float m3 = (float) Math.sin(winkel);
				float m4 = (float) Math.cos(winkel);
				float xoff = (float) (-Math.cos(winkel) * txtwidth / 2 - Math
						.sin(winkel)
						* fontsize / 2);
				float yoff = (float) (Math.sin(winkel) * txtwidth / 2 - Math
						.cos(winkel)
						* fontsize / 2);
				seitex.saveState();
				seitex.setGState(gs1);
				seitex.beginText();
				seitex.setFontAndSize(bf, fontsize);
				seitex.setTextMatrix(m1, m2, m3, m4, xoff + recc.width() / 2,
						yoff + recc.height() / 2);
				seitex.showText(text);
				seitex.endText();
				seitex.restoreState();
			}
			stamp.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(internalFrame, e.getMessage(), e
					.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Gets the PDF file that should be generated (or null if the output isn't a
	 * PDF file).
	 *
	 * @return the PDF file that should be generated
	 * @throws InstantiationException
	 */
	protected File getDestPathPDF() throws InstantiationException {
		return (File) getValue("destfile");
	}

	/**
	 * Indicates that the value of an argument has changed.
	 *
	 * @param arg
	 *            the argument that has changed
	 */
	public void valueHasChanged(ToolArgument arg) {
		if (internalFrame == null) {
			// if the internal frame is null, the tool was called from the
			// commandline
			return;
		}

	}

	/**
	 * This methods helps you running this tool as a standalone application.
	 * @param args the srcfile, watermark text and destfile
	 */
	public static void main(String[] args) {
		Watermarker watermarker = new Watermarker();
    	if (args.length != 5) {
    		System.err.println(watermarker.getUsage());
    	}
    	watermarker.setArguments(args);
        watermarker.execute();
	}
}