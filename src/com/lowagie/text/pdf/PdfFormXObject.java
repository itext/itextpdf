
package com.lowagie.text.pdf;

/**
 * <CODE>PdfFormObject</CODE> is a type of XObject containing a template-object.
 *
 * @author  bruno@lowagie.com
 */

public class PdfFormXObject extends PdfStream {

// public static final variables

	/** This is a PdfNumber representing 0. */
	public static final PdfNumber ZERO = new PdfNumber(0);

	/** This is a PdfNumber representing 1. */
	public static final PdfNumber ONE = new PdfNumber(1);

	/** This is the 1 - matrix. */
	public static final PdfArray MATRIX = new PdfArray();

	static {
		MATRIX.add(ONE);
		MATRIX.add(ZERO);
		MATRIX.add(ZERO);
		MATRIX.add(ONE);
		MATRIX.add(ZERO);
		MATRIX.add(ZERO);
	}

// membervariables


// constructor

	/**
	 * Constructs a <CODE>PdfFormXObject</CODE>-object.
	 *
	 * @param		template		the template
	 */

    PdfFormXObject(PdfTemplate template)// throws BadPdfFormatException
    {
		super();
		dictionary.put(PdfName.TYPE, PdfName.XOBJECT);
		dictionary.put(PdfName.SUBTYPE, PdfName.FORM);
		dictionary.put(PdfName.RESOURCES, template.getResources());
		dictionary.put(PdfName.BBOX, new PdfRectangle(0, 0, template.getWidth(), template.getHeight()));
		dictionary.put(PdfName.FORMTYPE, ONE);
		dictionary.put(PdfName.MATRIX, MATRIX);
        bytes = template.toPdf();
        dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
        try {flateCompress();} catch (Exception e){}
        //compress()
	}

}