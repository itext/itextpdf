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

import java.io.IOException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.BaseColor;
/** Implements the shading dictionary (or stream).
 *
 * @author Paulo Soares
 */
public class PdfShading {

    protected PdfDictionary shading;
    
    protected PdfWriter writer;
    
    protected int shadingType;
    
    protected ColorDetails colorDetails;
    
    protected PdfName shadingName;
    
    protected PdfIndirectReference shadingReference;
    
    private BaseColor cspace;
    
    /** Holds value of property bBox. */
    protected float[] bBox;
    
    /** Holds value of property antiAlias. */
    protected boolean antiAlias = false;
    
    /** Creates new PdfShading */
    protected PdfShading(PdfWriter writer) {
        this.writer = writer;
    }
    
    protected void setColorSpace(BaseColor color) {
        cspace = color;
        int type = ExtendedColor.getType(color);
        PdfObject colorSpace = null;
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                colorSpace = PdfName.DEVICEGRAY;
                break;
            }
            case ExtendedColor.TYPE_CMYK: {
                colorSpace = PdfName.DEVICECMYK;
                break;
            }
            case ExtendedColor.TYPE_SEPARATION: {
                SpotColor spot = (SpotColor)color;
                colorDetails = writer.addSimple(spot.getPdfSpotColor());
                colorSpace = colorDetails.getIndirectReference();
                break;
            }
            case ExtendedColor.TYPE_DEVICEN: {
                DeviceNColor deviceNColor = (DeviceNColor)color;
                colorDetails = writer.addSimple(deviceNColor.getPdfDeviceNColor());
                colorSpace = colorDetails.getIndirectReference();
                break;
            }
            case ExtendedColor.TYPE_PATTERN:
            case ExtendedColor.TYPE_SHADING: {
                throwColorSpaceError();
            }
            default:
                colorSpace = PdfName.DEVICERGB;
                break;
        }
        shading.put(PdfName.COLORSPACE, colorSpace);
    }
    
    public BaseColor getColorSpace() {
        return cspace;
    }
    
    public static void throwColorSpaceError() {
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.tiling.or.shading.pattern.cannot.be.used.as.a.color.space.in.a.shading.pattern"));
    }
    
    public static void checkCompatibleColors(BaseColor c1, BaseColor c2) {
        int type1 = ExtendedColor.getType(c1);
        int type2 = ExtendedColor.getType(c2);
        if (type1 != type2)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("both.colors.must.be.of.the.same.type"));
        if (type1 == ExtendedColor.TYPE_SEPARATION && ((SpotColor)c1).getPdfSpotColor() != ((SpotColor)c2).getPdfSpotColor())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.spot.color.must.be.the.same.only.the.tint.can.vary"));
        if (type1 == ExtendedColor.TYPE_PATTERN || type1 == ExtendedColor.TYPE_SHADING)
            throwColorSpaceError();
    }
    
    public static float[] getColorArray(BaseColor color) {
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                return new float[]{((GrayColor)color).getGray()};
            }
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor)color;
                return new float[]{cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack()};
            }
            case ExtendedColor.TYPE_SEPARATION: {
                return new float[]{((SpotColor)color).getTint()};
            }
            case ExtendedColor.TYPE_DEVICEN: {
                return ((DeviceNColor)color).getTints();
            }
            case ExtendedColor.TYPE_RGB: {
                return new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f};
            }
        }
        throwColorSpaceError();
        return null;
    }

    public static PdfShading type1(PdfWriter writer, BaseColor colorSpace, float domain[], float tMatrix[], PdfFunction function) {
        PdfShading sp = new PdfShading(writer);
        sp.shading = new PdfDictionary();
        sp.shadingType = 1;
        sp.shading.put(PdfName.SHADINGTYPE, new PdfNumber(sp.shadingType));
        sp.setColorSpace(colorSpace);
        if (domain != null)
            sp.shading.put(PdfName.DOMAIN, new PdfArray(domain));
        if (tMatrix != null)
            sp.shading.put(PdfName.MATRIX, new PdfArray(tMatrix));
        sp.shading.put(PdfName.FUNCTION, function.getReference());
        return sp;
    }
    
    public static PdfShading type2(PdfWriter writer, BaseColor colorSpace, float coords[], float domain[], PdfFunction function, boolean extend[]) {
        PdfShading sp = new PdfShading(writer);
        sp.shading = new PdfDictionary();
        sp.shadingType = 2;
        sp.shading.put(PdfName.SHADINGTYPE, new PdfNumber(sp.shadingType));
        sp.setColorSpace(colorSpace);
        sp.shading.put(PdfName.COORDS, new PdfArray(coords));
        if (domain != null)
            sp.shading.put(PdfName.DOMAIN, new PdfArray(domain));
        sp.shading.put(PdfName.FUNCTION, function.getReference());
        if (extend != null && (extend[0] || extend[1])) {
            PdfArray array = new PdfArray(extend[0] ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
            array.add(extend[1] ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
            sp.shading.put(PdfName.EXTEND, array);
        }
        return sp;
    }

    public static PdfShading type3(PdfWriter writer, BaseColor colorSpace, float coords[], float domain[], PdfFunction function, boolean extend[]) {
        PdfShading sp = type2(writer, colorSpace, coords, domain, function, extend);
        sp.shadingType = 3;
        sp.shading.put(PdfName.SHADINGTYPE, new PdfNumber(sp.shadingType));
        return sp;
    }
    
    public static PdfShading simpleAxial(PdfWriter writer, float x0, float y0, float x1, float y1, BaseColor startColor, BaseColor endColor, boolean extendStart, boolean extendEnd) {
        checkCompatibleColors(startColor, endColor);
        PdfFunction function = PdfFunction.type2(writer, new float[]{0, 1}, null, getColorArray(startColor),
            getColorArray(endColor), 1);
        return type2(writer, startColor, new float[]{x0, y0, x1, y1}, null, function, new boolean[]{extendStart, extendEnd});
    }
    
    public static PdfShading simpleAxial(PdfWriter writer, float x0, float y0, float x1, float y1, BaseColor startColor, BaseColor endColor) {
        return simpleAxial(writer, x0, y0, x1, y1, startColor, endColor, true, true);
    }
    
    public static PdfShading simpleRadial(PdfWriter writer, float x0, float y0, float r0, float x1, float y1, float r1, BaseColor startColor, BaseColor endColor, boolean extendStart, boolean extendEnd) {
        checkCompatibleColors(startColor, endColor);
        PdfFunction function = PdfFunction.type2(writer, new float[]{0, 1}, null, getColorArray(startColor),
            getColorArray(endColor), 1);
        return type3(writer, startColor, new float[]{x0, y0, r0, x1, y1, r1}, null, function, new boolean[]{extendStart, extendEnd});
    }

    public static PdfShading simpleRadial(PdfWriter writer, float x0, float y0, float r0, float x1, float y1, float r1, BaseColor startColor, BaseColor endColor) {
        return simpleRadial(writer, x0, y0, r0, x1, y1, r1, startColor, endColor, true, true);
    }

    PdfName getShadingName() {
        return shadingName;
    }
    
    PdfIndirectReference getShadingReference() {
        if (shadingReference == null)
            shadingReference = writer.getPdfIndirectReference();
        return shadingReference;
    }
    
    void setName(int number) {
        shadingName = new PdfName("Sh" + number);
    }
    
    public void addToBody() throws IOException {
        if (bBox != null)
            shading.put(PdfName.BBOX, new PdfArray(bBox));
        if (antiAlias)
            shading.put(PdfName.ANTIALIAS, PdfBoolean.PDFTRUE);
        writer.addToBody(shading, getShadingReference());
    }
    
    PdfWriter getWriter() {
        return writer;
    }
    
    ColorDetails getColorDetails() {
        return colorDetails;
    }
    
    public float[] getBBox() {
        return bBox;
    }
    
    public void setBBox(float[] bBox) {
        if (bBox.length != 4)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bbox.must.be.a.4.element.array"));
        this.bBox = bBox;
    }
    
    public boolean isAntiAlias() {
        return antiAlias;
    }
    
    public void setAntiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
    }
    
}
