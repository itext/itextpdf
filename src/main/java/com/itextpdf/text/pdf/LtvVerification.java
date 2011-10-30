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
    private PdfStamperImp writer;
    private PdfReader reader;
    private AcroFields acroFields;
    private Map<PdfName,ValidationData> validated = new HashMap<PdfName,ValidationData>();


    public LtvVerification(PdfStamper stp) {
        writer = (PdfStamperImp)stp.getWriter();
        reader = stp.getReader();
        acroFields = stp.getAcroFields();
    }

    public boolean addVerification(String signatureName, OcspClient ocsp, CrlClient crl, boolean checkAllCertificates, boolean includeOcspAndCrl) throws Exception {
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
            if (includeOcspAndCrl || ocspEnc == null) {
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
        validated.put(getSignatureHashKey(signatureName), vd);
        return true;
    }

    private PdfName getSignatureHashKey(String signatureName) throws NoSuchAlgorithmException, IOException {
        PdfDictionary dic = acroFields.getSignatureDictionary(signatureName);
        PdfString contents = dic.getAsString(PdfName.CONTENTS);
        byte[] bc = contents.getOriginalBytes();
        byte[] bt = null;
        if (PdfName.ETSI_RFC3161.equals(PdfReader.getPdfObject(dic.get(PdfName.SUBFILTER)))) {
            ASN1InputStream din = new ASN1InputStream(new ByteArrayInputStream(bc));
            DERObject pkcs = din.readObject();
            bc = pkcs.getEncoded();
        }
        bt = hashBytesSha1(bc);
        return new PdfName(convertToHex(bt));
    }

    private static String convertToHex(byte[] bt) {
        ByteBuffer buf = new ByteBuffer();
        for (byte b : bt) {
            buf.appendHex(b);
        }
        return PdfEncodings.convertToString(buf.toByteArray(), null).toUpperCase();
    }
    
    private static byte[] hashBytesSha1(byte[] b) throws NoSuchAlgorithmException {
        MessageDigest sh = MessageDigest.getInstance("SHA1");
        return sh.digest(b);
    }

    public void merge() throws IOException {
        if (validated.isEmpty())
            return;
        createDss();
//        PdfDictionary catalog = reader.getCatalog();
//        PdfObject dss = catalog.get(PdfName.DSS);
//        if (dss == null)
//            createDss();
//        else
//            updateDss();
    }
    
    private void createDss() throws IOException {
        PdfDictionary catalog = reader.getCatalog();
        writer.markUsed(catalog);
        PdfArray ocsps = new PdfArray();
        PdfArray crls = new PdfArray();
        PdfDictionary vrim = new PdfDictionary();
        for (PdfName vkey : validated.keySet()) {
            PdfArray ocsp = new PdfArray();
            PdfArray crl = new PdfArray();
            PdfDictionary vri = new PdfDictionary();
            for (byte[] b : validated.get(vkey).crls) {
                PdfStream ps = new PdfStream(b);
                ps.flateCompress();
                PdfIndirectReference iref = writer.addToBody(ps, false).getIndirectReference();
                crl.add(iref);
                crls.add(iref);
            }
            for (byte[] b : validated.get(vkey).ocsps) {
                PdfStream ps = new PdfStream(b);
                ps.flateCompress();
                PdfIndirectReference iref = writer.addToBody(ps, false).getIndirectReference();
                ocsp.add(iref);
                ocsps.add(iref);
            }
            if (ocsp.size() > 0)
                vri.put(PdfName.OCSP, writer.addToBody(ocsp, false).getIndirectReference());
            if (crl.size() > 0)
                vri.put(PdfName.CRL, writer.addToBody(crl, false).getIndirectReference());
            vrim.put(vkey, writer.addToBody(vri, false).getIndirectReference());
        }
        PdfDictionary dss = new PdfDictionary();
        dss.put(PdfName.VRI, writer.addToBody(vrim, false).getIndirectReference());
        if (ocsps.size() > 0)
            dss.put(PdfName.OCSPS, writer.addToBody(ocsps, false).getIndirectReference());
        if (crls.size() > 0)
            dss.put(PdfName.CRLS, writer.addToBody(crls, false).getIndirectReference());
        catalog.put(PdfName.DSS, writer.addToBody(dss, false).getIndirectReference());
    }
    
    private static class ValidationData {
        public List<byte[]> crls = new ArrayList<byte[]>();
        public List<byte[]> ocsps = new ArrayList<byte[]>();
    }
}
