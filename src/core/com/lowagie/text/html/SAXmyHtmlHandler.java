/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.lowagie.text.html;

import java.util.HashMap;
import java.util.Properties;

import org.xml.sax.Attributes;

import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.xml.SAXiTextHandler;
import com.lowagie.text.xml.XmlPeer;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 */

public class SAXmyHtmlHandler extends SAXiTextHandler // SAXmyHandler
{

    /** These are the properties of the body section. */
    private Properties bodyAttributes = new Properties();

    /** This is the status of the table border. */
    private boolean tableBorder = false;

    /**
     * Constructs a new SAXiTextHandler that will translate all the events
     * triggered by the parser to actions on the <CODE>Document</CODE>-object.
     * 
     * @param document
     *            this is the document on which events must be triggered
     */

    public SAXmyHtmlHandler(DocListener document) {
        super(document, new HtmlTagMap());
    }
    /**
     * Constructs a new SAXiTextHandler that will translate all the events
     * triggered by the parser to actions on the <CODE>Document</CODE>-object.
     * 
     * @param document
     *            this is the document on which events must be triggered
     * @param bf
     */

    public SAXmyHtmlHandler(DocListener document, BaseFont bf) {
        super(document, new HtmlTagMap(), bf);
    }

    /**
     * Constructs a new SAXiTextHandler that will translate all the events
     * triggered by the parser to actions on the <CODE>Document</CODE>-object.
     * 
     * @param document
     *            this is the document on which events must be triggered
     * @param htmlTags
     *            a tagmap translating HTML tags to iText tags
     */

    public SAXmyHtmlHandler(DocListener document, HashMap htmlTags) {
        super(document, htmlTags);
    }

    /**
     * This method gets called when a start tag is encountered.
     * 
     * @param uri
     *            the Uniform Resource Identifier
     * @param lname
     *            the local name (without prefix), or the empty string if
     *            Namespace processing is not being performed.
     * @param name
     *            the name of the tag that is encountered
     * @param attrs
     *            the list of attributes
     */

    public void startElement(String uri, String lname, String name,
            Attributes attrs) {
        // System.err.println("Start: " + name);

        // super.handleStartingTags is replaced with handleStartingTags
        // suggestion by Vu Ngoc Tan/Hop
    	name = name.toLowerCase();
        if (HtmlTagMap.isHtml(name)) {
            // we do nothing
            return;
        }
        if (HtmlTagMap.isHead(name)) {
            // we do nothing
            return;
        }
        if (HtmlTagMap.isTitle(name)) {
            // we do nothing
            return;
        }
        if (HtmlTagMap.isMeta(name)) {
            // we look if we can change the body attributes
            String meta = null;
            String content = null;
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String attribute = attrs.getQName(i);
                    if (attribute.equalsIgnoreCase(HtmlTags.CONTENT))
                        content = attrs.getValue(i);
                    else if (attribute.equalsIgnoreCase(HtmlTags.NAME))
                        meta = attrs.getValue(i);
                }
            }
            if (meta != null && content != null) {
                bodyAttributes.put(meta, content);
            }
            return;
        }
        if (HtmlTagMap.isLink(name)) {
            // we do nothing for the moment, in a later version we could extract
            // the style sheet
            return;
        }
        if (HtmlTagMap.isBody(name)) {
            // maybe we could extract some info about the document: color,
            // margins,...
            // but that's for a later version...
            XmlPeer peer = new XmlPeer(ElementTags.ITEXT, name);
            peer.addAlias(ElementTags.TOP, HtmlTags.TOPMARGIN);
            peer.addAlias(ElementTags.BOTTOM, HtmlTags.BOTTOMMARGIN);
            peer.addAlias(ElementTags.RIGHT, HtmlTags.RIGHTMARGIN);
            peer.addAlias(ElementTags.LEFT, HtmlTags.LEFTMARGIN);
            bodyAttributes.putAll(peer.getAttributes(attrs));
            handleStartingTags(peer.getTag(), bodyAttributes);
            return;
        }
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            if (ElementTags.TABLE.equals(peer.getTag()) || ElementTags.CELL.equals(peer.getTag())) {
                Properties p = peer.getAttributes(attrs);
                String value;
                if (ElementTags.TABLE.equals(peer.getTag())
                        && (value = p.getProperty(ElementTags.BORDERWIDTH)) != null) {
                    if (Float.parseFloat(value + "f") > 0) {
                        tableBorder = true;
                    }
                }
                if (tableBorder) {
                    p.put(ElementTags.LEFT, String.valueOf(true));
                    p.put(ElementTags.RIGHT, String.valueOf(true));
                    p.put(ElementTags.TOP, String.valueOf(true));
                    p.put(ElementTags.BOTTOM, String.valueOf(true));
                }
                handleStartingTags(peer.getTag(), p);
                return;
            }
            handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
            return;
        }
        Properties attributes = new Properties();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String attribute = attrs.getQName(i).toLowerCase();
                attributes.setProperty(attribute, attrs.getValue(i).toLowerCase());
            }
        }
        handleStartingTags(name, attributes);
    }

    /**
     * This method gets called when an end tag is encountered.
     * 
     * @param uri
     *            the Uniform Resource Identifier
     * @param lname
     *            the local name (without prefix), or the empty string if
     *            Namespace processing is not being performed.
     * @param name
     *            the name of the tag that ends
     */

    public void endElement(String uri, String lname, String name) {
        // System.err.println("End: " + name);
    	name = name.toLowerCase();
        if (ElementTags.PARAGRAPH.equals(name)) {
            try {
                document.add((Element) stack.pop());
                return;
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
        if (HtmlTagMap.isHead(name)) {
            // we do nothing
            return;
        }
        if (HtmlTagMap.isTitle(name)) {
            if (currentChunk != null) {
                bodyAttributes.put(ElementTags.TITLE, currentChunk.getContent());
            }
            return;
        }
        if (HtmlTagMap.isMeta(name)) {
            // we do nothing
            return;
        }
        if (HtmlTagMap.isLink(name)) {
            // we do nothing
            return;
        }
        if (HtmlTagMap.isBody(name)) {
            // we do nothing
            return;
        }
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            if (ElementTags.TABLE.equals(peer.getTag())) {
                tableBorder = false;
            }
            super.handleEndingTags(peer.getTag());
            return;
        }
        // super.handleEndingTags is replaced with handleEndingTags
        // suggestion by Ken Auer
        handleEndingTags(name);
    }
}