package com.lowagie.text.rtf.direct;

import java.util.Stack;
import com.lowagie.text.rtf.direct.ctrlwords.*;

/**
 * The <code>RtfParserState</code> contains the state information
 * for the parser. The current state object is pushed/popped in a stack
 * when a group change is made.
 * 
 * When an open group is encountered, the current state is copied and 
 * then pushed on the top of the stack
 * When a close group is encountered, the current state is overwritten with
 * the popped value from the top of the stack 
 * 
 * @author Howard Shank (hgshank@yahoo.com)
 */
public class RtfParserState {
	/**
	 * The parser state.
	 */
	public int parserState = RtfParser.PARSER_IN_UNKNOWN;
	/**
	 * The tokeniser state.
	 */
	public int tokeniserState = RtfParser.TOKENISER_STATE_IN_UNKOWN;
	/**
	 * The control word set as the group handler. 
	 */
	public Object groupHandler = null;
	/**
	 * The parsed value for the current group/control word.
	 */
	public StringBuffer text = null;
	/**
	 * Stack containing control word handlers. There could be multiple
	 * control words in a group.
	 */
	public Stack ctrlWordHandlers = null;
	/**
	 * The current control word handler.
	 */
	public Object ctrlWordHandler = null;
	/**
	 * The Document Properties.
	 */
	public RtfPropertyDocument docProp = null;
	/**
	 * The section properties.
	 */
	public RtfPropertySection sectProp = null;
	/**
	 * The paragraph properties.
	 */
	public RtfPropertyParagraph paraProp = null;
	/**
	 * The character properties.
	 */
	public RtfPropertyCharacter charProp = null;
	
	/**
	 * The current destination.
	 */
	public RtfDestination destination = null; //IRtfDestination currentDestination = null;
	/**
	 * Flag indicating if this is an extended destination \* control word
	 */
	public boolean isExtendedDestination = false;
	
	/**
	 * Default constructor
	 *
	 */
	public RtfParserState() {
		this.text = new StringBuffer();
		this.ctrlWordHandlers = new Stack();
		this.docProp = new RtfPropertyDocument();
		this.sectProp = new RtfPropertySection();
		this.paraProp = new RtfPropertyParagraph();
		this.charProp = new RtfPropertyCharacter();
		this.destination = new RtfDestinationNull();
	}
	/**
	 * Copy constructor
	 * @param orig The object to copy
	 */
	public RtfParserState(RtfParserState orig) {
		this.parserState = orig.parserState;
		this.tokeniserState = orig.tokeniserState;
		this.groupHandler = null;
		this.destination = orig.destination;
		this.text = new StringBuffer();
		this.ctrlWordHandlers = new Stack();
		this.docProp = orig.docProp;
		this.sectProp = orig.sectProp;
		this.paraProp = orig.paraProp;
		this.charProp = orig.charProp;
		this.destination = orig.destination;
	}
	
}
