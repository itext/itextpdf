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

public class RtfPropertyDocument {
	/**
	 * Page orientations - Portrait or Landscape
	 * 
	 * @author Howard Shank (hgshank@yahoo.com)
	 *
	 */
	public static final class Orientations {
		/**
		 * Portrait orientation
		 */
		public static final int PORTRAIT = 0;
		/**
		 * Landscape orientation
		 */
		public static final int LANDSCAPE = 1;
	}
	/**
	 * Page width in twips
	 */
    private int pageWidthTwips = 12240;
    /**
     * Page height in twips
     */
    private int pageHeightTwips = 15480;
    /**
     * Left margin in twips
     */
    private int marginLeftTwips = 1800;
    /**
     * Top margin in twips
     */
    private int marginTopTwips = 1440;
    /**
     * Right margin in twips
     */
    private int marginRightTwips = 1800;
    /**
     * Bottom margin in twips
     */
    private int marginBottomTwips = 1440;
    /**
     * Starting page number
     */
    private int pageNumberStart = 1;
    /**
     * Facing pages enabled?
     */
    private char enableFacingPages = 1;
    /**
     * Page orientation - landscape or portrait
     */
    private char pageOrientation = Orientations.PORTRAIT;
    /**
     * Default font number
     */
    private char defaultFontNumber = 0;
    
    /**
     * Default constuctor
     *
     */
    public RtfPropertyDocument() {
	}
    /**
     * Copy constructor
     * 
     * @param orig Original object to copy
     */
    public RtfPropertyDocument(RtfPropertyDocument orig) {
		this.pageWidthTwips = orig.pageWidthTwips;
		this.pageHeightTwips = orig.pageHeightTwips;
		this.marginLeftTwips = orig.marginLeftTwips;
		this.marginTopTwips = orig.marginTopTwips;
		this.marginRightTwips = orig.marginRightTwips;
		this.marginBottomTwips = orig.marginBottomTwips;
		this.pageNumberStart = orig.pageNumberStart;
		this.enableFacingPages = orig.enableFacingPages;
		this.pageOrientation = orig.pageOrientation;
		this.defaultFontNumber = orig.defaultFontNumber;
	}
    
    /**
	 * @return the defaultFontNumber
	 */
	public char getDefaultFontNumber() {
		return defaultFontNumber;
	}
	/**
	 * @param defaultFontNumber the defaultFontNumber to set
	 */
	public void setDefaultFontNumber(char defaultFontNumber) {
		this.defaultFontNumber = defaultFontNumber;
	}
	/**
	 * @return the enableFacingPages
	 */
	public char getEnableFacingPages() {
		return enableFacingPages;
	}
	/**
	 * @param enableFacingPages the enableFacingPages to set
	 */
	public void setEnableFacingPages(char enableFacingPages) {
		this.enableFacingPages = enableFacingPages;
	}
	/**
	 * @return the marginBottomTwips
	 */
	public int getMarginBottomTwips() {
		return marginBottomTwips;
	}
	/**
	 * @param marginBottomTwips the marginBottomTwips to set
	 */
	public void setMarginBottomTwips(int marginBottomTwips) {
		this.marginBottomTwips = marginBottomTwips;
	}
	/**
	 * @return the marginLeftTwips
	 */
	public int getMarginLeftTwips() {
		return marginLeftTwips;
	}
	/**
	 * @param marginLeftTwips the marginLeftTwips to set
	 */
	public void setMarginLeftTwips(int marginLeftTwips) {
		this.marginLeftTwips = marginLeftTwips;
	}
	/**
	 * @return the marginRightTwips
	 */
	public int getMarginRightTwips() {
		return marginRightTwips;
	}
	/**
	 * @param marginRightTwips the marginRightTwips to set
	 */
	public void setMarginRightTwips(int marginRightTwips) {
		this.marginRightTwips = marginRightTwips;
	}
	/**
	 * @return the marginTopTwips
	 */
	public int getMarginTopTwips() {
		return marginTopTwips;
	}
	/**
	 * @param marginTopTwips the marginTopTwips to set
	 */
	public void setMarginTopTwips(int marginTopTwips) {
		this.marginTopTwips = marginTopTwips;
	}
	/**
	 * @return the pageHeightTwips
	 */
	public int getPageHeightTwips() {
		return pageHeightTwips;
	}
	/**
	 * @param pageHeightTwips the pageHeightTwips to set
	 */
	public void setPageHeightTwips(int pageHeightTwips) {
		this.pageHeightTwips = pageHeightTwips;
	}
	/**
	 * @return the pageNumberStart
	 */
	public int getPageNumberStart() {
		return pageNumberStart;
	}
	/**
	 * @param pageNumberStart the pageNumberStart to set
	 */
	public void setPageNumberStart(int pageNumberStart) {
		this.pageNumberStart = pageNumberStart;
	}
	/**
	 * @return the pageOrientation
	 */
	public char getPageOrientation() {
		return pageOrientation;
	}
	/**
	 * @param pageOrientation the pageOrientation to set
	 */
	public void setPageOrientation(char pageOrientation) {
		this.pageOrientation = pageOrientation;
	}
	/**
	 * @return the pageWidthTwips
	 */
	public int getPageWidthTwips() {
		return pageWidthTwips;
	}
	/**
	 * @param pageWidthTwips the pageWidthTwips to set
	 */
	public void setPageWidthTwips(int pageWidthTwips) {
		this.pageWidthTwips = pageWidthTwips;
	}
	/**
     * Reset values to defaults
     */
    public void setToDefault()
    {
    	this.pageWidthTwips = 12240;
    	this.pageHeightTwips = 15480;
    	this.marginLeftTwips = 1800;
    	this.marginTopTwips = 1440;
    	this.marginRightTwips = 1800;
    	this.marginBottomTwips = 1440;
    	this.pageNumberStart = 1;
    	this.enableFacingPages = 1;
    	this.pageOrientation = Orientations.PORTRAIT;
    	this.defaultFontNumber = 0;
    }
}
