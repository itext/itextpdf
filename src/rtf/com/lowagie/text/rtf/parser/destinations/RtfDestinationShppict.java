/*
 * $Id$
 * $Name$
 *
 * Copyright 2007 by Howard Shank (hgshank@yahoo.com)
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
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
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
 
package com.lowagie.text.rtf.parser.destinations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.ByteBuffer;
import com.lowagie.text.rtf.direct.RtfDirectContent;
import com.lowagie.text.rtf.parser.RtfImportMgr;
import com.lowagie.text.rtf.parser.RtfParser;
import com.lowagie.text.rtf.parser.ctrlwords.RtfCtrlWordData;

/**
 * <code>RtfDestinationShppict</code> handles data destined for picture destinations
 * 
 * @author Howard Shank (hgshank@yahoo.com)
 * @since 2.0.8
 */
public class RtfDestinationShppict extends RtfDestination {
	private ByteBuffer data = null;

	private StringBuffer hexChars = new StringBuffer(0);
	private StringBuffer buffer = new StringBuffer();

	/* picttype */
	private int pictureType = Image.ORIGINAL_NONE;
//	public static final int ORIGINAL_NONE = 0;
//	public static final int ORIGINAL_GIF = 3;
//	public static final int ORIGINAL_TIFF = 5;
//  public static final int ORIGINAL_PS = 7;

	// emfblip - EMF (nhanced metafile) - NOT HANDLED
	// pngblip int ORIGINAL_PNG = 2;
	// jpegblip Image.ORIGINAL_JPEG = 1; ORIGINAL_JPEG2000 = 8;

	// shppict - Destination
	// nonshpict - Destination - SKIP THIS
	// macpict - Mac QuickDraw- NOT HANDLED
	// pmmetafileN - OS/2 Metafile - NOT HANDLED
		// N * Meaning
		// 0x0004 PU_ARBITRARY
		// 0x0008 PU_PELS
		// 0x000C PU_LOMETRIC
		// 0x0010 PU_HIMETRIC
		// 0x0014 PU_LOENGLISH
		// 0x0018 PU_HIENGLISH
		// 0x001C PU_TWIPS
	private int pmmetafile = 0;
	// wmetafileN Image.RIGINAL_WMF = 6;
	// N * Type
	// 1 = MM_TEXT
	// 2 = M_LOMETRIC
	// 3 = MM_HIMETRIC
	// 4 = MM_LOENGLISH
	// 5 = MM_HIENGLISH
	// 6 = MM_TWIPS
	// 7 = MM_ISOTROPIC
	// 8 = MM_ANISOTROPIC
	// dibitmapN - DIB - Convert to BMP?
	// wbitmapN Image.ORIGINAL_BMP = 4;
	
	/* bitapinfo */
	// wbmbitspixelN - number of bits per pixel - 1 monochrome, 4 16 color, 8 256 color, 24 RGB - Default 1
	private Integer bitsPerPixel = new Integer(1);
	// wbmplanesN - number of color planes - must be 1
	private Integer planes = new Integer(1);
	// wbmwidthbytesN - number of bytes in each raster line
	private Integer widthBytes = null;
	
	
	
	/* pictsize */
	// picwN Ext field if the picture is a Windows metafile; picture width in pixels if the picture is a bitmap or
	// from quickdraw
	private Long width = null;
	// pichN
	private Long height = null;
	// picwgoalN
	private Long desiredWidth = null;
	// picgoalN
	private Long desiredHeight = null;
	// picscalexN
	private Integer scaleX = new Integer(100);
	// picscaleyN
	private Integer scaleY = new Integer(100);
	// picscaled - macpict setting
	private Boolean scaled = null;
	// picprop
	private Boolean inlinePicture = new Boolean(false);
	// defshp
	private Boolean wordArt = new Boolean(false);
	// piccroptN
	private Integer cropTop = new Integer(0);
	// piccropbN
	private Integer cropBottom = new Integer(0);
	// piccroplN
	private Integer cropLeft = new Integer(0);
	// piccroprN
	private Integer cropRight = new Integer(0);
	
	/* metafileinfo */
	// picbmp
	private boolean bitmap = false;
	//picbppN - Valid 1,4,8,24
	private int bbp = 1;
	
	/* data */
	// binN
	// 0 = HEX, 1 = BINARY
	public static final int FORMAT_HEXADECIMAL = 0;
	public static final int FORMAT_BINARY = 1;
	private int dataFormat = FORMAT_HEXADECIMAL;
	private long binaryLength = 0;
	// blipupiN
	private Integer unitsPerInch = null;
	// bliptagN
	private String tag = "";
	private static final int NORMAL = 0;
	private static final int BLIPUID = 1;
	
