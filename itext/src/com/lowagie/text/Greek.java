/*
 * @(#)Greek.java					0.40 2000/12/01
 *       release iText0.40			0.40 2000/??/??
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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

package com.lowagie.text;

/**
 * This class contains the symbols that correspond with Greek letters.
 * <P>
 * When you construct a <CODE>Phrase</CODE> (or a derived object) using a <CODE>String</CODE>,
 * this <CODE>String</CODE> can contain Greek Symbols. These are characters with an int value
 * between 913 and 937 (except 930) and between 945 and 969. With this class the value of the
 * corresponding character of the Font Symbol, can be retrieved.
 *
 * @see		Phrase
 *
 * @author  bruno@lowagie.com
 * @author  Evelyne De Cordier
 * @version 0.40, 2000/12/01
 *
 * @since   iText0.40
 */

public class Greek {

	/**
	 * Returns the first occurrence of a Greek symbol in a <CODE>String</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 * @return	an index of -1 if no Greek symbol was found
	 *
	 * @since	iText0.40 
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
	 * Looks for the corresponding symbol in the font Symbol.
	 *
	 * @param	c	the original ASCII-char
	 * @return	the corresponding symbol in font Symbol
	 *
	 * @since	iText0.40
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
			return 'J'; // PHI
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
			return 's'; // sigma
		case 963:
			return 's'; // sigma
		case 964:
			return 't'; // tau
		case 965:
			return 'u'; // upsilon
		case 966:
			return 'j'; // phi
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