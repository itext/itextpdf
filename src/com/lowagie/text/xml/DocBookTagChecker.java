/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
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

package com.lowagie.text.xml;

/**
 * This class is the default Checker for DocBookTags in DocBookHandler.
 * @author blowagie
 */
public class DocBookTagChecker implements DocBookTags {

	/**
	 * Empty constructor.
	 */
	public DocBookTagChecker() {
	}
	
	/**
	 * Checks if a tag corresponds with a Chunk.
	 * 
	 * @param object
	 * @return true if the tag is a Chunk type
	 */
	public boolean isChunk(Object object) {
		if (ABBREV.equals(object)) return true;
		if (ACRONYM.equals(object)) return true;
		if (APPLICATION.equals(object)) return true;
		if (AUTHORINITIALS.equals(object)) return true;
		if (ARG.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if a tag corresponds with a Paragraph (except for paragraphs that can be used as the title of a Section).
	 * 
	 * @param object
	 * @return
	 */
	public boolean isParagraph(Object object) {
		if (ACKNO.equals(object)) return true;
		if (ADDRESS.equals(object)) return true;
		if (ATTRIBUTION.equals(object)) return true;
		if (PARA.equals(object)) return true;
		if (SUBTITLE.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if a tag corresponds with a logical block that can only contain other objects, no content that isn't between tags.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isBlock(Object object) {
		if (AUTHOR.equals(object)) return true;
		if (AUTHORBLURB.equals(object)) return true;
		if (AUTHORGROUP.equals(object)) return true;
		if (ABSTRACT.equals(object)) return true;
		if (AFFILIATION.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if a tag corresponds with a tag that is not supported (yet).
	 * 
	 * @param object
	 * @return
	 */
	public boolean isNotSupported(Object object) {
		if (isIgnoreContent(object)) return true;
		if (AUDIODATA.equals(object)) return true;
		if (AUDIOOBJECT.equals(object)) return true;
		// Anchors
		if (ANCHOR.equals(object)) return true; // TODO
		// Lists
		if (ANSWER.equals(object)) return true; // TODO
		return false;
	}
	
	/**
	 * Checks if a tag corresponds with a Paragraph that can be used as the title of a Section.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isTitle(Object object) {
		if (TITLE.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if a tag corresponds with a Section.
	 * 
	 * @param object
	 * @return true if the tags is a Section type
	 */
	public boolean isSection(Object object) {
		if (APPENDIX.equals(object)) return true;
		if (ARTICLE.equals(object)) return true;
		if (BIBLIOGRAPHY.equals(object)) return true;
		if (BOOK.equals(object)) return true;
		if (CHAPTER.equals(object)) return true;
		if (COLOPHON.equals(object)) return true;
		if (DEDICATION.equals(object)) return true;
		if (GLOSSARY.equals(object)) return true;
		if (GLOSSARYINFO.equals(object)) return true;
		if (PART.equals(object)) return true;
		if (PREFACE.equals(object)) return true;
		if (REFERENCE.equals(object)) return true;
		if (INDEX.equals(object)) return true;
		if (LOT.equals(object)) return true;
		if (PARTINFO.equals(object)) return true;
		if (SECT1.equals(object)) return true;
		if (SECT2.equals(object)) return true;
		if (SECT3.equals(object)) return true;
		if (SECT4.equals(object)) return true;
		if (SECT5.equals(object)) return true;
		if (SECTION.equals(object)) return true;
		if (SETINDEX.equals(object)) return true;
		if (TOC.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if a tag contains information that isn't part of the content.
	 * 
	 * @param object
	 * @return true if the tag is an information type
	 */
	public boolean isInfo(Object object) {
		if (ARTICLEINFO.equals(object)) return true;
		if (BOOKINFO.equals(object)) return true;
		if (APPENDIXINFO.equals(object)) return true;
		if (INDEXINFO.equals(object)) return true;
		if (BIBLIOGRAPHYINFO.equals(object)) return true;
		if (CHAPTERINFO.equals(object)) return true;
		if (CLASSSYNOPSISINFO.equals(object)) return true;
		if (FUNCSYNOPSISINFO.equals(object)) return true;
		if (GLOSSARYINFO.equals(object)) return true;
		if (MSGINFO.equals(object)) return true;
		if (OBJECTINFO.equals(object)) return true;
		if (PREFACEINFO.equals(object)) return true;
		if (REFENTRYINFO.equals(object)) return true;
		if (REFERENCEINFO.equals(object)) return true;
		if (REFMISCINFO.equals(object)) return true;
		if (REFSECT1INFO.equals(object)) return true;
		if (REFSECT2INFO.equals(object)) return true;
		if (REFSECT3INFO.equals(object)) return true;
		if (REFSYNOPSISDIVINFO.equals(object)) return true;
		if (RELEASEINFO.equals(object)) return true;
		if (SCREENINFO.equals(object)) return true;
		if (PARTINFO.equals(object)) return true;
		if (SECT1INFO.equals(object)) return true;
		if (SECT2INFO.equals(object)) return true;
		if (SECT3INFO.equals(object)) return true;
		if (SECT4INFO.equals(object)) return true;
		if (SECT5INFO.equals(object)) return true;
		if (SECTIONINFO.equals(object)) return true;
		if (SETINDEXINFO.equals(object)) return true;
		if (SETINFO.equals(object)) return true;
		if (SIDEBARINFO.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if the tag can be ignored, but not necessarily the content.
	 * 
	 * @param object
	 * @return true if the tag can be ignored
	 */
	public boolean isIgnore(Object object) {
		if (isBlock(object)) return true;
		if (isNotSupported(object)) return true;
		return false;
	}
	
	/**
	 * Checks if the content of this tag has to be ignored.
	 * @param object
	 * @return true if the content of the tag has to be ignored.
	 */
	public boolean isIgnoreContent(Object object) {
		if (isInfo(object)) return true;
		// not necessary
		if (ALT.equals(object)) return true;
		// not supported
		if (ACCEL.equals(object)) return true;
		if (ACTION.equals(object)) return true;
		if (AREA.equals(object)) return true;
		if (AREASET.equals(object)) return true;
		if (AREASPEC.equals(object)) return true;
		if (ARTPAGENUMS.equals(object)) return true;
		return false;
	}
	
	/**
	 * Checks if the tag is a newpage tag.
	 * 
	 * @param object
	 * @return true if the tag can be ignored
	 */
	public boolean isNewPage(Object object) {
		if (BEGINPAGE.equals(object)) return true;
		return false;
	}

}