/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Balder Van Camp, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.css.parser;

import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.parser.state.CommentEnd;
import com.itextpdf.tool.xml.css.parser.state.CommentInside;
import com.itextpdf.tool.xml.css.parser.state.CommentStart;
import com.itextpdf.tool.xml.css.parser.state.Properties;
import com.itextpdf.tool.xml.css.parser.state.Rule;
import com.itextpdf.tool.xml.css.parser.state.Unknown;

import java.util.HashMap;
import java.util.Map;

/**
 * State controller for CSS Processing
 * @author redlab_b
 *
 */
public class CssStateController {

	private State current;
	private State previous;
	private final State commentEnd;
	private final State commentStart;
	private final State commentInside;
	private final StringBuilder buffer;
	private final State properties;
	private final State unknown;
	private String currentSelector;
	private final State rule;
	private final CssUtils utils;
	private final CssFile css;

	/**
	 * @param file the CssFile
	 *
	 */
	public CssStateController(final CssFile file) {
		this.css = file;
		utils = CssUtils.getInstance();
		buffer = new StringBuilder();
		commentStart = new CommentStart(this);
		commentEnd = new CommentEnd(this);
		commentInside = new CommentInside(this);
		unknown = new Unknown(this);
		properties = new Properties(this);
		rule = new Rule(this);
		current = unknown;
	}

	/**
	 * Appends a character to the buffer.
	 * @param c the char to append to the buffer.
	 */
	public void append(final char c) {
		this.buffer.append(c);

	}

	/**
	 * Sets the state to the previous state
	 */
	public void previous() {
		this.current = previous;
	}

	/**
	 * Processes the character, delegates to states.
	 *
	 * @param c a character that needs to be processed
	 */
	public void process(final char c) {
		current.process(c);
	}

	private void processProps(final String props) {
		String[] split = props.split(";");
		Map<String, String> map = new HashMap<String, String>();
		for (String prop : split) {
			String[] propSplit = prop.split(":");
			if (propSplit.length == 2) {
				map.put(utils.stripDoubleSpacesTrimAndToLowerCase(propSplit[0]), utils.stripDoubleSpacesAndTrim(propSplit[1]));
			}
		}
		if (currentSelector.contains(",")) {
			String[] selectors = currentSelector.split(",");
            //check for rules like p, {…}
			for (int i = 0; i < selectors.length; i++) {
                selectors[i] = utils.stripDoubleSpacesAndTrim(selectors[i]);
                if (selectors[i].length() == 0)
                    return;
            }
            for (String selector : selectors) {
                //if any separated selector has errors, all others become invalid.
                //in this case we just clear map, it is the easies way to support this.
				if (!css.add(selector, map))
                    map.clear();
			}
		} else {
			css.add(utils.stripDoubleSpacesAndTrim(currentSelector), map);
		}
	}

	private void setState(final State state) {
		this.current = state;
	}

	private void setPrevious() {
		this.previous = current;
	}

	/**
	 *
	 */
	public void stateCommentEnd() {
		setState(commentEnd);
	}

	/**
	 *
	 */
	public void stateCommentInside() {
		setState(commentInside);
	}

	/**
	 *
	 */
	public void stateCommentStart() {
		setPrevious();
		setState(commentStart);
	}

	/**
	 *
	 */
	public void stateProperties() {
		previous = current;
		setState(properties);
	}

	/**
	 *
	 */
	public void stateUnknown() {
		setState(unknown);
	}

	/**
	 *
	 */
	public void stateRule() {
		setState(rule);
	}

	/**
	 *
	 */
	public void storeSelector() {
		this.currentSelector = buffer.toString();
		buffer.setLength(0);
	}

	/**
	 *
	 */
	public void storeProperties() {
		processProps(buffer.toString());
		buffer.setLength(0);
	}
}
