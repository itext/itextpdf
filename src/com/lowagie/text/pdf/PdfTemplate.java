/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import java.io.IOException;
import java.awt.Color;

/**
 * Implements the form XObject.
 */

public class PdfTemplate extends PdfContentByte {
    public static final int TYPE_TEMPLATE = 1;
    public static final int TYPE_IMPORTED = 2;
    public static final int TYPE_PATTERN = 3;
    protected int type;
    /** The indirect reference to this template */
    protected PdfIndirectReference thisReference;
    
    /** The fonts used by this template */
    protected PdfFontDictionary fontDictionary;
    
    /** The images and other templates used by this template */
    protected PdfXObjectDictionary xObjectDictionary;
    
    protected PdfColorDictionary colorDictionary;
    
    protected PdfPatternDictionary patternDictionary;
    
    protected PdfShadingDictionary shadingDictionary;
    
    /** The bounding box of this template */
    protected Rectangle bBox = new Rectangle(0, 0);
    
    protected PdfArray matrix;
    
    /**
     *Creates a <CODE>PdfTemplate</CODE>.
     */
    
    protected PdfTemplate() {
        super(null);
        type = TYPE_TEMPLATE;
    }
    
    /**
     * Creates new PdfTemplate
     *
     * @param wr the <CODE>PdfWriter</CODE>
     */
    
    PdfTemplate(PdfWriter wr) {
        super(wr);
        type = TYPE_TEMPLATE;
        fontDictionary = new PdfFontDictionary();
        xObjectDictionary = new PdfXObjectDictionary();
        colorDictionary = new PdfColorDictionary();
        patternDictionary = new PdfPatternDictionary();
        shadingDictionary = new PdfShadingDictionary();
        thisReference = writer.getPdfIndirectReference();
    }
    
    /**
     * Sets the bounding width of this template.
     *
     * @param width the bounding width
     */
    
    public void setWidth(float width) {
        bBox.setLeft(0);
        bBox.setRight(width);
    }
    
    /**
     * Sets the bounding heigth of this template.
     *
     * @param height the bounding height
     */
    
    public void setHeight(float height) {
        bBox.setBottom(0);
        bBox.setTop(height);
    }
    
    /**
     * Gets the bounding width of this template.
     *
     * @return width the bounding width
     */
    public float getWidth() {
        return bBox.width();
    }
    
    /**
     * Gets the bounding heigth of this template.
     *
     * @return heigth the bounding height
     */
    
    public float getHeight() {
        return bBox.height();
    }
    
    public Rectangle getBoundingBox() {
        return bBox;
    }
    
    public void setBoundingBox(Rectangle bBox) {
        this.bBox = bBox;
    }
    
    public void setMatrix(float a, float b, float c, float d, float e, float f) {
		matrix = new PdfArray();
		matrix.add(new PdfNumber(a));
		matrix.add(new PdfNumber(b));
		matrix.add(new PdfNumber(c));
		matrix.add(new PdfNumber(d));
		matrix.add(new PdfNumber(e));
		matrix.add(new PdfNumber(f));
	}

	PdfArray getMatrix() {
		return matrix;
	}
    
    /**
     * Gets the indirect reference to this template.
     *
     * @return the indirect reference to this template
     */
    
    PdfIndirectReference getIndirectReference() {
        return thisReference;
    }
    
