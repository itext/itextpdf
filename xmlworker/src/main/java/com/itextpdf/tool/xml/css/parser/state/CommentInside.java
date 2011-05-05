package com.itextpdf.tool.xml.css.parser.state;

import com.itextpdf.tool.xml.css.parser.CssStateController;
import com.itextpdf.tool.xml.css.parser.State;

/**
 * @author redlab_b
 *
 */
public class CommentInside implements State {

	private final CssStateController controller;

	/**
	 * @param cssStateController
	 */
	public CommentInside(final CssStateController cssStateController) {
		this.controller = cssStateController;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.css.parser.State#process(char)
	 */
	public void process(final char c) {
		if ('*' == c) {
			controller.stateCommentEnd();
		}

	}

}
