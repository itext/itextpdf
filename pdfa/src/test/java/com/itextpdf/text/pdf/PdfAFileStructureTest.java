package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: chin
 * Date: 5/2/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
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
        In a cross reference subsection header the starting object number and the range shall be separated by a single SPACE character (20h).
        The xref keyword and the cross reference subsection header shall be separated by a single EOL marker.
     */
    @Test
    public void crossReferenceTable() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Chunk("Hello World"));
        document.close();

        byte[] bytes = baos.toByteArray();
        String str = new String(bytes, 643, 8);
        Assert.assertEquals("xref\n0 7", str);
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
