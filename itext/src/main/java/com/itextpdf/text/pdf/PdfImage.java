/*
 * $Id: PdfImage.java 6293 2014-02-25 15:24:31Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.Image;

/**
 * <CODE>PdfImage</CODE> is a <CODE>PdfStream</CODE> containing an image-<CODE>Dictionary</CODE> and -stream.
 */

public class PdfImage extends PdfStream {
    
    static final int TRANSFERSIZE = 4096;
    // membervariables
    
    /** This is the <CODE>PdfName</CODE> of the image. */
    protected PdfName name = null;

    protected Image image = null;
    
    // constructor
    
    /**
     * Constructs a <CODE>PdfImage</CODE>-object.
     *
     * @param image the <CODE>Image</CODE>-object
     * @param name the <CODE>PdfName</CODE> for this image
     * @throws BadPdfFormatException on error
     */
    
    public PdfImage(Image image, String name, PdfIndirectReference maskRef) throws BadPdfFormatException {
        super();
        this.image = image;
        if (name == null) 
        	generateImgResName( image );
        else
        	this.name = new PdfName(name);
        put(PdfName.TYPE, PdfName.XOBJECT);
        put(PdfName.SUBTYPE, PdfName.IMAGE);
        put(PdfName.WIDTH, new PdfNumber(image.getWidth()));
        put(PdfName.HEIGHT, new PdfNumber(image.getHeight()));
        if (image.getLayer() != null)
            put(PdfName.OC, image.getLayer().getRef());
        if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 0xff))
            put(PdfName.IMAGEMASK, PdfBoolean.PDFTRUE);
        if (maskRef != null) {
            if (image.isSmask())
                put(PdfName.SMASK, maskRef);
            else
                put(PdfName.MASK, maskRef);
        }
        if (image.isMask() && image.isInverted())
            put(PdfName.DECODE, new PdfLiteral("[1 0]"));
        if (image.isInterpolation())
            put(PdfName.INTERPOLATE, PdfBoolean.PDFTRUE);
        InputStream is = null;
        try {
        	// deal with transparency
            int transparency[] = image.getTransparency();
            if (transparency != null && !image.isMask() && maskRef == null) {
                StringBuilder s = new StringBuilder("[");
                for (int k = 0; k < transparency.length; ++k)
                    s.append(transparency[k]).append(" ");
                s.append("]");
                put(PdfName.MASK, new PdfLiteral(s.toString()));
            }
            // Raw Image data
            if (image.isImgRaw()) {
                // will also have the CCITT parameters
                int colorspace = image.getColorspace();
                bytes = image.getRawData();
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                int bpc = image.getBpc();
                if (bpc > 0xff) {
                    if (!image.isMask())
                        put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    put(PdfName.FILTER, PdfName.CCITTFAXDECODE);
                    int k = bpc - Image.CCITTG3_1D;
                    PdfDictionary decodeparms = new PdfDictionary();
                    if (k != 0)
                        decodeparms.put(PdfName.K, new PdfNumber(k));
                    if ((colorspace & Image.CCITT_BLACKIS1) != 0)
                        decodeparms.put(PdfName.BLACKIS1, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENCODEDBYTEALIGN) != 0)
                        decodeparms.put(PdfName.ENCODEDBYTEALIGN, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENDOFLINE) != 0)
                        decodeparms.put(PdfName.ENDOFLINE, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENDOFBLOCK) != 0)
                        decodeparms.put(PdfName.ENDOFBLOCK, PdfBoolean.PDFFALSE);
                    decodeparms.put(PdfName.COLUMNS, new PdfNumber(image.getWidth()));
                    decodeparms.put(PdfName.ROWS, new PdfNumber(image.getHeight()));
                    put(PdfName.DECODEPARMS, decodeparms);
                }
                else {
                    switch(colorspace) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            if (image.isInverted())
                                put(PdfName.DECODE, new PdfLiteral("[1 0]"));
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            if (image.isInverted())
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0]"));
                            break;
                        case 4:
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted())
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                    }
                    PdfDictionary additional = image.getAdditional();
                    if (additional != null)
                        putAll(additional);
                    if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 8))
                        remove(PdfName.COLORSPACE);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.getBpc()));
                    if (image.isDeflated())
                        put(PdfName.FILTER, PdfName.FLATEDECODE);
                    else {
                        flateCompress(image.getCompressionLevel());
                    }
                }
                return;
            }
            // GIF, JPEG or PNG
            String errorID;
            if (image.getRawData() == null){
                is = image.getUrl().openStream();
                errorID = image.getUrl().toString();
            }
            else{
                is = new java.io.ByteArrayInputStream(image.getRawData());
                errorID = "Byte array";
            }
            switch(image.type()) {
                case Image.JPEG:
                    put(PdfName.FILTER, PdfName.DCTDECODE);
                    if (image.getColorTransform() == 0) {
                        PdfDictionary decodeparms = new PdfDictionary();
                        decodeparms.put(PdfName.COLORTRANSFORM, new PdfNumber(0));
                        put(PdfName.DECODEPARMS, decodeparms);
                    }
                    switch(image.getColorspace()) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            break;
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                            }
                    }
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    if (image.getRawData() != null){
                        bytes = image.getRawData();
                        put(PdfName.LENGTH, new PdfNumber(bytes.length));
                        return;
                    }
                    streamBytes = new ByteArrayOutputStream();
                    transferBytes(is, streamBytes, -1);
                    break;
                case Image.JPEG2000:
                    put(PdfName.FILTER, PdfName.JPXDECODE);
                    if (image.getColorspace() > 0) {
                        switch(image.getColorspace()) {
                            case 1:
                                put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                                break;
                            case 3:
                                put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                                break;
                            default:
                                put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                        }
                        put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.getBpc()));
                    }
                    if (image.getRawData() != null){
                        bytes = image.getRawData();
                        put(PdfName.LENGTH, new PdfNumber(bytes.length));
                        return;
                    }
                    streamBytes = new ByteArrayOutputStream();
                    transferBytes(is, streamBytes, -1);
                    break;
                case Image.JBIG2:
                    put(PdfName.FILTER, PdfName.JBIG2DECODE);
                    put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    if (image.getRawData() != null){
                        bytes = image.getRawData();
                        put(PdfName.LENGTH, new PdfNumber(bytes.length));
                        return;
                    }
                    streamBytes = new ByteArrayOutputStream();
                    transferBytes(is, streamBytes, -1);
                	break;
                default:
                    throw new BadPdfFormatException(MessageLocalization.getComposedMessage("1.is.an.unknown.image.format", errorID));
            }
            if (image.getCompressionLevel() > NO_COMPRESSION)
            	flateCompress(image.getCompressionLevel());
            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
        }
        catch(IOException ioe) {
            throw new BadPdfFormatException(ioe.getMessage());
        }
        finally {
            if (is != null) {
                try{
                    is.close();
                }
                catch (Exception ee) {
                    // empty on purpose
                }
            }
        }
    }
    
    /**
     * Returns the <CODE>PdfName</CODE> of the image.
     *
     * @return		the name
     */
    
    public PdfName name() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    static void transferBytes(InputStream in, OutputStream out, int len) throws IOException {
        byte buffer[] = new byte[TRANSFERSIZE];
        if (len < 0)
            len = 0x7fff0000;
        int size;
        while (len != 0) {
            size = in.read(buffer, 0, Math.min(len, TRANSFERSIZE));
            if (size < 0)
                return;
            out.write(buffer, 0, size);
            len -= size;
        }
    }
    
    protected void importAll(PdfImage dup) {
        name = dup.name;
        compressed = dup.compressed;
        compressionLevel = dup.compressionLevel;
        streamBytes = dup.streamBytes;
        bytes = dup.bytes;
        hashMap = dup.hashMap;
    }
    
    /**
     * Called when no resource name is provided in our constructor.  This generates a 
     * name that is required to be unique within a given resource dictionary.
     * @since 5.0.1
     */
    private void generateImgResName( Image img ) {
    	name = new PdfName( "img" + Long.toHexString( img.getMySerialId() ) );
    }
}
