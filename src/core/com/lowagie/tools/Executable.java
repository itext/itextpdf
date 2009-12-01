/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.lowagie.tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import com.lowagie.text.error_messages.MessageLocalization;

/**
 * This class enables you to call an executable that will show a PDF file.
 */
public class Executable {
	
	/**
	 * The path to Acrobat Reader.
	 */
	public static String acroread = null;

	
	/**
	 * Performs an action on a PDF document.
	 * @param fileName
	 * @param parameters
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	private static Process action(final String fileName,
			String parameters, boolean waitForTermination) throws IOException {
		Process process = null;
		if (parameters.trim().length() > 0) {
			parameters = " " + parameters.trim();
		}
		else {
			parameters = "";
		}
		if (acroread != null) {
			process = Runtime.getRuntime().exec(
					acroread + parameters + " \"" + fileName + "\"");
		}
		else if (isWindows()) {
			if (isWindows9X()) {
				process = Runtime.getRuntime().exec(
						"command.com /C start acrord32" + parameters + " \"" + fileName + "\"");
			}
			else {
				process = Runtime.getRuntime().exec(
					"cmd /c start acrord32" + parameters + " \"" + fileName + "\"");
			}
		}
		else if (isMac()) {
			if (parameters.trim().length() == 0) {
				process = Runtime.getRuntime().exec(
					new String[] { "/usr/bin/open", fileName });
			}
			else {
				process = Runtime.getRuntime().exec(
						new String[] { "/usr/bin/open", parameters.trim(), fileName });
			}
		}
		try {
			if (process != null && waitForTermination)
				process.waitFor();
		} catch (InterruptedException ie) {
		}
		return process;
	}
	
	/**
	 * Opens a PDF document.
	 * @param fileName
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	public static final Process openDocument(String fileName,
			boolean waitForTermination) throws IOException {
		return action(fileName, "", waitForTermination);
	}

	/**
	 * Opens a PDF document.
	 * @param file
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	public static final Process openDocument(File file,
			boolean waitForTermination) throws IOException {
		return openDocument(file.getAbsolutePath(), waitForTermination);
	}

	/**
	 * Opens a PDF document.
	 * @param fileName
	 * @return a process
	 * @throws IOException
	 */
	public static final Process openDocument(String fileName) throws IOException {
		return openDocument(fileName, false);
	}

	/**
	 * Opens a PDF document.
	 * @param file
	 * @return a process
	 * @throws IOException
	 */
	public static final Process openDocument(File file) throws IOException {
		return openDocument(file, false);
	}
	
	/**
	 * Prints a PDF document.
	 * @param fileName
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocument(String fileName,
			boolean waitForTermination) throws IOException {
		return action(fileName, "/p", waitForTermination);
	}

	/**
	 * Prints a PDF document.
	 * @param file
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocument(File file,
			boolean waitForTermination) throws IOException {
		return printDocument(file.getAbsolutePath(), waitForTermination);
	}

	/**
	 * Prints a PDF document.
	 * @param fileName
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocument(String fileName) throws IOException {
		return printDocument(fileName, false);
	}

	/**
	 * Prints a PDF document.
	 * @param file
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocument(File file) throws IOException {
		return printDocument(file, false);
	}
	
	/**
	 * Prints a PDF document without opening a Dialog box.
	 * @param fileName
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocumentSilent(String fileName,
			boolean waitForTermination) throws IOException {
		return action(fileName, "/p /h", waitForTermination);
	}

	/**
	 * Prints a PDF document without opening a Dialog box.
	 * @param file
	 * @param waitForTermination
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocumentSilent(File file,
			boolean waitForTermination) throws IOException {
		return printDocumentSilent(file.getAbsolutePath(), waitForTermination);
	}

	/**
	 * Prints a PDF document without opening a Dialog box.
	 * @param fileName
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocumentSilent(String fileName) throws IOException {
		return printDocumentSilent(fileName, false);
	}

	/**
	 * Prints a PDF document without opening a Dialog box.
	 * @param file
	 * @return a process
	 * @throws IOException
	 */
	public static final Process printDocumentSilent(File file) throws IOException {
		return printDocumentSilent(file, false);
	}
	
	/**
	 * Launches a browser opening an URL.
	 *
	 * @param url the URL you want to open in the browser
	 * @throws IOException
	 */
	public static final void launchBrowser(String url) throws IOException {
		try {
			if (isMac()) {
				Class macUtils = Class.forName("com.apple.mrj.MRJFileUtils");
				Method openURL = macUtils.getDeclaredMethod("openURL", new Class[] {String.class});
				openURL.invoke(null, new Object[] {url});
			}
			else if (isWindows())
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else { //assume Unix or Linux
	            String[] browsers = {
	               "firefox", "opera", "konqueror", "mozilla", "netscape" };
	            String browser = null;
	            for (int count = 0; count < browsers.length && browser == null; count++)
	               if (Runtime.getRuntime().exec(new String[] {"which", browsers[count]}).waitFor() == 0)
	                  browser = browsers[count];
	            if (browser == null)
	               throw new Exception(MessageLocalization.getComposedMessage("could.not.find.web.browser"));
	            else
	               Runtime.getRuntime().exec(new String[] {browser, url});
	            }
	         }
	      catch (Exception e) {
	         throw new IOException(MessageLocalization.getComposedMessage("error.attempting.to.launch.web.browser"));
	      }
	}

	/**
	 * Checks the Operating System.
	 * 
	 * @return true if the current os is Windows
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.indexOf("windows") != -1 || os.indexOf("nt") != -1;
	}

	/**
	 * Checks the Operating System.
	 * 
	 * @return true if the current os is Windows
	 */
	public static boolean isWindows9X() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.equals("windows 95") || os.equals("windows 98");
	}

	/**
	 * Checks the Operating System.
	 * 
	 * @return true if the current os is Apple
	 */
	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.indexOf("mac") != -1;
	}

	/**
	 * Checks the Operating System.
	 * 
	 * @return true if the current os is Linux
	 */
	public static boolean isLinux() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.indexOf("linux") != -1;
	}
}
