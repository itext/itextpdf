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

import com.itextpdf.text.*;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.events.PdfPCellEventForwarder;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A cell in a PdfPTable.
 */
public class PdfPCell extends Rectangle implements IAccessibleElement {

    private ColumnText column = new ColumnText(null);

    /**
     * Vertical alignment of the cell.
     */
    private int verticalAlignment = Element.ALIGN_TOP;

    /**
     * Left padding of the cell.
     */
    private float paddingLeft = 2;

    /**
     * Right padding of the cell.
     */
    private float paddingRight = 2;

    /**
     * Top padding of the cell.
     */
    private float paddingTop = 2;

    /**
     * Bottom padding of the cell.
     */
    private float paddingBottom = 2;

    /**
     * Fixed height of the cell.
     */
    private float fixedHeight = 0;
    
    /**
     * Fixed height of the cell.
     */
    private float calculatedHeight = 0;

    /**
     * Minimum height of the cell.
     */
    private float minimumHeight;

    /**
     * This field is used to cache the height which is calculated on getMaxHeight() method call;
     * this helps to avoid unnecessary recalculations on table drawing.
     */
    private float cachedMaxHeight;

    /**
     * Holds value of property noWrap.
     */
    private boolean noWrap = false;

    /**
     * Holds value of property table.
     */
    private PdfPTable table;

    /**
     * Holds value of property colspan.
     */
    private int colspan = 1;

    /**
     * Holds value of property rowspan.
     *
     * @since	2.1.6
     */
    private int rowspan = 1;

    /**
     * Holds value of property image.
     */
    private Image image;

    /**
     * Holds value of property cellEvent.
     */
    private PdfPCellEvent cellEvent;

    /**
     * Holds value of property useDescender.
     */
    private boolean useDescender = false;

    /**
     * Increases padding to include border if true
     */
    private boolean useBorderPadding = false;

    /**
     * The text in the cell.
     */
    protected Phrase phrase;

    /**
     * The rotation of the cell. Possible values are 0, 90, 180 and 270.
     */
    private int rotation;

    protected PdfName role = PdfName.TD;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected AccessibleElementId id = new AccessibleElementId();

    protected ArrayList<PdfPHeaderCell> headers = null;

    /**
     * Constructs an empty <CODE>PdfPCell</CODE>. The default padding is 2.
     */
    public PdfPCell() {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        column.setLeading(0, 1);
    }

    /**
     * Constructs a <CODE>PdfPCell</CODE> with a <CODE>Phrase</CODE>. The
     * default padding is 2.
     *
     * @param phrase the text
     */
    public PdfPCell(Phrase phrase) {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        column.addText(this.phrase = phrase);
        column.setLeading(0, 1);
    }

    /**
     * Constructs a <CODE>PdfPCell</CODE> with an <CODE>Image</CODE>. The
     * default padding is 0.
     *
     * @param image the <CODE>Image</CODE>
     */
    public PdfPCell(Image image) {
        this(image, false);
    }

    /**
     * Constructs a <CODE>PdfPCell</CODE> with an <CODE>Image</CODE>. The
     * default padding is 0.25 for a border width of 0.5.
     *
     * @param image the <CODE>Image</CODE>
     * @param fit <CODE>true</CODE> to fit the image to the cell
     */
    public PdfPCell(Image image, boolean fit) {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        column.setLeading(0, 1);
        if (fit) {
            this.image = image;
            setPadding(borderWidth / 2);
        } else {
            image.setScaleToFitLineWhenOverflow(false);
            column.addText(this.phrase = new Phrase(new Chunk(image, 0, 0, true)));
            setPadding(0);
        }
    }

    /**
     * Constructs a <CODE>PdfPCell</CODE> with a <CODE>PdfPtable</CODE>. This
     * constructor allows nested tables. The default padding is 0.
     *
     * @param table The <CODE>PdfPTable</CODE>
     */
    public PdfPCell(PdfPTable table) {
        this(table, null);
    }

