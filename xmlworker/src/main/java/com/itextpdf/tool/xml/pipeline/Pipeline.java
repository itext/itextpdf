/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.NotImplementedException;

/**
 * @author Balder Van Camp
 *
 */
public interface Pipeline {

	public void setContext(WorkerContext context);

	public Pipeline open(Tag t, ProcessObject po) throws PipelineException;

	public Pipeline content(Tag t, String content, ProcessObject po) throws PipelineException;

	public Pipeline close(Tag t, ProcessObject po) throws PipelineException;

	/**
	 * @return
	 */
	public Pipeline getNext();

	/**
	 * @return
	 * @throws NotImplementedException
	 */
	public CustomContext getCustomContext() throws NoCustomContextException;
}
