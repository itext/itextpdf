package com.itextpdf.tool.xml.parser;

/**
 * @author redlab_b
 *
 */
public enum TagState {
	/**
	 * Notice that an opening tag has been encountered.
	 */
	OPEN,
	/**
	 * Notice that a closing tag has been encountered.
	 */
	CLOSE,
	/**
	 * Notice that no opening or closing tag has been encountered.
	 */
	NONE;
}
