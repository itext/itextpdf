/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf;
import com.lowagie.text.ExceptionConverter;

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
 *
 * @see		PdfObject
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 */

public class PdfName extends PdfObject implements Comparable{
    
    // static membervariables (a variety of standard names used in PDF)
    
    /** This is a static final PdfName */
    public static final PdfName A = new PdfName("A");
    
    /** This is a static final PdfName */
    public static final PdfName AA = new PdfName("AA");
    
    /** This is a static final PdfName */
    public static final PdfName AC = new PdfName("AC");
    
    /** This is a static final PdfName */
    public static final PdfName ACROFORM = new PdfName("AcroForm");
    
    /** This is a static final PdfName */
    public static final PdfName ACTION = new PdfName("Action");
    
    /** This is a static final PdfName */
    public static final PdfName ANNOT = new PdfName("Annot");
    
    /** This is a static final PdfName */
    public static final PdfName ANNOTS = new PdfName("Annots");
    
    /** This is a static final PdfName */
    public static final PdfName AP = new PdfName("AP");
    
    /** This is a static final PdfName */
    public static final PdfName AS = new PdfName("AS");
    
    /** This is a static final PdfName */
    public static final PdfName ASCII85DECODE = new PdfName("ASCII85Decode");
    
    /** This is a static final PdfName */
    public static final PdfName ASCIIHEXDECODE = new PdfName("ASCIIHexDecode");
    
    /** This is a static final PdfName */
    public static final PdfName AUTHOR = new PdfName("Author");
    
    /** This is a static final PdfName */
    public static final PdfName B = new PdfName("B");

    /** This is a static final PdfName */
    public static final PdfName BASEFONT = new PdfName("BaseFont");
    
    /** This is a static final PdfName */
    public static final PdfName BBOX = new PdfName("BBox");
    
    /** This is a static final PdfName */
    public static final PdfName BC = new PdfName("BC");
    
    /** This is a static final PdfName */
    public static final PdfName BG = new PdfName("BG");
    
    /** This is a static final PdfName */
    public static final PdfName BITSPERCOMPONENT = new PdfName("BitsPerComponent");
    
    /** This is a static final PdfName */
    public static final PdfName BL = new PdfName("Bl");
    
    /** This is a static final PdfName */
    public static final PdfName BLACKIS1 = new PdfName("BlackIs1");
    
    /** This is a static final PdfName */
    public static final PdfName BORDER = new PdfName("Border");
    
    /** This is a static final PdfName */
    public static final PdfName BS = new PdfName("BS");
    
    /** This is a static final PdfName */
    public static final PdfName BTN = new PdfName("Btn");
    
    /** This is a static final PdfName */
    public static final PdfName BYTERANGE = new PdfName("ByteRange");
    
    /** This is a static final PdfName */
    public static final PdfName C = new PdfName("C");
    
    /** This is a static final PdfName */
    public static final PdfName CA = new PdfName("CA");
    
    /** This is a static final PdfName */
    public static final PdfName CATALOG = new PdfName("Catalog");
    
    /** This is a static final PdfName */
    public static final PdfName CCITTFAXDECODE = new PdfName("CCITTFaxDecode");
    
    /** This is a static final PdfName */
    public static final PdfName CENTERWINDOW = new PdfName("CenterWindow");

    /** This is a static final PdfName */
    public static final PdfName CH = new PdfName("Ch");

    /** This is a static final PdfName */
    public static final PdfName CIRCLE = new PdfName("Circle");

    /** This is a static final PdfName */
    public static final PdfName CO = new PdfName("CO");
    
    /** This is a static final PdfName */
    public static final PdfName COLORS = new PdfName("Colors");
    
    /** This is a static final PdfName */
    public static final PdfName COLORSPACE = new PdfName("ColorSpace");
    
    /** This is a static final PdfName */
    public static final PdfName COLUMNS = new PdfName("Columns");
    
    /** This is a static final PdfName */
    public static final PdfName CONTENT = new PdfName("Content");
    
    /** This is a static final PdfName */
    public static final PdfName CONTENTS = new PdfName("Contents");
    
