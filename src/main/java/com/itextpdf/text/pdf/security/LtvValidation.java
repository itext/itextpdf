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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.operator.OperatorCreationException;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.LtvVerification.CertificateOption;

/**
 * Verifies the signatures in an LTV document.
 */
public class LtvValidation extends CertificateVerifier {
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(LtvValidation.class);
	
	/** Do we need to check all certificate, or only the signing certificate? */
	protected CertificateOption option = CertificateOption.SIGNING_CERTIFICATE;
	/** A key store against which certificates can be verified. */
	protected KeyStore keyStore = null;
	/** The class that is to be used to verify certificates. */
	protected CertificateVerifier customVerifier;
	/** Verify root. */
	protected boolean verifyRootCertificate = true;
	
	/** A reader object for the revision that is being verified. */
	protected PdfReader reader;
	/** The fields in the revision that is being verified. */
	protected AcroFields fields;
	/** The signature that covers the revision. */
	protected String signatureName;
	/** The PdfPKCS7 object for the signature. */
	protected PdfPKCS7 pkcs7;
	/**
	 * The document security store for the revision that is being verified
	 * (or <code>null</code> for the highest revision)
	 */
	protected PdfDictionary dss;
	/** The date the revision was signed, or <code>null</code> for the highest revision. */
	protected Date signDate;
	
	/**
	 * Verifies all the document-level timestamps and all the signatures in the document.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 * @throws OCSPException
	 * @throws OperatorCreationException
	 */
	public void verify() throws IOException, GeneralSecurityException, OCSPException, OperatorCreationException {
		while (pkcs7 != null) {
			verifySignature();
		}
	}
	
	/**
	 * Creates a VerificationData object for a PdfReader
	 * @param reader	a reader for the document we want to verify.
	 * @throws GeneralSecurityException 
	 */
	public LtvValidation(PdfReader reader) throws GeneralSecurityException {
		super(null);
		this.reader = reader;
		this.fields = reader.getAcroFields();
		List<String> names = fields.getSignatureNames();
		signatureName = names.get(names.size() - 1);
		pkcs7 = coversWholeDocument();
		LOGGER.info(String.format("Checking %ssignature %s", pkcs7.isTsp() ? "document-level timestamp " : "", signatureName));
		this.signDate = new Date();
	}
	
	/**
	 * Switches to the previous revision.
	 * @throws IOException
	 * @throws GeneralSecurityException 
	 */
	public void switchToPreviousRevision() throws IOException, GeneralSecurityException {
		LOGGER.info("Switching to previous revision.");
		dss = reader.getCatalog().getAsDict(PdfName.DSS);
		Calendar cal = pkcs7.getTimeStampDate();
		if (cal == null)
			cal = pkcs7.getSignDate();
		// TODO: get date from signature
	    signDate = cal.getTime();
		List<String> names = fields.getSignatureNames();
		if (names.size() > 1) {
			signatureName = names.get(names.size() - 2);
			reader = new PdfReader(fields.extractRevision(signatureName));
			this.fields = reader.getAcroFields();
			names = fields.getSignatureNames();
			signatureName = names.get(names.size() - 1);
			pkcs7 = coversWholeDocument();
			LOGGER.info(String.format("Checking %ssignature %s", pkcs7.isTsp() ? "document-level timestamp " : "", signatureName));
		}
		else {
			LOGGER.info("No signatures in revision");
			pkcs7 = null;
		}
	}
	
	/**
	 * Checks if the signature covers the whole document
	 * and throws an exception if the document was altered
	 * @return a PdfPKCS7 object
	 * @throws GeneralSecurityException
	 */
	protected PdfPKCS7 coversWholeDocument() throws GeneralSecurityException {
		PdfPKCS7 pkcs7 = fields.verifySignature(signatureName);
		if (fields.signatureCoversWholeDocument(signatureName)) {
			LOGGER.info("The timestamp covers whole document.");
		}
		else {
			throw new GeneralSecurityException("Signature doesn't cover whole document.");
		}
		if (pkcs7.verify()) {
			LOGGER.info("The signed document has not been modified.");
			return pkcs7;
		}
		else {
			throw new GeneralSecurityException("The document was altered after the final signature was applied.");
		}
	}
	
	/**
	 * Sets the certificate option.
	 * @param	option	Either CertificateOption.SIGNING_CERTIFICATE (default) or CertificateOption.WHOLE_CHAIN
	 */
	public void setCertificateOption(CertificateOption option) {
		this.option = option;
	}
	
