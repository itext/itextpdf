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
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.tool.xml.AbstractTagProcessor;
import com.itextpdf.tool.xml.Provider;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.css.apply.NoNewLineParagraphCssApplier;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;

/**
 * @author redlab_b
 *
 */
public class Anchor extends AbstractTagProcessor {

	private static final Logger logger = LoggerFactory.getLogger();
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#startElement(com.itextpdf.tool.xml
	 * .Tag, java.util.List, com.itextpdf.text.Document)
	 */
	@Override
	public List<Element> start(final Tag tag) {
		if (null != tag.getAttributes().get(HTML.Attribute.HREF)) {
			Map<String, String> css = tag.getCSS();
			if (css.get(CSS.Property.TEXT_DECORATION) == null) {
				css.put(CSS.Property.TEXT_DECORATION, CSS.Value.UNDERLINE);
			}
			if (css.get(CSS.Property.COLOR) == null) {
				css.put(CSS.Property.COLOR, "blue");
			}
		}
		return new ArrayList<Element>(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document, java.lang.String)
	 */
	@Override
	public List<Element> content(final Tag tag, final String content) {
		String sanitized = HTMLUtils.sanitizeInline(content);
		List<Element> l = new ArrayList<Element>(1);
		if (sanitized.length() > 0) {
			l.add(new ChunkCssApplier().apply(new Chunk(sanitized), tag));
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag, java.util.List, com.itextpdf.text.Document)
	 */
	@Override
	public List<Element> end(final Tag tag, final List<Element> currentContent) {
		String name = tag.getAttributes().get(HTML.Attribute.NAME);
		List<Element> elems = new ArrayList<Element>(0);
		if (currentContent.size() > 0) {
			NoNewLineParagraph p = new NoNewLineParagraph();
			String url = tag.getAttributes().get(HTML.Attribute.HREF);
			for (Element e : currentContent) {
				for (Chunk c : e.getChunks()) {
					if (null != url) {
						if (url.startsWith("#")) {
							if (logger.isLogging()) {
								logger.log(Anchor.class, String.format("Creating a local goto link to %s", url));
							}
							c.setLocalGoto(url.replaceFirst("#", ""));
						} else {
							// TODO check url validity?
							if (null != configuration.getProvider() && !url.startsWith("http")) {
								String root = configuration.getProvider().get(Provider.GLOBAL_LINK_ROOT);
								if (root.endsWith("/") && url.startsWith("/")) {
									root = root.substring(0, root.length() - 1);
								}
								url = root + url;
							}
							if (logger.isLogging()) {
								logger.log(Anchor.class, String.format("Creating a www link to %s", url));
							}
							c.setAnchor(url);
						}
					} else if (null != name) {
						c.setLocalDestination(name);
						if (logger.isLogging()) {
							logger.log(Anchor.class, String.format("Setting local destination for  %s", name));
						}
					}

				}
				p.add(e);
			}
			elems.add(new NoNewLineParagraphCssApplier(configuration).apply(p, tag));
		} else
		// !currentContent > 0 ; An empty "a" tag has been encountered.
		// we're using an anchor space hack here. without the space reader does not jump to destination
		if (null != configuration.getWriter() &&  null != name) {
			if (logger.isLogging()) {
				logger.log(Anchor.class, String.format("Trying to set local destination %s with space hack", name));
			}
			Chunk dest = new Chunk(" ").setLocalDestination(name);
			elems.add(dest);
			/*
			PdfWriter writer = configuration.getWriter();
			ColumnText c = new ColumnText(writer.getDirectContent());
			c.setSimpleColumn(new Phrase(dest), 1, writer.getVerticalPosition(false), 1, writer.getVerticalPosition(false), 5, Element.ALIGN_LEFT);
			try {
				c.go();
			} catch (DocumentException e) {
				throw new RuntimeWorkerException(e);
			}
			*/
		}
		return elems;
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
