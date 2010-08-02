/*
 * $Id: InlineImageUtils.java $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Kevin Day, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;

/**
 * Utility methods to help with processing of inline images
 * @since 5.0.4
 */
public final class InlineImageUtils {
    private InlineImageUtils(){}

    /**
     * Simple class in case users need to differentiate an exception from processing
     * inline images vs other exceptions 
     * @since 5.0.4
     */
    public static class InlineImageParseException extends IOException{
        
        public InlineImageParseException(String message) {
            super(message);
        }

    }
    
    /**
     * Map between key abbreviations allowed in dictionary of inline images and their
     * equivalent image dictionary keys
     */
    private final static Map<PdfName, PdfName> inlineImageEntryAbbreviationMap;
    static { // static initializer
        inlineImageEntryAbbreviationMap = new HashMap<PdfName, PdfName>();

        // allowed entries - just pass these through
        inlineImageEntryAbbreviationMap.put(PdfName.BITSPERCOMPONENT, PdfName.BITSPERCOMPONENT);
        inlineImageEntryAbbreviationMap.put(PdfName.COLORSPACE, PdfName.COLORSPACE);
        inlineImageEntryAbbreviationMap.put(PdfName.DECODE, PdfName.DECODE);
        inlineImageEntryAbbreviationMap.put(PdfName.DECODEPARMS, PdfName.DECODEPARMS);
        inlineImageEntryAbbreviationMap.put(PdfName.FILTER, PdfName.FILTER);
        inlineImageEntryAbbreviationMap.put(PdfName.HEIGHT, PdfName.HEIGHT);
        inlineImageEntryAbbreviationMap.put(PdfName.IMAGEMASK, PdfName.IMAGEMASK);
        inlineImageEntryAbbreviationMap.put(PdfName.INTENT, PdfName.INTENT);
        inlineImageEntryAbbreviationMap.put(PdfName.INTERPOLATE, PdfName.INTERPOLATE);
        inlineImageEntryAbbreviationMap.put(PdfName.WIDTH, PdfName.WIDTH);

        // abbreviations - transform these to corresponding correct values
        inlineImageEntryAbbreviationMap.put(new PdfName("BPC"), PdfName.BITSPERCOMPONENT);
        inlineImageEntryAbbreviationMap.put(new PdfName("CS"), PdfName.COLORSPACE);
        inlineImageEntryAbbreviationMap.put(new PdfName("D"), PdfName.DECODE);
        inlineImageEntryAbbreviationMap.put(new PdfName("DP"), PdfName.DECODEPARMS);
        inlineImageEntryAbbreviationMap.put(new PdfName("F"), PdfName.FILTER);
        inlineImageEntryAbbreviationMap.put(new PdfName("H"), PdfName.HEIGHT);
        inlineImageEntryAbbreviationMap.put(new PdfName("IM"), PdfName.IMAGEMASK);
        inlineImageEntryAbbreviationMap.put(new PdfName("I"), PdfName.INTERPOLATE);
        inlineImageEntryAbbreviationMap.put(new PdfName("W"), PdfName.WIDTH);
    }
    
