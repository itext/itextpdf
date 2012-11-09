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
