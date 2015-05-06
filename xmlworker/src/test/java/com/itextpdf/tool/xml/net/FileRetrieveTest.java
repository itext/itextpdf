/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
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
package com.itextpdf.tool.xml.net;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * @author redlab_b
 *
 */
public class FileRetrieveTest {
	static {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
	}
	private static final Logger LOG = LoggerFactory.getLogger(FileRetrieveTest.class);
    private FileRetrieveImpl retriever;
	private File expected;
	private File actual;

    @Before
    public void setup() {
        retriever = new FileRetrieveImpl();
        actual = new File("./src/test/resources/css/actual.css");
        expected = new File("./src/test/resources/css/test.css");
    }

    @After
    public void tearDown() {
		FileUtils.deleteQuietly(actual);
    }

    @Test
	public void retrieveURL() throws IOException {
		boolean execute = true;
    	try {
    		URL url = new URL("http://itextsupport.com/files/testresources/css/test.css");
			url.openConnection().connect();
		} catch (SocketTimeoutException e) {
			LOG.info("Skipping retrieve from URL test as we cannot open the url itself. Maybe no internet connection. Marking test as Success");
			execute = false;
		} catch (IOException e) {
			LOG.info("Skipping retrieve from URL test as we cannot open the url itself. Maybe no internet connection. Marking test as Success");
			execute = false;
		}
		if (execute) {
    		final FileOutputStream out = new FileOutputStream(actual);
    		retriever.processFromHref("http://itextsupport.com/files/testresources/css/test.css", new ReadingProcessor() {

    			public void process(final int inbit) {
    				try {
						out.write((char)inbit);
					} catch (IOException e) {
						throw new RuntimeWorkerException(e);
					}
    			}
    		});
    		out.close();
		}
    }

    @Test
    public void retrieveStreamFromFile() throws MalformedURLException, IOException {
    	final FileOutputStream out = new FileOutputStream(actual);
        InputStream css = FileRetrieveTest.class.getResourceAsStream("/css/test.css");
		retriever.processFromStream(css,
                new ReadingProcessor() {

                    public void process(final int inbit) {
                        try {
							out.write((char)inbit);
						} catch (IOException e) {
							throw new RuntimeWorkerException(e);
						}
                    }
                });
		css.close();
		out.close();
		Assert.assertTrue(FileUtils.contentEquals(expected, actual));
    }
    @Test
    public void retrieveFile() throws MalformedURLException, IOException {
    	final FileOutputStream out = new FileOutputStream(actual);
    	retriever.addRootDir(new File("./src/test/resources"));
    	retriever.processFromHref("/css/test.css",
    			new ReadingProcessor() {

    		public void process(final int inbit) {
    			try {
					out.write((char)inbit);
				} catch (IOException e) {
					throw new RuntimeWorkerException(e);
				}
    		}
    	});
    	out.close();
    	Assert.assertTrue(FileUtils.contentEquals(expected, actual));
    }
}
