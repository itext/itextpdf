/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For license details please refer to http://xml.apache.org/fop
 */

package com.lowagie.text.pdf.hyphenation;

import com.lowagie.text.ExceptionConverter;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PatternInternalParser implements PatternConsumer {

    PatternConsumer consumer;

    public PatternInternalParser() {
    }

    public PatternInternalParser(PatternConsumer consumer) {
        this.consumer = consumer;
    }

    public void setConsumer(PatternConsumer consumer) {
        this.consumer = consumer;
    }
    
    protected String getHyphString(InputStream is) throws IOException{
        InputStreamReader isr = new InputStreamReader(is, "UTF8");
        char c[] = new char[4000];
        StringBuffer buf = new StringBuffer();
        while (true) {
            int n = isr.read(c);
            if (n < 0)
                break;
            buf.append(c, 0, n);
        }
        isr.close();
        return buf.toString();
    }

    public void parse(String filename) {
        InputStream is;
        try {
            is = new FileInputStream(filename);
        }
        catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        parse(is);
    }
    
    public void parse(InputStream is) {
        String hyphs;
        try {
            hyphs = getHyphString(is);
        }
        catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        parseString(hyphs);
    }

    public void parseString(String hyphs) {
        StringTokenizer tk = new StringTokenizer(hyphs);
        readClasses(tk);
        readExceptions(tk);
        readPatterns(tk);
    }

    protected void readClasses(StringTokenizer tk) {
        String token = "";
        while (tk.hasMoreTokens()) {
            token = tk.nextToken();
            if (token.equals("*"))
                break;
            consumer.addClass(token);
        }
    }

    protected void readExceptions(StringTokenizer tk) {
        String token = "";
        while (tk.hasMoreTokens()) {
            token = tk.nextToken();
            if (token.equals("*"))
                break;
            String word = token;
            ArrayList vec = new ArrayList();
            while (tk.hasMoreTokens()) {
                token = tk.nextToken();
                if (token.equals("{")) {
                    String t1 = tk.nextToken();
                    if (t1.equals("N"))
                        t1 = null;
                    String t2 = tk.nextToken();
                    if (t2.equals("N"))
                        t2 = null;
                    String t3 = tk.nextToken();
                    if (t3.equals("N"))
                        t3 = null;
                    Hyphen hy = new Hyphen(t2, t1, t3);
                    vec.add(hy);
                }
                else if (token.equals("#")) {
                    break;
                }
                else
                    vec.add(token);
            }
            consumer.addException(word, vec);
        }
    }
    
    protected void readPatterns(StringTokenizer tk) {
        String token = "";
        while (tk.hasMoreTokens()) {
            token = tk.nextToken();
            consumer.addPattern(token, tk.nextToken());
        }
    }
    
    // PatternConsumer implementation for testing purposes
    public void addClass(String c) {
        System.out.println("class: " + c);
    }

    public void addException(String w, ArrayList e) {
        System.out.println("exception: " + w + " : " + e.toString());
    }

    public void addPattern(String p, String v) {
        System.out.println("pattern: " + p + " : " + v);
    }

/*    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            PatternInternalParser pp = new PatternInternalParser();
            pp.setConsumer(pp);
            pp.parse(args[0]);
        }
    }*/

}
