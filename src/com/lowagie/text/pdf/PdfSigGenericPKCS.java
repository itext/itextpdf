/*
 * Copyright 2004 by Paulo Soares.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.text.pdf;

import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.security.cert.CRL;
import java.security.PrivateKey;
import com.lowagie.text.ExceptionConverter;
import java.io.ByteArrayOutputStream;

public abstract class PdfSigGenericPKCS extends PdfSignature {
    protected String hashAlgorithm;
    protected String provider = null;
    protected PdfPKCS7 pkcs;
    protected String   name;

    public PdfSigGenericPKCS(PdfName filter, PdfName subFilter) {
        super(filter, subFilter);
    }

    public void setSignInfo(PrivateKey privKey, Certificate[] certChain, CRL[] crlList) {
        try {
            pkcs = new PdfPKCS7(privKey, certChain, crlList, hashAlgorithm, provider, PdfName.ADBE_PKCS7_SHA1.equals(get(PdfName.SUBFILTER)));
            if (PdfName.ADBE_X509_RSA_SHA1.equals(get(PdfName.SUBFILTER))) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                for (int k = 0; k < certChain.length; ++k) {
                    bout.write(certChain[k].getEncoded());
                }
                bout.close();
                setCert(bout.toByteArray());
                setContents(pkcs.getEncodedPKCS1());
            }
            else
                setContents(pkcs.getEncodedPKCS7());
            name = PdfPKCS7.getSubjectFields(pkcs.getSigningCertificate()).getField("CN");
            if (name != null)
                put(PdfName.NAME, new PdfString(name, PdfObject.TEXT_UNICODE));
            put(PdfName.M, new PdfDate());
            pkcs = new PdfPKCS7(privKey, certChain, crlList, hashAlgorithm, provider, PdfName.ADBE_PKCS7_SHA1.equals(get(PdfName.SUBFILTER)));
            put(PdfName.BYTERANGE, new PdfLiteral("[0                                 "));
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public String getName() {
        return name;
    }

    public PdfPKCS7 getSigner() {
        return pkcs;
    }

    public byte[] getSignerContents() {
        if (PdfName.ADBE_X509_RSA_SHA1.equals(get(PdfName.SUBFILTER)))
            return pkcs.getEncodedPKCS1();
        else
            return pkcs.getEncodedPKCS7();
    }

    public static class VeriSign extends PdfSigGenericPKCS {
        public VeriSign() {
            super(PdfName.VERISIGN_PPKVS, PdfName.ADBE_PKCS7_DETACHED);
            hashAlgorithm = "MD5";
            put(PdfName.R, new PdfNumber(65537));
        }

        public VeriSign(String provider) {
            this();
            this.provider = provider;
        }
    }

    public static class PPKLite extends PdfSigGenericPKCS {
        public PPKLite() {
            super(PdfName.ADOBE_PPKLITE, PdfName.ADBE_X509_RSA_SHA1);
            hashAlgorithm = "SHA1";
            put(PdfName.R, new PdfNumber(65541));
        }

        public PPKLite(String provider) {
            this();
            this.provider = provider;
        }
    }

    public static class PPKMS extends PdfSigGenericPKCS {
        public PPKMS() {
            super(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_SHA1);
            hashAlgorithm = "SHA1";
        }

        public PPKMS(String provider) {
            this();
            this.provider = provider;
        }
    }
}