    /**
     * Constructs a <CODE>PdfPCell</CODE> with a <CODE>PdfPtable</CODE>. This
     * constructor allows nested tables.
     *
     * @param table The <CODE>PdfPTable</CODE>
     * @param style	The style to apply to the cell (you could use
     * getDefaultCell())
     * @since 2.1.0
     */
    public PdfPCell(PdfPTable table, PdfPCell style) {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        column.setLeading(0, 1);
        this.table = table;
        table.setWidthPercentage(100);
        table.setExtendLastRow(true);
        column.addElement(table);
        if (style != null) {
            cloneNonPositionParameters(style);
            verticalAlignment = style.verticalAlignment;
            paddingLeft = style.paddingLeft;
            paddingRight = style.paddingRight;
            paddingTop = style.paddingTop;
            paddingBottom = style.paddingBottom;
            colspan = style.colspan;
            rowspan = style.rowspan;
            cellEvent = style.cellEvent;
            useDescender = style.useDescender;
            useBorderPadding = style.useBorderPadding;
            rotation = style.rotation;
        } else {
            setPadding(0);
        }
    }

    /**
     * Constructs a deep copy of a <CODE>PdfPCell</CODE>.
     *
     * @param cell the <CODE>PdfPCell</CODE> to duplicate
     */
    public PdfPCell(PdfPCell cell) {
        super(cell.llx, cell.lly, cell.urx, cell.ury);
        cloneNonPositionParameters(cell);
        verticalAlignment = cell.verticalAlignment;
        paddingLeft = cell.paddingLeft;
        paddingRight = cell.paddingRight;
        paddingTop = cell.paddingTop;
        paddingBottom = cell.paddingBottom;
        phrase = cell.phrase;
        fixedHeight = cell.fixedHeight;
        minimumHeight = cell.minimumHeight;
        noWrap = cell.noWrap;
        colspan = cell.colspan;
        rowspan = cell.rowspan;
        if (cell.table != null) {
            table = new PdfPTable(cell.table);
        }
        image = Image.getInstance(cell.image);
        cellEvent = cell.cellEvent;
        useDescender = cell.useDescender;
        column = ColumnText.duplicate(cell.column);
        useBorderPadding = cell.useBorderPadding;
        rotation = cell.rotation;
        id = cell.id;
        role = cell.role;
        if (cell.accessibleAttributes != null) {
            accessibleAttributes = new HashMap<PdfName, PdfObject>(cell.accessibleAttributes);
        }
        headers = cell.headers;
    }

    /**
     * Adds an iText element to the cell.
     *
     * @param element
     */
    public void addElement(Element element) {
        if (table != null) {
            table = null;
            column.setText(null);
        }
        if (element instanceof PdfPTable) {
            ((PdfPTable) element).setSplitLate(false);
        } else if (element instanceof PdfDiv) {
            for (Element divChildElement : ((PdfDiv) element).getContent()) {
                if (divChildElement instanceof PdfPTable) {
                    ((PdfPTable) divChildElement).setSplitLate(false);
                }
            }
        }
        column.addElement(element);
    }

    /**
     * Gets the <CODE>Phrase</CODE> from this cell.
     *
     * @return the <CODE>Phrase</CODE>
     */
    public Phrase getPhrase() {
        return phrase;
    }

    /**
     * Sets the <CODE>Phrase</CODE> for this cell.
     *
     * @param phrase the <CODE>Phrase</CODE>
     */
    public void setPhrase(Phrase phrase) {
        table = null;
        image = null;
        column.setText(this.phrase = phrase);
    }

    /**
     * Gets the horizontal alignment for the cell.
     *
     * @return the horizontal alignment for the cell
     */
    public int getHorizontalAlignment() {
        return column.getAlignment();
    }

    /**
     * Sets the horizontal alignment for the cell. It could be
     * <CODE>Element.ALIGN_CENTER</CODE> for example.
     *
     * @param horizontalAlignment The horizontal alignment
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        column.setAlignment(horizontalAlignment);
    }

    /**
     * Gets the vertical alignment for the cell.
     *
     * @return the vertical alignment for the cell
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Sets the vertical alignment for the cell. It could be
     * <CODE>Element.ALIGN_MIDDLE</CODE> for example.
     *
     * @param verticalAlignment The vertical alignment
     */
    public void setVerticalAlignment(int verticalAlignment) {
        if (table != null) {
            table.setExtendLastRow(verticalAlignment == Element.ALIGN_TOP);
        }
        this.verticalAlignment = verticalAlignment;
    }

