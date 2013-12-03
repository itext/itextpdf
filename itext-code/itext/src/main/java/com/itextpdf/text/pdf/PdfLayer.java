/*
 * $Id: PdfLayer.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.error_messages.MessageLocalization;
/**
 * An optional content group is a dictionary representing a collection of graphics
 * that can be made visible or invisible dynamically by users of viewer applications.
 * In iText they are referenced as layers.
 *
 * @author Paulo Soares
 */
public class PdfLayer extends PdfDictionary implements PdfOCG {
    protected PdfIndirectReference ref;
    protected ArrayList<PdfLayer> children;
    protected PdfLayer parent;
    protected String title;

    /**
     * Holds value of property on.
     */
    private boolean on = true;

    /**
     * Holds value of property onPanel.
     */
    private boolean onPanel = true;

    PdfLayer(String title) {
        this.title = title;
    }

    /**
     * Creates a title layer. A title layer is not really a layer but a collection of layers
     * under the same title heading.
     * @param title the title text
     * @param writer the <CODE>PdfWriter</CODE>
     * @return the title layer
     */
    public static PdfLayer createTitle(String title, PdfWriter writer) {
        if (title == null)
            throw new NullPointerException(MessageLocalization.getComposedMessage("title.cannot.be.null"));
        PdfLayer layer = new PdfLayer(title);
        writer.registerLayer(layer);
        return layer;
    }
    /**
     * Creates a new layer.
     * @param name the name of the layer
     * @param writer the writer
     * @throws IOException 
     */
    public PdfLayer(String name, PdfWriter writer) throws IOException {
        super(PdfName.OCG);
        setName(name);
        if (writer instanceof PdfStamperImp)
        	ref = writer.addToBody(this).getIndirectReference();
        else
        	ref = writer.getPdfIndirectReference();
        writer.registerLayer(this);
    }

    String getTitle() {
        return title;
    }

    /**
     * Adds a child layer. Nested layers can only have one parent.
     * @param child the child layer
     */
    public void addChild(PdfLayer child) {
        if (child.parent != null)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.layer.1.already.has.a.parent", child.getAsString(PdfName.NAME).toUnicodeString()));
        child.parent = this;
        if (children == null)
            children = new ArrayList<PdfLayer>();
        children.add(child);
    }


    /**
     * Gets the parent layer.
     * @return the parent layer or <CODE>null</CODE> if the layer has no parent
     */
    public PdfLayer getParent() {
        return parent;
    }

    /**
     * Gets the children layers.
     * @return the children layers or <CODE>null</CODE> if the layer has no children
     */
    public ArrayList<PdfLayer> getChildren() {
        return children;
    }

    /**
     * Gets the <CODE>PdfIndirectReference</CODE> that represents this layer.
     * @return the <CODE>PdfIndirectReference</CODE> that represents this layer
     */
    public PdfIndirectReference getRef() {
        return ref;
    }

    /**
     * Sets the <CODE>PdfIndirectReference</CODE> that represents this layer.
     * This can only be done from PdfStamperImp.
     * @param	ref	The reference to the OCG object
     * @since	2.1.2
     */
    void setRef(PdfIndirectReference ref) {
    	this.ref = ref;
    }

    /**
     * Sets the name of this layer.
     * @param name the name of this layer
     */
    public void setName(String name) {
        put(PdfName.NAME, new PdfString(name, PdfObject.TEXT_UNICODE));
    }

    /**
     * Gets the dictionary representing the layer. It just returns <CODE>this</CODE>.
     * @return the dictionary representing the layer
     */
    public PdfObject getPdfObject() {
        return this;
    }

    /**
     * Gets the initial visibility of the layer.
     * @return the initial visibility of the layer
     */
    public boolean isOn() {
        return this.on;
    }

