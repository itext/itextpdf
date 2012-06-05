package com.itextpdf.tool.xml.css.apply;

import java.util.Map;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.*;
import com.itextpdf.tool.xml.html.HTML;

public class DivCssApplier {
    private final CssUtils utils = CssUtils.getInstance();

    public PdfDiv apply(final PdfDiv div, final Tag t, final MarginMemory memory, final PageSizeContainable psc) {
        Map<String, String> css = t.getCSS();
        float fontSize = FontSizeTranslator.getInstance().getFontSize(t);
        if (fontSize == Font.UNDEFINED) {
            fontSize =  FontSizeTranslator.DEFAULT_FONT_SIZE;
        }
        String align = null;
        if (t.getAttributes().containsKey(HTML.Attribute.ALIGN)) {
            align = t.getAttributes().get(HTML.Attribute.ALIGN);
        } else if (css.containsKey(CSS.Property.TEXT_ALIGN)) {
            align = css.get(CSS.Property.TEXT_ALIGN);
        }

        if (align != null) {
            if (align.equalsIgnoreCase(CSS.Value.CENTER)) {
                div.setTextAlignment(Element.ALIGN_CENTER);
            } else if (align.equalsIgnoreCase(CSS.Value.RIGHT)) {
                div.setTextAlignment(Element.ALIGN_RIGHT);
            } else if (align.equalsIgnoreCase(CSS.Value.JUSTIFY)) {
                div.setTextAlignment(Element.ALIGN_JUSTIFIED);
            }
        }


        String widthValue = t.getCSS().get(HTML.Attribute.WIDTH);
        if (widthValue == null) {
            widthValue = t.getAttributes().get(HTML.Attribute.WIDTH);
        }
        if (widthValue != null) {
            if (utils.isNumericValue(widthValue) || utils.isMetricValue(widthValue)) {
				div.setWidth(utils.parsePxInCmMmPcToPt(widthValue));
            } else if (utils.isRelativeValue(widthValue)) {
                if (widthValue.contains(CSS.Value.PERCENTAGE)) {
                    div.setPercentageWidth(utils.parseRelativeValue(widthValue, 1f));
                } else {
                    div.setWidth(utils.parseRelativeValue(widthValue, fontSize));
                }
            }
        }

        String heightValue = t.getCSS().get(HTML.Attribute.HEIGHT);
        if (heightValue == null) {
            heightValue = t.getAttributes().get(HTML.Attribute.HEIGHT);
        }
        if (heightValue != null) {
            if (utils.isNumericValue(heightValue) || utils.isMetricValue(heightValue)) {
                div.setHeight(utils.parsePxInCmMmPcToPt(heightValue));
            } else if (utils.isRelativeValue(heightValue)) {
                if (heightValue.contains(CSS.Value.PERCENTAGE)) {
                    div.setPercentageHeight(utils.parseRelativeValue(heightValue, 1f));
                } else {
                    div.setHeight(utils.parseRelativeValue(heightValue, fontSize));
                }
            }
        }

        Float marginTop = null;
        Float marginBottom = null;

        for (Map.Entry<String, String> entry : css.entrySet()) {
            String key = entry.getKey();
			String value = entry.getValue();
            if (key.equalsIgnoreCase(CSS.Property.LEFT)) {
                div.setLeft(utils.parseValueToPt(value, fontSize));
            } else if (key.equalsIgnoreCase(CSS.Property.RIGHT)) {
                if (div.getWidth() == null || div.getLeft() ==  null) {
                    div.setRight(utils.parseValueToPt(value, fontSize));
                }
            } else if (key.equalsIgnoreCase(CSS.Property.TOP)) {
                div.setTop(utils.parseValueToPt(value, fontSize));
            } else if (key.equalsIgnoreCase(CSS.Property.BOTTOM)) {
                if (div.getHeight() == null || div.getTop() == null) {
                    div.setBottom(utils.parseValueToPt(value, fontSize));
                }
            } else if (key.equalsIgnoreCase(CSS.Property.BACKGROUND_COLOR)) {
				div.setBackgroundColor(HtmlUtilities.decodeColor(value));
            } else if (key.equalsIgnoreCase(CSS.Property.PADDING_LEFT)) {
                div.setPaddingLeft(utils.parseValueToPt(value, fontSize));
            } else if (key.equalsIgnoreCase(CSS.Property.PADDING_RIGHT)) {
                div.setPaddingRight(utils.parseValueToPt(value, fontSize));
            } else if (key.equalsIgnoreCase(CSS.Property.PADDING_TOP)) {
                div.setPaddingTop(utils.parseValueToPt(value, fontSize));
            } else if (key.equalsIgnoreCase(CSS.Property.PADDING_BOTTOM)) {
                div.setPaddingBottom(utils.parseValueToPt(value, fontSize));
            } else if (key.equalsIgnoreCase(CSS.Property.MARGIN_TOP)) {
                marginTop = utils.calculateMarginTop(value, fontSize, memory);
            } else if (key.equalsIgnoreCase(CSS.Property.MARGIN_BOTTOM)) {
                marginBottom = utils.parseValueToPt(value, fontSize);
            } else if (key.equalsIgnoreCase(CSS.Property.FLOAT)) {
                if (value.equalsIgnoreCase(CSS.Value.LEFT)) {
                    div.setFloatType(PdfDiv.FloatType.LEFT);
                } else if (value.equalsIgnoreCase(CSS.Value.RIGHT)) {
                    div.setFloatType(PdfDiv.FloatType.RIGHT);
                }
            } else if (key.equalsIgnoreCase(CSS.Property.POSITION)) {
                if (value.equalsIgnoreCase(CSS.Value.ABSOLUTE)) {
                    div.setPosition(PdfDiv.PositionType.ABSOLUTE);
                } else if (value.equalsIgnoreCase(CSS.Value.FIXED)) {
                    div.setPosition(PdfDiv.PositionType.FIXED);
                } else if (value.equalsIgnoreCase(CSS.Value.RELATIVE)) {
                    div.setPosition(PdfDiv.PositionType.RELATIVE);
                }
            }

            //TODO: border, background properties.
        }

	    	/*for (Map.Entry<String, String> entry : css.entrySet()) {
	        	String key = entry.getKey();
				String value = entry.getValue();
				cell.setUseBorderPadding(true);
                if(key.equalsIgnoreCase(CSS.Property.BACKGROUND_COLOR)) {
					values.setBackground(HtmlUtilities.decodeColor(value));
				} else if(key.equalsIgnoreCase(CSS.Property.VERTICAL_ALIGN)) {
					if(value.equalsIgnoreCase(CSS.Value.TOP)) {
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						cell.setPaddingTop(cell.getPaddingTop()+6);
					} else if(value.equalsIgnoreCase(CSS.Value.BOTTOM)) {
						cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
						cell.setPaddingBottom(cell.getPaddingBottom()+6);
					}
				} else if(key.contains(CSS.Property.BORDER)) {
					if(key.contains(CSS.Value.TOP)) {
						setTopOfBorder(cell, key, value, values);
					} else if(key.contains(CSS.Value.BOTTOM)) {
						setBottomOfBorder(cell, key, value, values);
					} else if(key.contains(CSS.Value.LEFT)) {
						setLeftOfBorder(cell, key, value, values);
					} else if(key.contains(CSS.Value.RIGHT)) {
						setRightOfBorder(cell, key, value, values);
					}
				}
	    	}*/

        return div;
    }

}
