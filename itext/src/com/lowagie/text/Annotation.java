/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * An <CODE>Annotation</CODE> is a little note that can be added to a page
 * on a document.
 *
 * @see		Element
 * @see		Anchor
 */

public class Annotation implements Element, MarkupAttributes {
    
    // membervariables
    
/** This is a possible annotation type. */
    public static final int TEXT = 0;    
/** This is a possible annotation type. */
    public static final int URL_NET = 1;    
/** This is a possible annotation type. */
    public static final int URL_AS_STRING = 2;    
/** This is a possible annotation type. */
    public static final int FILE_DEST = 3;    
/** This is a possible annotation type. */
    public static final int FILE_PAGE = 4;    
/** This is a possible annotation type. */
    public static final int NAMED_DEST = 5;    
/** This is a possible annotation type. */
    public static final int LAUNCH = 6;
    
/** This is a possible attribute. */
    public static String TITLE = "title";
/** This is a possible attribute. */
    public static String CONTENT = "content";
/** This is a possible attribute. */
    public static String URL = "url";
/** This is a possible attribute. */
    public static String FILE = "file";
/** This is a possible attribute. */
    public static String DESTINATION = "destination";
/** This is a possible attribute. */
    public static String PAGE = "page";
/** This is a possible attribute. */
    public static String NAMED = "named";
/** This is a possible attribute. */
    public static String APPLICATION = "application";
/** This is a possible attribute. */
    public static String PARAMETERS = "parameters";
/** This is a possible attribute. */
    public static String OPERATION = "operation";
/** This is a possible attribute. */
    public static String DEFAULTDIR = "defaultdir";
/** This is a possible attribute. */
    public static String LLX = "llx";
/** This is a possible attribute. */
    public static String LLY = "lly";
/** This is a possible attribute. */
    public static String urX = "urx";
/** This is a possible attribute. */
    public static String URY = "ury";
    
/** This is the type of annotation. */
    protected int annotationtype;
    
/** This is the title of the <CODE>Annotation</CODE>. */
    protected HashMap attributes = new HashMap();

/** Contains extra markupAttributes */
    protected Properties markupAttributes;
    
/** This is the lower left x-value */
    protected float llx = Float.MIN_VALUE;
/** This is the lower left y-value */
    protected float lly = Float.MIN_VALUE;
/** This is the upper right x-value */
    protected float urx = Float.MIN_VALUE;
/** This is the upper right y-value */
    protected float ury = Float.MIN_VALUE;
    
    // constructors
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 */
    
    private Annotation(float llx, float lly, float urx, float ury) {
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;
    }
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 *
 * @param	title	the title of the annotation
 * @param	text	the content of the annotation
 */
    
    public Annotation(String title, String text) {
        annotationtype = TEXT;
        attributes.put(TITLE, title);
        attributes.put(CONTENT, text);
    }
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 *
 * @param	title	the title of the annotation
 * @param	text	the content of the annotation
 * @param       llx     the lower left x-value
 * @param       lly     the lower left y-value
 * @param       urx     the upper right x-value
 * @param       ury     the upper right y-value
 */
    
    public Annotation(String title, String text, float llx, float lly, float urx, float ury) {
        this(llx, lly, urx, ury);
        annotationtype = TEXT;
        attributes.put(TITLE, title);
        attributes.put(CONTENT, text);
    }
    
/**
 * Constructs an <CODE>Annotation</CODE>.
 */
    
    public Annotation(float llx, float lly, float urx, float ury, URL url) {
        this(llx, lly, urx, ury);
        annotationtype = URL_NET;
        attributes.put(URL, url);
    }
    
/**
 * Constructs an <CODE>Annotation</CODE>.
 */
    
    public Annotation(float llx, float lly, float urx, float ury, String url) {
        this(llx, lly, urx, ury);
        annotationtype = URL_AS_STRING;
        attributes.put(FILE, url);
    }
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 */
    
    public Annotation(float llx, float lly, float urx, float ury, String file, String dest) {
        this(llx, lly, urx, ury);
        annotationtype = FILE_DEST;
        attributes.put(FILE, file);
        attributes.put(DESTINATION, dest);
    }
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 */
    
    public Annotation(float llx, float lly, float urx, float ury, String file, int page) {
        this(llx, lly, urx, ury);
        annotationtype = FILE_PAGE;
        attributes.put(FILE, file);
        attributes.put(PAGE, new Integer(page));
    }
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 */
    
