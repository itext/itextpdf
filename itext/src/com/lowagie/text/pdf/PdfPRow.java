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

    protected PdfContentByte lines;
    protected PdfContentByte backgr;
    protected PdfContentByte text;
    protected PdfPCell cells[];
    protected float maxHeight = 0;
    protected boolean calculated = false;

    public PdfPRow(PdfContentByte lines, PdfContentByte backgr, PdfContentByte text, PdfPCell cells[])
    {
        this.lines = lines;
        this.backgr = backgr;
        this.text = text;
        this.cells = cells;
    }
    
    public float calculateHeights()
    {
        for (int k = 0; k < cells.length; ++k) {
            PdfPCell cell = cells[k];
            float rightLimit = cell.isNoWrap() ? 20000 : cell.right() - cell.getPaddingRight();
            ColumnText ct = new ColumnText(text);
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
            float height = cell.height();
            if (height < cell.getFixedHeight())
                height = cell.getFixedHeight();
            if (height > maxHeight)
                maxHeight = height;
        }
        calculated = true;
        return maxHeight;
    }
    
    public void writeCells()
    {
        if (!calculated)
            calculateHeights();
        for (int k = 0; k < cells.length; ++k) {
            PdfPCell cell = cells[k];
            float rightLimit = cell.isNoWrap() ? 20000 : cell.right() - cell.getPaddingRight();
            ColumnText ct = new ColumnText(text);
            float tly = 0;
            switch (cell.getVerticalAlignment()) {
                case Element.ALIGN_BOTTOM:
                    tly = cell.top() - maxHeight + cell.height() - cell.getPaddingTop();
                    break;
                case Element.ALIGN_MIDDLE:
                    tly = cell.top() + (cell.height() - maxHeight) / 2 - cell.getPaddingTop();
                    break;
                default:
                    tly = cell.top() - cell.getPaddingTop();
                    break;
            }
                    
            ct.setSimpleColumn(cell.getPhrase(),
                cell.left() + cell.getPaddingLeft(),
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
            // the coordinates of the border are retrieved
            float x1 = cell.left();
            float y1 = cell.top();
            float x2 = cell.right();
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
    }
}
