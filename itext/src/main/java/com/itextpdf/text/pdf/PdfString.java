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
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.internal.PdfIsoKeys;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A <CODE>PdfString</CODE>-class is the PDF-equivalent of a
 * JAVA-<CODE>String</CODE>-object.
 * <P>
 * A string is a sequence of characters delimited by parenthesis.
 * If a string is too long to be conveniently placed on a single line, it may
 * be split across multiple lines by using the backslash character (\) at the
 * end of a line to indicate that the string continues on the following line.
 * Within a string, the backslash character is used as an escape to specify
 * unbalanced parenthesis, non-printing ASCII characters, and the backslash
 * character itself. Use of the \<I>ddd</I> escape sequence is the preferred
 * way to represent characters outside the printable ASCII character set.<BR>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.2.3 (page 53-56).
 *
 * @see PdfObject
 * @see BadPdfFormatException
 */
public class PdfString extends PdfObject {
    
    // CLASS VARIABLES
    
    /** The value of this object. */
    protected String value = NOTHING;
    
    protected String originalValue = null;
    
    /** The encoding. */
    protected String encoding = TEXT_PDFDOCENCODING;
    
    protected int objNum = 0;
    
    protected int objGen = 0;
    
    protected boolean hexWriting = false;

    // CONSTRUCTORS
    
    /**
     * Constructs an empty <CODE>PdfString</CODE>-object.
     */
    public PdfString() {
        super(STRING);
    }
    
    /**
     * Constructs a <CODE>PdfString</CODE>-object containing a string in the
     * standard encoding <CODE>TEXT_PDFDOCENCODING</CODE>.
     *
     * @param value    the content of the string
     */
    public PdfString(String value) {
        super(STRING);
        this.value = value;
    }
    
    /**
     * Constructs a <CODE>PdfString</CODE>-object containing a string in the
     * specified encoding.
     *
     * @param value    the content of the string
     * @param encoding an encoding
     */
    public PdfString(String value, String encoding) {
        super(STRING);
        this.value = value;
        this.encoding = encoding;
    }
    
    /**
     * Constructs a <CODE>PdfString</CODE>-object.
     *
     * @param bytes    an array of <CODE>byte</CODE>
     */
    public PdfString(byte[] bytes) {
        super(STRING);
        value = PdfEncodings.convertToString(bytes, null);
        encoding = NOTHING;
    }
    
    // methods overriding some methods in PdfObject
    
    /**
     * Writes the PDF representation of this <CODE>PdfString</CODE> as an array
     * of <CODE>byte</CODE> to the specified <CODE>OutputStream</CODE>.
     * 
     * @param writer for backwards compatibility
     * @param os The <CODE>OutputStream</CODE> to write the bytes to.
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, PdfIsoKeys.PDFISOKEY_OBJECT, this);
        byte b[] = getBytes();
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null && !crypto.isEmbeddedFilesOnly())
            b = crypto.encryptByteArray(b);
        if (hexWriting) {
            ByteBuffer buf = new ByteBuffer();
            buf.append('<');
            int len = b.length;
            for (int k = 0; k < len; ++k)
                buf.appendHex(b[k]);
            buf.append('>');
            os.write(buf.toByteArray());
        }
        else
            os.write(StringUtils.escapeString(b));
    }
    
    /**
     * Returns the <CODE>String</CODE> value of this <CODE>PdfString</CODE>-object.
     *
     * @return A <CODE>String</CODE>
     */
    public String toString() {
        return value;
    }
    
    public byte[] getBytes() {
        if (bytes == null) {
            if (encoding != null && encoding.equals(TEXT_UNICODE) && PdfEncodings.isPdfDocEncoding(value))
                bytes = PdfEncodings.convertToBytes(value, TEXT_PDFDOCENCODING);
            else
                bytes = PdfEncodings.convertToBytes(value, encoding);
        }
        return bytes;
    }
    
    // other methods
    
    /**
     * Returns the Unicode <CODE>String</CODE> value of this
     * <CODE>PdfString</CODE>-object.
     *
     * @return A <CODE>String</CODE>
     */
    public String toUnicodeString() {
        if (encoding != null && encoding.length() != 0)
            return value;
        getBytes();
        if (bytes.length >= 2 && bytes[0] == (byte)254 && bytes[1] == (byte)255)
            return PdfEncodings.convertToString(bytes, PdfObject.TEXT_UNICODE);
        else
            return PdfEncodings.convertToString(bytes, PdfObject.TEXT_PDFDOCENCODING);
    }
    
    /**
     * Gets the encoding of this string.
     *
     * @return a <CODE>String</CODE>
     */
    public String getEncoding() {
        return encoding;
    }
    
    void setObjNum(int objNum, int objGen) {
        this.objNum = objNum;
        this.objGen = objGen;
    }
    
    /**
     * Decrypt an encrypted <CODE>PdfString</CODE>
     */
    void decrypt(PdfReader reader) {
        PdfEncryption decrypt = reader.getDecrypt();
        if (decrypt != null) {
            originalValue = value;
            decrypt.setHashKey(objNum, objGen);
            bytes = PdfEncodings.convertToBytes(value, null);
            bytes = decrypt.decryptByteArray(bytes);
            value = PdfEncodings.convertToString(bytes, null);
        }
    }
   
    public byte[] getOriginalBytes() {
        if (originalValue == null)
            return getBytes();
        return PdfEncodings.convertToBytes(originalValue, null);
    }
    
    public PdfString setHexWriting(boolean hexWriting) {
        this.hexWriting = hexWriting;
        return this;
    }
    
    public boolean isHexWriting() {
        return hexWriting;
    }
}
