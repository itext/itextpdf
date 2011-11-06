package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;

/**
 * Add verification according to PAdES-LTV (part 4)
 * @author psoares
 */
public class LtvVerification {
    private PdfStamperImp writer;
    private PdfReader reader;
    private AcroFields acroFields;
    private Map<PdfName,ValidationData> validated = new HashMap<PdfName,ValidationData>();
    private boolean used = false;
    /**
     * What type of verification to include 
     */
    public enum Level {
        /**
         * Include only OCSP
         */
        OCSP, 
        /**
         * Include only CRL
         */
        CRL, 
        /**
         * Include both OCSP and CRL
         */
        OCSP_CRL, 
        /**
         * Include CRL only if OCSP can't be read
         */
        OCSP_OPTIONAL_CRL
    }

    /**
     * Options for how many certificates to include
     */
    public enum CertificateOption {
        /**
         * Include verification just for the signing certificate
         */
        SIGNING_CERTIFICATE,
        /**
         * Include verification for the whole chain of certificates
         */
        WHOLE_CHAIN
    }
    
    /**
     * Certificate inclusion in the DSS and VRI dictionaries in the CERT and CERTS
     * keys
     */
    public enum CertificateInclusion {
        /**
         * Include certificates in the DSS and VRI dictionaries
         */
        YES,
        /**
         * Do not include certificates in the DSS and VRI dictionaries
         */
        NO
    }
    /**
     * The verification constructor
     * @param stp the PdfStamper to apply the validation to
     */
    LtvVerification(PdfStamper stp) {
        writer = (PdfStamperImp)stp.getWriter();
        reader = stp.getReader();
        acroFields = stp.getAcroFields();
    }

