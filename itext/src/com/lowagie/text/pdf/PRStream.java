package com.lowagie.text.pdf;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
/**
 * a Literal
 */

class PRStream extends PdfStream {
    
    protected PRDictionary dictionary;
    protected PdfReader reader;
    protected int offset;
    protected int length;
    
    PRStream(PRDictionary dictionary, PdfReader reader, int offset)
    {
        this.dictionary = dictionary;
        this.reader = reader;
        this.offset = offset;
    }
    
    PRStream(PRDictionary dictionary, PdfReader reader, byte conts[])
    {
        this.dictionary = dictionary;
        this.reader = reader;
        this.offset = -1;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DeflaterOutputStream zip = new DeflaterOutputStream(stream);
            zip.write(conts);
            zip.close();
            bytes = stream.toByteArray();
        }
        catch(IOException ioe) {
            System.err.println("The PRStream was not compressed: " + ioe.getMessage());
        }
        setLength(bytes.length);
        dictionary.put(new PRName("Filter"), new PRName("FlateDecode"));
    }
    
    void setLength(int length) {
        this.length = length;
        dictionary.put(new PRName("Length"), new PRNumber(length));
    }
    
    PRDictionary getDictionary() {
        return dictionary;
    }
    
    void setDictionary(PRDictionary dic) {
        dictionary = dic;
    }
    
    int getOffset() {
        return offset;
    }
    
    int getLength() {
        return length;
    }
    
    public byte[] toPdf(PdfWriter writer) {
        dicBytes = dictionary.toPdf(writer);
        return null;
    }

    int getStreamLength(PdfWriter writer) {
        if (dicBytes == null)
            toPdf(writer);
        return length + dicBytes.length + SIZESTREAM;
    }
    
    void writeTo(OutputStream out, PdfWriter writer) throws IOException{
        if (dicBytes == null)
            toPdf(writer);
        out.write(dicBytes);
        out.write(STARTSTREAM);
        if (length > 0) {
            PdfEncryption crypto = writer.getEncryption();
            if (offset < 0) {
                if (crypto == null)
                    out.write(bytes);
                else {
                    crypto.prepareKey();
                    byte buf[] = new byte[length];
                    System.arraycopy(bytes, 0, buf, 0, length);
                    crypto.encryptRC4(buf);
                    out.write(buf);
                }
            }
            else {
                byte buf[] = new byte[Math.min(length, 4092)];
                RandomAccessFileOrArray file = writer.getReaderFile(reader);
                file.seek(offset);
                int size = length;
                if (crypto != null)
                    crypto.prepareKey();
                while (size > 0) {
                    int r = file.read(buf, 0, Math.min(size, buf.length));
                    size -= r;
                    if (crypto != null)
                        crypto.encryptRC4(buf, 0, r);
                    out.write(buf, 0, r);
                }
            }
        }
        out.write(ENDSTREAM);
    }
}