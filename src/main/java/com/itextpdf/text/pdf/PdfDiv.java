package com.itextpdf.text.pdf;

import java.util.List;
import java.util.ArrayList;

import com.itextpdf.text.*;
import com.itextpdf.text.api.Spaceable;

public class PdfDiv implements LargeElement, Spaceable {

    private ArrayList<Element> content;

    private Float xPos = null;

    private Float yPos = null;

    private Float width = null;

    private Float height = null;

    private int alignment = Element.ALIGN_UNDEFINED;

    /**
     * Indicates if the PdfPTable is complete once added to the document.
     */
    protected boolean complete = true;
    /**
     * The spacing before the table.
     */
    protected float spacingBefore;

    /**
     * The spacing after the table.
     */
    protected float spacingAfter;

    public PdfDiv() {
        content = new ArrayList<Element>();
    }

    /**
	 * @see com.itextpdf.text.LargeElement#flushContent()
	 */
	public void flushContent() {
		//content.clear();
	}

	/**
	 * @see com.itextpdf.text.LargeElement#isComplete()
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * @see com.itextpdf.text.LargeElement#setComplete(boolean)
	 */
	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public List<Chunk> getChunks() {
        return new ArrayList<Chunk>();
    }

    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return Element.DIV;
    }

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(final ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }

    /**
     * Sets the spacing before this table.
     *
     * @param	spacing		the new spacing
     */
    public void setSpacingBefore(final float spacing) {
        this.spacingBefore = spacing;
    }

    /**
     * Sets the spacing after this table.
     *
     * @param	spacing		the new spacing
     */
    public void setSpacingAfter(final float spacing) {
        this.spacingAfter = spacing;
    }

    /**
     * Gets the spacing before this table.
     *
     * @return	the spacing
     */
    public float getSpacingBefore() {
        return spacingBefore;
    }

    /**
     * Gets the spacing after this table.
     *
     * @return	the spacing
     */
    public float getSpacingAfter() {
        return spacingAfter;
    }

    /**
     * Gets the alignment of this paragraph.
     *
     * @return alignment
     */
    public int getAlignment() {
        return alignment;
    }


    /**
     * Sets the alignment of this paragraph.
     *
     * @param	alignment		the new alignment
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void addElement(Element element) {
        content.add(element);
    }

    public Float getXPos() {
        return this.xPos;
    }

    public void setXPos(Float xPos) {
        this.xPos = xPos;
    }

    public Float getYPos() {
        return this.yPos;
    }

    public void setYPos(Float yPos) {
        this.yPos = yPos;
    }

    public Float getWidth() {
        return this.width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return this.height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Element popFirstElement() {
        Element firstElement = null;
        if (content.size() > 0) {
            firstElement = content.get(0);
            content.remove(0);
        }
        return firstElement;
    }
}
