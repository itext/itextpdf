/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package com.lowagie.text.pdf.hyphenation;

import java.util.Vector;

/**
 * This interface is used to connect the XML pattern file parser to
 * the hyphenation tree.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public interface PatternConsumer {

    /**
     * Add a character class.
     * A character class defines characters that are considered
     * equivalent for the purpose of hyphenation (e.g. "aA"). It
     * usually means to ignore case.
     */
    public void addClass(String chargroup);

    /**
     * Add a hyphenation exception. An exception replaces the
     * result obtained by the algorithm for cases for which this
     * fails or the user wants to provide his own hyphenation.
     * A hyphenatedword is a vector of alternating String's and
     * {@link Hyphen Hyphen} instances
     */
    public void addException(String word, Vector hyphenatedword);

    /**
     * Add hyphenation patterns.
     * @param pattern
     * @param values interletter values expressed as a string of
     * digit characters.
     */
    public void addPattern(String pattern, String values);

}
