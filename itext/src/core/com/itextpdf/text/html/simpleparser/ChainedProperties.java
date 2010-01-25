/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.html.simpleparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.itextpdf.text.ElementTags;

public class ChainedProperties {

	public final static int fontSizes[] = { 8, 10, 12, 14, 18, 24, 36 };

	private static final class ChainedProperty {
	    final String key;
	    final HashMap<String, String> property;
	    ChainedProperty(String key, HashMap<String, String> property) {
	    this.key = key;
	    this.property = property;
	    }
	}

	public ArrayList<ChainedProperty> chain = new ArrayList<ChainedProperty>();

	/** Creates a new instance of ChainedProperties */
	public ChainedProperties() {
	}

	public String getProperty(String key) {
    		for (int k = chain.size() - 1; k >= 0; --k) {
                        ChainedProperty p = chain.get(k);
                        HashMap<String, String> prop = p.property;
                        String ret = prop.get(key);
			if (ret != null)
				return ret;
		}
		return null;
	}

	public boolean hasProperty(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
		        ChainedProperty p = chain.get(k);
                        HashMap<String, String> prop = p.property;
			if (prop.containsKey(key))
				return true;
		}
		return false;
	}

	public void addToChain(String key, HashMap<String, String> prop) {
		// adjust the font size
		String value = prop.get(ElementTags.SIZE);
		if (value != null) {
			if (value.endsWith("pt")) {
				prop.put(ElementTags.SIZE, value.substring(0,
						value.length() - 2));
			} else {
				int s = 0;
				if (value.startsWith("+") || value.startsWith("-")) {
					String old = getProperty("basefontsize");
					if (old == null)
						old = "12";
					float f = Float.parseFloat(old);
					int c = (int) f;
					for (int k = fontSizes.length - 1; k >= 0; --k) {
						if (c >= fontSizes[k]) {
							s = k;
							break;
						}
					}
					int inc = Integer.parseInt(value.startsWith("+") ? value
							.substring(1) : value);
					s += inc;
				} else {
					try {
						s = Integer.parseInt(value) - 1;
					} catch (NumberFormatException nfe) {
						s = 0;
					}
				}
				if (s < 0)
					s = 0;
				else if (s >= fontSizes.length)
					s = fontSizes.length - 1;
				prop.put(ElementTags.SIZE, Integer.toString(fontSizes[s]));
			}
		}
		chain.add(new ChainedProperty(key, prop));
	}

	public void removeChain(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
			if (key.equals(chain.get(k).key)) {
				chain.remove(k);
				return;
			}
		}
	}
}
