/*
 * EnumerateTTC.java
 *
 * Created on December 26, 2001, 9:18 PM
 */

package com.lowagie.text.pdf;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import com.lowagie.text.DocumentException;
/**
 *
 * @author  Administrator
 * @version 
 */
class EnumerateTTC extends TrueTypeFont{

    protected String[] names;

    EnumerateTTC(String ttcFile) throws DocumentException, IOException {
        fileName = ttcFile;
        rf = new RandomAccessFileOrArray(ttcFile);
        findNames();
    }

    EnumerateTTC(byte ttcArray[]) throws DocumentException, IOException {
        fileName = "Byte array TTC";
        rf = new RandomAccessFileOrArray(ttcArray);
        findNames();
    }
    
    void findNames() throws DocumentException, IOException {
        tables = new HashMap();
        
        try {
            String mainTag = readStandardString(4);
            if (!mainTag.equals("ttcf"))
                throw new DocumentException(fileName + " is not a valid TTC file.");
            rf.skipBytes(4);
            int dirCount = rf.readInt();
            names = new String[dirCount];
            int dirPos = rf.getFilePointer();
            for (int dirIdx = 0; dirIdx < dirCount; ++dirIdx) {
                tables.clear();
                rf.seek(dirPos);
                rf.skipBytes(dirIdx * 4);
                directoryOffset = rf.readInt();
                rf.seek(directoryOffset);
                if (rf.readInt() != 0x00010000)
                    throw new DocumentException(fileName + " is not a valid TTF file.");
                int num_tables = rf.readUnsignedShort();
                rf.skipBytes(6);
                for (int k = 0; k < num_tables; ++k) {
                    String tag = readStandardString(4);
                    rf.skipBytes(4);
                    int table_location[] = new int[2];
                    table_location[0] = rf.readInt();
                    table_location[1] = rf.readInt();
                    tables.put(tag, table_location);
                }
                names[dirIdx] = getBaseFont();
            }
        }
        finally {
            if (rf != null)
                rf.close();
        }
    }
    
    String[] getNames() {
        return names;
    }

}
