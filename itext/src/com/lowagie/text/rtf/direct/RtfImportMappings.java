package com.lowagie.text.rtf.direct;

import java.awt.Color;
import java.util.HashMap;

/**
 * The RtfImportMappings make it possible to define font
 * and color mappings when using the RtfWriter2.importRtfFragment
 * method. This is necessary, because a RTF fragment does not
 * contain font or color information, just references to the
 * font and color tables.<br /><br />
 * 
 * The font mappings are fontNr -&gt; fontName and the color
 * mappigns are colorNr -&gt; Color.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfImportMappings {
	/**
	 * The fontNr to fontName mappings.
	 */
	private HashMap fontMappings = null;
	/**
	 * The colorNr to Color mappings.
	 */
	private HashMap colorMappings = null;
	
	/**
	 * Constructs a new RtfImportMappings initialising the mappings.
	 */
	public RtfImportMappings() {
		this.fontMappings = new HashMap();
		this.colorMappings = new HashMap();
	}
	
	/**
	 * Add a font to the list of mappings.
	 * 
	 * @param fontNr The font number.
	 * @param fontName The font name.
	 */
	public void addFont(String fontNr, String fontName) {
		this.fontMappings.put(fontNr, fontName);
	}
	
	/**
	 * Add a color to the list of mappings.
	 * 
	 * @param colorNr The color number.
	 * @param color The Color.
	 */
	public void addColor(String colorNr, Color color) {
		this.colorMappings.put(colorNr, color);
	}
	
	/**
	 * Gets the list of font mappings. String to String.
	 * 
	 * @return The font mappings.
	 */
	public HashMap getFontMappings() {
		return this.fontMappings;
	}
	
	/**
	 * Gets the list of color mappings. String to Color.
	 * 
	 * @return The color mappings.
	 */
	public HashMap getColorMappings() {
		return this.colorMappings;
	}
}
