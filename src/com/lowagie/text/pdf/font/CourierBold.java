/*
 * @(#)CourierBold.java				0.23 2000/02/02
 *               iText0.3:			0.23 2000/02/14
 *       release iText0.35:         0.23 2000/08/11
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
 * Very special thanks to Stefan Mainz (Stefan.Mainz@Dynaware.de) from Dynaware Systemberatung GmbH
 * for different suggestions to optimize the font-classes. Ao Stefan wrote a parser to parse the
 * Adobe Font Metric-files.
 */

package com.lowagie.text.pdf.font;

import com.lowagie.text.pdf.PdfFontMetrics;
import com.lowagie.text.pdf.PdfName;

/**
 * This class contains the metrics of the font <VAR>Courier Bold</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.1/cob_____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class CourierBold extends PdfFontMetrics {

// static membervariables
  							   
	/** Contains the widths of the Courier Bold characters. */
	public final static int[] METRIC_STANDARD =
	{
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //space
		600, //exclam
		600, //quotedbl
		600, //numbersign
		600, //dollar
		600, //percent
		600, //ampersand
		600, //quoteright
		600, //parenleft
		600, //parenright
		600, //asterisk
		600, //plus
		600, //comma
		600, //hyphen
		600, //period
		600, //slash
		600, //zero
		600, //one
		600, //two
		600, //three
		600, //four
		600, //five
		600, //six
		600, //seven
		600, //eight
		600, //nine
		600, //colon
		600, //semicolon
		600, //less
		600, //equal
		600, //greater
		600, //question
		600, //at
		600, //A
		600, //B
		600, //C
		600, //D
		600, //E
		600, //F
		600, //G
		600, //H
		600, //I
		600, //J
		600, //K
		600, //L
		600, //M
		600, //N
		600, //O
		600, //P
		600, //Q
		600, //R
		600, //S
		600, //T
		600, //U
		600, //V
		600, //W
		600, //X
		600, //Y
		600, //Z
		600, //bracketleft
		600, //backslash
		600, //bracketright
		600, //asciicircum
		600, //underscore
		600, //quoteleft
		600, //a
		600, //b
		600, //c
		600, //d
		600, //e
		600, //f
		600, //g
		600, //h
		600, //i
		600, //j
		600, //k
		600, //l
		600, //m
		600, //n
		600, //o
		600, //p
		600, //q
		600, //r
		600, //s
		600, //t
		600, //u
		600, //v
		600, //w
		600, //x
		600, //y
		600, //z
		600, //braceleft
		600, //bar
		600, //braceright
		600, //asciitilde
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //exclamdown
		600, //cent
		600, //sterling
		600, //fraction
		600, //yen
		600, //florin
		600, //section
		600, //currency
		600, //quotesingle
		600, //quotedblleft
		600, //guillemotleft
		600, //guilsinglleft
		600, //guilsinglright
		600, //fi
		600, //fl
		0, //
		600, //endash
		600, //dagger
		600, //daggerdbl
		600, //periodcentered
		0, //
		600, //paragraph
		600, //bullet
		600, //quotesinglbase
		600, //quotedblbase
		600, //quotedblright
		600, //guillemotright
		600, //ellipsis
		600, //perthousand
		0, //
		600, //questiondown
		0, //
		600, //grave
		600, //acute
		600, //circumflex
		600, //tilde
		600, //macron
		600, //breve
		600, //dotaccent
		600, //dieresis
		0, //
		600, //ring
		600, //cedilla
		0, //
		0, //hangarumlaut
		600, //ogonek
		600, //caron
		600, //emdash
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //AE
		0, //
		600, //ordfeminine
		0, //
		0, //
		0, //
		0, //
		600, //Lslash
		600, //Oslash
		600, //OE
		600, //ordmasculine
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //ae
		0, //
		0, //
		0, //
		600, //dotlessi
		0, //
		0, //
		600, //lslash
		600, //oslash
		600, //oe
		600, //germandbls
		0, //
		0, //
		0, //
		0 //
	}; 

	/** Contains the widths of the Courier Bold characters. */
	public final static int[] METRIC_WIN_ANSI =
	{
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //space
		600, //exclam
		600, //quotedbl
		600, //numbersign
		600, //dollar
		600, //percent
		600, //ampersand
		600, //quoteright
		600, //parenleft
		600, //parenright
		600, //asterisk
		600, //plus
		600, //comma
		600, //hyphen
		600, //period
		600, //slash
		600, //zero
		600, //one
		600, //two
		600, //three
		600, //four
		600, //five
		600, //six
		600, //seven
		600, //eight
		600, //nine
		600, //colon
		600, //semicolon
		600, //less
		600, //equal
		600, //greater
		600, //question
		600, //at
		600, //A
		600, //B
		600, //C
		600, //D
		600, //E
		600, //F
		600, //G
		600, //H
		600, //I
		600, //J
		600, //K
		600, //L
		600, //M
		600, //N
		600, //O
		600, //P
		600, //Q
		600, //R
		600, //S
		600, //T
		600, //U
		600, //V
		600, //W
		600, //X
		600, //Y
		600, //Z
		600, //bracketleft
		600, //backslash
		600, //bracketright
		600, //asciicircum
		600, //underscore
		600, //grave
		600, //a
		600, //b
		600, //c
		600, //d
		600, //e
		600, //f
		600, //g
		600, //h
		600, //i
		600, //j
		600, //k
		600, //l
		600, //m
		600, //n
		600, //o
		600, //p
		600, //q
		600, //r
		600, //s
		600, //t
		600, //u
		600, //v
		600, //w
		600, //x
		600, //y
		600, //z
		600, //braceleft
		600, //bar
		600, //braceright
		600, //asciitilde
		600, //bullet
		600, //bullet
		600, //bullet
		600, //quotesinglbase
		600, //florin
		600, //quotedblbase
		600, //ellipsis
		600, //dagger
		600, //daggerdbl
		600, //circumflex
		600, //perthousand
		600, //Scaron
		600, //guilsinglleft
		600, //OE
		600, //bullet
		600, //bullet
		600, //bullet
		600, //bullet
		600, //quoteleft
		600, //quoteright
		600, //quotedblleft
		600, //quotedblright
		600, //bullet
		600, //endash
		600, //emdash
		600, //tilde
		600, //trademark
		600, //scaron
		600, //guilsinglright
		600, //oe
		600, //bullet
		600, //zcaron
		600, //Ydieresis
		600, //space
		600, //exclamdown
		600, //cent
		600, //sterling
		600, //currency
		600, //yen
		600, //brokenbar
		600, //section
		600, //dieresis
		600, //copyright
		600, //ordfeminine
		600, //guillemotleft
		600, //logicalnot
		600, //hyphen
		600, //registered
		600, //macron
		600, //degree
		600, //plusminus
		600, //twosuperior
		600, //threesuperior
		600, //acute
		600, //mu
		600, //paragraph
		600, //periodcentered
		600, //cedilla
		600, //onesuperior
		600, //ordmasculine
		600, //guillemotright
		600, //onequarter
		600, //onehalf
		600, //threequarters
		600, //questiondown
		600, //Agrave
		600, //Aacute
		600, //Acircumflex
		600, //Atilde
		600, //Adieresis
		600, //Aring
		600, //AE
		600, //Ccedilla
		600, //Egrave
		600, //Eacute
		600, //Ecircumflex
		600, //Edieresis
		600, //Igrave
		600, //Iacute
		600, //Icircumflex
		600, //Idieresis
		600, //Eth
		600, //Ntilde
		600, //Ograve
		600, //Oacute
		600, //Ocircumflex
		600, //Otilde
		600, //Odieresis
		600, //multiply
		600, //Oslash
		600, //Ugrave
		600, //Uacute
		600, //Ucircumflex
		600, //Udieresis
		600, //Yacute
		600, //Thorn
		600, //germandbls
		600, //agrave
		600, //aacute
		600, //acircumflex
		600, //atilde
		600, //adieresis
		600, //aring
		600, //ae
		600, //ccedilla
		600, //egrave
		600, //eacute
		600, //ecircumflex
		600, //edieresis
		600, //igrave
		600, //iacute
		600, //icircumflex
		600, //idieresis
		600, //eth
		600, //ntilde
		600, //ograve
		600, //oacute
		600, //ocircumflex
		600, //otilde
		600, //odieresis
		600, //divide
		600, //oslash
		600, //ugrave
		600, //uacute
		600, //ucircumflex
		600, //udieresis
		600, //yacute
		600, //thorn
		600 //ydieresis
	};

	/** Contains the widths of the Courier Bold characters. */
	public final static int[] METRIC_MAC_ROMAN =
	{
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //space
		600, //exclam
		600, //quotedbl
		600, //numbersign
		600, //dollar
		600, //percent
		600, //ampersand
		600, //quoteright
		600, //parenleft
		600, //parenright
		600, //asterisk
		600, //plus
		600, //comma
		600, //hyphen
		600, //period
		600, //slash
		600, //zero
		600, //one
		600, //two
		600, //three
		600, //four
		600, //five
		600, //six
		600, //seven
		600, //eight
		600, //nine
		600, //colon
		600, //semicolon
		600, //less
		600, //equal
		600, //greater
		600, //question
		600, //at
		600, //A
		600, //B
		600, //C
		600, //D
		600, //E
		600, //F
		600, //G
		600, //H
		600, //I
		600, //J
		600, //K
		600, //L
		600, //M
		600, //N
		600, //O
		600, //P
		600, //Q
		600, //R
		600, //S
		600, //T
		600, //U
		600, //V
		600, //W
		600, //X
		600, //Y
		600, //Z
		600, //bracketleft
		600, //backslash
		600, //bracketright
		600, //asciicircum
		600, //underscore
		600, //grave
		600, //a
		600, //b
		600, //c
		600, //d
		600, //e
		600, //f
		600, //g
		600, //h
		600, //i
		600, //j
		600, //k
		600, //l
		600, //m
		600, //n
		600, //o
		600, //p
		600, //q
		600, //r
		600, //s
		600, //t
		600, //u
		600, //v
		600, //w
		600, //x
		600, //y
		600, //z
		600, //braceleft
		600, //bar
		600, //braceright
		600, //asciitilde
		0, //
		600, //Adieresis
		600, //Aring
		600, //Ccedilla
		600, //Eacute
		600, //Ntilde
		600, //Odieresis
		600, //Udieresis
		600, //aacute
		600, //agrave
		600, //acircumflex
		600, //adieresis
		600, //atilde
		600, //aring
		600, //ccedilla
		600, //eacute
		600, //egrave
		600, //ecircumflex
		600, //edieresis
		600, //iacute
		600, //igrave
		600, //icircumflex
		600, //idieresis
		600, //ntilde
		600, //oacute
		600, //ograve
		600, //ocircumflex
		600, //odieresis
		600, //otilde
		600, //uacute
		600, //ugrave
		600, //ucircumflex
		600, //udieresis
		600, //dagger
		600, //degree
		600, //cent
		600, //sterling
		600, //section
		600, //bullet
		600, //paragraph
		600, //germandbls
		600, //registered
		600, //copyright
		600, //trademark
		600, //acute
		600, //dieresis
		0, //
		600, //AE
		600, //Oslash
		0, //
		600, //plusminus
		0, //
		0, //
		600, //yen
		600, //mu
		0, //
		0, //
		0, //
		0, //
		0, //
		600, //ordfeminine
		600, //ordmasculine
		0, //
		600, //ae
		600, //oslash
		600, //questiondown
		600, //exclamdown
		600, //logicalnot
		0, //
		600, //florin
		0, //
		0, //
		0, //guilmotleft
		0, //guilmotright
		600, //ellipsis
		600, //space
		600, //Agrave
		600, //Atilde
		600, //Otilde
		600, //OE
		600, //oe
		600, //endash
		600, //emdash
		600, //quotedblleft
		600, //quotedblright
		600, //quoteleft
		600, //quoteright
		600, //divide
		0, //
		600, //ydieresis
		600, //Ydieresis
		600, //fraction
		600, //currency
		600, //guilsinglleft
		600, //guilsinglright
		600, //fi
		600, //fl
		600, //daggerdbl
		600, //periodcentered
		600, //quotesinglbase
		600, //quotedblbase
		600, //perthousand
		600, //Acircumflex
		600, //Ecircumflex
		600, //Aacute
		600, //Edieresis
		600, //Egrave
		600, //Iacute
		600, //Icircumflex
		600, //Idieresis
		600, //Igrave
		600, //Oacute
		600, //Ocircumflex
		0, //
		600, //Ograve
		600, //Uacute
		600, //Ucircumflex
		600, //Ugrave
		600, //dotlessi
		600, //circumflex
		600, //tilde
		600, //macron
		600, //breve
		600, //dotaccent
		600, //ring
		600, //cedilla
		600, //hungarumlaut
		600, //ogonek
		600 //caron
	};

