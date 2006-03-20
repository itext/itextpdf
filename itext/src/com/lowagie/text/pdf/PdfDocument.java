/*
 * $Name$
 * $Id$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lowagie.text.Anchor;
import com.lowagie.text.Annotation;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Graphic;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Meta;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.StringCompare;
import com.lowagie.text.Table;
import com.lowagie.text.Watermark;
import com.lowagie.text.xml.xmp.XmpWriter;

/**
 * <CODE>PdfDocument</CODE> is the class that is used by <CODE>PdfWriter</CODE>
 * to translate a <CODE>Document</CODE> into a PDF with different pages.
 * <P>
 * A <CODE>PdfDocument</CODE> always listens to a <CODE>Document</CODE>
 * and adds the Pdf representation of every <CODE>Element</CODE> that is
 * added to the <CODE>Document</CODE>.
 *
 * @see		com.lowagie.text.Document
 * @see		com.lowagie.text.DocListener
 * @see		PdfWriter
 */

class PdfDocument extends Document implements DocListener {
    
    /**
     * <CODE>PdfInfo</CODE> is the PDF InfoDictionary.
     * <P>
     * A document's trailer may contain a reference to an Info dictionary that provides information
     * about the document. This optional dictionary may contain one or more keys, whose values
     * should be strings.<BR>
     * This object is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 6.10 (page 120-121)
     */
    
    public static class PdfInfo extends PdfDictionary {
        
        // constructors
        
        /**
         * Construct a <CODE>PdfInfo</CODE>-object.
         */
        
        PdfInfo() {
            super();
            addProducer();
            addCreationDate();
        }
        
        /**
         * Constructs a <CODE>PdfInfo</CODE>-object.
         *
         * @param		author		name of the author of the document
         * @param		title		title of the document
         * @param		subject		subject of the document
         */
        
        PdfInfo(String author, String title, String subject) {
            this();
            addTitle(title);
            addSubject(subject);
            addAuthor(author);
        }
        
        /**
         * Adds the title of the document.
         *
         * @param	title		the title of the document
         */
        
        void addTitle(String title) {
            put(PdfName.TITLE, new PdfString(title, PdfObject.TEXT_UNICODE));
        }
        
        /**
         * Adds the subject to the document.
         *
         * @param	subject		the subject of the document
         */
        
        void addSubject(String subject) {
            put(PdfName.SUBJECT, new PdfString(subject, PdfObject.TEXT_UNICODE));
        }
        
        /**
         * Adds some keywords to the document.
         *
         * @param	keywords		the keywords of the document
         */
        
        void addKeywords(String keywords) {
            put(PdfName.KEYWORDS, new PdfString(keywords, PdfObject.TEXT_UNICODE));
        }
        
        /**
         * Adds the name of the author to the document.
         *
         * @param	author		the name of the author
         */
        
        void addAuthor(String author) {
            put(PdfName.AUTHOR, new PdfString(author, PdfObject.TEXT_UNICODE));
        }
        
        /**
         * Adds the name of the creator to the document.
         *
         * @param	creator		the name of the creator
         */
        
        void addCreator(String creator) {
            put(PdfName.CREATOR, new PdfString(creator, PdfObject.TEXT_UNICODE));
        }
        
        /**
         * Adds the name of the producer to the document.
         */
        
        void addProducer() {
            // This line may only be changed by Bruno Lowagie or Paulo Soares
            put(PdfName.PRODUCER, new PdfString(getVersion()));
            // Do not edit the line above!
        }
        
        /**
         * Adds the date of creation to the document.
         */
        
        void addCreationDate() {
            PdfString date = new PdfDate();
            put(PdfName.CREATIONDATE, date);
            put(PdfName.MODDATE, date);
        }
        
        void addkey(String key, String value) {
            if (key.equals("Producer") || key.equals("CreationDate"))
                return;
            put(new PdfName(key), new PdfString(value, PdfObject.TEXT_UNICODE));
        }
    }
    
    /**
     * <CODE>PdfCatalog</CODE> is the PDF Catalog-object.
     * <P>
     * The Catalog is a dictionary that is the root node of the document. It contains a reference
     * to the tree of pages contained in the document, a reference to the tree of objects representing
     * the document's outline, a reference to the document's article threads, and the list of named
     * destinations. In addition, the Catalog indicates whether the document's outline or thumbnail
     * page images should be displayed automatically when the document is viewed and wether some location
     * other than the first page should be shown when the document is opened.<BR>
     * In this class however, only the reference to the tree of pages is implemented.<BR>
     * This object is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 6.2 (page 67-71)
     */
    
    static class PdfCatalog extends PdfDictionary {
        
        PdfWriter writer;
        // constructors
        
        /**
         * Constructs a <CODE>PdfCatalog</CODE>.
         *
         * @param		pages		an indirect reference to the root of the document's Pages tree.
         * @param writer the writer the catalog applies to
         */
        
        PdfCatalog(PdfIndirectReference pages, PdfWriter writer) {
            super(CATALOG);
            this.writer = writer;
            put(PdfName.PAGES, pages);
        }
        
        /**
         * Constructs a <CODE>PdfCatalog</CODE>.
         *
         * @param		pages		an indirect reference to the root of the document's Pages tree.
         * @param		outlines	an indirect reference to the outline tree.
         * @param writer the writer the catalog applies to
         */
        
        PdfCatalog(PdfIndirectReference pages, PdfIndirectReference outlines, PdfWriter writer) {
            super(CATALOG);
            this.writer = writer;
            put(PdfName.PAGES, pages);
            put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
            put(PdfName.OUTLINES, outlines);
        }
        
        /**
         * Adds the names of the named destinations to the catalog.
         * @param localDestinations the local destinations
         * @param documentJavaScript the javascript used in the document
         * @param writer the writer the catalog applies to
         */
        void addNames(TreeMap localDestinations, ArrayList documentJavaScript, PdfWriter writer) {
            if (localDestinations.size() == 0 && documentJavaScript.size() == 0)
                return;
            try {
                PdfDictionary names = new PdfDictionary();
                if (localDestinations.size() > 0) {
                    PdfArray ar = new PdfArray();
                    for (Iterator i = localDestinations.keySet().iterator(); i.hasNext();) {
                        String name = (String)i.next();
                        Object obj[] = (Object[])localDestinations.get(name);
                        PdfIndirectReference ref = (PdfIndirectReference)obj[1];
                        ar.add(new PdfString(name));
                        ar.add(ref);
                    }
                    PdfDictionary dests = new PdfDictionary();
                    dests.put(PdfName.NAMES, ar);
                    names.put(PdfName.DESTS, writer.addToBody(dests).getIndirectReference());
                }
                if (documentJavaScript.size() > 0) {
                    String s[] = new String[documentJavaScript.size()];
                    for (int k = 0; k < s.length; ++k)
                        s[k] = Integer.toHexString(k);
                    Arrays.sort(s, new StringCompare());
                    PdfArray ar = new PdfArray();
                    for (int k = 0; k < s.length; ++k) {
                        ar.add(new PdfString(s[k]));
                        ar.add((PdfIndirectReference)documentJavaScript.get(k));
                    }
                    PdfDictionary js = new PdfDictionary();
                    js.put(PdfName.NAMES, ar);
                    names.put(PdfName.JAVASCRIPT, writer.addToBody(js).getIndirectReference());
                }
                put(PdfName.NAMES, writer.addToBody(names).getIndirectReference());
            }
            catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
        
        /** Sets the viewer preferences as the sum of several constants.
         * @param preferences the viewer preferences
         * @see PdfWriter#setViewerPreferences
         */
        
        void setViewerPreferences(int preferences) {
            PdfReader.setViewerPreferences(preferences, this);
        }
        
        void setOpenAction(PdfAction action) {
            put(PdfName.OPENACTION, action);
        }
        
        
        /** Sets the document level additional actions.
         * @param actions   dictionary of actions
         */
        void setAdditionalActions(PdfDictionary actions) {
            try {
                put(PdfName.AA, writer.addToBody(actions).getIndirectReference());
            } catch (Exception e) {
                new ExceptionConverter(e);
            }
        }
        
        
        void setPageLabels(PdfPageLabels pageLabels) {
            put(PdfName.PAGELABELS, pageLabels.getDictionary());
        }
        
        void setAcroForm(PdfObject fields) {
            put(PdfName.ACROFORM, fields);
        }
    }
    
    // membervariables
    private PdfIndirectReference thumb;
    
    /** The characters to be applied the hanging ponctuation. */
    static final String hangingPunctuation = ".,;:'";
    
    /** The <CODE>PdfWriter</CODE>. */
    private PdfWriter writer;
    
    /** some meta information about the Document. */
    private PdfInfo info = new PdfInfo();
    
    /** Signals that OnOpenDocument should be called. */
    private boolean firstPageEvent = true;
    
    /** Signals that onParagraph is valid. */
    private boolean isParagraph = true;
    
    // Horizontal line
    
    /** The line that is currently being written. */
    private PdfLine line = null;
    
    /** This represents the current indentation of the PDF Elements on the left side. */
    private float indentLeft = 0;
    
    /** This represents the current indentation of the PDF Elements on the right side. */
    private float indentRight = 0;
    
    /** This represents the current indentation of the PDF Elements on the left side. */
    private float listIndentLeft = 0;
    
    /** This represents the current alignment of the PDF Elements. */
    private int alignment = Element.ALIGN_LEFT;
    
    // Vertical lines
    
    /** This is the PdfContentByte object, containing the text. */
    private PdfContentByte text;
    
    /** This is the PdfContentByte object, containing the borders and other Graphics. */
    private PdfContentByte graphics;
    
    /** The lines that are written until now. */
    private ArrayList lines = new ArrayList();
    
    /** This represents the leading of the lines. */
    private float leading = 0;
    
    /** This is the current height of the document. */
    private float currentHeight = 0;
    
    /** This represents the current indentation of the PDF Elements on the top side. */
    private float indentTop = 0;
    
    /** This represents the current indentation of the PDF Elements on the bottom side. */
    private float indentBottom = 0;
    
    /** This checks if the page is empty. */
    private boolean pageEmpty = true;
    
    private int textEmptySize;
    // resources
    
    /** This is the size of the next page. */
    protected Rectangle nextPageSize = null;
    
    /** This is the size of the several boxes of the current Page. */
    protected HashMap thisBoxSize = new HashMap();
    
    /** This is the size of the several boxes that will be used in
     * the next page. */
    protected HashMap boxSize = new HashMap();
    
    /** This are the page resources of the current Page. */
    protected PageResources pageResources;
    
    // images
    
    /** This is the image that could not be shown on a previous page. */
    private Image imageWait = null;
    
    /** This is the position where the image ends. */
    private float imageEnd = -1;
    
    /** This is the indentation caused by an image on the left. */
    private float imageIndentLeft = 0;
    
    /** This is the indentation caused by an image on the right. */
    private float imageIndentRight = 0;
    
    // annotations and outlines
    
    /** This is the array containing the references to the annotations. */
    private ArrayList annotations;
    
    /** This is an array containg references to some delayed annotations. */
    private ArrayList delayedAnnotations = new ArrayList();
    
    /** This is the AcroForm object. */
    PdfAcroForm acroForm;
    
    /** This is the root outline of the document. */
    private PdfOutline rootOutline;
    
    /** This is the current <CODE>PdfOutline</CODE> in the hierarchy of outlines. */
    private PdfOutline currentOutline;
    
    /** The current active <CODE>PdfAction</CODE> when processing an <CODE>Anchor</CODE>. */
    private PdfAction currentAction = null;
    
    /**
     * Stores the destinations keyed by name. Value is
     * <CODE>Object[]{PdfAction,PdfIndirectReference,PdfDestintion}</CODE>.
     */
    private TreeMap localDestinations = new TreeMap(new StringCompare());
    
    private ArrayList documentJavaScript = new ArrayList();
    
    /** these are the viewerpreferences of the document */
    private int viewerPreferences = 0;
    
    private String openActionName;
    private PdfAction openActionAction;
    private PdfDictionary additionalActions;
    private PdfPageLabels pageLabels;
    
    //add by Jin-Hsia Yang
    private boolean isNewpage = false;
    
    private float paraIndent = 0;
    //end add by Jin-Hsia Yang
    
    /** margin in x direction starting from the left. Will be valid in the next page */
    protected float nextMarginLeft;
    
    /** margin in x direction starting from the right. Will be valid in the next page */
    protected float nextMarginRight;
    
    /** margin in y direction starting from the top. Will be valid in the next page */
    protected float nextMarginTop;
    
