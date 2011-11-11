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
	 * @param message
	 */
	public NoDataException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoDataException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoDataException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
