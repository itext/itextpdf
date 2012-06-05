package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.*;
import com.itextpdf.text.api.Spaceable;

public class PdfDiv implements Element, Spaceable {
    public enum FloatType {NONE, LEFT, RIGHT};

    public enum PositionType {STATIC, ABSOLUTE, FIXED, RELATIVE};

    private ArrayList<Element> content;

    private Float left = null;

    private Float top = null;

    private Float right = null;

    private Float bottom = null;

    private Float width = null;

    private Float height = null;

    private Float percentageHeight = null;

    private Float percentageWidth = null;

    private float contentWidth = 0;

    private float contentHeight = 0;

    private int textAlignment = Element.ALIGN_UNDEFINED;

    private float paddingLeft = 0;

    private float paddingRight = 0;

    private float paddingTop = 0;

    private float paddingBottom = 0;

    private FloatType floatType = FloatType.NONE;

    private PositionType position = PositionType.STATIC;

    private FloatLayout floatLayout = null;

    public float getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(float contentWidth) {
        this.contentWidth = contentWidth;
    }

    public float getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(float contentHeight) {
        this.contentHeight = contentHeight;
    }

    public float getActualHeight() {
        return height != null && height >= contentHeight ? height : contentHeight;
    }

    public float getActualWidth() {
        return width != null && width >= contentWidth ? width : contentWidth;
    }

    public Float getPercentageHeight() {
        return percentageHeight;
    }

    public void setPercentageHeight(Float percentageHeight) {
        this.percentageHeight = percentageHeight;
    }

    public Float getPercentageWidth() {
        return percentageWidth;
    }

    public void setPercentageWidth(Float percentageWidth) {
        this.percentageWidth = percentageWidth;
    }

    public BaseColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private BaseColor backgroundColor = null;

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

    public FloatType getFloatType() {
        return floatType;
    }

    public void setFloatType(FloatType floatType) {
        this.floatType = floatType;
    }

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
    }

    public ArrayList<Element> getContent() {
        return content;
    }

    public void setContent(ArrayList<Element> content) {
        this.content = content;
    }

    public int layout(final ColumnText compositeColumn, boolean simulate, final float llx, final float lly, final float urx, final float ury) throws DocumentException {

        float leftX = Math.min(llx, urx);
        float maxY = Math.max(lly, ury);
        float minY = Math.min(lly, ury);
        float rightX = Math.max(llx, urx);
        float yLine = maxY;

        if (width != null && width > 0) {
            if (width < rightX - leftX) {
                rightX = leftX + width;
            } else if (width > rightX - leftX) {
                return ColumnText.NO_MORE_COLUMN;
            }
        } else if (percentageWidth != null) {
            contentWidth = (rightX - leftX) * percentageWidth;
            rightX = leftX + contentWidth;
        }

        if (height != null && height > 0) {
            if (height < maxY - minY) {
                minY = maxY - height;
            } else if (height > maxY - minY) {
                return ColumnText.NO_MORE_COLUMN;
            }
        } else if (percentageHeight != null) {
            contentHeight = (maxY - minY) * percentageHeight;
            minY = maxY - contentHeight;
        }

        if (!simulate && position == PdfDiv.PositionType.RELATIVE) {
            Float translationX = null;
            if (left != null) {
                translationX = left;
            } else if (right != null) {
                translationX = -right;
            } else {
                translationX = 0f;
            }

            Float translationY = null;
            if (top != null) {
                translationY = -top;
            } else if (bottom != null) {
                translationY = bottom;
            } else {
                translationY = 0f;
            }
            compositeColumn.getCanvas().saveState();
            compositeColumn.getCanvas().transform(new AffineTransform(1f, 0, 0, 1f, translationX, translationY));
        }

        if (!simulate) {
            if (backgroundColor != null && getActualWidth() > 0  && getActualHeight() > 0) {
                float backgroundWidth = getActualWidth();
                float backgroundHeight = getActualHeight();
                if (width != null) {
                    backgroundWidth = width > 0 ? width : 0;
                }

                if (height != null) {
                    backgroundHeight = height > 0 ? height : 0;
                }
                if (backgroundWidth > 0 && backgroundHeight > 0) {
                    Rectangle background = new Rectangle(leftX, maxY - backgroundHeight, leftX + backgroundWidth, maxY);
                    background.setBackgroundColor(backgroundColor);
                    compositeColumn.getCanvas().rectangle(background);
                }
            }
        }

        if (percentageWidth == null) {
            contentWidth = 0;
        }
        if (percentageHeight == null) {
            contentHeight = 0;
        }

        minY += paddingBottom;
        leftX += paddingLeft;
        rightX -= paddingRight;

        yLine -= paddingTop;

        int status = ColumnText.NO_MORE_TEXT;

        if (!content.isEmpty()) {
            //if (floatLayout == null) {
            ArrayList<Element> floatingElements = new ArrayList<Element>();
            floatingElements.addAll(content);
            floatLayout = new FloatLayout(compositeColumn, floatingElements);
            //}

            floatLayout.setSimpleColumn(leftX, minY, rightX, yLine);
            status = floatLayout.layout(simulate);
            yLine = floatLayout.getYLine();
            if (percentageWidth == null && contentWidth < floatLayout.getFilledWidth()) {
                contentWidth = floatLayout.getFilledWidth();
            }
        }


        if (!simulate && position == PdfDiv.PositionType.RELATIVE) {
            compositeColumn.getCanvas().restoreState();
        }

        yLine -= paddingBottom;
        if (percentageHeight == null) {
            contentHeight = maxY - yLine;
        }

        if (percentageWidth == null) {
            contentWidth += paddingLeft + paddingRight;
        }

        return status;
    }
}
