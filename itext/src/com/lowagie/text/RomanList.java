/*
 * Copyright 2003 by Michael Niedermair.
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
package com.lowagie.text;

/**
 * 
 * A special-version of <CODE>LIST</CODE> whitch use roman-letters.
 * 
 * @see com.lowagie.text.List
 * @version 2003-06-22
 * @author Michael Niedermair
 */

public class RomanList extends List {

	/**
	 * UpperCase or LowerCase
	 */
	protected boolean romanlower;

	/**
	 * Initialisierung
	 * 
	 * @param symbolIndent	indent
	 */
	public RomanList(int symbolIndent) {
		super(true, symbolIndent);
	}

	/**
	 * Initialisierung 
	 * @param	romanlower		roman-char in lowercase   
	 * @param 	symbolIndent	indent
	 */
	public RomanList(boolean romanlower, int symbolIndent) {
		super(true, symbolIndent);
		this.romanlower = romanlower;
	}

	/**
	 * set the roman-letters to lowercase otherwise to uppercase
	 * 
	 * @param romanlower
	 */
	public void setRomanLower(boolean romanlower) {
		this.romanlower = romanlower;
	}

	/**
	 * Checks if the list is roman-letter with lowercase
	 *
	 * @return	<CODE>true</CODE> if the roman-letter is lowercase, <CODE>false</CODE> otherwise.
	 */
	public boolean isRomanLower() {
		return romanlower;
	}

	/**
	 * Adds an <CODE>Object</CODE> to the <CODE>List</CODE>.
	 *
	 * @param	o	the object to add.
	 */
	public boolean add(Object o) {
		if (o instanceof ListItem) {
			ListItem item = (ListItem) o;
			Chunk chunk;
			if (romanlower)
				chunk = new Chunk(toRomanLowerCase(first + list.size()), symbol.font());
			else
				chunk = new Chunk(toRomanUppercase(first + list.size()), symbol.font());
			chunk.append(".");
			item.setListSymbol(chunk);
			item.setIndentationLeft(symbolIndent);
			item.setIndentationRight(0);
			list.add(item);
		} else if (o instanceof List) {
			List nested = (List) o;
			nested.setIndentationLeft(nested.indentationLeft() + symbolIndent);
			first--;
			return list.add(nested);
		} else if (o instanceof String) {
			return this.add(new ListItem((String) o));
		}
		return false;
	}

	// ****************************************************************************************

	/*
	 * Wandelt eine Integer-Zahl in römische Schreibweise um
	 *
	 * Regeln: http://de.wikipedia.org/wiki/R%F6mische_Ziffern
	 *  
	 * 1. Die Ziffern werden addiert, wobei sie von groß nach klein sortiert sind:
	 *
	 *  XVII = 10+5+1+1=17 
	 *
	 * 2. Eine kleinere Ziffer, die links von einer größeren steht, wird abgezogen:
	 * 
	 *  IV = 5-1=4 
	 *  CM = 1000-100=900 
	 *
	 * 3. Maximal drei gleiche Ziffern stehen nebeneinander (Ausnahme: IIII auf Zifferblaettern von Uhren):
	 * 
	 *  XXX = 30 
	 *  XL = 40 (und nicht XXXX) 
	 *  IX = 9 (und nicht VIIII) 
	 *  Diese "Subtraktionsschreibweise" ist erst im Mittelalter allgemein gebräuchlich geworden. 
	 *  Vorher wurde oft "IIII" für "4" geshrieben. 
	 *
	 * 4. Bei mehreren möglichen Schreibweisen wird in der Regel der kürzesten der Vorzug gegeben:
	 *
	 *  IC = 99 (auch LXLIX) 
	 *  IL = 49 (auch XLIX oder sogar XLVIV) 
	 *  Andererseits gibt es die Vorschrift, nach der ein Symbol, das einen Wert von 10n darstellt, 
	 *  nicht einem Symbol, das einen Wert von 10(n+1) darstellt, direkt voranstehen darf. 
	 *  Nach dieser Regel wäre die Schreibweise "XCIX" für "99" der Schreibweise "IC" vorzuziehen. 
	 *
	 * 5. Die römischen Zahlen V, L und D können nicht größeren Zahlen voran gestellt werden:
	 *
	 *  XCV = 95 (nicht VC) 
	 * 
	 *  Zahlen über 3000 werden dargestellt durch Einkastung der Tausender: |IX|LIV=9054
	 * 
	 *
	 * Zahlen größer als 3.000.000 werden durch Doppelstrich etc. dargestellt.
	 */

