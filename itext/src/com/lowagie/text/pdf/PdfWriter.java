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
import com.lowagie.text.Table;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Image;

/**
 * A <CODE>DocWriter</CODE> class for PDF.
 * <P>
 * When this <CODE>PdfWriter</CODE> is added
 * to a certain <CODE>PdfDocument</CODE>, the PDF representation of every Element
 * added to this Document will be written to the outputstream.</P>
 *
 * @author  bruno@lowagie.com
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
            
            final byte[] toPdf() {
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
        
        // constructors
        
/**
 * Constructs a new <CODE>PdfBody</CODE>.
 *
 * @param	offset	the offset of the body
 */
        
        PdfBody(int offset) {
            xrefs = new ArrayList();
            xrefs.add(new PdfCrossReference(0, 65535));
            xrefs.add(new PdfCrossReference(0));
            position = offset;
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
            PdfIndirectObject indirect = new PdfIndirectObject(size(), object);
            xrefs.add(new PdfCrossReference(position));
            position += indirect.length();
            return indirect;
        }
        
/**
 * Gets a PdfIndirectReference for an object that will be created in the future.
 * @return a PdfIndirectReference
 */
        
        final PdfIndirectReference getPdfIndirectReference()
        {
            xrefs.add(new PdfCrossReference(0));
            return new PdfIndirectReference(0, size() - 1);
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
            PdfIndirectObject indirect = new PdfIndirectObject(ref.getNumber(), object);
            xrefs.set(ref.getNumber(), new PdfCrossReference(position));
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
            PdfIndirectObject indirect = new PdfIndirectObject(PdfWriter.ROOT, object);
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
                    stream.write(entry.toPdf());
                }
            }
            catch (IOException ioe) {
                throw new RuntimeException("Error in PdfIndirectObject::getBytes()!  Error was: " + ioe);
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
        
        public PdfTrailer(int size, int offset, PdfIndirectReference root, PdfIndirectReference info) {
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                stream.write(getISOBytes("trailer\n"));
                
                PdfDictionary dictionary = new PdfDictionary();
                dictionary.put(PdfName.SIZE, new PdfNumber(size));
                dictionary.put(PdfName.ROOT, root);
                if (info != null) {
                    dictionary.put(PdfName.INFO, info);
                }
                stream.write(dictionary.toPdf());
                stream.write(getISOBytes("\nstartxref\n"));
                stream.write(getISOBytes(String.valueOf(offset)));
                stream.write(getISOBytes("\n%%EOF"));
            }
            catch (IOException ioe) {
                throw new RuntimeException("Error in PdfTrailer!  Error was: " + ioe);
            }
            bytes = stream.toByteArray();
        }
        
/**
 * Returns the PDF representation of this <CODE>PdfObject</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
        
        final byte[] toPdf() {
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
    
/** this is the header of a PDF document */
    private static byte[] HEADER = getISOBytes("%PDF-1.3\n%\u00e0\u00e1\u00e2\u00e3\n");
    
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
    
/** The direct content in this document. */
    protected PdfContentByte directContent;
    
/** The direct content under in this document. */
    protected PdfContentByte directContentUnder;
    
/** The fonts of this document */
    protected HashMap documentFonts = new HashMap();
    
    // membervariables
    
/** body of the PDF document */
    private PdfBody body = new PdfBody(OFFSET);
    
/** the pdfdocument object. */
    private PdfDocument pdf;
    
/** The <CODE>PdfPageEvent</CODE> for this document. */
    private PdfPageEvent pageEvent;
    
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
            System.err.println(ioe.getMessage());
        }
        page.add(object.getIndirectReference());
        page.setParent(ROOTREFERENCE);
        PdfIndirectObject pageObject = body.add(page);
        try {
            pageObject.writeTo(os);
            os.flush();
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
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
                System.err.println(ioe.getMessage());
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
            System.err.println(ioe.getMessage());
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
            pdf.close();
            try {
                // add the form XObjects
                for (Iterator it = formXObjects.keySet().iterator(); it.hasNext();) {
                    PdfTemplate template = (PdfTemplate)it.next();
                    PdfIndirectObject obj = body.add(template.getFormXObject(), template.getIndirectReference());
                    obj.writeTo(os);
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
                
                // write the cross-reference table of the body
                os.write(body.getCrossReferenceTable());
                // make the trailer
                PdfTrailer trailer = new PdfTrailer(body.size(),
                body.offset(),
                indirectCatalog.getIndirectReference(),
                info.getIndirectReference());
                os.write(trailer.toPdf());
                super.close();
            }
            catch(IOException ioe) {
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
    
/**
 * Gets the root outline.
 */
    
    public PdfOutline getRootOutline() {
        return directContent.getRootOutline();
    }
    
/**
 * Adds a <CODE>BaseFont</CODE> to the document and to the page resources.
 * @param bf the <CODE>BaseFont</CODE> to add
 * @return the name of the font in the document
 */
    
    PdfName add(BaseFont bf) {
        Object ret[] = (Object[])addSimple(bf);
        pdf.addFont((PdfName)ret[0], (PdfIndirectReference)ret[1]);
        return (PdfName)ret[0];
    }
    
/**
 * Adds a <CODE>BaseFont</CODE> to the document but not to the page resources.
 * It is used for templates.
 * @param bf the <CODE>BaseFont</CODE> to add
 * @return an <CODE>Object[]</CODE> where position 0 is a <CODE>PdfName</CODE>
 * and position 1 is an <CODE>PdfIndirectReference</CODE>
 */
    
    Object[] addSimple(BaseFont bf) {
        Object ret[] = (Object[])documentFonts.get(bf);
        if (ret == null) {
            PdfIndirectReference ind_font = null;
            try {
                for (int k = 0; k < 3; ++k) {
                    PdfObject pobj = bf.getFontInfo(ind_font, k);
                    if (pobj != null){
                        PdfIndirectObject obj = body.add(pobj);
                        ind_font = obj.getIndirectReference();
                        obj.writeTo(os);
                    }
                }
            }
            catch(Exception e) {
                System.err.println(e.getMessage());
            }
            String fontName = "F" + (fontNumber++);
            try {
                ret = new Object[]{new PdfName(fontName), ind_font};
            }
            catch (BadPdfFormatException e) {
            }
            documentFonts.put(bf, ret);
        }
        return ret;
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
        catch (Exception e) {}
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
    
/** Sets the viewer preferences as the sum of several constants:<br>
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
}