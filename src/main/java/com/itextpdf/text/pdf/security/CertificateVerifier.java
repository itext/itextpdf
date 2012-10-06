/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text.pdf.security;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

/**
 * Superclass for a series of certificate verifiers that will typically
 * be used as the final verifier in a chain of other verifiers.
 * It wraps another <code>CertificateVerifier</code> whose verify()
 * method will be called.
 * There's also a simple verify() method to verify a certificate
 * against a <code>KeyStore</code>.
 */
public class CertificateVerifier {

	/** The previous CertificateVerifier in the chain of verifiers. */
	protected CertificateVerifier verifier;

	/** A key store against which certificates can be verified. */
	protected KeyStore keyStore = null;
	
	/** Indicates if going online to verify a certificate is allowed. */
	protected boolean onlineCheckingAllowed = true;

	/**
	 * Creates the final CertificateVerifier in a chain of verifiers.
	 * @param verifier	the previous verifier in the chain
	 */
	public CertificateVerifier(CertificateVerifier verifier) {
		this.verifier = verifier;
	}
	
	/**
	 * Sets the Key Store against which a certificate can be checked.
	 * @param keyStore a root store
	 */
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	/**
	 * Decide whether or not online checking is allowed.
	 * @param onlineCheckingAllowed
	 */
	public void setOnlineCheckingAllowed(boolean onlineCheckingAllowed) {
		this.onlineCheckingAllowed = onlineCheckingAllowed;
	}
	
	/**
	 * Calls the previous verifier in the chain, or returns false.
	 * @param signCert	the certificate that needs to be checked
	 * @param issuerCert	its issuer
	 * @param signDate		the date the certificate needs to be valid
	 * @return true if the certificate was successfully verified.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public boolean verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate)
			throws GeneralSecurityException, IOException {
		if (verifier != null)
			return verifier.verify(signCert, issuerCert, signDate);
		return false;
	}
	
	/**
	 * Verifies a single certificate against a key store (if present).
	 * @param cert	the certificate to verify
	 * @param signDate		the date the certificate needs to be valid
	 * @return true if the certificate was signed by a trusted anchor in the root store
	 */
	public boolean verify(Date signDate, X509Certificate cert, X509Certificate issuer) {
		if (keyStore == null)
			return false;
		if (issuer != null) {
			try {
				cert.verify(issuer.getPublicKey());
				return true;
			}
			catch(GeneralSecurityException e) {
				// do nothing
			}
		}
		try {
			if (signDate != null)
				cert.checkValidity(signDate);
        	for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements();) {
                String alias = aliases.nextElement();
                try {
    					if (!keyStore.isCertificateEntry(alias))
    					    continue;
                        X509Certificate anchor = (X509Certificate)keyStore.getCertificate(alias);
						cert.verify(anchor.getPublicKey());
	                    return true;
					} catch (GeneralSecurityException e) {
						continue;
					}
        	}
		}
        catch (GeneralSecurityException e) {
        	return false;
        }
		return false;
	}
}