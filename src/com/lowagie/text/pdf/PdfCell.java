/*
 * $Id$
 * $Name$
 *
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URL;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Anchor;

/**
 * A <CODE>PdfCell</CODE> is the PDF translation of a <CODE>Cell</CODE>.
 * <P>
 * A <CODE>PdfCell</CODE> is an <CODE>ArrayList</CODE> of <CODE>PdfLine</CODE>s.
 *
 * @see		com.lowagie.text.Rectangle
 * @see		com.lowagie.text.Cell
 * @see		PdfLine
 * @see		PdfTable
 */

public class PdfCell extends Rectangle {
    
    // membervariables
    
/** These are the PdfLines in the Cell. */
    private ArrayList lines;
    
/** This is the leading of the lines. */
    private float leading;
    
/** This is the number of the row the cell is in. */
    private int rownumber;
    
/** This is the rowspan of the cell. */
    private int rowspan;
    
/** This is the cellspacing of the cell. */
    private float cellpadding;
    
/** This is the cellpadding of the cell. */
    private float cellspacing;
    
/** Indicates if this cell belongs to the header of a <CODE>PdfTable</CODE> */
    private boolean header = false;
    
    
    // constructors
    
/**
 * Constructs a <CODE>PdfCell</CODE>-object.
 *
 * @param	cell		the original <CODE>Cell</CODE>
 * @param	rownumber	the number of the <CODE>Row</CODE> the <CODE>Cell</CODE> was in.
 * @param	left		the left border of the <CODE>PdfCell</CODE>
 * @param	right		the right border of the <CODE>PdfCell</CODE>
 * @param	top			the top border of the <CODE>PdfCell</CODE>
 * @param	cellspacing	the cellspacing of the <CODE>Table</CODE>
 * @param	cellpadding	the cellpadding	of the <CODE>Table</CODE>
 */
    
    public PdfCell(Cell cell, int rownumber, float left, float right, float top, float cellspacing, float cellpadding) {
        // initialisation of the Rectangle
        super(left, top, right, top);
        setBorder(cell.border());
        setBorderWidth(cell.borderWidth());
        setBorderColor(cell.borderColor());
        setBackgroundColor(cell.backgroundColor());
        setGrayFill(cell.grayFill());
        
        // initialisation of the lines
        PdfChunk chunk;
        Element element;
        PdfChunk overflow;
        lines = new ArrayList();
        leading = cell.leading();
        int alignment = cell.horizontalAlignment();
        left += cellspacing + cellpadding;
        right -= cellspacing + cellpadding;
        
/*
 * Fixes bug 224848
 * 03/01/3001 David Freels
 * The height variable is adjusted just before the PDFLine is created
 * allowing the text to be rendered accordingly. The ALIGN_MIDDLE calculation
 * will be a little off for single row cells.
 */
        float height = leading + cellpadding;
        float rowSpan = (float)cell.rowspan();
        
        switch(cell.verticalAlignment())
        {
            case Element.ALIGN_BOTTOM:
                height *= rowSpan;
                break;
            case Element.ALIGN_MIDDLE:
                height *= (rowSpan / 1.5);
                break;
                default:   //Alignment will be top
                    if(rowSpan < 2)
                    {
                        height -= (height / 2.5);
                    }
                    break;
        }
        
        PdfLine line = new PdfLine(left, right, alignment, height);
        
        ArrayList allActions;
        int aCounter;
        // we loop over all the elements of the cell
        for (Iterator i = cell.getElements(); i.hasNext(); ) {
            element = (Element) i.next();
            switch(element.type()) {
                // if the element is a list
                case Element.LIST:
                    if (line.size() > 0) {
                        line.resetAlignment();
                        lines.add(line);
                    }
                    allActions = new ArrayList();
                    processActions(element, null, allActions);
                    aCounter = 0;
                    ListItem item;
                    // we loop over all the listitems
                    for (Iterator items = ((List)element).getItems().iterator(); items.hasNext(); ) {
                        item = (ListItem) items.next();
                        line = new PdfLine(left + item.indentationLeft(), right, alignment, leading);
                        line.setListItem(item);
                        for (Iterator j = item.getChunks().iterator(); j.hasNext(); ) {
                            chunk = new PdfChunk((Chunk) j.next(), (PdfAction)(allActions.get(aCounter++)));
                            while ((overflow = line.add(chunk)) != null) {
                                lines.add(line);
                                line = new PdfLine(left + item.indentationLeft(), right, alignment, leading);
                                chunk = overflow;
                            }
                            line.resetAlignment();
                            lines.add(line);
                            line = new PdfLine(left + item.indentationLeft(), right, alignment, leading);
                        }
                    }
                    line = new PdfLine(left, right, alignment, leading);
                    break;
                    // if the element is something else
                    default:
                        allActions = new ArrayList();
                        processActions(element, null, allActions);
                        aCounter = 0;
                        // we loop over the chunks
                        for (Iterator j = element.getChunks().iterator(); j.hasNext(); ) {
                            Chunk c = (Chunk) j.next();
                            chunk = new PdfChunk(c, (PdfAction)(allActions.get(aCounter++)));
                            while ((overflow = line.add(chunk)) != null) {
                                lines.add(line);
                                line = new PdfLine(left, right, alignment, leading);
                                chunk = overflow;
                            }
                        }
                        // if the element is a paragraph, section or chapter, we reset the alignment and add the line
                        switch (element.type()) {
                            case Element.PARAGRAPH:
                            case Element.SECTION:
                            case Element.CHAPTER:
                                line.resetAlignment();
                                lines.add(line);
                                line = new PdfLine(left, right, alignment, leading);
                        }
            }
        }
        if (line.size() > 0) {
            lines.add(line);
        }
/*		if (lines.size() > 0) {
                        ((PdfLine)lines.get(lines.size() - 1)).resetAlignment();
                }
 */
        // we set some additional parameters
        setBottom(top - leading * lines.size() - 2 * cellpadding - 6);
        this.cellpadding = cellpadding;
        this.cellspacing = cellspacing;
        
        rowspan = cell.rowspan();
        this.rownumber = rownumber;
    }
    
