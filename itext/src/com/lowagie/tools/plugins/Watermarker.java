/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This class by Carsten hammer and Bruno Lowagie.
 * Copyright (c) 2005 Carsten Hammer and Bruno Lowagie.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@list.sourceforge.net
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
			BaseFont bf = BaseFont.createFont("Helvetica", BaseFont.WINANSI,
					false);
			PdfReader reader = new PdfReader(((File) getValue("srcfile"))
					.getAbsolutePath());
			int pagecount = reader.getNumberOfPages();
			PdfGState gs1 = new PdfGState();
			gs1.setFillOpacity(0.5f);
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
    	if (args.length != 4) {
    		System.err.println(watermarker.getUsage());
    	}
    	watermarker.setArguments(args);
        watermarker.execute();
	}
}