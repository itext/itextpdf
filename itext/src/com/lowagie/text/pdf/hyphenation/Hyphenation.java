/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For license details please refer to http://xml.apache.org/fop
 */

package com.lowagie.text.pdf.hyphenation;

/**
 * This class represents a hyphenated word.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public class Hyphenation {
    int[] hyphenPoints;
    String word;

    /**
     * number of hyphenation points in word
     */
    int len;

    /**
     * rawWord as made of alternating strings and {@link Hyphen Hyphen}
     * instances
     */
    Hyphenation(String word, int[] points) {
        this.word = word;
        hyphenPoints = points;
        len = points.length;
    }

    /**
     * @return the number of hyphenation points in the word
     */
    public int length() {
        return len;
    }

    /**
     * @return the pre-break text, not including the hyphen character
     */
    public String getPreHyphenText(int index) {
        return word.substring(0, hyphenPoints[index]);
    }

    /**
     * @return the post-break text
     */
    public String getPostHyphenText(int index) {
        return word.substring(hyphenPoints[index]);
    }

    /**
     * @return the hyphenation points
     */
    public int[] getHyphenationPoints() {
        return hyphenPoints;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        int start = 0;
        for (int i = 0; i < len; i++) {
            str.append(word.substring(start, hyphenPoints[i]) + "-");
            start = hyphenPoints[i];
        }
        str.append(word.substring(start));
        return str.toString();
    }

}
