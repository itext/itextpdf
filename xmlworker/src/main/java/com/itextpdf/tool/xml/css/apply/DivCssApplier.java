/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
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
        float fontSize = FontSizeTranslator.getInstance().translateFontSize(t);
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
            div.setTextAlignment(CSS.getElementAlignment(align));
        }

        String widthValue = css.get(HTML.Attribute.WIDTH);
        if (widthValue == null) {
            widthValue = t.getAttributes().get(HTML.Attribute.WIDTH);
        }
        if (widthValue != null) {
            float pageWidth = psc.getPageSize().getWidth();
            if (utils.isNumericValue(widthValue) || utils.isMetricValue(widthValue)) {
				div.setWidth(Math.min(pageWidth, utils.parsePxInCmMmPcToPt(widthValue)));
            } else if (utils.isRelativeValue(widthValue)) {
                if (widthValue.contains(CSS.Value.PERCENTAGE)) {
                    div.setPercentageWidth(utils.parseRelativeValue(widthValue, 1f));
                } else {
                    div.setWidth(Math.min(pageWidth, utils.parseRelativeValue(widthValue, fontSize)));
                }
            }
        }

        String heightValue = css.get(HTML.Attribute.HEIGHT);
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
            } else if (key.equalsIgnoreCase(CSS.Property.DISPLAY)) {
                if (value.equalsIgnoreCase(CSS.Value.BLOCK)){
                    div.setDisplay(PdfDiv.DisplayType.BLOCK);
                } else if (value.equalsIgnoreCase(CSS.Value.INLINE)){
                    div.setDisplay(PdfDiv.DisplayType.INLINE);
                } else if (value.equalsIgnoreCase(CSS.Value.INLINE_BLOCK)){
                    div.setDisplay(PdfDiv.DisplayType.INLINE_BLOCK);
                }else if (value.equalsIgnoreCase(CSS.Value.INLINE_TABLE)){
                    div.setDisplay(PdfDiv.DisplayType.INLINE_TABLE);
                }else if (value.equalsIgnoreCase(CSS.Value.LIST_ITEM)){
                    div.setDisplay(PdfDiv.DisplayType.LIST_ITEM);
                } else if (value.equalsIgnoreCase(CSS.Value.NONE)){
                    div.setDisplay(PdfDiv.DisplayType.NONE);
                } else if (value.equalsIgnoreCase(CSS.Value.RUN_IN)){
                    div.setDisplay(PdfDiv.DisplayType.RUN_IN);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_CAPTION)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_CAPTION);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_CELL)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_CELL);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_COLUMN_GROUP)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_COLUMN_GROUP);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_COLUMN)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_COLUMN);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_FOOTER_GROUP)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_FOOTER_GROUP);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_HEADER_GROUP)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_HEADER_GROUP);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_ROW)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_ROW);
                } else if (value.equalsIgnoreCase(CSS.Value.TABLE_ROW_GROUP)) {
                    div.setDisplay(PdfDiv.DisplayType.TABLE_ROW_GROUP);
                }
            } else if (key.equalsIgnoreCase(CSS.Property.BORDER_TOP_STYLE)) {
                if (value.equalsIgnoreCase(CSS.Value.DOTTED)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.DOTTED);
                } else if (value.equalsIgnoreCase(CSS.Value.DASHED)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.DASHED);
                } else if (value.equalsIgnoreCase(CSS.Value.SOLID)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.SOLID);
                } else if (value.equalsIgnoreCase(CSS.Value.DOUBLE)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.DOUBLE);
                } else if (value.equalsIgnoreCase(CSS.Value.GROOVE)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.GROOVE);
                } else if (value.equalsIgnoreCase(CSS.Value.RIDGE)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.RIDGE);
                } else if (value.equalsIgnoreCase(CSS.Value.INSET)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.INSET);
                } else if (value.equalsIgnoreCase(CSS.Value.OUTSET)) {
                    div.setBorderTopStyle(PdfDiv.BorderTopStyle.OUTSET);
                }
            } else if (key.equalsIgnoreCase(CSS.Property.PAGE_BREAK_INSIDE)) {
                if (value.equalsIgnoreCase(CSS.Value.AVOID)) {
                    div.setKeepTogether(true);
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
