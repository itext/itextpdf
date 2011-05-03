package com.itextpdf.tool.xml;
/**
 * 
 */


import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author redlab
 * 
 */
public class WatermarkEvent extends PdfPageEventHelper {

	protected PdfTemplate logo = null;
	
	public void init(PdfWriter writer) {
		PdfContentByte canvas = writer.getDirectContentUnder();

		logo = canvas.createTemplate(100, 100);
		PdfGState transparency = new PdfGState();
        transparency.setFillOpacity(0.2f);
        logo.setGState(transparency);
		logo.moveTo(	23.73f,  5.47f);
		logo.curveTo(	32.13f,  7.94f,
							35.73f, 28.15f,
							39.67f, 38.04f);
		logo.curveTo(	44.82f, 37.79f,
							54.41f, 42.38f,
							45.69f, 42.97f);
		logo.curveTo(	43.19f, 38.56f,
							23.84f, 49.61f,
							20.55f, 49.7f);
		logo.curveTo(	 8.58f, 50.03f,
							34.62f, 39.75f,
							34.68f, 39.99f);
		logo.curveTo(	34.64f, 39.80f,
							29.43f, 13.55f,
							26.08f, 10.12f);
		logo.curveTo(	19.24f,  3.11f,
							12.73f, 15.57f,
							13.35f, 22.21f);
		logo.curveTo(	13.60f, 24.85f,
							24.32f, 17.14f,
							17.21f, 25.02f);
		logo.curveTo(	 5.66f, 37.82f,
							 9.11f,  1.17f,
							23.73f,  5.47f);
		logo.setCMYKColorFillF(1, 0.5f, 0, 0.467f);
		logo.fill();
		logo.setCMYKColorFillF(0, 0.467f, 1, 0);
		logo.moveTo(	18.00f, 34.80f);
		logo.curveTo(	18.00f, 37.01f,
							16.21f, 38.80f,
							14.00f, 38.80f);
		logo.curveTo(	11.79f, 38.80f,
							10.00f, 37.01f,
							10.00f, 34.80f);
		logo.curveTo(	10.00f, 32.59f,
							11.79f, 30.80f,
							14.00f, 30.80f);
		logo.curveTo(	16.21f, 30.80f,
							18.00f, 32.59f,
							18.00f, 34.80f);
		logo.fill();


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEvent#onEndPage(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		if (logo == null)
			init(writer);
		PdfContentByte canvas = writer.getDirectContent();
		canvas.addTemplate(logo, 10, 0, 0, 10, 36, 180);
	}
}
