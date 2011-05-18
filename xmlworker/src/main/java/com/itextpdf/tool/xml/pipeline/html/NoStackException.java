/**
 *
 */
package com.itextpdf.tool.xml.pipeline.html;

/**
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
	 * @param message
	 */
	public NoStackException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoStackException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoStackException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
