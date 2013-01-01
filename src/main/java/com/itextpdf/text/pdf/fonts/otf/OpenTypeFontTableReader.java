/*
 * $Id: $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.fonts.otf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 *  
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public abstract class OpenTypeFontTableReader {
    
    protected final RandomAccessFileOrArray rf;
    protected final int tableLocation;
    
    public OpenTypeFontTableReader(String fontFilePath, int tableLocation) throws IOException { 
        this.rf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(fontFilePath));
        this.tableLocation = tableLocation;
    }
    
    /**
     * This is the starting point of the class. A sub-class must call this method to start getting call backs
     * to the {@link #readSubTable(int, int)} method.
     */
    protected final void readLookupListTable() throws IOException {
        int lookupListTableLocation = tableLocation + readHeader().lookupListOffset;
        
        rf.seek(lookupListTableLocation);
        int lookupCount = rf.readShort();
        System.out.println("lookupCount=" + lookupCount);

        List<Integer> lookupTableOffsets = new ArrayList<Integer>();

        for (int i = 0; i < lookupCount; i++) {
            int lookupTableOffset = rf.readShort();
            lookupTableOffsets.add(lookupTableOffset);
        }
        
        for (int lookupTableOffset : lookupTableOffsets) {
            readLookupTable(lookupListTableLocation + lookupTableOffset);
        }
    }
    
    protected abstract void readSubTable(int lookupType, int subTableLocation) throws IOException;
    
    private void readLookupTable(int lookupTableLocation) throws IOException {
        rf.seek(lookupTableLocation);
        int lookupType = rf.readShort();
        System.out.println("lookupType=" + lookupType);

        int lookupFlag = rf.readShort();
        System.out.println("lookupFlag=" + lookupFlag);
        int subTableCount = rf.readShort();
        System.out.println("subTableCount=" + subTableCount);

        List<Integer> subTableOffsets = new ArrayList<Integer>();

        for (int i = 0; i < subTableCount; i++) {
            int subTableOffset = rf.readShort();
            subTableOffsets.add(subTableOffset);
        }

        for (int subTableOffset : subTableOffsets) {
            System.out.println("subTableOffset=" + subTableOffset);
            readSubTable(lookupType, lookupTableLocation + subTableOffset);
        }
    }
    
    protected final List<Integer> readCoverageFormat(int coverageLocation) throws IOException {
        rf.seek(coverageLocation);
        int coverageFormat = rf.readShort();

        List<Integer> glyphIds;

        if (coverageFormat == 1) {
            int glyphCount = rf.readShort();

            glyphIds = new ArrayList<Integer>(glyphCount);

            for (int i = 0; i < glyphCount; i++) {
                int coverageGlyphId = rf.readShort();
                glyphIds.add(coverageGlyphId);
            }

        } else if (coverageFormat == 2) {

            int rangeCount = rf.readShort();

            glyphIds = new ArrayList<Integer>();

            for (int i = 0; i < rangeCount; i++) {
                readRangeRecord(glyphIds);
            }

        } else {
            throw new UnsupportedOperationException("Invalid coverage format: " + coverageFormat);
        }

        return Collections.unmodifiableList(glyphIds);
    }

    private void readRangeRecord(List<Integer> glyphIds) throws IOException {
        int startGlyphId = rf.readShort();
        int endGlyphId = rf.readShort();
        //skip the `startCoverageIndex` field
        rf.skipBytes(2);

        for (int glyphId = startGlyphId; glyphId <= endGlyphId; glyphId++) {
            glyphIds.add(glyphId);
        }

    }
    
    private TableHeader readHeader() throws IOException {
        rf.seek(tableLocation);
        // 32 bit signed
        int version = rf.readInt();
        // 16 bit unsigned
        int scriptListOffset = rf.readUnsignedShort();
        int featureListOffset = rf.readUnsignedShort();
        int lookupListOffset = rf.readUnsignedShort();

        System.out.println("version=" + version);
        System.out.println("scriptListOffset=" + scriptListOffset);
        System.out.println("featureListOffset=" + featureListOffset);
        System.out.println("lookupListOffset=" + lookupListOffset);
        
        TableHeader header = new TableHeader(version, scriptListOffset, featureListOffset, lookupListOffset);
        
        return header;
    }

}
