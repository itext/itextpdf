/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
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

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.lowagie.text.Rectangle;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;
/** Reads a PDF document and prepares it to import pages to our
 * document. This class is not mutable and is thread safe; this means that
 * a single instance can serve as many output documents as needed and can even be static.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfReader {

    static final PdfName pageInhCandidates[] = {
        PdfName.MEDIABOX, PdfName.ROTATE, PdfName.RESOURCES, PdfName.CROPBOX};

    PRTokeniser tokens;
    int xref[];
    PdfObject xrefObj[];
    PdfDictionary trailer;
    PdfDictionary pages[];
    ArrayList pageInh;
    int pagesCount;
    
    /** Reads and parses a PDF document.
     * @param filename the file name of the document
     * @throws IOException on error
     */    
    public PdfReader(String filename) throws IOException {
        tokens = new PRTokeniser(filename);
        readPdf();
    }
    
    /** Reads and parses a PDF document.
     * @param pdfIn the byte array with the document
     * @throws IOException on error
     */    
    public PdfReader(byte pdfIn[]) throws IOException {
        tokens = new PRTokeniser(pdfIn);
        readPdf();
    }
    
    public RandomAccessFileOrArray getSafeFile() {
        return tokens.getSafeFile();
    }
    
    protected PdfReaderInstance getPdfReaderInstance(PdfWriter writer) {
        return new PdfReaderInstance(this, writer, xrefObj, pages);
    }
    
    /** Gets the number of pages in the document.
     * @return the number of pages in the document
     */    
    public int getNumberOfPages() {
        return pages.length;
    }
    
    /**
     * Gets the page rotation. This value can be 0, 90, 180 or 270.
     * @param index the page number. The first page is 1
     * @return the page rotation
     */    
    public int getPageRotation(int index) {
        PdfDictionary page = pages[index - 1];
        PdfNumber rotate = (PdfNumber)getPdfObject(page.get(PdfName.ROTATE));
        if (rotate == null)
            return 0;
        else
            return rotate.intValue();
    }
    
    /** Gets the page size, taking rotation into account. This
     * is a <CODE>Rectangle</CODE> with the value of the /MediaBox and the /Rotate key.
     * @param index the page number. The first page is 1
     * @return a <CODE>Rectangle</CODE>
     */    
    public Rectangle getPageSizeWithRotation(int index) {
        Rectangle rect = getPageSize(index);
        int rotation = getPageRotation(index);
        while (rotation > 0) {
            rect = rect.rotate();
            rotation -= 90;
        }
        return rect;
    }
    
    /** Gets the page size without taking rotation into account. This
     * is the value of the /MediaBox key.
     * @param index the page number. The first page is 1
     * @return the page size
     */    
    public Rectangle getPageSize(int index) {
        PdfDictionary page = pages[index - 1];
        PdfArray mediaBox = (PdfArray)getPdfObject(page.get(PdfName.MEDIABOX));
        return getNormalizedRectangle(mediaBox);
    }
    
    /** Gets the crop box without taking rotation into account. This
     * is the value of the /CropBox key. The crop box is the part
     * of the document to be displayed or printed. It usually is the same
     * as the media box but may be smaller.
     * @param index the page number. The first page is 1
     * @return the crop box
     */    
    public Rectangle getCropBox(int index) {
        PdfDictionary page = pages[index - 1];
        PdfArray cropBox = (PdfArray)getPdfObject(page.get(PdfName.CROPBOX));
        if (cropBox == null)
            return getPageSize(index);
        return getNormalizedRectangle(cropBox);
    }
    
    /** Returns the content of the document information dictionary as a <CODE>HashMap</CODE>
     * of <CODE>String</CODE>.
     * @return content of the document information dictionary
     */    
    public HashMap getInfo() {
        HashMap map = new HashMap();
        PdfDictionary info = (PdfDictionary)getPdfObject(trailer.get(PdfName.INFO));
        if (info == null)
            return map;
        for (Iterator it = info.getKeys().iterator(); it.hasNext();) {
            PdfName key = (PdfName)it.next();
            PdfObject obj = (PdfObject)getPdfObject(info.get(key));
            if (obj == null)
                continue;
            String value = obj.toString();
            switch (obj.type()) {
                case PdfObject.STRING: {
                    byte b[] = PdfEncodings.convertToBytes(value, null);
                    if (b.length >= 2 && b[0] == (byte)254 && b[1] == (byte)255)
                        value = PdfEncodings.convertToString(b, PdfObject.TEXT_UNICODE);
                    else
                        value = PdfEncodings.convertToString(b, PdfObject.ENCODING);
                    break;
                }
                case PdfObject.NAME: {
                    value = PdfName.decodeName(value);
                    break;
                }
            }
            map.put(PdfName.decodeName(key.toString()), value);
        }
        return map;
    }
    
    public static Rectangle getNormalizedRectangle(PdfArray box) {
        ArrayList rect = box.getArrayList();
        float llx = ((PdfNumber)rect.get(0)).floatValue();
        float lly = ((PdfNumber)rect.get(1)).floatValue();
        float urx = ((PdfNumber)rect.get(2)).floatValue();
        float ury = ((PdfNumber)rect.get(3)).floatValue();
        return new Rectangle(Math.min(llx, urx), Math.min(lly, ury),
            Math.max(llx, urx), Math.max(lly, ury));
    }
    
    protected void readPdf() throws IOException {
        try {
            tokens.checkPdfHeader();
            readXref();
            readDocObj();
            readPages();
        }
        finally {
            try {
                tokens.close();
            }
            catch (Exception e) {
                // empty on purpose
            }
        }
    }

    protected PdfObject getPdfObject(PdfObject obj) {
        if (obj == null)
            return null;
        if (obj.type() != PdfObject.INDIRECT)
            return obj;
        int idx = ((PRIndirectReference)obj).getNumber();
        obj = xrefObj[idx];
        if (obj == null)
            return PdfNull.PDFNULL;
        else
            return obj;
    }
    
    protected void pushPageAttributes(PdfDictionary nodePages) {
        PdfDictionary dic = new PdfDictionary();
        if (pageInh.size() != 0) {
            dic.putAll((PdfDictionary)pageInh.get(pageInh.size() - 1));
        }
        for (int k = 0; k < pageInhCandidates.length; ++k) {
            PdfObject obj = nodePages.get(pageInhCandidates[k]);
            if (obj != null)
                dic.put(pageInhCandidates[k], obj);
        }
        pageInh.add(dic);
    }

    protected void popPageAttributes() {
        pageInh.remove(pageInh.size() - 1);
    }
    
    protected void iteratePages(PdfDictionary page) throws IOException {
        PdfName type = (PdfName)getPdfObject(page.get(PdfName.TYPE));
        if (type.equals(PdfName.PAGE)) {
            PdfDictionary dic = (PdfDictionary)pageInh.get(pageInh.size() - 1);
            PdfName key;
            for (Iterator i = dic.getKeys().iterator(); i.hasNext();) {
                key = (PdfName)i.next();
                if (page.get(key) == null)
                    page.put(key, dic.get(key));
            }
            pages[pagesCount++] = page;
        }
        else {
            pushPageAttributes(page);
            PdfArray kidsPR = (PdfArray)getPdfObject(page.get(PdfName.KIDS));
            ArrayList kids = kidsPR.getArrayList();
            for (int k = 0; k < kids.size(); ++k){
                PdfDictionary kid = (PdfDictionary)getPdfObject((PdfObject)kids.get(k));
                iteratePages(kid);
            }
            popPageAttributes();
        }
    }
    
    protected void readPages() throws IOException {
        pageInh = new ArrayList();
        PdfDictionary catalog = (PdfDictionary)getPdfObject(trailer.get(PdfName.ROOT));
        PdfDictionary rootPages = (PdfDictionary)getPdfObject(catalog.get(PdfName.PAGES));
        PdfNumber count = (PdfNumber)getPdfObject(rootPages.get(PdfName.COUNT));
        pages = new PdfDictionary[count.intValue()];
        pagesCount = 0;
        iteratePages(rootPages);
        pageInh = null;
    }
    
    protected void readDocObj() throws IOException {
        ArrayList streams = new ArrayList();
        xrefObj = new PdfObject[xref.length];
        for (int k = 1; k < xrefObj.length; ++k) {
            int pos = xref[k];
            if (pos <= 0)
                continue;
            tokens.seek(pos);
            tokens.nextValidToken();
            if (tokens.getTokenType() != PRTokeniser.TK_NUMBER)
                tokens.throwError("Invalid object number.");
            int objNum = tokens.intValue();
            tokens.nextValidToken();
            if (tokens.getTokenType() != PRTokeniser.TK_NUMBER)
                tokens.throwError("Invalid generation number.");
            int objGen = tokens.intValue();
            tokens.nextValidToken();
            if (!tokens.getStringValue().equals("obj"))
                tokens.throwError("Token 'obj' expected.");
            PdfObject obj = readPRObject();
            xrefObj[k] = obj;
            if (obj.type() == PdfObject.STREAM)
                streams.add(obj);
        }
        for (int k = 0; k < streams.size(); ++k) {
            PRStream stream = (PRStream)streams.get(k);
            PdfObject length = getPdfObject(stream.get(PdfName.LENGTH));
            stream.setLength(((PdfNumber)length).intValue());
        }
    }
    
    protected void readXref() throws IOException {
        tokens.seek(tokens.getStartxref());
        tokens.nextToken();
        if (!tokens.getStringValue().equals("startxref"))
            throw new IOException("startxref not found.");
        tokens.nextToken();
        if (tokens.getTokenType() != PRTokeniser.TK_NUMBER)
            throw new IOException("startxref is not followed by a number.");
        int startxref = tokens.intValue();
        tokens.seek(startxref);
        int ch;
        do {
            ch = tokens.read();
        } while (ch != -1 && ch != 't');
        if (ch == -1)
            throw new IOException("Unexpected end of file.");
        tokens.backOnePosition();
        tokens.nextValidToken();
        if (!tokens.getStringValue().equals("trailer"))
            throw new IOException("trailer not found.");
        trailer = (PdfDictionary)readPRObject();
        if (trailer.get(PdfName.ENCRYPT) != null)
            throw new IOException("Encrypted files are not supported.");
        PdfNumber xrefSize = (PdfNumber)trailer.get(PdfName.SIZE);
        xref = new int[xrefSize.intValue()];
        tokens.seek(startxref);
        readXrefSection();
        PdfDictionary trailer2 = trailer;
        while (true) {
            PdfNumber prev = (PdfNumber)trailer2.get(PdfName.PREV);
            if (prev == null)
                break;
            tokens.seek(prev.intValue());
            readXrefSection();
            trailer2 = (PdfDictionary)readPRObject();
        }
    }
    
    protected void readXrefSection() throws IOException {
        tokens.nextValidToken();
        if (!tokens.getStringValue().equals("xref"))
            tokens.throwError("xref subsection not found");
        int start = 0;
        int end = 0;
        int pos = 0;
        int gen = 0;
        while (true) {
            tokens.nextValidToken();
            if (tokens.getStringValue().equals("trailer"))
                break;
            if (tokens.getTokenType() != PRTokeniser.TK_NUMBER)
                tokens.throwError("Object number of the first object in this xref subsection not found");
            start = tokens.intValue();
            tokens.nextValidToken();
            if (tokens.getTokenType() != PRTokeniser.TK_NUMBER)
                tokens.throwError("Number of entries in this xref subsection not found");
            end = tokens.intValue() + start;
            for (int k = start; k < end; ++k) {
                tokens.nextValidToken();
                pos = tokens.intValue();
                tokens.nextValidToken();
                gen = tokens.intValue();
                tokens.nextValidToken();
                if (tokens.getStringValue().equals("n")) {
                    if (xref[k] == 0)
                        xref[k] = pos;
                }
                else if (tokens.getStringValue().equals("f")) {
                    if (xref[k] == 0)
                        xref[k] = -1;
                }
                else
                    tokens.throwError("Invalid cross-reference entry in this xref subsection");
            }
        }
    }
    
    protected PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (true) {
            tokens.nextValidToken();
            if (tokens.getTokenType() == PRTokeniser.TK_END_DIC)
                break;
            if (tokens.getTokenType() != PRTokeniser.TK_NAME)
                tokens.throwError("Dictionary key is not a name.");
            PdfName name = new PdfName(tokens.getStringValue());
            PdfObject obj = readPRObject();
            int type = obj.type();
            if (-type == PRTokeniser.TK_END_DIC)
                tokens.throwError("Unexpected '>>'");
            if (-type == PRTokeniser.TK_END_ARRAY)
                tokens.throwError("Unexpected ']'");
            dic.put(name, obj);
        }
        return dic;
    }
    
    protected PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readPRObject();
            int type = obj.type();
            if (-type == PRTokeniser.TK_END_ARRAY)
                break;
            if (-type == PRTokeniser.TK_END_DIC)
                tokens.throwError("Unexpected '>>'");
            array.add(obj);
        }
        return array;
    }
    
    protected PdfObject readPRObject() throws IOException {
        tokens.nextValidToken();
        int type = tokens.getTokenType();
        switch (type) {
            case PRTokeniser.TK_START_DIC:
            {
                PdfDictionary dic = readDictionary();
                int pos = tokens.getFilePointer();
                tokens.nextValidToken();
                if (tokens.getStringValue().equals("stream")) {
                    int ch = tokens.read();
                    if (ch == '\r')
                        ch = tokens.read();
                    if (ch != '\n')
                        tokens.backOnePosition();
                    PRStream stream = new PRStream(this, tokens.getFilePointer());
                    stream.putAll(dic);
                    return stream;
                }
                else {
                    tokens.seek(pos);
                    return dic;
                }
            }
            case PRTokeniser.TK_START_ARRAY:
                return readArray();
            case PRTokeniser.TK_NUMBER:
                return new PdfNumber(tokens.getStringValue());
            case PRTokeniser.TK_STRING:
                return new PdfString(tokens.getStringValue(), null);
            case PRTokeniser.TK_NAME:
                return new PdfName(tokens.getStringValue());
            case PRTokeniser.TK_REF:
                return new PRIndirectReference(this, tokens.getReference(), tokens.getReference());
            default:
                return new PdfLiteral(-type, tokens.getStringValue());
        }
    }

    public static byte[] FlateDecode(byte in[]) {
        byte b[] = FlateDecode(in, true);
        if (b == null)
            return FlateDecode(in, false);
        return b;
    }
    
    public static byte[] FlateDecode(byte in[], boolean strict) {
        ByteArrayInputStream stream = new ByteArrayInputStream(in);
        InflaterInputStream zip = new InflaterInputStream(stream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte b[] = new byte[strict ? 4092 : 1];
        try {
            int n;
            while ((n = zip.read(b)) >= 0) {
                out.write(b, 0, n);
            }
            zip.close();
            out.close();
            return out.toByteArray();
        }
        catch (Exception e) {
            if (strict)
                return null;
            return out.toByteArray();
        }
    }

    public static byte[] ASCIIHexDecode(byte in[]) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean first = true;
        int n1 = 0;
        for (int k = 0; k < in.length; ++k) {
            int ch = in[k] & 0xff;
            if (ch == '>')
                break;
            if (PRTokeniser.isWhitespace(ch))
                continue;
            int n = PRTokeniser.getHex(ch);
            if (n == -1)
                throw new RuntimeException("Illegal character in ASCIIHexDecode.");
            if (first)
                n1 = n;
            else
                out.write((byte)((n1 << 4) + n));
            first = !first;
        }
        if (!first)
            out.write((byte)(n1 << 4));
        return out.toByteArray();
    }

    public static byte[] ASCII85Decode(byte in[]) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int state = 0;
        int chn[] = new int[5];
        for (int k = 0; k < in.length; ++k) {
            int ch = in[k] & 0xff;
            if (ch == '~')
                break;
            if (PRTokeniser.isWhitespace(ch))
                continue;
            if (ch == 'z' && state == 0) {
                out.write(0);
                out.write(0);
                out.write(0);
                out.write(0);
                continue;
            }
            if (ch < '!' || ch > 'u')
                throw new RuntimeException("Illegal character in ASCII85Decode.");
            chn[state] = ch - '!';
            ++state;
            if (state == 5) {
                state = 0;
                int r = 0;
                for (int j = 0; j < 5; ++j)
                    r = r * 85 + chn[j];
                out.write((byte)(r >> 24));
                out.write((byte)(r >> 16));
                out.write((byte)(r >> 8));
                out.write((byte)r);
            }
        }
        int r = 0;
        if (state == 1)
            throw new RuntimeException("Illegal length in ASCII85Decode.");
        if (state == 2) {
            r = chn[0] * 85 + chn[1];
            out.write((byte)r);
        }
        else if (state == 3) {
            r = chn[0] * 85 * 85 + chn[1] * 85 + chn[2];
            out.write((byte)(r >> 8));
            out.write((byte)r);
        }
        else if (state == 4) {
            r = chn[0] * 85 * 85 * 85 + chn[1] * 85 * 85 + chn[2] * 85 + chn[3];
            out.write((byte)(r >> 16));
            out.write((byte)(r >> 8));
            out.write((byte)r);
        }
        return out.toByteArray();
    }

    public static byte[] LZWDecode(byte in[]) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZWDecoder lzw = new LZWDecoder();
        lzw.decode(in, out);
        return out.toByteArray();
    }
}
