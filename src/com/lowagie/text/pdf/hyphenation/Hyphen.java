/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For license details please refer to http://xml.apache.org/fop
 */

package com.lowagie.text.pdf.hyphenation;

import java.io.Serializable;

/**
 * This class represents a hyphen. A 'full' hyphen is made of 3 parts:
 * the pre-break text, post-break text and no-break. If no line-break
 * is generated at this position, the no-break text is used, otherwise,
 * pre-break and post-break are used. Typically, pre-break is equal to
 * the hyphen character and the others are empty. However, this general
 * scheme allows support for cases in some languages where words change
 * spelling if they're split across lines, like german's 'backen' which
 * hyphenates 'bak-ken'. BTW, this comes from TeX.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */

public class Hyphen implements Serializable {
    public String preBreak;
    public String noBreak;
    public String postBreak;

    Hyphen(String pre, String no, String post) {
        preBreak = pre;
        noBreak = no;
        postBreak = post;
    }

    Hyphen(String pre) {
        preBreak = pre;
        noBreak = null;
        postBreak = null;
    }

    public String toString() {
        if (noBreak == null && postBreak == null && preBreak != null
                && preBreak.equals("-"))
            return "-";
        StringBuffer res = new StringBuffer("{");
        res.append(preBreak);
        res.append("}{");
        res.append(postBreak);
        res.append("}{");
        res.append(noBreak);
        res.append('}');
        return res.toString();
    }

}
