/*
 * $Id: Anchor.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * An <CODE>Anchor</CODE> can be a reference or a destination of a reference.
 * <P>
 * An <CODE>Anchor</CODE> is a special kind of <CODE>Phrase</CODE>.
 * It is constructed in the same way.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Anchor anchor = new Anchor("this is a link");</STRONG>
 * <STRONG>anchor.setName("LINK");</STRONG>
 * <STRONG>anchor.setReference("http://www.lowagie.com");</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Phrase
 */

public class Anchor extends Phrase {

	// constant
	private static final long serialVersionUID = -852278536049236911L;

    // membervariables

	/** This is the name of the <CODE>Anchor</CODE>. */
    protected String name = null;

    /** This is the reference of the <CODE>Anchor</CODE>. */
    protected String reference = null;

    // constructors

    /**
     * Constructs an <CODE>Anchor</CODE> without specifying a leading.
     */
    public Anchor() {
        super(16);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain leading.
     *
     * @param	leading		the leading
     */

    public Anchor(final float leading) {
        super(leading);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>.
     *
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public Anchor(final Chunk chunk) {
        super(chunk);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     */
    public Anchor(final String string) {
        super(string);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>
     * and a certain <CODE>Font</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public Anchor(final String string, final Font font) {
        super(string, font);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public Anchor(final float leading, final Chunk chunk) {
        super(leading, chunk);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain leading
     * and a certain <CODE>String</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     */
    public Anchor(final float leading, final String string) {
        super(leading, string);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain leading,
     * a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public Anchor(final float leading, final String string, final Font font) {
        super(leading, string, font);
    }

    /**
     * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Phrase</CODE>.
     *
     * @param	phrase		a <CODE>Phrase</CODE>
     */
    public Anchor(final Phrase phrase) {
    	super(phrase);
    	if (phrase instanceof Anchor) {
    		Anchor a = (Anchor) phrase;
    		setName(a.name);
    		setReference(a.reference);
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
    @Override
    public boolean process(final ElementListener listener) {
        try {
            Chunk chunk;
            Iterator<Chunk> i = getChunks().iterator();
            boolean localDestination = reference != null && reference.startsWith("#");
            boolean notGotoOK = true;
            while (i.hasNext()) {
                chunk = i.next();
                if (name != null && notGotoOK && !chunk.isEmpty()) {
                    chunk.setLocalDestination(name);
                    notGotoOK = false;
                }
                if (localDestination) {
                    chunk.setLocalGoto(reference.substring(1));
                }
                listener.add(chunk);
            }
            return true;
        }
        catch(DocumentException de) {
            return false;
        }
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    @Override
    public List<Chunk> getChunks() {
        boolean localDestination = reference != null && reference.startsWith("#");
        boolean notGotoOK = true;
    	List<Chunk> tmp = new ArrayList<Chunk>();
        Iterator<Element> i = iterator();
        Element element;
        while (i.hasNext()) {
            element = (Element) i.next();
            if (element instanceof Chunk) {
            	Chunk chunk = (Chunk)element;
            	notGotoOK = applyAnchor(chunk, notGotoOK, localDestination);
                tmp.add(chunk);
            }
            else {
            	for (Chunk c : element.getChunks()) {
            		notGotoOK = applyAnchor(c, notGotoOK, localDestination);
            		tmp.add(c);
            	}
            }
        }
        return tmp;
    }
    
    /**
     * Applies the properties of the Anchor to a Chunk.
     * @param chunk			the Chunk (part of the Anchor)
     * @param notGotoOK		if true, this chunk will determine the local destination
     * @param localDestination	true if the chunk is a local goto and the reference a local destination
     * @return	the value of notGotoOK or false, if a previous Chunk was used to determine the local destination
     */
    protected boolean applyAnchor(Chunk chunk, boolean notGotoOK, boolean localDestination) {
        if (name != null && notGotoOK && !chunk.isEmpty()) {
            chunk.setLocalDestination(name);
            notGotoOK = false;
        }
        if (localDestination) {
            chunk.setLocalGoto(reference.substring(1));
        }
        else if (reference != null)
            chunk.setAnchor(reference);
        return notGotoOK;
    }

    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    @Override
    public int type() {
        return Element.ANCHOR;
    }

    // methods

    /**
     * Sets the name of this <CODE>Anchor</CODE>.
     *
     * @param	name		a new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the reference of this <CODE>Anchor</CODE>.
     *
     * @param	reference		a new reference
     */
    public void setReference(final String reference) {
        this.reference = reference;
    }

    // methods to retrieve information

	/**
     * Returns the name of this <CODE>Anchor</CODE>.
     *
     * @return	a name
     */
    public String getName() {
        return name;
    }

	/**
     * Gets the reference of this <CODE>Anchor</CODE>.
     *
     * @return	a reference
     */
    public String getReference() {
        return reference;
    }

	/**
     * Gets the reference of this <CODE>Anchor</CODE>.
     *
     * @return	an <CODE>URL</CODE>
     */
    public URL getUrl() {
        try {
            return new URL(reference);
        }
        catch(MalformedURLException mue) {
            return null;
        }
    }

}
