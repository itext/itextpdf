package com.lowagie.text.rtf.direct;

import java.awt.Color;
import java.util.HashMap;

import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfColor;
import com.lowagie.text.rtf.style.RtfFont;

/**
 * The RtfImportHeader stores the docment header information from
 * an RTF document that is being imported. Currently font and
 * color settings are stored. The RtfImportHeader maintains a mapping
 * from font and color numbers from the imported RTF document to
 * the RTF document that is the target of the import. This guarantees
 * that the merged document has the correct font and color settings.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfImportHeader {
	/**
	 * The HashMap storing the font number mappings.
	 */
	private HashMap importFontMapping = null;
	/**
	 * The HashMap storing the color number mapings.
	 */
	private HashMap importColorMapping = null;
	/**
	 * The RtfDocument to get font and color numbers from.
	 */
	private RtfDocument rtfDoc = null;
	
	/**
	 * Constructs a new RtfImportHeader.
	 * 
	 * @param rtfDoc The RtfDocument to get font and color numbers from.
	 */
	public RtfImportHeader(RtfDocument rtfDoc) {
		this.rtfDoc = rtfDoc;
		this.importFontMapping = new HashMap();
		this.importColorMapping = new HashMap();
	}
	
	/**
	 * Imports a font. The font name is looked up in the RtfDocumentHeader and
	 * then the mapping from original font number to actual font number is added.
	 * 
	 * @param fontNr The original font number.
	 * @param fontName The font name to look up.
	 */
	public void importFont(String fontNr, String fontName) {
		RtfFont rtfFont = new RtfFont(fontName);
		rtfFont.setRtfDocument(this.rtfDoc);
		this.importFontMapping.put(fontNr, Integer.toString(this.rtfDoc.getDocumentHeader().getFontNumber(rtfFont)));
	}
	
	/**
	 * Performs the mapping from the original font number to the actual
	 * font number in the resulting RTF document. If the font number was not
	 * seen during import (thus no mapping) then 0 is returned, guaranteeing
	 * that the font number is always valid.
	 * 
	 * @param fontNr The font number to map.
	 * @return The mapped font number.
	 */
	public String mapFontNr(String fontNr) {
		if(this.importFontMapping.containsKey(fontNr)) {
			return (String) this.importFontMapping.get(fontNr);
		} else {
			return "0";
		}
	}
	
	/**
	 * Imports a color value. The color number for the color defined
	 * by its red, green and blue values is determined and then the
	 * resulting mapping is added.
	 * 
	 * @param colorNr The original color number.
	 * @param color The color to import.
	 */
	public void importColor(String colorNr, Color color) {
		RtfColor rtfColor = new RtfColor(this.rtfDoc, color);
		this.importColorMapping.put(colorNr, Integer.toString(rtfColor.getColorNumber()));
	}
	
	/**
	 * Performs the mapping from the original font number to the actual font
	 * number used in the RTF document. If the color number was not
	 * seen during import (thus no mapping) then 0 is returned, guaranteeing
	 * that the color number is always valid.
	 * 
	 * @param colorNr The color number to map.
	 * @return The mapped color number
	 */
	public String mapColorNr(String colorNr) {
		if(this.importColorMapping.containsKey(colorNr)) {
			return (String) this.importColorMapping.get(colorNr);
		} else {
			return "0";
		}
	}
}
