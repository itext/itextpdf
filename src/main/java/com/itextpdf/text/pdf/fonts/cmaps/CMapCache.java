/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.pdf.fonts.cmaps;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author psoares
 */
public class CMapCache {
    private static final HashMap<String,CMapUniCid> cacheUniCid = new HashMap<String,CMapUniCid>();
    private static final HashMap<String,CMapCidUni> cacheCidUni = new HashMap<String,CMapCidUni>();
    private static final HashMap<String,CMapCidByte> cacheCidByte = new HashMap<String,CMapCidByte>();
    private static final HashMap<String,CMapByteCid> cacheByteCid = new HashMap<String,CMapByteCid>();
    
    public static CMapUniCid getCachedCMapUniCid(String name) throws IOException {
        CMapUniCid cmap = null;
        synchronized (cacheUniCid) {
            cmap = cacheUniCid.get(name);
        }
        if (cmap == null) {
            cmap = new CMapUniCid();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheUniCid) {
                cacheUniCid.put(name, cmap);
            }
        }
        return cmap;
    }
    
    public static CMapCidUni getCachedCMapCidUni(String name) throws IOException {
        CMapCidUni cmap = null;
        synchronized (cacheCidUni) {
            cmap = cacheCidUni.get(name);
        }
        if (cmap == null) {
            cmap = new CMapCidUni();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheCidUni) {
                cacheCidUni.put(name, cmap);
            }
        }
        return cmap;
    }
    
    public static CMapCidByte getCachedCMapCidByte(String name) throws IOException {
        CMapCidByte cmap = null;
        synchronized (cacheCidByte) {
            cmap = cacheCidByte.get(name);
        }
        if (cmap == null) {
            cmap = new CMapCidByte();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheCidByte) {
                cacheCidByte.put(name, cmap);
            }
        }
        return cmap;
    }
    
    public static CMapByteCid getCachedCMapByteCid(String name) throws IOException {
        CMapByteCid cmap = null;
        synchronized (cacheByteCid) {
            cmap = cacheByteCid.get(name);
        }
        if (cmap == null) {
            cmap = new CMapByteCid();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheByteCid) {
                cacheByteCid.put(name, cmap);
            }
        }
        return cmap;
    }
}
