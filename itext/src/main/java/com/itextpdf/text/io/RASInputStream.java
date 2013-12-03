/*
 * $Id: RASInputStream.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * BVBA Authors: Kevin Day, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON
 * INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General License for more
 * details. You should have received a copy of the GNU Affero General License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General License.
 *
 * In accordance with Section 7(b) of the GNU Affero General License, a covered
 * work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */

package com.itextpdf.text.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that uses a RandomAccessSource as it's underlying source 
 * @since 5.3.5
 */
public class RASInputStream extends InputStream {
	/**
	 * The source
	 */
	private final RandomAccessSource source;
	/**
	 * The current position in the source
	 */
	private long position = 0;
	
	/**
	 * Creates an input stream based on the source
	 * @param source the source
	 */
	public RASInputStream(RandomAccessSource source){
		this.source = source;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		int count = source.get(position, b, off, len);
		position += count;
		return count;
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public int read() throws IOException {
		return source.get(position++);
	}

}
