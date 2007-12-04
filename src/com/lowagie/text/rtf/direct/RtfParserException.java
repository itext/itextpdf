package com.lowagie.text.rtf.direct;

/**
 * Signals that an error has occurred in a <CODE>RtfParser2</CODE>.
 */

public class RtfParserException extends Exception {
	private static final long serialVersionUID = 2857489935812968235L;
	private Exception ex;

    /**
     * Creates a Document exception.
     * @param ex an exception that has to be turned into a RtfParserException
     */
    public RtfParserException(Exception ex) {
        this.ex = ex;
    }
    
    // constructors
    
    /**
     * Constructs a <CODE>RtfParserException</CODE> whithout a message.
     */
    public RtfParserException() {
        super();
    }
    
    /**
     * Constructs a <code>RtfParserException</code> with a message.
     *
     * @param		message			a message describing the exception
     */
    public RtfParserException(String message) {
        super(message);
    }

    /**
     * We print the message of the checked exception 
     * @return the error message
     */
    public String getMessage() {
        if (ex == null)
            return super.getMessage();
        else
            return ex.getMessage();
    }

    /**
     * and make sure we also produce a localized version 
     * @return a localized message
     */
    public String getLocalizedMessage() {
        if (ex == null)
            return super.getLocalizedMessage();
        else
            return ex.getLocalizedMessage();
    }

    /**
     * The toString() is changed to be prefixed with ExceptionConverter 
     * @return the String version of the exception
     */
    public String toString() {
        if (ex == null)
            return super.toString();
        else
            return split(getClass().getName()) + ": " + ex;
    }

    /** we have to override this as well */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * here we prefix, with s.print(), not s.println(), the stack
     * trace with "ExceptionConverter:" 
     * @param s a printstream object
     */
    public void printStackTrace(java.io.PrintStream s) {
        if (ex == null)
            super.printStackTrace(s);
        else {
            synchronized (s) {
                s.print(split(getClass().getName()) + ": ");
                ex.printStackTrace(s);
            }
        }
    }

    /**
     * Again, we prefix the stack trace with "ExceptionConverter:" 
     * @param s A PrintWriter object
     */
    public void printStackTrace(java.io.PrintWriter s) {
        if (ex == null)
            super.printStackTrace(s);
        else {
            synchronized (s) {
                s.print(split(getClass().getName()) + ": ");
                ex.printStackTrace(s);
            }
        }
    }

    /**
     * Removes everything in a String that comes before a '.'
     * @param s the original string
     * @return the part that comes after the dot
     */
    private static String split(String s) {
        int i = s.lastIndexOf('.');
        if (i < 0)
            return s;
        else
            return s.substring(i + 1);
    }
}
