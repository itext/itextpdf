/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import com.itextpdf.tool.xml.CSSResolver;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.CustomContext;
import com.itextpdf.tool.xml.pipeline.NoCustomContextException;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.PipelineException;
import com.itextpdf.tool.xml.pipeline.ProcessObject;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;

/**
 * @author Balder Van Camp
 *
 */
public class CssResolverPipeline extends AbstractPipeline {

	private final CSSResolver resolver;

	/**
	 * @param next
	 * @param cssResolver
	 */
	public CssResolverPipeline(final CSSResolver cssResolver, final Pipeline next) {
		super(next);
		this.resolver = cssResolver;
	}

	/**
	 *
	 */
	public static final String CSS_RESOLVER = "CSS_RESOLVER";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		CustomContext cc = getContext().get(CssResolverPipeline.class);
		if (null != cc) {
			((CSSResolver) ((MapContext) cc).get(CSS_RESOLVER)).resolveStyles(t);
		}
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getNewCustomContext()
	 */
	public CustomContext getCustomContext() throws NoCustomContextException {
		MapContext mc = new MapContext();
		mc.put(CSS_RESOLVER, this.resolver);
		return mc;
	}

}
