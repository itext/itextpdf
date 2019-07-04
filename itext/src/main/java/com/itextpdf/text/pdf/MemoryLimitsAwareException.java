package com.itextpdf.text.pdf;

/**
 * Exception class for exceptions occurred during decompressed pdf streams processing.
 */
public class MemoryLimitsAwareException extends RuntimeException {

    public static final String DuringDecompressionMultipleStreamsInSumOccupiedMoreMemoryThanAllowed = "During decompression multiple streams in sum occupied more memory than allowed. Please either check your pdf or increase the allowed single decompressed pdf stream maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.";
    public static final String DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed = "During decompression a single stream occupied more memory than allowed. Please either check your pdf or increase the allowed multiple decompressed pdf streams maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.";
    public static final String DuringDecompressionSingleStreamOccupiedMoreThanMaxIntegerValue = "During decompression a single stream occupied more than a maximum integer value. Please check your pdf.";
    public static final String UnknownPdfException = "Unknown PdfException.";
    /**
     * Creates a new instance of MemoryLimitsAwareException.
     *
     * @param message the detail message.
     */
    public MemoryLimitsAwareException(String message) {
        super(message);
    }
}
