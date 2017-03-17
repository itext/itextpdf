/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfAFileStructureTest {


    /*
        The % character of the file header shall occur at byte offset 0 of the file.
        The file header line shall be immediately followed by a comment consisting of a % character followed by at least four characters, each of whose encoded byte values shall have a decimal value greater than 127.
     */
    @Test
    public void fileHeader() throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Chunk("Hello World"));
        document.close();

        byte[] bytes = baos.toByteArray();
        Assert.assertEquals(bytes[0], '%');
        Assert.assertTrue(bytes[10] < 0);
        Assert.assertTrue(bytes[11] < 0);
        Assert.assertTrue(bytes[12] < 0);
        Assert.assertTrue(bytes[13] < 0);
    }

    /*
        The file trailer dictionary shall contain the ID keyword.
        The keyword Encrypt shall not be used in the trailer dictionary.
        No data shall follow the last end-of-file marker except a single optional end-of-line marker.
     */
    @Test
    public void fileTrailer() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Chunk("Hello World"));
        document.close();

        byte[] bytes = baos.toByteArray();
        String str = new String(bytes, bytes.length - 6, 6);
        Assert.assertEquals("%%EOF\n", str);
        PdfReader reader = new PdfReader(baos.toByteArray());
        Assert.assertNotNull(reader.getTrailer().get(PdfName.ID));
        reader.close();
    }

    /*
        Hexadecimal strings shall contain an even number of non-white-space characters, each in the range 0 to 9, A to F or a to f.
     */
    @Test
    public void stringObjects() throws IOException {
        byte[] bytes = new byte[256];
        for (int i = 0; i < 256; i++)
            bytes[i] = (byte) i;
        PdfString str = new PdfString(bytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        str.setHexWriting(true);
        str.toPdf(null, baos);
        String s = new String(baos.toByteArray());
        Assert.assertEquals(514, s.length());
    }

    /*
        The stream keyword shall be followed either by a CARRIAGE RETURN (0Dh) and LINE FEED (0Ah) character sequence or by a single LINE FEED character.
        The endstream keyword shall be preceded by an EOL marker.
        The value of the Length key specified in the stream dictionary shall match the number of bytes in the file following the LINE FEED character after the stream keyword and preceding the EOL marker before the endstream keyword.
        A stream object dictionary shall not contain the F, FFilter, or FDecodeParams keys.
    */
    @Test
    public void streamObjects() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Chunk("Hello World"));
        PdfIndirectReference ref = writer.addToBody(new PdfStream("Hello World".getBytes())).getIndirectReference();
        writer.getInfo().put(new PdfName("HelloWorld"), ref);
        document.close();

        byte[] bytes = baos.toByteArray();
        String str = new String(bytes, 22, 44);
        Assert.assertEquals("\n<</Length 11>>stream\nHello World\nendstream\n", str);
    }

    /*
        The object number and generation number shall be separated by a single white-space character.
        The generation number and obj keyword shall be separated by a single white-space character.
        The object number and endobj keyword shall each be preceded by an EOL marker. The obj and endobj keywords shall each be followed by an EOL marker.
     */
    @Test
    public void indirectObjects() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Chunk("Hello World"));
        PdfIndirectReference ref = writer.addToBody(new PdfString("Hello World")).getIndirectReference();
        writer.getInfo().put(new PdfName("HelloWorld"), ref);
        document.close();

        byte[] bytes = baos.toByteArray();
        String str = new String(bytes, 14, 30);
        Assert.assertEquals("\n1 0 obj\n(Hello World)\nendobj\n", str);
    }


}
