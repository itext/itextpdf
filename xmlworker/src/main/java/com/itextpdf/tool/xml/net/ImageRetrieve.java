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
package com.itextpdf.tool.xml.net;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.tool.xml.net.exc.NoImageException;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;

/**
 * @author redlab_b
 *
 */
public class ImageRetrieve {
	private final ImageProvider provider;
	/**
	 * @param imageProvider the provider to use.
	 *
	 */
	public ImageRetrieve(final ImageProvider imageProvider) {
		this.provider = imageProvider;
	}
	/**
	 *
	 */
	public ImageRetrieve() {
		this.provider = null;
	}
	/**
	 * @param src an URI that can be used to retrieve an image
	 * @return an iText Image object
	 * @throws NoImageException if there is no image
	 * @throws IOException if an IOException occurred
	 */
	public com.itextpdf.text.Image retrieveImage(final String src) throws NoImageException, IOException {
		com.itextpdf.text.Image img = null;
		if (null != provider) {
			img = provider.retrieve(src);
		}

		if (null == img) {
			String path = null;
			if (src.startsWith("http")) {
				// full url available
				path = src;
			} else if (null != provider){
				String root = this.provider.getImageRootPath();
				if (null != root) {
					if (root.endsWith("/") && src.startsWith("/")) {
						root = root.substring(0, root.length() - 1);
					}
					path = root + src;
				}
			} else {
				path = src;
			}
			if (null != path) {
				try {
					if (path.startsWith("http")) {
						img = com.itextpdf.text.Image.getInstance(path);
					} else {
						img = com.itextpdf.text.Image.getInstance(new File(path).toURI().toURL());
					}
					if (null != provider && null != img) {
						provider.store( src, img);
					}
				} catch (BadElementException e) {
					throw new NoImageException(src, e);
				} catch (MalformedURLException e) {
					throw new NoImageException(src, e);
				}
			} else {
				throw new NoImageException(src);
			}
		}
		return img;
	}
}