    /**
     * Gets the effective left padding. This will include the left border width
     * if {@link #isUseBorderPadding()} is true.
     *
     * @return effective value of property paddingLeft.
     */
    public float getEffectivePaddingLeft() {
        if (isUseBorderPadding()) {
            float border = getBorderWidthLeft() / (isUseVariableBorders() ? 1f : 2f);
            return paddingLeft + border;
        }
        return paddingLeft;
    }

    /**
     * @return Value of property paddingLeft.
     */
    public float getPaddingLeft() {
        return paddingLeft;
    }

    /**
     * Setter for property paddingLeft.
     *
     * @param paddingLeft New value of property paddingLeft.
     */
    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    /**
     * Gets the effective right padding. This will include the right border
     * width if {@link #isUseBorderPadding()} is true.
     *
     * @return effective value of property paddingRight.
     */
    public float getEffectivePaddingRight() {
        if (isUseBorderPadding()) {
            float border = getBorderWidthRight() / (isUseVariableBorders() ? 1f : 2f);
            return paddingRight + border;
        }
        return paddingRight;
    }

    /**
     * Getter for property paddingRight.
     *
     * @return Value of property paddingRight.
     */
    public float getPaddingRight() {
        return paddingRight;
    }

    /**
     * Setter for property paddingRight.
     *
     * @param paddingRight New value of property paddingRight.
     */
    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    /**
     * Gets the effective top padding. This will include the top border width if
     * {@link #isUseBorderPadding()} is true.
     *
     * @return effective value of property paddingTop.
     */
    public float getEffectivePaddingTop() {
        if (isUseBorderPadding()) {
            float border = getBorderWidthTop() / (isUseVariableBorders() ? 1f : 2f);
            return paddingTop + border;
        }
        return paddingTop;
    }

    /**
     * Getter for property paddingTop.
     *
     * @return Value of property paddingTop.
     */
    public float getPaddingTop() {
        return paddingTop;
    }

    /**
     * Setter for property paddingTop.
     *
     * @param paddingTop New value of property paddingTop.
     */
    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    /**
     * Gets the effective bottom padding. This will include the bottom border
     * width if {@link #isUseBorderPadding()} is true.
     *
     * @return effective value of property paddingBottom.
     */
    public float getEffectivePaddingBottom() {
        if (isUseBorderPadding()) {
            float border = getBorderWidthBottom() / (isUseVariableBorders() ? 1f : 2f);
            return paddingBottom + border;
        }
        return paddingBottom;
    }

    /**
     * Getter for property paddingBottom.
     *
     * @return Value of property paddingBottom.
     */
    public float getPaddingBottom() {
        return paddingBottom;
    }

    /**
     * Setter for property paddingBottom.
     *
     * @param paddingBottom New value of property paddingBottom.
     */
    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    /**
     * Sets the padding of the contents in the cell (space between content and
     * border).
     *
     * @param padding
     */
    public void setPadding(float padding) {
        paddingBottom = padding;
        paddingTop = padding;
        paddingLeft = padding;
        paddingRight = padding;
    }

    /**
     * If true, then effective padding will include border widths
     *
     * @return true if effective padding includes border widths
     */
    public boolean isUseBorderPadding() {
        return useBorderPadding;
    }

    /**
     * Adjusts effective padding to include border widths.
     *
     * @param use adjust effective padding if true
     */
    public void setUseBorderPadding(boolean use) {
        useBorderPadding = use;
    }

    /**
     * Sets the leading fixed and variable. The resultant leading will be:
     * fixedLeading+multipliedLeading*maxFontSize where maxFontSize is the size
     * of the biggest font in the line.
     *
     * @param fixedLeading the fixed leading
     * @param multipliedLeading the variable leading
     */
    public void setLeading(float fixedLeading, float multipliedLeading) {
        column.setLeading(fixedLeading, multipliedLeading);
    }

    /**
     * Gets the fixed leading.
     *
     * @return the leading
     */
    public float getLeading() {
        return column.getLeading();
    }

    /**
     * Gets the variable leading.
     *
     * @return the leading
     */
    public float getMultipliedLeading() {
        return column.getMultipliedLeading();
    }

    /**
     * Sets the first paragraph line indent.
     *
     * @param indent the indent
     */
    public void setIndent(float indent) {
        column.setIndent(indent);
    }

    /**
     * Gets the first paragraph line indent.
     *
     * @return the indent
     */
    public float getIndent() {
        return column.getIndent();
    }

