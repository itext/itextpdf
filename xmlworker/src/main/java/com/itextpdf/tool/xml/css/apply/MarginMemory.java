/**
 *
 */
package com.itextpdf.tool.xml.css.apply;

import java.util.List;

import com.itextpdf.tool.xml.exceptions.NoDataException;

/**
 * The marginmemory helps remembering the last margin bottom and roottags. These are needed to calculate the right
 * margins for some elements when applying CSS.
 *
 * @author redlab_b
 *
 */
public interface MarginMemory {

	/**
	 * @return a Float
	 * @throws NoDataException if there is no LastMarginBottom set
	 */
	Float getLastMarginBottom() throws NoDataException;

	/**
	 * @return a list of roottags
	 */
	List<String> getRootTags();

	/**
	 * Set the last margin bottom.
	 * @param lmb set the float for lmb
	 */
	void setLastMarginBottom(Float lmb);

}
