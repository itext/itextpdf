/*
 * $Id$
 * $Name$
 * 
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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
 * Special thanks to Nassib Nassar of Etymon Systems, Inc. (info@etymon.com)
 * I used most of the public static membervariables of his class PjName.
 *     
 * Very special thanks to Troy Harrison, Systems Consultant
 * of CNA Life Department-Information Technology
 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>
 * His input concerning the changes in version rugPdf0.20 was
 * really very important.
 */

package com.lowagie.text.pdf;

/**
 * <CODE>PdfName</CODE> is an object that can be used as a name in a PDF-file.
 * <P>
 * A name, like a string, is a sequence of characters. It must begin with a slash
 * followed by a sequence of ASCII characters in the range 32 through 136 except
 * %, (, ), [, ], &lt;, &gt;, {, }, / and #. Any character except 0x00 may be included
 * in a name by writing its twocharacter hex code, preceded by #. The maximum number
 * of characters in a name is 127.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.5 (page 39-40).
 * <P>
 * In version rugPdf0.20 I made this class Comparable, this way all the entry's in a
 * <CODE>PdfDictionary</CODE> will be sorted alfabetically.
 *
 * @see		PdfObject
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 *
 * @author  bruno@lowagie.com
 * @version 0.39 2000/11/22
 *
 * @since   rugPdf0.10
 */

public class PdfName extends PdfObject implements Comparable {

// static membervariables (a variety of standard names used in PDF)
 
	/** This is a static final PdfName */
	public static final PdfName A = new PdfName("/A", 0);

