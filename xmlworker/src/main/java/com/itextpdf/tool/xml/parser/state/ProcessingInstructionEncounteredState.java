package com.itextpdf.tool.xml.parser.state;

import com.itextpdf.tool.xml.parser.XMLParser;

/**
 *
 */
public class ProcessingInstructionEncounteredState extends TagAttributeState {

    protected String name = null;

	/**
	 * @param parser the XMLParser
	 */
	public ProcessingInstructionEncounteredState(final XMLParser parser) {
		super(parser);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.parser.State#process(int)
	 */
    public void process(final char character) {
        String tag = this.parser.bufferToString();
        if (name == null && Character.isWhitespace(character)) {
            if (Character.isWhitespace(character)) {
                name = tag;
                this.parser.memory().currentTag(name);
                this.parser.flush();
            } else if (character == '?') {
                this.parser.flush();
                this.parser.selectState().selfClosing();
            }
        } else {
            if (Character.isWhitespace(character)) {
                checkAttributeWithNoValue();
			    this.parser.flush();
            } else if (character == '?') {
                name = null;
                checkAttributeWithNoValue();
                this.parser.flush();
                this.parser.selectState().selfClosing();
            } else {
                this.parser.append(character);
            }
        }
    }
}