    /** This is a static final PdfName */
    public static final PdfName COUNT = new PdfName("Count");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName COURIER = new PdfName("Courier");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName COURIER_BOLD = new PdfName("Courier-Bold");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName COURIER_OBLIQUE = new PdfName("Courier-Oblique");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName COURIER_BOLDOBLIQUE = new PdfName("Courier-BoldOblique");
    
    /** This is a static final PdfName */
    public static final PdfName CREATIONDATE = new PdfName("CreationDate");
    
    /** This is a static final PdfName */
    public static final PdfName CREATOR = new PdfName("Creator");
    
    /** This is a static final PdfName */
    public static final PdfName CROPBOX = new PdfName("CropBox");
    
    /** This is a static final PdfName */
    public static final PdfName D = new PdfName("D");
    
    /** This is a static final PdfName */
    public static final PdfName DA = new PdfName("DA");
    
    /** This is a static final PdfName */
    public static final PdfName DCTDECODE = new PdfName("DCTDecode");
    
    /** This is a static final PdfName */
    public static final PdfName DECODE = new PdfName("Decode");
    
    /** This is a static final PdfName */
    public static final PdfName DECODEPARMS = new PdfName("DecodeParms");
    
    /** This is a static final PdfName */
    public static final PdfName DEST = new PdfName("Dest");
    
    /** This is a static final PdfName */
    public static final PdfName DESTS = new PdfName("Dests");
    
    /** This is a static final PdfName */
    public static final PdfName DEVICEGRAY = new PdfName("DeviceGray");
    
    /** This is a static final PdfName */
    public static final PdfName DEVICERGB = new PdfName("DeviceRGB");
    
    /** This is a static final PdfName */
    public static final PdfName DEVICECMYK = new PdfName("DeviceCMYK");
    
    /** This is a static final PdfName */
    public static final PdfName DIRECTION = new PdfName("Direction");
    
    /** This is a static final PdfName */
    public static final PdfName DR = new PdfName("DR");
    
    /** This is a static final PdfName */
    public static final PdfName DUR = new PdfName("Dur");

    /** This is a static final PdfName */
    public static final PdfName DV = new PdfName("DV");

    /** This is a static final PdfName */
    public static final PdfName E = new PdfName("E");
    
    /** This is a static final PdfName */
    public static final PdfName EARLYCHANGE = new PdfName("EarlyChange");
    
    /** This is a static final PdfName */
    public static final PdfName ENCODEDBYTEALIGN = new PdfName("EncodedByteAlign");
    
    /** This is a static final PdfName */
    public static final PdfName ENCODING = new PdfName("Encoding");
    
    /** This is a static final PdfName */
    public static final PdfName ENCRYPT = new PdfName("Encrypt");
    
    /** This is a static final PdfName */
    public static final PdfName ENDOFBLOCK = new PdfName("EndOfBlock");
    
    /** This is a static final PdfName */
    public static final PdfName ENDOFLINE = new PdfName("EndOfLine");
    
    /** This is a static final PdfName */
    public static final PdfName EXTGSTATE = new PdfName("ExtGState");
    
    /** This is a static final PdfName */
    public static final PdfName F = new PdfName("F");
    
    /** This is a static final PdfName */
    public static final PdfName FDECODEPARMS = new PdfName("FDecodeParms");
    
    /** This is a static final PdfName */
    public static final PdfName FF = new PdfName("Ff");
    
    /** This is a static final PdfName */
    public static final PdfName FFILTER = new PdfName("FFilter");
    
    /** This is a static final PdfName */
    public static final PdfName FIELDS = new PdfName("Fields");
    
    /** This is a static final PdfName */
    public static final PdfName FILTER = new PdfName("Filter");
    
    /** This is a static final PdfName */
    public static final PdfName FIRST = new PdfName("First");
    
    /** This is a static final PdfName */
    public static final PdfName FIRSTCHAR = new PdfName("FirstChar");
    
    /** This is a static final PdfName */
    public static final PdfName FIRSTPAGE = new PdfName("FirstPage");
    