    /**
     * Add verification for a particular signature
     * @param signatureName the signature to validate (it may be a timestamp)
     * @param ocsp the interface to get the OCSP
     * @param crl the interface to get the CRL
     * @param certOption
     * @param level the validation options to include
     * @param certInclude
     * @return true if a validation was generated, false otherwise
     * @throws Exception
     */
    public boolean addVerification(String signatureName, OcspClient ocsp, CrlClient crl, CertificateOption certOption, Level level, CertificateInclusion certInclude) throws Exception {
        if (used)
            throw new IllegalStateException(MessageLocalization.getComposedMessage("verification.already.output"));
        PdfPKCS7 pk = acroFields.verifySignature(signatureName);
        Certificate[] xc = pk.getSignCertificateChain();
        ValidationData vd = new ValidationData();
        for (int k = 0; k < xc.length; ++k) {
            byte[] ocspEnc = null;
            if (ocsp != null && level != Level.CRL && k < xc.length - 1) {
                ocspEnc = ocsp.getEncoded((X509Certificate)xc[k], (X509Certificate)xc[k + 1], null);
                if (ocspEnc != null)
                    vd.ocsps.add(ocspEnc);
            }
            if (crl != null && (level != Level.OCSP || (level == Level.OCSP_OPTIONAL_CRL && ocspEnc == null))) {
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
            if (certOption == CertificateOption.SIGNING_CERTIFICATE)
                break;
        }
        if (vd.crls.isEmpty() && vd.ocsps.isEmpty())
            return false;
        if (certInclude == CertificateInclusion.YES) {
            for (Certificate c : xc) {
                vd.certs.add(c.getEncoded());
            }
        }
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

    /**
     * Merges the validation with any validation already in the document or creates
     * a new one.
     * @throws IOException 
     */
    void merge() throws IOException {
        if (used || validated.isEmpty())
            return;
        used = true;
        PdfDictionary catalog = reader.getCatalog();
        PdfObject dss = catalog.get(PdfName.DSS);
        if (dss == null)
            createDss();
        else
            updateDss();
    }
    
    private void updateDss() throws IOException {
        PdfDictionary catalog = reader.getCatalog();
        writer.markUsed(catalog);
        PdfDictionary dss = catalog.getAsDict(PdfName.DSS);
        PdfArray ocsps = dss.getAsArray(PdfName.OCSPS);
        PdfArray crls = dss.getAsArray(PdfName.CRLS);
        PdfArray certs = dss.getAsArray(PdfName.CERTS);
        dss.remove(PdfName.OCSPS);
        dss.remove(PdfName.CRLS);
        dss.remove(PdfName.CERTS);
        PdfDictionary vrim = dss.getAsDict(PdfName.VRI);
        //delete old validations
        if (vrim != null) {
            for (PdfName n : vrim.getKeys()) {
                if (validated.containsKey(n)) {
                    PdfDictionary vri = vrim.getAsDict(n);
                    if (vri != null) {
                        deleteOldReferences(ocsps, vri.getAsArray(PdfName.OCSP));
                        deleteOldReferences(crls, vri.getAsArray(PdfName.CRL));
                        deleteOldReferences(certs, vri.getAsArray(PdfName.CERT));
                    }
                }
            }
        }
        if (ocsps == null)
            ocsps = new PdfArray();
        if (crls == null)
            crls = new PdfArray();
        if (certs == null)
            certs = new PdfArray();
        outputDss(dss, vrim, ocsps, crls, certs);
    }
    
    private static void deleteOldReferences(PdfArray all, PdfArray toDelete) {
        if (all == null || toDelete == null)
            return;
        for (PdfObject pi : toDelete) {
            if (!pi.isIndirect())
                continue;
            PRIndirectReference pir = (PRIndirectReference)pi;
            for (int k = 0; k < all.size(); ++k) {
                PdfObject po = all.getPdfObject(k);
                if (!po.isIndirect())
                    continue;
                PRIndirectReference pod = (PRIndirectReference)po;
                if (pir.getNumber() == pod.getNumber()) {
                    all.remove(k);
                    --k;
                }
            }
        }
    }
    
    private void createDss() throws IOException {
        outputDss(new PdfDictionary(), new PdfDictionary(), new PdfArray(), new PdfArray(), new PdfArray());
    }
    
    private void outputDss(PdfDictionary dss, PdfDictionary vrim, PdfArray ocsps, PdfArray crls, PdfArray certs) throws IOException {
        PdfDictionary catalog = reader.getCatalog();
        writer.markUsed(catalog);
        for (PdfName vkey : validated.keySet()) {
            PdfArray ocsp = new PdfArray();
            PdfArray crl = new PdfArray();
            PdfArray cert = new PdfArray();
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
            for (byte[] b : validated.get(vkey).certs) {
                PdfStream ps = new PdfStream(b);
                ps.flateCompress();
                PdfIndirectReference iref = writer.addToBody(ps, false).getIndirectReference();
                cert.add(iref);
                certs.add(iref);
            }
            if (ocsp.size() > 0)
                vri.put(PdfName.OCSP, writer.addToBody(ocsp, false).getIndirectReference());
            if (crl.size() > 0)
                vri.put(PdfName.CRL, writer.addToBody(crl, false).getIndirectReference());
            if (cert.size() > 0)
                vri.put(PdfName.CERT, writer.addToBody(cert, false).getIndirectReference());
            vrim.put(vkey, writer.addToBody(vri, false).getIndirectReference());
        }
        dss.put(PdfName.VRI, writer.addToBody(vrim, false).getIndirectReference());
        if (ocsps.size() > 0)
            dss.put(PdfName.OCSPS, writer.addToBody(ocsps, false).getIndirectReference());
        if (crls.size() > 0)
            dss.put(PdfName.CRLS, writer.addToBody(crls, false).getIndirectReference());
        if (certs.size() > 0)
            dss.put(PdfName.CERTS, writer.addToBody(certs, false).getIndirectReference());
        catalog.put(PdfName.DSS, writer.addToBody(dss, false).getIndirectReference());
    }
    
    private static class ValidationData {
        public List<byte[]> crls = new ArrayList<byte[]>();
        public List<byte[]> ocsps = new ArrayList<byte[]>();
        public List<byte[]> certs = new ArrayList<byte[]>();
    }
}
