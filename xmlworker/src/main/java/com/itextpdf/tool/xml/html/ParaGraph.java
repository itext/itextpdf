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
 * OF THIRD PARTY RIGHTS.
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.pdfelement.TabbedChunk;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public List<Element> content(final WorkerContext ctx, final Tag tag, final String content) {
		List<Chunk> sanitizedChunks = HTMLUtils.sanitize(content, false);
		List<Element> l = new ArrayList<Element>(1);
        for (Chunk sanitized : sanitizedChunks) {
            HtmlPipelineContext myctx;
            try {
                myctx = getHtmlPipelineContext(ctx);
            } catch (NoCustomContextException e) {
                throw new RuntimeWorkerException(e);
            }
            if ((null != tag.getCSS().get(CSS.Property.TAB_INTERVAL))) {
                TabbedChunk tabbedChunk = new TabbedChunk(sanitized.getContent());
                if (null != getLastChild(tag) && null != getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)) {
                    tabbedChunk.setTabCount(Integer.parseInt(getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)));
                }
                l.add(getCssAppliers().apply(tabbedChunk, tag, myctx));
            } else if (null != getLastChild(tag) && null != getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)) {
                TabbedChunk tabbedChunk = new TabbedChunk(sanitized.getContent());
                tabbedChunk.setTabCount(Integer.parseInt(getLastChild(tag).getCSS().get(CSS.Property.XFA_TAB_COUNT)));
                l.add(getCssAppliers().apply(tabbedChunk, tag, myctx));
            } else {
                l.add(getCssAppliers().apply(sanitized, tag, myctx));
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
	public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
        List<Element> l = new ArrayList<Element>(1);
        if (currentContent.size() > 0) {
            List<Element> elements = new ArrayList<Element>();
            List<ListItem> listItems = new ArrayList<ListItem>();
            for (Element el : currentContent) {
                if (el instanceof ListItem) {
                    if (!elements.isEmpty()) {
                        processParagraphItems(ctx, tag, elements, l);
                        elements.clear();
                    }
                    listItems.add((ListItem)el);
                } else {
                    if (!listItems.isEmpty()) {
                        processListItems(ctx, tag, listItems, l);
                        listItems.clear();
                    }
                    elements.add(el);
                }
            }
            if (!elements.isEmpty()) {
                processParagraphItems(ctx, tag, elements, l);
                elements.clear();
            } else if (!listItems.isEmpty()) {
                processListItems(ctx, tag, listItems, l);
                listItems.clear();
            }
        }
		return l;
	}

    protected void processParagraphItems(final WorkerContext ctx, final Tag tag, final List<Element> paragraphItems, List<Element> l) {
        Paragraph p = new Paragraph();
        p.setMultipliedLeading(1.2f);
//        Element lastElement = paragraphItems.get(paragraphItems.size() - 1);
//        if (lastElement instanceof Chunk && Chunk.NEWLINE.getContent().equals(((Chunk) lastElement).getContent())) {
//            paragraphItems.remove(paragraphItems.size() - 1);
//        }
        Map<String, String> css = tag.getCSS();
        if (null != css.get(CSS.Property.TAB_INTERVAL)) {
            addTabIntervalContent(ctx, tag, paragraphItems, p, css.get(CSS.Property.TAB_INTERVAL));
            l.add(p);
        } else if (null != css.get(CSS.Property.TAB_STOPS)) { // <para tabstops=".." /> could use same implementation page 62
            addTabStopsContent(paragraphItems, p, css.get(CSS.Property.TAB_STOPS));
            l.add(p);
        } else if (null != css.get(CSS.Property.XFA_TAB_STOPS)) { // <para tabStops=".." /> could use same implementation page 63
            addTabStopsContent(paragraphItems, p, css.get(CSS.Property.XFA_TAB_STOPS)); // leader elements needs to be
            l.add(p);                                                                    // extracted.
        } else {
            List<Element> paraList = currentContentToParagraph(paragraphItems, true, true, tag, ctx);
            if (!l.isEmpty() && !paraList.isEmpty()) {
                Element firstElement = paraList.get(0);
                if (firstElement instanceof Paragraph ) {
                    ((Paragraph) firstElement).setSpacingBefore(0);
                }
            }
            for (Element e : paraList) {
                l.add(e);
            }
        }
    }
    protected void processListItems(final WorkerContext ctx, final Tag tag, final List<ListItem> listItems, List<Element> l) {
        try {
            com.itextpdf.text.List list = new com.itextpdf.text.List();
            list.setAlignindent(false);
            list = (com.itextpdf.text.List) getCssAppliers().apply(list, tag,
                getHtmlPipelineContext(ctx));
            list.setIndentationLeft(0);
            int i = 0;
            for (ListItem li : listItems) {
                li = (ListItem) getCssAppliers().apply(li, tag, getHtmlPipelineContext(ctx));
                if (i != listItems.size() - 1) {
                    li.setSpacingAfter(0);
                }
                if (i != 0 ) {
                    li.setSpacingBefore(0);
                }
                i++;
                li.setMultipliedLeading(1.2f);
                list.add(li);
            }
            if (!l.isEmpty()) {
                Element latestElement = l.get(l.size() - 1);
                if (latestElement instanceof Paragraph ) {
                    ((Paragraph) latestElement).setSpacingAfter(0);
                }
            }
            l.add(list);
        } catch (NoCustomContextException e) {
            throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
        }
    }

	/**
	 * Applies the tab interval of the p tag on its {@link TabbedChunk} elements. <br />
	 * The style "xfa-tab-count" of the {@link TabbedChunk} is multiplied with the tab interval of the p tag. This width is then added to a new {@link TabbedChunk}.</br>
	 * Elements other than TabbedChunks are added directly to the given Paragraph p.
	 *
	 * @param currentContent containing the elements inside the p tag.
	 * @param p paragraph to which the tabbed chunks will be added.
	 * @param value the value of style "tab-interval".
	 */
	private void addTabIntervalContent(final WorkerContext ctx, final Tag tag, final List<Element> currentContent, final Paragraph p, final String value) {
		float width = 0;
		for(Element e: currentContent) {
			if (e instanceof TabbedChunk) {
				width += ((TabbedChunk) e).getTabCount()*CssUtils.getInstance().parsePxInCmMmPcToPt(value);
				TabbedChunk tab = new TabbedChunk(new VerticalPositionMark(), width, false);
				p.add(new Chunk(tab));
				p.add(new Chunk((TabbedChunk) e));
			} else {
                if (e instanceof LineSeparator) {
                    try {
                        HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
                        Chunk newLine = (Chunk)getCssAppliers().apply(new Chunk(Chunk.NEWLINE), tag, htmlPipelineContext);
                        p.add(newLine);
                    } catch (NoCustomContextException e1) {
                        throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e1);
                    }
                }
				p.add(e);
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
                else {
                    p.add(e);
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