// constructor

	/**
	 * Constructs a Metrics-object for this font.
	 * 
	 * @param	encoding		the encoding of the font
	 * @param	fontsize		the size of the font
	 *
	 * @since   iText0.30
	 */

	public CourierBold(int encoding, int fontsize) {
		super(encoding, fontsize);
		switch (encoding) {
		case STANDARD:
			setWidth(METRIC_STANDARD);
			break;
		case MAC_ROMAN:
			setWidth(METRIC_MAC_ROMAN);
			break;
		default:
			setWidth(METRIC_WIN_ANSI);
		}
	}

// implementing the abstract methods of the superclass.

	/**
	 * Gets the name of this font.
	 *
	 * @return		a <CODE>PdfName</CODE>
	 *
	 * @since   iText0.30
	 */
	
	public final PdfName name() {
		return PdfName.COURIER_BOLD;
	}

	/**
	 * Gets the kerning of a certain pair of characters.
	 * <P>
	 * In the case of <CODE>CourierBold</CODE>, the kerning is always <VAR>0</VAR>.
	 * 
	 * @param	char1	the first character
	 * @param	char2	the second character
	 * @return	the kerning
	 *
	 * @since   iText0.30
	 */

	public final int kerning(char char1, char char2) {
		return 0;
	} 

	/**
	 * Gets the fonttype of a font of the same family, but with a different style.
	 * 
	 * @param	style
	 * @return	a fonttype
	 *
	 * @since   iText0.30
	 */

	public final int getStyle(int style) {
		switch(style) {
		case NORMAL:
			return COURIER;
		case ITALIC:
			return COURIER_OBLIQUE;
		case BOLDITALIC:
			return COURIER_BOLDOBLIQUE;
		default:
			return COURIER_BOLD;
		}
	}
}
