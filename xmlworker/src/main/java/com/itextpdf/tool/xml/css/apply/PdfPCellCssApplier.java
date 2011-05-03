/*
 * $Id: $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
import java.util.Map.Entry;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssApplier;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.WidthCalculator;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.pdfelement.FixedWidthCell;
import com.itextpdf.tool.xml.html.table.CellSpacingEvent;
import com.itextpdf.tool.xml.html.table.Table;
import com.itextpdf.tool.xml.html.table.TableStyleValues;

/**
 * @author Emiel Ackermann
 *
 */
public class PdfPCellCssApplier implements CssApplier<PdfPCell> {

    private final CssUtils utils = CssUtils.getInstance();
	private final XMLWorkerConfig configuration;
	private final TableStyleValues styleValues = new TableStyleValues();

	public PdfPCellCssApplier(final XMLWorkerConfig configuration) {
		this.configuration = configuration;
	}
    /*
     * (non-Javadoc)
     *
     * @see
     * com.itextpdf.tool.xml.css.CssApplier#apply(com.itextpdf.text.Element,
     * com.itextpdf.tool.xml.Tag)
     */
    public PdfPCell apply(PdfPCell cell, final Tag t) {
    	Map<String, String> css = t.getCSS();
		String emptyCells = css.get(CSS.Property.EMPTY_CELLS);
		if(null != emptyCells && CSS.Value.HIDE.equalsIgnoreCase(emptyCells) && cell.getCompositeElements() == null) {
			cell.setBorder(Rectangle.NO_BORDER);
		} else {
	    	cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Default css behavior. Implementation of "vertical-align" style further along.
			if(t.getAttributes().get(HTML.Attribute.WIDTH) != null || css.get("width") != null) {
				cell = new FixedWidthCell(cell.getCompositeElements(), new WidthCalculator().getWidth(t, configuration));
			}
	        String colspan = t.getAttributes().get(HTML.Attribute.COLSPAN);
	        if (null != colspan) {
	            cell.setColspan(Integer.parseInt(colspan));
	        }
	        String rowspan = t.getAttributes().get(HTML.Attribute.ROWSPAN);
	        if (null != rowspan) {
	            cell.setRowspan(Integer.parseInt(rowspan));
	        }
	        boolean borderEncountered = false;
	    	for (Entry<String, String> entry : css.entrySet()) {
	        	String key = entry.getKey();
				String value = entry.getValue();
				cell.setUseBorderPadding(true);
				if(key.equalsIgnoreCase(CSS.Property.HEIGHT)) {
					cell.setFixedHeight(utils.parsePxInCmMmPcToPt(value));
				} else if(key.equalsIgnoreCase(CSS.Property.BACKGROUND_COLOR)) {
					cell.setBackgroundColor(HtmlUtilities.decodeColor(value));
				} else if(key.equalsIgnoreCase(CSS.Property.VERTICAL_ALIGN)) {
					if(value.equalsIgnoreCase(CSS.Value.TOP)) {
						cell.setVerticalAlignment(Element.ALIGN_TOP);
					} else if(value.equalsIgnoreCase(CSS.Value.BOTTOM)) {
						cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					}
				} else if(key.contains(CSS.Property.BORDER)) {
					borderEncountered = true;
					if(key.contains(CSS.Value.TOP)) {
						setTopOfBorder(cell, key, value);
					} else if(key.contains(CSS.Value.BOTTOM)) {
						setBottomOfBorder(cell, key, value);
					} else if(key.contains(CSS.Value.LEFT)) {
						setLeftOfBorder(cell, key, value);
					} else if(key.contains(CSS.Value.RIGHT)) {
						setRightOfBorder(cell, key, value);
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
	    	Tag table = t.getParent();
	    	while(!table.getTag().equals("table")){
	    		table = table.getParent();
	    	}
	    	if(!borderEncountered) {
				String border = table.getAttributes().get(CSS.Property.BORDER);
				if(border != null) {
					cell.setBorderColor(BaseColor.BLACK);
					cell.setBorderWidth(utils.parsePxInCmMmPcToPt(border));
					styleValues.setHorBorderSpacing(1.5f);
					styleValues.setVerBorderSpacing(1.5f);
				}
			} else {
				styleValues.setHorBorderSpacing(new Table().getBorderOrCellSpacing(true, table.getCSS(), table.getAttributes()));
				styleValues.setVerBorderSpacing(new Table().getBorderOrCellSpacing(false, table.getCSS(), table.getAttributes()));
			}
		}
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setCellEvent(new CellSpacingEvent(styleValues));
        return cell;
    }

	private void setTopOfBorder(final PdfPCell cell, final String key, final String value) {
		if(key.contains(CSS.Property.WIDTH)) {
			cell.setBorderWidthTop(utils.parsePxInCmMmPcToPt(value));
			if(cell.getBorderWidthBottom() == 0.5f){
				cell.setBorderWidthBottom(2.25f);
			}
			if(cell.getBorderWidthLeft() == 0.5f){
				cell.setBorderWidthLeft(2.25f);
			}
			if(cell.getBorderWidthRight() == 0.5f){
				cell.setBorderWidthRight(2.25f);
			}
		} else if(cell.getBorderWidthTop() == 0.5f) {
			cell.setBorderWidthTop(2.25f);
		}
		if(key.contains(CSS.Property.COLOR)) {
			cell.setBorderColorTop(HtmlUtilities.decodeColor(value));
			if(cell.getBorderWidthBottom() == 0.5f){
				cell.setBorderWidthBottom(2.25f);
			}
			if(cell.getBorderWidthLeft() == 0.5f){
				cell.setBorderWidthLeft(2.25f);
			}
			if(cell.getBorderWidthRight() == 0.5f){
				cell.setBorderWidthRight(2.25f);
			}
		} else if(cell.getBorderColorTop() == null){
			cell.setBorderColorTop(BaseColor.BLACK);
		}
//		if(key.contains("style")) {
//			If any, which are the border styles in iText?
//		}
	}
	private void setBottomOfBorder(final PdfPCell cell, final String key, final String value) {
		if(key.contains(CSS.Property.WIDTH)) {
			cell.setBorderWidthBottom(utils.parsePxInCmMmPcToPt(value));
			if(cell.getBorderWidthTop() == 0.5f){
				cell.setBorderWidthTop(2.25f);
			}
			if(cell.getBorderWidthLeft() == 0.5f){
				cell.setBorderWidthLeft(2.25f);
			}
			if(cell.getBorderWidthRight() == 0.5f){
				cell.setBorderWidthRight(2.25f);
			}
		} else if(cell.getBorderWidthBottom() == 0.5f) {
			cell.setBorderWidthBottom(2.25f);
		}
		if(key.contains(CSS.Property.COLOR)) {
			cell.setBorderColorBottom(HtmlUtilities.decodeColor(value));
			if(cell.getBorderWidthTop() == 0.5f){
				cell.setBorderWidthTop(2.25f);
			}
			if(cell.getBorderWidthLeft() == 0.5f){
				cell.setBorderWidthLeft(2.25f);
			}
			if(cell.getBorderWidthRight() == 0.5f){
				cell.setBorderWidthRight(2.25f);
			}
		} else if(cell.getBorderColorBottom() == null){
			cell.setBorderColorBottom(BaseColor.BLACK);
		}
//		if(key.contains("style")) {
//			If any, which are the border styles in iText?
//		}
	}
	private void setLeftOfBorder(final PdfPCell cell, final String key, final String value) {
		if(key.contains(CSS.Property.WIDTH)) {
			cell.setBorderWidthLeft(utils.parsePxInCmMmPcToPt(value));
			if(cell.getBorderWidthTop() == 0.5f){
				cell.setBorderWidthTop(2.25f);
			}
			if(cell.getBorderWidthBottom() == 0.5f){
				cell.setBorderWidthBottom(2.25f);
			}
			if(cell.getBorderWidthRight() == 0.5f){
				cell.setBorderWidthRight(2.25f);
			}
		} else if(cell.getBorderWidthLeft() == 0.5f) {
			cell.setBorderWidthLeft(2.25f);
		}
		if(key.contains(CSS.Property.COLOR)) {
			cell.setBorderColorLeft(HtmlUtilities.decodeColor(value));
			if(cell.getBorderWidthTop() == 0.5f){
				cell.setBorderWidthTop(2.25f);
			}
			if(cell.getBorderWidthBottom() == 0.5f){
				cell.setBorderWidthBottom(2.25f);
			}
			if(cell.getBorderWidthRight() == 0.5f){
				cell.setBorderWidthRight(2.25f);
			}
		} else if(cell.getBorderColorLeft() == null){
			cell.setBorderColorLeft(BaseColor.BLACK);
		}
//		if(key.contains("style")) {
//			If any, which are the border styles in iText?
//		}
	}
	private void setRightOfBorder(final PdfPCell cell, final String key, final String value) {
		if(key.contains(CSS.Property.WIDTH)) {
			cell.setBorderWidthRight(utils .parsePxInCmMmPcToPt(value));
			if(cell.getBorderWidthTop() == 0.5f){
				cell.setBorderWidthTop(2.25f);
			}
			if(cell.getBorderWidthLeft() == 0.5f){
				cell.setBorderWidthLeft(2.25f);
			}
			if(cell.getBorderWidthBottom() == 0.5f){
				cell.setBorderWidthBottom(2.25f);
			}
		} else if(cell.getBorderWidthRight() == 0.5f) {
			cell.setBorderWidthRight(2.25f);
		}
		if(key.contains(CSS.Property.COLOR)) {
			cell.setBorderColorRight(HtmlUtilities.decodeColor(value));
			if(cell.getBorderWidthTop() == 0.5f){
				cell.setBorderWidthTop(2.25f);
			}
			if(cell.getBorderWidthLeft() == 0.5f){
				cell.setBorderWidthLeft(2.25f);
			}
			if(cell.getBorderWidthBottom() == 0.5f){
				cell.setBorderWidthBottom(2.25f);
			}
		} else if(cell.getBorderColorRight() == null){
			cell.setBorderColorRight(BaseColor.BLACK);
		}
//		if(key.contains("style")) {
//			If any, which are the border styles in iText?
//		}
	}
}
