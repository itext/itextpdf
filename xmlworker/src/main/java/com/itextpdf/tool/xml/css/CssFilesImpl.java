/*
 * $Id$
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2011 1T3XT BVBA Authors: Balder Van Camp, Emiel
 * Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License version 3 as published by the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.itextpdf.tool.xml.Tag;

/**
 * @author itextpdf.com
 *
 */
public class CssFilesImpl implements CssFiles {

	private final List<CssFile> files;
	private final CssUtils utils;
	private final CssSelector select;

	/**
     * Constructs a new CssFilesImpl.
     */
	public CssFilesImpl() {
		this.files = new ArrayList<CssFile>();
		this.utils = CssUtils.getInstance();
		this.select = new CssSelector();
	}

	/**
	 * Construct a new CssFilesImpl with the given css file.
	 * @param css the css file
	 */
	public CssFilesImpl(final CssFile css) {
		this();
		this.add(css);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.css.CssFiles#hasFiles()
	 */
	public boolean hasFiles() {
		return !this.files.isEmpty();
	}

	/**
	 * Processes a tag and retrieves CSS. Selectors created:
	 * <ul>
	 * <li>element</li>
	 * <li>element&gt;element (and a spaced version element &gt; element)</li>
	 * <li>#id</li>
	 * <li>.class</li>
	 * <li>element+element ( and a spaced version element + element)</li>
	 * </ul>
	 */
	public Map<String, String> getCSS(final Tag t) {
		Map<String, String> aggregatedProps = new HashMap<String, String>();
		Set<String> childSelectors = select.createAllSelectors(t);
		for (String selector : childSelectors) {
			populateCss(aggregatedProps, selector);
		}
		return aggregatedProps;
	}

	/**
	 * @param aggregatedProps the map to put the properties in.
	 * @param selector the selector to search for.
	 */
	public void populateCss(final Map<String, String> aggregatedProps, final String selector) {
		for (CssFile cssFile : this.files) {
			Map<String, String> t = cssFile.get(selector);
			Map<String, String> css = new HashMap<String, String>();
			for (Entry<String, String> e : t.entrySet()) {
				String key = utils.stripDoubleSpacesAndTrim(e.getKey());
				String value = utils.stripDoubleSpacesAndTrim(e.getValue());
				if (CSS.Property.BORDER.equalsIgnoreCase(key)) {
					css.putAll(utils.parseBorder(value));
				} else if (CSS.Property.MARGIN.equalsIgnoreCase(key)) {
					css.putAll(utils.parseBoxValues(value, "margin-", ""));
				} else if (CSS.Property.BORDER_WIDTH.equalsIgnoreCase(key)) {
					css.putAll(utils.parseBoxValues(value, "border-", "-width"));
				} else if (CSS.Property.BORDER_STYLE.equalsIgnoreCase(key)) {
					css.putAll(utils.parseBoxValues(value, "border-", "-style"));
				} else if (CSS.Property.BORDER_COLOR.equalsIgnoreCase(key)) {
					css.putAll(utils.parseBoxValues(value, "border-", "-color"));
				} else if (CSS.Property.PADDING.equalsIgnoreCase(key)) {
					css.putAll(utils.parseBoxValues(value, "padding-", ""));
				} else if (CSS.Property.FONT.equalsIgnoreCase(key)) {
					css.putAll(utils.processFont(value));
				} else if (CSS.Property.LIST_STYLE.equalsIgnoreCase(key)) {
					css.putAll(utils.processListStyle(value));
				} else {
					css.put(key, value);
				}
			}
			aggregatedProps.putAll(css);
		}
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.CssFiles#addFile(com.itextpdf.tool.xml.css.CssFile)
	 */
	public void add(final CssFile css) {
		this.files.add(css);
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.CssFiles#clear()
	 */
	public void clear() {
		Iterator<CssFile> iterator = files.iterator();
		while(iterator.hasNext()) {
			CssFile next = iterator.next();
			if (!next.isPersistent()) {
				iterator.remove();
			}
		}
	}

}
