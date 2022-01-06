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
package com.itextpdf.text.pdf.codec.wmf;

import java.util.ArrayList;
import java.util.Stack;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;

public class MetaState {

    public static final int TA_NOUPDATECP = 0;
    public static final int TA_UPDATECP = 1;
    public static final int TA_LEFT = 0;
    public static final int TA_RIGHT = 2;
    public static final int TA_CENTER = 6;
    public static final int TA_TOP = 0;
    public static final int TA_BOTTOM = 8;
    public static final int TA_BASELINE = 24;

    public static final int TRANSPARENT = 1;
    public static final int OPAQUE = 2;

    public static final int ALTERNATE = 1;
    public static final int WINDING = 2;

    public Stack<MetaState> savedStates;
    public ArrayList<MetaObject> MetaObjects;
    public Point currentPoint;
    public MetaPen currentPen;
    public MetaBrush currentBrush;
    public MetaFont currentFont;
    public BaseColor currentBackgroundColor = BaseColor.WHITE;
    public BaseColor currentTextColor = BaseColor.BLACK;
    public int backgroundMode = OPAQUE;
    public int polyFillMode = ALTERNATE;
    public int lineJoin = 1;
    public int textAlign;
    public int offsetWx;
    public int offsetWy;
    public int extentWx;
    public int extentWy;
    public float scalingX;
    public float scalingY;


    /** Creates new MetaState */
    public MetaState() {
        savedStates = new Stack<MetaState>();
        MetaObjects = new ArrayList<MetaObject>();
        currentPoint = new Point(0, 0);
        currentPen = new MetaPen();
        currentBrush = new MetaBrush();
        currentFont = new MetaFont();
    }

    public MetaState(MetaState state) {
        setMetaState(state);
    }

    public void setMetaState(MetaState state) {
        savedStates = state.savedStates;
        MetaObjects = state.MetaObjects;
        currentPoint = state.currentPoint;
        currentPen = state.currentPen;
        currentBrush = state.currentBrush;
        currentFont = state.currentFont;
        currentBackgroundColor = state.currentBackgroundColor;
        currentTextColor = state.currentTextColor;
        backgroundMode = state.backgroundMode;
        polyFillMode = state.polyFillMode;
        textAlign = state.textAlign;
        lineJoin = state.lineJoin;
        offsetWx = state.offsetWx;
        offsetWy = state.offsetWy;
        extentWx = state.extentWx;
        extentWy = state.extentWy;
        scalingX = state.scalingX;
        scalingY = state.scalingY;
    }

    public void addMetaObject(MetaObject object) {
        for (int k = 0; k < MetaObjects.size(); ++k) {
            if (MetaObjects.get(k) == null) {
                MetaObjects.set(k, object);
                return;
            }
        }
        MetaObjects.add(object);
    }

    public void selectMetaObject(int index, PdfContentByte cb) {
        MetaObject obj = MetaObjects.get(index);
        if (obj == null)
            return;
        int style;
        switch (obj.getType()) {
            case MetaObject.META_BRUSH:
                currentBrush = (MetaBrush)obj;
                style = currentBrush.getStyle();
                if (style == MetaBrush.BS_SOLID) {
                    BaseColor color = currentBrush.getColor();
                    cb.setColorFill(color);
                }
                else if (style == MetaBrush.BS_HATCHED) {
                    BaseColor color = currentBackgroundColor;
                    cb.setColorFill(color);
                }
                break;
            case MetaObject.META_PEN:
            {
                currentPen = (MetaPen)obj;
                style = currentPen.getStyle();
                if (style != MetaPen.PS_NULL) {
                    BaseColor color = currentPen.getColor();
                    cb.setColorStroke(color);
                    cb.setLineWidth(Math.abs(currentPen.getPenWidth() * scalingX / extentWx));
                    switch (style) {
                        case MetaPen.PS_DASH:
                            cb.setLineDash(18, 6, 0);
                            break;
                        case MetaPen.PS_DASHDOT:
                            cb.setLiteral("[9 6 3 6]0 d\n");
                            break;
                        case MetaPen.PS_DASHDOTDOT:
                            cb.setLiteral("[9 3 3 3 3 3]0 d\n");
                            break;
                        case MetaPen.PS_DOT:
                            cb.setLineDash(3, 0);
                            break;
                        default:
                            cb.setLineDash(0);
                            break;
                    }
                }
                break;
            }
            case MetaObject.META_FONT:
            {
                currentFont = (MetaFont)obj;
                break;
            }
        }
    }

