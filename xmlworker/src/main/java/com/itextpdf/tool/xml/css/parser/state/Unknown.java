package com.itextpdf.tool.xml.css.parser.state;

import com.itextpdf.tool.xml.css.parser.CssStateController;
import com.itextpdf.tool.xml.css.parser.State;

/**
 * @author Balder Van Camp
 *
 */
public class Unknown implements State {

	private final CssStateController controller;

	/**
	 * @param cssStateController
	 */
	public Unknown(final CssStateController cssStateController) {
		this.controller= cssStateController;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.parser.State#process(char)
	 */
	public void process(final char c) {
		if ('/' == c) {
			controller.stateCommentStart();
		} else if ('{' == c) {
			controller.storeSelector();
			controller.stateProperties();
		} else if ('@' == c) {
			controller.stateRule();
		} else {
			controller.append(c);
		}

	}

}
