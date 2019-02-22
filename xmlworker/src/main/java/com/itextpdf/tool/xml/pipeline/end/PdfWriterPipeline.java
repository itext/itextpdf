/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.pipeline.end;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.CustomContext;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;

/**
 * This pipeline writes to a Document.
 * @author redlab_b
 *
 */
public class PdfWriterPipeline extends AbstractPipeline<MapContext> {

	private static final Logger LOG = LoggerFactory.getLogger(PdfWriterPipeline.class);
	private Document doc;
	private PdfWriter writer;

	/**
	 */
	public PdfWriterPipeline() {
		super(null);
	}

	/**
	 * @param next the next pipeline if any.
	 */
	public PdfWriterPipeline(final Pipeline<?> next) {
		super(next);
	}

	/**
	 * @param doc the document
	 * @param writer the writer
	 */
	public PdfWriterPipeline(final Document doc, final PdfWriter writer) {
		super(null);
		this.doc = doc;
		this.writer = writer;
		continiously = true;
	}

	/**
	 * The key for the {@link Document} in the {@link MapContext} used as {@link CustomContext}.
	 */
	public static final String DOCUMENT = "DOCUMENT";
	/**
	 * The key for the {@link PdfWriter} in the {@link MapContext} used as {@link CustomContext}.
	 */
	public static final String WRITER = "WRITER";
	/**
	 * The key for the a boolean in the {@link MapContext} used as {@link CustomContext}. Setting to true enables swallowing of DocumentExceptions
	 */
	public static final String CONTINUOUS = "CONTINUOUS";
	private Boolean continiously;

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#init(com.itextpdf.tool.xml.WorkerContext)
	 */
	@Override
	public Pipeline<?> init(final WorkerContext context) throws PipelineException {
		MapContext mc = new MapContext();
		continiously = Boolean.TRUE;
		mc.put(CONTINUOUS, continiously);
		if (null != doc) {
			mc.put(DOCUMENT, doc);
		}
		if (null != writer) {
			mc.put(WRITER, writer);
		}
		context.put(getContextKey(), mc);
		return super.init(context);
	}
	/**
	 * @param po
	 * @throws PipelineException
	 */
	private void write(final WorkerContext context, final ProcessObject po) throws PipelineException {
		MapContext mp = getLocalContext(context);
		if (po.containsWritable()) {
			Document doc = (Document) mp.get(DOCUMENT);
			boolean continuousWrite = (Boolean) mp.get(CONTINUOUS);
			Writable writable = null;
			while (null != (writable = po.poll())) {
				if (writable instanceof WritableElement) {
					for (Element e : ((WritableElement) writable).elements()) {
						applyNested(e, continuousWrite, doc);		
					}
				}
			}
		}
	}
	
	/**
	 * @param Element
	 * @param boolean
	 * @param Document
	 * @throws PipelineException
	 * Method for nested Div especially if you have inside a nested list (ex ol/ul)
	 */
	private void applyNested(Element e, boolean continuousWrite, Document doc) throws PipelineException{
		try {
			Class<?> noparams[] = {};
			Class<?> clazz = Class.forName(e.getClass().getName());
			try{
				Method method = clazz.getDeclaredMethod("getContent", noparams);
				Class<?> lista = method.invoke(e).getClass();
				Class<?> listzz = Class.forName(lista.getName());
				Method methodSize = listzz.getDeclaredMethod("size", noparams);
				if (method.invoke(e) != null && (Integer) methodSize.invoke(method.invoke(e)) > 0) {
					for (Element par : (Iterable<Element>) method.invoke(e)) {
						try{
							Class<?> clazzNested = Class.forName(par.getClass().getName());
							Method methodNested = clazzNested.getDeclaredMethod("getContent", noparams);
							Class<?> listaNested = methodNested.invoke(par).getClass();
							Class<?> listzzNested = Class.forName(listaNested.getName());
							Method methodSizeNested = listzzNested.getDeclaredMethod("size", noparams);
							if(methodNested.invoke(e) != null && (Integer) methodSizeNested.invoke(methodNested.invoke(par)) > 0){
								applyNested(par, continuousWrite, doc);
							}	
						} catch(NoSuchMethodException err) {
							//it has'nt method getContent, so it's not a Div
							doc.add(par);
						}
					}
				} else {
					doc.add(e);
				}
			} catch(NoSuchMethodException err) {
				//non ha metodo getContent dunque non è un DIV
				doc.add(e);
			}
			
		} catch (DocumentException e1) {
			if (!continuousWrite) {
				throw new PipelineException(e1);
			} else {
				LOG.error(LocaleMessages.getInstance().getMessage(LocaleMessages.ELEMENT_NOT_ADDED_EXC),
						e1);
			}
		} catch (ClassNotFoundException e1) {
			LOG.error(e1.toString());
		} catch (IllegalAccessException e1) {
			LOG.error(e1.toString());
		} catch (SecurityException e1) {
			LOG.error(e1.toString());
		} catch (IllegalArgumentException e1) {
			LOG.error(e1.toString());
		} catch (InvocationTargetException e1) {
			LOG.error(e1.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> open(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		write(context, po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#content(com.itextpdf.tool
	 * .xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> content(final WorkerContext context, final Tag currentTag, final String text, final ProcessObject po) throws PipelineException {
		write(context, po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> close(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		write(context ,po);
		return getNext();
	}

	/**
	 * The document to write to.
	 * @param document the Document
	 */
	public void setDocument(final Document document) {
		this.doc = document;
	}

	/**
	 * The writer used to write to the document.
	 * @param writer the writer.
	 */
	public void setWriter(final PdfWriter writer) {
		this.writer = writer;
	}
}
