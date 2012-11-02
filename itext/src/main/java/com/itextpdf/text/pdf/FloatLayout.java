/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

    public FloatLayout(ColumnText compositeColumn, List<Element> elements) {
        this.compositeColumn = ColumnText.duplicate(compositeColumn);
        content = elements;
    }

    public FloatLayout(PdfContentByte canvas, List<Element> elements) {
        this.compositeColumn = new ColumnText(canvas);
        compositeColumn.setUseAscender(false);
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

    public int layout(boolean simulate)  throws DocumentException  {
        int status = ColumnText.NO_MORE_TEXT;
        filledWidth = 0;

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
                        status = floatingLayout(compositeColumn, floatingElements, simulate);
                        if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                            break;
                        }
                    }

                    content.remove(0);

                    status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);

                    if (!simulate) {
                        status = floatingElement.layout(compositeColumn, simulate, floatLeftX, minY, floatRightX, yLine);
                    }

                    yLine -= floatingElement.getActualHeight();

                    if (floatingElement.getActualWidth() > filledWidth) {
                        filledWidth = floatingElement.getActualWidth();
                    }
                    if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        content.add(0, floatingElement);
                        break;
                    }
                }
            } else {
                floatingElements.add(content.get(0));
                content.remove(0);
            }
        }

        if ((status & ColumnText.NO_MORE_TEXT) != 0 && !floatingElements.isEmpty()) {
            status = floatingLayout(compositeColumn, floatingElements, simulate);
        }

        content.addAll(0, floatingElements);

        return status;
    }

    private int floatingLayout(ColumnText compositeColumn, List<Element> floatingElements, boolean simulate) throws DocumentException {
        int status = ColumnText.NO_MORE_TEXT;
        float minYLine = yLine;
        float leftWidth = 0;
        float rightWidth = 0;

        while (!floatingElements.isEmpty()) {
            if (floatingElements.get(0) instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv) floatingElements.get(0);
                floatingElements.remove(0);
                status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    yLine = minYLine;
                    floatLeftX = leftX;
                    floatRightX = rightX;
                    status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                    if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        floatingElements.add(0, floatingElement);
                        break;
                    }
                }
                if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT) {
                    status = floatingElement.layout(compositeColumn, simulate, floatLeftX, minY, floatRightX, yLine);
                    floatLeftX += floatingElement.getActualWidth();
                    leftWidth += floatingElement.getActualWidth();
                } else if (floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
                    status = floatingElement.layout(compositeColumn, simulate, floatRightX - floatingElement.getActualWidth() - 0.01f, minY, floatRightX, yLine);
                    floatRightX -= floatingElement.getActualWidth();
                    rightWidth += floatingElement.getActualWidth();
                }
                minYLine = Math.min(minYLine, yLine - floatingElement.getActualHeight());
            } else {
                Element firstElement = floatingElements.get(0);
                if (firstElement instanceof Spaceable) {
                    yLine -= ((Spaceable) firstElement).getSpacingBefore();
                }
                compositeColumn = simulate ? ColumnText.duplicate(compositeColumn) : compositeColumn;
                compositeColumn.addElement(firstElement);
                floatingElements.remove(0);
                if (yLine > minYLine)
                    compositeColumn.setSimpleColumn(floatLeftX, yLine, floatRightX, minYLine);
                else
                    compositeColumn.setSimpleColumn(floatLeftX, yLine, floatRightX, minY);

                compositeColumn.setFilledWidth(0);

                status = compositeColumn.go(simulate);
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
                    compositeColumn.setSimpleColumn(floatLeftX, yLine, floatRightX, minY);
                    status = compositeColumn.go(simulate);
                    minYLine = compositeColumn.getYLine() + compositeColumn.getDescender();
                    yLine = minYLine;
                    if (compositeColumn.getFilledWidth() > filledWidth) {
                        filledWidth = compositeColumn.getFilledWidth();
                    }
                } else {
                    if (rightWidth > 0) {
                        rightWidth += compositeColumn.getFilledWidth();
                    } else if (leftWidth > 0) {
                        leftWidth += compositeColumn.getFilledWidth();
                    } else if (compositeColumn.getFilledWidth() > filledWidth) {
                        filledWidth = compositeColumn.getFilledWidth();
                    }
                    minYLine = Math.min(compositeColumn.getYLine() + compositeColumn.getDescender(), minYLine);
                    yLine = compositeColumn.getYLine() + compositeColumn.getDescender();
                }

                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    if (!simulate)
                        floatingElements.addAll(0, compositeColumn.getCompositeElements());
                    else {
                        floatingElements.add(0, firstElement);
                    }
                    compositeColumn.getCompositeElements().clear();
                    break;
                } else {
                    compositeColumn.getCompositeElements().clear();
                }
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
