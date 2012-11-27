/*
 * $Id: FileChannelRandomAccessSource.java 5551 2012-11-21 18:47:14Z trumpetinc $
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
import java.nio.channels.FileChannel;

/**
 * A RandomAccessSource that is based on an underlying {@link FileChannel}.  The channel will be mapped into memory for efficient reads.
 * As an implementation detail, we use {@link GroupedRandomAccessSource} functionality, but override to make determination of the underlying
 * mapped page more efficient - and to close each page as another is opened
 * @since 5.3.5
 */
class FileChannelRandomAccessSource extends GroupedRandomAccessSource implements RandomAccessSource {
    private static final int DEFAULT_BUFSIZE = 1 << 28; 

    /**
     * The size of the buffers to use when mapping files into memory.  This must be greater than 0 and less than {@link Integer#MAX_VALUE}
     */
    private final int bufferSize;
    
    /**
     * The channel this source is based on
     */
    private final FileChannel channel;

    /**
     * Constructs a new {@link FileChannelRandomAccessSource} based on the specified FileChannel, with a default buffer size
     * @param channel the channel to use as the backing store
     * @throws IOException if the channel cannot be opened or mapped
     */
    public FileChannelRandomAccessSource(FileChannel channel) throws IOException {
		this(channel, DEFAULT_BUFSIZE);
	}
    
    /**
     * Constructs a new {@link FileChannelRandomAccessSource} based on the specified FileChannel, with a specific buffer size
     * @param channel the channel to use as the backing store
     * @param bufferSize the size of the buffers to use
     * @throws IOException if the channel cannot be opened or mapped
     */
	public FileChannelRandomAccessSource(final FileChannel channel, final int bufferSize) throws IOException {
        super(buildSources(channel, bufferSize));
		this.channel = channel;
        this.bufferSize = bufferSize;
	}

	/**
	 * Constructs a set of {@link MappedChannelRandomAccessSource}s for each page (of size bufferSize) of the underlying channel
	 * @param channel the underlying channel
	 * @param bufferSize the size of each page (the last page may be shorter)
	 * @return a list of sources that represent the pages of the channel
	 * @throws IOException if IO fails for any reason
	 */
	private static RandomAccessSource[] buildSources(final FileChannel channel, final int bufferSize) throws IOException{
		long size = channel.size();
		int bufferCount = (int)(size/bufferSize) + (size % bufferSize == 0 ? 0 : 1);

		MappedChannelRandomAccessSource[] sources = new MappedChannelRandomAccessSource[bufferCount];
        for (int i = 0; i < bufferCount; i++){
        	long pageOffset = (long)i*bufferSize;
        	long pageLength = Math.min(size - pageOffset, bufferSize);
        	sources[i] = new MappedChannelRandomAccessSource(channel, pageOffset, pageLength);
        }
        return sources;
		
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	protected int getStartingSourceIndex(long offset) {
		return (int) (offset / bufferSize);
	}

	@Override
	/**
	 * {@inheritDoc}
	 * For now, close the source that is no longer being used.  In the future, we may implement an MRU that allows multiple pages to be opened at a time
	 */
	protected void sourceReleased(RandomAccessSource source) throws IOException {
		source.close();
	}
	
	@Override
    /**
     * {@inheritDoc}
     * Cleans the mapped bytebuffers and closes the channel
     */
    public void close() throws IOException {
    	super.close();
        channel.close();
    }

}
