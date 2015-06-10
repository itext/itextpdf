package com.itextpdf.text.io;

import com.itextpdf.text.pdf.PdfObject;

import java.io.*;

public class TempFileCache {

    public class ObjectPosition {
        ObjectPosition(long offset, int length) {
            this.offset = offset;
            this.length = length;
        }

        long offset;
        int length;
    }

    private String filename;
    private RandomAccessFile cache;
    private ByteArrayOutputStream baos;

    private byte[] buf;


    public TempFileCache(String filename) throws IOException {
        this.filename = filename;
        File f = new File(filename);
        File parent = f.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        cache = new RandomAccessFile(filename, "rw");

        baos = new ByteArrayOutputStream();
    }

    public ObjectPosition put(PdfObject obj) throws IOException {
        baos.reset();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        long offset, size;
        offset = cache.length();

        oos.writeObject(obj);
        cache.seek(offset);
        cache.write(baos.toByteArray());

        size = cache.length() - offset;

        return new ObjectPosition(offset, (int)size);
    }

    public PdfObject get(ObjectPosition pos) throws IOException, ClassNotFoundException {
        PdfObject obj = null;
        if (pos != null) {
            cache.seek(pos.offset);
            cache.read(getBuffer(pos.length), 0, pos.length);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(getBuffer(pos.length)));
            try {
                obj = (PdfObject) ois.readObject();
            } finally {
                ois.close();
            }
        }

        return obj;
    }

    private byte[] getBuffer(int size) {
        if (buf == null || buf.length < size) {
            buf = new byte[size];
        }

        return buf;
    }

    public void close() throws IOException {
        cache.close();
        cache = null;

        new File(filename).delete();
    }


}