    /**
     * Gets the extra space between paragraphs.
     *
     * @return the extra space between paragraphs
     */
    public float getExtraParagraphSpace() {
        return column.getExtraParagraphSpace();
    }

    /**
     * Sets the extra space between paragraphs.
     *
     * @param extraParagraphSpace the extra space between paragraphs
     */
    public void setExtraParagraphSpace(float extraParagraphSpace) {
        column.setExtraParagraphSpace(extraParagraphSpace);
    }

    /**
     * Set a calculated height for the cell.
     *
     * @param calculatedHeight New value of property calculatedHeight.
     */
    public void setCalculatedHeight(float calculatedHeight) {
        this.calculatedHeight = calculatedHeight;
    }

    /**
     * Get the calculated height of the cell.
     *
     * @return Value of property calculatedHeight.
     */
    public float getCalculatedHeight() {
        return calculatedHeight;
    }

    /**
     * Tells you whether the height was calculated.
     *
     * @return	true if the height was calculated.
     */
    public boolean hasCalculatedHeight() {
        return getCalculatedHeight() > 0;
    }

    /**
     * Set a fixed height for the cell. This will automatically unset
     * minimumHeight, if set.
     *
     * @param fixedHeight New value of property fixedHeight.
     */
    public void setFixedHeight(float fixedHeight) {
        this.fixedHeight = fixedHeight;
        minimumHeight = 0;
    }

    /**
     * Get the fixed height of the cell.
     *
     * @return Value of property fixedHeight.
     */
    public float getFixedHeight() {
        return fixedHeight;
    }

    /**
     * Tells you whether the cell has a fixed height.
     *
     * @return	true is a fixed height was set.
     * @since 2.1.5
     */
    public boolean hasFixedHeight() {
        return getFixedHeight() > 0;
    }

    /**
     * Gets the height which was calculated on last call of getMaxHeight().
     * If cell's bBox and content wasn't changed this value is actual maxHeight of the cell.
     * @return max height which was calculated on last call of getMaxHeight(); if getMaxHeight() wasn't called the return value is 0
     */
    public float getCachedMaxHeight() {
        return cachedMaxHeight;
    }

    public boolean hasCachedMaxHeight() {
        return cachedMaxHeight > 0;
    }

    /**
     * Set a minimum height for the cell. This will automatically unset
     * fixedHeight, if set.
     *
     * @param minimumHeight New value of property minimumHeight.
     */
    public void setMinimumHeight(float minimumHeight) {
        this.minimumHeight = minimumHeight;
        fixedHeight = 0;
    }

    /**
     * Get the minimum height of the cell.
     *
     * @return Value of property minimumHeight.
     */
    public float getMinimumHeight() {
        return minimumHeight;
    }

    /**
     * Tells you whether the cell has a minimum height.
     *
     * @return	true if a minimum height was set.
     * @since 2.1.5
     */
    public boolean hasMinimumHeight() {
        return getMinimumHeight() > 0;
    }

    /**
     * Getter for property noWrap.
     *
     * @return Value of property noWrap.
     */
    public boolean isNoWrap() {
        return noWrap;
    }

    /**
     * Setter for property noWrap.
     *
     * @param noWrap New value of property noWrap.
     */
    public void setNoWrap(boolean noWrap) {
        this.noWrap = noWrap;
    }

    /**
     * Getter for property table.
     *
     * @return Value of property table.
     * @since 2.x
     */
    public PdfPTable getTable() {
        return table;
    }

    void setTable(PdfPTable table) {
        this.table = table;
        column.setText(null);
        image = null;
        if (table != null) {
            table.setExtendLastRow(verticalAlignment == Element.ALIGN_TOP);
            column.addElement(table);
            table.setWidthPercentage(100);
        }
    }

    /**
     * Getter for property colspan.
     *
     * @return Value of property colspan.
     */
    public int getColspan() {
        return colspan;
    }

    /**
     * Setter for property colspan.
     *
     * @param colspan New value of property colspan.
     */
    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    /**
     * Getter for property rowspan.
     *
     * @return Value of property rowspan.
     * @since	2.1.6
     */
    public int getRowspan() {
        return rowspan;
    }

    /**
     * Setter for property rowspan.
     *
     * @param rowspan New value of property rowspan.
     * @since	2.1.6
     */
    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    /**
     * Sets the following paragraph lines indent.
     *
     * @param indent the indent
     */
    public void setFollowingIndent(float indent) {
        column.setFollowingIndent(indent);
    }

