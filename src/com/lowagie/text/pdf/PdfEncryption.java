package com.lowagie.text.pdf;

import java.security.MessageDigest;
import com.lowagie.text.DocWriter;

/**
 *
 * @author  psoares
 */
 
public final class PdfEncryption {

    static final byte pad[] = {
        (byte)0x28, (byte)0xBF, (byte)0x4E, (byte)0x5E, (byte)0x4E, (byte)0x75,
        (byte)0x8A, (byte)0x41, (byte)0x64, (byte)0x00, (byte)0x4E, (byte)0x56,
        (byte)0xFF, (byte)0xFA, (byte)0x01, (byte)0x08, (byte)0x2E, (byte)0x2E,
        (byte)0x00, (byte)0xB6, (byte)0xD0, (byte)0x68, (byte)0x3E, (byte)0x80,
        (byte)0x2F, (byte)0x0C, (byte)0xA9, (byte)0xFE, (byte)0x64, (byte)0x53,
        (byte)0x69, (byte)0x7A};
        
    byte state[] = new byte[256];
    int x;
    int y;
    /** The encryption key for a particular object/generation */
    byte key[];
    /** The encryption key length for a particular object/generation */
    int keySize;
    /** The global encryption key */
    byte mkey[];
    /** Work area to prepare the object/generation bytes */
    byte extra[] = new byte[5];
    /** The message digest algorithm MD5 */
    MessageDigest md5;
    /** The encryption key for the owner */
    byte ownerKey[] = new byte[32];
    /** The encryption key for the user */
    byte userKey[] = new byte[32];
    int permissions;
    byte documentID[];
    
