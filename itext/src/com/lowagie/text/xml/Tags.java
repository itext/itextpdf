/*
 * $Id$
 * $Name$
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *  	  
 */

package com.lowagie.text.xml;

import java.util.Properties;

import com.lowagie.text.*;

/**
 * A class that implements the <CODE>Tags</CODE>-interface maps your XML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public interface Tags {

	/**
	 * Checks if a certain <CODE>String</CODE> corresponds with a tagname.
	 *
	 * @param	tag		a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> is a tagname, <CODE>false</CODE> otherwise.
	 */

	public boolean isTag(String tag);

	/**
	 * Checks if a certain tag corresponds with the roottag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>itext</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isDocumentRoot(String tag);

	/**
	 * Checks if a certain tag corresponds with the chaptertag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 */

	public boolean isChapter(String tag);

	/**
	 * Checks if a certain tag corresponds with the sectiontag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 */

	public boolean isSection(String tag);				   

	/**
	 * Alters a given <CODE>Section</CODE> following a set of attributes.
	 *
	 * @param	section		the section that has to be changed
	 * @param	attributes	the attributes
	 */

	public void setSection(Section section, Properties attributes);				   

	/**
	 * Checks if a certain tag corresponds with the titletag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 */

	public boolean isTitle(String tag);  

	/**
	 * Checks if a certain tag corresponds with the paragraph-tag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 */

	public boolean isParagraph(String tag);				   					   

	/**
	 * Returns a <CODE>Paragraph</CODE> that has been constructed taking in account
	 * the value of some <VAR>attributes</VAR>.
	 *
	 * @param	attributes		Some attributes
	 * @return	a <CODE>Paragraph</CODE>
	 */

	public Paragraph getParagraph(Properties attributes);

	/**
	 * Checks if a certain tag corresponds with the phrase-tag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 */

	public boolean isPhrase(String tag);					   

	/**
	 * Returns a <CODE>Phrase</CODE> that has been constructed taking in account
	 * the value of some <VAR>attributes</VAR>.
	 *
	 * @param	attributes		Some attributes
	 * @return	a <CODE>Phrase</CODE>
	 */

	public Phrase getPhrase(Properties attributes);				   

	/**
	 * Checks if a certain tag corresponds with the chunk-tag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 */

	public boolean isChunk(String tag);					   

	/**
	 * Returns a <CODE>Chunk</CODE> that has been constructed taking in account
	 * the value of some <VAR>attributes</VAR>.
	 *
	 * @param	attributes		Some attributes
	 * @return	a <CODE>Chunk</CODE>
	 */

	public Chunk getChunk(Properties attributes);


	/**
	 * Gets the meta type if a certain attribute represents some meta data.
	 *
	 * @param	attribute	an attribute name
	 * @return	the type of meta data
	 */

	public int getMetaType(String attribute);
}