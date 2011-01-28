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
import java.util.List;
import java.util.Map;

import com.itextpdf.text.ElementTags;

/**
 * Stores the hierarchy of tags along with the properties of each tag.
 */
public class ChainedProperties {

	/**
	 * A series of predefined font sizes.
	 * @since 5.0.6 (renamed)
	 */
	public final static int FONTSIZES[] = { 8, 10, 12, 14, 18, 24, 36 };

	/**
	 * Class that stores the info about one tag in the chain.
	 */
	private static final class ChainedProperty {
		/** A possible tag */
	    final String tag;
	    /** The styles corresponding with the tag */
	    final Map<String, String> attrs;
	    /**
	     * Constructs a chained property.
	     * @param	tag		an XML/HTML tag
	     * @param	attrs	the tag's attributes
	     */
	    ChainedProperty(String tag, Map<String, String> attrs) {
	    	this.tag = tag;
	    	this.attrs = attrs;
	    }
	}

	/** A list of chained properties representing the tag hierarchy. */
	public List<ChainedProperty> chain = new ArrayList<ChainedProperty>();

	/** Creates a new instance of ChainedProperties */
	public ChainedProperties() {
	}

	/**
	 * Walks through the hierarchy (bottom-up) looking for
	 * a property key. Returns a value as soon as a match
	 * is found or null if the key can't be found.
	 * @param	key	the key of the property
	 * @return	the value of the property
	 */
	public String getProperty(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
			ChainedProperty p = chain.get(k);
			Map<String, String> attrs = p.attrs;
			String ret = attrs.get(key);
			if (ret != null)
				return ret;
		}
		return null;
	}
	
	/**
	 * Walks through the hierarchy (bottom-up) looking for
	 * a property key. Returns true as soon as a match is
	 * found or false if the key can't be found.
	 * @param	key	the key of the property
	 * @return	true if the key is found
	 */
	public boolean hasProperty(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
			ChainedProperty p = chain.get(k);
			Map<String, String> attrs = p.attrs;
			if (attrs.containsKey(key))
				return true;
		}
		return false;
	}

	/**
	 * Adds a tag and its corresponding properties to the chain.
	 * @param tag	the tags that needs to be added to the chain
	 * @param attrs	the tag's attributes
	 */
	public void addToChain(String tag, HashMap<String, String> attrs) {
		this.adjustFontSize(attrs);
		chain.add(new ChainedProperty(tag, attrs));
	}

	/**
	 * Walks through the hierarchy (bottom-up) and removes the
	 * first occurrence of a tag that is encountered.
	 * @param	tag	the tag that needs to be removed
	 */
	public void removeChain(String tag) {
		for (int k = chain.size() - 1; k >= 0; --k) {
			if (tag.equals(chain.get(k).tag)) {
				chain.remove(k);
				return;
			}
		}
	}
	
	/**
	 * If the properties contain a font size, the size may need to
	 * be adjusted based on font sizes higher in the hierarchy.
	 * @param	attrs the attributes that may have to be updated
	 * @since 5.0.6 (renamed)
	 */
	protected void adjustFontSize(HashMap<String, String> attrs) {
		// fetch the font size
		String value = attrs.get(ElementTags.SIZE);
		// do nothing if the font size isn't defined
		if (value == null)
			return;
		// the font is defined as a real size: remove "pt"
		if (value.endsWith("pt")) {
			attrs.put(ElementTags.SIZE,
				value.substring(0, value.length() - 2));
			return;
		}
		// the font is expressed as an index in a series of predefined font sizes
		int sIndex = 0;
		// the font is defined as a relative size
		if (value.startsWith("+") || value.startsWith("-")) {
			// fetch the previous value
			String old = getProperty("basefontsize");
			if (old == null)
				old = "12";
			int c = (int)Float.parseFloat(old);
			// look for the nearest font size in the predefined series
			for (int k = FONTSIZES.length - 1; k >= 0; --k) {
				if (c >= FONTSIZES[k]) {
					sIndex = k;
					break;
				}
			}
			// retrieve the difference
			int diff =
				Integer.parseInt(value.startsWith("+") ?
					value.substring(1) : value);
			// apply the difference
			sIndex += diff;
		}
		// the font is defined as an index
		else {
			try {
				sIndex = Integer.parseInt(value) - 1;
			} catch (NumberFormatException nfe) {
				sIndex = 0;
			}
		}
		if (sIndex < 0)
			sIndex = 0;
		else if (sIndex >= FONTSIZES.length)
			sIndex = FONTSIZES.length - 1;
		attrs.put(ElementTags.SIZE, Integer.toString(FONTSIZES[sIndex]));
	}
}
