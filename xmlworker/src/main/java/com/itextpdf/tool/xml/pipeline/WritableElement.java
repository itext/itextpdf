/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Element;

/**
 * @author Balder Van Camp
 *
 */
public class WritableElement implements Writable {

	private final ArrayList<Element> list;
	/**
	 *
	 */
	public WritableElement() {
		this.list = new ArrayList<Element>();
	}
	/**
	 * @param currentContent
	 */
	public WritableElement(final List<Element> currentContent) {
		this();
		this.list.addAll(currentContent);
	}
	/**
	 * @return the list of element
	 */
	public List<Element> elements() {
		return list;
	}

}
