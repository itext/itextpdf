/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Bruno Lowagie
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
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
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


/**
 * A barcode is a Chunk with a certain type of barcode font.
 * <P>
 * With this class you can construct several types of barcode
 * in different sizes, representing any 'product' or 'article' number.
 */

public class PdfBarcode extends com.lowagie.text.Chunk {
    
    /** This is a type of barcode. */
    public static final int CODE39 = 1;
    
    /** This is a type of barcode. */
    public static final int UPCA = 2;
    
    /** This is a type of barcode. */
    public static final int EAN13 = 3;
    
    /** This is a type of barcode. */
    public static final int INTERLEAVED_2_OF_5 = 4;
    
    /** The variable parity table in EAN 13 */
    public static final int[][] variableParity =
    { {0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2},
    {0, 0, 1, 0, 1, 1, 2, 2, 2, 2, 2, 2},
    {0, 0, 1, 1, 0, 1, 2, 2, 2, 2, 2, 2},
    {0, 0, 1, 1, 1, 0, 2, 2, 2, 2, 2, 2},
    {0, 1, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2},
    {0, 1, 1, 0, 0, 1, 2, 2, 2, 2, 2, 2},
    {0, 1, 1, 1, 0, 0, 2, 2, 2, 2, 2, 2},
    {0, 1, 0, 1, 0, 1, 2, 2, 2, 2, 2, 2},
    {0, 1, 0, 1, 1, 0, 2, 2, 2, 2, 2, 2},
    {0, 1, 1, 0, 1, 0, 2, 2, 2, 2, 2, 2} };
    
    /**
     * Creates a new Barcode.
     *
     * @param   ttf     the ttf file representing the barcode font
     * @param   size    the size of the barcode
     * @param   number  the number you want to convert to a barcode in String format
     */
    
    public PdfBarcode(String ttf, int type, int size, String number) throws com.lowagie.text.BadElementException, com.lowagie.text.DocumentException, java.io.IOException {
        super(convertToCode(type, number), new com.lowagie.text.Font(BaseFont.createFont(ttf, "winansi", true), size));
    }
    
    /**
     * Creates a new Barcode.
     *
     * @param   ttf     the ttf file representing the barcode font
     * @param   size    the size of the barcode
     * @param   number  the number you want to convert to a barcode in long format
     */
    
    public PdfBarcode(String ttf, int type, int size, long number) throws com.lowagie.text.BadElementException, com.lowagie.text.DocumentException, java.io.IOException {
        super(convertToCode(type, String.valueOf(number)), new com.lowagie.text.Font(BaseFont.createFont(ttf, "winansi", true), size));
    }
    
    /**
     * Converts a String representing a number to a barcode with a specific barcode font.
     *
     * @param   type    the type of barcode
     * @param   number  the number you want to convert to a barcode in long format
     */
    
    private static String convertToCode(int type, String number) throws com.lowagie.text.BadElementException {
        StringBuffer code = new StringBuffer();
        int length = number.length();
        int digit;
        int pos = 0;
        try {
            switch(type) {
                case CODE39:
                    code.append('*');
                    while (pos < length) {
                        code.append(number.substring(pos, ++pos));
                    }
                    code.append('*');
                    break;
                case UPCA:
                    if (length > 12) throw new com.lowagie.text.BadElementException("An UPC-A barcode can only encode a 12 digit number (your number was " + number + ").");
                    number = addZero(number, 12);
                    digit = Integer.parseInt(number.substring(pos, ++pos));
                    code.append((char) (digit + 80));
                    while (pos < 6) {
                        digit = Integer.parseInt(number.substring(pos, ++pos));
                        code.append((char) (digit + 48));
                    }
                    code.append((char) 112);
                    while (pos < 11) {
                        digit = Integer.parseInt(number.substring(pos, ++pos));
                        code.append((char) (digit + 64));
                    }
                    digit = Integer.parseInt(number.substring(11));
                    code.append((char) (digit + 96));
                    break;
                case EAN13:
                    if (length > 13) throw new com.lowagie.text.BadElementException("An EAN-13 barcode can only encode a 13 digit number (your number was " + number + ").");
                    number = addZero(number, 13);
                    int firstdigit = Integer.parseInt(number.substring(pos, ++pos));
                    code.append((char) (firstdigit + 33));
                    digit = Integer.parseInt(number.substring(pos, ++pos));
                    code.append((char) (digit + 96));
                    while (pos < 7) {
                        digit = Integer.parseInt(number.substring(pos, ++pos));
                        code.append((char) (digit + 48 + 16 * variableParity[firstdigit][pos - 2]));
                    }
                    code.append((char) 124);
                    while (pos < 12) {
                        digit = Integer.parseInt(number.substring(pos, ++pos));
                        code.append((char) (digit + 48 + 16 * variableParity[firstdigit][pos - 2]));
                    }
                    digit = Integer.parseInt(number.substring(12));
                    code.append((char) (digit + 112));
                    break;
                case INTERLEAVED_2_OF_5:
                    if (length % 2 == 1) {
                        number = addZero(number, length + 1);
                    }
                    code.append('(');
                    while (number.length() > 2) {
                        digit = Integer.parseInt(number.substring(0, 2));
                        code.append(convertInterleaved(digit));
                        pos += 2;
                        number = number.substring(2);
                    }
                    digit = Integer.parseInt(number);
                    code.append(convertInterleaved(digit));
                    code.append(')');
                    break;
                default:
                    throw new com.lowagie.text.BadElementException("This type of barcode is not supported yet: " + type);
            }
        }
        catch(NumberFormatException nfe) {
            throw new com.lowagie.text.BadElementException("NumberFormatException at position " + pos + " in " + number + ": " + nfe.getMessage());
        }
        return code.toString();
    }
    
    /**
     * Converts an int to a convert interleaved character.
     */
    
    private static char convertInterleaved(int digits) throws NumberFormatException {
        int i;
        if (digits < 50) {
            i = 48;
        }
        else if (digits < 100) {
            i = 142;
        }
        else {
            throw new NumberFormatException(String.valueOf(digits));
        }
        return (char)(digits + i);
    }
    
    /**
     * Adds leading zeros.
     */
    
    private static String addZero(String number, int length) {
        StringBuffer buf = new StringBuffer();
        int zeros = length - number.length();
        for (int i = 0; i < zeros; i++) {
            buf.append('0');
        }
        buf.append(number);
        return buf.toString();
    }
}