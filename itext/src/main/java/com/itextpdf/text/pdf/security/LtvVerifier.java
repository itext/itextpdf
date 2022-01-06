/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.log.Level;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPResp;

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
public class LtvVerifier extends RootStoreVerifier {
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(LtvVerifier.class);
	
	/** Do we need to check all certificate, or only the signing certificate? */
	protected CertificateOption option = CertificateOption.SIGNING_CERTIFICATE;
	/** Verify root. */
	protected boolean verifyRootCertificate = true;
	
	/** A reader object for the revision that is being verified. */
	protected PdfReader reader;
	/** The fields in the revision that is being verified. */
	protected AcroFields fields;
	/** The date the revision was signed, or <code>null</code> for the highest revision. */
	protected Date signDate;
	/** The signature that covers the revision. */
	protected String signatureName;
	/** The PdfPKCS7 object for the signature. */
	protected PdfPKCS7 pkcs7;
	/** Indicates if we're working with the latest revision. */
	protected boolean latestRevision = true;
	/** The document security store for the revision that is being verified */
	protected PdfDictionary dss;
	
	/**
	 * Creates a VerificationData object for a PdfReader
	 * @param reader	a reader for the document we want to verify.
	 * @throws GeneralSecurityException 
	 */
	public LtvVerifier(PdfReader reader) throws GeneralSecurityException {
		super(null);
		this.reader = reader;
		this.fields = reader.getAcroFields();
		List<String> names = fields.getSignatureNames();
		signatureName = names.get(names.size() - 1);
		this.signDate = new Date();
		pkcs7 = coversWholeDocument();
		if (LOGGER.isLogging(Level.INFO)) {
			LOGGER.info(String.format("Checking %ssignature %s", pkcs7.isTsp() ? "document-level timestamp " : "", signatureName));
		}
	}
	
	/**
	 * Sets an extra verifier.
	 * @param verifier the verifier to set
	 */
	public void setVerifier(CertificateVerifier verifier) {
		this.verifier = verifier;
	}
	
	/**
	 * Sets the certificate option.
	 * @param	option	Either CertificateOption.SIGNING_CERTIFICATE (default) or CertificateOption.WHOLE_CHAIN
	 */
	public void setCertificateOption(CertificateOption option) {
		this.option = option;
	}
	
	/**
	 * Set the verifyRootCertificate to false if you can't verify the root certificate.
	 */
	public void setVerifyRootCertificate(boolean verifyRootCertificate) {
		this.verifyRootCertificate = verifyRootCertificate;
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
			throw new VerificationException(null, "Signature doesn't cover whole document.");
		}
		if (pkcs7.verify()) {
			LOGGER.info("The signed document has not been modified.");
			return pkcs7;
		}
		else {
			throw new VerificationException(null, "The document was altered after the final signature was applied.");
		}
	}
	
	/**
	 * Verifies all the document-level timestamps and all the signatures in the document.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public List<VerificationOK> verify(List<VerificationOK> result) throws IOException, GeneralSecurityException {
		if (result == null)
			result = new ArrayList<VerificationOK>();
		while (pkcs7 != null) {
			result.addAll(verifySignature());
		}
		return result;
	}
	
	/**
	 * Verifies a document level timestamp.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public List<VerificationOK> verifySignature() throws GeneralSecurityException, IOException {
        LOGGER.info("Verifying signature.");
        List<VerificationOK> result = new ArrayList<VerificationOK>();
		// Get the certificate chain
		Certificate[] chain = pkcs7.getSignCertificateChain();
		verifyChain(chain);
		// how many certificates in the chain do we need to check?
		int total = 1;
		if (CertificateOption.WHOLE_CHAIN.equals(option)) {
			total = chain.length;
		}
		// loop over the certificates
		X509Certificate signCert;
		X509Certificate issuerCert;
		for (int i = 0; i < total; ) {
			// the certificate to check
			signCert = (X509Certificate) chain[i++];
			// its issuer
			issuerCert = null;
			if (i < chain.length)
				issuerCert = (X509Certificate) chain[i];
			// now lets verify the certificate
			LOGGER.info(signCert.getSubjectDN().getName());
			List<VerificationOK> list = verify(signCert, issuerCert, signDate);
			if (list.size() == 0) {
				try {
					signCert.verify(signCert.getPublicKey()); 
					if (latestRevision && chain.length > 1) {
						list.add(new VerificationOK(signCert, this.getClass(), "Root certificate in final revision"));
					}
					if (list.size() == 0 && verifyRootCertificate) {
						throw new GeneralSecurityException();
					}
					else if (chain.length > 1)
						list.add(new VerificationOK(signCert, this.getClass(), "Root certificate passed without checking"));
				}
				catch(GeneralSecurityException e) {
					throw new VerificationException(signCert, "Couldn't verify with CRL or OCSP or trusted anchor");
				}
			}
			result.addAll(list);
		}
		// go to the previous revision
		switchToPreviousRevision();
		return result;
	}

	/**
	 * Checks the certificates in a certificate chain:
	 * are they valid on a specific date, and
	 * do they chain up correctly?
	 * @param chain
	 * @throws GeneralSecurityException
	 */
	public void verifyChain(Certificate[] chain) throws GeneralSecurityException {
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
	 * Verifies certificates against a list of CRLs and OCSP responses.
	 * @param signingCert
	 * @param issuerCert
	 * @return a list of <code>VerificationOK</code> objects.
	 * The list will be empty if the certificate couldn't be verified.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @see com.itextpdf.text.pdf.security.RootStoreVerifier#verify(java.security.cert.X509Certificate, java.security.cert.X509Certificate)
	 */
	public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
		// we'll verify agains the rootstore (if present)
		RootStoreVerifier rootStoreVerifier = new RootStoreVerifier(verifier);
		rootStoreVerifier.setRootStore(rootStore);
		// We'll verify against a list of CRLs
		CRLVerifier crlVerifier = new CRLVerifier(rootStoreVerifier, getCRLsFromDSS());
		crlVerifier.setRootStore(rootStore);
		crlVerifier.setOnlineCheckingAllowed(latestRevision || onlineCheckingAllowed);
		// We'll verify against a list of OCSPs
		OCSPVerifier ocspVerifier = new OCSPVerifier(crlVerifier, getOCSPResponsesFromDSS());
		ocspVerifier.setRootStore(rootStore);
		ocspVerifier.setOnlineCheckingAllowed(latestRevision || onlineCheckingAllowed);
		// We verify the chain
		return ocspVerifier.verify(signCert, issuerCert, signDate);
	}
	
	/**
	 * Switches to the previous revision.
	 * @throws IOException
	 * @throws GeneralSecurityException 
	 */
	public void switchToPreviousRevision() throws IOException, GeneralSecurityException {
		LOGGER.info("Switching to previous revision.");
		latestRevision = false;
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
			if (LOGGER.isLogging(Level.INFO)) {
				LOGGER.info(String.format("Checking %ssignature %s", pkcs7.isTsp() ? "document-level timestamp " : "", signatureName));
			}
		}
		else {
			LOGGER.info("No signatures in revision");
			pkcs7 = null;
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
