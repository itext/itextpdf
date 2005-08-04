/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This class by Mark Thompson. Copyright (c) 2002 Mark Thompson.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@list.sourceforge.net
 */
package com.lowagie.tools.plugins;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JInternalFrame;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you split a PDF in two separate PDF files.
 */
public class SinglePages extends AbstractTool {
	/**
	 * Constructs an Encrypt object.
	 */
	public SinglePages() {
		arguments.add(new FileArgument(this, "srcfile", "The file you want to split", false, new PdfFilter()));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Single Pages", true, true, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
	}
	
	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
        try {			
			if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
			File src = (File)getValue("srcfile");
            File directory = src.getParentFile();
            String name = src.getName();
            name = name.substring(0, name.lastIndexOf("."));
        	// we create a reader for a certain document
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			// we retrieve the total number of pages
			int n = reader.getNumberOfPages();
			System.out.println("There are " + n + " pages in the original file.");
			Document document;
			int pagenumber;
            for (int i = 0; i < n; i++) {
            	pagenumber = i + 1;
            	// step 1: creation of a document-object
            	document = new Document(reader.getPageSizeWithRotation(pagenumber)); 
				// step 2: we create a writer that listens to the document
            	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(directory, name + "_" + pagenumber + ".pdf")));
            	// step 3: we open the document
            	document.open();
            	PdfContentByte cb = writer.getDirectContent();
				PdfImportedPage page = writer.getImportedPage(reader, pagenumber);
				int rotation = reader.getPageRotation(pagenumber);
				if (rotation == 90 || rotation == 270) {
					cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(pagenumber).height());
				}
				else {
					cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
				}
				// step 5: we close the document
				document.close();
			}
        }
        catch(Exception e) {
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
     * Concatenates two PDF files.
     * @param args
     */
	public static void main(String[] args) {
    	SinglePages tool = new SinglePages();
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
		throw new InstantiationException("There are more than one destfile.");
	}
}
