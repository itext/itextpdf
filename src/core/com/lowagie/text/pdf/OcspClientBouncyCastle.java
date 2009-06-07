package com.lowagie.text.pdf;

import com.lowagie.text.ExceptionConverter;
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

/**
 *
 * @author psoares
 */
public class OcspClientBouncyCastle implements OcspClient {
    
    private X509Certificate rootCert;
    private X509Certificate checkCert;
    private String url;
    
    public OcspClientBouncyCastle(X509Certificate checkCert, X509Certificate rootCert, String url) {
        this.checkCert = checkCert;
        this.rootCert = rootCert;
        this.url = url;
    }
    
    private static OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException, IOException {
        //Add provider BC
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        // Generate the id for the certificate we are looking for
        CertificateID id = new CertificateID(CertificateID.HASH_SHA1, issuerCert, serialNumber);
        
        // basic request generation with nonce
        OCSPReqGenerator gen = new OCSPReqGenerator();
        
        gen.addRequest(id);
        
        // create details for nonce extension
        Vector oids = new Vector();
        Vector values = new Vector();
        
        oids.add(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        values.add(new X509Extension(false, new DEROctetString(new DEROctetString(PdfEncryption.createDocumentId()).getEncoded())));
        
        gen.setRequestExtensions(new X509Extensions(oids, values));
        
        return gen.generate();
    }
    
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
                throw new IOException("Invalid HTTP response");
            }
            //Get Response
            InputStream in = (InputStream) con.getContent();
            OCSPResp ocspResponse = new OCSPResp(in);

            if (ocspResponse.getStatus() != 0)
                throw new IOException("Invalid status: " + ocspResponse.getStatus());
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
                        throw new IOException("OCSP Status is revoked!");
                    }
                    else {
                        throw new IOException("OCSP Status is unknown!");
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