	/**
	 * Sets the Key Store against which a certificate can be checked.
	 * @param keyStore a root store
	 */
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}
	
	/**
	 * Adds an extra Certificate verifier.
	 */
	public void setCustomVerifier(CertificateVerifier customVerifier) {
		this.customVerifier = customVerifier;
	}

	/**
	 * Set the verifyRootCertificate to false if you can't verify the root certificate.
	 */
	public void setVerifyRootCertificate(boolean verifyRootCertificate) {
		this.verifyRootCertificate = verifyRootCertificate;
	}

	/**
	 * Checks the certificates in a certificate chain:
	 * are they valid on a specific date, and
	 * do they chain up correctly?
	 * @param chain
	 * @throws GeneralSecurityException
	 */
	public void checkCertificateValidity(Certificate[] chain) throws GeneralSecurityException {
		// Loop over the certificates in the chain
		for (int i = 0; i < chain.length; i++) {
			X509Certificate cert = (X509Certificate) chain[i];
			// check if the certificate was/is valid
			cert.checkValidity(signDate);
			// check if the previous certificate was issued by this certificate
			if (i > 0)
				chain[i-1].verify(chain[i].getPublicKey());
		}
		LOGGER.info("All certificates are valid on " + signDate.toString());
	}
	
	/**
	 * Checks if the certificate can be verified against a key in the key store with trusted anchors.
	 */
	public boolean verifyAgainstKeyStore(Certificate[] certs) throws GeneralSecurityException {
		if (keyStore == null)
			return false;

	    for (int k = 0; k < certs.length; ++k) {
	        X509Certificate cert = (X509Certificate)certs[k];
	        for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements();) {
	            try {
	                String alias = aliases.nextElement();
	                if (!keyStore.isCertificateEntry(alias))
	                    continue;
	                X509Certificate certStoreX509 = (X509Certificate)keyStore.getCertificate(alias);
	                try {
	                    cert.verify(certStoreX509.getPublicKey());
	                    return true;
	                }
	                catch (Exception e) {
	                    continue;
	                }
	            }
	            catch (Exception ex) {
	            }
	        }
        }
		return false;
	}
	
	/**
	 * Verifies a document level timestamp.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws OCSPException
	 * @throws OperatorCreationException
	 */
	public void verifySignature() throws GeneralSecurityException, IOException {
        LOGGER.info("Verifying signature.");
		// Get the certificate chain
		Certificate[] chain = pkcs7.getSignCertificateChain();
		checkCertificateValidity(chain);
		boolean anchorFound = verifyAgainstKeyStore(chain);
		// Feedback
		if (anchorFound)
			LOGGER.info("Verified against key store!");
		
		if (chain.length < 2 && !anchorFound)
        	throw new GeneralSecurityException("Self-signed certificates can't be checked");
		// get signing and issuer certificate
		int total = 1;
		if (CertificateOption.WHOLE_CHAIN.equals(option)) {
			total = chain.length;
		}
		X509Certificate signCert;
		X509Certificate issuerCert;
		for (int i = 0; i < total; ) {
			signCert = (X509Certificate) chain[i];
			issuerCert = null;
			i++;
			if (i < chain.length)
				issuerCert = (X509Certificate) chain[i];
			LOGGER.info(signCert.getSubjectDN().getName());

			CRLVerifier crlVerifier = new CRLVerifier(customVerifier, getCRLsFromDSS());
			OCSPVerifier ocspVerifier = new OCSPVerifier(crlVerifier, getOCSPResponsesFromDSS());
			verifier = new CertificateVerifier(ocspVerifier);
			if (!(this.verify(signCert, issuerCert, signDate) || anchorFound))
				throw new GeneralSecurityException("Couldn't verify with CRL or OCSP or trusted anchor");
		}
		switchToPreviousRevision();
	}
	
	/**
	 * Verifies certificates against a list of CRLs and OCSP responses.
	 * @param signingCert
	 * @param issuerCert
	 * @return true if the certificate was successfully verified.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @see com.itextpdf.text.pdf.security.CertificateVerifier#verify(java.security.cert.X509Certificate, java.security.cert.X509Certificate)
	 */
	public boolean verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
		if (issuerCert == null)
			return !verifyRootCertificate;
		return verifier.verify(signCert, issuerCert, signDate);
	}
	
	/**
	 * Gets a list of X509CRL objects from a Document Security Store.
	 * @return	a list of CRLs
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public List<X509CRL> getCRLsFromDSS() throws GeneralSecurityException, IOException {
		List<X509CRL> crls = new ArrayList<X509CRL>();
		if (dss == null)
			return crls;
		PdfArray crlarray = dss.getAsArray(PdfName.CRLS);
		if (crlarray == null)
			return crls;
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		for (int i = 0; i < crlarray.size(); i++) {
			PRStream stream = (PRStream) crlarray.getAsStream(i);
			X509CRL crl = (X509CRL)cf.generateCRL(new ByteArrayInputStream(PdfReader.getStreamBytes(stream)));
			crls.add(crl);
		}
		return crls;
	}
	
	/**
	 * Gets OCSP responses from the Document Security Store.
	 * @return	a list of BasicOCSPResp objects
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public List<BasicOCSPResp> getOCSPResponsesFromDSS() throws IOException, GeneralSecurityException {
		List<BasicOCSPResp> ocsps = new ArrayList<BasicOCSPResp>();
		if (dss == null)
			return ocsps;
		PdfArray ocsparray = dss.getAsArray(PdfName.OCSPS);
		if (ocsparray == null)
			return ocsps;
		for (int i = 0; i < ocsparray.size(); i++) {
			PRStream stream = (PRStream) ocsparray.getAsStream(i);
			OCSPResp ocspResponse = new OCSPResp(PdfReader.getStreamBytes(stream));
			if (ocspResponse.getStatus() == 0)
				try {
					ocsps.add((BasicOCSPResp) ocspResponse.getResponseObject());
				} catch (OCSPException e) {
					throw new GeneralSecurityException(e);
				}
		}
		return ocsps;
	}
}
