package com.lowagie.text.pdf;

public class PdfDeveloperExtension {

	public static final PdfDeveloperExtension ADOBE_1_7_EXTENSIONLEVEL3 =
		new PdfDeveloperExtension(PdfName.ADBE, PdfName._1_7, 3);
	
	protected PdfName prefix;
	protected PdfName baseversion;
	protected int extensionLevel;
	
	public PdfDeveloperExtension(PdfName prefix, PdfName baseversion, int extensionLevel) {
		this.prefix = prefix;
		this.baseversion = baseversion;
		this.extensionLevel = extensionLevel;
	}

	public PdfName getPrefix() {
		return prefix;
	}
	
	public PdfName getBaseversion() {
		return baseversion;
	}
	
	public int getExtensionLevel() {
		return extensionLevel;
	}
	
	public PdfDictionary getDeveloperExtensions() {
		PdfDictionary developerextensions = new PdfDictionary();
		developerextensions.put(PdfName.BASEVERSION, baseversion);
		developerextensions.put(PdfName.EXTENSIONLEVEL, new PdfNumber(extensionLevel));
		return developerextensions;
	}
}
