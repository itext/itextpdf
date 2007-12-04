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

public class RtfPropertySection {
	
	/**
	 * Section break types
	 * 
	 * @author Howard Shank (hgshank@yahoo.com)
	 *
	 */
	public static final class SectionBreakTypes {
		/**
		 * None
		 */
		public static final int NONE = 0;
		/**
		 * Column break
		 */
		public static final int COLUMN = 1;
		/**
		 * Even page break
		 */
		public static final int EVEN = 2;
		/**
		 * Odd page break
		 */
		public static final int ODD = 3;
		/**
		 * Page break
		 */
		public static final int PAGE = 4;
	}
	
	/**
	 * Page number formats.
	 * 
	 * @author Howard Shank (hgshank@yahoo.com)
	 *
	 */
	public static final class PageNumberFormats {
		/**
		 * Decimal number format
		 */
		public static final int DECIMAL = 0; 
		/**
		 * Uppercase Roman Numeral
		 */
		public static final int ROMAN_NUMERAL_UPPERCASE = 1;
		/**
		 * Lowercase Roman Numeral
		 */
		public static final int ROMAN_NUMERAL_LOWERCASE = 2;
		/**
		 * Uppercase Letter
		 */
		public static final int LETTER_UPPERCASE = 3;
		/**
		 * Lowercase Letter
		 */
		public static final int LETTER_LOWERCASE = 4;
	}
	
	/**
	 * Number of columns
	 */
	private int numberOfColumns = 1;
	/**
	 * section break type
	 */
	private int sectionBreakType = SectionBreakTypes.NONE;
	/**
	 * X position of the page number in twips
	 */
	private int pageNumberPositionX = 0;
	/**
	 * Y position of the page number in twips
	 */
	private int pageNumberPositionY = 0;
	/**
	 * Page number formatting
	 */
	private int pageNumberFormat = PageNumberFormats.DECIMAL;
	
	/**
	 * Default constructor
	 *
	 */
	public RtfPropertySection() {
	}
	
	/**
	 * Copy constructor
	 * @param orig Original object to copy
	 */
	public RtfPropertySection(RtfPropertySection orig) {
		this.numberOfColumns = orig.numberOfColumns;
		this.sectionBreakType = orig.sectionBreakType;
		this.pageNumberPositionX = orig.pageNumberPositionX;
		this.pageNumberPositionY = orig.pageNumberPositionY;
		this.pageNumberFormat = orig.pageNumberFormat;
	}
	
    /**
     * Reset values to defaults
     */
    public void setToDefault() {
    	this.numberOfColumns = 1;
    	this.sectionBreakType = SectionBreakTypes.NONE;
    	this.pageNumberPositionX = 0;
    	this.pageNumberPositionY = 0;
    	this.pageNumberFormat = PageNumberFormats.DECIMAL;	
    }
    
	/**
	 * @return the numberOfColumns
	 */
	public int getNumberOfColumns() {
		return numberOfColumns;
	}
	/**
	 * @param numberOfColumns the numberOfColumns to set
	 */
	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}
	/**
	 * @return the pageNumberFormat
	 */
	public int getPageNumberFormat() {
		return pageNumberFormat;
	}
	/**
	 * @param pageNumberFormat the pageNumberFormat to set
	 */
	public void setPageNumberFormat(int pageNumberFormat) {
		this.pageNumberFormat = pageNumberFormat;
	}
	/**
	 * @return the pageNumberPositionX
	 */
	public int getPageNumberPositionX() {
		return pageNumberPositionX;
	}
	/**
	 * @param pageNumberPositionX the pageNumberPositionX to set
	 */
	public void setPageNumberPositionX(int pageNumberPositionX) {
		this.pageNumberPositionX = pageNumberPositionX;
	}
	/**
	 * @return the pageNumberPositionY
	 */
	public int getPageNumberPositionY() {
		return pageNumberPositionY;
	}
	/**
	 * @param pageNumberPositionY the pageNumberPositionY to set
	 */
	public void setPageNumberPositionY(int pageNumberPositionY) {
		this.pageNumberPositionY = pageNumberPositionY;
	}
	/**
	 * @return the sectionBreakType
	 */
	public int getSectionBreakType() {
		return sectionBreakType;
	}
	/**
	 * @param sectionBreakType the sectionBreakType to set
	 */
	public void setSectionBreakType(int sectionBreakType) {
		this.sectionBreakType = sectionBreakType;
	}
}
