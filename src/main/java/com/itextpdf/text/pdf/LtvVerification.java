package com.itextpdf.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public boolean AddVerification(String signatureName, OcspClient ocsp, CrlClient crl, boolean checkAllCertificates) throws Exception {
        PdfPKCS7 pk = acroFields.verifySignature(signatureName);
        Certificate[] xc = pk.getSignCertificateChain();
        ValidationData vd = new ValidationData();
        for (int k = 0; k < xc.length; ++k) {
            byte[] ocspEnc = null;
            if (k < xc.length - 1) {
                ocspEnc = ocsp.getEncoded((X509Certificate)xc[k], (X509Certificate)xc[k + 1], null);
                if (ocspEnc != null)
                    vd.ocsps.add(ocspEnc);
            }
            if (ocspEnc == null) {
                byte[] cim = crl.getEncoded((X509Certificate)xc[k], null);
                if (cim != null) {
                    boolean dup = false;
                    for (byte[] b : vd.crls) {
                        if (Arrays.equals(b, cim)) {
                            dup = true;
                            break;
                        }
                    }
                    if (!dup)
                        vd.crls.add(cim);
                }
            }
            if (!checkAllCertificates)
                break;
        }
        if (vd.crls.isEmpty() && vd.ocsps.isEmpty())
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
        return new PdfName(ConvertToHex(bt));
    }

    private static String ConvertToHex(byte[] bt) {
        ByteBuffer buf = new ByteBuffer();
        for (byte b : bt) {
            buf.appendHex(b);
        }
        return PdfEncodings.convertToString(buf.toByteArray(), null).toUpperCase();
    }
    
    private static byte[] HashBytesSha1(byte[] b) throws NoSuchAlgorithmException {
        MessageDigest sh = MessageDigest.getInstance("SHA1");
        return sh.digest(b);
    }

    private static class ValidationData {
        public List<byte[]> crls = new ArrayList<byte[]>();
        public List<byte[]> ocsps = new ArrayList<byte[]>();
    }
}
