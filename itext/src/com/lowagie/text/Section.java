/*
 * @(#)Section.java					0.22 2000/02/02
 *       release iText0.3:			0.22 2000/02/14
 *       release iText0.35:         0.22 2000/08/11
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

package com.lowagie.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A <CODE>Section</CODE> is a part of a <CODE>Document</CODE> containing
 * other <CODE>Section</CODE>s, <CODE>Paragraph</CODE>s, <CODE>List</CODE>
 * and/or <CODE>Table</CODE>s.
 * <P>
 * Remark: you can not construct a <CODE>Section</CODE> yourself.
 * You will have to ask an instance of <CODE>Section</CODE> to the
 * <CODE>Chapter</CODE> or <CODE>Section</CODE> to which you want to
 * add the new <CODE>Section</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * Paragraph title2 = new Paragraph("This is Chapter 2", new Font(Font.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));
 * Chapter chapter2 = new Chapter(title2, 2);
 * Paragraph someText = new Paragraph("This is some text");
 * chapter2.add(someText);
 * Paragraph title21 = new Paragraph("This is Section 1 in Chapter 2", new Font(Font.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section1 = chapter2.addSection(title21);</STRONG>
 * Paragraph someSectionText = new Paragraph("This is some silly paragraph in a chapter and/or section. It contains some text to test the functionality of Chapters and Section.");
 * <STRONG>section1.add(someSectionText);</STRONG>
 * Paragraph title211 = new Paragraph("This is SubSection 1 in Section 1 in Chapter 2", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section11 = section1.addSection(40, title211, 2);<STRONG>
 * <STRONG>section11.add(someSectionText);<STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @author  bruno@lowagie.com
 * @version 0.22, 2000/02/02
 *
 * @since   iText0.30
 */

public class Section extends ArrayList implements Element {

// membervariables

	/** This is the title of this section. */
	protected Paragraph title;

	/** This is the number of subsections. */
	protected int subsections = 0;

	/** This is the complete list of sectionnumbers of this section and the parents of this section. */
	protected ArrayList numbers;
	
	/** This is the number of sectionnumbers that has to be shown before the section title. */
	protected int numberDepth;

	/** The indentation of this section on the left side. */
	protected int indentationLeft;

	/** The indentation of this section on the right side. */
	protected int indentationRight;

	/** The additional indentation of the content of this section. */
	protected int sectionIndent;

// constructors

	/**
	 * Constructs a new <CODE>Section</CODE>.
	 *
	 * @param	title			a <CODE>Paragraph</CODE>
	 * @param	numberDepth		the numberDepth
	 *
	 * @since	iText0.30
	 */

