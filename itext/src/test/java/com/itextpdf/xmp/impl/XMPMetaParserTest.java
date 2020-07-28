package com.itextpdf.xmp.impl;

import com.itextpdf.xmp.XMPException;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XMPMetaParserTest {

    private static final String XMP_WITH_XXE = "<?xpacket begin=\"\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n"
            + "<!DOCTYPE foo [ <!ENTITY xxe SYSTEM \"./src/test/resources/com/itextpdf/xmp/impl/xxe-data.txt\" > ]>\n"
            + "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n"
            + "    <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n"
            + "        <rdf:Description rdf:about=\"\" xmlns:pdfaid=\"http://www.aiim.org/pdfa/ns/id/\">\n"
            + "            <pdfaid:part>&xxe;1</pdfaid:part>\n"
            + "            <pdfaid:conformance>B</pdfaid:conformance>\n"
            + "        </rdf:Description>\n"
            + "    </rdf:RDF>\n"
            + "</x:xmpmeta>\n"
            + "<?xpacket end=\"r\"?>";

    @Test
    public void xxeTestFromString() {
        try {
            XMPMetaParser.parse(XMP_WITH_XXE, null);
        } catch (XMPException e) {
            Assert.assertEquals("Children of resource property element must be XML elements", e.getMessage());
        }
    }

    @Test
    public void xxeTestFromByteBuffer() {
        try {
            XMPMetaParser.parse(XMP_WITH_XXE.getBytes(), null);
        } catch (XMPException e) {
            Assert.assertEquals("Children of resource property element must be XML elements", e.getMessage());
        }
    }

    @Test
    public void xxeTestFromInputStream() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(XMP_WITH_XXE.getBytes());
            XMPMetaParser.parse(inputStream, null);
        } catch (XMPException e) {
            Assert.assertEquals("Children of resource property element must be XML elements", e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
