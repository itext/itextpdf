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
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.OptionArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you generate a PDF that shows N pages on 1.
 */
public class NUp extends AbstractTool {
	
	static {
		addVersion("$Id$");
	}
	
	/**
	 * Constructs an Burst object.
	 */
	public NUp() {
		menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
		arguments.add(new FileArgument(this, "srcfile", "The file you want to N-up", false, new PdfFilter()));
		arguments.add(new FileArgument(this, "destfile", "The resulting PDF", true, new PdfFilter()));
		OptionArgument oa = new OptionArgument(this, "pages", "The number of pages you want to copy to 1 page");
		oa.addOption("2", "1");
		oa.addOption("4", "2");
		oa.addOption("8", "3");
		oa.addOption("16", "4");
		arguments.add(oa);
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("N-up", true, true, true);
		internalFrame.setSize(300, 160);
		internalFrame.setJMenuBar(getMenubar());
		internalFrame.getContentPane().add(getConsole(30, 30));
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
				pages = 1;
			}
			// we create a reader for a certain document
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			// we retrieve the total number of pages and the page size
			int total = reader.getNumberOfPages();
			System.out.println("There are " + total + " pages in the original file.");
            Rectangle pageSize = reader.getPageSizeWithRotation(1);
            boolean rotate = false;
            if (pages % 2 == 1) {
            	rotate = true;
            	pageSize = pageSize.rotate();
            }
			// step 1: creation of a document-object
			Document document = new Document(pageSize, 0, 0, 0, 0);
			// step 2: we create a writer that listens to the document
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
			// step 3: we open the document
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			Image page;
			int n = (int)Math.pow(2, pages);
			int c = 0;
			int r = 0;
			switch(pages) {
			case 1:
				c = 2;
				r = 1;
				break;
			case 2:
				c = 2;
				r = 2;
				break;
			case 3:
				c = 4;
				r = 2;
				break;
			case 4:
				c = 4;
				r = 4;
			}
			// step 4: we add content
			PdfPTable table = null;
			Image image;
			for (int i = 0; i < total; i++) {
				if (i % n == 0) {
					if (table != null) document.add(table);
					table = new PdfPTable(c);
					table.setTotalWidth(pageSize.width());
					table.setLockedWidth(true);
				}
				image = Image.getInstance(writer.getImportedPage(reader, i + 1));
				if (rotate) image.rotate();
				PdfPCell cell = new PdfPCell(image, true);
				cell.setFixedHeight(pageSize.width() / r);
				cell.setPadding(0f);
				table.addCell(cell);
			}
			// step 5: we close the document
			document.close();
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
    	NUp tool = new NUp();
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
