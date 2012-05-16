package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.api.Spaceable;

public class PdfDiv implements LargeElement, Spaceable {
    public static final int NONE_FLOAT = -1;

    public static final int LEFT_FLOAT = 0;

    public static final int RIGHT_FLOAT = 1;

    public static final int STATIC_POSITION = 0;

    public static final int ABSOLUTE_POSITION = 1;

    public static final int FIXED_POSITION = 2;

    public static final int RELATIVE_POSITION = 3;

    private ArrayList<Element> content;

    private Float left = null;

    private Float top = null;

    private Float right = null;

    private Float bottom = null;

    private Float width = null;

    private Float height = null;

    private int textAlignment = Element.ALIGN_UNDEFINED;

    private float paddingLeft = 0;

    private float paddingRight = 0;

    private float paddingTop = 0;

    private float paddingBottom = 0;

    private int floatPosition = NONE_FLOAT;

    private int position = STATIC_POSITION;

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
     * @return textAlignment
     */
    public int getTextAlignment() {
        return this.textAlignment;
    }


    /**
     * Sets the alignment of this paragraph.
     *
     * @param	textAlignment		the new alignment
     */
    public void setTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void addElement(Element element) {
        content.add(element);
    }

    public Float getLeft() {
        return this.left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getRight() {
        return this.right;
    }

    public void setRight(Float right) {
        this.right = right;
    }

    public Float getTop() {
        return this.top;
    }

    public void setTop(Float top) {
        this.top = top;
    }

    public Float getBottom() {
        return this.bottom;
    }

    public void setBottom(Float bottom) {
        this.bottom = bottom;
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

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

        public int getFloatPosition() {
        return floatPosition;
    }

    public void setFloatPosition(int floatPosition) {
        this.floatPosition = floatPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Element popFirstElement() {
        Element firstElement = null;
        if (content.size() > 0) {
            firstElement = content.get(0);
            content.remove(0);
        }
        return firstElement;
    }

    public Rectangle writeContent(final PdfContentByte canvas, final float llx, final float lly, final float urx, final float ury) {
        return new Rectangle(0, 0, 0, 0);
    }
}
