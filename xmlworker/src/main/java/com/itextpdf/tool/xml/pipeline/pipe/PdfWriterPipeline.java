/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.pipeline.CustomContext;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.NoCustomContextException;
import com.itextpdf.tool.xml.pipeline.PipelineException;
import com.itextpdf.tool.xml.pipeline.ProcessObject;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.Writable;
import com.itextpdf.tool.xml.pipeline.WritableDirect;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;

/**
 * @author Balder Van Camp
 *
 */
public class PdfWriterPipeline extends AbstractPipeline {

	private Document doc;
	private PdfWriter writer;

	/**
	 * @param next
	 */
	public PdfWriterPipeline() {
		super(null);
	}

	/**
	 * @param next
	 */
	public PdfWriterPipeline(final Pipeline next) {
		super(next);
	}

	/**
	 * @param doc2
	 * @param writer2
	 */
	public PdfWriterPipeline(final Document doc, final PdfWriter writer) {
		super(null);
		this.doc = doc;
		this.writer = writer;
	}

	/**
	 *
	 */
	public static final String DOCUMENT = "DOCUMENT";
	public static final String WRITER = "WRITER";
	public static final String CONTINUOUS = "CONTINUOUS";

	/**
	 * @param po
	 * @throws PipelineException
	 */
	private void write(final ProcessObject po) throws PipelineException {
		CustomContext cc = getContext().get(PdfWriterPipeline.class);
		MapContext mp = (MapContext) cc;
		if (po.containsWritable()) {
			Document doc = (Document) mp.get(DOCUMENT);
			PdfWriter writer = (PdfWriter) mp.get(WRITER);
			boolean continuousWrite = (Boolean) mp.get(CONTINUOUS);
			Writable writable = null;
			while (null != (writable = po.poll())) {
				if (writable instanceof WritableElement) {
					for (Element e : ((WritableElement) writable).elements()) {
						try {
							if (!doc.add(e)) {
								// do something: log ?
							}
						} catch (DocumentException e1) {
							if (!continuousWrite) {
								throw new PipelineException(e1);
							} else {
								// TODO log
							}
						}
					}
				} else if (writable instanceof WritableDirect) {
					try {
						((WritableDirect) writable).write(writer, doc);
					} catch (DocumentException e) {
						if (!continuousWrite) {
							throw new PipelineException(e);
						} else {
							// TODO log
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		write(po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#content(com.itextpdf.tool
	 * .xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline content(final Tag t, final String content, final ProcessObject po) throws PipelineException {
		write(po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		write(po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getNewCustomContext()
	 */
	public CustomContext getCustomContext() throws NoCustomContextException {
		MapContext mc = new MapContext();
		mc.put(CONTINUOUS, Boolean.TRUE);
		mc.put(DOCUMENT, doc);
		mc.put(WRITER, writer);
		return mc;
	}

	public void setDocument(final Document document) {
		this.doc = document;
	}

	public void setWriter(final PdfWriter writer) {
		this.writer = writer;
	}
}
