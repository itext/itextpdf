/*
 * @(#)TimesRoman.java				0.23 2000/02/02
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
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.4/tir_____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class TimesRoman extends PdfFontMetrics {

// static membervariables

	/** Contains the widths of the TimesRoman characters. */
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
		408, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		564, //plus
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
		278, //colon
		278, //semicolon
		564, //less
		564, //equal
		564, //greater
		444, //question
		921, //at
		722, //A
		667, //B
		667, //C
		722, //D
		611, //E
		556, //F
		722, //G
		722, //H
		333, //I
		389, //J
		722, //K
		611, //L
		889, //M
		722, //N
		722, //O
		556, //P
		722, //Q
		667, //R
		556, //S
		611, //T
		722, //U
		722, //V
		944, //W
		722, //X
		722, //Y
		611, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		469, //asciicircum
		500, //underscore
		333, //quoteleft
		444, //a
		500, //b
		444, //c
		500, //d
		444, //e
		333, //f
		500, //g
		500, //h
		278, //i
		278, //j
		500, //k
		278, //l
		778, //m
		500, //n
		500, //o
		500, //p
		500, //q
		333, //r
		389, //s
		278, //t
		500, //u
		500, //v
		722, //w
		500, //x
		500, //y
		444, //z
		480, //braceleft
		200, //bar
		480, //braceright
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
		333, //exclamdown
		500, //cent
		500, //sterling
		167, //fraction
		500, //yen
		500, //florin
		500, //section
		500, //currency
		180, //quotesingle
		444, //quotedblleft
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
		453, //paragraph
		350, //bullet
		333, //quotesinglbase
		444, //quotedblbase
		444, //quotedblright
		500, //guillemotright
		1000, //ellipsis
		1000, //perthousand
		0, //
		444, //questiondown
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
		889, //AE
		0, //
		276, //ordfeminine
		0, //
		0, //
		0, //
		0, //
		611, //Lslash
		722, //Oslash
		889, //OE
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
		{ 65, -55 // space A
		, 84, -18 // space T
		, 86, -50 // space V
		, 87, -30 // space W
		, 89, -90 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -50 // quoteright d
		, 108, -10 // quoteright l
		, 114, -50 // quoteright r
		, 115, -55 // quoteright s
		, 116, -18 // quoteright t
		, 118, -50 // quoteright v
		},
		null,
		null,
		null,
		null,
		{ 39, -70 // comma quoteright
		, 186, -70 // comma quotedblright
		},
		null,
		{ 39, -70 // period quoteright
		, 186, -70 // period quotedblright
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
		{ 39, -111 // A quoteright
		, 67, -40 // A C
		, 71, -40 // A G
		, 79, -55 // A O
		, 81, -55 // A Q
		, 84, -111 // A T
		, 85, -55 // A U
		, 86, -135 // A V
		, 87, -90 // A W
		, 89, -105 // A Y
		, 118, -74 // A v
		, 119, -92 // A w
		, 121, -92 // A y
		},
		{ 65, -35 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -40 // D A
		, 86, -40 // D V
		, 87, -30 // D W
		, 89, -55 // D Y
		},
		null,
		{ 44, -80 // F comma
		, 46, -80 // F period
		, 65, -74 // F A
		, 97, -15 // F a
		, 111, -15 // F o
		},
		null,
		null,
		null,
		{ 65, -60 // J A
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -35 // K o
		, 117, -15 // K u
		, 121, -25 // K y
		},
		{ 39, -92 // L quoteright
		, 84, -92 // L T
		, 86, -100 // L V
		, 87, -74 // L W
		, 89, -100 // L Y
		, 121, -55 // L y
		},
		null,
		{ 65, -35 // N A
		},
		{ 65, -35 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -35 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -111 // P comma
		, 46, -111 // P period
		, 65, -92 // P A
		, 97, -15 // P a
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 84, -60 // R T
		, 85, -40 // R U
		, 86, -80 // R V
		, 87, -55 // R W
		, 89, -65 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -92 // T hyphen
		, 46, -74 // T period
		, 58, -50 // T colon
		, 59, -55 // T semicolon
		, 65, -93 // T A
		, 79, -18 // T O
		, 97, -80 // T a
		, 101, -70 // T e
		, 105, -35 // T i
		, 111, -80 // T o
		, 114, -35 // T r
		, 117, -45 // T u
		, 119, -80 // T w
		, 121, -80 // T y
		},
		{ 65, -40 // U A
		},
		{ 44, -129 // V comma
		, 45, -100 // V hyphen
		, 46, -129 // V period
		, 58, -74 // V colon
		, 59, -74 // V semicolon
		, 65, -135 // V A
		, 71, -15 // V G
		, 79, -40 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -60 // V i
		, 111, -129 // V o
		, 117, -75 // V u
		},
		{ 44, -92 // W comma
		, 45, -65 // W hyphen
		, 46, -92 // W period
		, 58, -37 // W colon
		, 59, -37 // W semicolon
		, 65, -120 // W A
		, 79, -10 // W O
		, 97, -80 // W a
		, 101, -80 // W e
		, 105, -40 // W i
		, 111, -80 // W o
		, 117, -50 // W u
		, 121, -73 // W y
		},
		null,
		{ 44, -129 // Y comma
		, 45, -111 // Y hyphen
		, 46, -129 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -120 // Y A
		, 79, -30 // Y O
		, 97, -100 // Y a
		, 101, -100 // Y e
		, 105, -55 // Y i
		, 111, -110 // Y o
		, 117, -111 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 65, -80 // quoteleft A
		, 96, -74 // quoteleft quoteleft
		},
		{ 118, -20 // a v
		, 119, -15 // a w
		},
		{ 46, -40 // b period
		, 117, -20 // b u
		, 118, -15 // b v
		},
		{ 121, -15 // c y
		},
		null,
		{ 103, -15 // e g
		, 118, -25 // e v
		, 119, -25 // e w
		, 120, -15 // e x
		, 121, -15 // e y
		},
		{ 39, 55 // f quoteright
		, 97, -10 // f a
		, 102, -25 // f f
		, 105, -20 // f i
		, 245, -50 // f dotlessi
		},
		{ 97, -5 // g a
		},
		{ 121, -5 // h y
		},
		{ 118, -25 // i v
		},
		null,
		{ 101, -10 // k e
		, 111, -10 // k o
		, 121, -15 // k y
		},
		{ 119, -10 // l w
		},
		null,
		{ 118, -40 // n v
		, 121, -15 // n y
		},
		{ 118, -15 // o v
		, 119, -25 // o w
		, 121, -10 // o y
		},
		{ 121, -10 // p y
		},
		null,
		{ 44, -40 // r comma
		, 45, -20 // r hyphen
		, 46, -55 // r period
		, 103, -18 // r g
		},
		null,
		null,
		null,
		{ 44, -65 // v comma
		, 46, -65 // v period
		, 97, -25 // v a
		, 101, -15 // v e
		, 111, -20 // v o
		},
		{ 44, -65 // w comma
		, 46, -65 // w period
		, 97, -10 // w a
		, 111, -10 // w o
		},
		{ 101, -15 // x e
		},
		{ 44, -65 // y comma
		, 46, -65 // y period
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
		{ 65, -80 // quotedblleft A
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

	/** Contains the widths of the TimesRoman characters. */
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
		408, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		564, //plus
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
		278, //colon
		278, //semicolon
		564, //less
		564, //equal
		564, //greater
		444, //question
		921, //at
		722, //A
		667, //B
		667, //C
		722, //D
		611, //E
		556, //F
		722, //G
		722, //H
		333, //I
		389, //J
		722, //K
		611, //L
		889, //M
		722, //N
		722, //O
		556, //P
		722, //Q
		667, //R
		556, //S
		611, //T
		722, //U
		722, //V
		944, //W
		722, //X
		722, //Y
		611, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		469, //asciicircum
		500, //underscore
		333, //grave
		444, //a
		500, //b
		444, //c
		500, //d
		444, //e
		333, //f
		500, //g
		500, //h
		278, //i
		278, //j
		500, //k
		278, //l
		778, //m
		500, //n
		500, //o
		500, //p
		500, //q
		333, //r
		389, //s
		278, //t
		500, //u
		500, //v
		722, //w
		500, //x
		500, //y
		444, //z
		480, //braceleft
		200, //bar
		480, //braceright
		541, //asciitilde
		350, //bullet
		350, //bullet
		350, //bullet
		333, //quotesinglbase
		500, //florin
		444, //quotedblbase
		1000, //ellipsis
		500, //dagger
		500, //daggerdbl
		333, //circumflex
		1000, //perthousand
		556, //Scaron
		333, //guilsinglleft
		889, //OE
		350, //bullet
		350, //bullet
		350, //bullet
		350, //bullet
		333, //quoteleft
		333, //quoteright
		444, //quotedblleft
		444, //quotedblright
		350, //bullet
		500, //endash
		1000, //emdash
		333, //tilde
		980, //trademark
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
		200, //brokenbar
		500, //section
		333, //dieresis
		760, //copyright
		276, //ordfeminine
		500, //guillemotleft
		564, //logicalnot
		333, //hyphen
		760, //registered
		333, //macron
		400, //degree
		564, //plusminus
		300, //twosuperior
		300, //threesuperior
		333, //acute
		500, //mu
		453, //paragraph
		250, //periodcentered
		333, //cedilla
		300, //onesuperior
		310, //ordmasculine
		500, //guillemotright
		750, //onequarter
		750, //onehalf
		750, //threequarters
		444, //questiondown
		722, //Agrave
		722, //Aacute
		722, //Acircumflex
		722, //Atilde
		722, //Adieresis
		722, //Aring
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
		722, //Ntilde
		722, //Ograve
		722, //Oacute
		722, //Ocircumflex
		722, //Otilde
		722, //Odieresis
		564, //multiply
		722, //Oslash
		722, //Ugrave
		722, //Uacute
		722, //Ucircumflex
		722, //Udieresis
		722, //Yacute
		556, //Thorn
		500, //germandbls
		444, //agrave
		444, //aacute
		444, //acircumflex
		444, //atilde
		444, //adieresis
		444, //aring
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
		564, //divide
		500, //oslash
		500, //ugrave
		500, //uacute
		500, //ucircumflex
		500, //udieresis
		500, //yacute
		500, //thorn
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
		, 84, -18 // space T
		, 86, -50 // space V
		, 87, -30 // space W
		, 89, -90 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -50 // quoteright d
		, 108, -10 // quoteright l
		, 114, -50 // quoteright r
		, 115, -55 // quoteright s
		, 116, -18 // quoteright t
		, 118, -50 // quoteright v
		, 146, -74 // quoteright quoteright
		, 160, -74 // quoteright space
		},
		null,
		null,
		null,
		null,
		{ 39, -70 // comma quoteright
		, 146, -70 // comma quoteright
		, 148, -70 // comma quotedblright
		},
		null,
		{ 39, -70 // period quoteright
		, 146, -70 // period quoteright
		, 148, -70 // period quotedblright
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
		{ 39, -111 // A quoteright
		, 67, -40 // A C
		, 71, -40 // A G
		, 79, -55 // A O
		, 81, -55 // A Q
		, 84, -111 // A T
		, 85, -55 // A U
		, 86, -135 // A V
		, 87, -90 // A W
		, 89, -105 // A Y
		, 118, -74 // A v
		, 119, -92 // A w
		, 121, -92 // A y
		, 146, -111 // A quoteright
		},
		{ 65, -35 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -40 // D A
		, 86, -40 // D V
		, 87, -30 // D W
		, 89, -55 // D Y
		},
		null,
		{ 44, -80 // F comma
		, 46, -80 // F period
		, 65, -74 // F A
		, 97, -15 // F a
		, 111, -15 // F o
		},
		null,
		null,
		null,
		{ 65, -60 // J A
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -35 // K o
		, 117, -15 // K u
		, 121, -25 // K y
		},
		{ 39, -92 // L quoteright
		, 84, -92 // L T
		, 86, -100 // L V
		, 87, -74 // L W
		, 89, -100 // L Y
		, 121, -55 // L y
		, 146, -92 // L quoteright
		},
		null,
		{ 65, -35 // N A
		},
		{ 65, -35 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -35 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -111 // P comma
		, 46, -111 // P period
		, 65, -92 // P A
		, 97, -15 // P a
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 84, -60 // R T
		, 85, -40 // R U
		, 86, -80 // R V
		, 87, -55 // R W
		, 89, -65 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -92 // T hyphen
		, 46, -74 // T period
		, 58, -50 // T colon
		, 59, -55 // T semicolon
		, 65, -93 // T A
		, 79, -18 // T O
		, 97, -80 // T a
		, 101, -70 // T e
		, 105, -35 // T i
		, 111, -80 // T o
		, 114, -35 // T r
		, 117, -45 // T u
		, 119, -80 // T w
		, 121, -80 // T y
		, 173, -92 // T hyphen
		},
		{ 65, -40 // U A
		},
		{ 44, -129 // V comma
		, 45, -100 // V hyphen
		, 46, -129 // V period
		, 58, -74 // V colon
		, 59, -74 // V semicolon
		, 65, -135 // V A
		, 71, -15 // V G
		, 79, -40 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -60 // V i
		, 111, -129 // V o
		, 117, -75 // V u
		, 173, -100 // V hyphen
		},
		{ 44, -92 // W comma
		, 45, -65 // W hyphen
		, 46, -92 // W period
		, 58, -37 // W colon
		, 59, -37 // W semicolon
		, 65, -120 // W A
		, 79, -10 // W O
		, 97, -80 // W a
		, 101, -80 // W e
		, 105, -40 // W i
		, 111, -80 // W o
		, 117, -50 // W u
		, 121, -73 // W y
		, 173, -65 // W hyphen
		},
		null,
		{ 44, -129 // Y comma
		, 45, -111 // Y hyphen
		, 46, -129 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -120 // Y A
		, 79, -30 // Y O
		, 97, -100 // Y a
		, 101, -100 // Y e
		, 105, -55 // Y i
		, 111, -110 // Y o
		, 117, -111 // Y u
		, 173, -111 // Y hyphen
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 118, -20 // a v
		, 119, -15 // a w
		},
		{ 46, -40 // b period
		, 117, -20 // b u
		, 118, -15 // b v
		},
		{ 121, -15 // c y
		},
		null,
		{ 103, -15 // e g
		, 118, -25 // e v
		, 119, -25 // e w
		, 120, -15 // e x
		, 121, -15 // e y
		},
		{ 39, 55 // f quoteright
		, 97, -10 // f a
		, 102, -25 // f f
		, 105, -20 // f i
		, 146, 55 // f quoteright
		},
		{ 97, -5 // g a
		},
		{ 121, -5 // h y
		},
		{ 118, -25 // i v
		},
		null,
		{ 101, -10 // k e
		, 111, -10 // k o
		, 121, -15 // k y
		},
		{ 119, -10 // l w
		},
		null,
		{ 118, -40 // n v
		, 121, -15 // n y
		},
		{ 118, -15 // o v
		, 119, -25 // o w
		, 121, -10 // o y
		},
		{ 121, -10 // p y
		},
		null,
		{ 44, -40 // r comma
		, 45, -20 // r hyphen
		, 46, -55 // r period
		, 103, -18 // r g
		, 173, -20 // r hyphen
		},
		null,
		null,
		null,
		{ 44, -65 // v comma
		, 46, -65 // v period
		, 97, -25 // v a
		, 101, -15 // v e
		, 111, -20 // v o
		},
		{ 44, -65 // w comma
		, 46, -65 // w period
		, 97, -10 // w a
		, 111, -10 // w o
		},
		{ 101, -15 // x e
		},
		{ 44, -65 // y comma
		, 46, -65 // y period
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
		{ 65, -80 // quoteleft A
		, 145, -74 // quoteleft quoteleft
		},
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -50 // quoteright d
		, 108, -10 // quoteright l
		, 114, -50 // quoteright r
		, 115, -55 // quoteright s
		, 116, -18 // quoteright t
		, 118, -50 // quoteright v
		, 146, -74 // quoteright quoteright
		, 160, -74 // quoteright space
		},
		{ 65, -80 // quotedblleft A
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
		, 84, -18 // space T
		, 86, -50 // space V
		, 87, -30 // space W
		, 89, -90 // space Y
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
		408, //quotedbl
		500, //numbersign
		500, //dollar
		833, //percent
		778, //ampersand
		333, //quoteright
		333, //parenleft
		333, //parenright
		500, //asterisk
		564, //plus
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
		278, //colon
		278, //semicolon
		564, //less
		564, //equal
		564, //greater
		444, //question
		921, //at
		722, //A
		667, //B
		667, //C
		722, //D
		611, //E
		556, //F
		722, //G
		722, //H
		333, //I
		389, //J
		722, //K
		611, //L
		889, //M
		722, //N
		722, //O
		556, //P
		722, //Q
		667, //R
		556, //S
		611, //T
		722, //U
		722, //V
		944, //W
		722, //X
		722, //Y
		611, //Z
		333, //bracketleft
		278, //backslash
		333, //bracketright
		469, //asciicircum
		500, //underscore
		333, //grave
		444, //a
		500, //b
		444, //c
		500, //d
		444, //e
		333, //f
		500, //g
		500, //h
		278, //i
		278, //j
		500, //k
		278, //l
		778, //m
		500, //n
		500, //o
		500, //p
		500, //q
		333, //r
		389, //s
		278, //t
		500, //u
		500, //v
		722, //w
		500, //x
		500, //y
		444, //z
		480, //braceleft
		200, //bar
		480, //braceright
		541, //asciitilde
		0, //
		722, //Adieresis
		722, //Aring
		667, //Ccedilla
		611, //Eacute
		722, //Ntilde
		722, //Odieresis
		722, //Udieresis
		444, //aacute
		444, //agrave
		444, //acircumflex
		444, //adieresis
		444, //atilde
		444, //aring
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
		453, //paragraph
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
		564, //plusminus
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
		444, //questiondown
		333, //exclamdown
		564, //logicalnot
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
		722, //Otilde
		889, //OE
		722, //oe
		500, //endash
		1000, //emdash
		444, //quotedblleft
		444, //quotedblright
		333, //quoteleft
		333, //quoteright
		564, //divide
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
		444, //quotedblbase
		1000, //perthousand
		722, //Acircumflex
		611, //Ecircumflex
		722, //Aacute
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
		{ 65, -55 // space A
		, 84, -18 // space T
		, 86, -50 // space V
		, 87, -30 // space W
		, 89, -90 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -50 // quoteright d
		, 108, -10 // quoteright l
		, 114, -50 // quoteright r
		, 115, -55 // quoteright s
		, 116, -18 // quoteright t
		, 118, -50 // quoteright v
		, 202, -74 // quoteright space
		, 213, -74 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		{ 39, -70 // comma quoteright
		, 211, -70 // comma quotedblright
		, 213, -70 // comma quoteright
		},
		null,
		{ 39, -70 // period quoteright
		, 211, -70 // period quotedblright
		, 213, -70 // period quoteright
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
		{ 39, -111 // A quoteright
		, 67, -40 // A C
		, 71, -40 // A G
		, 79, -55 // A O
		, 81, -55 // A Q
		, 84, -111 // A T
		, 85, -55 // A U
		, 86, -135 // A V
		, 87, -90 // A W
		, 89, -105 // A Y
		, 118, -74 // A v
		, 119, -92 // A w
		, 121, -92 // A y
		, 213, -111 // A quoteright
		},
		{ 65, -35 // B A
		, 85, -10 // B U
		},
		null,
		{ 65, -40 // D A
		, 86, -40 // D V
		, 87, -30 // D W
		, 89, -55 // D Y
		},
		null,
		{ 44, -80 // F comma
		, 46, -80 // F period
		, 65, -74 // F A
		, 97, -15 // F a
		, 111, -15 // F o
		},
		null,
		null,
		null,
		{ 65, -60 // J A
		},
		{ 79, -30 // K O
		, 101, -25 // K e
		, 111, -35 // K o
		, 117, -15 // K u
		, 121, -25 // K y
		},
		{ 39, -92 // L quoteright
		, 84, -92 // L T
		, 86, -100 // L V
		, 87, -74 // L W
		, 89, -100 // L Y
		, 121, -55 // L y
		, 213, -92 // L quoteright
		},
		null,
		{ 65, -35 // N A
		},
		{ 65, -35 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -35 // O W
		, 88, -40 // O X
		, 89, -50 // O Y
		},
		{ 44, -111 // P comma
		, 46, -111 // P period
		, 65, -92 // P A
		, 97, -15 // P a
		},
		{ 85, -10 // Q U
		},
		{ 79, -40 // R O
		, 84, -60 // R T
		, 85, -40 // R U
		, 86, -80 // R V
		, 87, -55 // R W
		, 89, -65 // R Y
		},
		null,
		{ 44, -74 // T comma
		, 45, -92 // T hyphen
		, 46, -74 // T period
		, 58, -50 // T colon
		, 59, -55 // T semicolon
		, 65, -93 // T A
		, 79, -18 // T O
		, 97, -80 // T a
		, 101, -70 // T e
		, 105, -35 // T i
		, 111, -80 // T o
		, 114, -35 // T r
		, 117, -45 // T u
		, 119, -80 // T w
		, 121, -80 // T y
		},
		{ 65, -40 // U A
		},
		{ 44, -129 // V comma
		, 45, -100 // V hyphen
		, 46, -129 // V period
		, 58, -74 // V colon
		, 59, -74 // V semicolon
		, 65, -135 // V A
		, 71, -15 // V G
		, 79, -40 // V O
		, 97, -111 // V a
		, 101, -111 // V e
		, 105, -60 // V i
		, 111, -129 // V o
		, 117, -75 // V u
		},
		{ 44, -92 // W comma
		, 45, -65 // W hyphen
		, 46, -92 // W period
		, 58, -37 // W colon
		, 59, -37 // W semicolon
		, 65, -120 // W A
		, 79, -10 // W O
		, 97, -80 // W a
		, 101, -80 // W e
		, 105, -40 // W i
		, 111, -80 // W o
		, 117, -50 // W u
		, 121, -73 // W y
		},
		null,
		{ 44, -129 // Y comma
		, 45, -111 // Y hyphen
		, 46, -129 // Y period
		, 58, -92 // Y colon
		, 59, -92 // Y semicolon
		, 65, -120 // Y A
		, 79, -30 // Y O
		, 97, -100 // Y a
		, 101, -100 // Y e
		, 105, -55 // Y i
		, 111, -110 // Y o
		, 117, -111 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 118, -20 // a v
		, 119, -15 // a w
		},
		{ 46, -40 // b period
		, 117, -20 // b u
		, 118, -15 // b v
		},
		{ 121, -15 // c y
		},
		null,
		{ 103, -15 // e g
		, 118, -25 // e v
		, 119, -25 // e w
		, 120, -15 // e x
		, 121, -15 // e y
		},
		{ 39, 55 // f quoteright
		, 97, -10 // f a
		, 102, -25 // f f
		, 105, -20 // f i
		, 213, 55 // f quoteright
		, 245, -50 // f dotlessi
		},
		{ 97, -5 // g a
		},
		{ 121, -5 // h y
		},
		{ 118, -25 // i v
		},
		null,
		{ 101, -10 // k e
		, 111, -10 // k o
		, 121, -15 // k y
		},
		{ 119, -10 // l w
		},
		null,
		{ 118, -40 // n v
		, 121, -15 // n y
		},
		{ 118, -15 // o v
		, 119, -25 // o w
		, 121, -10 // o y
		},
		{ 121, -10 // p y
		},
		null,
		{ 44, -40 // r comma
		, 45, -20 // r hyphen
		, 46, -55 // r period
		, 103, -18 // r g
		},
		null,
		null,
		null,
		{ 44, -65 // v comma
		, 46, -65 // v period
		, 97, -25 // v a
		, 101, -15 // v e
		, 111, -20 // v o
		},
		{ 44, -65 // w comma
		, 46, -65 // w period
		, 97, -10 // w a
		, 111, -10 // w o
		},
		{ 101, -15 // x e
		},
		{ 44, -65 // y comma
		, 46, -65 // y period
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
		, 84, -18 // space T
		, 86, -50 // space V
		, 87, -30 // space W
		, 89, -90 // space Y
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 65, -80 // quotedblleft A
		},
		null,
		{ 65, -80 // quoteleft A
		, 212, -74 // quoteleft quoteleft
		},
		{ 32, -74 // quoteright space
		, 39, -74 // quoteright quoteright
		, 100, -50 // quoteright d
		, 108, -10 // quoteright l
		, 114, -50 // quoteright r
		, 115, -55 // quoteright s
		, 116, -18 // quoteright t
		, 118, -50 // quoteright v
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

	public TimesRoman(int encoding, int fontsize) {
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
		return PdfName.TIMES_ROMAN;
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
		case BOLD:
			return TIMES_BOLD;
		case ITALIC:
			return TIMES_ITALIC;
		case BOLDITALIC:
			return TIMES_BOLDITALIC;
		default:
			return TIMES_ROMAN;
		}
	}
}
