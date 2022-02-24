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

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.*;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A special element to put a collection of elements at an absolute position.
 */
public class PdfDiv implements Element, Spaceable, IAccessibleElement {
    public enum FloatType {NONE, LEFT, RIGHT};

    public enum PositionType {STATIC, ABSOLUTE, FIXED, RELATIVE};

    public enum DisplayType {NONE, BLOCK, INLINE, INLINE_BLOCK, INLINE_TABLE, LIST_ITEM, RUN_IN, TABLE, TABLE_CAPTION, TABLE_CELL, TABLE_COLUMN_GROUP, TABLE_COLUMN, TABLE_FOOTER_GROUP,
    TABLE_HEADER_GROUP, TABLE_ROW, TABLE_ROW_GROUP};

    public enum BorderTopStyle {DOTTED, DASHED, SOLID, DOUBLE, GROOVE, RIDGE, INSET, OUTSET};

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

    private DisplayType display;

    private FloatLayout floatLayout = null;

    private BorderTopStyle borderTopStyle;

    private float yLine;

    protected int runDirection = PdfWriter.RUN_DIRECTION_NO_BIDI;

    /**
     * Defines if the div should be kept on one page if possible
     */
    private boolean keepTogether;

    protected PdfName role = PdfName.DIV;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected AccessibleElementId id = new AccessibleElementId();

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

    /**
     * IMPROTANT NOTE: be careful with this method because it would return correct result
     * only in case if {@link PdfDiv#layout(PdfContentByte, boolean, boolean, float, float, float, float)}
     * was already called.
     * @return the actual height the div would require to layout it's content
     */

    public float getActualHeight() {
        return height != null && height >= contentHeight ? height : contentHeight;
    }

    /**
     * IMPROTANT NOTE: be careful with this method because it would return correct result
     * only in case if {@link PdfDiv#layout(PdfContentByte, boolean, boolean, float, float, float, float)}
     * was already called.
     * @return the actual width the div would require to layout it's content
     */
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


    public DisplayType getDisplay() {
        return display;
    }

    public void setDisplay(DisplayType display) {
        this.display = display;
    }


    public BaseColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(BaseColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Image will be scaled to fit in the div occupied area.
     */
    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
    }

    /**
     * Image will be scaled to fit in the div occupied area.
     */
    public void setBackgroundImage(Image image, float width, float height) {
        this.backgroundImage = image;
        this.backgroundImageWidth = width;
        this.backgroundImageHeight = height;
    }

    public float getYLine() {
        return yLine;
    }

    public int getRunDirection() {
        return runDirection;
    }

    public void setRunDirection(int runDirection) {
        this.runDirection = runDirection;
    }

    public boolean getKeepTogether() {
        return keepTogether;
    }

    public void setKeepTogether(boolean keepTogether) {
        this.keepTogether = keepTogether;
    }

    private BaseColor backgroundColor = null;

    private Image backgroundImage;
    private Float backgroundImageWidth;
    private Float backgroundImageHeight;

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
        keepTogether = false;
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

    public BorderTopStyle getBorderTopStyle() {
        return borderTopStyle;
    }

    public void setBorderTopStyle(BorderTopStyle borderTopStyle) {
        this.borderTopStyle = borderTopStyle;
    }

    public int layout(final PdfContentByte canvas, boolean useAscender, boolean simulate, final float llx, final float lly, final float urx, final float ury) throws DocumentException {

        float leftX = Math.min(llx, urx);
        float maxY = Math.max(lly, ury);
        float minY = Math.min(lly, ury);
        float rightX = Math.max(llx, urx);
        yLine = maxY;
        boolean contentCutByFixedHeight = false;

        if (width != null && width > 0) {
            if (width < rightX - leftX) {
                rightX = leftX + width;
            } else if (width > rightX - leftX) {
                return ColumnText.NO_MORE_COLUMN;
            }
        } else if (percentageWidth != null) {
            contentWidth = (rightX - leftX) * percentageWidth;
            rightX = leftX + contentWidth;
        } else if (percentageWidth == null) {
            if (this.floatType == FloatType.NONE && (this.display == null ||
                this.display == DisplayType.BLOCK || this.display == DisplayType.LIST_ITEM ||
                this.display == DisplayType.RUN_IN)){
                contentWidth = rightX - leftX;
            }
        }

        if (height != null && height > 0) {
            if (height < maxY - minY) {
                minY = maxY - height;
                contentCutByFixedHeight = true;
            } else if (height > maxY - minY) {
                return ColumnText.NO_MORE_COLUMN;
            }
        } else if (percentageHeight != null) {
            if (percentageHeight < 1.0) {
                contentCutByFixedHeight = true;
            }
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
            canvas.saveState();
            canvas.transform(new AffineTransform(1f, 0, 0, 1f, translationX, translationY));
        }

        if (!simulate) {
            if ((backgroundColor != null || backgroundImage != null) && getActualWidth() > 0  && getActualHeight() > 0) {
                float backgroundWidth = getActualWidth();
                float backgroundHeight = getActualHeight();
                if (width != null) {
                    backgroundWidth = width > 0 ? width : 0;
                }

                if (height != null) {
                    backgroundHeight = height > 0 ? height : 0;
                }
                if (backgroundWidth > 0 && backgroundHeight > 0) {
                    Rectangle background = new Rectangle(leftX, maxY - backgroundHeight, backgroundWidth+leftX, maxY);
                    if (backgroundColor != null) {
                        background.setBackgroundColor(backgroundColor);
                        PdfArtifact artifact = new PdfArtifact();
                        canvas.openMCBlock(artifact);
                        canvas.rectangle(background);
                        canvas.closeMCBlock(artifact);
                    }
                    if (backgroundImage != null) {
                        if (backgroundImageWidth == null) {
                            backgroundImage.scaleToFit(background);
                        } else {
                            backgroundImage.scaleAbsolute(backgroundImageWidth, backgroundImageHeight);
                        }
                        backgroundImage.setAbsolutePosition(background.getLeft(), background.getBottom());
                        canvas.openMCBlock(backgroundImage);
                        canvas.addImage(backgroundImage);
                        canvas.closeMCBlock(backgroundImage);
                    }
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
            if (this.floatLayout == null) {
                ArrayList<Element> floatingElements = new ArrayList<Element>(content);
                floatLayout = new FloatLayout(floatingElements, useAscender);
                floatLayout.setRunDirection(runDirection);
            }

            floatLayout.setSimpleColumn(leftX, minY, rightX, yLine);
            if (getBorderTopStyle() != null) {
                floatLayout.compositeColumn.setIgnoreSpacingBefore(false);
            }

            status = floatLayout.layout(canvas, simulate);
            yLine = floatLayout.getYLine();
            if (percentageWidth == null && contentWidth < floatLayout.getFilledWidth()) {
                contentWidth = floatLayout.getFilledWidth();
            }
        }


        if (!simulate && position == PdfDiv.PositionType.RELATIVE) {
            canvas.restoreState();
        }

        yLine -= paddingBottom;
        if (percentageHeight == null) {
            contentHeight = maxY - yLine;
        }

        if (percentageWidth == null) {
            contentWidth += paddingLeft + paddingRight;
        }

        return contentCutByFixedHeight ? ColumnText.NO_MORE_TEXT : status;
    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null)
            return accessibleAttributes.get(key);
        else
            return null;
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null)
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return accessibleAttributes;
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        return id;
    }

    public void setId(final AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }
}
