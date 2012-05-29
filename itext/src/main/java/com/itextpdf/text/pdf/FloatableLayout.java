package com.itextpdf.text.pdf;

import java.util.List;

import com.itextpdf.text.DocumentException;

public class FloatableLayout {
     /** Upper bound of the column. */
    protected float maxY;

    /** Lower bound of the column. */
    protected float minY;

    protected float leftX;

    protected float rightX;

    public float getYLine() {
        return yLine;
    }

    public void setYLine(float yLine) {
        this.yLine = yLine;
    }

    /** The current y line location. Text will be written at this line minus the leading. */
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

    public FloatableLayout(ColumnText compositeColumn) {
        this.compositeColumn = ColumnText.duplicate(compositeColumn);
    }

    public FloatableLayout(PdfContentByte canvas) {
        this.compositeColumn = new ColumnText(canvas);
        compositeColumn.setUseAscender(true);
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

    /*private int layout(PdfDiv floatingElement, boolean simulate) throws DocumentException {
        Float width = floatingElement.getWidth();
        if (width != null) {
            if (width < rightX - leftX) {
                rightX = leftX + width;
            }
        }

        Float height = floatingElement.getHeight();
        if (height != null) {
            if (height < maxY - minY) {
                minY = maxY - height;
            }
        }

        if (!simulate && floatingElement.getPosition() == PdfDiv.PositionType.RELATIVE) {
            compositeColumn.getCanvas().saveState();
            compositeColumn.getCanvas().transform(new AffineTransform(1f, 0, 0, 1f, floatingElement.getLeft(), -floatingElement.getTop()));
        }

        if (!simulate) {
            if (floatingElement.getBackgroundColor() != null) {
                Rectangle background = new Rectangle(leftX, maxY - height, leftX + width, maxY);
                background.setBackgroundColor(floatingElement.getBackgroundColor());
                compositeColumn.getCanvas().rectangle(background);
            }
        }

        minY += floatingElement.getPaddingBottom();
        leftX += floatingElement.getPaddingLeft();
        rightX -= floatingElement.getPaddingRight();

        yLine -= floatingElement.getPaddingTop();

        ArrayList<PdfDiv> floatingElements = new ArrayList();

        int status = ColumnText.NO_MORE_TEXT;

        for (Element childElement : floatingElement.getContent()) {
            if (childElement instanceof PdfDiv) {
                PdfDiv childFloatingElement = (PdfDiv) childElement;
                if (childFloatingElement.getPosition() != PdfDiv.PositionType.FIXED && childFloatingElement.getPosition() != PdfDiv.PositionType.ABSOLUTE) {
                    if (childFloatingElement.getFloatType() != PdfDiv.FloatType.NONE) {
                        if (!compositeColumn.getCompositeElements().isEmpty()) {
                            status = layoutStaticElement(simulate);
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
                status = layout(floatingElements, simulate);
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    break;
                }
            }

            compositeColumn.addElement(childElement);
        }


        if ((status & ColumnText.NO_MORE_TEXT) != 0) {
            if (!floatingElements.isEmpty()) {
                status = layout(floatingElements, simulate);
            } else if (!compositeColumn.getCompositeElements().isEmpty()) {
                status = layoutStaticElement(simulate);
            }
        }

        if (!simulate && floatingElement.getPosition() == PdfDiv.PositionType.RELATIVE) {
            compositeColumn.getCanvas().restoreState();
        }

        yLine -= floatingElement.getPaddingBottom();

        if (width == null) {
            floatingElement.setWidth(filledWidth + floatingElement.getPaddingLeft() + floatingElement.getPaddingRight());
        }

        if (height == null) {
            floatingElement.setHeight(maxY - (yLine - floatingElement.getPaddingBottom()));
        }

        return ColumnText.NO_MORE_TEXT;
    }*/

    public int layout(List<PdfDiv> floatingElements, boolean simulate)  throws DocumentException  {
        float minYLine = yLine;
        boolean floating = false;

        int status = ColumnText.NO_MORE_TEXT;
        while (!floatingElements.isEmpty()) {
            PdfDiv floatingElement = floatingElements.get(0);
            if (floating != (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT || floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT)) {
                yLine = minYLine;
                floatLeftX= leftX;
                floatRightX = rightX;
            }
            if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT || floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
                floating = true;
                status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    if (floatLeftX == leftX && floatRightX == rightX) {
                        break;
                    }

                    yLine = minYLine;
                    floatLeftX = leftX;
                    floatRightX = rightX;
                    status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                    if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                        break;
                    }
                }
                if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT) {
                    status = floatingElement.layout(compositeColumn, simulate, floatLeftX, minY, floatRightX, yLine);
                    floatLeftX += floatingElement.getActualWidth();
                } else if (floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
                    status = floatingElement.layout(compositeColumn, simulate, floatRightX - floatingElement.getActualWidth(), minY, floatRightX, yLine);
                    floatRightX -= floatingElement.getActualWidth();
                }

                minYLine = Math.min(minYLine, yLine - floatingElement.getActualHeight());

            } else {
                floating = false;
                status = floatingElement.layout(compositeColumn, true, floatLeftX, minY, floatRightX, yLine);
                if ((status & ColumnText.NO_MORE_TEXT) == 0) {
                    break;
                }
                status = floatingElement.layout(compositeColumn, simulate, floatLeftX, minY, floatRightX, yLine);

                yLine -= floatingElement.getActualHeight();
                yLine -= compositeColumn.descender;
                minYLine = yLine;
            }
            floatingElements.remove(0);
        }

        yLine = minYLine;
        return status;
    }

    /*public int layoutStaticElement(boolean simulate)  throws DocumentException {
        compositeColumn.setSimpleColumn(leftX, minY, rightX, yLine);
        compositeColumn.setFilledWidth(0);
        int status = compositeColumn.go(simulate);
        if ((status & ColumnText.NO_MORE_TEXT) != 0 ) {
            yLine = compositeColumn.getYLine();
            if (filledWidth < compositeColumn.getFilledWidth()) {
                filledWidth = compositeColumn.getFilledWidth();
            }
            yLine += compositeColumn.getDescender();
        }
        return status;
    }*/
}
