/*
 * Copyright 2003 by Paulo Soares.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.awt.Color;

/** Query and change fields in existing documents either by method
 * calls or by FDF merging.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class AcroFields {

    PdfReader reader;
    PdfWriter writer;
    HashMap fields;
    private int topFirst;
    private ArrayList sigNames;
    static private final int DA_FONT = 0;
    static private final int DA_SIZE = 1;
    static private final int DA_COLOR = 2;
    
    private boolean lastWasString;
    
    /** Holds value of property generateAppearances. */
    private boolean generateAppearances = true;
    
    AcroFields(PdfReader reader, PdfWriter writer) {
        this.reader = reader;
        this.writer = writer;
        fill();
    }

    void fill() {
        fields = new HashMap();
        PdfDictionary top = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM));
        for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
            PdfDictionary page = reader.getPageN(k);
            PdfArray annots = (PdfArray)PdfReader.getPdfObject(page.get(PdfName.ANNOTS));
            if (annots == null)
                continue;
            ArrayList arr = annots.getArrayList();
            for (int j = 0; j < arr.size(); ++j) {
                PdfObject annoto = PdfReader.getPdfObject((PdfObject)arr.get(j));
                if ((annoto instanceof PdfIndirectReference) && !annoto.isIndirect())
                    continue;
                PdfDictionary annot = (PdfDictionary)annoto;
                if (!PdfName.WIDGET.equals(annot.get(PdfName.SUBTYPE)))
                    continue;
                PdfDictionary widget = annot;
                PdfDictionary dic = new PdfDictionary();
                dic.putAll(annot);
                String name = "";
                PdfDictionary value = null;
                PdfObject lastV = null;
                while (annot != null) {
                    dic.mergeDifferent(annot);
                    PdfString t = (PdfString)annot.get(PdfName.T);
                    if (t != null)
                        name = t.toUnicodeString() + "." + name;
                    if (lastV == null && annot.get(PdfName.V) != null)
                        lastV = annot.get(PdfName.V);
                    if (value == null &&  t != null) {
                        value = annot;
                        if (annot.get(PdfName.V) == null && lastV  != null)
                            value.put(PdfName.V, lastV);
                    }
                    annot = (PdfDictionary)PdfReader.getPdfObject(annot.get(PdfName.PARENT));
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
            dic = (PdfDictionary)dic.get(PdfName.AP);
            if (dic == null)
                continue;
            PdfObject ob = dic.get(PdfName.N);
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
            dic = (PdfDictionary)item.merged.get(k);
            dic.put(PdfName.T, ss);
        }
        fields.remove(oldName);
        fields.put(newName, item);
        return true;
    }
    
    static private Object[] splitDAelements(String da) {
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
    
    PdfAppearance getAppearance(PdfDictionary merged, String text) throws IOException, DocumentException {
        topFirst = 0;
        TextField tx = new TextField(writer, null, null);
        tx.setBorderWidth(0);
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
                        if (po != null && po.type() == PdfObject.INDIRECT)
                            tx.setFont(new DocumentFont((PRIndirectReference)po));
                    }
                }
            }
        }
        //rotation, border and backgound color
        PdfDictionary mk = (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.MK));
        if (mk != null) {
            PdfArray ar = (PdfArray)PdfReader.getPdfObject(mk.get(PdfName.BC));
            tx.setBorderColor(getMKColor(ar));
            ar = (PdfArray)PdfReader.getPdfObject(mk.get(PdfName.BG));
            tx.setBackgroundColor(getMKColor(ar));
            PdfNumber rotation = (PdfNumber)PdfReader.getPdfObject(mk.get(PdfName.R));
            if (rotation != null)
                tx.setRotation(rotation.intValue());
        }
        //multiline
        int flags = 0;
        PdfNumber nfl = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.FF));
        if (nfl != null)
            flags = nfl.intValue();
        tx.setOptions(((flags & PdfFormField.FF_MULTILINE) == 0 ? 0 : TextField.MULTILINE) | ((flags & PdfFormField.FF_COMB) == 0 ? 0 : TextField.COMB));
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
        //rect
        PdfArray rect = (PdfArray)PdfReader.getPdfObject(merged.get(PdfName.RECT));
        Rectangle box = PdfReader.getNormalizedRectangle(rect);
        if (tx.getRotation() == 90 || tx.getRotation() == 270)
            box = box.rotate();
        tx.setBox(box);
        PdfName fieldType = (PdfName)PdfReader.getPdfObject(merged.get(PdfName.FT));
        if (PdfName.TX.equals(fieldType)) {
            tx.setText(text);
            return tx.getAppearance();
        }
        if (!PdfName.CH.equals(fieldType))
            throw new DocumentException("An appearance was requested without a variable text field.");
        if ((flags & PdfFormField.FF_COMBO) != 0) {
            tx.setText(text);
            return tx.getAppearance();
        }
        PdfArray opt = (PdfArray)PdfReader.getPdfObject(merged.get(PdfName.OPT));
        int arrsize = 0;
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
            int idx = 0;
            for (int k = 0; k < choices.length; ++k) {
                if (text.equals(choices[k])) {
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
                return new Color(((PdfNumber)cc.get(0)).floatValue(), ((PdfNumber)cc.get(1)).floatValue(), ((PdfNumber)cc.get(2)).floatValue());
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
        Item item = (Item)fields.get(field);
        if (item == null)
            return false;
        InstHit hit = new InstHit(inst);
        if (name.equalsIgnoreCase("textcolor")) {
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
                        }
                    }
                }
            }
        }
        else if (name.equalsIgnoreCase("bgcolor") || name.equalsIgnoreCase("bordercolor")) {
            PdfName dname = (name.equalsIgnoreCase("bgcolor") ? PdfName.BG : PdfName.BC);
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfDictionary mk = (PdfDictionary)PdfReader.getPdfObject(((PdfDictionary)item.merged.get(k)).get(PdfName.MK));
                    if (mk == null) {
                        if (value == null)
                            return true;
                        mk = new PdfDictionary();
                        ((PdfDictionary)item.merged.get(k)).put(PdfName.MK, mk);
                        ((PdfDictionary)item.widgets.get(k)).put(PdfName.MK, mk);
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
    
    /**
     * Sets a field property. Valid property names are:
     * <p>
     * <ul>
     * <li>flags - a set of flags specifying various characteristics of the field’s widget annotation.
     * The value of this entry replaces that of the F entry in the form’s corresponding annotation dictionary.<br>
     * <li>setflags - a set of flags to be set (turned on) in the F entry of the form’s corresponding
     * widget annotation dictionary. Bits equal to 1 cause the corresponding bits in F to be set to 1.<br>
     * <li>clrflags - a set of flags to be cleared (turned off) in the F entry of the form’s corresponding
     * widget annotation dictionary. Bits equal to 1 cause the corresponding
     * bits in F to be set to 0.<br>
     * <li>fflags - a set of flags specifying various characteristics of the field. The value
     * of this entry replaces that of the Ff entry in the form’s corresponding field dictionary.<br>
     * <li>setfflags - a set of flags to be set (turned on) in the Ff entry of the form’s corresponding
     * field dictionary. Bits equal to 1 cause the corresponding bits in Ff to be set to 1.<br>
     * <li>clrfflags - a set of flags to be cleared (turned off) in the Ff entry of the form’s corresponding
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
                }
            }
        }
        else if (name.equalsIgnoreCase("fflags")) {
            PdfNumber num = new PdfNumber(value);
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    ((PdfDictionary)item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary)item.values.get(k)).put(PdfName.FF, num);
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
                }
            }
        }
        else
            return false;
        return true;
    }
    
    /** Sets the fields by FDF merging.
     * @param fdf the FDF form
     * @throws IOException on error
     * @throws DocumentException on error
     */    
    public void setFields(FdfReader fdf) throws IOException, DocumentException {
        HashMap fd = fdf.getFields();
        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
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
        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String f = (String)i.next();
            String v = xfdf.getFieldValue(f);
            if (v != null)
                setField(f, v);
        }
    }

    /** Sets the field value.
     * @param name the fully qualified field name
     * @param value the field value
     * @throws IOException on error
     * @throws DocumentException on error
     * @return <CODE>true</CODE> if the field was found and changed,
     * <CODE>false</CODE> otherwise
     */    
    public boolean setField(String name, String value) throws IOException, DocumentException {
        return setField(name, value, value);
    }
    
    /** Sets the field value and the display string. The display string
     * is used to build the appearance in the cases where the value
     * is modified by Acrobat with JavaScript and the algorithm is
     * known.
     * @param name the fully qualified field name
     * @param value the field value
     * @param display the string that is used for the appearance
     * @return <CODE>true</CODE> if the field was found and changed,
     * <CODE>false</CODE> otherwise
     * @throws IOException on error
     * @throws DocumentException on error
     */    
    public boolean setField(String name, String value, String display) throws IOException, DocumentException {
        if (writer == null)
            throw new DocumentException("This AcroFields instance is read-only.");
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
        if (PdfName.TX.equals(type) || PdfName.CH.equals(type)) {
            PdfString v = new PdfString(value, PdfObject.TEXT_UNICODE);
            for (int idx = 0; idx < item.values.size(); ++idx) {
                ((PdfDictionary)item.values.get(idx)).put(PdfName.V, v);
                PdfDictionary merged = (PdfDictionary)item.merged.get(idx);
                merged.put(PdfName.V, v);
                PdfDictionary widget = (PdfDictionary)item.widgets.get(idx);
                if (generateAppearances) {
                    PdfAppearance app = getAppearance(merged, display);
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
                }
                else {
                    widget.remove(PdfName.AP);
                    merged.remove(PdfName.AP);
                }
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
                    PdfDictionary merged = (PdfDictionary)item.merged.get(idx);
                    merged.put(PdfName.V, v);
                    merged.put(PdfName.AS, v);
                    PdfDictionary widget = (PdfDictionary)item.widgets.get(idx);
                    widget.put(PdfName.AS, v);
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
                    if (valt != null) {
                        PdfString ps = new PdfString(value, PdfObject.TEXT_UNICODE);
                        ((PdfDictionary)item.values.get(idx)).put(PdfName.V, ps);
                        merged.put(PdfName.V, ps);
                    }
                    else {
                        ((PdfDictionary)item.values.get(idx)).put(PdfName.V, v);
                        merged.put(PdfName.V, v);
                    }
                    if (isInAP(widget,  vt)) {
                        merged.put(PdfName.AS, vt);
                        widget.put(PdfName.AS, vt);
                    }
                    else {
                        merged.put(PdfName.AS, PdfName.OFF);
                        widget.put(PdfName.AS, PdfName.OFF);
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
        return (Item)fields.get(name);
    }
    
    /**
     * Gets the field box positions in the document. The return is an array of <CODE>float</CODE>
     * multiple of 5. For each of this groups the values are: [page, llx, lly, urx,
     * ury].
     * @param name the field name
     * @return the positions or <CODE>null</CODE> if field does not exist
     */    
    public float[] getFieldPositions(String name) {
        Item item = (Item)fields.get(name);
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
                ret[ptr] = ((Integer)item.page.get(k)).floatValue();
                ++ptr;
                ret[ptr++] = r.left();
                ret[ptr++] = r.bottom();
                ret[ptr++] = r.right();
                ret[ptr++] = r.top();
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
        Item item = (Item)fields.get(name);
        if (item == null)
            return false;
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM));
        
        if (acroForm == null)
            return false;
        PdfArray arrayf = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS));
        if (arrayf == null)
            return false;
        for (int k = 0; k < item.widget_refs.size(); ++k) {
            int pageV = ((Integer)item.page.get(k)).intValue();
            if (page != -1 && page != pageV)
                continue;
            PdfIndirectReference ref = (PdfIndirectReference)item.widget_refs.get(k);
            PdfDictionary wd = (PdfDictionary)PdfReader.getPdfObject(ref);
            PdfDictionary pageDic = reader.getPageN(pageV);
            PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
            if (annots != null) {
                if (removeRefFromArray(annots, ref) == 0)
                    pageDic.remove(PdfName.ANNOTS);
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
     * Gets the field names that have signatures.
     * @return the field names that have signatures
     */    
    public ArrayList getSignatureNames() {
        if (sigNames != null)
            return sigNames;
        sigNames = new ArrayList();
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
            sigNames.add(entry.getKey());
        }
        return sigNames;
    }
    
    /**
     * Gets the signature dictionary, the one keyed by /V.
     * @param name the field name
     * @return the signature dictionary keyed by /V or <CODE>null</CODE> if the field is not
     * a signature
     */    
    public PdfDictionary getSignatureDictionary(String name) {
        getSignatureNames();
        if (sigNames.indexOf(name) < 0)
            return null;
        Item item = (Item)fields.get(name);
        PdfDictionary merged = (PdfDictionary)item.merged.get(0);
        PdfObject vo = PdfReader.getPdfObject(merged.get(PdfName.V));
        return (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.V));
    }
    
    /**
     * Checks is the signature covers the entire document or just part of it.
     * @param name the signature field name
     * @return <CODE>true</CODE> if the signature covers the entire document,
     * <CODE>false</CODE> otherwise
     */    
    public boolean signatureCoversWholeDocument(String name) {
        PdfDictionary v = getSignatureDictionary(name);
        if (v == null)
            return false;
        RandomAccessFileOrArray rf = reader.getSafeFile();
        try {
            rf.reOpen();
            int len = rf.length();
            rf.close();
            PdfArray bounds = (PdfArray)PdfReader.getPdfObject(v.get(PdfName.BYTERANGE));
            ArrayList ar = bounds.getArrayList();
            int last = ar.size() - 2;
            int offset = ((PdfNumber)ar.get(last)).intValue();
            int count = ((PdfNumber)ar.get(last + 1)).intValue();
            return offset + count == len;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        finally {
            try{rf.close();}catch(Exception e){}
        }
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
            str = (PdfString)PdfReader.getPdfObject(v.get(PdfName.NAME));
            if (str != null)
                pk.setSignName(str.toUnicodeString());
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
}
