/**
 *
 */
package com.itextpdf.tool.xml.css.apply;

import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssApplier;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Emiel Ackermann
 *
 */
public class LineSeparatorCssApplier implements CssApplier<LineSeparator>{

	private final HtmlPipelineContext configuration;

	/**
	 * @param htmlPipelineContext the context
	 */
	public LineSeparatorCssApplier(final HtmlPipelineContext htmlPipelineContext) {
		this.configuration = htmlPipelineContext;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.CssApplier#apply(com.itextpdf.text.Element, com.itextpdf.tool.xml.Tag)
	 */
	public LineSeparator apply(final LineSeparator ls, final Tag t) {
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
				percentage = (CssUtils.getInstance().parsePxInCmMmPcToPt(widthStr)/configuration.getPageSize().getWidth())*100;
			}
		}
		ls.setPercentage(percentage);
		return ls;
	}

}
