/*
 * @(#)Symbol.java					0.31 2000/08/10
 *               iText0.3:			0.23 2000/02/14
 *       release iText0.35:         0.31 2000/08/11
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
 * This class contains the metrics of the font <VAR>Symbol</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.3/sy______.afm 
 *
 * @author  bruno@lowagie.com
 * @version 0.31 2000/08/10
 * @since   iText0.30  
 */

public class Symbol extends PdfFontMetrics {

// static membervariables

	/** Contains the widths of the Symbol characters. */
	public final static int[] METRIC =
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
		250, //space
		333, //exclam
		713, //universal
		500, //numbersign
		549, //existential
		833, //percent
		778, //ampersand
		439, //suchthat
		333, //parenleft
		333, //parenright
		500, //asteriskmath
		549, //plus
		250, //comma
		549, //minus
		250, //period
		278, //slash
		500, //zero
		500, //one
		500, //two
		500, //three
		500, //four
		500, //five
		500, //six
		500, //seven
		500, //eight
		500, //nine
		278, //colon
		278, //semicolon
		549, //less
		549, //equal
		549, //greater
		444, //question
		549, //congruent
		722, //Alpha
		667, //Beta
		722, //Chi
		612, //Delta
		611, //Epsilon
		763, //Phi
		603, //Gamma
		722, //Eta
		333, //Iota
		631, //theta1
		722, //Kappa
		686, //Lambda
		889, //Mu
		722, //Nu
		722, //Omicron
		768, //Pi
		741, //Theta
		556, //Rho
		592, //Sigma
		611, //Tau
		690, //Upsilon
		439, //sigma1
		768, //Omega
		645, //Xi
		795, //Psi
		611, //Zeta
		333, //bracketleft
		863, //therefore
		333, //bracketright
		658, //perpendicular
		500, //underscore
		500, //radicalex
		631, //alpha
		549, //beta
		549, //chi
		494, //delta
		439, //epsilon
		521, //phi
		411, //gamma
		603, //eta
		329, //iota
		603, //phi1
		549, //kappa
		549, //lambda
		576, //mu
		521, //nu
		549, //omicron
		549, //pi
		521, //theta
		549, //rho
		603, //sigma
		439, //tau
		576, //upsilon
		713, //omega1
		686, //omega
		493, //xi
		686, //psi
		494, //zeta
		480, //braceleft
		200, //bar
		480, //braceright
		549, //similar
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
		620, //Upsilon1
		247, //minute
		549, //lessequal
		167, //fraction
		713, //infinity
		500, //florin
		753, //club
		753, //diamond
		753, //heart
		753, //spade
		1042, //arrowboth
		987, //arrowleft
		603, //arrowup
		987, //arrowright
		603, //arrowdown
		400, //degree
		549, //plusminus
		411, //second
		549, //greaterequal
		549, //multiply
		713, //proportional
		494, //partialdiff
		460, //bullet
		549, //divide
		549, //notequal
		549, //equivalence
		549, //approxequal
		1000, //ellipsis
		603, //arrowvertex
		1000, //arrowhorizex
		658, //carriagereturn
		823, //aleph
		686, //Ifraktur
		795, //Rfraktur
		987, //weierstrass
		768, //circlemultiply
		768, //circleplus
		823, //emptyset
		768, //intersection
		768, //union
		713, //propersuperset
		713, //reflexsuperset
		713, //notsubset
		713, //propersubset
		713, //reflexsubset
		713, //element
		713, //notelement
		768, //angle
		713, //gradient
		790, //registerserif
		790, //copyrightserif
		890, //trademarkserif
		823, //product
		549, //radical
		250, //dotmath
		713, //logicalnot
		603, //logicaland
		603, //logicalor
		1042, //arrowdblboth
		987, //arrowdblleft
		603, //arrowdblup
		987, //arrowdblright
		603, //arrowdbldown
		494, //lozenge
		329, //angleleft
		790, //registersans
		790, //copyrightsans
		786, //trademarksans
		713, //summation
		384, //parenlefttp
		384, //parenleftex
		384, //parenleftbt
		384, //bracketlefttp
		384, //bracketleftex
		384, //bracketleftbt
		494, //bracelefttp
		494, //braceleftmid
		494, //braceleftbt
		494, //braceex
		0, //
		329, //angleright
		274, //integral
		686, //integraltp
		686, //integralex
		686, //integralbt
		384, //parenrighttp
		384, //parenrightex
		384, //parenrightbt
		384, //bracketrighttp
		384, //bracketrightex
		384, //bracketrightbt
		494, //bracerighttp
		494, //bracerightmid
		494, //bracerightbt
		0 //
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

	public Symbol(int encoding, int fontsize) {
		super(PdfFontMetrics.STANDARD, fontsize);
		setWidth(METRIC);
	}

// implementing the abstract methods of the superclass.

	/**
	 * Gets the name of this font.
	 *
	 * @return	a <CODE>PdfName</CODE>
	 *
	 * @since   iText0.30
	 */
	
	public final PdfName name() {
		return PdfName.SYMBOL;
	}

	/**
	 * Gets the kerning of a certain pair of characters.
	 * <P>
	 * The kerning is always <VAR>0</VAR> for the font <CODE>Symbol</CODE>.
	 *
	 * @param	character1	the first character
	 * @param	character2	the second character
	 * @return	the kerning
	 *
	 * @since   iText0.30
	 */

	public final int kerning(char character1, char character2) {
		return 0;
	} 

	/**
	 * Gets the fonttype of a font of the same family, but with a different style.
	 * <P>
	 * Symbol only comes in 1 style
	 * 
	 * @param	style
	 * @return	a fonttype
	 *
	 * @since   iText0.30
	 */

	public final int getStyle(int style) {
		return SYMBOL;
	}
}
