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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashSet;

import com.lowagie.text.DocListener;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.ImgWMF;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.ImgPostscript;

/**
 * A <CODE>DocWriter</CODE> class for PDF.
 * <P>
 * When this <CODE>PdfWriter</CODE> is added
 * to a certain <CODE>PdfDocument</CODE>, the PDF representation of every Element
 * added to this Document will be written to the outputstream.</P>
 */

public class PdfWriter extends DocWriter {
    
    // inner classes
    
    /**
     * This class generates the structure of a PDF document.
     * <P>
     * This class covers the third section of Chapter 5 in the 'Portable Document Format
     * Reference Manual version 1.3' (page 55-60). It contains the body of a PDF document
     * (section 5.14) and it can also generate a Cross-reference Table (section 5.15).
     *
     * @see		PdfWriter
     * @see		PdfObject
     * @see		PdfIndirectObject
     */
    
    public static class PdfBody {
        
        // inner classes
        
        /**
         * <CODE>PdfCrossReference</CODE> is an entry in the PDF Cross-Reference table.
         */
        
        static class PdfCrossReference implements Comparable {
            
            // membervariables
            private int type;
            
            /**	Byte offset in the PDF file. */
            private int offset;
            
            private int refnum;
            /**	generation of the object. */
            private int generation;
            
            // constructors
            /**
             * Constructs a cross-reference element for a PdfIndirectObject.
             * @param refnum
             * @param	offset		byte offset of the object
             * @param	generation	generationnumber of the object
             */
            
            PdfCrossReference(int refnum, int offset, int generation) {
                type = 0;
                this.offset = offset;
                this.refnum = refnum;
                this.generation = generation;
            }
            
            /**
             * Constructs a cross-reference element for a PdfIndirectObject.
             * @param refnum
             * @param	offset		byte offset of the object
             */
            
            PdfCrossReference(int refnum, int offset) {
                type = 1;
                this.offset = offset;
                this.refnum = refnum;
                this.generation = 0;
            }
            
            PdfCrossReference(int type, int refnum, int offset, int generation) {
                this.type = type;
                this.offset = offset;
                this.refnum = refnum;
                this.generation = generation;
            }
            
            int getRefnum() {
                return refnum;
            }
            
            /**
             * Returns the PDF representation of this <CODE>PdfObject</CODE>.
             * @param os
             * @throws IOException
             */
            
            public void toPdf(OutputStream os) throws IOException {
                // This code makes it more difficult to port the lib to JDK1.1.x:
                // StringBuffer off = new StringBuffer("0000000000").append(offset);
                // off.delete(0, off.length() - 10);
                // StringBuffer gen = new StringBuffer("00000").append(generation);
                // gen.delete(0, gen.length() - 5);
                // so it was changed into this:
                String s = "0000000000" + offset;
                StringBuffer off = new StringBuffer(s.substring(s.length() - 10));
                s = "00000" + generation;
                String gen = s.substring(s.length() - 5);
                if (generation == 65535) {
                    os.write(getISOBytes(off.append(' ').append(gen).append(" f \n").toString()));
                }
                else
                    os.write(getISOBytes(off.append(' ').append(gen).append(" n \n").toString()));
            }
            
            /**
             * Writes PDF syntax to the OutputStream
             * @param midSize
             * @param os
             * @throws IOException
             */
            public void toPdf(int midSize, OutputStream os) throws IOException {
                os.write((byte)type);
                while (--midSize >= 0)
                    os.write((byte)((offset >>> (8 * midSize)) & 0xff));
                os.write((byte)((generation >>> 8) & 0xff));
                os.write((byte)(generation & 0xff));
            }
            
            /**
             * @see java.lang.Comparable#compareTo(java.lang.Object)
             */
            public int compareTo(Object o) {
                PdfCrossReference other = (PdfCrossReference)o;
                return (refnum < other.refnum ? -1 : (refnum==other.refnum ? 0 : 1));
            }
            
            /**
             * @see java.lang.Object#equals(java.lang.Object)
             */
            public boolean equals(Object obj) {
                if (obj instanceof PdfCrossReference) {
                    PdfCrossReference other = (PdfCrossReference)obj;
                    return (refnum == other.refnum);
                }
                else
                    return false;
            }
            
        }
        
        // membervariables
        
        /** array containing the cross-reference table of the normal objects. */
        private TreeSet xrefs;
        private int refnum;
        /** the current byteposition in the body. */
        private int position;
        private PdfWriter writer;
        // constructors
        
        /**
         * Constructs a new <CODE>PdfBody</CODE>.
         * @param writer
         */
        PdfBody(PdfWriter writer) {
            xrefs = new TreeSet();
            xrefs.add(new PdfCrossReference(0, 0, 65535));
            position = writer.getOs().getCounter();
            refnum = 1;
            this.writer = writer;
        }
        
        void setRefnum(int refnum) {
            this.refnum = refnum;
        }
        
        // methods
        
        private static final int OBJSINSTREAM = 200;
        
        private ByteBuffer index;
        private ByteBuffer streamObjects;
        private int currentObjNum;
        private int numObj = 0;
        
        private PdfWriter.PdfBody.PdfCrossReference addToObjStm(PdfObject obj, int nObj) throws IOException {
            if (numObj >= OBJSINSTREAM)
                flushObjStm();
            if (index == null) {
                index = new ByteBuffer();
                streamObjects = new ByteBuffer();
                currentObjNum = getIndirectReferenceNumber();
                numObj = 0;
            }
            int p = streamObjects.size();
            int idx = numObj++;
            PdfEncryption enc = writer.crypto;
            writer.crypto = null;
            obj.toPdf(writer, streamObjects);
            writer.crypto = enc;
            streamObjects.append(' ');
            index.append(nObj).append(' ').append(p).append(' ');
            return new PdfWriter.PdfBody.PdfCrossReference(2, nObj, currentObjNum, idx);
        }
        
        private void flushObjStm() throws IOException {
            if (numObj == 0)
                return;
            int first = index.size();
            index.append(streamObjects);
            PdfStream stream = new PdfStream(index.toByteArray());
            stream.flateCompress();
            stream.put(PdfName.TYPE, PdfName.OBJSTM);
            stream.put(PdfName.N, new PdfNumber(numObj));
            stream.put(PdfName.FIRST, new PdfNumber(first));
            add(stream, currentObjNum);
            index = null;
            streamObjects = null;
            numObj = 0;
        }
        
        /**
         * Adds a <CODE>PdfObject</CODE> to the body.
         * <P>
         * This methods creates a <CODE>PdfIndirectObject</CODE> with a
         * certain number, containing the given <CODE>PdfObject</CODE>.
         * It also adds a <CODE>PdfCrossReference</CODE> for this object
         * to an <CODE>ArrayList</CODE> that will be used to build the
         * Cross-reference Table.
         *
         * @param		object			a <CODE>PdfObject</CODE>
         * @return		a <CODE>PdfIndirectObject</CODE>
         * @throws IOException
         */
        
        PdfIndirectObject add(PdfObject object) throws IOException {
            return add(object, getIndirectReferenceNumber());
        }
        
        PdfIndirectObject add(PdfObject object, boolean inObjStm) throws IOException {
            return add(object, getIndirectReferenceNumber(), inObjStm);
        }
        
        /**
         * Gets a PdfIndirectReference for an object that will be created in the future.
         * @return a PdfIndirectReference
         */
        
        PdfIndirectReference getPdfIndirectReference() {
            return new PdfIndirectReference(0, getIndirectReferenceNumber());
        }
        
        int getIndirectReferenceNumber() {
            int n = refnum++;
            xrefs.add(new PdfCrossReference(n, 0, 65536));
            return n;
        }
        
        /**
         * Adds a <CODE>PdfObject</CODE> to the body given an already existing
         * PdfIndirectReference.
         * <P>
         * This methods creates a <CODE>PdfIndirectObject</CODE> with the number given by
         * <CODE>ref</CODE>, containing the given <CODE>PdfObject</CODE>.
         * It also adds a <CODE>PdfCrossReference</CODE> for this object
         * to an <CODE>ArrayList</CODE> that will be used to build the
         * Cross-reference Table.
         *
         * @param		object			a <CODE>PdfObject</CODE>
         * @param		ref		        a <CODE>PdfIndirectReference</CODE>
         * @return		a <CODE>PdfIndirectObject</CODE>
         * @throws IOException
         */
        
        PdfIndirectObject add(PdfObject object, PdfIndirectReference ref) throws IOException {
            return add(object, ref.getNumber());
        }
        
