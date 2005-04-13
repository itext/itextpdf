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

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.ImageFilter;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * Knits two TIFF files, one with the even pages and another with the odd pages, together.
 */
public class KnitTiff extends AbstractTool {
	/**
	 * Constructs a Tiff2Pdf object.
	 */
	public KnitTiff() {
		menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
		arguments.add(new FileArgument(this, "odd", "The tiff file with the odd pages", false, new ImageFilter(false, false, false, false, false, true)));
		arguments.add(new FileArgument(this, "even", "The tiff file with the even pages", false, new ImageFilter(false, false, false, false, false, true)));
		arguments.add(new FileArgument(this, "destfile", "The file to which the converted TIFF has to be written", true, new PdfFilter()));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("KnitTiff", true, true, true);
		internalFrame.setSize(550, 250);
		internalFrame.setJMenuBar(getMenubar());
		internalFrame.getContentPane().add(getConsole(40, 30));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("odd") == null) throw new InstantiationException("You need to choose a sourcefile for the odd pages");
			File odd_file = (File)getValue("odd");
			if (getValue("even") == null) throw new InstantiationException("You need to choose a sourcefile for the even pages");
			File even_file = (File)getValue("even");
			if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
			File pdf_file = (File)getValue("destfile");
			RandomAccessFileOrArray odd = new RandomAccessFileOrArray(odd_file.getAbsolutePath());
			RandomAccessFileOrArray even = new RandomAccessFileOrArray(even_file.getAbsolutePath());
			Image img = TiffImage.getTiffImage(odd, 1);
			Document document = new Document(new Rectangle(img.scaledWidth(),
					img.scaledHeight()));
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(pdf_file));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			int count = Math.max(TiffImage.getNumberOfPages(odd), TiffImage
					.getNumberOfPages(even));
			for (int c = 0; c < count; ++c) {
				try {
					Image imgOdd = TiffImage.getTiffImage(odd, c + 1);
					Image imgEven = TiffImage.getTiffImage(even, count - c);
					document.setPageSize(new Rectangle(imgOdd.scaledWidth(),
							imgOdd.scaledHeight()));
					document.newPage();
					imgOdd.setAbsolutePosition(0, 0);
					cb.addImage(imgOdd);
					document.setPageSize(new Rectangle(imgEven.scaledWidth(),
							imgEven.scaledHeight()));
					document.newPage();
					imgEven.setAbsolutePosition(0, 0);
					cb.addImage(imgEven);

				} catch (Exception e) {
					System.out.println("Exception page " + (c + 1) + " "
							+ e.getMessage());
				}
			}
			odd.close();
			even.close();
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
			// if the internal frame is null, the tool was called from the commandline
			return;
		}
		// represent the changes of the argument in the internal frame
	}

	
    /**
     * Converts a tiff file to PDF.
     * @param args
     */
	public static void main(String[] args) {
    	KnitTiff tool = new KnitTiff();
    	if (args.length < 3) {
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