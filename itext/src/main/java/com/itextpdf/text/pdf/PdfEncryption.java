/*
 * $Id: PdfEncryption.java 6520 2014-08-27 15:19:32Z achingarev $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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

import com.itextpdf.text.pdf.crypto.ARCFOUREncryption;
import com.itextpdf.text.error_messages.MessageLocalization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;


import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.crypto.AESCipherCBCnoPad;
import com.itextpdf.text.pdf.crypto.IVGenerator;

/**
 * 
 * @author Paulo Soares
 * @author Kazuya Ujihara
 */
public class PdfEncryption {

	public static final int STANDARD_ENCRYPTION_40 = 2;

	public static final int STANDARD_ENCRYPTION_128 = 3;

	public static final int AES_128 = 4;

	public static final int AES_256 = 5;

	private static final byte[] pad = { (byte) 0x28, (byte) 0xBF, (byte) 0x4E,
			(byte) 0x5E, (byte) 0x4E, (byte) 0x75, (byte) 0x8A, (byte) 0x41,
			(byte) 0x64, (byte) 0x00, (byte) 0x4E, (byte) 0x56, (byte) 0xFF,
			(byte) 0xFA, (byte) 0x01, (byte) 0x08, (byte) 0x2E, (byte) 0x2E,
			(byte) 0x00, (byte) 0xB6, (byte) 0xD0, (byte) 0x68, (byte) 0x3E,
			(byte) 0x80, (byte) 0x2F, (byte) 0x0C, (byte) 0xA9, (byte) 0xFE,
			(byte) 0x64, (byte) 0x53, (byte) 0x69, (byte) 0x7A };

	private static final byte[] salt = { (byte) 0x73, (byte) 0x41, (byte) 0x6c,
			(byte) 0x54 };

	private static final byte[] metadataPad = { (byte) 255, (byte) 255,
			(byte) 255, (byte) 255 };

	/** The encryption key for a particular object/generation */
	byte key[];

	/** The encryption key length for a particular object/generation */
	int keySize;

	/** The global encryption key */
	byte mkey[] = new byte[0];

	/** Work area to prepare the object/generation bytes */
	byte extra[] = new byte[5];

	/** The message digest algorithm MD5 */
	MessageDigest md5;

	/** The encryption key for the owner */
	byte ownerKey[] = new byte[32];

	/** The encryption key for the user */
	byte userKey[] = new byte[32];

    byte[] oeKey;
    byte[] ueKey;
    byte[] perms;

	/** The public key security handler for certificate encryption */
	protected PdfPublicKeySecurityHandler publicKeyHandler = null;

	long permissions;

	byte documentID[];

	static long seq = System.currentTimeMillis();

	private int revision;
    

	private ARCFOUREncryption arcfour = new ARCFOUREncryption();

	/** The generic key length. It may be 40 or 128. */
	private int keyLength;

	private boolean encryptMetadata;
	
	/**
	 * Indicates if the encryption is only necessary for embedded files.
	 * @since 2.1.3
	 */
	private boolean embeddedFilesOnly;

	private int cryptoMode;

	public PdfEncryption() {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
		publicKeyHandler = new PdfPublicKeySecurityHandler();
	}

	public PdfEncryption(PdfEncryption enc) {
		this();
        if (enc.key != null)
            key = (byte[]) enc.key.clone();
        keySize = enc.keySize;
		mkey = (byte[]) enc.mkey.clone();
		ownerKey = (byte[]) enc.ownerKey.clone();
		userKey = (byte[]) enc.userKey.clone();
		permissions = enc.permissions;
		if (enc.documentID != null)
			documentID = (byte[]) enc.documentID.clone();
		revision = enc.revision;
		keyLength = enc.keyLength;
		encryptMetadata = enc.encryptMetadata;
		embeddedFilesOnly = enc.embeddedFilesOnly;
		publicKeyHandler = enc.publicKeyHandler;
	}

