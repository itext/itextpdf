package com.lowagie.text.pdf;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class PdfEncryptionStream extends FilterOutputStream {
    
    protected PdfEncryption enc;
    private byte buf[] = new byte[1];
    
    public PdfEncryptionStream(OutputStream out, PdfEncryption enc) {
        super(out);
        this.enc = enc;
    }
    
    public void write(byte[] b, int off, int len) throws IOException {
        if ((off | len | (b.length - (len + off)) | (off + len)) < 0)
            throw new IndexOutOfBoundsException();
        enc.encryptRC4(b, off, len);
        out.write(b, off, len);
    }
    
    public void close() throws IOException {
    }
    
    public void write(int b) throws IOException {
        buf[0] = (byte)b;
        write(buf);
    }
    
    public void flush() throws IOException {
    }
    
}
