/*
 * Copyright 2003 by Emmanuel Hugonnet.
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

import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Implements the UCC/EAN-128 in C mode with multiple parts in code.
 *
 * <p>
 * The code is numeric only (which is correct in C) with FNC1 char to  separate
 * data if the Application Identifier supports variable data. The default
 * parameters are:
 * <pre>
 * x = 0.8f;
 * font = BaseFont.createFont("Helvetica", "winansi", false);
 * size = 8;
 * baseline = size;
 * barHeight = size * 3;
 * textAlignment = Element.ALIGN_CENTER;
 * codeType = CODE128_UCC; (not used)
 * </pre>
 * </p>
 *
 * @author Emmanuel Hugonnet (emmanuel.hugonnet.at.illicom.com)
 * @author Olivier Albrecht (albrecht.at.illicom.com)
 */
public class BarcodeEAN128 extends Barcode128 {
    //A map of all the Application Identifier existing in EAN128
    public static HashMap ids;
    
    //A map beetween a char and its index in
    public static IntHashtable codesB;
    
    static {
        codesB = new IntHashtable(100);
        ids = new HashMap(10000);
        ids.put("00", new IdentifierEAN("00", "n2+n18", 20, false));
        ids.put("01", new IdentifierEAN("01", "n2+n14", 16, false));
        ids.put("02", new IdentifierEAN("02", "n2+n14", 16, false));
        ids.put("10", new IdentifierEAN("10", "n2+an...20", -1, true));
        ids.put("11", new IdentifierEAN("11", "n2+n6", 8, false));
        ids.put("13", new IdentifierEAN("13", "n2+n6", 8, false));
        ids.put("15", new IdentifierEAN("15", "n2+n6", 8, false));
        ids.put("17", new IdentifierEAN("17", "n2+n6", 8, false));
        ids.put("20", new IdentifierEAN("20", "n2+n6", 8, false));
        ids.put("21", new IdentifierEAN("21", "n2+an...20", -1, true));
        ids.put("22", new IdentifierEAN("22", "n2+an...29", -1, true));
        ids.put("230", new IdentifierEAN("230", "n3+n...19", -1, true));
        ids.put("231", new IdentifierEAN("231", "n3+n...19", -1, true));
        ids.put("232", new IdentifierEAN("232", "n3+n...19", -1, true));
        ids.put("233", new IdentifierEAN("233", "n3+n...19", -1, true));
        ids.put("234", new IdentifierEAN("234", "n3+n...19", -1, true));
        ids.put("235", new IdentifierEAN("235", "n3+n...19", -1, true));
        ids.put("236", new IdentifierEAN("236", "n3+n...19", -1, true));
        ids.put("237", new IdentifierEAN("237", "n3+n...19", -1, true));
        ids.put("238", new IdentifierEAN("238", "n3+n...19", -1, true));
        ids.put("239", new IdentifierEAN("239", "n3+n...19", -1, true));
        ids.put("240", new IdentifierEAN("240", "n3+an...30", -1, true));
        ids.put("241", new IdentifierEAN("241", "n3+an...30", -1, true));
        ids.put("250", new IdentifierEAN("250", "n3+an...30", -1, true));
        ids.put("30", new IdentifierEAN("30", "n2+n...8", -1, true));
        
        for (int i = 3100; i < 3700; i++)
            ids.put("" + i, new IdentifierEAN("" + i, "n2+n...8", -1, true));
        ids.put("337", new IdentifierEAN("337", "n4+n6", 10, false));
        ids.put("37", new IdentifierEAN("37", "n2+n...8", -1, true));
        ids.put("400", new IdentifierEAN("400", "n3+an...30", -1, true));
        ids.put("401", new IdentifierEAN("401", "n3+an...30", -1, true));
        ids.put("402", new IdentifierEAN("402", "n3+n17", 20, false));
        ids.put("403", new IdentifierEAN("403", "n3+an...30", -1, true));
        ids.put("410", new IdentifierEAN("410", "n3+n13", 16, false));
        ids.put("411", new IdentifierEAN("411", "n3+n13", 16, false));
        ids.put("412", new IdentifierEAN("412", "n3+n13", 16, false));
        ids.put("413", new IdentifierEAN("413", "n3+n13", 16, false));
        ids.put("414", new IdentifierEAN("414", "n3+n13", 16, false));
        ids.put("420", new IdentifierEAN("420", "n3+an...9", -1, true));
        ids.put("421", new IdentifierEAN("421", "n3+n3+an...9", -1, true));
        ids.put("422", new IdentifierEAN("422", "n3+n3", 6, false));
        ids.put("8001", new IdentifierEAN("8001", "n4+n14", 18, false));
        ids.put("8002", new IdentifierEAN("8002", "n4+an...30", -1, true));
        ids.put("8003", new IdentifierEAN("8003", "n4+n14+an...16", -1, true));
        ids.put("8004", new IdentifierEAN("8004", "n4+an...30", -1, true));
        ids.put("8005", new IdentifierEAN("8005", "n4+n6", 10, false));
        ids.put("8006", new IdentifierEAN("8006", "n4+n14+n2+n2", 22, false));
        ids.put("8018", new IdentifierEAN("8018", "n4+n18", 22, false));
        ids.put("8100", new IdentifierEAN("8100", "n4+n1+n5", 10, false));
        ids.put("8101", new IdentifierEAN("8101", "n4+n1+n5+n4", 14, false));
        ids.put("8102", new IdentifierEAN("8102", "n4+n1+n1", 6, false));
        ids.put("90", new IdentifierEAN("90", "n2+an...30", -1, true));
        
        for (int j = 91; j < 100; j++) {
            ids.put("" + j, new IdentifierEAN("" + j, "n2+an...30", -1, true));
        }
        
        for (int k = 0; k < 95; ++k)
            codesB.put(k + ' ',  k);
        for (int k = 95; k < 196; ++k)
            codesB.put(k - 95 + '\u00c3', k);

        //codesB is not completed and codesA should be done too
    }
    
