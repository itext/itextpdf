/*
 * $Id: CertificateVerification.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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

import java.security.KeyStore;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.tsp.TimeStampToken;

/**
 * This class consists of some methods that allow you to verify certificates.
 */
public class CertificateVerification {

    /**
     * Verifies a single certificate.
     * @param cert the certificate to verify
     * @param crls the certificate revocation list or <CODE>null</CODE>
     * @param calendar the date or <CODE>null</CODE> for the current date
     * @return a <CODE>String</CODE> with the error description or <CODE>null</CODE>
     * if no error
     */
    public static String verifyCertificate(X509Certificate cert, Collection<CRL> crls, Calendar calendar) {
        if (calendar == null)
            calendar = new GregorianCalendar();
        if (cert.hasUnsupportedCriticalExtension()) {
        	for (String oid : cert.getCriticalExtensionOIDs()) {
    			// KEY USAGE and DIGITAL SIGNING is ALLOWED
        		if ("2.5.29.15".equals(oid) && cert.getKeyUsage()[0]) {
        			continue;
        		}
        		try {
        			// EXTENDED KEY USAGE and TIMESTAMPING is ALLOWED
					if ("2.5.29.37".equals(oid) && cert.getExtendedKeyUsage().contains("1.3.6.1.5.5.7.3.8")) {
						continue;
					}
				} catch (CertificateParsingException e) {
					// DO NOTHING;
				}
                return "Has unsupported critical extension";
        	}
        }
        try {
            cert.checkValidity(calendar.getTime());
        }
        catch (Exception e) {
            return e.getMessage();
        }
        if (crls != null) {
            for (CRL crl : crls) {
                if (crl.isRevoked(cert))
                    return "Certificate revoked";
            }
        }
        return null;
    }

	/**
	 * Verifies a certificate chain against a KeyStore.
	 * @param certs the certificate chain
	 * @param keystore the <CODE>KeyStore</CODE>
	 * @param crls the certificate revocation list or <CODE>null</CODE>
	 * @param calendar the date or <CODE>null</CODE> for the current date
	 * @return <CODE>null</CODE> if the certificate chain could be validated or a
	 * <CODE>Object[]{cert,error}</CODE> where <CODE>cert</CODE> is the
	 * failed certificate and <CODE>error</CODE> is the error message
	 */
	public static List<VerificationException> verifyCertificates(Certificate certs[], KeyStore keystore, Collection<CRL> crls, Calendar calendar) {
	    List<VerificationException> result = new ArrayList<VerificationException>();
		if (calendar == null)
	        calendar = new GregorianCalendar();
	    for (int k = 0; k < certs.length; ++k) {
	        X509Certificate cert = (X509Certificate)certs[k];
	        String err = verifyCertificate(cert, crls, calendar);
	        if (err != null)
	            result.add(new VerificationException(cert, err));
	        try {
	            for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements();) {
	                try {
	                    String alias = aliases.nextElement();
	                    if (!keystore.isCertificateEntry(alias))
	                        continue;
	                    X509Certificate certStoreX509 = (X509Certificate)keystore.getCertificate(alias);
	                    if (verifyCertificate(certStoreX509, crls, calendar) != null)
	                        continue;
	                    try {
	                        cert.verify(certStoreX509.getPublicKey());
	                        return result;
	                    }
	                    catch (Exception e) {
	                        continue;
	                    }
	                }
	                catch (Exception ex) {
	                }
	            }
	        }
	        catch (Exception e) {
	        }
	        int j;
	        for (j = 0; j < certs.length; ++j) {
	            if (j == k)
	                continue;
	            X509Certificate certNext = (X509Certificate)certs[j];
	            try {
	                cert.verify(certNext.getPublicKey());
	                break;
	            }
	            catch (Exception e) {
	            }
	        }
	        if (j == certs.length) {
	        	result.add(new VerificationException(cert, "Cannot be verified against the KeyStore or the certificate chain"));
	        }
	    }
	    if (result.size() == 0)
	    	result.add(new VerificationException(null, "Invalid state. Possible circular certificate chain"));
	    return result;
	}

	/**
	 * Verifies a certificate chain against a KeyStore.
	 * @param certs the certificate chain
	 * @param keystore the <CODE>KeyStore</CODE>
	 * @param calendar the date or <CODE>null</CODE> for the current date
	 * @return <CODE>null</CODE> if the certificate chain could be validated or a
	 * <CODE>Object[]{cert,error}</CODE> where <CODE>cert</CODE> is the
	 * failed certificate and <CODE>error</CODE> is the error message
	 */
	public static List<VerificationException> verifyCertificates(Certificate certs[], KeyStore keystore, Calendar calendar) {
		return verifyCertificates(certs, keystore, null, calendar);
	}
	
	/**
	 * Verifies an OCSP response against a KeyStore.
	 * @param ocsp the OCSP response
	 * @param keystore the <CODE>KeyStore</CODE>
	 * @param provider the provider or <CODE>null</CODE> to use the BouncyCastle provider
	 * @return <CODE>true</CODE> is a certificate was found
	 */
	public static boolean verifyOcspCertificates(BasicOCSPResp ocsp, KeyStore keystore, String provider) {
	    if (provider == null)
	        provider = "BC";
	    try {
	        for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements();) {
	            try {
	                String alias = aliases.nextElement();
	                if (!keystore.isCertificateEntry(alias))
	                    continue;
	                X509Certificate certStoreX509 = (X509Certificate)keystore.getCertificate(alias);
	                if (ocsp.isSignatureValid(new JcaContentVerifierProviderBuilder().setProvider(provider).build(certStoreX509.getPublicKey())))
	                    return true;
	            }
	            catch (Exception ex) {
	            }
	        }
	    }
	    catch (Exception e) {
	    }
	    return false;
	}

	/**
	 * Verifies a time stamp against a KeyStore.
	 * @param ts the time stamp
	 * @param keystore the <CODE>KeyStore</CODE>
	 * @param provider the provider or <CODE>null</CODE> to use the BouncyCastle provider
	 * @return <CODE>true</CODE> is a certificate was found
	 */
	public static boolean verifyTimestampCertificates(TimeStampToken ts, KeyStore keystore, String provider) {
	    if (provider == null)
	        provider = "BC";
	    try {
	        for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements();) {
	            try {
	                String alias = aliases.nextElement();
	                if (!keystore.isCertificateEntry(alias))
	                    continue;
	                X509Certificate certStoreX509 = (X509Certificate)keystore.getCertificate(alias);
	                ts.isSignatureValid(new JcaSimpleSignerInfoVerifierBuilder().setProvider(provider).build(certStoreX509));
	                return true;
	            }
	            catch (Exception ex) {
	            }
	        }
	    }
	    catch (Exception e) {
	    }
	    return false;
	}

}