        PdfIndirectObject add(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException {
            return add(object, ref.getNumber(), inObjStm);
        }
        
        PdfIndirectObject add(PdfObject object, int refNumber) throws IOException {
            return add(object, refNumber, true); // to false
        }
        
        PdfIndirectObject add(PdfObject object, int refNumber, boolean inObjStm) throws IOException {
            if (inObjStm && object.canBeInObjStm() && writer.isFullCompression()) {
                PdfCrossReference pxref = addToObjStm(object, refNumber);
                PdfIndirectObject indirect = new PdfIndirectObject(refNumber, object, writer);
                if (!xrefs.add(pxref)) {
                    xrefs.remove(pxref);
                    xrefs.add(pxref);
                }
                return indirect;
            }
            else {
                PdfIndirectObject indirect = new PdfIndirectObject(refNumber, object, writer);
                PdfCrossReference pxref = new PdfCrossReference(refNumber, position);
                if (!xrefs.add(pxref)) {
                    xrefs.remove(pxref);
                    xrefs.add(pxref);
                }
                indirect.writeTo(writer.getOs());
                position = writer.getOs().getCounter();
                return indirect;
            }
        }
        
        /**
         * Adds a <CODE>PdfResources</CODE> object to the body.
         *
         * @param		object			the <CODE>PdfResources</CODE>
         * @return		a <CODE>PdfIndirectObject</CODE>
         */
        
//        PdfIndirectObject add(PdfResources object) {
//            return add(object);
//        }
        
        /**
         * Adds a <CODE>PdfPages</CODE> object to the body.
         *
         * @param		object			the root of the document
         * @return		a <CODE>PdfIndirectObject</CODE>
         */
        
//        PdfIndirectObject add(PdfPages object) throws IOException {
//            PdfIndirectObject indirect = new PdfIndirectObject(PdfWriter.ROOT, object, writer);
//            rootOffset = position;
//            indirect.writeTo(writer.getOs());
//            position = writer.getOs().getCounter();
//            return indirect;
//        }
        
        /**
         * Returns the offset of the Cross-Reference table.
         *
         * @return		an offset
         */
        
        int offset() {
            return position;
        }
        
        /**
         * Returns the total number of objects contained in the CrossReferenceTable of this <CODE>Body</CODE>.
         *
         * @return	a number of objects
         */
        
        int size() {
            return Math.max(((PdfCrossReference)xrefs.last()).getRefnum() + 1, refnum);
        }
        
        /**
         * Returns the CrossReferenceTable of the <CODE>Body</CODE>.
         * @param os
         * @param root
         * @param info
         * @param encryption
         * @param fileID
         * @param prevxref
         * @throws IOException
         */
        
        void writeCrossReferenceTable(OutputStream os, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID, int prevxref) throws IOException {
            int refNumber = 0;
            if (writer.isFullCompression()) {
                flushObjStm();
                refNumber = getIndirectReferenceNumber();
                xrefs.add(new PdfCrossReference(refNumber, position));
            }
            PdfCrossReference entry = (PdfCrossReference)xrefs.first();
            int first = entry.getRefnum();
            int len = 0;
            ArrayList sections = new ArrayList();
            for (Iterator i = xrefs.iterator(); i.hasNext(); ) {
                entry = (PdfCrossReference)i.next();
                if (first + len == entry.getRefnum())
                    ++len;
                else {
                    sections.add(new Integer(first));
                    sections.add(new Integer(len));
                    first = entry.getRefnum();
                    len = 1;
                }
            }
            sections.add(new Integer(first));
            sections.add(new Integer(len));
            if (writer.isFullCompression()) {
                int mid = 4;
                int mask = 0xff000000;
                for (; mid > 1; --mid) {
                    if ((mask & position) != 0)
                        break;
                    mask >>>= 8;
                }
                ByteBuffer buf = new ByteBuffer();
                
                for (Iterator i = xrefs.iterator(); i.hasNext(); ) {
                    entry = (PdfCrossReference) i.next();
                    entry.toPdf(mid, buf);
                }
                PdfStream xr = new PdfStream(buf.toByteArray());
                buf = null;
                xr.flateCompress();
                xr.put(PdfName.SIZE, new PdfNumber(size()));
                xr.put(PdfName.ROOT, root);
                if (info != null) {
                    xr.put(PdfName.INFO, info);
                }
                if (encryption != null)
                    xr.put(PdfName.ENCRYPT, encryption);
                if (fileID != null)
                    xr.put(PdfName.ID, fileID);
                xr.put(PdfName.W, new PdfArray(new int[]{1, mid, 2}));
                xr.put(PdfName.TYPE, PdfName.XREF);
                PdfArray idx = new PdfArray();
                for (int k = 0; k < sections.size(); ++k)
                    idx.add(new PdfNumber(((Integer)sections.get(k)).intValue()));
                xr.put(PdfName.INDEX, idx);
                if (prevxref > 0)
                    xr.put(PdfName.PREV, new PdfNumber(prevxref));
                PdfEncryption enc = writer.crypto;
                writer.crypto = null;
                PdfIndirectObject indirect = new PdfIndirectObject(refNumber, xr, writer);
                indirect.writeTo(writer.getOs());
                writer.crypto = enc;
            }
            else {
                os.write(getISOBytes("xref\n"));
                Iterator i = xrefs.iterator();
                for (int k = 0; k < sections.size(); k += 2) {
                    first = ((Integer)sections.get(k)).intValue();
                    len = ((Integer)sections.get(k + 1)).intValue();
                    os.write(getISOBytes(String.valueOf(first)));
                    os.write(getISOBytes(" "));
                    os.write(getISOBytes(String.valueOf(len)));
                    os.write('\n');
                    while (len-- > 0) {
                        entry = (PdfCrossReference) i.next();
                        entry.toPdf(os);
                    }
                }
            }
        }
    }
    
    /**
     * <CODE>PdfTrailer</CODE> is the PDF Trailer object.
     * <P>
     * This object is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 5.16 (page 59-60).
     */
    
    static class PdfTrailer extends PdfDictionary {
        
        // membervariables
        
        int offset;
        
        // constructors
        
        /**
         * Constructs a PDF-Trailer.
         *
         * @param		size		the number of entries in the <CODE>PdfCrossReferenceTable</CODE>
         * @param		offset		offset of the <CODE>PdfCrossReferenceTable</CODE>
         * @param		root		an indirect reference to the root of the PDF document
         * @param		info		an indirect reference to the info object of the PDF document
         * @param encryption
         * @param fileID
         * @param prevxref
         */
        
        PdfTrailer(int size, int offset, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID, int prevxref) {
            this.offset = offset;
            put(PdfName.SIZE, new PdfNumber(size));
            put(PdfName.ROOT, root);
            if (info != null) {
                put(PdfName.INFO, info);
            }
            if (encryption != null)
                put(PdfName.ENCRYPT, encryption);
            if (fileID != null)
                put(PdfName.ID, fileID);
            if (prevxref > 0)
                put(PdfName.PREV, new PdfNumber(prevxref));
        }
        
        /**
         * Returns the PDF representation of this <CODE>PdfObject</CODE>.
         * @param writer
         * @param os
         * @throws IOException
         */
        public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
            os.write(getISOBytes("trailer\n"));
            super.toPdf(null, os);
            os.write(getISOBytes("\nstartxref\n"));
            os.write(getISOBytes(String.valueOf(offset)));
            os.write(getISOBytes("\n%%EOF\n"));
        }
    }
    // static membervariables
    
    /** A viewer preference */
    public static final int PageLayoutSinglePage = 1;
    /** A viewer preference */
    public static final int PageLayoutOneColumn = 2;
    /** A viewer preference */
    public static final int PageLayoutTwoColumnLeft = 4;
    /** A viewer preference */
    public static final int PageLayoutTwoColumnRight = 8;
    
    /** A viewer preference */
    public static final int PageModeUseNone = 16;
    /** A viewer preference */
    public static final int PageModeUseOutlines = 32;
    /** A viewer preference */
    public static final int PageModeUseThumbs = 64;
    /** A viewer preference */
    public static final int PageModeFullScreen = 128;
    /** A viewer preference */
    public static final int PageModeUseOC = 1 << 20;
    
    /** A viewer preference */
    public static final int HideToolbar = 256;
    /** A viewer preference */
    public static final int HideMenubar = 512;
    /** A viewer preference */
    public static final int HideWindowUI = 1024;
    /** A viewer preference */
    public static final int FitWindow = 2048;
    /** A viewer preference */
    public static final int CenterWindow = 4096;
    
    /** A viewer preference */
    public static final int NonFullScreenPageModeUseNone = 8192;
    /** A viewer preference */
    public static final int NonFullScreenPageModeUseOutlines = 16384;
    /** A viewer preference */
    public static final int NonFullScreenPageModeUseThumbs = 32768;
    /** A viewer preference */
    public static final int NonFullScreenPageModeUseOC = 1 << 19;
    
    /** A viewer preference */
    public static final int DirectionL2R = 1 << 16;
    /** A viewer preference */
    public static final int DirectionR2L = 1 << 17;
    /** A viewer preference */
    public static final int DisplayDocTitle = 1 << 18;
    /** A viewer preference */
    public static final int PrintScalingNone = 1 << 20;
    /** The mask to decide if a ViewerPreferences dictionary is needed */
    static final int ViewerPreferencesMask = 0xffff00;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowPrinting = 4 + 2048;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowModifyContents = 8;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowCopy = 16;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowModifyAnnotations = 32;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowFillIn = 256;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowScreenReaders = 512;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowAssembly = 1024;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowDegradedPrinting = 4;
    /** Type of encryption */
    public static final boolean STRENGTH40BITS = false;
    /** Type of encryption */
    public static final boolean STRENGTH128BITS = true;
    /** action value */
    public static final PdfName DOCUMENT_CLOSE = PdfName.WC;
    /** action value */
    public static final PdfName WILL_SAVE = PdfName.WS;
    /** action value */
    public static final PdfName DID_SAVE = PdfName.DS;
    /** action value */
    public static final PdfName WILL_PRINT = PdfName.WP;
    /** action value */
    public static final PdfName DID_PRINT = PdfName.DP;
    /** action value */
    public static final PdfName PAGE_OPEN = PdfName.O;
    /** action value */
    public static final PdfName PAGE_CLOSE = PdfName.C;

    /** signature value */
    public static final int SIGNATURE_EXISTS = 1;
    /** signature value */
    public static final int SIGNATURE_APPEND_ONLY = 2;
    
    /** possible PDF version */
    public static final char VERSION_1_2 = '2';
    /** possible PDF version */
    public static final char VERSION_1_3 = '3';
    /** possible PDF version */
    public static final char VERSION_1_4 = '4';
    /** possible PDF version */
    public static final char VERSION_1_5 = '5';
    /** possible PDF version */
    public static final char VERSION_1_6 = '6';
    
    private static final int VPOINT = 7;
    /** this is the header of a PDF document */
    protected byte[] HEADER = getISOBytes("%PDF-1.4\n%\u00e2\u00e3\u00cf\u00d3\n");

    protected int prevxref = 0;
    
    protected PdfPages root = new PdfPages(this);
    
    /** Dictionary, containing all the images of the PDF document */
    protected PdfDictionary imageDictionary = new PdfDictionary();
    
    /** This is the list with all the images in the document. */
    private HashMap images = new HashMap();
    
    /** The form XObjects in this document. The key is the xref and the value
        is Object[]{PdfName, template}.*/
    protected HashMap formXObjects = new HashMap();
    
    /** The name counter for the form XObjects name. */
    protected int formXObjectsCounter = 1;
    
    /** The font number counter for the fonts in the document. */
    protected int fontNumber = 1;
    
    /** The color number counter for the colors in the document. */
    protected int colorNumber = 1;
    
    /** The patten number counter for the colors in the document. */
    protected int patternNumber = 1;
    
    /** The direct content in this document. */
    protected PdfContentByte directContent;
    
    /** The direct content under in this document. */
    protected PdfContentByte directContentUnder;
    
    /** The fonts of this document */
    protected HashMap documentFonts = new HashMap();
    
    /** The colors of this document */
    protected HashMap documentColors = new HashMap();
    
    /** The patterns of this document */
    protected HashMap documentPatterns = new HashMap();
    
    protected HashMap documentShadings = new HashMap();
    
    protected HashMap documentShadingPatterns = new HashMap();
    
    protected ColorDetails patternColorspaceRGB;
    protected ColorDetails patternColorspaceGRAY;
    protected ColorDetails patternColorspaceCMYK;
    protected HashMap documentSpotPatterns = new HashMap();
    
