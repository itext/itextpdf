/*
 * $Id: PdfFormField.java 5914 2013-07-28 14:18:11Z blowagie $
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
import java.util.ArrayList;

import com.itextpdf.text.Rectangle;

/** Implements form fields.
 *
 * @author Paulo Soares
 */
public class PdfFormField extends PdfAnnotation {

    public static final int FF_READ_ONLY = 1;
    public static final int FF_REQUIRED = 2;
    public static final int FF_NO_EXPORT = 4;
    public static final int FF_NO_TOGGLE_TO_OFF = 16384;
    public static final int FF_RADIO = 32768;
    public static final int FF_PUSHBUTTON = 65536;
    public static final int FF_MULTILINE = 4096;
    public static final int FF_PASSWORD = 8192;
    public static final int FF_COMBO = 131072;
    public static final int FF_EDIT = 262144;
    public static final int FF_FILESELECT = 1048576;
    public static final int FF_MULTISELECT = 2097152;
    public static final int FF_DONOTSPELLCHECK = 4194304;
    public static final int FF_DONOTSCROLL = 8388608;
    public static final int FF_COMB = 16777216;
    public static final int FF_RADIOSINUNISON = 1 << 25;
    /**
     * Allows text fields to support rich text.
     * @since 5.0.6
     */
    public static final int FF_RICHTEXT = 1 << 25;
    public static final int Q_LEFT = 0;
    public static final int Q_CENTER = 1;
    public static final int Q_RIGHT = 2;
    public static final int MK_NO_ICON = 0;
    public static final int MK_NO_CAPTION = 1;
    public static final int MK_CAPTION_BELOW = 2;
    public static final int MK_CAPTION_ABOVE = 3;
    public static final int MK_CAPTION_RIGHT = 4;
    public static final int MK_CAPTION_LEFT = 5;
    public static final int MK_CAPTION_OVERLAID = 6;
    public static final PdfName IF_SCALE_ALWAYS = PdfName.A;
    public static final PdfName IF_SCALE_BIGGER = PdfName.B;
    public static final PdfName IF_SCALE_SMALLER = PdfName.S;
    public static final PdfName IF_SCALE_NEVER = PdfName.N;
    public static final PdfName IF_SCALE_ANAMORPHIC = PdfName.A;
    public static final PdfName IF_SCALE_PROPORTIONAL = PdfName.P;
    public static final boolean MULTILINE = true;
    public static final boolean SINGLELINE = false;
    public static final boolean PLAINTEXT = false;
    public static final boolean PASSWORD = true;
    static PdfName mergeTarget[] = {PdfName.FONT, PdfName.XOBJECT, PdfName.COLORSPACE, PdfName.PATTERN};

    /** Holds value of property parent. */
    protected PdfFormField parent;

    protected ArrayList<PdfFormField> kids;

/**
 * Constructs a new <CODE>PdfAnnotation</CODE> of subtype link (Action).
 */

    public PdfFormField(PdfWriter writer, float llx, float lly, float urx, float ury, PdfAction action) {
        super(writer, llx, lly, urx, ury, action);
        put(PdfName.TYPE, PdfName.ANNOT);
        put(PdfName.SUBTYPE, PdfName.WIDGET);
        annotation = true;
    }

    /** Creates new PdfFormField */
    protected PdfFormField(PdfWriter writer) {
        super(writer, null);
        form = true;
        annotation = false;
    }

    public void setWidget(Rectangle rect, PdfName highlight) {
        put(PdfName.TYPE, PdfName.ANNOT);
        put(PdfName.SUBTYPE, PdfName.WIDGET);
        put(PdfName.RECT, new PdfRectangle(rect));
        annotation = true;
        if (highlight != null && !highlight.equals(HIGHLIGHT_INVERT))
            put(PdfName.H, highlight);
    }

    public static PdfFormField createEmpty(PdfWriter writer) {
        PdfFormField field = new PdfFormField(writer);
        return field;
    }

    public void setButton(int flags) {
        put(PdfName.FT, PdfName.BTN);
        if (flags != 0)
            put(PdfName.FF, new PdfNumber(flags));
    }

    protected static PdfFormField createButton(PdfWriter writer, int flags) {
        PdfFormField field = new PdfFormField(writer);
        field.setButton(flags);
        return field;
    }

    public static PdfFormField createPushButton(PdfWriter writer) {
        return createButton(writer, FF_PUSHBUTTON);
    }

    public static PdfFormField createCheckBox(PdfWriter writer) {
        return createButton(writer, 0);
    }

    public static PdfFormField createRadioButton(PdfWriter writer, boolean noToggleToOff) {
        return createButton(writer, FF_RADIO + (noToggleToOff ? FF_NO_TOGGLE_TO_OFF : 0));
    }

