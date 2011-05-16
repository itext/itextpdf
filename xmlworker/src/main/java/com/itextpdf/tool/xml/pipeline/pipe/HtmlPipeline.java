/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.StackKeeper;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.TagProcessor;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.CustomContext;
import com.itextpdf.tool.xml.pipeline.NoCustomContextException;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.PipelineException;
import com.itextpdf.tool.xml.pipeline.ProcessObject;
import com.itextpdf.tool.xml.pipeline.WritableElement;

/**
 * @author Balder Van Camp
 *
 */
public class HtmlPipeline extends AbstractPipeline {

	private final XMLWorkerConfig config;

	/**
	 * @param next
	 */
	public HtmlPipeline(final XMLWorkerConfig config, final Pipeline next) {
		super(next);
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getMyContext();
		try {
			TagProcessor tp = hcc.resolveProcessor(t.getTag(), t.getNameSpace());
			if (tp.isStackOwner()) {
				hcc.addFirst(new StackKeeper(t));
			}
			List<Element> content = tp.startElement(t);
			if (content.size() > 0) {
				if (tp.isStackOwner()) {
					StackKeeper peek = hcc.peek();
					for (Element elem : content) {
						peek.add(elem);
					}
				} else {
					po.add(new WritableElement(hcc.currentContent()));
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw new PipelineException(e);
			}
		}
		return getNext();
	}

	/**
	 * @return
	 */
	private HtmlPipelineContext getMyContext() {
		CustomContext cc = getContext().get(HtmlPipeline.class);
		HtmlPipelineContext hcc = (HtmlPipelineContext) cc;
		return hcc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#content(com.itextpdf.tool
	 * .xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline content(final Tag t, final String content, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getMyContext();
		TagProcessor tp = hcc.resolveProcessor(t.getTag(), t.getNameSpace());
		List<Element> elems = tp.content(t, content);
		if (hcc.isEmpty() && elems.size() > 0) {
			po.add(new WritableElement(elems));
		} else if (elems.size() > 0){
			StackKeeper peek = hcc.peek();
			for (Element e : elems) {
				peek.add(e);
			}
		}
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getMyContext();
		TagProcessor tp;
		try {
			tp = hcc.resolveProcessor(t.getTag(), t.getNameSpace());
			if (hcc.isEmpty()) {
				List<Element> elems = tp.endElement(t, hcc.currentContent());
				if (elems.size() > 0) {
					for (Element e : elems) {
						hcc.currentContent().add(e);
					}
				}
				po.add(new WritableElement(hcc.currentContent()));
				hcc.currentContent().clear();
			} else if (tp.isStackOwner()) {
				// remove the element from the StackKeeper Queue if end tag is
				// found
				List<Element> elements = hcc.poll().getElements();
				List<Element> elems = tp.endElement(t, elements);
				if (hcc.isEmpty() && elems.size() > 0) {
					po.add(new WritableElement(elems));
				} else if (elems.size() > 0) {
					StackKeeper peek = hcc.peek();
					for (Element elem : elems) {
						peek.add(elem);
					}
				}
				hcc.currentContent().clear();
			} else {
				List<Element> elems = tp.endElement(t, hcc.currentContent());
				if (elems.size() > 0) {
					StackKeeper peek = hcc.peek();
					for (Element elem : elems) {
						peek.add(elem);
					}
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		} finally {
		}
		return getNext();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getNewCustomContext()
	 */
	public CustomContext getCustomContext() throws NoCustomContextException {
		return new HtmlPipelineContext(config);
	}

}