    protected HashMap documentExtGState = new HashMap();
    
    protected HashMap documentLayers = new HashMap();
    protected HashSet documentOCG = new HashSet();
    protected ArrayList documentOCGorder = new ArrayList();
    protected PdfOCProperties OCProperties;
    protected PdfArray OCGRadioGroup = new PdfArray();
    
    protected PdfDictionary defaultColorspace = new PdfDictionary();
    protected float userunit = 0f;
    
    /** PDF/X value */
    public static final int PDFXNONE = 0;
    /** PDF/X value */
    public static final int PDFX1A2001 = 1;
    /** PDF/X value */
    public static final int PDFX32002 = 2;

    private int pdfxConformance = PDFXNONE;
    
    static final int PDFXKEY_COLOR = 1;
    static final int PDFXKEY_CMYK = 2;
    static final int PDFXKEY_RGB = 3;
    static final int PDFXKEY_FONT = 4;
    static final int PDFXKEY_IMAGE = 5;
    static final int PDFXKEY_GSTATE = 6;
    static final int PDFXKEY_LAYER = 7;
    
    // membervariables
    
    /** body of the PDF document */
    protected PdfBody body;
    
    /** the pdfdocument object. */
    protected PdfDocument pdf;
    
    /** The <CODE>PdfPageEvent</CODE> for this document. */
    private PdfPageEvent pageEvent;
    
    protected PdfEncryption crypto;
    
    protected HashMap importedPages = new HashMap();
    
    protected PdfReaderInstance currentPdfReaderInstance;
    
    /** The PdfIndirectReference to the pages. */
    protected ArrayList pageReferences = new ArrayList();
    
    protected int currentPageNumber = 1;
    
    protected PdfDictionary group;
    
    /** The default space-char ratio. */    
    public static final float SPACE_CHAR_RATIO_DEFAULT = 2.5f;
    /** Disable the inter-character spacing. */    
    public static final float NO_SPACE_CHAR_RATIO = 10000000f;
    
    /** Use the default run direction. */    
    public static final int RUN_DIRECTION_DEFAULT = 0;
    /** Do not use bidirectional reordering. */    
    public static final int RUN_DIRECTION_NO_BIDI = 1;
    /** Use bidirectional reordering with left-to-right
     * preferential run direction.
     */    
    public static final int RUN_DIRECTION_LTR = 2;
    /** Use bidirectional reordering with right-to-left
     * preferential run direction.
     */    
    public static final int RUN_DIRECTION_RTL = 3;
    protected int runDirection = RUN_DIRECTION_NO_BIDI;
    /**
     * The ratio between the extra word spacing and the extra character spacing.
     * Extra word spacing will grow <CODE>ratio</CODE> times more than extra character spacing.
     */
    private float spaceCharRatio = SPACE_CHAR_RATIO_DEFAULT;
    
    /** Holds value of property extraCatalog. */
    private PdfDictionary extraCatalog;
    
    /**
     * Holds value of property fullCompression.
     */
    protected boolean fullCompression = false;
        
    // constructor
    
    protected PdfWriter() {
    }
    
    /**
     * Constructs a <CODE>PdfWriter</CODE>.
     * <P>
     * Remark: a PdfWriter can only be constructed by calling the method
     * <CODE>getInstance(Document document, OutputStream os)</CODE>.
     *
     * @param	document	The <CODE>PdfDocument</CODE> that has to be written
     * @param	os			The <CODE>OutputStream</CODE> the writer has to write to.
     */
    
    protected PdfWriter(PdfDocument document, OutputStream os) {
        super(document, os);
        pdf = document;
        directContent = new PdfContentByte(this);
        directContentUnder = new PdfContentByte(this);
    }
    
    // get an instance of the PdfWriter
    
    /**
     * Gets an instance of the <CODE>PdfWriter</CODE>.
     *
     * @param	document	The <CODE>Document</CODE> that has to be written
     * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
     * @return	a new <CODE>PdfWriter</CODE>
     *
     * @throws	DocumentException on error
     */
    
