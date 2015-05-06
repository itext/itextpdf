/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.HeightCalculator;
import com.itextpdf.tool.xml.css.WidthCalculator;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.table.CellSpacingEvent;
import com.itextpdf.tool.xml.html.table.Table;
import com.itextpdf.tool.xml.html.table.TableStyleValues;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Emiel Ackermann
 *
 */
public class HtmlCellCssApplier {

    private final CssUtils utils = CssUtils.getInstance();

	/**
	 * Applies css to a HtmlCell
	 * 
	 * @param cell the HtmlCell
	 * @param t the tag with the styles
	 * @param memory current margin memory
	 * @param psc the {@link PageSize} container
	 * @return a styled HtmlCell
	 */
    public HtmlCell apply(final HtmlCell cell, final Tag t, final MarginMemory memory, final PageSizeContainable psc) {
        Tag row = t.getParent();
        while(row != null && !row.getName().equals(HTML.Tag.TR)){
    		row = row.getParent();
    	}
        Tag table = t.getParent();
        while(table!= null && !table.getName().equals(HTML.Tag.TABLE)){
    		table = table.getParent();
    	}

        final TableStyleValues values = Table.setBorderAttributeForCell(table);
    	Map<String, String> css = t.getCSS();
		String emptyCells = css.get(CSS.Property.EMPTY_CELLS);
		if(null != emptyCells && CSS.Value.HIDE.equalsIgnoreCase(emptyCells) && cell.getCompositeElements() == null) {
			cell.setBorder(Rectangle.NO_BORDER);
		} else {
	    	cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Default css behavior. Implementation of "vertical-align" style further along.
            String vAlign = null;
            if (t.getAttributes().containsKey(HTML.Attribute.VALIGN)) {
                vAlign = t.getAttributes().get(HTML.Attribute.VALIGN);
            } else if (css.containsKey(HTML.Attribute.VALIGN)) {
                vAlign = css.get(HTML.Attribute.VALIGN);
            } else if (row != null) {
                if (row.getAttributes().containsKey(HTML.Attribute.VALIGN)) {
                    vAlign = row.getAttributes().get(HTML.Attribute.VALIGN);
                } else if (row.getCSS().containsKey(HTML.Attribute.VALIGN)) {
                    vAlign = row.getCSS().get(HTML.Attribute.VALIGN);
                }
            }
            if (vAlign != null) {
                if (vAlign.equalsIgnoreCase(CSS.Value.TOP)) {
                    cell.setVerticalAlignment(Element.ALIGN_TOP);
                } else if (vAlign.equalsIgnoreCase(CSS.Value.BOTTOM)) {
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                }
            }

            String align = null;
            if (t.getAttributes().containsKey(HTML.Attribute.ALIGN)) {
                align = t.getAttributes().get(HTML.Attribute.ALIGN);
            } else if (css.containsKey(CSS.Property.TEXT_ALIGN)) {
                align = css.get(CSS.Property.TEXT_ALIGN);
            }

            if (align != null) {
                if (align.equalsIgnoreCase(CSS.Value.CENTER)) {
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                } else if (align.equalsIgnoreCase(CSS.Value.RIGHT)) {
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                } else if (align.equalsIgnoreCase(CSS.Value.JUSTIFY)) {
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                }
            }

			if(t.getAttributes().get(HTML.Attribute.WIDTH) != null || css.get(HTML.Attribute.WIDTH) != null) {
				cell.setFixedWidth(new WidthCalculator().getWidth(t, memory.getRootTags(), psc.getPageSize().getWidth()));
			}

            HeightCalculator heightCalc = new HeightCalculator();
            Float height = heightCalc.getHeight(t, psc.getPageSize().getHeight());
            if (height == null && row != null) {
                height = heightCalc.getHeight(row, psc.getPageSize().getHeight());
            }
            if (height != null) {
                cell.setMinimumHeight(height);
            }

	        String colspan = t.getAttributes().get(HTML.Attribute.COLSPAN);
	        if (null != colspan) {
	            cell.setColspan(Integer.parseInt(colspan));
	        }
	        String rowspan = t.getAttributes().get(HTML.Attribute.ROWSPAN);
	        if (null != rowspan) {
	            cell.setRowspan(Integer.parseInt(rowspan));
	        }
	    	for (Entry<String, String> entry : css.entrySet()) {
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
				} else if(key.contains(CSS.Property.CELLPADDING) || key.contains(CSS.Property.PADDING)) {
					if(key.contains(CSS.Value.TOP)) {
						cell.setPaddingTop(cell.getPaddingTop()+utils.parsePxInCmMmPcToPt(value));
					} else if(key.contains(CSS.Value.BOTTOM)) {
						cell.setPaddingBottom(cell.getPaddingBottom()+utils.parsePxInCmMmPcToPt(value));
					} else if(key.contains(CSS.Value.LEFT)) {
						cell.setPaddingLeft(cell.getPaddingLeft()+utils.parsePxInCmMmPcToPt(value));
					} else if(key.contains(CSS.Value.RIGHT)) {
						cell.setPaddingRight(cell.getPaddingRight()+utils.parsePxInCmMmPcToPt(value));
					}
				} else if(key.contains(CSS.Property.TEXT_ALIGN)) {
					if(value.equalsIgnoreCase(CSS.Value.LEFT)) {
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					} else if(value.equalsIgnoreCase(CSS.Value.CENTER)) {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					} else if(value.equalsIgnoreCase(CSS.Value.RIGHT)) {
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
				}
	    	}
	    	cell.setPaddingLeft(cell.getPaddingLeft() + values.getHorBorderSpacing() + values.getBorderWidthLeft());
			cell.setPaddingRight(cell.getPaddingRight() + values.getBorderWidthRight());
	    	cell.setPaddingTop(cell.getPaddingTop() + values.getVerBorderSpacing() + values.getBorderWidthTop());
	    	cell.setPaddingBottom(cell.getPaddingBottom() + values.getBorderWidthBottom());
		}
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setCellEvent(new CellSpacingEvent(values));
		cell.setCellValues(values);
        return cell;
    }

