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
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Balder Van Camp
 *
 */
public class XMLWorkerHTMLExample extends Setup {

	@Test
	public void test() throws IOException, DocumentException {
		Document doc = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(
		"./target/test-classes/examples/columbus2.pdf")));
		doc.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext();
		CSSResolver cssResolver = new StyleAttrCSSResolver();
		cssResolver.addCss(XMLWorkerHelper.getInstance().getDefaultCSS());
		htmlContext.setImageProvider(new AbstractImageProvider() {

			public String getImageRootPath() {
				return "http://www.gutenberg.org/dirs/1/8/0/6/18066/18066-h/";
			}
		}).setPageSize(PageSize.A4).setTagFactory(Tags.getHtmlTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext , new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}
}
