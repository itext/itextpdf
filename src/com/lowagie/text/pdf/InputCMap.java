/*
 * InputCMap.java
 *
 * Created on March 15, 2001, 2:22 PM
 */

package com.lowagie.text.pdf;

import java.io.*;
import java.util.*;

/**
 *
 * @author  psoares
 * @version 
 */
public class InputCMap {

    public TreeMap CMap;
    
    class T2 extends TrueType
    {
        public void go(String name) throws Exception
        {
            fileName = name;
            this.process(fileName);
        }
        
        public int[] getWidths()
        {
            return GlyphWidths;
        }
    }
    /** Creates new InputCMap */
    public InputCMap()
    {
    }
    
    public void process(String icmap, String font, String fileout) throws Exception
    {
        int idx = font.lastIndexOf('\\') + 1;
        String fontName = font.substring(idx, font.length() - 5);
        idx = icmap.lastIndexOf('\\') + 1;
        String mapName = icmap.substring(idx);
        CMap = new TreeMap();
        readCMap(icmap);
        T2 tt = new T2();
        tt.go(font);
        PrintWriter fout = new PrintWriter(new FileWriter(fileout));
        fout.println("static int " + fontName.replace('-', '_') + "_" + mapName.replace('-', '_')
            + "[] = {");
        int n = 0;
        for (Iterator i = CMap.keySet().iterator(); i.hasNext();) {
            Integer c = (Integer)i.next();
            Integer gl = (Integer)CMap.get(c);
            int w = tt.getGlyphWidth(gl.intValue());
            if (w != 1000) {
                fout.print(c + "," + w + ",");
                if (++n == 16) {
                    n = 0;
                    fout.println();
                }
            }
        }
        fout.println();
        fout.println();
        
        int ww[] = tt.getWidths();
        int count = 0;
        int last = -1;
        int mark = 1;
        int repmark = 1;
        fout.print("[");
        for (int k = 1; k < ww.length; ++k) {
            int tg = ww[k];
            if (last == tg) {
                if (count == 0) {
                    if (mark < k - 1) {
                        fout.print(mark + " [");
                        for (int j = mark; j < k - 1; ++j) {
                            fout.print(ww[j] + " ");
                        }
                        fout.print("]");
                    }
                    mark = k - 1;
                }
                ++count;
            }
            else {
                if (count != 0) {
                    if (ww[mark] != 1000)
                        fout.print(mark + " " + (mark + count) + " " + ww[mark] + " ");
                    count = 0;
                    mark = k;
                }
            }
            last = tg;
        }
        fout.print(mark + " [");
        for (int j = mark; j < ww.length; ++j) {
            fout.print(ww[j] + " ");
        }
        fout.print("]]");
        fout.println();
        fout.println();
        
        fout.close();
    }
    
    public void readCMap(String icmap) throws Exception
    {
        BufferedReader fin = new BufferedReader(new FileReader(icmap));
        String line;
        line = "";
        for (;;) {
            int nextLines = 0;
            while ((line = fin.readLine()) != null) {
                StringTokenizer tok = new StringTokenizer(line);
                if (tok.countTokens() < 2)
                    continue;
                String dummy = tok.nextToken();
                if (tok.nextToken().equals("begincidrange")) {
                    nextLines = Integer.parseInt(dummy);
                    break;
                }
            }
            if (line == null)
                break;
            while (nextLines > 0 && (line = fin.readLine()) != null) {
                StringTokenizer tok = new StringTokenizer(line);
                if (tok.countTokens() < 3)
                    continue;
                --nextLines;
                int vstart = decint(tok.nextToken());
                int vend = decint(tok.nextToken());
                int glyph = decint(tok.nextToken());
                for (; vstart <= vend; ++vstart) {
                    CMap.put(new Integer(vstart), new Integer(glyph++));
                }
            }
            if (line == null)
                break;
        }
    }
    
    public int decint(String s)
    {
        if (s.startsWith("<"))
            return Integer.parseInt(s.substring(1, 5), 16);
        else
            return Integer.parseInt(s);
    }
}
