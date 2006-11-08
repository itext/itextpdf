/*
 * AESCipher.java
 *
 * Created on November 7, 2006, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.lowagie.text.pdf.crypto;

import com.lowagie.text.ExceptionConverter;

/**
 *
 * @author psoares
 */
public class AESCipher {
    private PaddedBufferedBlockCipher bp;
    
    /** Creates a new instance of AESCipher */
    public AESCipher(boolean forEncryption, byte[] key, byte[] iv) {
        BlockCipher aes = new AESFastEngine();
        BlockCipher cbc = new CBCBlockCipher(aes);
        bp = new PaddedBufferedBlockCipher(cbc);
        KeyParameter kp = new KeyParameter(key);
        ParametersWithIV piv = new ParametersWithIV(kp, iv);
        bp.init(forEncryption, piv);
    }
    
    public byte[] update(byte[] inp, int inpOff, int inpLen) {
        int neededLen = bp.getUpdateOutputSize(inpLen);
        byte[] outp = null;
        if (neededLen > 0)
            outp = new byte[neededLen];
        else
            neededLen = 0;
        bp.processBytes(inp, inpOff, inpLen, outp, 0);
        return outp;
    }
    
    public byte[] doFinal() {
        int neededLen = bp.getOutputSize(0);
        byte[] outp = null;
        if (neededLen > 0)
            outp = new byte[neededLen];
        else
            neededLen = 0;
        int n = 0;
        try {
            n = bp.doFinal(outp, 0);
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
        if (n != outp.length) {
            byte[] outp2 = new byte[n];
            System.arraycopy(outp, 0, outp2, 0, n);
            return outp2;
        }
        else
            return outp;
    }
    
}