	public void setCryptoMode(int mode, int kl) {
		cryptoMode = mode;
		encryptMetadata = (mode & PdfWriter.DO_NOT_ENCRYPT_METADATA) != PdfWriter.DO_NOT_ENCRYPT_METADATA;
		embeddedFilesOnly = (mode & PdfWriter.EMBEDDED_FILES_ONLY) == PdfWriter.EMBEDDED_FILES_ONLY;
		mode &= PdfWriter.ENCRYPTION_MASK;
		switch (mode) {
		case PdfWriter.STANDARD_ENCRYPTION_40:
			encryptMetadata = true;
			embeddedFilesOnly = false;
			keyLength = 40;
			revision = STANDARD_ENCRYPTION_40;
			break;
		case PdfWriter.STANDARD_ENCRYPTION_128:
			embeddedFilesOnly = false;
			if (kl > 0)
				keyLength = kl;
			else
				keyLength = 128;
			revision = STANDARD_ENCRYPTION_128;
			break;
		case PdfWriter.ENCRYPTION_AES_128:
			keyLength = 128;
			revision = AES_128;
			break;
		case PdfWriter.ENCRYPTION_AES_256:
			keyLength = 256;
            keySize = 32;
			revision = AES_256;
			break;
		default:
			throw new IllegalArgumentException(MessageLocalization.getComposedMessage("no.valid.encryption.mode"));
		}
	}

	public int getCryptoMode() {
		return cryptoMode;
	}

	public boolean isMetadataEncrypted() {
		return encryptMetadata;
	}

    public long getPermissions() {
        return permissions;
    }

	/**
	 * Indicates if only the embedded files have to be encrypted.
	 * @return	if true only the embedded files will be encrypted
	 * @since	2.1.3
	 */
	public boolean isEmbeddedFilesOnly() {
		return embeddedFilesOnly;
	}

	/**
	 */
	private byte[] padPassword(byte userPassword[]) {
		byte userPad[] = new byte[32];
		if (userPassword == null) {
			System.arraycopy(pad, 0, userPad, 0, 32);
		} else {
			System.arraycopy(userPassword, 0, userPad, 0, Math.min(
					userPassword.length, 32));
			if (userPassword.length < 32)
				System.arraycopy(pad, 0, userPad, userPassword.length,
						32 - userPassword.length);
		}

		return userPad;
	}

