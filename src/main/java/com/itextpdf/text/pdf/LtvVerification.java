package com.itextpdf.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;

/**
 *
 * @author psoares
 */
public class LtvVerification {
    private PdfWriter writer;
    private PdfReader reader;
    private AcroFields acroFields;
    private Map<PdfName,ValidationData> validated = new HashMap<PdfName,ValidationData>();


    public LtvVerification(PdfStamper stp) {
        writer = stp.getWriter();
        reader = stp.getReader();
        acroFields = stp.getAcroFields();
    }

    public boolean AddVerification(String signatureName) throws Exception {
        PdfPKCS7 pk = acroFields.verifySignature(signatureName);
        Certificate[] xc = pk.getSignCertificateChain();
        String urlOcsp = PdfPKCS7.getOCSPURL((X509Certificate)xc[0]);
        ValidationData vd = new ValidationData();
        if (urlOcsp != null && xc.length > 1) {
            OcspClientBouncyCastle oc = new OcspClientBouncyCastle((X509Certificate)xc[0], (X509Certificate)xc[1], urlOcsp);
            vd.ocsp = oc.getEncoded();
        }
        else {
            String urlCrl = PdfPKCS7.getCrlUrl((X509Certificate)xc[0]);
            CrlClientImp cim = new CrlClientImp(urlCrl);
            vd.crl = cim.getEncoded();
        }
        if (vd.crl == null && vd.ocsp == null)
            return false;
        validated.put(GetSignatureHashKey(signatureName), vd);
        return true;
    }

    private PdfName GetSignatureHashKey(String signatureName) throws NoSuchAlgorithmException, IOException {
        PdfDictionary dic = acroFields.getSignatureDictionary(signatureName);
        PdfString contents = dic.getAsString(PdfName.CONTENTS);
        byte[] bc = contents.getOriginalBytes();
        byte[] bt = null;
        if (PdfName.ETSI_RFC3161.equals(PdfReader.getPdfObject(dic.get(PdfName.SUBFILTER)))) {
            ASN1InputStream din = new ASN1InputStream(new ByteArrayInputStream(bc));
            DERObject pkcs = din.readObject();
            bc = pkcs.getEncoded();
        }
        bt = HashBytesSha1(bc);
        ByteBuffer buf = new ByteBuffer();
        for (byte b : bt) {
            buf.appendHex(b);
        }
        return new PdfName(PdfEncodings.convertToString(buf.toByteArray(), null).toUpperCase());
    }

    private static byte[] HashBytesSha1(byte[] b) throws NoSuchAlgorithmException {
        MessageDigest sh = MessageDigest.getInstance("SHA1");
        return sh.digest(b);
    }

    private static class ValidationData {
        public byte[] crl;
        public byte[] ocsp;
    }
}
