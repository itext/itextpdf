package com.lowagie.text.pdf;

import java.awt.print.PrinterGraphics;
import java.awt.print.PrinterJob;

/**
 * This is an extension class for the sole purpose of implementing the
 * {@link java.awt.print.PrinterGraphics PrinterGraphics} interface.
 */
public class PdfPrinterGraphics2D extends PdfGraphics2D implements PrinterGraphics
{
	private PrinterJob printerJob;
	
	public PdfPrinterGraphics2D(PdfContentByte cb, float width, float height, FontMapper fontMapper,
			boolean onlyShapes, boolean convertImagesToJPEG, float quality, PrinterJob printerJob)	{
		super(cb, width, height, fontMapper, onlyShapes, convertImagesToJPEG, quality);
		this.printerJob = printerJob;
	}

	public PrinterJob getPrinterJob()	{
		return printerJob;
	}
}
