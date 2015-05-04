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
package com.itextpdf.tool.xml.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.util.ParentTreeUtil;

/**
 * @author Emiel Ackermann, redlab_b
 *
 */
public class Header extends AbstractTagProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(Header.class);
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag, java.util.List, com.itextpdf.text.Document, java.lang.String)
	 */
    @Override
	public List<Element> content(final WorkerContext ctx, final Tag tag, final String content) {
    	return textContent(ctx, tag, content);
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag, java.util.List, com.itextpdf.text.Document)
	 */
    @Override
	public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
		List<Element> l = new ArrayList<Element>(1);
		ParentTreeUtil pt = new ParentTreeUtil();
		if (currentContent.size() > 0) {
			List<Element> currentContentToParagraph = currentContentToParagraph(currentContent, true, true, tag, ctx);
			final HtmlPipelineContext context;
			try {
				context = getHtmlPipelineContext(ctx);
				boolean oldBookmark = context.autoBookmark();
		
				if(!(pt.getParentTree(tag).isEmpty())&& pt.getParentTree(tag).contains(HTML.Tag.TD))
					context.autoBookmark(false);
				
				/*if(tag.getParent()!=null&&tag.getParent().getName().equals(HTML.Tag.TD)){
					context.autoBookmark(false);
					System.out.println(tag.getParent().getName());
				}*/
				
				if (context.autoBookmark()) {
					final Paragraph title = new Paragraph();
					for (Element w: currentContentToParagraph) {
							title.add(w);
					}

					l.add(new WritableDirectElement(WritableDirectElement.DIRECT_ELEMENT_TYPE_HEADER) {

						public void write(final PdfWriter writer, final Document doc) throws DocumentException {
							PdfDestination destination = new PdfDestination(PdfDestination.XYZ, 20,
									writer.getVerticalPosition(false), 0);
							Map<String, Object> memory = context.getMemory();
							HeaderNode tree = (HeaderNode) memory.get(HtmlPipelineContext.BOOKMARK_TREE);
							int level = getLevel(tag);
							if (null == tree) {
								// first h tag encounter
								tree = new HeaderNode(0, writer.getRootOutline(), null);
							} else {
								// calculate parent
								int lastLevel = tree.level();
								if (lastLevel == level) {
									tree = tree.parent();
								} else if (lastLevel > level) {
									while (lastLevel >= level) {
										lastLevel = tree.parent().level();
										tree = tree.parent();
									}
								}
							}
							if (LOGGER.isLogging(Level.TRACE)) {
								LOGGER.trace(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.ADD_HEADER), title.toString()));
							}
							HeaderNode node = new HeaderNode(level,new PdfOutline(tree.outline(), destination, title), tree);
							memory.put(HtmlPipelineContext.BOOKMARK_TREE, node);
						}
					});
				}
				context.autoBookmark(oldBookmark);
			} catch (NoCustomContextException e) {
				if (LOGGER.isLogging(Level.ERROR)) {
					LOGGER.error(LocaleMessages.getInstance().getMessage(LocaleMessages.HEADER_BM_DISABLED), e);
				}
			}
			l.addAll(currentContentToParagraph);
		}
		return l;
	}

    /**
	 * @param tag
	 * @return
	 */
	private int getLevel(final Tag tag) {
		return Integer.parseInt(Character.toString(tag.getName().charAt(1)));
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