	private int state = NORMAL;
    /**
     * Constant for converting pixels to twips
     */
    private static final int PIXEL_TWIPS_FACTOR = 15;
    
    
	public RtfDestinationShppict() {
		super(null);
	}

	/**
	 * Constructs a new RtfDestinationShppict.
	 */
	public RtfDestinationShppict(RtfParser parser) {
		super(parser);
	}
	
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestination#closeDestination()
	 */
	public boolean closeDestination() {
		if(this.rtfParser.isImport()) {
			if(this.buffer.length()>0) {
				writeBuffer();
			}
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestination#handleGroupEnd()
	 */
	public boolean handleCloseGroup() {
		this.onCloseGroup();	// event handler
		
		if(this.rtfParser.isImport()) {
			if(this.buffer.length()>0) {
				writeBuffer();
			}
			this.writeText("}");
			return true;
		}
		if(this.rtfParser.isConvert()) {
		}

		//		switch(dataFormat) {
//		case HEXADECIMAL:
//			data1 = new byte[data.()/2];
//			StringBuffer hexChars = new StringBuffer("00");
//			for(int idx = 0; idx< data.length()/2; idx++) {
//            	try {
//					hexChars.setCharAt(0, data.[2*idx[]);
//					hexChars.setCharAt(1, data.charAt(2*idx+1));
//					data1[idx]=(byte)Integer.parseInt(hexChars.toString() , 16);
//				} catch (NumberFormatException e) {
//					e.printStackTrace();
//				}
//			}
//
//			break;
//		case BINARY:
//			data1 = data.getBuffer();
//			break;
//		}
		Image img = null;
		try {
				img = Image.getInstance(data.getBuffer());
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(img != null) {
				FileOutputStream out =null;
				try {
					out = new FileOutputStream("c:\\test.png");
					out.write(img.getOriginalData());
					out.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				img.scaleAbsolute(this.desiredWidth.floatValue()/PIXEL_TWIPS_FACTOR, this.desiredHeight.floatValue()/PIXEL_TWIPS_FACTOR);
				//img.scaleAbsolute(this.width.floatValue()/PIXEL_TWIPS_FACTOR, this.height.floatValue()/PIXEL_TWIPS_FACTOR);
				//img.scalePercent(this.scaleX.floatValue(), this.scaleY.floatValue());
				
				try {
					this.rtfParser.getDocument().add(img);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			data = new ByteBuffer();
			dataFormat = FORMAT_HEXADECIMAL;
			
			return true;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestination#handleGroupStart()
	 */
	public boolean handleOpenGroup() {
		this.onOpenGroup();	// event handler
		
		if(this.rtfParser.isImport()) {
		}
		if(this.rtfParser.isConvert()) {
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.parser.destinations.RtfDestination#handleOpenNewGroup()
	 */
	public boolean handleOpeningSubGroup() {
		if(this.rtfParser.isImport()) {
			if(this.buffer.length()>0) {
				writeBuffer();
			}
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestination#handleCharacter(char[])
	 */
	public boolean handleCharacter(char[] ch) {
		
		if(this.rtfParser.isImport()) {
			if(buffer.length() > 254)
				writeBuffer();
		}
		if(data == null) data = new ByteBuffer();
		switch(dataFormat) {
		case FORMAT_HEXADECIMAL:
			hexChars.append(ch);
			if(hexChars.length() == 2) {
				try {
					data.append((char)Integer.parseInt(hexChars.toString() , 16));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				hexChars = new StringBuffer();
			}
			break;
		case FORMAT_BINARY:
			data.append(ch[0]);
			break;
		}
		
		return true;
	}
	public boolean handleControlWord(RtfCtrlWordData ctrlWordData) {
		boolean result = false;
		boolean skipCtrlWord = false;
		if(this.rtfParser.isImport()) {
			if(ctrlWordData.ctrlWord.equals("shppict")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("nonshppict")) { skipCtrlWord = true; this.rtfParser.setTokeniserStateSkipGroup(); result = true;}
			if(ctrlWordData.ctrlWord.equals("blipuid")) { skipCtrlWord = true; this.rtfParser.setTokeniserStateSkipGroup(); result = true;}
			if(ctrlWordData.ctrlWord.equals("picprop")) { skipCtrlWord = true; this.rtfParser.setTokeniserStateSkipGroup(); result = true;}
			if(ctrlWordData.ctrlWord.equals("pict")) { result = true;}
		}
		if(this.rtfParser.isConvert()) {
			if(ctrlWordData.ctrlWord.equals("shppict")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("nonshppict")) { skipCtrlWord = true; this.rtfParser.setTokeniserStateSkipGroup(); result = true;}
			if(ctrlWordData.ctrlWord.equals("blipuid")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("pict")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("emfblip")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("pngblip")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("jepgblip")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("macpict")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("pmmetafile")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("wmetafile")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("dibitmap")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("wbitmap")) { result = true;}
			/* bitmap information */
			if(ctrlWordData.ctrlWord.equals("wbmbitspixel")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("wbmplanes")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("wbmwidthbytes")) { result = true;}
			/* picture size, scaling and cropping */
			if(ctrlWordData.ctrlWord.equals("picw")) { this.width = ctrlWordData.toLong(); result = true;}
			if(ctrlWordData.ctrlWord.equals("pich")) { this.height = ctrlWordData.toLong(); result = true;}
			if(ctrlWordData.ctrlWord.equals("picwgoal")) { this.desiredWidth = ctrlWordData.toLong(); result = true;}
			if(ctrlWordData.ctrlWord.equals("pichgoal")) { this.desiredHeight = ctrlWordData.toLong(); result = true;}
			if(ctrlWordData.ctrlWord.equals("picscalex")) { this.scaleX = ctrlWordData.toInteger(); result = true;}
			if(ctrlWordData.ctrlWord.equals("picscaley")) { this.scaleY = ctrlWordData.toInteger();result = true;}
			if(ctrlWordData.ctrlWord.equals("picscaled")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("picprop")) { skipCtrlWord = true; this.rtfParser.setTokeniserStateSkipGroup(); result = true;}
			if(ctrlWordData.ctrlWord.equals("defshp")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("piccropt")) { this.cropTop = ctrlWordData.toInteger(); result = true;}
			if(ctrlWordData.ctrlWord.equals("piccropb")) { this.cropBottom = ctrlWordData.toInteger(); result = true;}
			if(ctrlWordData.ctrlWord.equals("piccropl")) { this.cropLeft = ctrlWordData.toInteger(); result = true;}
			if(ctrlWordData.ctrlWord.equals("piccropr")) { this.cropRight = ctrlWordData.toInteger(); result = true;}
			/* metafile information */
			if(ctrlWordData.ctrlWord.equals("picbmp")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("picbpp")) { result = true;}
			/* picture data */
			if(ctrlWordData.ctrlWord.equals("bin")) { dataFormat = FORMAT_BINARY; result = true;}
			if(ctrlWordData.ctrlWord.equals("blipupi")) { result = true;}
			if(ctrlWordData.ctrlWord.equals("blipuid")) { skipCtrlWord = true; this.rtfParser.setTokeniserStateSkipGroup(); result = true;}
			if(ctrlWordData.ctrlWord.equals("bliptag")) { result = true;}
		
		}
		if(!skipCtrlWord) {
		switch(this.rtfParser.getConversionType()) {
		case RtfParser.TYPE_IMPORT_FULL:
				writeBuffer();
				writeText(ctrlWordData.toString());
			result = true;
			break;		
		case RtfParser.TYPE_IMPORT_FRAGMENT:
				writeBuffer();
				writeText(ctrlWordData.toString());
			result = true;
			break;
		case RtfParser.TYPE_CONVERT:
			result = true;
			break;
		default:	// error because is should be an import or convert
			result = false;
			break;
		}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestination#setDefaults()
	 */
	public void setToDefaults() {
		
		this.buffer = new StringBuffer();
		this.data = null;
		this.width = null;
		this.height = null;
		this.desiredWidth = null;
		this.desiredHeight = null;
		this.scaleX = new Integer(100);
		this.scaleY = new Integer(100);
		this.scaled = null;
		this.inlinePicture = new Boolean(false);
		this.wordArt = new Boolean(false);
		this.cropTop = new Integer(0);
		this.cropBottom = new Integer(0);
		this.cropLeft = new Integer(0);
		this.cropRight = new Integer(0);
		this.bitmap = false;
		this.bbp = 1;
		this.dataFormat = FORMAT_HEXADECIMAL;
		this.binaryLength = 0;
		this.unitsPerInch = null;
		this.tag = "";
	}

	private void writeBuffer() {
		writeText(this.buffer.toString());
		setToDefaults();
	}
	private void writeText(String value) {
		if(this.rtfParser.getState().newGroup) {
			this.rtfParser.getRtfDocument().add(new RtfDirectContent("{"));
			this.rtfParser.getState().newGroup = false;
		}
		if(value.length() > 0) {
			this.rtfParser.getRtfDocument().add(new RtfDirectContent(value));
		}
	}

}
