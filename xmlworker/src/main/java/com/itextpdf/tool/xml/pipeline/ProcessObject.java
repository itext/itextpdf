/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import java.util.LinkedList;
import java.util.Queue;


/**
 * @author Balder Van Camp
 *
 */
public class ProcessObject {

	private final Queue<Writable> queue;
	/**
	 *
	 */
	public ProcessObject() {
		queue = new LinkedList<Writable>();
	}
	/**
	 * @return true if a writable is contained.
	 */
	public boolean containsWritable() {
		return !queue.isEmpty();
	}

	/**
	 * @return a Writable or null if none.
	 */
	public Writable poll() {
		return queue.poll();
	}
	/**
	 * @param writable
	 */
	public void add(final Writable writable) {
		queue.add(writable);
	}

}
