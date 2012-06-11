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
package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.cert.CRL;
import java.security.cert.Certificate;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.security.CertificateInfo;

/**
 * A signature dictionary representation for the standard filters.
 */
public abstract class PdfSigGenericPKCS extends PdfSignature {

    /**
     * The crypto provider
     */    
    protected String provider = null;
    /**
     * The class instance that calculates the PKCS#1 and PKCS#7
     */    
    protected PdfPKCS7 pkcs;
    /**
     * The subject name in the signing certificate (the element "CN")
     */    
    protected String   name;

    /**
     * The hash algorithm, for example "SHA1"
     */    
    protected String hashAlgorithm;
    private String digestEncryptionAlgorithm;
    
    private byte externalDigest[];
    private byte externalRSAdata[];

    /**
     * Creates a generic standard filter.
     * @param filter the filter name
     * @param subFilter the sub-filter name
     */    
    public PdfSigGenericPKCS(PdfName filter, PdfName subFilter) {
        super(filter, subFilter);
    }

    /**
     * Sets the crypto information to sign.
     * @param privKey the private key
     * @param certChain the certificate chain
     * @param crlList the certificate revocation list. It can be <CODE>null</CODE>
     */    
    public void setSignInfo(PrivateKey privKey, Certificate[] certChain, CRL[] crlList) {
        try {
            pkcs = new PdfPKCS7(privKey, certChain, crlList, hashAlgorithm, provider, PdfName.ADBE_PKCS7_SHA1.equals(get(PdfName.SUBFILTER)));
            pkcs.setExternalDigest(externalDigest, externalRSAdata, digestEncryptionAlgorithm);
            if (PdfName.ADBE_X509_RSA_SHA1.equals(get(PdfName.SUBFILTER))) {
                if (certChain.length > 1) {
                    PdfArray arr = new PdfArray();
                    for (int ii = 0; ii < certChain.length; ii++) {
                        arr.add(new PdfString(certChain[ii].getEncoded()));
                    }
                    put(PdfName.CERT, arr);
                }
                else {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    for (int k = 0; k < certChain.length; ++k) {
                        bout.write(certChain[k].getEncoded());
                    }
                    bout.close();
                    setCert(bout.toByteArray());
                }
                setContents(pkcs.getEncodedPKCS1());
            }
            else
                setContents(pkcs.getEncodedPKCS7());
            name = CertificateInfo.getSubjectFields(pkcs.getSigningCertificate()).getField("CN");
            if (name != null)
                put(PdfName.NAME, new PdfString(name, PdfObject.TEXT_UNICODE));
            pkcs = new PdfPKCS7(privKey, certChain, crlList, hashAlgorithm, provider, PdfName.ADBE_PKCS7_SHA1.equals(get(PdfName.SUBFILTER)));
            pkcs.setExternalDigest(externalDigest, externalRSAdata, digestEncryptionAlgorithm);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Sets the digest/signature to an external calculated value.
     * @param digest the digest. This is the actual signature
     * @param RSAdata the extra data that goes into the data tag in PKCS#7
     * @param digestEncryptionAlgorithm the encryption algorithm. It may must be <CODE>null</CODE> if the <CODE>digest</CODE>
     * is also <CODE>null</CODE>. If the <CODE>digest</CODE> is not <CODE>null</CODE>
     * then it may be "RSA" or "DSA"
     */    
    public void setExternalDigest(byte digest[], byte RSAdata[], String digestEncryptionAlgorithm) {
        externalDigest = digest;
        externalRSAdata = RSAdata;
        this.digestEncryptionAlgorithm = digestEncryptionAlgorithm;
    }

    /**
     * Gets the subject name in the signing certificate (the element "CN")
     * @return the subject name in the signing certificate (the element "CN")
     */    
    public String getName() {
        return name;
    }

    /**
     * Gets the class instance that does the actual signing.
     * @return the class instance that does the actual signing
     */    
    public PdfPKCS7 getSigner() {
        return pkcs;
    }

    /**
     * Gets the signature content. This can be a PKCS#1 or a PKCS#7. It corresponds to
     * the /Contents key.
     * @return the signature content
     */    
    public byte[] getSignerContents() {
        if (PdfName.ADBE_X509_RSA_SHA1.equals(get(PdfName.SUBFILTER)))
            return pkcs.getEncodedPKCS1();
        else
            return pkcs.getEncodedPKCS7();
    }

    /**
     * Creates a standard filter of the type VeriSign.
     */    
    public static class VeriSign extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */        
        public VeriSign() {
            super(PdfName.VERISIGN_PPKVS, PdfName.ADBE_PKCS7_DETACHED);
            hashAlgorithm = "MD5";
            put(PdfName.R, new PdfNumber(65537));
        }

        /**
         * The constructor for an explicit provider.
         * @param provider the crypto provider
         */        
        public VeriSign(String provider) {
            this();
            this.provider = provider;
        }
    }

    /**
     * Creates a standard filter of the type self signed.
     */    
    public static class PPKLite extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */        
        public PPKLite() {
            super(PdfName.ADOBE_PPKLITE, PdfName.ADBE_X509_RSA_SHA1);
            hashAlgorithm = "SHA1";
            put(PdfName.R, new PdfNumber(65541));
        }

        /**
         * The constructor for an explicit provider.
         * @param provider the crypto provider
         */        
        public PPKLite(String provider) {
            this();
            this.provider = provider;
        }
    }

    /**
     * Creates a standard filter of the type Windows Certificate.
     * @deprecated See ISO-32000-2, adbe.pkcs7.sha1 should no longer be used
     */    
    public static class PPKMS extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */        
        public PPKMS() {
            super(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_SHA1);
            hashAlgorithm = "SHA1";
        }

        /**
         * The constructor for an explicit provider.
         * @param provider the crypto provider
         */        
        public PPKMS(String provider) {
            this();
            this.provider = provider;
        }
    }
}