    /**
     * Gets the following paragraph lines indent.
     *
     * @return the indent
     */
    public float getFollowingIndent() {
        return column.getFollowingIndent();
    }

    /**
     * Sets the right paragraph lines indent.
     *
     * @param indent the indent
     */
    public void setRightIndent(float indent) {
        column.setRightIndent(indent);
    }

    /**
     * Gets the right paragraph lines indent.
     *
     * @return the indent
     */
    public float getRightIndent() {
        return column.getRightIndent();
    }

    /**
     * Gets the space/character extra spacing ratio for fully justified text.
     *
     * @return the space/character extra spacing ratio
     */
    public float getSpaceCharRatio() {
        return column.getSpaceCharRatio();
    }

    /**
     * Sets the ratio between the extra word spacing and the extra character
     * spacing when the text is fully justified. Extra word spacing will grow
     * <CODE>spaceCharRatio</CODE> times more than extra character spacing. If
     * the ratio is <CODE>PdfWriter.NO_SPACE_CHAR_RATIO</CODE> then the extra
     * character spacing will be zero.
     *
     * @param spaceCharRatio the ratio between the extra word spacing and the
     * extra character spacing
     */
    public void setSpaceCharRatio(float spaceCharRatio) {
        column.setSpaceCharRatio(spaceCharRatio);
    }

    /**
     * Sets the run direction of the text content in the cell. May be either of:
     * PdfWriter.RUN_DIRECTION_DEFAULT, PdfWriter.RUN_DIRECTION_NO_BIDI,
     * PdfWriter.RUN_DIRECTION_LTR or PdfWriter.RUN_DIRECTION_RTL.
     *
     * @param runDirection
     */
    public void setRunDirection(int runDirection) {
        column.setRunDirection(runDirection);
    }

    /**
     * Gets the run direction of the text content in the cell
     *
     * @return One of the following values: PdfWriter.RUN_DIRECTION_DEFAULT,
     * PdfWriter.RUN_DIRECTION_NO_BIDI, PdfWriter.RUN_DIRECTION_LTR or
     * PdfWriter.RUN_DIRECTION_RTL.
     */
    public int getRunDirection() {
        return column.getRunDirection();
    }

    /**
     * Getter for property image.
     *
     * @return Value of property image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Setter for property image.
     *
     * @param image New value of property image.
     */
    public void setImage(Image image) {
        column.setText(null);
        table = null;
        this.image = image;
    }

    /**
     * Gets the cell event for this cell.
     *
     * @return the cell event
     */
    public PdfPCellEvent getCellEvent() {
        return cellEvent;
    }

    /**
     * Sets the cell event for this cell.
     *
     * @param cellEvent the cell event
     */
    public void setCellEvent(PdfPCellEvent cellEvent) {
        if (cellEvent == null) {
            this.cellEvent = null;
        } else if (this.cellEvent == null) {
            this.cellEvent = cellEvent;
        } else if (this.cellEvent instanceof PdfPCellEventForwarder) {
            ((PdfPCellEventForwarder) this.cellEvent).addCellEvent(cellEvent);
        } else {
            PdfPCellEventForwarder forward = new PdfPCellEventForwarder();
            forward.addCellEvent(this.cellEvent);
            forward.addCellEvent(cellEvent);
            this.cellEvent = forward;
        }
    }

    /**
     * Gets the arabic shaping options.
     *
     * @return the arabic shaping options
     */
    public int getArabicOptions() {
        return column.getArabicOptions();
    }

    /**
     * Sets the arabic shaping options. The option can be AR_NOVOWEL,
     * AR_COMPOSEDTASHKEEL and AR_LIG.
     *
     * @param arabicOptions the arabic shaping options
     */
    public void setArabicOptions(int arabicOptions) {
        column.setArabicOptions(arabicOptions);
    }

    /**
     * Gets state of first line height based on max ascender
     *
     * @return true if an ascender is to be used.
     */
    public boolean isUseAscender() {
        return column.isUseAscender();
    }

    /**
     * Enables/ Disables adjustment of first line height based on max ascender.
     *
     * @param useAscender adjust height if true
     */
    public void setUseAscender(boolean useAscender) {
        column.setUseAscender(useAscender);
    }

