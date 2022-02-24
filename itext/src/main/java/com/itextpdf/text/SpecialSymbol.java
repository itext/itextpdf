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
package com.itextpdf.text;

import com.itextpdf.text.Font.FontFamily;

/**
 * This class contains the symbols that correspond with special symbols.
 * <P>
 * When you construct a <CODE>Phrase</CODE> with Phrase.getInstance using a <CODE>String</CODE>,
 * this <CODE>String</CODE> can contain special Symbols. These are characters with an int value
 * between 913 and 937 (except 930) and between 945 and 969. With this class the value of the
 * corresponding character of the Font Symbol, can be retrieved.
 *
 * @see		Phrase
 *
 * @author  Bruno Lowagie
 * @author  Evelyne De Cordier
 */

public class SpecialSymbol {
    
	/**
	 * Returns the first occurrence of a special symbol in a <CODE>String</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 * @return	an index of -1 if no special symbol was found
	 */
    public static int index(String string) {
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (getCorrespondingSymbol(string.charAt(i)) != ' ') {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Gets a chunk with a symbol character.
     * @param c a character that has to be changed into a symbol
     * @param font Font if there is no SYMBOL character corresponding with c
     * @return a SYMBOL version of a character
     */
    public static Chunk get(char c, Font font) {
        char greek = SpecialSymbol.getCorrespondingSymbol(c);
        if (greek == ' ') {
            return new Chunk(String.valueOf(c), font);
        }
        Font symbol = new Font(FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
        String s = String.valueOf(greek);
        return new Chunk(s, symbol);
    }
    
    /**
     * Looks for the corresponding symbol in the font Symbol.
     *
     * @param	c	the original ASCII-char
     * @return	the corresponding symbol in font Symbol
     */
    public static char getCorrespondingSymbol(char c) {
        switch(c) {
            case 913:
                return 'A'; // ALFA
            case 914:
                return 'B'; // BETA
            case 915:
                return 'G'; // GAMMA
            case 916:
                return 'D'; // DELTA
            case 917:
                return 'E'; // EPSILON
            case 918:
                return 'Z'; // ZETA
            case 919:
                return 'H'; // ETA
            case 920:
                return 'Q'; // THETA
            case 921:
                return 'I'; // IOTA
            case 922:
                return 'K'; // KAPPA
            case 923:
                return 'L'; // LAMBDA
            case 924:
                return 'M'; // MU
            case 925:
                return 'N'; // NU
            case 926:
                return 'X'; // XI
            case 927:
                return 'O'; // OMICRON
            case 928:
                return 'P'; // PI
            case 929:
                return 'R'; // RHO
            case 931:
                return 'S'; // SIGMA
            case 932:
                return 'T'; // TAU
            case 933:
                return 'U'; // UPSILON
            case 934:
                return 'F'; // PHI
            case 935:
                return 'C'; // CHI
            case 936:
                return 'Y'; // PSI
            case 937:
                return 'W'; // OMEGA
            case 945:
                return 'a'; // alfa
            case 946:
                return 'b'; // beta
            case 947:
                return 'g'; // gamma
            case 948:
                return 'd'; // delta
            case 949:
                return 'e'; // epsilon
            case 950:
                return 'z'; // zeta
            case 951:
                return 'h'; // eta
            case 952:
                return 'q'; // theta
            case 953:
                return 'i'; // iota
            case 954:
                return 'k'; // kappa
            case 955:
                return 'l'; // lambda
            case 956:
                return 'm'; // mu
            case 957:
                return 'n'; // nu
            case 958:
                return 'x'; // xi
            case 959:
                return 'o'; // omicron
            case 960:
                return 'p'; // pi
            case 961:
                return 'r'; // rho
            case 962:
                return 'V'; // sigma
            case 963:
                return 's'; // sigma
            case 964:
                return 't'; // tau
            case 965:
                return 'u'; // upsilon
            case 966:
                return 'f'; // phi
            case 967:
                return 'c'; // chi
            case 968:
                return 'y'; // psi
            case 969:
                return 'w'; // omega
                default:
                    return ' ';
        }
    }
}
