package com.itextpdf.tool.xml;

import java.util.ArrayList;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.pipeline.WritableElement;

/**
 * Implementation of the <code>ElementHandler</code> interface that helps
 * you build a list of iText <code>Element</code>s.
 */
public class ElementList extends ArrayList<Element> implements ElementHandler {

	/**
	 * @see com.itextpdf.tool.xml.ElementHandler#add(com.itextpdf.tool.xml.Writable)
	 */
	public void add(Writable w) {
		if (w instanceof WritableElement) {
			this.addAll(((WritableElement)w).elements());
		}
	}

	/** Serial version UID */
	private static final long serialVersionUID = -3943194552607332537L;
}
