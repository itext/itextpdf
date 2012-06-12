package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.api.Spaceable;

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

        List<Element> floatingElements = new ArrayList<Element>();

        while (!content.isEmpty()) {
            if (content.get(0) instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv)content.get(0);
                if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT || floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
                    floatingElements.add(floatingElement);
                    content.remove(0);
                } else {
                    if (!floatingElements.isEmpty()) {
                        status = floatingLayout(floatingElements, simulate);
                        //if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        //    break;
                        //}
                    }

                    status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);

                    if (!simulate) {
                        status = floatingElement.layout(compositeColumn, simulate, floatLeftX, minY, floatRightX, yLine);
                    }

                    yLine -= floatingElement.getActualHeight();

                    if (floatingElement.getActualWidth() > filledWidth) {
                        filledWidth = floatingElement.getActualWidth();
                    }
                    //if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    //    break;
                    //}
                    content.remove(0);
                }
            } else {
                floatingElements.add(content.get(0));
                content.remove(0);
            }
        }

        if (/*(status & ColumnText.NO_MORE_TEXT) != 0 && */!floatingElements.isEmpty()) {
            status = floatingLayout(floatingElements, simulate);
        }

        content.addAll(floatingElements);

        return status;
    }

    private int floatingLayout(List<Element> floatingElements, boolean simulate) throws DocumentException {
        int status = ColumnText.NO_MORE_TEXT;
        float minYLine = yLine;
        float leftWidth = 0;
        float rightWidth = 0;

        while (!floatingElements.isEmpty()) {
            if (floatingElements.get(0) instanceof PdfDiv) {
                PdfDiv floatingElement = (PdfDiv) floatingElements.get(0);
                status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    yLine = minYLine;
                    floatLeftX = leftX;
                    floatRightX = rightX;
                    status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                    //if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    //    break;
                    //}
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
                compositeColumn.addElement(firstElement);
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

                compositeColumn.getCompositeElements().clear();
                //if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                //    break;
                //}
            }

            floatingElements.remove(0);
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
