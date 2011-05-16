/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.PipelineException;
import com.itextpdf.tool.xml.pipeline.ProcessObject;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;

/**
 * @author Balder Van Camp
 *
 */
public class AutoDocPipeline extends AbstractPipeline {

	private final FileMaker fm;
	private final String tag;
	private final String opentag;

	/**
	 * @param fm
	 * @param tag
	 * @param opentag
	 * @param next
	 *
	 */
	public AutoDocPipeline(final FileMaker fm, final String tag, final String opentag, final Pipeline next) {
		super(next);
		this.fm = fm;
		this.tag = tag;
		this.opentag = opentag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.AbstractPipeline#open(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		String tagName = t.getTag();
		if (tag.equals(tagName)) {
			MapContext cc = (MapContext) getContext().get(PdfWriterPipeline.class);
			Document d = new Document();
			try {
				OutputStream os = fm.getStream();
				cc.put(PdfWriterPipeline.DOCUMENT, d);
				cc.put(PdfWriterPipeline.WRITER, PdfWriter.getInstance(d, os));
			} catch (IOException e) {
				throw new PipelineException(e);
			} catch (DocumentException e) {
				throw new PipelineException(e);
			}
		}
		if (t.getTag().equalsIgnoreCase(opentag)) {
			MapContext cc = (MapContext) getContext().get(PdfWriterPipeline.class);
			Document d = (Document) cc.get(PdfWriterPipeline.DOCUMENT);
			CssUtils cssUtils = CssUtils.getInstance();
			float pageWidth = d.getPageSize().getWidth();
			float marginLeft = 0;
			float marginRight = 0;
			float marginTop = 0;
			float marginBottom = 0;
			Map<String, String> css = t.getCSS();
			for (Entry<String, String> entry : css.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (key.equalsIgnoreCase(CSS.Property.MARGIN_LEFT)) {
					marginLeft = cssUtils.parseValueToPt(value, pageWidth);
				} else if (key.equalsIgnoreCase(CSS.Property.MARGIN_RIGHT)) {
					marginRight = cssUtils.parseValueToPt(value, pageWidth);
				} else if (key.equalsIgnoreCase(CSS.Property.MARGIN_TOP)) {
					marginTop = cssUtils.parseValueToPt(value, pageWidth);
				} else if (key.equalsIgnoreCase(CSS.Property.MARGIN_BOTTOM)) {
					marginBottom = cssUtils.parseValueToPt(value, pageWidth);
				}
			}
			d.setMargins(marginLeft, marginRight, marginTop, marginBottom);
			d.open();
		}

		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.AbstractPipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		String tagName = t.getTag();
		if (tag.equals(tagName)) {
			MapContext cc = (MapContext) getContext().get(PdfWriterPipeline.class);
			Document d = (Document) cc.get(PdfWriterPipeline.DOCUMENT);
			d.close();
		}
		return getNext();
	}


}