	/**
	 * Array mit römischen Zeichen und deren Wert
	 */
	private static final RomanDigit[] roman =
		{
			new RomanDigit('m', 1000, false),
			new RomanDigit('d', 500, false),
			new RomanDigit('c', 100, true),
			new RomanDigit('l', 50, false),
			new RomanDigit('x', 10, true),
			new RomanDigit('v', 5, false),
			new RomanDigit('i', 1, true)};

	/** 
	 * Wandelt eine Integerzahl in römische Kleinbuchstaben um.
	 */
	public static String toRoman(int number) {
		return toRomanLowerCase(number);
	}

	/** 
	 * Wandelt eine Integerzahl in römische Großbuchstaben um.
	 */
	public static String toRomanUppercase(int number) {
		return toRomanLowerCase(number).toUpperCase();
	}

	/** 
	 * Wandelt eine Integerzahl in römische Kleinbuchstaben um.
	 */
	public static String toRomanLowerCase(int number) {

		// Buffer
		StringBuffer buf = new StringBuffer();

		// kleiner 0 ? Vorzeichen festlegen
		if (number < 0) {
			buf.append('-');
			number = -number;
		}

		// größer 3000
		if (number > 3000) {
			// rekursiver Aufruf (ohne tausender-Bereich)
			buf.append('|');
			buf.append(toRomanLowerCase(number / 1000));
			buf.append('|');
			// tausender-Bereich 
			number = number - (number / 1000) * 1000;
		}

		// Schleife
		int pos = 0;
		while (true) {
			// roman-array durchlaufen
			RomanDigit dig = roman[pos];

			// solange Zahl größer roman-Wert
			while (number >= dig.value) {
				// Zeichen hinzufügen
				buf.append(dig.digit);
				// Wert des Zeichens abziehen
				number -= dig.value;
			}

			// Abbruch
			if (number <= 0) {
				break;
			}
			// pre=false suchen (ab Stelle pos)
			int j = pos;
			while (!roman[++j].pre);

			// neuer Wert größer
			if (number + roman[j].value >= dig.value) {
				// hinzufügen
				buf.append(roman[j].digit).append(dig.digit);
				// Wert vom Rest abziehen
				number -= dig.value - roman[j].value;
			}
			pos++;
		}
		return buf.toString();
	}

	/**
	 * Hilfsklasse für römische Zeichen
	 */
	private static class RomanDigit {

		// Zeichen
		public char digit;

		// Wert
		public int value;

		// kann Vorangestellt werden
		public boolean pre;

		// Initialisierung
		RomanDigit(char digit, int value, boolean pre) {
			this.digit = digit;
			this.value = value;
			this.pre = pre;
		}
	}

	// Test
	//	public static void main(String[] args) throws Exception {
	//
	//		for (int i = -1; i <= 20; i++) {
	//			System.out.println(i + " = " + toRoman(i));
	//		}
	//		System.out.println("49   = " + toRoman(49));
	//		System.out.println("50   = " + toRoman(50));
	//		System.out.println("51   = " + toRoman(51));
	//		System.out.println("99   = " + toRoman(99));
	//		System.out.println("100  = " + toRoman(100));
	//		System.out.println("101  = " + toRoman(101));
	//		System.out.println("499  = " + toRoman(499));
	//		System.out.println("500  = " + toRoman(500));
	//		System.out.println("501  = " + toRoman(501));
	//		System.out.println("999  = " + toRoman(999));
	//		System.out.println("1000 = " + toRoman(1000));
	//		System.out.println("1001 = " + toRoman(1001));
	//		System.out.println("2999 = " + toRoman(2999));
	//		System.out.println("3000 = " + toRoman(3000));
	//		System.out.println("3001 = " + toRoman(3001));
	//		System.out.println("9054 = " + toRoman(9054));
	//		System.out.println("99999  = " + toRoman(99999));
	//		System.out.println("100000 = " + toRoman(100000));
	//		System.out.println("109054 = " + toRoman(109054));
	//		System.out.println("3109054 = " + toRoman(3109054));
	//	}

}
