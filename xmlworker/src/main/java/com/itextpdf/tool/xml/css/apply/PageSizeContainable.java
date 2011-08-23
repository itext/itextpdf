/**
 *
 */
package com.itextpdf.tool.xml.css.apply;

import com.itextpdf.text.Rectangle;

/**
 * Classes implementing PageSizeContainable have a {@link Rectangle} in possession that defines a PageSize.
 * @author redlab_b
 *
 */
public interface PageSizeContainable {

	/**
	 * returns the Rectangle that indicates a pagesize.
	 * @return the contained <code>Rectangle</code>
	 */
	Rectangle getPageSize();

}