    public PdfEncryption() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
        }
    }
    
    public void setupAllKeys(byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) {
        if (strength128Bits)
            permissions |= 0xfffff0c0;
        else
            permissions |= 0xffffffc0;
        permissions &= 0xfffffffc;
        this.permissions = permissions;
        long time;
        long mem;
        byte userPad[] = new byte[32];
        if (userPassword == null)
            System.arraycopy(pad, 0, userPad, 0, 32);
        else {
            System.arraycopy(userPassword, 0, userPad, 0, Math.min(userPassword.length, 32));
            if (userPassword.length < 32)
                System.arraycopy(pad, 0, userPad, userPassword.length, 32 - userPassword.length);
        }
        byte ownerPad[] = new byte[32];
        if (ownerPassword == null || ownerPassword.length == 0) {
            System.arraycopy(pad, 0, ownerPad, 0, 32);
            time = System.currentTimeMillis();
            mem = Runtime.getRuntime().freeMemory();
            String s = time + "-" + mem;
            System.arraycopy(md5.digest(s.getBytes()), 0, ownerPad, 0, 16);
        }
        else {
            System.arraycopy(ownerPassword, 0, ownerPad, 0, Math.min(ownerPassword.length, 32));
            if (ownerPassword.length < 32)
                System.arraycopy(pad, 0, ownerPad, ownerPassword.length, 32 - ownerPassword.length);
        }
        mkey = new byte[strength128Bits ? 16 : 5];
        byte digest[] = md5.digest(ownerPad);
        if (strength128Bits) {
            for (int k = 0; k < 50; ++k)
                digest = md5.digest(digest);
            System.arraycopy(userPad, 0, ownerKey, 0, 32);
            for (int i = 0; i < 20; ++i){
                for (int j = 0; j < mkey.length ; ++j)
                    mkey[j] = (byte)(digest[j] ^ i);
                prepareRC4Key(mkey);
                encryptRC4(ownerKey);
            }
        }
        else {
            prepareRC4Key(digest, 0, mkey.length);
            encryptRC4(userPad, ownerKey);
        }
        time = System.currentTimeMillis();
        mem = Runtime.getRuntime().freeMemory();
        String s = time + "+" + mem;
        documentID = md5.digest(s.getBytes());
        md5.update(userPad);
        md5.update(ownerKey);
        extra[0] = (byte)permissions;
        extra[1] = (byte)(permissions >> 8);
        extra[2] = (byte)(permissions >> 16);
        extra[3] = (byte)(permissions >> 24);
        md5.update(extra, 0, 4);
        digest = md5.digest(documentID);
        if (strength128Bits) {
            for (int k = 0; k < 50; ++k)
                digest = md5.digest(digest);
        }
        System.arraycopy(digest, 0, mkey, 0, mkey.length);
        
        // Compute the user key
        
        if (strength128Bits) {
            md5.update(pad);
            digest = md5.digest(documentID);
            System.arraycopy(digest, 0, userKey, 0, 16);
            for (int k = 16; k < 32; ++k)
                userKey[k] = 0;
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < mkey.length; ++j)
                    digest[j] = (byte)(mkey[j] ^ i);
                prepareRC4Key(digest, 0, mkey.length);
                encryptRC4(userKey, 0, 16);
            }
        }
        else {
            prepareRC4Key(mkey);
            encryptRC4(pad, userKey);
        }
    }
    
    public void prepareKey() {
        prepareRC4Key(key, 0, keySize);
    }
    
    public void setHashKey(int number, int generation) {
        extra[0] = (byte)number;
        extra[1] = (byte)(number >> 8);
        extra[2] = (byte)(number >> 16);
        extra[3] = (byte)generation;
        extra[4] = (byte)(generation >> 8);
        md5.update(mkey);
        key = md5.digest(extra);
        keySize = mkey.length + 5;
        if (keySize > 16)
            keySize = 16;
    }
    
    public PdfLiteral getFileID() {
        ByteBuffer b = new ByteBuffer();
        b.append('[');
        b.append(PdfContentByte.escapeString(documentID));
        b.append(PdfContentByte.escapeString(documentID));
        b.append(']');
        return new PdfLiteral(b.toByteArray());
    }
    
    public PdfDictionary getEncryptionDictionary() {
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.FILTER, PdfName.STANDARD);
        dic.put(PdfName.O, new PdfLiteral(PdfContentByte.escapeString(ownerKey)));
        dic.put(PdfName.U, new PdfLiteral(PdfContentByte.escapeString(userKey)));
        dic.put(PdfName.P, new PdfNumber(permissions));
        if (mkey.length > 5) {
            dic.put(PdfName.V, new PdfNumber(2));
            dic.put(PdfName.R, new PdfNumber(3));
            dic.put(PdfName.LENGTH, new PdfNumber(128));
        }
        else {
            dic.put(PdfName.V, new PdfNumber(1));
            dic.put(PdfName.R, new PdfNumber(2));
        }
        return dic;
    }
    
    public void prepareRC4Key(byte key[]) {
        prepareRC4Key(key, 0, key.length);
    }
    
    public void prepareRC4Key(byte key[], int off, int len) {
        int index1 = 0;
        int index2 = 0;
        for (int k = 0; k < 256; ++k)
            state[k] = (byte)k;
        x = 0;
        y = 0;
        byte tmp;
        for (int k = 0; k < 256; ++k) {
            index2 = (key[index1 + off] + state[k] + index2) & 255;
            tmp = state[k];
            state[k] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % len;
        }
    }
    
    public void encryptRC4(byte dataIn[], int off, int len, byte dataOut[]) {
        int length = len + off;
        byte tmp;
        for (int k = off; k < length; ++k) {
            x = (x + 1) & 255;
            y = (state[x] + y) & 255;
            tmp = state[x];
            state[x] = state[y];
            state[y] = tmp;
            dataOut[k] = (byte)(dataIn[k] ^ state[(state[x] + state[y]) & 255]);
        }
    }

    public void encryptRC4(byte data[], int off, int len) {
        encryptRC4(data, off, len, data);
    }

    public void encryptRC4(byte dataIn[], byte dataOut[]) {
        encryptRC4(dataIn, 0, dataIn.length, dataOut);
    }
    
    public void encryptRC4(byte data[]) {
        encryptRC4(data, 0, data.length, data);
    }
}
