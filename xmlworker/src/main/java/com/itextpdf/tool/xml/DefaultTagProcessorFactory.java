/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.tool.xml;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;

/**
 * A Default implementation of the TagProcessorFactory that uses a map to store the TagProcessors. Within the same
 * {@link ClassLoader}s this Processor can also load the processors when they are only stored with there fully qualified
 * class names.
 *
 * @author redlab_b
 *
 */
public class DefaultTagProcessorFactory implements TagProcessorFactory {

	protected final class FactoryObject {
		private final String className;
		private TagProcessor proc;

		/**
		 * @param className
		 *
		 */
		public FactoryObject(final String className) {
			this.className = className;
		}

		/**
		 * @param className
		 * @param processor
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
		this.map = new HashMap<String, FactoryObject>();
	}

	/**
	 * @param className
	 * @return the loaded tag processor
	 * @throws NoTagProcessorException
	 */
	protected TagProcessor load(final String className) throws NoTagProcessorException {
		try {
			Class<?> c = Class.forName(className);
			return (TagProcessor) c.newInstance();
		} catch (LinkageError e) {
			throw new NoTagProcessorException(className);
		} catch (ClassNotFoundException e) {
			throw new NoTagProcessorException(className, e);
		} catch (InstantiationException e) {
			throw new NoTagProcessorException(className, e);
		} catch (IllegalAccessException e) {
			throw new NoTagProcessorException(className, e);
		}

	}

	/**
	 * @throws NoTagProcessorException when the processor was not found for the given tag.
	 */
	public TagProcessor getProcessor(final String tag) {
		FactoryObject fo = map.get(tag);
		if (fo != null) {
			return fo.getProcessor();
		}
		throw new NoTagProcessorException(tag);
	}

	/**
	 * Add a TagProcessor.
	 *
	 * @param tag the tag the processor with the given className maps to
	 * @param className the fully qualified class name (class has to be found on classpath)
	 */
	public void addProcessor(final String tag, final String className) {
		map.put(tag, new FactoryObject(className));
	}

	/**
	 * Add a TagProcessor.
	 *
	 * @param tag the tag the processor with the given className maps to
	 * @param processor the TagProcessor
	 */
	public void addProcessor(final String tag, final TagProcessor processor) {
		map.put(tag, new FactoryObject(processor.getClass().getName(), processor));

	}

}
