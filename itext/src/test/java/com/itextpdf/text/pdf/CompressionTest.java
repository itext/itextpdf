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