    // overriding of the Rectangle methods
    
/**
 * Returns the lower left x-coordinaat.
 *
 * @return		the lower left x-coordinaat
 */
    
    public float left() {
        return super.left(cellspacing);
    }
    
/**
 * Returns the upper right x-coordinate.
 *
 * @return		the upper right x-coordinate
 */
    
    public float right() {
        return super.right(cellspacing);
    }
    
/**
 * Returns the upper right y-coordinate.
 *
 * @return		the upper right y-coordinate
 */
    
    public float top() {
        return super.top(cellspacing);
    }
    
/**
 * Returns the lower left y-coordinate.
 *
 * @return		the lower left y-coordinate
 */
    
    public float bottom() {
        return super.bottom(cellspacing);
    }
    
    // methods
    
/**
 * Gets the lines of a cell that can be drawn between certain limits.
 * <P>
 * Remark: all the lines that can be drawn are removed from the object!
 *
 * @param	top		the top of the part of the table that can be drawn
 * @param	bottom	the bottom of the part of the table that can be drawn
 * @return	an <CODE>ArrayList</CODE> of <CODE>PdfLine</CODE>s
 */
    
    public ArrayList getLines(float top, float bottom) {
        
        // if the bottom of the page is higher than the top of the cell: do nothing
        if (top() < bottom) {
            return null;
        }
        
        // initialisations
        PdfLine line;
        float lineHeight;
        float currentPosition = Math.min(top(), top);
        ArrayList result = new ArrayList();
        
        // we loop over the lines
        int size = lines.size();
        for (int i = 0; i < size; i++) {
            line = (PdfLine) lines.get(i);
            lineHeight = line.height();
            currentPosition -= lineHeight;
            // if the currentPosition is higher than the bottom, we add the line to the result
            if (currentPosition > (bottom + 2.0f * cellpadding + 0.4f * leading)) { // bugfix by Tom Ring and Veerendra Namineni
                result.add(line);
                // as soon as a line is part of the result, we blank it out, except for table headers
                if (!header) {
                    lines.set(i, new PdfLine(left(-cellpadding - cellspacing), right(-cellpadding - cellspacing), Element.ALIGN_LEFT, leading));
                }
            }
        }
        // if the bottom of the cell is higher than the bottom of the page, the cell is written, so we can remove all lines
        
        // bugfix solving an endless loop problem by Leslie Baski
        if (!header && bottom <= bottom()) {
            lines = new ArrayList();
        }
        return result;
    }
    
/**
 * Indicates that this cell belongs to the header of a <CODE>PdfTable</CODE>.
 *
 * @return	<CODE>void</CODE>
 */
    
