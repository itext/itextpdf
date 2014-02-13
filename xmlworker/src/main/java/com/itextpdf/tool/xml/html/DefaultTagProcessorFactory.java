/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.tool.xml.html;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;

/**
 * A Default implementation of the TagProcessorFactory that uses a map to store
 * the TagProcessors. Within the same {@link ClassLoader}s this Processor can
 * also load the processors when they are only stored with there fully qualified
 * class names.<br />
 * <strong>Note:</strong> this implementation does not use namespaces (yet)!
 *
 *
 * @author redlab_b
 *
 */
public class DefaultTagProcessorFactory implements TagProcessorFactory {

	/**
	 * Internal Object to keep TagProcessors.
	 * @author redlab_b
	 *
	 */
	protected final class FactoryObject {
		private final String className;
		private TagProcessor proc;

		/**
		 * @param className the fully qualified class name
		 *
		 */
		public FactoryObject(final String className) {
			this.className = className;
		}

		/**
		 * @param className the fully qualified class name
		 * @param processor the processor object
		 */
		public FactoryObject(final String className, final TagProcessor processor) {
			this(className);
			this.proc = processor;

		}

		/**
		 *
		 * @return the className
		 */
		public String getClassName() {
			return this.className;
		}

		/**
		 * @return return the processor
		 */
		public TagProcessor getProcessor() {
			if (null == this.proc) {
				this.proc = load(this.className);
			}
			return proc;
		}

	}

	private final Map<String, FactoryObject> map;

	/**
	 *
	 */
	public DefaultTagProcessorFactory() {
		this.map = new ConcurrentHashMap<String, FactoryObject>();
	}

	/**
	 * Tries to load given processor with Class.forName
	 * @param className fully qualified className
	 * @return the loaded tag processor
	 * @throws NoTagProcessorException if TagProcessor could not be loaded.
	 */
	protected TagProcessor load(final String className) throws NoTagProcessorException {
		try {
			Class<?> c = Class.forName(className);
			return (TagProcessor) c.newInstance();
		} catch (LinkageError e) {
			throw new NoTagProcessorException(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className));
		} catch (ClassNotFoundException e) {
			throw new NoTagProcessorException(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className), e);
		} catch (InstantiationException e) {
			throw new NoTagProcessorException(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className), e);
		} catch (IllegalAccessException e) {
			throw new NoTagProcessorException(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className), e);
		}

	}

	/**
	 * Tries to load given processor with Class.forName
	 * 
	 * @param className fully qualified className
	 * @param loader the classloader to use
	 * @return the loaded tag processor
	 * @throws NoTagProcessorException if TagProcessor could not be loaded.
	 */
	protected TagProcessor load(final String className, final ClassLoader loader) throws NoTagProcessorException {
		try {
			Class<?> c = loader.loadClass(className);
			return (TagProcessor) c.newInstance();
		} catch (LinkageError e) {
			throw new NoTagProcessorException(String.format(
					LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className));
		} catch (ClassNotFoundException e) {
			throw new NoTagProcessorException(String.format(
					LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className), e);
		} catch (InstantiationException e) {
			throw new NoTagProcessorException(String.format(
					LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className), e);
		} catch (IllegalAccessException e) {
			throw new NoTagProcessorException(String.format(
					LocaleMessages.getInstance().getMessage(LocaleMessages.NO_TAGPROCESSOR), className), e);
		}

	}

	/**
	 * @throws NoTagProcessorException when the processor was not found for the given tag.
	 */
	public TagProcessor getProcessor(final String tag, final String nameSpace) {
		FactoryObject fo = map.get(tag);
		if (fo != null) {
			return fo.getProcessor();
		}
		throw new NoTagProcessorException(tag);
	}

	/**
	 * Add an unloaded TagProcessor.
	 *
	 * @param tag the tag the processor with the given className maps to
	 * @param className the fully qualified class name (class has to be found on classpath, will be loaded with Class.forName()
	 */
	public void addProcessor(final String tag, final String className) {
		map.put(tag, new FactoryObject(className));
	}

	/**
	 * Add a loaded TagProcessor.
	 *
	 * @param tag the tag the processor with the given className maps to
	 * @param processor the TagProcessor
	 */
	public void addProcessor(final String tag, final TagProcessor processor) {
		map.put(tag, new FactoryObject(processor.getClass().getName(), processor));

	}
	/**
	 *
	 */
	public void addProcessor( final TagProcessor processor, final String ... tags) {
		for (String tag : tags) {
			addProcessor(tag, processor);
		}
	}
	/**
	 * Add one tag processor that handles multiple tags.
	 * @param className the fully qualified class name (class has to be found on classpath)
	 * @param tags list of tags this processor maps to.
	 */
	public void addProcessor( final String className, final String ... tags) {
		for (String tag : tags) {
			addProcessor(tag, className);
		}
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.html.TagProcessorFactory#removeProcessor(java.lang.String)
	 */
	public void removeProcessor(final String tag) {
		map.remove(tag);
	}

}
