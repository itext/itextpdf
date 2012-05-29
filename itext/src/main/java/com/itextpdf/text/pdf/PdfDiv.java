package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.*;
import com.itextpdf.text.api.Spaceable;

public class PdfDiv implements LargeElement, Spaceable {
    public enum FloatType {NONE, LEFT, RIGHT};

    public enum PositionType {STATIC, ABSOLUTE, FIXED, RELATIVE};

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

    private FloatType floatType = FloatType.NONE;

    private PositionType position = PositionType.STATIC;

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

    private float contentWidth = 0;

    private float contentHeight = 0;

    public BaseColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private BaseColor backgroundColor = null;

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

    public Element popFirstElement() {
        Element firstElement = null;
        if (content.size() > 0) {
            firstElement = content.get(0);
            content.remove(0);
        }
        return firstElement;
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
        }

        if (height != null && height > 0) {
            if (height < maxY - minY) {
                minY = maxY - height;
            } else if (height > maxY - minY) {
                return ColumnText.NO_MORE_COLUMN;
            }
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

        contentWidth = 0;
        contentHeight = 0;

        minY += paddingBottom;
        leftX += paddingLeft;
        rightX -= paddingRight;

        yLine -= paddingTop;

        ArrayList<PdfDiv> floatingElements = new ArrayList();

        int status = ColumnText.NO_MORE_TEXT;

        for (Element childElement : content) {
            if (childElement instanceof PdfDiv) {
                PdfDiv childFloatingElement = (PdfDiv) childElement;
                if (childFloatingElement.getPosition() != PdfDiv.PositionType.FIXED && childFloatingElement.getPosition() != PdfDiv.PositionType.ABSOLUTE) {
                    if (childFloatingElement.getFloatType() != PdfDiv.FloatType.NONE) {
                        if (compositeColumn.getCompositeElements() != null && !compositeColumn.getCompositeElements().isEmpty()) {
                            Element firstElement = compositeColumn.getCompositeElements().get(0);
                            if (firstElement instanceof Spaceable) {
                                yLine -= ((Spaceable)firstElement).getSpacingBefore();
                            }
                            compositeColumn.setSimpleColumn(leftX, minY, rightX, yLine);
                            compositeColumn.setFilledWidth(0);
                            status = compositeColumn.go(simulate);
                            yLine = compositeColumn.getYLine();
                            yLine += compositeColumn.getDescender();
                            if (contentWidth < compositeColumn.getFilledWidth()) {
                                contentWidth = compositeColumn.getFilledWidth();
                            }
                            yLine += compositeColumn.getDescender();
                            if ((status & ColumnText.NO_MORE_TEXT) == 0 ) {
                                break;
                            }
                        }
                        floatingElements.add(childFloatingElement);
                        continue;

                    }
                }
            }

            if (!floatingElements.isEmpty()) {
                FloatableLayout fl = new FloatableLayout(compositeColumn);
                fl.setSimpleColumn(leftX, minY, rightX, yLine);
                status = fl.layout(floatingElements, simulate);
                yLine = fl.getYLine();
                if (contentWidth < fl.getFilledWidth()) {
                    contentWidth = fl.getFilledWidth();
                }
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    break;
                }
            }

            compositeColumn.addElement(childElement);
        }


        if ((status & ColumnText.NO_MORE_TEXT) != 0) {
            if (!floatingElements.isEmpty()) {
                FloatableLayout fl = new FloatableLayout(compositeColumn);
                fl.setSimpleColumn(leftX, minY, rightX, yLine);
                status = fl.layout(floatingElements, simulate);
                yLine = fl.getYLine();
                if (contentWidth < compositeColumn.getFilledWidth()) {
                    contentWidth = fl.getFilledWidth();
                }
            } else if (compositeColumn.getCompositeElements() != null && !compositeColumn.getCompositeElements().isEmpty()) {
                Element firstElement = compositeColumn.getCompositeElements().get(0);
                if (firstElement instanceof Spaceable) {
                    yLine -= ((Spaceable) firstElement).getSpacingBefore();
                }
                compositeColumn.setSimpleColumn(leftX, minY, rightX, yLine);
                compositeColumn.setFilledWidth(0);
                status = compositeColumn.go(simulate);
                yLine = compositeColumn.getYLine();
                yLine += compositeColumn.getDescender();
                if (contentWidth < compositeColumn.getFilledWidth()) {
                    contentWidth = compositeColumn.getFilledWidth();
                }
            }
        }

        if (!simulate && position == PdfDiv.PositionType.RELATIVE) {
            compositeColumn.getCanvas().restoreState();
        }

        yLine -= paddingBottom;
        contentHeight = maxY - yLine;
        contentWidth += paddingLeft + paddingRight;

        return status;
    }

    /*public static int writeFloatingElements(final ColumnText compositeColumn, final float llx, final float lly, final float urx, final float ury, ArrayList<PdfDiv> floatingElements) throws DocumentException {
        float currentLeftX = llx;
        float currentRightX = urx;
        float yLine = ury;
        float minYLine = yLine;

        while (floatingElements.isEmpty()) {
            PdfDiv floatingElement = floatingElements.get(0);
            floatingElements.remove(0);

            Rectangle box;
            if (floatingElement.getFloatType() == FloatType.LEFT) {
                box = floatingElement.writeContent(compositeColumn, currentLeftX, lly, currentRightX, yLine);
            } else if (floatingElement.getWidth() != null && floatingElement.getFloatType() == FloatType.RIGHT) {
                box = floatingElement.writeContent(compositeColumn, currentRightX - floatingElement.getWidth(), lly, currentRightX, yLine);
            } else {
                box = floatingElement.writeContent(compositeColumn, currentLeftX, lly, currentRightX, yLine);
            }

            if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                if (yLine == minYLine) {
                    return;
                }
                yLine = minYLine;
                currentLeftX = llx;
                currentRightX = urx;
                floatingElement.writeContent(compositeColumn, currentLeftX, lly, currentRightX, yLine);
                status = compositeColumn.;
                minYLine = compositeColumn.getYLine();
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    return;
                }
            }

            minYLine = compositeColumn.getYLine();

            if (floatingElement.getFloatType() == FloatType.LEFT) {
                if (floatingElement.getWidth() != null) {
                    currentLeftX += floatingElement.getWidth();
                } else {
                    currentLeftX = llx;
                    currentRightX = urx;
                    yLine = minYLine;
                }
            } else if (floatingElement.getFloatType() == FloatType.RIGHT) {
                if (floatingElement.getWidth() != null) {
                    currentRightX -= floatingElement.getWidth();
                } else {
                    currentLeftX = llx;
                    currentRightX = urx;
                    yLine = minYLine;
                }
            }

        }
    }

    public void writeSaticElements(final ColumnText compositeColumn, final float llx, final float lly, final float urx, final float ury) {

    }*/
}