    /** margin in y direction starting from the bottom. Will be valid in the next page */
    protected float nextMarginBottom;
    
/** The duration of the page */
    protected int duration=-1; // negative values will indicate no duration
    
/** The page transition */
    protected PdfTransition transition=null; 
    
    protected PdfDictionary pageAA = null;
    
    /** Holds value of property strictImageSequence. */
    private boolean strictImageSequence = false;    

    /** Holds the type of the last element, that has been added to the document. */
    private int lastElementType = -1;    
    
    private boolean isNewPagePending;
    
    protected int markPoint;
    
    // constructors
    
    /**
     * Constructs a new PDF document.
     * @throws DocumentException on error
     */
    
    public PdfDocument() throws DocumentException {
        super();
        addProducer();
        addCreationDate();
    }
    
    // listener methods
    
    /**
     * Adds a <CODE>PdfWriter</CODE> to the <CODE>PdfDocument</CODE>.
     *
     * @param writer the <CODE>PdfWriter</CODE> that writes everything
     *                     what is added to this document to an outputstream.
     * @throws DocumentException on error
     */
    
    public void addWriter(PdfWriter writer) throws DocumentException {
        if (this.writer == null) {
            this.writer = writer;
            acroForm = new PdfAcroForm(writer);
            return;
        }
        throw new DocumentException("You can only add a writer to a PdfDocument once.");
    }
    
    /**
     * Sets the pagesize.
     *
     * @param pageSize the new pagesize
     * @return <CODE>true</CODE> if the page size was set
     */
    
    public boolean setPageSize(Rectangle pageSize) {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        nextPageSize = new Rectangle(pageSize);
        return true;
    }
    
    /**
     * Changes the header of this document.
     *
     * @param header the new header
     */
    
    public void setHeader(HeaderFooter header) {
        if (writer != null && writer.isPaused()) {
            return;
        }
        super.setHeader(header);
    }
    
    /**
     * Resets the header of this document.
     */
    
    public void resetHeader() {
        if (writer != null && writer.isPaused()) {
            return;
        }
        super.resetHeader();
    }
    
    /**
     * Changes the footer of this document.
     *
     * @param	footer		the new footer
     */
    
    public void setFooter(HeaderFooter footer) {
        if (writer != null && writer.isPaused()) {
            return;
        }
        super.setFooter(footer);
    }
    
    /**
     * Resets the footer of this document.
     */
    
    public void resetFooter() {
        if (writer != null && writer.isPaused()) {
            return;
        }
        super.resetFooter();
    }
    
    /**
     * Sets the page number to 0.
     */
    
    public void resetPageCount() {
        if (writer != null && writer.isPaused()) {
            return;
        }
        super.resetPageCount();
    }
    
    /**
     * Sets the page number.
     *
     * @param	pageN		the new page number
     */
    
    public void setPageCount(int pageN) {
        if (writer != null && writer.isPaused()) {
            return;
        }
        super.setPageCount(pageN);
    }
    
    /**
     * Sets the <CODE>Watermark</CODE>.
     *
     * @param watermark the watermark to add
     * @return <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
     */
    
    public boolean add(Watermark watermark) {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        this.watermark = watermark;
        return true;
    }
    
    /**
     * Removes the <CODE>Watermark</CODE>.
     */
    
    public void removeWatermark() {
        if (writer != null && writer.isPaused()) {
            return;
        }
        this.watermark = null;
    }
    
    /**
     * Sets the margins.
     *
     * @param	marginLeft		the margin on the left
     * @param	marginRight		the margin on the right
     * @param	marginTop		the margin on the top
     * @param	marginBottom	the margin on the bottom
     * @return	a <CODE>boolean</CODE>
     */
    
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        nextMarginLeft = marginLeft;
        nextMarginRight = marginRight;
        nextMarginTop = marginTop;
        nextMarginBottom = marginBottom;
        return true;
    }
    
    protected PdfArray rotateAnnotations() {
        PdfArray array = new PdfArray();
        int rotation = pageSize.getRotation() % 360;
        int currentPage = writer.getCurrentPageNumber();
        for (int k = 0; k < annotations.size(); ++k) {
            PdfAnnotation dic = (PdfAnnotation)annotations.get(k);
            int page = dic.getPlaceInPage();
            if (page > currentPage) {
                delayedAnnotations.add(dic);
                continue;
            }
            if (dic.isForm()) {
                if (!dic.isUsed()) {
                    HashMap templates = dic.getTemplates();
                    if (templates != null)
                        acroForm.addFieldTemplates(templates);
                }
                PdfFormField field = (PdfFormField)dic;
                if (field.getParent() == null)
                    acroForm.addDocumentField(field.getIndirectReference());
            }
            if (dic.isAnnotation()) {
                array.add(dic.getIndirectReference());
                if (!dic.isUsed()) {
                    PdfRectangle rect = (PdfRectangle)dic.get(PdfName.RECT);
                    if (rect != null) {
                    	switch (rotation) {
                        	case 90:
                        		dic.put(PdfName.RECT, new PdfRectangle(
                        				pageSize.top() - rect.bottom(),
										rect.left(),
										pageSize.top() - rect.top(),
										rect.right()));
                        		break;
                        	case 180:
                        		dic.put(PdfName.RECT, new PdfRectangle(
                        				pageSize.right() - rect.left(),
										pageSize.top() - rect.bottom(),
										pageSize.right() - rect.right(),
										pageSize.top() - rect.top()));
                        		break;
                        	case 270:
                        		dic.put(PdfName.RECT, new PdfRectangle(
                        				rect.bottom(),
										pageSize.right() - rect.left(),
										rect.top(),
										pageSize.right() - rect.right()));
                        		break;
                    	}
                    }
                }
            }
            if (!dic.isUsed()) {
                dic.setUsed();
                try {
                    writer.addToBody(dic, dic.getIndirectReference());
                }
                catch (IOException e) {
                    throw new ExceptionConverter(e);
                }
            }
        }
        return array;
    }
    
    /**
     * Makes a new page and sends it to the <CODE>PdfWriter</CODE>.
     *
     * @return a <CODE>boolean</CODE>
     * @throws DocumentException on error
     */
    
    public boolean newPage() throws DocumentException {
        lastElementType = -1;
        //add by Jin-Hsia Yang
        isNewpage = true;
        //end add by Jin-Hsia Yang
        if (writer.getDirectContent().size() == 0 && writer.getDirectContentUnder().size() == 0 && (pageEmpty || (writer != null && writer.isPaused()))) {
            return false;
        }
        PdfPageEvent pageEvent = writer.getPageEvent();
        if (pageEvent != null)
            pageEvent.onEndPage(writer, this);
        
        //Added to inform any listeners that we are moving to a new page (added by David Freels)
        super.newPage();
        
        // the following 2 lines were added by Pelikan Stephan
        imageIndentLeft = 0;
        imageIndentRight = 0;
        
        // we flush the arraylist with recently written lines
        flushLines();
        // we assemble the resources of this pages
        pageResources.addDefaultColorDiff(writer.getDefaultColorspace());        
        PdfDictionary resources = pageResources.getResources();
        // we make a new page and add it to the document
        if (writer.getPDFXConformance() != PdfWriter.PDFXNONE) {
            if (thisBoxSize.containsKey("art") && thisBoxSize.containsKey("trim"))
                throw new PdfXConformanceException("Only one of ArtBox or TrimBox can exist in the page.");
            if (!thisBoxSize.containsKey("art") && !thisBoxSize.containsKey("trim")) {
                if (thisBoxSize.containsKey("crop"))
                    thisBoxSize.put("trim", thisBoxSize.get("crop"));
                else
                    thisBoxSize.put("trim", new PdfRectangle(pageSize, pageSize.getRotation()));
            }
        }
        PdfPage page;
        int rotation = pageSize.getRotation();
        page = new PdfPage(new PdfRectangle(pageSize, rotation), thisBoxSize, resources, rotation);
        // we add tag info
        if (writer.isTagged())
	             page.put(PdfName.STRUCTPARENTS, new PdfNumber(writer.getCurrentPageNumber() - 1));
//      we add the transitions
        if (this.transition!=null) {
            page.put(PdfName.TRANS, this.transition.getTransitionDictionary());
            transition = null;
        }
        if (this.duration>0) {
            page.put(PdfName.DUR,new PdfNumber(this.duration));
            duration = 0;
        }
        // we add the page object additional actions
        if (pageAA != null) {
            try {
                page.put(PdfName.AA, writer.addToBody(pageAA).getIndirectReference());
            }
            catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            pageAA = null;
        }
        // we check if the userunit is defined
        if (writer.getUserunit() > 0f) {
	        page.put(PdfName.USERUNIT, new PdfNumber(writer.getUserunit()));
	    }
        // we add the annotations
        if (annotations.size() > 0) {
            PdfArray array = rotateAnnotations();
            if (array.size() != 0)
                page.put(PdfName.ANNOTS, array);
        }
        // we add the thumbs
        if (thumb != null) {
            page.put(PdfName.THUMB, thumb);
            thumb = null;
        }
        if (!open || close) {
            throw new PdfException("The document isn't open.");
        }
        if (text.size() > textEmptySize)
            text.endText();
        else
            text = null;
        writer.add(page, new PdfContents(writer.getDirectContentUnder(), graphics, text, writer.getDirectContent(), pageSize));
        // we initialize the new page
        initPage();
        
        //add by Jin-Hsia Yang
        isNewpage = false;
        //end add by Jin-Hsia Yang
        
        return true;
    }
    
    // methods to open and close a document
    
    /**
     * Opens the document.
     * <P>
     * You have to open the document before you can begin to add content
     * to the body of the document.
     */
    
