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
 * This tool lets you generate a PDF that shows N pages on 1.
 */
public class NUp extends AbstractTool {

	static {
		addVersion("$Id$");
	}

	/**
	 * Constructs an NUp object.
	 */
	public NUp() {
		menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
		arguments.add(new FileArgument(this, "srcfile", "The file you want to N-up", false, new PdfFilter()));
		arguments.add(new FileArgument(this, "destfile", "The resulting PDF", true, new PdfFilter()));
		OptionArgument oa = new OptionArgument(this, "pow2", "The number of pages you want to copy to 1 page");
		oa.addOption("2", "1");
		oa.addOption("4", "2");
		oa.addOption("8", "3");
		oa.addOption("16", "4");
		oa.addOption("32", "5");
		oa.addOption("64", "6");
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
			int pow2;
			try {
				pow2 = Integer.parseInt((String) getValue("pow2"));
			}
			catch(Exception e) {
				pow2 = 1;
			}
			// we create a reader for a certain document
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			// we retrieve the total number of pages and the page size
			int total = reader.getNumberOfPages();
			System.out.println("There are " + total + " pages in the original file.");
            Rectangle pageSize = reader.getPageSize(1);
            Rectangle newSize = (pow2 % 2) == 0 ? new Rectangle(pageSize.width(), pageSize.height()) : new Rectangle(pageSize.height(), pageSize.width());
            Rectangle unitSize = new Rectangle(pageSize.width(), pageSize.height());
            Rectangle currentSize;
            for (int i = 0; i < pow2; i++) {
            	unitSize = new Rectangle(unitSize.height() / 2, unitSize.width());
            }
            int n = (int)Math.pow(2, pow2);
            int r = (int)Math.pow(2, (int)pow2 / 2);
            int c = n / r;
			// step 1: creation of a document-object
			Document document = new Document(newSize, 0, 0, 0, 0);
			// step 2: we create a writer that listens to the document
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
			// step 3: we open the document
			document.open();
			// step 4: adding the content
			PdfContentByte cb = writer.getDirectContent();
			PdfImportedPage page;
			float offsetX, offsetY, factor;
			int rotation, p;
			for (int i = 0; i < total; i++) {
				if (i % n == 0) {
					document.newPage();
				}
				p = i + 1;
				offsetX = unitSize.width() * ((i % n) % c);
				offsetY = newSize.height() - (unitSize.height() * (((i % n) / c) + 1));
				currentSize = reader.getPageSize(p);
				factor = Math.min(unitSize.width() / currentSize.width(), unitSize.height() / currentSize.height());
				offsetX += (unitSize.width() - (currentSize.width() * factor)) / 2f;
				offsetY += (unitSize.height() - (currentSize.height() * factor)) / 2f;
				page = writer.getImportedPage(reader, p);
				cb.addTemplate(page, factor, 0, 0, factor, offsetX, offsetY);
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
     * Generates an NUp version of an existing PDF file.
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
