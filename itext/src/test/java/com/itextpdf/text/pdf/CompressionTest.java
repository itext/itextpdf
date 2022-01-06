/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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


    private static void testDecompressionBomb(PdfReader reader, String expectedExceptionMessage) throws IOException {

        String thrownExceptionMessage = null;
        try {
            byte[] bytes = reader.getPageContent(1);
        } catch (MemoryLimitsAwareException e) {
            thrownExceptionMessage = e.getMessage();
        } catch (OutOfMemoryError e) {
            Assert.assertTrue(false);
        }

        reader.close();
        Assert.assertEquals(expectedExceptionMessage, thrownExceptionMessage);
    }
}