    /** This is a static final PdfName */
    public static final PdfName FIT = new PdfName("Fit");
    
    /** This is a static final PdfName */
    public static final PdfName FITH = new PdfName("FitH");
    
    /** This is a static final PdfName */
    public static final PdfName FITV = new PdfName("FitV");
    
    /** This is a static final PdfName */
    public static final PdfName FITR = new PdfName("FitR");
    
    /** This is a static final PdfName */
    public static final PdfName FITB = new PdfName("FitB");
    
    /** This is a static final PdfName */
    public static final PdfName FITBH = new PdfName("FitBH");
    
    /** This is a static final PdfName */
    public static final PdfName FITBV = new PdfName("FitBV");
    
    /** This is a static final PdfName */
    public static final PdfName FITWINDOW = new PdfName("FitWindow");
    
    /** This is a static final PdfName */
    public static final PdfName FLAGS = new PdfName("Flags");
    
    /** This is a static final PdfName */
    public static final PdfName FLATEDECODE = new PdfName("FlateDecode");
    
    /** This is a static final PdfName */
    public static final PdfName FO = new PdfName("Fo");
    
    /** This is a static final PdfName */
    public static final PdfName FONT = new PdfName("Font");
    
    /** This is a static final PdfName */
    public static final PdfName FONTDESCRIPTOR = new PdfName("FontDescriptor");
    
    /** This is a static final PdfName */
    public static final PdfName FORM = new PdfName("Form");
    
    /** This is a static final PdfName */
    public static final PdfName FORMTYPE = new PdfName("FormType");
    
    /** This is a static final PdfName */
    public static final PdfName FREETEXT = new PdfName("FreeText");
    
    /** This is a static final PdfName */
    public static final PdfName FT = new PdfName("FT");
    
    /** This is a static final PdfName */
    public static final PdfName FULLSCREEN = new PdfName("FullScreen");
    
    /** This is a static final PdfName */
    public static final PdfName FUNCTIONTYPE = new PdfName("FunctionType");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName GOTO = new PdfName("GoTo");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName GOTOR = new PdfName("GoToR");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName H = new PdfName("H");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName HEIGHT = new PdfName("Height");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName HELVETICA = new PdfName("Helvetica");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName HELVETICA_BOLD = new PdfName("Helvetica-Bold");
    
    /** This is a static PdfName PdfName of a base 14 type 1 font */
    public static final PdfName HELVETICA_OBLIQUE = new PdfName("Helvetica-Oblique");
    
    /** This is a static PdfName PdfName of a base 14 type 1 font */
    public static final PdfName HELVETICA_BOLDOBLIQUE = new PdfName("Helvetica-BoldOblique");
    
    /** This is a static final PdfName */
    public static final PdfName HID = new PdfName("Hid");
    
    /** This is a static final PdfName */
    public static final PdfName HIDE = new PdfName("Hide");
    
    /** This is a static final PdfName */
    public static final PdfName HIDEMENUBAR = new PdfName("HideMenubar");
    
    /** This is a static final PdfName */
    public static final PdfName HIDETOOLBAR = new PdfName("HideToolbar");
    
    /** This is a static final PdfName */
    public static final PdfName HIDEWINDOWUI = new PdfName("HideWindowUI");
    
    /** This is a static final PdfName */
    public static final PdfName HIGHLIGHT = new PdfName("Highlight");
    
    /** This is a static final PdfName */
    public static final PdfName I = new PdfName("I");
    
    /** This is a static final PdfName */
    public static final PdfName ID = new PdfName("ID");
    
    /** This is a static final PdfName */
    public static final PdfName IF = new PdfName("IF");
    
    /** This is a static final PdfName */
    public static final PdfName IMAGE = new PdfName("Image");
    
    /** This is a static final PdfName */
    public static final PdfName IMAGEB = new PdfName("ImageB");
    
    /** This is a static final PdfName */
    public static final PdfName IMAGEC = new PdfName("ImageC");
    
