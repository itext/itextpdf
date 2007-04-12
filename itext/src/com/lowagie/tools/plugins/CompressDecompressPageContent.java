package com.lowagie.tools.plugins;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.LabelAccessory;
import com.lowagie.tools.arguments.OptionArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

public class CompressDecompressPageContent extends AbstractTool {

	static {
		addVersion("$Id$");
	}
	
	/**
	 * Constructs a Burst object.
	 */
	public CompressDecompressPageContent() {
		FileArgument f = new FileArgument(this, "srcfile", "The file you want to compress/decompress", false, new PdfFilter());
		f.setLabel(new LabelAccessory());
		arguments.add(f);
		arguments.add(new FileArgument(this, "destfile", "The file to which the compressed/decompressed PDF has to be written", true, new PdfFilter()));
		OptionArgument oa = new OptionArgument(this, "compress", "compress");
		oa.addOption("Compress page content", "true");
		oa.addOption("Decompress page content", "false");
		arguments.add(oa);
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Compress/Decompress", true, false, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== Compress/Decompress OPENED ===");
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
     * Compresses/decompresses the page content streams in a PDF file.
     * @param args
     */
	public static void main(String[] args) {
		CompressDecompressPageContent tool = new CompressDecompressPageContent();
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
	
	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
			if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
			boolean compress = "true".equals(getValue("compress"));
			PdfReader reader = new PdfReader(((File)getValue("srcfile")).getAbsolutePath());
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(getDestPathPDF()));
			synchronized(arguments) {
				Document.compress = compress;
				int total = reader.getNumberOfPages() + 1;
				for (int i = 1; i < total; i++) {
					reader.setPageContent(i, reader.getPageContent(i));
				}
				stamper.close();
				Document.compress = true;
			}
		}
		catch(Exception e) {
        	JOptionPane.showMessageDialog(internalFrame,
        		    e.getMessage(),
        		    e.getClass().getName(),
        		    JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
		}
	}
}
