/**
 *
 */
package com.itextpdf.tool.xml;

/**
 * @author redlab_b
 *
 */
public interface TagListener {

	/**
	 * @param tag
	 */
	void open(Tag tag);

	/**
	 * @param tag
	 */
	void close(Tag tag);

	/**
	 * @param tag
	 * @param text
	 */
	void text(Tag tag, String text);

}
