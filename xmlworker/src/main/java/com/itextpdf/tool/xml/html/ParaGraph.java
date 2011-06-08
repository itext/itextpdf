/*
 * $Id$
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
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.html.pdfelement.TabbedChunk;

/**
 * @author redlab_b
 *
 */
public class ParaGraph extends AbstractTagProcessor {


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document, java.lang.String)
	 */
	@Override
	public List<Element> content(final Tag tag, final String content) {
		String sanitized = HTMLUtils.sanitize(content);
		List<Element> l = new ArrayList<Element>(1);
		if (sanitized.length() > 0) {
			if ((null != tag.getCSS().get(CSS.Property.TAB_INTERVAL))) {
				TabbedChunk tabbedChunk = new TabbedChunk(sanitized);
				if (null != getLastChild(tag) && null != getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)) {
					tabbedChunk.setTabCount(Integer.parseInt(getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)));
				}
				l.add(new ChunkCssApplier().apply(tabbedChunk, tag));
			} else if (null != getLastChild(tag) && null != getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)) {
				TabbedChunk tabbedChunk = new TabbedChunk(sanitized);
				tabbedChunk.setTabCount(Integer.parseInt(getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)));
				l.add(new ChunkCssApplier().apply(tabbedChunk, tag));
			} else {
				l.add(new ChunkCssApplier().apply(new Chunk(sanitized), tag));
			}
		}
		return l;
	}

	private Tag getLastChild(final Tag tag) {
		if (0 != tag.getChildren().size())
			return tag.getChildren().get(tag.getChildren().size() - 1);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document)
	 */
	@Override
	public List<Element> end(final Tag tag, final List<Element> currentContent) {
		List<Element> l = new ArrayList<Element>(1);
		if (currentContent.size() > 0) {
			Paragraph p = new Paragraph();
			Map<String, String> css = tag.getCSS();
			if (null != css.get(CSS.Property.TAB_INTERVAL)) {
				addTabIntervalContent(currentContent, p, css.get(CSS.Property.TAB_INTERVAL));
				l.add(p);
			} else if (null != css.get(CSS.Property.TAB_STOPS)) { // <para tabstops=".." /> could use same implementation page 62
				addTabStopsContent(currentContent, p, css.get(CSS.Property.TAB_STOPS));
				l.add(p);
			} else if (null != css.get(CSS.Property.XFA_TAB_STOPS)) { // <para tabStops=".." /> could use same implementation page 63
				addTabStopsContent(currentContent, p, css.get(CSS.Property.XFA_TAB_STOPS)); // leader elements needs to be
				l.add(p);																	// extracted.
			} else {
				for (Element e:  currentContentToParagraph(currentContent, true, true, tag)) {
					l.add(e);
				}
			}
		}
		return l;
	}

	/**
	 * Applies the tab interval of the p tag on its {@link TabbedChunk} elements. <br />
	 * The style "xfa-tab-count" of the {@link TabbedChunk} is multiplied with the tab interval of the p tag. This width is then added to a new {@link TabbedChunk}.
	 *
	 * @param currentContent containing the elements inside the p tag.
	 * @param p paragraph to which the tabbed chunks will be added.
	 * @param value the value of style "tab-interval".
	 */
	private void addTabIntervalContent(final List<Element> currentContent, final Paragraph p, final String value) {
		float width = 0;
		for(Element e: currentContent) {
			if (e instanceof TabbedChunk) {
				width += ((TabbedChunk) e).getTabCount()*CssUtils.getInstance().parsePxInCmMmPcToPt(value);
				TabbedChunk tab = new TabbedChunk(new VerticalPositionMark(), width, false);
				p.add(new Chunk(tab));
				p.add(new Chunk((TabbedChunk) e));
			}
		}
	}

	/**
	 * Applies the tab stops of the p tag on its {@link TabbedChunk} elements.
	 *
	 * @param currentContent containing the elements inside the p tag.
	 * @param p paragraph to which the tabbed chunks will be added.
	 * @param value the value of style "tab-stops".
	 */
	private void addTabStopsContent(final List<Element> currentContent, final Paragraph p, final String value) {
		List<Chunk> tabs = new ArrayList<Chunk>();
		String[] alignAndWidth = value.split(" ");
		float tabWidth = 0;
		for(int i = 0 , j = 1; j < alignAndWidth.length ; i+=2, j+=2) {
			tabWidth += CssUtils.getInstance().parsePxInCmMmPcToPt(alignAndWidth[j]);
			TabbedChunk tab = new TabbedChunk(new VerticalPositionMark(), tabWidth, true, alignAndWidth[i]);
			tabs.add(tab);
		}
		int tabsPerRow = tabs.size();
		int currentTab = 0;
		for(Element e: currentContent) {
			if (e instanceof TabbedChunk) {
				if(currentTab == tabsPerRow) {
					currentTab = 0;
				}
				if(((TabbedChunk) e).getTabCount() != 0 /* == 1*/) {
					p.add(new Chunk(tabs.get(currentTab)));
					p.add(new Chunk((TabbedChunk) e));
					++currentTab;
//				} else { // wat doet een tabCount van groter dan 1? sla een tab over of count * tabWidth?
//					int widthMultiplier = ((TabbedChunk) e).getTabCount();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
	 */
	@Override
	public boolean isStackOwner() {
		return true;
	}

}
