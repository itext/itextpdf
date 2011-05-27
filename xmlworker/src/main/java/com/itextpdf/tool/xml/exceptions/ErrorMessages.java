/**
 *
 */
package com.itextpdf.tool.xml.exceptions;

import java.util.ListResourceBundle;

/**
 * @author Emiel Ackermann
 *
 */
public class ErrorMessages extends ListResourceBundle {

	public static final String UNSUPPORTED_CHARSET = "UNSUPPORTED_CHARSET";
	public static final String INVALID_NESTED_TAG = "INVALID_NESTED_TAG";
	public static final String NO_CUSTOM_CONTEXT = "NO_CUSTOM_CONTEXT";
	public static final String UNSUPPORTED_CLONING = "UNSUPPORTED_CLONING";
	public static final String NO_TAGPROCESSOR = "NO_TAGPROCESSOR";
	public static final String NO_SIBLING = "NO_SIBLING";
	private static ErrorMessages myself;

	public static synchronized ErrorMessages getInstance() {
		if(null == myself) {
			myself = new ErrorMessages();
		}
		return myself;
	}

	/* (non-Javadoc)
	 * @see java.util.ListResourceBundle#getContents()
	 */
	@Override
	public Object[][] getContents() {
		return contents;
	}

	private static Object[][] contents = {
	    { UNSUPPORTED_CHARSET, "detected charset is not supported" },
	    { INVALID_NESTED_TAG, "Invalid nested tag %s found, expected closing tag %s" },
	    { NO_CUSTOM_CONTEXT, "No custom context found" },
	    { UNSUPPORTED_CLONING, "Cloning of %s not supported" },
	    { NO_TAGPROCESSOR, "No tagProcessor found for %s" },
	    { NO_SIBLING, "No sibling found for %s on relative position %d" }
	};
}
