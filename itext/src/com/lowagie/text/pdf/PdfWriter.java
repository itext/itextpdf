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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.TreeMap;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Image;
import java.awt.Color;
import com.lowagie.text.ExceptionConverter;

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
    
    public class PdfBody {
        
        // inner classes
        
        /**
         * <CODE>PdfCrossReference</CODE> is an entry in the PDF Cross-Reference table.
         */
        
        class PdfCrossReference {
            
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
            
            final byte[] toPdf(PdfWriter writer) {
                // This code makes it more difficult to port the lib to JDK1.1.x:
                // StringBuffer off = new StringBuffer("0000000000").append(offset);
                // off.delete(0, off.length() - 10);
                // StringBuffer gen = new StringBuffer("00000").append(generation);
                // gen.delete(0, gen.length() - 5);
                // so it was changed into this:
                String s = "0000000000" + offset;
                StringBuffer off = new StringBuffer(s.substring(s.length() - 10));
                s = "00000" + generation;
                StringBuffer gen = new StringBuffer(s.substring(s.length() - 5));
                if (generation == 65535) {
                    return getISOBytes(off.append(' ').append(gen).append(" f \n").toString());
                }
                return getISOBytes(off.append(' ').append(gen).append(" n \n").toString());
            }
        }
        
        // membervariables
        
        /**	Byte offset in the PDF file of the root object. */
        private int rootOffset;
        
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
        
        PdfBody(int offset, PdfWriter writer) {
            xrefs = new ArrayList();
            xrefs.add(new PdfCrossReference(0, 65535));
            xrefs.add(new PdfCrossReference(0));
            position = offset;
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
        
        final PdfIndirectObject add(PdfObject object) {
            PdfIndirectObject indirect = new PdfIndirectObject(size(), object, writer);
            xrefs.add(new PdfCrossReference(position));
            position += indirect.length();
            return indirect;
        }
        
        /**
         * Gets a PdfIndirectReference for an object that will be created in the future.
         * @return a PdfIndirectReference
         */
        
        final PdfIndirectReference getPdfIndirectReference() {
            xrefs.add(new PdfCrossReference(0));
            return new PdfIndirectReference(0, size() - 1);
        }
        
        final int getIndirectReferenceNumber() {
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
        
        final PdfIndirectObject add(PdfObject object, PdfIndirectReference ref) {
            PdfIndirectObject indirect = new PdfIndirectObject(ref.getNumber(), object, writer);
            xrefs.set(ref.getNumber(), new PdfCrossReference(position));
            position += indirect.length();
            return indirect;
        }
        
        final PdfIndirectObject add(PdfObject object, int refNumber) {
            PdfIndirectObject indirect = new PdfIndirectObject(refNumber, object, writer);
            xrefs.set(refNumber, new PdfCrossReference(position));
            position += indirect.length();
            return indirect;
        }
        
        /**
         * Adds a <CODE>PdfResources</CODE> object to the body.
         *
         * @param		object			the <CODE>PdfResources</CODE>
         * @return		a <CODE>PdfIndirectObject</CODE>
         */
        
        final PdfIndirectObject add(PdfResources object) {
            return add(object);
        }
        
        /**
         * Adds a <CODE>PdfPages</CODE> object to the body.
         *
         * @param		object			the root of the document
         * @return		a <CODE>PdfIndirectObject</CODE>
         */
        
        final PdfIndirectObject add(PdfPages object) {
            PdfIndirectObject indirect = new PdfIndirectObject(PdfWriter.ROOT, object, writer);
            rootOffset = position;
            position += indirect.length();
            return indirect;
        }
        
        /**
         * Returns the offset of the Cross-Reference table.
         *
         * @return		an offset
         */
        
        final int offset() {
            return position;
        }
        
        /**
         * Returns the total number of objects contained in the CrossReferenceTable of this <CODE>Body</CODE>.
         *
         * @return	a number of objects
         */
        
        final int size() {
            return xrefs.size();
        }
        
        /**
         * Returns the CrossReferenceTable of the <CODE>Body</CODE>.
         *
         * @return	an array of <CODE>byte</CODE>s
         */
        
        final byte[] getCrossReferenceTable() {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                stream.write(getISOBytes("xref\n0 "));
                stream.write(getISOBytes(String.valueOf(size())));
                stream.write(getISOBytes("\n"));
                // we set the ROOT object
                xrefs.set(PdfWriter.ROOT, new PdfCrossReference(rootOffset));
                // all the other objects
                PdfCrossReference entry;
                for (Iterator i = xrefs.iterator(); i.hasNext(); ) {
                    entry = (PdfCrossReference) i.next();
                    stream.write(entry.toPdf(null));
                }
            }
            catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            return stream.toByteArray();
        }
    }
    
    /**
     * <CODE>PdfTrailer</CODE> is the PDF Trailer object.
     * <P>
     * This object is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 5.16 (page 59-60).
     */
    
    class PdfTrailer {
        
        // membervariables
        
        /** content of the trailer */
        private byte[] bytes;
        
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
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                stream.write(getISOBytes("trailer\n"));
                
                PdfDictionary dictionary = new PdfDictionary();
                dictionary.put(PdfName.SIZE, new PdfNumber(size));
                dictionary.put(PdfName.ROOT, root);
                if (info != null) {
                    dictionary.put(PdfName.INFO, info);
                }
                if (encryption != null)
                    dictionary.put(PdfName.ENCRYPT, encryption);
                if (fileID != null)
                    dictionary.put(PdfName.ID, fileID);
                stream.write(dictionary.toPdf(null));
                stream.write(getISOBytes("\nstartxref\n"));
                stream.write(getISOBytes(String.valueOf(offset)));
                stream.write(getISOBytes("\n%%EOF\n"));
            }
            catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            bytes = stream.toByteArray();
        }
        
        /**
         * Returns the PDF representation of this <CODE>PdfObject</CODE>.
         *
         * @return		an array of <CODE>byte</CODE>s
         */
        
        final byte[] toPdf(PdfWriter writer) {
            return bytes;
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
    
    /** this is the header of a PDF document */
    private static byte[] HEADER = getISOBytes("%PDF-1.4\n%\u00e0\u00e1\u00e2\u00e3\n");
    
    /** byte offset of the Body */
    private static final int OFFSET = HEADER.length;
    
    /** This is the object number of the root. */
    private static final int ROOT = 1;
    
    /** This is an indirect reference to the root. */
    private static final PdfIndirectReference ROOTREFERENCE = new PdfIndirectReference(PdfObject.DICTIONARY, ROOT);
    
    /** Indirect reference to the root of the document. */
    protected PdfPages root = new PdfPages();
    
    /** Dictionary, containing all the images of the PDF document */
    protected PdfXObjectDictionary imageDictionary = new PdfXObjectDictionary();
    
    /** The form XObjects in this document. */
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
    
    protected ColorDetails patternColorspaceRGB;
    protected ColorDetails patternColorspaceGRAY;
    protected ColorDetails patternColorspaceCMYK;
    protected HashMap documentSpotPatterns = new HashMap();
    
    // membervariables
    
    /** body of the PDF document */
    private PdfBody body = new PdfBody(OFFSET, this);
    
    /** the pdfdocument object. */
    private PdfDocument pdf;
    
    /** The <CODE>PdfPageEvent</CODE> for this document. */
    private PdfPageEvent pageEvent;
    
    private PdfEncryption crypto;
    
    private HashMap importedPages = new HashMap();
    
    private PdfReaderInstance currentPdfReaderInstance;
    
/** The PdfIndirectReference to the pages. */
    private ArrayList pageReferences = new ArrayList();
    
    private int currentPageNumber = 1;
    
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
    
    public PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException {
        if (!open) {
            throw new PdfException("The document isn't open.");
        }
        PdfIndirectObject object = body.add(contents);
        try {
            object.writeTo(os);
            os.flush();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        page.add(object.getIndirectReference());
        page.setParent(ROOTREFERENCE);
        PdfIndirectObject pageObject = body.add(page, getPageReference(currentPageNumber++));
        try {
            pageObject.writeTo(os);
            os.flush();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        root.add(pageObject.getIndirectReference());
        return pageObject.getIndirectReference();
    }
    
    /**
     * Writes a <CODE>PdfImage</CODE> to the outputstream.
     *
     * @param pdfImage the image to be added
     * @return a <CODE>PdfIndirectReference</CODE> to the encapsulated image
     * @throws PdfException when a document isn't open yet, or has been closed
     */
    
    public PdfIndirectReference add(PdfImage pdfImage) throws PdfException {
        if (! imageDictionary.contains(pdfImage)) {
            PdfIndirectObject object = body.add(pdfImage);
            try {
                object.writeTo(os);
            }
            catch(IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            imageDictionary.put(pdfImage.name(), object.getIndirectReference());
            return object.getIndirectReference();
        }
        return (PdfIndirectReference) imageDictionary.get(pdfImage.name());
    }
    
    /**
     * return the <CODE>PdfIndirectReference</CODE> to the image with a given name.
     *
     * @param name the name of the image
     * @return a <CODE>PdfIndirectReference</CODE>
     */
    
    public PdfIndirectReference getImageReference(PdfName name) {
        return (PdfIndirectReference) imageDictionary.get(name);
    }
    
    /**
     * Writes a <CODE>PdfOutline</CODE> to the outputstream.
     *
     * @return a <CODE>PdfIndirectReference</CODE> to the encapsulated outline
     * @param outline the outline to be written
     * @throws PdfException when a document isn't open yet, or has been closed
     */
    
    public PdfIndirectReference add(PdfOutline outline) throws PdfException {
        PdfIndirectObject object = body.add(outline);
        try {
            object.writeTo(os);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        return object.getIndirectReference();
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
        try {
            os.write(HEADER);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
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
                // add the fonts
                for (Iterator it = documentFonts.values().iterator(); it.hasNext();) {
                    FontDetails details = (FontDetails)it.next();
                    details.writeFont(this);
                }
                
                // add the form XObjects
                for (Iterator it = formXObjects.keySet().iterator(); it.hasNext();) {
                    PdfTemplate template = (PdfTemplate)it.next();
                    if (template.getType() == PdfTemplate.TYPE_TEMPLATE) {
                        PdfIndirectObject obj = body.add(template.getFormXObject(), template.getIndirectReference());
                        obj.writeTo(os);
                    }
                }
                // add all the dependencies in the imported pages
                for (Iterator it = importedPages.values().iterator(); it.hasNext();) {
                    currentPdfReaderInstance = (PdfReaderInstance)it.next();
                    currentPdfReaderInstance.writeAllPages();
                }
                
                // add the color
                for (Iterator it = documentColors.values().iterator(); it.hasNext();) {
                    ColorDetails color = (ColorDetails)it.next();
                    PdfIndirectObject cobj = body.add(color.getSpotColor(), color.getIndirectReference());
                    cobj.writeTo(os);
                }
                // add the pattern
                for (Iterator it = documentPatterns.keySet().iterator(); it.hasNext();) {
                    PdfPatternPainter pat = (PdfPatternPainter)it.next();
                    PdfIndirectObject pobj = body.add(pat.getPattern(), pat.getIndirectReference());
                    pobj.writeTo(os);
                }
                // add the root to the body
                PdfIndirectObject rootObject = body.add(root);
                rootObject.writeTo(os);
                // make the catalog-object and add it to the body
                PdfIndirectObject indirectCatalog = body.add(((PdfDocument)document).getCatalog(rootObject.getIndirectReference()));
                indirectCatalog.writeTo(os);
                // add the info-object to the body
                PdfIndirectObject info = body.add(((PdfDocument)document).getInfo());
                info.writeTo(os);
                PdfIndirectReference encryption = null;
                PdfLiteral fileID = null;
                if (crypto != null) {
                    PdfIndirectObject encryptionObject = body.add(crypto.getEncryptionDictionary());
                    encryptionObject.writeTo(os);
                    encryption = encryptionObject.getIndirectReference();
                    fileID = crypto.getFileID();
                }
                
                // write the cross-reference table of the body
                os.write(body.getCrossReferenceTable());
                // make the trailer
                PdfTrailer trailer = new PdfTrailer(body.size(),
                body.offset(),
                indirectCatalog.getIndirectReference(),
                info.getIndirectReference(),
                encryption,
                fileID);
                os.write(trailer.toPdf(this));
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
     *
     * @return	the bottom height of the just added table
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
        return directContent;
    }
    
    /**
     * Gets the direct content under for this document. There is only one direct content,
     * multiple calls to this method will allways retrieve the same.
     * @return the direct content
     */
    
    public PdfContentByte getDirectContentUnder() {
        return directContentUnder;
    }
    
    /**
     * Resets all the direct contents to empty. This happens when a new page is started.
     */
    
    void resetContent() {
        directContent.reset();
        directContentUnder.reset();
    }
    
    /** Gets the root outline.
     * @return the root outline
     */
    
    public PdfOutline getRootOutline() {
        return directContent.getRootOutline();
    }
    
    /**
     * Adds a <CODE>BaseFont</CODE> to the document and to the page resources.
     * @param bf the <CODE>BaseFont</CODE> to add
     * @return the name of the font in the document
     */
    
    FontDetails add(BaseFont bf) {
        FontDetails ret = (FontDetails)addSimple(bf);
        pdf.addFont(ret.getFontName(), ret.getIndirectReference());
        return ret;
    }
    
    /**
     * Adds a <CODE>BaseFont</CODE> to the document but not to the page resources.
     * It is used for templates.
     * @param bf the <CODE>BaseFont</CODE> to add
     * @return an <CODE>Object[]</CODE> where position 0 is a <CODE>PdfName</CODE>
     * and position 1 is an <CODE>PdfIndirectReference</CODE>
     */
    
    FontDetails addSimple(BaseFont bf) {
        FontDetails ret = (FontDetails)documentFonts.get(bf);
        if (ret == null) {
            try {
                ret = new FontDetails(new PdfName("F" + (fontNumber++)), body.getPdfIndirectReference(), bf);
            }
            catch (BadPdfFormatException e) {
                throw new ExceptionConverter(e);
            }
            documentFonts.put(bf, ret);
        }
        return ret;
    }
    
    /**
     * Adds a <CODE>SpotColor</CODE> to the document and to the page resources.
     * @param spc the <CODE>PdfSpotColor</CODE> to add
     * @return the name of the spotcolor in the document
     */
    
    ColorDetails add(PdfSpotColor spc) {
        ColorDetails ret = (ColorDetails)addSimple(spc);
        pdf.addColor(ret.getColorName(), ret.getIndirectReference());
        return ret;
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
            try {
                ret = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), spc);
            }
            catch (BadPdfFormatException e) {
                throw new ExceptionConverter(e);
            }
            documentColors.put(spc, ret);
        }
        return ret;
    }
    
    ColorDetails addSimplePatternColorspace(Color color) {
        int type = ExtendedColor.getType(color);
        if (type == ExtendedColor.TYPE_PATTERN)
            throw new RuntimeException("An uncolored tile pattern can not have another pattern as color.");
        try {
            switch (type) {
                case ExtendedColor.TYPE_RGB:
                    if (patternColorspaceRGB == null) {
                        patternColorspaceRGB = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICERGB);
                        PdfIndirectObject cobj = body.add(array, patternColorspaceRGB.getIndirectReference());
                        cobj.writeTo(os);
                    }
                    return patternColorspaceRGB;
                case ExtendedColor.TYPE_CMYK:
                    if (patternColorspaceCMYK == null) {
                        patternColorspaceCMYK = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICECMYK);
                        PdfIndirectObject cobj = body.add(array, patternColorspaceCMYK.getIndirectReference());
                        cobj.writeTo(os);
                    }
                    return patternColorspaceCMYK;
                case ExtendedColor.TYPE_GRAY:
                    if (patternColorspaceGRAY == null) {
                        patternColorspaceGRAY = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICEGRAY);
                        PdfIndirectObject cobj = body.add(array, patternColorspaceGRAY.getIndirectReference());
                        cobj.writeTo(os);
                    }
                    return patternColorspaceGRAY;
                case ExtendedColor.TYPE_SEPARATION:
                {
                    ColorDetails details = addSimple(((SpotColor)color).getPdfSpotColor());
                    ColorDetails patternDetails = (ColorDetails)documentSpotPatterns.get(details);
                    if (patternDetails == null) {
                        patternDetails = new ColorDetails(new PdfName("CS" + (colorNumber++)), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(details.getIndirectReference());
                        PdfIndirectObject cobj = body.add(array, patternDetails.getIndirectReference());
                        cobj.writeTo(os);
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
    
    PdfIndirectReference getPdfIndirectReference() {
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
        PdfName name = (PdfName)formXObjects.get(template);
        try {
            if (name == null) {
                name = new PdfName("Xf" + formXObjectsCounter);
                ++formXObjectsCounter;
                formXObjects.put(template, name);
            }
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
            iob.writeTo(os);
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
    
    PdfIndirectObject addToBody(PdfObject object) throws IOException {
        PdfIndirectObject iobj = body.add(object);
        iobj.writeTo(os);
        return iobj;
    }
    
    PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
        PdfIndirectObject iobj = body.add(object, ref);
        iobj.writeTo(os);
        return iobj;
    }
    
    PdfIndirectObject addToBody(PdfObject object, int refNumber) throws IOException {
        PdfIndirectObject iobj = body.add(object, refNumber);
        iobj.writeTo(os);
        return iobj;
    }
    
    /** When the document opens it will jump to the destination with
     * this name.
     * @param name the name of the destination to jump to
     */
    public void setOpenAction(String name) {
        pdf.setOpenAction(name);
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
    PdfIndirectReference getPageReference(int page) {
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

    public void addTextField(String name, String text, float llx, float lly, float urx, float ury) {
        try {
            PdfTemplate template = new PdfTemplate(this);
            template.setWidth(urx - llx);
            template.setHeight(ury - lly);
            addDirectTemplateSimple(template);
            BaseFont bf = BaseFont.createFont("Helvetica", "winansi", false);
            template.setGrayFill(1);
            template.rectangle(0, 0, urx - llx, ury - lly);
            template.fill();
            template.setLiteral("/Tx BMC q\n");
            //template.saveState();
            //template.rectangle(0, 0, urx - llx, ury - lly);
            //template.stroke();
            template.rectangle(1, 1, urx - llx - 1, ury - lly - 1);
            template.clip();
            template.newPath();
            template.beginText();
            template.setFontAndSize(bf, 12);
            template.setRGBColorFillF(0, 0, 1);
            template.setTextMatrix(0, 0);
            template.showText(text);
            template.endText();
            template.setLiteral("Q\nEMC");
            FontDetails fd = add(bf);
            PdfDictionary acroForm = new PdfDictionary();
            PdfDictionary annot = new PdfDictionary();
            annot.put(PdfName.TYPE, PdfName.ANNOT);
            annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
            annot.put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
            //annot.put(PdfName.BORDER, new PdfLiteral("[0 0 1]"));
            annot.put(PdfName.FT, PdfName.TX);
            annot.put(PdfName.T, new PdfString(name));
            annot.put(PdfName.V, new PdfString(text));
            //annot.put(PdfName.MK, new PdfLiteral("<< /BG [1 1 1] >>"));
            annot.put(PdfName.F, new PdfLiteral("4"));
            annot.put(PdfName.FF, new PdfLiteral("8"));
            annot.put(PdfName.Q, new PdfLiteral("0"));
            ByteBuffer bb = new ByteBuffer();
            bb.append(fd.getFontName().toPdf(null)).append(" 0 Tf 0 0 1 rg");
            annot.put(PdfName.DA, new PdfStringLiteral(bb.toByteArray()));
            annot.put(PdfName.DR, template.getResources());
            acroForm.put(PdfName.DA, new PdfStringLiteral(bb.toByteArray()));
            acroForm.put(PdfName.DR, template.getResources());
            PdfDictionary ap = new PdfDictionary();
            ap.put(PdfName.N, template.getIndirectReference());
            annot.put(PdfName.AP, ap);
            PdfIndirectReference ref = addToBody(annot).getIndirectReference();
            acroForm.put(PdfName.FIELDS, new PdfArray(ref));
            PdfIndirectReference aref = addToBody(acroForm).getIndirectReference();
            pdf.addAcroForm(aref, ref);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
