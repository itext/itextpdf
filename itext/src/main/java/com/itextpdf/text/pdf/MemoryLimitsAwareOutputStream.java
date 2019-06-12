package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.MemoryLimitsAwareException;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * This class implements an output stream which can be used for memory limits aware decompression of pdf streams.
 */
class MemoryLimitsAwareOutputStream extends ByteArrayOutputStream {

    /**
     * The maximum size of array to allocate.
     * Attempts to allocate larger arrays will result in an exception.
     */
    private static final int DEFAULT_MAX_STREAM_SIZE = Integer.MAX_VALUE - 8;

    /**
     * The maximum size of array to allocate.
     * Attempts to allocate larger arrays will result in an exception.
     */
    private int maxStreamSize = DEFAULT_MAX_STREAM_SIZE;

    /**
     * Creates a new byte array output stream. The buffer capacity is
     * initially 32 bytes, though its size increases if necessary.
     */
    public MemoryLimitsAwareOutputStream() {
        super();
    }

    /**
     * Creates a new byte array output stream, with a buffer capacity of
     * the specified size, in bytes.
     *
     * @param size the initial size.
     * @throws IllegalArgumentException if size is negative.
     */
    public MemoryLimitsAwareOutputStream(int size) {
        super(size);
    }

    /**
     * Gets the maximum size which can be occupied by this output stream.
     *
     * @return the maximum size which can be occupied by this output stream.
     */
    public long getMaxStreamSize() {
        return maxStreamSize;
    }

    /**
     * Sets the maximum size which can be occupied by this output stream.
     *
     * @param maxStreamSize the maximum size which can be occupied by this output stream.
     * @return this {@link MemoryLimitsAwareOutputStream}
     */
    public MemoryLimitsAwareOutputStream setMaxStreamSize(int maxStreamSize) {
        this.maxStreamSize = maxStreamSize;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void write(byte[] b, int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }

        int minCapacity = count + len;
        if (minCapacity < 0) { // overflow
            throw new MemoryLimitsAwareException(MemoryLimitsAwareException.DuringDecompressionSingleStreamOccupiedMoreThanMaxIntegerValue);
        }
        if (minCapacity > maxStreamSize) {
            throw new MemoryLimitsAwareException(MemoryLimitsAwareException.DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed);
        }

        // calculate new capacity
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity < 0) { // overflow
            throw new MemoryLimitsAwareException(MemoryLimitsAwareException.DuringDecompressionSingleStreamOccupiedMoreThanMaxIntegerValue);
        }
        if (newCapacity - maxStreamSize > 0) {
            newCapacity = maxStreamSize;
            byte[] copy = new byte[newCapacity];
            System.arraycopy(buf, 0, copy, 0,
                    Math.min(buf.length, newCapacity));
            buf = copy;
        }
        super.write(b, off, len);
    }
}