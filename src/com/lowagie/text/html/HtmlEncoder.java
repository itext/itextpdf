/*
 * @(#)HtmlEncoder.java				0.22 2000/02/02
 *       release iText0.3:			0.22 2000/02/14
 *       release iText0.35:         0.22 2000/08/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *  	  
 * Very special thanks to my colleague at the University of Ghent,
 * Mario Maccarini (mario.maccarini@rug.ac.be); he made some very
 * useful HTNL-classes.
 */

package com.lowagie.text.html;

import java.awt.Color;

import com.lowagie.text.Element;

/**
 * This class converts a <CODE>String</CODE> to the HTML-format of a String.
 * <P>
 * To convert the <CODE>String</CODE>, each character is examined:
 * <UL>
 * <LI>ASCII-characters from 000 till 031 are represented as &amp;#xxx;<BR>
 *     (with xxx = the value of the character)
 * <LI>ASCII-characters from 032 t/m 127 are represented by the character itself, except for:
 *     <UL>
 *     <LI>'\n'	becomes &lt;BR&gt;\n
 *     <LI>&quot; becomes &amp;quot;
 *     <LI>&amp; becomes &amp;amp;
 *     <LI>&lt; becomes &amp;lt;
 *     <LI>&gt; becomes &amp;gt;
 *     </UL>
 * <LI>ASCII-characters from 128 till 255 are represented as &amp;#xxx;<BR>
 *     (with xxx = the value of the character)
 * </UL>
 * <P>
 * Example:
 * <P><BLOCKQUOTE><PRE>
 *    String htmlPresentation = HtmlEncoder.encode("Marie-Th&#233;r&#232;se S&#248;rensen");
 * </PRE></BLOCKQUOTE><P>
 * for more info: see O'Reilly; "HTML: The Definitive Guide" (page 164)
 *
 * @author  mario.maccarini@rug.ac.be
 * @author  bruno@lowagie.com
 * @version 0.22, 2000/02/02
 *
 * @since   iText0.30
 */

public class HtmlEncoder { 

// membervariables

    /** List with the HTML translation of all the characters. */
    private static final String[] htmlCode = new String[256];

    static {
		for (int i = 0; i < 10; i++) {
			htmlCode[i] = "&#00" + i + ";";
		}

 		for (int i = 10; i < 32; i++) {
			htmlCode[i] = "&#0" + i + ";";
		}

		for (int i = 32; i < 128; i++) {
			htmlCode[i] = String.valueOf((char)i);
		}

		// Special characters
		htmlCode['\n'] = "<BR>\n";
		htmlCode['\"'] = "&quot;"; // double quote
		htmlCode['&'] = "&amp;"; // ampersand
		htmlCode['<'] = "&lt;"; // lower than
		htmlCode['>'] = "&gt;"; // greater than

		for (int i = 128; i < 256; i++) {
			htmlCode[i] = "&#" + i + ";";
		}
    }


// constructors

	/**
	 * This class will never be constructed.
	 * <P>
	 * HtmlEncoder only contains static methods.
	 * 
	 * @since	iText0.30
	 */
	private HtmlEncoder () { }

// methods

    /**
     * Converts a <CODE>String</CODE> to the HTML-format of this <CODE>String</CODE>.
	 *
	 * @param	string	The <CODE>String</CODE> to convert 
     * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
     */

    public static String encode(String string) {
		int n = string.length();
		char character;
		StringBuffer buffer = new StringBuffer();
        // loop over all the characters of the String.
		for (int i = 0; i < n; i++) {
			character = string.charAt(i);
			// the Htmlcode of these characters are added to a StringBuffer one by one
			try {
				buffer.append(htmlCode[character]);
			}
			catch(ArrayIndexOutOfBoundsException aioobe) {
				buffer.append(character);
			}
		}
        return buffer.toString();
	}

	/**
	 * Converts a <CODE>Color</CODE> into a HTML representation of this <CODE>Color</CODE>.
	 *
	 * @param	color	the <CODE>Color</CODE> that has to be converted.
	 * @return	the HTML representation of this <COLOR>Color</COLOR>
	 *
	 * @since	iText0.30
	 */

	public static String encode(Color color) {
		StringBuffer buffer = new StringBuffer("#");
		if (color.getRed() < 16) {
			buffer.append('0');
		}
		buffer.append(Integer.toString(color.getRed(), 16));
		if (color.getGreen() < 16) {
			buffer.append('0');
		}
		buffer.append(Integer.toString(color.getGreen(), 16));
		if (color.getBlue() < 16) {
			buffer.append('0');
		}
		buffer.append(Integer.toString(color.getBlue(), 16));
		return buffer.toString();
	}

	/**
	 * Gets the HTML value for the align-key.
	 *
	 * @param	align	the alignment
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	public static String getAlignment(int align) {
		switch(align) {
		case Element.ALIGN_LEFT:
			return "Left";
		case Element.ALIGN_CENTER:
			return "Center";
		case Element.ALIGN_RIGHT:
			return "Right";
		case Element.ALIGN_JUSTIFIED:
			return "Justify";
		case Element.ALIGN_TOP:
			return "Top";
		case Element.ALIGN_MIDDLE:
			return "Middle";
		case Element.ALIGN_BOTTOM:
			return "Bottom";
		case Element.ALIGN_BASELINE:
			return "Baseline";
		default:
			return "Left";
		}
	}
}