/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Vector;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.ocsp.BasicOCSPResp;
import org.bouncycastle.ocsp.CertificateID;
import org.bouncycastle.ocsp.CertificateStatus;
import org.bouncycastle.ocsp.OCSPException;
import org.bouncycastle.ocsp.OCSPReq;
import org.bouncycastle.ocsp.OCSPReqGenerator;
import org.bouncycastle.ocsp.OCSPResp;
import org.bouncycastle.ocsp.SingleResp;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * OcspClient implementation using BouncyCastle.
 * @author psoares
 * @since	2.1.6
 */
public class OcspClientBouncyCastle implements OcspClient {
    /** root certificate */
    private X509Certificate rootCert;
    /** check certificate */
    private X509Certificate checkCert;
    /** OCSP URL */
    private String url;

    /**
     * Creates an instance of an OcspClient that will be using BouncyCastle.
     * @param checkCert	the check certificate
     * @param rootCert	the root certificate
     * @param url	the OCSP URL
     */
    public OcspClientBouncyCastle(X509Certificate checkCert, X509Certificate rootCert, String url) {
        this.checkCert = checkCert;
        this.rootCert = rootCert;
        this.url = url;
    }

    /**
     * Generates an OCSP request using BouncyCastle.
     * @param issuerCert	certificate of the issues
     * @param serialNumber	serial number
     * @return	an OCSP request
     * @throws OCSPException
     * @throws IOException
     */
    private static OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException, IOException {
        //Add provider BC
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Generate the id for the certificate we are looking for
        CertificateID id = new CertificateID(CertificateID.HASH_SHA1, issuerCert, serialNumber);

        // basic request generation with nonce
        OCSPReqGenerator gen = new OCSPReqGenerator();

        gen.addRequest(id);

        // create details for nonce extension
        Vector<DERObjectIdentifier> oids = new Vector<DERObjectIdentifier>();
        Vector<X509Extension> values = new Vector<X509Extension>();

        oids.add(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        values.add(new X509Extension(false, new DEROctetString(new DEROctetString(PdfEncryption.createDocumentId()).getEncoded())));

        gen.setRequestExtensions(new X509Extensions(oids, values));

        return gen.generate();
    }

    /**
     * @return 	a byte array
     * @see com.itextpdf.text.pdf.OcspClient#getEncoded()
     */
    public byte[] getEncoded() {
        try {
            OCSPReq request = generateOCSPRequest(rootCert, checkCert.getSerialNumber());
            byte[] array = request.getEncoded();
            URL urlt = new URL(url);
            HttpURLConnection con = (HttpURLConnection)urlt.openConnection();
            con.setRequestProperty("Content-Type", "application/ocsp-request");
            con.setRequestProperty("Accept", "application/ocsp-response");
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
            dataOut.write(array);
            dataOut.flush();
            dataOut.close();
            if (con.getResponseCode() / 100 != 2) {
                throw new IOException(MessageLocalization.getComposedMessage("invalid.http.response.1", con.getResponseCode()));
            }
            //Get Response
            InputStream in = (InputStream) con.getContent();
            OCSPResp ocspResponse = new OCSPResp(in);

            if (ocspResponse.getStatus() != 0)
                throw new IOException(MessageLocalization.getComposedMessage("invalid.status.1", ocspResponse.getStatus()));
            BasicOCSPResp basicResponse = (BasicOCSPResp) ocspResponse.getResponseObject();
            if (basicResponse != null) {
                SingleResp[] responses = basicResponse.getResponses();
                if (responses.length == 1) {
                    SingleResp resp = responses[0];
                    Object status = resp.getCertStatus();
                    if (status == CertificateStatus.GOOD) {
                        return basicResponse.getEncoded();
                    }
                    else if (status instanceof org.bouncycastle.ocsp.RevokedStatus) {
                        throw new IOException(MessageLocalization.getComposedMessage("ocsp.status.is.revoked"));
                    }
                    else {
                        throw new IOException(MessageLocalization.getComposedMessage("ocsp.status.is.unknown"));
                    }
                }
            }
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
        return null;
    }
}
