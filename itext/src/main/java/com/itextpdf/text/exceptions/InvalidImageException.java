package com.itextpdf.text.exceptions;

/**
 * RuntimeException to indicate that the provided Image is invalid/corrupted.
 * Should only be thrown/not caught when ignoring invalid images.
 * @since 5.4.2
 */
public class InvalidImageException extends RuntimeException {

    /** a serial version UID */
    private static final long serialVersionUID = -1319471492541702697L;
    private final Throwable cause;

    /**
     * Creates an instance with a message and no cause
     * @param	message	the reason why the document isn't a PDF document according to iText.
     */
    public InvalidImageException(final String message) {
        this(message, null);
    }

    /**
     * Creates an exception with a message and a cause
     * @param message	the reason why the document isn't a PDF document according to iText.
     * @param cause the cause of the exception, if any
     */
    public InvalidImageException(String message, Throwable cause){
        super(message);
        this.cause = cause;
    }

    /**
     * This method is included (instead of using super(message, cause) in the constructors) to support backwards compatabilty with
     * JDK 1.5, which did not have cause constructors for Throwable
     * @return the cause of this exception
     */
    public Throwable getCause() {
        return cause;
    }
}
