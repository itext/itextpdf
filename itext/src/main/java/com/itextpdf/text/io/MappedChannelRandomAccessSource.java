/*
 * $Id: MappedChannelRandomAccessSource.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
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
import java.nio.channels.FileChannel;

/**
 * A RandomAccessSource that represents a memory mapped section of an underlying FileChannel.
 * This source can be closed and will automatically re-open as needed.
 * This class is an internal implementation detail of the {@link FileChannelRandomAccessSource} class and
 * shouldn't be used by general iText users.
 * @since 5.3.5
 */
class MappedChannelRandomAccessSource implements RandomAccessSource {
	/**
	 * The underlying channel
	 */
	private final FileChannel channel;
	/**
	 * The offset into the channel that this source maps to
	 */
	private final long offset;
	/**
	 * The number of bytes this source maps to
	 */
	private final long length;
	
	/**
	 * If the map is active, the actual map.  null other wise.
	 */
	private ByteBufferRandomAccessSource source;
	
	/**
	 * Create a new source based on the channel.  Mapping will not occur until data is actually read.
	 * @param channel the underlying channel
	 * @param offset the offset of the map
	 * @param length the length of the map
	 */
	public MappedChannelRandomAccessSource(FileChannel channel, long offset, long length) {
		if (offset < 0) 
			throw new IllegalArgumentException(offset + " is negative");
		if (length <= 0) 
			throw new IllegalArgumentException(length + " is zero or negative");
		
		this.channel = channel;
		this.offset = offset;
		this.length = length;
		this.source = null;
	}

	/**
	 * Map the region of the channel
	 * @throws IOException if there is a problem with creating the map
	 */
	void open() throws IOException {
		if (source != null)
			return;
			
		if (!channel.isOpen())
			throw new IllegalStateException("Channel is closed");

		try{
			source = new ByteBufferRandomAccessSource(channel.map(FileChannel.MapMode.READ_ONLY, offset, length));
		} catch (IOException e){
			if (exceptionIsMapFailureException(e))
				throw new MapFailedException(e);
			throw e;
		}
	}
	
    /**
     * Utility method that determines whether a given IOException is the result
     * of a failure to map a memory mapped file.  It would be better if the runtime
     * provided a special exception for this case, but it doesn't, so we have to rely
     * on parsing the exception message.
     * @param e the exception to check
     * @return true if the exception was the result of a failure to map a memory mapped file
     */
    private static boolean exceptionIsMapFailureException(IOException e){
        if (e.getMessage() != null && e.getMessage().indexOf("Map failed") >= 0)
            return true;

        return false;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int get(long position) throws IOException {
		if (source == null)
			throw new IOException("RandomAccessSource not opened");
		return source.get(position);
	}

	/**
	 * {@inheritDoc}
	 */
	public int get(long position, byte[] bytes, int off, int len) throws IOException {
		if (source == null)
			throw new IOException("RandomAccessSource not opened");
		return source.get(position, bytes, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	public long length() {
		return length;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		if (source == null)
			return;
		source.close();
		source = null;
	}

	@Override
	public String toString() {
		return getClass().getName() + " (" + offset + ", " + length + ")";
	}
}
