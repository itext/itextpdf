/**
 *
 */
package com.itextpdf.tool.xml.example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.taglistener.DocMarginTagListener;

/**
 * This test class show you how to work with the {@link DocMarginTagListener}.
 *
 * @author redlab_b
 *
 */
public class TagListenerExample001 {

	private XMLWorkerConfigurationImpl config;
	private XMLWorkerHelper helper;

	/**
	 * Initialize some stuff.
	 */
	@Before
	public void setup() {
		config = new XMLWorkerConfigurationImpl();
		helper = new XMLWorkerHelper();
		new File("./target/out/").mkdirs();
	}

	/**
	 * This test shows you how to use a TagListener to set margins based on the
	 * margins from the body tag and open the {@link Document}
	 *
	 * @throws DocumentException
	 * @throws IOException
	 */
	@Test
	public void showCaseSettingMargins() throws DocumentException, IOException {
		final Document doc = new Document(PageSize.A4);
		OutputStream os = new FileOutputStream(new File("./target/out/", "TagListenerExample001..pdf" ));
		final PdfWriter writer = PdfWriter.getInstance(doc, os);
		// Create a CSSResolver, this is responsible for finding CSS that belongs to tags.
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver();
		// Here we add a CSS File to the CSSResolver. The default CSS file, based on firefoxes default CSS.
		cssResolver.addCssFile(helper.getDefaultCSS());
		// Setup config
		// Accept unknown tags
		config.acceptUnknown(true)
		.isParsingHTML(true)
		// Set the default HTML TagProcessers
		.tagProcessorFactory(Tags.getHtmlTagProcessorFactory())
		// Set the cssResolver
		.cssResolver(cssResolver)
		.autoBookMark(false)
		.addTagListener(new DocMarginTagListener(writer, doc))
		.document(doc).pdfWriter(writer);
		// Read the html
		BufferedInputStream bis = new BufferedInputStream(TagListenerExample001.class.getResourceAsStream("/documentation.html"));
		// And parse.
		// No need to open or close the document, that's done by the DocMarginListener
		helper.parseXML(new ElementHandler() {

			public void addAll(final List<Element> currentContent) throws DocumentException {
				for (Element e : currentContent) {
					doc.add(e);
				}

			}

			public void add(final Element e) throws DocumentException {
				doc.add(e);
			}
		}, new InputStreamReader(bis), config);
		doc.close();
		//writer.close();

	}
}
