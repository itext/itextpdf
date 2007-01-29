package com.lowagie.text.pdf.interfaces;

import java.security.cert.Certificate;

import com.lowagie.text.DocumentException;

/**
 * Encryption settings are described in section 3.5 (more specifically
 * section 3.5.2) of the PDF Reference 1.7.
 * They are explained in section 3.3.3 of the book 'iText in Action'.
 * The values of the different  preferences were originally stored
 * in class PdfWriter, but they have been moved to this separate interface
 * for reasons of convenience.
 */

public interface PdfEncryptionSettings {

	// types of encryption
	
    /** Type of encryption */
    public static final int ENCRYPTION_RC4_40 = 0;
    /** Type of encryption */
    public static final int ENCRYPTION_RC4_128 = 1;
    /** Type of encryption */
    public static final int ENCRYPTION_AES_128 = 2;
    /** Mask to separate the encryption type from the encryption mode. */
    static final int ENCRYPTION_MASK = 7;
    /** Add this to the mode to keep the metadata in clear text */
    public static final int DO_NOT_ENCRYPT_METADATA = 8;
    
	// permissions
	
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowPrinting = 4 + 2048;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowModifyContents = 8;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowCopy = 16;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowModifyAnnotations = 32;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowFillIn = 256;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowScreenReaders = 512;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowAssembly = 1024;
    /** The operation permitted when the document is opened with the user password */
    public static final int AllowDegradedPrinting = 4;
    
    // Strength of the RC4 encryption (kept for historical reasons)
    /** Type of RC4 encryption strength*/
    public static final boolean STRENGTH40BITS = false;
    /** Type of RC4 encryption strength */
    public static final boolean STRENGTH128BITS = true;

    
    /**
     * Sets the encryption options for this document. The userPassword and the
     * ownerPassword can be null or have zero length. In this case the ownerPassword
     * is replaced by a random string. The open permissions for the document can be
     * AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     * AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     * The permissions can be combined by ORing them.
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param encryptionType the type of encryption. It can be one of ENCRYPTION_RC4_40, ENCRYPTION_RC4_128 or ENCRYPTION_AES128.
     * Optionally DO_NOT_ENCRYPT_METADATA can be ored to output the metadata in cleartext
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(byte userPassword[], byte ownerPassword[], int permissions, int encryptionType) throws DocumentException;

    /**
     * Sets the certificate encryption options for this document. An array of one or more public certificates
     * must be provided together with an array of the same size for the permissions for each certificate.
     *  The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * Optionally DO_NOT_ENCRYPT_METADATA can be ored to output the metadata in cleartext
     * @param certs the public certificates to be used for the encryption
     * @param permissions the user permissions for each of the certicates
     * @param encryptionType the type of encryption. It can be one of ENCRYPTION_RC4_40, ENCRYPTION_RC4_128 or ENCRYPTION_AES128.
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException;
}