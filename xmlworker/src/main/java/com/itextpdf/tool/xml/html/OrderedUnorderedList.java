/*
 * $Id: $
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2011 1T3XT BVBA Authors: Balder Van Camp, Emiel
 * Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License version 3 as published by the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.tool.xml.AbstractTagProcessor;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.css.apply.ListStyleTypeCssApplier;

/**
 * @author Balder Van Camp
 *
 */
public class OrderedUnorderedList extends AbstractTagProcessor {

	/**
	 *
	 */
	private static final FontSizeTranslator fst = FontSizeTranslator.getInstance();
	/**
	 *
	 */
	private static final CssUtils utils = CssUtils.getInstance();

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag, java.util.List)
	 */
	@Override
	public List<Element> endElement(final Tag tag, final List<Element> currentContent) {
		List<Element> l = new ArrayList<Element>(1);
		com.itextpdf.text.List list = new com.itextpdf.text.List();
		if (currentContent.size() > 0) {
			list = new ListStyleTypeCssApplier(configuration).apply(list, tag);
			for (int i=0; i<currentContent.size(); i++) {
				ListItem li = (ListItem) currentContent.get(i);
				// FIXME margin and padding left and right have been applied on ListItem level, but do not seem to work.
				// margin and padding-top of this list will be set on the first ListItem.
				if(i==0){
					float ownFontSize = fst.getFontSize(tag);
					float ownMarginTop = 0;
					if(tag.getCSS().get(CSS.Property.MARGIN_TOP)==null) {
						if(CssUtils.ROOT_TAGS.contains(tag.getParent().getTag())) {
							ownMarginTop = ownFontSize;
						}
					} else {
						ownMarginTop = utils.parseValueToPt(tag.getCSS().get(CSS.Property.MARGIN_TOP),ownFontSize);
					}
					float ownPaddingTop = tag.getCSS().get(CSS.Property.PADDING_TOP)!=null?utils.parseValueToPt(tag.getCSS().get(CSS.Property.PADDING_TOP),ownFontSize):0;
					float totalSpacingTop = 0;
					//Margin-top values of this tag and its first child needs to be compared if paddingTop = 0.
					if(ownPaddingTop == 0) {
						Tag firstChild = tag.getChildren().get(0);
						float firstChildFontSize = fst.getFontSize(firstChild);
						float firstChildMarginTop = firstChild.getCSS().get(CSS.Property.MARGIN_TOP)!=null?utils.parseValueToPt(firstChild.getCSS().get(CSS.Property.MARGIN_TOP),firstChildFontSize):0;
						float firstChildPaddingTop = firstChild.getCSS().get(CSS.Property.PADDING_TOP)!=null?utils.parseValueToPt(firstChild.getCSS().get(CSS.Property.PADDING_TOP),firstChildFontSize):0;
						totalSpacingTop = firstChildPaddingTop;
						float marginTop = 0;
						if(ownMarginTop != 0 && firstChildMarginTop != 0){
							marginTop = ownMarginTop>=firstChildMarginTop?ownMarginTop:firstChildMarginTop;
						} else if (ownMarginTop != 0) {
							marginTop = ownMarginTop;
						} else if (firstChildMarginTop != 0) {
							marginTop = firstChildMarginTop;
						}
						totalSpacingTop += utils.calculateMarginTop(tag, marginTop+"pt", 0);
					} else {
						// SpacingBefore has already been applied on the ListItem itself and it can be reused.
						totalSpacingTop = li.getSpacingBefore();
						totalSpacingTop += utils.calculateMarginTop(tag, ownMarginTop+"pt", 0);
						totalSpacingTop += ownPaddingTop;
					}
					li.setSpacingBefore(calculateTopOrBottomMargin(true, tag, i, li));
				// margin and padding-bottom of this list will be set on the last ListItem.
				}
				if (i==currentContent.size()-1) {
					li.setSpacingAfter(calculateTopOrBottomMargin(false, tag, i, li));

				}
				list.add(li);
			}
		}
		l.add(list);
		return l;
	}

	private float calculateTopOrBottomMargin(final boolean isTop, final Tag tag, final int i, final ListItem li) {
		String end = isTop?"-top":"-bottom";
		float ownFontSize = fst.getFontSize(tag);
		float ownMargin = 0;
		if(tag.getCSS().get(CSS.Property.MARGIN+end)==null) {
			if(CssUtils.ROOT_TAGS.contains(tag.getParent().getTag())) {
				ownMargin = ownFontSize;
			}
		} else {
			ownMargin = utils.parseValueToPt(tag.getCSS().get(CSS.Property.MARGIN+end),ownFontSize);
		}
		float ownPadding = tag.getCSS().get(CSS.Property.PADDING+end)!=null?utils.parseValueToPt(tag.getCSS().get(CSS.Property.PADDING+end),ownFontSize):0;
		float totalSpacing = 0;
		//Margin values of this tag and its first child needs to be compared if paddingTop or bottom = 0.
		if(ownPadding == 0) {
			Tag child = tag.getChildren().get(i);
			float childFontSize = fst.getFontSize(child);
			float childMargin = child.getCSS().get(CSS.Property.MARGIN+end)!=null?utils.parseValueToPt(child.getCSS().get(CSS.Property.MARGIN+end),childFontSize):0;
			float childPadding = child.getCSS().get(CSS.Property.PADDING+end)!=null?utils.parseValueToPt(child.getCSS().get(CSS.Property.PADDING+end),childFontSize):0;
			float margin = 0;
			if(ownMargin != 0 && childMargin != 0){
				margin = ownMargin>=childMargin?ownMargin:childMargin;
			} else if (ownMargin != 0) {
				margin = ownMargin;
			} else if (childMargin != 0) {
				margin = childMargin;
			}
			if(isTop) {
				totalSpacing = childPadding + utils.calculateMarginTop(tag, margin+"pt", 0);
			} else {
				totalSpacing = childPadding + margin;
			}
		} else {
			// Spacing has already been applied on the ListItem itself and it can be reused.
			if(isTop){
				totalSpacing = li.getSpacingBefore();
				totalSpacing += utils.calculateMarginTop(tag, ownMargin+"pt", 0);
			} else {
				totalSpacing = li.getSpacingAfter()+ownMargin;
			}
			totalSpacing += ownPadding;
		}
		return totalSpacing;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
	 */
	@Override
	public boolean isStackOwner() {
		return true;
	}

}
