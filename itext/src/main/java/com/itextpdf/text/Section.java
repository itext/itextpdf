/*
 * $Id: Section.java 6192 2014-01-29 14:37:53Z eugenemark $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.api.Indentable;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

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
 * Paragraph title2 = new Paragraph("This is Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));
 * Chapter chapter2 = new Chapter(title2, 2);
 * Paragraph someText = new Paragraph("This is some text");
 * chapter2.add(someText);
 * Paragraph title21 = new Paragraph("This is Section 1 in Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section1 = chapter2.addSection(title21);</STRONG>
 * Paragraph someSectionText = new Paragraph("This is some silly paragraph in a chapter and/or section. It contains some text to test the functionality of Chapters and Section.");
 * <STRONG>section1.add(someSectionText);</STRONG>
 * Paragraph title211 = new Paragraph("This is SubSection 1 in Section 1 in Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section11 = section1.addSection(40, title211, 2);</STRONG>
 * <STRONG>section11.add(someSectionText);</STRONG>
 * </PRE></BLOCKQUOTE>
 */

public class Section extends ArrayList<Element> implements TextElementArray, LargeElement, Indentable, IAccessibleElement {
    // constant
	/**
	 * A possible number style. The default number style: "1.2.3."
	 * @since	iText 2.0.8
	 */
	public static final int NUMBERSTYLE_DOTTED = 0;
	/**
	 * A possible number style. For instance: "1.2.3"
	 * @since	iText 2.0.8
	 */
	public static final int NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT = 1;

	/** A serial version uid. */
	private static final long serialVersionUID = 3324172577544748043L;

	// member variables

	/** The title of this section. */
    protected Paragraph title;

    /** The bookmark title if different from the content title */
    protected String bookmarkTitle;

    /** The number of sectionnumbers that has to be shown before the section title. */
    protected int numberDepth;

    /**
     * The style for sectionnumbers.
     * @since	iText 2.0.8
     */
    protected int numberStyle = NUMBERSTYLE_DOTTED;

    /** The indentation of this section on the left side. */
    protected float indentationLeft;

    /** The indentation of this section on the right side. */
    protected float indentationRight;

    /** The additional indentation of the content of this section. */
    protected float indentation;

    /** false if the bookmark children are not visible */
    protected boolean bookmarkOpen = true;

    /** true if the section has to trigger a new page */
    protected boolean triggerNewPage = false;

    /** This is the number of subsections. */
    protected int subsections = 0;

    /** This is the complete list of sectionnumbers of this section and the parents of this section. */
    protected ArrayList<Integer> numbers = null;

    /**
     * Indicates if the Section will be complete once added to the document.
     * @since	iText 2.0.8
     */
    protected boolean complete = true;

    /**
     * Indicates if the Section was added completely to the document.
     * @since	iText 2.0.8
     */
    protected boolean addedCompletely = false;

    /**
     * Indicates if this is the first time the section was added.
     * @since	iText 2.0.8
     */
    protected boolean notAddedYet = true;

    // constructors

    /**
     * Constructs a new <CODE>Section</CODE>.
     */
    protected Section() {
        title = new Paragraph();
        numberDepth = 1;
        title.setRole(new PdfName("H" + numberDepth));
    }

    /**
     * Constructs a new <CODE>Section</CODE>.
     *
     * @param	title			a <CODE>Paragraph</CODE>
     * @param	numberDepth		the numberDepth
     */
    protected Section(final Paragraph title, final int numberDepth) {
        this.numberDepth = numberDepth;
        this.title = title;
        if (title != null)
            title.setRole(new PdfName("H" + numberDepth));
    }

    // implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener		the <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(final ElementListener listener) {
        try {
        	Element element;
            for (Object element2 : this) {
            	element = (Element)element2;
                listener.add(element);
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
     */
    public int type() {
        return Element.SECTION;
    }

    /**
     * Checks if this object is a <CODE>Chapter</CODE>.
     *
     * @return	<CODE>true</CODE> if it is a <CODE>Chapter</CODE>,
     *			<CODE>false</CODE> if it is a <CODE>Section</CODE>.
     */
    public boolean isChapter() {
        return type() == Element.CHAPTER;
    }

    /**
     * Checks if this object is a <CODE>Section</CODE>.
     *
     * @return	<CODE>true</CODE> if it is a <CODE>Section</CODE>,
     *			<CODE>false</CODE> if it is a <CODE>Chapter</CODE>.
     */
    public boolean isSection() {
        return type() == Element.SECTION;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public List<Chunk> getChunks() {
        List<Chunk> tmp = new ArrayList<Chunk>();
        for (Object element : this) {
            tmp.addAll(((Element) element).getChunks());
        }
        return tmp;
    }

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return false;
	}

    // overriding some of the ArrayList-methods

    /**
     * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
     * to this <CODE>Section</CODE>.
     *
     * @param	index	index at which the specified element is to be inserted
     * @param	element   	an element of type <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>=
     * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
     * @since 5.0.1 (signature changed to use Element)
     */
    @Override
    public void add(final int index, final Element element) {
    	if (isAddedCompletely()) {
    		throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document"));
    	}
        try {
            if (element.isNestable()) {
                super.add(index, element);
            }
            else {
                throw new ClassCastException(MessageLocalization.getComposedMessage("you.can.t.add.a.1.to.a.section", element.getClass().getName()));
            }
        }
        catch(ClassCastException cce) {
            throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", cce.getMessage()));
        }
    }

    /**
     * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
     * to this <CODE>Section</CODE>.
     *
     * @param	element   an element of type <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
     * @return	a boolean
     * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or <CODE>Section</CODE>
     * @since 5.0.1 (signature changed to use Element)
     */
    @Override
    public boolean add(final Element element) {
    	if (isAddedCompletely()) {
    		throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document"));
    	}
        try {
            if (element.type() == Element.SECTION) {
                Section section = (Section) element;
                section.setNumbers(++subsections, numbers);
                return super.add(section);
            }
            else if (element instanceof MarkedSection && ((MarkedObject)element).element.type() == Element.SECTION) {
            	MarkedSection mo = (MarkedSection)element;
            	Section section = (Section)mo.element;
            	section.setNumbers(++subsections, numbers);
            	return super.add(mo);
            }
            else if (element.isNestable()) {
            	return super.add(element);
            }
            else {
                throw new ClassCastException(MessageLocalization.getComposedMessage("you.can.t.add.a.1.to.a.section", element.getClass().getName()));
            }
        }
        catch(ClassCastException cce) {
            throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", cce.getMessage()));
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
    @Override
    public boolean addAll(final Collection<? extends Element> collection) {
    	if (collection.size() == 0)
    		return false;
        for (Element element : collection) {
            this.add(element);
        }
        return true;
    }

    // methods that return a Section

    /**
     * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(final float indentation, final Paragraph title, final int numberDepth) {
    	if (isAddedCompletely()) {
    		throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document"));
    	}
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
     * @return  a new Section object
     */
    public Section addSection(final float indentation, final Paragraph title) {
        return addSection(indentation, title, numberDepth + 1);
    }

    /**
     * Creates a <CODE>Section</CODE>, add it to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(final Paragraph title, final int numberDepth) {
        return addSection(0, title, numberDepth);
    }

    /**
     * Adds a marked section. For use in class MarkedSection only!
     * @return the MarkedSection
     */
    protected MarkedSection addMarkedSection() {
    	MarkedSection section = new MarkedSection(new Section(null, numberDepth + 1));
    	add(section);
    	return section;
    }

    /**
     * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(final Paragraph title) {
        return addSection(0, title, numberDepth + 1);
    }

    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(final float indentation, final String title, final int numberDepth) {
        return addSection(indentation, new Paragraph(title), numberDepth);
    }

    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(final String title, final int numberDepth) {
        return addSection(new Paragraph(title), numberDepth);
    }

    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(final float indentation, final String title) {
        return addSection(indentation, new Paragraph(title));
    }

    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(final String title) {
        return addSection(new Paragraph(title));
    }

    // public methods

    /**
     * Sets the title of this section.
     *
     * @param	title	the new title
     */
    public void setTitle(final Paragraph title) {
        this.title = title;
    }

	/**
     * Returns the title, preceded by a certain number of sectionnumbers.
     *
     * @return	a <CODE>Paragraph</CODE>
     */
    public Paragraph getTitle() {
        return constructTitle(title, numbers, numberDepth, numberStyle);
    }

    /**
     * Constructs a Paragraph that will be used as title for a Section or Chapter.
     * @param	title	the title of the section
     * @param	numbers	a list of sectionnumbers
     * @param	numberDepth	how many numbers have to be shown
     * @param	numberStyle	the numbering style
     * @return	a Paragraph object
	 * @since	iText 2.0.8
     */
    public static Paragraph constructTitle(final Paragraph title, final ArrayList<Integer> numbers, final int numberDepth, final int numberStyle) {
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
            buf.insert(0, numbers.get(i).intValue());
        }
        if (numberStyle == NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT) {
        	buf.deleteCharAt(buf.length() - 2);
        }
        Paragraph result = new Paragraph(title);

        result.add(0, new Chunk(buf.toString(), title.getFont()));
        return result;
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
    public void setNumberDepth(final int numberDepth) {
        this.numberDepth = numberDepth;
    }

	/**
     * Returns the numberdepth of this <CODE>Section</CODE>.
     *
     * @return	the numberdepth
     */
    public int getNumberDepth() {
        return numberDepth;
    }

    /**
     * Sets the style for numbering sections.
     * Possible values are {@link Section#NUMBERSTYLE_DOTTED}: 1.2.3. (the default)
     * or {@link Section#NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT}: 1.2.3
     * @param numberStyle the style to use
	 * @since	iText 2.0.8
     */
	public void setNumberStyle(final int numberStyle) {
		this.numberStyle = numberStyle;
	}

	/**
	 * Gets the style used for numbering sections.
	 * @since	iText 2.0.8
	 * @return	a value corresponding with a numbering style
	 */
	public int getNumberStyle() {
		return numberStyle;
	}

    /**
     * Sets the indentation of this <CODE>Section</CODE> on the left side.
     *
     * @param	indentation		the indentation
     */
    public void setIndentationLeft(final float indentation) {
        indentationLeft = indentation;
    }

	/**
     * Returns the indentation of this <CODE>Section</CODE> on the left side.
     *
     * @return	the indentation
     */
    public float getIndentationLeft() {
        return indentationLeft;
    }

    /**
     * Sets the indentation of this <CODE>Section</CODE> on the right side.
     *
     * @param	indentation		the indentation
     */
    public void setIndentationRight(final float indentation) {
        indentationRight = indentation;
    }

	/**
     * Returns the indentation of this <CODE>Section</CODE> on the right side.
     *
     * @return	the indentation
     */
    public float getIndentationRight() {
        return indentationRight;
    }

    /**
     * Sets the indentation of the content of this <CODE>Section</CODE>.
     *
     * @param	indentation		the indentation
     */
    public void setIndentation(final float indentation) {
        this.indentation = indentation;
    }

	/**
     * Returns the indentation of the content of this <CODE>Section</CODE>.
     *
     * @return	the indentation
     */
    public float getIndentation() {
        return indentation;
    }

    /** Setter for property bookmarkOpen.
     * @param bookmarkOpen false if the bookmark children are not
     * visible.
     */
    public void setBookmarkOpen(final boolean bookmarkOpen) {
        this.bookmarkOpen = bookmarkOpen;
    }

    /**
     * Getter for property bookmarkOpen.
     * @return Value of property bookmarkOpen.
     */
    public boolean isBookmarkOpen() {
        return bookmarkOpen;
    }

    /**
     * Setter for property triggerNewPage.
     * @param triggerNewPage true if a new page has to be triggered.
     */
	public void setTriggerNewPage(final boolean triggerNewPage) {
		this.triggerNewPage = triggerNewPage;
	}

    /**
     * Getter for property bookmarkOpen.
     * @return Value of property triggerNewPage.
     */
    public boolean isTriggerNewPage() {
		return triggerNewPage && notAddedYet;
	}

    /**
     * Sets the bookmark title. The bookmark title is the same as the section title but
     * can be changed with this method.
     * @param bookmarkTitle the bookmark title
     */
    public void setBookmarkTitle(final String bookmarkTitle) {
        this.bookmarkTitle = bookmarkTitle;
    }

	/**
     * Gets the bookmark title.
     * @return the bookmark title
     */
    public Paragraph getBookmarkTitle() {
        if (bookmarkTitle == null)
            return getTitle();
        else
            return new Paragraph(bookmarkTitle);
    }

    /**
     * Changes the Chapter number.
     * @param number the new number
     */
    public void setChapterNumber(final int number) {
    	numbers.set(numbers.size() - 1, Integer.valueOf(number));
    	Object s;
    	for (Iterator<Element> i = iterator(); i.hasNext(); ) {
    		s = i.next();
    		if (s instanceof Section) {
    			((Section)s).setChapterNumber(number);
    		}
    	}
    }

	/**
     * Returns the depth of this section.
     *
     * @return	the depth
     */
    public int getDepth() {
        return numbers.size();
    }

    // private methods

    /**
     * Sets the number of this section.
     *
     * @param	number		the number of this section
     * @param	numbers		an <CODE>ArrayList<Integer></CODE>, containing the numbers of the Parent
     */
    private void setNumbers(final int number, final ArrayList<Integer> numbers) {
        this.numbers = new ArrayList<Integer>();
        this.numbers.add(Integer.valueOf(number));
        this.numbers.addAll(numbers);
    }

	/**
	 * Indicates if this is the first time the section is added.
	 * @since	iText2.0.8
	 * @return	true if the section wasn't added yet
	 */
	public boolean isNotAddedYet() {
		return notAddedYet;
	}

	/**
	 * Sets the indication if the section was already added to
	 * the document.
	 * @since	iText2.0.8
	 * @param notAddedYet
	 */
	public void setNotAddedYet(final boolean notAddedYet) {
		this.notAddedYet = notAddedYet;
	}

    /**
     * @return return the addedCompletely value
     * @since	iText 2.0.8
     */
    protected boolean isAddedCompletely() {
		return addedCompletely;
	}

	/**
     * @param addedCompletely true if section was completely added, false otherwise
	 * @since	iText 2.0.8
	 */
	protected void setAddedCompletely(final boolean addedCompletely) {
		this.addedCompletely = addedCompletely;
	}

	/**
	 * @since	iText 2.0.8
	 * @see com.itextpdf.text.LargeElement#flushContent()
	 */
	public void flushContent() {
		setNotAddedYet(false);
		title = null;
		Element element;
		for (Iterator<Element> i = iterator(); i.hasNext(); ) {
			element = i.next();
			if (element instanceof Section) {
				Section s = (Section)element;
				if (!s.isComplete() && size() == 1) {
					s.flushContent();
					return;
				}
				else {
					s.setAddedCompletely(true);
				}
			}
			i.remove();
		}
	}

	/**
     * @since	iText 2.0.8
	 * @see com.itextpdf.text.LargeElement#isComplete()
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
     * @since	iText 2.0.8
	 * @see com.itextpdf.text.LargeElement#setComplete(boolean)
	 */
	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	/**
	 * Adds a new page to the section.
	 * @since	2.1.1
	 */
	public void newPage() {
		this.add(Chunk.NEXTPAGE);
	}

    public PdfObject getAccessibleAttribute(final PdfName key) {
        return title.getAccessibleAttribute(key);
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        title.setAccessibleAttribute(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return title.getAccessibleAttributes();
    }

    public PdfName getRole() {
        return title.getRole();
    }

    public void setRole(final PdfName role) {
        title.setRole(role);
    }

    public AccessibleElementId getId() {
        return title.getId();
    }

    public void setId(final AccessibleElementId id) {
        title.setId(id);
    }

    public boolean isInline() {
        return false;
    }
}
