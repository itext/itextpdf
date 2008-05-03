package com.lowagie.text;

import java.util.ArrayList;

import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Draws a horizontal line at the current position. 
 * @since	2.1.2
 */
public class GenericLine extends PdfTemplate implements Element {

	/**
	 * Allows you to define the length of the line as a percentage
	 * of the available page width.
	 * @since	2.1.2
	 */
	protected float widthPercentage = 100;
	/**
	 * The height necessary to drawn the generic line (similar to a leading
	 * in other objects). The default value is twice the current leading.
	 * @since	2.1.2 
	 */
	protected float advanceY = Float.NaN;
	/**
	 * The minimum vertical space needed to draw this generic line (if
	 * there is less space, a newPage() will be triggered). Most of the
	 * time minimumY will be equal to advanceY minus the offset.
	 * Reason: you don't need the full height to draw a line at the end
	 * of a page.
	 * @since	2.1.2
	 */
	protected float minimumY = Float.NaN;
	/**
	 * The offset used when drawing the line. By default half of advanceY.
	 * @since	2.1.2
	 */
	protected float verticalOffset = Float.NaN;
	/**
	 * The alignment of the line.
	 * @since	2.1.2
	 */
	protected int horizontalAlignment = Element.ALIGN_CENTER;
	
	/**
	 * Creates a generic line object.
	 * You can use this object as is to draw a line.
	 * You can use super class methods to change the way
	 * the line is rendered (change its color, dash pattern, thickness,...).
	 * Or you can decide not to draw any line, but to use this object
	 * to draw other stuff.
	 * @since	2.1.2
	 */
	public GenericLine() {
		super();
	}

	/**
	 * Gets the length of the line as a percentage of the available
	 * page width. If 0 no line is drawn.
	 * @return the widthPercentage
	 * @since	2.1.2
	 */
	public float getWidthPercentage() {
		return widthPercentage;
	}

	/**
	 * Sets the length of the line as a percentage of the available
	 * page width. Set to 0 if you don't want to draw a line.
	 * @param widthPercentage the widthPercentage to set
	 * @since	2.1.2
	 */
	public void setWidthPercentage(float widthPercentage) {
		this.widthPercentage = widthPercentage;
	}

	/**
	 * Returns the amount of vertical space that will be
	 * consumed when drawing this object. 
	 * @return a leading
	 * @since	2.1.2
	 */
	public float getAdvanceY() {
		return advanceY;
	}

	/**
	 * Sets the amount of vertical space that will be consumed
	 * when drawing this object.
	 * @param advanceY	a value for the height of this object.
	 * @since	2.1.2
	 */
	public void setAdvanceY(float advanceY) {
		this.advanceY = advanceY;
	}

	/**
	 * Returns the minimum amount of vertical space
	 * that needs to be available before drawing this object. 
	 * @return	the minimum space that needs to be available before drawing the line
	 * @since	2.1.2
	 */
	public float getMinimumY() {
		return minimumY;
	}

	/**
	 * Sets the minimum amount of vertical space that
	 * needs to be available before drawing this object.
	 * @param minumumY	a minimum height that needs to be available before drawing this object
	 * @since	2.1.2
	 */
	public void setMinimumY(float minimumY) {
		this.minimumY = minimumY;
	}

	/**
	 * Returns the vertical offset of the line.
	 * @return the verticalOffset
	 * @since	2.1.2
	 */
	public float getVerticalOffset() {
		return verticalOffset;
	}

	/**
	 * Sets the vertical offset of the line.
	 * @param verticalOffset the verticalOffset to set
	 * @since	2.1.2
	 */
	public void setVerticalOffset(float verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	/**
	 * Sets the horizontal alignment of the line.
	 * @return the horizontalAlignment
	 * @since	2.1.2
	 */
	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * Gets the horizontal alignment of the line.
	 * @param horizontalAlignment the horizontalAlignment to set
	 * @since	2.1.2
	 */
	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 * @see com.lowagie.text.Element#getChunks()
	 */
	public ArrayList getChunks() {
		return new ArrayList();
	}

	/**
	 * @see com.lowagie.text.Element#isContent()
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.lowagie.text.Element#isNestable()
	 */
	public boolean isNestable() {
		return false;
	}

	/**
	 * @see com.lowagie.text.Element#process(ElementListener)
	 */
	public boolean process(ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException e) {
			return false;
		}
	}

	/**
	 * @see com.lowagie.text.Element#type()
	 */
	public int type() {
		return Element.LINE;
	}
}