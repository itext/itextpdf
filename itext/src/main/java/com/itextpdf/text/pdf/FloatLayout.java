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

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.text.api.Spaceable;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for PdfDiv to put a collection of Element objects
 * at an absolute position.
 */
public class FloatLayout {
    protected float maxY;

    protected float minY;

    protected float leftX;

    protected float rightX;

    public float getYLine() {
        return yLine;
    }

    public void setYLine(float yLine) {
        this.yLine = yLine;
    }

    protected float yLine;

    protected float floatLeftX;

    protected float floatRightX;

    public float getFilledWidth() {
        return filledWidth;
    }

    public void setFilledWidth(float filledWidth) {
        this.filledWidth = filledWidth;
    }

    protected float filledWidth;

    protected final ColumnText compositeColumn;

    protected final List<Element> content;

    protected final boolean useAscender;

    public int getRunDirection() {
        return compositeColumn.getRunDirection();
    }

    public void setRunDirection(int runDirection) {
        compositeColumn.setRunDirection(runDirection);
    }

    public FloatLayout(List<Element> elements, boolean useAscender) {
        compositeColumn = new ColumnText(null);
        compositeColumn.setUseAscender(useAscender);
        this.useAscender = useAscender;
        content = elements;
    }

    public void setSimpleColumn(final float llx, final float lly, final float urx, final float ury) {
        leftX = Math.min(llx, urx);
        maxY = Math.max(lly, ury);
        minY = Math.min(lly, ury);
        rightX = Math.max(llx, urx);
        floatLeftX = leftX;
        floatRightX = rightX;
        yLine = maxY;
        filledWidth = 0;
    }

