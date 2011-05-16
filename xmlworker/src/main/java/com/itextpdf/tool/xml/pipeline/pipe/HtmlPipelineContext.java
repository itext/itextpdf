/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.StackKeeper;
import com.itextpdf.tool.xml.TagProcessor;
import com.itextpdf.tool.xml.TagProcessorFactory;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.CustomContext;

/**
 * @author Balder Van Camp
 *
 */
public class HtmlPipelineContext implements CustomContext {

	private final LinkedList<StackKeeper> queue;
	private final boolean acceptUnknown = true;
	private final TagProcessorFactory tagFactory = Tags.getHtmlTagProcessorFactory();
	private final List<Element> ctn = new ArrayList<Element>();
	private final XMLWorkerConfig config;

	/**
	 *
	 */
	public HtmlPipelineContext(final XMLWorkerConfig config) {
		this.queue = new LinkedList<StackKeeper>();
		this.config = config;
	}
	/**
	 * @param tag
	 * @param nameSpace
	 * @return
	 */
	public TagProcessor resolveProcessor(final String tag, final String nameSpace) {
		TagProcessor tp = tagFactory.getProcessor(tag, nameSpace);
		tp.setConfiguration(config);
		return tp;
	}

	/**
	 * @param stackKeeper
	 */
	public void addFirst(final StackKeeper stackKeeper) {
		this.queue.addFirst(stackKeeper);

	}

	/**
	 * Retrieves, but does not remove, the head (first element) of this list.
	 * @return
	 */
	public StackKeeper peek() {
			return this.queue.getFirst();
	}

	/**
	 * @return
	 */
	public List<Element> currentContent() {
		return ctn;
	}

	/**
	 * @return
	 */
	public boolean acceptUnknown() {
		return this.acceptUnknown;
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * @return
	 */
	public StackKeeper poll() {
		return this.queue.removeFirst();
	}

}
