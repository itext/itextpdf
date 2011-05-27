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
package com.itextpdf.tool.xml.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;

/**
 * @author redlab_b
 *
 */
public class FileRetrieveImpl implements FileRetrieve {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileRetrieveImpl.class);
	private final List<File> rootdirs;
	private final List<String> urls;

	/**
	 *
	 */
	public FileRetrieveImpl() {
		rootdirs = new CopyOnWriteArrayList<File>();
		urls = new CopyOnWriteArrayList<String>();
	}

	/**
	 * @param strings
	 */
	public FileRetrieveImpl(final String[] strings) {
		this();
		for (String s : strings) {
			if (s.startsWith("http") || s.startsWith("https")) {
				urls.add(s);
			} else {
				File f = new File(s);
				if (f.isDirectory()) {
					rootdirs.add(f);
				}
			}
		}
	}

	/**
	 * ProcessFromHref first tries to create an {@link URL} from the given href,
	 * if that throws a {@link MalformedURLException}, it will prepend the given
	 * root urls to href until a valid URL is found. If there by then there is
	 * no valid url found, ths method will see if the given href is a valid file
	 * and can read it. If it's not a valid file or a file that can't be read,
	 * the given rootdirs will be set as rootpath with the given href as
	 * filepath untill a valid file has been found.
	 */
	public void processFromHref(final String href, final ReadingProcessor processor) throws IOException {
		if (LOGGER.isLogging(Level.DEBUG)) {
			LOGGER.debug(String.format(LocaleMessages.getInstance().getMessage("retrieve.file.from"), href));
		}
		URL url = null;
		File f = null;
		boolean isfile = false;
		try {
			url = new URL(href);
		} catch (MalformedURLException e) {
			try {
				url = detectWithRootUrls(href);
			} catch (MalformedURLException e1) {
				// its probably a file, try to detect it.
				f = new File(href);
				isfile = true;
				if (!(f.isFile() && f.canRead())) {
					isfile = false;
					for (File root : rootdirs) {
						f = new File(root, href);
						if (f.isFile() && f.canRead()) {
							isfile = true;
							break;
						}

					}
				}
			}
		}
		InputStream in = null;
		if (null != url) {
			in = url.openStream();
		} else if (isfile) {
			in = new FileInputStream(f);
		}
		read(processor, in);
	}

	/**
	 * @param href
	 * @throws MalformedURLException if no valid url could be found.
	 */
	private URL detectWithRootUrls(final String href) throws MalformedURLException {
		for (String root : urls) {
    		try {
				return new URL(root + href);
			} catch (MalformedURLException e) {
			}
    	}
		throw new MalformedURLException();
	}

    /* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.net.FileRetrieve#processFromStream(java.io.InputStream, com.itextpdf.tool.xml.net.ReadingProcessor)
	 */
    public void processFromStream(final InputStream in, final ReadingProcessor processor) throws IOException {
        read(processor, in);
    }

    /**
     * @param processor
     * @param in
     * @throws IOException
     */
    private void read(final ReadingProcessor processor, final InputStream in) throws IOException {
        try {
            int inbit = -1;
            while ((inbit = in.read()) != -1) {
                processor.process(inbit);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
            	throw new RuntimeWorkerException(e);
            }
        }
    }

	/**
	 * Add a root directory.
	 * @param dir the root directory
	 */
	public void addRootDir(final File dir) {
		rootdirs.add(dir);
	}

	/**
	 * Add a rooturl
	 * @param url the url
	 */
	public void addURL(final String url) {
		urls.add(url);
	}

}
