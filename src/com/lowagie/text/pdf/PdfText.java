/*
 * @(#)PdfText.java					0.24 2000/02/02
 *       release rugPdf0.10:		0.05 99/04/01
 *               rugPdf0.20:		0.14 99/12/01
 *               iText0.3:			0.24 2000/02/14
 *               iText0.35:         0.24 2000/08/11
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

package com.lowagie.text.pdf;

/**
 * <CODE>PdfContent</CODE> is an object containing the textual contents of a page.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 8.7 (Text state; page 339-348).
 *
 * @see		PdfContent
 * @see		PdfContents
 *
 * @author  bruno@lowagie.com
 * @version 0.24 2000/02/02
 * @since   rugPdf0.10
 */

class PdfText extends PdfContent {

// membervariables

	/** x-coordinate of the startposition of the <VAR>current line</VAR> */
	protected int offset = 0;

	/** y-coordinate of the <VAR>current point</VAR> */
	protected int y = 0;

	/** leading */
	protected int leading = 0;

// constructor

	/**
	 * Constructs a <CODE>PdfText</CODE>-object.
	 *
	 * @since		rugPdf0.20
	 */

	public PdfText() {
		super();
	}

	/**
	 * Addidtional constructor.
	 * <P>
	 * When construction a new <CODE>PdfText</CODE>-object, the first things you normally do,
	 * is move to the top of the page and set a leading:
	 * <BLOCKQUOTE><CODE>
	 * PdfText text = new PdfText();<BR>
	 * text.move(document.left(), document.top());<BR>
	 * text.setLeading(14);
	 * </CODE>
	 * </BLOCKQUOTE>
	 * This constructor allows you to do all this in once.
	 *
	 * @since		rugPdf0.20
	 */

	PdfText(int offset, int y, int leading) {
		super();
		this.offset = offset;
		this.y = y;
		this.leading = leading;
	}

// methods concerning the membervariables

	/**
		 * Returns the PDF representation of this <CODE>PdfText</CODE>-object.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.2 (page 344-345).
	 *
	 * @return		a <CODE>String</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final String toPdf() {
		String text = toString();
		if (text.length() == 0) {
			return "";
		}
		return new StringBuffer("BT\n").append(text).append("ET").toString();
		}

	/**
	 * Returns the startposition of the <VAR>current line</VAR>.
	 *
	 * @return		a coordinate
		 *
	 * @since		iText0.30
	 */

	final int offset() {
		return offset;
	}

	/**
	 * Returns the y-coordinate of the <VAR>current point</VAR>.
	 *
	 * @return		a coordinate
		 *
	 * @since		rugPdf0.10
	 */

	final int y() {
		return y;
	}

	/**
	 * Returns the leading.
	 *
	 * @return		the leading value
		 *
	 * @since		rugPdf0.10
	 */

	final int leading() {
		return leading;
	}

// methods to add textual content

