/*
 * @(#)Helvetica.java				0.23 2000/02/02
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
 * This class contains the metrics of the font <VAR>Helvetica</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.2/hv______.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class Helvetica extends PdfFontMetrics {

// static membervariables

	/** Contains the widths of the Helvetica characters. */
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
		278, //space
		278, //exclam
		355, //quotedbl
		556, //numbersign
		556, //dollar
		889, //percent
		667, //ampersand
		222, //quoteright
		333, //parenleft
		333, //parenright
		389, //asterisk
		584, //plus
		278, //comma
		333, //hyphen
		278, //period
		278, //slash
		556, //zero
		556, //one
		556, //two
		556, //three
		556, //four
		556, //five
		556, //six
		556, //seven
		556, //eight
		556, //nine
		278, //colon
		278, //semicolon
		584, //less
		584, //equal
		584, //greater
		556, //question
		1015, //at
		667, //A
		667, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		722, //H
		278, //I
		500, //J
		667, //K
		556, //L
		833, //M
		722, //N
		778, //O
		667, //P
		778, //Q
		722, //R
		667, //S
		611, //T
		722, //U
		667, //V
		944, //W
		667, //X
		667, //Y
		611, //Z
		278, //bracketleft
		278, //backslash
		278, //bracketright
		469, //asciicircum
		556, //underscore
		222, //quoteleft
		556, //a
		556, //b
		500, //c
		556, //d
		556, //e
		278, //f
		556, //g
		556, //h
		222, //i
		222, //j
		500, //k
		222, //l
		833, //m
		556, //n
		556, //o
		556, //p
		556, //q
		333, //r
		500, //s
		278, //t
		556, //u
		500, //v
		722, //w
		500, //x
		500, //y
		500, //z
		334, //braceleft
		260, //bar
		334, //braceright
		584, //asciitilde
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
		333, //exclamdown
		556, //cent
		556, //sterling
		167, //fraction
		556, //yen
		556, //florin
		556, //section
		556, //currency
		191, //quotesingle
		333, //quotedblleft
		556, //guillemotleft
		333, //guilsinglleft
		333, //guilsinglright
		500, //fi
		500, //fl
		0, //
		556, //endash
		556, //dagger
		556, //daggerdbl
		278, //periodcentered
		0, //
		537, //paragraph
		350, //bullet
		222, //quotesinglbase
		333, //quotedblbase
		333, //quotedblright
		556, //guillemotright
		1000, //ellipsis
		1000, //perthousand
		0, //
		611, //questiondown
		0, //
		333, //grave
		333, //acute
		333, //circumflex
		333, //tilde
		333, //macron
		333, //breve
		333, //dotaccent
		333, //dieresis
		0, //
		333, //ring
		333, //cedilla
		0, //
		0, //hangarumlaut
		333, //ogonek
		333, //caron
		1000, //emdash
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
		1000, //AE
		0, //
		370, //ordfeminine
		0, //
		0, //
		0, //
		0, //
		556, //Lslash
		778, //Oslash
		1000, //OE
		365, //ordmasculine
		0, //
		0, //
		0, //
		0, //
		0, //
		889, //ae
		0, //
		0, //
		0, //
		278, //dotlessi
		0, //
		0, //
		222, //lslash
		611, //oslash
		944, //oe
		611, //germandbls
		0, //
		0, //
		0, //
		0 //
	};

	/** Contains the kerning information of certain pairs of characters. */
	public final static int[][] KERNING_STANDARD =
	{
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 84, -50 // space T
		, 86, -50 // space V
		, 87, -40 // space W
		, 89, -90 // space Y
		, 96, -60 // space quoteleft
		, 170, -30 // space quotedblleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -70 // quoteright space
		, 39, -57 // quoteright quoteright
		, 100, -50 // quoteright d
		, 114, -50 // quoteright r
		, 115, -50 // quoteright s
		},
		null,
		null,
		null,
		null,
		{ 39, -100 // comma quoteright
		, 186, -100 // comma quotedblright
		},
		null,
		{ 32, -60 // period space
		, 39, -100 // period quoteright
		, 186, -100 // period quotedblright
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -50 // colon space
		},
		{ 32, -50 // semicolon space
		},
		null,
		null,
		null,
		null,
		null,
		{ 67, -30 // A C
		, 71, -30 // A G
		, 79, -30 // A O
		, 81, -30 // A Q
		, 84, -120 // A T
		, 85, -50 // A U
		, 86, -70 // A V
		, 87, -50 // A W
		, 89, -100 // A Y
		, 117, -30 // A u
		, 118, -40 // A v
		, 119, -40 // A w
		, 121, -40 // A y
		},
		{ 44, -20 // B comma
		, 46, -20 // B period
		, 85, -10 // B U
		},
		{ 44, -30 // C comma
		, 46, -30 // C period
		},
		{ 44, -70 // D comma
		, 46, -70 // D period
		, 65, -40 // D A
		, 86, -70 // D V
		, 87, -40 // D W
		, 89, -90 // D Y
		},
		null,
		{ 44, -150 // F comma
		, 46, -150 // F period
		, 65, -80 // F A
		, 97, -50 // F a
		, 101, -30 // F e
		, 111, -30 // F o
		, 114, -45 // F r
		},
		null,
		null,
		null,
		{ 44, -30 // J comma
		, 46, -30 // J period
		, 65, -20 // J A
		, 97, -20 // J a
		, 117, -20 // J u
		},
		{ 79, -50 // K O
		, 101, -40 // K e
		, 111, -40 // K o
		, 117, -30 // K u
		, 121, -50 // K y
		},
		{ 39, -160 // L quoteright
		, 84, -110 // L T
		, 86, -110 // L V
		, 87, -70 // L W
		, 89, -140 // L Y
		, 121, -30 // L y
		, 186, -140 // L quotedblright
		},
		null,
		null,
		{ 44, -40 // O comma
		, 46, -40 // O period
		, 65, -20 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -30 // O W
		, 88, -60 // O X
		, 89, -70 // O Y
		},
		{ 44, -180 // P comma
		, 46, -180 // P period
		, 65, -120 // P A
		, 97, -40 // P a
		, 101, -50 // P e
		, 111, -50 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -20 // R O
		, 84, -30 // R T
		, 85, -40 // R U
		, 86, -50 // R V
		, 87, -30 // R W
		, 89, -50 // R Y
		},
		{ 44, -20 // S comma
		, 46, -20 // S period
		},
		{ 44, -120 // T comma
		, 45, -140 // T hyphen
		, 46, -120 // T period
		, 58, -20 // T colon
		, 59, -20 // T semicolon
		, 65, -120 // T A
		, 79, -40 // T O
		, 97, -120 // T a
		, 101, -120 // T e
		, 111, -120 // T o
		, 114, -120 // T r
		, 117, -120 // T u
		, 119, -120 // T w
		, 121, -120 // T y
		},
		{ 44, -40 // U comma
		, 46, -40 // U period
		, 65, -40 // U A
		},
		{ 44, -125 // V comma
		, 45, -80 // V hyphen
		, 46, -125 // V period
		, 58, -40 // V colon
		, 59, -40 // V semicolon
		, 65, -80 // V A
		, 71, -40 // V G
		, 79, -40 // V O
		, 97, -70 // V a
		, 101, -80 // V e
		, 111, -80 // V o
		, 117, -70 // V u
		},
		{ 44, -80 // W comma
		, 45, -40 // W hyphen
		, 46, -80 // W period
		, 65, -50 // W A
		, 79, -20 // W O
		, 97, -40 // W a
		, 101, -30 // W e
		, 111, -30 // W o
		, 117, -30 // W u
		, 121, -20 // W y
		},
		null,
		{ 44, -140 // Y comma
		, 45, -140 // Y hyphen
		, 46, -140 // Y period
		, 58, -60 // Y colon
		, 59, -60 // Y semicolon
		, 65, -110 // Y A
		, 79, -85 // Y O
		, 97, -140 // Y a
		, 101, -140 // Y e
		, 105, -20 // Y i
		, 111, -140 // Y o
		, 117, -110 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 96, -57 // quoteleft quoteleft
		},
		{ 118, -20 // a v
		, 119, -20 // a w
		, 121, -30 // a y
		},
		{ 44, -40 // b comma
		, 46, -40 // b period
		, 98, -10 // b b
		, 108, -20 // b l
		, 117, -20 // b u
		, 118, -20 // b v
		, 121, -20 // b y
		},
		{ 44, -15 // c comma
		, 107, -20 // c k
		},
		null,
		{ 44, -15 // e comma
		, 46, -15 // e period
		, 118, -30 // e v
		, 119, -20 // e w
		, 120, -30 // e x
		, 121, -20 // e y
		},
		{ 39, 50 // f quoteright
		, 44, -30 // f comma
		, 46, -30 // f period
		, 97, -30 // f a
		, 101, -30 // f e
		, 111, -30 // f o
		, 186, 60 // f quotedblright
		, 245, -28 // f dotlessi
		},
		{ 114, -10 // g r
		},
		{ 121, -30 // h y
		},
		null,
		null,
		{ 101, -20 // k e
		, 111, -20 // k o
		},
		null,
		{ 117, -10 // m u
		, 121, -15 // m y
		},
		{ 117, -10 // n u
		, 118, -20 // n v
		, 121, -15 // n y
		},
		{ 44, -40 // o comma
		, 46, -40 // o period
		, 118, -15 // o v
		, 119, -15 // o w
		, 120, -30 // o x
		, 121, -30 // o y
		},
		{ 44, -35 // p comma
		, 46, -35 // p period
		, 121, -30 // p y
		},
		null,
		{ 44, -50 // r comma
		, 46, -50 // r period
		, 58, 30 // r colon
		, 59, 30 // r semicolon
		, 97, -10 // r a
		, 105, 15 // r i
		, 107, 15 // r k
		, 108, 15 // r l
		, 109, 25 // r m
		, 110, 25 // r n
		, 112, 30 // r p
		, 116, 40 // r t
		, 117, 15 // r u
		, 118, 30 // r v
		, 121, 30 // r y
		},
		{ 44, -15 // s comma
		, 46, -15 // s period
		, 119, -30 // s w
		},
		null,
		null,
		{ 44, -80 // v comma
		, 46, -80 // v period
		, 97, -25 // v a
		, 101, -25 // v e
		, 111, -25 // v o
		},
		{ 44, -60 // w comma
		, 46, -60 // w period
		, 97, -15 // w a
		, 101, -10 // w e
		, 111, -10 // w o
		},
		{ 101, -30 // x e
		},
		{ 44, -100 // y comma
		, 46, -100 // y period
		, 97, -20 // y a
		, 101, -20 // y e
		, 111, -20 // y o
		},
		{ 101, -15 // z e
		, 111, -15 // z o
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -40 // quotedblright space
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 44, -95 // oslash comma
		, 46, -95 // oslash period
		, 97, -55 // oslash a
		, 98, -55 // oslash b
		, 99, -55 // oslash c
		, 100, -55 // oslash d
		, 101, -55 // oslash e
		, 102, -55 // oslash f
		, 103, -55 // oslash g
		, 104, -55 // oslash h
		, 105, -55 // oslash i
		, 106, -55 // oslash j
		, 107, -55 // oslash k
		, 108, -55 // oslash l
		, 109, -55 // oslash m
		, 110, -55 // oslash n
		, 111, -55 // oslash o
		, 112, -55 // oslash p
		, 113, -55 // oslash q
		, 114, -55 // oslash r
		, 115, -55 // oslash s
		, 116, -55 // oslash t
		, 117, -55 // oslash u
		, 118, -70 // oslash v
		, 119, -70 // oslash w
		, 120, -85 // oslash x
		, 121, -70 // oslash y
		, 122, -55 // oslash z
		},
		null,
		null,
		null,
		null,
		null,
		null
	};

	/** Contains the widths of the Helvetica characters. */
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
		278, //space
		278, //exclam
		355, //quotedbl
		556, //numbersign
		556, //dollar
		889, //percent
		667, //ampersand
		222, //quoteright
		333, //parenleft
		333, //parenright
		389, //asterisk
		584, //plus
		278, //comma
		333, //hyphen
		278, //period
		278, //slash
		556, //zero
		556, //one
		556, //two
		556, //three
		556, //four
		556, //five
		556, //six
		556, //seven
		556, //eight
		556, //nine
		278, //colon
		278, //semicolon
		584, //less
		584, //equal
		584, //greater
		556, //question
		1015, //at
		667, //A
		667, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		722, //H
		278, //I
		500, //J
		667, //K
		556, //L
		833, //M
		722, //N
		778, //O
		667, //P
		778, //Q
		722, //R
		667, //S
		611, //T
		722, //U
		667, //V
		944, //W
		667, //X
		667, //Y
		611, //Z
		278, //bracketleft
		278, //backslash
		278, //bracketright
		469, //asciicircum
		556, //underscore
		333, //grave
		556, //a
		556, //b
		500, //c
		556, //d
		556, //e
		278, //f
		556, //g
		556, //h
		222, //i
		222, //j
		500, //k
		222, //l
		833, //m
		556, //n
		556, //o
		556, //p
		556, //q
		333, //r
		500, //s
		278, //t
		556, //u
		500, //v
		722, //w
		500, //x
		500, //y
		500, //z
		334, //braceleft
		260, //bar
		334, //braceright
		584, //asciitilde
		350, //bullet
		350, //bullet
		350, //bullet
		222, //quotesinglbase
		556, //florin
		333, //quotedblbase
		1000, //ellipsis
		556, //dagger
		556, //daggerdbl
		333, //circumflex
		1000, //perthousand
		667, //Scaron
		333, //guilsinglleft
		1000, //OE
		350, //bullet
		350, //bullet
		350, //bullet
		350, //bullet
		222, //quoteleft
		222, //quoteright
		333, //quotedblleft
		333, //quotedblright
		350, //bullet
		556, //endash
		1000, //emdash
		333, //tilde
		1000, //trademark
		500, //scaron
		333, //guilsinglright
		944, //oe
		350, //bullet
		500, //zcaron
		667, //Ydieresis
		278, //space
		333, //exclamdown
		556, //cent
		556, //sterling
		556, //currency
		556, //yen
		260, //brokenbar
		556, //section
		333, //dieresis
		737, //copyright
		370, //ordfeminine
		556, //guillemotleft
		584, //logicalnot
		333, //hyphen
		737, //registered
		333, //macron
		400, //degree
		584, //plusminus
		333, //twosuperior
		333, //threesuperior
		333, //acute
		556, //mu
		537, //paragraph
		278, //periodcentered
		333, //cedilla
		333, //onesuperior
		365, //ordmasculine
		556, //guillemotright
		834, //onequarter
		834, //onehalf
		834, //threequarters
		611, //questiondown
		667, //Agrave
		667, //Aacute
		667, //Acircumflex
		667, //Atilde
		667, //Adieresis
		667, //Aring
		1000, //AE
		722, //Ccedilla
		667, //Egrave
		667, //Eacute
		667, //Ecircumflex
		667, //Edieresis
		278, //Igrave
		278, //Iacute
		278, //Icircumflex
		278, //Idieresis
		722, //Eth
		722, //Ntilde
		778, //Ograve
		778, //Oacute
		778, //Ocircumflex
		778, //Otilde
		778, //Odieresis
		584, //multiply
		778, //Oslash
		722, //Ugrave
		722, //Uacute
		722, //Ucircumflex
		722, //Udieresis
		667, //Yacute
		667, //Thorn
		611, //germandbls
		556, //agrave
		556, //aacute
		556, //acircumflex
		556, //atilde
		556, //adieresis
		556, //aring
		889, //ae
		500, //ccedilla
		556, //egrave
		556, //eacute
		556, //ecircumflex
		556, //edieresis
		278, //igrave
		278, //iacute
		278, //icircumflex
		278, //idieresis
		556, //eth
		556, //ntilde
		556, //ograve
		556, //oacute
		556, //ocircumflex
		556, //otilde
		556, //odieresis
		584, //divide
		611, //oslash
		556, //ugrave
		556, //uacute
		556, //ucircumflex
		556, //udieresis
		500, //yacute
		556, //thorn
		500 //ydieresis
	};

	/** Contains the kerning information of certain pairs of characters. */
	public final static int[][] KERNING_WIN_ANSI =
	{
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 84, -50 // space T
		, 86, -50 // space V
		, 87, -40 // space W
		, 89, -90 // space Y
		, 145, -60 // space quoteleft
		, 147, -30 // space quotedblleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -70 // quoteright space
		, 39, -57 // quoteright quoteright
		, 100, -50 // quoteright d
		, 114, -50 // quoteright r
		, 115, -50 // quoteright s
		, 146, -57 // quoteright quoteright
		, 160, -70 // quoteright space
		},
		null,
		null,
		null,
		null,
		{ 39, -100 // comma quoteright
		, 146, -100 // comma quoteright
		, 148, -100 // comma quotedblright
		},
		null,
		{ 32, -60 // period space
		, 39, -100 // period quoteright
		, 146, -100 // period quoteright
		, 148, -100 // period quotedblright
		, 160, -60 // period space
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -50 // colon space
		, 160, -50 // colon space
		},
		{ 32, -50 // semicolon space
		, 160, -50 // semicolon space
		},
		null,
		null,
		null,
		null,
		null,
		{ 67, -30 // A C
		, 71, -30 // A G
		, 79, -30 // A O
		, 81, -30 // A Q
		, 84, -120 // A T
		, 85, -50 // A U
		, 86, -70 // A V
		, 87, -50 // A W
		, 89, -100 // A Y
		, 117, -30 // A u
		, 118, -40 // A v
		, 119, -40 // A w
		, 121, -40 // A y
		},
		{ 44, -20 // B comma
		, 46, -20 // B period
		, 85, -10 // B U
		},
		{ 44, -30 // C comma
		, 46, -30 // C period
		},
		{ 44, -70 // D comma
		, 46, -70 // D period
		, 65, -40 // D A
		, 86, -70 // D V
		, 87, -40 // D W
		, 89, -90 // D Y
		},
		null,
		{ 44, -150 // F comma
		, 46, -150 // F period
		, 65, -80 // F A
		, 97, -50 // F a
		, 101, -30 // F e
		, 111, -30 // F o
		, 114, -45 // F r
		},
		null,
		null,
		null,
		{ 44, -30 // J comma
		, 46, -30 // J period
		, 65, -20 // J A
		, 97, -20 // J a
		, 117, -20 // J u
		},
		{ 79, -50 // K O
		, 101, -40 // K e
		, 111, -40 // K o
		, 117, -30 // K u
		, 121, -50 // K y
		},
		{ 39, -160 // L quoteright
		, 84, -110 // L T
		, 86, -110 // L V
		, 87, -70 // L W
		, 89, -140 // L Y
		, 121, -30 // L y
		, 146, -160 // L quoteright
		, 148, -140 // L quotedblright
		},
		null,
		null,
		{ 44, -40 // O comma
		, 46, -40 // O period
		, 65, -20 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -30 // O W
		, 88, -60 // O X
		, 89, -70 // O Y
		},
		{ 44, -180 // P comma
		, 46, -180 // P period
		, 65, -120 // P A
		, 97, -40 // P a
		, 101, -50 // P e
		, 111, -50 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -20 // R O
		, 84, -30 // R T
		, 85, -40 // R U
		, 86, -50 // R V
		, 87, -30 // R W
		, 89, -50 // R Y
		},
		{ 44, -20 // S comma
		, 46, -20 // S period
		},
		{ 44, -120 // T comma
		, 45, -140 // T hyphen
		, 46, -120 // T period
		, 58, -20 // T colon
		, 59, -20 // T semicolon
		, 65, -120 // T A
		, 79, -40 // T O
		, 97, -120 // T a
		, 101, -120 // T e
		, 111, -120 // T o
		, 114, -120 // T r
		, 117, -120 // T u
		, 119, -120 // T w
		, 121, -120 // T y
		, 173, -140 // T hyphen
		},
		{ 44, -40 // U comma
		, 46, -40 // U period
		, 65, -40 // U A
		},
		{ 44, -125 // V comma
		, 45, -80 // V hyphen
		, 46, -125 // V period
		, 58, -40 // V colon
		, 59, -40 // V semicolon
		, 65, -80 // V A
		, 71, -40 // V G
		, 79, -40 // V O
		, 97, -70 // V a
		, 101, -80 // V e
		, 111, -80 // V o
		, 117, -70 // V u
		, 173, -80 // V hyphen
		},
		{ 44, -80 // W comma
		, 45, -40 // W hyphen
		, 46, -80 // W period
		, 65, -50 // W A
		, 79, -20 // W O
		, 97, -40 // W a
		, 101, -30 // W e
		, 111, -30 // W o
		, 117, -30 // W u
		, 121, -20 // W y
		, 173, -40 // W hyphen
		},
		null,
		{ 44, -140 // Y comma
		, 45, -140 // Y hyphen
		, 46, -140 // Y period
		, 58, -60 // Y colon
		, 59, -60 // Y semicolon
		, 65, -110 // Y A
		, 79, -85 // Y O
		, 97, -140 // Y a
		, 101, -140 // Y e
		, 105, -20 // Y i
		, 111, -140 // Y o
		, 117, -110 // Y u
		, 173, -140 // Y hyphen
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 118, -20 // a v
		, 119, -20 // a w
		, 121, -30 // a y
		},
		{ 44, -40 // b comma
		, 46, -40 // b period
		, 98, -10 // b b
		, 108, -20 // b l
		, 117, -20 // b u
		, 118, -20 // b v
		, 121, -20 // b y
		},
		{ 44, -15 // c comma
		, 107, -20 // c k
		},
		null,
		{ 44, -15 // e comma
		, 46, -15 // e period
		, 118, -30 // e v
		, 119, -20 // e w
		, 120, -30 // e x
		, 121, -20 // e y
		},
		{ 39, 50 // f quoteright
		, 44, -30 // f comma
		, 46, -30 // f period
		, 97, -30 // f a
		, 101, -30 // f e
		, 111, -30 // f o
		, 146, 50 // f quoteright
		, 148, 60 // f quotedblright
		},
		{ 114, -10 // g r
		},
		{ 121, -30 // h y
		},
		null,
		null,
		{ 101, -20 // k e
		, 111, -20 // k o
		},
		null,
		{ 117, -10 // m u
		, 121, -15 // m y
		},
		{ 117, -10 // n u
		, 118, -20 // n v
		, 121, -15 // n y
		},
		{ 44, -40 // o comma
		, 46, -40 // o period
		, 118, -15 // o v
		, 119, -15 // o w
		, 120, -30 // o x
		, 121, -30 // o y
		},
		{ 44, -35 // p comma
		, 46, -35 // p period
		, 121, -30 // p y
		},
		null,
		{ 44, -50 // r comma
		, 46, -50 // r period
		, 58, 30 // r colon
		, 59, 30 // r semicolon
		, 97, -10 // r a
		, 105, 15 // r i
		, 107, 15 // r k
		, 108, 15 // r l
		, 109, 25 // r m
		, 110, 25 // r n
		, 112, 30 // r p
		, 116, 40 // r t
		, 117, 15 // r u
		, 118, 30 // r v
		, 121, 30 // r y
		},
		{ 44, -15 // s comma
		, 46, -15 // s period
		, 119, -30 // s w
		},
		null,
		null,
		{ 44, -80 // v comma
		, 46, -80 // v period
		, 97, -25 // v a
		, 101, -25 // v e
		, 111, -25 // v o
		},
		{ 44, -60 // w comma
		, 46, -60 // w period
		, 97, -15 // w a
		, 101, -10 // w e
		, 111, -10 // w o
		},
		{ 101, -30 // x e
		},
		{ 44, -100 // y comma
		, 46, -100 // y period
		, 97, -20 // y a
		, 101, -20 // y e
		, 111, -20 // y o
		},
		{ 101, -15 // z e
		, 111, -15 // z o
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 145, -57 // quoteleft quoteleft
		},
		{ 32, -70 // quoteright space
		, 39, -57 // quoteright quoteright
		, 100, -50 // quoteright d
		, 114, -50 // quoteright r
		, 115, -50 // quoteright s
		, 146, -57 // quoteright quoteright
		, 160, -70 // quoteright space
		},
		null,
		{ 32, -40 // quotedblright space
		, 160, -40 // quotedblright space
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 84, -50 // space T
		, 86, -50 // space V
		, 87, -40 // space W
		, 89, -90 // space Y
		, 145, -60 // space quoteleft
		, 147, -30 // space quotedblleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 44, -95 // oslash comma
		, 46, -95 // oslash period
		, 97, -55 // oslash a
		, 98, -55 // oslash b
		, 99, -55 // oslash c
		, 100, -55 // oslash d
		, 101, -55 // oslash e
		, 102, -55 // oslash f
		, 103, -55 // oslash g
		, 104, -55 // oslash h
		, 105, -55 // oslash i
		, 106, -55 // oslash j
		, 107, -55 // oslash k
		, 108, -55 // oslash l
		, 109, -55 // oslash m
		, 110, -55 // oslash n
		, 111, -55 // oslash o
		, 112, -55 // oslash p
		, 113, -55 // oslash q
		, 114, -55 // oslash r
		, 115, -55 // oslash s
		, 116, -55 // oslash t
		, 117, -55 // oslash u
		, 118, -70 // oslash v
		, 119, -70 // oslash w
		, 120, -85 // oslash x
		, 121, -70 // oslash y
		, 122, -55 // oslash z
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null
	};
			
	/** Contains the widths of the Helvetica characters. */
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
		278, //space
		278, //exclam
		355, //quotedbl
		556, //numbersign
		556, //dollar
		889, //percent
		667, //ampersand
		222, //quoteright
		333, //parenleft
		333, //parenright
		389, //asterisk
		584, //plus
		278, //comma
		333, //hyphen
		278, //period
		278, //slash
		556, //zero
		556, //one
		556, //two
		556, //three
		556, //four
		556, //five
		556, //six
		556, //seven
		556, //eight
		556, //nine
		278, //colon
		278, //semicolon
		584, //less
		584, //equal
		584, //greater
		556, //question
		1015, //at
		667, //A
		667, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		722, //H
		278, //I
		500, //J
		667, //K
		556, //L
		833, //M
		722, //N
		778, //O
		667, //P
		778, //Q
		722, //R
		667, //S
		611, //T
		722, //U
		667, //V
		944, //W
		667, //X
		667, //Y
		611, //Z
		278, //bracketleft
		278, //backslash
		278, //bracketright
		469, //asciicircum
		556, //underscore
		333, //grave
		556, //a
		556, //b
		500, //c
		556, //d
		556, //e
		278, //f
		556, //g
		556, //h
		222, //i
		222, //j
		500, //k
		222, //l
		833, //m
		556, //n
		556, //o
		556, //p
		556, //q
		333, //r
		500, //s
		278, //t
		556, //u
		500, //v
		722, //w
		500, //x
		500, //y
		500, //z
		334, //braceleft
		260, //bar
		334, //braceright
		584, //asciitilde
		0, //
		667, //Adieresis
		667, //Aring
		722, //Ccedilla
		667, //Eacute
		722, //Ntilde
		778, //Odieresis
		722, //Udieresis
		556, //aacute
		556, //agrave
		556, //acircumflex
		556, //adieresis
		556, //atilde
		556, //aring
		500, //ccedilla
		556, //eacute
		556, //egrave
		556, //ecircumflex
		556, //edieresis
		278, //iacute
		278, //igrave
		278, //icircumflex
		278, //idieresis
		556, //ntilde
		556, //oacute
		556, //ograve
		556, //ocircumflex
		556, //odieresis
		556, //otilde
		556, //uacute
		556, //ugrave
		556, //ucircumflex
		556, //udieresis
		556, //dagger
		400, //degree
		556, //cent
		556, //sterling
		556, //section
		350, //bullet
		537, //paragraph
		611, //germandbls
		737, //registered
		737, //copyright
		1000, //trademark
		333, //acute
		333, //dieresis
		0, //
		1000, //AE
		778, //Oslash
		0, //
		584, //plusminus
		0, //
		0, //
		556, //yen
		556, //mu
		0, //
		0, //
		0, //
		0, //
		0, //
		370, //ordfeminine
		365, //ordmasculine
		0, //
		889, //ae
		611, //oslash
		611, //questiondown
		333, //exclamdown
		584, //logicalnot
		0, //
		556, //florin
		0, //
		0, //
		0, //guilmotleft
		0, //guilmotright
		1000, //ellipsis
		278, //space
		667, //Agrave
		667, //Atilde
		778, //Otilde
		1000, //OE
		944, //oe
		556, //endash
		1000, //emdash
		333, //quotedblleft
		333, //quotedblright
		222, //quoteleft
		222, //quoteright
		584, //divide
		0, //
		500, //ydieresis
		667, //Ydieresis
		167, //fraction
		556, //currency
		333, //guilsinglleft
		333, //guilsinglright
		500, //fi
		500, //fl
		556, //daggerdbl
		278, //periodcentered
		222, //quotesinglbase
		333, //quotedblbase
		1000, //perthousand
		667, //Acircumflex
		667, //Ecircumflex
		667, //Aacute
		667, //Edieresis
		667, //Egrave
		278, //Iacute
		278, //Icircumflex
		278, //Idieresis
		278, //Igrave
		778, //Oacute
		778, //Ocircumflex
		0, //
		778, //Ograve
		722, //Uacute
		722, //Ucircumflex
		722, //Ugrave
		278, //dotlessi
		333, //circumflex
		333, //tilde
		333, //macron
		333, //breve
		333, //dotaccent
		333, //ring
		333, //cedilla
		333, //hungarumlaut
		333, //ogonek
		333 //caron
	};

	/** Contains the kerning information of certain pairs of characters. */
	public final static int[][] KERNING_MAC_ROMAN =
	{
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 84, -50 // space T
		, 86, -50 // space V
		, 87, -40 // space W
		, 89, -90 // space Y
		, 210, -30 // space quotedblleft
		, 212, -60 // space quoteleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -70 // quoteright space
		, 39, -57 // quoteright quoteright
		, 100, -50 // quoteright d
		, 114, -50 // quoteright r
		, 115, -50 // quoteright s
		, 202, -70 // quoteright space
		, 213, -57 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		{ 39, -100 // comma quoteright
		, 211, -100 // comma quotedblright
		, 213, -100 // comma quoteright
		},
		null,
		{ 32, -60 // period space
		, 39, -100 // period quoteright
		, 202, -60 // period space
		, 211, -100 // period quotedblright
		, 213, -100 // period quoteright
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -50 // colon space
		, 202, -50 // colon space
		},
		{ 32, -50 // semicolon space
		, 202, -50 // semicolon space
		},
		null,
		null,
		null,
		null,
		null,
		{ 67, -30 // A C
		, 71, -30 // A G
		, 79, -30 // A O
		, 81, -30 // A Q
		, 84, -120 // A T
		, 85, -50 // A U
		, 86, -70 // A V
		, 87, -50 // A W
		, 89, -100 // A Y
		, 117, -30 // A u
		, 118, -40 // A v
		, 119, -40 // A w
		, 121, -40 // A y
		},
		{ 44, -20 // B comma
		, 46, -20 // B period
		, 85, -10 // B U
		},
		{ 44, -30 // C comma
		, 46, -30 // C period
		},
		{ 44, -70 // D comma
		, 46, -70 // D period
		, 65, -40 // D A
		, 86, -70 // D V
		, 87, -40 // D W
		, 89, -90 // D Y
		},
		null,
		{ 44, -150 // F comma
		, 46, -150 // F period
		, 65, -80 // F A
		, 97, -50 // F a
		, 101, -30 // F e
		, 111, -30 // F o
		, 114, -45 // F r
		},
		null,
		null,
		null,
		{ 44, -30 // J comma
		, 46, -30 // J period
		, 65, -20 // J A
		, 97, -20 // J a
		, 117, -20 // J u
		},
		{ 79, -50 // K O
		, 101, -40 // K e
		, 111, -40 // K o
		, 117, -30 // K u
		, 121, -50 // K y
		},
		{ 39, -160 // L quoteright
		, 84, -110 // L T
		, 86, -110 // L V
		, 87, -70 // L W
		, 89, -140 // L Y
		, 121, -30 // L y
		, 211, -140 // L quotedblright
		, 213, -160 // L quoteright
		},
		null,
		null,
		{ 44, -40 // O comma
		, 46, -40 // O period
		, 65, -20 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -30 // O W
		, 88, -60 // O X
		, 89, -70 // O Y
		},
		{ 44, -180 // P comma
		, 46, -180 // P period
		, 65, -120 // P A
		, 97, -40 // P a
		, 101, -50 // P e
		, 111, -50 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -20 // R O
		, 84, -30 // R T
		, 85, -40 // R U
		, 86, -50 // R V
		, 87, -30 // R W
		, 89, -50 // R Y
		},
		{ 44, -20 // S comma
		, 46, -20 // S period
		},
		{ 44, -120 // T comma
		, 45, -140 // T hyphen
		, 46, -120 // T period
		, 58, -20 // T colon
		, 59, -20 // T semicolon
		, 65, -120 // T A
		, 79, -40 // T O
		, 97, -120 // T a
		, 101, -120 // T e
		, 111, -120 // T o
		, 114, -120 // T r
		, 117, -120 // T u
		, 119, -120 // T w
		, 121, -120 // T y
		},
		{ 44, -40 // U comma
		, 46, -40 // U period
		, 65, -40 // U A
		},
		{ 44, -125 // V comma
		, 45, -80 // V hyphen
		, 46, -125 // V period
		, 58, -40 // V colon
		, 59, -40 // V semicolon
		, 65, -80 // V A
		, 71, -40 // V G
		, 79, -40 // V O
		, 97, -70 // V a
		, 101, -80 // V e
		, 111, -80 // V o
		, 117, -70 // V u
		},
		{ 44, -80 // W comma
		, 45, -40 // W hyphen
		, 46, -80 // W period
		, 65, -50 // W A
		, 79, -20 // W O
		, 97, -40 // W a
		, 101, -30 // W e
		, 111, -30 // W o
		, 117, -30 // W u
		, 121, -20 // W y
		},
		null,
		{ 44, -140 // Y comma
		, 45, -140 // Y hyphen
		, 46, -140 // Y period
		, 58, -60 // Y colon
		, 59, -60 // Y semicolon
		, 65, -110 // Y A
		, 79, -85 // Y O
		, 97, -140 // Y a
		, 101, -140 // Y e
		, 105, -20 // Y i
		, 111, -140 // Y o
		, 117, -110 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 118, -20 // a v
		, 119, -20 // a w
		, 121, -30 // a y
		},
		{ 44, -40 // b comma
		, 46, -40 // b period
		, 98, -10 // b b
		, 108, -20 // b l
		, 117, -20 // b u
		, 118, -20 // b v
		, 121, -20 // b y
		},
		{ 44, -15 // c comma
		, 107, -20 // c k
		},
		null,
		{ 44, -15 // e comma
		, 46, -15 // e period
		, 118, -30 // e v
		, 119, -20 // e w
		, 120, -30 // e x
		, 121, -20 // e y
		},
		{ 39, 50 // f quoteright
		, 44, -30 // f comma
		, 46, -30 // f period
		, 97, -30 // f a
		, 101, -30 // f e
		, 111, -30 // f o
		, 211, 60 // f quotedblright
		, 213, 50 // f quoteright
		, 245, -28 // f dotlessi
		},
		{ 114, -10 // g r
		},
		{ 121, -30 // h y
		},
		null,
		null,
		{ 101, -20 // k e
		, 111, -20 // k o
		},
		null,
		{ 117, -10 // m u
		, 121, -15 // m y
		},
		{ 117, -10 // n u
		, 118, -20 // n v
		, 121, -15 // n y
		},
		{ 44, -40 // o comma
		, 46, -40 // o period
		, 118, -15 // o v
		, 119, -15 // o w
		, 120, -30 // o x
		, 121, -30 // o y
		},
		{ 44, -35 // p comma
		, 46, -35 // p period
		, 121, -30 // p y
		},
		null,
		{ 44, -50 // r comma
		, 46, -50 // r period
		, 58, 30 // r colon
		, 59, 30 // r semicolon
		, 97, -10 // r a
		, 105, 15 // r i
		, 107, 15 // r k
		, 108, 15 // r l
		, 109, 25 // r m
		, 110, 25 // r n
		, 112, 30 // r p
		, 116, 40 // r t
		, 117, 15 // r u
		, 118, 30 // r v
		, 121, 30 // r y
		},
		{ 44, -15 // s comma
		, 46, -15 // s period
		, 119, -30 // s w
		},
		null,
		null,
		{ 44, -80 // v comma
		, 46, -80 // v period
		, 97, -25 // v a
		, 101, -25 // v e
		, 111, -25 // v o
		},
		{ 44, -60 // w comma
		, 46, -60 // w period
		, 97, -15 // w a
		, 101, -10 // w e
		, 111, -10 // w o
		},
		{ 101, -30 // x e
		},
		{ 44, -100 // y comma
		, 46, -100 // y period
		, 97, -20 // y a
		, 101, -20 // y e
		, 111, -20 // y o
		},
		{ 101, -15 // z e
		, 111, -15 // z o
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 44, -95 // oslash comma
		, 46, -95 // oslash period
		, 97, -55 // oslash a
		, 98, -55 // oslash b
		, 99, -55 // oslash c
		, 100, -55 // oslash d
		, 101, -55 // oslash e
		, 102, -55 // oslash f
		, 103, -55 // oslash g
		, 104, -55 // oslash h
		, 105, -55 // oslash i
		, 106, -55 // oslash j
		, 107, -55 // oslash k
		, 108, -55 // oslash l
		, 109, -55 // oslash m
		, 110, -55 // oslash n
		, 111, -55 // oslash o
		, 112, -55 // oslash p
		, 113, -55 // oslash q
		, 114, -55 // oslash r
		, 115, -55 // oslash s
		, 116, -55 // oslash t
		, 117, -55 // oslash u
		, 118, -70 // oslash v
		, 119, -70 // oslash w
		, 120, -85 // oslash x
		, 121, -70 // oslash y
		, 122, -55 // oslash z
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 84, -50 // space T
		, 86, -50 // space V
		, 87, -40 // space W
		, 89, -90 // space Y
		, 210, -30 // space quotedblleft
		, 212, -60 // space quoteleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -40 // quotedblright space
		, 202, -40 // quotedblright space
		},
		{ 212, -57 // quoteleft quoteleft
		},
		{ 32, -70 // quoteright space
		, 39, -57 // quoteright quoteright
		, 100, -50 // quoteright d
		, 114, -50 // quoteright r
		, 115, -50 // quoteright s
		, 202, -70 // quoteright space
		, 213, -57 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null
	};


// constructor

	/**
	 * Constructs a Metrics-object for this font.
	 * 
	 * @param	encoding		the encoding of the font
	 * @param	fontsize		the size of the font
	 *
	 * @since		iText0.30
	 */

	public Helvetica(int encoding, int fontsize) {
		super(encoding, fontsize);
		switch (encoding) {
		case STANDARD:
			setWidth(METRIC_STANDARD);
			setKerning(KERNING_STANDARD);
			break;
		case MAC_ROMAN:
			setWidth(METRIC_MAC_ROMAN);
			setKerning(KERNING_STANDARD);
			break;
		default:
			setWidth(METRIC_WIN_ANSI);
			setKerning(KERNING_WIN_ANSI);
		}
	}

// implementing the abstract methods of the superclass.

	/**
	 * Gets the name of this font.
	 *
	 * @return	a <CODE>PdfName</CODE>
	 *
	 * @since		iText0.30
	 */
	
	public final PdfName name() {
		return PdfName.HELVETICA;
	} 

	/**
	 * Gets the fonttype of a font of the same family, but with a different style.
	 * 
	 * @param	style
	 * @return	a fonttype
	 *
	 * @since		iText0.30
	 */

	public final int getStyle(int style) {
		switch(style) {
		case BOLD:
			return HELVETICA_BOLD;
		case ITALIC:
			return HELVETICA_OBLIQUE;
		case BOLDITALIC:
			return HELVETICA_BOLDOBLIQUE;
		default:
			return HELVETICA;
		}
	}
}

