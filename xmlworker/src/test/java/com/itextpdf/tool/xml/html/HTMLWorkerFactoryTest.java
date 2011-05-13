/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
import java.util.List;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;


/**
 * @author Balder
 *
 */
public class HTMLWorkerFactoryTest {
    public static final String RESOURCE_TEST_PATH = "./target/test-classes/";
	public static final String SNIPPETS = "/snippets/";

//    private static final String TEST = "xfa-support_";
//    private static final String TEST = "Atkins_";
//    private static final String TEST = "b-p_";
//    private static final String TEST = "br-sub-sup_";
//    private static final String TEST = "font_color_";
//    private static final String TEST = "fontSizes_";
//    private static final String TEST = "line-height_letter-spacing_";
//    private static final String TEST = "longtext_";
//    private static final String TEST = "xfa-support_";
//    private static final String TEST = "margin-align_";
//    private static final String TEST = "xfa-hor-vert_";
//    private static final String TEST = "text-indent_text-decoration_";
//    private static final String TEST = "comment-double-print_";
//    private static final String TEST = "tab_";
//	  private static final String TEST = "table_";

//	  private static final String TEST = "tableInTable_";
//	  private static final String TEST = "lists_";
//	  private static final String TEST = "img_";
//	  private static final String TEST = "position_";
//	  private static final String TEST = "h_";
//	  private static final String TEST = "booksales_";
//	  private static final String TEST = "index_";
	  private static final String TEST = "headers_";
//	  private static final String TEST = "headers_noroottag_";
//	  private static final String TEST = "index_anchor_";
//	  private static final String TEST = "lineheight_";
//	  private static final String TEST = "table_exception_";
//	  private static final String TEST = "table_exception_";
//	  private static final String TEST = "pagebreaks_";

    static {
    	FontFactory.registerDirectories();
    	Document.compress = false;
    }
    private final CssUtils utils = CssUtils.getInstance();

	@Test
	public void parseXfaOnlyXML() throws IOException {
		final Document doc = new Document(PageSize.A4);
		float margin = utils.parseRelativeValue("10%", PageSize.A4.getWidth());
		doc.setMargins(margin, margin, margin, margin);
		PdfWriter writer = null;
		try {
            writer = PdfWriter.getInstance(doc, new FileOutputStream(
                    RESOURCE_TEST_PATH
                            +TEST+"Test.pdf"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		XMLWorkerConfigurationImpl conf = new XMLWorkerConfigurationImpl();
		conf.document(doc).pdfWriter(writer);
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver();

		conf.tagProcessorFactory(new Tags().getHtmlTagProcessorFactory()).cssResolver(cssResolver)
				.acceptUnknown(true);

		BufferedInputStream bis = new BufferedInputStream(HTMLWorkerFactoryTest.class.getResourceAsStream(SNIPPETS+TEST+"snippet.html"));
		XMLWorkerHelper helper = new XMLWorkerHelper();
		CssFile defaultCSS = helper.getDefaultCSS();
		if (null != defaultCSS) {
			cssResolver.addCssFile(defaultCSS);
		}
		doc.open();
		helper.parseXML(new ElementHandler() {

			public void addAll(final List<Element> currentContent) throws DocumentException {
				for (Element e : currentContent) {
					doc.add(e);
				}

			}

			public void add(final Element e) throws DocumentException {
				doc.add(e);
			}
		}, new InputStreamReader(bis), conf);
        doc.close();
	}
}
