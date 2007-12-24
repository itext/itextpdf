package com.lowagie.text.pdf;

import java.io.IOException;

public class BadPasswordException extends IOException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -4333706268155063964L;

	/**
	 * Creates an exception saying the user password was incorrect.
	 */
	public BadPasswordException() {
		super("Bad user Password");
	}
}
