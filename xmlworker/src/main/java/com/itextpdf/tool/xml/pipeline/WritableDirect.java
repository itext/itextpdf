/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Balder Van Camp
 *
 */
public interface WritableDirect extends Writable {

	/**
	 * @param writer
	 * @param doc
	 * @throws DocumentException
	 */
	public void write(final PdfWriter writer, final Document doc) throws DocumentException;

}
