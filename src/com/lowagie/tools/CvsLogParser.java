/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Enumeration;

/**
 * If you get a changelog from CVS, for instance:
 * cvs -d:ext:blowagie@itext.cvs.sourceforge.net:/cvsroot/itext log -d ">2005-07-29"
 * you get an overview that contains all the changes.
 * With this class, you can parse out the important entries.
 *
 * @author blowagie
 */
public class CvsLogParser implements Enumeration {

	/** the tokenizer object. */
	protected StreamTokenizer st;
	
	/** indicates if the current token contains changes. */
	protected boolean changes = false;
	
	/** indicates if the tokenizer has more tokens. */
	protected boolean more = false;
	
	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public CvsLogParser(String file) throws FileNotFoundException {
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		st = new StreamTokenizer(r);
		st.eolIsSignificant(true);
		st.ordinaryChar('/');
		st.ordinaryChar('\'');
		more = true;
	}

	/**
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return more;
	}
	
	/**
	 * Returns the next token in the log file.
	 * @see java.util.Enumeration#nextElement()
	 */
	public Object nextElement(){
		StringBuffer token = new StringBuffer();
		StringBuffer line = new StringBuffer();
		boolean moreToken = true;
		changes = false;
		try {
			while (more && moreToken) {
				st.nextToken();
				switch(st.ttype) {
				case StreamTokenizer.TT_EOF:
					more = false;
				case StreamTokenizer.TT_EOL:
					token.append(line.toString());
					if (line.toString().endsWith("=============================================================================")) {
						moreToken = false;
					}
					else {
						line = new StringBuffer("\n");
					}
					break;
				case StreamTokenizer.TT_WORD:
					line.append(st.sval);
					line.append(' ');
					break;
				case StreamTokenizer.TT_NUMBER:
					if (st.nval > 0 && line.toString().endsWith("selected revisions :")) {
						changes = true;
					}
					line.append(st.nval);
					break;
				default:
					line.append((char) st.ttype);
				}
			}
			return token.toString();
		}
		catch(IOException ioe) {
			more = false;
			return "";
		}
	}

	/**
	 * Indicates if the current token is one that contains changes.
	 * @return true if the token is relevant
	 */
	private boolean hasChanged() {
		return changes;
	}
	
	
	/**
	 * Parses a log form CVS.
	 * @param args the path to the logfile
	 */
	public static void main(String[] args) {
		try {
			CvsLogParser p = new CvsLogParser(args[0]);
			String token;
			while (p.hasMoreElements()) {
				token = (String) p.nextElement();
				if (p.hasChanged()) {
					System.out.println(token);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
