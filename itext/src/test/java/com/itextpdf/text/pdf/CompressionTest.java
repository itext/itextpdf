/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2026 iText Group NV
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

import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfImageObject;

import java.io.EOFException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import org.junit.function.ThrowingRunnable;

public class CompressionTest {

    private static final String SRC_DIR = "./src/test/resources/com/itextpdf/text/pdf/CompressionTest/";

    @Test
    public void decompressionBombInsideSingleStreamTest01() throws IOException {
        MemoryLimitsAwareHandler memoryHandler = new MemoryLimitsAwareHandler();
        memoryHandler.setMaxSizeOfSingleDecompressedPdfStream(5000000);
        memoryHandler.setMaxSizeOfDecompressedPdfStreamsSum(1000000000000000000l); // just to ensure that the single stream related exception is thrown

        ReaderProperties properties = new ReaderProperties();
        properties.setMemoryLimitsAwareHandler(memoryHandler);

        PdfReader reader = new PdfReader(properties, SRC_DIR + "acsploit_output.pdf");

        testDecompressionBomb(reader, MemoryLimitsAwareException.DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed);
    }

    @Test
    public void decompressionBombInsideMultipleStreamsTimingTest01() throws IOException {
        MemoryLimitsAwareHandler memoryHandler = new MemoryLimitsAwareHandler();
        memoryHandler.setMaxSizeOfSingleDecompressedPdfStream(Integer.MAX_VALUE / 10 * 9); // just to ensure that the multiple streams related exception is thrown
        memoryHandler.setMaxSizeOfDecompressedPdfStreamsSum(1000000);

        ReaderProperties properties = new ReaderProperties();
        properties.setMemoryLimitsAwareHandler(memoryHandler);

        PdfReader reader = new PdfReader(properties, SRC_DIR + "acsploit_timing.pdf");

        testDecompressionBomb(reader, MemoryLimitsAwareException.DuringDecompressionMultipleStreamsInSumOccupiedMoreMemoryThanAllowed);
    }

    @Test
    public void decompressionBombInsideMultipleStreamsTimingTest02() throws IOException {
        MemoryLimitsAwareHandler memoryHandler = new MemoryLimitsAwareHandler();
        memoryHandler.setMaxSizeOfSingleDecompressedPdfStream(Integer.MAX_VALUE / 10 * 9);  // just to ensure that the multiple streams related exception is thrown
        memoryHandler.setMaxSizeOfDecompressedPdfStreamsSum(1000000);

        ReaderProperties properties = new ReaderProperties();
        properties.setMemoryLimitsAwareHandler(memoryHandler);

        PdfReader reader = new PdfReader(properties, SRC_DIR + "acsploit_timing2.pdf");

        testDecompressionBomb(reader, MemoryLimitsAwareException.DuringDecompressionMultipleStreamsInSumOccupiedMoreMemoryThanAllowed);
    }

    @Test
    public void flateBombTest() throws IOException {
        PdfReader reader = new PdfReader(SRC_DIR + "pageStreamFlateBomb.pdf");
        testDecompressionBomb(reader,
                MemoryLimitsAwareException.DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed);
    }

    @Test
    public void pngDecodeStreamTest() throws IOException {
        // This test demonstrates a possible false positive
        PdfReader reader = new PdfReader(SRC_DIR + "png5000x5000.pdf");
        PdfDictionary resources = reader.getPageResources(1);
        PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
        PdfIndirectReference objRef = xobjects.getAsIndirectObject(new PdfName("Im0"));
        PRStream stream = (PRStream) PdfReader.getPdfObject(objRef);
        try {
            PdfImageObject img = new PdfImageObject(stream);
        } catch (MemoryLimitsAwareException e) {
            Assert.assertEquals(
                    MemoryLimitsAwareException.DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed,
                    e.getMessage());
            return;
        }

        Assert.fail("Expected MemoryLimitsAwareException was not thrown");
    }

