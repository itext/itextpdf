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
 * A simple XML parser.  This parser is, like the SAX parser,
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
    
    /**
     * Parses the XML document firing the events to the handler.
     * @param doc the document handler
     * @param r the document. The encoding is already resolved. The reader is not closed
     * @throws IOException on error
     */
    public static void parse(SimpleXMLDocHandler doc,Reader r) throws IOException {
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
        while((c = r.read()) != -1) {
            
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
                    sb.setLength(0);
                    depth--;
                    if(depth==0)
                        mode = DONE;
                    doc.endElement(tagName);
                } else {
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
                    if(cent.equals("lt"))
                        sb.append('<');
                    else if(cent.equals("gt"))
                        sb.append('>');
                    else if(cent.equals("amp"))
                        sb.append('&');
                    else if(cent.equals("quot"))
                        sb.append('"');
                    else if(cent.equals("apos"))
                        sb.append('\'');
                    else if(cent.startsWith("#x"))
                        sb.append((char)Integer.parseInt(cent.substring(2),16));
                    else if(cent.startsWith("#"))
                        sb.append((char)Integer.parseInt(cent.substring(1)));
                    // Insert custom entity definitions here
                    else
                        exc("Unknown entity: &"+cent+";",line,col);
                } else {
                    etag.append((char)c);
                }
                
                // we have just seen something like this:
                // <foo a="b"/
                // and are looking for the final >.
            } else if(mode == SINGLE_TAG) {
                if(tagName == null)
                    tagName = sb.toString();
                if(c != '>')
                    exc("Expected > for tag: <"+tagName+"/>",line,col);
                doc.startElement(tagName,attrs);
                doc.endElement(tagName);
                if(depth==0) {
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
                } else if(c == '[' && sb.toString().equals("![CDATA")) {
                    mode = CDATA;
                    sb.setLength(0);
                } else if(c == 'E' && sb.toString().equals("!DOCTYP")) {
                    sb.setLength(0);
                    mode = DOCTYPE;
                } else if(Character.isWhitespace((char)c)) {
                    tagName = sb.toString();
                    sb.setLength(0);
                    mode = IN_TAG;
                } else {
                    sb.append((char)c);
                }
                
                // We are processing the quoted right-hand side
                // of an element's attribute.
            } else if(mode == QUOTE) {
                if(c == quotec) {
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
                } else {
                    exc("Error in attribute processing",line,col);
                }
                
            } else if(mode == ATTRIBUTE_LVALUE) {
                if(Character.isWhitespace((char)c)) {
                    lvalue = sb.toString();
                    sb.setLength(0);
                    mode = ATTRIBUTE_EQUAL;
                } else if(c == '=') {
                    lvalue = sb.toString();
                    sb.setLength(0);
                    mode = ATTRIBUTE_RVALUE;
                } else {
                    sb.append((char)c);
                }
                
            } else if(mode == ATTRIBUTE_EQUAL) {
                if(c == '=') {
                    mode = ATTRIBUTE_RVALUE;
                } else if(Character.isWhitespace((char)c)) {
                    ;
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
        if(mode == DONE)
            doc.endDocument();
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
    }
}
