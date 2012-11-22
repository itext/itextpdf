/*
 * $Id: OffsetRandomAccessSource.java 5550 2012-11-21 13:26:06Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT
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

/**
 * A RandomAccessSource that is wraps another RandomAccessSouce but offsets the start by a specific amount.  This is useful
 * when a file might have junk bytes at the beginning (such as a properly constructed PDF that happens to have some number of bytes
 * pre-pended).  This has the effect of reducing the length of the source by the offset amount, and re-indexing all absolute
 * get methods by the offset amount.
 * @since 5.3.5
 */
public class OffsetRandomAccessSource implements RandomAccessSource {
	/**
	 * The source
	 */
	private final RandomAccessSource source;
	
	/**
	 * The amount to offset the source by
	 */
	private final long offset;
	
	/**
	 * Constructs a new OffsetRandomAccessSource
	 * @param source the source
	 * @param offset the amount of the offset to use
	 */
	public OffsetRandomAccessSource(RandomAccessSource source, long offset) {
		this.source = source;
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 * Note that the position will be adjusted to read from the corrected location in the underlying source
	 */
	public int get(long position) throws IOException {
		return source.get(offset + position);
	}

	/**
	 * {@inheritDoc}
	 * Note that the position will be adjusted to read from the corrected location in the underlying source
	 */
	public int get(long position, byte[] bytes, int off, int len) throws IOException {
		return source.get(offset + position, bytes, off, len);
	}

	/**
	 * {@inheritDoc}
	 * Note that the length will be adjusted to read from the corrected location in the underlying source
	 */
	public long length() {
		return source.length() - offset;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		source.close();
	}

}