    /** This is a static final PdfName */
    public static final PdfName IMAGEI = new PdfName("ImageI");
    
    /** This is a static final PdfName */
    public static final PdfName IMAGEMASK = new PdfName("ImageMask");
    
    /** This is a static final PdfName */
    public static final PdfName INDEXED = new PdfName("Indexed");
    
    /** This is a static final PdfName */
    public static final PdfName INFO = new PdfName("Info");
    
    /** This is a static final PdfName */
    public static final PdfName INKLIST = new PdfName("InkList");
    
    /** This is a static final PdfName */
    public static final PdfName IMPORTDATA = new PdfName("ImportData");
    
    /** This is a static final PdfName */
    public static final PdfName INTERPOLATE = new PdfName("Interpolate");
    
    /** This is a static final PdfName */
    public static final PdfName ISMAP = new PdfName("IsMap");
    
    /** This is a static final PdfName */
    public static final PdfName IX = new PdfName("IX");
    
    /** This is a static final PdfName */
    public static final PdfName JAVASCRIPT = new PdfName("JavaScript");
    
    /** This is a static final PdfName */
    public static final PdfName JS = new PdfName("JS");
    
    /** This is a static final PdfName */
    public static final PdfName K = new PdfName("K");
    
    /** This is a static final PdfName */
    public static final PdfName KEYWORDS = new PdfName("Keywords");
    
    /** This is a static final PdfName */
    public static final PdfName KIDS = new PdfName("Kids");
    
    /** This is a static final PdfName */
    public static final PdfName L = new PdfName("L");
    
    /** This is a static final PdfName */
    public static final PdfName L2R = new PdfName("L2R");
    
    /** This is a static final PdfName */
    public static final PdfName LAST = new PdfName("Last");
    
    /** This is a static final PdfName */
    public static final PdfName LASTCHAR = new PdfName("LastChar");
    
    /** This is a static final PdfName */
    public static final PdfName LASTPAGE = new PdfName("LastPage");
    
    /** This is a static final PdfName */
    public static final PdfName LAUNCH = new PdfName("Launch");
    
    /** This is a static final PdfName */
    public static final PdfName LENGTH = new PdfName("Length");
    
    /** This is a static final PdfName */
    public static final PdfName LIMITS = new PdfName("Limits");
    
    /** This is a static final PdfName */
    public static final PdfName LINE = new PdfName("Line");
    
    /** This is a static final PdfName */
    public static final PdfName LINK = new PdfName("Link");
    
    /** This is a static final PdfName */
    public static final PdfName LOCATION = new PdfName("Location");
    
    /** This is a static final PdfName */
    public static final PdfName LZWDECODE = new PdfName("LZWDecode");
    
    /** This is a static final PdfName */
    public static final PdfName M = new PdfName("M");
    
    /** This is a static final PdfName */
    public static final PdfName MATRIX = new PdfName("Matrix");
    
    /** This is a static final PdfName of an encoding */
    public static final PdfName MAC_EXPERT_ENCODING = new PdfName("MacExpertEncoding");
    
    /** This is a static final PdfName of an encoding */
    public static final PdfName MAC_ROMAN_ENCODING = new PdfName("MacRomanEncoding");
    
    /** This is a static final PdfName of an encoding */
    public static final PdfName MASK = new PdfName("Mask");

    /** This is a static final PdfName of an encoding */
    public static final PdfName MAXLEN = new PdfName("MaxLen");

    /** This is a static final PdfName of an encoding */
    public static final PdfName MK = new PdfName("MK");

    /** This is a static final PdfName */
    public static final PdfName MEDIABOX = new PdfName("MediaBox");

    /** This is a static final PdfName */
    public static final PdfName MODDATE = new PdfName("ModDate");
    
    /** This is a static final PdfName */
    public static final PdfName N = new PdfName("N");
    
    /** This is a static final PdfName */
    public static final PdfName NAME = new PdfName("Name");
    
    /** This is a static final PdfName */
    public static final PdfName NAMED = new PdfName("Named");
    
    /** This is a static final PdfName */
    public static final PdfName NAMES = new PdfName("Names");
    
