package com.lowagie.text.pdf;

import java.io.*;
import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;

/**
 * A <CODE>PdfPattern</CODE> defines a ColorSpace
 *
 * @see		PdfStream
 */

public class PdfPattern extends PdfStream {

	PdfPattern(PdfPatternPainter painter) {
		super();
		PdfNumber one = new PdfNumber(1);
		PdfArray matrix = painter.getMatrix();
		if ( matrix != null ) {
			dictionary.put(PdfName.MATRIX, matrix);
		}
		dictionary.put(PdfName.TYPE, PdfName.PATTERN);
		dictionary.put(PdfName.BBOX, new PdfRectangle(painter.getBoundingBox()));
		dictionary.put(PdfName.RESOURCES, painter.getResources());
		try {
			PdfName key = new PdfName("TilingType");
			dictionary.put(key, one);
			key = new PdfName("PatternType");
			dictionary.put(key, one);
			key = new PdfName("PaintType");
            if (painter.isStencil())
    			dictionary.put(key, new PdfNumber(2));
            else
    			dictionary.put(key, one);
			key = new PdfName("XStep");
			dictionary.put(key, new PdfNumber(painter.getXStep()));
			key = new PdfName("YStep");
			dictionary.put(key, new PdfNumber(painter.getYStep()));
		} catch ( BadPdfFormatException bfe) {
            throw new ExceptionConverter(bfe);
		}
		bytes = painter.toPdf(null);
        dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
		try {
            flateCompress();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
	}
}