    public static PdfWriter getInstance(Document document, OutputStream os)
    throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener(pdf);
        PdfWriter writer = new PdfWriter(pdf, os);
        pdf.addWriter(writer);
        return writer;
    }
    
    /** Gets an instance of the <CODE>PdfWriter</CODE>.
     *
     * @return a new <CODE>PdfWriter</CODE>
     * @param document The <CODE>Document</CODE> that has to be written
     * @param os The <CODE>OutputStream</CODE> the writer has to write to.
     * @param listener A <CODE>DocListener</CODE> to pass to the PdfDocument.
     * @throws DocumentException on error
     */
    
    public static PdfWriter getInstance(Document document, OutputStream os, DocListener listener)
    throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        pdf.addDocListener(listener);
        document.addDocListener(pdf);
        PdfWriter writer = new PdfWriter(pdf, os);
        pdf.addWriter(writer);
        return writer;
    }
    
    // methods to write objects to the outputstream
    
    /**
     * Adds some <CODE>PdfContents</CODE> to this Writer.
     * <P>
     * The document has to be open before you can begin to add content
     * to the body of the document.
     *
     * @return a <CODE>PdfIndirectReference</CODE>
     * @param page the <CODE>PdfPage</CODE> to add
     * @param contents the <CODE>PdfContents</CODE> of the page
     * @throws PdfException on error
     */
    
    PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException {
        if (!open) {
            throw new PdfException("The document isn't open.");
        }
        PdfIndirectObject object;
        try {
            object = addToBody(contents);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        page.add(object.getIndirectReference());
        if (group != null) {
            page.put(PdfName.GROUP, group);
            group = null;
        }
        root.addPage(page);
        currentPageNumber++;
        return null;
    }
    
    /** Adds an image to the document but not to the page resources. It is used with
     * templates and <CODE>Document.add(Image)</CODE>.
     * @param image the <CODE>Image</CODE> to add
     * @return the name of the image added
     * @throws PdfException on error
     * @throws DocumentException on error
     */
    PdfName addDirectImageSimple(Image image) throws PdfException, DocumentException {
        PdfName name;
        // if the images is already added, just retrieve the name
        if (images.containsKey(image.getMySerialId())) {
            name = (PdfName) images.get(image.getMySerialId());
        }
        // if it's a new image, add it to the document
        else {
            if (image.isImgTemplate()) {
                name = new PdfName("img" + images.size());
                if (image.templateData() == null) {
                    if(image instanceof ImgWMF){
                        try {
                            ImgWMF wmf = (ImgWMF)image;
                            wmf.readWMF(getDirectContent().createTemplate(0, 0));
                        }
                        catch (Exception e) {
                            throw new DocumentException(e);
                        }
                    }else{
                        try {
                            ((ImgPostscript)image).readPostscript(getDirectContent().createTemplate(0, 0));
                        }
                        catch (Exception e) {
                            throw new DocumentException(e);
                        }
                        
                    }
                }
            }
            else {
                Image maskImage = image.getImageMask();
                PdfIndirectReference maskRef = null;
                if (maskImage != null) {
                    PdfName mname = (PdfName)images.get(maskImage.getMySerialId());
                    maskRef = getImageReference(mname);
                }
                PdfImage i = new PdfImage(image, "img" + images.size(), maskRef);
                if (image.hasICCProfile()) {
                    PdfICCBased icc = new PdfICCBased(image.getICCProfile());
                    PdfIndirectReference iccRef = add(icc);
                    PdfArray iccArray = new PdfArray();
                    iccArray.add(PdfName.ICCBASED);
                    iccArray.add(iccRef);
                    PdfObject colorspace = i.get(PdfName.COLORSPACE);
                    if (colorspace != null && colorspace.isArray()) {
                        ArrayList ar = ((PdfArray)colorspace).getArrayList();
                        if (ar.size() > 1 && PdfName.INDEXED.equals(ar.get(0)))
                            ar.set(1, iccArray);
                        else
                            i.put(PdfName.COLORSPACE, iccArray);
                    }
                    else
                        i.put(PdfName.COLORSPACE, iccArray);
                }
                add(i);
                name = i.name();
            }
            images.put(image.getMySerialId(), name);
        }
        return name;
    }

    /**
     * Writes a <CODE>PdfImage</CODE> to the outputstream.
     *
     * @param pdfImage the image to be added
     * @return a <CODE>PdfIndirectReference</CODE> to the encapsulated image
     * @throws PdfException when a document isn't open yet, or has been closed
     */
    
    PdfIndirectReference add(PdfImage pdfImage) throws PdfException {
        if (! imageDictionary.contains(pdfImage.name())) {
            checkPDFXConformance(this, PDFXKEY_IMAGE, pdfImage);
            PdfIndirectObject object;
            try {
                object = addToBody(pdfImage);
            }
            catch(IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            imageDictionary.put(pdfImage.name(), object.getIndirectReference());
            return object.getIndirectReference();
        }
        return (PdfIndirectReference) imageDictionary.get(pdfImage.name());
    }
    
    protected PdfIndirectReference add(PdfICCBased icc) throws PdfException {
        PdfIndirectObject object;
        try {
            object = addToBody(icc);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        return object.getIndirectReference();
    }
    
    /**
     * return the <CODE>PdfIndirectReference</CODE> to the image with a given name.
     *
     * @param name the name of the image
     * @return a <CODE>PdfIndirectReference</CODE>
     */
    
    PdfIndirectReference getImageReference(PdfName name) {
        return (PdfIndirectReference) imageDictionary.get(name);
    }
    
    // methods to open and close the writer
    
    /**
     * Signals that the <CODE>Document</CODE> has been opened and that
     * <CODE>Elements</CODE> can be added.
     * <P>
     * When this method is called, the PDF-document header is
     * written to the outputstream.
     */
    
    public void open() {
        super.open();
        try {
            os.write(HEADER);
            body = new PdfBody(this);
            if (pdfxConformance == PDFX32002) {
                PdfDictionary sec = new PdfDictionary();
                sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f,2.2f,2.2f}));
                sec.put(PdfName.MATRIX, new PdfArray(new float[]{0.4124f,0.2126f,0.0193f,0.3576f,0.7152f,0.1192f,0.1805f,0.0722f,0.9505f}));
                sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f,1f,1.089f}));
                PdfArray arr = new PdfArray(PdfName.CALRGB);
                arr.add(sec);
                setDefaultColorspace(PdfName.DEFAULTRGB, addToBody(arr).getIndirectReference());
            }
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    private static void getOCGOrder(PdfArray order, PdfLayer layer) {
        if (!layer.isOnPanel())
            return;
        if (layer.getTitle() == null)
            order.add(layer.getRef());
        ArrayList children = layer.getChildren();
        if (children == null)
            return;
        PdfArray kids = new PdfArray();
        if (layer.getTitle() != null)
            kids.add(new PdfString(layer.getTitle(), PdfObject.TEXT_UNICODE));
        for (int k = 0; k < children.size(); ++k) {
            getOCGOrder(kids, (PdfLayer)children.get(k));
        }
        if (kids.size() > 0)
            order.add(kids);
    }
    
    private void addASEvent(PdfName event, PdfName category) {
        PdfArray arr = new PdfArray();
        for (Iterator it = documentOCG.iterator(); it.hasNext();) {
            PdfLayer layer = (PdfLayer)it.next();
            PdfDictionary usage = (PdfDictionary)layer.get(PdfName.USAGE);
            if (usage != null && usage.get(category) != null)
                arr.add(layer.getRef());
        }
        if (arr.size() == 0)
            return;
        PdfDictionary d = (PdfDictionary)OCProperties.get(PdfName.D);
        PdfArray arras = (PdfArray)d.get(PdfName.AS);
        if (arras == null) {
            arras = new PdfArray();
            d.put(PdfName.AS, arras);
        }
        PdfDictionary as = new PdfDictionary();
        as.put(PdfName.EVENT, event);
        as.put(PdfName.CATEGORY, new PdfArray(category));
        as.put(PdfName.OCGS, arr);
        arras.add(as);
    }
    
    private void fillOCProperties(boolean erase) {
        if (OCProperties == null)
            OCProperties = new PdfOCProperties();
        if (erase) {
            OCProperties.remove(PdfName.OCGS);
            OCProperties.remove(PdfName.D);
        }
        if (OCProperties.get(PdfName.OCGS) == null) {
            PdfArray gr = new PdfArray();
            for (Iterator it = documentOCG.iterator(); it.hasNext();) {
                PdfLayer layer = (PdfLayer)it.next();
                gr.add(layer.getRef());
            }
            OCProperties.put(PdfName.OCGS, gr);
        }
        if (OCProperties.get(PdfName.D) != null)
            return;
        ArrayList docOrder = new ArrayList(documentOCGorder);
        for (Iterator it = docOrder.iterator(); it.hasNext();) {
            PdfLayer layer = (PdfLayer)it.next();
            if (layer.getParent() != null)
                it.remove();
        }
        PdfArray order = new PdfArray();
        for (Iterator it = docOrder.iterator(); it.hasNext();) {
            PdfLayer layer = (PdfLayer)it.next();
            getOCGOrder(order, layer);
        }
        PdfDictionary d = new PdfDictionary();
        OCProperties.put(PdfName.D, d);
        d.put(PdfName.ORDER, order);
        PdfArray gr = new PdfArray();
        for (Iterator it = documentOCG.iterator(); it.hasNext();) {
            PdfLayer layer = (PdfLayer)it.next();
            if (!layer.isOn())
                gr.add(layer.getRef());
        }
        if (gr.size() > 0)
            d.put(PdfName.OFF, gr);
        if (OCGRadioGroup.size() > 0)
            d.put(PdfName.RBGROUPS, OCGRadioGroup);
        addASEvent(PdfName.VIEW, PdfName.ZOOM);
        addASEvent(PdfName.VIEW, PdfName.VIEW);
        addASEvent(PdfName.PRINT, PdfName.PRINT);
        addASEvent(PdfName.EXPORT, PdfName.EXPORT);
        d.put(PdfName.LISTMODE, PdfName.VISIBLEPAGES);
    }
    
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj)
    {
        PdfDictionary catalog = ((PdfDocument)document).getCatalog(rootObj);
        if (documentOCG.size() == 0)
            return catalog;
        fillOCProperties(false);
        catalog.put(PdfName.OCPROPERTIES, OCProperties);
        return catalog;
    }

    protected void addSharedObjectsToBody() throws IOException {
        // add the fonts
        for (Iterator it = documentFonts.values().iterator(); it.hasNext();) {
            FontDetails details = (FontDetails)it.next();
            details.writeFont(this);
        }
        // add the form XObjects
        for (Iterator it = formXObjects.values().iterator(); it.hasNext();) {
            Object objs[] = (Object[])it.next();
            PdfTemplate template = (PdfTemplate)objs[1];
            if (template != null && template.getIndirectReference() instanceof PRIndirectReference)
                continue;
            if (template != null && template.getType() == PdfTemplate.TYPE_TEMPLATE) {
                PdfIndirectObject obj = addToBody(template.getFormXObject(), template.getIndirectReference());
            }
        }
        // add all the dependencies in the imported pages
        for (Iterator it = importedPages.values().iterator(); it.hasNext();) {
            currentPdfReaderInstance = (PdfReaderInstance)it.next();
            currentPdfReaderInstance.writeAllPages();
        }
        currentPdfReaderInstance = null;
        // add the color
        for (Iterator it = documentColors.values().iterator(); it.hasNext();) {
            ColorDetails color = (ColorDetails)it.next();
            PdfIndirectObject cobj = addToBody(color.getSpotColor(this), color.getIndirectReference());
        }
        // add the pattern
        for (Iterator it = documentPatterns.keySet().iterator(); it.hasNext();) {
            PdfPatternPainter pat = (PdfPatternPainter)it.next();
            PdfIndirectObject pobj = addToBody(pat.getPattern(), pat.getIndirectReference());
        }
        // add the shading patterns
        for (Iterator it = documentShadingPatterns.keySet().iterator(); it.hasNext();) {
            PdfShadingPattern shadingPattern = (PdfShadingPattern)it.next();
            shadingPattern.addToBody();
        }
        // add the shadings
        for (Iterator it = documentShadings.keySet().iterator(); it.hasNext();) {
            PdfShading shading = (PdfShading)it.next();
            shading.addToBody();
        }
        // add the extgstate
        for (Iterator it = documentExtGState.keySet().iterator(); it.hasNext();) {
            PdfDictionary gstate = (PdfDictionary)it.next();
            PdfObject obj[] = (PdfObject[])documentExtGState.get(gstate);
            addToBody(gstate, (PdfIndirectReference)obj[1]);
        }
        // add the layers
        for (Iterator it = documentLayers.keySet().iterator(); it.hasNext();) {
            PdfOCG layer = (PdfOCG)it.next();
            if (layer instanceof PdfLayerMembership)
                addToBody(layer.getPdfObject(), layer.getRef());
        }
        for (Iterator it = documentOCG.iterator(); it.hasNext();) {
            PdfOCG layer = (PdfOCG)it.next();
            addToBody(layer.getPdfObject(), layer.getRef());
        }
    }
    
    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <P>
     * The pages-tree is built and written to the outputstream.
     * A Catalog is constructed, as well as an Info-object,
     * the referencetable is composed and everything is written
     * to the outputstream embedded in a Trailer.
     */
    
    public synchronized void close() {
        if (open) {
            if ((currentPageNumber - 1) != pageReferences.size())
                throw new RuntimeException("The page " + pageReferences.size() +
                " was requested but the document has only " + (currentPageNumber - 1) + " pages.");
            pdf.close();
            try {
                addSharedObjectsToBody();
                // add the root to the body
                PdfIndirectReference rootRef = root.writePageTree();
                // make the catalog-object and add it to the body
                PdfDictionary catalog = getCatalog(rootRef);
                // make pdfx conformant
                PdfDictionary info = getInfo();
                if (pdfxConformance != PDFXNONE) {
                    if (info.get(PdfName.GTS_PDFXVERSION) == null) {
                        if (pdfxConformance == PDFX1A2001) {
                            info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-1:2001"));
                            info.put(new PdfName("GTS_PDFXConformance"), new PdfString("PDF/X-1a:2001"));
                        }
                        else if (pdfxConformance == PDFX32002)
                            info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-3:2002"));
                    }
                    if (info.get(PdfName.TITLE) == null) {
                        info.put(PdfName.TITLE, new PdfString("Pdf document"));
                    }
                    if (info.get(PdfName.CREATOR) == null) {
                        info.put(PdfName.CREATOR, new PdfString("Unknown"));
                    }
                    if (info.get(PdfName.TRAPPED) == null) {
                        info.put(PdfName.TRAPPED, new PdfName("False"));
                    }
                    getExtraCatalog();
                    if (extraCatalog.get(PdfName.OUTPUTINTENTS) == null) {
                        PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT);
                        out.put(PdfName.OUTPUTCONDITION, new PdfString("SWOP CGATS TR 001-1995"));
                        out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("CGATS TR 001"));
                        out.put(PdfName.REGISTRYNAME, new PdfString("http://www.color.org"));
                        out.put(PdfName.INFO, new PdfString(""));
                        out.put(PdfName.S, PdfName.GTS_PDFX);
                        extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
                    }
                }
                if (extraCatalog != null) {
                    catalog.mergeDifferent(extraCatalog);
                }
                PdfIndirectObject indirectCatalog = addToBody(catalog, false);
                // add the info-object to the body
                PdfIndirectObject infoObj = addToBody(info, false);
                PdfIndirectReference encryption = null;
                PdfObject fileID = null;
                body.flushObjStm();
                if (crypto != null) {
                    PdfIndirectObject encryptionObject = addToBody(crypto.getEncryptionDictionary(), false);
                    encryption = encryptionObject.getIndirectReference();
                    fileID = crypto.getFileID();
                }
                else
                    fileID = PdfEncryption.createInfoId(PdfEncryption.createDocumentId());
                
                // write the cross-reference table of the body
                body.writeCrossReferenceTable(os, indirectCatalog.getIndirectReference(),
                    infoObj.getIndirectReference(), encryption,  fileID, prevxref);

                // make the trailer
                if (fullCompression) {
                    os.write(getISOBytes("startxref\n"));
                    os.write(getISOBytes(String.valueOf(body.offset())));
                    os.write(getISOBytes("\n%%EOF\n"));
                }
                else {
                    PdfTrailer trailer = new PdfTrailer(body.size(),
                    body.offset(),
                    indirectCatalog.getIndirectReference(),
                    infoObj.getIndirectReference(),
                    encryption,
                    fileID, prevxref);
                    trailer.toPdf(this, os);
                }
                super.close();
            }
            catch(IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
    }
    
    // methods
    
    /**
     * Sometimes it is necessary to know where the just added <CODE>Table</CODE> ends.
     *
     * For instance to avoid to add another table in a page that is ending up, because
     * the new table will be probably splitted just after the header (it is an
     * unpleasant effect, isn't it?).
     *
     * Added on September 8th, 2001
     * by Francesco De Milato
     * francesco.demilato@tiscalinet.it
     * @param table the <CODE>Table</CODE>
     * @return the bottom height of the just added table
     */
    
    public float getTableBottom(Table table) {
        return pdf.bottom(table) - pdf.indentBottom();
    }
    
    /**
	 * Gets a pre-rendered table.
	 * (Contributed by dperezcar@fcc.es) 
	 * @param table		Contains the table definition.  Its contents are deleted, after being pre-rendered.
     * @return a PdfTable
	 */
	
	public PdfTable getPdfTable(Table table) {
		return pdf.getPdfTable(table, true);
	}

	/**
	 * Row additions to the original {@link Table} used to build the {@link PdfTable} are processed and pre-rendered,
	 * and then the contents are deleted. 
	 * If the pre-rendered table doesn't fit, then it is fully rendered and its data discarded.  
	 * There shouldn't be any column change in the underlying {@link Table} object.
	 * (Contributed by dperezcar@fcc.es) 
	 *
	 * @param	table		The pre-rendered table obtained from {@link #getPdfTable(Table)} 
	 * @return	true if the table is rendered and emptied.
	 * @throws DocumentException
	 * @see #getPdfTable(Table)
	 */
	
	public boolean breakTableIfDoesntFit(PdfTable table) throws DocumentException {
		return pdf.breakTableIfDoesntFit(table);
	}
    
    /**
     * Checks if a <CODE>Table</CODE> fits the current page of the <CODE>PdfDocument</CODE>.
     *
     * @param	table	the table that has to be checked
     * @param	margin	a certain margin
     * @return	<CODE>true</CODE> if the <CODE>Table</CODE> fits the page, <CODE>false</CODE> otherwise.
     */
    
    public boolean fitsPage(Table table, float margin) {
        return pdf.bottom(table) > pdf.indentBottom() + margin;
    }
    
    /**
     * Checks if a <CODE>Table</CODE> fits the current page of the <CODE>PdfDocument</CODE>.
     *
     * @param	table	the table that has to be checked
     * @return	<CODE>true</CODE> if the <CODE>Table</CODE> fits the page, <CODE>false</CODE> otherwise.
     */
    
    public boolean fitsPage(Table table) {
        return fitsPage(table, 0);
    }
    
    /**
     * Checks if a <CODE>PdfPTable</CODE> fits the current page of the <CODE>PdfDocument</CODE>.
     *
     * @param	table	the table that has to be checked
     * @param	margin	a certain margin
     * @return	<CODE>true</CODE> if the <CODE>PdfPTable</CODE> fits the page, <CODE>false</CODE> otherwise.
     */
    public boolean fitsPage(PdfPTable table, float margin) {
        return pdf.fitsPage(table, margin);
    }
    
    /**
     * Checks if a <CODE>PdfPTable</CODE> fits the current page of the <CODE>PdfDocument</CODE>.
     *
     * @param	table	the table that has to be checked
     * @return	<CODE>true</CODE> if the <CODE>PdfPTable</CODE> fits the page, <CODE>false</CODE> otherwise.
     */
    public boolean fitsPage(PdfPTable table) {
        return pdf.fitsPage(table, 0);
    }
    
    /**
     * Gets the current vertical page position.
     * @param ensureNewLine Tells whether a new line shall be enforced. This may cause side effects 
     *   for elements that do not terminate the lines they've started because those lines will get
     *   terminated. 
     * @return The current vertical page position.
     */
    public float getVerticalPosition(boolean ensureNewLine) {
        return pdf.getVerticalPosition(ensureNewLine);
    }
    
    /**
     * Checks if writing is paused.
     *
     * @return		<CODE>true</CODE> if writing temporarely has to be paused, <CODE>false</CODE> otherwise.
     */
    
    boolean isPaused() {
        return pause;
    }
    
    /**
     * Gets the direct content for this document. There is only one direct content,
     * multiple calls to this method will allways retrieve the same.
     * @return the direct content
     */
    
    public PdfContentByte getDirectContent() {
        if (!open)
            throw new RuntimeException("The document is not open.");
        return directContent;
    }
    
    /**
     * Gets the direct content under for this document. There is only one direct content,
     * multiple calls to this method will allways retrieve the same.
     * @return the direct content
     */
    
    public PdfContentByte getDirectContentUnder() {
        if (!open)
            throw new RuntimeException("The document is not open.");
        return directContentUnder;
    }
    
    /**
     * Resets all the direct contents to empty. This happens when a new page is started.
     */
    
    void resetContent() {
        directContent.reset();
        directContentUnder.reset();
    }
    
    /** Gets the AcroForm object.
     * @return the <CODE>PdfAcroForm</CODE>
     */
    
    public PdfAcroForm getAcroForm() {
        return pdf.getAcroForm();
    }
    
    /** Gets the root outline.
     * @return the root outline
     */
    
    public PdfOutline getRootOutline() {
        return directContent.getRootOutline();
    }
    
    /**
     * Returns the outputStreamCounter.
     * @return the outputStreamCounter
     */
    public OutputStreamCounter getOs() {
        return os;
    }
        
    /**
     * Adds a <CODE>BaseFont</CODE> to the document but not to the page resources.
     * It is used for templates.
     * @param bf the <CODE>BaseFont</CODE> to add
     * @return an <CODE>Object[]</CODE> where position 0 is a <CODE>PdfName</CODE>
     * and position 1 is an <CODE>PdfIndirectReference</CODE>
     */
    
    FontDetails addSimple(BaseFont bf) {
        if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
            return new FontDetails(new PdfName("F" + (fontNumber++)), ((DocumentFont)bf).getIndirectReference(), bf);
        }
        FontDetails ret = (FontDetails)documentFonts.get(bf);
        if (ret == null) {
            checkPDFXConformance(this, PDFXKEY_FONT, bf);
            ret = new FontDetails(new PdfName("F" + (fontNumber++)), body.getPdfIndirectReference(), bf);
            documentFonts.put(bf, ret);
        }
        return ret;
    }
    
    void eliminateFontSubset(PdfDictionary fonts) {
        for (Iterator it = documentFonts.values().iterator(); it.hasNext();) {
            FontDetails ft = (FontDetails)it.next();
            if (fonts.get(ft.getFontName()) != null)
                ft.setSubset(false);
        }
    }
    
    /**
     * Adds a <CODE>SpotColor</CODE> to the document but not to the page resources.
     * @param spc the <CODE>SpotColor</CODE> to add
     * @return an <CODE>Object[]</CODE> where position 0 is a <CODE>PdfName</CODE>
     * and position 1 is an <CODE>PdfIndirectReference</CODE>
     */
    
    ColorDetails addSimple(PdfSpotColor spc) {
        ColorDetails ret = (ColorDetails)documentColors.get(spc);
        if (ret == null) {
            ret = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), spc);
            documentColors.put(spc, ret);
        }
        return ret;
    }
    
    ColorDetails addSimplePatternColorspace(Color color) {
        int type = ExtendedColor.getType(color);
        if (type == ExtendedColor.TYPE_PATTERN || type == ExtendedColor.TYPE_SHADING)
            throw new RuntimeException("An uncolored tile pattern can not have another pattern or shading as color.");
        try {
            switch (type) {
                case ExtendedColor.TYPE_RGB:
                    if (patternColorspaceRGB == null) {
                        patternColorspaceRGB = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICERGB);
                        PdfIndirectObject cobj = addToBody(array, patternColorspaceRGB.getIndirectReference());
                    }
                    return patternColorspaceRGB;
                case ExtendedColor.TYPE_CMYK:
                    if (patternColorspaceCMYK == null) {
                        patternColorspaceCMYK = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICECMYK);
                        PdfIndirectObject cobj = addToBody(array, patternColorspaceCMYK.getIndirectReference());
                    }
                    return patternColorspaceCMYK;
                case ExtendedColor.TYPE_GRAY:
                    if (patternColorspaceGRAY == null) {
                        patternColorspaceGRAY = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICEGRAY);
                        PdfIndirectObject cobj = addToBody(array, patternColorspaceGRAY.getIndirectReference());
                    }
                    return patternColorspaceGRAY;
                case ExtendedColor.TYPE_SEPARATION: {
                    ColorDetails details = addSimple(((SpotColor)color).getPdfSpotColor());
                    ColorDetails patternDetails = (ColorDetails)documentSpotPatterns.get(details);
                    if (patternDetails == null) {
                        patternDetails = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(details.getIndirectReference());
                        PdfIndirectObject cobj = addToBody(array, patternDetails.getIndirectReference());
                        documentSpotPatterns.put(details, patternDetails);
                    }
                    return patternDetails;
                }
                default:
                    throw new RuntimeException("Invalid color type in PdfWriter.addSimplePatternColorspace().");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    void addSimpleShadingPattern(PdfShadingPattern shading) {
        if (!documentShadingPatterns.containsKey(shading)) {
            shading.setName(patternNumber);
            ++patternNumber;
            documentShadingPatterns.put(shading, null);
            addSimpleShading(shading.getShading());
        }
    }
    
    void addSimpleShading(PdfShading shading) {
        if (!documentShadings.containsKey(shading)) {
            documentShadings.put(shading, null);
            shading.setName(documentShadings.size());
        }
    }
    
    PdfObject[] addSimpleExtGState(PdfDictionary gstate) {
        if (!documentExtGState.containsKey(gstate)) {
            checkPDFXConformance(this, PDFXKEY_GSTATE, gstate);
            documentExtGState.put(gstate, new PdfObject[]{new PdfName("GS" + (documentExtGState.size() + 1)), getPdfIndirectReference()});
        }
        return (PdfObject[])documentExtGState.get(gstate);
    }
    
    void registerLayer(PdfOCG layer) {
        checkPDFXConformance(this, PDFXKEY_LAYER, null);
        if (layer instanceof PdfLayer) {
            PdfLayer la = (PdfLayer)layer;
            if (la.getTitle() == null) {
                if (!documentOCG.contains(layer)) {
                    documentOCG.add(layer);
                    documentOCGorder.add(layer);
                }
            }
            else {
                documentOCGorder.add(layer);
            }
        }
        else
            throw new IllegalArgumentException("Only PdfLayer is accepted.");
    }
    
    PdfName addSimpleLayer(PdfOCG layer) {
        if (!documentLayers.containsKey(layer)) {
            checkPDFXConformance(this, PDFXKEY_LAYER, null);
            documentLayers.put(layer, new PdfName("OC" + (documentLayers.size() + 1)));
        }
        return (PdfName)documentLayers.get(layer);
    }
    
    /**
     * Gets the <CODE>PdfDocument</CODE> associated with this writer.
     * @return the <CODE>PdfDocument</CODE>
     */
    
    PdfDocument getPdfDocument() {
        return pdf;
    }
    
    /**
     * Gets a <CODE>PdfIndirectReference</CODE> for an object that
     * will be created in the future.
     * @return the <CODE>PdfIndirectReference</CODE>
     */
    
    public PdfIndirectReference getPdfIndirectReference() {
        return body.getPdfIndirectReference();
    }
    
    int getIndirectReferenceNumber() {
        return body.getIndirectReferenceNumber();
    }
    
    PdfName addSimplePattern(PdfPatternPainter painter) {
        PdfName name = (PdfName)documentPatterns.get(painter);
        try {
            if ( name == null ) {
                name = new PdfName("P" + patternNumber);
                ++patternNumber;
                documentPatterns.put(painter, name);
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        return name;
    }
    
    /**
     * Adds a template to the document but not to the page resources.
     * @param template the template to add
     * @param forcedName the template name, rather than a generated one. Can be null
     * @return the <CODE>PdfName</CODE> for this template
     */
    
    PdfName addDirectTemplateSimple(PdfTemplate template, PdfName forcedName) {
        PdfIndirectReference ref = template.getIndirectReference();
        Object obj[] = (Object[])formXObjects.get(ref);
        PdfName name = null;
        try {
            if (obj == null) {
                if (forcedName == null) {
                    name = new PdfName("Xf" + formXObjectsCounter);
                    ++formXObjectsCounter;
                }
                else
                    name = forcedName;
                if (template.getType() == PdfTemplate.TYPE_IMPORTED)
                    template = null;
                formXObjects.put(ref, new Object[]{name, template});
            }
            else
                name = (PdfName)obj[0];
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        return name;
    }
    
    /**
     * Sets the <CODE>PdfPageEvent</CODE> for this document.
     * @param pageEvent the <CODE>PdfPageEvent</CODE> for this document
     */
    
    public void setPageEvent(PdfPageEvent pageEvent) {
        this.pageEvent = pageEvent;
    }
    
    /**
     * Gets the <CODE>PdfPageEvent</CODE> for this document or <CODE>null</CODE>
     * if none is set.
     * @return the <CODE>PdfPageEvent</CODE> for this document or <CODE>null</CODE>
     * if none is set
     */
    
    public PdfPageEvent getPageEvent() {
        return pageEvent;
    }
    
    /**
     * Adds the local destinations to the body of the document.
     * @param dest the <CODE>HashMap</CODE> containing the destinations
     * @throws IOException on error
     */
    
    void addLocalDestinations(TreeMap dest) throws IOException {
        for (Iterator i = dest.keySet().iterator(); i.hasNext();) {
            String name = (String)i.next();
            Object obj[] = (Object[])dest.get(name);
            PdfDestination destination = (PdfDestination)obj[2];
            if (destination == null)
                throw new RuntimeException("The name '" + name + "' has no local destination.");
            if (obj[1] == null)
                obj[1] = getPdfIndirectReference();
            PdfIndirectObject iob = addToBody(destination, (PdfIndirectReference)obj[1]);
        }
    }
    
    /**
     * Gets the current pagenumber of this document.
     *
     * @return a page number
     */
    
    public int getPageNumber() {
        return pdf.getPageNumber();
    }
    
    /**
     * Sets the viewer preferences by ORing some constants.
     * <p>
     * <ul>
     * <li>The page layout to be used when the document is opened (choose one).
     *   <ul>
     *   <li><b>PageLayoutSinglePage</b> - Display one page at a time. (default)
     *   <li><b>PageLayoutOneColumn</b> - Display the pages in one column.
     *   <li><b>PageLayoutTwoColumnLeft</b> - Display the pages in two columns, with
     *       oddnumbered pages on the left.
     *   <li><b>PageLayoutTwoColumnRight</b> - Display the pages in two columns, with
     *       oddnumbered pages on the right.
     *   </ul>
     * <li>The page mode how the document should be displayed
     *     when opened (choose one).
     *   <ul>
     *   <li><b>PageModeUseNone</b> - Neither document outline nor thumbnail images visible. (default)
     *   <li><b>PageModeUseOutlines</b> - Document outline visible.
     *   <li><b>PageModeUseThumbs</b> - Thumbnail images visible.
     *   <li><b>PageModeFullScreen</b> - Full-screen mode, with no menu bar, window
     *       controls, or any other window visible.
     *   <li><b>PageModeUseOC</b> - Optional content group panel visible
     *   </ul>
     * <li><b>HideToolbar</b> - A flag specifying whether to hide the viewer application's tool
     *     bars when the document is active.
     * <li><b>HideMenubar</b> - A flag specifying whether to hide the viewer application's
     *     menu bar when the document is active.
     * <li><b>HideWindowUI</b> - A flag specifying whether to hide user interface elements in
     *     the document's window (such as scroll bars and navigation controls),
     *     leaving only the document's contents displayed.
     * <li><b>FitWindow</b> - A flag specifying whether to resize the document's window to
     *     fit the size of the first displayed page.
     * <li><b>CenterWindow</b> - A flag specifying whether to position the document's window
     *     in the center of the screen.
     * <li><b>DisplayDocTitle</b> - A flag specifying whether to display the document's title
     *     in the top bar.
     * <li>The predominant reading order for text. This entry has no direct effect on the
     *     document's contents or page numbering, but can be used to determine the relative
     *     positioning of pages when displayed side by side or printed <i>n-up</i> (choose one).
     *   <ul>
     *   <li><b>DirectionL2R</b> - Left to right
     *   <li><b>DirectionR2L</b> - Right to left (including vertical writing systems such as
     *       Chinese, Japanese, and Korean)
     *   </ul>
     * <li>The document's page mode, specifying how to display the
     *     document on exiting full-screen mode. It is meaningful only
     *     if the page mode is <b>PageModeFullScreen</b> (choose one).
     *   <ul>
     *   <li><b>NonFullScreenPageModeUseNone</b> - Neither document outline nor thumbnail images
     *       visible
     *   <li><b>NonFullScreenPageModeUseOutlines</b> - Document outline visible
     *   <li><b>NonFullScreenPageModeUseThumbs</b> - Thumbnail images visible
     *   <li><b>NonFullScreenPageModeUseOC</b> - Optional content group panel visible
     *   </ul>
     * <li><b>PrintScalingNone</b> - Indicates that the print dialog should reflect no page scaling.
     * </ul>
     * @param preferences the viewer preferences
     */
    
    public void setViewerPreferences(int preferences) {
        pdf.setViewerPreferences(preferences);
    }
    
    /** Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param strength128Bits <code>true</code> for 128 bit key length, <code>false</code> for 40 bit key length
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) throws DocumentException {
        if (pdf.isOpen())
            throw new DocumentException("Encryption can only be added before opening the document.");
        crypto = new PdfEncryption();
        crypto.setupAllKeys(userPassword, ownerPassword, permissions, strength128Bits);
    }
    
    /**
     * Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param strength <code>true</code> for 128 bit key length, <code>false</code> for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(getISOBytes(userPassword), getISOBytes(ownerPassword), permissions, strength);
    }
    
    /**
     * Adds an object to the PDF body.
     * @param object
     * @return a PdfIndirectObject
     * @throws IOException
     */
    public PdfIndirectObject addToBody(PdfObject object) throws IOException {
        PdfIndirectObject iobj = body.add(object);
        return iobj;
    }
    
    /**
     * Adds an object to the PDF body.
     * @param object
     * @param inObjStm
     * @return a PdfIndirectObject
     * @throws IOException
     */
    public PdfIndirectObject addToBody(PdfObject object, boolean inObjStm) throws IOException {
        PdfIndirectObject iobj = body.add(object, inObjStm);
        return iobj;
    }
    
    /**
     * Adds an object to the PDF body.
     * @param object
     * @param ref
     * @return a PdfIndirectObject
     * @throws IOException
     */
    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
        PdfIndirectObject iobj = body.add(object, ref);
        return iobj;
    }
    
    /**
     * Adds an object to the PDF body.
     * @param object
     * @param ref
     * @param inObjStm
     * @return a PdfIndirectObject
     * @throws IOException
     */
    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException {
        PdfIndirectObject iobj = body.add(object, ref, inObjStm);
        return iobj;
    }
    
    /**
     * Adds an object to the PDF body.
     * @param object
     * @param refNumber
     * @return a PdfIndirectObject
     * @throws IOException
     */
    public PdfIndirectObject addToBody(PdfObject object, int refNumber) throws IOException {
        PdfIndirectObject iobj = body.add(object, refNumber);
        return iobj;
    }
    
    /**
     * Adds an object to the PDF body.
     * @param object
     * @param refNumber
     * @param inObjStm
     * @return a PdfIndirectObject
     * @throws IOException
     */
    public PdfIndirectObject addToBody(PdfObject object, int refNumber, boolean inObjStm) throws IOException {
        PdfIndirectObject iobj = body.add(object, refNumber, inObjStm);
        return iobj;
    }
    
    /** When the document opens it will jump to the destination with
     * this name.
     * @param name the name of the destination to jump to
     */
    public void setOpenAction(String name) {
        pdf.setOpenAction(name);
    }
    
    /** Additional-actions defining the actions to be taken in
     * response to various trigger events affecting the document
     * as a whole. The actions types allowed are: <CODE>DOCUMENT_CLOSE</CODE>,
     * <CODE>WILL_SAVE</CODE>, <CODE>DID_SAVE</CODE>, <CODE>WILL_PRINT</CODE>
     * and <CODE>DID_PRINT</CODE>.
     *
     * @param actionType the action type
     * @param action the action to execute in response to the trigger
     * @throws PdfException on invalid action type
     */
    public void setAdditionalAction(PdfName actionType, PdfAction action) throws PdfException {
        if (!(actionType.equals(DOCUMENT_CLOSE) ||
        actionType.equals(WILL_SAVE) ||
        actionType.equals(DID_SAVE) ||
        actionType.equals(WILL_PRINT) ||
        actionType.equals(DID_PRINT))) {
            throw new PdfException("Invalid additional action type: " + actionType.toString());
        }
        pdf.addAdditionalAction(actionType, action);
    }
    
    /** When the document opens this <CODE>action</CODE> will be
     * invoked.
     * @param action the action to be invoked
     */
    public void setOpenAction(PdfAction action) {
        pdf.setOpenAction(action);
    }
    
    /** Sets the page labels
     * @param pageLabels the page labels
     */
    public void setPageLabels(PdfPageLabels pageLabels) {
        pdf.setPageLabels(pageLabels);
    }
    
    PdfEncryption getEncryption() {
        return crypto;
    }
    
    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        return currentPdfReaderInstance.getReaderFile();
    }
    
    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        return currentPdfReaderInstance.getNewObjectNumber(number, generation);
    }
    
    /** Gets a page from other PDF document. The page can be used as
     * any other PdfTemplate. Note that calling this method more than
     * once with the same parameters will retrieve the same object.
     * @param reader the PDF document where the page is
     * @param pageNumber the page number. The first page is 1
     * @return the template representing the imported page
     */
    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        PdfReaderInstance inst = (PdfReaderInstance)importedPages.get(reader);
        if (inst == null) {
            inst = reader.getPdfReaderInstance(this);
            importedPages.put(reader, inst);
        }
        return inst.getImportedPage(pageNumber);
    }
    
    /** Adds a JavaScript action at the document level. When the document
     * opens all this JavaScript runs.
     * @param js The JavaScrip action
     */
    public void addJavaScript(PdfAction js) {
        pdf.addJavaScript(js);
    }
    
    /** Adds a JavaScript action at the document level. When the document
     * opens all this JavaScript runs.
     * @param code the JavaScript code
     * @param unicode select JavaScript unicode. Note that the internal
     * Acrobat JavaScript engine does not support unicode,
     * so this may or may not work for you
     */
    public void addJavaScript(String code, boolean unicode) {
        addJavaScript(PdfAction.javaScript(code, this, unicode));
    }
    
    /** Adds a JavaScript action at the document level. When the document
     * opens all this JavaScript runs.
     * @param code the JavaScript code
     */
    public void addJavaScript(String code) {
        addJavaScript(code, false);
    }
    
    /** Sets the crop box. The crop box should not be rotated even if the
     * page is rotated. This change only takes effect in the next
     * page.
     * @param crop the crop box
     */
    public void setCropBoxSize(Rectangle crop) {
        pdf.setCropBoxSize(crop);
    }
    
    /** Gets a reference to a page existing or not. If the page does not exist
     * yet the reference will be created in advance. If on closing the document, a
     * page number greater than the total number of pages was requested, an
     * exception is thrown.
     * @param page the page number. The first page is 1
     * @return the reference to the page
     */
    public PdfIndirectReference getPageReference(int page) {
        --page;
        if (page < 0)
            throw new IndexOutOfBoundsException("The page numbers start at 1.");
        PdfIndirectReference ref;
        if (page < pageReferences.size()) {
            ref = (PdfIndirectReference)pageReferences.get(page);
            if (ref == null) {
                ref = body.getPdfIndirectReference();
                pageReferences.set(page, ref);
            }
        }
        else {
            int empty = page - pageReferences.size();
            for (int k = 0; k < empty; ++k)
                pageReferences.add(null);
            ref = body.getPdfIndirectReference();
            pageReferences.add(ref);
        }
        return ref;
    }
    
    PdfIndirectReference getCurrentPage() {
        return getPageReference(currentPageNumber);
    }
    
    int getCurrentPageNumber() {
        return currentPageNumber;
    }
    
    /** Adds the <CODE>PdfAnnotation</CODE> to the calculation order
     * array.
     * @param annot the <CODE>PdfAnnotation</CODE> to be added
     */
    public void addCalculationOrder(PdfFormField annot) {
        pdf.addCalculationOrder(annot);
    }
    
    /** Set the signature flags.
     * @param f the flags. This flags are ORed with current ones
     */
    public void setSigFlags(int f) {
        pdf.setSigFlags(f);
    }
    
    /** Adds a <CODE>PdfAnnotation</CODE> or a <CODE>PdfFormField</CODE>
     * to the document. Only the top parent of a <CODE>PdfFormField</CODE>
     * needs to be added.
     * @param annot the <CODE>PdfAnnotation</CODE> or the <CODE>PdfFormField</CODE> to add
     */
    public void addAnnotation(PdfAnnotation annot) {
        pdf.addAnnotation(annot);
    }
    
    void addAnnotation(PdfAnnotation annot, int page) {
        addAnnotation(annot);
    }
    
    /** Sets the PDF version. Must be used right before the document
     * is opened. Valid options are VERSION_1_2, VERSION_1_3,
     * VERSION_1_4, VERSION_1_5 and VERSION_1_6. VERSION_1_4 is the default.
     * @param version the version number
     */
    public void setPdfVersion(char version) {
        if (HEADER.length > VPOINT)
            HEADER[VPOINT] = (byte)version;
    }
    
    /** Reorder the pages in the document. A <CODE>null</CODE> argument value
     * only returns the number of pages to process. It is
     * advisable to issue a <CODE>Document.newPage()</CODE>
     * before using this method.
     * @return the total number of pages
     * @param order an array with the new page sequence. It must have the
     * same size as the number of pages.
     * @throws DocumentException if all the pages are not present in the array
     */
    public int reorderPages(int order[]) throws DocumentException {
        return root.reorderPages(order);
    }
    
    /** Gets the space/character extra spacing ratio for
     * fully justified text.
     * @return the space/character extra spacing ratio
     */
    public float getSpaceCharRatio() {
        return spaceCharRatio;
    }
    
    /** Sets the ratio between the extra word spacing and the extra character spacing
     * when the text is fully justified.
     * Extra word spacing will grow <CODE>spaceCharRatio</CODE> times more than extra character spacing.
     * If the ratio is <CODE>PdfWriter.NO_SPACE_CHAR_RATIO</CODE> then the extra character spacing
     * will be zero.
     * @param spaceCharRatio the ratio between the extra word spacing and the extra character spacing
     */
    public void setSpaceCharRatio(float spaceCharRatio) {
        if (spaceCharRatio < 0.001f)
            this.spaceCharRatio = 0.001f;
        else
            this.spaceCharRatio = spaceCharRatio;
    }
    
    /** Sets the run direction. This is only used as a placeholder
     * as it does not affect anything.
     * @param runDirection the run direction
     */    
    public void setRunDirection(int runDirection) {
        if (runDirection < RUN_DIRECTION_NO_BIDI || runDirection > RUN_DIRECTION_RTL)
            throw new RuntimeException("Invalid run direction: " + runDirection);
        this.runDirection = runDirection;
    }
    
    /** Gets the run direction.
     * @return the run direction
     */    
    public int getRunDirection() {
        return runDirection;
    }

    /**
     * Sets the display duration for the page (for presentations)
     * @param seconds   the number of seconds to display the page
     */
    public void setDuration(int seconds) {
        pdf.setDuration(seconds);
    }
    
    /**
     * Sets the transition for the page
     * @param transition   the Transition object
     */
    public void setTransition(PdfTransition transition) {
        pdf.setTransition(transition);
    }
    
    /** Writes the reader to the document and frees the memory used by it.
     * The main use is when concatenating multiple documents to keep the
     * memory usage restricted to the current appending document.
     * @param reader the <CODE>PdfReader</CODE> to free
     * @throws IOException on error
     */    
    public void freeReader(PdfReader reader) throws IOException {
        currentPdfReaderInstance = (PdfReaderInstance)importedPages.get(reader);
        if (currentPdfReaderInstance == null)
            return;
        currentPdfReaderInstance.writeAllPages();
        currentPdfReaderInstance = null;
        importedPages.remove(reader);
    }
    
    /** Sets the open and close page additional action.
     * @param actionType the action type. It can be <CODE>PdfWriter.PAGE_OPEN</CODE>
     * or <CODE>PdfWriter.PAGE_CLOSE</CODE>
     * @param action the action to perform
     * @throws PdfException if the action type is invalid
     */    
    public void setPageAction(PdfName actionType, PdfAction action) throws PdfException {
        if (!actionType.equals(PAGE_OPEN) && !actionType.equals(PAGE_CLOSE))
            throw new PdfException("Invalid page additional action type: " + actionType.toString());
        pdf.setPageAction(actionType, action);
    }
    
    /** Gets the current document size. This size only includes
     * the data already writen to the output stream, it does not
     * include templates or fonts. It is usefull if used with
     * <CODE>freeReader()</CODE> when concatenating many documents
     * and an idea of the current size is needed.
     * @return the approximate size without fonts or templates
     */    
    public int getCurrentDocumentSize() {
        return body.offset() + body.size() * 20 + 0x48;
    }
    
    /** Getter for property strictImageSequence.
     * @return value of property strictImageSequence
     *
     */
    public boolean isStrictImageSequence() {
        return pdf.isStrictImageSequence();
    }
    
    /** Sets the image sequence to follow the text in strict order.
     * @param strictImageSequence new value of property strictImageSequence
     *
     */
    public void setStrictImageSequence(boolean strictImageSequence) {
        pdf.setStrictImageSequence(strictImageSequence);
    }
    
    /**
     * If you use setPageEmpty(false), invoking newPage() after a blank page will add a newPage.
     * @param pageEmpty the state
     */
    public void setPageEmpty(boolean pageEmpty) {
        pdf.setPageEmpty(pageEmpty);
    }

    /** Gets the info dictionary for changing.
     * @return the info dictionary
     */    
    public PdfDictionary getInfo() {
        return ((PdfDocument)document).getInfo();
    }
    
    /**
     * Sets extra keys to the catalog.
     * @return the catalog to change
     */    
    public PdfDictionary getExtraCatalog() {
        if (extraCatalog == null)
            extraCatalog = new PdfDictionary();
        return this.extraCatalog;
    }
    
    /**
     * Sets the document in a suitable way to do page reordering.
     */    
     public void setLinearPageMode() {
        root.setLinearMode(null);
    }
    
    /** Getter for property group.
     * @return Value of property group.
     *
     */
    public PdfDictionary getGroup() {
        return this.group;
    }
    
    /** Setter for property group.
     * @param group New value of property group.
     *
     */
    public void setGroup(PdfDictionary group) {
        this.group = group;
    }
    
    /**
     * Sets the PDFX conformance level. Allowed values are PDFX1A2001 and PDFX32002. It
     * must be called before opening the document.
     * @param pdfxConformance the conformance level
     */    
    public void setPDFXConformance(int pdfxConformance) {
        if (this.pdfxConformance == pdfxConformance)
            return;
        if (pdf.isOpen())
            throw new PdfXConformanceException("PDFX conformance can only be set before opening the document.");
        if (crypto != null)
            throw new PdfXConformanceException("A PDFX conforming document cannot be encrypted.");
        if (pdfxConformance != PDFXNONE)
            setPdfVersion(VERSION_1_3);
        this.pdfxConformance = pdfxConformance;
    }
 
    /**
     * Gets the PDFX conformance level.
     * @return the PDFX conformance level
     */    
    public int getPDFXConformance() {
        return pdfxConformance;
    }
    
    static void checkPDFXConformance(PdfWriter writer, int key, Object obj1) {
        if (writer == null || writer.pdfxConformance == PDFXNONE)
            return;
        int conf = writer.pdfxConformance;
        switch (key) {
            case PDFXKEY_COLOR:
                switch (conf) {
                    case PDFX1A2001:
                        if (obj1 instanceof ExtendedColor) {
                            ExtendedColor ec = (ExtendedColor)obj1;
                            switch (ec.getType()) {
                                case ExtendedColor.TYPE_CMYK:
                                case ExtendedColor.TYPE_GRAY:
                                    return;
                                case ExtendedColor.TYPE_RGB:
                                    throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                                case ExtendedColor.TYPE_SEPARATION:
                                    SpotColor sc = (SpotColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, sc.getPdfSpotColor().getAlternativeCS());
                                    break;
                                case ExtendedColor.TYPE_SHADING:
                                    ShadingColor xc = (ShadingColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, xc.getPdfShadingPattern().getShading().getColorSpace());
                                    break;
                                case ExtendedColor.TYPE_PATTERN:
                                    PatternColor pc = (PatternColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, pc.getPainter().getDefaultColor());
                                    break;
                            }
                        }
                        else if (obj1 instanceof Color)
                            throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                        break;
                }
                break;
            case PDFXKEY_CMYK:
                break;
            case PDFXKEY_RGB:
                if (conf == PDFX1A2001)
                    throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                break;
            case PDFXKEY_FONT:
                if (!((BaseFont)obj1).isEmbedded())
                    throw new PdfXConformanceException("All the fonts must be embedded.");
                break;
            case PDFXKEY_IMAGE:
                PdfImage image = (PdfImage)obj1;
                if (image.get(PdfName.SMASK) != null)
                    throw new PdfXConformanceException("The /SMask key is not allowed in images.");
                switch (conf) {
                    case PDFX1A2001:
                        PdfObject cs = image.get(PdfName.COLORSPACE);
                        if (cs == null)
                            return;
                        if (cs.isName()) {
                            if (PdfName.DEVICERGB.equals(cs))
                                throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                        }
                        else if (cs.isArray()) {
                            if (PdfName.CALRGB.equals((PdfObject)((PdfArray)cs).getArrayList().get(0)))
                                throw new PdfXConformanceException("Colorspace CalRGB is not allowed.");
                        }
                        break;
                }
                break;
            case PDFXKEY_GSTATE:
                PdfDictionary gs = (PdfDictionary)obj1;
                PdfObject obj = gs.get(PdfName.BM);
                if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
                    throw new PdfXConformanceException("Blend mode " + obj.toString() + " not allowed.");
                obj = gs.get(PdfName.CA);
                double v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException("Transparency is not allowed: /CA = " + v);
                obj = gs.get(PdfName.ca);
                v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException("Transparency is not allowed: /ca = " + v);
                break;
            case PDFXKEY_LAYER:
                throw new PdfXConformanceException("Layers are not allowed.");
        }
    }
    
    /**
     * Sets the values of the output intent dictionary. Null values are allowed to
     * suppress any key.
     * @param outputConditionIdentifier a value
     * @param outputCondition a value
     * @param registryName a value
     * @param info a value
     * @param destOutputProfile a value
     * @throws IOException on error
     */    
    public void setOutputIntents(String outputConditionIdentifier, String outputCondition, String registryName, String info, byte destOutputProfile[]) throws IOException {
        getExtraCatalog();
        PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT);
        if (outputCondition != null)
            out.put(PdfName.OUTPUTCONDITION, new PdfString(outputCondition, PdfObject.TEXT_UNICODE));
        if (outputConditionIdentifier != null)
            out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString(outputConditionIdentifier, PdfObject.TEXT_UNICODE));
        if (registryName != null)
            out.put(PdfName.REGISTRYNAME, new PdfString(registryName, PdfObject.TEXT_UNICODE));
        if (info != null)
            out.put(PdfName.INFO, new PdfString(registryName, PdfObject.TEXT_UNICODE));
        if (destOutputProfile != null) {
            PdfStream stream = new PdfStream(destOutputProfile);
            stream.flateCompress();
            out.put(PdfName.DESTOUTPUTPROFILE, addToBody(stream).getIndirectReference());
        }
        out.put(PdfName.S, PdfName.GTS_PDFX);
        extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
    }
    
    private static String getNameString(PdfDictionary dic, PdfName key) {
        PdfObject obj = PdfReader.getPdfObject(dic.get(key));
        if (obj == null || !obj.isString())
            return null;
        return ((PdfString)obj).toUnicodeString();
    }
    
    /**
     * Copies the output intent dictionary from other document to this one.
     * @param reader the other document
     * @param checkExistence <CODE>true</CODE> to just check for the existence of a valid output intent
     * dictionary, <CODE>false</CODE> to insert the dictionary if it exists
     * @throws IOException on error
     * @return <CODE>true</CODE> if the output intent dictionary exists, <CODE>false</CODE>
     * otherwise
     */    
    public boolean setOutputIntents(PdfReader reader, boolean checkExistence) throws IOException {
        PdfDictionary catalog = reader.getCatalog();
        PdfArray outs = (PdfArray)PdfReader.getPdfObject(catalog.get(PdfName.OUTPUTINTENTS));
        if (outs == null)
            return false;
        ArrayList arr = outs.getArrayList();
        if (arr.size() == 0)
            return false;
        PdfDictionary out = (PdfDictionary)PdfReader.getPdfObject((PdfObject)arr.get(0));
        PdfObject obj = PdfReader.getPdfObject(out.get(PdfName.S));
        if (obj == null || !PdfName.GTS_PDFX.equals(obj))
            return false;
        if (checkExistence)
            return true;
        PRStream stream = (PRStream)PdfReader.getPdfObject(out.get(PdfName.DESTOUTPUTPROFILE));
        byte destProfile[] = null;
        if (stream != null) {
            destProfile = PdfReader.getStreamBytes(stream);
        }
        setOutputIntents(getNameString(out, PdfName.OUTPUTCONDITIONIDENTIFIER), getNameString(out, PdfName.OUTPUTCONDITION),
            getNameString(out, PdfName.REGISTRYNAME), getNameString(out, PdfName.INFO), destProfile);
        return true;
    }
    
    /**
     * Sets the page box sizes. Allowed names are: "crop", "trim", "art" and "bleed".
     * @param boxName the box size
     * @param size the size
     */    
    public void setBoxSize(String boxName, Rectangle size) {
        pdf.setBoxSize(boxName, size);
    }
    
    /**
     * Gets the default colorspaces.
     * @return the default colorspaces
     */    
    public PdfDictionary getDefaultColorspace() {
        return defaultColorspace;
    }

    /**
     * Sets the default colorspace that will be applied to all the document.
     * The colorspace is only applied if another colorspace with the same name
     * is not present in the content.
     * <p>
     * The colorspace is applied immediately when creating templates and at the page
     * end for the main document content.
     * @param key the name of the colorspace. It can be <CODE>PdfName.DEFAULTGRAY</CODE>, <CODE>PdfName.DEFAULTRGB</CODE>
     * or <CODE>PdfName.DEFAULTCMYK</CODE>
     * @param cs the colorspace. A <CODE>null</CODE> or <CODE>PdfNull</CODE> removes any colorspace with the same name
     */    
    public void setDefaultColorspace(PdfName key, PdfObject cs) {
        if (cs == null || cs.isNull())
            defaultColorspace.remove(key);
        defaultColorspace.put(key, cs);
    }

    /**
     * Gets the 1.5 compression status.
     * @return <code>true</code> if the 1.5 compression is on
     */
    public boolean isFullCompression() {
        return this.fullCompression;
    }
    
    /**
     * Sets the document's compression to the new 1.5 mode with object streams and xref
     * streams. It can be set at any time but once set it can't be unset.
     * <p>
     * If set before opening the document it will also set the pdf version to 1.5.
     */
    public void setFullCompression() {
        this.fullCompression = true;
        setPdfVersion(VERSION_1_5);
    }
    
    /**
     * Gets the <B>Optional Content Properties Dictionary</B>. Each call fills the dictionary with the current layer
     * state. It's advisable to only call this method right before close and do any modifications
     * at that time.
     * @return the Optional Content Properties Dictionary
     */    
    public PdfOCProperties getOCProperties() {
        fillOCProperties(true);
        return OCProperties;
    }
    
    /**
     * Sets a collection of optional content groups whose states are intended to follow
     * a "radio button" paradigm. That is, the state of at most one optional
     * content group in the array should be ON at a time: if one group is turned
     * ON, all others must be turned OFF.
     * @param group the radio group
     */    
    public void addOCGRadioGroup(ArrayList group) {
        PdfArray ar = new PdfArray();
        for (int k = 0; k < group.size(); ++k) {
            PdfLayer layer = (PdfLayer)group.get(k);
            if (layer.getTitle() == null)
                ar.add(layer.getRef());
        }
        if (ar.size() == 0)
            return;
        OCGRadioGroup.add(ar);
    }
    
    /**
     * Sets the the thumbnail image for the current page.
     * @param image the image
     * @throws PdfException on error
     * @throws DocumentException or error
     */    
    public void setThumbnail(Image image) throws PdfException, DocumentException {
        pdf.setThumbnail(image);
    }

	/**
	 * A UserUnit is a value that defines the default user space unit.
	 * The minimum UserUnit is 1 (1 unit = 1/72 inch).
	 * The maximum UserUnit is 75,000.
	 * Remark that this userunit only works starting with PDF1.6!
	 * @return Returns the userunit.
	 */
	public float getUserunit() {
		return userunit;
	}
	/**
	 * A UserUnit is a value that defines the default user space unit.
	 * The minimum UserUnit is 1 (1 unit = 1/72 inch).
	 * The maximum UserUnit is 75,000.
	 * Remark that this userunit only works starting with PDF1.6!
	 * @param userunit The userunit to set.
	 * @throws DocumentException
	 */
	public void setUserunit(float userunit) throws DocumentException {
		if (userunit < 1f || userunit > 75000f) throw new DocumentException("UserUnit should be a value between 1 and 75000.");
		this.userunit = userunit;
	}
}