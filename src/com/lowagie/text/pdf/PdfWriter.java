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

import com.lowagie.text.DocListener;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.ImgWMF;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;

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
        
        static class PdfCrossReference {
            
            // membervariables
            
            /**	Byte offset in the PDF file. */
            private int offset;
            
            /**	generation of the object. */
            private int generation;
            
            // constructors
            /**
             * Constructs a cross-reference element for a PdfIndirectObject.
             *
             * @param	offset		byte offset of the object
             * @param	generation	generationnumber of the object
             */
            
            PdfCrossReference(int offset, int generation) {
                this.offset = offset;
                this.generation = generation;
            }
            
            /**
             * Constructs a cross-reference element for a PdfIndirectObject.
             *
             * @param	offset		byte offset of the object
             */
            
            PdfCrossReference(int offset) {
                this(offset, 0);
            }
            
            /**
             * Returns the PDF representation of this <CODE>PdfObject</CODE>.
             *
             * @return		an array of <CODE>byte</CODE>s
             */
            
            public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
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
        }
        
        // membervariables
        
        /** array containing the cross-reference table of the normal objects. */
        private ArrayList xrefs;
        
        /** the current byteposition in the body. */
        private int position;
        private PdfWriter writer;
        // constructors
        
        /**
         * Constructs a new <CODE>PdfBody</CODE>.
         *
         * @param	offset	the offset of the body
         */
        
        PdfBody(PdfWriter writer) {
            xrefs = new ArrayList();
            xrefs.add(new PdfCrossReference(0, 65535));
            position = writer.getOs().getCounter();
            this.writer = writer;
        }
        
        // methods
        
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
         */
        
        PdfIndirectObject add(PdfObject object) throws IOException {
            PdfIndirectObject indirect = new PdfIndirectObject(size(), object, writer);
            xrefs.add(new PdfCrossReference(position));
            indirect.writeTo(writer.getOs());
            position = writer.getOs().getCounter();
            return indirect;
        }
        
        /**
         * Gets a PdfIndirectReference for an object that will be created in the future.
         * @return a PdfIndirectReference
         */
        
        PdfIndirectReference getPdfIndirectReference() {
            xrefs.add(new PdfCrossReference(0));
            return new PdfIndirectReference(0, size() - 1);
        }
        
        int getIndirectReferenceNumber() {
            xrefs.add(new PdfCrossReference(0));
            return size() - 1;
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
         */
        
        PdfIndirectObject add(PdfObject object, PdfIndirectReference ref) throws IOException {
            PdfIndirectObject indirect = new PdfIndirectObject(ref.getNumber(), object, writer);
            xrefs.set(ref.getNumber(), new PdfCrossReference(position));
            indirect.writeTo(writer.getOs());
            position = writer.getOs().getCounter();
            return indirect;
        }
        
        PdfIndirectObject add(PdfObject object, int refNumber) throws IOException {
            PdfIndirectObject indirect = new PdfIndirectObject(refNumber, object, writer);
            xrefs.set(refNumber, new PdfCrossReference(position));
            indirect.writeTo(writer.getOs());
            position = writer.getOs().getCounter();
            return indirect;
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
            return xrefs.size();
        }
        
        /**
         * Returns the CrossReferenceTable of the <CODE>Body</CODE>.
         *
         * @return	an array of <CODE>byte</CODE>s
         */
        
        void writeCrossReferenceTable(OutputStream os) throws IOException {
            os.write(getISOBytes("xref\n0 "));
            os.write(getISOBytes(String.valueOf(size())));
            os.write('\n');
            // all the other objects
            PdfCrossReference entry;
            for (Iterator i = xrefs.iterator(); i.hasNext(); ) {
                entry = (PdfCrossReference) i.next();
                entry.toPdf(null, os);
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
         */
        
        PdfTrailer(int size, int offset, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID) {
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
        }
        
        /**
         * Returns the PDF representation of this <CODE>PdfObject</CODE>.
         *
         * @return		an array of <CODE>byte</CODE>s
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
    public static final int DirectionL2R = 65536;
    /** A viewer preference */
    public static final int DirectionR2L = 131072;
    /** The mask to decide if a ViewerPreferences dictionary is needed */
    static final int ViewerPreferencesMask = 0x3ff00;
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
    
    public static final PdfName DOCUMENT_CLOSE = PdfName.DC;
    public static final PdfName WILL_SAVE = PdfName.WS;
    public static final PdfName DID_SAVE = PdfName.DS;
    public static final PdfName WILL_PRINT = PdfName.WP;
    public static final PdfName DID_PRINT = PdfName.DP;
    
    public static final PdfName PAGE_OPEN = PdfName.O;
    public static final PdfName PAGE_CLOSE = PdfName.C;

    public static final int SIGNATURE_EXISTS = 1;
    public static final int SIGNATURE_APPEND_ONLY = 2;
    
    public static final char VERSION_1_2 = '2';
    public static final char VERSION_1_3 = '3';
    public static final char VERSION_1_4 = '4';
    public static final char VERSION_1_5 = '5';
    
    private static final int VPOINT = 7;
    /** this is the header of a PDF document */
    protected byte[] HEADER = getISOBytes("%PDF-1.4\n%\u00e2\u00e3\u00cf\u00d3\n");
    
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
    
    /** The defaukt space-char ratio. */    
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
    
    // constructor
    
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
            object = body.add(contents);
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
                    try {
                        ImgWMF wmf = (ImgWMF)image;
                        wmf.readWMF(getDirectContent().createTemplate(0, 0));
                    }
                    catch (Exception e) {
                        throw new DocumentException(e.getMessage());
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
                    if (colorspace != null && colorspace.type() == PdfObject.ARRAY) {
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
            PdfIndirectObject object;
            try {
                object = body.add(pdfImage);
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
            object = body.add(icc);
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
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj)
    {
        return ((PdfDocument)document).getCatalog(rootObj);
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
                PdfIndirectObject obj = body.add(template.getFormXObject(), template.getIndirectReference());
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
            PdfIndirectObject cobj = body.add(color.getSpotColor(this), color.getIndirectReference());
        }
        // add the pattern
        for (Iterator it = documentPatterns.keySet().iterator(); it.hasNext();) {
            PdfPatternPainter pat = (PdfPatternPainter)it.next();
            PdfIndirectObject pobj = body.add(pat.getPattern(), pat.getIndirectReference());
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
                if (extraCatalog != null) {
                    catalog.mergeDifferent(extraCatalog);
                }
                PdfIndirectObject indirectCatalog = body.add(catalog);
                // add the info-object to the body
                PdfIndirectObject info = body.add(((PdfDocument)document).getInfo());
                PdfIndirectReference encryption = null;
                PdfObject fileID = null;
                if (crypto != null) {
                    PdfIndirectObject encryptionObject = body.add(crypto.getEncryptionDictionary());
                    encryption = encryptionObject.getIndirectReference();
                    fileID = crypto.getFileID();
                }
                else
                    fileID = PdfEncryption.createInfoId(PdfEncryption.createDocumentId());
                
                // write the cross-reference table of the body
                body.writeCrossReferenceTable(os);
                // make the trailer
                PdfTrailer trailer = new PdfTrailer(body.size(),
                body.offset(),
                indirectCatalog.getIndirectReference(),
                info.getIndirectReference(),
                encryption,
                fileID);
                trailer.toPdf(this, os);
                super.close();
            }
            catch(IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
    }
    
    // methods
    
    /**
     * Returns the number of the next object that can be added to the body.
     *
     * @return	the size of the body-object
     */
    
    int size() {
        return body.size();
    }
    
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
    
    OutputStreamCounter getOs() {
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
            return new FontDetails(new PdfName("F" + (fontNumber++)), body.getPdfIndirectReference(), bf);
        }
        FontDetails ret = (FontDetails)documentFonts.get(bf);
        if (ret == null) {
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
                        PdfIndirectObject cobj = body.add(array, patternColorspaceRGB.getIndirectReference());
                    }
                    return patternColorspaceRGB;
                case ExtendedColor.TYPE_CMYK:
                    if (patternColorspaceCMYK == null) {
                        patternColorspaceCMYK = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICECMYK);
                        PdfIndirectObject cobj = body.add(array, patternColorspaceCMYK.getIndirectReference());
                    }
                    return patternColorspaceCMYK;
                case ExtendedColor.TYPE_GRAY:
                    if (patternColorspaceGRAY == null) {
                        patternColorspaceGRAY = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICEGRAY);
                        PdfIndirectObject cobj = body.add(array, patternColorspaceGRAY.getIndirectReference());
                    }
                    return patternColorspaceGRAY;
                case ExtendedColor.TYPE_SEPARATION: {
                    ColorDetails details = addSimple(((SpotColor)color).getPdfSpotColor());
                    ColorDetails patternDetails = (ColorDetails)documentSpotPatterns.get(details);
                    if (patternDetails == null) {
                        patternDetails = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(details.getIndirectReference());
                        PdfIndirectObject cobj = body.add(array, patternDetails.getIndirectReference());
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
            documentExtGState.put(gstate, new PdfObject[]{new PdfName("GS" + (documentExtGState.size() + 1)), getPdfIndirectReference()});
        }
        return (PdfObject[])documentExtGState.get(gstate);
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
     * @return the <CODE>PdfName</CODE> for this template
     */
    
    PdfName addDirectTemplateSimple(PdfTemplate template) {
        PdfIndirectReference ref = template.getIndirectReference();
        Object obj[] = (Object[])formXObjects.get(ref);
        PdfName name = null;
        try {
            if (obj == null) {
                name = new PdfName("Xf" + formXObjectsCounter);
                ++formXObjectsCounter;
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
            PdfIndirectObject iob = body.add(destination, (PdfIndirectReference)obj[1]);
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
     * Sets the viewer preferences by ORing some of these constants:<br>
     * <ul>
     * <li>The page layout to be used when the document is opened (choose one).
     *   <ul>
     *   <li><b>PageLayoutSinglePage</b> - Display one page at a time.
     *   <li><b>PageLayoutOneColumn</b> - Display the pages in one column.
     *   <li><b>PageLayoutTwoColumnLeft</b> - Display the pages in two columns, with
     *       oddnumbered pages on the left.
     *   <li><b>PageLayoutTwoColumnRight</b> - Display the pages in two columns, with
     *       oddnumbered pages on the right.
     *   </ul>
     * <li>The page mode how the document should be displayed
     *     when opened (choose one).
     *   <ul>
     *   <li><b>PageModeUseNone</b> - Neither document outline nor thumbnail images visible.
     *   <li><b>PageModeUseOutlines</b> - Document outline visible.
     *   <li><b>PageModeUseThumbs</b> - Thumbnail images visible.
     *   <li><b>PageModeFullScreen</b> - Full-screen mode, with no menu bar, window
     *       controls, or any other window visible.
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
     * <li>The document's page mode, specifying how to display the
     *     document on exiting full-screen mode. It is meaningful only
     *     if the page mode is <b>PageModeFullScreen</b> (choose one).
     *   <ul>
     *   <li><b>NonFullScreenPageModeUseNone</b> - Neither document outline nor thumbnail images
     *       visible
     *   <li><b>NonFullScreenPageModeUseOutlines</b> - Document outline visible
     *   <li><b>NonFullScreenPageModeUseThumbs</b> - Thumbnail images visible
     *   </ul>
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
     * @param strength128Bits true for 128 bit key length. false for 40 bit key length
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
     * @param strength true for 128 bit key length. false for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(getISOBytes(userPassword), getISOBytes(ownerPassword), permissions, strength);
    }
    
    public PdfIndirectObject addToBody(PdfObject object) throws IOException {
        PdfIndirectObject iobj = body.add(object);
        return iobj;
    }
    
    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
        PdfIndirectObject iobj = body.add(object, ref);
        return iobj;
    }
    
    public PdfIndirectObject addToBody(PdfObject object, int refNumber) throws IOException {
        PdfIndirectObject iobj = body.add(object, refNumber);
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
    
    int getNewObjectNumber(PdfReader reader, int number, int generation) {
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
    
    /** Sets the PDF version. Must be used right before the document
     * is opened. Valid options are VERSION_1_2, VERSION_1_3,
     * VERSION_1_4 and VERSION_1_5. VERSION_1_4 is the default.
     * @param version the version number
     */
    public void setPdfVersion(char version) {
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
    
    public void setPageEmpty(boolean pageEmpty) {
        pdf.setPageEmpty(pageEmpty);
    }

    /** Gets the info dictionary for changing.
     * @return the info dictionary
     */    
    public PdfDictionary getInfo() {
        return ((PdfDocument)document).getInfo();
    }
    
    public PdfDictionary getExtraCatalog() {
        return this.extraCatalog;
    }
    
    /** Sets extra keys to the catalog.
     * @param extraCatalog the extra keys
     */    
    public void setExtraCatalog(PdfDictionary extraCatalog) {
        this.extraCatalog = extraCatalog;
    }
    
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
    
}