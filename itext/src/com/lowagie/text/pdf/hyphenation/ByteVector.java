/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package com.lowagie.text.pdf.hyphenation;

import java.io.Serializable;

/**
 * This class implements a simple byte vector with access to the
 * underlying array.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public class ByteVector implements Serializable {

    /**
     * Capacity increment size
     */
    private static final int DEFAULT_BLOCK_SIZE = 2048;
    private int BLOCK_SIZE;

    /**
     * The encapsulated array
     */
    private byte[] array;

    /**
     * Points to next free item
     */
    private int n;

    public ByteVector() {
        this(DEFAULT_BLOCK_SIZE);
    }

    public ByteVector(int capacity) {
        if (capacity > 0)
            BLOCK_SIZE = capacity;
        else
            BLOCK_SIZE = DEFAULT_BLOCK_SIZE;
        array = new byte[BLOCK_SIZE];
        n = 0;
    }

    public ByteVector(byte[] a) {
        BLOCK_SIZE = DEFAULT_BLOCK_SIZE;
        array = a;
        n = 0;
    }

    public ByteVector(byte[] a, int capacity) {
        if (capacity > 0)
            BLOCK_SIZE = capacity;
        else
            BLOCK_SIZE = DEFAULT_BLOCK_SIZE;
        array = a;
        n = 0;
    }

    public byte[] getArray() {
        return array;
    }

    /**
     * return number of items in array
     */
    public int length() {
        return n;
    }

    /**
     * returns current capacity of array
     */
    public int capacity() {
        return array.length;
    }

    public void put(int index, byte val) {
        array[index] = val;
    }

    public byte get(int index) {
        return array[index];
    }

    /**
     * This is to implement memory allocation in the array. Like malloc().
     */
    public int alloc(int size) {
        int index = n;
        int len = array.length;
        if (n + size >= len) {
            byte[] aux = new byte[len + BLOCK_SIZE];
            System.arraycopy(array, 0, aux, 0, len);
            array = aux;
        }
        n += size;
        return index;
    }

    public void trimToSize() {
        if (n < array.length) {
            byte[] aux = new byte[n];
            System.arraycopy(array, 0, aux, 0, n);
            array = aux;
        }
    }

}