    /**
     * Adds a template to this template.
     *
     * @param template the template
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     */
    
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        checkNoPattern(template);
        PdfName name = writer.addDirectTemplateSimple(template);
        content.append("q ");
        content.append(a).append(' ');
        content.append(b).append(' ');
        content.append(c).append(' ');
        content.append(d).append(' ');
        content.append(e).append(' ');
        content.append(f).append(" cm ");
        content.append(name.toString()).append(" Do Q").append_i(separator);
        xObjectDictionary.put(name, template.getIndirectReference());
    }
    
    /**
     * Adds an <CODE>Image</CODE> to this template. The positioning of the <CODE>Image</CODE>
     * is done with the transformation matrix. To position an <CODE>image</CODE> at (x,y)
     * use addImage(image, image_width, 0, 0, image_height, x, y).
     * @param image the <CODE>Image</CODE> object
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     * @throws DocumentException on error
     */
    
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        try {
            PdfName name;
            if (image.isImgTemplate()) {
                name = pdf.addDirectImageSimple(image);
                PdfTemplate template = image.templateData();
                float w = template.getWidth();
                float h = template.getHeight();
                addTemplate(template, a / w, b / w, c / h, d / h, e, f);
            }
            else {
                Image maskImage = image.getImageMask();
                if (maskImage != null) {
                    PdfName mname = pdf.addDirectImageSimple(maskImage);
                    xObjectDictionary.put(mname, writer.getImageReference(mname));
                }
                name = pdf.addDirectImageSimple(image);
                content.append("q ");
                content.append(a).append(' ');
                content.append(b).append(' ');
                content.append(c).append(' ');
                content.append(d).append(' ');
                content.append(e).append(' ');
                content.append(f).append(" cm ");
                content.append(name.toString()).append(" Do Q").append_i(separator);
            }
            if (!image.isImgTemplate())
                xObjectDictionary.put(name, writer.getImageReference(name));
        }
        catch (Exception ee) {
            throw new DocumentException(ee.getMessage());
        }
    }
    
    public void setColorFill(PdfSpotColor sp, float tint) {
        state.colorDetails = writer.addSimple(sp);
        colorDictionary.put(state.colorDetails.getColorName(), state.colorDetails.getIndirectReference());
        content.append(state.colorDetails.getColorName().toPdf(null)).append(" cs ").append(tint).append(" scn").append_i(separator);
    }
    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        state.colorDetails = writer.addSimple(sp);
        colorDictionary.put(state.colorDetails.getColorName(), state.colorDetails.getIndirectReference());
        content.append(state.colorDetails.getColorName().toPdf(null)).append(" CS ").append(tint).append(" SCN").append_i(separator);
    }
    
    public void setPatternFill(PdfPatternPainter p) {
        if (p.isStencil()) {
            setPatternFill(p, p.getDefaultColor());
            return;
        }
        checkWriter();
        PdfName name = writer.addSimplePattern(p);
        patternDictionary.put(name, p.getIndirectReference());
        content.append(PdfName.PATTERN.toPdf(null)).append(" cs ").append(name.toPdf(null)).append(" scn").append_i(separator);
    }
    
    public void setPatternStroke(PdfPatternPainter p) {
        if (p.isStencil()) {
            setPatternStroke(p, p.getDefaultColor());
            return;
        }
        checkWriter();
        PdfName name = writer.addSimplePattern(p);
        patternDictionary.put(name, p.getIndirectReference());
        content.append(PdfName.PATTERN.toPdf(null)).append(" CS ").append(name.toPdf(null)).append(" SCN").append_i(separator);
    }

    public void setPatternFill(PdfPatternPainter p, Color color, float tint) {
        checkWriter();
        if (!p.isStencil())
            throw new RuntimeException("An uncolored pattern was expected.");
        PdfName name = writer.addSimplePattern(p);
        patternDictionary.put(name, p.getIndirectReference());
        ColorDetails csDetail = writer.addSimplePatternColorspace(color);
        colorDictionary.put(csDetail.getColorName(), csDetail.getIndirectReference());
        content.append(csDetail.getColorName().toPdf(null)).append(" cs").append_i(separator);
        outputColorNumbers(color, tint);
        content.append(' ').append(name.toPdf(null)).append(" scn").append_i(separator);
    }
    
    public void setPatternStroke(PdfPatternPainter p, Color color, float tint) {
        checkWriter();
        if (!p.isStencil())
            throw new RuntimeException("An uncolored pattern was expected.");
        PdfName name = writer.addSimplePattern(p);
        patternDictionary.put(name, p.getIndirectReference());
        ColorDetails csDetail = writer.addSimplePatternColorspace(color);
        colorDictionary.put(csDetail.getColorName(), csDetail.getIndirectReference());
        content.append(csDetail.getColorName().toPdf(null)).append(" CS").append_i(separator);
        outputColorNumbers(color, tint);
        content.append(' ').append(name.toPdf(null)).append(" SCN").append_i(separator);
    }

    public void paintShading(PdfShading shading) {
        writer.addSimpleShading(shading);
        shadingDictionary.put(shading.getShadingName(), shading.getShadingReference());
        content.append(shading.getShadingName().toPdf(null)).append(" sh").append_i(separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null)
            colorDictionary.put(details.getColorName(), details.getIndirectReference());
    }
    
    public void setShadingFill(PdfShadingPattern shading) {
        writer.addSimpleShadingPattern(shading);
        patternDictionary.put(shading.getPatternName(), shading.getPatternReference());
        content.append(PdfName.PATTERN.toPdf(null)).append(" cs ").append(shading.getPatternName().toPdf(null)).append(" scn").append_i(separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null)
            colorDictionary.put(details.getColorName(), details.getIndirectReference());
    }

    public void setShadingStroke(PdfShadingPattern shading) {
        writer.addSimpleShadingPattern(shading);
        patternDictionary.put(shading.getPatternName(), shading.getPatternReference());
        content.append(PdfName.PATTERN.toPdf(null)).append(" CS ").append(shading.getPatternName().toPdf(null)).append(" SCN").append_i(separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null)
            colorDictionary.put(details.getColorName(), details.getIndirectReference());
    }
    
    public void beginVariableText() {
        content.append("/Tx BMC ");
    }
    
    public void endVariableText() {
        content.append("EMC ");
    }
    
    /**
     * Constructs the resources used by this template.
     *
     * @return the resources used by this template
     */
    
    PdfObject getResources() {
        PdfResources resources = new PdfResources();
        int procset = PdfProcSet.PDF;
        if (fontDictionary.containsFont()) {
            resources.add(fontDictionary);
            procset |= PdfProcSet.TEXT;
        }
        if (xObjectDictionary.containsXObject()) {
            resources.add(xObjectDictionary);
            procset |= PdfProcSet.IMAGEC;
        }
        if (colorDictionary.containsColorSpace())
            resources.add(colorDictionary);
        if (patternDictionary.containsPattern())
            resources.add(patternDictionary);
        if (shadingDictionary.containsShading())
            resources.add(shadingDictionary);
        resources.add(new PdfProcSet(procset));
        return resources;
    }
    
    /**
     * Gets the stream representing this template.
     *
     * @return the stream representing this template
     */
    
    PdfStream getFormXObject() throws IOException {
        return new PdfFormXObject(this);
    }
    
    /**
     * Set the font and the size for the subsequent text writing.
     *
     * @param bf the font
     * @param size the font size in points
     */
    
    public void setFontAndSize(BaseFont bf, float size) {
        state.size = size;
        state.fontDetails = writer.addSimple(bf);
        PdfName name = state.fontDetails.getFontName();
        content.append(name.toPdf(null)).append(' ').append(size).append(" Tf").append_i(separator);
        fontDictionary.put(name, state.fontDetails.getIndirectReference());
    }
    
    /**
     * Gets a duplicate of this <CODE>PdfTemplate</CODE>. All
     * the members are copied by reference but the buffer stays different.
     * @return a copy of this <CODE>PdfTemplate</CODE>
     */
    
    public PdfContentByte getDuplicate() {
        PdfTemplate tpl = new PdfTemplate();
        tpl.writer = writer;
        tpl.pdf = pdf;
        tpl.thisReference = thisReference;
        tpl.fontDictionary = fontDictionary;
        tpl.xObjectDictionary = xObjectDictionary;
        tpl.colorDictionary = colorDictionary;
        tpl.patternDictionary = patternDictionary;
        tpl.shadingDictionary = shadingDictionary;
        tpl.bBox = new Rectangle(bBox);
        if (matrix != null) {
            tpl.matrix = new PdfArray(matrix);
        }
        return tpl;
    }
    
    public int getType() {
        return type;
    }
}
