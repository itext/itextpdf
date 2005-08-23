/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext@lowagie.com
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
 * cvs -d:ext:blowagie@cvs.sourceforge.net:/cvsroot/itext log -d ">2005-07-29"
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
		String type;
		boolean moreToken = true;
		changes = false;
		try {
			while (more && moreToken) {
				int i = st.nextToken();
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
					line.append(" ");
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
