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
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;


/**
 * @author Balder
 *
 */
public class HTMLWorkerFactoryTest {
    public static final String OUT = "./target/test-classes/com/itextpdf/tool/xml/html/";
//	public static final String SNIPPETS = "/snippets/";
	public static final String SNIPPETS = "/bugs/";

//	private static final String TEST = "doc_";
//    private static final String TEST = "xfa-support_";
//    private static final String TEST = "Atkins_";
//    private static final String TEST = "b-p_";
//    private static final String TEST = "br-sub-sup_";
//    private static final String TEST = "font_color_";
//    private static final String TEST = "fontSizes_";
//    private static final String TEST = "line-height_letter-spacing_";
//    private static final String TEST = "longtext_";
//    private static final String TEST = "error_message_test_";
//    private static final String TEST = "xfa-support_";
//    private static final String TEST = "margin-align_";
//    private static final String TEST = "xfa-hor-vert_";
//    private static final String TEST = "text-indent_text-decoration_";
//    private static final String TEST = "comment-double-print_";
//    private static final String TEST = "tab_";
//	  private static final String TEST = "table_";
//	  private static final String TEST = "tableInTable_";
//	  private static final String TEST = "table_incomplete_";
//	  private static final String TEST = "lists_";
//	  private static final String TEST = "img_";
//	  private static final String TEST = "position_";
//	  private static final String TEST = "h_";
//	  private static final String TEST = "booksales_";
//	  private static final String TEST = "index_";
//	  private static final String TEST = "headers_";
//	  private static final String TEST = "headers_noroottag_";
//	  private static final String TEST = "index_anchor_";
//	  private static final String TEST = "lineheight_";
//	  private static final String TEST = "table_exception_";
//	  private static final String TEST = "widthTable_";
//	  private static final String TEST ="test-table-a_";
//	  private static final String TEST ="test-table-b_";
//	  private static final String TEST ="test-table-c_";
//	  private static final String TEST ="test-table-d_";
//	  private static final String TEST = "pagebreaks_";

	// Bug snippets

	  private static final String TEST = "colored_lists_";

    static {
    	//FontFactory.registerDirectories();
    	Document.compress = false;
    	LoggerFactory.getInstance().setLogger(new SysoLogger(3));
    }
    private final CssUtils utils = CssUtils.getInstance();

	@Test
	public void parseXfaOnlyXML() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(HTMLWorkerFactoryTest.class.getResourceAsStream(String.format("%s%ssnippet.html", SNIPPETS, TEST)));
		final Document doc = new Document(PageSize.A4);
		float margin = utils.parseRelativeValue("10%", PageSize.A4.getWidth());
		doc.setMargins(margin, margin, margin, margin);
		PdfWriter writer = null;
		try {
            writer = PdfWriter.getInstance(doc, new FileOutputStream(
                    String.format("%s%sTest.pdf", OUT, TEST)));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		Pipeline pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		doc.open();
		XMLParser p = new XMLParser(true, worker);
		p.parse(new InputStreamReader(bis));
        doc.close();
	}
}
