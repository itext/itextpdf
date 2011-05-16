/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.PipelineException;
import com.itextpdf.tool.xml.pipeline.ProcessObject;
import com.itextpdf.tool.xml.pipeline.Writable;

/**
 * @author Balder Van Camp
 *
 */
public class ElementHandlerPipeline extends AbstractPipeline {

	private final ElementHandler handler;

	/**
	 * @param next
	 */
	public ElementHandlerPipeline(final ElementHandler handler, final Pipeline next) {
		super(next);
		this.handler =handler;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#open(com.itextpdf.tool.xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		consume(po);
		return getNext();
	}

	/**
	 * @param po
	 */
	private void consume(final ProcessObject po) {
		if (po.containsWritable()) {
			Writable w = null;
			while ( null != (w =po.poll())) {
				handler.add(w);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#content(com.itextpdf.tool.xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline content(final Tag t, final String content, final ProcessObject po) throws PipelineException {
		consume(po);
		return getNext();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#close(com.itextpdf.tool.xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		consume(po);
		return getNext();
	}

}
