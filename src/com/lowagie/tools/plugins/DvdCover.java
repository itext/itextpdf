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

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.ImageArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This is a simple tool that generates a cover for a DVD.
 */
public class DvdCover extends AbstractTool {
	
	/**
	 * Constructs a DvdCover object.
	 */
	public DvdCover() {
		arguments.add(new FileArgument(this, "destfile", "The file to which the PDF has to be written", true, new PdfFilter()));
		arguments.add(new ToolArgument(this, "title", "The title of the DVD", String.class.getName()));
		arguments.add(new ToolArgument(this, "backgroundcolor", "The backgroundcolor of the DVD Cover (for instance 0xFFFFFF)", Color.class.getName()));
		arguments.add(new ImageArgument(this, "front", "The front image of the DVD Cover"));
		arguments.add(new ImageArgument(this, "back", "The back image of the DVD Cover"));
		arguments.add(new ImageArgument(this, "side", "The side image of the DVD Cover"));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Make your own DVD Cover", true, true, true);
		internalFrame.setSize(500, 300);
		internalFrame.setJMenuBar(getMenubar());
	}
    
    /**
     * @see com.lowagie.tools.plugins.AbstractTool#execute()
     */
    public void execute() {   
        try {
            // step 1: creation of a document-object
            Rectangle pageSize = new Rectangle(780, 525);
            if (getValue("backgroundcolor") != null) pageSize.setBackgroundColor((Color)getValue("backgroundcolor"));
            Document document = new Document(pageSize);
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
        	if (getValue("destfile") == null) throw new DocumentException("You must provide a destination file!");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream((File)getValue("destfile")));
                        
            // step 3: we open the document
            document.open();
            
            // step 4:
        	PdfContentByte cb = writer.getDirectContent();
            if (getValue("title") != null) {
            	cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), 24);
            	cb.beginText();
            	if (getValue("front") == null) {
            		cb.showTextAligned(Element.ALIGN_CENTER, (String)getValue("title"), 595f, 262f, 0f);
            	}
            	if (getValue("side") == null) {
            		cb.showTextAligned(Element.ALIGN_CENTER, (String)getValue("title"), 385f, 262f, 270f);
            	}
            	cb.endText(); 
            }
            cb.moveTo(370, 0);
            cb.lineTo(370, 525);
            cb.moveTo(410, 525);
            cb.lineTo(410, 0);
            cb.stroke();
            if (getValue("front") != null) {
            	Image front = (Image)getValue("front");
            	front.scaleToFit(370, 525);
            	front.setAbsolutePosition(410f + (370f - front.scaledWidth()) / 2f, (525f - front.scaledHeight()) / 2f);
            	document.add(front);
            }
            if (getValue("back") != null) {
            	Image back = (Image)getValue("back");
            	back.scaleToFit(370, 525);
            	back.setAbsolutePosition((370f - back.scaledWidth()) / 2f, (525f - back.scaledHeight()) / 2f);
            	document.add(back);
            }
            if (getValue("side") != null) {
            	Image side = (Image)getValue("side");
            	side.scaleToFit(40, 525);
            	side.setAbsolutePosition(370 + (40f - side.scaledWidth()) / 2f, (525f - side.scaledHeight()) / 2f);
            	document.add(side);
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
     * Generates a DVD Cover in PDF.
     * @param args	an array containing [0] a filename [1] a title [2] a backgroundcolor [3] a front image [4] a back image [5] a side image
     */
    public static void main(String[] args) {
    	DvdCover tool = new DvdCover();
    	if (args.length == 0) {
    		System.err.println(tool.getUsage());
    	}
    	tool.setArguments(args);
        tool.execute();
    }
}