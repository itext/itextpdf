package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.security.ExternalDecryptionProcess;

import java.security.Key;
import java.security.cert.Certificate;

public class ReaderProperties {

    Certificate certificate = null;
    Key certificateKey = null;
    String certificateKeyProvider = null;
    ExternalDecryptionProcess externalDecryptionProcess = null;
    byte[] ownerPassword = null;
    boolean partialRead = false;
    boolean closeSourceOnconstructorError = true;
    MemoryLimitsAwareHandler memoryLimitsAwareHandler = null;

    public ReaderProperties setCertificate(Certificate certificate) {
        this.certificate = certificate;
        return this;
    }

    public ReaderProperties setCertificateKey(Key certificateKey) {
        this.certificateKey = certificateKey;
        return this;
    }

    public ReaderProperties setCertificateKeyProvider(String certificateKeyProvider) {
        this.certificateKeyProvider = certificateKeyProvider;
        return this;
    }

    public ReaderProperties setExternalDecryptionProcess(ExternalDecryptionProcess externalDecryptionProcess) {
        this.externalDecryptionProcess = externalDecryptionProcess;
        return this;
    }

    public ReaderProperties setOwnerPassword(byte[] ownerPassword) {
        this.ownerPassword = ownerPassword;
        return this;
    }

    public ReaderProperties setPartialRead(boolean partialRead) {
        this.partialRead = partialRead;
        return this;
    }

    public ReaderProperties setCloseSourceOnconstructorError(boolean closeSourceOnconstructorError) {
        this.closeSourceOnconstructorError = closeSourceOnconstructorError;
        return this;
    }

    public ReaderProperties setMemoryLimitsAwareHandler(MemoryLimitsAwareHandler memoryLimitsAwareHandler) {
        this.memoryLimitsAwareHandler = memoryLimitsAwareHandler;
        return this;
    }
}
