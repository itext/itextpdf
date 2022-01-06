/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */

/*
 * The original version of this class was published in an article by professor Heinz Kabutz.
 * Read http://www.javaspecialists.eu/archive/Issue033.html
 * "This material from The Java(tm) Specialists' Newsletter by Maximum Solutions (South Africa).
 * Please contact Maximum Solutions for more information."
 * 
 * Copyright (C) 2001 Dr. Heinz M. Kabutz
 * Permission was granted by Dr. Kabutz to use this source code in iText.
 */
package com.itextpdf.text;

/**
 * The ExceptionConverter changes a checked exception into an
 * unchecked exception.
 */
public class ExceptionConverter extends RuntimeException {
    private static final long serialVersionUID = 8657630363395849399L;
	/** we keep a handle to the wrapped exception */
    private Exception ex;
    /** prefix for the exception */
    private String prefix;

    /**
     * Construct a RuntimeException based on another Exception
     * @param ex the exception that has to be turned into a RuntimeException
     */
    public ExceptionConverter(Exception ex) {
        super(ex);
        this.ex = ex;
        prefix = (ex instanceof RuntimeException) ? "" : "ExceptionConverter: ";
    }

    /**
     * Convert an Exception into an unchecked exception. Return the exception if it is
     * already an unchecked exception or return an ExceptionConverter wrapper otherwise
     *
     * @param ex the exception to convert
     * @return an unchecked exception 
     * @since 2.1.6
     */
    public static final RuntimeException convertException(Exception ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        }
        return new ExceptionConverter(ex);
    }

    /**
     * and allow the user of ExceptionConverter to get a handle to it. 
     * @return the original exception
     */
    public Exception getException() {
        return ex;
    }

    /**
     * We print the message of the checked exception 
     * @return message of the original exception
     */
    public String getMessage() {
        return ex.getMessage();
    }

    /**
     * and make sure we also produce a localized version
     * @return localized version of the message
     */
    public String getLocalizedMessage() {
        return ex.getLocalizedMessage();
    }

    /**
     * The toString() is changed to be prefixed with ExceptionConverter 
     * @return String version of the exception
     */
    public String toString() {
        return prefix + ex;
    }

    /** we have to override this as well */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * here we prefix, with s.print(), not s.println(), the stack
     * trace with "ExceptionConverter:" 
     * @param s
     */
    public void printStackTrace(java.io.PrintStream s) {
        synchronized (s) {
            s.print(prefix);
            ex.printStackTrace(s);
        }
    }

    /**
     * Again, we prefix the stack trace with "ExceptionConverter:" 
     * @param s
     */
    public void printStackTrace(java.io.PrintWriter s) {
        synchronized (s) {
            s.print(prefix);
            ex.printStackTrace(s);
        }
    }

    /**
     * requests to fill in the stack trace we will have to ignore.
     * We can't throw an exception here, because this method
     * is called by the constructor of Throwable 
     * @return a Throwable
     */
    public Throwable fillInStackTrace() {
        return this;
    }
}
