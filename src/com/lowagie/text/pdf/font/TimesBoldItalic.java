/*
 * @(#)TimesBoldItalic.java			0.23 2000/02/02
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
 * This class contains the metrics of the font <VAR>BoldItalic</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.4/tibi____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class TimesBoldItalic extends PdfFontMetrics {

// static membervariables

	/** Contains the widths of the TimesBoldItalic characters. */
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
		389, //exclam
		555, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
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
		832, //at
		667, //A
		667, //B
		667, //C
		722, //D
		667, //E
		667, //F
		722, //G
		778, //H
		389, //I
		500, //J
		667, //K
		611, //L
		889, //M
		722, //N
		722, //O
		611, //P
		722, //Q
		667, //R
		556, //S
		611, //T
		722, //U
		667, //V
		889, //W
		667, //X
		611, //Y
		611, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		570, //asciicircum
		500, //underscore
		333, //quoteleft
		500, //a
		500, //b
		444, //c
		500, //d
		444, //e
		333, //f
		500, //g
		556, //h
		278, //i
		278, //j
		500, //k
		278, //l
		778, //m
		556, //n
		500, //o
		500, //p
		500, //q
		389, //r
		389, //s
		278, //t
		556, //u
		444, //v
		667, //w
		500, //x
		444, //y
		389, //z
		348, //braceleft
		220, //bar
		348, //braceright
		570, //asciitilde
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
		0, //
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
		500, //paragraph
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
		944, //AE
		0, //
		266, //ordfeminine
		0, //
		0, //
		0, //
		0, //
		611, //Lslash
		722, //Oslash
		944, //OE
		300, //ordmasculine
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
		{ 65, -37 // space A
		, 86, -70 // space V
		, 87, -70 // space W
		, 89, -70 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -15 // quoteright d
		, 114, -15 // quoteright r
		, 115, -74 // quoteright s
		, 116, -37 // quoteright t
		, 118, -15 // quoteright v
		},
		null,
		null,
		null,
		null,
		{ 39, -95 // comma quoteright
		, 186, -95 // comma quotedblright
		},
		null,
		{ 39, -95 // period quoteright
		, 186, -95 // period quotedblright
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
		, 67, -65 // A C
		, 71, -60 // A G
		, 79, -50 // A O
		, 81, -55 // A Q
		, 84, -55 // A T
		, 85, -50 // A U
		, 86, -95 // A V
		, 87, -100 // A W
		, 89, -70 // A Y
		, 117, -30 // A u
		, 118, -74 // A v
		, 119, -74 // A w
		, 121, -74 // A y
		},
		{ 65, -25 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -25 // D A
		, 86, -50 // D V
		, 87, -40 // D W
		, 89, -50 // D Y
		},
		null,
		{ 44, -129 // F comma
		, 46, -129 // F period
		, 65, -100 // F A
		, 97, -95 // F a
		, 101, -100 // F e
		, 105, -40 // F i
		, 111, -70 // F o
		, 114, -50 // F r
		},
		null,
		null,
		null,
		{ 44, -10 // J comma
		, 46, -10 // J period
		, 65, -25 // J A
		, 97, -40 // J a
		, 101, -40 // J e
		, 111, -40 // J o
		, 117, -40 // J u
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -25 // K o
		, 117, -20 // K u
		, 121, -20 // K y
		},
		{ 39, -55 // L quoteright
		, 84, -18 // L T
		, 86, -37 // L V
		, 87, -37 // L W
		, 89, -37 // L Y
		, 121, -37 // L y
		},
		null,
		{ 65, -30 // N A
		},
		{ 65, -40 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -129 // P comma
		, 46, -129 // P period
		, 65, -85 // P A
		, 97, -40 // P a
		, 101, -50 // P e
		, 111, -55 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 84, -30 // R T
		, 85, -40 // R U
		, 86, -18 // R V
		, 87, -18 // R W
		, 89, -18 // R Y
		},
		null,
		{ 44, -92 // T comma
		, 45, -92 // T hyphen
		, 46, -92 // T period
		, 58, -74 // T colon
		, 59, -74 // T semicolon
		, 65, -55 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -37 // T i
		, 111, -95 // T o
		, 114, -37 // T r
		, 117, -37 // T u
		, 119, -37 // T w
		, 121, -37 // T y
		},
		{ 65, -45 // U A
		},
		{ 44, -129 // V comma
		, 45, -70 // V hyphen
		, 46, -129 // V period
		, 58, -74 // V colon
		, 59, -74 // V semicolon
		, 65, -85 // V A
		, 71, -10 // V G
		, 79, -30 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -55 // V i
		, 111, -111 // V o
		, 117, -55 // V u
		},
		{ 44, -74 // W comma
		, 45, -50 // W hyphen
		, 46, -74 // W period
		, 58, -55 // W colon
		, 59, -55 // W semicolon
		, 65, -74 // W A
		, 79, -15 // W O
		, 97, -85 // W a
		, 101, -90 // W e
		, 105, -37 // W i
		, 111, -80 // W o
		, 117, -55 // W u
		, 121, -55 // W y
		},
		null,
		{ 44, -92 // Y comma
		, 45, -92 // Y hyphen
		, 46, -74 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -74 // Y A
		, 79, -25 // Y O
		, 97, -92 // Y a
		, 101, -111 // Y e
		, 105, -55 // Y i
		, 111, -111 // Y o
		, 117, -92 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 96, -74 // quoteleft quoteleft
		},
		null,
		{ 46, -40 // b period
		, 98, -10 // b b
		, 117, -20 // b u
		},
		{ 104, -10 // c h
		, 107, -10 // c k
		},
		null,
		{ 98, -10 // e b
		},
		{ 39, 55 // f quoteright
		, 44, -10 // f comma
		, 46, -10 // f period
		, 101, -10 // f e
		, 102, -18 // f f
		, 111, -10 // f o
		, 245, -30 // f dotlessi
		},
		null,
		null,
		null,
		null,
		{ 101, -30 // k e
		, 111, -10 // k o
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 118, -15 // o v
		, 119, -25 // o w
		, 120, -10 // o x
		, 121, -10 // o y
		},
		null,
		null,
		{ 44, -65 // r comma
		, 46, -65 // r period
		},
		null,
		null,
		null,
		{ 44, -37 // v comma
		, 46, -37 // v period
		, 101, -15 // v e
		, 111, -15 // v o
		},
		{ 44, -37 // w comma
		, 46, -37 // w period
		, 97, -10 // w a
		, 101, -10 // w e
		, 111, -15 // w o
		},
		{ 101, -10 // x e
		},
		{ 44, -37 // y comma
		, 46, -37 // y period
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

	/** Contains the widths of the TimesBoldItalic characters. */
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
		389, //exclam
		555, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
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
		832, //at
		667, //A
		667, //B
		667, //C
		722, //D
		667, //E
		667, //F
		722, //G
		778, //H
		389, //I
		500, //J
		667, //K
		611, //L
		889, //M
		722, //N
		722, //O
		611, //P
		722, //Q
		667, //R
		556, //S
		611, //T
		722, //U
		667, //V
		889, //W
		667, //X
		611, //Y
		611, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		570, //asciicircum
		500, //underscore
		333, //grave
		500, //a
		500, //b
		444, //c
		500, //d
		444, //e
		333, //f
		500, //g
		556, //h
		278, //i
		278, //j
		500, //k
		278, //l
		778, //m
		556, //n
		500, //o
		500, //p
		500, //q
		389, //r
		389, //s
		278, //t
		556, //u
		444, //v
		667, //w
		500, //x
		444, //y
		389, //z
		348, //braceleft
		220, //bar
		348, //braceright
		570, //asciitilde
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
		944, //OE
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
		389, //zcaron
		611, //Ydieresis
		250, //space
		389, //exclamdown
		500, //cent
		500, //sterling
		500, //currency
		500, //yen
		220, //brokenbar
		500, //section
		333, //dieresis
		747, //copyright
		266, //ordfeminine
		500, //guillemotleft
		606, //logicalnot
		333, //hyphen
		747, //registered
		333, //macron
		400, //degree
		570, //plusminus
		300, //twosuperior
		300, //threesuperior
		333, //acute
		576, //mu
		500, //paragraph
		250, //periodcentered
		333, //cedilla
		300, //onesuperior
		300, //ordmasculine
		500, //guillemotright
		750, //onequarter
		750, //onehalf
		750, //threequarters
		500, //questiondown
		667, //Agrave
		667, //Aacute
		667, //Acircumflex
		667, //Atilde
		667, //Adieresis
		667, //Aring
		944, //AE
		667, //Ccedilla
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
		722, //Ograve
		722, //Oacute
		722, //Ocircumflex
		722, //Otilde
		722, //Odieresis
		570, //multiply
		722, //Oslash
		722, //Ugrave
		722, //Uacute
		722, //Ucircumflex
		722, //Udieresis
		611, //Yacute
		611, //Thorn
		500, //germandbls
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
		{ 65, -37 // space A
		, 86, -70 // space V
		, 87, -70 // space W
		, 89, -70 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -15 // quoteright d
		, 114, -15 // quoteright r
		, 115, -74 // quoteright s
		, 116, -37 // quoteright t
		, 118, -15 // quoteright v
		, 146, -74 // quoteright quoteright
		, 160, -74 // quoteright space
		},
		null,
		null,
		null,
		null,
		{ 39, -95 // comma quoteright
		, 146, -95 // comma quoteright
		, 148, -95 // comma quotedblright
		},
		null,
		{ 39, -95 // period quoteright
		, 146, -95 // period quoteright
		, 148, -95 // period quotedblright
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
		, 67, -65 // A C
		, 71, -60 // A G
		, 79, -50 // A O
		, 81, -55 // A Q
		, 84, -55 // A T
		, 85, -50 // A U
		, 86, -95 // A V
		, 87, -100 // A W
		, 89, -70 // A Y
		, 117, -30 // A u
		, 118, -74 // A v
		, 119, -74 // A w
		, 121, -74 // A y
		, 146, -74 // A quoteright
		},
		{ 65, -25 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -25 // D A
		, 86, -50 // D V
		, 87, -40 // D W
		, 89, -50 // D Y
		},
		null,
		{ 44, -129 // F comma
		, 46, -129 // F period
		, 65, -100 // F A
		, 97, -95 // F a
		, 101, -100 // F e
		, 105, -40 // F i
		, 111, -70 // F o
		, 114, -50 // F r
		},
		null,
		null,
		null,
		{ 44, -10 // J comma
		, 46, -10 // J period
		, 65, -25 // J A
		, 97, -40 // J a
		, 101, -40 // J e
		, 111, -40 // J o
		, 117, -40 // J u
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -25 // K o
		, 117, -20 // K u
		, 121, -20 // K y
		},
		{ 39, -55 // L quoteright
		, 84, -18 // L T
		, 86, -37 // L V
		, 87, -37 // L W
		, 89, -37 // L Y
		, 121, -37 // L y
		, 146, -55 // L quoteright
		},
		null,
		{ 65, -30 // N A
		},
		{ 65, -40 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -129 // P comma
		, 46, -129 // P period
		, 65, -85 // P A
		, 97, -40 // P a
		, 101, -50 // P e
		, 111, -55 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 84, -30 // R T
		, 85, -40 // R U
		, 86, -18 // R V
		, 87, -18 // R W
		, 89, -18 // R Y
		},
		null,
		{ 44, -92 // T comma
		, 45, -92 // T hyphen
		, 46, -92 // T period
		, 58, -74 // T colon
		, 59, -74 // T semicolon
		, 65, -55 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -37 // T i
		, 111, -95 // T o
		, 114, -37 // T r
		, 117, -37 // T u
		, 119, -37 // T w
		, 121, -37 // T y
		, 173, -92 // T hyphen
		},
		{ 65, -45 // U A
		},
		{ 44, -129 // V comma
		, 45, -70 // V hyphen
		, 46, -129 // V period
		, 58, -74 // V colon
		, 59, -74 // V semicolon
		, 65, -85 // V A
		, 71, -10 // V G
		, 79, -30 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -55 // V i
		, 111, -111 // V o
		, 117, -55 // V u
		, 173, -70 // V hyphen
		},
		{ 44, -74 // W comma
		, 45, -50 // W hyphen
		, 46, -74 // W period
		, 58, -55 // W colon
		, 59, -55 // W semicolon
		, 65, -74 // W A
		, 79, -15 // W O
		, 97, -85 // W a
		, 101, -90 // W e
		, 105, -37 // W i
		, 111, -80 // W o
		, 117, -55 // W u
		, 121, -55 // W y
		, 173, -50 // W hyphen
		},
		null,
		{ 44, -92 // Y comma
		, 45, -92 // Y hyphen
		, 46, -74 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -74 // Y A
		, 79, -25 // Y O
		, 97, -92 // Y a
		, 101, -111 // Y e
		, 105, -55 // Y i
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
		null,
		{ 46, -40 // b period
		, 98, -10 // b b
		, 117, -20 // b u
		},
		{ 104, -10 // c h
		, 107, -10 // c k
		},
		null,
		{ 98, -10 // e b
		},
		{ 39, 55 // f quoteright
		, 44, -10 // f comma
		, 46, -10 // f period
		, 101, -10 // f e
		, 102, -18 // f f
		, 111, -10 // f o
		, 146, 55 // f quoteright
		},
		null,
		null,
		null,
		null,
		{ 101, -30 // k e
		, 111, -10 // k o
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 118, -15 // o v
		, 119, -25 // o w
		, 120, -10 // o x
		, 121, -10 // o y
		},
		null,
		null,
		{ 44, -65 // r comma
		, 46, -65 // r period
		},
		null,
		null,
		null,
		{ 44, -37 // v comma
		, 46, -37 // v period
		, 101, -15 // v e
		, 111, -15 // v o
		},
		{ 44, -37 // w comma
		, 46, -37 // w period
		, 97, -10 // w a
		, 101, -10 // w e
		, 111, -15 // w o
		},
		{ 101, -10 // x e
		},
		{ 44, -37 // y comma
		, 46, -37 // y period
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
		{ 145, -74 // quoteleft quoteleft
		},
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -15 // quoteright d
		, 114, -15 // quoteright r
		, 115, -74 // quoteright s
		, 116, -37 // quoteright t
		, 118, -15 // quoteright v
		, 146, -74 // quoteright quoteright
		, 160, -74 // quoteright space
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
		{ 65, -37 // space A
		, 86, -70 // space V
		, 87, -70 // space W
		, 89, -70 // space Y
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
		389, //exclam
		555, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
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
		832, //at
		667, //A
		667, //B
		667, //C
		722, //D
		667, //E
		667, //F
		722, //G
		778, //H
		389, //I
		500, //J
		667, //K
		611, //L
		889, //M
		722, //N
		722, //O
		611, //P
		722, //Q
		667, //R
		556, //S
		611, //T
		722, //U
		667, //V
		889, //W
		667, //X
		611, //Y
		611, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		570, //asciicircum
		500, //underscore
		333, //grave
		500, //a
		500, //b
		444, //c
		500, //d
		444, //e
		333, //f
		500, //g
		556, //h
		278, //i
		278, //j
		500, //k
		278, //l
		778, //m
		556, //n
		500, //o
		500, //p
		500, //q
		389, //r
		389, //s
		278, //t
		556, //u
		444, //v
		667, //w
		500, //x
		444, //y
		389, //z
		348, //braceleft
		220, //bar
		348, //braceright
		570, //asciitilde
		0, //
		667, //Adieresis
		667, //Aring
		667, //Ccedilla
		667, //Eacute
		722, //Ntilde
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
		500, //paragraph
		500, //germandbls
		747, //registered
		747, //copyright
		1000, //trademark
		333, //acute
		333, //dieresis
		0, //
		944, //AE
		722, //Oslash
		0, //
		570, //plusminus
		0, //
		0, //
		500, //yen
		576, //mu
		0, //
		0, //
		0, //
		0, //
		0, //
		266, //ordfeminine
		300, //ordmasculine
		0, //
		722, //ae
		500, //oslash
		500, //questiondown
		389, //exclamdown
		606, //logicalnot
		0, //
		500, //florin
		0, //
		0, //
		0, //guilmotleft
		0, //guilmotright
		1000, //ellipsis
		250, //space
		667, //Agrave
		667, //Atilde
		722, //Otilde
		944, //OE
		722, //oe
		500, //endash
		1000, //emdash
		500, //quotedblleft
		500, //quotedblright
		333, //quoteleft
		333, //quoteright
		570, //divide
		0, //
		444, //ydieresis
		611, //Ydieresis
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
		667, //Acircumflex
		667, //Ecircumflex
		667, //Aacute
		667, //Edieresis
		667, //Egrave
		389, //Iacute
		389, //Icircumflex
		389, //Idieresis
		389, //Igrave
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
		{ 65, -37 // space A
		, 86, -70 // space V
		, 87, -70 // space W
		, 89, -70 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -15 // quoteright d
		, 114, -15 // quoteright r
		, 115, -74 // quoteright s
		, 116, -37 // quoteright t
		, 118, -15 // quoteright v
		, 202, -74 // quoteright space
		, 213, -74 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		{ 39, -95 // comma quoteright
		, 211, -95 // comma quotedblright
		, 213, -95 // comma quoteright
		},
		null,
		{ 39, -95 // period quoteright
		, 211, -95 // period quotedblright
		, 213, -95 // period quoteright
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
		, 67, -65 // A C
		, 71, -60 // A G
		, 79, -50 // A O
		, 81, -55 // A Q
		, 84, -55 // A T
		, 85, -50 // A U
		, 86, -95 // A V
		, 87, -100 // A W
		, 89, -70 // A Y
		, 117, -30 // A u
		, 118, -74 // A v
		, 119, -74 // A w
		, 121, -74 // A y
		, 213, -74 // A quoteright
		},
		{ 65, -25 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -25 // D A
		, 86, -50 // D V
		, 87, -40 // D W
		, 89, -50 // D Y
		},
		null,
		{ 44, -129 // F comma
		, 46, -129 // F period
		, 65, -100 // F A
		, 97, -95 // F a
		, 101, -100 // F e
		, 105, -40 // F i
		, 111, -70 // F o
		, 114, -50 // F r
		},
		null,
		null,
		null,
		{ 44, -10 // J comma
		, 46, -10 // J period
		, 65, -25 // J A
		, 97, -40 // J a
		, 101, -40 // J e
		, 111, -40 // J o
		, 117, -40 // J u
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -25 // K o
		, 117, -20 // K u
		, 121, -20 // K y
		},
		{ 39, -55 // L quoteright
		, 84, -18 // L T
		, 86, -37 // L V
		, 87, -37 // L W
		, 89, -37 // L Y
		, 121, -37 // L y
		, 213, -55 // L quoteright
		},
		null,
		{ 65, -30 // N A
		},
		{ 65, -40 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -129 // P comma
		, 46, -129 // P period
		, 65, -85 // P A
		, 97, -40 // P a
		, 101, -50 // P e
		, 111, -55 // P o
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 84, -30 // R T
		, 85, -40 // R U
		, 86, -18 // R V
		, 87, -18 // R W
		, 89, -18 // R Y
		},
		null,
		{ 44, -92 // T comma
		, 45, -92 // T hyphen
		, 46, -92 // T period
		, 58, -74 // T colon
		, 59, -74 // T semicolon
		, 65, -55 // T A
		, 79, -18 // T O
		, 97, -92 // T a
		, 101, -92 // T e
		, 105, -37 // T i
		, 111, -95 // T o
		, 114, -37 // T r
		, 117, -37 // T u
		, 119, -37 // T w
		, 121, -37 // T y
		},
		{ 65, -45 // U A
		},
		{ 44, -129 // V comma
		, 45, -70 // V hyphen
		, 46, -129 // V period
		, 58, -74 // V colon
		, 59, -74 // V semicolon
		, 65, -85 // V A
		, 71, -10 // V G
		, 79, -30 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -55 // V i
		, 111, -111 // V o
		, 117, -55 // V u
		},
		{ 44, -74 // W comma
		, 45, -50 // W hyphen
		, 46, -74 // W period
		, 58, -55 // W colon
		, 59, -55 // W semicolon
		, 65, -74 // W A
		, 79, -15 // W O
		, 97, -85 // W a
		, 101, -90 // W e
		, 105, -37 // W i
		, 111, -80 // W o
		, 117, -55 // W u
		, 121, -55 // W y
		},
		null,
		{ 44, -92 // Y comma
		, 45, -92 // Y hyphen
		, 46, -74 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -74 // Y A
		, 79, -25 // Y O
		, 97, -92 // Y a
		, 101, -111 // Y e
		, 105, -55 // Y i
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
		null,
		{ 46, -40 // b period
		, 98, -10 // b b
		, 117, -20 // b u
		},
		{ 104, -10 // c h
		, 107, -10 // c k
		},
		null,
		{ 98, -10 // e b
		},
		{ 39, 55 // f quoteright
		, 44, -10 // f comma
		, 46, -10 // f period
		, 101, -10 // f e
		, 102, -18 // f f
		, 111, -10 // f o
		, 213, 55 // f quoteright
		, 245, -30 // f dotlessi
		},
		null,
		null,
		null,
		null,
		{ 101, -30 // k e
		, 111, -10 // k o
		},
		null,
		null,
		{ 118, -40 // n v
		},
		{ 118, -15 // o v
		, 119, -25 // o w
		, 120, -10 // o x
		, 121, -10 // o y
		},
		null,
		null,
		{ 44, -65 // r comma
		, 46, -65 // r period
		},
		null,
		null,
		null,
		{ 44, -37 // v comma
		, 46, -37 // v period
		, 101, -15 // v e
		, 111, -15 // v o
		},
		{ 44, -37 // w comma
		, 46, -37 // w period
		, 97, -10 // w a
		, 101, -10 // w e
		, 111, -15 // w o
		},
		{ 101, -10 // x e
		},
		{ 44, -37 // y comma
		, 46, -37 // y period
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
		{ 65, -37 // space A
		, 86, -70 // space V
		, 87, -70 // space W
		, 89, -70 // space Y
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
		{ 212, -74 // quoteleft quoteleft
		},
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -15 // quoteright d
		, 114, -15 // quoteright r
		, 115, -74 // quoteright s
		, 116, -37 // quoteright t
		, 118, -15 // quoteright v
		, 202, -74 // quoteright space
		, 213, -74 // quoteright quoteright
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

	public TimesBoldItalic(int encoding, int fontsize) {
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
		return PdfName.TIMES_BOLDITALIC;
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
		case ITALIC:
			return TIMES_ITALIC;
		default:
			return TIMES_BOLDITALIC;
		}
	}
}
