/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

/**
 * @author Balder Van Camp
 *
 */
public interface WorkerContext {

	/**
	 * @param klass
	 * @param name
	 * @return
	 */
	CustomContext get(Class<?> klass);

}
