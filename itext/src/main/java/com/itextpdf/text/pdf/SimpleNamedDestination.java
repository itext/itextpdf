/*
 * $Id: SimpleNamedDestination.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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
package com.itextpdf.text.pdf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.xml.simpleparser.IanaEncodings;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import com.itextpdf.text.xml.XMLUtil;

/**
 *
 * @author Paulo Soares
 */
public final class SimpleNamedDestination implements SimpleXMLDocHandler {

    private HashMap<String, String> xmlNames;
    private HashMap<String, String> xmlLast;

    private SimpleNamedDestination() {
    }

    public static HashMap<String, String> getNamedDestination(PdfReader reader, boolean fromNames) {
        IntHashtable pages = new IntHashtable();
        int numPages = reader.getNumberOfPages();
        for (int k = 1; k <= numPages; ++k)
            pages.put(reader.getPageOrigRef(k).getNumber(), k);
        HashMap<String, PdfObject> names = fromNames ? reader.getNamedDestinationFromNames() : reader.getNamedDestinationFromStrings();
        HashMap<String, String> n2 = new HashMap<String, String>(names.size());
        for (Map.Entry<String, PdfObject> entry: names.entrySet()) {
            PdfArray arr = (PdfArray)entry.getValue();
            StringBuffer s = new StringBuffer();
            try {
                s.append(pages.get(arr.getAsIndirectObject(0).getNumber()));
                s.append(' ').append(arr.getPdfObject(1).toString().substring(1));
                for (int k = 2; k < arr.size(); ++k)
                    s.append(' ').append(arr.getPdfObject(k).toString());
                n2.put(entry.getKey(), s.toString());
            }
            catch (Exception e) {
            }
        }
        return n2;
    }

    /**
     * Exports the destinations to XML. The DTD for this XML is:
     * <p>
     * <pre>
     * &lt;?xml version='1.0' encoding='UTF-8'?&gt;
     * &lt;!ELEMENT Name (#PCDATA)&gt;
     * &lt;!ATTLIST Name
     *    Page CDATA #IMPLIED
     * &gt;
     * &lt;!ELEMENT Destination (Name)*&gt;
     * </pre>
     * @param names the names
     * @param out the export destination. The stream is not closed
     * @param encoding the encoding according to IANA conventions
     * @param onlyASCII codes above 127 will always be escaped with &amp;#nn; if <CODE>true</CODE>,
     * whatever the encoding
     * @throws IOException on error
     * @since 5.0.1 (generic type in signature)
     */
    public static void exportToXML(HashMap<String, String> names, OutputStream out, String encoding, boolean onlyASCII) throws IOException {
        String jenc = IanaEncodings.getJavaEncoding(encoding);
        Writer wrt = new BufferedWriter(new OutputStreamWriter(out, jenc));
        exportToXML(names, wrt, encoding, onlyASCII);
    }

    /**
     * Exports the destinations to XML.
     * @param names the names
     * @param wrt the export destination. The writer is not closed
     * @param encoding the encoding according to IANA conventions
     * @param onlyASCII codes above 127 will always be escaped with &amp;#nn; if <CODE>true</CODE>,
     * whatever the encoding
     * @throws IOException on error
     * @since 5.0.1 (generic type in signature)
     */
    public static void exportToXML(HashMap<String, String> names, Writer wrt, String encoding, boolean onlyASCII) throws IOException {
        wrt.write("<?xml version=\"1.0\" encoding=\"");
        wrt.write(XMLUtil.escapeXML(encoding, onlyASCII));
        wrt.write("\"?>\n<Destination>\n");
        for (Map.Entry<String, String> entry: names.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            wrt.write("  <Name Page=\"");
            wrt.write(XMLUtil.escapeXML(value, onlyASCII));
            wrt.write("\">");
            wrt.write(XMLUtil.escapeXML(escapeBinaryString(key), onlyASCII));
            wrt.write("</Name>\n");
        }
        wrt.write("</Destination>\n");
        wrt.flush();
    }

    /**
     * Import the names from XML.
     * @param in the XML source. The stream is not closed
     * @throws IOException on error
     * @return the names
     */
    public static HashMap<String, String> importFromXML(InputStream in) throws IOException {
        SimpleNamedDestination names = new SimpleNamedDestination();
        SimpleXMLParser.parse(names, in);
        return names.xmlNames;
    }

    /**
     * Import the names from XML.
     * @param in the XML source. The reader is not closed
     * @throws IOException on error
     * @return the names
     */
    public static HashMap<String, String> importFromXML(Reader in) throws IOException {
        SimpleNamedDestination names = new SimpleNamedDestination();
        SimpleXMLParser.parse(names, in);
        return names.xmlNames;
    }