    /**
     * Sets the initial visibility of the layer.
     * @param on the initial visibility of the layer
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    private PdfDictionary getUsage() {
        PdfDictionary usage = getAsDict(PdfName.USAGE);
        if (usage == null) {
            usage = new PdfDictionary();
            put(PdfName.USAGE, usage);
        }
        return usage;
    }

    /**
     * Used by the creating application to store application-specific
     * data associated with this optional content group.
     * @param creator a text string specifying the application that created the group
     * @param subtype a string defining the type of content controlled by the group. Suggested
     * values include but are not limited to <B>Artwork</B>, for graphic-design or publishing
     * applications, and <B>Technical</B>, for technical designs such as building plans or
     * schematics
     */
    public void setCreatorInfo(String creator, String subtype) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.CREATOR, new PdfString(creator, PdfObject.TEXT_UNICODE));
        dic.put(PdfName.SUBTYPE, new PdfName(subtype));
        usage.put(PdfName.CREATORINFO, dic);
    }

    /**
     * Specifies the language of the content controlled by this
     * optional content group
     * @param lang a language string which specifies a language and possibly a locale
     * (for example, <B>es-MX</B> represents Mexican Spanish)
     * @param preferred used by viewer applications when there is a partial match but no exact
     * match between the system language and the language strings in all usage dictionaries
     */
    public void setLanguage(String lang, boolean preferred) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.LANG, new PdfString(lang, PdfObject.TEXT_UNICODE));
        if (preferred)
            dic.put(PdfName.PREFERRED, PdfName.ON);
        usage.put(PdfName.LANGUAGE, dic);
    }

    /**
     * Specifies the recommended state for content in this
     * group when the document (or part of it) is saved by a viewer application to a format
     * that does not support optional content (for example, an earlier version of
     * PDF or a raster image format).
     * @param export the export state
     */
    public void setExport(boolean export) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.EXPORTSTATE, export ? PdfName.ON : PdfName.OFF);
        usage.put(PdfName.EXPORT, dic);
    }

    /**
     * Specifies a range of magnifications at which the content
     * in this optional content group is best viewed.
     * @param min the minimum recommended magnification factors at which the group
     * should be ON. A negative value will set the default to 0
     * @param max the maximum recommended magnification factor at which the group
     * should be ON. A negative value will set the largest possible magnification supported by the
     * viewer application
     */
    public void setZoom(float min, float max) {
        if (min <= 0 && max < 0)
            return;
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        if (min > 0)
            dic.put(PdfName.MIN_LOWER_CASE, new PdfNumber(min));
        if (max >= 0)
            dic.put(PdfName.MAX_LOWER_CASE, new PdfNumber(max));
        usage.put(PdfName.ZOOM, dic);
    }

    /**
     * Specifies that the content in this group is intended for
     * use in printing
     * @param subtype a name specifying the kind of content controlled by the group;
     * for example, <B>Trapping</B>, <B>PrintersMarks</B> and <B>Watermark</B>
     * @param printstate indicates that the group should be
     * set to that state when the document is printed from a viewer application
     */
    public void setPrint(String subtype, boolean printstate) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.SUBTYPE, new PdfName(subtype));
        dic.put(PdfName.PRINTSTATE, printstate ? PdfName.ON : PdfName.OFF);
        usage.put(PdfName.PRINT, dic);
    }

    /**
     * Indicates that the group should be set to that state when the
     * document is opened in a viewer application.
     * @param view the view state
     */
    public void setView(boolean view) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.VIEWSTATE, view ? PdfName.ON : PdfName.OFF);
        usage.put(PdfName.VIEW, dic);
    }
    
    /**
     * Indicates that the group contains a pagination artifact.
     * @param pe one of the following names: "HF" (Header Footer),
     * "FG" (Foreground), "BG" (Background), or "L" (Logo).
     * @since 5.0.2
     */
    public void setPageElement(String pe) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.SUBTYPE, new PdfName(pe));
        usage.put(PdfName.PAGEELEMENT, dic);
    }
    
    /**
     * One of more users for whom this optional content group is primarily intended.
     * @param type should be "Ind" (Individual), "Ttl" (Title), or "Org" (Organization).
     * @param names one or more names
     * @since 5.0.2
     */
    public void setUser(String type, String... names) {
        PdfDictionary usage = getUsage();
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.TYPE, new PdfName(type));
        PdfArray arr = new PdfArray();
        for (String s : names)
        	arr.add(new PdfString(s, PdfObject.TEXT_UNICODE));
        usage.put(PdfName.NAME, arr);
        usage.put(PdfName.USER, dic);
    }

    /**
     * Gets the layer visibility in Acrobat's layer panel
     * @return the layer visibility in Acrobat's layer panel
     */
    public boolean isOnPanel() {
        return this.onPanel;
    }

    /**
     * Sets the visibility of the layer in Acrobat's layer panel. If <CODE>false</CODE>
     * the layer cannot be directly manipulated by the user. Note that any children layers will
     * also be absent from the panel.
     * @param onPanel the visibility of the layer in Acrobat's layer panel
     */
    public void setOnPanel(boolean onPanel) {
        this.onPanel = onPanel;
    }

}
