/*
 * Copyright 2003 Paulo Soares
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
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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
package com.lowagie.text.xml.simpleparser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Stack;

/**
 * A simple XML and HTML parser.  This parser is, like the SAX parser,
 * an event based parser, but with much less functionality.
 * <p>
 * The parser can:
 * <p>
 * <ul>
 * <li>It recognizes the encoding used
 * <li>It recognizes all the elements' start tags and end tags
 * <li>It lists attributes, where attribute values can be enclosed in single or double quotes
 * <li>It recognizes the <code>&lt;[CDATA[ ... ]]&gt;</code> construct
 * <li>It recognizes the standard entities: &amp;amp;, &amp;lt;, &amp;gt;, &amp;quot;, and &amp;apos;, as well as numeric entities
 * <li>It maps lines ending in <code>\r\n</code> and <code>\r</code> to <code>\n</code> on input, in accordance with the XML Specification, Section 2.11
 * </ul>
 * <p>
 * The code is based on <A HREF="http://www.javaworld.com/javatips/jw-javatip128_p.html">http://www.javaworld.com/javatips/jw-javatip128_p.html</A> with some extra
 * code from XERCES to recognize the encoding.
 */
public class SimpleXMLParser {
    
    private static int popMode(Stack st) {
        if(!st.empty())
            return ((Integer)st.pop()).intValue();
        else
            return PRE;
    }
    
    private final static int
    TEXT = 1,
    ENTITY = 2,
    OPEN_TAG = 3,
    CLOSE_TAG = 4,
    START_TAG = 5,
    ATTRIBUTE_LVALUE = 6,
    ATTRIBUTE_EQUAL = 9,
    ATTRIBUTE_RVALUE = 10,
    QUOTE = 7,
    IN_TAG = 8,
    SINGLE_TAG = 12,
    COMMENT = 13,
    DONE = 11,
    DOCTYPE = 14,
    PRE = 15,
    CDATA = 16;
    
    private SimpleXMLParser() {
    }
    
