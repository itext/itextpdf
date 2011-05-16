/**
 *
 */
package com.itextpdf.tool.xml.pipeline.ctx;

import com.itextpdf.tool.xml.exceptions.NotImplementedException;
import com.itextpdf.tool.xml.pipeline.CustomContext;
import com.itextpdf.tool.xml.pipeline.WorkerContext;

/**
 * @author Balder Van Camp
 *
 */
public class WorkerContextImpl extends MapContext implements WorkerContext {

	/**
	 *
	 */
	public WorkerContextImpl() {
		super();
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.WorkerContext#get(java.lang.Class)
	 */
	public CustomContext get(final Class<?> klass) {
		return (CustomContext) super.get(klass.getName());
	}
	/**
	 *
	 * @param klass
	 * @param context
	 */
	public void add(final Class<?> klass, final CustomContext context) {
		super.put(klass.getName(), context);
	}
	/**
	 * Accepts {@link CustomContext} objects only.
	 * @throws IllegalArgumentException if anything else then a {@link CustomContext} is passed as argument.
	 */
	@Override
	public void put(final String key, final Object o) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.ctx.MapContext#get(java.lang.String)
	 */
	@Override
	public Object get(final String key) {
		throw new NotImplementedException();
	}


}
