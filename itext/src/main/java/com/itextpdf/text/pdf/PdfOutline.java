/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

/**
 * <CODE>PdfOutline</CODE> is an object that represents a PDF outline entry.
 * <P>
 * An outline allows a user to access views of a document by name.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 6.7 (page 104-106)
 *
 * @see		PdfDictionary
 */

public class PdfOutline extends PdfDictionary {

    // membervariables

    /** the <CODE>PdfIndirectReference</CODE> of this object */
    private PdfIndirectReference reference;

    /** value of the <B>Count</B>-key */
    private int count = 0;

    /** value of the <B>Parent</B>-key */
    private PdfOutline parent;

    /** value of the <B>Destination</B>-key */
    private PdfDestination destination;

    /** The <CODE>PdfAction</CODE> for this outline.
     */
    private PdfAction action;

    protected ArrayList<PdfOutline> kids = new ArrayList<PdfOutline>();

    protected PdfWriter writer;

    /** Holds value of property tag. */
    private String tag;

    /** Holds value of property open. */
    private boolean open;

    /** Holds value of property color. */
    private BaseColor color;

    /** Holds value of property style. */
    private int style = 0;

    // constructors

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for the <CODE>outlines object</CODE>.
     *
     * @param writer The PdfWriter you are adding the outline to
     */

