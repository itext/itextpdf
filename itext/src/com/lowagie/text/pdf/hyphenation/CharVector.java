/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package com.lowagie.text.pdf.hyphenation;

import java.io.Serializable;

/**
 * This class implements a simple char vector with access to the
 * underlying array.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public class CharVector implements Cloneable, Serializable {

    /**
     * Capacity increment size
     */
    private static final int DEFAULT_BLOCK_SIZE = 2048;
    private int BLOCK_SIZE;

    /**
     * The encapsulated array
     */
    private char[] array;

    /**
     * Points to next free item
     */
    private int n;

    public CharVector() {
        this(DEFAULT_BLOCK_SIZE);
    }

    public CharVector(int capacity) {
        if (capacity > 0)
            BLOCK_SIZE = capacity;
        else
            BLOCK_SIZE = DEFAULT_BLOCK_SIZE;
        array = new char[BLOCK_SIZE];
        n = 0;
    }

    public CharVector(char[] a) {
        BLOCK_SIZE = DEFAULT_BLOCK_SIZE;
        array = a;
        n = a.length;
    }

    public CharVector(char[] a, int capacity) {
        if (capacity > 0)
            BLOCK_SIZE = capacity;
        else
            BLOCK_SIZE = DEFAULT_BLOCK_SIZE;
        array = a;
        n = a.length;
    }

    /**
     * Reset Vector but don't resize or clear elements
     */
    public void clear() {
        n = 0;
    }

    public Object clone() {
        CharVector cv = new CharVector((char[])array.clone(), BLOCK_SIZE);
        cv.n = this.n;
        return cv;
    }

    public char[] getArray() {
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

    public void put(int index, char val) {
        array[index] = val;
    }

    public char get(int index) {
        return array[index];
    }

    public int alloc(int size) {
        int index = n;
        int len = array.length;
        if (n + size >= len) {
            char[] aux = new char[len + BLOCK_SIZE];
            System.arraycopy(array, 0, aux, 0, len);
            array = aux;
        }
        n += size;
        return index;
    }

    public void trimToSize() {
        if (n < array.length) {
            char[] aux = new char[n];
            System.arraycopy(array, 0, aux, 0, n);
            array = aux;
        }
    }

}
