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
package com.lowagie.text.pdf;

import java.io.*;
import java.util.Stack;
import java.util.HashMap;

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
 * The code is based on <A HREF="http://www.javaworld.com/javaworld/javatips/javatip128/">
 * http://www.javaworld.com/javaworld/javatips/javatip128/</A> with some extra
 * code from XERCES to recognize the encoding.
 */
public class SimpleXMLParser {
    private static final HashMap fIANA2JavaMap = new HashMap();
    private static final HashMap entityMap = new HashMap();
    
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
        parse(doc, new InputStreamReader(in, getJavaEncoding(encoding)));
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
    
    /**
     * Gets the java encoding from the IANA encoding. If the encoding cannot be found
     * it returns the input.
     * @param iana the IANA encoding
     * @return the java encoding
     */    
    public static String getJavaEncoding(String iana) {
        String IANA = iana.toUpperCase();
        String jdec = (String)fIANA2JavaMap.get(IANA);
        if (jdec == null)
            jdec = iana;
        return jdec;
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
                        char ce = decodeEntity(cent);
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
                    ;
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
                    ;
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
                    ;
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
                        sb.append("&#").append(c).append(";");
                    else
                        sb.append((char)c);
            }
        }
        return sb.toString();
    }
    
    public static char decodeEntity(String s) {
        Character c = (Character)entityMap.get(s);
        if (c == null)
            return '\0';
        else
            return c.charValue();
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

    static {        
        // add IANA to Java encoding mappings.
        fIANA2JavaMap.put("BIG5", "Big5");
        fIANA2JavaMap.put("CSBIG5", "Big5");
        fIANA2JavaMap.put("CP037", "CP037");
        fIANA2JavaMap.put("IBM037", "CP037");
        fIANA2JavaMap.put("CSIBM037", "CP037");
        fIANA2JavaMap.put("EBCDIC-CP-US", "CP037");
        fIANA2JavaMap.put("EBCDIC-CP-CA", "CP037");
        fIANA2JavaMap.put("EBCDIC-CP-NL", "CP037");
        fIANA2JavaMap.put("EBCDIC-CP-WT", "CP037");
        fIANA2JavaMap.put("IBM277", "CP277");
        fIANA2JavaMap.put("CP277", "CP277");
        fIANA2JavaMap.put("CSIBM277", "CP277");
        fIANA2JavaMap.put("EBCDIC-CP-DK", "CP277");
        fIANA2JavaMap.put("EBCDIC-CP-NO", "CP277");
        fIANA2JavaMap.put("IBM278", "CP278");
        fIANA2JavaMap.put("CP278", "CP278");
        fIANA2JavaMap.put("CSIBM278", "CP278");
        fIANA2JavaMap.put("EBCDIC-CP-FI", "CP278");
        fIANA2JavaMap.put("EBCDIC-CP-SE", "CP278");
        fIANA2JavaMap.put("IBM280", "CP280");
        fIANA2JavaMap.put("CP280", "CP280");
        fIANA2JavaMap.put("CSIBM280", "CP280");
        fIANA2JavaMap.put("EBCDIC-CP-IT", "CP280");
        fIANA2JavaMap.put("IBM284", "CP284");
        fIANA2JavaMap.put("CP284", "CP284");
        fIANA2JavaMap.put("CSIBM284", "CP284");
        fIANA2JavaMap.put("EBCDIC-CP-ES", "CP284");
        fIANA2JavaMap.put("EBCDIC-CP-GB", "CP285");
        fIANA2JavaMap.put("IBM285", "CP285");
        fIANA2JavaMap.put("CP285", "CP285");
        fIANA2JavaMap.put("CSIBM285", "CP285");
        fIANA2JavaMap.put("EBCDIC-CP-FR", "CP297");
        fIANA2JavaMap.put("IBM297", "CP297");
        fIANA2JavaMap.put("CP297", "CP297");
        fIANA2JavaMap.put("CSIBM297", "CP297");
        fIANA2JavaMap.put("EBCDIC-CP-AR1", "CP420");
        fIANA2JavaMap.put("IBM420", "CP420");
        fIANA2JavaMap.put("CP420", "CP420");
        fIANA2JavaMap.put("CSIBM420", "CP420");
        fIANA2JavaMap.put("EBCDIC-CP-HE", "CP424");
        fIANA2JavaMap.put("IBM424", "CP424");
        fIANA2JavaMap.put("CP424", "CP424");
        fIANA2JavaMap.put("CSIBM424", "CP424");
        fIANA2JavaMap.put("EBCDIC-CP-CH", "CP500");
        fIANA2JavaMap.put("IBM500", "CP500");
        fIANA2JavaMap.put("CP500", "CP500");
        fIANA2JavaMap.put("CSIBM500", "CP500");
        fIANA2JavaMap.put("EBCDIC-CP-CH", "CP500");
        fIANA2JavaMap.put("EBCDIC-CP-BE", "CP500");
        fIANA2JavaMap.put("IBM868", "CP868");
        fIANA2JavaMap.put("CP868", "CP868");
        fIANA2JavaMap.put("CSIBM868", "CP868");
        fIANA2JavaMap.put("CP-AR", "CP868");
        fIANA2JavaMap.put("IBM869", "CP869");
        fIANA2JavaMap.put("CP869", "CP869");
        fIANA2JavaMap.put("CSIBM869", "CP869");
        fIANA2JavaMap.put("CP-GR", "CP869");
        fIANA2JavaMap.put("IBM870", "CP870");
        fIANA2JavaMap.put("CP870", "CP870");
        fIANA2JavaMap.put("CSIBM870", "CP870");
        fIANA2JavaMap.put("EBCDIC-CP-ROECE", "CP870");
        fIANA2JavaMap.put("EBCDIC-CP-YU", "CP870");
        fIANA2JavaMap.put("IBM871", "CP871");
        fIANA2JavaMap.put("CP871", "CP871");
        fIANA2JavaMap.put("CSIBM871", "CP871");
        fIANA2JavaMap.put("EBCDIC-CP-IS", "CP871");
        fIANA2JavaMap.put("IBM918", "CP918");
        fIANA2JavaMap.put("CP918", "CP918");
        fIANA2JavaMap.put("CSIBM918", "CP918");
        fIANA2JavaMap.put("EBCDIC-CP-AR2", "CP918");
        fIANA2JavaMap.put("EUC-JP", "EUCJIS");
        fIANA2JavaMap.put("CSEUCPkdFmtJapanese", "EUCJIS");
        fIANA2JavaMap.put("EUC-KR", "KSC5601");
        fIANA2JavaMap.put("GB2312", "GB2312");
        fIANA2JavaMap.put("CSGB2312", "GB2312");
        fIANA2JavaMap.put("ISO-2022-JP", "JIS");
        fIANA2JavaMap.put("CSISO2022JP", "JIS");
        fIANA2JavaMap.put("ISO-2022-KR", "ISO2022KR");
        fIANA2JavaMap.put("CSISO2022KR", "ISO2022KR");
        fIANA2JavaMap.put("ISO-2022-CN", "ISO2022CN");
        
        fIANA2JavaMap.put("X0201", "JIS0201");
        fIANA2JavaMap.put("CSISO13JISC6220JP", "JIS0201");
        fIANA2JavaMap.put("X0208", "JIS0208");
        fIANA2JavaMap.put("ISO-IR-87", "JIS0208");
        fIANA2JavaMap.put("X0208dbiJIS_X0208-1983", "JIS0208");
        fIANA2JavaMap.put("CSISO87JISX0208", "JIS0208");
        fIANA2JavaMap.put("X0212", "JIS0212");
        fIANA2JavaMap.put("ISO-IR-159", "JIS0212");
        fIANA2JavaMap.put("CSISO159JISX02121990", "JIS0212");
        fIANA2JavaMap.put("SHIFT_JIS", "SJIS");
        fIANA2JavaMap.put("CSSHIFT_JIS", "SJIS");
        fIANA2JavaMap.put("MS_Kanji", "SJIS");
        
        // Add support for Cp1252 and its friends
        fIANA2JavaMap.put("WINDOWS-1250", "Cp1250");
        fIANA2JavaMap.put("WINDOWS-1251", "Cp1251");
        fIANA2JavaMap.put("WINDOWS-1252", "Cp1252");
        fIANA2JavaMap.put("WINDOWS-1253", "Cp1253");
        fIANA2JavaMap.put("WINDOWS-1254", "Cp1254");
        fIANA2JavaMap.put("WINDOWS-1255", "Cp1255");
        fIANA2JavaMap.put("WINDOWS-1256", "Cp1256");
        fIANA2JavaMap.put("WINDOWS-1257", "Cp1257");
        fIANA2JavaMap.put("WINDOWS-1258", "Cp1258");
        fIANA2JavaMap.put("TIS-620", "TIS620");
        
        fIANA2JavaMap.put("ISO-8859-1", "ISO8859_1");
        fIANA2JavaMap.put("ISO-IR-100", "ISO8859_1");
        fIANA2JavaMap.put("ISO_8859-1", "ISO8859_1");
        fIANA2JavaMap.put("LATIN1", "ISO8859_1");
        fIANA2JavaMap.put("CSISOLATIN1", "ISO8859_1");
        fIANA2JavaMap.put("L1", "ISO8859_1");
        fIANA2JavaMap.put("IBM819", "ISO8859_1");
        fIANA2JavaMap.put("CP819", "ISO8859_1");
        
        fIANA2JavaMap.put("ISO-8859-2", "ISO8859_2");
        fIANA2JavaMap.put("ISO-IR-101", "ISO8859_2");
        fIANA2JavaMap.put("ISO_8859-2", "ISO8859_2");
        fIANA2JavaMap.put("LATIN2", "ISO8859_2");
        fIANA2JavaMap.put("CSISOLATIN2", "ISO8859_2");
        fIANA2JavaMap.put("L2", "ISO8859_2");
        
        fIANA2JavaMap.put("ISO-8859-3", "ISO8859_3");
        fIANA2JavaMap.put("ISO-IR-109", "ISO8859_3");
        fIANA2JavaMap.put("ISO_8859-3", "ISO8859_3");
        fIANA2JavaMap.put("LATIN3", "ISO8859_3");
        fIANA2JavaMap.put("CSISOLATIN3", "ISO8859_3");
        fIANA2JavaMap.put("L3", "ISO8859_3");
        
        fIANA2JavaMap.put("ISO-8859-4", "ISO8859_4");
        fIANA2JavaMap.put("ISO-IR-110", "ISO8859_4");
        fIANA2JavaMap.put("ISO_8859-4", "ISO8859_4");
        fIANA2JavaMap.put("LATIN4", "ISO8859_4");
        fIANA2JavaMap.put("CSISOLATIN4", "ISO8859_4");
        fIANA2JavaMap.put("L4", "ISO8859_4");
        
        fIANA2JavaMap.put("ISO-8859-5", "ISO8859_5");
        fIANA2JavaMap.put("ISO-IR-144", "ISO8859_5");
        fIANA2JavaMap.put("ISO_8859-5", "ISO8859_5");
        fIANA2JavaMap.put("CYRILLIC", "ISO8859_5");
        fIANA2JavaMap.put("CSISOLATINCYRILLIC", "ISO8859_5");
        
        fIANA2JavaMap.put("ISO-8859-6", "ISO8859_6");
        fIANA2JavaMap.put("ISO-IR-127", "ISO8859_6");
        fIANA2JavaMap.put("ISO_8859-6", "ISO8859_6");
        fIANA2JavaMap.put("ECMA-114", "ISO8859_6");
        fIANA2JavaMap.put("ASMO-708", "ISO8859_6");
        fIANA2JavaMap.put("ARABIC", "ISO8859_6");
        fIANA2JavaMap.put("CSISOLATINARABIC", "ISO8859_6");
        
        fIANA2JavaMap.put("ISO-8859-7", "ISO8859_7");
        fIANA2JavaMap.put("ISO-IR-126", "ISO8859_7");
        fIANA2JavaMap.put("ISO_8859-7", "ISO8859_7");
        fIANA2JavaMap.put("ELOT_928", "ISO8859_7");
        fIANA2JavaMap.put("ECMA-118", "ISO8859_7");
        fIANA2JavaMap.put("GREEK", "ISO8859_7");
        fIANA2JavaMap.put("CSISOLATINGREEK", "ISO8859_7");
        fIANA2JavaMap.put("GREEK8", "ISO8859_7");
        
        fIANA2JavaMap.put("ISO-8859-8", "ISO8859_8");
        fIANA2JavaMap.put("ISO-8859-8-I", "ISO8859_8"); // added since this encoding only differs w.r.t. presentation
        fIANA2JavaMap.put("ISO-IR-138", "ISO8859_8");
        fIANA2JavaMap.put("ISO_8859-8", "ISO8859_8");
        fIANA2JavaMap.put("HEBREW", "ISO8859_8");
        fIANA2JavaMap.put("CSISOLATINHEBREW", "ISO8859_8");
        
        fIANA2JavaMap.put("ISO-8859-9", "ISO8859_9");
        fIANA2JavaMap.put("ISO-IR-148", "ISO8859_9");
        fIANA2JavaMap.put("ISO_8859-9", "ISO8859_9");
        fIANA2JavaMap.put("LATIN5", "ISO8859_9");
        fIANA2JavaMap.put("CSISOLATIN5", "ISO8859_9");
        fIANA2JavaMap.put("L5", "ISO8859_9");
        
        fIANA2JavaMap.put("KOI8-R", "KOI8_R");
        fIANA2JavaMap.put("CSKOI8-R", "KOI8_R");
        fIANA2JavaMap.put("US-ASCII", "ASCII");
        fIANA2JavaMap.put("ISO-IR-6", "ASCII");
        fIANA2JavaMap.put("ANSI_X3.4-1986", "ASCII");
        fIANA2JavaMap.put("ISO_646.IRV:1991", "ASCII");
        fIANA2JavaMap.put("ASCII", "ASCII");
        fIANA2JavaMap.put("CSASCII", "ASCII");
        fIANA2JavaMap.put("ISO646-US", "ASCII");
        fIANA2JavaMap.put("US", "ASCII");
        fIANA2JavaMap.put("IBM367", "ASCII");
        fIANA2JavaMap.put("CP367", "ASCII");
        fIANA2JavaMap.put("UTF-8", "UTF8");
        fIANA2JavaMap.put("UTF-16", "Unicode");
        fIANA2JavaMap.put("UTF-16BE", "UnicodeBig");
        fIANA2JavaMap.put("UTF-16LE", "UnicodeLittle");

        entityMap.put("nbsp", new Character('\u00a0')); // no-break space = non-breaking space, U+00A0 ISOnum
        entityMap.put("iexcl", new Character('\u00a1')); // inverted exclamation mark, U+00A1 ISOnum
        entityMap.put("cent", new Character('\u00a2')); // cent sign, U+00A2 ISOnum
        entityMap.put("pound", new Character('\u00a3')); // pound sign, U+00A3 ISOnum
        entityMap.put("curren", new Character('\u00a4')); // currency sign, U+00A4 ISOnum
        entityMap.put("yen", new Character('\u00a5')); // yen sign = yuan sign, U+00A5 ISOnum
        entityMap.put("brvbar", new Character('\u00a6')); // broken bar = broken vertical bar, U+00A6 ISOnum
        entityMap.put("sect", new Character('\u00a7')); // section sign, U+00A7 ISOnum
        entityMap.put("uml", new Character('\u00a8')); // diaeresis = spacing diaeresis, U+00A8 ISOdia
        entityMap.put("copy", new Character('\u00a9')); // copyright sign, U+00A9 ISOnum
        entityMap.put("ordf", new Character('\u00aa')); // feminine ordinal indicator, U+00AA ISOnum
        entityMap.put("laquo", new Character('\u00ab')); // left-pointing double angle quotation mark = left pointing guillemet, U+00AB ISOnum
        entityMap.put("not", new Character('\u00ac')); // not sign, U+00AC ISOnum
        entityMap.put("shy", new Character('\u00ad')); // soft hyphen = discretionary hyphen, U+00AD ISOnum
        entityMap.put("reg", new Character('\u00ae')); // registered sign = registered trade mark sign, U+00AE ISOnum
        entityMap.put("macr", new Character('\u00af')); // macron = spacing macron = overline = APL overbar, U+00AF ISOdia
        entityMap.put("deg", new Character('\u00b0')); // degree sign, U+00B0 ISOnum
        entityMap.put("plusmn", new Character('\u00b1')); // plus-minus sign = plus-or-minus sign, U+00B1 ISOnum
        entityMap.put("sup2", new Character('\u00b2')); // superscript two = superscript digit two = squared, U+00B2 ISOnum
        entityMap.put("sup3", new Character('\u00b3')); // superscript three = superscript digit three = cubed, U+00B3 ISOnum
        entityMap.put("acute", new Character('\u00b4')); // acute accent = spacing acute, U+00B4 ISOdia
        entityMap.put("micro", new Character('\u00b5')); // micro sign, U+00B5 ISOnum
        entityMap.put("para", new Character('\u00b6')); // pilcrow sign = paragraph sign, U+00B6 ISOnum
        entityMap.put("middot", new Character('\u00b7')); // middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum
        entityMap.put("cedil", new Character('\u00b8')); // cedilla = spacing cedilla, U+00B8 ISOdia
        entityMap.put("sup1", new Character('\u00b9')); // superscript one = superscript digit one, U+00B9 ISOnum
        entityMap.put("ordm", new Character('\u00ba')); // masculine ordinal indicator, U+00BA ISOnum
        entityMap.put("raquo", new Character('\u00bb')); // right-pointing double angle quotation mark = right pointing guillemet, U+00BB ISOnum
        entityMap.put("frac14", new Character('\u00bc')); // vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum
        entityMap.put("frac12", new Character('\u00bd')); // vulgar fraction one half = fraction one half, U+00BD ISOnum
        entityMap.put("frac34", new Character('\u00be')); // vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum
        entityMap.put("iquest", new Character('\u00bf')); // inverted question mark = turned question mark, U+00BF ISOnum
        entityMap.put("Agrave", new Character('\u00c0')); // latin capital letter A with grave = latin capital letter A grave, U+00C0 ISOlat1
        entityMap.put("Aacute", new Character('\u00c1')); // latin capital letter A with acute, U+00C1 ISOlat1
        entityMap.put("Acirc", new Character('\u00c2')); // latin capital letter A with circumflex, U+00C2 ISOlat1
        entityMap.put("Atilde", new Character('\u00c3')); // latin capital letter A with tilde, U+00C3 ISOlat1
        entityMap.put("Auml", new Character('\u00c4')); // latin capital letter A with diaeresis, U+00C4 ISOlat1
        entityMap.put("Aring", new Character('\u00c5')); // latin capital letter A with ring above = latin capital letter A ring, U+00C5 ISOlat1
        entityMap.put("AElig", new Character('\u00c6')); // latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1
        entityMap.put("Ccedil", new Character('\u00c7')); // latin capital letter C with cedilla, U+00C7 ISOlat1
        entityMap.put("Egrave", new Character('\u00c8')); // latin capital letter E with grave, U+00C8 ISOlat1
        entityMap.put("Eacute", new Character('\u00c9')); // latin capital letter E with acute, U+00C9 ISOlat1
        entityMap.put("Ecirc", new Character('\u00ca')); // latin capital letter E with circumflex, U+00CA ISOlat1
        entityMap.put("Euml", new Character('\u00cb')); // latin capital letter E with diaeresis, U+00CB ISOlat1
        entityMap.put("Igrave", new Character('\u00cc')); // latin capital letter I with grave, U+00CC ISOlat1
        entityMap.put("Iacute", new Character('\u00cd')); // latin capital letter I with acute, U+00CD ISOlat1
        entityMap.put("Icirc", new Character('\u00ce')); // latin capital letter I with circumflex, U+00CE ISOlat1
        entityMap.put("Iuml", new Character('\u00cf')); // latin capital letter I with diaeresis, U+00CF ISOlat1
        entityMap.put("ETH", new Character('\u00d0')); // latin capital letter ETH, U+00D0 ISOlat1
        entityMap.put("Ntilde", new Character('\u00d1')); // latin capital letter N with tilde, U+00D1 ISOlat1
        entityMap.put("Ograve", new Character('\u00d2')); // latin capital letter O with grave, U+00D2 ISOlat1
        entityMap.put("Oacute", new Character('\u00d3')); // latin capital letter O with acute, U+00D3 ISOlat1
        entityMap.put("Ocirc", new Character('\u00d4')); // latin capital letter O with circumflex, U+00D4 ISOlat1
        entityMap.put("Otilde", new Character('\u00d5')); // latin capital letter O with tilde, U+00D5 ISOlat1
        entityMap.put("Ouml", new Character('\u00d6')); // latin capital letter O with diaeresis, U+00D6 ISOlat1
        entityMap.put("times", new Character('\u00d7')); // multiplication sign, U+00D7 ISOnum
        entityMap.put("Oslash", new Character('\u00d8')); // latin capital letter O with stroke = latin capital letter O slash, U+00D8 ISOlat1
        entityMap.put("Ugrave", new Character('\u00d9')); // latin capital letter U with grave, U+00D9 ISOlat1
        entityMap.put("Uacute", new Character('\u00da')); // latin capital letter U with acute, U+00DA ISOlat1
        entityMap.put("Ucirc", new Character('\u00db')); // latin capital letter U with circumflex, U+00DB ISOlat1
        entityMap.put("Uuml", new Character('\u00dc')); // latin capital letter U with diaeresis, U+00DC ISOlat1
        entityMap.put("Yacute", new Character('\u00dd')); // latin capital letter Y with acute, U+00DD ISOlat1
        entityMap.put("THORN", new Character('\u00de')); // latin capital letter THORN, U+00DE ISOlat1
        entityMap.put("szlig", new Character('\u00df')); // latin small letter sharp s = ess-zed, U+00DF ISOlat1
        entityMap.put("agrave", new Character('\u00e0')); // latin small letter a with grave = latin small letter a grave, U+00E0 ISOlat1
        entityMap.put("aacute", new Character('\u00e1')); // latin small letter a with acute, U+00E1 ISOlat1
        entityMap.put("acirc", new Character('\u00e2')); // latin small letter a with circumflex, U+00E2 ISOlat1
        entityMap.put("atilde", new Character('\u00e3')); // latin small letter a with tilde, U+00E3 ISOlat1
        entityMap.put("auml", new Character('\u00e4')); // latin small letter a with diaeresis, U+00E4 ISOlat1
        entityMap.put("aring", new Character('\u00e5')); // latin small letter a with ring above = latin small letter a ring, U+00E5 ISOlat1
        entityMap.put("aelig", new Character('\u00e6')); // latin small letter ae = latin small ligature ae, U+00E6 ISOlat1
        entityMap.put("ccedil", new Character('\u00e7')); // latin small letter c with cedilla, U+00E7 ISOlat1
        entityMap.put("egrave", new Character('\u00e8')); // latin small letter e with grave, U+00E8 ISOlat1
        entityMap.put("eacute", new Character('\u00e9')); // latin small letter e with acute, U+00E9 ISOlat1
        entityMap.put("ecirc", new Character('\u00ea')); // latin small letter e with circumflex, U+00EA ISOlat1
        entityMap.put("euml", new Character('\u00eb')); // latin small letter e with diaeresis, U+00EB ISOlat1
        entityMap.put("igrave", new Character('\u00ec')); // latin small letter i with grave, U+00EC ISOlat1
        entityMap.put("iacute", new Character('\u00ed')); // latin small letter i with acute, U+00ED ISOlat1
        entityMap.put("icirc", new Character('\u00ee')); // latin small letter i with circumflex, U+00EE ISOlat1
        entityMap.put("iuml", new Character('\u00ef')); // latin small letter i with diaeresis, U+00EF ISOlat1
        entityMap.put("eth", new Character('\u00f0')); // latin small letter eth, U+00F0 ISOlat1
        entityMap.put("ntilde", new Character('\u00f1')); // latin small letter n with tilde, U+00F1 ISOlat1
        entityMap.put("ograve", new Character('\u00f2')); // latin small letter o with grave, U+00F2 ISOlat1
        entityMap.put("oacute", new Character('\u00f3')); // latin small letter o with acute, U+00F3 ISOlat1
        entityMap.put("ocirc", new Character('\u00f4')); // latin small letter o with circumflex, U+00F4 ISOlat1
        entityMap.put("otilde", new Character('\u00f5')); // latin small letter o with tilde, U+00F5 ISOlat1
        entityMap.put("ouml", new Character('\u00f6')); // latin small letter o with diaeresis, U+00F6 ISOlat1
        entityMap.put("divide", new Character('\u00f7')); // division sign, U+00F7 ISOnum
        entityMap.put("oslash", new Character('\u00f8')); // latin small letter o with stroke, = latin small letter o slash, U+00F8 ISOlat1
        entityMap.put("ugrave", new Character('\u00f9')); // latin small letter u with grave, U+00F9 ISOlat1
        entityMap.put("uacute", new Character('\u00fa')); // latin small letter u with acute, U+00FA ISOlat1
        entityMap.put("ucirc", new Character('\u00fb')); // latin small letter u with circumflex, U+00FB ISOlat1
        entityMap.put("uuml", new Character('\u00fc')); // latin small letter u with diaeresis, U+00FC ISOlat1
        entityMap.put("yacute", new Character('\u00fd')); // latin small letter y with acute, U+00FD ISOlat1
        entityMap.put("thorn", new Character('\u00fe')); // latin small letter thorn, U+00FE ISOlat1
        entityMap.put("yuml", new Character('\u00ff')); // latin small letter y with diaeresis, U+00FF ISOlat1
        // Latin Extended-B
        entityMap.put("fnof", new Character('\u0192')); // latin small f with hook = function = florin, U+0192 ISOtech
        // Greek
        entityMap.put("Alpha", new Character('\u0391')); // greek capital letter alpha, U+0391
        entityMap.put("Beta", new Character('\u0392')); // greek capital letter beta, U+0392
        entityMap.put("Gamma", new Character('\u0393')); // greek capital letter gamma, U+0393 ISOgrk3
        entityMap.put("Delta", new Character('\u0394')); // greek capital letter delta, U+0394 ISOgrk3
        entityMap.put("Epsilon", new Character('\u0395')); // greek capital letter epsilon, U+0395
        entityMap.put("Zeta", new Character('\u0396')); // greek capital letter zeta, U+0396
        entityMap.put("Eta", new Character('\u0397')); // greek capital letter eta, U+0397
        entityMap.put("Theta", new Character('\u0398')); // greek capital letter theta, U+0398 ISOgrk3
        entityMap.put("Iota", new Character('\u0399')); // greek capital letter iota, U+0399
        entityMap.put("Kappa", new Character('\u039a')); // greek capital letter kappa, U+039A
        entityMap.put("Lambda", new Character('\u039b')); // greek capital letter lambda, U+039B ISOgrk3
        entityMap.put("Mu", new Character('\u039c')); // greek capital letter mu, U+039C
        entityMap.put("Nu", new Character('\u039d')); // greek capital letter nu, U+039D
        entityMap.put("Xi", new Character('\u039e')); // greek capital letter xi, U+039E ISOgrk3
        entityMap.put("Omicron", new Character('\u039f')); // greek capital letter omicron, U+039F
        entityMap.put("Pi", new Character('\u03a0')); // greek capital letter pi, U+03A0 ISOgrk3
        entityMap.put("Rho", new Character('\u03a1')); // greek capital letter rho, U+03A1
        // there is no Sigmaf, and no U+03A2 character either
        entityMap.put("Sigma", new Character('\u03a3')); // greek capital letter sigma, U+03A3 ISOgrk3
        entityMap.put("Tau", new Character('\u03a4')); // greek capital letter tau, U+03A4
        entityMap.put("Upsilon", new Character('\u03a5')); // greek capital letter upsilon, U+03A5 ISOgrk3
        entityMap.put("Phi", new Character('\u03a6')); // greek capital letter phi, U+03A6 ISOgrk3
        entityMap.put("Chi", new Character('\u03a7')); // greek capital letter chi, U+03A7
        entityMap.put("Psi", new Character('\u03a8')); // greek capital letter psi, U+03A8 ISOgrk3
        entityMap.put("Omega", new Character('\u03a9')); // greek capital letter omega, U+03A9 ISOgrk3
        entityMap.put("alpha", new Character('\u03b1')); // greek small letter alpha, U+03B1 ISOgrk3
        entityMap.put("beta", new Character('\u03b2')); // greek small letter beta, U+03B2 ISOgrk3
        entityMap.put("gamma", new Character('\u03b3')); // greek small letter gamma, U+03B3 ISOgrk3
        entityMap.put("delta", new Character('\u03b4')); // greek small letter delta, U+03B4 ISOgrk3
        entityMap.put("epsilon", new Character('\u03b5')); // greek small letter epsilon, U+03B5 ISOgrk3
        entityMap.put("zeta", new Character('\u03b6')); // greek small letter zeta, U+03B6 ISOgrk3
        entityMap.put("eta", new Character('\u03b7')); // greek small letter eta, U+03B7 ISOgrk3
        entityMap.put("theta", new Character('\u03b8')); // greek small letter theta, U+03B8 ISOgrk3
        entityMap.put("iota", new Character('\u03b9')); // greek small letter iota, U+03B9 ISOgrk3
        entityMap.put("kappa", new Character('\u03ba')); // greek small letter kappa, U+03BA ISOgrk3
        entityMap.put("lambda", new Character('\u03bb')); // greek small letter lambda, U+03BB ISOgrk3
        entityMap.put("mu", new Character('\u03bc')); // greek small letter mu, U+03BC ISOgrk3
        entityMap.put("nu", new Character('\u03bd')); // greek small letter nu, U+03BD ISOgrk3
        entityMap.put("xi", new Character('\u03be')); // greek small letter xi, U+03BE ISOgrk3
        entityMap.put("omicron", new Character('\u03bf')); // greek small letter omicron, U+03BF NEW
        entityMap.put("pi", new Character('\u03c0')); // greek small letter pi, U+03C0 ISOgrk3
        entityMap.put("rho", new Character('\u03c1')); // greek small letter rho, U+03C1 ISOgrk3
        entityMap.put("sigmaf", new Character('\u03c2')); // greek small letter final sigma, U+03C2 ISOgrk3
        entityMap.put("sigma", new Character('\u03c3')); // greek small letter sigma, U+03C3 ISOgrk3
        entityMap.put("tau", new Character('\u03c4')); // greek small letter tau, U+03C4 ISOgrk3
        entityMap.put("upsilon", new Character('\u03c5')); // greek small letter upsilon, U+03C5 ISOgrk3
        entityMap.put("phi", new Character('\u03c6')); // greek small letter phi, U+03C6 ISOgrk3
        entityMap.put("chi", new Character('\u03c7')); // greek small letter chi, U+03C7 ISOgrk3
        entityMap.put("psi", new Character('\u03c8')); // greek small letter psi, U+03C8 ISOgrk3
        entityMap.put("omega", new Character('\u03c9')); // greek small letter omega, U+03C9 ISOgrk3
        entityMap.put("thetasym", new Character('\u03d1')); // greek small letter theta symbol, U+03D1 NEW
        entityMap.put("upsih", new Character('\u03d2')); // greek upsilon with hook symbol, U+03D2 NEW
        entityMap.put("piv", new Character('\u03d6')); // greek pi symbol, U+03D6 ISOgrk3
        // General Punctuation
        entityMap.put("bull", new Character('\u2022')); // bullet = black small circle, U+2022 ISOpub
        // bullet is NOT the same as bullet operator, U+2219
        entityMap.put("hellip", new Character('\u2026')); // horizontal ellipsis = three dot leader, U+2026 ISOpub
        entityMap.put("prime", new Character('\u2032')); // prime = minutes = feet, U+2032 ISOtech
        entityMap.put("Prime", new Character('\u2033')); // double prime = seconds = inches, U+2033 ISOtech
        entityMap.put("oline", new Character('\u203e')); // overline = spacing overscore, U+203E NEW
        entityMap.put("frasl", new Character('\u2044')); // fraction slash, U+2044 NEW
        // Letterlike Symbols
        entityMap.put("weierp", new Character('\u2118')); // script capital P = power set = Weierstrass p, U+2118 ISOamso
        entityMap.put("image", new Character('\u2111')); // blackletter capital I = imaginary part, U+2111 ISOamso
        entityMap.put("real", new Character('\u211c')); // blackletter capital R = real part symbol, U+211C ISOamso
        entityMap.put("trade", new Character('\u2122')); // trade mark sign, U+2122 ISOnum
        entityMap.put("alefsym", new Character('\u2135')); // alef symbol = first transfinite cardinal, U+2135 NEW
        // alef symbol is NOT the same as hebrew letter alef,
        // U+05D0 although the same glyph could be used to depict both characters
        // Arrows
        entityMap.put("larr", new Character('\u2190')); // leftwards arrow, U+2190 ISOnum
        entityMap.put("uarr", new Character('\u2191')); // upwards arrow, U+2191 ISOnum
        entityMap.put("rarr", new Character('\u2192')); // rightwards arrow, U+2192 ISOnum
        entityMap.put("darr", new Character('\u2193')); // downwards arrow, U+2193 ISOnum
        entityMap.put("harr", new Character('\u2194')); // left right arrow, U+2194 ISOamsa
        entityMap.put("crarr", new Character('\u21b5')); // downwards arrow with corner leftwards = carriage return, U+21B5 NEW
        entityMap.put("lArr", new Character('\u21d0')); // leftwards double arrow, U+21D0 ISOtech
        // ISO 10646 does not say that lArr is the same as the 'is implied by' arrow
        // but also does not have any other character for that function. So ? lArr can
        // be used for 'is implied by' as ISOtech suggests
        entityMap.put("uArr", new Character('\u21d1')); // upwards double arrow, U+21D1 ISOamsa
        entityMap.put("rArr", new Character('\u21d2')); // rightwards double arrow, U+21D2 ISOtech
        // ISO 10646 does not say this is the 'implies' character but does not have 
        // another character with this function so ?
        // rArr can be used for 'implies' as ISOtech suggests
        entityMap.put("dArr", new Character('\u21d3')); // downwards double arrow, U+21D3 ISOamsa
        entityMap.put("hArr", new Character('\u21d4')); // left right double arrow, U+21D4 ISOamsa
        // Mathematical Operators
        entityMap.put("forall", new Character('\u2200')); // for all, U+2200 ISOtech
        entityMap.put("part", new Character('\u2202')); // partial differential, U+2202 ISOtech
        entityMap.put("exist", new Character('\u2203')); // there exists, U+2203 ISOtech
        entityMap.put("empty", new Character('\u2205')); // empty set = null set = diameter, U+2205 ISOamso
        entityMap.put("nabla", new Character('\u2207')); // nabla = backward difference, U+2207 ISOtech
        entityMap.put("isin", new Character('\u2208')); // element of, U+2208 ISOtech
        entityMap.put("notin", new Character('\u2209')); // not an element of, U+2209 ISOtech
        entityMap.put("ni", new Character('\u220b')); // contains as member, U+220B ISOtech
        // should there be a more memorable name than 'ni'?
        entityMap.put("prod", new Character('\u220f')); // n-ary product = product sign, U+220F ISOamsb
        // prod is NOT the same character as U+03A0 'greek capital letter pi' though
        // the same glyph might be used for both
        entityMap.put("sum", new Character('\u2211')); // n-ary sumation, U+2211 ISOamsb
        // sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both
        entityMap.put("minus", new Character('\u2212')); // minus sign, U+2212 ISOtech
        entityMap.put("lowast", new Character('\u2217')); // asterisk operator, U+2217 ISOtech
        entityMap.put("radic", new Character('\u221a')); // square root = radical sign, U+221A ISOtech
        entityMap.put("prop", new Character('\u221d')); // proportional to, U+221D ISOtech
        entityMap.put("infin", new Character('\u221e')); // infinity, U+221E ISOtech
        entityMap.put("ang", new Character('\u2220')); // angle, U+2220 ISOamso
        entityMap.put("and", new Character('\u2227')); // logical and = wedge, U+2227 ISOtech
        entityMap.put("or", new Character('\u2228')); // logical or = vee, U+2228 ISOtech
        entityMap.put("cap", new Character('\u2229')); // intersection = cap, U+2229 ISOtech
        entityMap.put("cup", new Character('\u222a')); // union = cup, U+222A ISOtech
        entityMap.put("int", new Character('\u222b')); // integral, U+222B ISOtech
        entityMap.put("there4", new Character('\u2234')); // therefore, U+2234 ISOtech
        entityMap.put("sim", new Character('\u223c')); // tilde operator = varies with = similar to, U+223C ISOtech
        // tilde operator is NOT the same character as the tilde, U+007E,
        // although the same glyph might be used to represent both
        entityMap.put("cong", new Character('\u2245')); // approximately equal to, U+2245 ISOtech
        entityMap.put("asymp", new Character('\u2248')); // almost equal to = asymptotic to, U+2248 ISOamsr
        entityMap.put("ne", new Character('\u2260')); // not equal to, U+2260 ISOtech
        entityMap.put("equiv", new Character('\u2261')); // identical to, U+2261 ISOtech
        entityMap.put("le", new Character('\u2264')); // less-than or equal to, U+2264 ISOtech
        entityMap.put("ge", new Character('\u2265')); // greater-than or equal to, U+2265 ISOtech
        entityMap.put("sub", new Character('\u2282')); // subset of, U+2282 ISOtech
        entityMap.put("sup", new Character('\u2283')); // superset of, U+2283 ISOtech
        // note that nsup, 'not a superset of, U+2283' is not covered by the Symbol 
        // font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn
        entityMap.put("nsub", new Character('\u2284')); // not a subset of, U+2284 ISOamsn
        entityMap.put("sube", new Character('\u2286')); // subset of or equal to, U+2286 ISOtech
        entityMap.put("supe", new Character('\u2287')); // superset of or equal to, U+2287 ISOtech
        entityMap.put("oplus", new Character('\u2295')); // circled plus = direct sum, U+2295 ISOamsb
        entityMap.put("otimes", new Character('\u2297')); // circled times = vector product, U+2297 ISOamsb
        entityMap.put("perp", new Character('\u22a5')); // up tack = orthogonal to = perpendicular, U+22A5 ISOtech
        entityMap.put("sdot", new Character('\u22c5')); // dot operator, U+22C5 ISOamsb
        // dot operator is NOT the same character as U+00B7 middle dot
        // Miscellaneous Technical
        entityMap.put("lceil", new Character('\u2308')); // left ceiling = apl upstile, U+2308 ISOamsc
        entityMap.put("rceil", new Character('\u2309')); // right ceiling, U+2309 ISOamsc
        entityMap.put("lfloor", new Character('\u230a')); // left floor = apl downstile, U+230A ISOamsc
        entityMap.put("rfloor", new Character('\u230b')); // right floor, U+230B ISOamsc
        entityMap.put("lang", new Character('\u2329')); // left-pointing angle bracket = bra, U+2329 ISOtech
        // lang is NOT the same character as U+003C 'less than' 
        // or U+2039 'single left-pointing angle quotation mark'
        entityMap.put("rang", new Character('\u232a')); // right-pointing angle bracket = ket, U+232A ISOtech
        // rang is NOT the same character as U+003E 'greater than' 
        // or U+203A 'single right-pointing angle quotation mark'
        // Geometric Shapes
        entityMap.put("loz", new Character('\u25ca')); // lozenge, U+25CA ISOpub
        // Miscellaneous Symbols
        entityMap.put("spades", new Character('\u2660')); // black spade suit, U+2660 ISOpub
        // black here seems to mean filled as opposed to hollow
        entityMap.put("clubs", new Character('\u2663')); // black club suit = shamrock, U+2663 ISOpub
        entityMap.put("hearts", new Character('\u2665')); // black heart suit = valentine, U+2665 ISOpub
        entityMap.put("diams", new Character('\u2666')); // black diamond suit, U+2666 ISOpub
        // C0 Controls and Basic Latin
        entityMap.put("quot", new Character('\u0022')); // quotation mark = APL quote, U+0022 ISOnum
        entityMap.put("amp", new Character('\u0026')); // ampersand, U+0026 ISOnum
        entityMap.put("apos", new Character('\''));
        entityMap.put("lt", new Character('\u003c')); // less-than sign, U+003C ISOnum
        entityMap.put("gt", new Character('\u003e')); // greater-than sign, U+003E ISOnum
        // Latin Extended-A
        entityMap.put("OElig", new Character('\u0152')); // latin capital ligature OE, U+0152 ISOlat2
        entityMap.put("oelig", new Character('\u0153')); // latin small ligature oe, U+0153 ISOlat2
        // ligature is a misnomer, this is a separate character in some languages
        entityMap.put("Scaron", new Character('\u0160')); // latin capital letter S with caron, U+0160 ISOlat2
        entityMap.put("scaron", new Character('\u0161')); // latin small letter s with caron, U+0161 ISOlat2
        entityMap.put("Yuml", new Character('\u0178')); // latin capital letter Y with diaeresis, U+0178 ISOlat2
        // Spacing Modifier Letters
        entityMap.put("circ", new Character('\u02c6')); // modifier letter circumflex accent, U+02C6 ISOpub
        entityMap.put("tilde", new Character('\u02dc')); // small tilde, U+02DC ISOdia
        // General Punctuation
        entityMap.put("ensp", new Character('\u2002')); // en space, U+2002 ISOpub
        entityMap.put("emsp", new Character('\u2003')); // em space, U+2003 ISOpub
        entityMap.put("thinsp", new Character('\u2009')); // thin space, U+2009 ISOpub
        entityMap.put("zwnj", new Character('\u200c')); // zero width non-joiner, U+200C NEW RFC 2070
        entityMap.put("zwj", new Character('\u200d')); // zero width joiner, U+200D NEW RFC 2070
        entityMap.put("lrm", new Character('\u200e')); // left-to-right mark, U+200E NEW RFC 2070
        entityMap.put("rlm", new Character('\u200f')); // right-to-left mark, U+200F NEW RFC 2070
        entityMap.put("ndash", new Character('\u2013')); // en dash, U+2013 ISOpub
        entityMap.put("mdash", new Character('\u2014')); // em dash, U+2014 ISOpub
        entityMap.put("lsquo", new Character('\u2018')); // left single quotation mark, U+2018 ISOnum
        entityMap.put("rsquo", new Character('\u2019')); // right single quotation mark, U+2019 ISOnum
        entityMap.put("sbquo", new Character('\u201a')); // single low-9 quotation mark, U+201A NEW
        entityMap.put("ldquo", new Character('\u201c')); // left double quotation mark, U+201C ISOnum
        entityMap.put("rdquo", new Character('\u201d')); // right double quotation mark, U+201D ISOnum
        entityMap.put("bdquo", new Character('\u201e')); // double low-9 quotation mark, U+201E NEW
        entityMap.put("dagger", new Character('\u2020')); // dagger, U+2020 ISOpub
        entityMap.put("Dagger", new Character('\u2021')); // double dagger, U+2021 ISOpub
        entityMap.put("permil", new Character('\u2030')); // per mille sign, U+2030 ISOtech
        entityMap.put("lsaquo", new Character('\u2039')); // single left-pointing angle quotation mark, U+2039 ISO proposed
        // lsaquo is proposed but not yet ISO standardized
        entityMap.put("rsaquo", new Character('\u203a')); // single right-pointing angle quotation mark, U+203A ISO proposed
        // rsaquo is proposed but not yet ISO standardized
        entityMap.put("euro", new Character('\u20ac')); // euro sign, U+20AC NEW
    
    
    }
}