/*
 * Copyright 2003-2005 by Paulo Soares.
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

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Node;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;

/** Query and change fields in existing documents either by method
 * calls or by FDF merging.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class AcroFields {

    PdfReader reader;
    PdfWriter writer;
    HashMap fields;
    private int topFirst;
    private HashMap sigNames;
    private boolean append;
    public static final int DA_FONT = 0;
    public static final int DA_SIZE = 1;
    public static final int DA_COLOR = 2;
    private HashMap extensionFonts = new HashMap();
    private XfaForm xfa;
    /**
     * A field type invalid or not found.
     */    
    public static final int FIELD_TYPE_NONE = 0;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_PUSHBUTTON = 1;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_CHECKBOX = 2;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_RADIOBUTTON = 3;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_TEXT = 4;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_LIST = 5;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_COMBO = 6;
    /**
     * A field type.
     */    
    public static final int FIELD_TYPE_SIGNATURE = 7;
    
    private boolean lastWasString;
    
    /** Holds value of property generateAppearances. */
    private boolean generateAppearances = true;
    
    private HashMap localFonts = new HashMap();
    
    private float extraMarginLeft;
    private float extraMarginTop;
    private ArrayList substitutionFonts;
    
    AcroFields(PdfReader reader, PdfWriter writer) {
        this.reader = reader;
        this.writer = writer;
        try {
            xfa = new XfaForm(reader);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        if (writer instanceof PdfStamperImp) {
            append = ((PdfStamperImp)writer).isAppend();
        }
        fill();
    }

    void fill() {
        fields = new HashMap();
        PdfDictionary top = (PdfDictionary)PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.ACROFORM));
        if (top == null)
            return;
        PdfArray arrfds = (PdfArray)PdfReader.getPdfObjectRelease(top.get(PdfName.FIELDS));
        if (arrfds == null || arrfds.size() == 0)
            return;
        arrfds = null;
        for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
            PdfDictionary page = reader.getPageNRelease(k);
            PdfArray annots = (PdfArray)PdfReader.getPdfObjectRelease(page.get(PdfName.ANNOTS), page);
            if (annots == null)
                continue;
            ArrayList arr = annots.getArrayList();
            for (int j = 0; j < arr.size(); ++j) {
                PdfObject annoto = PdfReader.getPdfObject((PdfObject)arr.get(j), annots);
                if (!(annoto instanceof PdfDictionary)) {
                    PdfReader.releaseLastXrefPartial((PdfObject)arr.get(j));
                    continue;
                }
                PdfDictionary annot = (PdfDictionary)annoto;
                if (!PdfName.WIDGET.equals(annot.get(PdfName.SUBTYPE))) {
                    PdfReader.releaseLastXrefPartial((PdfObject)arr.get(j));
                    continue;
                }
                PdfDictionary widget = annot;
                PdfDictionary dic = new PdfDictionary();
                dic.putAll(annot);
                String name = "";
                PdfDictionary value = null;
                PdfObject lastV = null;
                while (annot != null) {
                    dic.mergeDifferent(annot);
                    PdfString t = (PdfString)PdfReader.getPdfObject(annot.get(PdfName.T));
                    if (t != null)
                        name = t.toUnicodeString() + "." + name;
                    if (lastV == null && annot.get(PdfName.V) != null)
                        lastV = PdfReader.getPdfObjectRelease(annot.get(PdfName.V));
                    if (value == null &&  t != null) {
                        value = annot;
                        if (annot.get(PdfName.V) == null && lastV  != null)
                            value.put(PdfName.V, lastV);
                    }
                    annot = (PdfDictionary)PdfReader.getPdfObject(annot.get(PdfName.PARENT), annot);
                }
                if (name.length() > 0)
                    name = name.substring(0, name.length() - 1);
                Item item = (Item)fields.get(name);
                if (item == null) {
                    item = new Item();
                    fields.put(name, item);
                }
                if (value == null)
                    item.values.add(widget);
                else
                    item.values.add(value);
                item.widgets.add(widget);
                item.widget_refs.add(arr.get(j)); // must be a reference
                if (top != null)
                    dic.mergeDifferent(top);
                item.merged.add(dic);
                item.page.add(new Integer(k));
                item.tabOrder.add(new Integer(j));
            }
        }
    }
    
    /** Gets the list of appearance names. Use it to get the names allowed
     * with radio and checkbox fields. If the /Opt key exists the values will
     * also be included. The name 'Off' may also be valid
     * even if not returned in the list.
     * @param fieldName the fully qualified field name
     * @return the list of names or <CODE>null</CODE> if the field does not exist
     */    
    public String[] getAppearanceStates(String fieldName) {
        Item fd = (Item)fields.get(fieldName);
        if (fd == null)
            return null;
        HashMap names = new HashMap();
        PdfDictionary vals = (PdfDictionary)fd.values.get(0);
        PdfObject opts = PdfReader.getPdfObject(vals.get(PdfName.OPT));
        if (opts != null) {
            if (opts.isString())
                names.put(((PdfString)opts).toUnicodeString(), null);
            else if (opts.isArray()) {
                ArrayList list = ((PdfArray)opts).getArrayList();
                for (int k = 0; k < list.size(); ++k) {
                    PdfObject v = PdfReader.getPdfObject((PdfObject)list.get(k));
                    if (v != null && v.isString())
                        names.put(((PdfString)v).toUnicodeString(), null);
                }
            }
        }
        ArrayList wd = fd.widgets;
        for (int k = 0; k < wd.size(); ++k) {
            PdfDictionary dic = (PdfDictionary)wd.get(k);
            dic = (PdfDictionary)PdfReader.getPdfObject(dic.get(PdfName.AP));
            if (dic == null)
                continue;
            PdfObject ob = PdfReader.getPdfObject(dic.get(PdfName.N));
            if (ob == null || !ob.isDictionary())
                continue;
            dic = (PdfDictionary)ob;
            for (Iterator it = dic.getKeys().iterator(); it.hasNext();) {
                String name = PdfName.decodeName(((PdfName)it.next()).toString());
                names.put(name, null);
            }
        }
        String out[] = new String[names.size()];
        return (String[])names.keySet().toArray(out);
    }
    
    private String[] getListOption(String fieldName, int idx) {
        Item fd = getFieldItem(fieldName);
        if (fd == null)
            return null;
        PdfObject obj = PdfReader.getPdfObject(((PdfDictionary)fd.merged.get(0)).get(PdfName.OPT));
        if (obj == null || !obj.isArray())
            return null;
        PdfArray ar = (PdfArray)obj;
        String[] ret = new String[ar.size()];
        ArrayList a = ar.getArrayList();
        for (int k = 0; k < a.size(); ++k) {
            obj = PdfReader.getPdfObject((PdfObject)a.get(k));
            try {
                if (obj.isArray()) {
                    obj = (PdfObject)((PdfArray)obj).getArrayList().get(idx);
                }
                if (obj.isString())
                    ret[k] = ((PdfString)obj).toUnicodeString();
                else
                    ret[k] = obj.toString();
            }
            catch (Exception e) {
                ret[k] = "";
            }
        }
        return ret;
    }
    
    /**
     * Gets the list of export option values from fields of type list or combo.
     * If the field doesn't exist or the field type is not list or combo it will return
     * <CODE>null</CODE>.
     * @param fieldName the field name
     * @return the list of export option values from fields of type list or combo
     */    
    public String[] getListOptionExport(String fieldName) {
        return getListOption(fieldName, 0);
    }
    
    /**
     * Gets the list of display option values from fields of type list or combo.
     * If the field doesn't exist or the field type is not list or combo it will return
     * <CODE>null</CODE>.
     * @param fieldName the field name
     * @return the list of export option values from fields of type list or combo
     */    
    public String[] getListOptionDisplay(String fieldName) {
        return getListOption(fieldName, 1);
    }
    
    /**
     * Sets the option list for fields of type list or combo. One of <CODE>exportValues</CODE>
     * or <CODE>displayValues</CODE> may be <CODE>null</CODE> but not both. This method will only
     * set the list but will not set the value or appearance. For that, calling <CODE>setField()</CODE>
     * is required.
     * <p>
     * An example:
     * <p>
     * <PRE>
     * PdfReader pdf = new PdfReader("input.pdf");
     * PdfStamper stp = new PdfStamper(pdf, new FileOutputStream("output.pdf"));
     * AcroFields af = stp.getAcroFields();
     * af.setListOption("ComboBox", new String[]{"a", "b", "c"}, new String[]{"first", "second", "third"});
     * af.setField("ComboBox", "b");
     * stp.close();
     * </PRE>
     * @param fieldName the field name
     * @param exportValues the export values
     * @param displayValues the display values
     * @return <CODE>true</CODE> if the operation succeeded, <CODE>false</CODE> otherwise
     */    
    public boolean setListOption(String fieldName, String[] exportValues, String[] displayValues) {
        if (exportValues == null && displayValues == null)
            return false;
        if (exportValues != null && displayValues != null && exportValues.length != displayValues.length)
            throw new IllegalArgumentException("The export and the display array must have the same size.");
        int ftype = getFieldType(fieldName);
        if (ftype != FIELD_TYPE_COMBO && ftype != FIELD_TYPE_LIST)
            return false;
        Item fd = (Item)fields.get(fieldName);
        String[] sing = null;
        if (exportValues == null && displayValues != null)
            sing = displayValues;
        else if (exportValues != null && displayValues == null)
            sing = exportValues;
        PdfArray opt = new PdfArray();
        if (sing != null) {
            for (int k = 0; k < sing.length; ++k)
                opt.add(new PdfString(sing[k], PdfObject.TEXT_UNICODE));
        }
        else {
            for (int k = 0; k < exportValues.length; ++k) {
                PdfArray a = new PdfArray();
                a.add(new PdfString(exportValues[k], PdfObject.TEXT_UNICODE));
                a.add(new PdfString(displayValues[k], PdfObject.TEXT_UNICODE));
                opt.add(a);
            }
        }
        ((PdfDictionary)fd.values.get(0)).put(PdfName.OPT, opt);
        for (int j = 0; j < fd.merged.size(); ++j)
            ((PdfDictionary)fd.merged.get(j)).put(PdfName.OPT, opt);
        return true;
    }
    
    /**
     * Gets the field type. The type can be one of: <CODE>FIELD_TYPE_PUSHBUTTON</CODE>,
     * <CODE>FIELD_TYPE_CHECKBOX</CODE>, <CODE>FIELD_TYPE_RADIOBUTTON</CODE>,
     * <CODE>FIELD_TYPE_TEXT</CODE>, <CODE>FIELD_TYPE_LIST</CODE>,
     * <CODE>FIELD_TYPE_COMBO</CODE> or <CODE>FIELD_TYPE_SIGNATURE</CODE>.
     * <p>
     * If the field does not exist or is invalid it returns
     * <CODE>FIELD_TYPE_NONE</CODE>.
     * @param fieldName the field name
     * @return the field type
     */    
    public int getFieldType(String fieldName) {
        Item fd = getFieldItem(fieldName);
        if (fd == null)
            return FIELD_TYPE_NONE;
        PdfObject type = PdfReader.getPdfObject(((PdfDictionary)fd.merged.get(0)).get(PdfName.FT));
        if (type == null)
            return FIELD_TYPE_NONE;
        int ff = 0;
        PdfObject ffo = PdfReader.getPdfObject(((PdfDictionary)fd.merged.get(0)).get(PdfName.FF));
        if (ffo != null && ffo.type() == PdfObject.NUMBER)
            ff = ((PdfNumber)ffo).intValue();
        if (PdfName.BTN.equals(type)) {
            if ((ff & PdfFormField.FF_PUSHBUTTON) != 0)
                return FIELD_TYPE_PUSHBUTTON;
            if ((ff & PdfFormField.FF_RADIO) != 0)
                return FIELD_TYPE_RADIOBUTTON;
            else
                return FIELD_TYPE_CHECKBOX;
        }
        else if (PdfName.TX.equals(type)) {
            return FIELD_TYPE_TEXT;
        }
        else if (PdfName.CH.equals(type)) {
            if ((ff & PdfFormField.FF_COMBO) != 0)
                return FIELD_TYPE_COMBO;
            else
                return FIELD_TYPE_LIST;
        }
        else if (PdfName.SIG.equals(type)) {
            return FIELD_TYPE_SIGNATURE;
        }
        return FIELD_TYPE_NONE;
    }
    
    /**
     * Export the fields as a FDF.
     * @param writer the FDF writer
     */    
    public void exportAsFdf(FdfWriter writer) {
        for (Iterator it = fields.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            Item item = (Item)entry.getValue();
            String name = (String)entry.getKey();
            PdfObject v = PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.V));
            if (v == null)
                continue;
            String value = getField(name);
            if (lastWasString)
                writer.setFieldAsString(name, value);
            else
                writer.setFieldAsName(name, value);
        }
    }
    
    /**
     * Renames a field. Only the last part of the name can be renamed. For example,
     * if the original field is "ab.cd.ef" only the "ef" part can be renamed.
     * @param oldName the old field name
     * @param newName the new field name
     * @return <CODE>true</CODE> if the renaming was successful, <CODE>false</CODE>
     * otherwise
     */    
    public boolean renameField(String oldName, String newName) {
        int idx1 = oldName.lastIndexOf('.') + 1;
        int idx2 = newName.lastIndexOf('.') + 1;
        if (idx1 != idx2)
            return false;
        if (!oldName.substring(0, idx1).equals(newName.substring(0, idx2)))
            return false;
        if (fields.containsKey(newName))
            return false;
        Item item = (Item)fields.get(oldName);
        if (item == null)
            return false;
        newName = newName.substring(idx2);
        PdfString ss = new PdfString(newName, PdfObject.TEXT_UNICODE);
        for (int k = 0; k < item.merged.size(); ++k) {
            PdfDictionary dic = (PdfDictionary)item.values.get(k);
            dic.put(PdfName.T, ss);
            markUsed(dic);
            dic = (PdfDictionary)item.merged.get(k);
            dic.put(PdfName.T, ss);
        }
        fields.remove(oldName);
        fields.put(newName, item);
        return true;
    }
    
    public static Object[] splitDAelements(String da) {
        try {
            PRTokeniser tk = new PRTokeniser(PdfEncodings.convertToBytes(da, null));
            ArrayList stack = new ArrayList();
            Object ret[] = new Object[3];
            while (tk.nextToken()) {
                if (tk.getTokenType() == PRTokeniser.TK_COMMENT)
                    continue;
                if (tk.getTokenType() == PRTokeniser.TK_OTHER) {
                    String operator = tk.getStringValue();
                    if (operator.equals("Tf")) {
                        if (stack.size() >= 2) {
                            ret[DA_FONT] = stack.get(stack.size() - 2);
                            ret[DA_SIZE] = new Float((String)stack.get(stack.size() - 1));
                        }
                    }
                    else if (operator.equals("g")) {
                        if (stack.size() >= 1) {
                            float gray = new Float((String)stack.get(stack.size() - 1)).floatValue();
                            if (gray != 0)
                                ret[DA_COLOR] = new GrayColor(gray);
                        }
                    }
                    else if (operator.equals("rg")) {
                        if (stack.size() >= 3) {
                            float red = new Float((String)stack.get(stack.size() - 3)).floatValue();
                            float green = new Float((String)stack.get(stack.size() - 2)).floatValue();
                            float blue = new Float((String)stack.get(stack.size() - 1)).floatValue();
                            ret[DA_COLOR] = new Color(red, green, blue);
                        }
                    }
                    else if (operator.equals("k")) {
                        if (stack.size() >= 4) {
                            float cyan = new Float((String)stack.get(stack.size() - 4)).floatValue();
                            float magenta = new Float((String)stack.get(stack.size() - 3)).floatValue();
                            float yellow = new Float((String)stack.get(stack.size() - 2)).floatValue();
                            float black = new Float((String)stack.get(stack.size() - 1)).floatValue();
                            ret[DA_COLOR] = new CMYKColor(cyan, magenta, yellow, black);
                        }
                    }
                    stack.clear();
                }
                else
                    stack.add(tk.getStringValue());
            }
            return ret;
        }
        catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    public void decodeGenericDictionary(PdfDictionary merged, BaseField tx) throws IOException, DocumentException {
        int flags = 0;
        // the text size and color
        PdfString da = (PdfString)PdfReader.getPdfObject(merged.get(PdfName.DA));
        if (da != null) {
            Object dab[] = splitDAelements(da.toUnicodeString());
            if (dab[DA_SIZE] != null)
                tx.setFontSize(((Float)dab[DA_SIZE]).floatValue());
            if (dab[DA_COLOR] != null)
                tx.setTextColor((Color)dab[DA_COLOR]);
            if (dab[DA_FONT] != null) {
                PdfDictionary font = (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.DR));
                if (font != null) {
                    font = (PdfDictionary)PdfReader.getPdfObject(font.get(PdfName.FONT));
                    if (font != null) {
                        PdfObject po = font.get(new PdfName((String)dab[DA_FONT]));
                        if (po != null && po.type() == PdfObject.INDIRECT) {
                            PRIndirectReference por = (PRIndirectReference)po;
                            BaseFont bp = new DocumentFont((PRIndirectReference)po);
                            tx.setFont(bp);
                            Integer porkey = new Integer(por.getNumber());
                            BaseFont porf = (BaseFont)extensionFonts.get(porkey);
                            if (porf == null) {
                                if (!extensionFonts.containsKey(porkey)) {
                                    PdfDictionary fo = (PdfDictionary)PdfReader.getPdfObject(po);
                                    PdfDictionary fd = (PdfDictionary)PdfReader.getPdfObject(fo.get(PdfName.FONTDESCRIPTOR));
                                    if (fd != null) {
                                        PRStream prs = (PRStream)PdfReader.getPdfObject(fd.get(PdfName.FONTFILE2));
                                        if (prs == null)
                                            prs = (PRStream)PdfReader.getPdfObject(fd.get(PdfName.FONTFILE3));
                                        if (prs == null) {
                                            extensionFonts.put(porkey, null);
                                        }
                                        else {
                                            try {
                                                porf = BaseFont.createFont("font.ttf", BaseFont.IDENTITY_H, true, false, PdfReader.getStreamBytes(prs), null);
                                            }
                                            catch (Exception e) {
                                            }
                                            extensionFonts.put(porkey, porf);
                                        }
                                    }
                                }
                            }
                            if (tx instanceof TextField)
                                ((TextField)tx).setExtensionFont(porf);
                        }
                        else {
                            BaseFont bf = (BaseFont)localFonts.get(dab[DA_FONT]);
                            if (bf == null) {
                                String fn[] = (String[])stdFieldFontNames.get(dab[DA_FONT]);
                                if (fn != null) {
                                    try {
                                        String enc = "winansi";
                                        if (fn.length > 1)
                                            enc = fn[1];
                                        bf = BaseFont.createFont(fn[0], enc, false);
                                        tx.setFont(bf);
                                    }
                                    catch (Exception e) {
                                        // empty
                                    }
                                }
                            }
                            else
                                tx.setFont(bf);
                        }
                    }
                }
            }
        }
        //rotation, border and backgound color
        PdfDictionary mk = (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.MK));
        if (mk != null) {
            PdfArray ar = (PdfArray)PdfReader.getPdfObject(mk.get(PdfName.BC));
            Color border = getMKColor(ar);
            tx.setBorderColor(border);
            if (border != null)
                tx.setBorderWidth(1);
            ar = (PdfArray)PdfReader.getPdfObject(mk.get(PdfName.BG));
            tx.setBackgroundColor(getMKColor(ar));
            PdfNumber rotation = (PdfNumber)PdfReader.getPdfObject(mk.get(PdfName.R));
            if (rotation != null)
                tx.setRotation(rotation.intValue());
        }
        //flags
        PdfNumber nfl = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.F));
        flags = 0;
        if (nfl != null) {
            flags = nfl.intValue();
            if ((flags & PdfFormField.FLAGS_PRINT) != 0 && (flags & PdfFormField.FLAGS_HIDDEN) != 0)
                tx.setVisibility(BaseField.HIDDEN);
            else if ((flags & PdfFormField.FLAGS_PRINT) != 0 && (flags & PdfFormField.FLAGS_NOVIEW) != 0)
                tx.setVisibility(BaseField.HIDDEN_BUT_PRINTABLE);
            else if ((flags & PdfFormField.FLAGS_PRINT) != 0)
                tx.setVisibility(BaseField.VISIBLE);
            else
                tx.setVisibility(BaseField.VISIBLE_BUT_DOES_NOT_PRINT);
        }
        //multiline
        nfl = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.FF));
        flags = 0;
        if (nfl != null)
            flags = nfl.intValue();
        tx.setOptions(flags);
        if ((flags & PdfFormField.FF_COMB) != 0) {
            PdfNumber maxLen = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.MAXLEN));
            int len = 0;
            if (maxLen != null)
                len = maxLen.intValue();
            tx.setMaxCharacterLength(len);
        }
        //alignment
        nfl = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.Q));
        if (nfl != null) {
            if (nfl.intValue() == PdfFormField.Q_CENTER)
                tx.setAlignment(Element.ALIGN_CENTER);
            else if (nfl.intValue() == PdfFormField.Q_RIGHT)
                tx.setAlignment(Element.ALIGN_RIGHT);
        }
        //border styles
        PdfDictionary bs = (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.BS));
        if (bs != null) {
            PdfNumber w = (PdfNumber)PdfReader.getPdfObject(bs.get(PdfName.W));
            if (w != null)
                tx.setBorderWidth(w.floatValue());
            PdfName s = (PdfName)PdfReader.getPdfObject(bs.get(PdfName.S));
            if (PdfName.D.equals(s))
                tx.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
            else if (PdfName.B.equals(s))
                tx.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
            else if (PdfName.I.equals(s))
                tx.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
            else if (PdfName.U.equals(s))
                tx.setBorderStyle(PdfBorderDictionary.STYLE_UNDERLINE);
        }
        else {
            PdfArray bd = (PdfArray)PdfReader.getPdfObject(merged.get(PdfName.BORDER));
            if (bd != null) {
                ArrayList ar = bd.getArrayList();
                if (ar.size() >= 3)
                    tx.setBorderWidth(((PdfNumber)ar.get(2)).floatValue());
                if (ar.size() >= 4)
                    tx.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
            }
        }
    }
    
    PdfAppearance getAppearance(PdfDictionary merged, String text, String fieldName) throws IOException, DocumentException {
        topFirst = 0;
        TextField tx = null;
        if (fieldCache == null || !fieldCache.containsKey(fieldName)) {
            tx = new TextField(writer, null, null);
            tx.setExtraMargin(extraMarginLeft, extraMarginTop);
            tx.setBorderWidth(0);
            tx.setSubstitutionFonts(substitutionFonts);
            decodeGenericDictionary(merged, tx);
            //rect
            PdfArray rect = (PdfArray)PdfReader.getPdfObject(merged.get(PdfName.RECT));
            Rectangle box = PdfReader.getNormalizedRectangle(rect);
            if (tx.getRotation() == 90 || tx.getRotation() == 270)
                box = box.rotate();
            tx.setBox(box);
            if (fieldCache != null)
                fieldCache.put(fieldName, tx);
        }
        else {
            tx = (TextField)fieldCache.get(fieldName);
            tx.setWriter(writer);
        }
        PdfName fieldType = (PdfName)PdfReader.getPdfObject(merged.get(PdfName.FT));
        if (PdfName.TX.equals(fieldType)) {
            tx.setText(text);
            return tx.getAppearance();
        }
        if (!PdfName.CH.equals(fieldType))
            throw new DocumentException("An appearance was requested without a variable text field.");
        PdfArray opt = (PdfArray)PdfReader.getPdfObject(merged.get(PdfName.OPT));
        int flags = 0;
        PdfNumber nfl = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.FF));
        if (nfl != null)
            flags = nfl.intValue();
        if ((flags & PdfFormField.FF_COMBO) != 0 && opt == null) {
            tx.setText(text);
            return tx.getAppearance();
        }
        if (opt != null) {
            ArrayList op = opt.getArrayList();
            String choices[] = new String[op.size()];
            String choicesExp[] = new String[op.size()];
            for (int k = 0; k < op.size(); ++k) {
                PdfObject obj = (PdfObject)op.get(k);
                if (obj.isString()) {
                    choices[k] = choicesExp[k] = ((PdfString)obj).toUnicodeString();
                }
                else {
                    ArrayList opar = ((PdfArray)obj).getArrayList();
                    choicesExp[k] = ((PdfString)opar.get(0)).toUnicodeString();
                    choices[k] = ((PdfString)opar.get(1)).toUnicodeString();
                }
            }
            if ((flags & PdfFormField.FF_COMBO) != 0) {
                for (int k = 0; k < choices.length; ++k) {
                    if (text.equals(choicesExp[k])) {
                        text = choices[k];
                        break;
                    }
                }
                tx.setText(text);
                return tx.getAppearance();
            }
            int idx = 0;
            for (int k = 0; k < choicesExp.length; ++k) {
                if (text.equals(choicesExp[k])) {
                    idx = k;
                    break;
                }
            }
            tx.setChoices(choices);
            tx.setChoiceExports(choicesExp);
            tx.setChoiceSelection(idx);
        }
        PdfAppearance app = tx.getListAppearance();
        topFirst = tx.getTopFirst();
        return app;
    }
    
    Color getMKColor(PdfArray ar) {
        if (ar == null)
            return null;
        ArrayList cc = ar.getArrayList();
        switch (cc.size()) {
            case 1:
                return new GrayColor(((PdfNumber)cc.get(0)).floatValue());
            case 3:
                return new Color(ExtendedColor.normalize(((PdfNumber)cc.get(0)).floatValue()), ExtendedColor.normalize(((PdfNumber)cc.get(1)).floatValue()), ExtendedColor.normalize(((PdfNumber)cc.get(2)).floatValue()));
            case 4:
                return new CMYKColor(((PdfNumber)cc.get(0)).floatValue(), ((PdfNumber)cc.get(1)).floatValue(), ((PdfNumber)cc.get(2)).floatValue(), ((PdfNumber)cc.get(3)).floatValue());
            default:
                return null;
        }
    }
    
    /** Gets the field value.
     * @param name the fully qualified field name
     * @return the field value
     */    
    public String getField(String name) {
        if (xfa.isXfaPresent()) {
            name = xfa.findFieldName(name, this);
            if (name == null)
                return null;
            name = XfaForm.Xml2Som.getShortName(name);
            return XfaForm.getNodeText(xfa.findDatasetsNode(name));
        }
        Item item = (Item)fields.get(name);
        if (item == null)
            return null;
        lastWasString = false;
        PdfObject v = PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.V));
        if (v == null)
            return "";
        PdfName type = (PdfName)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.FT));
        if (PdfName.BTN.equals(type)) {
            PdfNumber ff = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.FF));
            int flags = 0;
            if (ff != null)
                flags = ff.intValue();
            if ((flags & PdfFormField.FF_PUSHBUTTON) != 0)
                return "";
            String value = "";
            if (v.isName())
                value = PdfName.decodeName(v.toString());
            else if (v.isString())
                value = ((PdfString)v).toUnicodeString();
            PdfObject opts = PdfReader.getPdfObject(((PdfDictionary)item.values.get(0)).get(PdfName.OPT));
            if (opts != null && opts.isArray()) {
                ArrayList list = ((PdfArray)opts).getArrayList();
                int idx = 0;
                try {
                    idx = Integer.parseInt(value);
                    PdfString ps = (PdfString)list.get(idx);
                    value = ps.toUnicodeString();
                    lastWasString = true;
                }
                catch (Exception e) {
                }
            }
            return value;
        }
        if (v.isString()) {
            lastWasString = true;
            return ((PdfString)v).toUnicodeString();
        }
        return PdfName.decodeName(v.toString());
    }

    /**
     * Sets a field property. Valid property names are:
     * <p>
     * <ul>
     * <li>textfont - sets the text font. The value for this entry is a <CODE>BaseFont</CODE>.<br>
     * <li>textcolor - sets the text color. The value for this entry is a <CODE>java.awt.Color</CODE>.<br>
     * <li>textsize - sets the text size. The value for this entry is a <CODE>Float</CODE>.
     * <li>bgcolor - sets the background color. The value for this entry is a <CODE>java.awt.Color</CODE>.
     *     If <code>null</code> removes the background.<br>
     * <li>bordercolor - sets the border color. The value for this entry is a <CODE>java.awt.Color</CODE>.
     *     If <code>null</code> removes the border.<br>
     * </ul>
     * @param field the field name
     * @param name the property name
     * @param value the property value
     * @param inst an array of <CODE>int</CODE> indexing into <CODE>AcroField.Item.merged</CODE> elements to process.
     * Set to <CODE>null</CODE> to process all
     * @return <CODE>true</CODE> if the property exists, <CODE>false</CODE> otherwise
     */    
    public boolean setFieldProperty(String field, String name, Object value, int inst[]) {
        if (writer == null)
            throw new RuntimeException("This AcroFields instance is read-only.");
        try {
            Item item = (Item)fields.get(field);
            if (item == null)
                return false;
            InstHit hit = new InstHit(inst);
            if (name.equalsIgnoreCase("textfont")) {
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfString da = (PdfString)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(k)).get(PdfName.DA));
                        PdfDictionary dr = (PdfDictionary)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(k)).get(PdfName.DR));
                        if (da != null && dr != null) {
                            Object dao[] = splitDAelements(da.toUnicodeString());
                            PdfAppearance cb = new PdfAppearance();
                            if (dao[DA_FONT] != null) {
                                BaseFont bf = (BaseFont)value;
                                PdfName psn = (PdfName)PdfAppearance.stdFieldFontNames.get(bf.getPostscriptFontName());
                                if (psn == null) {
                                    psn = new PdfName(bf.getPostscriptFontName());
                                }
                                PdfDictionary fonts = (PdfDictionary)PdfReader.getPdfObject(dr.get(PdfName.FONT));
                                if (fonts == null) {
                                    fonts = new PdfDictionary();
                                    dr.put(PdfName.FONT, fonts);
                                }
                                PdfIndirectReference fref = (PdfIndirectReference)fonts.get(psn);
                                PdfDictionary top = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM));
                                markUsed(top);
                                dr = (PdfDictionary)PdfReader.getPdfObject(top.get(PdfName.DR));
                                if (dr == null) {
                                    dr = new PdfDictionary();
                                    top.put(PdfName.DR, dr);
                                }
                                markUsed(dr);
                                PdfDictionary fontsTop = (PdfDictionary)PdfReader.getPdfObject(dr.get(PdfName.FONT));
                                if (fontsTop == null) {
                                    fontsTop = new PdfDictionary();
                                    dr.put(PdfName.FONT, fontsTop);
                                }
                                markUsed(fontsTop);
                                PdfIndirectReference frefTop = (PdfIndirectReference)fontsTop.get(psn);
                                if (frefTop != null) {
                                    if (fref == null)
                                        fonts.put(psn, frefTop);
                                }
                                else if (fref == null) {
                                    FontDetails fd;
                                    if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
                                        fd = new FontDetails(null, ((DocumentFont)bf).getIndirectReference(), bf);
                                    }
                                    else {
                                        bf.setSubset(false);
                                        fd = writer.addSimple(bf);
                                        localFonts.put(psn.toString().substring(1), bf);
                                    }
                                    fontsTop.put(psn, fd.getIndirectReference());
                                    fonts.put(psn, fd.getIndirectReference());
                                }
                                ByteBuffer buf = cb.getInternalBuffer();
                                buf.append(psn.getBytes()).append(' ').append(((Float)dao[DA_SIZE]).floatValue()).append(" Tf ");
                                if (dao[DA_COLOR] != null)
                                    cb.setColorFill((Color)dao[DA_COLOR]);
                                PdfString s = new PdfString(cb.toString());
                                ((PdfDictionary)item.merged.get(k)).put(PdfName.DA, s);
                                ((PdfDictionary)item.widgets.get(k)).put(PdfName.DA, s);
                                markUsed((PdfDictionary)item.widgets.get(k));
                            }
                        }
                    }
                }
            }
            else if (name.equalsIgnoreCase("textcolor")) {
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfString da = (PdfString)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(k)).get(PdfName.DA));
                        if (da != null) {
                            Object dao[] = splitDAelements(da.toUnicodeString());
                            PdfAppearance cb = new PdfAppearance();
                            if (dao[DA_FONT] != null) {
                                ByteBuffer buf = cb.getInternalBuffer();
                                buf.append(new PdfName((String)dao[DA_FONT]).getBytes()).append(' ').append(((Float)dao[DA_SIZE]).floatValue()).append(" Tf ");
                                cb.setColorFill((Color)value);
                                PdfString s = new PdfString(cb.toString());
                                ((PdfDictionary)item.merged.get(k)).put(PdfName.DA, s);
                                ((PdfDictionary)item.widgets.get(k)).put(PdfName.DA, s);
                                markUsed((PdfDictionary)item.widgets.get(k));
                            }
                        }
                    }
                }
            }
            else if (name.equalsIgnoreCase("textsize")) {
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfString da = (PdfString)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(k)).get(PdfName.DA));
                        if (da != null) {
                            Object dao[] = splitDAelements(da.toUnicodeString());
                            PdfAppearance cb = new PdfAppearance();
                            if (dao[DA_FONT] != null) {
                                ByteBuffer buf = cb.getInternalBuffer();
                                buf.append(new PdfName((String)dao[DA_FONT]).getBytes()).append(' ').append(((Float)value).floatValue()).append(" Tf ");
                                if (dao[DA_COLOR] != null)
                                    cb.setColorFill((Color)dao[DA_COLOR]);
                                PdfString s = new PdfString(cb.toString());
                                ((PdfDictionary)item.merged.get(k)).put(PdfName.DA, s);
                                ((PdfDictionary)item.widgets.get(k)).put(PdfName.DA, s);
                                markUsed((PdfDictionary)item.widgets.get(k));
                            }
                        }
                    }
                }
            }
            else if (name.equalsIgnoreCase("bgcolor") || name.equalsIgnoreCase("bordercolor")) {
                PdfName dname = (name.equalsIgnoreCase("bgcolor") ? PdfName.BG : PdfName.BC);
                for (int k = 0; k < item.merged.size(); ++k) {
                    if (hit.isHit(k)) {
                        PdfObject obj = PdfReader.getPdfObject(((PdfDictionary)item.merged.get(k)).get(PdfName.MK));
                        markUsed(obj);
                        PdfDictionary mk = (PdfDictionary)obj;
                        if (mk == null) {
                            if (value == null)
                                return true;
                            mk = new PdfDictionary();
                            ((PdfDictionary)item.merged.get(k)).put(PdfName.MK, mk);
                            ((PdfDictionary)item.widgets.get(k)).put(PdfName.MK, mk);
                            markUsed((PdfDictionary)item.widgets.get(k));
                        }
                        if (value == null)
                            mk.remove(dname);
                        else
                            mk.put(dname, PdfFormField.getMKColor((Color)value));
                    }
                }
            }
            else
                return false;
            return true;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    /**
     * Sets a field property. Valid property names are:
     * <p>
     * <ul>
     * <li>flags - a set of flags specifying various characteristics of the field's widget annotation.
     * The value of this entry replaces that of the F entry in the form's corresponding annotation dictionary.<br>
     * <li>setflags - a set of flags to be set (turned on) in the F entry of the form's corresponding
     * widget annotation dictionary. Bits equal to 1 cause the corresponding bits in F to be set to 1.<br>
     * <li>clrflags - a set of flags to be cleared (turned off) in the F entry of the form's corresponding
     * widget annotation dictionary. Bits equal to 1 cause the corresponding
     * bits in F to be set to 0.<br>
     * <li>fflags - a set of flags specifying various characteristics of the field. The value
     * of this entry replaces that of the Ff entry in the form's corresponding field dictionary.<br>
     * <li>setfflags - a set of flags to be set (turned on) in the Ff entry of the form's corresponding
     * field dictionary. Bits equal to 1 cause the corresponding bits in Ff to be set to 1.<br>
     * <li>clrfflags - a set of flags to be cleared (turned off) in the Ff entry of the form's corresponding
     * field dictionary. Bits equal to 1 cause the corresponding bits in Ff
     * to be set to 0.<br>
     * </ul>
     * @param field the field name
     * @param name the property name
     * @param value the property value
     * @param inst an array of <CODE>int</CODE> indexing into <CODE>AcroField.Item.merged</CODE> elements to process.
     * Set to <CODE>null</CODE> to process all
     * @return <CODE>true</CODE> if the property exists, <CODE>false</CODE> otherwise
     */    
    public boolean setFieldProperty(String field, String name, int value, int inst[]) {
        if (writer == null)
            throw new RuntimeException("This AcroFields instance is read-only.");
        Item item = (Item)fields.get(field);
        if (item == null)
            return false;
        InstHit hit = new InstHit(inst);
        if (name.equalsIgnoreCase("flags")) {
            PdfNumber num = new PdfNumber(value);
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.F, num);
                    ((PdfDictionary)item.widgets.get(k)).put(PdfName.F, num);
                    markUsed((PdfDictionary)item.widgets.get(k));
                }
            }
        }
        else if (name.equalsIgnoreCase("setflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.widgets.get(k)).get(PdfName.F));
                    int val = 0;
                    if (num != null)
                        val = num.intValue();
                    num = new PdfNumber(val | value);
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.F, num);
                    ((PdfDictionary)item.widgets.get(k)).put(PdfName.F, num);
                    markUsed((PdfDictionary)item.widgets.get(k));
                }
            }
        }
        else if (name.equalsIgnoreCase("clrflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.widgets.get(k)).get(PdfName.F));
                    int val = 0;
                    if (num != null)
                        val = num.intValue();
                    num = new PdfNumber(val & (~value));
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.F, num);
                    ((PdfDictionary)item.widgets.get(k)).put(PdfName.F, num);
                    markUsed((PdfDictionary)item.widgets.get(k));
                }
            }
        }
        else if (name.equalsIgnoreCase("fflags")) {
            PdfNumber num = new PdfNumber(value);
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary)item.values.get(k)).put(PdfName.FF, num);
                    markUsed((PdfDictionary)item.values.get(k));
                }
            }
        }
        else if (name.equalsIgnoreCase("setfflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.values.get(k)).get(PdfName.FF));
                    int val = 0;
                    if (num != null)
                        val = num.intValue();
                    num = new PdfNumber(val | value);
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary)item.values.get(k)).put(PdfName.FF, num);
                    markUsed((PdfDictionary)item.values.get(k));
                }
            }
        }
        else if (name.equalsIgnoreCase("clrfflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.values.get(k)).get(PdfName.FF));
                    int val = 0;
                    if (num != null)
                        val = num.intValue();
                    num = new PdfNumber(val & (~value));
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary)item.values.get(k)).put(PdfName.FF, num);
                    markUsed((PdfDictionary)item.values.get(k));
                }
            }
        }
        else
            return false;
        return true;
    }
    
    /**
     * Merges an XML data structure into this form.
     * @param n the top node of the data structure
     * @throws java.io.IOException on error
     * @throws com.lowagie.text.DocumentException o error
     */
    public void mergeXfaData(Node n) throws IOException, DocumentException {
        XfaForm.Xml2SomDatasets data = new XfaForm.Xml2SomDatasets(n);
        for (Iterator it = data.getOrder().iterator(); it.hasNext();) {
            String name = (String)it.next();
            String text = XfaForm.getNodeText((Node)data.getName2Node().get(name));
            setField(name, text);
        }
    }
    
    /** Sets the fields by FDF merging.
     * @param fdf the FDF form
     * @throws IOException on error
     * @throws DocumentException on error
     */    
    public void setFields(FdfReader fdf) throws IOException, DocumentException {
        HashMap fd = fdf.getFields();
        for (Iterator i = fd.keySet().iterator(); i.hasNext();) {
            String f = (String)i.next();
            String v = fdf.getFieldValue(f);
            if (v != null)
                setField(f, v);
        }
    }
    
    /** Sets the fields by XFDF merging.
     * @param xfdf the XFDF form
     * @throws IOException on error
     * @throws DocumentException on error
     */
    
    public void setFields(XfdfReader xfdf) throws IOException, DocumentException {
        HashMap fd = xfdf.getFields();
        for (Iterator i = fd.keySet().iterator(); i.hasNext();) {
            String f = (String)i.next();
            String v = xfdf.getFieldValue(f);
            if (v != null)
                setField(f, v);
        }
    }

    /**
     * Regenerates the field appearance.
     * This is usefull when you change a field property, but not its value,
     * for instance form.setFieldProperty("f", "bgcolor", Color.BLUE, null);
     * This won't have any effect, unless you use regenerateField("f") after changing
     * the property.
     * 
     * @param name the fully qualified field name or the partial name in the case of XFA forms
     * @throws IOException on error
     * @throws DocumentException on error
     * @return <CODE>true</CODE> if the field was found and changed,
     * <CODE>false</CODE> otherwise
     */    
    public boolean regenerateField(String name) throws IOException, DocumentException {
    	String value = getField(name);
        return setField(name, value, value);
    }

    /** Sets the field value.
     * @param name the fully qualified field name or the partial name in the case of XFA forms
     * @param value the field value
     * @throws IOException on error
     * @throws DocumentException on error
     * @return <CODE>true</CODE> if the field was found and changed,
     * <CODE>false</CODE> otherwise
     */    
    public boolean setField(String name, String value) throws IOException, DocumentException {
        return setField(name, value, null);
    }
    
    /** Sets the field value and the display string. The display string
     * is used to build the appearance in the cases where the value
     * is modified by Acrobat with JavaScript and the algorithm is
     * known.
     * @param name the fully qualified field name or the partial name in the case of XFA forms
     * @param value the field value
     * @param display the string that is used for the appearance. If <CODE>null</CODE>
     * the <CODE>value</CODE> parameter will be used
     * @return <CODE>true</CODE> if the field was found and changed,
     * <CODE>false</CODE> otherwise
     * @throws IOException on error
     * @throws DocumentException on error
     */    
    public boolean setField(String name, String value, String display) throws IOException, DocumentException {
        if (writer == null)
            throw new DocumentException("This AcroFields instance is read-only.");
        if (xfa.isXfaPresent()) {
            name = xfa.findFieldName(name, this);
            if (name == null)
                return false;
            String shortName = XfaForm.Xml2Som.getShortName(name);
            Node xn = xfa.findDatasetsNode(shortName);
            if (xn == null) {
                xn = xfa.getDatasetsSom().insertNode(xfa.getDatasetsNode(), shortName);
            }
            xfa.setNodeText(xn, value);
        }
        Item item = (Item)fields.get(name);
        if (item == null)
            return false;
        PdfName type = (PdfName)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.FT));
        if (PdfName.TX.equals(type)) {
            PdfNumber maxLen = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.MAXLEN));
            int len = 0;
            if (maxLen != null)
                len = maxLen.intValue();
            if (len > 0)
                value = value.substring(0, Math.min(len, value.length()));
        }
        if (display == null)
            display = value;
        if (PdfName.TX.equals(type) || PdfName.CH.equals(type)) {
            PdfString v = new PdfString(value, PdfObject.TEXT_UNICODE);
            for (int idx = 0; idx < item.values.size(); ++idx) {
                PdfDictionary valueDic = (PdfDictionary)item.values.get(idx);
                valueDic.put(PdfName.V, v);
                valueDic.remove(PdfName.I);
                markUsed(valueDic);                
                PdfDictionary merged = (PdfDictionary)item.merged.get(idx);
                merged.remove(PdfName.I);
                merged.put(PdfName.V, v);
                PdfDictionary widget = (PdfDictionary)item.widgets.get(idx);
                if (generateAppearances) {
                    PdfAppearance app = getAppearance(merged, display, name);
                    if (PdfName.CH.equals(type)) {
                        PdfNumber n = new PdfNumber(topFirst);
                        widget.put(PdfName.TI, n);
                        merged.put(PdfName.TI, n);
                    }
                    PdfDictionary appDic = (PdfDictionary)PdfReader.getPdfObject(widget.get(PdfName.AP));
                    if (appDic == null) {
                        appDic = new PdfDictionary();
                        widget.put(PdfName.AP, appDic);
                        merged.put(PdfName.AP, appDic);
                    }
                    appDic.put(PdfName.N, app.getIndirectReference());
                    writer.releaseTemplate(app);
                }
                else {
                    widget.remove(PdfName.AP);
                    merged.remove(PdfName.AP);
                }
                markUsed(widget);
            }
            return true;
        }
        else if (PdfName.BTN.equals(type)) {
            PdfNumber ff = (PdfNumber)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(0)).get(PdfName.FF));
            int flags = 0;
            if (ff != null)
                flags = ff.intValue();
            if ((flags & PdfFormField.FF_PUSHBUTTON) != 0)
                return true;
            PdfName v = new PdfName(value);
            if ((flags & PdfFormField.FF_RADIO) == 0) {
                for (int idx = 0; idx < item.values.size(); ++idx) {
                    ((PdfDictionary)item.values.get(idx)).put(PdfName.V, v);
                    markUsed((PdfDictionary)item.values.get(idx));
                    PdfDictionary merged = (PdfDictionary)item.merged.get(idx);
                    merged.put(PdfName.V, v);
                    merged.put(PdfName.AS, v);
                    PdfDictionary widget = (PdfDictionary)item.widgets.get(idx);
                    if (isInAP(widget,  v))
                        widget.put(PdfName.AS, v);
                    else
                        widget.put(PdfName.AS, PdfName.Off);
                    markUsed(widget);
                }
            }
            else {
                ArrayList lopt = new ArrayList();
                PdfObject opts = PdfReader.getPdfObject(((PdfDictionary)item.values.get(0)).get(PdfName.OPT));
                if (opts != null && opts.isArray()) {
                    ArrayList list = ((PdfArray)opts).getArrayList();
                    for (int k = 0; k < list.size(); ++k) {
                        PdfObject vv = PdfReader.getPdfObject((PdfObject)list.get(k));
                        if (vv != null && vv.isString())
                            lopt.add(((PdfString)vv).toUnicodeString());
                        else
                            lopt.add(null);
                    }
                }
                int vidx = lopt.indexOf(value);
                PdfName valt = null;
                PdfName vt;
                if (vidx >= 0) {
                    vt = valt = new PdfName(String.valueOf(vidx));
                }
                else
                    vt = v;
                for (int idx = 0; idx < item.values.size(); ++idx) {
                    PdfDictionary merged = (PdfDictionary)item.merged.get(idx);
                    PdfDictionary widget = (PdfDictionary)item.widgets.get(idx);
                    markUsed((PdfDictionary)item.values.get(idx));
                    if (valt != null) {
                        PdfString ps = new PdfString(value, PdfObject.TEXT_UNICODE);
                        ((PdfDictionary)item.values.get(idx)).put(PdfName.V, ps);
                        merged.put(PdfName.V, ps);
                    }
                    else {
                        ((PdfDictionary)item.values.get(idx)).put(PdfName.V, v);
                        merged.put(PdfName.V, v);
                    }
                    markUsed(widget);
                    if (isInAP(widget,  vt)) {
                        merged.put(PdfName.AS, vt);
                        widget.put(PdfName.AS, vt);
                    }
                    else {
                        merged.put(PdfName.AS, PdfName.Off);
                        widget.put(PdfName.AS, PdfName.Off);
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    boolean isInAP(PdfDictionary dic, PdfName check) {
        PdfDictionary appDic = (PdfDictionary)PdfReader.getPdfObject(dic.get(PdfName.AP));
        if (appDic == null)
            return false;
        PdfDictionary NDic = (PdfDictionary)PdfReader.getPdfObject(appDic.get(PdfName.N));
        return (NDic != null && NDic.get(check) != null);
    }
    
    /** Gets all the fields. The fields are keyed by the fully qualified field name and
     * the value is an instance of <CODE>AcroFields.Item</CODE>.
     * @return all the fields
     */    
    public HashMap getFields() {
        return fields;
    }
    
    /**
     * Gets the field structure.
     * @param name the name of the field
     * @return the field structure or <CODE>null</CODE> if the field
     * does not exist
     */    
    public Item getFieldItem(String name) {
        if (xfa.isXfaPresent()) {
            name = xfa.findFieldName(name, this);
            if (name == null)
                return null;
        }
        return (Item)fields.get(name);
    }
    
    /**
     * Gets the long XFA translated name.
     * @param name the name of the field
     * @return the long field name
     */    
    public String getTranslatedFieldName(String name) {
        if (xfa.isXfaPresent()) {
            String namex = xfa.findFieldName(name, this);
            if (namex != null)
                name = namex;
        }
        return name;
    }
    
    /**
     * Gets the field box positions in the document. The return is an array of <CODE>float</CODE>
     * multiple of 5. For each of this groups the values are: [page, llx, lly, urx,
     * ury]. The coordinates have the page rotation in consideration.
     * @param name the field name
     * @return the positions or <CODE>null</CODE> if field does not exist
     */    
    public float[] getFieldPositions(String name) {
        Item item = getFieldItem(name);
        if (item == null)
            return null;
        float ret[] = new float[item.page.size() * 5];
        int ptr = 0;
        for (int k = 0; k < item.page.size(); ++k) {
            try {
                PdfDictionary wd = (PdfDictionary)item.widgets.get(k);
                PdfArray rect = (PdfArray)wd.get(PdfName.RECT);
                if (rect == null)
                    continue;
                Rectangle r = PdfReader.getNormalizedRectangle(rect);
                int page = ((Integer)item.page.get(k)).intValue();
                int rotation = reader.getPageRotation(page);
                ret[ptr++] = page;
                if (rotation != 0) {
                    Rectangle pageSize = reader.getPageSize(page);
                    switch (rotation) {
                        case 270:
                            r = new Rectangle(
                                pageSize.getTop() - r.getBottom(),
                                r.getLeft(),
                                pageSize.getTop() - r.getTop(),
                                r.getRight());
                            break;
                        case 180:
                            r = new Rectangle(
                                pageSize.getRight() - r.getLeft(),
                                pageSize.getTop() - r.getBottom(),
                                pageSize.getRight() - r.getRight(),
                                pageSize.getTop() - r.getTop());
                            break;
                        case 90:
                            r = new Rectangle(
                                r.getBottom(),
                                pageSize.getRight() - r.getLeft(),
                                r.getTop(),
                                pageSize.getRight() - r.getRight());
                            break;
                    }
                    r.normalize();
                }
                ret[ptr++] = r.getLeft();
                ret[ptr++] = r.getBottom();
                ret[ptr++] = r.getRight();
                ret[ptr++] = r.getTop();
            }
            catch (Exception e) {
                // empty on purpose
            }
        }
        if (ptr < ret.length) {
            float ret2[] = new float[ptr];
            System.arraycopy(ret, 0, ret2, 0, ptr);
            return ret2;
        }
        return ret;
    }
    
    private int removeRefFromArray(PdfArray array, PdfObject refo) {
        ArrayList ar = array.getArrayList();
        if (refo == null || !refo.isIndirect())
            return ar.size();
        PdfIndirectReference ref = (PdfIndirectReference)refo;
        for (int j = 0; j < ar.size(); ++j) {
            PdfObject obj = (PdfObject)ar.get(j);
            if (!obj.isIndirect())
                continue;
            if (((PdfIndirectReference)obj).getNumber() == ref.getNumber())
                ar.remove(j--);
        }
        return ar.size();
    }
    
    /**
     * Removes all the fields from <CODE>page</CODE>.
     * @param page the page to remove the fields from
     * @return <CODE>true</CODE> if any field was removed, <CODE>false otherwise</CODE>
     */    
    public boolean removeFieldsFromPage(int page) {
        if (page < 1)
            return false;
        String names[] = new String[fields.size()];
        fields.keySet().toArray(names);
        boolean found = false;
        for (int k = 0; k < names.length; ++k) {
            boolean fr = removeField(names[k], page);
            found = (found || fr);
        }
        return found;
    }
    
    /**
     * Removes a field from the document. If page equals -1 all the fields with this
     * <CODE>name</CODE> are removed from the document otherwise only the fields in
     * that particular page are removed.
     * @param name the field name
     * @param page the page to remove the field from or -1 to remove it from all the pages
     * @return <CODE>true</CODE> if the field exists, <CODE>false otherwise</CODE>
     */    
    public boolean removeField(String name, int page) {
        Item item = getFieldItem(name);
        if (item == null)
            return false;
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM), reader.getCatalog());
        
        if (acroForm == null)
            return false;
        PdfArray arrayf = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
        if (arrayf == null)
            return false;
        for (int k = 0; k < item.widget_refs.size(); ++k) {
            int pageV = ((Integer)item.page.get(k)).intValue();
            if (page != -1 && page != pageV)
                continue;
            PdfIndirectReference ref = (PdfIndirectReference)item.widget_refs.get(k);
            PdfDictionary wd = (PdfDictionary)PdfReader.getPdfObject(ref);
            PdfDictionary pageDic = reader.getPageN(pageV);
            PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS), pageDic);
            if (annots != null) {
                if (removeRefFromArray(annots, ref) == 0) {
                    pageDic.remove(PdfName.ANNOTS);
                    markUsed(pageDic);
                }
                else
                    markUsed(annots);
            }
            PdfReader.killIndirect(ref);
            PdfIndirectReference kid = ref;
            while ((ref = (PdfIndirectReference)wd.get(PdfName.PARENT)) != null) {
                wd = (PdfDictionary)PdfReader.getPdfObject(ref);
                PdfArray kids = (PdfArray)PdfReader.getPdfObject(wd.get(PdfName.KIDS));
                if (removeRefFromArray(kids, kid) != 0)
                    break;
                kid = ref;
                PdfReader.killIndirect(ref);
            }
            if (ref == null) {
                removeRefFromArray(arrayf, kid);
                markUsed(arrayf);
            }
            if (page != -1) {
                item.merged.remove(k);
                item.page.remove(k);
                item.values.remove(k);
                item.widget_refs.remove(k);
                item.widgets.remove(k);
                --k;
            }
        }
        if (page == -1 || item.merged.size() == 0)
            fields.remove(name);
        return true;
    }
    
    /**
     * Removes a field from the document.
     * @param name the field name
     * @return <CODE>true</CODE> if the field exists, <CODE>false otherwise</CODE>
     */    
    public boolean removeField(String name) {
        return removeField(name, -1);
    }
    
    /** Gets the property generateAppearances.
     * @return the property generateAppearances
     */
    public boolean isGenerateAppearances() {
        return this.generateAppearances;
    }
    
    /** Sets the option to generate appearances. Not generating apperances
     * will speed-up form filling but the results can be
     * unexpected in Acrobat. Don't use it unless your environment is well
     * controlled. The default is <CODE>true</CODE>.
     * @param generateAppearances the option to generate appearances
     */
    public void setGenerateAppearances(boolean generateAppearances) {
        this.generateAppearances = generateAppearances;
        PdfDictionary top = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM));
        if (generateAppearances)
            top.remove(PdfName.NEEDAPPEARANCES);
        else
            top.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
    }
    
    /** The field representations for retrieval and modification. */    
    public static class Item {
        /** An array of <CODE>PdfDictionary</CODE> where the value tag /V
         * is present.
         */        
        public ArrayList values = new ArrayList();
        /** An array of <CODE>PdfDictionary</CODE> with the widgets.
         */        
        public ArrayList widgets = new ArrayList();
        /** An array of <CODE>PdfDictionary</CODE> with the widget references.
         */
        public ArrayList widget_refs = new ArrayList();
        /** An array of <CODE>PdfDictionary</CODE> with all the field
         * and widget tags merged.
         */        
        public ArrayList merged = new ArrayList();
        /** An array of <CODE>Integer</CODE> with the page numbers where
         * the widgets are displayed.
         */        
        public ArrayList page = new ArrayList();
        /** An array of <CODE>Integer</CODE> with the tab order of the field in the page.
         */        
        public ArrayList tabOrder = new ArrayList();
    }
    
    private static class InstHit {
        IntHashtable hits;
        public InstHit(int inst[]) {
            if (inst == null)
                return;
            hits = new IntHashtable();
            for (int k = 0; k < inst.length; ++k)
                hits.put(inst[k], 1);
        }
        
        public boolean isHit(int n) {
            if (hits == null)
                return true;
            return hits.containsKey(n);
        }
    }
    
    /**
     * Gets the field names that have signatures and are signed.
     * @return the field names that have signatures and are signed
     */    
    public ArrayList getSignatureNames() {
        if (sigNames != null)
            return new ArrayList(sigNames.keySet());
        sigNames = new HashMap();
        ArrayList sorter = new ArrayList();
        for (Iterator it = fields.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            Item item = (Item)entry.getValue();
            PdfDictionary merged = (PdfDictionary)item.merged.get(0);
            if (!PdfName.SIG.equals(merged.get(PdfName.FT)))
                continue;
            PdfObject vo = PdfReader.getPdfObject(merged.get(PdfName.V));
            if (vo == null || vo.type() != PdfObject.DICTIONARY)
                continue;
            PdfDictionary v = (PdfDictionary)vo;
            PdfObject contents = v.get(PdfName.CONTENTS);
            if (contents == null || contents.type() != PdfObject.STRING)
                continue;
            PdfObject ro = v.get(PdfName.BYTERANGE);
            if (ro == null || ro.type() != PdfObject.ARRAY)
                continue;
            ArrayList ra = ((PdfArray)ro).getArrayList();
            if (ra.size() < 2)
                continue;
            int length = ((PdfNumber)ra.get(ra.size() - 1)).intValue() + ((PdfNumber)ra.get(ra.size() - 2)).intValue();
            sorter.add(new Object[]{entry.getKey(), new int[]{length, 0}});
        }
        Collections.sort(sorter, new AcroFields.SorterComparator());
        if (!sorter.isEmpty()) {
            if (((int[])((Object[])sorter.get(sorter.size() - 1))[1])[0] == reader.getFileLength())
                totalRevisions = sorter.size();
            else
                totalRevisions = sorter.size() + 1;
            for (int k = 0; k < sorter.size(); ++k) {
                Object objs[] = (Object[])sorter.get(k);
                String name = (String)objs[0];
                int p[] = (int[])objs[1];
                p[1] = k + 1;
                sigNames.put(name, p);
            }
        }
        return new ArrayList(sigNames.keySet());
    }
    
    /**
     * Gets the field names that have blank signatures.
     * @return the field names that have blank signatures
     */    
    public ArrayList getBlankSignatureNames() {
        getSignatureNames();
        ArrayList sigs = new ArrayList();
        for (Iterator it = fields.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            Item item = (Item)entry.getValue();
            PdfDictionary merged = (PdfDictionary)item.merged.get(0);
            if (!PdfName.SIG.equals(merged.get(PdfName.FT)))
                continue;
            if (sigNames.containsKey(entry.getKey()))
                continue;
            sigs.add(entry.getKey());
        }
        return sigs;
    }
    
    /**
     * Gets the signature dictionary, the one keyed by /V.
     * @param name the field name
     * @return the signature dictionary keyed by /V or <CODE>null</CODE> if the field is not
     * a signature
     */    
    public PdfDictionary getSignatureDictionary(String name) {
        getSignatureNames();
        name = getTranslatedFieldName(name);
        if (!sigNames.containsKey(name))
            return null;
        Item item = (Item)fields.get(name);
        PdfDictionary merged = (PdfDictionary)item.merged.get(0);
        return (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.V));
    }
    
    /**
     * Checks is the signature covers the entire document or just part of it.
     * @param name the signature field name
     * @return <CODE>true</CODE> if the signature covers the entire document,
     * <CODE>false</CODE> otherwise
     */    
    public boolean signatureCoversWholeDocument(String name) {
        getSignatureNames();
        name = getTranslatedFieldName(name);
        if (!sigNames.containsKey(name))
            return false;
        return ((int[])sigNames.get(name))[0] == reader.getFileLength();
    }
    
    /**
     * Verifies a signature. An example usage is:
     * <p>
     * <pre>
     * KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
     * PdfReader reader = new PdfReader("my_signed_doc.pdf");
     * AcroFields af = reader.getAcroFields();
     * ArrayList names = af.getSignatureNames();
     * for (int k = 0; k &lt; names.size(); ++k) {
     *    String name = (String)names.get(k);
     *    System.out.println("Signature name: " + name);
     *    System.out.println("Signature covers whole document: " + af.signatureCoversWholeDocument(name));
     *    PdfPKCS7 pk = af.verifySignature(name);
     *    Calendar cal = pk.getSignDate();
     *    Certificate pkc[] = pk.getCertificates();
     *    System.out.println("Subject: " + PdfPKCS7.getSubjectFields(pk.getSigningCertificate()));
     *    System.out.println("Document modified: " + !pk.verify());
     *    Object fails[] = PdfPKCS7.verifyCertificates(pkc, kall, null, cal);
     *    if (fails == null)
     *        System.out.println("Certificates verified against the KeyStore");
     *    else
     *        System.out.println("Certificate failed: " + fails[1]);
     * }
     * </pre>
     * @param name the signature field name
     * @return a <CODE>PdfPKCS7</CODE> class to continue the verification
     */    
    public PdfPKCS7 verifySignature(String name) {
        return verifySignature(name, null);
    }
    
    /**
     * Verifies a signature. An example usage is:
     * <p>
     * <pre>
     * KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
     * PdfReader reader = new PdfReader("my_signed_doc.pdf");
     * AcroFields af = reader.getAcroFields();
     * ArrayList names = af.getSignatureNames();
     * for (int k = 0; k &lt; names.size(); ++k) {
     *    String name = (String)names.get(k);
     *    System.out.println("Signature name: " + name);
     *    System.out.println("Signature covers whole document: " + af.signatureCoversWholeDocument(name));
     *    PdfPKCS7 pk = af.verifySignature(name);
     *    Calendar cal = pk.getSignDate();
     *    Certificate pkc[] = pk.getCertificates();
     *    System.out.println("Subject: " + PdfPKCS7.getSubjectFields(pk.getSigningCertificate()));
     *    System.out.println("Document modified: " + !pk.verify());
     *    Object fails[] = PdfPKCS7.verifyCertificates(pkc, kall, null, cal);
     *    if (fails == null)
     *        System.out.println("Certificates verified against the KeyStore");
     *    else
     *        System.out.println("Certificate failed: " + fails[1]);
     * }
     * </pre>
     * @param name the signature field name
     * @param provider the provider or <code>null</code> for the default provider
     * @return a <CODE>PdfPKCS7</CODE> class to continue the verification
     */    
    public PdfPKCS7 verifySignature(String name, String provider) {
        PdfDictionary v = getSignatureDictionary(name);
        if (v == null)
            return null;
        try {
            PdfName sub = (PdfName)PdfReader.getPdfObject(v.get(PdfName.SUBFILTER));
            PdfString contents = (PdfString)PdfReader.getPdfObject(v.get(PdfName.CONTENTS));
            PdfPKCS7 pk = null;
            if (sub.equals(PdfName.ADBE_X509_RSA_SHA1)) {
                PdfString cert = (PdfString)PdfReader.getPdfObject(v.get(PdfName.CERT));
                pk = new PdfPKCS7(contents.getOriginalBytes(), cert.getBytes(), provider);
            }
            else
                pk = new PdfPKCS7(contents.getOriginalBytes(), provider);
            updateByteRange(pk, v);
            PdfString str = (PdfString)PdfReader.getPdfObject(v.get(PdfName.M));
            if (str != null)
                pk.setSignDate(PdfDate.decode(str.toString()));
            PdfObject obj = PdfReader.getPdfObject(v.get(PdfName.NAME));
            if (obj != null) {
              if (obj.isString())
                pk.setSignName(((PdfString)obj).toUnicodeString());
              else if(obj.isName())
                pk.setSignName(PdfName.decodeName(obj.toString()));
            }
            str = (PdfString)PdfReader.getPdfObject(v.get(PdfName.REASON));
            if (str != null)
                pk.setReason(str.toUnicodeString());
            str = (PdfString)PdfReader.getPdfObject(v.get(PdfName.LOCATION));
            if (str != null)
                pk.setLocation(str.toUnicodeString());
            return pk;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    private void updateByteRange(PdfPKCS7 pkcs7, PdfDictionary v) {
        PdfArray b = (PdfArray)PdfReader.getPdfObject(v.get(PdfName.BYTERANGE));
        RandomAccessFileOrArray rf = reader.getSafeFile();
        try {
            rf.reOpen();
            byte buf[] = new byte[8192];
            ArrayList ar = b.getArrayList();
            for (int k = 0; k < ar.size(); ++k) {
                int start = ((PdfNumber)ar.get(k)).intValue();
                int length = ((PdfNumber)ar.get(++k)).intValue();
                rf.seek(start);
                while (length > 0) {
                    int rd = rf.read(buf, 0, Math.min(length, buf.length));
                    if (rd <= 0)
                        break;
                    length -= rd;
                    pkcs7.update(buf, 0, rd);
                }
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        finally {
            try{rf.close();}catch(Exception e){}
        }
    }

    private void markUsed(PdfObject obj) {
        if (!append)
            return;
        ((PdfStamperImp)writer).markUsed(obj);
    }
    
    /**
     * Gets the total number of revisions this document has.
     * @return the total number of revisions
     */
    public int getTotalRevisions() {
        getSignatureNames();
        return this.totalRevisions;
    }
    
    /**
     * Gets this <CODE>field</CODE> revision.
     * @param field the signature field name
     * @return the revision or zero if it's not a signature field
     */    
    public int getRevision(String field) {
        getSignatureNames();
        field = getTranslatedFieldName(field);
        if (!sigNames.containsKey(field))
            return 0;
        return ((int[])sigNames.get(field))[1];
    }
    
    /**
     * Extracts a revision from the document.
     * @param field the signature field name
     * @return an <CODE>InputStream</CODE> covering the revision. Returns <CODE>null</CODE> if
     * it's not a signature field
     * @throws IOException on error
     */    
    public InputStream extractRevision(String field) throws IOException {
        getSignatureNames();
        field = getTranslatedFieldName(field);
        if (!sigNames.containsKey(field))
            return null;
        int length = ((int[])sigNames.get(field))[0];
        RandomAccessFileOrArray raf = reader.getSafeFile();
        raf.reOpen();
        raf.seek(0);
        return new RevisionStream(raf, length);
    }

    /**
     * Gets the appearances cache.
     * @return the appearances cache
     */
    public HashMap getFieldCache() {
        return this.fieldCache;
    }
    
    /**
     * Sets a cache for field appearances. Parsing the existing PDF to
     * create a new TextField is time expensive. For those tasks that repeatedly
     * fill the same PDF with different field values the use of the cache has dramatic
     * speed advantages. An example usage:
     * <p>
     * <pre>
     * String pdfFile = ...;// the pdf file used as template
     * ArrayList xfdfFiles = ...;// the xfdf file names
     * ArrayList pdfOutFiles = ...;// the output file names, one for each element in xpdfFiles
     * HashMap cache = new HashMap();// the appearances cache
     * PdfReader originalReader = new PdfReader(pdfFile);
     * for (int k = 0; k &lt; xfdfFiles.size(); ++k) {
     *    PdfReader reader = new PdfReader(originalReader);
     *    XfdfReader xfdf = new XfdfReader((String)xfdfFiles.get(k));
     *    PdfStamper stp = new PdfStamper(reader, new FileOutputStream((String)pdfOutFiles.get(k)));
     *    AcroFields af = stp.getAcroFields();
     *    af.setFieldCache(cache);
     *    af.setFields(xfdf);
     *    stp.close();
     * }
     * </pre>
     * @param fieldCache an HasMap that will carry the cached appearances
     */
    public void setFieldCache(HashMap fieldCache) {
        this.fieldCache = fieldCache;
    }
    
    /**
     * Sets extra margins in text fields to better mimic the Acrobat layout.
     * @param extraMarginLeft the extra marging left
     * @param extraMarginTop the extra margin top
     */    
    public void setExtraMargin(float extraMarginLeft, float extraMarginTop) {
        this.extraMarginLeft = extraMarginLeft;
        this.extraMarginTop = extraMarginTop;
    }
    
    /**
     * Adds a substitution font to the list. The fonts in this list will be used if the original
     * font doesn't contain the needed glyphs.
     * @param font the font
     */
    public void addSubstitutionFont(BaseFont font) {
        if (substitutionFonts == null)
            substitutionFonts = new ArrayList();
        substitutionFonts.add(font);
    }

    private static final HashMap stdFieldFontNames = new HashMap();
    
    /**
     * Holds value of property totalRevisions.
     */
    private int totalRevisions;
    
    /**
     * Holds value of property fieldCache.
     */
    private HashMap fieldCache;
    
    static {
        stdFieldFontNames.put("CoBO", new String[]{"Courier-BoldOblique"});
        stdFieldFontNames.put("CoBo", new String[]{"Courier-Bold"});
        stdFieldFontNames.put("CoOb", new String[]{"Courier-Oblique"});
        stdFieldFontNames.put("Cour", new String[]{"Courier"});
        stdFieldFontNames.put("HeBO", new String[]{"Helvetica-BoldOblique"});
        stdFieldFontNames.put("HeBo", new String[]{"Helvetica-Bold"});
        stdFieldFontNames.put("HeOb", new String[]{"Helvetica-Oblique"});
        stdFieldFontNames.put("Helv", new String[]{"Helvetica"});
        stdFieldFontNames.put("Symb", new String[]{"Symbol"});
        stdFieldFontNames.put("TiBI", new String[]{"Times-BoldItalic"});
        stdFieldFontNames.put("TiBo", new String[]{"Times-Bold"});
        stdFieldFontNames.put("TiIt", new String[]{"Times-Italic"});
        stdFieldFontNames.put("TiRo", new String[]{"Times-Roman"});
        stdFieldFontNames.put("ZaDb", new String[]{"ZapfDingbats"});
        stdFieldFontNames.put("HySm", new String[]{"HYSMyeongJo-Medium", "UniKS-UCS2-H"});
        stdFieldFontNames.put("HyGo", new String[]{"HYGoThic-Medium", "UniKS-UCS2-H"});
        stdFieldFontNames.put("KaGo", new String[]{"HeiseiKakuGo-W5", "UniKS-UCS2-H"});
        stdFieldFontNames.put("KaMi", new String[]{"HeiseiMin-W3", "UniJIS-UCS2-H"});
        stdFieldFontNames.put("MHei", new String[]{"MHei-Medium", "UniCNS-UCS2-H"});
        stdFieldFontNames.put("MSun", new String[]{"MSung-Light", "UniCNS-UCS2-H"});
        stdFieldFontNames.put("STSo", new String[]{"STSong-Light", "UniGB-UCS2-H"});
    }

    private static class RevisionStream extends InputStream {
        private byte b[] = new byte[1];
        private RandomAccessFileOrArray raf;
        private int length;
        private int rangePosition = 0;
        private boolean closed;
        
        private RevisionStream(RandomAccessFileOrArray raf, int length) {
            this.raf = raf;
            this.length = length;
        }
        
        public int read() throws IOException {
            int n = read(b);
            if (n != 1)
                return -1;
            return b[0] & 0xff;
        }
        
        public int read(byte[] b, int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            if (rangePosition >= length) {
                close();
                return -1;
            }
            int elen = Math.min(len, length - rangePosition);
            raf.readFully(b, off, elen);
            rangePosition += elen;
            return elen;
        }
        
        public void close() throws IOException {
            if (!closed) {
                raf.close();
                closed = true;
            }
        }
    }
    
    private static class SorterComparator implements Comparator {        
        public int compare(Object o1, Object o2) {
            int n1 = ((int[])((Object[])o1)[1])[0];
            int n2 = ((int[])((Object[])o2)[1])[0];
            return n1 - n2;
        }        
    }

    /**
     * Gets the list of substitution fonts. The list is composed of <CODE>BaseFont</CODE> and can be <CODE>null</CODE>. The fonts in this list will be used if the original
     * font doesn't contain the needed glyphs.
     * @return the list
     */
    public ArrayList getSubstitutionFonts() {
        return substitutionFonts;
    }

    /**
     * Sets a list of substitution fonts. The list is composed of <CODE>BaseFont</CODE> and can also be <CODE>null</CODE>. The fonts in this list will be used if the original
     * font doesn't contain the needed glyphs.
     * @param substitutionFonts the list
     */
    public void setSubstitutionFonts(ArrayList substitutionFonts) {
        this.substitutionFonts = substitutionFonts;
    }

    /**
     * Gets the XFA form processor.
     * @return the XFA form processor
     */
    public XfaForm getXfa() {
        return xfa;
    }
    
    private static final PdfName[] buttonRemove = {PdfName.MK, PdfName.F , PdfName.FF , PdfName.Q , PdfName.BS , PdfName.BORDER};
    
    /**
     * Creates a new pushbutton from an existing field. This pushbutton can be changed and be used to replace 
     * an existing one, with the same name or other name, as long is it is in the same document. To replace an existing pushbutton
     * call {@link #replacePushbuttonField(String,PdfFormField)}.
     * @param field the field name that should be a pushbutton
     * @return a new pushbutton or <CODE>null</CODE> if the field is not a pushbutton
     */
    public PushbuttonField getNewPushbuttonFromField(String field) {
        try {
            if (getFieldType(field) != FIELD_TYPE_PUSHBUTTON)
                return null;
            float[] pos = getFieldPositions(field);
            Rectangle box = new Rectangle(pos[1], pos[2], pos[3], pos[4]);
            PushbuttonField newButton = new PushbuttonField(writer, box, null);
            Item item = getFieldItem(field);
            PdfDictionary dic = (PdfDictionary)item.merged.get(0);
            decodeGenericDictionary(dic, newButton);
            PdfDictionary mk = (PdfDictionary)PdfReader.getPdfObject(dic.get(PdfName.MK));
            if (mk != null) {
                PdfString text = (PdfString)PdfReader.getPdfObject(mk.get(PdfName.CA));
                if (text != null)
                    newButton.setText(text.toUnicodeString());
                PdfNumber tp = (PdfNumber)PdfReader.getPdfObject(mk.get(PdfName.TP));
                if (tp != null)
                    newButton.setLayout(tp.intValue() + 1);
                PdfDictionary ifit = (PdfDictionary)PdfReader.getPdfObject(mk.get(PdfName.IF));
                if (ifit != null) {
                    PdfName sw = (PdfName)PdfReader.getPdfObject(ifit.get(PdfName.SW));
                    if (sw != null) {
                        int scale = PushbuttonField.SCALE_ICON_ALWAYS;
                        if (sw.equals(PdfName.B))
                            scale = PushbuttonField.SCALE_ICON_IS_TOO_BIG;
                        else if (sw.equals(PdfName.S))
                            scale = PushbuttonField.SCALE_ICON_IS_TOO_SMALL;
                        else if (sw.equals(PdfName.N))
                            scale = PushbuttonField.SCALE_ICON_NEVER;
                        newButton.setScaleIcon(scale);
                    }
                    sw = (PdfName)PdfReader.getPdfObject(ifit.get(PdfName.S));
                    if (sw != null) {
                        if (sw.equals(PdfName.A))
                            newButton.setProportionalIcon(false);
                    }
                    PdfArray aj = (PdfArray)PdfReader.getPdfObject(ifit.get(PdfName.A));
                    if (aj != null && aj.size() == 2) {
                        float left = ((PdfNumber)PdfReader.getPdfObject((PdfObject)aj.getArrayList().get(0))).floatValue();
                        float bottom = ((PdfNumber)PdfReader.getPdfObject((PdfObject)aj.getArrayList().get(1))).floatValue();
                        newButton.setIconHorizontalAdjustment(left);
                        newButton.setIconVerticalAdjustment(bottom);
                    }
                    PdfObject fb = PdfReader.getPdfObject(ifit.get(PdfName.FB));
                    if (fb != null && fb.toString().equals("true"))
                        newButton.setIconFitToBounds(true);
                }
                PdfObject i = mk.get(PdfName.I);
                if (i != null && i.isIndirect())
                    newButton.setIconReference((PRIndirectReference)i);
            }
            return newButton;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    /**
     * Replaces the field with a new pushbutton. The pushbutton can be created with
     * {@link #getNewPushbuttonFromField(String)} from the same document or it can be a
     * generic PdfFormField of the type pushbutton.
     * @param field the field name
     * @param button the <CODE>PdfFormField</CODE> representing the pushbutton
     * @return <CODE>true</CODE> if the field was replaced, <CODE>false</CODE> if the field
     * was not a pushbutton
     */
    public boolean replacePushbuttonField(String field, PdfFormField button) {
        if (getFieldType(field) != FIELD_TYPE_PUSHBUTTON)
            return false;
        Item item = getFieldItem(field);
        PdfDictionary merged = (PdfDictionary)item.merged.get(0);
        PdfDictionary values = (PdfDictionary)item.values.get(0);
        PdfDictionary widgets = (PdfDictionary)item.widgets.get(0);
        for (int k = 0; k < buttonRemove.length; ++k) {
            merged.remove(buttonRemove[k]);
            values.remove(buttonRemove[k]);
            widgets.remove(buttonRemove[k]);
        }
        for (Iterator it = button.getKeys().iterator(); it.hasNext();) {
            PdfName key = (PdfName)it.next();
            if (key.equals(PdfName.T) || key.equals(PdfName.RECT))
                continue;
            merged.put(key, button.get(key));
            widgets.put(key, button.get(key));
        }
        return true;
    }
}
