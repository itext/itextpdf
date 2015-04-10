/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package book.pdftemplates;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author iText
 */
public class FillTemplateHelper extends PdfPageEventHelper {
    // initialized in constructor
    protected PdfReader reader;
    protected Rectangle pageSize;
    protected Rectangle body;
    protected float mLeft, mRight, mTop, mBottom;
    protected Rectangle to;
    protected Rectangle from;
    protected Rectangle date;
    protected Rectangle footer;
    protected BaseFont basefont;
    protected Font font;
    // initialized with setter
    protected String sender = "";
    protected String receiver = "";
    // initialized upon opening the document
    protected PdfTemplate background;
    protected PdfTemplate total;
    protected String today;
        
    public FillTemplateHelper(String stationery) throws IOException, DocumentException {
        reader = new PdfReader(stationery);
        AcroFields fields = reader.getAcroFields();
        pageSize = reader.getPageSize(1);
        body = fields.getFieldPositions("body").get(0).position;
        mLeft = body.getLeft() - pageSize.getLeft();
        mRight = pageSize.getRight() - body.getRight();
        mTop = pageSize.getTop() - body.getTop();
        mBottom = body.getBottom() - pageSize.getBottom();
        to = fields.getFieldPositions("to").get(0).position;
        from = fields.getFieldPositions("from").get(0).position;
        date = fields.getFieldPositions("date").get(0).position;
        footer = fields.getFieldPositions("footer").get(0).position;
        basefont = BaseFont.createFont();
        font = new Font(basefont, 12);
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Rectangle getPageSize() {
        return pageSize;
    }

    public float getmLeft() {
        return mLeft;
    }

    public float getmRight() {
        return mRight;
    }

    public float getmTop() {
        return mTop;
    }

    public float getmBottom() {
        return mBottom;
    }
    
    public Rectangle getBody() {
        return body;
    }
        
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        background = writer.getImportedPage(reader, 1);
        total = writer.getDirectContent().createTemplate(30, 15);
        today = DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(new Date());
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();
        // background
        canvas.addTemplate(background, 0, 0);
        try {
            // date
            ColumnText ct = new ColumnText(canvas);
            ct.setSimpleColumn(date);
            ct.addText(new Chunk(today, font));
            ct.go();
            // footer (page X of Y)
            ct.setSimpleColumn(footer);
            ct.addText(new Chunk("page " + writer.getPageNumber(), font));
            ct.addText(new Chunk(Image.getInstance(total), 0, 0));
            ct.go();
            // from address
            ct.setSimpleColumn(from);
            ct.addElement(new Paragraph(sender, font));
            ct.go();
            // to address
            ct.setSimpleColumn(to);
            ct.addElement(new Paragraph(receiver, font));
            ct.go();
        } catch (DocumentException e) {
            // can never happen, but if it does, we want to know!
            throw new ExceptionConverter(e);
        }
    }
    
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // we only know the total number of pages at the moment the document is closed.
        String s = "/" + (writer.getPageNumber() - 1);
        Phrase p = new Phrase(12, s, font);
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT, p, 0.5f, 0, 0);
    }

    public static ElementList parseHtml(String content, String style, TagProcessorFactory tagProcessors) throws IOException {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(style));
        cssResolver.addCss(cssFile);
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(tagProcessors);
        htmlContext.autoBookmark(false);
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline html = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new FileInputStream(content));
        return elements;
    }
}