    public int layout(PdfContentByte canvas, boolean simulate)  throws DocumentException  {
        compositeColumn.setCanvas(canvas);
        int status = ColumnText.NO_MORE_TEXT;

        ArrayList<Element> floatingElements = new ArrayList<Element>();
        List<Element> content = simulate ? new ArrayList<Element>(this.content) : this.content;

        while (!content.isEmpty()) {
            if (content.get(0) instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv)content.get(0);
                if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT || floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
                    floatingElements.add(floatingElement);
                    content.remove(0);
                } else {
                    if (!floatingElements.isEmpty()) {
                        status = floatingLayout(floatingElements, simulate);
                        if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                            break;
                        }
                    }

                    content.remove(0);

                    status = floatingElement.layout(canvas, useAscender, true, floatLeftX, minY, floatRightX, yLine);

                    if (floatingElement.getKeepTogether() && (status & ColumnText.NO_MORE_TEXT) == 0) {
                        //check for empty page
                        if (compositeColumn.getCanvas().getPdfDocument().currentHeight > 0 || yLine != maxY) {
                            content.add(0, floatingElement);
                            break;
                        }
                    }

                    if (!simulate) {
                        canvas.openMCBlock(floatingElement);
                        status = floatingElement.layout(canvas, useAscender, simulate, floatLeftX, minY, floatRightX, yLine);
                        canvas.closeMCBlock(floatingElement);
                    }

                    if (floatingElement.getActualWidth() > filledWidth) {
                        filledWidth = floatingElement.getActualWidth();
                    }
                    if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        content.add(0, floatingElement);
                        yLine = floatingElement.getYLine();
                        break;
                    } else {
                        yLine -= floatingElement.getActualHeight();
                    }
                }
            } else {
                floatingElements.add(content.get(0));
                content.remove(0);
            }
        }

        if ((status & ColumnText.NO_MORE_TEXT) != 0 && !floatingElements.isEmpty()) {
            status = floatingLayout(floatingElements, simulate);
        }

        content.addAll(0, floatingElements);

        return status;
    }

    private int floatingLayout(List<Element> floatingElements, boolean simulate) throws DocumentException {
        int status = ColumnText.NO_MORE_TEXT;
        float minYLine = yLine;
        float leftWidth = 0;
        float rightWidth = 0;

        ColumnText currentCompositeColumn = compositeColumn;
        if (simulate) {
            currentCompositeColumn = ColumnText.duplicate(compositeColumn);
        }

        boolean ignoreSpacingBefore = maxY == yLine;

        while (!floatingElements.isEmpty()) {
            Element nextElement = floatingElements.get(0);
            floatingElements.remove(0);
            if (nextElement instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv) nextElement;
                status = floatingElement.layout(compositeColumn.getCanvas(), useAscender, true, floatLeftX, minY, floatRightX, yLine);
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    yLine = minYLine;
                    floatLeftX = leftX;
                    floatRightX = rightX;
                    status = floatingElement.layout(compositeColumn.getCanvas(), useAscender, true, floatLeftX, minY, floatRightX, yLine);
                    if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        floatingElements.add(0, floatingElement);
                        break;
                    }
                }
                if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT) {
                    status = floatingElement.layout(compositeColumn.getCanvas(), useAscender, simulate, floatLeftX, minY, floatRightX, yLine);
                    floatLeftX += floatingElement.getActualWidth();
                    leftWidth += floatingElement.getActualWidth();
                } else if (floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
                    status = floatingElement.layout(compositeColumn.getCanvas(), useAscender, simulate, floatRightX - floatingElement.getActualWidth() - 0.01f, minY, floatRightX, yLine);
                    floatRightX -= floatingElement.getActualWidth();
                    rightWidth += floatingElement.getActualWidth();
                }
                minYLine = Math.min(minYLine, yLine - floatingElement.getActualHeight());
            } else {
                if (minY > minYLine) {
                    status = ColumnText.NO_MORE_COLUMN;
                    floatingElements.add(0, nextElement);
                    if (currentCompositeColumn != null)
                        currentCompositeColumn.setText(null);
                    break;
                } else {
                    if (nextElement instanceof Spaceable && (!ignoreSpacingBefore || !currentCompositeColumn.isIgnoreSpacingBefore() || ((Spaceable) nextElement).getPaddingTop() != 0)) {
                        yLine -= ((Spaceable) nextElement).getSpacingBefore();
                    }
                    if (simulate) {
                        if (nextElement instanceof PdfPTable)
                            currentCompositeColumn.addElement(new PdfPTable((PdfPTable)nextElement));
                        else
                            currentCompositeColumn.addElement(nextElement);
                    } else {
                        currentCompositeColumn.addElement(nextElement);
                    }

                    if (yLine > minYLine)
                        currentCompositeColumn.setSimpleColumn(floatLeftX, yLine, floatRightX, minYLine);
                    else
                        currentCompositeColumn.setSimpleColumn(floatLeftX, yLine, floatRightX, minY);

                    currentCompositeColumn.setFilledWidth(0);

                    status = currentCompositeColumn.go(simulate);
                    if (yLine > minYLine && (floatLeftX > leftX || floatRightX < rightX) && (status & ColumnText.NO_MORE_TEXT) == 0) {
                        yLine = minYLine;
                        floatLeftX = leftX;
                        floatRightX = rightX;
                        if (leftWidth != 0 && rightWidth != 0) {
                            filledWidth = rightX - leftX;
                        } else {
                            if (leftWidth > filledWidth) {
                                filledWidth = leftWidth;
                            }
                            if (rightWidth > filledWidth) {
                                filledWidth = rightWidth;
                            }
                        }

                        leftWidth = 0;
                        rightWidth = 0;
                        if (simulate && nextElement instanceof PdfPTable) {
                            currentCompositeColumn.addElement(new PdfPTable((PdfPTable)nextElement));
                        }

                        currentCompositeColumn.setSimpleColumn(floatLeftX, yLine, floatRightX, minY);
                        status = currentCompositeColumn.go(simulate);
                        minYLine = currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender();
                        yLine = minYLine;
                        if (currentCompositeColumn.getFilledWidth() > filledWidth) {
                            filledWidth = currentCompositeColumn.getFilledWidth();
                        }
                    } else {
                        if (rightWidth > 0) {
                            rightWidth += currentCompositeColumn.getFilledWidth();
                        } else if (leftWidth > 0) {
                            leftWidth += currentCompositeColumn.getFilledWidth();
                        } else if (currentCompositeColumn.getFilledWidth() > filledWidth) {
                            filledWidth = currentCompositeColumn.getFilledWidth();
                        }
                        minYLine = Math.min(currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender(), minYLine);
                        yLine = currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender();
                    }

                    if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        if (!simulate) {
                            floatingElements.addAll(0, currentCompositeColumn.getCompositeElements());
                            currentCompositeColumn.getCompositeElements().clear();
                        } else {
                            floatingElements.add(0, nextElement);
                            currentCompositeColumn.setText(null);
                        }
                        break;
                    } else {
                        currentCompositeColumn.setText(null);
                    }
                }
            }

            if (nextElement instanceof Paragraph) {
                Paragraph p = (Paragraph) nextElement;
                for (Element e : p) {
                    if (e instanceof WritableDirectElement) {
                        WritableDirectElement writableElement = (WritableDirectElement) e;
                        if (writableElement.getDirectElementType() == WritableDirectElement.DIRECT_ELEMENT_TYPE_HEADER && !simulate) {
                            PdfWriter writer = compositeColumn.getCanvas().getPdfWriter();
                            PdfDocument doc = compositeColumn.getCanvas().getPdfDocument();

                            // here is used a little hack:
                            // writableElement.write() method implementation uses PdfWriter.getVerticalPosition() to create PdfDestination (see com.itextpdf.tool.xml.html.Header),
                            // so here we are adjusting document's currentHeight in order to make getVerticalPosition() return value corresponding to real current position
                            float savedHeight = doc.currentHeight;
                            doc.currentHeight = doc.top() - yLine - doc.indentation.indentTop;
                            writableElement.write(writer, doc);
                            doc.currentHeight = savedHeight;
                        }
                    }
                }
            }

            if (ignoreSpacingBefore && nextElement.getChunks().size() == 0) {
                if (nextElement instanceof Paragraph){
                    Paragraph p = (Paragraph) nextElement;
                    Element e = p.get(0);
                    if (e instanceof WritableDirectElement){
                        WritableDirectElement writableElement  = (WritableDirectElement) e;
                        if (writableElement.getDirectElementType() != WritableDirectElement.DIRECT_ELEMENT_TYPE_HEADER) {
                            ignoreSpacingBefore = false;
                        }
                    }
                } else if (nextElement instanceof Spaceable) {
                    ignoreSpacingBefore = false;
                }

            } else {
                ignoreSpacingBefore = false;
            }
        }

        if (leftWidth != 0 && rightWidth != 0) {
            filledWidth = rightX - leftX;
        } else {
            if (leftWidth > filledWidth) {
                filledWidth = leftWidth;
            }
            if (rightWidth > filledWidth) {
                filledWidth = rightWidth;
            }
        }

        yLine = minYLine;
        floatLeftX = leftX;
        floatRightX = rightX;

        return status;
    }
}
