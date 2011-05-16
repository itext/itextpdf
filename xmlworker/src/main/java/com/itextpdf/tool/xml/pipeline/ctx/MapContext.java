/**
 *
 */
package com.itextpdf.tool.xml.pipeline.ctx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itextpdf.tool.xml.pipeline.CustomContext;

/**
 * @author Balder Van Camp
 *
 */
public class MapContext implements CustomContext {

	private final Map<String, Object> map;

	/**
	 *
	 */
	public MapContext() {
		map = new ConcurrentHashMap<String, Object>();
	}

	public Object get(final String key) {
		return map.get(key);
	}

	public void put(final String key, final Object o) {
		map.put(key, o);
	}

}
