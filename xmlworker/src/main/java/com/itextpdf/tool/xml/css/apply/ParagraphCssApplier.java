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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.HTML;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Applies CSS on a {@link Paragraph}
 *
 * @author itextpdf.com
 */
public class ParagraphCssApplier {


	private final CssAppliers appliers;

	/**
	 * Construct a ParagraphCssApplier.
	 *
	 */
	public ParagraphCssApplier(final CssAppliers appliers) {
		this.appliers = appliers;
    }

	/**
	 * Styles a paragraph
	 *
	 * @param p the paragraph
	 * @param t the tag
	 * @param configuration the MarginMemory
	 * @return a styled {@link Paragraph}
	 */
    public Paragraph apply(final Paragraph p, final Tag t, final MarginMemory configuration) {
        /*MaxLeadingAndSize m = new MaxLeadingAndSize();
        if (configuration.getRootTags().contains(t.getName())) {
            m.setLeading(t);
        } else {
            m.setVariablesBasedOnChildren(t);
        }*/
		final CssUtils utils = CssUtils.getInstance();
        float fontSize = FontSizeTranslator.getInstance().getFontSize(t);
        if (fontSize == Font.UNDEFINED) fontSize = 0;
        float lmb = 0;
        boolean hasLMB = false;
        Map<String, String> css = t.getCSS();
        for (Entry<String, String> entry : css.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (CSS.Property.MARGIN_TOP.equalsIgnoreCase(key)) {
                p.setSpacingBefore(p.getSpacingBefore() + utils.calculateMarginTop(value, fontSize, configuration));
            } else if (CSS.Property.PADDING_TOP.equalsIgnoreCase(key)) {
                p.setSpacingBefore(p.getSpacingBefore() + utils.parseValueToPt(value, fontSize));
                p.setPaddingTop(utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.MARGIN_BOTTOM.equalsIgnoreCase(key)) {
                float after = utils.parseValueToPt(value, fontSize);
                p.setSpacingAfter(p.getSpacingAfter() + after);
                lmb = after;
                hasLMB = true;
            } else if (CSS.Property.PADDING_BOTTOM.equalsIgnoreCase(key)) {
                p.setSpacingAfter(p.getSpacingAfter() + utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.MARGIN_LEFT.equalsIgnoreCase(key)) {
                p.setIndentationLeft(p.getIndentationLeft() + utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.MARGIN_RIGHT.equalsIgnoreCase(key)) {
                p.setIndentationRight(p.getIndentationRight() + utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.PADDING_LEFT.equalsIgnoreCase(key)) {
                p.setIndentationLeft(p.getIndentationLeft() + utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.PADDING_RIGHT.equalsIgnoreCase(key)) {
                p.setIndentationRight(p.getIndentationRight() + utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.TEXT_ALIGN.equalsIgnoreCase(key)) {
                if (CSS.Value.RIGHT.equalsIgnoreCase(value)) {
                    p.setAlignment(Element.ALIGN_RIGHT);
                } else if (CSS.Value.CENTER.equalsIgnoreCase(value)) {
                    p.setAlignment(Element.ALIGN_CENTER);
                } else if (CSS.Value.LEFT.equalsIgnoreCase(value)) {
                    p.setAlignment(Element.ALIGN_LEFT);
                } else if (CSS.Value.JUSTIFY.equalsIgnoreCase(value)) {
                    p.setAlignment(Element.ALIGN_JUSTIFIED);
                }
            } else if (CSS.Property.TEXT_INDENT.equalsIgnoreCase(key)) {
                p.setFirstLineIndent(utils.parseValueToPt(value, fontSize));
            } else if (CSS.Property.LINE_HEIGHT.equalsIgnoreCase(key)) {
                if(utils.isNumericValue(value)) {
                    p.setLeading(Float.parseFloat(value) * fontSize);
                } else if (utils.isRelativeValue(value)) {
                    p.setLeading(utils.parseRelativeValue(value, fontSize));
                } else if (utils.isMetricValue(value)){
                    p.setLeading(utils.parsePxInCmMmPcToPt(value));
                }
            }
        }

        if ( t.getAttributes().containsKey(HTML.Attribute.ALIGN)) {
            String value = t.getAttributes().get(HTML.Attribute.ALIGN);

            if ( value != null ) {
                if ( value.equalsIgnoreCase(CSS.Value.RIGHT)) {
                    p.setAlignment(Element.ALIGN_RIGHT);
                } else if ( value.equalsIgnoreCase(CSS.Value.LEFT)) {
                    p.setAlignment(Element.ALIGN_LEFT);
                } else if ( value.equalsIgnoreCase(CSS.Value.CENTER)) {
                    p.setAlignment(Element.ALIGN_CENTER);
                } else if ( value.equalsIgnoreCase(CSS.Value.JUSTIFY)) {
                    p.setAlignment(Element.ALIGN_JUSTIFIED);
                }
            }
        }
        // setDefaultMargin to largestFont if no margin-bottom is set and p-tag is child of the root tag.
        /*if (null != t.getParent()) {
            String parent = t.getParent().getName();
            if (css.get(CSS.Property.MARGIN_TOP) == null && configuration.getRootTags().contains(parent)) {
                p.setSpacingBefore(p.getSpacingBefore() + utils.calculateMarginTop(fontSize + "pt", 0, configuration));
            }
            if (css.get(CSS.Property.MARGIN_BOTTOM) == null && configuration.getRootTags().contains(parent)) {
                p.setSpacingAfter(p.getSpacingAfter() + fontSize);
                css.put(CSS.Property.MARGIN_BOTTOM, fontSize + "pt");
                lmb = fontSize;
                hasLMB = true;
            }
            //p.setLeading(m.getLargestLeading());  We need possibility to detect that line-height undefined;
            if (p.getAlignment() == -1) {
                p.setAlignment(Element.ALIGN_LEFT);
            }
        }*/

        if (hasLMB) {
            configuration.setLastMarginBottom(lmb);
        }
		Font font = appliers.getChunkCssAplier().applyFontStyles(t);
        p.setFont(font);
        // TODO reactive for positioning and implement more
//		if(null != configuration.getWriter() && null != css.get("position")) {
//			positionNoNewLineParagraph(p, css);
//			p = null;
//		}
        return p;
    }

//	private void positionParagraph(final Paragraph p, final Map<String, String> css) {
//		PdfContentByte canvas = configuration.getWriter().getDirectContent();
//		ColumnText ct = new ColumnText(canvas);
//		float remainingWidth = configuration.getPageSize().getWidth();
//		int numberOfLines = 1;
//		float llx = 0;
//		float lly = 0;
//		float urx = 0;
//		float ury = 0;
//		if(null != css.get(CSS.Value.LEFT)) {
//			llx = utils.parsePxInCmMmPcToPt(css.get(CSS.Value.LEFT));
//			remainingWidth -= llx;
//		} else if(null != css.get(CSS.Value.RIGHT)) { // in html right is ignored if left is set.
//			urx = utils.parsePxInCmMmPcToPt(css.get(CSS.Value.RIGHT));
//			remainingWidth -= urx;
//		}
//		if(llx == 0) {
//			llx = urx;
//			for(Chunk c: p.getChunks()) {
//				remainingWidth -= c.getWidthPoint();
//				llx -= c.getWidthPoint();
//				if(remainingWidth < 0) {
//					numberOfLines++;
//					remainingWidth = configuration.getPageSize().getWidth()-urx-Math.abs(remainingWidth);
//				}
//			}
//			if(llx < 0) {
//				llx = 0;
//			}
//		} else if(urx == 0) {
//			urx = llx;
//			for(Chunk c: p.getChunks()) {
//				remainingWidth -= c.getWidthPoint();
//				urx += c.getWidthPoint();
//				if(remainingWidth < 0) {
//					numberOfLines++;
//					remainingWidth = configuration.getPageSize().getWidth()-llx-Math.abs(remainingWidth);
//				}
//			}
//			if(urx > configuration.getPageSize().getWidth()) {
//				urx = configuration.getPageSize().getWidth();
//			}
////				urx += p.getFirstLineIndent() + p.getIndentationLeft() + p.getIndentationRight();
//		} else {
//			for(Chunk c: p.getChunks()) {
//				remainingWidth -= c.getWidthPoint();
//				if(remainingWidth < 0) {
//					numberOfLines++;
//					remainingWidth = configuration.getPageSize().getWidth()-llx-urx-Math.abs(remainingWidth);
//				}
//			}
//		}
//		if(null != css.get(CSS.Property.TOP)) {
//			ury = -utils.parsePxInCmMmPcToPt(css.get(CSS.Property.TOP))-p.getSpacingBefore();
//		}
//		if(css.get(CSS.Property.POSITION).equalsIgnoreCase(CSS.Value.RELATIVE)) {
//			float textHeight = utils.validateTextHeight(css, p.getLeading() * numberOfLines);
//			if (configuration.getMemory().get(XMLWorkerConfig.VERTICAL_POSITION) == null) {
//				ury += configuration.getWriter().getVerticalPosition(false);
//			} else {
//				ury += (Float)configuration.getMemory().get(XMLWorkerConfig.VERTICAL_POSITION);
//			}
//			lly = ury - textHeight;
//			configuration.getMemory().put(XMLWorkerConfig.VERTICAL_POSITION, textHeight + p.getSpacingBefore() + p.getSpacingAfter());
//		} else { //position:"absolute"
//			lly = ury - utils.validateTextHeight(css, p.getLeading()*numberOfLines);
//		}
//		ct.setSimpleColumn(p, llx, lly, urx, ury, p.getLeading(), p.getAlignment());
//		if(null != css.get(CSS.Property.BACKGROUND_COLOR)) {
//			canvas.setColorFill(HtmlUtilities.decodeColor(css.get(CSS.Property.BACKGROUND_COLOR)));
//			llx -= p.getIndentationLeft();
//			lly -= utils.checkMetricStyle(css, CSS.Property.PADDING_BOTTOM) + p.getLeading()/3.5f;
//			urx += p.getIndentationRight();
//			ury += utils.checkMetricStyle(css, CSS.Property.PADDING_TOP);
//			canvas.rectangle(llx, lly, urx-llx, ury-lly);
//			canvas.fill();
//		}
//		try {
//			ct.go();
//		} catch (DocumentException e) {
//			throw new RuntimeWorkerException(e);
//		}
//	}
}
