/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.css;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.tool.xml.CSSResolver;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;

/**
 * Resolves CSS properties.
 *
 * @author Balder Van Camp
 *
 */
public class StyleAttrCSSResolver implements CSSResolver {

	/**
	 *
	 */
	private static final String HTTP = "http";
	/**
	 *
	 */
	public static final String STYLE = "style";
	private final CssUtils utils;
	private CssInheritanceRules inherit;
	private final CssFiles cssFiles;

	/**
	 * Construct a new {@link StyleAttrCSSResolver} with default settings.
	 */
	public StyleAttrCSSResolver() {
		this(new CssFilesImpl(), CssUtils.getInstance());
	}

	/**
	 * Construct a new StyleAttrCSSResolver with the given {@link CssFiles} and the {@link DefaultCssInheritanceRules}.
	 *
	 * @param cssFiles a {@link CssFiles} implementation.
	 */
	public StyleAttrCSSResolver(final CssFiles cssFiles) {
		this(cssFiles, CssUtils.getInstance());

	}

	/**
	 * Construct a new StyleAttrCSSResolver with the given {@link CssFiles} and {@link CssUtils} and the
	 * {@link DefaultCssInheritanceRules}.
	 *
	 * @param cssFiles a {@link CssFiles} implementation.
	 * @param utils the CssUtils to use.
	 */
	public StyleAttrCSSResolver(final CssFiles cssFiles, final CssUtils utils) {
		this(new DefaultCssInheritanceRules(), cssFiles, utils);
	}

	/**
	 * Construct a new StyleAttrCSSResolver with the given {@link CssFiles} and {@link CssUtils}.
	 *
	 * @param rules the {@link CssInheritanceRules} to use.
	 * @param cssFiles a {@link CssFiles} implementation.
	 * @param utils the CssUtils to use.
	 */
	public StyleAttrCSSResolver(final CssInheritanceRules rules, final CssFiles cssFiles, final CssUtils utils) {
		this.utils = utils;
		this.cssFiles = cssFiles;
		this.inherit = rules;
	}

	/**
	 * Also taking into account the CSS properties of any parent tag in the given tag.
	 *
	 * @see com.itextpdf.tool.xml.CSSResolver#resolveStyles(com.itextpdf.tool.xml.Tag)
	 */
	public void resolveStyles(final Tag t) {
		// get css for this tag from resolver
		Map<String, String> tagCss = new HashMap<String, String>();
		if (null != cssFiles && cssFiles.hasFiles()) {
			tagCss = cssFiles.getCSS(t);
//			Map<String, String> css = cssFiles.getCSS(t);
//			if (null != css) {
//				for (Entry<String, String> entry : css.entrySet()) {
//					splitRules(tagCss,utils.stripDoubleSpacesAndTrim(entry.getKey()), utils.stripDoubleSpacesAndTrim(entry.getValue()));
//				}
//			}
		}
		// get css from style attr
		if (null != t.getAttributes() && !t.getAttributes().isEmpty()) {
			if (t.getAttributes().get(HTML.Attribute.CELLPADDING) != null) {
				tagCss.putAll(utils.parseBoxValues(t.getAttributes().get(HTML.Attribute.CELLPADDING), "cellpadding-", ""));
			}
			if (t.getAttributes().get(HTML.Attribute.CELLSPACING) != null) {
				tagCss.putAll(utils.parseBoxValues(t.getAttributes().get(HTML.Attribute.CELLSPACING), "cellspacing-", ""));
			}
			String styleAtt = t.getAttributes().get(HTML.Attribute.STYLE);
			if (null != styleAtt && styleAtt.length() > 0) {
				String[] styles = styleAtt.split(";");
				for (String s : styles) {
					String[] part = s.split(":",2);
					if (part.length == 2) {
						String key = part[0].trim();
						String value = utils.stripDoubleSpacesAndTrim(part[1]);
						splitRules(tagCss, key, value);
					}
				}
			}
		}
		// inherit css from parent tags, as defined in provided CssInheritanceRules or if property = inherit
		Map<String, String> css = t.getCSS();
		if (mustInherit(t.getTag()) && null != t.getParent() && null != t.getParent().getCSS()) {
			if (null != this.inherit) {
				for (Entry<String, String> entry : t.getParent().getCSS().entrySet()) {
					String key = entry.getKey();
					if ((tagCss.containsKey(key) && CSS.Value.INHERIT.equals(tagCss.get(key)) ) || canInherite(t, key)) {
						//splitRules(css, key, entry.getValue());
						css.put(key, entry.getValue());
					}
				}
			} else {
				css.putAll(t.getParent().getCSS());
			}
		}
		// overwrite properties (if value != inherit)
		for (Entry<String, String> e : tagCss.entrySet()) {
			if (!CSS.Value.INHERIT.equalsIgnoreCase(e.getValue())) {
				css.put(e.getKey(), e.getValue());
			}
		}

	}

