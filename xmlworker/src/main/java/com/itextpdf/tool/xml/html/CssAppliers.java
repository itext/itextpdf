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
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.css.apply.PageSizeContainable;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;

/**
 * @author redlab
 *
 */
public interface CssAppliers {

	/**
	 * Given the element e, this method will lookup the right applier for the given Element. The mapping is done by
	 * instance of.<br />
	 * order of check:
	 * <ol>
	 * <li>{@link Chunk}</li>
	 * <li>{@link Paragraph}</li>
	 * <li>{@link NoNewLineParagraph}</li>
	 * <li>{@link HtmlCell}</li>
	 * <li>{@link List}</li>
	 * <li>{@link LineSeparator}</li>
	 * <li>{@link Image}</li>
	 * </ol>
	 *
	 * @param e the Element
	 * @param t the tag
	 * @param mm the MarginMemory
	 * @param psc the {@link PageSize} container
	 * @param ip an ImageProvider
	 * @return the element with CSS applied onto, note: the element can be a new element.
	 */
	Element apply(Element e, final Tag t, final MarginMemory mm, final PageSizeContainable psc, final ImageProvider ip);

	/**
	 * Convenience method.
	 *
	 * @see #apply(Element, Tag, MarginMemory, PageSizeContainable, ImageProvider)
	 * @param e the Element
	 * @param t the tag
	 * @param ctx the Context object
	 * @return the element with CSS applied onto, note: the element can be a new element.
	 */
	Element apply(final Element e, final Tag t, final HtmlPipelineContext ctx);

	/**
	 * @return the chunk css applier
	 */
	ChunkCssApplier getChunkCssAplier();
	
	public void setChunkCssAplier(final ChunkCssApplier chunkCssAplier);

    CssAppliers clone();

}
