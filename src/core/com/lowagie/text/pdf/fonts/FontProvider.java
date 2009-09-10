package com.lowagie.text.pdf.fonts;

import java.awt.Color;

import com.lowagie.text.Font;

public interface FontProvider {
	/**
	 * Checks if a certain font is registered.
	 *
	 * @param   fontname    the name of the font that has to be checked.
	 * @return  true if the font is found
	 */
	public boolean isRegistered(String fontname);

	/**
	 * Constructs a <CODE>Font</CODE>-object.
	 *
	 * @param	fontname    the name of the font
	 * @param	encoding    the encoding of the font
	 * @param       embedded    true if the font is to be embedded in the PDF
	 * @param	size	    the size of this font
	 * @param	style	    the style of this font
	 * @param	color	    the <CODE>Color</CODE> of this font.
	 * @return the Font constructed based on the parameters
	 */
	public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, Color color);
}