	/**
	 */
	private byte[] computeOwnerKey(byte userPad[], byte ownerPad[]) {
        byte ownerKey[] = new byte[32];
        byte digest[] = md5.digest(ownerPad);
        if (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) {
            byte mkey[] = new byte[keyLength / 8];
            // only use for the input as many bit as the key consists of
            for (int k = 0; k < 50; ++k) {
                md5.update(digest, 0, mkey.length);
                System.arraycopy(md5.digest(), 0, digest, 0, mkey.length);
            }
            System.arraycopy(userPad, 0, ownerKey, 0, 32);
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < mkey.length; ++j)
                    mkey[j] = (byte) (digest[j] ^ i);
                arcfour.prepareARCFOURKey(mkey);
                arcfour.encryptARCFOUR(ownerKey);
            }
        } else {
            arcfour.prepareARCFOURKey(digest, 0, 5);
            arcfour.encryptARCFOUR(userPad, ownerKey);
        }
        return ownerKey;
	}

	/**
	 * 
	 * ownerKey, documentID must be setup
	 */
	private void setupGlobalEncryptionKey(byte[] documentID, byte userPad[],
			byte ownerKey[], long permissions) {
		this.documentID = documentID;
		this.ownerKey = ownerKey;
		this.permissions = permissions;
		// use variable keylength
		mkey = new byte[keyLength / 8];

		// fixed by ujihara in order to follow PDF reference
		md5.reset();
		md5.update(userPad);
		md5.update(ownerKey);

		byte ext[] = new byte[4];
		ext[0] = (byte) permissions;
		ext[1] = (byte) (permissions >> 8);
		ext[2] = (byte) (permissions >> 16);
		ext[3] = (byte) (permissions >> 24);
		md5.update(ext, 0, 4);
		if (documentID != null)
			md5.update(documentID);
		if (!encryptMetadata)
			md5.update(metadataPad);

		byte digest[] = new byte[mkey.length];
		System.arraycopy(md5.digest(), 0, digest, 0, mkey.length);

		// only use the really needed bits as input for the hash
		if (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) {
			for (int k = 0; k < 50; ++k)
				System.arraycopy(md5.digest(digest), 0, digest, 0, mkey.length);
		}

		System.arraycopy(digest, 0, mkey, 0, mkey.length);
	}

	/**
	 * 
	 * mkey must be setup
	 */
	// use the revision to choose the setup method
	private void setupUserKey() {
		if (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) {
			md5.update(pad);
			byte digest[] = md5.digest(documentID);
			System.arraycopy(digest, 0, userKey, 0, 16);
			for (int k = 16; k < 32; ++k)
				userKey[k] = 0;
			for (int i = 0; i < 20; ++i) {
				for (int j = 0; j < mkey.length; ++j)
					digest[j] = (byte) (mkey[j] ^ i);
				arcfour.prepareARCFOURKey(digest, 0, mkey.length);
				arcfour.encryptARCFOUR(userKey, 0, 16);
			}
		} else {
			arcfour.prepareARCFOURKey(mkey);
			arcfour.encryptARCFOUR(pad, userKey);
		}
	}

	// gets keylength and revision and uses revision to choose the initial values
	// for permissions
	public void setupAllKeys(byte userPassword[], byte ownerPassword[],
			int permissions) {
		if (ownerPassword == null || ownerPassword.length == 0)
			ownerPassword = md5.digest(createDocumentId());
		permissions |= (revision == STANDARD_ENCRYPTION_128 || revision == AES_128 || revision == AES_256) ? 0xfffff0c0
				: 0xffffffc0;
		permissions &= 0xfffffffc;
        this.permissions = permissions;
        if (revision == AES_256) {
            try {
                if (userPassword == null)
                    userPassword = new byte[0];
                documentID = createDocumentId();
                byte[] uvs = IVGenerator.getIV(8);
                byte[] uks = IVGenerator.getIV(8);
                key = IVGenerator.getIV(32);
                // Algorithm 3.8.1
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(userPassword, 0, Math.min(userPassword.length, 127));
                md.update(uvs);
                userKey = new byte[48];
                md.digest(userKey, 0, 32);
                System.arraycopy(uvs, 0, userKey, 32, 8);
                System.arraycopy(uks, 0, userKey, 40, 8);
                // Algorithm 3.8.2
                md.update(userPassword, 0, Math.min(userPassword.length, 127));
                md.update(uks);
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(true, md.digest());
                ueKey = ac.processBlock(key, 0, key.length);
                // Algorithm 3.9.1
                byte[] ovs = IVGenerator.getIV(8);
                byte[] oks = IVGenerator.getIV(8);
                md.update(ownerPassword, 0, Math.min(ownerPassword.length, 127));
                md.update(ovs);
                md.update(userKey);
                ownerKey = new byte[48];
                md.digest(ownerKey, 0, 32);
                System.arraycopy(ovs, 0, ownerKey, 32, 8);
                System.arraycopy(oks, 0, ownerKey, 40, 8);
                // Algorithm 3.9.2
                md.update(ownerPassword, 0, Math.min(ownerPassword.length, 127));
                md.update(oks);
                md.update(userKey);
                ac = new AESCipherCBCnoPad(true, md.digest());
                oeKey = ac.processBlock(key, 0, key.length);
                // Algorithm 3.10
                byte[] permsp = IVGenerator.getIV(16);
                permsp[0] = (byte)permissions;
                permsp[1] = (byte)(permissions >> 8);
                permsp[2] = (byte)(permissions >> 16);
                permsp[3] = (byte)(permissions >> 24);
                permsp[4] = (byte)(255);
                permsp[5] = (byte)(255);
                permsp[6] = (byte)(255);
                permsp[7] = (byte)(255);
                permsp[8] = encryptMetadata ? (byte)'T' : (byte)'F';
                permsp[9] = (byte)'a';
                permsp[10] = (byte)'d';
                permsp[11] = (byte)'b';
                ac = new AESCipherCBCnoPad(true, key);
                perms = ac.processBlock(permsp, 0, permsp.length);
            }
            catch (Exception ex) {
                throw new ExceptionConverter(ex);
            }
        }
        else {
            // PDF reference 3.5.2 Standard Security Handler, Algorithm 3.3-1
            // If there is no owner password, use the user password instead.
            byte userPad[] = padPassword(userPassword);
            byte ownerPad[] = padPassword(ownerPassword);

            this.ownerKey = computeOwnerKey(userPad, ownerPad);
            documentID = createDocumentId();
            setupByUserPad(this.documentID, userPad, this.ownerKey, permissions);
        }
	}

    private static final int VALIDATION_SALT_OFFSET = 32;
    private static final int KEY_SALT_OFFSET = 40;
    private static final int SALT_LENGHT = 8;
    private static final int OU_LENGHT = 48;

    public boolean readKey(PdfDictionary enc, byte[] password) throws BadPasswordException {
        try {
            if (password == null)
                password = new byte[0];
            byte[] oValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.O).toString());
            byte[] uValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.U).toString());
            byte[] oeValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.OE).toString());
            byte[] ueValue = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.UE).toString());
            byte[] perms = com.itextpdf.text.DocWriter.getISOBytes(enc.get(PdfName.PERMS).toString());
            boolean isUserPass = false;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password, 0, Math.min(password.length, 127));
            md.update(oValue, VALIDATION_SALT_OFFSET, SALT_LENGHT);
            md.update(uValue, 0, OU_LENGHT);
            byte[] hash = md.digest();
            boolean isOwnerPass = compareArray(hash, oValue, 32);
            if (isOwnerPass) {
                md.update(password, 0, Math.min(password.length, 127));
                md.update(oValue, KEY_SALT_OFFSET, SALT_LENGHT);
                md.update(uValue, 0, OU_LENGHT);
                hash = md.digest();
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, hash);
                key = ac.processBlock(oeValue, 0, oeValue.length);
            }
            else {
                md.update(password, 0, Math.min(password.length, 127));
                md.update(uValue, VALIDATION_SALT_OFFSET, SALT_LENGHT);
                hash = md.digest();
                isUserPass = compareArray(hash, uValue, 32);
                if (!isUserPass)
                    throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password"));
                md.update(password, 0, Math.min(password.length, 127));
                md.update(uValue, KEY_SALT_OFFSET, SALT_LENGHT);
                hash = md.digest();
                AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, hash);
                key = ac.processBlock(ueValue, 0, ueValue.length);
            }
            AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, key);
            byte[] decPerms = ac.processBlock(perms, 0, perms.length);
            if (decPerms[9] != (byte)'a' || decPerms[10] != (byte)'d' || decPerms[11] != (byte)'b')
                throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password"));
            permissions = (decPerms[0] & 0xff) | ((decPerms[1] & 0xff) << 8)
                    | ((decPerms[2] & 0xff) << 16) | ((decPerms[2] & 0xff) << 24);
            encryptMetadata = decPerms[8] == (byte)'T';
            return isOwnerPass;
        }
        catch (BadPasswordException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    private static boolean compareArray(byte[] a, byte[] b, int len) {
        for (int k = 0; k < len; ++k) {
            if (a[k] != b[k]) {
                return false;
            }
        }
        return true;
    }

	public static byte[] createDocumentId() {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
		long time = System.currentTimeMillis();
		long mem = Runtime.getRuntime().freeMemory();
		String s = time + "+" + mem + "+" + (seq++);
		return md5.digest(s.getBytes());
	}

	/**
	 */
	public void setupByUserPassword(byte[] documentID, byte userPassword[],
			byte ownerKey[], long permissions) {
		setupByUserPad(documentID, padPassword(userPassword), ownerKey,
				permissions);
	}

	/**
	 */
	private void setupByUserPad(byte[] documentID, byte userPad[],
			byte ownerKey[], long permissions) {
		setupGlobalEncryptionKey(documentID, userPad, ownerKey, permissions);
		setupUserKey();
	}

	/**
	 */
	public void setupByOwnerPassword(byte[] documentID, byte ownerPassword[],
			byte userKey[], byte ownerKey[], long permissions) {
		setupByOwnerPad(documentID, padPassword(ownerPassword), userKey,
				ownerKey, permissions);
	}

	private void setupByOwnerPad(byte[] documentID, byte ownerPad[],
			byte userKey[], byte ownerKey[], long permissions) {
		byte userPad[] = computeOwnerKey(ownerKey, ownerPad); // userPad will
																// be set in
																// this.ownerKey
		setupGlobalEncryptionKey(documentID, userPad, ownerKey, permissions); // step
																				// 3
		setupUserKey();
	}

	public void setKey(byte[] key) {
        this.key = key;
    }
    
    public void setupByEncryptionKey(byte[] key, int keylength) {
		mkey = new byte[keylength / 8];
		System.arraycopy(key, 0, mkey, 0, mkey.length);
	}

	public void setHashKey(int number, int generation) {
        if (revision == AES_256)
            return;
		md5.reset(); // added by ujihara
		extra[0] = (byte) number;
		extra[1] = (byte) (number >> 8);
		extra[2] = (byte) (number >> 16);
		extra[3] = (byte) generation;
		extra[4] = (byte) (generation >> 8);
		md5.update(mkey);
		md5.update(extra);
		if (revision == AES_128)
			md5.update(salt);
		key = md5.digest();
		keySize = mkey.length + 5;
		if (keySize > 16)
			keySize = 16;
	}

	public static PdfObject createInfoId(byte id[], boolean modified) throws IOException {
		ByteBuffer buf = new ByteBuffer(90);
		buf.append('[').append('<');
		if (id.length != 16)
			id = createDocumentId();
		for (int k = 0; k < 16; ++k)
			buf.appendHex(id[k]);
		buf.append('>').append('<');
		if (modified)
			id = createDocumentId();
		for (int k = 0; k < 16; ++k)
			buf.appendHex(id[k]);
		buf.append('>').append(']');
		buf.close();
		return new PdfLiteral(buf.toByteArray());
	}

	public PdfDictionary getEncryptionDictionary() {
		PdfDictionary dic = new PdfDictionary();
        
		if (publicKeyHandler.getRecipientsSize() > 0) {
			PdfArray recipients = null;

			dic.put(PdfName.FILTER, PdfName.PUBSEC);
			dic.put(PdfName.R, new PdfNumber(revision));

			try {
				recipients = publicKeyHandler.getEncodedRecipients();
			} catch (Exception f) {
				throw new ExceptionConverter(f);
			}

			if (revision == STANDARD_ENCRYPTION_40) {
				dic.put(PdfName.V, new PdfNumber(1));
				dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S4);
				dic.put(PdfName.RECIPIENTS, recipients);
			} else if (revision == STANDARD_ENCRYPTION_128 && encryptMetadata) {
				dic.put(PdfName.V, new PdfNumber(2));
				dic.put(PdfName.LENGTH, new PdfNumber(128));
				dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S4);
				dic.put(PdfName.RECIPIENTS, recipients);
			} else {
                if (revision == AES_256) {
                    dic.put(PdfName.R, new PdfNumber(AES_256));
                    dic.put(PdfName.V, new PdfNumber(5));
                }
                else {
                    dic.put(PdfName.R, new PdfNumber(AES_128));
                    dic.put(PdfName.V, new PdfNumber(4));
                }
				dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S5);

				PdfDictionary stdcf = new PdfDictionary();
				stdcf.put(PdfName.RECIPIENTS, recipients);
				if (!encryptMetadata)
					stdcf.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE);
				if (revision == AES_128) {
					stdcf.put(PdfName.CFM, PdfName.AESV2);
					stdcf.put(PdfName.LENGTH, new PdfNumber(128));
                }
                else if (revision == AES_256) {
					stdcf.put(PdfName.CFM, PdfName.AESV3);
                    stdcf.put(PdfName.LENGTH, new PdfNumber(256));
                }
				else
					stdcf.put(PdfName.CFM, PdfName.V2);
				PdfDictionary cf = new PdfDictionary();
				cf.put(PdfName.DEFAULTCRYPTFILTER, stdcf);
				dic.put(PdfName.CF, cf);
                if (embeddedFilesOnly) {
					dic.put(PdfName.EFF, PdfName.DEFAULTCRYPTFILTER);
					dic.put(PdfName.STRF, PdfName.IDENTITY);
					dic.put(PdfName.STMF, PdfName.IDENTITY);
				}
				else {
					dic.put(PdfName.STRF, PdfName.DEFAULTCRYPTFILTER);
					dic.put(PdfName.STMF, PdfName.DEFAULTCRYPTFILTER);
				}
			}

			MessageDigest md = null;
			byte[] encodedRecipient = null;

			try {
                if (revision == AES_256)
				    md = MessageDigest.getInstance("SHA-256");
                else
				    md = MessageDigest.getInstance("SHA-1");
				md.update(publicKeyHandler.getSeed());
				for (int i = 0; i < publicKeyHandler.getRecipientsSize(); i++) {
					encodedRecipient = publicKeyHandler.getEncodedRecipient(i);
					md.update(encodedRecipient);
				}
				if (!encryptMetadata)
					md.update(new byte[] { (byte) 255, (byte) 255, (byte) 255,
							(byte) 255 });
			} catch (Exception f) {
				throw new ExceptionConverter(f);
			}

			byte[] mdResult = md.digest();

            if (revision == AES_256)
                key = mdResult;
            else
                setupByEncryptionKey(mdResult, keyLength);
		} else {
			dic.put(PdfName.FILTER, PdfName.STANDARD);
			dic.put(PdfName.O, new PdfLiteral(StringUtils
					.escapeString(ownerKey)));
			dic.put(PdfName.U, new PdfLiteral(StringUtils
					.escapeString(userKey)));
			dic.put(PdfName.P, new PdfNumber(permissions));
			dic.put(PdfName.R, new PdfNumber(revision));

			if (revision == STANDARD_ENCRYPTION_40) {
				dic.put(PdfName.V, new PdfNumber(1));
			} else if (revision == STANDARD_ENCRYPTION_128 && encryptMetadata) {
				dic.put(PdfName.V, new PdfNumber(2));
				dic.put(PdfName.LENGTH, new PdfNumber(128));

			}
            else if (revision == AES_256) {
				if (!encryptMetadata)
					dic.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE);
                dic.put(PdfName.OE, new PdfLiteral(StringUtils
                        .escapeString(oeKey)));
                dic.put(PdfName.UE, new PdfLiteral(StringUtils
                        .escapeString(ueKey)));
                dic.put(PdfName.PERMS, new PdfLiteral(StringUtils
                        .escapeString(perms)));
				dic.put(PdfName.V, new PdfNumber(revision));
				dic.put(PdfName.LENGTH, new PdfNumber(256));
				PdfDictionary stdcf = new PdfDictionary();
				stdcf.put(PdfName.LENGTH, new PdfNumber(32));
				if (embeddedFilesOnly) {
					stdcf.put(PdfName.AUTHEVENT, PdfName.EFOPEN);
					dic.put(PdfName.EFF, PdfName.STDCF);
					dic.put(PdfName.STRF, PdfName.IDENTITY);
					dic.put(PdfName.STMF, PdfName.IDENTITY);
				}
				else {
					stdcf.put(PdfName.AUTHEVENT, PdfName.DOCOPEN);
					dic.put(PdfName.STRF, PdfName.STDCF);
					dic.put(PdfName.STMF, PdfName.STDCF);
				}
				stdcf.put(PdfName.CFM, PdfName.AESV3);
				PdfDictionary cf = new PdfDictionary();
				cf.put(PdfName.STDCF, stdcf);
				dic.put(PdfName.CF, cf);
            }
            else {
				if (!encryptMetadata)
					dic.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE);
				dic.put(PdfName.R, new PdfNumber(AES_128));
				dic.put(PdfName.V, new PdfNumber(4));
				dic.put(PdfName.LENGTH, new PdfNumber(128));
				PdfDictionary stdcf = new PdfDictionary();
				stdcf.put(PdfName.LENGTH, new PdfNumber(16));
				if (embeddedFilesOnly) {
					stdcf.put(PdfName.AUTHEVENT, PdfName.EFOPEN);
					dic.put(PdfName.EFF, PdfName.STDCF);
					dic.put(PdfName.STRF, PdfName.IDENTITY);
					dic.put(PdfName.STMF, PdfName.IDENTITY);
				}
				else {
					stdcf.put(PdfName.AUTHEVENT, PdfName.DOCOPEN);
					dic.put(PdfName.STRF, PdfName.STDCF);
					dic.put(PdfName.STMF, PdfName.STDCF);
				}
				if (revision == AES_128)
					stdcf.put(PdfName.CFM, PdfName.AESV2);
				else
					stdcf.put(PdfName.CFM, PdfName.V2);
				PdfDictionary cf = new PdfDictionary();
				cf.put(PdfName.STDCF, stdcf);
				dic.put(PdfName.CF, cf);
			}
		}

		return dic;
	}

	public PdfObject getFileID(boolean modified) throws IOException {
		return createInfoId(documentID, modified);
	}

	public OutputStreamEncryption getEncryptionStream(OutputStream os) {
		return new OutputStreamEncryption(os, key, 0, keySize, revision);
	}

	public int calculateStreamSize(int n) {
		if (revision == AES_128 || revision == AES_256)
			return (n & 0x7ffffff0) + 32;
		else
			return n;
	}

	public byte[] encryptByteArray(byte[] b) {
		try {
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			OutputStreamEncryption os2 = getEncryptionStream(ba);
			os2.write(b);
			os2.finish();
			return ba.toByteArray();
		} catch (IOException ex) {
			throw new ExceptionConverter(ex);
		}
	}

	public StandardDecryption getDecryptor() {
		return new StandardDecryption(key, 0, keySize, revision);
	}

	public byte[] decryptByteArray(byte[] b) {
		try {
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			StandardDecryption dec = getDecryptor();
			byte[] b2 = dec.update(b, 0, b.length);
			if (b2 != null)
				ba.write(b2);
			b2 = dec.finish();
			if (b2 != null)
				ba.write(b2);
			return ba.toByteArray();
		} catch (IOException ex) {
			throw new ExceptionConverter(ex);
		}
	}

	public void addRecipient(Certificate cert, int permission) {
		documentID = createDocumentId();
		publicKeyHandler.addRecipient(new PdfPublicKeyRecipient(cert,
				permission));
	}

	public byte[] computeUserPassword(byte[] ownerPassword) {
		byte[] userPad = computeOwnerKey(ownerKey, padPassword(ownerPassword));
		for (int i = 0; i < userPad.length; i++) {
			boolean match = true;
			for (int j = 0; j < userPad.length - i; j++) {
				if (userPad[i + j] != pad[j]) {
					match = false;
					break;
                }
			}
			if (!match) continue;
			byte[] userPassword = new byte[i];
			System.arraycopy(userPad, 0, userPassword, 0, i);
			return userPassword;
		}
		return userPad;
	}
}
