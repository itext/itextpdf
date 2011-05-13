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
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.XMLWorkerImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.taglistener.AutoPDFTagListener;
import com.itextpdf.tool.xml.taglistener.FileMaker;

/**
 * This test class show you how to work with the {@link AutoPDFTagListener}.
 *
 * @author redlab_b
 *
 */
public class TagListenerExample002 {

	private XMLWorkerConfigurationImpl config;
	private XMLWorkerHelper helper;
	private File root;

	/**
	 * Initialize some stuff.
	 */
	@Before
	public void setup() {
		config = new XMLWorkerConfigurationImpl();
		helper = new XMLWorkerHelper();
		root = new File("./target/out/");
		root.mkdirs();
	}

	/**
	 * This test shows you how to use a TagListener that can continuously create documents.
	 *
	 * @throws DocumentException
	 * @throws IOException
	 */
	@Test
	public void showCaseAutoPDFTagListener() throws IOException {
		AutoPDFTagListener autoPDFTagListener = new AutoPDFTagListener(new FileMaker() {

			private int i = 1;

			public OutputStream getStream() throws IOException {
				return new FileOutputStream(new File(root, "TagListenerExample002_"+i++ +".pdf"));
			}
		}, config,PageSize.A4, helper.getDefaultCSS());
		// Setup config
		// Accept unknown tags
		config.acceptUnknown(true)
		.isParsingHTML(true)
		.autoBookMark(true)
		// Set the default HTML TagProcessers
		.tagProcessorFactory(Tags.getHtmlTagProcessorFactory())
		// Set the TagListener
		.addTagListener(autoPDFTagListener);
		// Read the html
		BufferedInputStream bis = new BufferedInputStream(TagListenerExample002.class.getResourceAsStream("/multiple.demo"));
		// And parse.
		final XMLWorker worker = new XMLWorkerImpl(config);
		worker.setDocumentListener(autoPDFTagListener);
		XMLParser p = new XMLParser(config.isParsingHTML(), worker);
		p.parse(new InputStreamReader(bis));
		// No need to open or close the document, that's done by the DocMarginListener
	}
}
