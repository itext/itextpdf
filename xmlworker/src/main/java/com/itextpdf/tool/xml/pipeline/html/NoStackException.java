/**
 *
 */
package com.itextpdf.tool.xml.pipeline.html;

/**
 * Thrown when a StackKeeper was expected but could not be retrieved.
 * @author itextpdf.com
 *
 */
public class NoStackException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public NoStackException() {
	}

	/**
	 * @param message a message
	 */
	public NoStackException(final String message) {
		super(message);
	}

	/**
	 * @param cause a cause
	 */
	public NoStackException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message a message
	 * @param cause a cause
	 */
	public NoStackException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
