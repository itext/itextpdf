package com.lowagie.text.pdf;

/**
 * Time Stamp Authority client (caller) interface.
 * <p>
 * Interface used by the PdfPKCS7 digital signature builder to call
 * Time Stamp Authority providing RFC 3161 compliant time stamp token.
 * @author Martin Brunecky, 07/17/2007
 */
public interface TSAClient {
    /**
     * Get the time stamp token size estimate.
     * Implementation must return value large enough to accomodate the entire token
     * returned by getTimeStampToken() _prior_ to actual getTimeStampToken() call.
     */
    public int getTokenSizeEstimate();
    
    /**
     * Get RFC 3161 timeStampToken.
     * Method may return null indicating that timestamp should be skipped.
     * @param caller PdfPKCS7 - calling PdfPKCS7 instance (in case caller needs it)
     * @param imprint byte[] - data imprint to be time-stamped
     * @return byte[] - encoded, TSA signed data of the timeStampToken
     * @throws Exception - TSA request failed
     */
    public byte[] getTimeStampToken(PdfPKCS7 caller, byte[] imprint) throws Exception;
    
}