package com.itextpdf.tool.xml.css;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.HTML;

import java.util.List;

/**
 *
 */
public class HeightCalculator {
    private final CssUtils utils = CssUtils.getInstance();

    public Float getHeight(final Tag tag, final float pageHeight){
		Float height = null;
		String heightValue = tag.getCSS().get(HTML.Attribute.HEIGHT);
		if(heightValue == null) {
			heightValue = tag.getAttributes().get(HTML.Attribute.HEIGHT);
		}
		if(heightValue != null) {
			if(utils.isNumericValue(heightValue) || utils.isMetricValue(heightValue)) {
				height = utils.parsePxInCmMmPcToPt(heightValue);
			} else if (utils.isRelativeValue(heightValue)) {
				Tag ancestor = tag;
				float firstAncestorsWidth = 0;
				while(firstAncestorsWidth == 0 && ancestor.getParent() != null) {
					ancestor = ancestor.getParent();
					firstAncestorsWidth = getHeight(ancestor, pageHeight);
				}
				if (firstAncestorsWidth == 0) {
					height = utils.parseRelativeValue(heightValue, pageHeight);
				} else {
					height = utils.parseRelativeValue(heightValue, firstAncestorsWidth);
				}
			}
		}

		return height;
	}
}
