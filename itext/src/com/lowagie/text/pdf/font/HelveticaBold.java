/*
 * @(#)HelveticaBold.java			0.23 2000/02/02
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
 * This class contains the metrics of the font <VAR>Helvetica Bold</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.2/hvb_____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */

public class HelveticaBold extends PdfFontMetrics {

// static membervariables
						   					
	/** The widths of the Helvetica Bold characters. */
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
		333, //exclam
		474, //quotedbl
		556, //numbersign
		556, //dollar
		889, //percent
		722, //ampersand
		278, //quoteright
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
		333, //colon
		333, //semicolon
		584, //less
		584, //equal
		584, //greater
		611, //question
		975, //at
		722, //A
		722, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		722, //H
		278, //I
		556, //J
		722, //K
		611, //L
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
		333, //bracketleft
		278, //backslash
		333, //bracketright
		584, //asciicircum
		556, //underscore
		278, //quoteleft
		556, //a
		611, //b
		556, //c
		611, //d
		556, //e
		333, //f
		611, //g
		611, //h
		278, //i
		278, //j
		556, //k
		278, //l
		889, //m
		611, //n
		611, //o
		611, //p
		611, //q
		389, //r
		556, //s
		333, //t
		611, //u
		556, //v
		778, //w
		556, //x
		556, //y
		500, //z
		389, //braceleft
		280, //bar
		389, //braceright
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
		238, //quotesingle
		500, //quotedblleft
		556, //guillemotleft
		333, //guilsinglleft
		333, //guilsinglright
		611, //fi
		611, //fl
		0, //
		556, //endash
		556, //dagger
		556, //daggerdbl
		278, //periodcentered
		0, //
		556, //paragraph
		350, //bullet
		278, //quotesinglbase
		500, //quotedblbase
		500, //quotedblright
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
		611, //Lslash
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
		278, //lslash
		611, //oslash
		944, //oe
		611, //germandbls
		0, //
		0, //
		0, //
		0 //
	};

	/** Contains the kerning info of certain pairs of characters. */
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
		{ 84, -100 // space T
		, 86, -80 // space V
		, 87, -80 // space W
		, 89, -120 // space Y
		, 96, -60 // space quoteleft
		, 170, -80 // space quotedblleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -80 // quoteright space
		, 39, -46 // quoteright quoteright
		, 100, -80 // quoteright d
		, 108, -20 // quoteright l
		, 114, -40 // quoteright r
		, 115, -60 // quoteright s
		, 118, -20 // quoteright v
		},
		null,
		null,
		null,
		null,
		{ 32, -40 // comma space
		, 39, -120 // comma quoteright
		, 186, -120 // comma quotedblright
		},
		null,
		{ 32, -40 // period space
		, 39, -120 // period quoteright
		, 186, -120 // period quotedblright
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
		{ 32, -40 // colon space
		},
		{ 32, -40 // semicolon space
		},
		null,
		null,
		null,
		null,
		null,
		{ 67, -40 // A C
		, 71, -50 // A G
		, 79, -40 // A O
		, 81, -40 // A Q
		, 84, -90 // A T
		, 85, -50 // A U
		, 86, -80 // A V
		, 87, -60 // A W
		, 89, -110 // A Y
		, 117, -30 // A u
		, 118, -40 // A v
		, 119, -30 // A w
		, 121, -30 // A y
		},
		{ 65, -30 // B A
		, 85, -10 // B U
		},
		null,
		{ 44, -30 // D comma
		, 46, -30 // D period
		, 65, -40 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -70 // D Y
		},
		null,
		{ 44, -100 // F comma
		, 46, -100 // F period
		, 65, -80 // F A
		, 97, -20 // F a
		},
		null,
		null,
		null,
		{ 44, -20 // J comma
		, 46, -20 // J period
		, 65, -20 // J A
		, 117, -20 // J u
		},
		{ 79, -30 // K O
		, 101, -15 // K e
		, 111, -35 // K o
		, 117, -30 // K u
		, 121, -40 // K y
		},
		{ 39, -140 // L quoteright
		, 84, -90 // L T
		, 86, -110 // L V
		, 87, -80 // L W
		, 89, -120 // L Y
		, 121, -30 // L y
		, 186, -140 // L quotedblright
		},
		null,
		null,
		{ 44, -40 // O comma
		, 46, -40 // O period
		, 65, -50 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -50 // O X
		, 89, -70 // O Y
		},
		{ 44, -120 // P comma
		, 46, -120 // P period
		, 65, -100 // P A
		, 97, -30 // P a
		, 101, -30 // P e
		, 111, -40 // P o
		},
		{ 44, 20 // Q comma
		, 46, 20 // Q period
		, 85, -10 // Q U
		},
		{ 79, -20 // R O
		, 84, -20 // R T
		, 85, -20 // R U
		, 86, -50 // R V
		, 87, -40 // R W
		, 89, -50 // R Y
		},
		null,
		{ 44, -80 // T comma
		, 45, -120 // T hyphen
		, 46, -80 // T period
		, 58, -40 // T colon
		, 59, -40 // T semicolon
		, 65, -90 // T A
		, 79, -40 // T O
		, 97, -80 // T a
		, 101, -60 // T e
		, 111, -80 // T o
		, 114, -80 // T r
		, 117, -90 // T u
		, 119, -60 // T w
		, 121, -60 // T y
		},
		{ 44, -30 // U comma
		, 46, -30 // U period
		, 65, -50 // U A
		},
		{ 44, -120 // V comma
		, 45, -80 // V hyphen
		, 46, -120 // V period
		, 58, -40 // V colon
		, 59, -40 // V semicolon
		, 65, -80 // V A
		, 71, -50 // V G
		, 79, -50 // V O
		, 97, -60 // V a
		, 101, -50 // V e
		, 111, -90 // V o
		, 117, -60 // V u
		},
		{ 44, -80 // W comma
		, 45, -40 // W hyphen
		, 46, -80 // W period
		, 58, -10 // W colon
		, 59, -10 // W semicolon
		, 65, -60 // W A
		, 79, -20 // W O
		, 97, -40 // W a
		, 101, -35 // W e
		, 111, -60 // W o
		, 117, -45 // W u
		, 121, -20 // W y
		},
		null,
		{ 44, -100 // Y comma
		, 46, -100 // Y period
		, 58, -50 // Y colon
		, 59, -50 // Y semicolon
		, 65, -110 // Y A
		, 79, -70 // Y O
		, 97, -90 // Y a
		, 101, -80 // Y e
		, 111, -100 // Y o
		, 117, -100 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 96, -46 // quoteleft quoteleft
		},
		{ 103, -10 // a g
		, 118, -15 // a v
		, 119, -15 // a w
		, 121, -20 // a y
		},
		{ 108, -10 // b l
		, 117, -20 // b u
		, 118, -20 // b v
		, 121, -20 // b y
		},
		{ 104, -10 // c h
		, 107, -20 // c k
		, 108, -20 // c l
		, 121, -10 // c y
		},
		{ 100, -10 // d d
		, 118, -15 // d v
		, 119, -15 // d w
		, 121, -15 // d y
		},
		{ 44, 10 // e comma
		, 46, 20 // e period
		, 118, -15 // e v
		, 119, -15 // e w
		, 120, -15 // e x
		, 121, -15 // e y
		},
		{ 39, 30 // f quoteright
		, 44, -10 // f comma
		, 46, -10 // f period
		, 101, -10 // f e
		, 111, -20 // f o
		, 186, 30 // f quotedblright
		},
		{ 101, 10 // g e
		, 103, -10 // g g
		},
		{ 121, -20 // h y
		},
		null,
		null,
		{ 111, -15 // k o
		},
		{ 119, -15 // l w
		, 121, -15 // l y
		},
		{ 117, -20 // m u
		, 121, -30 // m y
		},
		{ 117, -10 // n u
		, 118, -40 // n v
		, 121, -20 // n y
		},
		{ 118, -20 // o v
		, 119, -15 // o w
		, 120, -30 // o x
		, 121, -20 // o y
		},
		{ 121, -15 // p y
		},
		null,
		{ 44, -60 // r comma
		, 45, -20 // r hyphen
		, 46, -60 // r period
		, 99, -20 // r c
		, 100, -20 // r d
		, 103, -15 // r g
		, 111, -20 // r o
		, 113, -20 // r q
		, 115, -15 // r s
		, 116, 20 // r t
		, 118, 10 // r v
		, 121, 10 // r y
		},
		{ 119, -15 // s w
		},
		null,
		null,
		{ 44, -80 // v comma
		, 46, -80 // v period
		, 97, -20 // v a
		, 111, -30 // v o
		},
		{ 44, -40 // w comma
		, 46, -40 // w period
		, 111, -20 // w o
		},
		{ 101, -10 // x e
		},
		{ 44, -80 // y comma
		, 46, -80 // y period
		, 97, -30 // y a
		, 101, -10 // y e
		, 111, -25 // y o
		},
		{ 101, 10 // z e
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
		{ 32, -80 // quotedblright space
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
		null
	};

	/** Contains the widths of the HelveticaBold characters. */
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
		333, //exclam
		474, //quotedbl
		556, //numbersign
		556, //dollar
		889, //percent
		722, //ampersand
		278, //quoteright
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
		333, //colon
		333, //semicolon
		584, //less
		584, //equal
		584, //greater
		611, //question
		975, //at
		722, //A
		722, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		722, //H
		278, //I
		556, //J
		722, //K
		611, //L
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
		333, //bracketleft
		278, //backslash
		333, //bracketright
		584, //asciicircum
		556, //underscore
		333, //grave
		556, //a
		611, //b
		556, //c
		611, //d
		556, //e
		333, //f
		611, //g
		611, //h
		278, //i
		278, //j
		556, //k
		278, //l
		889, //m
		611, //n
		611, //o
		611, //p
		611, //q
		389, //r
		556, //s
		333, //t
		611, //u
		556, //v
		778, //w
		556, //x
		556, //y
		500, //z
		389, //braceleft
		280, //bar
		389, //braceright
		584, //asciitilde
		350, //bullet
		350, //bullet
		350, //bullet
		278, //quotesinglbase
		556, //florin
		500, //quotedblbase
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
		278, //quoteleft
		278, //quoteright
		500, //quotedblleft
		500, //quotedblright
		350, //bullet
		556, //endash
		1000, //emdash
		333, //tilde
		1000, //trademark
		556, //scaron
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
		280, //brokenbar
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
		611, //mu
		556, //paragraph
		278, //periodcentered
		333, //cedilla
		333, //onesuperior
		365, //ordmasculine
		556, //guillemotright
		834, //onequarter
		834, //onehalf
		834, //threequarters
		611, //questiondown
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
		556, //ccedilla
		556, //egrave
		556, //eacute
		556, //ecircumflex
		556, //edieresis
		278, //igrave
		278, //iacute
		278, //icircumflex
		278, //idieresis
		611, //eth
		611, //ntilde
		611, //ograve
		611, //oacute
		611, //ocircumflex
		611, //otilde
		611, //odieresis
		584, //divide
		611, //oslash
		611, //ugrave
		611, //uacute
		611, //ucircumflex
		611, //udieresis
		556, //yacute
		611, //thorn
		556 //ydieresis
	};

	/** Contains the kerning info of certain pairs of characters. */
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
		{ 84, -100 // space T
		, 86, -80 // space V
		, 87, -80 // space W
		, 89, -120 // space Y
		, 145, -60 // space quoteleft
		, 147, -80 // space quotedblleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -80 // quoteright space
		, 39, -46 // quoteright quoteright
		, 100, -80 // quoteright d
		, 108, -20 // quoteright l
		, 114, -40 // quoteright r
		, 115, -60 // quoteright s
		, 118, -20 // quoteright v
		, 146, -46 // quoteright quoteright
		, 160, -80 // quoteright space
		},
		null,
		null,
		null,
		null,
		{ 32, -40 // comma space
		, 39, -120 // comma quoteright
		, 146, -120 // comma quoteright
		, 148, -120 // comma quotedblright
		, 160, -40 // comma space
		},
		null,
		{ 32, -40 // period space
		, 39, -120 // period quoteright
		, 146, -120 // period quoteright
		, 148, -120 // period quotedblright
		, 160, -40 // period space
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
		{ 32, -40 // colon space
		, 160, -40 // colon space
		},
		{ 32, -40 // semicolon space
		, 160, -40 // semicolon space
		},
		null,
		null,
		null,
		null,
		null,
		{ 67, -40 // A C
		, 71, -50 // A G
		, 79, -40 // A O
		, 81, -40 // A Q
		, 84, -90 // A T
		, 85, -50 // A U
		, 86, -80 // A V
		, 87, -60 // A W
		, 89, -110 // A Y
		, 117, -30 // A u
		, 118, -40 // A v
		, 119, -30 // A w
		, 121, -30 // A y
		},
		{ 65, -30 // B A
		, 85, -10 // B U
		},
		null,
		{ 44, -30 // D comma
		, 46, -30 // D period
		, 65, -40 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -70 // D Y
		},
		null,
		{ 44, -100 // F comma
		, 46, -100 // F period
		, 65, -80 // F A
		, 97, -20 // F a
		},
		null,
		null,
		null,
		{ 44, -20 // J comma
		, 46, -20 // J period
		, 65, -20 // J A
		, 117, -20 // J u
		},
		{ 79, -30 // K O
		, 101, -15 // K e
		, 111, -35 // K o
		, 117, -30 // K u
		, 121, -40 // K y
		},
		{ 39, -140 // L quoteright
		, 84, -90 // L T
		, 86, -110 // L V
		, 87, -80 // L W
		, 89, -120 // L Y
		, 121, -30 // L y
		, 146, -140 // L quoteright
		, 148, -140 // L quotedblright
		},
		null,
		null,
		{ 44, -40 // O comma
		, 46, -40 // O period
		, 65, -50 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -50 // O X
		, 89, -70 // O Y
		},
		{ 44, -120 // P comma
		, 46, -120 // P period
		, 65, -100 // P A
		, 97, -30 // P a
		, 101, -30 // P e
		, 111, -40 // P o
		},
		{ 44, 20 // Q comma
		, 46, 20 // Q period
		, 85, -10 // Q U
		},
		{ 79, -20 // R O
		, 84, -20 // R T
		, 85, -20 // R U
		, 86, -50 // R V
		, 87, -40 // R W
		, 89, -50 // R Y
		},
		null,
		{ 44, -80 // T comma
		, 45, -120 // T hyphen
		, 46, -80 // T period
		, 58, -40 // T colon
		, 59, -40 // T semicolon
		, 65, -90 // T A
		, 79, -40 // T O
		, 97, -80 // T a
		, 101, -60 // T e
		, 111, -80 // T o
		, 114, -80 // T r
		, 117, -90 // T u
		, 119, -60 // T w
		, 121, -60 // T y
		, 173, -120 // T hyphen
		},
		{ 44, -30 // U comma
		, 46, -30 // U period
		, 65, -50 // U A
		},
		{ 44, -120 // V comma
		, 45, -80 // V hyphen
		, 46, -120 // V period
		, 58, -40 // V colon
		, 59, -40 // V semicolon
		, 65, -80 // V A
		, 71, -50 // V G
		, 79, -50 // V O
		, 97, -60 // V a
		, 101, -50 // V e
		, 111, -90 // V o
		, 117, -60 // V u
		, 173, -80 // V hyphen
		},
		{ 44, -80 // W comma
		, 45, -40 // W hyphen
		, 46, -80 // W period
		, 58, -10 // W colon
		, 59, -10 // W semicolon
		, 65, -60 // W A
		, 79, -20 // W O
		, 97, -40 // W a
		, 101, -35 // W e
		, 111, -60 // W o
		, 117, -45 // W u
		, 121, -20 // W y
		, 173, -40 // W hyphen
		},
		null,
		{ 44, -100 // Y comma
		, 46, -100 // Y period
		, 58, -50 // Y colon
		, 59, -50 // Y semicolon
		, 65, -110 // Y A
		, 79, -70 // Y O
		, 97, -90 // Y a
		, 101, -80 // Y e
		, 111, -100 // Y o
		, 117, -100 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 103, -10 // a g
		, 118, -15 // a v
		, 119, -15 // a w
		, 121, -20 // a y
		},
		{ 108, -10 // b l
		, 117, -20 // b u
		, 118, -20 // b v
		, 121, -20 // b y
		},
		{ 104, -10 // c h
		, 107, -20 // c k
		, 108, -20 // c l
		, 121, -10 // c y
		},
		{ 100, -10 // d d
		, 118, -15 // d v
		, 119, -15 // d w
		, 121, -15 // d y
		},
		{ 44, 10 // e comma
		, 46, 20 // e period
		, 118, -15 // e v
		, 119, -15 // e w
		, 120, -15 // e x
		, 121, -15 // e y
		},
		{ 39, 30 // f quoteright
		, 44, -10 // f comma
		, 46, -10 // f period
		, 101, -10 // f e
		, 111, -20 // f o
		, 146, 30 // f quoteright
		, 148, 30 // f quotedblright
		},
		{ 101, 10 // g e
		, 103, -10 // g g
		},
		{ 121, -20 // h y
		},
		null,
		null,
		{ 111, -15 // k o
		},
		{ 119, -15 // l w
		, 121, -15 // l y
		},
		{ 117, -20 // m u
		, 121, -30 // m y
		},
		{ 117, -10 // n u
		, 118, -40 // n v
		, 121, -20 // n y
		},
		{ 118, -20 // o v
		, 119, -15 // o w
		, 120, -30 // o x
		, 121, -20 // o y
		},
		{ 121, -15 // p y
		},
		null,
		{ 44, -60 // r comma
		, 45, -20 // r hyphen
		, 46, -60 // r period
		, 99, -20 // r c
		, 100, -20 // r d
		, 103, -15 // r g
		, 111, -20 // r o
		, 113, -20 // r q
		, 115, -15 // r s
		, 116, 20 // r t
		, 118, 10 // r v
		, 121, 10 // r y
		, 173, -20 // r hyphen
		},
		{ 119, -15 // s w
		},
		null,
		null,
		{ 44, -80 // v comma
		, 46, -80 // v period
		, 97, -20 // v a
		, 111, -30 // v o
		},
		{ 44, -40 // w comma
		, 46, -40 // w period
		, 111, -20 // w o
		},
		{ 101, -10 // x e
		},
		{ 44, -80 // y comma
		, 46, -80 // y period
		, 97, -30 // y a
		, 101, -10 // y e
		, 111, -25 // y o
		},
		{ 101, 10 // z e
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
		{ 145, -46 // quoteleft quoteleft
		},
		{ 32, -80 // quoteright space
		, 39, -46 // quoteright quoteright
		, 100, -80 // quoteright d
		, 108, -20 // quoteright l
		, 114, -40 // quoteright r
		, 115, -60 // quoteright s
		, 118, -20 // quoteright v
		, 146, -46 // quoteright quoteright
		, 160, -80 // quoteright space
		},
		null,
		{ 32, -80 // quotedblright space
		, 160, -80 // quotedblright space
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
		{ 84, -100 // space T
		, 86, -80 // space V
		, 87, -80 // space W
		, 89, -120 // space Y
		, 145, -60 // space quoteleft
		, 147, -80 // space quotedblleft
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
		278, //space
		333, //exclam
		474, //quotedbl
		556, //numbersign
		556, //dollar
		889, //percent
		722, //ampersand
		278, //quoteright
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
		333, //colon
		333, //semicolon
		584, //less
		584, //equal
		584, //greater
		611, //question
		975, //at
		722, //A
		722, //B
		722, //C
		722, //D
		667, //E
		611, //F
		778, //G
		722, //H
		278, //I
		556, //J
		722, //K
		611, //L
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
		333, //bracketleft
		278, //backslash
		333, //bracketright
		584, //asciicircum
		556, //underscore
		333, //grave
		556, //a
		611, //b
		556, //c
		611, //d
		556, //e
		333, //f
		611, //g
		611, //h
		278, //i
		278, //j
		556, //k
		278, //l
		889, //m
		611, //n
		611, //o
		611, //p
		611, //q
		389, //r
		556, //s
		333, //t
		611, //u
		556, //v
		778, //w
		556, //x
		556, //y
		500, //z
		389, //braceleft
		280, //bar
		389, //braceright
		584, //asciitilde
		0, //
		722, //Adieresis
		722, //Aring
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
		556, //ccedilla
		556, //eacute
		556, //egrave
		556, //ecircumflex
		556, //edieresis
		278, //iacute
		278, //igrave
		278, //icircumflex
		278, //idieresis
		611, //ntilde
		611, //oacute
		611, //ograve
		611, //ocircumflex
		611, //odieresis
		611, //otilde
		611, //uacute
		611, //ugrave
		611, //ucircumflex
		611, //udieresis
		556, //dagger
		400, //degree
		556, //cent
		556, //sterling
		556, //section
		350, //bullet
		556, //paragraph
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
		611, //mu
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
		722, //Agrave
		722, //Atilde
		778, //Otilde
		1000, //OE
		944, //oe
		556, //endash
		1000, //emdash
		500, //quotedblleft
		500, //quotedblright
		278, //quoteleft
		278, //quoteright
		584, //divide
		0, //
		556, //ydieresis
		667, //Ydieresis
		167, //fraction
		556, //currency
		333, //guilsinglleft
		333, //guilsinglright
		611, //fi
		611, //fl
		556, //daggerdbl
		278, //periodcentered
		278, //quotesinglbase
		500, //quotedblbase
		1000, //perthousand
		722, //Acircumflex
		667, //Ecircumflex
		722, //Aacute
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
		{ 84, -100 // space T
		, 86, -80 // space V
		, 87, -80 // space W
		, 89, -120 // space Y
		, 210, -80 // space quotedblleft
		, 212, -60 // space quoteleft
		},
		null,
		null,
		null,
		null,
		null,
		null,
		{ 32, -80 // quoteright space
		, 39, -46 // quoteright quoteright
		, 100, -80 // quoteright d
		, 108, -20 // quoteright l
		, 114, -40 // quoteright r
		, 115, -60 // quoteright s
		, 118, -20 // quoteright v
		, 202, -80 // quoteright space
		, 213, -46 // quoteright quoteright
		},
		null,
		null,
		null,
		null,
		{ 32, -40 // comma space
		, 39, -120 // comma quoteright
		, 202, -40 // comma space
		, 211, -120 // comma quotedblright
		, 213, -120 // comma quoteright
		},
		null,
		{ 32, -40 // period space
		, 39, -120 // period quoteright
		, 202, -40 // period space
		, 211, -120 // period quotedblright
		, 213, -120 // period quoteright
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
		{ 32, -40 // colon space
		, 202, -40 // colon space
		},
		{ 32, -40 // semicolon space
		, 202, -40 // semicolon space
		},
		null,
		null,
		null,
		null,
		null,
		{ 67, -40 // A C
		, 71, -50 // A G
		, 79, -40 // A O
		, 81, -40 // A Q
		, 84, -90 // A T
		, 85, -50 // A U
		, 86, -80 // A V
		, 87, -60 // A W
		, 89, -110 // A Y
		, 117, -30 // A u
		, 118, -40 // A v
		, 119, -30 // A w
		, 121, -30 // A y
		},
		{ 65, -30 // B A
		, 85, -10 // B U
		},
		null,
		{ 44, -30 // D comma
		, 46, -30 // D period
		, 65, -40 // D A
		, 86, -40 // D V
		, 87, -40 // D W
		, 89, -70 // D Y
		},
		null,
		{ 44, -100 // F comma
		, 46, -100 // F period
		, 65, -80 // F A
		, 97, -20 // F a
		},
		null,
		null,
		null,
		{ 44, -20 // J comma
		, 46, -20 // J period
		, 65, -20 // J A
		, 117, -20 // J u
		},
		{ 79, -30 // K O
		, 101, -15 // K e
		, 111, -35 // K o
		, 117, -30 // K u
		, 121, -40 // K y
		},
		{ 39, -140 // L quoteright
		, 84, -90 // L T
		, 86, -110 // L V
		, 87, -80 // L W
		, 89, -120 // L Y
		, 121, -30 // L y
		, 211, -140 // L quotedblright
		, 213, -140 // L quoteright
		},
		null,
		null,
		{ 44, -40 // O comma
		, 46, -40 // O period
		, 65, -50 // O A
		, 84, -40 // O T
		, 86, -50 // O V
		, 87, -50 // O W
		, 88, -50 // O X
		, 89, -70 // O Y
		},
		{ 44, -120 // P comma
		, 46, -120 // P period
		, 65, -100 // P A
		, 97, -30 // P a
		, 101, -30 // P e
		, 111, -40 // P o
		},
		{ 44, 20 // Q comma
		, 46, 20 // Q period
		, 85, -10 // Q U
		},
		{ 79, -20 // R O
		, 84, -20 // R T
		, 85, -20 // R U
		, 86, -50 // R V
		, 87, -40 // R W
		, 89, -50 // R Y
		},
		null,
		{ 44, -80 // T comma
		, 45, -120 // T hyphen
		, 46, -80 // T period
		, 58, -40 // T colon
		, 59, -40 // T semicolon
		, 65, -90 // T A
		, 79, -40 // T O
		, 97, -80 // T a
		, 101, -60 // T e
		, 111, -80 // T o
		, 114, -80 // T r
		, 117, -90 // T u
		, 119, -60 // T w
		, 121, -60 // T y
		},
		{ 44, -30 // U comma
		, 46, -30 // U period
		, 65, -50 // U A
		},
		{ 44, -120 // V comma
		, 45, -80 // V hyphen
		, 46, -120 // V period
		, 58, -40 // V colon
		, 59, -40 // V semicolon
		, 65, -80 // V A
		, 71, -50 // V G
		, 79, -50 // V O
		, 97, -60 // V a
		, 101, -50 // V e
		, 111, -90 // V o
		, 117, -60 // V u
		},
		{ 44, -80 // W comma
		, 45, -40 // W hyphen
		, 46, -80 // W period
		, 58, -10 // W colon
		, 59, -10 // W semicolon
		, 65, -60 // W A
		, 79, -20 // W O
		, 97, -40 // W a
		, 101, -35 // W e
		, 111, -60 // W o
		, 117, -45 // W u
		, 121, -20 // W y
		},
		null,
		{ 44, -100 // Y comma
		, 46, -100 // Y period
		, 58, -50 // Y colon
		, 59, -50 // Y semicolon
		, 65, -110 // Y A
		, 79, -70 // Y O
		, 97, -90 // Y a
		, 101, -80 // Y e
		, 111, -100 // Y o
		, 117, -100 // Y u
		},
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		{ 103, -10 // a g
		, 118, -15 // a v
		, 119, -15 // a w
		, 121, -20 // a y
		},
		{ 108, -10 // b l
		, 117, -20 // b u
		, 118, -20 // b v
		, 121, -20 // b y
		},
		{ 104, -10 // c h
		, 107, -20 // c k
		, 108, -20 // c l
		, 121, -10 // c y
		},
		{ 100, -10 // d d
		, 118, -15 // d v
		, 119, -15 // d w
		, 121, -15 // d y
		},
		{ 44, 10 // e comma
		, 46, 20 // e period
		, 118, -15 // e v
		, 119, -15 // e w
		, 120, -15 // e x
		, 121, -15 // e y
		},
		{ 39, 30 // f quoteright
		, 44, -10 // f comma
		, 46, -10 // f period
		, 101, -10 // f e
		, 111, -20 // f o
		, 211, 30 // f quotedblright
		, 213, 30 // f quoteright
		},
		{ 101, 10 // g e
		, 103, -10 // g g
		},
		{ 121, -20 // h y
		},
		null,
		null,
		{ 111, -15 // k o
		},
		{ 119, -15 // l w
		, 121, -15 // l y
		},
		{ 117, -20 // m u
		, 121, -30 // m y
		},
		{ 117, -10 // n u
		, 118, -40 // n v
		, 121, -20 // n y
		},
		{ 118, -20 // o v
		, 119, -15 // o w
		, 120, -30 // o x
		, 121, -20 // o y
		},
		{ 121, -15 // p y
		},
		null,
		{ 44, -60 // r comma
		, 45, -20 // r hyphen
		, 46, -60 // r period
		, 99, -20 // r c
		, 100, -20 // r d
		, 103, -15 // r g
		, 111, -20 // r o
		, 113, -20 // r q
		, 115, -15 // r s
		, 116, 20 // r t
		, 118, 10 // r v
		, 121, 10 // r y
		},
		{ 119, -15 // s w
		},
		null,
		null,
		{ 44, -80 // v comma
		, 46, -80 // v period
		, 97, -20 // v a
		, 111, -30 // v o
		},
		{ 44, -40 // w comma
		, 46, -40 // w period
		, 111, -20 // w o
		},
		{ 101, -10 // x e
		},
		{ 44, -80 // y comma
		, 46, -80 // y period
		, 97, -30 // y a
		, 101, -10 // y e
		, 111, -25 // y o
		},
		{ 101, 10 // z e
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
		{ 84, -100 // space T
		, 86, -80 // space V
		, 87, -80 // space W
		, 89, -120 // space Y
		, 210, -80 // space quotedblleft
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
		{ 32, -80 // quotedblright space
		, 202, -80 // quotedblright space
		},
		{ 212, -46 // quoteleft quoteleft
		},
		{ 32, -80 // quoteright space
		, 39, -46 // quoteright quoteright
		, 100, -80 // quoteright d
		, 108, -20 // quoteright l
		, 114, -40 // quoteright r
		, 115, -60 // quoteright s
		, 118, -20 // quoteright v
		, 202, -80 // quoteright space
		, 213, -46 // quoteright quoteright
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

	public HelveticaBold(int encoding, int fontsize) {
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
		return PdfName.HELVETICA_BOLD;
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
			return HELVETICA;
		case ITALIC:
			return HELVETICA_OBLIQUE;
		case BOLDITALIC:
			return HELVETICA_BOLDOBLIQUE;
		default:
			return HELVETICA_BOLD;
		}
	}
}