    static PdfArray createDestinationArray(String value, PdfWriter writer) {
        PdfArray ar = new PdfArray();
        StringTokenizer tk = new StringTokenizer(value);
        int n = Integer.parseInt(tk.nextToken());
        ar.add(writer.getPageReference(n));
        if (!tk.hasMoreTokens()) {
            ar.add(PdfName.XYZ);
            ar.add(new float[]{0, 10000, 0});
        }
        else {
            String fn = tk.nextToken();
            if (fn.startsWith("/"))
                fn = fn.substring(1);
            ar.add(new PdfName(fn));
            for (int k = 0; k < 4 && tk.hasMoreTokens(); ++k) {
                fn = tk.nextToken();
                if (fn.equals("null"))
                    ar.add(PdfNull.PDFNULL);
                else
                    ar.add(new PdfNumber(fn));
            }
        }
        return ar;
    }

    public static PdfDictionary outputNamedDestinationAsNames(HashMap<String, String> names, PdfWriter writer) {
        PdfDictionary dic = new PdfDictionary();
        for (Map.Entry<String, String> entry: names.entrySet()) {
            try {
                String key = entry.getKey();
                String value = entry.getValue();
                PdfArray ar = createDestinationArray(value, writer);
                PdfName kn = new PdfName(key);
                dic.put(kn, ar);
            }
            catch (Exception e) {
                // empty on purpose
            }
        }
        return dic;
    }

    public static PdfDictionary outputNamedDestinationAsStrings(HashMap<String, String> names, PdfWriter writer) throws IOException {
        HashMap<String, PdfObject> n2 = new HashMap<String, PdfObject>(names.size());
        for (Map.Entry<String, String> entry: names.entrySet()) {
            try {
                String value = entry.getValue();
                PdfArray ar = createDestinationArray(value, writer);
                n2.put(entry.getKey(), writer.addToBody(ar).getIndirectReference());
            }
            catch (Exception e) {
            }
        }
        return PdfNameTree.writeTree(n2, writer);
    }

    public static String escapeBinaryString(String s) {
        StringBuffer buf = new StringBuffer();
        char cc[] = s.toCharArray();
        int len = cc.length;
        for (int k = 0; k < len; ++k) {
            char c = cc[k];
            if (c < ' ') {
                buf.append('\\');
                String octal = "00" + Integer.toOctalString(c);
                buf.append(octal.substring(octal.length() - 3));
            }
            else if (c == '\\')
                buf.append("\\\\");
            else
                buf.append(c);
        }
        return buf.toString();
    }

    public static String unEscapeBinaryString(String s) {
        StringBuffer buf = new StringBuffer();
        char cc[] = s.toCharArray();
        int len = cc.length;
        for (int k = 0; k < len; ++k) {
            char c = cc[k];
            if (c == '\\') {
                if (++k >= len) {
                    buf.append('\\');
                    break;
                }
                c = cc[k];
                if (c >= '0' && c <= '7') {
                    int n = c - '0';
                    ++k;
                    for (int j = 0; j < 2 && k < len; ++j) {
                        c = cc[k];
                        if (c >= '0' && c <= '7') {
                            ++k;
                            n = n * 8 + c - '0';
                        }
                        else {
                            break;
                        }
                    }
                    --k;
                    buf.append((char)n);
                }
                else
                    buf.append(c);
            }
            else
                buf.append(c);
        }
        return buf.toString();
    }

    public void endDocument() {
    }

    public void endElement(String tag) {
        if (tag.equals("Destination")) {
            if (xmlLast == null && xmlNames != null)
                return;
            else
                throw new RuntimeException(MessageLocalization.getComposedMessage("destination.end.tag.out.of.place"));
        }
        if (!tag.equals("Name"))
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.end.tag.1", tag));
        if (xmlLast == null || xmlNames == null)
            throw new RuntimeException(MessageLocalization.getComposedMessage("name.end.tag.out.of.place"));
        if (!xmlLast.containsKey("Page"))
            throw new RuntimeException(MessageLocalization.getComposedMessage("page.attribute.missing"));
        xmlNames.put(unEscapeBinaryString(xmlLast.get("Name")), xmlLast.get("Page"));
        xmlLast = null;
    }

    public void startDocument() {
    }

    public void startElement(String tag, Map<String, String> h) {
        if (xmlNames == null) {
            if (tag.equals("Destination")) {
                xmlNames = new HashMap<String, String>();
                return;
            }
            else
                throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.destination"));
        }
        if (!tag.equals("Name"))
            throw new RuntimeException(MessageLocalization.getComposedMessage("tag.1.not.allowed", tag));
        if (xmlLast != null)
            throw new RuntimeException(MessageLocalization.getComposedMessage("nested.tags.are.not.allowed"));
        xmlLast = new HashMap<String, String>(h);
        xmlLast.put("Name", "");
    }

    public void text(String str) {
        if (xmlLast == null)
            return;
        String name = xmlLast.get("Name");
        name += str;
        xmlLast.put("Name", name);
    }
}
