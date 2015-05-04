/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.parser;

import com.itextpdf.tool.xml.parser.state.*;

/**
 * Switches the current state in the {@link XMLParser}.
 * @author redlab_b
 *
 */
public class StateController {

	private final State unknown;
	private final State tagEncountered;
	private final State tagAttributes;
	private final State inTag;
	private final State attrValue;
	private final State singleQuoted;
	private final State doubleQuoted;
	private final State selfClosing;
	private final State specialChar;
	private final State closingTag;
	private final State comment;
	private final State closeComment;
	private final State cdata;
	private final State xml;
	private final State doctype;
	private final State unquoted;
    private final State processingInstruction;
	private final XMLParser parser;
	private State currentState;
	private State previousState;
	private State starComment;
	private State closeStarComment;

	/**
	 * Constructs a StateController with the given parser.
	 * @param parser the Parser
	 * @param isHTML true if this parser is going to parse HTML, this results in different whitespace handling.
	 */
	public StateController(final XMLParser parser, boolean isHTML) {
		this.parser = parser;
		unknown = new UnknownState(parser);
		tagEncountered = new TagEncounteredState(parser);
		tagAttributes = new TagAttributeState(parser);
		inTag = (isHTML)?new InsideTagHTMLState(parser):new InsideTagState(parser);
		attrValue = new AttributeValueState(parser);
		singleQuoted = new SingleQuotedAttrValueState(parser);
		doubleQuoted = new DoubleQuotedAttrValueState(parser);
		selfClosing = new SelfClosingTagState(parser);
		specialChar = new SpecialCharState(parser);
		closingTag = new ClosingTagState(parser);
		comment = new CommentState(parser);
		closeComment = new CloseCommentState(parser);
		cdata = new CdataState(parser);
		xml = new XmlState(parser);
		doctype = new DocTypeState(parser);
		unquoted = new UnquotedAttrState(parser);
        processingInstruction = new ProcessingInstructionEncounteredState(parser);
		previousState = null;
		currentState = null;
		starComment = new StarCommentState(parser);
		closeStarComment = new CloseStarCommentState(parser);
	}
	/**
	 *	Changes the state.
	 * @param state the state to set as current state.
	 * @return the Parser
	 */
	public XMLParser setState(final State state) {
		previousState = currentState;
		currentState = state;
		parser.setState(state);
		return parser;
	}

	/**
	 * Returns to the previous state.
	 * @return Parser
	 */
	public XMLParser previousState() {
		parser.setState(previousState);
		return parser;
	}

	/**
	 * set Parser state to {@link UnknownState}.
	 * @return Parser
	 */
	public XMLParser unknown() {
		return setState(unknown);
	}

	/**
	 * set Parser state to {@link TagEncounteredState}.
	 * @return Parser
	 */
	public XMLParser tagEncountered() {
		return setState(tagEncountered);
	}

	/**
	 * set Parser state to {@link TagAttributeState}.
	 * @return Parser
	 */
	public XMLParser tagAttributes() {
		return setState(tagAttributes);
	}

	/**
	 * set Parser state to {@link InsideTagState}.
	 * @return Parser
	 */
	public XMLParser inTag() {
		return setState(inTag);
	}

	/**
	 * set Parser state to {@link AttributeValueState}.
	 * @return Parser
	 */
	public XMLParser attributeValue() {
		return setState(attrValue);
	}

	/**
	 * set Parser state to {@link SingleQuotedAttrValueState}.
	 * @return Parser
	 */
	public XMLParser singleQuotedAttr() {
		return setState(singleQuoted);
	}

	/**
	 * set Parser state to {@link DoubleQuotedAttrValueState}.
	 * @return Parser
	 */
	public XMLParser doubleQuotedAttr() {
		return setState(doubleQuoted);
	}

    /**
	 * set Parser state to {@link ProcessingInstructionEncounteredState}.
	 * @return Parser
	 */
    public XMLParser processingInstructions() {
        return setState(processingInstruction);
    }

	/**
	 * set Parser state to {@link SelfClosingTagState}.
	 * @return Parser
	 */
	public XMLParser selfClosing() {
		return setState(selfClosing);
	}

	/**
	 *set Parser state to {@link SpecialCharState}.
	 * @return Parser
	 */
	public XMLParser specialChar() {
		return setState(this.specialChar);
	}

	/**
	 * set Parser state to {@link ClosingTagState}.
	 * @return Parser
	 */
	public XMLParser closingTag() {
		return setState(this.closingTag);
	}

	/**
	 * set Parser state to {@link CommentState}.
	 * @return Parser
	 */
	public XMLParser comment() {
		return setState(this.comment);
	}

	/**
	 * set Parser state to {@link CloseCommentState}.
	 * @return Parser
	 */
	public XMLParser closeComment() {
		return setState(closeComment);
	}

	/**
	 * set Parser state to {@link CdataState}.
	 * @return Parser
	 */
	public XMLParser cdata() {
		return setState(cdata);
	}

	/**
	 * set Parser state to {@link DocTypeState}.
	 * @return Parser
	 */
	public XMLParser doctype() {
		return setState(doctype);
	}
	/**
	 * set Parser state to {@link UnquotedAttrState}.
	 * @return Parser
	 *
	 */
	public XMLParser unquotedAttr() {
		return setState(unquoted);

	}

	/**
	 * set Parser state to {@link StarCommentState}.
	 * @return Parser
	 */
	public XMLParser starComment() {
		return setState(this.starComment);
	}

	/**
	 * set Parser state to {@link CloseStarCommentState}.
	 * @return Parser
	 */
	public XMLParser closeStarComment() {
		return setState(this.closeStarComment);
	}
}