/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002, 2003, 2004 by Mark Hall
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

package com.lowagie.text.rtf.graphic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.wmf.MetaDo;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.text.RtfParagraph;


/**
 * The RtfImage contains one image. Supported image types are jpeg, png, wmf, bmp.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Paulo Soares
 */
public class RtfImage extends RtfElement {
    
    /**
     * Constant for the shape/picture group
     */
    private static final byte[] PICTURE_GROUP = "\\*\\shppict".getBytes();
    /**
     * Constant for a picture
     */
    private static final byte[] PICTURE = "\\pict".getBytes();
    /**
     * Constant for a jpeg image
     */
    private static final byte[] PICTURE_JPEG = "\\jpegblip".getBytes();
    /**
     * Constant for a png image
     */
    private static final byte[] PICTURE_PNG = "\\pngblip".getBytes();
    /**
     * Constant for a bmp image
     */
    private static final byte[] PICTURE_BMP = "\\dibitmap0".getBytes();
    /**
     * Constant for a wmf image
     */
    private static final byte[] PICTURE_WMF = "\\wmetafile8".getBytes();
    /**
     * Constant for the picture width
     */
    private static final byte[] PICTURE_WIDTH = "\\picw".getBytes();
    /**
     * Constant for the picture height
     */
    private static final byte[] PICTURE_HEIGHT = "\\pich".getBytes();
    /**
     * Constant for the picture width scale
     */
    private static final byte[] PICTURE_SCALE_X = "\\picscalex".getBytes();
    /**
     * Constant for the picture height scale
     */
    private static final byte[] PICTURE_SCALE_Y = "\\picscaley".getBytes();
    
    /**
     * The type of image this is.
     */
    private int imageType = Image.ORIGINAL_NONE;
    /**
     * The actual image. Already formated for direct inclusion in the rtf document
     */
    private byte[] image = new byte[0];
    /**
     * The alignment of this picture
     */
    private int alignment = Element.ALIGN_LEFT;
    /**
     * The width of this picture
     */
    private float width = 0;
    /**
     * The height of this picutre
     */
    private float height = 0;
    /**
     * The intended display width of this picture
     */
    private float plainWidth = 0;
    /**
     * The intended display height of this picture
     */
    private float plainHeight = 0;
    
    /**
     * Constructs a RtfImage for an Image.
     * 
     * @param doc The RtfDocument this RtfImage belongs to
     * @param image The Image that this RtfImage wraps
     * @throws DocumentException If an error occured accessing the image content
     */
    public RtfImage(RtfDocument doc, Image image) throws DocumentException {
        super(doc);
        imageType = image.getOriginalType();
        if (!(imageType == Image.ORIGINAL_JPEG || imageType == Image.ORIGINAL_BMP
                || imageType == Image.ORIGINAL_PNG || imageType == Image.ORIGINAL_WMF)) {
            throw new DocumentException("Only BMP, PNG, WMF and JPEG images are supported by the RTF Writer");
        }
        alignment = image.alignment();
        width = image.width();
        height = image.height();
        plainWidth = image.plainWidth();
        plainHeight = image.plainHeight();
        this.image = getImage(image);
    }
    
    /**
     * Extracts the image data from the Image. The data is formated for direct inclusion
     * in a rtf document
     * 
     * @param image The Image for which to extract the content
     * @return The image data formated for the rtf document
     * @throws DocumentException If an error occurs accessing the image content
     */
    private byte[] getImage(Image image) throws DocumentException {
        ByteArrayOutputStream imageTemp = new ByteArrayOutputStream();
        try {
            InputStream imageIn;
            if (imageType == Image.ORIGINAL_BMP) {
                imageIn = new ByteArrayInputStream(MetaDo.wrapBMP(image));
            } else {
                if (image.getOriginalData() == null) {
                    imageIn = image.url().openStream();
                } else {
                    imageIn = new ByteArrayInputStream(image.getOriginalData());
                }
                if (imageType == Image.ORIGINAL_WMF) { //remove the placeable header
                    long skipLength = 22;
                	while(skipLength > 0) {
                	    skipLength = skipLength - imageIn.skip(skipLength);
                	}
                }
            }
            int buffer = 0;
            int count = 0;
            while((buffer = imageIn.read()) != -1) {
                String helperStr = Integer.toHexString(buffer);
                if (helperStr.length() < 2) helperStr = "0" + helperStr;
                imageTemp.write(helperStr.getBytes());
                count++;
                if (count == 64) {
                    imageTemp.write((byte) '\n');
                    count = 0;
                }
            }
        } catch(IOException ioe) {
            throw new DocumentException(ioe.getMessage());
        }
        return imageTemp.toByteArray();
    }
    
    /**
     * Writes the RtfImage content
     * 
     * @return the RtfImage content
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            switch(alignment) {
            	case Element.ALIGN_LEFT:
            		result.write(RtfParagraph.ALIGN_LEFT);
            		break;
            	case Element.ALIGN_RIGHT:
            		result.write(RtfParagraph.ALIGN_RIGHT);
            		break;
            	case Element.ALIGN_CENTER:
            		result.write(RtfParagraph.ALIGN_CENTER);
            		break;
            	case Element.ALIGN_JUSTIFIED:
            		result.write(RtfParagraph.ALIGN_JUSTIFY);
            		break;
            }
            result.write(OPEN_GROUP);
            result.write(PICTURE_GROUP);
            result.write(OPEN_GROUP);
            result.write(PICTURE);
            switch(imageType) {
            	case Image.ORIGINAL_JPEG:
            	    result.write(PICTURE_JPEG);
            		break;
            	case Image.ORIGINAL_PNG:
            	    result.write(PICTURE_PNG);
            		break;
            	case Image.ORIGINAL_WMF:
            	case Image.ORIGINAL_BMP:
            	    result.write(PICTURE_WMF);
            		break;
            }
            result.write(PICTURE_WIDTH);
            result.write(intToByteArray((int) (plainWidth * RtfElement.TWIPS_FACTOR)));
            result.write(PICTURE_HEIGHT);
            result.write(intToByteArray((int) (plainHeight * RtfElement.TWIPS_FACTOR)));
            if(width > 0) {
                result.write(PICTURE_SCALE_X);
                result.write(intToByteArray((int) (100 / width * plainWidth)));
            }
            if(height > 0) {
                result.write(PICTURE_SCALE_Y);
                result.write(intToByteArray((int) (100 / height * plainHeight)));
            }
            result.write(DELIMITER);
            result.write((byte) '\n');
            result.write(image);
            result.write(CLOSE_GROUP);
            result.write(CLOSE_GROUP);
            result.write((byte) '\n');
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
}
