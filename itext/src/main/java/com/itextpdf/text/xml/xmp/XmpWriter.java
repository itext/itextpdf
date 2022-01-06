/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
package com.itextpdf.text.xml.xmp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.itextpdf.text.Version;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.xmp.*;
import com.itextpdf.xmp.options.PropertyOptions;
import com.itextpdf.xmp.options.SerializeOptions;

/**
 * With this class you can create an Xmp Stream that can be used for adding
 * Metadata to a PDF Dictionary. Remark that this class doesn't cover the
 * complete XMP specification.
 */
public class XmpWriter {

	/** A possible charset for the XMP. */
	public static final String UTF8 = "UTF-8";
	/** A possible charset for the XMP. */
	public static final String UTF16 = "UTF-16";
	/** A possible charset for the XMP. */
	public static final String UTF16BE = "UTF-16BE";
	/** A possible charset for the XMP. */
	public static final String UTF16LE = "UTF-16LE";

    protected XMPMeta xmpMeta;
    protected OutputStream outputStream;
    protected SerializeOptions serializeOptions;

	/**
	 * Creates an XmpWriter.
	 * @param os
	 * @param utfEncoding
	 * @param extraSpace
	 * @throws IOException
	 */
	public XmpWriter(OutputStream os, String utfEncoding, int extraSpace) throws IOException {
        outputStream = os;
        serializeOptions = new SerializeOptions();
        if (UTF16BE.equals(utfEncoding) || UTF16.equals(utfEncoding))
            serializeOptions.setEncodeUTF16BE(true);
        else if (UTF16LE.equals(utfEncoding))
            serializeOptions.setEncodeUTF16LE(true);
        serializeOptions.setPadding(extraSpace);
        xmpMeta = XMPMetaFactory.create();
        xmpMeta.setObjectName(XMPConst.TAG_XMPMETA);
        xmpMeta.setObjectName("");
        try {
            xmpMeta.setProperty(XMPConst.NS_DC, DublinCoreProperties.FORMAT, "application/pdf");
            xmpMeta.setProperty(XMPConst.NS_PDF, PdfProperties.PRODUCER, Version.getInstance().getVersion());
        } catch (XMPException xmpExc) {}
	}

	/**
	 * Creates an XmpWriter.
	 * @param os
	 * @throws IOException
	 */
	public XmpWriter(OutputStream os) throws IOException {
		this(os, UTF8, 2000);
	}

    /**
     * @param os
     * @param info
     * @throws IOException
     */
    public XmpWriter(OutputStream os, PdfDictionary info) throws IOException {
        this(os);
        if (info != null) {
            PdfName key;
            PdfObject obj;
            String value;
            for (PdfName pdfName : info.getKeys()) {
                key = pdfName;
                obj = info.get(key);
                if (obj == null)
                    continue;
                if (!obj.isString())
                    continue;
                value = ((PdfString) obj).toUnicodeString();
                try {
                    addDocInfoProperty(key, value);
                } catch (XMPException xmpExc) {
                    throw new IOException(xmpExc.getMessage());
                }
            }
        }
    }
    
    /**
     * @param os
     * @param info
     * @throws IOException
     * @since 5.0.1 (generic type in signature)
     */
    public XmpWriter(OutputStream os, Map<String, String> info) throws IOException {
        this(os);
        if (info != null) {
            String key;
            String value;
            for (Map.Entry<String, String> entry : info.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (value == null)
                    continue;
                try {
                    addDocInfoProperty(key, value);
                } catch (XMPException xmpExc) {
                    throw new IOException(xmpExc.getMessage());
                }
            }
        }
    }
    
    public XMPMeta getXmpMeta() {
        return xmpMeta;
    }

    /** Sets the XMP to read-only */
	public void setReadOnly() {
        serializeOptions.setReadOnlyPacket(true);
	}

	/**
	 * @param about The about to set.
	 */
	public void setAbout(String about) {
        xmpMeta.setObjectName(about);
	}

