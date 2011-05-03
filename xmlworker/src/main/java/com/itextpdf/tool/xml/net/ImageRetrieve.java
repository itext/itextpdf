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
package com.itextpdf.tool.xml.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.itextpdf.text.BadElementException;
import com.itextpdf.tool.xml.Provider;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;

/**
 * @author Balder Van Camp
 *
 */
public class ImageRetrieve {

	private final Provider provider;
	/**
	 *
	 */
	public ImageRetrieve(final Provider provider) {
		this.provider = provider;
	}
	/**
	 * @param src
	 * @return
	 */
	public com.itextpdf.text.Image retrieveImage(final String src) {
		com.itextpdf.text.Image img;
		img = provider.retrieve(src);

		if (null == img) {
			String path = null;
			if (src.startsWith("http")) {
				// full url available
				path = src;
			} else {
				String root = this.provider.get(Provider.GLOBAL_IMAGE_ROOT);
				if (null != root) {
					if (root.endsWith("/") && src.startsWith("/")) {
						root = root.substring(0, root.length() - 1);
					}
					path = root + src;
				}
			}
			if (null != path) {
				try {
					if (path.startsWith("http")) {
						img = com.itextpdf.text.Image.getInstance(path);
					} else {
						img = com.itextpdf.text.Image.getInstance(new URL("file:///" + path));

					}
					if (null != img) {
						provider.store( src, img);
					}
					// TODO configurable option if errors should be swallowed ?
				} catch (BadElementException e) {
					throw new RuntimeWorkerException(e);
				} catch (MalformedURLException e) {
					throw new RuntimeWorkerException(e);
				} catch (IOException e) {
//					throw new RuntimeWorkerException(e);
				}
			}
		}
		return img;
	}
}
