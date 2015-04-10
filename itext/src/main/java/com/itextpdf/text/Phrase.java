/*
 * $Id: Phrase.java 6134 2013-12-23 13:15:14Z blowagie $
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

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.HyphenationEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A <CODE>Phrase</CODE> is a series of <CODE>Chunk</CODE>s.
 * <P>
 * A <CODE>Phrase</CODE> has a main <CODE>Font</CODE>, but some chunks
 * within the phrase can have a <CODE>Font</CODE> that differs from the
 * main <CODE>Font</CODE>. All the <CODE>Chunk</CODE>s in a <CODE>Phrase</CODE>
 * have the same <CODE>leading</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // When no parameters are passed, the default leading = 16
 * <STRONG>Phrase phrase0 = new Phrase();</STRONG>
 * <STRONG>Phrase phrase1 = new Phrase("this is a phrase");</STRONG>
 * // In this example the leading is passed as a parameter
 * <STRONG>Phrase phrase2 = new Phrase(16, "this is a phrase with leading 16");</STRONG>
 * // When a Font is passed (explicitly or embedded in a chunk), the default leading = 1.5 * size of the font
 * <STRONG>Phrase phrase3 = new Phrase("this is a phrase with a red, normal font Courier, size 12", FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL, new Color(255, 0, 0)));</STRONG>
 * <STRONG>Phrase phrase4 = new Phrase(new Chunk("this is a phrase"));</STRONG>
 * <STRONG>Phrase phrase5 = new Phrase(18, new Chunk("this is a phrase", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Chunk
 * @see		Paragraph
 * @see		Anchor
 */

public class Phrase extends ArrayList<Element> implements TextElementArray {

    // constants
	private static final long serialVersionUID = 2643594602455068231L;

	// membervariables
	/** This is the leading of this phrase. */
    protected float leading = Float.NaN;

    /** The text leading that is multiplied by the biggest font size in the line. */
    protected float multipliedLeading = 0;

    /** This is the font of this phrase. */
    protected Font font;

    /** Null, unless the Phrase has to be hyphenated.
     * @since	2.1.2
     */
    protected HyphenationEvent hyphenation = null;

    /**
     * Predefined tab position and properties(alignment, leader and etc.);
     * @since	5.4.1
     */
    protected TabSettings tabSettings = null;

    // constructors

    /**
     * Constructs a <CODE>Phrase</CODE> without specifying a leading.
     */
    public Phrase() {
        this(16);
    }

    /**
     * Copy constructor for <CODE>Phrase</CODE>.
     * @param phrase the Phrase to copy
     */
    public Phrase(final Phrase phrase) {
        super();
        this.addAll(phrase);
        setLeading(phrase.getLeading(), phrase.getMultipliedLeading());
        font = phrase.getFont();
        tabSettings = phrase.getTabSettings();
        setHyphenation(phrase.getHyphenation());
    }

	/**
     * Constructs a <CODE>Phrase</CODE> with a certain leading.
     *
     * @param	leading		the leading
     */
    public Phrase(final float leading) {
        this.leading = leading;
        font = new Font();
    }

    /**
     * Constructs a <CODE>Phrase</CODE> with a certain <CODE>Chunk</CODE>.
     *
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public Phrase(final Chunk chunk) {
        super.add(chunk);
        font = chunk.getFont();
        setHyphenation(chunk.getHyphenation());
    }

	/**
     * Constructs a <CODE>Phrase</CODE> with a certain <CODE>Chunk</CODE>
     * and a certain leading.
     *
     * @param	leading	the leading
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public Phrase(final float leading, final Chunk chunk) {
        this.leading = leading;
        super.add(chunk);
        font = chunk.getFont();
        setHyphenation(chunk.getHyphenation());
    }

    /**
     * Constructs a <CODE>Phrase</CODE> with a certain <CODE>String</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     */
    public Phrase(final String string) {
        this(Float.NaN, string, new Font());
    }

    /**
     * Constructs a <CODE>Phrase</CODE> with a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public Phrase(final String string, final Font font) {
        this(Float.NaN, string, font);
    }

    /**
     * Constructs a <CODE>Phrase</CODE> with a certain leading and a certain <CODE>String</CODE>.
     *
     * @param	leading	the leading
     * @param	string		a <CODE>String</CODE>
     */
    public Phrase(final float leading, final String string) {
        this(leading, string, new Font());
    }