	/**
	 * Adds an rdf:Description.
	 * @param xmlns
	 * @param content
	 * @throws IOException
	 */
    @Deprecated
	public void addRdfDescription(String xmlns, String content) throws IOException {
        try {
            String str = "<rdf:RDF xmlns:rdf=\"" + XMPConst.NS_RDF + "\">" +
                    "<rdf:Description rdf:about=\"" + xmpMeta.getObjectName() +
                    "\" " +
                    xmlns +
                    ">" +
                    content +
                    "</rdf:Description></rdf:RDF>\n";
            XMPMeta extMeta = XMPMetaFactory.parseFromString(str);
            XMPUtils.appendProperties(extMeta, xmpMeta, true, true);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc.getMessage());
        }
	}

	/**
	 * Adds an rdf:Description.
	 * @param s
	 * @throws IOException
	 */
    @Deprecated
	public void addRdfDescription(XmpSchema s) throws IOException {
        try {
            String str = "<rdf:RDF xmlns:rdf=\"" + XMPConst.NS_RDF + "\">" +
                    "<rdf:Description rdf:about=\"" + xmpMeta.getObjectName() +
                    "\" " +
                    s.getXmlns() +
                    ">" +
                    s.toString() +
                    "</rdf:Description></rdf:RDF>\n";
            XMPMeta extMeta = XMPMetaFactory.parseFromString(str);
            XMPUtils.appendProperties(extMeta, xmpMeta, true, true);
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc.getMessage());
        }
	}

    /**
     * @param schemaNS The namespace URI for the property. Has the same usage as in getProperty.
     * @param propName The name of the property.
     *                 Has the same usage as in <code>getProperty()</code>.
     * @param value    the value for the property (only leaf properties have a value).
     *                 Arrays and non-leaf levels of structs do not have values.
     *                 Must be <code>null</code> if the value is not relevant.<br/>
     *                 The value is automatically detected: Boolean, Integer, Long, Double, XMPDateTime and
     *                 byte[] are handled, on all other <code>toString()</code> is called.
     * @throws XMPException Wraps all errors and exceptions that may occur.
     */
    public void setProperty(String schemaNS, String propName, Object value) throws XMPException {
        xmpMeta.setProperty(schemaNS, propName, value);
    }

    /**
     * Simplifies the construction of an array by not requiring that you pre-create an empty array.
     * The array that is assigned is created automatically if it does not yet exist. Each call to
     * appendArrayItem() appends an item to the array.
     *
     * @param schemaNS  The namespace URI for the array.
     * @param arrayName The name of the array. May be a general path expression, must not be null or
     *                  the empty string.
     * @param value     the value of the array item.
     * @throws XMPException Wraps all errors and exceptions that may occur.
     */
    public void appendArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
        xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(PropertyOptions.ARRAY), value, null);
    }

    /**
     * Simplifies the construction of an ordered array by not requiring that you pre-create an empty array.
     * The array that is assigned is created automatically if it does not yet exist. Each call to
     * appendArrayItem() appends an item to the array.
     *
     * @param schemaNS  The namespace URI for the array.
     * @param arrayName The name of the array. May be a general path expression, must not be null or
     *                  the empty string.
     * @param value     the value of the array item.
     * @throws XMPException Wraps all errors and exceptions that may occur.
     */
    public void appendOrderedArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
        xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), value, null);
    }

    /**
     * Simplifies the construction of an alternate array by not requiring that you pre-create an empty array.
     * The array that is assigned is created automatically if it does not yet exist. Each call to
     * appendArrayItem() appends an item to the array.
     *
     * @param schemaNS  The namespace URI for the array.
     * @param arrayName The name of the array. May be a general path expression, must not be null or
     *                  the empty string.
     * @param value     the value of the array item.
     * @throws XMPException Wraps all errors and exceptions that may occur.
     */
    public void appendAlternateArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
        xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(PropertyOptions.ARRAY_ALTERNATE), value, null);
    }

    /**
     * Flushes and closes the XmpWriter.
     * @throws IOException
     */
    public void serialize(OutputStream externalOutputStream) throws XMPException {
        XMPMetaFactory.serialize(xmpMeta, externalOutputStream, serializeOptions);
    }

	/**
	 * Flushes and closes the XmpWriter.
	 * @throws IOException
	 */
	public void close() throws IOException {
        if (outputStream == null)
            return;
        try {
            XMPMetaFactory.serialize(xmpMeta, outputStream, serializeOptions);
            outputStream = null;
        } catch (XMPException xmpExc) {
            throw new IOException(xmpExc.getMessage());
        }
    }

    public void addDocInfoProperty(Object key, String value) throws XMPException {
        if (key instanceof String)
            key = new PdfName((String) key);
        if (PdfName.TITLE.equals(key)) {
            xmpMeta.setLocalizedText(XMPConst.NS_DC, DublinCoreProperties.TITLE, XMPConst.X_DEFAULT, XMPConst.X_DEFAULT, value);
        } else if (PdfName.AUTHOR.equals(key)) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, DublinCoreProperties.CREATOR, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), value, null);
        } else if (PdfName.SUBJECT.equals(key)) {
            xmpMeta.setLocalizedText(XMPConst.NS_DC, DublinCoreProperties.DESCRIPTION, XMPConst.X_DEFAULT, XMPConst.X_DEFAULT, value);
        } else if (PdfName.KEYWORDS.equals(key)) {
            for (String v : value.split(",|;"))
                if (v.trim().length() > 0)
                    xmpMeta.appendArrayItem(XMPConst.NS_DC, DublinCoreProperties.SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), v.trim(), null);
            xmpMeta.setProperty(XMPConst.NS_PDF, PdfProperties.KEYWORDS, value);
        } else if (PdfName.PRODUCER.equals(key)) {
            xmpMeta.setProperty(XMPConst.NS_PDF, PdfProperties.PRODUCER, value);
        } else if (PdfName.CREATOR.equals(key)) {
            xmpMeta.setProperty(XMPConst.NS_XMP, XmpBasicProperties.CREATORTOOL, value);
        } else if (PdfName.CREATIONDATE.equals(key)) {
            xmpMeta.setProperty(XMPConst.NS_XMP, XmpBasicProperties.CREATEDATE, PdfDate.getW3CDate(value));
        } else if (PdfName.MODDATE.equals(key)) {
            xmpMeta.setProperty(XMPConst.NS_XMP, XmpBasicProperties.MODIFYDATE, PdfDate.getW3CDate(value));
        }
    }
}