    final void setHeader() {
        header = true;
    }
    
/**
 * Checks if the cell may be removed.
 * <P>
 * Headers may allways be removed, even if they are drawn only partially:
 * they will be repeated on each following page anyway!
 *
 * @return	<CODE>true</CODE> if all the lines are allready drawn; <CODE>false</CODE> otherwise.
 */
    
    final boolean mayBeRemoved() {
        return (header || lines.size() < 1);
    }
    
/**
 * Returns the number of lines in the cell.
 *
 * @return	a value
 */
    
    public int size() {
        return lines.size();
    }
    
/**
 * Returns the number of lines in the cell that are not empty.
 *
 * @return	a value
 */
    
    public int remainingLines() {
        if (lines.size() == 0) return 0; 
        int result = 0;
        int size = lines.size();
        PdfLine line;
        for (int i = 0; i < size; i++) {
            line = (PdfLine) lines.get(i);
            if (line.size() > 0) result++;
        }
        return result;
    }
    
/**
 * Returns the height that can be used to draw text.
 */
    
    public float availableHeight() {
        return height() - 2 * cellpadding;
    }
    
/**
 * Returns the height that needs to be added if the remaining lines don't fit in the remaining height.
 */
    
    public float compensatingHeight(float lostTableBottom) {
        int remainingLines = remainingLines();
        if (remainingLines == 0) return 0f;
        int usedLines = lines.size() - remainingLines;
        float neededHeight = remainingLines * leading;
        float remainingHeight = availableHeight() - (usedLines * leading) - (lostTableBottom % leading);
        if (neededHeight > remainingHeight) {
            return 6 + neededHeight - remainingHeight;
        }
        return 0f;
    }
    
    // methods to retrieve membervariables
    
/**
 * Gets the leading of a cell.
 *
 * @return	the leading of the lines is the cell.
 */
    
    public float leading() {
        return leading;
    }
    
/**
 * Gets the number of the row this cell is in..
 *
 * @return	a number
 */
    
    public int rownumber() {
        return rownumber;
    }
    
/**
 * Gets the rowspan of a cell.
 *
 * @return	the rowspan of the cell
 */
    
    public int rowspan() {
        return rowspan;
    }
    
/**
 * Gets the cellspacing of a cell. .
 *
 * @return	a value
 */
    
    public float cellspacing() {
        return cellspacing;
    }
    
/**
 * Gets the cellpadding of a cell..
 *
 * @return	a value
 */
    
    public float cellpadding() {
        return cellpadding;
    }
    
/**
 * Processes all actions contained in the cell.
 */
    
    protected void processActions(Element element, PdfAction action, ArrayList allActions)
    {
        if (element.type() == Element.ANCHOR) {
            String url = ((Anchor)element).reference();
            if (url != null) {
                action = new PdfAction(url);
            }
        }
        Iterator i;
        switch (element.type()) {
            case Element.PHRASE:
            case Element.SECTION:
            case Element.ANCHOR:
            case Element.CHAPTER:
            case Element.LISTITEM:
            case Element.PARAGRAPH:
                for (i = ((ArrayList)element).iterator(); i.hasNext(); ) {
                    processActions((Element)i.next(), action, allActions);
                }
                break;
            case Element.CHUNK:
                allActions.add(action);
                break;
            case Element.LIST:
                for (i = ((List)element).getItems().iterator(); i.hasNext(); ) {
                    processActions((Element)i.next(), action, allActions);
                }
                break;
                default:
                    ArrayList tmp = element.getChunks();
                    int n = element.getChunks().size();
                    while (n-- > 0)
                        allActions.add(action);
                    break;
        }
    }
}
