/**
 *
 */
package com.itextpdf.tool.xml.exceptions;

/**
 * Thrown when something that needs data to function does not have that data.
 * @author redlab_b
 *
 */
public class NoDataException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public NoDataException() {
	}

	/**
	 * @param message the message
	 */
	public NoDataException(final String message) {
		super(message);
	}

	/**
	 * @param cause a cause
	 */
	public NoDataException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public NoDataException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
