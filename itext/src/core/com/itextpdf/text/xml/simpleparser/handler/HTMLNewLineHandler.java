/**
 *
 */
package com.itextpdf.text.xml.simpleparser.handler;

import java.util.HashSet;
import java.util.Set;

import com.itextpdf.text.xml.simpleparser.NewLineHandler;

/**
 * This {@link NewLineHandler} returns true on the tags <code>p</code>,
 * <code>blockqoute</code>and <code>br</code>
 *
 * @author Balder
 *
 */
public class HTMLNewLineHandler implements NewLineHandler {

	private final Set<String> newLineTags = new HashSet<String>();

	/**
	 * Default constructor
	 *
	 * @since 5.0.6
	 */
	public HTMLNewLineHandler() {
		newLineTags.add("p");
		newLineTags.add("blockquote");
		newLineTags.add("br");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.text.xml.simpleparser.NewLineHandler#isNewLineTag(java.lang
	 * .String)
	 */
	public boolean isNewLineTag(final String tag) {
		return newLineTags.contains(tag);
	}

}
