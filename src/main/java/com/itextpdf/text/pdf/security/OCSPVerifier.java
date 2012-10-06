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
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

public class OCSPVerifier extends CertificateVerifier {
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(OCSPVerifier.class);
	
	protected List<BasicOCSPResp> ocsps;

	public OCSPVerifier(CertificateVerifier verifier, List<BasicOCSPResp> ocsps) {
		super(verifier);
		this.ocsps = ocsps;
	}

	public boolean verify(X509Certificate signCert,
			X509Certificate issuerCert, Date signDate)
			throws GeneralSecurityException, IOException {
		BigInteger serialNumber = signCert.getSerialNumber();
		int validOCSPsFound = 0;
		for (BasicOCSPResp ocspResp : ocsps) {
			if (verify(ocspResp, serialNumber, issuerCert, signDate))
				validOCSPsFound++;
		}
		if (onlineCheckingAllowed && validOCSPsFound == 0) {
			if (verify(getOcspResponse(signCert, issuerCert), serialNumber, issuerCert, signDate))
				validOCSPsFound++;
		}
		LOGGER.info("Valid OCPS found: " + validOCSPsFound);
		return super.verify(signCert, issuerCert, signDate) || validOCSPsFound > 0;
	}
	
	
	public boolean verify(BasicOCSPResp ocspResp, BigInteger serialNumber, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
		if (ocspResp == null)
			return false;
		// Getting the responses
		SingleResp[] resp = ocspResp.getResponses();
		for (int i = 0; i < resp.length; i++) {
			// go to next if the revision was signed after the response expired
			if (signDate.after(resp[i].getNextUpdate())) {
				LOGGER.info(String.format("OCSP no longer valid: %s after %s", signDate, resp[i].getNextUpdate()));
				continue;
			}
			if (!serialNumber.equals(resp[i].getCertID().getSerialNumber())) {
				LOGGER.info("OCSP: Serial number doesn't match");
				continue;
			}
			// go to next if the issuer doesn't match
			try {
				if (!resp[i].getCertID().matchesIssuer(new X509CertificateHolder(issuerCert.getEncoded()), new BcDigestCalculatorProvider())) {
					LOGGER.info("OCSP: Issuers doesn't match.");
					continue;
				}
			} catch (OCSPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// check the status of the certificate
			Object status = resp[i].getCertStatus();
			if (status == CertificateStatus.GOOD) {
				// check if the OCSP response was genuine
				verify(ocspResp, issuerCert);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifies if an OCSP response is genuine
	 * @param ocspResp	the OCSP response
	 * @param issuerCert	the issuer certificate
	 * @throws GeneralSecurityException
	 */
	public void verify(BasicOCSPResp ocspResp, Certificate issuerCert) throws GeneralSecurityException {
		// by default the OCSP responder certificate is the issuer certificate
		Certificate responderCert = issuerCert;
		// if there's a different responder certificate, it's signed by the issuer certificate
		X509CertificateHolder[] certHolders = ocspResp.getCerts();
		if (certHolders.length > 0) {
			responderCert = new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate(certHolders[0]);
			responderCert.verify(issuerCert.getPublicKey());
		}
		try {
			// Checking if the response is valid
			ContentVerifierProvider verifierProvider = new JcaContentVerifierProviderBuilder().setProvider("BC").build(responderCert.getPublicKey());
			if (!ocspResp.isSignatureValid(verifierProvider)) {
				throw new GeneralSecurityException("OCSP response could not be verified");
			}
		} catch (OperatorCreationException e) {
			throw new GeneralSecurityException(e);
		} catch (OCSPException e) {
			throw new GeneralSecurityException(e);
		}
	}
	
	public boolean verifySignature(BasicOCSPResp ocspResp, Certificate responderCert) {
		if (isSignatureValid(ocspResp, responderCert))
			return true;
		if (keyStore == null)
			return false;
		try {
        	for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements();) {
                String alias = aliases.nextElement();
                try {
    				if (!keyStore.isCertificateEntry(alias))
    					continue;
                    X509Certificate certStoreX509 = (X509Certificate)keyStore.getCertificate(alias);
                    if (isSignatureValid(ocspResp, certStoreX509));
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
	
	public boolean isSignatureValid(BasicOCSPResp ocspResp, Certificate responderCert) {
		try {
			ContentVerifierProvider verifierProvider = new JcaContentVerifierProviderBuilder().setProvider("BC").build(responderCert.getPublicKey());
			return ocspResp.isSignatureValid(verifierProvider);
		} catch (OperatorCreationException e) {
			return false;
		} catch (OCSPException e) {
			return false;
		}
	}
	
	/**
	 * Gets an OCSP response online and returns it if the status is GOOD
	 * (without further checking).
	 * @param signCert	the signing certificate
	 * @param issuerCert	the issuer certificate
	 * @return an OCSP response
	 */
	public BasicOCSPResp getOcspResponse(X509Certificate signCert, X509Certificate issuerCert) {
		if (signCert == null && issuerCert == null) {
			return null;
		}
		if (!onlineCheckingAllowed) {
			LOGGER.info("Fetching OCSP online isn't allowed");
			return null;
		}
		OcspClientBouncyCastle ocsp = new OcspClientBouncyCastle();
		BasicOCSPResp ocspResp = ocsp.getBasicOCSPResp(
				(X509Certificate) signCert, issuerCert, null);
		if (ocspResp == null) {
			return null;
		}
		SingleResp[] resp = ocspResp.getResponses();
		for (int i = 0; i < resp.length; i++) {
			Object status = resp[i].getCertStatus();
			if (status == CertificateStatus.GOOD) {
				return ocspResp;
			}
		}
		return null;
	}

}
