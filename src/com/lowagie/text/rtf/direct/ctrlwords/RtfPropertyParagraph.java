/* $Id$
 * $Name$
 *
 * Copyright 2007 by Howard Shank (hgshank@yahoo.com)
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
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
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
package com.lowagie.text.rtf.direct.ctrlwords;

public class RtfPropertyParagraph {
	/**
	 * Justifications
	 * 
	 * @author Howard Shank (hgshank@yahoo.com)
	 *
	 */
	public static final class Justifications {
		/**
		 * Justify left
		 */
		public static final int LEFT = 0;
		/**
		 * Justify right
		 */
		public static final int RIGHT = 1;
		/**
		 * Justify center
		 */
		public static final int CENTER = 2;
		/**
		 * Justify full
		 */
		public static final int FULL = 3;
	}

	/**
	 * Left indent in twips
	 */
	private int indentLeft = 0;
	/**
	 * Right indent in twips
	 */
	private int indentRight = 0;
	/**
	 * First line indent in twips
	 */
	private int indentFirstLine = 0;
	/**
	 * Justification for paragraph
	 */
	private int justification = Justifications.LEFT;
	
	
	/**
	 * Default constructor
	 *
	 */
	public RtfPropertyParagraph() {
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param orig Original object to copy
	 */
	public RtfPropertyParagraph(RtfPropertyParagraph orig) {
		this.indentLeft = orig.indentLeft;
		this.indentRight = orig.indentRight;
		this.indentFirstLine = orig.indentFirstLine;
		this.justification = orig.justification;
	}
    
    /**
     * Reset values to defaults
     */
    public void setToDefault() {
    	this.indentLeft = 0;
    	this.indentRight = 0;
    	this.indentFirstLine = 0;
    	this.justification = Justifications.LEFT;
    }

	/**
	 * @return the indentFirstLine
	 */
	public int getIndentFirstLine() {
		return indentFirstLine;
	}

	/**
	 * @param indentFirstLine the indentFirstLine to set
	 */
	public void setIndentFirstLine(int indentFirstLine) {
		this.indentFirstLine = indentFirstLine;
	}

	/**
	 * @return the indentLeft
	 */
	public int getIndentLeft() {
		return indentLeft;
	}

	/**
	 * @param indentLeft the indentLeft to set
	 */
	public void setIndentLeft(int indentLeft) {
		this.indentLeft = indentLeft;
	}

	/**
	 * @return the indentRight
	 */
	public int getIndentRight() {
		return indentRight;
	}

	/**
	 * @param indentRight the indentRight to set
	 */
	public void setIndentRight(int indentRight) {
		this.indentRight = indentRight;
	}

	/**
	 * @return the justification
	 */
	public int getJustification() {
		return justification;
	}

	/**
	 * @param justification the justification to set
	 */
	public void setJustification(int justification) {
		this.justification = justification;
	}
}
