/*
 * @(#)TimesItalic.java				0.23 2000/02/02
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
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.2/tii_____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class TimesItalic extends PdfFontMetrics {

// static membervariables

	/** Contains the widths of the TimesItalic characters. */
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
		250, //space
		333, //exclam
		420, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		675, //plus
		250, //comma
		333, //hyphen
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
		333, //colon
		333, //semicolon
		675, //less
		675, //equal
		675, //greater
		500, //question
		920, //at
		611, //A
		611, //B
		667, //C
		722, //D
		611, //E
		611, //F
		722, //G
		722, //H
		333, //I
		444, //J
		667, //K
		556, //L
		833, //M
		667, //N
		722, //O
		611, //P
		722, //Q
		611, //R
		500, //S
		556, //T
		722, //U
		611, //V
		833, //W
		611, //X
		556, //Y
		556, //Z
		389, //bracketleft
		278, //backslash
		389, //bracketright
		422, //asciicircum
		500, //underscore
		333, //quoteleft
		500, //a
		500, //b
		444, //c
		500, //d
		444, //e
		278, //f
		500, //g
		500, //h
		278, //i
		278, //j
		444, //k
		278, //l
		722, //m
		500, //n
		500, //o
		500, //p
		500, //q
		389, //r
		389, //s
		278, //t
		500, //u
		444, //v
		667, //w
		444, //x
		444, //y
		389, //z
		400, //braceleft
		275, //bar
		400, //braceright
		541, //asciitilde
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
		389, //exclamdown
		500, //cent
		500, //sterling
		167, //fraction
		500, //yen
		500, //florin
		500, //section
		500, //currency
		214, //quotesingle
		556, //quotedblleft
		500, //guillemotleft
		333, //guilsinglleft
		333, //guilsinglright
		500, //fi
		500, //fl
		0, //
		500, //endash
		500, //dagger
		500, //daggerdbl
		250, //periodcentered
		0, //
		523, //paragraph
		350, //bullet
		333, //quotesinglbase
		556, //quotedblbase
		556, //quotedblright
		500, //guillemotright
		889, //ellipsis
		1000, //perthousand
		0, //
		500, //questiondown
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
		889, //emdash
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
		889, //AE
		0, //
		276, //ordfeminine
		0, //
		0, //
		0, //
		0, //
		556, //Lslash
		722, //Oslash
		944, //OE
		310, //ordmasculine
		0, //
		0, //
		0, //
		0, //
		0, //
		667, //ae
		0, //
		0, //
		0, //
		278, //dotlessi
		0, //
		0, //
		278, //lslash
		500, //oslash
		667, //oe
		500, //germandbls
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
		{ 65, -18 // space A
		, 84, -18 // space T
		, 86, -35 // space V
		, 87, -40 // space W
		, 89, -75 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -111 // quoteright space
		, 39, -111 // quoteright quoteright
		, 100, -25 // quoteright d
		, 114, -25 // quoteright r
		, 115, -40 // quoteright s
		, 116, -30 // quoteright t
		, 118, -10 // quoteright v
		},
		null,
		null,
		null,
		null,
		{ 39, -140 // comma quoteright
		, 186, -140 // comma quotedblright
		},
		null,
		{ 39, -140 // period quoteright
		, 186, -140 // period quotedblright
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
		{ 39, -37 // A quoteright
		, 67, -30 // A C
		, 71, -35 // A G
		, 79, -40 // A O
		, 81, -40 // A Q
		, 84, -37 // A T
		, 85, -50 // A U
		, 86, -105 // A V
		, 87, -95 // A W
		, 89, -55 // A Y
		, 117, -20 // A u
		, 118, -55 // A v
		, 119, -55 // A w
		, 121, -55 // A y
		},
		{ 65, -25 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -35 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -40 // D Y
		},
		null,
		{ 44, -135 // F comma
		, 46, -135 // F period
		, 65, -115 // F A
		, 97, -75 // F a
		, 101, -75 // F e
		, 105, -45 // F i
		, 111, -105 // F o
		, 114, -55 // F r
		},
		null,
		null,
		null,
		{ 44, -25 // J comma
		, 46, -25 // J period
		, 65, -40 // J A
		, 97, -35 // J a
		, 101, -25 // J e
		, 111, -25 // J o
		, 117, -35 // J u
		},
		{ 79, -50 // K O
		, 101, -35 // K e
		, 111, -40 // K o
		, 117, -40 // K u
		, 121, -40 // K y
		},
		{ 39, -37 // L quoteright
		, 84, -20 // L T
		, 86, -55 // L V
		, 87, -55 // L W
		, 89, -20 // L Y
		, 121, -30 // L y
		},
		null,
		{ 65, -27 // N A
		},
		{ 65, -55 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -135 // P comma
		, 46, -135 // P period
		, 65, -90 // P A
		, 97, -80 // P a
		, 101, -80 // P e
		, 111, -80 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 85, -40 // R U
		, 86, -18 // R V
		, 87, -18 // R W
		, 89, -18 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -74 // T hyphen
		, 46, -74 // T period
		, 58, -55 // T colon
		, 59, -65 // T semicolon
		, 65, -50 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -55 // T i
		, 111, -92 // T o
		, 114, -55 // T r
		, 117, -55 // T u
		, 119, -74 // T w
		, 121, -74 // T y
		},
		{ 44, -25 // U comma
		, 46, -25 // U period
		, 65, -40 // U A
		},
		{ 44, -129 // V comma
		, 45, -55 // V hyphen
		, 46, -129 // V period
		, 58, -65 // V colon
		, 59, -74 // V semicolon
		, 65, -60 // V A
		, 79, -30 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -74 // V i
		, 111, -111 // V o
		, 117, -74 // V u
		},
		{ 44, -92 // W comma
		, 45, -37 // W hyphen
		, 46, -92 // W period
		, 58, -65 // W colon
		, 59, -65 // W semicolon
		, 65, -60 // W A
		, 79, -25 // W O
		, 97, -92 // W a
		, 101, -92 // W e
		, 105, -55 // W i
		, 111, -92 // W o
		, 117, -55 // W u
		, 121, -70 // W y
		},
		null,
		{ 44, -92 // Y comma
		, 45, -74 // Y hyphen
		, 46, -92 // Y period
		, 58, -65 // Y colon
		, 59, -65 // Y semicolon
		, 65, -50 // Y A
		, 79, -15 // Y O
		, 97, -92 // Y a
		, 101, -92 // Y e
		, 105, -74 // Y i
		, 111, -92 // Y o
		, 117, -92 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 96, -111 // quoteleft quoteleft
		},
		{ 103, -10 // a g
		},
		{ 46, -40 // b period
		, 117, -20 // b u
		},
		{ 104, -15 // c h
		, 107, -20 // c k
		},
		null,
		{ 44, -10 // e comma
		, 46, -15 // e period
		, 103, -40 // e g
		, 118, -15 // e v
		, 119, -15 // e w
		, 120, -20 // e x
		, 121, -30 // e y
		},
		{ 39, 92 // f quoteright
		, 44, -10 // f comma
		, 46, -15 // f period
		, 102, -18 // f f
		, 105, -20 // f i
		, 245, -60 // f dotlessi
		},
		{ 44, -10 // g comma
		, 46, -15 // g period
		, 101, -10 // g e
		, 103, -10 // g g
		},
		null,
		null,
		null,
		{ 101, -10 // k e
		, 111, -10 // k o
		, 121, -10 // k y
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 103, -10 // o g
		, 118, -10 // o v
		},
		null,
		null,
		{ 44, -111 // r comma
		, 45, -20 // r hyphen
		, 46, -111 // r period
		, 97, -15 // r a
		, 99, -37 // r c
		, 100, -37 // r d
		, 101, -37 // r e
		, 103, -37 // r g
		, 111, -45 // r o
		, 113, -37 // r q
		, 115, -10 // r s
		},
		null,
		null,
		null,
		{ 44, -74 // v comma
		, 46, -74 // v period
		},
		{ 44, -74 // w comma
		, 46, -74 // w period
		},
		null,
		{ 44, -55 // y comma
		, 46, -55 // y period
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
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
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

	/** Contains the widths of the TimesItalic characters. */
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
		250, //space
		333, //exclam
		420, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		675, //plus
		250, //comma
		333, //hyphen
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
		333, //colon
		333, //semicolon
		675, //less
		675, //equal
		675, //greater
		500, //question
		920, //at
		611, //A
		611, //B
		667, //C
		722, //D
		611, //E
		611, //F
		722, //G
		722, //H
		333, //I
		444, //J
		667, //K
		556, //L
		833, //M
		667, //N
		722, //O
		611, //P
		722, //Q
		611, //R
		500, //S
		556, //T
		722, //U
		611, //V
		833, //W
		611, //X
		556, //Y
		556, //Z
		389, //bracketleft
		278, //backslash
		389, //bracketright
		422, //asciicircum
		500, //underscore
		333, //grave
		500, //a
		500, //b
		444, //c
		500, //d
		444, //e
		278, //f
		500, //g
		500, //h
		278, //i
		278, //j
		444, //k
		278, //l
		722, //m
		500, //n
		500, //o
		500, //p
		500, //q
		389, //r
		389, //s
		278, //t
		500, //u
		444, //v
		667, //w
		444, //x
		444, //y
		389, //z
		400, //braceleft
		275, //bar
		400, //braceright
		541, //asciitilde
		350, //bullet
		350, //bullet
		350, //bullet
		333, //quotesinglbase
		500, //florin
		556, //quotedblbase
		889, //ellipsis
		500, //dagger
		500, //daggerdbl
		333, //circumflex
		1000, //perthousand
		500, //Scaron
		333, //guilsinglleft
		944, //OE
		350, //bullet
		350, //bullet
		350, //bullet
		350, //bullet
		333, //quoteleft
		333, //quoteright
		556, //quotedblleft
		556, //quotedblright
		350, //bullet
		500, //endash
		889, //emdash
		333, //tilde
		980, //trademark
		389, //scaron
		333, //guilsinglright
		667, //oe
		350, //bullet
		389, //zcaron
		556, //Ydieresis
		250, //space
		389, //exclamdown
		500, //cent
		500, //sterling
		500, //currency
		500, //yen
		275, //brokenbar
		500, //section
		333, //dieresis
		760, //copyright
		276, //ordfeminine
		500, //guillemotleft
		675, //logicalnot
		333, //hyphen
		760, //registered
		333, //macron
		400, //degree
		675, //plusminus
		300, //twosuperior
		300, //threesuperior
		333, //acute
		500, //mu
		523, //paragraph
		250, //periodcentered
		333, //cedilla
		300, //onesuperior
		310, //ordmasculine
		500, //guillemotright
		750, //onequarter
		750, //onehalf
		750, //threequarters
		500, //questiondown
		611, //Agrave
		611, //Aacute
		611, //Acircumflex
		611, //Atilde
		611, //Adieresis
		611, //Aring
		889, //AE
		667, //Ccedilla
		611, //Egrave
		611, //Eacute
		611, //Ecircumflex
		611, //Edieresis
		333, //Igrave
		333, //Iacute
		333, //Icircumflex
		333, //Idieresis
		722, //Eth
		667, //Ntilde
		722, //Ograve
		722, //Oacute
		722, //Ocircumflex
		722, //Otilde
		722, //Odieresis
		675, //multiply
		722, //Oslash
		722, //Ugrave
		722, //Uacute
		722, //Ucircumflex
		722, //Udieresis
		556, //Yacute
		611, //Thorn
		500, //germandbls
		500, //agrave
		500, //aacute
		500, //acircumflex
		500, //atilde
		500, //adieresis
		500, //aring
		667, //ae
		444, //ccedilla
		444, //egrave
		444, //eacute
		444, //ecircumflex
		444, //edieresis
		278, //igrave
		278, //iacute
		278, //icircumflex
		278, //idieresis
		500, //eth
		500, //ntilde
		500, //ograve
		500, //oacute
		500, //ocircumflex
		500, //otilde
		500, //odieresis
		675, //divide
		500, //oslash
		500, //ugrave
		500, //uacute
		500, //ucircumflex
		500, //udieresis
		444, //yacute
		500, //thorn
		444 //ydieresis
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
		{ 65, -18 // space A
		, 84, -18 // space T
		, 86, -35 // space V
		, 87, -40 // space W
		, 89, -75 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -111 // quoteright space
		, 39, -111 // quoteright quoteright
		, 100, -25 // quoteright d
		, 114, -25 // quoteright r
		, 115, -40 // quoteright s
		, 116, -30 // quoteright t
		, 118, -10 // quoteright v
		, 146, -111 // quoteright quoteright
		, 160, -111 // quoteright space
		},
		null,
		null,
		null,
		null,
		{ 39, -140 // comma quoteright
		, 146, -140 // comma quoteright
		, 148, -140 // comma quotedblright
		},
		null,
		{ 39, -140 // period quoteright
		, 146, -140 // period quoteright
		, 148, -140 // period quotedblright
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
		{ 39, -37 // A quoteright
		, 67, -30 // A C
		, 71, -35 // A G
		, 79, -40 // A O
		, 81, -40 // A Q
		, 84, -37 // A T
		, 85, -50 // A U
		, 86, -105 // A V
		, 87, -95 // A W
		, 89, -55 // A Y
		, 117, -20 // A u
		, 118, -55 // A v
		, 119, -55 // A w
		, 121, -55 // A y
		, 146, -37 // A quoteright
		},
		{ 65, -25 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -35 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -40 // D Y
		},
		null,
		{ 44, -135 // F comma
		, 46, -135 // F period
		, 65, -115 // F A
		, 97, -75 // F a
		, 101, -75 // F e
		, 105, -45 // F i
		, 111, -105 // F o
		, 114, -55 // F r
		},
		null,
		null,
		null,
		{ 44, -25 // J comma
		, 46, -25 // J period
		, 65, -40 // J A
		, 97, -35 // J a
		, 101, -25 // J e
		, 111, -25 // J o
		, 117, -35 // J u
		},
		{ 79, -50 // K O
		, 101, -35 // K e
		, 111, -40 // K o
		, 117, -40 // K u
		, 121, -40 // K y
		},
		{ 39, -37 // L quoteright
		, 84, -20 // L T
		, 86, -55 // L V
		, 87, -55 // L W
		, 89, -20 // L Y
		, 121, -30 // L y
		, 146, -37 // L quoteright
		},
		null,
		{ 65, -27 // N A
		},
		{ 65, -55 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -135 // P comma
		, 46, -135 // P period
		, 65, -90 // P A
		, 97, -80 // P a
		, 101, -80 // P e
		, 111, -80 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 85, -40 // R U
		, 86, -18 // R V
		, 87, -18 // R W
		, 89, -18 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -74 // T hyphen
		, 46, -74 // T period
		, 58, -55 // T colon
		, 59, -65 // T semicolon
		, 65, -50 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -55 // T i
		, 111, -92 // T o
		, 114, -55 // T r
		, 117, -55 // T u
		, 119, -74 // T w
		, 121, -74 // T y
		, 173, -74 // T hyphen
		},
		{ 44, -25 // U comma
		, 46, -25 // U period
		, 65, -40 // U A
		},
		{ 44, -129 // V comma
		, 45, -55 // V hyphen
		, 46, -129 // V period
		, 58, -65 // V colon
		, 59, -74 // V semicolon
		, 65, -60 // V A
		, 79, -30 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -74 // V i
		, 111, -111 // V o
		, 117, -74 // V u
		, 173, -55 // V hyphen
		},
		{ 44, -92 // W comma
		, 45, -37 // W hyphen
		, 46, -92 // W period
		, 58, -65 // W colon
		, 59, -65 // W semicolon
		, 65, -60 // W A
		, 79, -25 // W O
		, 97, -92 // W a
		, 101, -92 // W e
		, 105, -55 // W i
		, 111, -92 // W o
		, 117, -55 // W u
		, 121, -70 // W y
		, 173, -37 // W hyphen
		},
		null,
		{ 44, -92 // Y comma
		, 45, -74 // Y hyphen
		, 46, -92 // Y period
		, 58, -65 // Y colon
		, 59, -65 // Y semicolon
		, 65, -50 // Y A
		, 79, -15 // Y O
		, 97, -92 // Y a
		, 101, -92 // Y e
		, 105, -74 // Y i
		, 111, -92 // Y o
		, 117, -92 // Y u
		, 173, -74 // Y hyphen
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 103, -10 // a g
		},
		{ 46, -40 // b period
		, 117, -20 // b u
		},
		{ 104, -15 // c h
		, 107, -20 // c k
		},
		null,
		{ 44, -10 // e comma
		, 46, -15 // e period
		, 103, -40 // e g
		, 118, -15 // e v
		, 119, -15 // e w
		, 120, -20 // e x
		, 121, -30 // e y
		},
		{ 39, 92 // f quoteright
		, 44, -10 // f comma
		, 46, -15 // f period
		, 102, -18 // f f
		, 105, -20 // f i
		, 146, 92 // f quoteright
		},
		{ 44, -10 // g comma
		, 46, -15 // g period
		, 101, -10 // g e
		, 103, -10 // g g
		},
		null,
		null,
		null,
		{ 101, -10 // k e
		, 111, -10 // k o
		, 121, -10 // k y
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 103, -10 // o g
		, 118, -10 // o v
		},
		null,
		null,
		{ 44, -111 // r comma
		, 45, -20 // r hyphen
		, 46, -111 // r period
		, 97, -15 // r a
		, 99, -37 // r c
		, 100, -37 // r d
		, 101, -37 // r e
		, 103, -37 // r g
		, 111, -45 // r o
		, 113, -37 // r q
		, 115, -10 // r s
		, 173, -20 // r hyphen
		},
		null,
		null,
		null,
		{ 44, -74 // v comma
		, 46, -74 // v period
		},
		{ 44, -74 // w comma
		, 46, -74 // w period
		},
		null,
		{ 44, -55 // y comma
		, 46, -55 // y period
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
		{ 145, -111 // quoteleft quoteleft
		},
		{ 32, -111 // quoteright space
		, 39, -111 // quoteright quoteright
		, 100, -25 // quoteright d
		, 114, -25 // quoteright r
		, 115, -40 // quoteright s
		, 116, -30 // quoteright t
		, 118, -10 // quoteright v
		, 146, -111 // quoteright quoteright
		, 160, -111 // quoteright space
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
		{ 65, -18 // space A
		, 84, -18 // space T
		, 86, -35 // space V
		, 87, -40 // space W
		, 89, -75 // space Y
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
		null,
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
		250, //space
		333, //exclam
		420, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		675, //plus
		250, //comma
		333, //hyphen
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
		333, //colon
		333, //semicolon
		675, //less
		675, //equal
		675, //greater
		500, //question
		920, //at
		611, //A
		611, //B
		667, //C
		722, //D
		611, //E
		611, //F
		722, //G
		722, //H
		333, //I
		444, //J
		667, //K
		556, //L
		833, //M
		667, //N
		722, //O
		611, //P
		722, //Q
		611, //R
		500, //S
		556, //T
		722, //U
		611, //V
		833, //W
		611, //X
		556, //Y
		556, //Z
		389, //bracketleft
		278, //backslash
		389, //bracketright
		422, //asciicircum
		500, //underscore
		333, //grave
		500, //a
		500, //b
		444, //c
		500, //d
		444, //e
		278, //f
		500, //g
		500, //h
		278, //i
		278, //j
		444, //k
		278, //l
		722, //m
		500, //n
		500, //o
		500, //p
		500, //q
		389, //r
		389, //s
		278, //t
		500, //u
		444, //v
		667, //w
		444, //x
		444, //y
		389, //z
		400, //braceleft
		275, //bar
		400, //braceright
		541, //asciitilde
		0, //
		611, //Adieresis
		611, //Aring
		667, //Ccedilla
		611, //Eacute
		667, //Ntilde
		722, //Odieresis
		722, //Udieresis
		500, //aacute
		500, //agrave
		500, //acircumflex
		500, //adieresis
		500, //atilde
		500, //aring
		444, //ccedilla
		444, //eacute
		444, //egrave
		444, //ecircumflex
		444, //edieresis
		278, //iacute
		278, //igrave
		278, //icircumflex
		278, //idieresis
		500, //ntilde
		500, //oacute
		500, //ograve
		500, //ocircumflex
		500, //odieresis
		500, //otilde
		500, //uacute
		500, //ugrave
		500, //ucircumflex
		500, //udieresis
		500, //dagger
		400, //degree
		500, //cent
		500, //sterling
		500, //section
		350, //bullet
		523, //paragraph
		500, //germandbls
		760, //registered
		760, //copyright
		980, //trademark
		333, //acute
		333, //dieresis
		0, //
		889, //AE
		722, //Oslash
		0, //
		675, //plusminus
		0, //
		0, //
		500, //yen
		500, //mu
		0, //
		0, //
		0, //
		0, //
		0, //
		276, //ordfeminine
		310, //ordmasculine
		0, //
		667, //ae
		500, //oslash
		500, //questiondown
		389, //exclamdown
		675, //logicalnot
		0, //
		500, //florin
		0, //
		0, //
		0, //guilmotleft
		0, //guilmotright
		889, //ellipsis
		250, //space
		611, //Agrave
		611, //Atilde
		722, //Otilde
		944, //OE
		667, //oe
		500, //endash
		889, //emdash
		556, //quotedblleft
		556, //quotedblright
		333, //quoteleft
		333, //quoteright
		675, //divide
		0, //
		444, //ydieresis
		556, //Ydieresis
		167, //fraction
		500, //currency
		333, //guilsinglleft
		333, //guilsinglright
		500, //fi
		500, //fl
		500, //daggerdbl
		250, //periodcentered
		333, //quotesinglbase
		556, //quotedblbase
		1000, //perthousand
		611, //Acircumflex
		611, //Ecircumflex
		611, //Aacute
		611, //Edieresis
		611, //Egrave
		333, //Iacute
		333, //Icircumflex
		333, //Idieresis
		333, //Igrave
		722, //Oacute
		722, //Ocircumflex
		0, //
		722, //Ograve
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
		{ 65, -18 // space A
		, 84, -18 // space T
		, 86, -35 // space V
		, 87, -40 // space W
		, 89, -75 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -111 // quoteright space
		, 39, -111 // quoteright quoteright
		, 100, -25 // quoteright d
		, 114, -25 // quoteright r
		, 115, -40 // quoteright s
		, 116, -30 // quoteright t
		, 118, -10 // quoteright v
		, 202, -111 // quoteright space
		, 213, -111 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		{ 39, -140 // comma quoteright
		, 211, -140 // comma quotedblright
		, 213, -140 // comma quoteright
		},
		null,
		{ 39, -140 // period quoteright
		, 211, -140 // period quotedblright
		, 213, -140 // period quoteright
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
		{ 39, -37 // A quoteright
		, 67, -30 // A C
		, 71, -35 // A G
		, 79, -40 // A O
		, 81, -40 // A Q
		, 84, -37 // A T
		, 85, -50 // A U
		, 86, -105 // A V
		, 87, -95 // A W
		, 89, -55 // A Y
		, 117, -20 // A u
		, 118, -55 // A v
		, 119, -55 // A w
		, 121, -55 // A y
		, 213, -37 // A quoteright
		},
		{ 65, -25 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -35 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -40 // D Y
		},
		null,
		{ 44, -135 // F comma
		, 46, -135 // F period
		, 65, -115 // F A
		, 97, -75 // F a
		, 101, -75 // F e
		, 105, -45 // F i
		, 111, -105 // F o
		, 114, -55 // F r
		},
		null,
		null,
		null,
		{ 44, -25 // J comma
		, 46, -25 // J period
		, 65, -40 // J A
		, 97, -35 // J a
		, 101, -25 // J e
		, 111, -25 // J o
		, 117, -35 // J u
		},
		{ 79, -50 // K O
		, 101, -35 // K e
		, 111, -40 // K o
		, 117, -40 // K u
		, 121, -40 // K y
		},
		{ 39, -37 // L quoteright
		, 84, -20 // L T
		, 86, -55 // L V
		, 87, -55 // L W
		, 89, -20 // L Y
		, 121, -30 // L y
		, 213, -37 // L quoteright
		},
		null,
		{ 65, -27 // N A
		},
		{ 65, -55 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -135 // P comma
		, 46, -135 // P period
		, 65, -90 // P A
		, 97, -80 // P a
		, 101, -80 // P e
		, 111, -80 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 85, -40 // R U
		, 86, -18 // R V
		, 87, -18 // R W
		, 89, -18 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -74 // T hyphen
		, 46, -74 // T period
		, 58, -55 // T colon
		, 59, -65 // T semicolon
		, 65, -50 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -55 // T i
		, 111, -92 // T o
		, 114, -55 // T r
		, 117, -55 // T u
		, 119, -74 // T w
		, 121, -74 // T y
		},
		{ 44, -25 // U comma
		, 46, -25 // U period
		, 65, -40 // U A
		},
		{ 44, -129 // V comma
		, 45, -55 // V hyphen
		, 46, -129 // V period
		, 58, -65 // V colon
		, 59, -74 // V semicolon
		, 65, -60 // V A
		, 79, -30 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -74 // V i
		, 111, -111 // V o
		, 117, -74 // V u
		},
		{ 44, -92 // W comma
		, 45, -37 // W hyphen
		, 46, -92 // W period
		, 58, -65 // W colon
		, 59, -65 // W semicolon
		, 65, -60 // W A
		, 79, -25 // W O
		, 97, -92 // W a
		, 101, -92 // W e
		, 105, -55 // W i
		, 111, -92 // W o
		, 117, -55 // W u
		, 121, -70 // W y
		},
		null,
		{ 44, -92 // Y comma
		, 45, -74 // Y hyphen
		, 46, -92 // Y period
		, 58, -65 // Y colon
		, 59, -65 // Y semicolon
		, 65, -50 // Y A
		, 79, -15 // Y O
		, 97, -92 // Y a
		, 101, -92 // Y e
		, 105, -74 // Y i
		, 111, -92 // Y o
		, 117, -92 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 103, -10 // a g
		},
		{ 46, -40 // b period
		, 117, -20 // b u
		},
		{ 104, -15 // c h
		, 107, -20 // c k
		},
		null,
		{ 44, -10 // e comma
		, 46, -15 // e period
		, 103, -40 // e g
		, 118, -15 // e v
		, 119, -15 // e w
		, 120, -20 // e x
		, 121, -30 // e y
		},
		{ 39, 92 // f quoteright
		, 44, -10 // f comma
		, 46, -15 // f period
		, 102, -18 // f f
		, 105, -20 // f i
		, 213, 92 // f quoteright
		, 245, -60 // f dotlessi
		},
		{ 44, -10 // g comma
		, 46, -15 // g period
		, 101, -10 // g e
		, 103, -10 // g g
		},
		null,
		null,
		null,
		{ 101, -10 // k e
		, 111, -10 // k o
		, 121, -10 // k y
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 103, -10 // o g
		, 118, -10 // o v
		},
		null,
		null,
		{ 44, -111 // r comma
		, 45, -20 // r hyphen
		, 46, -111 // r period
		, 97, -15 // r a
		, 99, -37 // r c
		, 100, -37 // r d
		, 101, -37 // r e
		, 103, -37 // r g
		, 111, -45 // r o
		, 113, -37 // r q
		, 115, -10 // r s
		},
		null,
		null,
		null,
		{ 44, -74 // v comma
		, 46, -74 // v period
		},
		{ 44, -74 // w comma
		, 46, -74 // w period
		},
		null,
		{ 44, -55 // y comma
		, 46, -55 // y period
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
		{ 65, -18 // space A
		, 84, -18 // space T
		, 86, -35 // space V
		, 87, -40 // space W
		, 89, -75 // space Y
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
		{ 212, -111 // quoteleft quoteleft
		},
		{ 32, -111 // quoteright space
		, 39, -111 // quoteright quoteright
		, 100, -25 // quoteright d
		, 114, -25 // quoteright r
		, 115, -40 // quoteright s
		, 116, -30 // quoteright t
		, 118, -10 // quoteright v
		, 202, -111 // quoteright space
		, 213, -111 // quoteright quoteright
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
	 * @since   iText0.30
	 */

	public TimesItalic(int encoding, int fontsize) {
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
	 * @since   iText0.30
	 */
	
	public final PdfName name() {
		return PdfName.TIMES_ITALIC;
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
			return TIMES_ROMAN;
		case BOLD:
			return TIMES_BOLD;
		case BOLDITALIC:
			return TIMES_BOLDITALIC;
		default:
			return TIMES_ITALIC;
		}
	}
}
