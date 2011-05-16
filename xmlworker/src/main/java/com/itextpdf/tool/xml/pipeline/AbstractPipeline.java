/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import com.itextpdf.tool.xml.Tag;

/**
 * @author Balder Van Camp
 *
 */
public abstract class AbstractPipeline implements Pipeline {

	private WorkerContext context;
	private final Pipeline next;

	/**
	 *
	 */
	public AbstractPipeline(final Pipeline next) {
		this.next = next;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#setContext(com.itextpdf
	 * .tool.xml.pipeline.WorkerContext)
	 */
	public void setContext(final WorkerContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getNext()
	 */
	public Pipeline getNext() {
		return next;
	}

	public WorkerContext getContext() {
		return this.context;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#content(com.itextpdf.tool
	 * .xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	public Pipeline content(final Tag t, final String content, final ProcessObject po) throws PipelineException {
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		return getNext();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getCustomContext()
	 */
	public CustomContext getCustomContext() throws NoCustomContextException {
		throw new NoCustomContextException();
	}
}