    public Annotation(float llx, float lly, float urx, float ury, int named) {
        this(llx, lly, urx, ury);
        annotationtype = NAMED_DEST;
        attributes.put(NAMED, new Integer(named));
    }
    
/**
 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
 */
    
    public Annotation(float llx, float lly, float urx, float ury, String application, String parameters, String operation, String defaultdir) {
        this(llx, lly, urx, ury);
        annotationtype = LAUNCH;
        attributes.put(APPLICATION, application);
        attributes.put(PARAMETERS, parameters);
        attributes.put(OPERATION, operation);
        attributes.put(DEFAULTDIR, defaultdir);
    }
    
/**
 * Returns an <CODE>Annotation</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 */
    
    public Annotation(Properties attrs) {
        String title = (String)attrs.remove(ElementTags.TITLE);
        String text = (String)attrs.remove(ElementTags.CONTENT);
        if (title == null) {
            title = "";
        }
        if (text == null) {
            text = "";
        }
        attributes.put(TITLE, title);
        attributes.put(CONTENT, text);
    }
    
    // implementation of the Element-methods
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public final int type() {
        return Element.ANNOTATION;
    }
    
    // methods
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param	listener 	an <CODE>ElementListener</CODE>
 * @return	<CODE>true</CODE> if the element was processed successfully
 */
    
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
    // methods to retrieve information
    
/**
 * Returns the lower left x-value.
 *
 * @return	a value
 */
    
    public final float llx() {
        return llx;
    }
    
/**
 * Returns the lower left y-value.
 *
 * @return	a value
 */
    
    public final float lly() {
        return lly;
    }
    
/**
 * Returns the uppper right x-value.
 *
 * @return	a value
 */
    
    public final float urx() {
        return urx;
    }
    
/**
 * Returns the uppper right y-value.
 *
 * @return	a value
 */
    
    public final float ury() {
        return ury;
    }
    
/**
 * Returns the lower left x-value.
 *
 * @param       the default value
 * @return	a value
 */
    
    public final float llx(float def) {
        if (llx == Float.MIN_VALUE) return def;
        return llx;
    }
    
/**
 * Returns the lower left y-value.
 *
 * @param       def     the default value
 * @return	a value
 */
    
    public final float lly(float def) {
        if (lly == Float.MIN_VALUE) return def;
        return lly;
    }
    
/**
 * Returns the upper right x-value.
 *
 * @param       the default value
 * @return	a value
 */
    
    public final float urx(float def) {
        if (urx == Float.MIN_VALUE) return def;
        return urx;
    }
    
/**
 * Returns the upper right y-value.
 *
 * @param       the default value
 * @return	a value
 */
    
    public final float ury(float def) {
        if (ury == Float.MIN_VALUE) return def;
        return ury;
    }
    
/**
 * Returns the type of this <CODE>Annotation</CODE>.
 *
 * @return	a type
 */
    
    public final int annotationType() {
        return annotationtype;
    }
    
/**
 * Returns the title of this <CODE>Annotation</CODE>.
 *
 * @return	a name
 */
    
    public final String title() {
        String s = (String)attributes.get(TITLE);
        if (s == null) s = "";
        return s;
    }
    
/**
 * Gets the content of this <CODE>Annotation</CODE>.
 *
 * @return	a reference
 */
    
    public final String content() {
        String s = (String)attributes.get(CONTENT);
        if (s == null) s = "";
        return s;
    }
    
/**
 * Gets the content of this <CODE>Annotation</CODE>.
 *
 * @return	a reference
 */
    
    public final HashMap attributes() {
        return attributes;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.ANNOTATION.equals(tag);
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributeNames()
 */
    public Set getMarkupAttributeNames() {
        return (markupAttributes == null) ? Collections.EMPTY_SET : markupAttributes.keySet();
    }
    
    
/**
 * @see com.lowagie.text.MarkupAttributes#setMarkupAttribute(java.lang.String, java.lang.String)
 */
    public void setMarkupAttribute(String name, String value) {
        markupAttributes = (markupAttributes == null) ? new Properties() : markupAttributes;
        markupAttributes.put(name, value);
    }
    
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttribute(java.lang.String)
 */
    public String getMarkupAttribute(String name) {
        return (markupAttributes == null) ? null : String.valueOf(markupAttributes.get(name));
    }
}
