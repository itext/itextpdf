/*
 * Created on Aug 6, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.lowagie.text.rtf.headerfooter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.document.RtfDocument;


/**
 * The RtfHeaderFooterGroup holds 0 - 3 RtfHeaderFooters that create a group
 * of headers or footers.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfHeaderFooterGroup extends HeaderFooter implements RtfBasicElement {
    
    /**
     * This RtfHeaderFooterGroup contains no RtfHeaderFooter objects
     */
    private static final int MODE_NONE = 0;
    /**
     * This RtfHeaderFooterGroup contains one RtfHeaderFooter object
     */
    private static final int MODE_SINGLE = 1;
    /**
     * This RtfHeaderFooterGroup contains two or three RtfHeaderFooter objects
     */
    private static final int MODE_MULTIPLE = 2;
    
    /**
     * The current mode of this RtfHeaderFooterGroup. Defaults to MODE_NONE
     */
    private int mode = MODE_NONE;
    /**
     * The current type of this RtfHeaderFooterGroup. Defaults to RtfHeaderFooter.TYPE_HEADER
     */
    private int type = RtfHeaderFooter.TYPE_HEADER;
    
    /**
     * The RtfHeaderFooter for all pages
     */
    private RtfHeaderFooter headerAll = null;
    /**
     * The RtfHeaderFooter for the first page
     */
    private RtfHeaderFooter headerFirst = null;
    /**
     * The RtfHeaderFooter for the left hand pages
     */
    private RtfHeaderFooter headerLeft = null;
    /**
     * The RtfHeaderFooter for the right hand pages
     */
    private RtfHeaderFooter headerRight = null;
    /**
     * The RtfDocument this RtfHeaderFooterGroup belongs to
     */
    private RtfDocument document = null;

    /**
     * Constructs a RtfHeaderGroup to which you add headers/footers using 
     * via the setHeaderFooter method.
     *
     */
    public RtfHeaderFooterGroup() {
        super(new Phrase(""), false);
        this.mode = MODE_NONE;
    }
    
    /**
     * Constructs a certain type of RtfHeaderFooterGroup. RtfHeaderFooter.TYPE_HEADER
     * and RtfHeaderFooter.TYPE_FOOTER are valid values for type.
     * 
     * @param doc The RtfDocument this RtfHeaderFooter belongs to
     * @param type The type of RtfHeaderFooterGroup to create
     */
    public RtfHeaderFooterGroup(RtfDocument doc, int type) {
        super(new Phrase(""), false);
        this.document = doc;
        this.type = type;
    }
    
    /**
     * Constructs a RtfHeaderFooterGroup by copying the content of the original
     * RtfHeaderFooterGroup
     * 
     * @param doc The RtfDocument this RtfHeaderFooter belongs to
     * @param headerFooter The RtfHeaderFooterGroup to copy
     * @param type The type of RtfHeaderFooterGroup to create
     */
    public RtfHeaderFooterGroup(RtfDocument doc, RtfHeaderFooterGroup headerFooter, int type) {
        super(new Phrase(""), false);
        this.document = doc;
        this.mode = headerFooter.getMode();
        this.type = type;
        if(headerFooter.getHeaderAll() != null) {
            this.headerAll = new RtfHeaderFooter(this.document, headerFooter.getHeaderAll(), RtfHeaderFooter.DISPLAY_ALL_PAGES);
        }
        if(headerFooter.getHeaderFirst() != null) {
            this.headerFirst = new RtfHeaderFooter(this.document, headerFooter.getHeaderFirst(), RtfHeaderFooter.DISPLAY_FIRST_PAGE);
        }
        if(headerFooter.getHeaderLeft() != null) {
            this.headerLeft = new RtfHeaderFooter(this.document, headerFooter.getHeaderLeft(), RtfHeaderFooter.DISPLAY_LEFT_PAGES);
        }
        if(headerFooter.getHeaderRight() != null) {
            this.headerRight = new RtfHeaderFooter(this.document, headerFooter.getHeaderRight(), RtfHeaderFooter.DISPLAY_RIGHT_PAGES);
        }
        setType(this.type);
    }
    
    /**
     * Constructs a RtfHeaderFooterGroup for a certain RtfHeaderFooter.
     * 
     * @param doc The RtfDocument this RtfHeaderFooter belongs to
     * @param headerFooter The RtfHeaderFooter to display
     * @param type The typ of RtfHeaderFooterGroup to create
     */
    public RtfHeaderFooterGroup(RtfDocument doc, RtfHeaderFooter headerFooter, int type) {
        super(new Phrase(""), false);
        this.document = doc;
        this.type = type;
        this.mode = MODE_SINGLE;
        headerAll = headerFooter;
        headerAll.setType(this.type);
    }
    
    /**
     * Constructs a RtfHeaderGroup for a certain HeaderFooter
     * 
     * @param doc The RtfDocument this RtfHeaderFooter belongs to
     * @param headerFooter The HeaderFooter to display
     * @param type The typ of RtfHeaderFooterGroup to create
     */
    public RtfHeaderFooterGroup(RtfDocument doc, HeaderFooter headerFooter, int type) {
        super(new Phrase(""), false);
        this.document = doc;
        this.type = type;
        this.mode = MODE_SINGLE;
        headerAll = new RtfHeaderFooter(doc, headerFooter, type, RtfHeaderFooter.DISPLAY_ALL_PAGES);
        headerAll.setType(this.type);
    }
    
    /**
     * Sets the RtfDocument this RtfElement belongs to
     * 
     * @param doc The RtfDocument to use
     */
    public void setRtfDocument(RtfDocument doc) {
        this.document = doc;
        if(headerAll != null) {
            headerAll.setRtfDocument(this.document);
        }
        if(headerFirst != null) {
            headerFirst.setRtfDocument(this.document);
        }
        if(headerLeft != null) {
            headerLeft.setRtfDocument(this.document);
        }
        if(headerRight != null) {
            headerRight.setRtfDocument(this.document);
        }
    }
    
    /**
     * Write the content of this RtfHeaderFooterGroup.
     * 
     * @return A byte array with the content of this RtfHeaderFooterGroup
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            if(this.mode == MODE_SINGLE) {
                result.write(headerAll.write());
            } else if(this.mode == MODE_MULTIPLE) {
                if(headerFirst != null) {
                    result.write(headerFirst.write());
                }
                if(headerLeft != null) {
                    result.write(headerLeft.write());
                }
                if(headerRight != null) {
                    result.write(headerRight.write());
                }
                if(headerAll != null) {
                    result.write(headerAll.write());
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Set a RtfHeaderFooter to be displayed at a certain position
     * 
     * @param headerFooter The RtfHeaderFooter to display
     * @param displayAt The display location to use
     */
    public void setHeaderFooter(RtfHeaderFooter headerFooter, int displayAt) {
        this.mode = MODE_MULTIPLE;
        headerFooter.setRtfDocument(this.document);
        headerFooter.setType(this.type);
        headerFooter.setDisplayAt(displayAt);
        switch(displayAt) {
            case RtfHeaderFooter.DISPLAY_ALL_PAGES:
                headerAll = headerFooter;
            	break;
            case RtfHeaderFooter.DISPLAY_FIRST_PAGE:
                headerFirst = headerFooter;
                break;
            case RtfHeaderFooter.DISPLAY_LEFT_PAGES:
                headerLeft = headerFooter;
                break;
            case RtfHeaderFooter.DISPLAY_RIGHT_PAGES:
                headerRight = headerFooter;
                break;
        }
    }
    
    /**
     * Set a HeaderFooter to be displayed at a certain position
     * 
     * @param headerFooter The HeaderFooter to set
     * @param displayAt The display location to use
     */
    public void setHeaderFooter(HeaderFooter headerFooter, int displayAt) {
        this.mode = MODE_MULTIPLE;
        switch(displayAt) {
            case RtfHeaderFooter.DISPLAY_ALL_PAGES:
                headerAll = new RtfHeaderFooter(this.document, headerFooter, this.type, displayAt);
            	break;
            case RtfHeaderFooter.DISPLAY_FIRST_PAGE:
                headerFirst = new RtfHeaderFooter(this.document, headerFooter, this.type, displayAt);
                break;
            case RtfHeaderFooter.DISPLAY_LEFT_PAGES:
                headerLeft = new RtfHeaderFooter(this.document, headerFooter, this.type, displayAt);
                break;
            case RtfHeaderFooter.DISPLAY_RIGHT_PAGES:
                headerRight = new RtfHeaderFooter(this.document, headerFooter, this.type, displayAt);
                break;
        }
    }
    
    /**
     * Set that this RtfHeaderFooterGroup should have a title page. If only
     * a header / footer for all pages exists, then it will be copied to the
     * first page aswell.
     */
    public void setHasTitlePage() {
        if(this.mode == MODE_SINGLE) {
            headerFirst = new RtfHeaderFooter(this.document, headerAll, RtfHeaderFooter.DISPLAY_FIRST_PAGE);
            headerFirst.setType(this.type);
        }
    }
    
    /**
     * Set that this RtfHeaderFooterGroup should have facing pages. If only
     * a header / footer for all pages exists, then it will be copied to the left
     * and right pages aswell.
     */
    public void setHasFacingPages() {
        if(this.mode == MODE_SINGLE) {
            this.mode = MODE_MULTIPLE;
            headerLeft = new RtfHeaderFooter(this.document, headerAll, RtfHeaderFooter.DISPLAY_LEFT_PAGES);
            headerLeft.setType(this.type);
            headerRight = new RtfHeaderFooter(this.document, headerAll, RtfHeaderFooter.DISPLAY_RIGHT_PAGES);
            headerRight.setType(this.type);
            headerAll = null;
        }
    }
    
    /**
     * Get whether this RtfHeaderFooterGroup has a titlepage
     * 
     * @return Whether this RtfHeaderFooterGroup has a titlepage
     */
    public boolean hasTitlePage() {
        return (headerFirst != null);
    }
    
    /**
     * Get whether this RtfHeaderFooterGroup has facing pages
     * 
     * @return Whether this RtfHeaderFooterGroup has facing pages
     */
    public boolean hasFacingPages() {
        return (headerLeft != null || headerRight != null);
    }

    /**
     * Unused
     */
    public void setInTable(boolean inTable) {
    }
    
    /**
     * Unused
     */
    public void setInHeader(boolean inHeader) {
    }
    
    /**
     * Set the type of this RtfHeaderFooterGroup. RtfHeaderFooter.TYPE_HEADER
     * or RtfHeaderFooter.TYPE_FOOTER. Also sets the type for all RtfHeaderFooters
     * of this RtfHeaderFooterGroup.
     * 
     * @param type The type to use
     */
    public void setType(int type) {
        this.type = type;
        if(headerAll != null) {
            headerAll.setType(this.type);
        }
        if(headerFirst != null) {
            headerFirst.setType(this.type);
        }
        if(headerLeft != null) {
            headerLeft.setType(this.type);
        }
        if(headerRight != null) {
            headerRight.setType(this.type);
        }
    }
    
    /**
     * Gets the mode of this RtfHeaderFooterGroup
     * 
     * @return The mode of this RtfHeaderFooterGroup
     */
    protected int getMode() {
        return this.mode;
    }
    
    /**
     * Gets the RtfHeaderFooter for all pages
     * 
     * @return The RtfHeaderFooter for all pages 
     */
    protected RtfHeaderFooter getHeaderAll() {
        return headerAll;
    }

    /**
     * Gets the RtfHeaderFooter for the title page
     * 
     * @return The RtfHeaderFooter for the title page 
     */
    protected RtfHeaderFooter getHeaderFirst() {
        return headerFirst;
    }

    /**
     * Gets the RtfHeaderFooter for all left hand pages
     * 
     * @return The RtfHeaderFooter for all left hand pages 
     */
    protected RtfHeaderFooter getHeaderLeft() {
        return headerLeft;
    }

    /**
     * Gets the RtfHeaderFooter for all right hand pages
     * 
     * @return The RtfHeaderFooter for all right hand pages 
     */
    protected RtfHeaderFooter getHeaderRight() {
        return headerRight;
    }
}