    /**
     * Constructs a <CODE>Phrase</CODE> with a certain leading, a certain <CODE>String</CODE>
     * and a certain <CODE>Font</CODE>.
     *
     * @param	leading	the leading
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public Phrase(final float leading, final String string, final Font font) {
        this.leading = leading;
        this.font = font;
    	/* bugfix by August Detlefsen */
        if (string != null && string.length() != 0) {
            super.add(new Chunk(string, font));
        }
    }

    // implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(final ElementListener listener) {
        try {
            for (Object element : this) {
                listener.add((Element) element);
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
        return Element.PHRASE;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public java.util.List<Chunk> getChunks() {
    	java.util.List<Chunk> tmp = new ArrayList<Chunk>();
        for (Element element : this) {
            tmp.addAll(element.getChunks());
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
		return true;
	}

    // overriding some of the ArrayList-methods

    /**
     * Adds a <CODE>Chunk</CODE>, an <CODE>Anchor</CODE> or another <CODE>Phrase</CODE>
     * to this <CODE>Phrase</CODE>.
     *
     * @param	index	index at which the specified element is to be inserted
     * @param	element	an object of type <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
     * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
     * @since 5.0.1 (signature changed to use Element)
     */
    @Override
    public void add(final int index, final Element element) {
    	if (element == null) return;
        switch (element.type()) {
        case Element.CHUNK:
            Chunk chunk = (Chunk) element;
            if (!font.isStandardFont()) {
                chunk.setFont(font.difference(chunk.getFont()));
            }
            if (hyphenation != null && chunk.getHyphenation() == null && !chunk.isEmpty()) {
                chunk.setHyphenation(hyphenation);
            }
            super.add(index, chunk);
            return;
        case Element.PHRASE:
        case Element.PARAGRAPH:
        case Element.MARKED:
        case Element.DIV:
        case Element.ANCHOR:
        case Element.ANNOTATION:
        case Element.PTABLE:
        case Element.LIST:
        case Element.YMARK:
        case Element.WRITABLE_DIRECT:
            super.add(index, element);
	        return;
	    default:
	        throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", element.getClass().getName()));
        }
    }

    /**
     * Adds a <CODE>String</CODE> to this <CODE>Phrase</CODE>.
     *
     * @param   s       a string
     * @return  a boolean
     * @since 5.0.1
     */
    public boolean add(final String s) {
        if (s == null) {
            return false;
        }
        return super.add(new Chunk(s, font));
    }

    /**
     * Adds a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or another <CODE>Phrase</CODE>
     * to this <CODE>Phrase</CODE>.
     *
     * @param   element       an object of type <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
     * @return  a boolean
     * @throws  ClassCastException      when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
     * @since 5.0.1 (signature changed to use Element)
     */
    @Override
    public boolean add(final Element element) {
        if (element == null) return false;
        try {
        	// TODO same as in document - change switch to generic adding that works everywhere
            switch(element.type()) {
                case Element.CHUNK:
                    return addChunk((Chunk) element);
                case Element.PHRASE:
                case Element.PARAGRAPH:
                    Phrase phrase = (Phrase) element;
                    boolean success = true;
                    Element e;
	                for (Object element2 : phrase) {
	                    e = (Element) element2;
	                    if (e instanceof Chunk) {
	                        success &= addChunk((Chunk)e);
	                    }
	                    else {
	                        success &= this.add(e);
	                    }
	                }
                    return success;
                case Element.MARKED:
                case Element.DIV:
                case Element.ANCHOR:
                case Element.ANNOTATION:
                case Element.PTABLE: // case added by mr. Karen Vardanyan
                case Element.LIST:
                case Element.YMARK:
                case Element.WRITABLE_DIRECT:
                        return super.add(element);
                    default:
                        throw new ClassCastException(String.valueOf(element.type()));
            }
        }
        catch(ClassCastException cce) {
            throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", cce.getMessage()));
        }
    }

    /**
     * Adds a collection of <CODE>Chunk</CODE>s
     * to this <CODE>Phrase</CODE>.
     *
     * @param	collection	a collection of <CODE>Chunk</CODE>s, <CODE>Anchor</CODE>s and <CODE>Phrase</CODE>s.
     * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
     * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
     */
    @Override
    public boolean addAll(final Collection<? extends Element> collection) {
        for (Element e: collection) {
            this.add(e);
        }
        return true;
    }

    /**
     * Adds a Chunk.
     * <p>
     * This method is a hack to solve a problem I had with phrases that were split between chunks
     * in the wrong place.
     * @param chunk a Chunk to add to the Phrase
     * @return true if adding the Chunk succeeded
     */
    protected boolean addChunk(final Chunk chunk) {
    	Font f = chunk.getFont();
    	String c = chunk.getContent();
        if (font != null && !font.isStandardFont()) {
            f = font.difference(chunk.getFont());
        }
        if (size() > 0 && !chunk.hasAttributes()) {
            try {
                Chunk previous = (Chunk) get(size() - 1);
                if (!previous.hasAttributes()
                		&& (f == null
                		|| f.compareTo(previous.getFont()) == 0)
                		&& !"".equals(previous.getContent().trim())
                		&& !"".equals(c.trim())) {
                    previous.append(c);
                    return true;
                }
            }
            catch(ClassCastException cce) {
            }
        }
        Chunk newChunk = new Chunk(c, f);
        newChunk.setAttributes(chunk.getAttributes());
        newChunk.role = chunk.getRole();
        newChunk.accessibleAttributes = chunk.getAccessibleAttributes();
        if (hyphenation != null && newChunk.getHyphenation() == null && !newChunk.isEmpty()) {
        	newChunk.setHyphenation(hyphenation);
        }
        return super.add(newChunk);
    }

    /**
     * Adds an <CODE>Element</CODE> to the <CODE>Paragraph</CODE>.
     *
     * @param	object		the object to add.
     */
    protected void addSpecial(final Element object) {
        super.add(object);
    }

    // other methods that change the member variables

    /**
     * Sets the leading fixed and variable. The resultant leading will be
     * fixedLeading+multipliedLeading*maxFontSize where maxFontSize is the
     * size of the biggest font in the line.
     * @param fixedLeading the fixed leading
     * @param multipliedLeading the variable leading
     */
    public void setLeading(final float fixedLeading, final float multipliedLeading) {
        this.leading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
    }

    /**
     * @see com.itextpdf.text.Phrase#setLeading(float)
     */
    public void setLeading(final float fixedLeading) {
        this.leading = fixedLeading;
        this.multipliedLeading = 0;
    }

    /**
     * Sets the variable leading. The resultant leading will be
     * multipliedLeading*maxFontSize where maxFontSize is the
     * size of the biggest font in the line.
     * @param multipliedLeading the variable leading
     */
    public void setMultipliedLeading(final float multipliedLeading) {
        this.leading = 0;
        this.multipliedLeading = multipliedLeading;
    }

    /**
     * Sets the main font of this phrase.
     * @param font	the new font
     */
    public void setFont(final Font font) {
    	this.font = font;
    }

    // methods to retrieve information

	/**
     * Gets the leading of this phrase.
     *
     * @return	the linespacing
     */
    public float getLeading() {
        if (Float.isNaN(leading) && font != null) {
            return font.getCalculatedLeading(1.5f);
        }
        return leading;
    }

    /**
     * Gets the variable leading
     * @return the leading
     */
    public float getMultipliedLeading() {
        return multipliedLeading;
    }

    /**
     * Gets the total leading.
     * This method is based on the assumption that the
     * font of the Paragraph is the font of all the elements
     * that make part of the paragraph. This isn't necessarily
     * true.
     * @return the total leading (fixed and multiplied)
     */
    public float getTotalLeading() {
        float m = font == null ?
                Font.DEFAULTSIZE * multipliedLeading : font.getCalculatedLeading(multipliedLeading);
        if (m > 0 && !hasLeading()) {
            return m;
        }
        return getLeading() + m;
    }

    /**
     * Checks you if the leading of this phrase is defined.
     *
     * @return	true if the leading is defined
     */
    public boolean hasLeading() {
        if (Float.isNaN(leading)) {
            return false;
        }
        return true;
    }

	/**
     * Gets the font of the first <CODE>Chunk</CODE> that appears in this <CODE>Phrase</CODE>.
     *
     * @return	a <CODE>Font</CODE>
     */
    public Font getFont() {
        return font;
    }

	/**
     * Returns the content as a String object.
     * This method differs from toString because toString will return an ArrayList with the toString value of the Chunks in this Phrase.
	 * @return the content
     */
    public String getContent() {
    	StringBuffer buf = new StringBuffer();
    	for (Chunk c: getChunks()) {
    		buf.append(c.toString());
    	}
    	return buf.toString();
    }

    /**
     * Checks is this <CODE>Phrase</CODE> contains no or 1 empty <CODE>Chunk</CODE>.
     *
     * @return	<CODE>false</CODE> if the <CODE>Phrase</CODE>
     * contains more than one or more non-empty<CODE>Chunk</CODE>s.
     */
    @Override
    public boolean isEmpty() {
        switch(size()) {
            case 0:
                return true;
            case 1:
                Element element = get(0);
                if (element.type() == Element.CHUNK && ((Chunk) element).isEmpty()) {
                    return true;
                }
                return false;
                default:
                    return false;
        }
    }

    /**
     * Getter for the hyphenation settings.
     * @return	a HyphenationEvent
     * @since	2.1.2
     */
    public HyphenationEvent getHyphenation() {
		return hyphenation;
	}

    /**
     * Setter for the hyphenation.
     * @param	hyphenation	a HyphenationEvent instance
     * @since	2.1.2
     */
	public void setHyphenation(final HyphenationEvent hyphenation) {
		this.hyphenation = hyphenation;
	}

    /**
     * Getter for the tab stops settings.
     * @return	a HyphenationEvent
     * @since	5.4.1
     */
    public TabSettings getTabSettings() {
        return tabSettings;
    }

    /**
     * Setter for the tab stops.
     * @param	tabSettings tab settings
     * @since	5.4.1
     */
    public void setTabSettings(TabSettings tabSettings) {
        this.tabSettings = tabSettings;
    }

    // kept for historical reasons; people should use FontSelector
    // eligible for deprecation, but the methods are mentioned in the book p277.

    /**
     * Constructs a Phrase that can be used in the static getInstance() method.
     * @param	dummy	a dummy parameter
     */
    private Phrase(final boolean dummy) {
    }

    /**
     * Gets a special kind of Phrase that changes some characters into corresponding symbols.
     * @param string
     * @return a newly constructed Phrase
     */
    public static final Phrase getInstance(final String string) {
    	return getInstance(16, string, new Font());
    }

    /**
     * Gets a special kind of Phrase that changes some characters into corresponding symbols.
     * @param leading
     * @param string
     * @return a newly constructed Phrase
     */
    public static final Phrase getInstance(final int leading, final String string) {
    	return getInstance(leading, string, new Font());
    }

    /**
     * Gets a special kind of Phrase that changes some characters into corresponding symbols.
     * @param leading
     * @param string
     * @param font
     * @return a newly constructed Phrase
     */
    public static final Phrase getInstance(final int leading, String string, final Font font) {
    	Phrase p = new Phrase(true);
    	p.setLeading(leading);
    	p.font = font;
    	if (font.getFamily() != FontFamily.SYMBOL && font.getFamily() != FontFamily.ZAPFDINGBATS && font.getBaseFont() == null) {
            int index;
            while((index = SpecialSymbol.index(string)) > -1) {
                if (index > 0) {
                    String firstPart = string.substring(0, index);
                    p.add(new Chunk(firstPart, font));
                    string = string.substring(index);
                }
                Font symbol = new Font(FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
                StringBuffer buf = new StringBuffer();
                buf.append(SpecialSymbol.getCorrespondingSymbol(string.charAt(0)));
                string = string.substring(1);
                while (SpecialSymbol.index(string) == 0) {
                    buf.append(SpecialSymbol.getCorrespondingSymbol(string.charAt(0)));
                    string = string.substring(1);
                }
                p.add(new Chunk(buf.toString(), symbol));
            }
        }
        if (string != null && string.length() != 0) {
        	p.add(new Chunk(string, font));
        }
    	return p;
    }

    public boolean trim() {
        while (this.size() > 0) {
            Element firstChunk = this.get(0);
            if (firstChunk instanceof Chunk && ((Chunk)firstChunk).isWhitespace()) {
                this.remove(firstChunk);
            } else {
                break;
            }
        }
        while (this.size() > 0) {
            Element lastChunk = this.get(this.size() - 1);
            if (lastChunk instanceof Chunk && ((Chunk)lastChunk).isWhitespace()) {
                this.remove(lastChunk);
            } else {
                break;
            }
        }
        return size() > 0;
    }

}