	/** This is a static final PdfName */
	public static final PdfName AA = new PdfName("/AA", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ACTION = new PdfName("/Action", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ANNOT = new PdfName("/Annot", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ANNOTS = new PdfName("/Annots", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ASCII85DECODE = new PdfName("/ASCII85Decode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ASCIIHEXDECODE = new PdfName("/ASCIIHexDecode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName AUTHOR = new PdfName("/Author", 0);
 
	/** This is a static final PdfName */
	public static final PdfName B = new PdfName("/B", 0);
 
	/** This is a static final PdfName */
	public static final PdfName BASEFONT = new PdfName("/BaseFont", 0);
 
	/** This is a static final PdfName */
	public static final PdfName BBOX = new PdfName("/BBox", 0);
 
	/** This is a static final PdfName */
	public static final PdfName BITSPERCOMPONENT = new PdfName("/BitsPerComponent", 0);
 
	/** This is a static final PdfName */
	public static final PdfName BORDER = new PdfName("/Border", 0);
 
	/** This is a static final PdfName */
	public static final PdfName BS = new PdfName("/BS", 0);
 
	/** This is a static final PdfName */
	public static final PdfName C = new PdfName("/C", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CATALOG = new PdfName("/Catalog", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CCITTFAXDECODE = new PdfName("/CCITTFaxDecode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CENTERWINDOW = new PdfName("/CenterWindow", 0);
 
	/** This is a static final PdfName */
	public static final PdfName COLORS = new PdfName("/Colors", 0);
 
	/** This is a static final PdfName */
	public static final PdfName COLORSPACE = new PdfName("/ColorSpace", 0);
 
	/** This is a static final PdfName */
	public static final PdfName COLUMNS = new PdfName("/Columns", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CONTENT = new PdfName("/Content", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CONTENTS = new PdfName("/Contents", 0);
 
	/** This is a static final PdfName */
	public static final PdfName COUNT = new PdfName("/Count", 0);
 								 	
	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName COURIER = new PdfName("/Courier", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName COURIER_BOLD = new PdfName("/Courier-Bold", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName COURIER_OBLIQUE = new PdfName("/Courier-Oblique", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName COURIER_BOLDOBLIQUE = new PdfName("/Courier-BoldOblique", 0);

	/** This is a static final PdfName */
	public static final PdfName CREATIONDATE = new PdfName("/CreationDate", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CREATOR = new PdfName("/Creator", 0);
 
	/** This is a static final PdfName */
	public static final PdfName CROPBOX = new PdfName("/CropBox", 0);
 
	/** This is a static final PdfName */
	public static final PdfName D = new PdfName("/D", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DCTDECODE = new PdfName("/DCTDecode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DECODEPARMS = new PdfName("/DecodeParms", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DEST = new PdfName("/Dest", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DESTS = new PdfName("/Dests", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DEVICEGRAY = new PdfName("/DeviceGray", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DEVICERGB = new PdfName("/DeviceRGB", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DEVICECMYK = new PdfName("/DeviceCMYK", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DIRECTION = new PdfName("/Direction", 0);
 
	/** This is a static final PdfName */
	public static final PdfName DUR = new PdfName("/Dur", 0);
 
	/** This is a static final PdfName */
	public static final PdfName EARLYCHANGE = new PdfName("/EarlyChange", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ENCODING = new PdfName("/Encoding", 0);
 
	/** This is a static final PdfName */
	public static final PdfName EXTGSTATE = new PdfName("/ExtGState", 0);
 
	/** This is a static final PdfName */
	public static final PdfName F = new PdfName("/F", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FDECODEPARMS = new PdfName("/FDecodeParms", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FFILTER = new PdfName("/FFilter", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FILTER = new PdfName("/Filter", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FIRST = new PdfName("/First", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FIRSTCHAR = new PdfName("/FirstChar", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FIT = new PdfName("/Fit", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITH = new PdfName("/FitH", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITV = new PdfName("/FitV", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITR = new PdfName("/FitR", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITB = new PdfName("/FitB", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITBH = new PdfName("/FitBH", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITBV = new PdfName("/FitBV", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FITWINDOW = new PdfName("/FitWindow", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FLATEDECODE = new PdfName("/FlateDecode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FONT = new PdfName("/Font", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FONTDESCRIPTOR = new PdfName("/FontDescriptor", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FORM = new PdfName("/Form", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FORMTYPE = new PdfName("/FormType", 0);
 
	/** This is a static final PdfName */
	public static final PdfName FULLSCREEN = new PdfName("/FullScreen", 0);

	/** This is a static final PdfName of an attribute. */
	public static final PdfName GOTO = new PdfName("/GoTo", 0);

	/** This is a static final PdfName of an attribute. */
	public static final PdfName GOTOR = new PdfName("/GoToR", 0);

	/** This is a static final PdfName of an attribute. */
	public static final PdfName HEIGHT = new PdfName("/Height", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName HELVETICA = new PdfName("/Helvetica", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName HELVETICA_BOLD = new PdfName("/Helvetica-Bold", 0);

	/** This is a static PdfName PdfName of a base 14 type 1 font */
	public static final PdfName HELVETICA_OBLIQUE = new PdfName("/Helvetica-Oblique", 0);

	/** This is a static PdfName PdfName of a base 14 type 1 font */
	public static final PdfName HELVETICA_BOLDOBLIQUE = new PdfName("/Helvetica-BoldOblique", 0);
 
	/** This is a static final PdfName */
	public static final PdfName HID = new PdfName("/Hid", 0);
 
	/** This is a static final PdfName */
	public static final PdfName HIDEMENUBAR = new PdfName("/HideMenubar", 0);
 
	/** This is a static final PdfName */
	public static final PdfName HIDETOOLBAR = new PdfName("/HideToolbar", 0);
 
	/** This is a static final PdfName */
	public static final PdfName HIDEWINDOWUI = new PdfName("/HideWindowUI", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ID = new PdfName("/ID", 0);
 
	/** This is a static final PdfName */
	public static final PdfName IMAGE = new PdfName("/Image", 0);
 
	/** This is a static final PdfName */
	public static final PdfName IMAGEB = new PdfName("/ImageB", 0);
 
	/** This is a static final PdfName */
	public static final PdfName IMAGEC = new PdfName("/ImageC", 0);
 
	/** This is a static final PdfName */
	public static final PdfName IMAGEI = new PdfName("/ImageI", 0);
 
	/** This is a static final PdfName */
	public static final PdfName INDEXED = new PdfName("/Indexed", 0);
 
	/** This is a static final PdfName */
	public static final PdfName INFO = new PdfName("/Info", 0);
 
	/** This is a static final PdfName */
	public static final PdfName KEYWORDS = new PdfName("/Keywords", 0);
 
	/** This is a static final PdfName */
	public static final PdfName KIDS = new PdfName("/Kids", 0);
 
	/** This is a static final PdfName */
	public static final PdfName L2R = new PdfName("/L2R", 0);
 
	/** This is a static final PdfName */
	public static final PdfName LAST = new PdfName("/Last", 0);
 
	/** This is a static final PdfName */
	public static final PdfName LASTCHAR = new PdfName("/LastChar", 0);
 
	/** This is a static final PdfName */
	public static final PdfName LENGTH = new PdfName("/Length", 0);
 
	/** This is a static final PdfName */
	public static final PdfName LIMITS = new PdfName("/Limits", 0);
 
	/** This is a static final PdfName */
	public static final PdfName LINK = new PdfName("/Link", 0);
 
	/** This is a static final PdfName */
	public static final PdfName LZWDECODE = new PdfName("/LZWDecode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName MATRIX = new PdfName("/Matrix", 0);
	
	/** This is a static final PdfName of an encoding */
	public static final PdfName MAC_EXPERT_ENCODING = new PdfName("/MacExpertEncoding", 0);
 
	/** This is a static final PdfName of an encoding */
	public static final PdfName MAC_ROMAN_ENCODING = new PdfName("/MacRomanEncoding", 0);

	/** This is a static final PdfName of an encoding */
	public static final PdfName MASK = new PdfName("/Mask", 0);

	/** This is a static final PdfName */
	public static final PdfName MEDIABOX = new PdfName("/MediaBox", 0);
 
	/** This is a static final PdfName */
	public static final PdfName MODDATE = new PdfName("/ModDate", 0);
 
	/** This is a static final PdfName */
	public static final PdfName NAME = new PdfName("/Name", 0);
 
	/** This is a static final PdfName */
	public static final PdfName NAMES = new PdfName("/Names", 0);
 
	/** This is a static final PdfName */
	public static final PdfName NEXT = new PdfName("/Next", 0);
 
	/** This is a static final PdfName */
	public static final PdfName NONFULLSCREENPAGEMODE = new PdfName("/NonFullScreenPageMode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ONECOLUMN = new PdfName("/OneColumn", 0);
 
	/** This is a static final PdfName */
	public static final PdfName OPENACTION = new PdfName("/OpenAction", 0);
 
	/** This is a static final PdfName */
	public static final PdfName OUTLINES = new PdfName("/Outlines", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PAGE = new PdfName("/Page", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PAGELAYOUT = new PdfName("/PageLayout", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PAGEMODE = new PdfName("/PageMode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PAGES = new PdfName("/Pages", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PARENT = new PdfName("/Parent", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PATTERN = new PdfName("/Pattern", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PDF = new PdfName("/PDF", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PREDICTOR = new PdfName("/Predictor", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PREV = new PdfName("/Prev", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PROCSET = new PdfName("/ProcSet", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PRODUCER = new PdfName("/Producer", 0);
 
	/** This is a static final PdfName */
	public static final PdfName PROPERTIES = new PdfName("/Properties", 0);
 
	/** This is a static final PdfName */
	public static final PdfName R2L = new PdfName("/R2L", 0);
 
	/** This is a static final PdfName */
	public static final PdfName RECT = new PdfName("/Rect", 0);
 
	/** This is a static final PdfName */
	public static final PdfName RESOURCES = new PdfName("/Resources", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ROOT = new PdfName("/Root", 0);
 
	/** This is a static final PdfName */
	public static final PdfName ROTATE = new PdfName("/Rotate", 0);
 
	/** This is a static final PdfName */
	public static final PdfName RUNLENGTHDECODE = new PdfName("/RunLengthDecode", 0);
 
	/** This is a static final PdfName */
	public static final PdfName S = new PdfName("/S", 0);
 
	/** This is a static final PdfName */
	public static final PdfName SINGLEPAGE = new PdfName("/SinglePage", 0);
 
	/** This is a static final PdfName */
	public static final PdfName SIZE = new PdfName("/Size", 0);
 
	/** This is a static final PdfName */
	public static final PdfName SUBJECT = new PdfName("/Subject", 0);
 
	/** This is a static final PdfName */
	public static final PdfName SUBTYPE = new PdfName("/Subtype", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName SYMBOL = new PdfName("/Symbol", 0);
 
	/** This is a static final PdfName */
	public static final PdfName T = new PdfName("/T", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TEXT = new PdfName("/Text", 0);
 
	/** This is a static final PdfName */
	public static final PdfName THUMB = new PdfName("/Thumb", 0);
 
	/** This is a static final PdfName */
	public static final PdfName THREADS = new PdfName("/Threads", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName TIMES_ROMAN = new PdfName("/Times-Roman", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName TIMES_BOLD = new PdfName("/Times-Bold", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName TIMES_ITALIC = new PdfName("/Times-Italic", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName TIMES_BOLDITALIC = new PdfName("/Times-BoldItalic", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TITLE = new PdfName("/Title", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TRANS = new PdfName("/Trans", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TWOCOLUMNLEFT = new PdfName("/TwoColumnLeft", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TWOCOLUMNRIGHT = new PdfName("/TwoColumnRight", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TYPE = new PdfName("/Type", 0);
 
	/** This is a static final PdfName */
	public static final PdfName TYPE1 = new PdfName("/Type1", 0);

	/** This is a static final PdfName of an attribute. */
	public static final PdfName U = new PdfName("/U", 0);
 
	/** This is a static final PdfName */
	public static final PdfName URI = new PdfName("/URI", 0);
 
	/** This is a static final PdfName */
	public static final PdfName USENONE = new PdfName("/UseNone", 0);
 
	/** This is a static final PdfName */
	public static final PdfName USEOUTLINES = new PdfName("/UseOutlines", 0);
 
	/** This is a static final PdfName */
	public static final PdfName USETHUMBS = new PdfName("/UseThumbs", 0);
 
	/** This is a static final PdfName */
	public static final PdfName VIEWERPREFERENCES = new PdfName("/ViewerPreferences", 0);

	/** This is a static final PdfName of an attribute. */
	public static final PdfName W = new PdfName("/W", 0);

	/** This is a static final PdfName of an attribute. */
	public static final PdfName WIDTH = new PdfName("/Width", 0);
 
	/** This is a static final PdfName */
	public static final PdfName WIDTHS = new PdfName("/Widths", 0);
	
	/** This is a static final PdfName of an encoding */
	public static final PdfName WIN_ANSI_ENCODING = new PdfName("/WinAnsiEncoding", 0);
 
	/** This is a static final PdfName */
	public static final PdfName XOBJECT = new PdfName("/XObject", 0);
 
	/** This is a static final PdfName */
	public static final PdfName XYZ = new PdfName("/XYZ", 0);

	/** This is a static final PdfName of a base 14 type 1 font */
	public static final PdfName ZAPFDINGBATS = new PdfName("/ZapfDingbats", 0);

// constructors

	/**
	 * Constructs a <CODE>PdfName</CODE>-object.
	 *
	 * @param		name		the new Name.
	 * @exception	BadPdfFormatException	gets thrown	when the name is too long or an illegal character is used.
	 *
	 * @since rugPdf0.10
	 */

	PdfName(String name) throws BadPdfFormatException {
		super(PdfObject.NAME, name);
		// The minimum number of characters in a name is 1, the maximum is 127 (the '/' not included)
		if (bytes.length < 1 || bytes.length > 127) {
			throw new BadPdfFormatException("The name is too long (" + bytes.length + " characters).");
		}
		// The name has to be checked for illegal characters
		int length = name.length();
		for (int i = 0; i < length; i++) {
			if (name.charAt(i) < 32 || name.charAt(i) > 255) {
				throw new BadPdfFormatException("Illegal character on position " + i + ".");
			}
		}
		// every special character has to be substituted
		StringBuffer pdfName = new StringBuffer("/");
		char character;
		// loop over all the characters
		for (int index = 0; index < length; index++) {
			character = name.charAt(index);
			// special characters are escaped (reference manual p.39)
			switch (character) {
			case ' ':
			case '%':
			case '(':
			case ')':
			case '<':
			case '>':
			case '[':
			case ']':
			case '{':
			case '}':
			case '/':
			case '#':
				pdfName.append('#');
				pdfName.append(Integer.toString((int) character, 16));
				break;
			default:
				pdfName.append(character);
			}
		}
		setContent(pdfName.toString());
	}

	/**
	 * Constructs a <CODE>PdfName</CODE>-object that doesn't throw a BadPdfFormatException.
	 * <P>
	 * This extra private constructor was needed to be able to construct final static names.
	 *
	 * @param		name		the new Name.
	 * @param		dummy		a dummy variable
	 *
	 * @since rugPdf0.20
	 */

	private PdfName(String name, int dummy) {
		super(PdfObject.NAME, name);
	}

// methods

	/**
	 * Returns the <CODE>String</CODE> value of this <CODE>PdfName</CODE>.
	 *
	 * @return		a <CODE>String</CODE>
	 * 
	 * @since		rugPdf0.20					   
	 */

	public final String toString() {
        try {
            return new String(bytes, PdfObject.ENCODING);
        }
        catch (Exception e) {
            return new String(bytes);
        }
    }

// implementation of the Comparable-method

	/**
	 * Compares the names alfabetically.
	 *
	 * @param		object	an object of the type PdfName
	 * @return		the value 0 if the object is a name equal to the name of this object,
	 *				a value less than 0 if the argument's name is greater than the name of this object,
	 *				a value greater than 0 if the argument's name is less than the name of this object
	 * @throws		a <CODE>ClassCastException</CODE> if the argument is not a PdfName
	 *
	 * @since		rugPdf0.20
	 */

	public final int compareTo(Object object)
		throws ClassCastException {
		PdfName name = (PdfName) object;
		return toString().compareTo(name.toString());
	}
}