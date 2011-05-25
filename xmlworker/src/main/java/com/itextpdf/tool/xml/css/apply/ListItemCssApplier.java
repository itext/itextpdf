/**
 *
 */
package com.itextpdf.tool.xml.css.apply;

import com.itextpdf.text.ListItem;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CssApplier;

/**
 * @author Emiel Ackermann
 *
 */
public class ListItemCssApplier implements CssApplier<ListItem>{


	/**
	 * @param configuration
	 */
	public ListItemCssApplier() {
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.CssApplier#apply(com.itextpdf.text.Element, com.itextpdf.tool.xml.Tag)
	 */
	public ListItem apply(final ListItem li, final Tag t) {
//		li.setAlignment(alignment);
//		li.s
		return li;
	}

}