	private void setTopOfBorder(final HtmlCell cell, final String key, final String value, final TableStyleValues values) {
		if(key.contains(CSS.Property.WIDTH)) {
			values.setBorderWidthTop(utils.parsePxInCmMmPcToPt(value));
		}
		if(key.contains(CSS.Property.COLOR)) {
			values.setBorderColorTop(HtmlUtilities.decodeColor(value));
		} else if(values.getBorderColorTop() == null){
			values.setBorderColorTop(BaseColor.BLACK);
		}
		if(key.contains("style")) {
//			If any, which are the border styles in iText? simulate in the borderevent?
			if(values.getBorderWidthTop() == 0){
				values.setBorderWidthTop(2.25f);
			}
		}
	}
	private void setBottomOfBorder(final HtmlCell cell, final String key, final String value, final TableStyleValues values) {
		if(key.contains(CSS.Property.WIDTH)) {
			values.setBorderWidthBottom(utils.parsePxInCmMmPcToPt(value));
		}
		if(key.contains(CSS.Property.COLOR)) {
			values.setBorderColorBottom(HtmlUtilities.decodeColor(value));
		} else if(values.getBorderColorBottom() == null){
			values.setBorderColorBottom(BaseColor.BLACK);
		}
		if(key.contains("style")) {
//			If any, which are the border styles in iText? simulate in the borderevent?
			if(values.getBorderWidthBottom() == 0){
				values.setBorderWidthBottom(2.25f);
			}
		}
	}
	private void setLeftOfBorder(final HtmlCell cell, final String key, final String value, final TableStyleValues values) {
		if(key.contains(CSS.Property.WIDTH)) {
			values.setBorderWidthLeft(utils.parsePxInCmMmPcToPt(value));
		}
		if(key.contains(CSS.Property.COLOR)) {
			values.setBorderColorLeft(HtmlUtilities.decodeColor(value));
		} else if(values.getBorderColorLeft() == null){
			values.setBorderColorLeft(BaseColor.BLACK);
		}
		if(key.contains("style")) {
//			If any, which are the border styles in iText? simulate in the borderevent?
			if(values.getBorderWidthLeft() == 0){
				values.setBorderWidthLeft(2.25f);
			}
		}
	}
	private void setRightOfBorder(final HtmlCell cell, final String key, final String value, final TableStyleValues values) {
		if(key.contains(CSS.Property.WIDTH)) {
			values.setBorderWidthRight(utils.parsePxInCmMmPcToPt(value));
		}
		if(key.contains(CSS.Property.COLOR)) {
			values.setBorderColorRight(HtmlUtilities.decodeColor(value));
		} else if(values.getBorderColorRight() == null){
			values.setBorderColorRight(BaseColor.BLACK);
		}
		if(key.contains("style")) {
//			If any, which are the border styles in iText? simulate in the borderevent?
			if(values.getBorderWidthRight() == 0){
				values.setBorderWidthRight(2.25f);
			}
		}
	}
}
