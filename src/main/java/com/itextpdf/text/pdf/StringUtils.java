package com.itextpdf.text.pdf;

import com.itextpdf.text.DocWriter;

public class StringUtils {

    final static private byte[] r = DocWriter.getISOBytes("\\r");
    final static private byte[] n = DocWriter.getISOBytes("\\n");
    final static private byte[] t = DocWriter.getISOBytes("\\t");
    final static private byte[] b = DocWriter.getISOBytes("\\b");
    final static private byte[] f = DocWriter.getISOBytes("\\f");

    private StringUtils() {

    }

    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param bytes the <CODE>byte</CODE> array to escape
     * @return an escaped <CODE>byte</CODE> array
     */
    public static byte[] escapeString(final byte bytes[]) {
        ByteBuffer content = new ByteBuffer();
        escapeString(bytes, content);
        return content.toByteArray();
    }

    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param bytes the <CODE>byte</CODE> array to escape
     * @param content the content
     */
    public static void escapeString(final byte bytes[], final ByteBuffer content) {
        content.append_i('(');
        for (int k = 0; k < bytes.length; ++k) {
            byte c = bytes[k];
            switch (c) {
                case '\r':
                    content.append(r);
                    break;
                case '\n':
                    content.append(n);
                    break;
                case '\t':
                    content.append(t);
                    break;
                case '\b':
                    content.append(b);
                    break;
                case '\f':
                    content.append(f);
                    break;
                case '(':
                case ')':
                case '\\':
                    content.append_i('\\').append_i(c);
                    break;
                default:
                    content.append_i(c);
            }
        }
        content.append_i(')');
    }

}
