/**
 *
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
	 * This method shows you how to setup the processing yourself. This is how it's done in the {@link XMLWorkerHelper}
	 * @throws IOException if something with IO went wrong.
	 * @throws DocumentException if something with the document goes wrong.
	 */
	@Test
	public void setupDefaultProcessingYourself() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
		"./target/test-classes/examples/columbus2.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext();
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext , new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}
	/**
	 * Define an ImageRoot. You'll see that the document columbus3.pdf now has images.
	 * @throws IOException if something with IO went wrong.
	 * @throws DocumentException if something with the document goes wrong.
	 */
	@Test
	public void addingAnImageRoot() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
		"./target/test-classes/examples/columbus3.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext();
		htmlContext.setImageProvider(new AbstractImageProvider() {

			public String getImageRootPath() {
				return "http://www.gutenberg.org/dirs/1/8/0/6/18066/18066-h/";
			}
		}).setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext , new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}
	/**
	 * Define an ImageRoot. You'll see that the document columbus3.pdf now has images.
	 * @throws IOException if something with IO went wrong.
	 * @throws DocumentException if something with the document goes wrong.
	 */
	@Test
	public void addingALinkProvider() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
		"./target/test-classes/examples/columbus3.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext();
		htmlContext.setLinkProvider(new LinkProvider() {

			public String getLinkRoot() {
				return "http://www.gutenberg.org/dirs/1/8/0/6/18066/18066-h/";
			}
		}).setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext , new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}
}
