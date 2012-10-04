/*
 * $Id: LtvVerification.java 5437 2012-10-01 13:17:59Z blowagie $
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
import java.math.BigInteger;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Verifies the signatures in an LTV document.
 */
public class LtvValidation {
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(LtvValidation.class);
	
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
		while (pkcs7 != null && pkcs7.isTsp()) {
			verifyDocumentLevelTimestamp();
		}
		verifySignatures();
	}
	
	/**
	 * Creates a VerificationData object for a PdfReader
	 * @param reader	a reader for the document we want to verify.
	 * @throws GeneralSecurityException 
	 */
	public LtvValidation(PdfReader reader) throws GeneralSecurityException {
		this.reader = reader;
		this.fields = reader.getAcroFields();
		List<String> names = fields.getSignatureNames();
		signatureName = names.get(names.size() - 1);
		pkcs7 = coversWholeDocument();
		LOGGER.info(String.format("Checking %ssignature %s", pkcs7.isTsp() ? "document-level timestamp " : "", signatureName));
		this.signDate = new Date();
	}
	
	/**
	 * Checks if the signature covers the whole document
	 * and throws an exception if the document was altered
	 * @return a PdfPKCS7 object
	 * @throws GeneralSecurityException
	 */
	protected PdfPKCS7 coversWholeDocument() throws GeneralSecurityException {
		PdfPKCS7 pkcs7 = fields.verifySignature(signatureName);
		if (!pkcs7.isTsp()) {
			LOGGER.warn("The final revision of this document doesn't contain a document-level timestamp.");
		}
		if (fields.signatureCoversWholeDocument(signatureName)) {
			LOGGER.info("The document-level timestamp covers whole document.");
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
	 * Switches to the previous revision.
	 * @throws IOException
	 */
	public void switchToPreviousRevision() throws IOException {
		LOGGER.info("Switching to previous revision.");
		dss = reader.getCatalog().getAsDict(PdfName.DSS);
	    signDate = pkcs7.getTimeStampDate().getTime();
		List<String> names = fields.getSignatureNames();
		if (names.size() > 1) {
			signatureName = names.get(names.size() - 2);
			reader = new PdfReader(fields.extractRevision(signatureName));
			this.fields = reader.getAcroFields();
			names = fields.getSignatureNames();
			signatureName = names.get(names.size() - 1);
			pkcs7 = fields.verifySignature(signatureName, "BC");
			LOGGER.info(String.format("Checking %ssignature %s", pkcs7.isTsp() ? "document-level timestamp " : "", signatureName));
		}
		else {
			LOGGER.info("No signatures in revision");
		}
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
	 * Verifies a document level timestamp.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws OCSPException
	 * @throws OperatorCreationException
	 */
	public void verifyDocumentLevelTimestamp() throws GeneralSecurityException, IOException, OCSPException, OperatorCreationException {
        LOGGER.info("Verifying document-level timestamp.");
		// Get the certificate chain
		Certificate[] chain = pkcs7.getSignCertificateChain();
		if (chain.length < 2)
        	throw new GeneralSecurityException("Self-signed TSA certificates can't be checked");
		// Validate the chain; get signing and issuer certificate
		checkCertificateValidity(chain);
		X509Certificate signCert = (X509Certificate) chain[0];
		X509Certificate issuerCert = (X509Certificate) chain[1];
		
		// Certificate Revocation Lists
		List<X509CRL> crls;
		// Get CRLs from DSS
		if (dss != null) {
			crls = getCRLsFromDSS();
		}
		// Or get CRL online
		else {
			String crlurl = CertificateUtil.getCRLURL(signCert);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
	        X509CRL crl = (X509CRL) cf.generateCRL(new URL(crlurl).openStream());
	        crl.verify(issuerCert.getPublicKey());
	        crls = new ArrayList<X509CRL>();
	        crls.add(crl);
		}
		// check the CRLs
		boolean crlFound = verifyAgainstCrls(signCert, issuerCert, crls);
		
		// Online Certificate Status Protocol
		List<BasicOCSPResp> ocsps;
		// Get OCSP responses from DSS
		if (dss != null) {
			ocsps = getOCSPResponsesFromDSS();
		}
		// Or get OCSP response online
		else {
			ocsps = new ArrayList<BasicOCSPResp>();
			BasicOCSPResp ocsp = getOcspResponse(signCert, issuerCert);
			if (ocsp != null)
				ocsps.add(ocsp);
		}
		// Check the OCSP responses
		boolean ocspFound = verifyAgainstOCSPs(signCert, issuerCert, ocsps);
		
		// If no CRL or OCSP was available: we can't verify!
		if (!crlFound && !ocspFound)
			throw new GeneralSecurityException("Couldn't verify with CRL or OCSP");
		// Feedback
		if (crlFound)
			LOGGER.info("Valid CRL found!");
		if (ocspFound)
			LOGGER.info("Valid OCSP found!");
		switchToPreviousRevision();
	}
	
	/**
	 * Verifies the signatures.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws OCSPException
	 * @throws OperatorCreationException
	 */
	public void verifySignatures() throws GeneralSecurityException, IOException, OCSPException, OperatorCreationException {
		LOGGER.info("Verifying signatures.");
		PdfPKCS7 pkcs7;
		for (String name : fields.getSignatureNames()) {
			LOGGER.info("Signature: " + name);
			pkcs7 = fields.verifySignature(name);
			// get the certificate chain
			Certificate[] chain = pkcs7.getSignCertificateChain();
			if (chain.length < 2)
	        	throw new GeneralSecurityException("Self-signed TSA certificates can't be checked");
			// Check the validity; get signing cert and issuer cert
			checkCertificateValidity(chain);
			X509Certificate signCert = (X509Certificate) chain[0];
			X509Certificate issuerCert = (X509Certificate) chain[1];
			// Verify the signature
			if (pkcs7.verify()) {
				LOGGER.info("Integrity verified!");
				// Veriy CRLs from DSS
				List<X509CRL> crls = getCRLsFromDSS();
				boolean crlFound = verifyAgainstCrls(signCert, issuerCert, crls);
				// Verify OCSPs from DSS
				List<BasicOCSPResp> ocsps = getOCSPResponsesFromDSS();
				// If no valid CRLs or OCSP responses are found:
				boolean ocspFound = verifyAgainstOCSPs(signCert, issuerCert, ocsps);
				if (!crlFound && !ocspFound)
					throw new GeneralSecurityException("Couldn't verify with CRL or OCSP");
				// Feedback
				if (crlFound)
					LOGGER.info("Valid CRL found!");
				if (ocspFound)
					LOGGER.info("Valid OCSP found!");
			}
			else {
				throw new GeneralSecurityException("The document was altered after the final signature was applied.");
			}
		} 
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
	 * Verifies a certificate against a list of CRLs
	 * @param signCert	the signing certificate
	 * @param issuerCert	the issuer of the signing certificate
	 * @param crls	the list of CRLs
	 * @return true if the certificate wasn't revoked by any matching CRL
	 * @throws GeneralSecurityException
	 */
	public boolean verifyAgainstCrls(X509Certificate signCert, X509Certificate issuerCert, List<X509CRL> crls) throws GeneralSecurityException {
		int validCrlsFound = 0;
		for (X509CRL crl : crls) {
			// We only check CRLs valid on the signing date for which the issuer matches
			if (crl.getIssuerX500Principal().equals(signCert.getIssuerX500Principal())
				&& signDate.after(crl.getThisUpdate()) && signDate.before(crl.getNextUpdate())) {
				// the CRL has to be signed by the issuer of the signing certificate
				try {
					crl.verify(issuerCert.getPublicKey());
				} catch (GeneralSecurityException e) {
					continue;
				}
				// the signing certificate may not be revoked
				if (crl.isRevoked(signCert)) {
					throw new GeneralSecurityException("The certificate has been revoked.");
				}
				validCrlsFound++;
			}
		}
		return validCrlsFound > 0;
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
	
	
	/**
	 * Verifies a certificate against a list of OCSP responses
	 * @param signCert	the signing certificate
	 * @param issuerCert the issuer certificate
	 * @param ocsps the list of BasicOCSPResp objects
	 * @return true if the certificate corresponds with at least one valid OCSP response
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public boolean verifyAgainstOCSPs(X509Certificate signCert, X509Certificate issuerCert, List<BasicOCSPResp> ocsps) throws GeneralSecurityException, IOException {
		int validOCSPsFound = 0;
		BigInteger serialNumber = signCert.getSerialNumber();
		for (BasicOCSPResp ocspResp : ocsps) {
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
					verifyOCSPResponse(ocspResp, issuerCert);
					validOCSPsFound++;
				}
			}
		}
		return validOCSPsFound > 0;
	}
	
	/**
	 * Verifies if an OCSP response is genuine
	 * @param ocspResp	the OCSP response
	 * @param issuerCert	the issuer certificate
	 * @throws GeneralSecurityException
	 */
	public void verifyOCSPResponse(BasicOCSPResp ocspResp, Certificate issuerCert) throws GeneralSecurityException {
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
}
