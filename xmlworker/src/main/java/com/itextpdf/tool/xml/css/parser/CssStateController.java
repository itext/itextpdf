package com.itextpdf.tool.xml.css.parser;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.parser.state.CommentEnd;
import com.itextpdf.tool.xml.css.parser.state.CommentInside;
import com.itextpdf.tool.xml.css.parser.state.CommentStart;
import com.itextpdf.tool.xml.css.parser.state.Properties;
import com.itextpdf.tool.xml.css.parser.state.Rule;
import com.itextpdf.tool.xml.css.parser.state.Unknown;

/**
 * @author redlab_b
 *
 */
public class CssStateController {

	private State current;
	private State previous;
	private final State commentEnd;
	private final State commentStart;
	private final State commentInside;
	private final StringBuilder buffer;
	private final State properties;
	private final State unknown;
	private String currentSelector;
	private final State rule;
	private final CssUtils utils;
	private final CssFile css;

	/**
	 *
	 */
	public CssStateController(final CssFile file) {
		this.css = file;
		utils = CssUtils.getInstance();
		buffer = new StringBuilder();
		commentStart = new CommentStart(this);
		commentEnd = new CommentEnd(this);
		commentInside = new CommentInside(this);
		unknown = new Unknown(this);
		properties = new Properties(this);
		rule = new Rule(this);
		current = unknown;
	}

	/**
	 * Sets the state to the previous state
	 */
	public void previous() {
		this.current = previous;
	}

	/**
	 * @param c
	 */
	public void append(final char c) {
		this.buffer.append(c);

	}

	/**
	 *
	 */
	public void stateCommentEnd() {
		setState(commentEnd);
	}

	/**
	 * @param commentEnd2
	 */
	private void setState(final State state) {
		this.current = state;
	}

	private void setPrevious() {
		this.previous = current;
	}

	/**
	 *
	 */
	public void stateCommentInside() {
		setState(commentInside);
	}

	/**
	 *
	 */
	public void stateCommentStart() {
		setPrevious();
		setState(commentStart);
	}

	/**
	 *
	 */
	public void stateProperties() {
		previous = current;
		setState(properties);
	}

	/**
	 *
	 */
	public void storeSelector() {
		this.currentSelector = buffer.toString();
		buffer.setLength(0);
	}

	/**
	 *
	 */
	public void storeProperties() {
		processProps(buffer.toString());
		buffer.setLength(0);
	}

	/**
	 *
	 */
	public void stateUnknown() {
		setState(unknown);
	}

	/**
	 *
	 */
	public void stateRule() {
		setState(rule);
	}

	private void processProps(final String props) {
		String[] split = props.split(";");
		Map<String, String> map = new HashMap<String, String>();
		for (String prop : split) {
			String[] propSplit = prop.split(":");
			if (propSplit.length == 2) {
				map.put(utils.stripDoubleSpacesAndTrim(propSplit[0]), utils.stripDoubleSpacesAndTrim(propSplit[1]));
			}
		}
		if (currentSelector.contains(",")) {
			String[] selectors = currentSelector.split(",");
			for (String selector : selectors) {
				css.add(utils.stripDoubleSpacesAndTrim(selector), map);
			}
		} else {
			css.add(utils.stripDoubleSpacesAndTrim(currentSelector), map);
		}
	}

	/**
	 * Processes the character, delegates to states.
	 *
	 * @param c a character that needs to be processed
	 */
	public void process(final char c) {
		current.process(c);
	}
}
