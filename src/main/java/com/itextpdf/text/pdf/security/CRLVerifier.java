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
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

public class CRLVerifier extends CertificateVerifier {
	
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(CRLVerifier.class);
	
	List<X509CRL> crls;
	
	public CRLVerifier(CertificateVerifier verifier, List<X509CRL> crls) {
		super(verifier);
		this.crls = crls;
	}
	
	public boolean verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate)
			throws GeneralSecurityException, IOException {
		int validCrlsFound = 0;
		for (X509CRL crl : crls) {
			if (verify(crl, signCert, issuerCert, signDate))
				validCrlsFound++;
		}
		if (onlineCheckingAllowed && validCrlsFound == 0) {
			if (verify(getCRL(signCert, issuerCert), signCert, issuerCert, signDate))
				validCrlsFound++;
		}
		LOGGER.info("Valid CRLs found: " + validCrlsFound);
		return super.verify(signCert, issuerCert, signDate) || validCrlsFound > 0;
	}

	public boolean verify(X509CRL crl, X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException {
		if (crl == null)
			return false;
		// We only check CRLs valid on the signing date for which the issuer matches
		if (crl.getIssuerX500Principal().equals(signCert.getIssuerX500Principal())
			&& signDate.after(crl.getThisUpdate()) && signDate.before(crl.getNextUpdate())) {
			// the signing certificate may not be revoked
			if (verify(crl, issuerCert) && crl.isRevoked(signCert)) {
				throw new GeneralSecurityException("The certificate has been revoked.");
			}
			return true;
		}
		return false;
	}
	
	public X509CRL getCRL(X509Certificate signCert, X509Certificate issuerCert) {
		if (!onlineCheckingAllowed) {
			LOGGER.info("Fetching CRL online isn't allowed");
			return null;
		}
		try {
			String crlurl = CertificateUtil.getCRLURL(signCert);
			if (crlurl == null)
				return null;
			LOGGER.info("Getting CRL from " + crlurl);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509CRL crl = (X509CRL) cf.generateCRL(new URL(crlurl).openStream());
			if (verify(crl, issuerCert))
				return crl;
		}
		catch(IOException e) {
			return null;
		}
		catch(GeneralSecurityException e) {
			return null;
		}
		return null;
	}
	
	public boolean verify(X509CRL crl, Certificate crlIssuer) {
		try {
			crl.verify(crlIssuer.getPublicKey());
			return true;
		} catch (GeneralSecurityException e) {
			LOGGER.warn("CRL not issued by the same authority as the certificate that is being checked");
		}
		if (keyStore == null)
			return false;
		try {
        	for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements();) {
                String alias = aliases.nextElement();
                try {
    				if (!keyStore.isCertificateEntry(alias))
    					continue;
                    X509Certificate certStoreX509 = (X509Certificate)keyStore.getCertificate(alias);
                    crl.verify(certStoreX509.getPublicKey());
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
