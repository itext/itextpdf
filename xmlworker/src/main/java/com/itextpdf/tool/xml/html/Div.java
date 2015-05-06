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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;

/**
 * @author redlab_b
 *
 */
public class Div extends AbstractTagProcessor {

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag, java.lang.String)
	 */
	@Override
	public List<Element> content(final WorkerContext ctx, final Tag tag, final String content) {
		List<Chunk> sanitizedChunks = HTMLUtils.sanitize(content, false);
        NoNewLineParagraph noNewLineParagraph = new NoNewLineParagraph();
		List<Element> l = new ArrayList<Element>(1);
        for (Chunk sanitized : sanitizedChunks) {
            Font f = getCssAppliers().getChunkCssAplier().applyFontStyles(tag);
            sanitized.setFont(f);
            noNewLineParagraph.add(sanitized);
        }
        if (noNewLineParagraph.size() > 0) {
            try {
                l.add(getCssAppliers().apply(noNewLineParagraph, tag, getHtmlPipelineContext(ctx)));
            } catch (NoCustomContextException e) {
                throw new RuntimeWorkerException(e);
            }
        }
		return l;
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
		try {
			Paragraph p = null;
			PdfDiv div = (PdfDiv)getCssAppliers().apply(new PdfDiv(), tag, getHtmlPipelineContext(ctx));
            int direction = getRunDirection(tag);
            if (direction != PdfWriter.RUN_DIRECTION_DEFAULT) {
                div.setRunDirection(direction);
            }
			for (Element e : currentContent) {
				if (e instanceof Paragraph || e instanceof PdfPTable|| e instanceof PdfDiv) {
					if (p != null) {
                        if (p.trim()) {
						    div.addElement(p);
                        }
						p = null;
					}
					div.addElement(e);
                } else {
					if (p == null) {
						p = new Paragraph();
                        p.setAlignment(div.getTextAlignment());
                        if (direction == PdfWriter.RUN_DIRECTION_RTL) {
                            switch (p.getAlignment()) {
                                case Element.ALIGN_UNDEFINED:
                                case Element.ALIGN_CENTER:
                                case Element.ALIGN_JUSTIFIED:
                                case Element.ALIGN_JUSTIFIED_ALL:
                                    break;
                                case Element.ALIGN_RIGHT:
                                    p.setAlignment(Element.ALIGN_LEFT);
                                    break;
                                case Element.ALIGN_LEFT:
                                default:
                                    p.setAlignment(Element.ALIGN_RIGHT);
                                    break;
                            }
                        }
                        p.setMultipliedLeading(1.2f);
					}
					p.add(e);
				}
			}
			if (p != null && p.trim()) {
                div.addElement(p);
			}

			List<Element> l = new ArrayList<Element>(1);
            l.add(div);
			return l;
		} catch (NoCustomContextException e) {
			throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
		}
	}

	 /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
     */
    @Override
	public boolean isStackOwner() {
        return true;
    }
}
