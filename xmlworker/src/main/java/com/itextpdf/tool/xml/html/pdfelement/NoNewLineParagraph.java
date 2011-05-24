/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.html.pdfelement;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

/**
 * A paragraph that sets {@link Paragraph#setNewLine(boolean)} to false.
 * @author itextpdf.com
 *
 */
public class NoNewLineParagraph extends Paragraph {

	 /**
	 *
	 */
	private static final long serialVersionUID = -8392940968188620772L;

	/**
     * Constructs a <CODE>Paragraph</CODE>.
     */
    public NoNewLineParagraph() {
        super();
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain leading.
     *
     * @param	leading		the leading
     */
    public NoNewLineParagraph(final float leading) {
        super(leading);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>Chunk</CODE>.
     *
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public NoNewLineParagraph(final Chunk chunk) {
        super(chunk);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>Chunk</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public NoNewLineParagraph(final float leading, final Chunk chunk) {
        super(leading, chunk);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>String</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     */
    public NoNewLineParagraph(final String string) {
        super(string);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>String</CODE>
     * and a certain <CODE>Font</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public NoNewLineParagraph(final String string, final Font font) {
        super(string, font);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>String</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     */
    public NoNewLineParagraph(final float leading, final String string) {
        super(leading, string);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain leading, <CODE>String</CODE>
     * and <CODE>Font</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public NoNewLineParagraph(final float leading, final String string, final Font font) {
        super(leading, string, font);
        this.setNewLine(false);
    }

    /**
     * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>Phrase</CODE>.
     *
     * @param	phrase		a <CODE>Phrase</CODE>
     */
    public NoNewLineParagraph(final Phrase phrase) {
        super(phrase);
        this.setNewLine(false);
        if (phrase instanceof Paragraph) {
        	Paragraph p = (Paragraph)phrase;
        	setAlignment(p.getAlignment());
        	setLeading(phrase.getLeading(), p.getMultipliedLeading());
        	setIndentationLeft(p.getIndentationLeft());
        	setIndentationRight(p.getIndentationRight());
        	setFirstLineIndent(p.getFirstLineIndent());
        	setSpacingAfter(p.getSpacingAfter());
        	setSpacingBefore(p.getSpacingBefore());
        	setExtraParagraphSpace(p.getExtraParagraphSpace());
        }
    }


}
