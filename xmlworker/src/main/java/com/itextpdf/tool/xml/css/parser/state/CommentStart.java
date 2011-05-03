package com.itextpdf.tool.xml.css.parser.state;

import com.itextpdf.tool.xml.css.parser.CssStateController;
import com.itextpdf.tool.xml.css.parser.State;

/**
 * @author Balder Van Camp
 *
 */
public class CommentStart implements State {

	private final CssStateController controller;
	/**
	 *
	 */
	public CommentStart(final CssStateController controller) {
		this.controller = controller;
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.parser.State#process(int)
	 */
	public void process(final char c) {
		if ('*' == c) {
			this.controller.stateCommentInside();
		} else {
			this.controller.append('/');
			this.controller.append(c);
			this.controller.previous();
		}
	}

}