    /** This is a static final PdfName */
    public static final PdfName NEXT = new PdfName("Next");
    
    /** This is a static final PdfName */
    public static final PdfName NEXTPAGE = new PdfName("NextPage");
    
    /** This is a static final PdfName */
    public static final PdfName NONFULLSCREENPAGEMODE = new PdfName("NonFullScreenPageMode");
    
    /** This is a static final PdfName */
    public static final PdfName O = new PdfName("O");
    
    /** This is a static final PdfName */
    public static final PdfName ONECOLUMN = new PdfName("OneColumn");
    
    /** This is a static final PdfName */
    public static final PdfName OPEN = new PdfName("Open");
    
    /** This is a static final PdfName */
    public static final PdfName OPENACTION = new PdfName("OpenAction");
    
    /** This is a static final PdfName */
    public static final PdfName OPT = new PdfName("Opt");
    
    /** This is a static final PdfName */
    public static final PdfName ORDERING = new PdfName("Ordering");
    
    /** This is a static final PdfName */
    public static final PdfName OUTLINES = new PdfName("Outlines");
    
    /** This is a static final PdfName */
    public static final PdfName P = new PdfName("P");
    
    /** This is a static final PdfName */
    public static final PdfName PAGE = new PdfName("Page");
    
    /** This is a static final PdfName */
    public static final PdfName PAGELABELS = new PdfName("PageLabels");
    
    /** This is a static final PdfName */
    public static final PdfName PAGELAYOUT = new PdfName("PageLayout");
    
    /** This is a static final PdfName */
    public static final PdfName PAGEMODE = new PdfName("PageMode");
    
    /** This is a static final PdfName */
    public static final PdfName PAGES = new PdfName("Pages");
    
    /** This is a static final PdfName */
    public static final PdfName PANOSE = new PdfName("Panose");
    
    /** This is a static final PdfName */
    public static final PdfName PARENT = new PdfName("Parent");
    
    /** This is a static final PdfName */
    public static final PdfName PATTERN = new PdfName("Pattern");
    
    /** This is a static final PdfName */
    public static final PdfName PDF = new PdfName("PDF");
    
    /** This is a static final PdfName */
    public static final PdfName POPUP = new PdfName("Popup");
    
    /** This is a static final PdfName */
    public static final PdfName PREDICTOR = new PdfName("Predictor");
    
    /** This is a static final PdfName */
    public static final PdfName PREV = new PdfName("Prev");
    
    /** This is a static final PdfName */
    public static final PdfName PREVPAGE = new PdfName("PrevPage");
    
    /** This is a static final PdfName */
    public static final PdfName PROCSET = new PdfName("ProcSet");
    
    /** This is a static final PdfName */
    public static final PdfName PRODUCER = new PdfName("Producer");
    
    /** This is a static final PdfName */
    public static final PdfName PROPERTIES = new PdfName("Properties");
    
    /** This is a static final PdfName */
    public static final PdfName Q = new PdfName("Q");
    
    /** This is a static final PdfName */
    public static final PdfName QUADPOINTS = new PdfName("QuadPoints");
    
    /** This is a static final PdfName */
    public static final PdfName R = new PdfName("R");
    
    /** This is a static final PdfName */
    public static final PdfName R2L = new PdfName("R2L");
    
    /** This is a static final PdfName */
    public static final PdfName RC = new PdfName("RC");
    
    /** This is a static final PdfName */
    public static final PdfName REASON = new PdfName("Reason");
    
    /** This is a static final PdfName */
    public static final PdfName RECT = new PdfName("Rect");
    
    /** This is a static final PdfName */
    public static final PdfName REGISTRY = new PdfName("Registry");
    
    /** This is a static final PdfName */
    public static final PdfName RESETFORM = new PdfName("ResetForm");
    
    /** This is a static final PdfName */
    public static final PdfName RESOURCES = new PdfName("Resources");
    
    /** This is a static final PdfName */
    public static final PdfName RI = new PdfName("RI");
    