    @Test
    public void streamWithoutEndstreamKeywordTest() throws IOException {
        final PdfReader reader = new PdfReader(SRC_DIR + "NoEndstreamKeyword.pdf");
        final PdfStream xmpMetadataStream = reader.getCatalog().getAsStream(PdfName.METADATA);
        final int xmpMetadataStreamLength = xmpMetadataStream.getAsNumber(PdfName.LENGTH).intValue();
        Assert.assertEquals(27599, xmpMetadataStreamLength);

        Assert.assertThrows(EOFException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                reader.getMetadata();
            }
        });
    }

    @Test
    public void endDicInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayEndDictToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed: Unexpected '>>' at file pointer 532; Original message: Unexpected '>>'"
                + " at file pointer 532", e.getMessage());
    }

    @Test
    public void endArrayClosingBracketInsteadOfEndDicTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "endArrayClosingBracketInsteadOfEndDic.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed: Unexpected ']' at file pointer 221; Original message: Unexpected ']'"
                + " at file pointer 221", e.getMessage());
    }

    @Test
    public void endDicClosingBracketInsideTheDicTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "endDicClosingBracketInsideTheDic.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed: Unexpected '>>' at file pointer 221; Original message: Unexpected '>>'"
                + " at file pointer 221", e.getMessage());
    }

    @Test
    public void eofInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayEOFToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed:  is not a valid number - java.lang.NumberFormatException: "
                + "empty String; Original message:  is not a valid number - java.lang.NumberFormatException:"
                + " empty String", e.getMessage());
    }

    @Test
    public void endObjInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayEndObjToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed:  is not a valid number - java.lang.NumberFormatException: empty String;"
                + " Original message:  is not a valid number - java.lang.NumberFormatException: empty String",
                e.getMessage());
    }

    @Test
    public void nameInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayNameToken.pdf");
            }
        });
        Assert.assertEquals(" is not a valid number - java.lang.NumberFormatException: empty String", e.getMessage());
    }

    @Test
    public void objInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayObjToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed:  is not a valid number - java.lang.NumberFormatException: empty String; "
                + "Original message:  is not a valid number - java.lang.NumberFormatException: empty String",
                e.getMessage());
    }

    @Test
    public void refInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayRefToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed:  is not a valid number - java.lang.NumberFormatException: empty String; "
                + "Original message:  is not a valid number - java.lang.NumberFormatException: empty String",
                e.getMessage());
    }

    @Test
    public void startArrayInsteadOfArrayClosingBracketTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayStartArrayToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed:  is not a valid number - java.lang.NumberFormatException: empty String; "
                + "Original message:  is not a valid number - java.lang.NumberFormatException: empty String",
                e.getMessage());
    }

    @Test
    public void stringInsteadOfArrayClosingBracketTest() throws IOException {
        final PdfReader reader = new PdfReader(SRC_DIR + "invalidArrayStringToken.pdf");
        PdfArray actual = (PdfArray) reader.getPdfObject(4);
        PdfArray expected = new PdfArray(new float[]{5, 10, 15, 20});
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.getAsNumber(i).intValue(), actual.getAsNumber(i).intValue());
        }
    }

    @Test
    public void closingArrayBracketMissingConservativeTest() throws IOException {
        Exception e = Assert.assertThrows(InvalidPdfException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                new PdfReader(SRC_DIR + "invalidArrayObjToken.pdf");
            }
        });
        Assert.assertEquals("Rebuild failed:  is not a valid number - java.lang.NumberFormatException: empty String; "
                        + "Original message:  is not a valid number - java.lang.NumberFormatException: empty String",
                e.getMessage());
    }

    @Test
    public void parseArrayTest() throws IOException {
        final PdfReader reader = new PdfReader(SRC_DIR + "innerArraysInContentStreamWithEndDictToken.pdf");
        PRTokeniser cmpTokeniser = new PRTokeniser(new RandomAccessFileOrArray(
                new RandomAccessSourceFactory().createSource(reader.getPageContent(1))));
        final PdfContentParser parser = new PdfContentParser(cmpTokeniser);
        Exception e = Assert.assertThrows(IOException.class, new ThrowingRunnable() {
            public void run() throws Throwable {
                parseContentStream(parser);
            }
        });
        Assert.assertEquals("Unexpected '>>'", e.getMessage());
    }

    private static void parseContentStream(PdfContentParser parser) throws IOException {
        ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
        while (!parser.parse(operands).isEmpty()) {
            // do nothign with operands
        }
    }

    private static void testDecompressionBomb(PdfReader reader, String expectedExceptionMessage) throws IOException {
        String thrownExceptionMessage = null;
        try {
            byte[] bytes = reader.getPageContent(1);
        } catch (MemoryLimitsAwareException e) {
            thrownExceptionMessage = e.getMessage();
        } catch (OutOfMemoryError e) {
            Assert.fail("Expected MemoryLimitsAwareException was not thrown");
        }

        reader.close();
        Assert.assertEquals(expectedExceptionMessage, thrownExceptionMessage);
    }
}