    public void deleteMetaObject(int index) {
        MetaObjects.set(index, null);
    }

    public void saveState(PdfContentByte cb) {
        cb.saveState();
        MetaState state = new MetaState(this);
        savedStates.push(state);
    }

    public void restoreState(int index, PdfContentByte cb) {
        int pops;
        if (index < 0)
            pops = Math.min(-index, savedStates.size());
        else
            pops = Math.max(savedStates.size() - index, 0);
        if (pops == 0)
            return;
        MetaState state = null;
        while (pops-- != 0) {
            cb.restoreState();
            state = savedStates.pop();
        }
        setMetaState(state);
    }

    public void cleanup(PdfContentByte cb) {
        int k = savedStates.size();
        while (k-- > 0)
            cb.restoreState();
    }

    public float transformX(int x) {
        return ((float)x - offsetWx) * scalingX / extentWx;
    }

    public float transformY(int y) {
        return (1f - ((float)y - offsetWy) / extentWy) * scalingY;
    }

    public void setScalingX(float scalingX) {
        this.scalingX = scalingX;
    }

    public void setScalingY(float scalingY) {
        this.scalingY = scalingY;
    }

    public void setOffsetWx(int offsetWx) {
        this.offsetWx = offsetWx;
    }

    public void setOffsetWy(int offsetWy) {
        this.offsetWy = offsetWy;
    }

    public void setExtentWx(int extentWx) {
        this.extentWx = extentWx;
    }

    public void setExtentWy(int extentWy) {
        this.extentWy = extentWy;
    }

    public float transformAngle(float angle) {
        float ta = scalingY < 0 ? -angle : angle;
        return (float)(scalingX < 0 ? Math.PI - ta : ta);
    }

    public void setCurrentPoint(Point p) {
        currentPoint = p;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public MetaBrush getCurrentBrush() {
        return currentBrush;
    }

    public MetaPen getCurrentPen() {
        return currentPen;
    }

    public MetaFont getCurrentFont() {
        return currentFont;
    }

    /** Getter for property currentBackgroundColor.
     * @return Value of property currentBackgroundColor.
     */
    public BaseColor getCurrentBackgroundColor() {
        return currentBackgroundColor;
    }

    /** Setter for property currentBackgroundColor.
     * @param currentBackgroundColor New value of property currentBackgroundColor.
     */
    public void setCurrentBackgroundColor(BaseColor currentBackgroundColor) {
        this.currentBackgroundColor = currentBackgroundColor;
    }

    /** Getter for property currentTextColor.
     * @return Value of property currentTextColor.
     */
    public BaseColor getCurrentTextColor() {
        return currentTextColor;
    }

    /** Setter for property currentTextColor.
     * @param currentTextColor New value of property currentTextColor.
     */
    public void setCurrentTextColor(BaseColor currentTextColor) {
        this.currentTextColor = currentTextColor;
    }

    /** Getter for property backgroundMode.
     * @return Value of property backgroundMode.
     */
    public int getBackgroundMode() {
        return backgroundMode;
    }

    /** Setter for property backgroundMode.
     * @param backgroundMode New value of property backgroundMode.
     */
    public void setBackgroundMode(int backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    /** Getter for property textAlign.
     * @return Value of property textAlign.
     */
    public int getTextAlign() {
        return textAlign;
    }

    /** Setter for property textAlign.
     * @param textAlign New value of property textAlign.
     */
    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    /** Getter for property polyFillMode.
     * @return Value of property polyFillMode.
     */
    public int getPolyFillMode() {
        return polyFillMode;
    }

    /** Setter for property polyFillMode.
     * @param polyFillMode New value of property polyFillMode.
     */
    public void setPolyFillMode(int polyFillMode) {
        this.polyFillMode = polyFillMode;
    }

    public void setLineJoinRectangle(PdfContentByte cb) {
        if (lineJoin != 0) {
            lineJoin = 0;
            cb.setLineJoin(0);
        }
    }

    public void setLineJoinPolygon(PdfContentByte cb) {
        if (lineJoin == 0) {
            lineJoin = 1;
            cb.setLineJoin(1);
        }
    }

    public boolean getLineNeutral() {
        return lineJoin == 0;
    }

}
