/*
 * $Id: LoggerFactory.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.text.log;

/**
 * LoggerFactory can be used to set a logger. The logger should be created by
 * implementing {@link Logger}. In the implementation users can choose how they
 * log received messages. Added for developers. For some cases it can be handy
 * to receive logging statements while developing applications with iText
 *
 * @author redlab_b
 *
 */
public class LoggerFactory {

	static {
		myself = new LoggerFactory();
	}

	private static LoggerFactory myself;
	/**
	 * Returns the logger set in this LoggerFactory. Defaults to {@link NoOpLogger}
	 * @param klass
	 * @return the logger.
	 */
	public static Logger getLogger(final Class<?> klass) {
		return myself.logger.getLogger(klass);
	}
	/**
	 * Returns the logger set in this LoggerFactory. Defaults to {@link NoOpLogger}
	 * @param name
	 * @return the logger.
	 */
	public static Logger getLogger(final String name) {
		return myself.logger.getLogger(name);
	}
	/**
	 * Returns the LoggerFactory
	 * @return singleton instance of this LoggerFactory
	 */
	public static LoggerFactory getInstance() {
		return myself;
	}

	private Logger logger = new NoOpLogger();

	private LoggerFactory() {
	}

	/**
	 * Set the global logger to process logging statements with.
	 *
	 * @param logger the logger
	 */
	public void setLogger(final Logger logger) {
		this.logger = logger;
	}

	/**
	 * Get the logger.
	 *
	 * @return the logger
	 */
	public Logger logger() {
		return logger;
	}


}
