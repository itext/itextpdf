package com.lowagie.text.pdf;

import java.io.*;
/**
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
class PRTokeniser {
    
    static final int TK_NUMBER = 1;
    static final int TK_STRING = 2;
    static final int TK_NAME = 3;
    static final int TK_COMMENT = 4;
    static final int TK_START_ARRAY = 5;
    static final int TK_END_ARRAY = 6;
    static final int TK_START_DIC = 7;
    static final int TK_END_DIC = 8;
    static final int TK_REF = 9;
    static final int TK_OTHER = 10;
    
    static final String EMPTY = "";

    
    RandomAccessFileOrArray file;
    int type;
    String stringValue;
    int reference;
    int generation;

    PRTokeniser(String filename) throws IOException {
        file = new RandomAccessFileOrArray(filename);
    }

    PRTokeniser(byte pdfIn[]) {
        file = new RandomAccessFileOrArray(pdfIn);
    }
    
    void seek(int pos) throws IOException {
        file.seek(pos);
    }
    
    int getFilePointer() throws IOException {
        return file.getFilePointer();
    }

    void close() throws IOException {
        file.close();
    }
    
    int length() throws IOException {
        return file.length();
    }

    int read() throws IOException {
        return file.read();
    }
    
    RandomAccessFileOrArray getSafeFile() {
        return new RandomAccessFileOrArray(file);
    }
    
    String readString(int size) throws IOException {
        StringBuffer buf = new StringBuffer();
        int ch;
        while ((size--) > 0) {
            ch = file.read();
            if (ch == -1)
                break;
            buf.append((char)ch);
        }
        return buf.toString();
    }

    static final boolean isWhitespace(int ch) {
        return (ch == 0 || ch == 9 || ch == 10 || ch == 12 || ch == 13 || ch == 32);
    }
    
    static final boolean isDelimiter(int ch) {
        return (ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '[' || ch == ']' || ch == '/' || ch == '%');
    }

    int getTokenType() {
        return type;
    }
    
    String getStringValue() {
        return stringValue;
    }
    
    int getReference() {
        return reference;
    }
    
    int getGeneration() {
        return generation;
    }
    
    void backOnePosition() throws IOException {
        file.seek(file.getFilePointer() - 1);
    }
    
    void throwError(String error) throws IOException {
        throw new IOException(error + " at file pointer " + file.getFilePointer());
    }
    
    void checkPdfHeader() throws IOException {
        String str = readString(7);
        if (!str.equals("%PDF-1."))
            throw new IOException("PDF header signature not found.");
    }
    
    int getStartxref() throws IOException {
        int size = Math.min(1024, file.length());
        int pos = file.length() - size;
        file.seek(pos);
        String str = readString(1024);
        int idx = str.lastIndexOf("startxref");
        if (idx < 0)
            throw new IOException("PDF startxref not found.");
        return pos + idx;
    }

    static int getHex(int v) {
        if (v >= '0' && v <= '9')
            return v - '0';
        if (v >= 'A' && v <= 'F')
            return v - 'A' + 10;
        if (v >= 'a' && v <= 'f')
            return v - 'a' + 10;
        return -1;
    }
    
    void nextValidToken() throws IOException {
        int level = 0;
        String n1 = null;
        String n2 = null;
        int ptr = 0;
        while (nextToken()) {
            if (type == TK_COMMENT)
                continue;
            switch (level) {
                case 0:
                {
                    if (type != TK_NUMBER)
                        return;
                    ptr = file.getFilePointer();
                    n1 = stringValue;
                    ++level;
                    break;
                }
                case 1:
                {
                    if (type != TK_NUMBER) {
                        file.seek(ptr);
                        type = TK_NUMBER;
                        stringValue = n1;
                        return;
                    }
                    n2 = stringValue;
                    ++level;
                    break;
                }
                default:
                {
                    if (type != TK_OTHER || !stringValue.equals("R")) {
                        file.seek(ptr);
                        type = TK_NUMBER;
                        stringValue = n1;
                        return;
                    }
                    type = TK_REF;
                    reference = Integer.valueOf(n1).intValue();
                    generation = Integer.valueOf(n2).intValue();
                    return;
                }
            }
        }
        throwError("Unexpected end of file");
    }
    
    boolean nextToken() throws IOException {
        StringBuffer outBuf = null;
        stringValue = EMPTY;
        int ch = 0;
        do {
            ch = file.read();
        } while (ch != -1 && isWhitespace(ch));
        if (ch == -1)
            return false;
        switch (ch) {
            case '[':
                type = TK_START_ARRAY;
                break;
            case ']':
                type = TK_END_ARRAY;
                break;
            case '/':
            {
                outBuf = new StringBuffer();
                type = TK_NAME;
                while (true) {
                    ch = file.read();
                    if (ch == -1 || isDelimiter(ch) || isWhitespace(ch))
                        break;
                    if (ch == '#') {
                        ch = (getHex(file.read()) << 4) + getHex(file.read());
                    }
                    outBuf.append((char)ch);
                }
                backOnePosition();
                break;
            }
            case '>':
                ch = file.read();
                if (ch != '>')
                    throwError("'>' not expected");
                type = TK_END_DIC;
                break;
            case '<':
            {
                int v1 = file.read();
                if (v1 == '<') {
                    type = TK_START_DIC;
                    break;
                }
                outBuf = new StringBuffer();
                type = TK_STRING;
                int v2 = 0;
                while (true) {
                    if (v1 == '>')
                        break;
                    v1 = getHex(v1);
                    if (v1 < 0)
                        break;
                    v2 = file.read();
                    if (v2 == '>') {
                        ch = v1 << 4;
                        outBuf.append((char)ch);
                        break;
                    }
                    v2 = getHex(v2);
                    if (v2 < 0)
                        break;
                    ch = (v1 << 4) + v2;
                    outBuf.append((char)ch);
                    v1 = file.read();
                }
                if (v1 < 0 || v2 < 0)
                    throwError("Error reading string");
                break;
            }
            case '%':
                type = TK_COMMENT;
                do {
                    ch = file.read();
                } while (ch != -1 && ch != 'r' && ch != 'n');
                break;
            case '(':
            {
                outBuf = new StringBuffer();
                type = TK_STRING;
                int nesting = 0;
                while (true) {
                    ch = file.read();
                    if (ch == -1)
                        break;
                    if (ch == '(') {
                        ++nesting;
                    }
                    else if (ch == ')') {
                        --nesting;
                    }
                    else if (ch == '\\') {
                        boolean lineBreak = false;
                        ch = file.read();
                        switch (ch) {
                            case 'n':
                                ch = '\n';
                                break;
                            case 'r':
                                ch = '\r';
                                break;
                            case 't':
                                ch = '\t';
                                break;
                            case 'b':
                                ch = '\b';
                                break;
                            case 'f':
                                ch = '\f';
                                break;
                            case '(':
                            case ')':
                            case '\\':
                                break;
                            case '\r':
                                lineBreak = true;
                                ch = file.read();
                                if (ch != '\n')
                                    backOnePosition();
                                break;
                            case '\n':
                                lineBreak = true;
                                break;
                            default:
                            {
                                if (ch < '0' || ch > '7') {
                                    break;
                                }
                                int octal = ch - '0';
                                ch = file.read();
                                if (ch < '0' || ch > '7') {
                                    ch = octal;
                                    backOnePosition();
                                    break;
                                }
                                octal = (octal << 3) + ch - '0';
                                ch = file.read();
                                if (ch < '0' || ch > '7') {
                                    ch = octal;
                                    backOnePosition();
                                    break;
                                }
                                octal = (octal << 3) + ch - '0';
                                ch = octal & 0xff;
                                break;
                            }
                        }
                        if (lineBreak)
                            continue;
                        if (ch < 0)
                            break;
                    }
                    else if (ch == '\r') {
                        ch = file.read();
                        if (ch < 0)
                            break;
                        if (ch != '\n') {
                            ch = '\n';
                            backOnePosition();
                        }
                    }
                    if (nesting == -1)
                        break;
                    outBuf.append((char)ch);
                }
                if (ch == -1)
                    throwError("Error reading string");
                break;
            }
            default:
            {
                outBuf = new StringBuffer();
                if (ch == '.' || (ch >= '0' && ch <= '9')) {
                    type = TK_NUMBER;
                    do {
                        outBuf.append((char)ch);
                        ch = file.read();
                    } while (ch != -1 && ((ch >= '0' && ch <= '9') || ch == '.'));
                }
                else {
                    type = TK_OTHER;
                    do {
                        outBuf.append((char)ch);
                        ch = file.read();
                    } while (ch != -1 && !isDelimiter(ch) && !isWhitespace(ch));
                }
                backOnePosition();
                break;
            }
        }
        if (outBuf != null)
            stringValue = outBuf.toString();
        return true;
    }
    
    int intValue() {
        return Integer.valueOf(stringValue).intValue();
    }
}
