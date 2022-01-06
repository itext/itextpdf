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
import java.util.HashMap;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

/**
 * Implements the form XObject.
 */

public class PdfTemplate extends PdfContentByte implements IAccessibleElement {
    public static final int TYPE_TEMPLATE = 1;
    public static final int TYPE_IMPORTED = 2;
    public static final int TYPE_PATTERN = 3;
    protected int type;
    /** The indirect reference to this template */
    protected PdfIndirectReference thisReference;
    
    /** The resources used by this template */
    protected PageResources pageResources;
    
    
    /** The bounding box of this template */
    protected Rectangle bBox = new Rectangle(0, 0);
    
    protected PdfArray matrix;
    
    protected PdfTransparencyGroup group;
    
    protected PdfOCG layer;

    protected PdfIndirectReference pageReference;

    protected boolean contentTagged = false;

	/**
	 * A dictionary with additional information
	 * @since 5.1.0
	 */
	private PdfDictionary additional = null;

    protected PdfName role = PdfName.FIGURE;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    private AccessibleElementId id = null;
	
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
        pageResources = new PageResources();
        pageResources.addDefaultColor(wr.getDefaultColorspace());
        thisReference = writer.getPdfIndirectReference();
    }
    
    /**
     * Creates a new template.
     * <P>
     * Creates a new template that is nothing more than a form XObject. This template can be included
     * in this template or in another template. Templates are only written
     * to the output when the document is closed permitting things like showing text in the first page
     * that is only defined in the last page.
     *
     * @param writer the PdfWriter to use
     * @param width the bounding box width
     * @param height the bounding box height
     * @return the created template
     */
    public static PdfTemplate createTemplate(PdfWriter writer, float width, float height) {
        return createTemplate(writer, width, height, null);
    }
    
    static PdfTemplate createTemplate(PdfWriter writer, float width, float height, PdfName forcedName) {
        PdfTemplate template = new PdfTemplate(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template, forcedName);
        return template;
    }

    public boolean isTagged() {
        return super.isTagged() && contentTagged;
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
     * Sets the bounding height of this template.
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
        return bBox.getWidth();
    }
    
    /**
     * Gets the bounding height of this template.
     *
     * @return height the bounding height
     */
    
    public float getHeight() {
        return bBox.getHeight();
    }
    
    public Rectangle getBoundingBox() {
        return bBox;
    }
    
    public void setBoundingBox(Rectangle bBox) {
        this.bBox = bBox;
    }
    
    /**
     * Sets the layer this template belongs to.
     * @param layer the layer this template belongs to
     */    
    public void setLayer(PdfOCG layer) {
        this.layer = layer;
    }
    
    /**
     * Gets the layer this template belongs to.
     * @return the layer this template belongs to or <code>null</code> for no layer defined
     */
    public PdfOCG getLayer() {
        return layer;
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
    
    public PdfIndirectReference getIndirectReference() {
    	// uncomment the null check as soon as we're sure all examples still work
    	if (thisReference == null /* && writer != null */) {
    		thisReference = writer.getPdfIndirectReference();
    	}
        return thisReference;
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
        return getPageResources().getResources();
    }
    
    /**
     * Gets the stream representing this template.
     *
     * @param	compressionLevel	the compressionLevel
     * @return the stream representing this template
     * @since	2.1.3	(replacing the method without param compressionLevel)
     */
    public PdfStream getFormXObject(int compressionLevel) throws IOException {
        return new PdfFormXObject(this, compressionLevel);
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
        tpl.pageResources = pageResources;
        tpl.bBox = new Rectangle(bBox);
        tpl.group = group;
        tpl.layer = layer;
        if (matrix != null) {
            tpl.matrix = new PdfArray(matrix);
        }
        tpl.separator = separator;
        tpl.additional = additional;
        tpl.contentTagged = contentTagged;
        tpl.duplicatedFrom = this;
        return tpl;
    }
    
    public int getType() {
        return type;
    }
    
    PageResources getPageResources() {
        return pageResources;
    }

    /** Getter for property group.
     * @return Value of property group.
     *
     */
    public PdfTransparencyGroup getGroup() {
        return this.group;
    }
    
    /** Setter for property group.
     * @param group New value of property group.
     *
     */
    public void setGroup(PdfTransparencyGroup group) {
        this.group = group;
    }


	/**
	 * Getter for the dictionary with additional information.
	 *
	 * @return a PdfDictionary with additional information.
	 * @since 5.1.0
	 */
	public PdfDictionary getAdditional() {
		return this.additional;
	}

	/**
	 * Sets a dictionary with extra entries, for instance /Measure.
	 *
	 * @param additional
	 *            a PdfDictionary with additional information.
	 * @since 5.1.0
	 */
	public void setAdditional(PdfDictionary additional) {
		this.additional = additional;
	}

    public PdfIndirectReference getCurrentPage() {
        return pageReference == null ? writer.getCurrentPage() : pageReference;
    }

    public PdfIndirectReference getPageReference() {
        return pageReference;
    }

    public void setPageReference(PdfIndirectReference pageReference) {
        this.pageReference = pageReference;
    }

    public boolean isContentTagged() {
        return contentTagged;
    }

    public void setContentTagged(boolean contentTagged) {
        this.contentTagged = contentTagged;
    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null)
            return accessibleAttributes.get(key);
        else
            return null;
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null)
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return accessibleAttributes;
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        if (id == null)
            id = new AccessibleElementId();
        return id;
    }

    public void setId(final AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return true;
    }
}