    PdfOutline(PdfWriter writer) {
        super(OUTLINES);
        open = true;
        parent = null;
        this.writer = writer;
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param action the <CODE>PdfAction</CODE> for this outline item
     * @param title the title of this outline item
     */

    public PdfOutline(PdfOutline parent, PdfAction action, String title) {
        this(parent, action, title, true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>.
     *
     * @param parent the parent of this outline item
     * @param action the <CODE>PdfAction</CODE> for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
     */
    public PdfOutline(PdfOutline parent, PdfAction action, String title, boolean open) {
        super();
        this.action = action;
        initOutline(parent, title, open);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     */

    public PdfOutline(PdfOutline parent, PdfDestination destination, String title) {
        this(parent, destination, title, true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
     */
    public PdfOutline(PdfOutline parent, PdfDestination destination, String title, boolean open) {
        super();
        this.destination = destination;
        initOutline(parent, title, open);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param action the <CODE>PdfAction</CODE> for this outline item
     * @param title the title of this outline item
     */
    public PdfOutline(PdfOutline parent, PdfAction action, PdfString title) {
        this(parent, action, title, true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>.
     *
     * @param parent the parent of this outline item
     * @param action the <CODE>PdfAction</CODE> for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
     */
    public PdfOutline(PdfOutline parent, PdfAction action, PdfString title, boolean open) {
        this(parent, action, title.toString(), open);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     */

    public PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title) {
        this(parent, destination, title, true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
     */
    public PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title, boolean open) {
        this(parent, destination, title.toString(), true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param action the <CODE>PdfAction</CODE> for this outline item
     * @param title the title of this outline item
     */

    public PdfOutline(PdfOutline parent, PdfAction action, Paragraph title) {
        this(parent, action, title, true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>.
     *
     * @param parent the parent of this outline item
     * @param action the <CODE>PdfAction</CODE> for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
     */
    public PdfOutline(PdfOutline parent, PdfAction action, Paragraph title, boolean open) {
        super();
        StringBuffer buf = new StringBuffer();
        for (Chunk chunk: title.getChunks()) {
            buf.append(chunk.getContent());
        }
        this.action = action;
        initOutline(parent, buf.toString(), open);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     */

    public PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title) {
        this(parent, destination, title, true);
    }

    /**
     * Constructs a <CODE>PdfOutline</CODE>.
     * <P>
     * This is the constructor for an <CODE>outline entry</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
     */
    public PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title, boolean open) {
        super();
        StringBuffer buf = new StringBuffer();
        for (Object element : title.getChunks()) {
            Chunk chunk = (Chunk) element;
            buf.append(chunk.getContent());
        }
        this.destination = destination;
        initOutline(parent, buf.toString(), open);
    }


    // methods

    /** Helper for the constructors.
     * @param parent the parent outline
     * @param title the title for this outline
     * @param open <CODE>true</CODE> if the children are visible
     */
    void initOutline(PdfOutline parent, String title, boolean open) {
        this.open = open;
        this.parent = parent;
        writer = parent.writer;
        put(PdfName.TITLE, new PdfString(title, PdfObject.TEXT_UNICODE));
        parent.addKid(this);
        if (destination != null && !destination.hasPage()) // bugfix Finn Bock
            setDestinationPage(writer.getCurrentPage());
    }

    /**
     * Sets the indirect reference of this <CODE>PdfOutline</CODE>.
     *
     * @param reference the <CODE>PdfIndirectReference</CODE> to this outline.
     */

    public void setIndirectReference(PdfIndirectReference reference) {
        this.reference = reference;
    }

    /**
     * Gets the indirect reference of this <CODE>PdfOutline</CODE>.
     *
     * @return		the <CODE>PdfIndirectReference</CODE> to this outline.
     */

    public PdfIndirectReference indirectReference() {
        return reference;
    }

    /**
     * Gets the parent of this <CODE>PdfOutline</CODE>.
     *
     * @return		the <CODE>PdfOutline</CODE> that is the parent of this outline.
     */

    public PdfOutline parent() {
        return parent;
    }

    /**
     * Set the page of the <CODE>PdfDestination</CODE>-object.
     *
     * @param pageReference indirect reference to the page
     * @return <CODE>true</CODE> if this page was set as the <CODE>PdfDestination</CODE>-page.
     */

    public boolean setDestinationPage(PdfIndirectReference pageReference) {
        if (destination == null) {
            return false;
        }
        return destination.addPage(pageReference);
    }

    /**
     * Gets the destination for this outline.
     * @return the destination
     */
    public PdfDestination getPdfDestination() {
        return destination;
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    /**
     * returns the level of this outline.
     *
     * @return		a level
     */

    public int level() {
        if (parent == null) {
            return 0;
        }
        return parent.level() + 1;
    }

    /**
     * Returns the PDF representation of this <CODE>PdfOutline</CODE>.
     *
     * @param writer the PdfWriter
     * @param os
     * @throws IOException
     */

    @Override
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (color != null && !color.equals(BaseColor.BLACK)) {
            put(PdfName.C, new PdfArray(new float[]{color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f}));
        }
        int flag = 0;
        if ((style & Font.BOLD) != 0)
            flag |= 2;
        if ((style & Font.ITALIC) != 0)
            flag |= 1;
        if (flag != 0)
            put(PdfName.F, new PdfNumber(flag));
        if (parent != null) {
            put(PdfName.PARENT, parent.indirectReference());
        }
        if (destination != null && destination.hasPage()) {
            put(PdfName.DEST, destination);
        }
        if (action != null)
            put(PdfName.A, action);
        if (count != 0) {
            put(PdfName.COUNT, new PdfNumber(count));
        }
        super.toPdf(writer, os);
    }

    /**
     * Adds a kid to the outline
     * @param outline
     */
    public void addKid(PdfOutline outline) {
        kids.add(outline);
    }

    /**
     * Returns the kids of this outline
     * @return an ArrayList with PdfOutlines
     */
    public ArrayList<PdfOutline> getKids() {
        return kids;
    }

    /**
     * Sets the kids of this outline
     * @param kids
     */
    public void setKids(ArrayList<PdfOutline> kids) {
        this.kids = kids;
    }

    /** Getter for property tag.
     * @return Value of property tag.
     */
    public String getTag() {
        return tag;
    }

    /** Setter for property tag.
     * @param tag New value of property tag.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the title of this outline
     * @return the title as a String
     */
    public String getTitle() {
        PdfString title = (PdfString)get(PdfName.TITLE);
        return title.toString();
    }

    /**
     * Sets the title of this outline
     * @param title
     */
    public void setTitle(String title) {
        put(PdfName.TITLE, new PdfString(title, PdfObject.TEXT_UNICODE));
    }

    /** Getter for property open.
     * @return Value of property open.
     */
    public boolean isOpen() {
        return open;
    }

    /** Setter for property open.
     * @param open New value of property open.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /** Getter for property color.
     * @return Value of property color.
     *
     */
    public BaseColor getColor() {
        return this.color;
    }

    /** Setter for property color.
     * @param color New value of property color.
     *
     */
    public void setColor(BaseColor color) {
        this.color = color;
    }

    /** Getter for property style.
     * @return Value of property style.
     *
     */
    public int getStyle() {
        return this.style;
    }

    /** Setter for property style.
     * @param style New value of property style.
     *
     */
    public void setStyle(int style) {
        this.style = style;
    }

}
