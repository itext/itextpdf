/**
 *
 */
package com.itextpdf.tool.xml.css.apply;

import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;

/**
 * @author Emiel Ackermann
 *
 */
public class LineSeparatorCssApplier {

	/**
	 * Applies CSS to LineSeparators
	 * 
	 * @param ls the LineSeparator
	 * @param t the tag with styles
	 * @param psc the {@link PageSize} container
	 * @return the styled {@link LineSeparator}
	 */
	public LineSeparator apply(final LineSeparator ls, final Tag t, final PageSizeContainable psc) {
    	float lineWidth = 1;
    	Map<String, String> css = t.getCSS();
		if(css.get(CSS.Property.HEIGHT) != null) {
    		lineWidth = CssUtils.getInstance().parsePxInCmMmPcToPt(css.get(CSS.Property.HEIGHT));
    	}
		ls.setLineWidth(lineWidth);
		BaseColor lineColor = BaseColor.BLACK;
		if(css.get(CSS.Property.COLOR) != null) {
			lineColor  = HtmlUtilities.decodeColor(css.get(CSS.Property.COLOR));
		} else if (css.get(CSS.Property.BACKGROUND_COLOR) != null) {
			lineColor = HtmlUtilities.decodeColor(css.get(CSS.Property.BACKGROUND_COLOR));
		}
		ls.setLineColor(lineColor);
		float percentage = 100;
		String widthStr = css.get(CSS.Property.WIDTH);
		if(widthStr != null) {
			if(widthStr.contains("%")) {
				percentage = Float.parseFloat(widthStr.replace("%", ""));
			} else {
				percentage = (CssUtils.getInstance().parsePxInCmMmPcToPt(widthStr)/psc.getPageSize().getWidth())*100;
			}
		}
		ls.setPercentage(percentage);
		ls.setOffset(9);
		return ls;
	}

}