    /**
     * Map between value abbreviations allowed in dictionary of inline images for COLORSPACE
     */
    private static final Map<PdfName, PdfName> inlineImageColorSpaceAbbreviationMap;
    static {
        inlineImageColorSpaceAbbreviationMap = new HashMap<PdfName, PdfName>();
        
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("G"), PdfName.DEVICEGRAY);
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("RGB"), PdfName.DEVICERGB);
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("CMYK"), PdfName.DEVICECMYK);
        inlineImageColorSpaceAbbreviationMap.put(new PdfName("I"), PdfName.INDEXED);
    }
    
    /**
     * Map between value abbreviations allowed in dictionary of inline images for FILTER
     */
    private static final Map<PdfName, PdfName> inlineImageFilterAbbreviationMap;
    static {
        inlineImageFilterAbbreviationMap = new HashMap<PdfName, PdfName>();
        
        inlineImageFilterAbbreviationMap.put(new PdfName("AHx"), PdfName.ASCIIHEXDECODE);
        inlineImageFilterAbbreviationMap.put(new PdfName("A85"), PdfName.ASCII85DECODE);
        inlineImageFilterAbbreviationMap.put(new PdfName("LZW"), PdfName.LZWDECODE);
        inlineImageFilterAbbreviationMap.put(new PdfName("Fl"), PdfName.FLATEDECODE);
        inlineImageFilterAbbreviationMap.put(new PdfName("RL"), PdfName.RUNLENGTHDECODE);
        inlineImageFilterAbbreviationMap.put(new PdfName("CCF"), PdfName.CCITTFAXDECODE);
        inlineImageFilterAbbreviationMap.put(new PdfName("DCT"), PdfName.DCTDECODE);
    }
    
    /**
     * Parses an inline image from the provided content parser.  The parser must be positioned immediately following the BI operator in the content stream.
     * The parser will be left with current position immediately following the EI operator that terminates the inline image
     * @param ps the content parser to use for reading the image. 
     * @return the parsed image
     * @throws IOException if anything goes wring with the parsing
     * @throws InlineImageParseException if parsing of the inline image failed due to issues specific to inline image processing
     */
    public static PdfImageObject parseInlineImage(PdfContentParser ps) throws IOException{
        PdfDictionary inlineImageDictionary = parseInlineImageDictionary(ps);
        byte[] samples = parseInlineImageSamples(inlineImageDictionary, ps);
        return new PdfImageObject(inlineImageDictionary, samples);
    }
    
    /**
     * Parses the next inline image dictionary from the parser.  The parser must be positioned immediately following the EI operator.
     * The parser will be left with position immediately following the whitespace character that follows the ID operator that ends the inline image dictionary.
     * @param ps the parser to extract the embedded image information from
     * @return the dictionary for the inline image, with any abbreviations converted to regular image dictionary keys and values
     * @throws IOException if the parse fails
     */
    private static PdfDictionary parseInlineImageDictionary(PdfContentParser ps) throws IOException{
        // by the time we get to here, we have already parsed the BI operator
        PdfDictionary dictionary = new PdfDictionary();
        
        for(PdfObject key = ps.readPRObject(); key != null && !"ID".equals(key.toString()); key = ps.readPRObject()){
            PdfObject value = ps.readPRObject();

            PdfName resolvedKey = inlineImageEntryAbbreviationMap.get(key);
            if (resolvedKey == null)
                resolvedKey = (PdfName)key;

            dictionary.put(resolvedKey, getAlternateValue(resolvedKey, value));
        }

        int ch = ps.getTokeniser().read();
        if (!PRTokeniser.isWhitespace(ch))
            throw new IOException("Unexpected character " + ch + " found after ID in inline image");
        
        return dictionary;
    }
    
    /**
     * Transforms value abbreviations into their corresponding real value 
     * @param key the key that the value is for
     * @param value the value that might be an abbreviation
     * @return if value is an allowed abbreviation for the key, the expanded value for that abbreviation.  Otherwise, value is returned without modification 
     */
    private static PdfObject getAlternateValue(PdfName key, PdfObject value){
        if (key == PdfName.FILTER){
            if (value instanceof PdfName){
                PdfName altValue = inlineImageFilterAbbreviationMap.get(value);
                if (altValue != null)
                    return altValue;
            } else if (value instanceof PdfArray){
                PdfArray array = ((PdfArray)value);
                PdfArray altArray = new PdfArray();
                int count = array.size();
                for(int i = 0; i < count; i++){
                    altArray.add(getAlternateValue(key, array.getPdfObject(i)));
                }
                return altArray;
            }
        } else if (key == PdfName.COLORSPACE){
            PdfName altValue = inlineImageColorSpaceAbbreviationMap.get(value);
            if (altValue != null)
                return altValue;
        }
        
        return value;
    }
    
    /**
     * @param colorSpaceName the name of the color space. If null, a bi-tonal (black and white) color space is assumed.
     * @return the components per pixel for the specified color space
     */
    private static int getComponentsPerPixel(PdfName colorSpaceName){
        if (colorSpaceName == null)
            return 1;
        if (colorSpaceName.equals(PdfName.DEVICEGRAY))
            return 1;
        if (colorSpaceName.equals(PdfName.DEVICERGB))
            return 3;
        if (colorSpaceName.equals(PdfName.DEVICECMYK))
            return 4;
        
        throw new IllegalArgumentException("Unexpected color space " + colorSpaceName);
    }
    
    /**
     * Computes the number of unfiltered bytes that each row of the image will contain.
     * If the number of bytes results in a partial terminating byte, this number is rounded up
     * per the PDF specification
     * @param imageDictionary the dictionary of the inline image
     * @return the number of bytes per row of the image
     */
    private static int computeBytesPerRow(PdfDictionary imageDictionary){
        PdfNumber wObj = imageDictionary.getAsNumber(PdfName.WIDTH);
        PdfNumber bpcObj = imageDictionary.getAsNumber(PdfName.BITSPERCOMPONENT);
        int cpp = getComponentsPerPixel(imageDictionary.getAsName(PdfName.COLORSPACE));
        
        int w = wObj.intValue();
        int bpc = bpcObj != null ? bpcObj.intValue() : 1;
        
        
        int bytesPerRow = (w * bpc * cpp + 7) / 8;
        
        return bytesPerRow;
    }
    
    /**
     * Parses the samples of the image from the underlying content parser, ignoring all filters.
     * The parser must be positioned immediately after the ID operator that ends the inline image's dictionary.
     * The parser will be left positioned immediately following the EI operator.
     * This is primarily useful if no filters have been applied. 
     * @param imageDictionary the dictionary of the inline image
     * @param ps the content parser
     * @return the samples of the image
     * @throws IOException if anything bad happens during parsing
     */
    private static byte[] parseUnfilteredSamples(PdfDictionary imageDictionary, PdfContentParser ps) throws IOException{
        // special case:  when no filter is specified, we just read the number of bits
        // per component, multiplied by the width and height.
        if (imageDictionary.contains(PdfName.FILTER))
            throw new IllegalArgumentException("Dictionary contains filters");
        
        PdfNumber h = imageDictionary.getAsNumber(PdfName.HEIGHT);
        
        int bytesToRead = computeBytesPerRow(imageDictionary) * h.intValue();
        byte[] bytes = new byte[bytesToRead];
        PRTokeniser tokeniser = ps.getTokeniser();
        
        tokeniser.read(); // skip next character (which better be a whitespace character - I suppose we could check for this)
        for(int i = 0; i < bytesToRead; i++){
            int ch = tokeniser.read();
            if (ch == -1)
                throw new InlineImageParseException("End of content stream reached before end of image data");
            
            bytes[i] = (byte)ch;
        }
        PdfObject ei = ps.readPRObject();
        if (!ei.toString().equals("EI"))
            throw new InlineImageParseException("EI not found after end of image data");
        
        return bytes;
    }
    
    /**
     * Parses the samples of the image from the underlying content parser, accounting for filters
     * The parser must be positioned immediately after the ID operator that ends the inline image's dictionary.
     * The parser will be left positioned immediately following the EI operator.
     * <b>Note:</b>This implementation does not actually apply the filters at this time
     * @param imageDictionary the dictionary of the inline image
     * @param ps the content parser
     * @return the samples of the image
     * @throws IOException if anything bad happens during parsing
     */
    private static byte[] parseInlineImageSamples(PdfDictionary imageDictionary, PdfContentParser ps) throws IOException{
        // by the time we get to here, we have already parsed the ID operator
        
        if (!imageDictionary.contains(PdfName.FILTER)){
            return parseUnfilteredSamples(imageDictionary, ps);
        }
        
        
        // read all content until we reach an EI operator surrounded by whitespace.
        // The following algorithm has two potential issues: what if the image stream 
        // contains <ws>EI<ws> ?
        // Plus, there are some streams that don't have the <ws> before the EI operator
        // it sounds like we would have to actually decode the content stream, which
        // I'd rather avoid right now.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream accumulated = new ByteArrayOutputStream();
        int ch;
        int found = 0;
        PRTokeniser tokeniser = ps.getTokeniser();
        
        while ((ch = tokeniser.read()) != -1){
            if (found == 0 && PRTokeniser.isWhitespace(ch)){
                found++;
                accumulated.write(ch);
            } else if (found == 1 && ch == 'E'){
                found++;
                accumulated.write(ch);
            } else if (found == 1 && PRTokeniser.isWhitespace(ch)){
                // this clause is needed if we have a white space character that is part of the image data
                // followed by a whitespace character that precedes the EI operator.  In this case, we need
                // to flush the first whitespace, then treat the current whitespace as the first potential
                // character for the end of stream check.  Note that we don't increment 'found' here.
                baos.write(accumulated.toByteArray());
                accumulated.reset();
                accumulated.write(ch);
            } else if (found == 2 && ch == 'I'){ 
                found++;
                accumulated.write(ch);
            } else if (found == 3 && PRTokeniser.isWhitespace(ch)){
                return baos.toByteArray();
            } else {
                baos.write(accumulated.toByteArray());
                accumulated.reset();
                
                baos.write(ch);
                found = 0;
            }
        }
        throw new InlineImageParseException("Could not find image data or EI");
    }
}
