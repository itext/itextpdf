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
package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;

/**
 * @author itextpdf.com
 *
 */
public class XMLWorkerHTMLExample extends Setup {

	/**
	 * This method shows you how to setup the processing yourself. This is how
	 * it's done in the {@link XMLWorkerHelper}
	 *
	 * @throws IOException if something with IO went wrong.
	 * @throws DocumentException if something with the document goes wrong.
	 */
	@Test
	public void setupDefaultProcessingYourself() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
				"./src/test/resources/examples/columbus2.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext,
				new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}

	/**
	 * Define an ImageRoot. You'll see that the document columbus3.pdf now has
	 * images.
	 *
	 * @throws IOException if something with IO went wrong.
	 * @throws DocumentException if something with the document goes wrong.
	 */
	@Test
	public void addingAnImageRoot() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
				"./src/test/resources/examples/columbus3.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setImageProvider(new AbstractImageProvider() {

			public String getImageRootPath() {
				return "http://www.gutenberg.org/dirs/1/8/0/6/18066/18066-h/";
			}
		}).setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext,
				new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}

	/**
	 * Define a LinProvider. You'll see that the document columbus3.pdf now
	 * links that point to the right url.
	 *
	 * @throws IOException if something with IO went wrong.
	 * @throws DocumentException if something with the document goes wrong.
	 */
	@Test
	public void addingALinkProvider() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
				"./src/test/resources/examples/columbus3.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setLinkProvider(new LinkProvider() {

			public String getLinkRoot() {
				return "http://www.gutenberg.org/dirs/1/8/0/6/18066/18066-h/";
			}
		}).setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext,
				new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}
}
