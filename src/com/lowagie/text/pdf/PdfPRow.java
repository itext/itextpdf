/*
 * PdfPRow.java
 *
 * Created on June 17, 2001, 4:19 PM
 */

package com.lowagie.text.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import java.awt.Color;
/**
 *
 * @author  Administrator
 * @version 
 */
public class PdfPRow {

    protected PdfPCell cells[];
    protected float maxHeight = 0;
    protected boolean calculated = false;

    public PdfPRow(PdfPCell cells[])
    {
        this.cells = cells;
    }
    
    public PdfPRow(PdfPRow row)
    {
        maxHeight = row.maxHeight;
        calculated = row.calculated;
        cells = new PdfPCell[row.cells.length];
        for (int k = 0; k < cells.length; ++k)
            cells[k] = new PdfPCell(row.cells[k]);
    }
    
    public boolean setWidths(float widths[])
    {
        if (widths.length != cells.length)
            return false;
        float total = 0;
        calculated = false;
        for (int k = 0; k < widths.length; ++k) {
            PdfPCell cell = cells[k];
            cell.setLeft(total);
            total += widths[k];
            cell.setRight(total);
            cell.setTop(0);
        }
        return true;
    }
    
    public float calculateHeights()
    {
        for (int k = 0; k < cells.length; ++k) {
            PdfPCell cell = cells[k];
            PdfPTable table = cell.getTable();
            if (table == null) {
                float rightLimit = cell.isNoWrap() ? 20000 : cell.right() - cell.getPaddingRight();
                ColumnText ct = new ColumnText(null);
                ct.setSimpleColumn(cell.getPhrase(),
                    cell.left() + cell.getPaddingLeft(),
                    cell.top() - cell.getPaddingTop(),
                    rightLimit,
                    -20000,
                    0, cell.getHorizontalAlignment());
                ct.setLeading(cell.getLeading(), cell.getMultipliedLeading());
                ct.setIndent(cell.getIndent());
                ct.setExtraParagraphSpace(cell.getExtraParagraphSpace());
                try {
                    ct.go(true);
                }
                catch (DocumentException e) {
                }
                float yLine = ct.getYLine();
                cell.setBottom(yLine - cell.getPaddingBottom());
            }
            else {
                table.setTotalWidth(cell.right() - cell.getPaddingRight() - cell.getPaddingLeft() - cell.left());
                cell.setBottom(cell.top() - cell.getPaddingTop() - cell.getPaddingBottom() - table.getTotalHeight());
            }
            float height = cell.height();
            if (height < cell.getFixedHeight())
                height = cell.getFixedHeight();
            if (height > maxHeight)
                maxHeight = height;
        }
        calculated = true;
        return maxHeight;
    }

    public void writeBorderAndBackgroung(float xPos, float yPos, PdfPCell cell, PdfContentByte[] canvases)
    {
        PdfContentByte lines = canvases[PdfPTable.LINECANVAS];
        PdfContentByte backgr = canvases[PdfPTable.BACKGROUNDCANVAS];
        // the coordinates of the border are retrieved
        float x1 = cell.left() + xPos;
        float y1 = cell.top() + yPos;
        float x2 = cell.right() + xPos;
        float y2 = y1 - maxHeight;

        // the backgroundcolor is set
        Color background = cell.backgroundColor();
        if (background != null) {
            backgr.setRGBColorFill(background.getRed(), background.getGreen(), background.getBlue());
            backgr.rectangle(x1, y1, x2 - x1, y2 - y1);
            backgr.fill();
        }
        else if (cell.grayFill() > 0) {
            backgr.setGrayFill(cell.grayFill());
            backgr.rectangle(x1, y1, x2 - x1, y2 - y1);
            backgr.fill();
        }
        // if the element hasn't got any borders, nothing is added
        if (cell.hasBorders()) {

            // the width is set to the width of the element
            if (cell.borderWidth() != Rectangle.UNDEFINED) {
                lines.setLineWidth(cell.borderWidth());
            }

            // the color is set to the color of the element
            Color color = cell.borderColor();
            if (color != null) {
                lines.setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue());
            }

            // if the box is a rectangle, it is added as a rectangle
            if (cell.hasBorder(Rectangle.BOX)) {
                lines.rectangle(x1, y1, x2 - x1, y2 - y1);
            }
            // if the border isn't a rectangle, the different sides are added apart
            else {
                if (cell.hasBorder(Rectangle.RIGHT)) {
                    lines.moveTo(x2, y1);
                    lines.lineTo(x2, y2);
                }
                if (cell.hasBorder(Rectangle.LEFT)) {
                    lines.moveTo(x1, y1);
                    lines.lineTo(x1, y2);
                }
                if (cell.hasBorder(Rectangle.BOTTOM)) {
                    lines.moveTo(x1, y2);
                    lines.lineTo(x2, y2);
                }
                if (cell.hasBorder(Rectangle.TOP)) {
                    lines.moveTo(x1, y1);
                    lines.lineTo(x2, y1);
                }
            }
            lines.stroke();
            if (color != null) {
                lines.resetRGBColorStroke();
            }
        }            
    }
    
    public void writeCells(float xPos, float yPos, PdfContentByte[] canvases)
    {
        if (!calculated)
            calculateHeights();
        for (int k = 0; k < cells.length; ++k) {
            PdfPCell cell = cells[k];
            writeBorderAndBackgroung(xPos, yPos, cell, canvases);
            PdfPTable table = cell.getTable();
            float tly = 0;
            switch (cell.getVerticalAlignment()) {
                case Element.ALIGN_BOTTOM:
                    tly = cell.top() + yPos - maxHeight + cell.height() - cell.getPaddingTop();
                    break;
                case Element.ALIGN_MIDDLE:
                    tly = cell.top() + yPos + (cell.height() - maxHeight) / 2 - cell.getPaddingTop();
                    break;
                default:
                    tly = cell.top() + yPos - cell.getPaddingTop();
                    break;
            }
            if (table == null) {
                float rightLimit = cell.isNoWrap() ? 20000 : cell.right() + xPos - cell.getPaddingRight();
                ColumnText ct = new ColumnText(canvases[PdfPTable.TEXTCANVAS]);
                ct.setSimpleColumn(cell.getPhrase(),
                    cell.left() + xPos + cell.getPaddingLeft(),
                    tly,
                    rightLimit,
                    -20000,
                    0, cell.getHorizontalAlignment());
                ct.setLeading(cell.getLeading(), cell.getMultipliedLeading());
                ct.setIndent(cell.getIndent());
                ct.setExtraParagraphSpace(cell.getExtraParagraphSpace());
                try {
                    ct.go();
                }
                catch (DocumentException e) {
                }
            }
            else {
                table.writeSelectedRows(0, -1, cell.left() + xPos + cell.getPaddingLeft(),
                    tly, canvases);
            }
        }
    }
    
    public boolean isCalculated()
    {
        return calculated;
    }
    
    public float getMaxHeights()
    {
        if (calculated)
            return maxHeight;
        else
            return calculateHeights();
    }
}
