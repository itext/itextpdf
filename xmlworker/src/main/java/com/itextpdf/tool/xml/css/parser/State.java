package com.itextpdf.tool.xml.css.parser;

/**
 * @author redlab_b
 *
 */
public interface State {

	/**
	 * Processes a character.
	 * @param c the character to process
	 */
	public void process(final char c);
}