    /**
     * Getter for property useDescender.
     *
     * @return Value of property useDescender.
     */
    public boolean isUseDescender() {
        return useDescender;
    }

    /**
     * Setter for property useDescender.
     *
     * @param useDescender New value of property useDescender.
     */
    public void setUseDescender(boolean useDescender) {
        this.useDescender = useDescender;
    }

    /**
     * Gets the ColumnText with the content of the cell.
     *
     * @return a columntext object
     */
    public ColumnText getColumn() {
        return column;
    }

    /**
     * Returns the list of composite elements of the column.
     *
     * @return	a List object.
     * @since	2.1.1
     */
    public List<Element> getCompositeElements() {
        return getColumn().compositeElements;
    }

    /**
     * Sets the columntext in the cell.
     *
     * @param column
     */
    public void setColumn(ColumnText column) {
        this.column = column;
    }

    /**
     * Gets the rotation of the cell.
     *
     * @return the rotation of the cell.
     */
    @Override
    public int getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the cell. Possible values are 0, 90, 180 and 270.
     *
     * @param rotation the rotation of the cell
     */
    public void setRotation(int rotation) {
        rotation %= 360;
        if (rotation < 0) {
            rotation += 360;
        }
        if (rotation % 90 != 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("rotation.must.be.a.multiple.of.90"));
        }
        this.rotation = rotation;
    }

    /**
     * Returns the height of the cell.
     *
     * @return	the height of the cell
     * @since	3.0.0
     */
    public float getMaxHeight() {
        boolean pivoted = getRotation() == 90 || getRotation() == 270;
        Image img = getImage();
        if (img != null) {
            img.scalePercent(100);
            float refWidth = pivoted ? img.getScaledHeight() : img.getScaledWidth();
            float scale = (getRight() - getEffectivePaddingRight()
                    - getEffectivePaddingLeft() - getLeft()) / refWidth;
            img.scalePercent(scale * 100);
            float refHeight = pivoted ? img.getScaledWidth() : img.getScaledHeight();
            setBottom(getTop() - getEffectivePaddingTop() - getEffectivePaddingBottom() - refHeight);
        } else {
            if ((pivoted && hasFixedHeight()) || getColumn() == null) {
                setBottom(getTop() - getFixedHeight());
            } else {
                ColumnText ct = ColumnText.duplicate(getColumn());
                float right, top, left, bottom;
                if (pivoted) {
                    right = PdfPRow.RIGHT_LIMIT;
                    top = getRight() - getEffectivePaddingRight();
                    left = 0;
                    bottom = getLeft() + getEffectivePaddingLeft();
                } else {
                    right = isNoWrap() ? PdfPRow.RIGHT_LIMIT : getRight() - getEffectivePaddingRight();
                    top = getTop() - getEffectivePaddingTop();
                    left = getLeft() + getEffectivePaddingLeft();
                    bottom = hasCalculatedHeight() ? getTop() + getEffectivePaddingBottom() - getCalculatedHeight() : PdfPRow.BOTTOM_LIMIT;
                }
                PdfPRow.setColumn(ct, left, bottom, right, top);
                try {
                    ct.go(true);
                } catch (DocumentException e) {
                    throw new ExceptionConverter(e);
                }
                if (pivoted) {
                    setBottom(getTop() - getEffectivePaddingTop() - getEffectivePaddingBottom() - ct.getFilledWidth());
                } else {
                    float yLine = ct.getYLine();
                    if (isUseDescender()) {
                        yLine += ct.getDescender();
                    }
                    setBottom(yLine - getEffectivePaddingBottom());
                }
            }
        }
        float height = getHeight();
        if (height == getEffectivePaddingTop() + getEffectivePaddingBottom()) {
            height = 0;
        }
        if (hasFixedHeight()) {
            height = getFixedHeight();
        } else if (hasMinimumHeight() && height < getMinimumHeight()) {
            height = getMinimumHeight();
        }
        cachedMaxHeight = height;
        return height;
    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null) {
            return accessibleAttributes.get(key);
        } else {
            return null;
        }
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null) {
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        }
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

    public void addHeader(PdfPHeaderCell header) {
        if (headers == null) {
            headers = new ArrayList<PdfPHeaderCell>();
        }
        headers.add(header);
    }

    public ArrayList<PdfPHeaderCell> getHeaders() {
        return headers;
    }
}
