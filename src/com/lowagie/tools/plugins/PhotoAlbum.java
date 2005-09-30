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
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPageLabels;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * Converts a Tiff file to a PDF file.
 * Inspired by a comp.text.pdf question by Sebastian Schubert
 * and an answer by Hans-Werner Hilse.
 */
public class PhotoAlbum extends AbstractTool {
	
	static {
		addVersion("$Id$");
	}
	/**
	 * Constructs a PhotoAlbum object.
	 */
	public PhotoAlbum() {
		menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
		arguments.add(new FileArgument(this, "srcdir", "The directory containing the image files", false));
		arguments.add(new FileArgument(this, "destfile", "The file to which the converted TIFF has to be written", true, new PdfFilter()));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("PhotoAlbum", true, true, true);
		internalFrame.setSize(550, 250);
		internalFrame.setJMenuBar(getMenubar());
		internalFrame.getContentPane().add(getConsole(40, 30));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("srcdir") == null) throw new InstantiationException("You need to choose a source directory");
			File directory = (File)getValue("srcdir");
			if (directory.isFile()) directory = directory.getParentFile();
			if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
			File pdf_file = (File)getValue("destfile");
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
			writer.setViewerPreferences(PdfWriter.PageModeUseThumbs);
			PdfPageLabels pageLabels = new PdfPageLabels();
			int dpiX, dpiY;
			float imgWidthPica, imgHeightPica;
			TreeSet images = new TreeSet();
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) images.add(files[i]);
			}
			File image;
            for (Iterator i = images.iterator(); i.hasNext(); ) {
            	image = (File) i.next();
            	System.out.println("Testing image: " + image.getName());
            	try {
            		Image img = Image.getInstance(image.getAbsolutePath());
            		dpiX=img.getDpiX();
                    if (dpiX == 0) dpiX=72;
                    dpiY=img.getDpiY();
                    if (dpiY == 0) dpiY=72;
                    imgWidthPica=(72*img.plainWidth()) / dpiX;
                    imgHeightPica=(72*img.plainHeight()) / dpiY;
                    img.scaleAbsolute(imgWidthPica,imgHeightPica);
                    document.setPageSize(new Rectangle(imgWidthPica, imgHeightPica));
                	if (document.isOpen()) {
                		document.newPage();
                	}
                	else {
                		document.open();
                	}
                	img.setAbsolutePosition(0, 0);
                    document.add(img);
                    pageLabels.addPageLabel(writer.getPageNumber(), PdfPageLabels.EMPTY, image.getName());
                    System.out.println("Added image: " + image.getName());
                }
            	catch(Exception e) {
            		System.err.println(e.getMessage());
            	}
            }
        	if (document.isOpen()) {
        		writer.setPageLabels(pageLabels);
        	    document.close();
        	}
        	else {
        		System.err.println("No images were found in directory " + directory.getAbsolutePath());
        	}
		} catch (Exception e) {
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
     * Converts a tiff file to PDF.
     * @param args
     */
	public static void main(String[] args) {
    	PhotoAlbum tool = new PhotoAlbum();
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