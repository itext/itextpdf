/*
 * @(#)TimesBold.java				0.23 2000/02/02
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
 * This class contains the metrics of the font <VAR>Times Bold</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.2/tib_____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class TimesBold extends PdfFontMetrics {

// static membervariables

	/** Contains the widths of the TimesBold characters. */
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
		555, //quotedbl
		500, //numbersign
		500, //dollar
		1000, //percent
		833, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		570, //plus
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
		570, //less
		570, //equal
		570, //greater
		500, //question
		930, //at
		722, //A
		667, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		778, //H
		389, //I
		500, //J
		778, //K
		667, //L
		944, //M
		722, //N
		778, //O
		611, //P
		778, //Q
		722, //R
		556, //S
		667, //T
		722, //U
		722, //V
		1000, //W
		722, //X
		722, //Y
		667, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		581, //asciicircum
		500, //underscore
		333, //quoteleft
		500, //a
		556, //b
		444, //c
		556, //d
		444, //e
		333, //f
		500, //g
		556, //h
		278, //i
		333, //j
		556, //k
		278, //l
		833, //m
		556, //n
		500, //o
		556, //p
		556, //q
		444, //r
		389, //s
		333, //t
		556, //u
		500, //v
		722, //w
		500, //x
		500, //y
		444, //z
		394, //braceleft
		220, //bar
		394, //braceright
		520, //asciitilde
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
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
		500, //cent
		500, //sterling
		167, //fraction
		500, //yen
		500, //florin
		500, //section
		500, //currency
		278, //quotesingle
		500, //quotedblleft
		500, //guillemotleft
		333, //guilsinglleft
		333, //guilsinglright
		556, //fi
		556, //fl
		0, //
		500, //endash
		500, //dagger
		500, //daggerdbl
		250, //periodcentered
		0, //
		540, //paragraph
		350, //bullet
		333, //quotesinglbase
		500, //quotedblbase
		500, //quotedblright
		500, //guillemotright
		1000, //ellipsis
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
		300, //ordfeminine
		0, //
		0, //
		0, //
		0, //
		667, //Lslash
		778, //Oslash
		1000, //OE
		330, //ordmasculine
		0, //
		0, //
		0, //
		0, //
		0, //
		722, //ae
		0, //
		0, //
		0, //
		278, //dotlessi
		0, //
		0, //
		278, //lslash
		500, //oslash
		722, //oe
		556, //germandbls
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
		{ 65, -55 // space A
		, 84, -30 // space T
		, 86, -45 // space V
		, 87, -30 // space W
		, 89, -55 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -63 // quoteright quoteright
		, 100, -20 // quoteright d
		, 114, -20 // quoteright r
		, 115, -37 // quoteright s
		, 118, -20 // quoteright v
		},
		null,
		null,
		null,
		null,
		{ 39, -55 // comma quoteright
		, 186, -45 // comma quotedblright
		},
		null,
		{ 39, -55 // period quoteright
		, 186, -55 // period quotedblright
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
		{ 39, -74 // A quoteright
		, 67, -55 // A C
		, 71, -55 // A G
		, 79, -45 // A O
		, 81, -45 // A Q
		, 84, -95 // A T
		, 85, -50 // A U
		, 86, -145 // A V
		, 87, -130 // A W
		, 89, -100 // A Y
		, 112, -25 // A p
		, 117, -50 // A u
		, 118, -100 // A v
		, 119, -90 // A w
		, 121, -74 // A y
		},
		{ 65, -30 // B A
		, 85, -10 // B U
		},
		null,
		{ 46, -20 // D period
		, 65, -35 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -40 // D Y
		},
		null,
		{ 44, -92 // F comma
		, 46, -110 // F period
		, 65, -90 // F A
		, 97, -25 // F a
		, 101, -25 // F e
		, 111, -25 // F o
		},
		null,
		null,
		null,
		{ 46, -20 // J period
		, 65, -30 // J A
		, 97, -15 // J a
		, 101, -15 // J e
		, 111, -15 // J o
		, 117, -15 // J u
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -25 // K o
		, 117, -15 // K u
		, 121, -45 // K y
		},
		{ 39, -110 // L quoteright
		, 84, -92 // L T
		, 86, -92 // L V
		, 87, -92 // L W
		, 89, -92 // L Y
		, 121, -55 // L y
		, 186, -20 // L quotedblright
		},
		null,
		{ 65, -20 // N A
		},
		{ 65, -40 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -92 // P comma
		, 46, -110 // P period
		, 65, -74 // P A
		, 97, -10 // P a
		, 101, -20 // P e
		, 111, -20 // P o
		},
		{ 46, -20 // Q period
		, 85, -10 // Q U
		},
		{ 79, -30 // R O
		, 84, -40 // R T
		, 85, -30 // R U
		, 86, -55 // R V
		, 87, -35 // R W
		, 89, -35 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -92 // T hyphen
		, 46, -90 // T period
		, 58, -74 // T colon
		, 59, -74 // T semicolon
		, 65, -90 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -18 // T i
		, 111, -92 // T o
		, 114, -74 // T r
		, 117, -92 // T u
		, 119, -74 // T w
		, 121, -74 // T y
		},
		{ 44, -50 // U comma
		, 46, -50 // U period
		, 65, -60 // U A
		},
		{ 44, -129 // V comma
		, 45, -74 // V hyphen
		, 46, -145 // V period
		, 58, -92 // V colon
		, 59, -92 // V semicolon
		, 65, -135 // V A
		, 71, -30 // V G
		, 79, -45 // V O
		, 97, -92 // V a
		, 101, -100 // V e
		, 105, -37 // V i
		, 111, -100 // V o
		, 117, -92 // V u
		},
		{ 44, -92 // W comma
		, 45, -37 // W hyphen
		, 46, -92 // W period
		, 58, -55 // W colon
		, 59, -55 // W semicolon
		, 65, -120 // W A
		, 79, -10 // W O
		, 97, -65 // W a
		, 101, -65 // W e
		, 105, -18 // W i
		, 111, -75 // W o
		, 117, -50 // W u
		, 121, -60 // W y
		},
		null,
		{ 44, -92 // Y comma
		, 45, -92 // Y hyphen
		, 46, -92 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -110 // Y A
		, 79, -35 // Y O
		, 97, -85 // Y a
		, 101, -111 // Y e
		, 105, -37 // Y i
		, 111, -111 // Y o
		, 117, -92 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 65, -10 // quoteleft A
		, 96, -63 // quoteleft quoteleft
		},
		{ 118, -25 // a v
		},
		{ 46, -40 // b period
		, 98, -10 // b b
		, 117, -20 // b u
		, 118, -15 // b v
		},
		null,
		{ 119, -15 // d w
		},
		{ 118, -15 // e v
		},
		{ 39, 55 // f quoteright
		, 44, -15 // f comma
		, 46, -15 // f period
		, 105, -25 // f i
		, 111, -25 // f o
		, 186, 50 // f quotedblright
		, 245, -35 // f dotlessi
		},
		{ 46, -15 // g period
		},
		{ 121, -15 // h y
		},
		{ 118, -10 // i v
		},
		null,
		{ 101, -10 // k e
		, 111, -15 // k o
		, 121, -15 // k y
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 118, -10 // o v
		, 119, -10 // o w
		},
		null,
		null,
		{ 44, -92 // r comma
		, 45, -37 // r hyphen
		, 46, -100 // r period
		, 99, -18 // r c
		, 101, -18 // r e
		, 103, -10 // r g
		, 110, -15 // r n
		, 111, -18 // r o
		, 112, -10 // r p
		, 113, -18 // r q
		, 118, -10 // r v
		},
		null,
		null,
		null,
		{ 44, -55 // v comma
		, 46, -70 // v period
		, 97, -10 // v a
		, 101, -10 // v e
		, 111, -10 // v o
		},
		{ 44, -55 // w comma
		, 46, -70 // w period
		, 111, -10 // w o
		},
		null,
		{ 44, -55 // y comma
		, 46, -70 // y period
		, 101, -10 // y e
		, 111, -25 // y o
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
		{ 65, -10 // quotedblleft A
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
		null
	};

	/** Contains the widths of the TimesBold characters. */
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
		555, //quotedbl
		500, //numbersign
		500, //dollar
		1000, //percent
		833, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		570, //plus
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
		570, //less
		570, //equal
		570, //greater
		500, //question
		930, //at
		722, //A
		667, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		778, //H
		389, //I
		500, //J
		778, //K
		667, //L
		944, //M
		722, //N
		778, //O
		611, //P
		778, //Q
		722, //R
		556, //S
		667, //T
		722, //U
		722, //V
		1000, //W
		722, //X
		722, //Y
		667, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		581, //asciicircum
		500, //underscore
		333, //grave
		500, //a
		556, //b
		444, //c
		556, //d
		444, //e
		333, //f
		500, //g
		556, //h
		278, //i
		333, //j
		556, //k
		278, //l
		833, //m
		556, //n
		500, //o
		556, //p
		556, //q
		444, //r
		389, //s
		333, //t
		556, //u
		500, //v
		722, //w
		500, //x
		500, //y
		444, //z
		394, //braceleft
		220, //bar
		394, //braceright
		520, //asciitilde
		350, //bullet
		350, //bullet
		350, //bullet
		333, //quotesinglbase
		500, //florin
		500, //quotedblbase
		1000, //ellipsis
		500, //dagger
		500, //daggerdbl
		333, //circumflex
		1000, //perthousand
		556, //Scaron
		333, //guilsinglleft
		1000, //OE
		350, //bullet
		350, //bullet
		350, //bullet
		350, //bullet
		333, //quoteleft
		333, //quoteright
		500, //quotedblleft
		500, //quotedblright
		350, //bullet
		500, //endash
		1000, //emdash
		333, //tilde
		1000, //trademark
		389, //scaron
		333, //guilsinglright
		722, //oe
		350, //bullet
		444, //zcaron
		722, //Ydieresis
		250, //space
		333, //exclamdown
		500, //cent
		500, //sterling
		500, //currency
		500, //yen
		220, //brokenbar
		500, //section
		333, //dieresis
		747, //copyright
		300, //ordfeminine
		500, //guillemotleft
		570, //logicalnot
		333, //hyphen
		747, //registered
		333, //macron
		400, //degree
		570, //plusminus
		300, //twosuperior
		300, //threesuperior
		333, //acute
		556, //mu
		540, //paragraph
		250, //periodcentered
		333, //cedilla
		300, //onesuperior
		330, //ordmasculine
		500, //guillemotright
		750, //onequarter
		750, //onehalf
		750, //threequarters
		500, //questiondown
		722, //Agrave
		722, //Aacute
		722, //Acircumflex
		722, //Atilde
		722, //Adieresis
		722, //Aring
		1000, //AE
		722, //Ccedilla
		667, //Egrave
		667, //Eacute
		667, //Ecircumflex
		667, //Edieresis
		389, //Igrave
		389, //Iacute
		389, //Icircumflex
		389, //Idieresis
		722, //Eth
		722, //Ntilde
		778, //Ograve
		778, //Oacute
		778, //Ocircumflex
		778, //Otilde
		778, //Odieresis
		570, //multiply
		778, //Oslash
		722, //Ugrave
		722, //Uacute
		722, //Ucircumflex
		722, //Udieresis
		722, //Yacute
		611, //Thorn
		556, //germandbls
		500, //agrave
		500, //aacute
		500, //acircumflex
		500, //atilde
		500, //adieresis
		500, //aring
		722, //ae
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
		556, //ntilde
		500, //ograve
		500, //oacute
		500, //ocircumflex
		500, //otilde
		500, //odieresis
		570, //divide
		500, //oslash
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
		{ 65, -55 // space A
		, 84, -30 // space T
		, 86, -45 // space V
		, 87, -30 // space W
		, 89, -55 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -63 // quoteright quoteright
		, 100, -20 // quoteright d
		, 114, -20 // quoteright r
		, 115, -37 // quoteright s
		, 118, -20 // quoteright v
		, 146, -63 // quoteright quoteright
		, 160, -74 // quoteright space
		},
		null,
		null,
		null,
		null,
		{ 39, -55 // comma quoteright
		, 146, -55 // comma quoteright
		, 148, -45 // comma quotedblright
		},
		null,
		{ 39, -55 // period quoteright
		, 146, -55 // period quoteright
		, 148, -55 // period quotedblright
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
		{ 39, -74 // A quoteright
		, 67, -55 // A C
		, 71, -55 // A G
		, 79, -45 // A O
		, 81, -45 // A Q
		, 84, -95 // A T
		, 85, -50 // A U
		, 86, -145 // A V
		, 87, -130 // A W
		, 89, -100 // A Y
		, 112, -25 // A p
		, 117, -50 // A u
		, 118, -100 // A v
		, 119, -90 // A w
		, 121, -74 // A y
		, 146, -74 // A quoteright
		},
		{ 65, -30 // B A
		, 85, -10 // B U
		},
		null,
		{ 46, -20 // D period
		, 65, -35 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -40 // D Y
		},
		null,
		{ 44, -92 // F comma
		, 46, -110 // F period
		, 65, -90 // F A
		, 97, -25 // F a
		, 101, -25 // F e
		, 111, -25 // F o
		},
		null,
		null,
		null,
		{ 46, -20 // J period
		, 65, -30 // J A
		, 97, -15 // J a
		, 101, -15 // J e
		, 111, -15 // J o
		, 117, -15 // J u
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -25 // K o
		, 117, -15 // K u
		, 121, -45 // K y
		},
		{ 39, -110 // L quoteright
		, 84, -92 // L T
		, 86, -92 // L V
		, 87, -92 // L W
		, 89, -92 // L Y
		, 121, -55 // L y
		, 146, -110 // L quoteright
		, 148, -20 // L quotedblright
		},
		null,
		{ 65, -20 // N A
		},
		{ 65, -40 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -92 // P comma
		, 46, -110 // P period
		, 65, -74 // P A
		, 97, -10 // P a
		, 101, -20 // P e
		, 111, -20 // P o
		},
		{ 46, -20 // Q period
		, 85, -10 // Q U
		},
		{ 79, -30 // R O
		, 84, -40 // R T
		, 85, -30 // R U
		, 86, -55 // R V
		, 87, -35 // R W
		, 89, -35 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -92 // T hyphen
		, 46, -90 // T period
		, 58, -74 // T colon
		, 59, -74 // T semicolon
		, 65, -90 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -18 // T i
		, 111, -92 // T o
		, 114, -74 // T r
		, 117, -92 // T u
		, 119, -74 // T w
		, 121, -74 // T y
		, 173, -92 // T hyphen
		},
		{ 44, -50 // U comma
		, 46, -50 // U period
		, 65, -60 // U A
		},
		{ 44, -129 // V comma
		, 45, -74 // V hyphen
		, 46, -145 // V period
		, 58, -92 // V colon
		, 59, -92 // V semicolon
		, 65, -135 // V A
		, 71, -30 // V G
		, 79, -45 // V O
		, 97, -92 // V a
		, 101, -100 // V e
		, 105, -37 // V i
		, 111, -100 // V o
		, 117, -92 // V u
		, 173, -74 // V hyphen
		},
		{ 44, -92 // W comma
		, 45, -37 // W hyphen
		, 46, -92 // W period
		, 58, -55 // W colon
		, 59, -55 // W semicolon
		, 65, -120 // W A
		, 79, -10 // W O
		, 97, -65 // W a
		, 101, -65 // W e
		, 105, -18 // W i
		, 111, -75 // W o
		, 117, -50 // W u
		, 121, -60 // W y
		, 173, -37 // W hyphen
		},
		null,
		{ 44, -92 // Y comma
		, 45, -92 // Y hyphen
		, 46, -92 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -110 // Y A
		, 79, -35 // Y O
		, 97, -85 // Y a
		, 101, -111 // Y e
		, 105, -37 // Y i
		, 111, -111 // Y o
		, 117, -92 // Y u
		, 173, -92 // Y hyphen
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 118, -25 // a v
		},
		{ 46, -40 // b period
		, 98, -10 // b b
		, 117, -20 // b u
		, 118, -15 // b v
		},
		null,
		{ 119, -15 // d w
		},
		{ 118, -15 // e v
		},
		{ 39, 55 // f quoteright
		, 44, -15 // f comma
		, 46, -15 // f period
		, 105, -25 // f i
		, 111, -25 // f o
		, 146, 55 // f quoteright
		, 148, 50 // f quotedblright
		},
		{ 46, -15 // g period
		},
		{ 121, -15 // h y
		},
		{ 118, -10 // i v
		},
		null,
		{ 101, -10 // k e
		, 111, -15 // k o
		, 121, -15 // k y
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 118, -10 // o v
		, 119, -10 // o w
		},
		null,
		null,
		{ 44, -92 // r comma
		, 45, -37 // r hyphen
		, 46, -100 // r period
		, 99, -18 // r c
		, 101, -18 // r e
		, 103, -10 // r g
		, 110, -15 // r n
		, 111, -18 // r o
		, 112, -10 // r p
		, 113, -18 // r q
		, 118, -10 // r v
		, 173, -37 // r hyphen
		},
		null,
		null,
		null,
		{ 44, -55 // v comma
		, 46, -70 // v period
		, 97, -10 // v a
		, 101, -10 // v e
		, 111, -10 // v o
		},
		{ 44, -55 // w comma
		, 46, -70 // w period
		, 111, -10 // w o
		},
		null,
		{ 44, -55 // y comma
		, 46, -70 // y period
		, 101, -10 // y e
		, 111, -25 // y o
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
		{ 65, -10 // quoteleft A
		, 145, -63 // quoteleft quoteleft
		},
		{ 32, -74 // quoteright space
		, 39, -63 // quoteright quoteright
		, 100, -20 // quoteright d
		, 114, -20 // quoteright r
		, 115, -37 // quoteright s
		, 118, -20 // quoteright v
		, 146, -63 // quoteright quoteright
		, 160, -74 // quoteright space
		},
		{ 65, -10 // quotedblleft A
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
		{ 65, -55 // space A
		, 84, -30 // space T
		, 86, -45 // space V
		, 87, -30 // space W
		, 89, -55 // space Y
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
		555, //quotedbl
		500, //numbersign
		500, //dollar
		1000, //percent
		833, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		570, //plus
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
		570, //less
		570, //equal
		570, //greater
		500, //question
		930, //at
		722, //A
		667, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		778, //H
		389, //I
		500, //J
		778, //K
		667, //L
		944, //M
		722, //N
		778, //O
		611, //P
		778, //Q
		722, //R
		556, //S
		667, //T
		722, //U
		722, //V
		1000, //W
		722, //X
		722, //Y
		667, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		581, //asciicircum
		500, //underscore
		333, //grave
		500, //a
		556, //b
		444, //c
		556, //d
		444, //e
		333, //f
		500, //g
		556, //h
		278, //i
		333, //j
		556, //k
		278, //l
		833, //m
		556, //n
		500, //o
		556, //p
		556, //q
		444, //r
		389, //s
		333, //t
		556, //u
		500, //v
		722, //w
		500, //x
		500, //y
		444, //z
		394, //braceleft
		220, //bar
		394, //braceright
		520, //asciitilde
		0, //
		722, //Adieresis
		722, //Aring
		722, //Ccedilla
		667, //Eacute
		722, //Ntilde
		778, //Odieresis
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
		556, //ntilde
		500, //oacute
		500, //ograve
		500, //ocircumflex
		500, //odieresis
		500, //otilde
		556, //uacute
		556, //ugrave
		556, //ucircumflex
		556, //udieresis
		500, //dagger
		400, //degree
		500, //cent
		500, //sterling
		500, //section
		350, //bullet
		540, //paragraph
		556, //germandbls
		747, //registered
		747, //copyright
		1000, //trademark
		333, //acute
		333, //dieresis
		0, //
		1000, //AE
		778, //Oslash
		0, //
		570, //plusminus
		0, //
		0, //
		500, //yen
		556, //mu
		0, //
		0, //
		0, //
		0, //
		0, //
		300, //ordfeminine
		330, //ordmasculine
		0, //
		722, //ae
		500, //oslash
		500, //questiondown
		333, //exclamdown
		570, //logicalnot
		0, //
		500, //florin
		0, //
		0, //
		0, //guilmotleft
		0, //guilmotright
		1000, //ellipsis
		250, //space
		722, //Agrave
		722, //Atilde
		778, //Otilde
		1000, //OE
		722, //oe
		500, //endash
		1000, //emdash
		500, //quotedblleft
		500, //quotedblright
		333, //quoteleft
		333, //quoteright
		570, //divide
		0, //
		500, //ydieresis
		722, //Ydieresis
		167, //fraction
		500, //currency
		333, //guilsinglleft
		333, //guilsinglright
		556, //fi
		556, //fl
		500, //daggerdbl
		250, //periodcentered
		333, //quotesinglbase
		500, //quotedblbase
		1000, //perthousand
		722, //Acircumflex
		667, //Ecircumflex
		722, //Aacute
		667, //Edieresis
		667, //Egrave
		389, //Iacute
		389, //Icircumflex
		389, //Idieresis
		389, //Igrave
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
		{ 65, -55 // space A
		, 84, -30 // space T
		, 86, -45 // space V
		, 87, -30 // space W
		, 89, -55 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -63 // quoteright quoteright
		, 100, -20 // quoteright d
		, 114, -20 // quoteright r
		, 115, -37 // quoteright s
		, 118, -20 // quoteright v
		, 202, -74 // quoteright space
		, 213, -63 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		{ 39, -55 // comma quoteright
		, 211, -45 // comma quotedblright
		, 213, -55 // comma quoteright
		},
		null,
		{ 39, -55 // period quoteright
		, 211, -55 // period quotedblright
		, 213, -55 // period quoteright
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
		{ 39, -74 // A quoteright
		, 67, -55 // A C
		, 71, -55 // A G
		, 79, -45 // A O
		, 81, -45 // A Q
		, 84, -95 // A T
		, 85, -50 // A U
		, 86, -145 // A V
		, 87, -130 // A W
		, 89, -100 // A Y
		, 112, -25 // A p
		, 117, -50 // A u
		, 118, -100 // A v
		, 119, -90 // A w
		, 121, -74 // A y
		, 213, -74 // A quoteright
		},
		{ 65, -30 // B A
		, 85, -10 // B U
		},
		null,
		{ 46, -20 // D period
		, 65, -35 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -40 // D Y
		},
		null,
		{ 44, -92 // F comma
		, 46, -110 // F period
		, 65, -90 // F A
		, 97, -25 // F a
		, 101, -25 // F e
		, 111, -25 // F o
		},
		null,
		null,
		null,
		{ 46, -20 // J period
		, 65, -30 // J A
		, 97, -15 // J a
		, 101, -15 // J e
		, 111, -15 // J o
		, 117, -15 // J u
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -25 // K o
		, 117, -15 // K u
		, 121, -45 // K y
		},
		{ 39, -110 // L quoteright
		, 84, -92 // L T
		, 86, -92 // L V
		, 87, -92 // L W
		, 89, -92 // L Y
		, 121, -55 // L y
		, 211, -20 // L quotedblright
		, 213, -110 // L quoteright
		},
		null,
		{ 65, -20 // N A
		},
		{ 65, -40 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -92 // P comma
		, 46, -110 // P period
		, 65, -74 // P A
		, 97, -10 // P a
		, 101, -20 // P e
		, 111, -20 // P o
		},
		{ 46, -20 // Q period
		, 85, -10 // Q U
		},
		{ 79, -30 // R O
		, 84, -40 // R T
		, 85, -30 // R U
		, 86, -55 // R V
		, 87, -35 // R W
		, 89, -35 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -92 // T hyphen
		, 46, -90 // T period
		, 58, -74 // T colon
		, 59, -74 // T semicolon
		, 65, -90 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -18 // T i
		, 111, -92 // T o
		, 114, -74 // T r
		, 117, -92 // T u
		, 119, -74 // T w
		, 121, -74 // T y
		},
		{ 44, -50 // U comma
		, 46, -50 // U period
		, 65, -60 // U A
		},
		{ 44, -129 // V comma
		, 45, -74 // V hyphen
		, 46, -145 // V period
		, 58, -92 // V colon
		, 59, -92 // V semicolon
		, 65, -135 // V A
		, 71, -30 // V G
		, 79, -45 // V O
		, 97, -92 // V a
		, 101, -100 // V e
		, 105, -37 // V i
		, 111, -100 // V o
		, 117, -92 // V u
		},
		{ 44, -92 // W comma
		, 45, -37 // W hyphen
		, 46, -92 // W period
		, 58, -55 // W colon
		, 59, -55 // W semicolon
		, 65, -120 // W A
		, 79, -10 // W O
		, 97, -65 // W a
		, 101, -65 // W e
		, 105, -18 // W i
		, 111, -75 // W o
		, 117, -50 // W u
		, 121, -60 // W y
		},
		null,
		{ 44, -92 // Y comma
		, 45, -92 // Y hyphen
		, 46, -92 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -110 // Y A
		, 79, -35 // Y O
		, 97, -85 // Y a
		, 101, -111 // Y e
		, 105, -37 // Y i
		, 111, -111 // Y o
		, 117, -92 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 118, -25 // a v
		},
		{ 46, -40 // b period
		, 98, -10 // b b
		, 117, -20 // b u
		, 118, -15 // b v
		},
		null,
		{ 119, -15 // d w
		},
		{ 118, -15 // e v
		},
		{ 39, 55 // f quoteright
		, 44, -15 // f comma
		, 46, -15 // f period
		, 105, -25 // f i
		, 111, -25 // f o
		, 211, 50 // f quotedblright
		, 213, 55 // f quoteright
		, 245, -35 // f dotlessi
		},
		{ 46, -15 // g period
		},
		{ 121, -15 // h y
		},
		{ 118, -10 // i v
		},
		null,
		{ 101, -10 // k e
		, 111, -15 // k o
		, 121, -15 // k y
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 118, -10 // o v
		, 119, -10 // o w
		},
		null,
		null,
		{ 44, -92 // r comma
		, 45, -37 // r hyphen
		, 46, -100 // r period
		, 99, -18 // r c
		, 101, -18 // r e
		, 103, -10 // r g
		, 110, -15 // r n
		, 111, -18 // r o
		, 112, -10 // r p
		, 113, -18 // r q
		, 118, -10 // r v
		},
		null,
		null,
		null,
		{ 44, -55 // v comma
		, 46, -70 // v period
		, 97, -10 // v a
		, 101, -10 // v e
		, 111, -10 // v o
		},
		{ 44, -55 // w comma
		, 46, -70 // w period
		, 111, -10 // w o
		},
		null,
		{ 44, -55 // y comma
		, 46, -70 // y period
		, 101, -10 // y e
		, 111, -25 // y o
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
		{ 65, -55 // space A
		, 84, -30 // space T
		, 86, -45 // space V
		, 87, -30 // space W
		, 89, -55 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 65, -10 // quotedblleft A
		},
		null,
		{ 65, -10 // quoteleft A
		, 212, -63 // quoteleft quoteleft
		},
		{ 32, -74 // quoteright space
		, 39, -63 // quoteright quoteright
		, 100, -20 // quoteright d
		, 114, -20 // quoteright r
		, 115, -37 // quoteright s
		, 118, -20 // quoteright v
		, 202, -74 // quoteright space
		, 213, -63 // quoteright quoteright
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

	public TimesBold(int encoding, int fontsize) {
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
		return PdfName.TIMES_BOLD;
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
		case ITALIC:
			return TIMES_ITALIC;
		case BOLDITALIC:
			return TIMES_BOLDITALIC;
		default:
			return TIMES_BOLD;
		}
	}
}
