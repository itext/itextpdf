package com.itextpdf.text.pdf;

import java.security.cert.X509Certificate;

/**
 *
 * @author psoares
 */
public interface CrlClient {
	/**
	 * Gets an encoded byte array.
	 * @return	a byte array
	 */
    public byte[] getEncoded(X509Certificate checkCert, String url);
}
