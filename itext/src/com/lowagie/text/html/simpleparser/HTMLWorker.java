/*
 * Copyright 2004 Paulo Soares
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

package com.lowagie.text.html.simpleparser;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.Reader;

public class HTMLWorker implements SimpleXMLDocHandler, DocListener {
    
    public ArrayList objectList;
    public DocListener document;
    public Paragraph currentParagraph;
    public ChainedProperties cprops = new ChainedProperties();
    public Stack stack = new Stack();
    public boolean pendingP = false;
    public boolean pendingTR = false;
    public boolean pendingTD = false;
    public boolean pendingLI = false;
    public StyleSheet style = new StyleSheet();
    public boolean isPRE = false;
    public Stack tableState = new Stack();
    public boolean skipText = false;
    
    /** Creates a new instance of HTMLWorker */
    public HTMLWorker(DocListener document) {
        this.document = document;
    }
    
    public void setStyleSheet(StyleSheet style) {
        this.style = style;
    }
    
    public void parse(Reader reader) throws IOException {
        SimpleXMLParser.parse(this, null, reader, true);
    }
    
    public static ArrayList parseToList(Reader reader, StyleSheet style) throws IOException {
        HTMLWorker worker = new HTMLWorker(null);
        if (style != null)
            worker.style = style;
        worker.document = worker;
        worker.objectList = new ArrayList();
        worker.parse(reader);
        return worker.objectList;
    }
    
    public void endDocument() {
        try {
            for (int k = 0; k < stack.size(); ++k)
                document.add((Element)stack.elementAt(k));
            if (currentParagraph != null)
                document.add(currentParagraph);
            currentParagraph = null;
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    public void startDocument() {
        HashMap h = new HashMap();
        style.applyStyle("body", h);
        cprops.addToChain("body", h);
    }
    
    public void startElement(String tag, HashMap h) {
        if (!tagsSupported.containsKey(tag))
            return;
        style.applyStyle(tag, h);
        String follow = (String)FactoryProperties.followTags.get(tag);
        if (follow != null) {
            HashMap prop = new HashMap();
            prop.put(follow, null);
            cprops.addToChain(follow, prop);
            return;
        }
        if (tag.equals("a")) {
            cprops.addToChain(tag, h);
            if (currentParagraph == null)
                currentParagraph = new Paragraph();
            stack.push(currentParagraph);
            currentParagraph = new Paragraph();
            return;
        }
        if (tag.equals("br")) {
            if (currentParagraph == null)
                currentParagraph = new Paragraph();
            currentParagraph.add(FactoryProperties.createChunk("\n", cprops));
            return;
        }
        if (tag.equals("font") || tag.equals("span")) {
            cprops.addToChain(tag, h);
            return;
        }
        endElement("p");
        if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3") || tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
            if (!h.containsKey("size"))
                h.put("size", tag.substring(1));
            cprops.addToChain(tag, h);
            return;
        }
        if (tag.equals("ul")) {
            if (pendingLI)
                endElement("li");
            skipText = true;
            cprops.addToChain(tag, h);
            com.lowagie.text.List list = new com.lowagie.text.List(false, 10);
            list.setListSymbol("\u2022");
            stack.push(list);
            return;
        }
        if (tag.equals("ol")) {
            if (pendingLI)
                endElement("li");
            skipText = true;
            cprops.addToChain(tag, h);
            com.lowagie.text.List list = new com.lowagie.text.List(true, 10);
            stack.push(list);
            return;
        }
        if (tag.equals("li")) {
            if (pendingLI)
                endElement("li");
            skipText = false;
            pendingLI = true;
            cprops.addToChain(tag, h);
            stack.push(new com.lowagie.text.ListItem());
            return;
        }
        if (tag.equals("div") || tag.equals("body")) {
            cprops.addToChain(tag, h);
            return;
        }
        if (tag.equals("pre")) {
            if (!h.containsKey("face")) {
                h.put("face", "Courier");
            }
            cprops.addToChain(tag, h);
            isPRE = true;
            return;
        }
        if (tag.equals("p")) {
            cprops.addToChain(tag, h);
            currentParagraph = FactoryProperties.createParagraph(h);
            return;
        }
        if (tag.equals("tr")) {
            if (pendingTR)
                endElement("tr");
            skipText = true;
            pendingTR = true;
            cprops.addToChain("tr", h);
            return;
        }
        if (tag.equals("td") || tag.equals("th")) {
            if (pendingTD)
                endElement(tag);
            skipText = false;
            pendingTD = true;
            cprops.addToChain("td", h);
            stack.push(new IncCell(tag, cprops));
            return;
        }
        if (tag.equals("table")) {
            cprops.addToChain("table", h);
            IncTable table = new IncTable(h);
            stack.push(table);
            tableState.push(new boolean[]{pendingTR, pendingTD});
            pendingTR = pendingTD = false;
            skipText = true;
            return;
        }
    }
    
    public void endElement(String tag) {
        if (!tagsSupported.containsKey(tag))
            return;
        try {
            String follow = (String)FactoryProperties.followTags.get(tag);
            if (follow != null) {
                cprops.removeChain(follow);
                return;
            }
            if (tag.equals("font") || tag.equals("span")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals("a")) {
                String href = cprops.getProperty("href");
                if (currentParagraph == null)
                    currentParagraph = new Paragraph();
                if (href != null) {
                    ArrayList chunks = currentParagraph.getChunks();
                    for (int k = 0; k < chunks.size(); ++k) {
                        Chunk ck = (Chunk)chunks.get(k);
                        ck.setAnchor(href);
                    }
                }
                Paragraph tmp = (Paragraph)stack.pop();
                Phrase tmp2 = new Phrase();
                tmp2.add(currentParagraph);
                tmp.add(tmp2);
                currentParagraph = tmp;
                cprops.removeChain("a");
                return;
            }
            if (tag.equals("br")) {
                return;
            }
            if (currentParagraph != null) {
                if (stack.empty())
                    document.add(currentParagraph);
                else {
                    Object obj = stack.pop();;
                    if (obj instanceof TextElementArray) {
                        TextElementArray current = (TextElementArray)obj;
                        current.add(currentParagraph);
                    }
                    stack.push(obj);
                }
            }
            currentParagraph = null;
            if (tag.equals("ul") || tag.equals("ol")) {
                if (pendingLI)
                    endElement("li");
                skipText = false;
                cprops.removeChain(tag);
                if (stack.empty())
                    return;
                Object obj = stack.pop();
                if (!(obj instanceof com.lowagie.text.List)) {
                    stack.push(obj);
                    return;
                }
                if (stack.empty())
                    document.add((Element)obj);
                else
                    ((TextElementArray)stack.peek()).add(obj);
                return;
            }
            if (tag.equals("li")) {
                pendingLI = false;
                skipText = true;
                cprops.removeChain(tag);
                if (stack.empty())
                    return;
                Object obj = stack.pop();
                if (!(obj instanceof ListItem)) {
                    stack.push(obj);
                    return;
                }
                if (stack.empty()) {
                    document.add((Element)obj);
                    return;
                }
                Object list = stack.pop();
                if (!(list instanceof com.lowagie.text.List)) {
                    stack.push(list);
                    return;
                }
                ListItem item = (ListItem)obj;
                ((com.lowagie.text.List)list).add(item);
                ArrayList cks = item.getChunks();
                if (cks.size() > 0)
                    item.listSymbol().setFont(((Chunk)cks.get(0)).font());
                stack.push(list);
                return;
            }
            if (tag.equals("div") || tag.equals("body")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals("pre")) {
                cprops.removeChain(tag);
                isPRE = false;
                return;
            }
            if (tag.equals("p")) {
                cprops.removeChain(tag);
                pendingP = false;
                return;
            }
            if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3") || tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
                cprops.removeChain(tag);
                return;
            }
            if (tag.equals("table")) {
                if (pendingTR)
                    endElement("tr");
                cprops.removeChain("table");
                IncTable table = (IncTable) stack.pop();
                PdfPTable tb = table.buildTable();
                tb.setSplitRows(true);
                if (stack.empty())
                    document.add(tb);
                else
                    ((TextElementArray)stack.peek()).add(tb);
                boolean state[] = (boolean[])tableState.pop();
                pendingTR = state[0];
                pendingTD = state[1];
                skipText = false;
                return;
            }
            if (tag.equals("tr")) {
                if (pendingTD)
                    endElement("td");
                pendingTR = false;
                cprops.removeChain("tr");
                ArrayList cells = new ArrayList();
                IncTable table = null;
                while (true) {
                    Object obj = stack.pop();
                    if (obj instanceof IncCell) {
                        cells.add(((IncCell)obj).getCell());
                    }
                    if (obj instanceof IncTable) {
                        table = (IncTable)obj;
                        break;
                    }
                }
                table.addCols(cells);
                table.endRow();
                stack.push(table);
                skipText = true;
                return;
            }
            if (tag.equals("td") || tag.equals("th")) {
                pendingTD = false;
                cprops.removeChain("td");
                skipText = true;
                return;
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    public void text(String str) {
        if (skipText)
            return;
        String content = str;
        if (isPRE) {
            if (currentParagraph == null)
                currentParagraph = new Paragraph();
            currentParagraph.add(FactoryProperties.createChunk(content, cprops));
            return;
        }
        if (content.trim().length() == 0 && content.indexOf(' ') < 0) {
            return;
        }
        
        StringBuffer buf = new StringBuffer();
        int len = content.length();
        char character;
        boolean newline = false;
        for (int i = 0; i < len; i++) {
            switch(character = content.charAt(i)) {
                case ' ':
                    if (!newline) {
                        buf.append(character);
                    }
                    break;
                case '\n':
                    if (i > 0) {
                        newline = true;
                        buf.append(' ');
                    }
                    break;
                case '\r':
                    break;
                case '\t':
                    break;
                    default:
                        newline = false;
                        buf.append(character);
            }
        }
        if (currentParagraph == null)
            currentParagraph = FactoryProperties.createParagraph(cprops);
        currentParagraph.add(FactoryProperties.createChunk(buf.toString(), cprops));
    }
    
    public boolean add(Element element) throws DocumentException {
        objectList.add(element);
        return true;
    }
    
    public boolean add(Watermark watermark) {
        return true;
    }
    
    public void clearTextWrap() throws DocumentException {
    }
    
    public void close() {
    }
    
    public boolean newPage() throws DocumentException {
        return true;
    }
    
    public void open() {
    }
    
    public void removeWatermark() {
    }
    
    public void resetFooter() {
    }
    
    public void resetHeader() {
    }
    
    public void resetPageCount() {
    }
    
    public void setFooter(HeaderFooter footer) {
    }
    
    public void setHeader(HeaderFooter header) {
    }
    
    public boolean setMarginMirroring(boolean marginMirroring) {
        return true;
    }
    
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        return true;
    }
    
    public void setPageCount(int pageN) {
    }
    
    public boolean setPageSize(Rectangle pageSize) {
        return true;
    }
    
    public static final String tagsSupportedString = "ol ul li a pre font span br p div body table td th tr i b u sub sup em strong"
        + " h1 h2 h3 h4 h5 h6";
    
    public static final HashMap tagsSupported = new HashMap();
    
    static {
        StringTokenizer tok = new StringTokenizer(tagsSupportedString);
        while (tok.hasMoreTokens())
            tagsSupported.put(tok.nextToken(), null);
    }
}