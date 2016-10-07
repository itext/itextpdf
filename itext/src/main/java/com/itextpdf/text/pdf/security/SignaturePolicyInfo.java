package com.itextpdf.text.pdf.security;

import com.itextpdf.text.pdf.codec.Base64;
import org.spongycastle.asn1.DERIA5String;
import org.spongycastle.asn1.DERObjectIdentifier;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.esf.*;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;

/**
 * Class that encapsulates the signature policy information
 * @author J. Arturo
 *
 * Sample:
 *
 *      SignaturePolicyInfo spi = new SignaturePolicyInfo("2.16.724.1.3.1.1.2.1.9",
 *      "G7roucf600+f03r/o0bAOQ6WAs0=", "SHA-1", "https://sede.060.gob.es/politica_de_firma_anexo_1.pdf");
 */
public class SignaturePolicyInfo {
    private String policyIdentifier;
    private byte[] policyHash;
    private String policyDigestAlgorithm;
    private String policyUri;

    public SignaturePolicyInfo(String policyIdentifier, byte[] policyHash, String policyDigestAlgorithm, String policyUri) {
        if (policyIdentifier == null || policyIdentifier.length() == 0) {
            throw new IllegalArgumentException("Policy identifier cannot be null");
        }
        if (policyHash == null) {
            throw new IllegalArgumentException("Policy hash cannot be null");
        }
        if (policyDigestAlgorithm == null || policyDigestAlgorithm.length() == 0) {
            throw new IllegalArgumentException("Policy digest algorithm cannot be null");
        }

        this.policyIdentifier = policyIdentifier;
        this.policyHash = policyHash;
        this.policyDigestAlgorithm = policyDigestAlgorithm;
        this.policyUri = policyUri;
    }

    public SignaturePolicyInfo(String policyIdentifier, String policyHashBase64, String policyDigestAlgorithm, String policyUri) {
        this(policyIdentifier, policyHashBase64 != null ? Base64.decode(policyHashBase64) : null, policyDigestAlgorithm, policyUri);
    }

    public String getPolicyIdentifier() {
        return policyIdentifier;
    }

    public byte[] getPolicyHash() {
        return policyHash;
    }

    public String getPolicyDigestAlgorithm() {
        return policyDigestAlgorithm;
    }

    public String getPolicyUri() {
        return policyUri;
    }

    SignaturePolicyIdentifier toSignaturePolicyIdentifier() {
        String algId = DigestAlgorithms.getAllowedDigests(this.policyDigestAlgorithm);

        if (algId == null || algId.length() == 0) {
            throw new IllegalArgumentException("Invalid policy hash algorithm");
        }

        SignaturePolicyIdentifier signaturePolicyIdentifier = null;
        SigPolicyQualifierInfo spqi = null;

        if (this.policyUri != null && this.policyUri.length() > 0) {
            spqi = new SigPolicyQualifierInfo(PKCSObjectIdentifiers.id_spq_ets_uri, new DERIA5String(this.policyUri));
        }
        SigPolicyQualifiers qualifiers = new SigPolicyQualifiers(new SigPolicyQualifierInfo[] {spqi});

        signaturePolicyIdentifier = new SignaturePolicyIdentifier(new SignaturePolicyId(DERObjectIdentifier.getInstance(new DERObjectIdentifier(this.policyIdentifier.replace("urn:oid:", ""))),
                new OtherHashAlgAndValue(new AlgorithmIdentifier(algId), new DEROctetString(this.policyHash)), qualifiers));

        return signaturePolicyIdentifier;
    }
}
