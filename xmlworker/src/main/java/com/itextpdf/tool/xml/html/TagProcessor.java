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

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;

/**
 * @author redlab_b
 *
 */
public interface TagProcessor {


    /**
     * This method is called when a tag has been encountered.
     * @param ctx the WorkerContext
     * @param tag the tag encountered
     *
     * @return Element an Element to add to the current content;
     */
    List<Element> startElement(WorkerContext ctx, Tag tag);

    /**
     * This method is called if there is text content encountered between the
     * opening and closing tags this TagProcessor is mapped to.
     * @param ctx the WorkerContext
     * @param tag the tag encountered
     * @param content the text content between the tags this TagProcessor is
     *        mapped to.
     *
     * @return the element to add to the currentContent list
     */
    List<Element> content(WorkerContext ctx, Tag tag, String content);

	/**
	 * This method is called when a closing tag has been encountered of the
	 * TagProcessor implementation that is mapped to the tag.
	 * @param ctx the WorkerContext
	 * @param tag the tag encountered
	 * @param currentContent a list of content possibly created by TagProcessing
	 *            of inner tags, and by <code>startElement</code> and
	 *            <code>content</code> methods of this <code>TagProcessor</code>
	 *            .
	 *
	 * @return the resulting element to add to the document or a content stack.
	 */
    List<Element> endElement(WorkerContext ctx, Tag tag, List<Element> currentContent);

    /**
     * @return true if the tag implementation must keep it's own currentContent
     *         stack.
     */
    boolean isStackOwner();


}