	/**
	 * @param css the css map to populate
	 * @param key the property
	 * @param value the value
	 */
	private void splitRules(final Map<String, String> css, final String key, final String value) {
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

	/**
	 * By setting an implementation of {@link CssInheritanceRules} a developer can set rules on what css selectors are
	 * inherited from parent tags.
	 *
	 * @param cssInheritanceRules the inherit to set
	 */
	public void setCssInheritance(final CssInheritanceRules cssInheritanceRules) {
		this.inherit = cssInheritanceRules;
	}

	/**
	 * Defaults to true if no {@link CssInheritanceRules} implementation set.
	 *
	 * @param t
	 * @param property
	 * @return true if may be inherited false otherwise
	 */
	private boolean canInherite(final Tag t, final String property) {
		if (null != this.inherit) {
			return this.inherit.inheritCssSelector(t, property);
		}
		return true;
	}

	/**
	 * Defaults to true if no {@link CssInheritanceRules} implementation set.
	 *
	 * @param tag
	 * @return true if must be inherited false otherwise
	 */
	private boolean mustInherit(final String tag) {
		if (null != this.inherit) {
			return this.inherit.inheritCssTag(tag);
		}
		return true;
	}

	/**
	 * @param content
	 * @param charSet
	 * @throws CssResolverException
	 */
	public void addCss(final String content, final String charSet) throws CssResolverException {
		CssFileProcessor proc = new CssFileProcessor();
		FileRetrieveImpl retrieve = new FileRetrieveImpl();
		try {
			retrieve.processFromStream(new ByteArrayInputStream(content.getBytes(charSet)), proc);
			this.cssFiles.add(proc.getCss());
		} catch (UnsupportedEncodingException e) {
			throw new CssResolverException(e);
		} catch (IOException e) {
			throw new CssResolverException(e);
		}
	}

	/**
	 * Add a file to the CssFiles Collection.
	 *
	 * @param href the path, if it starts with http we try to retrieve the file from the net, if not we try a normal
	 *            file operation.
	 */
	public void addCssFile(final String href) throws CssResolverException {
		FileRetrieveImpl retrieve = new FileRetrieveImpl();
		CssFileProcessor cssFileProcessor = new CssFileProcessor();
		try {
			if (href.startsWith(HTTP)) {
				retrieve.processFromURL(new URL(href), cssFileProcessor);
			} else {
				retrieve.processFromFile(new File(href), cssFileProcessor);
			}
		} catch (MalformedURLException e) {
			throw new CssResolverException(e);
		} catch (IOException e) {
			throw new CssResolverException(e);
		}
		this.cssFiles.add(cssFileProcessor.getCss());
	}

	/**
	 * Add a file to the CssFiles Collection.
	 * @param file
	 */
	public void addCssFile(final CssFile file) {
		this.cssFiles.add(file);
	}
}
