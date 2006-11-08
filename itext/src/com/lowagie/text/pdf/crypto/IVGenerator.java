/*
 * IVGenerator.java
 *
 * Created on November 7, 2006, 3:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.lowagie.text.pdf.crypto;

/**
 *
 * @author psoares
 */
public class IVGenerator {
    
    private static RC4Encryption rc4;
    
    static {
        rc4 = new RC4Encryption();
        long time = System.currentTimeMillis();
        long mem = Runtime.getRuntime().freeMemory();
        String s = time + "+" + mem;
        rc4.prepareRC4Key(s.getBytes());
    }
    
    /** Creates a new instance of IVGenerator */
    private IVGenerator() {
    }
    
    public static byte[] getIV() {
        byte[] b = new byte[16];
        synchronized (rc4) {
            rc4.encryptRC4(b);
        }
        return b;
    }
}