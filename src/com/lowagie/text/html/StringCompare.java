/*
 * @(#)StringCompare.java
 *
 * This class was used in the 1.1-version of iText (by Volker Richert).
 * Paulo Soares suggested I should add it to the original library, so
 * that in the future it would be easier to port it to the JDK1.1.x.
 * 
 * Copyright (c) 2000 Volker Richert
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

package com.lowagie.text.html;

import java.util.Comparator;

public class StringCompare implements Comparator {

	public int compare(Object o1, Object o2) {
		return compare((String)o1, (String)o2);
	}

	public int compare(String s1, String s2) {
		return s1.compareTo(s2);
	}

	public boolean equals(Object o) {
		return true;
	}
}