    public void open() {
        if (!open) {
            super.open();
            writer.open();
            rootOutline = new PdfOutline(writer);
            currentOutline = rootOutline;
        }
        try {
            initPage();
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    void outlineTree(PdfOutline outline) throws IOException {
        outline.setIndirectReference(writer.getPdfIndirectReference());
        if (outline.parent() != null)
            outline.put(PdfName.PARENT, outline.parent().indirectReference());
        ArrayList kids = outline.getKids();
        int size = kids.size();
        for (int k = 0; k < size; ++k)
            outlineTree((PdfOutline)kids.get(k));
        for (int k = 0; k < size; ++k) {
            if (k > 0)
                ((PdfOutline)kids.get(k)).put(PdfName.PREV, ((PdfOutline)kids.get(k - 1)).indirectReference());
            if (k < size - 1)
                ((PdfOutline)kids.get(k)).put(PdfName.NEXT, ((PdfOutline)kids.get(k + 1)).indirectReference());
        }
        if (size > 0) {
            outline.put(PdfName.FIRST, ((PdfOutline)kids.get(0)).indirectReference());
            outline.put(PdfName.LAST, ((PdfOutline)kids.get(size - 1)).indirectReference());
        }
        for (int k = 0; k < size; ++k) {
            PdfOutline kid = (PdfOutline)kids.get(k);
            writer.addToBody(kid, kid.indirectReference());
        }
    }
    
    void writeOutlines() throws IOException {
        if (rootOutline.getKids().size() == 0)
            return;
        outlineTree(rootOutline);
        writer.addToBody(rootOutline, rootOutline.indirectReference());
    }
    
    void traverseOutlineCount(PdfOutline outline) {
        ArrayList kids = outline.getKids();
        PdfOutline parent = outline.parent();
        if (kids.size() == 0) {
            if (parent != null) {
                parent.setCount(parent.getCount() + 1);
            }
        }
        else {
            for (int k = 0; k < kids.size(); ++k) {
                traverseOutlineCount((PdfOutline)kids.get(k));
            }
            if (parent != null) {
                if (outline.isOpen()) {
                    parent.setCount(outline.getCount() + parent.getCount() + 1);
                }
                else {
                    parent.setCount(parent.getCount() + 1);
                    outline.setCount(-outline.getCount());
                }
            }
        }
    }
    
    void calculateOutlineCount() {
        if (rootOutline.getKids().size() == 0)
            return;
        traverseOutlineCount(rootOutline);
    }
    /**
     * Closes the document.
     * <B>
     * Once all the content has been written in the body, you have to close
     * the body. After that nothing can be written to the body anymore.
     */
    
    public void close() {
        if (close) {
            return;
        }
        try {
        	boolean wasImage = (imageWait != null);
            newPage();
            if (imageWait != null || wasImage) newPage();
            if (annotations.size() > 0)
                throw new RuntimeException(annotations.size() + " annotations had invalid placement pages.");
            PdfPageEvent pageEvent = writer.getPageEvent();
            if (pageEvent != null)
                pageEvent.onCloseDocument(writer, this);
            super.close();
            
            writer.addLocalDestinations(localDestinations);
            calculateOutlineCount();
            writeOutlines();
        }
        catch(Exception e) {
            throw new ExceptionConverter(e);
        }
        
        writer.close();
    }

    PageResources getPageResources() {
        return pageResources;
    }
    
    /** Adds a <CODE>PdfPTable</CODE> to the document.
     * @param ptable the <CODE>PdfPTable</CODE> to be added to the document.
     * @throws DocumentException on error
     */
    void addPTable(PdfPTable ptable) throws DocumentException {
        ColumnText ct = new ColumnText(writer.getDirectContent());
        if (currentHeight > 0) {
            Paragraph p = new Paragraph();
            p.setLeading(0);
            ct.addElement(p);
            // if the table prefers to be on a single page, and it wouldn't
	        //fit on the current page, start a new page.
	        if (ptable.getKeepTogether() && !fitsPage(ptable, 0f))
	        	newPage();
        }
        ct.addElement(ptable);
        boolean he = ptable.isHeadersInEvent();
        ptable.setHeadersInEvent(true);
        int loop = 0;
        while (true) {
            ct.setSimpleColumn(indentLeft(), indentBottom(), indentRight(), indentTop() - currentHeight);
            int status = ct.go();
            if ((status & ColumnText.NO_MORE_TEXT) != 0) {
                text.moveText(0, ct.getYLine() - indentTop() + currentHeight);
                currentHeight = indentTop() - ct.getYLine();
                break;
            }
            if (indentTop() - currentHeight == ct.getYLine())
                ++loop;
            else
                loop = 0;
            if (loop == 3) {
                add(new Paragraph("ERROR: Infinite table loop"));
                break;
            }
            newPage();
        }
        ptable.setHeadersInEvent(he);
    }
    
	/**
	 * Gets a PdfTable object
	 * (contributed by dperezcar@fcc.es)
	 * @param table a high level table object
	 * @param supportRowAdditions
	 * @return returns a PdfTable object
	 * @see PdfWriter#getPdfTable(Table)
	 */

	PdfTable getPdfTable(Table table, boolean supportRowAdditions) {
        return new PdfTable(table, indentLeft(), indentRight(), indentTop() - currentHeight, supportRowAdditions);
	}

	/**
	 * @see PdfWriter#breakTableIfDoesntFit(PdfTable)
	 * (contributed by dperezcar@fcc.es)
	 * @param table				Table to add
	 * @return true if the table will be broken
	 * @throws DocumentException
	 */
	
	boolean breakTableIfDoesntFit(PdfTable table) throws DocumentException {
		table.updateRowAdditions();
		// Do we have any full page available?
		if (!table.hasToFitPageTable() && table.bottom() <= indentBottom) {
			// Then output that page
			add(table, true);
			return true;
		}
		return false;
	}
    
    private static class RenderingContext {
        int countPageBreaks = 0;
        float pagetop = -1;
        float oldHeight = -1;

        PdfContentByte cellGraphics = null;
        
        float lostTableBottom;
        
        float maxCellBottom;
        float maxCellHeight;
        
        Map rowspanMap;
        Map pageMap = new HashMap();
        /**
         * A PdfPTable
         */
        public PdfTable table;
        
        /**
         * Consumes the rowspan
         * @param c
         * @return a rowspan.
         */
        public int consumeRowspan(PdfCell c) {
            if (c.rowspan() == 1) {
                return 1;
            }
            
            Integer i = (Integer) rowspanMap.get(c);
            if (i == null) {
                i = new Integer(c.rowspan());
            }
            
            i = new Integer(i.intValue() - 1);
            rowspanMap.put(c, i);

            if (i.intValue() < 1) {
                return 1;
            }
            return i.intValue();
        }

        /**
         * Looks at the current rowspan.
         * @param c
         * @return the current rowspan
         */
        public int currentRowspan(PdfCell c) {
            Integer i = (Integer) rowspanMap.get(c);
            if (i == null) {
                return c.rowspan();
            } else {
                return i.intValue();
            }
        }
        
        public int cellRendered(PdfCell cell, int pageNumber) {
            Integer i = (Integer) pageMap.get(cell);
            if (i == null) {
                i = new Integer(1);
            } else {
                i = new Integer(i.intValue() + 1);
            }
            pageMap.put(cell, i);

            Integer pageInteger = new Integer(pageNumber);
            Set set = (Set) pageMap.get(pageInteger);
            
            if (set == null) {
                set = new HashSet();
                pageMap.put(pageInteger, set);
            }
            
            set.add(cell);
            
            return i.intValue();
        }

        public int numCellRendered(PdfCell cell) {
            Integer i = (Integer) pageMap.get(cell);
            if (i == null) {
                i = new Integer(0);
            } 
            return i.intValue();
        }
        
        public boolean isCellRenderedOnPage(PdfCell cell, int pageNumber) {
            Integer pageInteger = new Integer(pageNumber);
            Set set = (Set) pageMap.get(pageInteger);
            
            if (set != null) {
                return set.contains(cell);
            }
            
            return false;
        }
    };
    
    private void analyzeRow(ArrayList rows, RenderingContext ctx) {
        ctx.maxCellBottom = indentBottom();

        // determine whether row(index) is in a rowspan
        int rowIndex = 0;

        ArrayList row = (ArrayList) rows.get(rowIndex);
        int maxRowspan = 1;
        Iterator iterator = row.iterator();
        while (iterator.hasNext()) {
            PdfCell cell = (PdfCell) iterator.next();
            maxRowspan = Math.max(ctx.currentRowspan(cell), maxRowspan);
        }
        rowIndex += maxRowspan;
        
        boolean useTop = true;
        if (rowIndex == rows.size()) {
            rowIndex = rows.size() - 1;
            useTop = false;
        }
        
        if (rowIndex < 0 || rowIndex >= rows.size()) return;
        
        row = (ArrayList) rows.get(rowIndex);
        iterator = row.iterator();
        while (iterator.hasNext()) {
            PdfCell cell = (PdfCell) iterator.next();
            Rectangle cellRect = cell.rectangle(ctx.pagetop, indentBottom());
            if (useTop) {
                ctx.maxCellBottom = Math.max(ctx.maxCellBottom, cellRect.top());
            } else {
                if (ctx.currentRowspan(cell) == 1) {
                    ctx.maxCellBottom = Math.max(ctx.maxCellBottom, cellRect.bottom());
                }
            }
        }
    }
    
	/**
	 * Adds a new table to 
	 * @param table				Table to add.  Rendered rows will be deleted after processing.
	 * @param onlyFirstPage		Render only the first full page
	 * @throws DocumentException
	 */
    private void add(PdfTable table, boolean onlyFirstPage) throws DocumentException {
        // before every table, we flush all lines
        flushLines();
        
        RenderingContext ctx = new RenderingContext();
        ctx.pagetop = indentTop();
        ctx.oldHeight = currentHeight;
        ctx.cellGraphics = new PdfContentByte(writer);
        ctx.rowspanMap = new HashMap();
        ctx.table = table;
        
		// initialisation of parameters
		PdfCell cell;
	                    
		boolean tableHasToFit =
			table.hasToFitPageTable() ? (table.bottom() < indentBottom() && table.height() < (top() - bottom())) : false;
		if (pageEmpty)
			tableHasToFit = false;
		boolean cellsHaveToFit = table.hasToFitPageCells();

		// drawing the table
		ArrayList dataCells = table.getCells();
                
		ArrayList headercells = table.getHeaderCells();
		// Check if we have removed header cells in a previous call
		if (headercells.size() > 0 && (dataCells.size() == 0 || dataCells.get(0) != headercells.get(0))) {
			ArrayList allCells = new ArrayList(dataCells.size()+headercells.size());
			allCells.addAll(headercells);
			allCells.addAll(dataCells);
			dataCells = allCells;
		}
        
        ArrayList cells = dataCells;
        ArrayList rows = extractRows(cells, ctx);
        
		while (!cells.isEmpty()) {
			// initialisation of some extra parameters;
			ctx.lostTableBottom = 0;
                        
			// loop over the cells
			boolean cellsShown = false;
			int currentGroupNumber = 0;
			boolean headerChecked = false;
            
            float headerHeight = 0;
           /* 
			for (ListIterator iterator = cells.listIterator(); iterator.hasNext() && !tableHasToFit;) {
				cell = (PdfCell) iterator.next();
				boolean atLeastOneFits = false;
				cellsHaveToFit = table.hasToFitPageCells();
				if( cellsHaveToFit ) {
					if( !cell.isHeader() ) {
						if (cell.getGroupNumber() != currentGroupNumber) {
							boolean cellsFit = true;
							currentGroupNumber = cell.getGroupNumber();
							int cellCount = 0;
							while (cell.getGroupNumber() == currentGroupNumber && cellsFit && iterator.hasNext()) {
								if (cell.bottom() < indentBottom()) {
                                    float cellHeight = cell.height();
                                    float pageSize = indentTop() - indentBottom() - headerHeight - 30;
								    if (cellHeight < pageSize) {
								        cellsFit = false;
                                    }
								}
								else {
									atLeastOneFits |= true;
									break;
								}
								cell = (PdfCell) iterator.next();
								cellCount++;
							}
							if (!atLeastOneFits) {
								cellsHaveToFit = false;
							}
							if (!cellsFit) {
								break;
							}
							for (int i = cellCount; i >= 0; i--) {
								cell = (PdfCell) iterator.previous();
							}
						}
					}
					else {
                        headerHeight = cell.height();
                        if( !headerChecked ) {
							headerChecked = true;
							boolean cellsFit = true;
							int cellCount = 0;
							float firstTop = cell.top();
							while (cell.isHeader() && cellsFit && iterator.hasNext()) {
								if (firstTop - cell.bottom(0) > indentTop() - currentHeight - indentBottom()) {
									cellsFit = false;
								}
								cell = (PdfCell) iterator.next();
								cellCount++;
							}
							currentGroupNumber = cell.getGroupNumber();
							while (cell.getGroupNumber() == currentGroupNumber && cellsFit && iterator.hasNext()) {
								if (firstTop - cell.bottom(0) > indentTop() - currentHeight - indentBottom() - 10.0) {
									cellsFit = false;
								}
								cell = (PdfCell) iterator.next();
								cellCount++;
							}
							for (int i = cellCount; i >= 0; i--) {
								cell = (PdfCell) iterator.previous();
							}
							if (!cellsFit) {
								while( cell.isHeader() ) {
									iterator.remove();
									cell = (PdfCell) iterator.next();
								}
                                // never break headers
//								break;
							}
						}
					}
				}
			}*/

            // draw the cells (line by line)
            
            Iterator iterator = rows.iterator();
              
            boolean atLeastOneFits = false;
            while (iterator.hasNext()) {
                ArrayList row = (ArrayList) iterator.next();
                                       
                analyzeRow(rows, ctx);
                renderCells(ctx, row, table.hasToFitPageCells() & atLeastOneFits);
                                
                if (!mayBeRemoved(row)) {
                    break;
                }
                
                consumeRowspan(row, ctx);
                iterator.remove();
                atLeastOneFits = true;
            }

//          compose cells array list for subsequent code
            cells.clear();
            Set opt = new HashSet();
            iterator = rows.iterator();
            while (iterator.hasNext()) {
                ArrayList row = (ArrayList) iterator.next();
                
                Iterator cellIterator = row.iterator();
                while (cellIterator.hasNext()) {
                    cell = (PdfCell) cellIterator.next();
                    
                    if (!opt.contains(cell)) {
                        cells.add(cell);
                        opt.add(cell);
                    }
                }
            }
            
            tableHasToFit = false;
            
			// we paint the graphics of the table after looping through all the cells
			Rectangle tablerec = new Rectangle(table);
			tablerec.setBorder(table.border());
			tablerec.setBorderWidth(table.borderWidth());
			tablerec.setBorderColor(table.borderColor());
			tablerec.setBackgroundColor(table.backgroundColor());
			PdfContentByte under = writer.getDirectContentUnder();
			under.rectangle(tablerec.rectangle(top(), indentBottom()));
			under.add(ctx.cellGraphics);
			// bugfix by Gerald Fehringer: now again add the border for the table
			// since it might have been covered by cell backgrounds
			tablerec.setBackgroundColor(null);
			tablerec = tablerec.rectangle(top(), indentBottom());
			tablerec.setBorder(table.border());
			under.rectangle(tablerec);
			// end bugfix

            ctx.cellGraphics = new PdfContentByte(null);
			// if the table continues on the next page
            
			if (!rows.isEmpty()) {
				graphics.setLineWidth(table.borderWidth());
				if (cellsShown && (table.border() & Rectangle.BOTTOM) == Rectangle.BOTTOM) {
					// Draw the bottom line
                                
					// the color is set to the color of the element
					Color tColor = table.borderColor();
					if (tColor != null) {
						graphics.setColorStroke(tColor);
					}
					graphics.moveTo(table.left(), Math.max(table.bottom(), indentBottom()));
					graphics.lineTo(table.right(), Math.max(table.bottom(), indentBottom()));
					graphics.stroke();
					if (tColor != null) {
						graphics.resetRGBColorStroke();
					}
				}
                            
				// old page
				pageEmpty = false;
                float difference = ctx.lostTableBottom;

				// new page
				newPage();
                
				ctx.countPageBreaks++;
                
				// G.F.: if something added in page event i.e. currentHeight > 0
				float heightCorrection = 0;
				boolean somethingAdded = false;
				if (currentHeight > 0) {
					heightCorrection = 6;
					currentHeight += heightCorrection;
					somethingAdded = true;
					newLine();
					flushLines();
					indentTop = currentHeight - leading;
					currentHeight = 0;
				}
				else {
                    flushLines();
				}
                            
				// this part repeats the table headers (if any)
				int size = headercells.size();
				if (size > 0) {
					// this is the top of the headersection
					cell = (PdfCell) headercells.get(0);
					float oldTop = cell.top(0);
					// loop over all the cells of the table header
					for (int i = 0; i < size; i++) {
						cell = (PdfCell) headercells.get(i);
						// calculation of the new cellpositions
						cell.setTop(indentTop() - oldTop + cell.top(0));
						cell.setBottom(indentTop() - oldTop + cell.bottom(0));
						ctx.pagetop = cell.bottom();
						// we paint the borders of the cell
						ctx.cellGraphics.rectangle(cell.rectangle(indentTop(), indentBottom()));
						// we write the text of the cell
						ArrayList images = cell.getImages(indentTop(), indentBottom());
						for (Iterator im = images.iterator(); im.hasNext();) {
							cellsShown = true;
							Image image = (Image) im.next();
							graphics.addImage(image);
						}
						lines = cell.getLines(indentTop(), indentBottom());
						float cellTop = cell.top(indentTop());
						text.moveText(0, cellTop-heightCorrection);
						float cellDisplacement = flushLines() - cellTop+heightCorrection;
						text.moveText(0, cellDisplacement);
					}
                                
					currentHeight = indentTop() - ctx.pagetop + table.cellspacing();
					text.moveText(0, ctx.pagetop - indentTop() - currentHeight);
				}
				else {
					if (somethingAdded) {
						ctx.pagetop = indentTop();
						text.moveText(0, -table.cellspacing());
					}
				}
				ctx.oldHeight = currentHeight - heightCorrection;
                            
				// calculating the new positions of the table and the cells
				size = Math.min(cells.size(), table.columns());
				int i = 0;
				while (i < size) {
					cell = (PdfCell) cells.get(i);
					if (cell.top(-table.cellspacing()) > ctx.lostTableBottom) {
						float newBottom = ctx.pagetop - difference + cell.bottom();
						float neededHeight = cell.remainingHeight();
						if (newBottom > ctx.pagetop - neededHeight) {
							difference += newBottom - (ctx.pagetop - neededHeight);
						}
					}
					i++;
				}
				size = cells.size();
				table.setTop(indentTop());
				table.setBottom(ctx.pagetop - difference + table.bottom(table.cellspacing()));
				for (i = 0; i < size; i++) {
					cell = (PdfCell) cells.get(i);
					float newBottom = ctx.pagetop - difference + cell.bottom();
					float newTop = ctx.pagetop - difference + cell.top(-table.cellspacing());
					if (newTop > indentTop() - currentHeight) {
						newTop = indentTop() - currentHeight;
					}
               
					cell.setTop(newTop );
					cell.setBottom(newBottom );
				}
				if (onlyFirstPage) {
					break;
				}
			}
		}
        
        float tableHeight = table.top() - table.bottom();
        currentHeight = ctx.oldHeight + tableHeight;

        text.moveText(0, -tableHeight );
        pageEmpty = false;
        
        if (ctx.countPageBreaks > 0) {
            // in case of tables covering more that one page have to have
            // a newPage followed to reset some internal state. Otherwise
            // subsequent tables are rendered incorrectly.
            isNewPagePending = true;
        }
    }

    private boolean mayBeRemoved(ArrayList row) {
        Iterator iterator = row.iterator();
        boolean mayBeRemoved = true;
        while (iterator.hasNext()) {
            PdfCell cell = (PdfCell) iterator.next();
           
            mayBeRemoved &= cell.mayBeRemoved();
        }
        return mayBeRemoved;
    }

    private void consumeRowspan(ArrayList row, RenderingContext ctx) {
        Iterator iterator = row.iterator();
        while (iterator.hasNext()) {
            PdfCell c = (PdfCell) iterator.next();
            ctx.consumeRowspan(c);
        }
    }
    
    private ArrayList extractRows(ArrayList cells, RenderingContext ctx) {
        PdfCell cell;
        PdfCell previousCell = null;
        ArrayList rows = new ArrayList();
        java.util.List rowCells = new ArrayList();
        
        Iterator iterator = cells.iterator();
        while (iterator.hasNext()) {
            cell = (PdfCell) iterator.next();

            boolean isAdded = false;

            boolean isEndOfRow = !iterator.hasNext();
            boolean isCurrentCellPartOfRow = !iterator.hasNext();
            
            if (previousCell != null) {
                if (cell.left() <= previousCell.left()) {
                    isEndOfRow = true;
                    isCurrentCellPartOfRow = false;
                }
            }
            
            if (isCurrentCellPartOfRow) {
                rowCells.add(cell);
                isAdded = true;
            }
            
            if (isEndOfRow) {
                if (!rowCells.isEmpty()) {
                    // add to rowlist
                    rows.add(rowCells);
                }
                
                // start a new list for next line
                rowCells = new ArrayList();                
            }

            if (!isAdded) {
                rowCells.add(cell);
            }
            
            previousCell = cell;
        }
        
        if (!rowCells.isEmpty()) {
            rows.add(rowCells);
        }
        
        // fill row information with rowspan cells to get complete "scan lines"
        for (int i = rows.size() - 1; i >= 0; i--) {
            ArrayList row = (ArrayList) rows.get(i);

            // iterator through row
            for (int j = 0; j < row.size(); j++) {
                PdfCell c = (PdfCell) row.get(j);
                int rowspan = c.rowspan();
                
                // fill in missing rowspan cells to complete "scan line"
                for (int k = 1; k < rowspan; k++) {
                    ArrayList spannedRow = ((ArrayList) rows.get(i + k));
                    spannedRow.add(j, c);
                }
            }
        }
                
        return rows;
    }

    private void renderCells(RenderingContext ctx, java.util.List cells, boolean hasToFit) throws DocumentException {
        PdfCell cell;
        Iterator iterator;
        if (hasToFit) {
            iterator = cells.iterator();
            while (iterator.hasNext()) {
            	cell = (PdfCell) iterator.next();
            	if (!cell.isHeader()) {
            		if (cell.bottom() < indentBottom()) return;
            	}
            }
        }
        iterator = cells.iterator();
        
        while (iterator.hasNext()) {
            cell = (PdfCell) iterator.next();
            if (!ctx.isCellRenderedOnPage(cell, getPageNumber())) {

                float correction = 0;
                if (ctx.numCellRendered(cell) >= 1) {
                    correction = 1.0f;
                }
            
                lines = cell.getLines(ctx.pagetop, indentBottom() - correction);
                
                // if there is still text to render we render it
                if (lines != null && lines.size() > 0) {
                    
                    // we write the text
                    float cellTop = cell.top(ctx.pagetop - ctx.oldHeight);
                    text.moveText(0, cellTop);
                    float cellDisplacement = flushLines() - cellTop;
                    
                    text.moveText(0, cellDisplacement);
                    if (ctx.oldHeight + cellDisplacement > currentHeight) {
                        currentHeight = ctx.oldHeight + cellDisplacement;
                    }

                    ctx.cellRendered(cell, getPageNumber());
                } 
                            
                float indentBottom = indentBottom();
                
                if (ctx.maxCellBottom != 0) {
                    indentBottom = Math.max(ctx.maxCellBottom, indentBottom);
                }
    
                Rectangle tableRect = ctx.table.rectangle(ctx.pagetop, indentBottom());
                
                indentBottom = Math.max(tableRect.bottom(), indentBottom);
                
                // we paint the borders of the cells
                Rectangle cellRect = cell.rectangle(tableRect.top(), indentBottom);
                cellRect.setBottom(cellRect.bottom());
                if (cellRect.height() > 0) {
                    ctx.lostTableBottom = indentBottom;
                    ctx.cellGraphics.rectangle(cellRect);
                }
    
                // and additional graphics
                ArrayList images = cell.getImages(ctx.pagetop, indentBottom());
                for (Iterator i = images.iterator(); i.hasNext();) {
                    Image image = (Image) i.next();
                    graphics.addImage(image);
                }
                
            }
        }
        
    }
        

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
     *
     * @param element the element to add
     * @return <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
     * @throws DocumentException when a document isn't open yet, or has been closed
     */
    
    public boolean add(Element element) throws DocumentException {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        try {
//        	 resolves problem described in add(PdfTable)
            if (isNewPagePending) {
                isNewPagePending = false;
                newPage();
            }
            switch(element.type()) {
                
                // Information (headers)
                case Element.HEADER:
                    info.addkey(((Meta)element).name(), ((Meta)element).content());
                    break;
                case Element.TITLE:
                    info.addTitle(((Meta)element).content());
                    break;
                case Element.SUBJECT:
                    info.addSubject(((Meta)element).content());
                    break;
                case Element.KEYWORDS:
                    info.addKeywords(((Meta)element).content());
                    break;
                case Element.AUTHOR:
                    info.addAuthor(((Meta)element).content());
                    break;
                case Element.CREATOR:
                    info.addCreator(((Meta)element).content());
                    break;
                case Element.PRODUCER:
                    // you can not change the name of the producer
                    info.addProducer();
                    break;
                case Element.CREATIONDATE:
                    // you can not set the creation date, only reset it
                    info.addCreationDate();
                    break;
                    
                    // content (text)
                case Element.CHUNK: {
                    // if there isn't a current line available, we make one
                    if (line == null) {
                        carriageReturn();
                    }
                    
                    // we cast the element to a chunk
                    PdfChunk chunk = new PdfChunk((Chunk) element, currentAction);
                    // we try to add the chunk to the line, until we succeed
                    {
                        PdfChunk overflow;
                        while ((overflow = line.add(chunk)) != null) {
                            carriageReturn();
                            chunk = overflow;
                        }
                    }
                    pageEmpty = false;
                    if (chunk.isAttribute(Chunk.NEWPAGE)) {
                        newPage();
                    }
                    break;
                }
                case Element.ANCHOR: {
                    Anchor anchor = (Anchor) element;
                    String url = anchor.reference();
                    leading = anchor.leading();
                    if (url != null) {
                        currentAction = new PdfAction(url);
                    }
                    
                    // we process the element
                    element.process(this);
                    currentAction = null;
                    break;
                }
                case Element.ANNOTATION: {
                    if (line == null) {
                        carriageReturn();
                    }
                    Annotation annot = (Annotation) element;
                    PdfAnnotation an = convertAnnotation(writer, annot);
                    annotations.add(an);
                    pageEmpty = false;
                    break;
                }
                case Element.PHRASE: {
                    // we cast the element to a phrase and set the leading of the document
                    leading = ((Phrase) element).leading();
                    // we process the element
                    element.process(this);
                    break;
                }
                case Element.PARAGRAPH: {
                    // we cast the element to a paragraph
                    Paragraph paragraph = (Paragraph) element;
                    
                    float spacingBefore = paragraph.spacingBefore();
                    if (spacingBefore != 0) {
                        leading = spacingBefore;
                        carriageReturn();
                        if (!pageEmpty) {
                            /*
                             * Don't add spacing before a paragraph if it's the first
                             * on the page
                             */
                            Chunk space = new Chunk(" ");
                            space.process(this);
                            carriageReturn();
                        }
                    }
                    
                    // we adjust the parameters of the document
                    alignment = paragraph.alignment();
                    leading = paragraph.leading();
                    
                    carriageReturn();
                    // we don't want to make orphans/widows
                    if (currentHeight + line.height() + leading > indentTop() - indentBottom()) {
                        newPage();
                    }

                    // Begin added: Bonf (Marc Schneider) 2003-07-29
                    //carriageReturn();
                    // End added: Bonf (Marc Schneider) 2003-07-29

                    indentLeft += paragraph.indentationLeft();
                    indentRight += paragraph.indentationRight();
                    
                    // Begin removed: Bonf (Marc Schneider) 2003-07-29
                    carriageReturn();
                    // End removed: Bonf (Marc Schneider) 2003-07-29

                    
                    //add by Jin-Hsia Yang
                    
                    paraIndent += paragraph.indentationLeft();
                    //end add by Jin-Hsia Yang
                    
                    PdfPageEvent pageEvent = writer.getPageEvent();
                    if (pageEvent != null && isParagraph)
                        pageEvent.onParagraph(writer, this, indentTop() - currentHeight);
                    
                    // if a paragraph has to be kept together, we wrap it in a table object
                    if (paragraph.getKeepTogether()) {
                        Table table = new Table(1, 1);
                        table.setOffset(0f);
                        table.setBorder(Table.NO_BORDER);
                        table.setWidth(100f);
                        table.setTableFitsPage(true);
                        Cell cell = new Cell(paragraph);
                        cell.setBorder(Table.NO_BORDER);
                        //patch by Matt Benson 11/01/2002 - 14:32:00
                        cell.setHorizontalAlignment(paragraph.alignment());
                        //end patch by Matt Benson
                        table.addCell(cell);
                        this.add(table);
                        break;
                    }
                    else
                        // we process the paragraph
                        element.process(this);
                    
                    //add by Jin-Hsia Yang and blowagie
                    paraIndent -= paragraph.indentationLeft();
                    //end add by Jin-Hsia Yang and blowagie
                    
                    // Begin removed: Bonf (Marc Schneider) 2003-07-29
                    //       carriageReturn();
                    // End removed: Bonf (Marc Schneider) 2003-07-29
                    
                    float spacingAfter = paragraph.spacingAfter();
                    if (spacingAfter != 0) {
                        leading = spacingAfter;
                        carriageReturn();
                        if (currentHeight + line.height() + leading < indentTop() - indentBottom()) {
                            /*
                             * Only add spacing after a paragraph if the extra
                             * spacing fits on the page.
                             */
                            Chunk space = new Chunk(" ");
                            space.process(this);
                            carriageReturn();
                        }
                        leading = paragraph.leading();      // restore original leading
                    }

                    if (pageEvent != null && isParagraph)
                        pageEvent.onParagraphEnd(writer, this, indentTop() - currentHeight);
                    
                    alignment = Element.ALIGN_LEFT;
                    indentLeft -= paragraph.indentationLeft();
                    indentRight -= paragraph.indentationRight();
                    
                    // Begin added: Bonf (Marc Schneider) 2003-07-29
                    carriageReturn();
                    // End added: Bonf (Marc Schneider) 2003-07-29

                    //add by Jin-Hsia Yang
                    
                    //end add by Jin-Hsia Yang
                    
                    break;
                }
                case Element.SECTION:
                case Element.CHAPTER: {
                    // Chapters and Sections only differ in their constructor
                    // so we cast both to a Section
                    Section section = (Section) element;
                    
                    boolean hasTitle = section.title() != null;
                    
                    // if the section is a chapter, we begin a new page
                    if (section.isChapter()) {
                        newPage();
                    }
                    // otherwise, we begin a new line
                    else {
                        newLine();
                    }

                    if (hasTitle) {
                    float fith = indentTop() - currentHeight;
                    int rotation = pageSize.getRotation();
                    if (rotation == 90 || rotation == 180)
                        fith = pageSize.height() - fith;
                    PdfDestination destination = new PdfDestination(PdfDestination.FITH, fith);
                    while (currentOutline.level() >= section.depth()) {
                        currentOutline = currentOutline.parent();
                    }
                    PdfOutline outline = new PdfOutline(currentOutline, destination, section.getBookmarkTitle(), section.isBookmarkOpen());
                    currentOutline = outline;
                    }
                    
                    // some values are set
                    carriageReturn();
                    indentLeft += section.indentationLeft();
                    indentRight += section.indentationRight();
                    
                    PdfPageEvent pageEvent = writer.getPageEvent();
                    if (pageEvent != null)
                        if (element.type() == Element.CHAPTER)
                            pageEvent.onChapter(writer, this, indentTop() - currentHeight, section.title());
                        else
                            pageEvent.onSection(writer, this, indentTop() - currentHeight, section.depth(), section.title());
                    
                    // the title of the section (if any has to be printed)
                    if (hasTitle) {
                        isParagraph = false;
                        add(section.title());
                        isParagraph = true;
                    }
                    indentLeft += section.indentation();
                    // we process the section
                    element.process(this);
                    // some parameters are set back to normal again
                    indentLeft -= section.indentationLeft() + section.indentation();
                    indentRight -= section.indentationRight();
                    
                    if (pageEvent != null)
                        if (element.type() == Element.CHAPTER)
                            pageEvent.onChapterEnd(writer, this, indentTop() - currentHeight);
                        else
                            pageEvent.onSectionEnd(writer, this, indentTop() - currentHeight);
                    
                    break;
                }
                case Element.LIST: {
                    // we cast the element to a List
                    List list = (List) element;
                    // we adjust the document
                    listIndentLeft += list.indentationLeft();
                    indentRight += list.indentationRight();
                    // we process the items in the list
                    element.process(this);
                    // some parameters are set back to normal again
                    listIndentLeft -= list.indentationLeft();
                    indentRight -= list.indentationRight();
                    break;
                }
                case Element.LISTITEM: {
                    // we cast the element to a ListItem
                    ListItem listItem = (ListItem) element;
                   
                    float spacingBefore = listItem.spacingBefore();
                    if (spacingBefore != 0) {
                        leading = spacingBefore;
                        carriageReturn();
                        if (!pageEmpty) {
                            /*
                             * Don't add spacing before a paragraph if it's the first
                             * on the page
                             */
                            Chunk space = new Chunk(" ");
                            space.process(this);
                            carriageReturn();
                        }
                    }
                   
                    // we adjust the document
                    alignment = listItem.alignment();
                    listIndentLeft += listItem.indentationLeft();
                    indentRight += listItem.indentationRight();
                    leading = listItem.leading();
                    carriageReturn();
                    // we prepare the current line to be able to show us the listsymbol
                    line.setListItem(listItem);
                    // we process the item
                    element.process(this);

                    float spacingAfter = listItem.spacingAfter();
                    if (spacingAfter != 0) {
                        leading = spacingAfter;
                        carriageReturn();
                        if (currentHeight + line.height() + leading < indentTop() - indentBottom()) {
                            /*
                             * Only add spacing after a paragraph if the extra
                             * spacing fits on the page.
                             */
                            Chunk space = new Chunk(" ");
                            space.process(this);
                            carriageReturn();
                        }
                        leading = listItem.leading();      // restore original leading
                    }
                   
                    // if the last line is justified, it should be aligned to the left
                    //                          if (line.hasToBeJustified()) {
                    //                                  line.resetAlignment();
                    //                          }
                    // some parameters are set back to normal again
                    carriageReturn();
                    listIndentLeft -= listItem.indentationLeft();
                    indentRight -= listItem.indentationRight();
                    break;
                }
                case Element.RECTANGLE: {
                    Rectangle rectangle = (Rectangle) element;
                    graphics.rectangle(rectangle);
                    pageEmpty = false;
                    break;
                }
                case Element.PTABLE: {
                    PdfPTable ptable = (PdfPTable)element;
                    if (ptable.size() <= ptable.getHeaderRows())
                        break; //nothing to do

                    // before every table, we add a new line and flush all lines
                    ensureNewLine();
                    flushLines();
                    addPTable(ptable);                    
                    pageEmpty = false;
                    break;
                }
                case Element.MULTI_COLUMN_TEXT: {
                    ensureNewLine();
                    flushLines();
                    MultiColumnText multiText = (MultiColumnText) element;
                    float height = multiText.write(writer.getDirectContent(), this, indentTop() - currentHeight);
                    currentHeight += height;
                    text.moveText(0, -1f* height);
                    pageEmpty = false;
                    break;
                }
                case Element.TABLE : {
                    
                    /**
                     * This is a list of people who worked on the Table functionality.
                     * To see who did what, please check the CVS repository:
                     *
                     * Leslie Baski
                     * Matt Benson
                     * Francesco De Milato
                     * David Freels
                     * Bruno Lowagie
                     * Veerendra Namineni
                     * Geert Poels
                     * Tom Ring
                     * Paulo Soares
                     * Gerald Fehringer
                     * Steve Appling
                     * Karsten Klein
                     */
	                    
					PdfTable table;
                    if (element instanceof PdfTable) {
                    	// Already pre-rendered
                    	table = (PdfTable)element;
						table.updateRowAdditions();
                    } else if (element instanceof SimpleTable) {
                    	PdfPTable ptable = ((SimpleTable)element).createPdfPTable();
                    	if (ptable.size() <= ptable.getHeaderRows())
                    		break; //nothing to do
            		
                    	// before every table, we add a new line and flush all lines
                    	ensureNewLine();
                    	flushLines();
                    	addPTable(ptable);                    
                    	pageEmpty = false;
                    	break;
                    } else if (element instanceof Table) {
                    	try {
                       		PdfPTable ptable = ((Table)element).createPdfPTable();
                       		if (ptable.size() <= ptable.getHeaderRows())
                       			break; //nothing to do
             		
                       		// before every table, we add a new line and flush all lines
                       		ensureNewLine();
                       		flushLines();
                       		addPTable(ptable);                    
                       		pageEmpty = false;
                       		break;
                    	}
                    	catch(BadElementException bee) {
                    		// constructing the PdfTable
                    		// Before the table, add a blank line using offset or default leading
                    		float offset = ((Table)element).getOffset();
                    		if (Float.isNaN(offset))
                    			offset = leading;
                    		carriageReturn();
                    		lines.add(new PdfLine(indentLeft(), indentRight(), alignment, offset));
                    		currentHeight += offset;
                    		table = getPdfTable((Table)element, false);
                    	}
					} else {
						return false;
					}
                    add(table, false);
                    break;
                }
                case Element.JPEG:
                case Element.IMGRAW:
                case Element.IMGTEMPLATE: {
                    //carriageReturn(); suggestion by Marc Campforts
                    add((Image) element);
                    break;
                }
                case Element.GRAPHIC: {
                    Graphic graphic = (Graphic) element;
                    graphic.processAttributes(indentLeft(), indentBottom(), indentRight(), indentTop(), indentTop() - currentHeight);
                    graphics.add(graphic);
                    pageEmpty = false;
                    break;
                }
                default:
                    return false;
            }
            lastElementType = element.type();
            return true;
        }
        catch(Exception e) {
            throw new DocumentException(e);
        }
    }
    
    // methods to add Content
    
    /**
     * Adds an image to the document.
     * @param image the <CODE>Image</CODE> to add
     * @throws PdfException on error
     * @throws DocumentException on error
     */
    
    private void add(Image image) throws PdfException, DocumentException {
        
        if (image.hasAbsolutePosition()) {
            graphics.addImage(image);
            pageEmpty = false;
            return;
        }
        
        // if there isn't enough room for the image on this page, save it for the next page
        if (currentHeight != 0 && indentTop() - currentHeight - image.scaledHeight() < indentBottom()) {
            if (!strictImageSequence && imageWait == null) {
                imageWait = image;
                return;
            }
            newPage();
            if (currentHeight != 0 && indentTop() - currentHeight - image.scaledHeight() < indentBottom()) {
                imageWait = image;
                return;
            }
        }
        pageEmpty = false;
        // avoid endless loops
        if (image == imageWait)
            imageWait = null;
        boolean textwrap = (image.alignment() & Image.TEXTWRAP) == Image.TEXTWRAP
        && !((image.alignment() & Image.MIDDLE) == Image.MIDDLE);
        boolean underlying = (image.alignment() & Image.UNDERLYING) == Image.UNDERLYING;
        float diff = leading / 2;
        if (textwrap) {
            diff += leading;
        }
        float lowerleft = indentTop() - currentHeight - image.scaledHeight() -diff;
        float mt[] = image.matrix();
        float startPosition = indentLeft() - mt[4];
        if ((image.alignment() & Image.RIGHT) == Image.RIGHT) startPosition = indentRight() - image.scaledWidth() - mt[4];
        if ((image.alignment() & Image.MIDDLE) == Image.MIDDLE) startPosition = indentLeft() + ((indentRight() - indentLeft() - image.scaledWidth()) / 2) - mt[4];
        if (image.hasAbsoluteX()) startPosition = image.absoluteX();
        if (textwrap) {
            if (imageEnd < 0 || imageEnd < currentHeight + image.scaledHeight() + diff) {
                imageEnd = currentHeight + image.scaledHeight() + diff;
            }
            if ((image.alignment() & Image.RIGHT) == Image.RIGHT) {
            	// indentation suggested by Pelikan Stephan
                imageIndentRight += image.scaledWidth() + image.indentationLeft();
            }
            else {
            	// indentation suggested by Pelikan Stephan
                imageIndentLeft += image.scaledWidth() + image.indentationRight();
            }
        }
        else {
        	if ((image.alignment() & Image.RIGHT) == Image.RIGHT) startPosition -= image.indentationRight();
        	else if ((image.alignment() & Image.LEFT) == Image.LEFT) startPosition += image.indentationLeft();
        	else if ((image.alignment() & Image.MIDDLE) == Image.MIDDLE) startPosition += image.indentationLeft() - image.indentationRight();
        }
        graphics.addImage(image, mt[0], mt[1], mt[2], mt[3], startPosition, lowerleft - mt[5]);
        if (!(textwrap || underlying)) {
            currentHeight += image.scaledHeight() + diff;
            flushLines();
            text.moveText(0, - (image.scaledHeight() + diff));
            newLine();
        }
    }
    
    /**
     * Initializes a page.
     * <P>
     * If the footer/header is set, it is printed.
     * @throws DocumentException on error
     */
    
    private void initPage() throws DocumentException {
        
        // initialisation of some page objects
    	markPoint = 0;
        annotations = delayedAnnotations;
        delayedAnnotations = new ArrayList();
        pageResources = new PageResources();
        writer.resetContent();
        
        // the pagenumber is incremented
        pageN++;
        
        // graphics and text are initialized
        float oldleading = leading;
        int oldAlignment = alignment;
        
        if (marginMirroring && (getPageNumber() & 1) == 0) {
            marginRight = nextMarginLeft;
            marginLeft = nextMarginRight;
        }
        else {
            marginLeft = nextMarginLeft;
            marginRight = nextMarginRight;
        }
        marginTop = nextMarginTop;
        marginBottom = nextMarginBottom;
        imageEnd = -1;
        imageIndentRight = 0;
        imageIndentLeft = 0;
        graphics = new PdfContentByte(writer);
        text = new PdfContentByte(writer);
        text.beginText();
        text.moveText(left(), top());
        textEmptySize = text.size();
        text.reset();
        text.beginText();
        leading = 16;
        indentBottom = 0;
        indentTop = 0;
        currentHeight = 0;
        
        // backgroundcolors, etc...
        pageSize = nextPageSize;
        thisBoxSize = new HashMap(boxSize);
        if (pageSize.backgroundColor() != null
        || pageSize.hasBorders()
        || pageSize.borderColor() != null) {
            add(pageSize);
        }
        
        // if there is a watermark, the watermark is added
        if (watermark != null) {
            float mt[] = watermark.matrix();
            graphics.addImage(watermark, mt[0], mt[1], mt[2], mt[3], watermark.offsetX() - mt[4], watermark.offsetY() - mt[5]);
        }
        
        // if there is a footer, the footer is added
        if (footer != null) {
			/*
				Added by Edgar Leonardo Prieto Perilla
			*/
			// Avoid footer identation
			float tmpIndentLeft = indentLeft;
			float tmpIndentRight = indentRight;
                        // Begin added: Bonf (Marc Schneider) 2003-07-29
                        float tmpListIndentLeft = listIndentLeft;
                        float tmpImageIndentLeft = imageIndentLeft;
                        float tmpImageIndentRight = imageIndentRight;
                        // End added: Bonf (Marc Schneider) 2003-07-29

			indentLeft = indentRight = 0;
                        // Begin added: Bonf (Marc Schneider) 2003-07-29
                        listIndentLeft = 0;
                        imageIndentLeft = 0;
                        imageIndentRight = 0;
                        // End added: Bonf (Marc Schneider) 2003-07-29
			/*
				End Added by Edgar Leonardo Prieto Perilla
			*/

			footer.setPageNumber(pageN);
            leading = footer.paragraph().leading();
            add(footer.paragraph());
            // adding the footer limits the height
            indentBottom = currentHeight;
            text.moveText(left(), indentBottom());
            flushLines();
            text.moveText(-left(), -bottom());
            footer.setTop(bottom(currentHeight));
            footer.setBottom(bottom() - (0.75f * leading));
            footer.setLeft(left());
            footer.setRight(right());
            graphics.rectangle(footer);
            indentBottom = currentHeight + leading * 2;
            currentHeight = 0;

			/*
				Added by Edgar Leonardo Prieto Perilla
			*/
			indentLeft = tmpIndentLeft;
			indentRight = tmpIndentRight;
                        // Begin added: Bonf (Marc Schneider) 2003-07-29
                        listIndentLeft = tmpListIndentLeft;
                        imageIndentLeft = tmpImageIndentLeft;
                        imageIndentRight = tmpImageIndentRight;
                        // End added: Bonf (Marc Schneider) 2003-07-29
			/*
				End Added by Edgar Leonardo Prieto Perilla
			*/
        }
        
        // we move to the left/top position of the page
        text.moveText(left(), top());
        
        // if there is a header, the header = added
        if (header != null) {
			/*
				Added by Edgar Leonardo Prieto Perilla
			*/
			// Avoid header identation
			float tmpIndentLeft = indentLeft;
			float tmpIndentRight = indentRight;
                        // Begin added: Bonf (Marc Schneider) 2003-07-29
                        float tmpListIndentLeft = listIndentLeft;
                        float tmpImageIndentLeft = imageIndentLeft;
                        float tmpImageIndentRight = imageIndentRight;
                        // End added: Bonf (Marc Schneider) 2003-07-29

			indentLeft = indentRight = 0;
                        //  Added: Bonf
                        listIndentLeft = 0;
                        imageIndentLeft = 0;
                        imageIndentRight = 0;
                        // End added: Bonf
			/*
				End Added by Edgar Leonardo Prieto Perilla
			*/
			
			header.setPageNumber(pageN);
            leading = header.paragraph().leading();
            text.moveText(0, leading);
            add(header.paragraph());
            newLine();
            indentTop = currentHeight - leading;
            header.setTop(top() + leading);
            header.setBottom(indentTop() + leading * 2 / 3);
            header.setLeft(left());
            header.setRight(right());
            graphics.rectangle(header);
            flushLines();
            currentHeight = 0;

			/*
				Added by Edgar Leonardo Prieto Perilla
			*/
			// Restore identation
			indentLeft = tmpIndentLeft;
			indentRight = tmpIndentRight;
                        // Begin added: Bonf (Marc Schneider) 2003-07-29
                        listIndentLeft = tmpListIndentLeft;
                        imageIndentLeft = tmpImageIndentLeft;
                        imageIndentRight = tmpImageIndentRight;
                        // End added: Bonf (Marc Schneider) 2003-07-29
			/*
				End Added by Edgar Leonardo Prieto Perilla
			*/
        }
        
        pageEmpty = true;
        
        // if there is an image waiting to be drawn, draw it
        try {
            if (imageWait != null) {
                add(imageWait);
                imageWait = null;
            }
        }
        catch(Exception e) {
            throw new ExceptionConverter(e);
        }
        
        leading = oldleading;
        alignment = oldAlignment;
        carriageReturn();
        PdfPageEvent pageEvent = writer.getPageEvent();
        if (pageEvent != null) {
            if (firstPageEvent) {
                pageEvent.onOpenDocument(writer, this);
            }
            pageEvent.onStartPage(writer, this);
        }
        firstPageEvent = false;
    }
    
    /**
     * If the current line is not empty or null, it is added to the arraylist
     * of lines and a new empty line is added.
     * @throws DocumentException on error
     */
    
    private void carriageReturn() throws DocumentException {
        // the arraylist with lines may not be null
        if (lines == null) {
            lines = new ArrayList();
        }
        // If the current line is not null
        if (line != null) {
            // we check if the end of the page is reached (bugfix by Francois Gravel)
            if (currentHeight + line.height() + leading < indentTop() - indentBottom()) {
                // if so nonempty lines are added and the heigt is augmented
                if (line.size() > 0) {
                    currentHeight += line.height();
                    lines.add(line);
                    pageEmpty = false;
                }
            }
            // if the end of the line is reached, we start a new page
            else {
                newPage();
            }
        }
        if (imageEnd > -1 && currentHeight > imageEnd) {
            imageEnd = -1;
            imageIndentRight = 0;
            imageIndentLeft = 0;
        }
        // a new current line is constructed
        line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
    }
    
    /**
     * Adds the current line to the list of lines and also adds an empty line.
     * @throws DocumentException on error
     */
    
    private void newLine() throws DocumentException {
        lastElementType = -1;
        carriageReturn();
        if (lines != null && lines.size() > 0) {
            lines.add(line);
            currentHeight += line.height();
        }
        line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
    }
    
    /**
     * Writes all the lines to the text-object.
     *
     * @return the displacement that was caused
     * @throws DocumentException on error
     */
    
    private float flushLines() throws DocumentException {
        
        // checks if the ArrayList with the lines is not null
        if (lines == null) {
            return 0;
        }
        
        //add by Jin-Hsia Yang
        boolean newline=false;
        //end add by Jin-Hsia Yang
        
        // checks if a new Line has to be made.
        if (line != null && line.size() > 0) {
            lines.add(line);
            line = new PdfLine(indentLeft(), indentRight(), alignment, leading);
            
            //add by Jin-Hsia Yang
            newline=true;
            //end add by Jin-Hsia Yang
            
        }
        
        // checks if the ArrayList with the lines is empty
        if (lines.size() == 0) {
            return 0;
        }
        
        // initialisation of some parameters
        Object currentValues[] = new Object[2];
        PdfFont currentFont = null;
        float displacement = 0;
        PdfLine l;
        Float lastBaseFactor = new Float(0);
        currentValues[1] = lastBaseFactor;
        // looping over all the lines
        for (Iterator i = lines.iterator(); i.hasNext(); ) {
                        
            // this is a line in the loop
            l = (PdfLine) i.next();
            
            if(isNewpage && newline) { // fix Ken@PDI
                newline=false;
                text.moveText(l.indentLeft() - indentLeft() + listIndentLeft + paraIndent,-l.height());
            }
            else {
                text.moveText(l.indentLeft() - indentLeft() + listIndentLeft, -l.height());
            }
            
            // is the line preceeded by a symbol?
            if (l.listSymbol() != null) {
                ColumnText.showTextAligned(graphics, Element.ALIGN_LEFT, new Phrase(l.listSymbol()), text.getXTLM() - l.listIndent(), text.getYTLM(), 0);
            }
            
            currentValues[0] = currentFont;
            
            writeLineToContent(l, text, graphics, currentValues, writer.getSpaceCharRatio());
            
            currentFont = (PdfFont)currentValues[0];
            
            displacement += l.height();
            if (indentLeft() - listIndentLeft != l.indentLeft()) {
                text.moveText(indentLeft() - l.indentLeft() - listIndentLeft, 0);
            }
            
        }
        lines = new ArrayList();
        return displacement;
    }
    
    // methods to retrieve information
    
    /**
     * Gets the <CODE>PdfInfo</CODE>-object.
     *
     * @return	<CODE>PdfInfo</COPE>
     */
    
    PdfInfo getInfo() {
        return info;
    }
    
    /**
     * Gets the <CODE>PdfCatalog</CODE>-object.
     *
     * @param pages an indirect reference to this document pages
     * @return <CODE>PdfCatalog</CODE>
     */
    
    PdfCatalog getCatalog(PdfIndirectReference pages) {
        PdfCatalog catalog;
        if (rootOutline.getKids().size() > 0) {
            catalog = new PdfCatalog(pages, rootOutline.indirectReference(), writer);
        }
        else
            catalog = new PdfCatalog(pages, writer);
        if (openActionName != null) {
            PdfAction action = getLocalGotoAction(openActionName);
            catalog.setOpenAction(action);
        }
        else if (openActionAction != null)
            catalog.setOpenAction(openActionAction);
        
        if (additionalActions != null)   {
            catalog.setAdditionalActions(additionalActions);
        }
        
        if (pageLabels != null)
            catalog.setPageLabels(pageLabels);
        catalog.addNames(localDestinations, documentJavaScript, writer);
        catalog.setViewerPreferences(viewerPreferences);
        if (acroForm.isValid()) {
            try {
                catalog.setAcroForm(writer.addToBody(acroForm).getIndirectReference());
            }
            catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
        return catalog;
    }
    
    // methods concerning the layout
    
    /**
     * Returns the bottomvalue of a <CODE>Table</CODE> if it were added to this document.
     *
     * @param	table	the table that may or may not be added to this document
     * @return	a bottom value
     */
    
    float bottom(Table table) {
//    	 where will the table begin?
        float h = (currentHeight > 0) ? indentTop() - currentHeight - 2f * leading : indentTop();
        // constructing a PdfTable
        PdfTable tmp = getPdfTable(table, false);
        return tmp.bottom();
    }
    
    /**
     * Checks if a <CODE>PdfPTable</CODE> fits the current page of the <CODE>PdfDocument</CODE>.
     *
     * @param	table	the table that has to be checked
     * @param	margin	a certain margin
     * @return	<CODE>true</CODE> if the <CODE>PdfPTable</CODE> fits the page, <CODE>false</CODE> otherwise.
     */
    
    boolean fitsPage(PdfPTable table, float margin) {
            if (!table.isLockedWidth()) {
                float totalWidth = (indentRight() - indentLeft()) * table.getWidthPercentage() / 100;
                table.setTotalWidth(totalWidth);
            }
        // ensuring that a new line has been started.
        ensureNewLine();
            return table.getTotalHeight() <= indentTop() - currentHeight - indentBottom() - margin;
        }
    
    
    /**
     * Gets the current vertical page position.
     * @param ensureNewLine Tells whether a new line shall be enforced. This may cause side effects 
     *   for elements that do not terminate the lines they've started because those lines will get
     *   terminated. 
     * @return The current vertical page position.
     */
    public float getVerticalPosition(boolean ensureNewLine) {
        // ensuring that a new line has been started.
        if (ensureNewLine) {
          ensureNewLine();
        }
        return top() -  currentHeight - indentTop;
    }
    
    /**
     * Ensures that a new line has been started. 
     */
    private void ensureNewLine() {
      try {
        if ((lastElementType == Element.PHRASE) || 
            (lastElementType == Element.CHUNK)) {
          newLine();
          flushLines();
        }
      } catch (DocumentException ex) {
        throw new ExceptionConverter(ex);
        }
    }
    
    /**
     * Gets the indentation on the left side.
     *
     * @return	a margin
     */
    
    private float indentLeft() {
        return left(indentLeft + listIndentLeft + imageIndentLeft);
    }
    
    /**
     * Gets the indentation on the right side.
     *
     * @return	a margin
     */
    
    private float indentRight() {
        return right(indentRight + imageIndentRight);
    }
    
    /**
     * Gets the indentation on the top side.
     *
     * @return	a margin
     */
    
    private float indentTop() {
        return top(indentTop);
    }
    
    /**
     * Gets the indentation on the bottom side.
     *
     * @return	a margin
     */
    
    float indentBottom() {
        return bottom(indentBottom);
    }
    
    /**
     * Adds a named outline to the document .
     * @param outline the outline to be added
     * @param name the name of this local destination
     */
    void addOutline(PdfOutline outline, String name) {
        localDestination(name, outline.getPdfDestination());
    }
    
    /**
     * Gets the AcroForm object.
     * @return the PdfAcroform object of the PdfDocument
     */
    
    public PdfAcroForm getAcroForm() {
        return acroForm;
    }
    
    /**
     * Gets the root outline. All the outlines must be created with a parent.
     * The first level is created with this outline.
     * @return the root outline
     */
    public PdfOutline getRootOutline() {
        return rootOutline;
    }
        
    /**
     * Writes a text line to the document. It takes care of all the attributes.
     * <P>
     * Before entering the line position must have been established and the
     * <CODE>text</CODE> argument must be in text object scope (<CODE>beginText()</CODE>).
     * @param line the line to be written
     * @param text the <CODE>PdfContentByte</CODE> where the text will be written to
     * @param graphics the <CODE>PdfContentByte</CODE> where the graphics will be written to
     * @param currentValues the current font and extra spacing values
     * @param ratio
     * @throws DocumentException on error
     */
    void writeLineToContent(PdfLine line, PdfContentByte text, PdfContentByte graphics, Object currentValues[], float ratio)  throws DocumentException {
        PdfFont currentFont = (PdfFont)(currentValues[0]);
        float lastBaseFactor = ((Float)(currentValues[1])).floatValue();
        PdfChunk chunk;
        int numberOfSpaces;
        int lineLen;
        boolean isJustified;
        float hangingCorrection = 0;
        float hScale = 1;
        float lastHScale = Float.NaN;
        float baseWordSpacing = 0;
        float baseCharacterSpacing = 0;
        
        numberOfSpaces = line.numberOfSpaces();
        lineLen = line.toString().length();
        // does the line need to be justified?
        isJustified = line.hasToBeJustified() && (numberOfSpaces != 0 || lineLen > 1);
        if (isJustified) {
            if (line.isNewlineSplit() && line.widthLeft() >= (lastBaseFactor * (ratio * numberOfSpaces + lineLen - 1))) {
                if (line.isRTL()) {
                    text.moveText(line.widthLeft() - lastBaseFactor * (ratio * numberOfSpaces + lineLen - 1), 0);
                }
                baseWordSpacing = ratio * lastBaseFactor;
                baseCharacterSpacing = lastBaseFactor;
            }
            else {
                float width = line.widthLeft();
                PdfChunk last = line.getChunk(line.size() - 1);
                if (last != null) {
                    String s = last.toString();
                    char c;
                    if (s.length() > 0 && hangingPunctuation.indexOf((c = s.charAt(s.length() - 1))) >= 0) {
                        float oldWidth = width;
                        width += last.font().width(c) * 0.4f;
                        hangingCorrection = width - oldWidth;
                    }
                }
                float baseFactor = width / (ratio * numberOfSpaces + lineLen - 1);
                baseWordSpacing = ratio * baseFactor;
                baseCharacterSpacing = baseFactor;
                lastBaseFactor = baseFactor;
            }
        }
        
        int lastChunkStroke = line.getLastStrokeChunk();
        int chunkStrokeIdx = 0;
        float xMarker = text.getXTLM();
        float baseXMarker = xMarker;
        float yMarker = text.getYTLM();
        boolean adjustMatrix = false;
        
        // looping over all the chunks in 1 line
        for (Iterator j = line.iterator(); j.hasNext(); ) {
            chunk = (PdfChunk) j.next();
            Color color = chunk.color();
            hScale = 1;
            
            if (chunkStrokeIdx <= lastChunkStroke) {
                float width;
                if (isJustified) {
                    width = chunk.getWidthCorrected(baseCharacterSpacing, baseWordSpacing);
                }
                else
                    width = chunk.width();
                if (chunk.isStroked()) {
                    PdfChunk nextChunk = line.getChunk(chunkStrokeIdx + 1);
                    if (chunk.isAttribute(Chunk.BACKGROUND)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.BACKGROUND))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        float fontSize = chunk.font().size();
                        float ascender = chunk.font().getFont().getFontDescriptor(BaseFont.ASCENT, fontSize);
                        float descender = chunk.font().getFont().getFontDescriptor(BaseFont.DESCENT, fontSize);
                        Object bgr[] = (Object[])chunk.getAttribute(Chunk.BACKGROUND);
                        graphics.setColorFill((Color)bgr[0]);
                        float extra[] = (float[])bgr[1];
                        graphics.rectangle(xMarker - extra[0],
                            yMarker + descender - extra[1] + chunk.getTextRise(),
                            width - subtract + extra[0] + extra[2],
                            ascender - descender + extra[1] + extra[3]);
                        graphics.fill();
                        graphics.setGrayFill(0);
                    }
                    if (chunk.isAttribute(Chunk.UNDERLINE)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.UNDERLINE))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        Object unders[][] = (Object[][])chunk.getAttribute(Chunk.UNDERLINE);
                        Color scolor = null;
                        for (int k = 0; k < unders.length; ++k) {
                            Object obj[] = unders[k];
                            scolor = (Color)obj[0];
                            float ps[] = (float[])obj[1];
                            if (scolor == null)
                                scolor = color;
                            if (scolor != null)
                                graphics.setColorStroke(scolor);
                            float fsize = chunk.font().size();
                            graphics.setLineWidth(ps[0] + fsize * ps[1]);
                            float shift = ps[2] + fsize * ps[3];
                            int cap2 = (int)ps[4];
                            if (cap2 != 0)
                                graphics.setLineCap(cap2);
                            graphics.moveTo(xMarker, yMarker + shift);
                            graphics.lineTo(xMarker + width - subtract, yMarker + shift);
                            graphics.stroke();
                            if (scolor != null)
                                graphics.resetGrayStroke();
                            if (cap2 != 0)
                                graphics.setLineCap(0);
                        }
                        graphics.setLineWidth(1);
                    }
                    if (chunk.isAttribute(Chunk.ACTION)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.ACTION))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        text.addAnnotation(new PdfAnnotation(writer, xMarker, yMarker, xMarker + width - subtract, yMarker + chunk.font().size(), (PdfAction)chunk.getAttribute(Chunk.ACTION)));
                    }
                    if (chunk.isAttribute(Chunk.REMOTEGOTO)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.REMOTEGOTO))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        Object obj[] = (Object[])chunk.getAttribute(Chunk.REMOTEGOTO);
                        String filename = (String)obj[0];
                        if (obj[1] instanceof String)
                            remoteGoto(filename, (String)obj[1], xMarker, yMarker, xMarker + width - subtract, yMarker + chunk.font().size());
                        else
                            remoteGoto(filename, ((Integer)obj[1]).intValue(), xMarker, yMarker, xMarker + width - subtract, yMarker + chunk.font().size());
                    }
                    if (chunk.isAttribute(Chunk.LOCALGOTO)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.LOCALGOTO))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        localGoto((String)chunk.getAttribute(Chunk.LOCALGOTO), xMarker, yMarker, xMarker + width - subtract, yMarker + chunk.font().size());
                    }
                    if (chunk.isAttribute(Chunk.LOCALDESTINATION)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.LOCALDESTINATION))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        localDestination((String)chunk.getAttribute(Chunk.LOCALDESTINATION), new PdfDestination(PdfDestination.XYZ, xMarker, yMarker + chunk.font().size(), 0));
                    }
                    if (chunk.isAttribute(Chunk.GENERICTAG)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.GENERICTAG))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        Rectangle rect = new Rectangle(xMarker, yMarker, xMarker + width - subtract, yMarker + chunk.font().size());
                        PdfPageEvent pev = writer.getPageEvent();
                        if (pev != null)
                            pev.onGenericTag(writer, this, rect, (String)chunk.getAttribute(Chunk.GENERICTAG));
                    }
                    if (chunk.isAttribute(Chunk.PDFANNOTATION)) {
                        float subtract = lastBaseFactor;
                        if (nextChunk != null && nextChunk.isAttribute(Chunk.PDFANNOTATION))
                            subtract = 0;
                        if (nextChunk == null)
                            subtract += hangingCorrection;
                        float fontSize = chunk.font().size();
                        float ascender = chunk.font().getFont().getFontDescriptor(BaseFont.ASCENT, fontSize);
                        float descender = chunk.font().getFont().getFontDescriptor(BaseFont.DESCENT, fontSize);
                        PdfAnnotation annot = PdfFormField.shallowDuplicate((PdfAnnotation)chunk.getAttribute(Chunk.PDFANNOTATION));
                        annot.put(PdfName.RECT, new PdfRectangle(xMarker, yMarker + descender, xMarker + width - subtract, yMarker + ascender));
                        text.addAnnotation(annot);
                    }
                    float params[] = (float[])chunk.getAttribute(Chunk.SKEW);
                    Float hs = (Float)chunk.getAttribute(Chunk.HSCALE);
                    if (params != null || hs != null) {
                        float b = 0, c = 0;
                        if (params != null) {
                            b = params[0];
                            c = params[1];
                        }
                        if (hs != null)
                            hScale = hs.floatValue();
                        text.setTextMatrix(hScale, b, c, 1, xMarker, yMarker);
                    }
                    if (chunk.isImage()) {
                        Image image = chunk.getImage();
                        float matrix[] = image.matrix();
                        matrix[Image.CX] = xMarker + chunk.getImageOffsetX() - matrix[Image.CX];
                        matrix[Image.CY] = yMarker + chunk.getImageOffsetY() - matrix[Image.CY];
                        graphics.addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
                        text.moveText(xMarker + lastBaseFactor + image.scaledWidth() - text.getXTLM(), 0);
                    }
                }
                xMarker += width;
                ++chunkStrokeIdx;
            }

            if (chunk.font().compareTo(currentFont) != 0) {
                currentFont = chunk.font();
                text.setFontAndSize(currentFont.getFont(), currentFont.size());
            }
            float rise = 0;
            Object textRender[] = (Object[])chunk.getAttribute(Chunk.TEXTRENDERMODE);
            int tr = 0;
            float strokeWidth = 1;
            Color strokeColor = null;
            Float fr = (Float)chunk.getAttribute(Chunk.SUBSUPSCRIPT);
            if (textRender != null) {
                tr = ((Integer)textRender[0]).intValue() & 3;
                if (tr != PdfContentByte.TEXT_RENDER_MODE_FILL)
                    text.setTextRenderingMode(tr);
                if (tr == PdfContentByte.TEXT_RENDER_MODE_STROKE || tr == PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE) {
                    strokeWidth = ((Float)textRender[1]).floatValue();
                    if (strokeWidth != 1)
                        text.setLineWidth(strokeWidth);
                    strokeColor = (Color)textRender[2];
                    if (strokeColor == null)
                        strokeColor = color;
                    if (strokeColor != null)
                        text.setColorStroke(strokeColor);
                }
            }
            if (fr != null)
                rise = fr.floatValue();
            if (color != null)
                text.setColorFill(color);
            if (rise != 0)
                text.setTextRise(rise);
            if (chunk.isImage()) {
                adjustMatrix = true;
            }
            // If it is a CJK chunk or Unicode TTF we will have to simulate the
            // space adjustment.
            else if (isJustified && numberOfSpaces > 0 && chunk.isSpecialEncoding()) {
                if (hScale != lastHScale) {
                    lastHScale = hScale;
                    text.setWordSpacing(baseWordSpacing / hScale);
                    text.setCharacterSpacing(baseCharacterSpacing / hScale);
                }
                String s = chunk.toString();
                int idx = s.indexOf(' ');
                if (idx < 0)
                    text.showText(chunk.toString());
                else {
                    float spaceCorrection = - baseWordSpacing * 1000f / chunk.font.size() / hScale;
                    PdfTextArray textArray = new PdfTextArray(s.substring(0, idx));
                    int lastIdx = idx;
                    while ((idx = s.indexOf(' ', lastIdx + 1)) >= 0) {
                        textArray.add(spaceCorrection);
                        textArray.add(s.substring(lastIdx, idx));
                        lastIdx = idx;
                    }
                    textArray.add(spaceCorrection);
                    textArray.add(s.substring(lastIdx));
                    text.showText(textArray);
                }
            }
            else {
                if (isJustified && hScale != lastHScale) {
                    lastHScale = hScale;
                    text.setWordSpacing(baseWordSpacing / hScale);
                    text.setCharacterSpacing(baseCharacterSpacing / hScale);
                }
                text.showText(chunk.toString());
            }
            
            if (rise != 0)
                text.setTextRise(0);
            if (color != null)
                text.resetRGBColorFill();
            if (tr != PdfContentByte.TEXT_RENDER_MODE_FILL)
                text.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
            if (strokeColor != null)
                text.resetRGBColorStroke();
            if (strokeWidth != 1)
                text.setLineWidth(1);            
            if (chunk.isAttribute(Chunk.SKEW) || chunk.isAttribute(Chunk.HSCALE)) {
                adjustMatrix = true;
                text.setTextMatrix(xMarker, yMarker);
            }
        }
        if (isJustified) {
            text.setWordSpacing(0);
            text.setCharacterSpacing(0);
            if (line.isNewlineSplit())
                lastBaseFactor = 0;
        }
        if (adjustMatrix)
            text.moveText(baseXMarker - text.getXTLM(), 0);
        currentValues[0] = currentFont;
        currentValues[1] = new Float(lastBaseFactor);
    }
    
    /**
     * Implements a link to other part of the document. The jump will
     * be made to a local destination with the same name, that must exist.
     * @param name the name for this link
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    void localGoto(String name, float llx, float lly, float urx, float ury) {
        PdfAction action = getLocalGotoAction(name);
        annotations.add(new PdfAnnotation(writer, llx, lly, urx, ury, action));
    }
    
    PdfAction getLocalGotoAction(String name) {
        PdfAction action;
        Object obj[] = (Object[])localDestinations.get(name);
        if (obj == null)
            obj = new Object[3];
        if (obj[0] == null) {
            if (obj[1] == null) {
                obj[1] = writer.getPdfIndirectReference();
            }
            action = new PdfAction((PdfIndirectReference)obj[1]);
            obj[0] = action;
            localDestinations.put(name, obj);
        }
        else {
            action = (PdfAction)obj[0];
        }
        return action;
    }
    
    /**
     * The local destination to where a local goto with the same
     * name will jump to.
     * @param name the name of this local destination
     * @param destination the <CODE>PdfDestination</CODE> with the jump coordinates
     * @return <CODE>true</CODE> if the local destination was added,
     * <CODE>false</CODE> if a local destination with the same name
     * already existed
     */
    boolean localDestination(String name, PdfDestination destination) {
        Object obj[] = (Object[])localDestinations.get(name);
        if (obj == null)
            obj = new Object[3];
        if (obj[2] != null)
            return false;
        obj[2] = destination;
        localDestinations.put(name, obj);
        destination.addPage(writer.getCurrentPage());
        return true;
    }
    
    /**
     * Implements a link to another document.
     * @param filename the filename for the remote document
     * @param name the name to jump to
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury) {
        annotations.add(new PdfAnnotation(writer, llx, lly, urx, ury, new PdfAction(filename, name)));
    }
    
    /**
     * Implements a link to another document.
     * @param filename the filename for the remote document
     * @param page the page to jump to
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury) {
        writer.addAnnotation(new PdfAnnotation(writer, llx, lly, urx, ury, new PdfAction(filename, page)));
    }
    
    /** Sets the viewer preferences as the sum of several constants.
     * @param preferences the viewer preferences
     * @see PdfWriter#setViewerPreferences
     */
    
    public void setViewerPreferences(int preferences) {
        viewerPreferences |= preferences;
    }
    
    /** Implements an action in an area.
     * @param action the <CODE>PdfAction</CODE>
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
        writer.addAnnotation(new PdfAnnotation(writer, llx, lly, urx, ury, action));
    }
    
    void setOpenAction(String name) {
        openActionName = name;
        openActionAction = null;
    }
    
    void setOpenAction(PdfAction action) {
        openActionAction = action;
        openActionName = null;
    }
    
    void addAdditionalAction(PdfName actionType, PdfAction action)  {
        if (additionalActions == null)  {
            additionalActions = new PdfDictionary();
        }
        if (action == null)
            additionalActions.remove(actionType);
        else
            additionalActions.put(actionType, action);
        if (additionalActions.size() == 0)
            additionalActions = null;
    }
    
    void setPageLabels(PdfPageLabels pageLabels) {
        this.pageLabels = pageLabels;
    }
    
    void addJavaScript(PdfAction js) {
        if (js.get(PdfName.JS) == null)
            throw new RuntimeException("Only JavaScript actions are allowed.");
        try {
            documentJavaScript.add(writer.addToBody(js).getIndirectReference());
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
    
    void setCropBoxSize(Rectangle crop) {
        setBoxSize("crop", crop);
    }
    
    void setBoxSize(String boxName, Rectangle size) {
        if (size == null)
            boxSize.remove(boxName);
        else
            boxSize.put(boxName, new PdfRectangle(size));
    }
    
    void addCalculationOrder(PdfFormField formField) {
        acroForm.addCalculationOrder(formField);
    }
    
    /**
     * Gives the size of a trim, art, crop or bleed box, or null if not defined.
     * @param boxName crop, trim, art or bleed
     */
    Rectangle getBoxSize(String boxName) {
    	PdfRectangle r = (PdfRectangle)thisBoxSize.get(boxName);
    	if (r != null) return r.getRectangle();
    	return null;
    }
    
    void setSigFlags(int f) {
        acroForm.setSigFlags(f);
    }
    
    void addFormFieldRaw(PdfFormField field) {
        annotations.add(field);
        ArrayList kids = field.getKids();
        if (kids != null) {
            for (int k = 0; k < kids.size(); ++k)
                addFormFieldRaw((PdfFormField)kids.get(k));
        }
    }
    
    void addAnnotation(PdfAnnotation annot) {
        pageEmpty = false;
        if (annot.isForm()) {
            PdfFormField field = (PdfFormField)annot;
            if (field.getParent() == null)
                addFormFieldRaw(field);
        }
        else
            annotations.add(annot);
    }
    
    /**
     * Sets the display duration for the page (for presentations)
     * @param seconds   the number of seconds to display the page
     */
    void setDuration(int seconds) {
        if (seconds > 0)
            this.duration=seconds;
        else
            this.duration=-1;
    }
    
    /**
     * Sets the transition for the page
     * @param transition   the PdfTransition object
     */
    void setTransition(PdfTransition transition) {
        this.transition=transition;
    }

    void setPageAction(PdfName actionType, PdfAction action) {
        if (pageAA == null) {
            pageAA = new PdfDictionary();
        }
        pageAA.put(actionType, action);
    }
    
    /** Getter for property strictImageSequence.
     * @return Value of property strictImageSequence.
     *
     */
    boolean isStrictImageSequence() {
        return this.strictImageSequence;
    }
    
    /** Setter for property strictImageSequence.
     * @param strictImageSequence New value of property strictImageSequence.
     *
     */
    void setStrictImageSequence(boolean strictImageSequence) {
        this.strictImageSequence = strictImageSequence;
    }
 
    void setPageEmpty(boolean pageEmpty) {
        this.pageEmpty = pageEmpty;
    }
	/**
	 * Method added by Pelikan Stephan
	 * @see com.lowagie.text.DocListener#clearTextWrap()
	 */
	public void clearTextWrap() throws DocumentException {
		super.clearTextWrap();
		float tmpHeight = imageEnd - currentHeight;
		if (line != null) {
			tmpHeight += line.height();
		}
		if ((imageEnd > -1) && (tmpHeight > 0)) {
			carriageReturn();
			currentHeight += tmpHeight;
		}
	}
    
    ArrayList getDocumentJavaScript() {
        return documentJavaScript;
    }

    /**
     * @see com.lowagie.text.DocListener#setMarginMirroring(boolean)
     */
    public boolean setMarginMirroring(boolean MarginMirroring) {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        return super.setMarginMirroring(MarginMirroring);
    }
    
    void setThumbnail(Image image) throws PdfException, DocumentException {
        thumb = writer.getImageReference(writer.addDirectImageSimple(image));
    }

    static PdfAnnotation convertAnnotation(PdfWriter writer, Annotation annot) throws IOException {
         switch(annot.annotationType()) {
            case Annotation.URL_NET:
                return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((URL) annot.attributes().get(Annotation.URL)));
            case Annotation.URL_AS_STRING:
                return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE)));
            case Annotation.FILE_DEST:
                return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE), (String) annot.attributes().get(Annotation.DESTINATION)));
            case Annotation.SCREEN:
                boolean sparams[] = (boolean[])annot.attributes().get(Annotation.PARAMETERS);
                String fname = (String) annot.attributes().get(Annotation.FILE);
                String mimetype = (String) annot.attributes().get(Annotation.MIMETYPE);
                PdfFileSpecification fs;
                if (sparams[0])
                    fs = PdfFileSpecification.fileEmbedded(writer, fname, fname, null);
                else
                    fs = PdfFileSpecification.fileExtern(writer, fname);
                PdfAnnotation ann = PdfAnnotation.createScreen(writer, new Rectangle(annot.llx(), annot.lly(), annot.urx(), annot.ury()),
                        fname, fs, mimetype, sparams[1]);
                return ann;
            case Annotation.FILE_PAGE:
                return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE), ((Integer) annot.attributes().get(Annotation.PAGE)).intValue()));
            case Annotation.NAMED_DEST:
                return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction(((Integer) annot.attributes().get(Annotation.NAMED)).intValue()));
            case Annotation.LAUNCH:
                return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.APPLICATION),(String) annot.attributes().get(Annotation.PARAMETERS),(String) annot.attributes().get(Annotation.OPERATION),(String) annot.attributes().get(Annotation.DEFAULTDIR)));
            default:
                PdfDocument doc = writer.getPdfDocument();
                if (doc.line == null)
                    return null;
                PdfAnnotation an = new PdfAnnotation(writer, annot.llx(doc.indentRight() - doc.line.widthLeft()), annot.lly(doc.indentTop() - doc.currentHeight), annot.urx(doc.indentRight() - doc.line.widthLeft() + 20), annot.ury(doc.indentTop() - doc.currentHeight - 20), new PdfString(annot.title()), new PdfString(annot.content()));
                return an;
        }
    }
    /**
	 * @return an XmpMetadata byte array
	 */
	public byte[] createXmpMetadata() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	    	XmpWriter xmp = new XmpWriter(baos, getInfo());
	        xmp.close();
	    }
	    catch(IOException ioe) {
	        ioe.printStackTrace();
	    }
	    return baos.toByteArray();
	}
	
	int getMarkPoint() {
	    return markPoint;
	}
	 
	void incMarkPoint() {
	    ++markPoint;
	}
}