	Section(Paragraph title, int numberDepth) {
		this.numberDepth = numberDepth;
		this.title = title;
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>DocListener</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was processed successfully
     * @since   iText0.30
     */

    public final boolean process(DocListener listener) {
		try {
			for (Iterator i = iterator(); i.hasNext(); ) {
				listener.add((Element) i.next());
			}
			return true;
		}
		catch(DocumentException de) {
			return false;
		}
	}

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
	 *
     * @since	iText0.30
     */

    public int type() {
		return Element.SECTION;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
	 *
     * @since	iText0.30
     */

    public ArrayList getChunks() {
		 ArrayList tmp = new ArrayList();
		 for (Iterator i = iterator(); i.hasNext(); ) {
			 tmp.addAll(((Element) i.next()).getChunks());
		 }
		 return tmp;
	}

// overriding some of the ArrayList-methods

	/**
	 * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
	 * to this <CODE>Section</CODE>.
	 *
	 * @param	index	index at which the specified element is to be inserted
	 * @param	object	an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>=
	 * @return	<CODE>void</CODE>
	 * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
	 *
	 * @since	iText0.30
	 */

	public void add(int index, Object o) {
		try {
			Element element = (Element) o;
			if (element.type() == Element.PARAGRAPH ||
				element.type() == Element.LIST ||
				element.type() == Element.TABLE) {
				super.add(index, element);
			}
			else {
				throw new ClassCastException(String.valueOf(element.type()));
			}
		}
		catch(ClassCastException cce) {
			throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
		}
	}

	/**
	 * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
	 * to this <CODE>Section</CODE>.
	 *
	 * @param	object	an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
	 * @return	a boolean
	 * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or <CODE>Section</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean add(Object o) {
		try {
			Element element = (Element) o;
			if (element.type() == Element.PARAGRAPH ||
				element.type() == Element.LIST ||
				element.type() == Element.TABLE) {
				return super.add(o);
			}
			else if (element.type() == Element.SECTION) {
				Section section = (Section) o;
				section.setNumbers(++subsections, numbers);
				return super.add(section);
			}
			else {
				throw new ClassCastException(String.valueOf(element.type()));
			}
		}
		catch(ClassCastException cce) {
			throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
		}
	}

	/**
	 * Adds a collection of <CODE>Element</CODE>s
	 * to this <CODE>Section</CODE>.
	 *
	 * @param	collection	a collection of <CODE>Paragraph</CODE>s, <CODE>List</CODE>s and/or <CODE>Table</CODE>s
	 * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
	 * @throws	ClassCastException if one of the objects isn't a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean addAll(Collection collection) {	
		for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
			this.add(iterator.next());
		}
		return true;
	}

// methods

	/**
	 * Creates a <CODE>Section</CODE>, add it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	title		the title of the new section
	 * @param	numberDepth	the numberDepth of the section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(Paragraph title, int numberDepth) {
		Section section = new Section(title, numberDepth);
		add(section);
		return section;
	}

	/**
	 * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	title		the title of the new section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(Paragraph title) {
		Section section = new Section(title, 1);
		add(section);
		return section;
	}

	/**
	 * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	indentation	the indentation of the new section
	 * @param	title		the title of the new section
	 * @param	numberDepth	the numberDepth of the section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(int indentation, Paragraph title, int numberDepth) {
		Section section = new Section(title, numberDepth);
		section.setIndentation(indentation);
		add(section);
		return section;
	}

	/**
	 * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	indentation	the indentation of the new section
	 * @param	title		the title of the new section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(int indentation, Paragraph title) {
		Section section = new Section(title, 1);
		section.setIndentation(indentation);
		add(section);
		return section;
	}

	/**
	 * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	title		the title of the new section
	 * @param	numberDepth	the numberDepth of the section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(String title, int numberDepth) {
		Section section = new Section(new Paragraph(title), numberDepth);
		add(section);
		return section;
	}

	/**
	 * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	title		the title of the new section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(String title) {
		Section section = new Section(new Paragraph(title), 1);
		add(section);
		return section;
	}

	/**
	 * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	indentation	the indentation of the new section
	 * @param	title		the title of the new section
	 * @param	numberDepth	the numberDepth of the section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(int indentation, String title, int numberDepth) {
		Section section = new Section(new Paragraph(title), numberDepth);
		section.setIndentation(indentation);
		add(section);
		return section;
	}

	/**
	 * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
	 *
	 * @param	indentation	the indentation of the new section
	 * @param	title		the title of the new section
	 *
     * @since	iText0.30
	 */

	public final Section addSection(int indentation, String title) {
		Section section = new Section(new Paragraph(title), 1);
		section.setIndentation(indentation);
		add(section);
		return section;
	}

	/**
	 * Sets the number of this section.
	 *
	 * @param	number		the number of this section
	 * @param	numbers		an <CODE>ArrayList</CODE>, containing the numbers of the Parent
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	private final void setNumbers(int number, ArrayList numbers) {
		this.numbers = new ArrayList();
		this.numbers.add(new Integer(number));
		this.numbers.addAll(numbers);
	}

	/**
	 * Sets the depth of the sectionnumbers that will be shown preceding the title.
	 * <P>
	 * If the numberdepth is 0, the sections will not be numbered. If the numberdepth
	 * is 1, the section will be numbered with their own number. If the numberdepth is
	 * higher (for instance x > 1), the numbers of x - 1 parents will be shown.
	 *
	 * @param	numberDepth		the new numberDepth
	 * @return	<CODE>void</CODE>
	 * 
	 * @since	iText0.30
	 */

	public void setNumberDepth(int numberDepth) {
		this.numberDepth = numberDepth;
	}

	/**
	 * Sets the indentation of this <CODE>Section</CODE> on the left side.
	 *
	 * @param	indentation		the indentation
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public final void setIndentationLeft(int indentation) {
		indentationLeft = indentation;
	}

	/**
	 * Sets the indentation of this <CODE>Section</CODE> on the right side.
	 *
	 * @param	indentation		the indentation
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public final void setIndentationRight(int indentation) {
		indentationRight = indentation;
	}

	/**
	 * Sets the indentation of the content of this <CODE>Section</CODE>.
	 *
	 * @param	indentation		the indentation
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public final void setIndentation(int indentation) {
		sectionIndent = indentation;
	}

// methods to retrieve information

	/**
	 * Checks if this object is a <CODE>Chapter</CODE>.
	 *
	 * @return	<CODE>true</CODE> if it is a <CODE>Chapter</CODE>,
	 *			<CODE>false</CODE> if it is a <CODE>Section</CODE>.
	 *
	 * @since	iText0.30
	 */

	public final boolean isChapter() {
		return type() == Element.CHAPTER;
	}

	/**
	 * Checks if this object is a <CODE>Section</CODE>.
	 *
	 * @return	<CODE>true</CODE> if it is a <CODE>Section</CODE>,
	 *			<CODE>false</CODE> if it is a <CODE>Chapter</CODE>.
	 *
	 * @since	iText0.30
	 */

	public final boolean isSection() {
		return type() == Element.SECTION;
	}

	/**
	 * Returns the indentation of this <CODE>Section</CODE> on the left side.
	 *
	 * @return	the indentation
	 *
	 * @since	iText0.30
	 */

	public final int indentationLeft() {
		return indentationLeft;
	}

	/**
	 * Returns the indentation of this <CODE>Section</CODE> on the right side.
	 *
	 * @return	the indentation
	 *
	 * @since	iText0.30
	 */

	public final int indentationRight() {
		return indentationRight;
	}

	/**
	 * Returns the indentation of the content of this <CODE>Section</CODE>.
	 *
	 * @return	the indentation
	 *
	 * @since	iText0.30
	 */

	public final int indentation() {
		return sectionIndent;
	}

	/**
	 * Returns the depth of this section.
	 *
	 * @return	the depth
	 *
	 * @since	iText0.30
	 */

	public final int depth() {
		return numbers.size();
	}

	/**
	 * Returns the title, preceeded by a certain number of sectionnumbers.
	 *
	 * @return	a <CODE>Paragraph</CODE>
	 *
	 * @since	iText0.30
	 */

	public Paragraph title() {
		 if (title == null) {
			 return null;
		 }
		 int depth = Math.min(numbers.size(), numberDepth);
		 if (depth < 1) {
			 return title;
		 }
		 StringBuffer buf = new StringBuffer(" ");
		 for (int i = 0; i < depth; i++) {
			 buf.insert(0, ".");
			 buf.insert(0, ((Integer) numbers.get(i)).intValue());
		 }
		 Chunk chunk = new Chunk(buf.toString(), title.font());
		 Paragraph result = new Paragraph(chunk);
		 result.add(title);
		 return result;
	}

	/**
	 * Returns a representation of this <CODE>Section</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * 
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("\t<SECTION SECTIONINDENTATION=\"");
		buf.append(sectionIndent);
		if (indentationLeft != 0) {
			buf.append("\" LEFTINDENTATION=\"");
			buf.append(indentationLeft);
		}
		if (indentationRight != 0) {
			buf.append("\" RIGHTINDENTATION=\"");
			buf.append(indentationRight);
		}
		buf.append("\">\n");
		buf.append("\t\t<TITLE>\n");
		buf.append("\t\t\t<NUMBERS DEPTH=\"");
		buf.append(numberDepth);
		buf.append("\">");
		buf.append(numbers.toString());
		buf.append("<NUMBERS>\n");
		if (title != null) {
			buf.append(title.toString());
		}
		buf.append("\t\t</TITLE>\n");
		for (Iterator i = iterator(); i.hasNext(); ) {
			buf.append(i.next().toString());
		}
		buf.append("\n\t</SECTION>\n");								
		return buf.toString();
	}
}