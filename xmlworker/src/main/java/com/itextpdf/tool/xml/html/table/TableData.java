/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.tool.xml.html.table;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.AbstractTagProcessor;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author redlab_b
 * 
 */
public class TableData extends AbstractTagProcessor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document, java.lang.String)
	 */
	@Override
	public List<Element> content(final WorkerContext ctx, final Tag tag,
			final String content) {
		return textContent(ctx, tag, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document)
	 */
	@Override
	public List<Element> end(final WorkerContext ctx, final Tag tag,
			final List<Element> currentContent) {
		HtmlCell cell = new HtmlCell();
                int direction = getRunDirection(tag);
                if (direction != PdfWriter.RUN_DIRECTION_DEFAULT) {
                    cell.setRunDirection(direction);
                }

        if (HTML.Tag.TH.equalsIgnoreCase(tag.getName())) {
            cell.setRole(PdfName.TH);
        }
        try {
            HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
            cell = (HtmlCell) getCssAppliers().apply(cell, tag, htmlPipelineContext);
        } catch (NoCustomContextException e1) {
            throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e1);
        }

		List<Element> l = new ArrayList<Element>(1);
        List<Element> chunks = new ArrayList<Element>();
        List<ListItem> listItems = new ArrayList<ListItem>();
        int index = -1;
		for (Element e : currentContent) {
            index++;
            if (e instanceof Chunk || e instanceof NoNewLineParagraph || e instanceof LineSeparator) {
                if (!listItems.isEmpty()) {
                    processListItems(ctx, tag, listItems, cell);
                }
                if (e instanceof Chunk && Chunk.NEWLINE.getContent().equals(((Chunk)e).getContent())) {
                    if (index == currentContent.size() - 1) {
                        continue;
                    } else {
                        Element nextElement = currentContent.get(index + 1);
                        if (!chunks.isEmpty() && !(nextElement instanceof Chunk) && !(nextElement instanceof NoNewLineParagraph)) {
                            continue;
                        }
                    }
                } else if (e instanceof LineSeparator) {
                    try {
                        HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
                        Chunk newLine = (Chunk)getCssAppliers().apply(new Chunk(Chunk.NEWLINE), tag, htmlPipelineContext);
                        chunks.add(newLine);
                    } catch (NoCustomContextException e1) {
                        throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e1);
                    }
                }
                chunks.add(e);
                continue;
            } else if (e instanceof ListItem) {
                if (!chunks.isEmpty()) {
                    processChunkItems(chunks, cell);
                }
                listItems.add((ListItem)e);
                continue;
            } else {
                if (!chunks.isEmpty()) {
                    processChunkItems(chunks, cell);
                }
                if (!listItems.isEmpty()) {
                    processListItems(ctx, tag, listItems, cell);
                }
            }

            if (e instanceof Paragraph) {
                if ( ((Paragraph)e).getAlignment() == Element.ALIGN_UNDEFINED ) {
                    ((Paragraph)e).setAlignment(cell.getHorizontalAlignment());
                }
            }

			cell.addElement(e);
		}
        if (!chunks.isEmpty()) {
            processChunkItems(chunks, cell);
        }
    	l.add(cell);
		return l;
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

    protected void processChunkItems(List<Element> chunks, HtmlCell cell) {
        Paragraph p = new Paragraph();
        p.setMultipliedLeading(1.2f);
        p.addAll(chunks);
        p.setAlignment(cell.getHorizontalAlignment());
        if (p.trim()) {
            cell.addElement(p);
        }
        chunks.clear();
    }

    protected void processListItems(final WorkerContext ctx, final Tag tag, List<ListItem> listItems, HtmlCell cell) {
        try {
            com.itextpdf.text.List list = new com.itextpdf.text.List();
            list.setAutoindent(false);
            list = (com.itextpdf.text.List) getCssAppliers().apply(list, tag,
                    getHtmlPipelineContext(ctx));
            list.setIndentationLeft(0);
            for (ListItem li : listItems) {
                li = (ListItem) getCssAppliers().apply(li, tag, getHtmlPipelineContext(ctx));
                li.setSpacingAfter(0);
                li.setSpacingBefore(0);

                li.setMultipliedLeading(1.2f);
                list.add(li);
            }
            cell.addElement(list);
            listItems.clear();
        } catch (NoCustomContextException e) {
            throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
        }
    }

}
