package com.itextpdf.tool.xml.css.parser.state;

import com.itextpdf.tool.xml.css.parser.CssStateController;
import com.itextpdf.tool.xml.css.parser.State;

/**
 * @author redlab_b
 *
 */
public class CommentEnd implements State {

	private final CssStateController controller;

	/**
	 * @param controller
	 */
	public CommentEnd(final CssStateController controller) {
		this.controller = controller;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.css.parser.State#process(char)
	 */
	public void process(final char c) {
		if ('/' == c) {
			this.controller.previous();
		} else {
			this.controller.stateCommentInside();
		}

	}

}