    /**
     * Parses the XML document firing the events to the handler.
     * @param doc the document handler
     * @param in the document. The encoding is deduced from the stream. The stream is not closed
     * @throws IOException on error
     */    
    public static void parse(SimpleXMLDocHandler doc, InputStream in) throws IOException {
        byte b4[] = new byte[4];
        int count = in.read(b4);
        if (count != 4)
            throw new IOException("Insufficient length.");
        String encoding = getEncodingName(b4);
        String decl = null;
        if (encoding.equals("UTF-8")) {
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) != -1) {
                if (c == '>')
                    break;
                sb.append((char)c);
            }
            decl = sb.toString();
        }
        else if (encoding.equals("CP037")) {
            ByteArrayOutputStream bi = new ByteArrayOutputStream();
            int c;
            while ((c = in.read()) != -1) {
                if (c == 0x6e) // that's '>' in ebcdic
                    break;
                bi.write(c);
            }
            decl = new String(bi.toByteArray(), "CP037");
        }
        if (decl != null) {
            decl = getDeclaredEncoding(decl);
            if (decl != null)
                encoding = decl;
        }
        parse(doc, new InputStreamReader(in, IanaToJava.getJavaEncoding(encoding)));
    }
    
    private static String getDeclaredEncoding(String decl) {
        if (decl == null)
            return null;
        int idx = decl.indexOf("encoding");
        if (idx < 0)
            return null;
        int idx1 = decl.indexOf('"', idx);
        int idx2 = decl.indexOf('\'', idx);
        if (idx1 == idx2)
            return null;
        if ((idx1 < 0 && idx2 > 0) || (idx2 > 0 && idx2 < idx1)) {
            int idx3 = decl.indexOf('\'', idx2 + 1);
            if (idx3 < 0)
                return null;
            return decl.substring(idx2 + 1, idx3);
        }
        if ((idx2 < 0 && idx1 > 0) || (idx1 > 0 && idx1 < idx2)) {
            int idx3 = decl.indexOf('"', idx1 + 1);
            if (idx3 < 0)
                return null;
            return decl.substring(idx1 + 1, idx3);
        }
        return null;
    }
    
    public static void parse(SimpleXMLDocHandler doc,Reader r) throws IOException {
        parse(doc, null, r, false);
    }
    
    /**
     * Parses the XML document firing the events to the handler.
     * @param doc the document handler
     * @param r the document. The encoding is already resolved. The reader is not closed
     * @throws IOException on error
     */
    public static void parse(SimpleXMLDocHandler doc, SimpleXMLDocHandlerComment comment, Reader r, boolean html) throws IOException {
        BufferedReader reader;
        if (r instanceof BufferedReader)
            reader = (BufferedReader)r;
        else
            reader = new BufferedReader(r);
        Stack st = new Stack();
        int depth = 0;
        int mode = PRE;
        int c = 0;
        int quotec = '"';
        depth = 0;
        StringBuffer sb = new StringBuffer();
        StringBuffer etag = new StringBuffer();
        String tagName = null;
        String lvalue = null;
        String rvalue = null;
        HashMap attrs = null;
        st = new Stack();
        doc.startDocument();
        int line=1, col=0;
        boolean eol = false;
        if (html)
            mode = TEXT;
        int pushBack = -1;
        while(true) {
            if (pushBack != -1) {
                c = pushBack;
                pushBack = -1;
            }
            else
                c = reader.read();
            if (c == -1)
                break;
            
            // We need to map \r, \r\n, and \n to \n
            // See XML spec section 2.11
            if(c == '\n' && eol) {
                eol = false;
                continue;
            } else if(eol) {
                eol = false;
            } else if(c == '\n') {
                line++;
                col=0;
            } else if(c == '\r') {
                eol = true;
                c = '\n';
                line++;
                col=0;
            } else {
                col++;
            }
            
            if(mode == DONE) {
                doc.endDocument();
                return;
                
                // We are between tags collecting text.
            } else if(mode == TEXT) {
                if(c == '<') {
                    st.push(new Integer(mode));
                    mode = START_TAG;
                    if(sb.length() > 0) {
                        doc.text(sb.toString());
                        sb.setLength(0);
                    }
                } else if(c == '&') {
                    st.push(new Integer(mode));
                    mode = ENTITY;
                    etag.setLength(0);
                } else
                    sb.append((char)c);
                
                // we are processing a closing tag: e.g. </foo>
            } else if(mode == CLOSE_TAG) {
                if(c == '>') {
                    mode = popMode(st);
                    tagName = sb.toString();
                    if (html)
                        tagName = tagName.toLowerCase();
                    sb.setLength(0);
                    depth--;
                    if(!html && depth==0)
                        mode = DONE;
                   doc.endElement(tagName);
                } else {
                    if (!Character.isWhitespace((char)c))
                        sb.append((char)c);
                }
                
                // we are processing CDATA
            } else if(mode == CDATA) {
                if(c == '>'
                && sb.toString().endsWith("]]")) {
                    sb.setLength(sb.length()-2);
                    doc.text(sb.toString());
                    sb.setLength(0);
                    mode = popMode(st);
                } else
                    sb.append((char)c);
                
                // we are processing a comment.  We are inside
                // the <!-- .... --> looking for the -->.
            } else if(mode == COMMENT) {
                if(c == '>'
                && sb.toString().endsWith("--")) {
                    if (comment != null) {
                        sb.setLength(sb.length() - 2);
                        comment.comment(sb.toString());
                    }
                    sb.setLength(0);
                    mode = popMode(st);
                } else
                    sb.append((char)c);
                
                // We are outside the root tag element
            } else if(mode == PRE) {
                if(c == '<') {
                    mode = TEXT;
                    st.push(new Integer(mode));
                    mode = START_TAG;
                }
                
                // We are inside one of these <? ... ?>
                // or one of these <!DOCTYPE ... >
            } else if(mode == DOCTYPE) {
                if(c == '>') {
                    mode = popMode(st);
                    if(mode == TEXT) mode = PRE;
                }
                
                // we have just seen a < and
                // are wondering what we are looking at
                // <foo>, </foo>, <!-- ... --->, etc.
            } else if(mode == START_TAG) {
                mode = popMode(st);
                if(c == '/') {
                    st.push(new Integer(mode));
                    mode = CLOSE_TAG;
                } else if (c == '?') {
                    mode = DOCTYPE;
                } else {
                    st.push(new Integer(mode));
                    mode = OPEN_TAG;
                    tagName = null;
                    attrs = new HashMap();
                    sb.append((char)c);
                }
                
                // we are processing an entity, e.g. &lt;, &#187;, etc.
            } else if(mode == ENTITY) {
                if(c == ';') {
                    mode = popMode(st);
                    String cent = etag.toString();
                    etag.setLength(0);
                    if(cent.startsWith("#x")) {
                        try {
                            char ci = (char)Integer.parseInt(cent.substring(2),16);
                            sb.append(ci);
                        }
                        catch (Exception es) {
                            sb.append('&').append(cent).append(';');
                        }
                    }
                    else if(cent.startsWith("#")) {
                        try {
                            char ci = (char)Integer.parseInt(cent.substring(1));
                            sb.append(ci);
                        }
                        catch (Exception es) {
                            sb.append('&').append(cent).append(';');
                        }
                    }
                    else {
                        char ce = EntitiesToUnicode.decodeEntity(cent);
                        if (ce == '\0')
                            sb.append('&').append(cent).append(';');
                        else
                        sb.append(ce);
                    }
                } else if ((c != '#' && (c < '0' || c > '9') && (c < 'a' || c > 'z')
                    && (c < 'A' || c > 'Z')) || etag.length() >= 7) {
                    mode = popMode(st);
                    pushBack = c;
                    sb.append('&').append(etag.toString());
                    etag.setLength(0);
                }
                else {
                    etag.append((char)c);
                }
                
                // we have just seen something like this:
                // <foo a="b"/
                // and are looking for the final >.
            } else if(mode == SINGLE_TAG) {
                if(tagName == null)
                    tagName = sb.toString();
                if (html)
                    tagName = tagName.toLowerCase();
                if(c != '>')
                    exc("Expected > for tag: <"+tagName+"/>",line,col);
                doc.startElement(tagName,attrs);
                doc.endElement(tagName);
                if(!html && depth==0) {
                    doc.endDocument();
                    return;
                }
                sb.setLength(0);
                attrs = new HashMap();
                tagName = null;
                mode = popMode(st);
                
                // we are processing something
                // like this <foo ... >.  It could
                // still be a <!-- ... --> or something.
            } else if(mode == OPEN_TAG) {
                if(c == '>') {
                    if(tagName == null)
                        tagName = sb.toString();
                    if (html)
                        tagName = tagName.toLowerCase();
                    sb.setLength(0);
                    depth++;
                    doc.startElement(tagName,attrs);
                    tagName = null;
                    attrs = new HashMap();
                    mode = popMode(st);
                } else if(c == '/') {
                    mode = SINGLE_TAG;
                } else if(c == '-' && sb.toString().equals("!-")) {
                    mode = COMMENT;
                    sb.setLength(0);
                } else if(c == '[' && sb.toString().equals("![CDATA")) {
                    mode = CDATA;
                    sb.setLength(0);
                } else if(c == 'E' && sb.toString().equals("!DOCTYP")) {
                    sb.setLength(0);
                    mode = DOCTYPE;
                } else if(Character.isWhitespace((char)c)) {
                    tagName = sb.toString();
                    if (html)
                        tagName = tagName.toLowerCase();
                    sb.setLength(0);
                    mode = IN_TAG;
                } else {
                    sb.append((char)c);
                }
                
                // We are processing the quoted right-hand side
                // of an element's attribute.
            } else if(mode == QUOTE) {
                if (html && quotec == ' ' && c == '>') {
                    rvalue = sb.toString();
                    sb.setLength(0);
                    attrs.put(lvalue,rvalue);
                    mode = popMode(st);
                    doc.startElement(tagName,attrs);
                    depth++;
                    tagName = null;
                    attrs = new HashMap();
                }
                else if (html && quotec == ' ' && Character.isWhitespace((char)c)) {
                    rvalue = sb.toString();
                    sb.setLength(0);
                    attrs.put(lvalue,rvalue);
                    mode = IN_TAG;
                }
                else if (html && quotec == ' ') {
                    sb.append((char)c);
                }
                else if(c == quotec) {
                    rvalue = sb.toString();
                    sb.setLength(0);
                    attrs.put(lvalue,rvalue);
                    mode = IN_TAG;
                    // See section the XML spec, section 3.3.3
                    // on normalization processing.
                } else if(" \r\n\u0009".indexOf(c)>=0) {
                    sb.append(' ');
                } else if(c == '&') {
                    st.push(new Integer(mode));
                    mode = ENTITY;
                    etag.setLength(0);
                } else {
                    sb.append((char)c);
                }
                
            } else if(mode == ATTRIBUTE_RVALUE) {
                if(c == '"' || c == '\'') {
                    quotec = c;
                    mode = QUOTE;
                } else if(Character.isWhitespace((char)c)) {
                    // empty
                } else if (html && c == '>') {
                    attrs.put(lvalue,sb.toString());
                    sb.setLength(0);
                    mode = popMode(st);
                    doc.startElement(tagName,attrs);
                    depth++;
                    tagName = null;
                    attrs = new HashMap();
                } else if (html) {
                    sb.append((char)c);
                    quotec = ' ';
                    mode = QUOTE;
                } else {
                    exc("Error in attribute processing",line,col);
                }
                
            } else if(mode == ATTRIBUTE_LVALUE) {
                if(Character.isWhitespace((char)c)) {
                    lvalue = sb.toString();
                    if (html)
                        lvalue = lvalue.toLowerCase();
                    sb.setLength(0);
                    mode = ATTRIBUTE_EQUAL;
                } else if(c == '=') {
                    lvalue = sb.toString();
                    if (html)
                        lvalue = lvalue.toLowerCase();
                    sb.setLength(0);
                    mode = ATTRIBUTE_RVALUE;
                } else if (html && c == '>') {
                    sb.setLength(0);
                    mode = popMode(st);
                    doc.startElement(tagName,attrs);
                    depth++;
                    tagName = null;
                    attrs = new HashMap();
                } else {
                    sb.append((char)c);
                }
                
            } else if(mode == ATTRIBUTE_EQUAL) {
                if(c == '=') {
                    mode = ATTRIBUTE_RVALUE;
                } else if(Character.isWhitespace((char)c)) {
                    // empty
                } else if (html && c == '>') {
                    sb.setLength(0);
                    mode = popMode(st);
                    doc.startElement(tagName,attrs);
                    depth++;
                    tagName = null;
                    attrs = new HashMap();
                } else if (html && c == '/') {
                    sb.setLength(0);
                    mode = SINGLE_TAG;
                } else if (html) {
                    sb.setLength(0);
                    sb.append((char)c);
                    mode = ATTRIBUTE_LVALUE;
                } else {
                    exc("Error in attribute processing.",line,col);
                }
                
            } else if(mode == IN_TAG) {
                if(c == '>') {
                    mode = popMode(st);
                    doc.startElement(tagName,attrs);
                    depth++;
                    tagName = null;
                    attrs = new HashMap();
                } else if(c == '/') {
                    mode = SINGLE_TAG;
                } else if(Character.isWhitespace((char)c)) {
                    // empty
                } else {
                    mode = ATTRIBUTE_LVALUE;
                    sb.append((char)c);
                }
            }
        }
        if(html || mode == DONE) {
            if (html && mode == TEXT)
                doc.text(sb.toString());
            doc.endDocument();
        }
        else
            exc("missing end tag",line,col);
    }
    private static void exc(String s,int line,int col) throws IOException {
        throw new IOException(s+" near line "+line+", column "+col);
    }
    
    /**
     * Escapes a string with the appropriated XML codes.
     * @param s the string to be escaped
     * @param onlyASCII codes above 127 will always be escaped with &amp;#nn; if <CODE>true</CODE>
     * @return the escaped string
     */    
    public static String escapeXML(String s, boolean onlyASCII) {
        char cc[] = s.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < len; ++k) {
            int c = cc[k];
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    if (onlyASCII && c > 127)
                        sb.append("&#").append(c).append(';');
                    else
                        sb.append((char)c);
            }
        }
        return sb.toString();
    }
    
    private static String getEncodingName(byte[] b4) {
        
        // UTF-16, with BOM
        int b0 = b4[0] & 0xFF;
        int b1 = b4[1] & 0xFF;
        if (b0 == 0xFE && b1 == 0xFF) {
            // UTF-16, big-endian
            return "UTF-16BE";
        }
        if (b0 == 0xFF && b1 == 0xFE) {
            // UTF-16, little-endian
            return "UTF-16LE";
        }
        
        // UTF-8 with a BOM
        int b2 = b4[2] & 0xFF;
        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            return "UTF-8";
        }
        
        // other encodings
        int b3 = b4[3] & 0xFF;
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C) {
            // UCS-4, big endian (1234)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, little endian (4321)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x3C && b3 == 0x00) {
            // UCS-4, unusual octet order (2143)
            // REVISIT: What should this be?
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, unusual octect order (3412)
            // REVISIT: What should this be?
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F) {
            // UTF-16, big-endian, no BOM
            // (or could turn out to be UCS-2...
            // REVISIT: What should this be?
            return "UTF-16BE";
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00) {
            // UTF-16, little-endian, no BOM
            // (or could turn out to be UCS-2...
            return "UTF-16LE";
        }
        if (b0 == 0x4C && b1 == 0x6F && b2 == 0xA7 && b3 == 0x94) {
            // EBCDIC
            // a la xerces1, return CP037 instead of EBCDIC here
            return "CP037";
        }
        
        // default encoding
        return "UTF-8";
    }
}