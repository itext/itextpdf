package com.lowagie.text.rtf.direct;

import java.util.Stack;
import com.lowagie.text.rtf.direct.ctrlwords.*;

public class RtfParserState {
	public int parserState = RtfParser.PARSER_IN_UNKNOWN;
	public int tokeniserState = RtfParser.TOKENISER_STATE_IN_UNKOWN;
	public Object groupHandler = null;
	public StringBuffer text = null;
	public Stack ctrlWordHandlers = null;
	public Object ctrlWordHandler = null;
	
	public RtfPropertyDocument docProp = null;
	public RtfPropertySection sectProp = null;
	public RtfPropertyParagraph paraProp = null;
	public RtfPropertyCharacter charProp = null;
	
	public RtfDestination destination = null; //IRtfDestination currentDestination = null;

	public boolean isExtendedDestination = false;
//    chp
//    pap
//    sep
//    dop
//    rds	These were originally enums
//    ris	These were originally enums
	
	public RtfParserState() {
		this.text = new StringBuffer();
		this.ctrlWordHandlers = new Stack();
		this.docProp = new RtfPropertyDocument();
		this.sectProp = new RtfPropertySection();
		this.paraProp = new RtfPropertyParagraph();
		this.charProp = new RtfPropertyCharacter();
		this.destination = new RtfDestinationNull();
	}

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
