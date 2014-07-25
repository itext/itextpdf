/*
 * $Id: MarkedSection.java 6134 2013-12-23 13:15:14Z blowagie $
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
 * OF THIRD PARTY RIGHTS
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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
package com.itextpdf.text;

import java.util.Collection;
import java.util.Iterator;

import com.itextpdf.text.api.Indentable;

/**
 * Wrapper that allows to add properties to a Chapter/Section object.
 * Before iText 1.5 every 'basic building block' implemented the MarkupAttributes interface.
 * By setting attributes, you could add markup to the corresponding XML and/or HTML tag.
 * This functionality was hardly used by anyone, so it was removed, and replaced by
 * the MarkedObject functionality.
 */

public class MarkedSection extends MarkedObject implements Indentable {

	/** This is the title of this section. */
	protected MarkedObject title = null;

	/**
	 * Creates a MarkedObject with a Section or Chapter object.
	 * @param section	the marked section
	 */
	public MarkedSection(Section section) {
		super();
		if (section.title != null) {
			title = new MarkedObject(section.title);
			section.setTitle(null);
		}
		this.element = section;
	}

	/**
	 * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
	 * to this <CODE>Section</CODE>.
	 *
	 * @param	index	index at which the specified element is to be inserted
	 * @param	o   	an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>=
	 * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
	 * @since 5.0.1 (signature changed to use Element)
	 */
	public void add(int index, Element o) {
		((Section)element).add(index, o);
	}

	/**
	 * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
	 * to this <CODE>Section</CODE>.
	 *
	 * @param	o   	an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
	 * @return	a boolean
	 * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or <CODE>Section</CODE>
	 * @since 5.0.1 (signature changed to use Element)
	 */
	public boolean add(Element o) {
		return ((Section)element).add(o);
	}

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param       listener        an <CODE>ElementListener</CODE>
     * @return <CODE>true</CODE> if the element was processed successfully
     */
    @Override
    public boolean process(ElementListener listener) {
        try {
        	Element element;
            for (Iterator<Element> i = ((Section)this.element).iterator(); i.hasNext(); ) {
            	element = i.next();
                listener.add(element);
            }
            return true;
        }
        catch(DocumentException de) {
            return false;
        }
    }

	/**
	 * Adds a collection of <CODE>Element</CODE>s
	 * to this <CODE>Section</CODE>.
	 *
	 * @param	collection	a collection of <CODE>Paragraph</CODE>s, <CODE>List</CODE>s and/or <CODE>Table</CODE>s
	 * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
	 * @throws	ClassCastException if one of the objects isn't a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE>
	 */
	public boolean addAll(Collection<? extends Element> collection) {
		return ((Section)element).addAll(collection);
	}

	/**
	 * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	indentation	the indentation of the new section
	 * @param	numberDepth	the numberDepth of the section
	 * @return  a new Section object
	 */
	public MarkedSection addSection(float indentation, int numberDepth) {
		MarkedSection section = ((Section)element).addMarkedSection();
		section.setIndentation(indentation);
		section.setNumberDepth(numberDepth);
		return section;
	}

	/**
	 * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	indentation	the indentation of the new section
	 * @return  a new Section object
	 */
	public MarkedSection addSection(float indentation) {
		MarkedSection section = ((Section)element).addMarkedSection();
		section.setIndentation(indentation);
		return section;
	}

	/**
	 * Creates a <CODE>Section</CODE>, add it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	numberDepth	the numberDepth of the section
	 * @return  a new Section object
	 */
	public MarkedSection addSection(int numberDepth) {
		MarkedSection section = ((Section)element).addMarkedSection();
		section.setNumberDepth(numberDepth);
		return section;
	}

	/**
	 * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
	 *
	 * @return  a new Section object
	 */
	public MarkedSection addSection() {
		return ((Section)element).addMarkedSection();
	}

	// public methods

	/**
	 * Sets the title of this section.
	 *
	 * @param	title	the new title
	 */
	public void setTitle(MarkedObject title) {
		if (title.element instanceof Paragraph)
			this.title = title;
	}

	/**
	 * Gets the title of this MarkedSection.
	 * @return	a MarkObject with a Paragraph containing the title of a Section
	 * @since	iText 2.0.8
	 */
    public MarkedObject getTitle() {
    	Paragraph result = Section.constructTitle((Paragraph)title.element, ((Section)element).numbers, ((Section)element).numberDepth, ((Section)element).numberStyle);
        MarkedObject mo = new MarkedObject(result);
        mo.markupAttributes = title.markupAttributes;
        return mo;
    }

	/**
	 * Sets the depth of the sectionnumbers that will be shown preceding the title.
	 * <P>
	 * If the numberdepth is 0, the sections will not be numbered. If the numberdepth
	 * is 1, the section will be numbered with their own number. If the numberdepth is
	 * higher (for instance x > 1), the numbers of x - 1 parents will be shown.
	 *
	 * @param	numberDepth		the new numberDepth
	 */
	public void setNumberDepth(int numberDepth) {
		((Section)element).setNumberDepth(numberDepth);
	}

	/**
	 * Sets the indentation of this <CODE>Section</CODE> on the left side.
	 *
	 * @param	indentation		the indentation
	 */
	public void setIndentationLeft(float indentation) {
		((Section)element).setIndentationLeft(indentation);
	}

	/**
	 * Sets the indentation of this <CODE>Section</CODE> on the right side.
	 *
	 * @param	indentation		the indentation
	 */
	public void setIndentationRight(float indentation) {
		((Section)element).setIndentationRight(indentation);
	}

	/**
	 * Sets the indentation of the content of this <CODE>Section</CODE>.
	 *
	 * @param	indentation		the indentation
	 */
	public void setIndentation(float indentation) {
		((Section)element).setIndentation(indentation);
	}

	/** Setter for property bookmarkOpen.
	 * @param bookmarkOpen false if the bookmark children are not
	 * visible.
	 */
	public void setBookmarkOpen(boolean bookmarkOpen) {
	 	((Section)element).setBookmarkOpen(bookmarkOpen);
	}

	/**
	 * Setter for property triggerNewPage.
	 * @param triggerNewPage true if a new page has to be triggered.
	 */
	public void setTriggerNewPage(boolean triggerNewPage) {
	  	((Section)element).setTriggerNewPage(triggerNewPage);
	}

	/**
	 * Sets the bookmark title. The bookmark title is the same as the section title but
	 * can be changed with this method.
	 * @param bookmarkTitle the bookmark title
	 */
	public void setBookmarkTitle(String bookmarkTitle) {
	  	((Section)element).setBookmarkTitle(bookmarkTitle);
	}

	/**
	 * Adds a new page to the section.
	 * @since	2.1.1
	 */
	public void newPage() {
		((Section)element).newPage();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.text.api.Indentable#getIndentationLeft()
	 */
	public float getIndentationLeft() {
		return ((Section)element).getIndentationLeft();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.text.api.Indentable#getIndentationRight()
	 */
	public float getIndentationRight() {
		return ((Section)element).getIndentationRight();
	}
}