    //Logger
    /*private Logger logger;*/
    
    //The text to display under the barcode in human readable form.
    private String codeText = "";
    
    //To calculate the length of the text.
    private StringBuffer bCode = new StringBuffer(50);
    
    //The checksum of the barcode
    private int checksum = 0;
    
    //To compute the checksum of the barcode
    private int ponderation = 2;
    
    /**
     * Creates a new instance of BarcodeEAN128
     *
     * @throws ExceptionConverter when the conversion is not possible.
     */
    public BarcodeEAN128() {
        /*logger = JWDLLogManager.getLogger(this);*/
        
        try {
            x = 0.8f;
            font = BaseFont.createFont("Helvetica", "winansi", false);
            size = 8;
            baseline = size;
            barHeight = size * 3;
            textAlignment = Element.ALIGN_CENTER;
            codeType = CODE128_UCC;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    /**
     * Gets the maximum area that the barcode and the text, if any, will occupy.
     * The lower left corner is always (0, 0).
     *
     * @return the size the barcode occupies.
     */
    public Rectangle getBarcodeSize(float fullWidth) {
        float fontX = 0;
        float fontY = 0;
        String fullCode;
        
        if (font != null) {
            if (baseline > 0) {
                fontY = baseline - font.getFontDescriptor(BaseFont.DESCENT, size);
            }
            else {
                fontY = -baseline + size;
            }
            fullCode = codeText;
            fontX = font.getWidthPoint(fullCode, size);
        }
        float maxWidth = Math.max(fullWidth, fontX);
        
        float fullHeight = barHeight + fontY;
        
        return new Rectangle(maxWidth, fullHeight);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getBarsEAN128(String text) {
        LinkedList barres = new LinkedList();
        int tLen = text.length();
        
        if (tLen == 0) {
            barres.add(BARS[START_B]);
            bCode.append(STARTB);
            barres.add(BARS[FNC1_INDEX]);
            bCode.append(FNC1);
            
            return getBarsArray(barres);
        }
        
        int index = 0;
        barres.add(BARS[START_C]);
        bCode.append(STARTC);
        barres.add(BARS[FNC1_INDEX]);
        bCode.append(FNC1);
        checksum = checksum + START_C;
        checksum = checksum + FNC1_INDEX;
        
        String bloc = "";
        while (bloc != null) {
            bloc = getBloc(text, index);
            
            if (bloc != null) {
                index = index + bloc.length();
                bCode.append(bloc);
                barres.add(getBarsCode(bloc));
            }
        }
        
        int chck = checksum % 103;
        barres.add(BARS[chck]);
        bCode.append((char)chck);
        barres.add(BARS_STOP);
        bCode.append((char)0);
        
        byte[] array = getBarsArray(barres);
        return array;
    }
    
    /**
     * Places the barcode in a <CODE>PdfContentByte</CODE>. The barcode is always
     * placed at coodinates (0, 0). Use the translation matrix to move it
     * elsewhere.
     *
     * <p>
     * The bars and text are written in the following colors:
     * </p>
     *
     * <p></p>
     *
     * <P></p>
     *
     * @param cb the <CODE>PdfContentByte</CODE> where the barcode will be placed
     * @param barColor the color of the bars. It can be <CODE>null</CODE>
     * @param textColor the color of the text. It can be <CODE>null</CODE>
     *
     * @return the dimensions the barcode occupies
     */
    public Rectangle placeBarcode(PdfContentByte cb, Color barColor,
    Color textColor) {
        String fullCode = code;
        float fontX = 0;
        byte[] bars = getBarsEAN128(code);
        
        if (font != null) {
            fontX = font.getWidthPoint(codeText, size);
        }
        
        float barStartY = 0;
        float textStartY = 0;
        
        if (font != null) {
            if (baseline <= 0) {
                textStartY = barHeight - baseline;
            }
            else {
                textStartY = -font.getFontDescriptor(BaseFont.DESCENT, size);
                barStartY = textStartY + baseline;
            }
        }
        
        boolean print = true;
        
        float fullWidth = (bars.length / 6 * 11 + 2)  * x;
        float barStartX = 0;
        float textStartX = 0;
        
        switch (textAlignment) {
            case Element.ALIGN_LEFT:
                break;
                
            case Element.ALIGN_RIGHT:
                
                if (fontX > fullWidth) {
                    barStartX = fontX - fullWidth;
                }
                else {
                    textStartX = fullWidth - fontX;
                }
                
                break;
                
            default:
                
                if (fontX > fullWidth) {
                    barStartX = (fontX - fullWidth) / 2;
                }
                else {
                    textStartX = (fullWidth - fontX) / 2;
                }
                
                break;
        }
        
        if (barColor != null) {
            cb.setColorFill(barColor);
        }
        for (int k = 0; k < bars.length; ++k) {
            float w = bars[k] * x;
            
            if (print) {
                cb.rectangle(barStartX, barStartY, w, barHeight);
            }
            print = !print;
            barStartX += w;
        }
        cb.fill();
        
        if (font != null) {
            if (textColor != null) {
                cb.setColorFill(textColor);
            }
            cb.beginText();
            cb.setFontAndSize(font, size);
            cb.setTextMatrix(textStartX, textStartY);
            cb.showText(codeText);
            cb.endText();
        }
        return getBarcodeSize(fullWidth);
    }
    
    /**
     * Compute the bars corresponding to a bloc of data.
     *
     * @param bloc : A bloc of data with its A.I. and its separator if there's
     *        one.
     *
     * @return the bars of the bloc
     */
    protected byte[] getBarsCode(String bloc) {
        try {
            int taille = 0;
            String currentData = bloc;
            
            boolean hasSeparator = bloc.endsWith("" + FNC1);
            
            if (hasSeparator) {
                currentData = bloc.substring(0, bloc.length() - 1);
                taille++;
            }
            
            int index = 0;
            
            if ((currentData.length() % 2) != 0) {
                taille = taille + ((currentData.length() + 1) / 2) + 2;
                index = currentData.length() - 1;
            }
            else {
                taille = taille + (currentData.length() / 2);
                index = currentData.length();
            }
            
            byte[] bars = new byte[taille * 6];
            int[] buffer = new int[2];
            int k = 0;
            int i = 0;
            
            while ((i < index) && (currentData != null) && !"".equals(currentData)) {
                buffer[0] = Integer.parseInt("" + currentData.charAt(0));
                buffer[1] = Integer.parseInt("" + currentData.charAt(1));
                currentData = currentData.substring(2);
                
                int value = (buffer[0] * 10) + buffer[1];
                byte[] newBars = BARS[value];
                checksum = checksum + (ponderation * value);
                ponderation++;
                
                for (int j = 0; j < 6; j++) {
                    bars[k] = BARS[value][j];
                    k = k + 1;
                }
                i = i + 2;
            }
            
            if ((currentData != null) && (currentData.length() > 0)) //Cas impaire
            {
                char c = currentData.charAt(0);
                checksum = checksum + (ponderation * CODE_AC_TO_B);
                ponderation++;
                
                for (int j = 0; j < 6; j++) {
                    bars[k] = BARS[CODE_AC_TO_B][j];
                    k = k + 1;
                }
                
                int integer = codesB.get(c);
                
                for (int j = 0; j < 6; j++) {
                    bars[k] = BARS[integer][j];
                    k = k + 1;
                }
                checksum = checksum + (ponderation * integer);
                ponderation++;
                
                checksum = checksum + (ponderation * CODE_AB_TO_C);
                ponderation++;
                
                for (int j = 0; j < 6; j++) {
                    bars[k] = BARS[CODE_AB_TO_C][j];
                    k = k + 1;
                }
            }
            
            if (hasSeparator) {
                checksum = checksum + (ponderation * FNC1_INDEX);
                
                ponderation++;
                
                for (int j = 0; j < 6; j++) {
                    bars[k] = BARS[FNC1_INDEX][j];
                    k = k + 1;
                }
            }
            
            return bars;
        }
        catch (Exception t) {
            throw new ExceptionConverter(t);
        }
    }
    
    /**
     * Find the next bloc of data to be encoded.  The bloc is found by its A.I.
     * and then computed by its length or with FNC1 as separator.
     *
     * @param texte the <CODE>Data</CODE> to be encoded.
     * @param index the <CODE>Index</CODE> where the bloc starts.
     *
     * @return DOCUMENT ME!
     */
    protected String getBloc(String texte, int index) {
        String key = null;
        String bloc = null;
        Iterator iter = ids.keySet().iterator();
        String left = texte.substring(index);
        
        if ((left == null) || "".equals(left.trim())) {
            return bloc;
        }
        
        while ((key == null) && iter.hasNext()) {
            String code = (String)iter.next();
            
            if (left.startsWith(code)) {
                key = code;
                codeText = codeText + "(" + key + ")";
            }
        }
        
        IdentifierEAN id = (IdentifierEAN)ids.get(key);
        
        if (id != null) {
            if (id.isVariable()) {
                int endBloc = left.indexOf(FNC1);
                
                if (endBloc > 0) {
                    bloc = left.substring(0, endBloc + 1);
                    
                    if (key != null) {
                        codeText = codeText + bloc.substring(key.length(), endBloc);
                    }
                    else {
                        codeText = codeText + bloc.substring(0, endBloc);
                    }
                }
                else {
                    bloc = left;
                    
                    if (key != null) {
                        codeText = codeText + bloc.substring(key.length());
                    }
                    else {
                        codeText = codeText + bloc;
                    }
                }
            }
            else {
                bloc = texte.substring(index, index + id.getSize());
                
                if (key != null) {
                    codeText = codeText + bloc.substring(key.length());
                }
                else {
                    codeText = codeText + bloc;
                }
            }
        }
        else {
            /*logger.info("ID EST NULL");*/
        }
        
        return bloc;
    }
    
    /**
     * Put the bytes describing the bars of the barcode in a single array.
     *
     * @param barres The list of bars which compose the barcode.
     *
     * @return a byte array which describes all the bars in the barcode.
     */
    private byte[] getBarsArray(LinkedList barres) {
        Iterator iter = barres.iterator();
        int taille = 0;
        
        while (iter.hasNext()) {
            taille = taille + ((byte[])iter.next()).length;
        }
        
        byte[] out = new byte[taille];
        iter = barres.iterator();
        
        int k = 0;
        
        while (iter.hasNext()) {
            byte[] barre = (byte[])iter.next();
            
            for (int i = 0; i < barre.length; i++) {
                out[k] = barre[i];
                k++;
            }
        }
        
        return out;
    }
    
    /**
     * Gets the maximum area that the barcode and the text, if any, will occupy.
     * The lower left corner is always (0, 0).
     *
     * @return the size the barcode occupies.
     */
    public Rectangle getBarcodeSize() {
        float fontX = 0;
        float fontY = 0;
        String fullCode;
        
        if (font != null) {
            if (baseline > 0)
                fontY = baseline - font.getFontDescriptor(BaseFont.DESCENT, size);
            else
                fontY = -baseline + size;
            fullCode = code;
            
            if (codeType == CODE128_RAW) {
                int idx = code.indexOf('\uffff');
                
                if (idx < 0)
                    fullCode = "";
                else
                    fullCode = code.substring(idx + 1);
            }
            fontX = font.getWidthPoint(fullCode, size);
        }
        
        if (codeType == CODE128_RAW) {
            int idx = code.indexOf('\uffff');
            
            if (idx >= 0)
                fullCode = code.substring(0, idx);
            else
                fullCode = code;
        }
        else
            fullCode = (char) START_C + FNC1 + code ;
        
        int len = fullCode.length();
        float fullWidth = ((len + 2) * 11 * x) + (2 * x);
        fullWidth = Math.max(fullWidth, fontX);
        
        float fullHeight = barHeight + fontY;
        return new Rectangle(fullWidth, fullHeight);
    }
    
    public static class IdentifierEAN {
        
        /** Holds value of property variable. */
        private boolean variable;
        
        /** Holds value of property id. */
        private String id;
        
        /** Holds value of property format. */
        private String format;
        
        /** Holds value of property size. */
        private int size;
        
        /** Creates a new instance of IdentifierEAN */
        public IdentifierEAN(String id, String format, int size, boolean variable) {
            this.id = id;
            this.format = format;
            this.variable = variable;
            this.size = size;
        }
        
        /** Getter for property variable.
         * @return Value of property variable.
         *
         */
        public boolean isVariable() {
            return this.variable;
        }
        
        /** Setter for property variable.
         * @param vraible New value of property variable.
         *
         */
        public void setVariable(boolean variable) {
            this.variable = variable;
        }
        
        /** Getter for property id.
         * @return Value of property id.
         *
         */
        public String getId() {
            return this.id;
        }
        
        /** Setter for property id.
         * @param id New value of property id.
         *
         */
        public void setId(String id) {
            this.id = id;
        }
        
        /** Getter for property format.
         * @return Value of property format.
         *
         */
        public String getFormat() {
            return this.format;
        }
        
        /** Setter for property format.
         * @param format New value of property format.
         *
         */
        public void setFormat(String format) {
            this.format = format;
        }
        
        /** Getter for property size.
         * @return Value of property size.
         *
         */
        public int getSize() {
            return this.size;
        }
        
        /** Setter for property size.
         * @param size New value of property size.
         *
         */
        public void setSize(int size) {
            this.size = size;
        }
        
    }
}
