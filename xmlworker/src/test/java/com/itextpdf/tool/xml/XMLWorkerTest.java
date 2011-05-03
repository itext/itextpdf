/*
 * $Id: $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;

/**
 * @author Balder Van Camp
 *
 */
public class XMLWorkerTest {

    /**
     * @author Balder Van Camp
     *
     */
    private final static class ListenerImpl implements ElementHandler {
        public void addAll(final List<Element> currentContent) throws DocumentException {

        }

        public void add(final Element e) throws DocumentException {

        }
    }

    private XMLWorker worker;
    private XMLWorkerHelper workerFactory;
	private XMLWorkerConfigurationImpl config;

    @Before
    public void setup() {
    	config = new XMLWorkerConfigurationImpl();
        config.tagProcessorFactory(new TagProcessorFactory() {

            public TagProcessor getProcessor(final String tag) {
                if ("t".equals(tag)) {
                    return new TagProcessor() {

                        public List<Element> startElement(final Tag tag) {
                            return new ArrayList<Element>(0);
                        }

                        public List<Element> endElement(final Tag tag, final List<Element> currentContent) {
                            return new ArrayList<Element>(0);
                        }

                        public List<Element> content(final Tag tag, final String content) {
                            return new ArrayList<Element>(0);

                        }

                        public boolean isStackOwner() {
                            return false;
                        }

						public void setConfiguration(final XMLWorkerConfig config) {

						}
                    };

                }
                throw new NoTagProcessorException(tag);
            }

        });

    }

    /**
     * Verify exception on unknown tag
     *
     * @throws IOException
     */
    @Test(expected = NoTagProcessorException.class)
    public void unknownTagNotAllowed() throws IOException {
        worker = new XMLWorkerImpl(config);
        workerFactory = new XMLWorkerHelper();
        workerFactory.setWorker(worker);
        worker.setDocumentListener(new ListenerImpl());
        String xml = "<q>rejferofjerfejro</q>";
        workerFactory.processXML(xml.getBytes());
    }

    /**
     * Verify no exception on unknown tag
     *
     * @throws IOException
     */
    @Test
    public void unknownTagAllowed() throws IOException {
    	config.acceptUnknown(true);
        worker = new XMLWorkerImpl(config);
        workerFactory = new XMLWorkerHelper();
        workerFactory.setWorker(worker);
        worker.setDocumentListener(new ListenerImpl());
        String xml = "<q>rejferofjerfejro</q>";
        workerFactory.processXML(xml.getBytes());
    }

    /**
     * Verify know tag loaded
     *
     * @throws IOException
     */
    @Test
    public void knownTagFoundAndLoaded() throws IOException {
        worker = new XMLWorkerImpl(config);
        workerFactory = new XMLWorkerHelper();
        workerFactory.setWorker(worker);
        worker.setDocumentListener(new ListenerImpl());
        String xml = "<t>rejferofjerfejro</t>";
        workerFactory.processXML(xml.getBytes());
    }
}