    /** This is a static final PdfName */
    public static final PdfName ROOT = new PdfName("Root");
    
    /** This is a static final PdfName */
    public static final PdfName ROTATE = new PdfName("Rotate");
    
    /** This is a static final PdfName */
    public static final PdfName ROWS = new PdfName("Rows");
    
    /** This is a static final PdfName */
    public static final PdfName RUNLENGTHDECODE = new PdfName("RunLengthDecode");
    
    /** This is a static final PdfName */
    public static final PdfName S = new PdfName("S");
    
    /** This is a static final PdfName */
    public static final PdfName SEPARATION = new PdfName("Separation");

    /** This is a static final PdfName */
    public static final PdfName SIG = new PdfName("Sig");

    /** This is a static final PdfName */
    public static final PdfName SIGFLAGS = new PdfName("SigFlags");

    /** This is a static final PdfName */
    public static final PdfName SINGLEPAGE = new PdfName("SinglePage");
    
    /** This is a static final PdfName */
    public static final PdfName SIZE = new PdfName("Size");
    
    /** This is a static final PdfName */
    public static final PdfName SQUARE = new PdfName("Square");
    
    /** This is a static final PdfName */
    public static final PdfName STAMP = new PdfName("Stamp");
    
    /** This is a static final PdfName */
    public static final PdfName STANDARD = new PdfName("Standard");
    
    /** This is a static final PdfName */
    public static final PdfName STRIKEOUT = new PdfName("StrikeOut");
    
    /** This is a static final PdfName */
    public static final PdfName SUBFILTER = new PdfName("SubFilter");
    
    /** This is a static final PdfName */
    public static final PdfName SUBJECT = new PdfName("Subject");
    
    /** This is a static final PdfName */
    public static final PdfName SUBMITFORM = new PdfName("SubmitForm");
    
    /** This is a static final PdfName */
    public static final PdfName SUBTYPE = new PdfName("Subtype");
    
    /** This is a static final PdfName */
    public static final PdfName SUPPLEMENT = new PdfName("Supplement");
    
    /** This is a static final PdfName */
    public static final PdfName SW = new PdfName("SW");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName SYMBOL = new PdfName("Symbol");
    
    /** This is a static final PdfName */
    public static final PdfName T = new PdfName("T");
    
    /** This is a static final PdfName */
    public static final PdfName TEXT = new PdfName("Text");
    
    /** This is a static final PdfName */
    public static final PdfName THUMB = new PdfName("Thumb");
    
    /** This is a static final PdfName */
    public static final PdfName THREADS = new PdfName("Threads");
    
    /** This is a static final PdfName */
    public static final PdfName TI = new PdfName("TI");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName TIMES_ROMAN = new PdfName("Times-Roman");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName TIMES_BOLD = new PdfName("Times-Bold");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName TIMES_ITALIC = new PdfName("Times-Italic");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName TIMES_BOLDITALIC = new PdfName("Times-BoldItalic");
    
    /** This is a static final PdfName */
    public static final PdfName TITLE = new PdfName("Title");
    
    /** This is a static final PdfName */
    public static final PdfName TM = new PdfName("TM");
    
    /** This is a static final PdfName */
    public static final PdfName TP = new PdfName("TP");
    
    /** This is a static final PdfName */
    public static final PdfName TRANS = new PdfName("Trans");
    
    /** This is a static final PdfName */
    public static final PdfName TU = new PdfName("TU");
    
    /** This is a static final PdfName */
    public static final PdfName TWOCOLUMNLEFT = new PdfName("TwoColumnLeft");
    
    /** This is a static final PdfName */
    public static final PdfName TWOCOLUMNRIGHT = new PdfName("TwoColumnRight");
    
    /** This is a static final PdfName */
    public static final PdfName TX = new PdfName("Tx");
    
    /** This is a static final PdfName */
    public static final PdfName TYPE = new PdfName("Type");
    
    /** This is a static final PdfName */
    public static final PdfName TYPE1 = new PdfName("Type1");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName U = new PdfName("U");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName UNDERLINE = new PdfName("Underline");
    