	/**
		 * Adds the content of another <CODE>PdfText</CODE>-object to this object.
	 *
	 * @param		other
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void add(PdfText other) {
		offset += other.offset();
		y += other.y();
		content.append(other.toString());
		}

	/**
		 * Sets the character spacing parameter.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.1 (page 340).</P>
	 *
	 * @param		charSpace			a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setCharacterSpacing(int charSpace) {
		content.append(charSpace).append(" Tc ");
		}

	/**
		 * Sets the character spacing parameter.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.1 (page 340).</P>
	 *
	 * @param		charSpace			a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setCharacterSpacing(double charSpace) {
		content.append(charSpace).append(" Tc ");
		}

	/**
		 * Sets the word spacing parameter.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.2 (page 340-341).</P>
	 *
	 * @param		wordSpace			a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setWordSpacing(int wordSpace) {
		content.append(wordSpace).append(" Tw ");
		}

	/**
		 * Sets the word spacing parameter.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.2 (page 340-341).</P>
	 *
	 * @param		wordSpace			a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setWordSpacing(double wordSpace) {
		content.append(wordSpace).append(" Tw ");
		}

	/**
		 * Sets the horizontal scaling parameter.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.3 (page 341-342).</P>
	 *
	 * @param		scale				a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setHorizontalScaling(int scale) {
		content.append(scale).append(" Tz\n");
		}

	/**
		 * Sets the text leading parameter.
	 * <P>
	 * The leading parameter is measured in text space units. It specifies the vertical distance
	 * between the baselines of adjacent lines of text.</P>
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.4 (page 342).</P>
	 *
	 * @param		leading			the new leading
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setLeading(int leading) {
		this.leading = leading;
		content.append(leading).append(" TL\n");
		}

	/**
		 * Changes the font.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.5 (page 342).</P>
	 *
	 * @param		font		a <CODE>PdfFont</CODE>-object
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setFont(PdfFont font) {
		content.append(font.getName().toString()).append(' ').append(font.size()).append(" Tf\n");
		}

	/**
		 * Sets the text rendering parameter.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.7 (page 343-344).</P>
	 *
	 * @param		rendering				a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setRenderingMode(int rendering) {
		content.append(rendering).append(" Tr ");
		}

	/**
		 * Sets the text rise parameter.
	 * <P>
	 * This allows to write text in subscript or superscript mode.</P>
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.1.8 (page 344).</P>
	 *
	 * @param		rise				a parameter
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setTextRise(int rise) {
		content.append(rise).append(" Ts ");
		}

	/**
		 * Moves to the start of the next line, offset from the start of the current line.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.3 (page 345-346).</P>
	 *
	 * @param		x			x-coordinate of the new current point
	 * @param		y			y-coordinate of the new current point
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void move(float x, float y) {
		this.offset += x;
		this.y += y;
		content.append(x).append(' ').append(y).append(" Td\n");
		}

	/**
		 * Moves to the start of the next line, offset from the start of the current line.
	 * <P>
	 * As a side effect, this sets the leading parameter in the text state.</P>
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.3 (page 345-346).</P>
	 *
	 * @param		x			offset of the new current point
	 * @param		y			y-coordinate of the new current point
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void moveWithLeading(int x, int y) {
		this.offset += x;
		this.y += y;
		leading = -y;
		content.append(x).append(' ').append(y).append(" TD\n");
		}

	/**
		 * Changes the text matrix.
	 * <P>
	 * Remark: this operation also initializes the current point position.</P>
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.3 (page 345-346).</P>
	 *
	 * @param		a			operand 1,1 in the matrix
	 * @param		b			operand 1,2 in the matrix
	 * @param		c			operand 2,1 in the matrix
	 * @param		d			operand 2,2 in the matrix
	 * @param		x			operand 3,1 in the matrix
	 * @param		y			operand 3,2 in the matrix
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void setMatrix(int a, int b, int c, int d, int x, int y) {
		content.append(a).append(' ').append(b).append(' ').append(c).append(' ').append(d).append(' ').append(x).append(' ').append(y).append(" Tm\n");
		}

	/**
		 * Moves to the start of the next line.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.3 (page 345-346).</P>
	 *
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void newLine() {
		this.y -= leading;
		content.append("T* ");
		}

	/**
		 * Shows text string, using the character and word spacing parameters from the text state.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		text			a <CODE>PdfPrintable</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void show(PdfPrintable text) {
		content.append(new PdfString(text).get()).append(" Tj\n");
		}

	/**
		 * Shows text string, using the character and word spacing parameters from the text state.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		text			a <CODE>String</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void show(String text) {
		content.append(new PdfString(text).get()).append(" Tj\n");
		}

	/**
		 * Moves to the next line and shows text string, using the character and word spacing parameters from the text state.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		text			a <CODE>PdfPrintable</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void newLineShow(PdfPrintable text) {
		this.y -= leading;
		content.append(new PdfString(text).get()).append(" '\n");
		}

	/**
		 * Moves to the next line and shows text string, using the character and word spacing parameters from the text state.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		text			a <CODE>String</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
	 */

		final void newLineShow(String text) {
		this.y -= leading;
		content.append(new PdfString(text).get()).append(" '\n");
		}

	/**
		 * Moves to the next line and shows text string, using the given values of the character and word spacing parameters.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		wordSpacing		a parameter
	 * @param		charSpacing		a parameter
	 * @param		text			a <CODE>PdfPrintable</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void newLineShow(int wordSpacing, int charSpacing, PdfPrintable text) {
		this.y -= leading;
		content.append(wordSpacing).append(' ').append(charSpacing).append(' ').append(new PdfString(text).get()).append(" \"\n");
		}

	/**
		 * Moves to the next line and shows text string, using the given values of the character and word spacing parameters.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		wordSpacing		a parameter
	 * @param		charSpacing		a parameter
	 * @param		text			a <CODE>String</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void newLineShow(int wordSpacing, int charSpacing, String text) {
		this.y -= leading;
		content.append(wordSpacing).append(' ').append(charSpacing).append(' ').append(new PdfString(text).get()).append(" \"\n");
		}

	/**
		 * Show with displacements.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.7.5 (page 346-347).
	 *
	 * @param		text			a <CODE>PdfArray</CODE>
	 * @return		<CODE>void</CODE>
		 *
	 * @since		rugPdf0.10
		 */

		final void show(PdfTextArray text) {
		content.append(text.toString()).append(" TJ\n");
		}
}