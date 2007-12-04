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

public class RtfPropertyCharacter {
	/**
	 * Flag - Bold 0/1
	 */
	private int bold = 0;
	/**
	 * Flag - underline 0/1
	 */
	private int underline = 0;
	/**
	 * Flag - italic 0/1
	 */
	private int italic = 0;
	
	/**
	 * default constructor
	 */
	public RtfPropertyCharacter() {
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param orig Original object to copy
	 */
	public RtfPropertyCharacter(RtfPropertyCharacter orig) {
		this.bold = orig.bold;
		this.underline = orig.underline;
		this.italic = orig.italic;
	}
	
    /**
     * Reset values to defaults
     */
    public void setToDefault() {
    	this.bold = 0;
    	this.underline = 0;
    	this.italic = 0;
    }
    
    public void toggleBold() {
    	this.bold = (this.bold==1?0:1);
    }
    
    public void toggleUnderline() {
    	this.underline = (this.underline==1?0:1);
    }
    
    public void toggleItalic() {
    	this.italic = (this.italic==1?0:1);
    }
    public void setBoldOn() {
    	this.bold = 1;
    }
    public void setUnderlineOn() {
    	this.underline = 1;
    }
    public void setItalicOn() {
    	this.italic = 1;
    }
    public void setBoldOff() {
    	this.bold = 0;
    }
    public void setUnderlineOff() {
    	this.underline = 0;
    }
    public void setItalicOff() {
    	this.italic = 0;
    }
	/**
	 * @return the bold
	 */
	public int getBold() {
		return bold;
	}

	/**
	 * @param bold the bold to set
	 */
	public void setBold(int bold) {
		this.bold = bold;
	}

	/**
	 * @return the italic
	 */
	public int getItalic() {
		return italic;
	}

	/**
	 * @param italic the italic to set
	 */
	public void setItalic(int italic) {
		this.italic = italic;
	}

	/**
	 * @return the underline
	 */
	public int getUnderline() {
		return underline;
	}

	/**
	 * @param underline the underline to set
	 */
	public void setUnderline(int underline) {
		this.underline = underline;
	}
    
}