    /** This is a static final PdfName */
    public static final PdfName URI = new PdfName("URI");
    
    /** This is a static final PdfName */
    public static final PdfName USENONE = new PdfName("UseNone");
    
    /** This is a static final PdfName */
    public static final PdfName USEOUTLINES = new PdfName("UseOutlines");
    
    /** This is a static final PdfName */
    public static final PdfName USETHUMBS = new PdfName("UseThumbs");
    
    /** This is a static final PdfName */
    public static final PdfName V = new PdfName("V");
    
    /** This is a static final PdfName */
    public static final PdfName VIEWERPREFERENCES = new PdfName("ViewerPreferences");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName W = new PdfName("W");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName WIDGET = new PdfName("Widget");
    
    /** This is a static final PdfName of an attribute. */
    public static final PdfName WIDTH = new PdfName("Width");
    
    /** This is a static final PdfName */
    public static final PdfName WIDTHS = new PdfName("Widths");
    
    /** This is a static final PdfName of an encoding */
    public static final PdfName WIN = new PdfName("Win");
    
    /** This is a static final PdfName of an encoding */
    public static final PdfName WIN_ANSI_ENCODING = new PdfName("WinAnsiEncoding");
    
    /** This is a static final PdfName */
    public static final PdfName X = new PdfName("X");
    
    /** This is a static final PdfName */
    public static final PdfName XOBJECT = new PdfName("XObject");
    
    /** This is a static final PdfName */
    public static final PdfName XYZ = new PdfName("XYZ");
    
    /** This is a static final PdfName of a base 14 type 1 font */
    public static final PdfName ZAPFDINGBATS = new PdfName("ZapfDingbats");
    
    private int hash = 0;

    // constructors
    
    /**
     * Constructs a <CODE>PdfName</CODE>-object.
     *
     * @param		name		the new Name.
     */
    
    PdfName(String name) {
        super(PdfObject.NAME, name);
        // The minimum number of characters in a name is 1, the maximum is 127 (the '/' not included)
        if (bytes.length < 1 || bytes.length > 127) {
            throw new IllegalArgumentException("The name is too long (" + bytes.length + " characters).");
        }
        // The name has to be checked for illegal characters
        int length = name.length();
        for (int i = 0; i < length; i++) {
            if (name.charAt(i) < 32 || name.charAt(i) > 255) {
                throw new IllegalArgumentException("Illegal character on position " + i + ".");
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
                    break;
            }
        }
        setContent(pdfName.toString());
    }
    
    // methods
    
    /**
     * Compares the names alfabetically.
     *
     * @param		object	an object of the type PdfName
     * @return		the value 0 if the object is a name equal to the name of this object,
     *				a value less than 0 if the argument's name is greater than the name of this object,
     *				a value greater than 0 if the argument's name is less than the name of this object
     * @throws		a <CODE>ClassCastException</CODE> if the argument is not a PdfName
     *
     */
    
    public final int compareTo(Object object) {
        PdfName name = (PdfName) object;
        
        byte myBytes[] = bytes;
        byte objBytes[] = name.bytes;
        int len = Math.min(myBytes.length, objBytes.length);
        for(int i=0; i<len; i++) {
            if(myBytes[i] > objBytes[i])
                return 1;
            
            if(myBytes[i] < objBytes[i])
                return -1;
        }
        if (myBytes.length < objBytes.length)
            return -1;
        if (myBytes.length > objBytes.length)
            return 1;
        return 0;
    }
    
    public final boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }
    
    public final int hashCode() {
        int h = hash;
        if (h == 0) {
            int ptr = 0;
            int len = bytes.length;
            
            for (int i = 0; i < len; i++)
                h = 31*h + (bytes[ptr++] & 0xff);
            hash = h;
        }
        return h;
    }
    
    /**
     * Returns the <CODE>String</CODE> value of this <CODE>PdfName</CODE>.
     *
     * @return		a <CODE>String</CODE>
     */
    
    public final String toString() {
        try {
            return new String(bytes, PdfObject.ENCODING);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
