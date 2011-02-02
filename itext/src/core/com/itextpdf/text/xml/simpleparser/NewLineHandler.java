/**
 *
 */
package com.itextpdf.text.xml.simpleparser;

/**
 * A NewLineHandler determines if an encountered tag should result in a new line
 * in a document.
 *
 * @author Balder
 * @since 5.0.6
 */
public interface NewLineHandler {

	/**
	 * @param tag the tag to check if after this one a new line should be in a document
	 * @return true in case a new line should be added.
	 * @since 5.0.6
	 */
	boolean isNewLineTag(String tag);

}
