/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Paulo Soares.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;
import java.util.HashMap;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import java.io.IOException;

/**
 * Implements the form XObject.
 */

public class PdfTemplate extends PdfContentByte {
    
/** The indirect reference to this template */
    protected PdfIndirectReference thisReference;
    
/** The fonts used by this template */
    protected PdfFontDictionary fontDictionary;
    
/** The images and other templates used by this template */
    protected PdfXObjectDictionary xObjectDictionary;

    protected PdfColorDictionary colorDictionary;    
/** The bounding box of this template */
    protected Rectangle bBox = new Rectangle(0, 0);
    
/**
 *Creates a <CODE>PdfTemplate</CODE>.
 */
    
    protected PdfTemplate() {
        super(null);
    }
    
/**
 * Creates new PdfTemplate
 *
 * @param wr the <CODE>PdfWriter</CODE>
 */
    
    PdfTemplate(PdfWriter wr) {
        super(wr);
        fontDictionary = new PdfFontDictionary();
        xObjectDictionary = new PdfXObjectDictionary();
        colorDictionary = new PdfColorDictionary();
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
    public float getWidth()
    {
        return bBox.width();
    }
    
/**
 * Gets the bounding heigth of this template.
 *
 * @return heigth the bounding height
 */
    
    public float getHeight()
    {
        return bBox.height();
    }
    
    public Rectangle getBoundingBox() {
        return bBox;
    }
    
    public void setBoundingBox(Rectangle bBox) {
        this.bBox = bBox;
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
        PdfName name = writer.addDirectTemplateSimple(template);
        content.append("q ");
        content.append(a).append(' ');
        content.append(b).append(' ');
        content.append(c).append(' ');
        content.append(d).append(' ');
        content.append(e).append(' ');
        content.append(f).append(" cm ");
        content.append(name.toString()).append(" Do Q\n");
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
            if (image.isImgTemplate()) {
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
                PdfName name = pdf.addDirectImageSimple(image);
                content.append("q ");
                content.append(a).append(' ');
                content.append(b).append(' ');
                content.append(c).append(' ');
                content.append(d).append(' ');
                content.append(e).append(' ');
                content.append(f).append(" cm ");
                content.append(name.toString()).append(" Do Q\n");
                xObjectDictionary.put(name, writer.getImageReference(name));
            }
        }
        catch (Exception ee) {
            throw new DocumentException(ee.getMessage());
        }
    }

    public void setColorFill(PdfSpotColor sp, float tint) {
        state.colorDetails = writer.addSimple(sp);
        colorDictionary.put(state.colorDetails.getColorName(), state.colorDetails.getIndirectReference());
        content.append(state.colorDetails.getColorName().toPdf(null)).append(" cs ").append(tint).append(" scn\n");
    }
    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        state.colorDetails = writer.addSimple(sp);
        colorDictionary.put(state.colorDetails.getColorName(), state.colorDetails.getIndirectReference());
        content.append(state.colorDetails.getColorName().toPdf(null)).append(" CS ").append(tint).append(" SCN\n");
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
        content.append(name.toPdf(null)).append(' ').append(size).append(" Tf\n");
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
        tpl.bBox = new Rectangle(bBox);
        return tpl;
    }
    
    public boolean isImportedPage() {
        return false;
    }
}
