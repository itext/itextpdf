/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import com.lowagie.text.pdf.PRTokeniser;

/**
 * A collection of convenience methods that were present in many different iText
 * classes.
 */

public class Utilities {

	/**
	 * Gets the keys of a Hashtable
	 * 
	 * @param table
	 *            a Hashtable
	 * @return the keyset of a Hashtable (or an empty set if table is null)
	 */
	public static Set getKeySet(Hashtable table) {
		return (table == null) ? Collections.EMPTY_SET : table.keySet();
	}

	/**
	 * Utility method to extend an array.
	 * 
	 * @param original
	 *            the original array or <CODE>null</CODE>
	 * @param item
	 *            the item to be added to the array
	 * @return a new array with the item appended
	 */
	public static Object[][] addToArray(Object original[][], Object item[]) {
		if (original == null) {
			original = new Object[1][];
			original[0] = item;
			return original;
		} else {
			Object original2[][] = new Object[original.length + 1][];
			System.arraycopy(original, 0, original2, 0, original.length);
			original2[original.length] = item;
			return original2;
		}
	}

	/**
	 * Checks for a true/false value of a key in a Properties object.
	 * @param attributes
	 * @param key
	 * @return
	 */
	public static boolean checkTrueOrFalse(Properties attributes, String key) {
		return "true".equalsIgnoreCase(attributes.getProperty(key));
	}

	/**
	 * Unescapes an URL. All the "%xx" are replaced by the 'xx' hex char value.
	 * @param src the url to unescape
	 * @return the eunescaped value
	 */    
	public static String unEscapeURL(String src) {
	    StringBuffer bf = new StringBuffer();
	    char[] s = src.toCharArray();
	    for (int k = 0; k < s.length; ++k) {
	        char c = s[k];
	        if (c == '%') {
	            if (k + 2 >= s.length) {
	                bf.append(c);
	                continue;
	            }
	            int a0 = PRTokeniser.getHex((int)s[k + 1]);
	            int a1 = PRTokeniser.getHex((int)s[k + 2]);
	            if (a0 < 0 || a1 < 0) {
	                bf.append(c);
	                continue;
	            }
	            bf.append((char)(a0 * 16 + a1));
	            k += 2;
	        }
	        else
	            bf.append(c);
	    }
	    return bf.toString();
	}

	private static String[] excUriEsc = {"%20", "%3C", "%3E", "%23", "%25", "%22", "%7B", "%7D", "%5B", "%5D", "%7C", "%5C", "%5E", "%60"};
	private static String excUri = " <>#%\"{}[]|\\\u005E\u0060";
	/**
	 * This method makes a valid URL from a given filename.
	 * <P>
	 * This method makes the conversion of this library from the JAVA 2 platform
	 * to a JDK1.1.x-version easier.
	 * 
	 * @param filename
	 *            a given filename
	 * @return a valid URL
	 * @throws MalformedURLException
	 */
	public static URL toURL(String filename) throws MalformedURLException {
		if (filename.startsWith("file:/") || filename.startsWith("http://")
				|| filename.startsWith("https://")
				|| filename.startsWith("jar:")) {
			return new URL(filename);
		}
		File f = new File(filename);
		String path = f.getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.endsWith("/") && f.isDirectory()) {
			path = path + "/";
		}
	    char[] t = path.toCharArray();
	    StringBuffer sb = new StringBuffer();
	    for (int k = 0; k < t.length; ++k) {
	        char c = t[k];
	        int a = Utilities.excUri.indexOf(c);
	        if (a >= 0)
	            sb.append(Utilities.excUriEsc[a]);
	        else
	            sb.append(c);
	    }
		return new URL("file", "", sb.toString());
	}

	/**
	 * This method is an alternative for the <CODE>InputStream.skip()</CODE>
	 * -method that doesn't seem to work properly for big values of <CODE>size
	 * </CODE>.
	 * 
	 * @param is
	 *            the <CODE>InputStream</CODE>
	 * @param size
	 *            the number of bytes to skip
	 * @throws IOException
	 */
	static public void skip(InputStream is, int size) throws IOException {
	    long n;
		while (size > 0) {
	        n = is.skip(size);
	        if (n <= 0)
	            break;
			size -= n;
		}
	}

}