    public static PdfFormField createTextField(PdfWriter writer, boolean multiline, boolean password, int maxLen) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.TX);
        int flags = multiline ? FF_MULTILINE : 0;
        flags += password ? FF_PASSWORD : 0;
        field.put(PdfName.FF, new PdfNumber(flags));
        if (maxLen > 0)
            field.put(PdfName.MAXLEN, new PdfNumber(maxLen));
        return field;
    }

    protected static PdfFormField createChoice(PdfWriter writer, int flags, PdfArray options, int topIndex) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.CH);
        field.put(PdfName.FF, new PdfNumber(flags));
        field.put(PdfName.OPT, options);
        if (topIndex > 0)
            field.put(PdfName.TI, new PdfNumber(topIndex));
        return field;
    }

    public static PdfFormField createList(PdfWriter writer, String options[], int topIndex) {
        return createChoice(writer, 0, processOptions(options), topIndex);
    }

    public static PdfFormField createList(PdfWriter writer, String options[][], int topIndex) {
        return createChoice(writer, 0, processOptions(options), topIndex);
    }

    public static PdfFormField createCombo(PdfWriter writer, boolean edit, String options[], int topIndex) {
        return createChoice(writer, FF_COMBO + (edit ? FF_EDIT : 0), processOptions(options), topIndex);
    }

    public static PdfFormField createCombo(PdfWriter writer, boolean edit, String options[][], int topIndex) {
        return createChoice(writer, FF_COMBO + (edit ? FF_EDIT : 0), processOptions(options), topIndex);
    }

    protected static PdfArray processOptions(String options[]) {
        PdfArray array = new PdfArray();
        for (int k = 0; k < options.length; ++k) {
            array.add(new PdfString(options[k], PdfObject.TEXT_UNICODE));
        }
        return array;
    }

    protected static PdfArray processOptions(String options[][]) {
        PdfArray array = new PdfArray();
        for (int k = 0; k < options.length; ++k) {
            String subOption[] = options[k];
            PdfArray ar2 = new PdfArray(new PdfString(subOption[0], PdfObject.TEXT_UNICODE));
            ar2.add(new PdfString(subOption[1], PdfObject.TEXT_UNICODE));
            array.add(ar2);
        }
        return array;
    }

    public static PdfFormField createSignature(PdfWriter writer) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.SIG);
        return field;
    }

    /** Getter for property parent.
     * @return Value of property parent.
     */
    public PdfFormField getParent() {
        return parent;
    }

    public void addKid(PdfFormField field) {
        field.parent = this;
        if (kids == null)
            kids = new ArrayList<PdfFormField>();
        kids.add(field);
    }

    public ArrayList<PdfFormField> getKids() {
        return kids;
    }

    /**
     * ORs together the given flags with the current /Ff value.  
     * @param flags flags to be added.
     * @return the old flag value
     */
    public int setFieldFlags(int flags) {
        PdfNumber obj = (PdfNumber)get(PdfName.FF);
        int old;
        if (obj == null)
            old = 0;
        else
            old = obj.intValue();
        int v = old | flags;
        put(PdfName.FF, new PdfNumber(v));
        return old;
    }

    public void setValueAsString(String s) {
        put(PdfName.V, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    public void setValueAsName(String s) {
        put(PdfName.V, new PdfName(s));
    }

    public void setValue(PdfSignature sig) {
        put(PdfName.V, sig);
    }
    
    /**
     * Sets the rich value for this field.  
     * It is suggested that the regular value of this field be set to an 
     * equivalent value.  Rich text values are only supported since PDF 1.5,
     * and require that the FF_RV flag be set.  See PDF Reference chapter 
     * 12.7.3.4 for details.
     * @param rv HTML markup for the rich value of this field
     * @since 5.0.6
     */
    public void setRichValue(String rv) {
    	put(PdfName.RV, new PdfString( rv ));
    }

    public void setDefaultValueAsString(String s) {
        put(PdfName.DV, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    public void setDefaultValueAsName(String s) {
        put(PdfName.DV, new PdfName(s));
    }

    public void setFieldName(String s) {
        if (s != null)
            put(PdfName.T, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    /**
     * The "user name" is the text shown as a tool.
     * @param s user name.
     */
    public void setUserName(String s) {
        put(PdfName.TU, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    /**
     * The mapping name is the name this field uses when submitting form data.
     * @param s
     */
    public void setMappingName(String s) {
        put(PdfName.TM, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    /**
     * Sets text alginment for this field
     * @param v  one of the Q_* contstants
     */
    public void setQuadding(int v) {
        put(PdfName.Q, new PdfNumber(v));
    }

    static void mergeResources(PdfDictionary result, PdfDictionary source, PdfStamperImp writer) {
        PdfDictionary dic = null;
        PdfDictionary res = null;
        PdfName target = null;
        for (int k = 0; k < mergeTarget.length; ++k) {
            target = mergeTarget[k];
            PdfDictionary pdfDict = source.getAsDict(target);
            if ((dic = pdfDict) != null) {
                if ((res = (PdfDictionary)PdfReader.getPdfObject(result.get(target), result)) == null) {
                    res = new PdfDictionary();
                }
                res.mergeDifferent(dic);
                result.put(target, res);
                if (writer != null)
                    writer.markUsed(res);
            }
        }
    }

    static void mergeResources(PdfDictionary result, PdfDictionary source) {
        mergeResources(result, source, null);
    }

    @Override
    public void setUsed() {
        used = true;
        if (parent != null)
            put(PdfName.PARENT, parent.getIndirectReference());
        if (kids != null) {
            PdfArray array = new PdfArray();
            for (int k = 0; k < kids.size(); ++k)
                array.add((kids.get(k)).getIndirectReference());
            put(PdfName.KIDS, array);
        }
        if (templates == null)
            return;
        PdfDictionary dic = new PdfDictionary();
        for (PdfTemplate template: templates) {
            mergeResources(dic, (PdfDictionary)template.getResources());
        }
        put(PdfName.DR, dic);
    }

    public static PdfAnnotation shallowDuplicate(PdfAnnotation annot) {
        PdfAnnotation dup;
        if (annot.isForm()) {
            dup = new PdfFormField(annot.writer);
            PdfFormField dupField = (PdfFormField)dup;
            PdfFormField srcField = (PdfFormField)annot;
            dupField.parent = srcField.parent;
            dupField.kids = srcField.kids;
        }
        else
            dup = new PdfAnnotation(annot.writer, null);
        dup.merge(annot);
        dup.form = annot.form;
        dup.annotation = annot.annotation;
        dup.templates = annot.templates;
        return dup;
    }
}
